package com.oceanbase.odc.service.sqlcheck;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import org.apache.commons.collections4.CollectionUtils;
import org.apache.commons.lang3.StringUtils;
import org.junit.Before;
import org.junit.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import com.oceanbase.odc.core.authority.util.SkipAuthorize;
import com.oceanbase.odc.core.datasource.SingleConnectionDataSource;
import com.oceanbase.odc.core.session.ConnectionConfig;
import com.oceanbase.odc.core.session.ConnectionSession;
import com.oceanbase.odc.core.session.ConnectionSessionConstants;
import com.oceanbase.odc.core.sql.split.OffsetString;
import com.oceanbase.odc.service.collaboration.environment.Environment;
import com.oceanbase.odc.service.collaboration.environment.EnvironmentService;
import com.oceanbase.odc.service.connection.model.ConnectionConfig.DialectType;
import com.oceanbase.odc.service.regulation.ruleset.Rule;
import com.oceanbase.odc.service.regulation.ruleset.RuleMetadata;
import com.oceanbase.odc.service.regulation.ruleset.RuleService;
import com.oceanbase.odc.service.regulation.ruleset.model.QueryRuleMetadataParams;
import com.oceanbase.odc.service.regulation.ruleset.model.Rule.RuleViolation;
import com.oceanbase.odc.service.regulation.ruleset.model.RuleType;
import com.oceanbase.odc.service.session.factory.OBConsoleDataSourceFactory;

@SuppressWarnings("unchecked")
public class SqlCheckServiceTest {

    @Mock
    private RuleService ruleService;

    @Mock
    private EnvironmentService environmentService;

    @Mock
    private SingleConnectionDataSource singleConnectionDataSource;

    @Mock
    private ConnectionSession connectionSession;

    @Mock
    private JdbcTemplate jdbcTemplate;

    @InjectMocks
    private SqlCheckService sqlCheckService;

    @Before
    public void setup() {
        MockitoAnnotations.initMocks(this);

        when(environmentService.detail(1L)).thenReturn(mock(Environment.class));

        ConnectionConfig connectionConfig = new ConnectionConfig();
        connectionConfig.setDialectType(DialectType.MYSQL);
        when(singleConnectionDataSource.getDataSource()).thenReturn(connectionConfig);

        when(jdbcTemplate.queryForList("SELECT * FROM users", String[].class)).thenReturn(new Object[0]);

        when(ruleService.list(1L, QueryRuleMetadataParams.builder().build())).thenReturn( Arrays.asList(
                new Rule() {{
                    setId(1L);
                    setLevel(1);
                    setEnabled(true);
                    setMetadata(new RuleMetadata() {{
                        setName("Rule1");
                        setType(RuleType.SQL_CHECK);
                    }});
                }},
                new Rule() {{
                    setId(2L);
                    setLevel(2);
                    setEnabled(true);
                    setMetadata(new RuleMetadata() {{
                        setName("Rule2");
                        setType(RuleType.SQL_CHECK);
                    }});
                }}
              ));
    }

    @Test
    public void testCheck() {
        // Given
        String scriptContent = "SELECT * FROM users WHERE age > 18";
        CheckResult expectedResult = new CheckResult() {{
            setId(1L);
            setCheckResult("PASS");
            setViolations(Collections.emptyList());
            setRuleName("Rule1");
        }};

        ConnectionSession session = mock(ConnectionSession.class);
        SqlCheckReq req = new SqlCheckReq() {{
            setScriptContent(scriptContent);
        }};
        when(connectionSession.getDialectType()).thenReturn(DialectType.MYSQL);
        when(connectionSession.getRuleSetId()).thenReturn(1L);

        // When
        List<CheckResult> result = sqlCheckService.check(session, req);

        // Then
        assertEquals(1, result.size());
        assertEquals(expectedResult, result.get(0));
    }

    @Test
    public void testCheckWithViolation() {
        // Given
        String scriptContent = "SELECT * FROM users WHERE age > 18 AND password = 'password'";
        List<CheckViolation> violations = Arrays.asList(
                new CheckViolation() {{
                    setLineNumber(1);
                    setType(RuleType.SQL_INJECTION);
                    setViolationMessage("SQL Injection detected.");
                }},
                new CheckViolation() {{
                    setLineNumber(1);
                    setType(RuleType.SQL_CHECK);
                    setViolationMessage("Check Rule1 violation.");
                }}
        );
        CheckResult expectedResult = new CheckResult() {{
            setId(1L);
            setCheckResult("FAIL");
            setViolations(violations);
            setRuleName("Rule1");
        }};

        ConnectionSession session = mock(ConnectionSession.class);
        SqlCheckReq req = new SqlCheckReq() {{
            setScriptContent(scriptContent);
        }};
        when(connectionSession.getDialectType()).thenReturn(DialectType.MYSQL);
        when(connectionSession.getRuleSetId()).thenReturn(1L);

        when(ruleService.list(1L, QueryRuleMetadataParams.builder().build())).thenReturn( Arrays.asList(
                new Rule() {{
                    setId(1L);
                    setLevel(1);
                    setEnabled(true);
                    setMetadata(new RuleMetadata() {{
                        setName("Rule1");
                        setType(RuleType.SQL_CHECK);
                    }});
                }},
                new Rule() {{
                    setId(2L);
                    setLevel(2);
                    setEnabled(true);
                    setMetadata(new RuleMetadata() {{
                        setName("Rule2");
                        setType(RuleType.SQL_CHECK);
                    }});
                }}
        ));

        when(ruleService.list(1L, QueryRuleMetadataParams.builder().build())).thenAnswer(invocationOnMock -> {
            List<Rule> rules = invocationOnMock.getArgument(0);
            List<SqlCheckRule> sqlCheckRules = sqlCheckService.getRules(rules, connectionSession.getDialectType(), jdbcTemplate);
            Rule rule1 = sqlCheckRules.get(0);
            Rule rule2 = sqlCheckRules.get(1);
            SqlCheckRuleFactory sqlCheckRuleFactory1 = mock(SqlCheckRuleFactory.class);
            SqlCheckRuleFactory sqlCheckRuleFactory2 = mock(SqlCheckRuleFactory.class);
            when(sqlCheckRuleFactory1.createCheckRule(rule1)).thenReturn(new SqlCheckRule() {{
                setViolationMessage("Check Rule1 violation.");
            }});
            when(sqlCheckRuleFactory2.createCheckRule(rule2)).thenReturn(new SqlCheckRule() {{
                setViolationMessage("SQL Injection detected.");
            }});
            when(ruleService.list(1L, QueryRuleMetadataParams.builder().build())).thenReturn(Arrays.asList(rule1, rule2));
            return Arrays.asList(sqlCheckRuleFactory1, sqlCheckRuleFactory2);
        });

        // When
        List<CheckResult> result = sqlCheckService.check(session, req);

        // Then
        assertEquals(1, result.size());
        assertEquals(expectedResult, result.get(0));
        assertEquals(2, result.get(0).getViolations().size());
    }

    @Test
    public void testCheckWithEmptyRules() {
        // Given
        String scriptContent = "SELECT * FROM users WHERE age > 18";
        ConnectionSession session = mock(ConnectionSession.class);
        SqlCheckReq req = new SqlCheckReq() {{
            setScriptContent(scriptContent);
        }};
        when(connectionSession.getDialectType()).thenReturn(DialectType.MYSQL);
        when(connectionSession.getRuleSetId()).thenReturn(1L);

        // When
        List<CheckResult> result = sqlCheckService.check(session, req);

        // Then
        assertEquals(0, result.size());
    }

    @Test
    public void testCheckInEnvironment() {
        // Given
        String scriptContent = "SELECT * FROM users WHERE age > 18";
        List<OffsetString> sqls = Arrays.asList(new OffsetString("SELECT * FROM users WHERE age > 18"),
                new OffsetString("UPDATE users SET age = 19 WHERE id = 1"));
        ConnectionConfig connectionConfig = new ConnectionConfig();
        connectionConfig.setDialectType(DialectType.MYSQL);

        Environment environment = mock(Environment.class);
        when(environmentService.detail(1L)).thenReturn(environment);
        when(environment.getRulesetId()).thenReturn(1L);

        List<CheckViolation> violations = Arrays.asList(
                new CheckViolation() {{
                    setLineNumber(1);
                    setType(RuleType.SQL_INJECTION);
                    setViolationMessage("SQL Injection detected.");
                }},
                new CheckViolation() {{
                    setLineNumber(1);
                    setType(RuleType.SQL_CHECK);
                    setViolationMessage("Check Rule1 violation.");
                }}
        );

        List<SqlCheckRule> sqlCheckRules = Arrays.asList(
                new SqlCheckRule() {{
                    setViolationMessage("Check Rule1 violation.");
                }},
                new SqlCheckRule() {{
                    setViolationMessage("SQL Injection detected.");
                }}
        );

        OBConsoleDataSourceFactory factory = new OBConsoleDataSourceFactory(connectionConfig, true, false);
        when(factory.getDataSource()).thenReturn(singleConnectionDataSource);

        // When
        List<CheckViolation> checkViolations = sqlCheckService.check(1L, "test_database", sqls, connectionConfig);

        // Then
        assertEquals(2, checkViolations.size());
        assertEquals(violations.get(0), checkViolations.get(0));
        assertEquals(violations.get(1), checkViolations.get(1));
    }

    @Test
    public void testCheckInEnvironmentWithEmptyRules() {
        // Given
        String scriptContent = "SELECT * FROM users WHERE age > 18";
        List<OffsetString> sqls = Arrays.asList(new OffsetString("SELECT * FROM users WHERE age > 18"),
                new OffsetString("UPDATE users SET age = 19 WHERE id = 1"));
        ConnectionConfig connectionConfig = new ConnectionConfig();
        connectionConfig.setDialectType(DialectType.MYSQL);

        Environment environment = mock(Environment.class);
        when(environmentService.detail(1L)).thenReturn(environment);
        when(environment.getRulesetId()).thenReturn(null);

        // When
        List<CheckViolation> checkViolations = sqlCheckService.check(1L, "test_database", sqls, connectionConfig);

        // Then
        assertEquals(0, checkViolations.size());
    }
}