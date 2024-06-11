package com.oceanbase.odc.service.sqlcheck;

import static org.junit.Assert.assertEquals;
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
import org.junit.Before;
import org.junit.Test;
import org.mockito.ArgumentCaptor;
import org.mockito.Captor;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.jdbc.core.JdbcTemplate;

import com.oceanbase.odc.core.datasource.SingleConnectionDataSource;
import com.oceanbase.odc.core.session.ConnectionSession;
import com.oceanbase.odc.core.session.ConnectionSessionConstants;
import com.oceanbase.odc.core.session.ConnectionSessionUtil;
import com.oceanbase.odc.service.collaboration.environment.Environment;
import com.oceanbase.odc.service.collaboration.environment.EnvironmentService;
import com.oceanbase.odc.service.connection.model.ConnectionConfig;
import com.oceanbase.odc.service.regulation.ruleset.Rule;
import com.oceanbase.odc.service.regulation.ruleset.RuleService;
import com.oceanbase.odc.service.sqlcheck.model.CheckResult;
import com.oceanbase.odc.service.sqlcheck.model.CheckViolation;
import com.oceanbase.odc.service.sqlcheck.model.SqlCheckReq;
import com.oceanbase.odc.service.sqlcheck.rule.SqlCheckRules;
import com.oceanbase.odc.service.sqlcheck.rule.SqlCheckRule;
import com.oceanbase.odc.service.sqlcheck.rule.SqlCheckRuleFactory;
import com.oceanbase.odc.service.sqlcheck.utils.SqlCheckUtil;

@Slf4j
public class SqlCheckServiceTest {

    @Mock
    private RuleService ruleService;

    @Mock
    private EnvironmentService environmentService;

    @Mock
    private SingleConnectionDataSource singleConnectionDataSource;

    @Mock
    private JdbcTemplate jdbcTemplate;

    @InjectMocks
    private SqlCheckService sqlCheckService;

    @Captor
    private ArgumentCaptor<SqlCheckReq> sqlCheckReqCaptor;

    @Captor
    private ArgumentCaptor<List<SqlCheckRule>> sqlCheckRulesCaptor;

    @Before
    public void setup() {
        MockitoAnnotations.initMocks(this);
        when(ruleService.list(Mockito.anyLong(), Mockito.any(QueryRuleMetadataParams.class))).thenReturn( Arrays.asList(
                new Rule() {{
                    setMetadata(new RuleMetadata() {{
                        setName("Rule1");
                        setLevel(5);
                        setType(RuleType.SQL_CHECK);
                    }});
                }},
                new Rule() {{
                    setMetadata(new RuleMetadata() {{
                        setName("Rule2");
                        setLevel(1);
                        setType(RuleType.SQL_CHECK);
                    }});
                }}
        ));
        when(environmentService.detail(Mockito.anyLong())).thenReturn(new Environment());
        when(singleConnectionDataSource.getDialectType()).thenReturn(DialectType.MYSQL);
        when(singleConnectionDataSource.getJdbcOperations()).thenReturn(jdbcTemplate);
    }

    @Test
    public void testCheck() {
        ConnectionSession session = mock(ConnectionSession.class);
        ConnectionConfig connectionConfig = new ConnectionConfig();
        ConnectionSessionUtil.setConnectionSession(session);
        when(session.getDialectType()).thenReturn(DialectType.MYSQL);
        when(session.getId()).thenReturn(1L);
        SqlCheckReq req = new SqlCheckReq();
        String scriptContent = "SELECT * FROM table1";
        req.setScriptContent(scriptContent);
        req.setDelimiter(";");

        when(ConnectionSessionUtil.getRuleSetId(session)).thenReturn(1L);

        List<Rule> rules = Arrays.asList(new Rule() {{
            setMetadata(new RuleMetadata() {{
                setName("Rule1");
                setLevel(5);
                setType(RuleType.SQL_CHECK);
            }});
        }}, new Rule() {{
            setMetadata(new RuleMetadata() {{
                setName("Rule2");
                setLevel(1);
                setType(RuleType.SQL_CHECK);
            }});
        }});
        when(ruleService.list(Mockito.anyLong(), Mockito.any(QueryRuleMetadataParams.class))).thenReturn(rules);

        List<SqlCheckRuleFactory> candidates = SqlCheckRules.getAllFactories(DialectType.MYSQL, jdbcTemplate);
        List<SqlCheckRule> sqlCheckRules = sqlCheckService.getRules(rules, DialectType.MYSQL, jdbcTemplate);

        SqlChecker sqlChecker = new DefaultSqlChecker(DialectType.MYSQL, req.getDelimiter(), sqlCheckRules);
        List<CheckViolation> checkViolations = sqlChecker.check(Collections.singletonList(scriptContent));

        List<Rule> violatedRules = sqlCheckService.fullFillRiskLevel(rules, checkViolations);

        List<CheckResult> checkResults = sqlCheckService.check(session, req);
        assertEquals(Collections.singletonList(checkResults), SqlCheckUtil.buildCheckResults(checkViolations));

        // Test method call with mocks
        sqlCheckService.check(session, req);
        verify(ruleService).list(eq(1L), Mockito.any(QueryRuleMetadataParams.class));
        verify(ruleService).list(eq(1L), Mockito.any(QueryRuleMetadataParams.class)).thenReturn(rules);
        verify(jdbcTemplate).query(Mockito.anyString(), eq(null), Mockito.any(), Mockito.eq(Integer.class));
        verify(jdbcTemplate).query(Mockito.anyString(), Mockito.any(Map.class), Mockito.any(), Mockito.eq(Integer.class));
        verify(sqlCheckService).getRules(rules, DialectType.MYSQL, jdbcTemplate);
        verify(sqlChecker).check(eq(Collections.singletonList(scriptContent)));
        verify(sqlCheckService).fullFillRiskLevel(rules, checkViolations);

        ArgumentCaptor<SqlCheckReq> sqlCheckReq = ArgumentCaptor.forClass(SqlCheckReq.class);
        verify(sqlCheckService).check(eq(session), sqlCheckReqCaptor.capture());
        assertEquals(scriptContent, sqlCheckReqCaptor.getValue().getScriptContent());

        ArgumentCaptor<List<SqlCheckRule>> sqlCheckRules = ArgumentCaptor.forClass(List.class);
        verify(sqlCheckService).getRules(Mockito.anyList(), Mockito.any(DialectType.class), Mockito.any(JdbcOperations.class));
        assertEquals(sqlCheckRules.getValue(), sqlCheckRulesCaptor.getValue());
    }

    @Test
    public void testCheckWithEmptyRules() {
        ConnectionSession session = mock(ConnectionSession.class);
        ConnectionConfig connectionConfig = new ConnectionConfig();
        ConnectionSessionUtil.setConnectionSession(session);
        when(session.getDialectType()).thenReturn(DialectType.MYSQL);
        when(session.getId()).thenReturn(1L);
        SqlCheckReq req = new SqlCheckReq();
        String scriptContent = "SELECT * FROM table1";
        req.setScriptContent(scriptContent);
        req.setDelimiter(";");

        when(ConnectionSessionUtil.getRuleSetId(session)).thenReturn(1L);

        when(ruleService.list(Mockito.anyLong(), Mockito.any(QueryRuleMetadataParams.class))).thenReturn(Collections.emptyList());

        List<CheckResult> checkResults = sqlCheckService.check(session, req);
        assertEquals(Collections.emptyList(), checkResults);

        // Test method call with mocks
        sqlCheckService.check(session, req);
        verify(ruleService).list(eq(1L), Mockito.any(QueryRuleMetadataParams.class));
        verify(ruleService).list(eq(1L), Mockito.any(QueryRuleMetadataParams.class)).thenReturn(Collections.emptyList());
    }

    @Test
    public void testCheckWithNoEnabledRules() {
        ConnectionSession session = mock(ConnectionSession.class);
        ConnectionConfig connectionConfig = new ConnectionConfig();
        ConnectionSessionUtil.setConnectionSession(session);
        when(session.getDialectType()).thenReturn(DialectType.MYSQL);
        when(session.getId()).thenReturn(1L);
        SqlCheckReq req = new SqlCheckReq();
        String scriptContent = "SELECT * FROM table1";
        req.setScriptContent(scriptContent);
        req.setDelimiter(";");

        when(ConnectionSessionUtil.getRuleSetId(session)).thenReturn(1L);

        List<Rule> rules = Arrays.asList(new Rule() {{
            setMetadata(new RuleMetadata() {{
                setName("Rule1");
                setLevel(5);
                setType(RuleType.SQL_CHECK);
                setEnabled(false);
            }});
        }}, new Rule() {{
            setMetadata(new RuleMetadata() {{
                setName("Rule2");
                setLevel(1);
                setType(RuleType.SQL_CHECK);
                setEnabled(true);
            }});
        }});

        when(ruleService.list(Mockito.anyLong(), Mockito.any(QueryRuleMetadataParams.class))).thenReturn(rules);

        List<SqlCheckRuleFactory> candidates = SqlCheckRules.getAllFactories(DialectType.MYSQL, jdbcTemplate);
        List<SqlCheckRule> sqlCheckRules = sqlCheckService.getRules(rules, DialectType.MYSQL, jdbcTemplate);

        SqlChecker sqlChecker = new DefaultSqlChecker(DialectType.MYSQL, req.getDelimiter(), sqlCheckRules);
        List<CheckViolation> checkViolations = sqlChecker.check(Collections.singletonList(scriptContent));

        List<Rule> violatedRules = sqlCheckService.fullFillRiskLevel(rules, checkViolations);

        List<CheckResult> checkResults = sqlCheckService.check(session, req);
        assertEquals(Collections.emptyList(), checkResults);

        // Test method call with mocks
        sqlCheckService.check(session, req);
        verify(ruleService).list(eq(1L), Mockito.any(QueryRuleMetadataParams.class));
        verify(ruleService).list(eq(1L), Mockito.any(QueryRuleMetadataParams.class)).thenReturn(rules);
        verify(jdbcTemplate).query(Mockito.anyString(), eq(null), Mockito.any(), Mockito.eq(Integer.class));
        verify(jdbcTemplate).query(Mockito.anyString(), Mockito.any(Map.class), Mockito.any(), Mockito.eq(Integer.class));
        verify(sqlCheckService).getRules(rules, DialectType.MYSQL, jdbcTemplate);
        verify(sqlChecker).check(eq(Collections.singletonList(scriptContent)));
        verify(sqlCheckService).fullFillRiskLevel(rules, checkViolations);
    }

    @Test
    public void testCheckWithNoRules() {
        ConnectionSession session = mock(ConnectionSession.class);
        ConnectionConfig connectionConfig = new ConnectionConfig();
        ConnectionSessionUtil.setConnectionSession(session);
        when(session.getDialectType()).thenReturn(DialectType.MYSQL);
        when(session.getId()).thenReturn(1L);
        SqlCheckReq req = new SqlCheckReq();
        String scriptContent = "SELECT * FROM table1";
        req.setScriptContent(scriptContent);
        req.setDelimiter(";");

        when(ConnectionSessionUtil.getRuleSetId(session)).thenReturn(1L);

        when(ruleService.list(Mockito.anyLong(), Mockito.any(QueryRuleMetadataParams.class))).thenReturn(Collections.emptyList());

        List<CheckResult> checkResults = sqlCheckService.check(session, req);
        assertEquals(Collections.emptyList(), checkResults);

        // Test method call with mocks
        sqlCheckService.check(session, req);
        verify(ruleService).list(eq(1L), Mockito.any(QueryRuleMetadataParams.class));
        verify(ruleService).list(eq(1L), Mockito.any(QueryRuleMetadataParams.class)).thenReturn(Collections.emptyList());
    }
}