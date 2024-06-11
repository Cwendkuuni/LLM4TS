package com.oceanbase.odc.service.sqlcheck;

import static org.junit.Assert.*;
import static org.mockito.Mockito.*;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import org.apache.commons.collections4.CollectionUtils;
import org.apache.commons.lang3.StringUtils;
import org.junit.Before;
import org.junit.Test;
import org.mockito.ArgumentMatchers;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.jdbc.core.JdbcTemplate;

import com.oceanbase.odc.core.authority.util.SkipAuthorize;
import com.oceanbase.odc.core.datasource.SingleConnectionDataSource;
import com.oceanbase.odc.core.session.ConnectionConfig;
import com.oceanbase.odc.core.session.ConnectionSession;
import com.oceanbase.odc.core.session.ConnectionSessionConstants;
import com.oceanbase.odc.core.shared.constant.DialectType;
import com.oceanbase.odc.service.collaboration.environment.Environment;
import com.oceanbase.odc.service.collaboration.environment.EnvironmentService;
import com.oceanbase.odc.service.connection.model.ConnectionConfig;
import com.oceanbase.odc.service.regulation.ruleset.Model.CheckResult;
import com.oceanbase.odc.service.regulation.ruleset.Model.CheckViolation;
import com.oceanbase.odc.service.regulation.ruleset.Model.CheckViolation.Level;
import com.oceanbase.odc.service.regulation.ruleset.Model.Rule;
import com.oceanbase.odc.service.regulation.ruleset.Model.Rule.RuleViolation;
import com.oceanbase.odc.service.regulation.ruleset.Model.RuleType;
import com.oceanbase.odc.service.regulation.ruleset.RuleService;
import com.oceanbase.odc.service.sqlcheck.SqlCheckReq;
import com.oceanbase.odc.service.sqlcheck.model.SqlCheckRule;
import com.oceanbase.odc.service.sqlcheck.model.SqlCheckRuleFactory;
import com.oceanbase.odc.service.sqlcheck.rule.SqlCheckRules;
import com.oceanbase.odc.service.sqlcheck.rule.SqlCheckUtil;
import com.oceanbase.odc.service.sqlcheck.util.SqlChecker;
import com.oceanbase.odc.service.sqlcheck.util.SqlCheckContext;

/**
 * {@link SqlCheckService} Test.
 *
 * @author yh263208
 * @date 2023-02-28 10:18
 */
@SuppressWarnings("unchecked")
public class SqlCheckServiceTest {

    @InjectMocks
    private SqlCheckService sqlCheckService;

    @Mock
    private RuleService ruleService;

    @Mock
    private EnvironmentService environmentService;

    @Mock
    private JdbcTemplate jdbcTemplate;

    private ConnectionSession session;
    private ConnectionConfig connectionConfig;
    private Environment environment;
    private SqlCheckReq sqlCheckReq;

    @Before
    public void setUp() {
        MockitoAnnotations.initMocks(this);
        session = mock(ConnectionSession.class);
        connectionConfig = mock(ConnectionConfig.class);
        environment = mock(Environment.class);
        sqlCheckReq = SqlCheckReq.builder().build();
    }

    @Test
    public void testCheck() {
        when(ruleService.list(ArgumentMatchers.anyLong(), ArgumentMatchers.any(QueryRuleMetadataParams.class)))
                .thenReturn(Collections.singletonList(getRule()));
        when(environmentService.detail(ArgumentMatchers.anyLong())).thenReturn(environment);
        when(jdbcTemplate.query(ArgumentMatchers.anyString(), ArgumentMatchers.any()))
                .thenReturn(Collections.emptyList());

        List<SqlCheckRule> sqlCheckRules = sqlCheckService.getRules(Collections.singletonList(getRule()),
                DialectType.MYSQL, jdbcTemplate);
        assertEquals(1, sqlCheckRules.size());

        Rule rule = new Rule();
        rule.setEnabled(true);
        rule.setMetadata(getRuleMetadata());
        when(ruleService.list(ArgumentMatchers.anyLong(), ArgumentMatchers.any(QueryRuleMetadataParams.class)))
                .thenReturn(Collections.singletonList(rule));

        List<CheckViolation> checkViolations = sqlCheckService.check(session, sqlCheckReq);
        assertEquals(0, checkViolations.size());

        RuleViolation violation = RuleViolation.builder().build();
        violation.setMessage("test violation message");
        List<CheckViolation> testCheckViolations = new ArrayList<>();
        testCheckViolations.add(CheckViolation.builder().setType(RuleType.SQL_CHECK).setViolation(violation).build());

        when(rule.getViolation()).thenReturn(violation);
        when(jdbcTemplate.query(ArgumentMatchers.anyString(), ArgumentMatchers.any()))
                .thenReturn(testCheckViolations);

        when(session.getDialectType()).thenReturn(DialectType.MYSQL);
        when(session.getSyncJdbcExecutor(ConnectionSessionConstants.CONSOLE_DS_KEY)).thenReturn(jdbcTemplate);

        List<CheckViolation> violations = sqlCheckService.check(session, sqlCheckReq);
        assertEquals(1, violations.size());
        assertEquals("test violation message", violations.get(0).getMessage());
        assertEquals(Level.LOW, violations.get(0).getLevel());

        List<CheckResult> checkResults = sqlCheckService.check(session, sqlCheckReq);
        assertEquals(1, checkResults.size());
        assertEquals(violations.get(0).getViolation().getMessage(), checkResults.get(0).getViolation().getMessage());
    }

    @Test
    public void testCheckWithEnvironment() {
        String databaseName = "test_database";
        List<OffsetString> sqls = Collections.singletonList(OffsetString.of("test sql"));
        ConnectionConfig config = ConnectionConfig.builder().build();

        when(ruleService.list(ArgumentMatchers.anyLong(), ArgumentMatchers.any(QueryRuleMetadataParams.class)))
                .thenReturn(Collections.singletonList(getRule()));
        when(environmentService.detail(ArgumentMatchers.anyLong())).thenReturn(environment);
        when(environment.getRulesetId()).thenReturn(1L);

        List<SqlCheckRule> sqlCheckRules = sqlCheckService.getRules(Collections.singletonList(getRule()),
                DialectType.MYSQL, jdbcTemplate);
        assertEquals(1, sqlCheckRules.size());

        Rule rule = new Rule();
        rule.setEnabled(true);
        rule.setMetadata(getRuleMetadata());
        when(ruleService.list(ArgumentMatchers.anyLong(), ArgumentMatchers.any(QueryRuleMetadataParams.class)))
                .thenReturn(Collections.singletonList(rule));

        when(environmentService.detail(ArgumentMatchers.anyLong())).thenReturn(environment);
        when(environment.getRulesetId()).thenReturn(1L);
        when(jdbcTemplate.query(ArgumentMatchers.anyString(), ArgumentMatchers.any()))
                .thenReturn(Collections.emptyList());

        List<CheckViolation> checkViolations = sqlCheckService.check(databaseName, sqls, config);
        assertEquals(0, checkViolations.size());

        RuleViolation violation = RuleViolation.builder().build();
        violation.setMessage("test violation message");
        List<CheckViolation> testCheckViolations = new ArrayList<>();
        testCheckViolations.add(CheckViolation.builder().setType(RuleType.SQL_CHECK).setViolation(violation).build());

        when(rule.getViolation()).thenReturn(violation);
        when(jdbcTemplate.query(ArgumentMatchers.anyString(), ArgumentMatchers.any()))
                .thenReturn(testCheckViolations);

        when(environmentService.detail(ArgumentMatchers.anyLong())).thenReturn(environment);
        when(environment.getRulesetId()).thenReturn(1L);

        List<CheckViolation> violations = sqlCheckService.check(databaseName, sqls, config);
        assertEquals(1, violations.size());
        assertEquals("test violation message", violations.get(0).getMessage());
        assertEquals(Level.LOW, violations.get(0).getLevel());

        List<CheckResult> checkResults = sqlCheckService.check(databaseName, sqls, config);
        assertEquals(1, checkResults.size());
        assertEquals(violations.get(0).getViolation().getMessage(), checkResults.get(0).getViolation().getMessage());
    }

    private Rule getRule() {
        Rule rule = new Rule();
        rule.setEnabled(true);
        rule.setMetadata(getRuleMetadata());
        return rule;
    }

    private RuleMetadata getRuleMetadata() {
        RuleMetadata metadata = new RuleMetadata();
        metadata.setName("test Rule");
        metadata.setType(RuleType.SQL_CHECK);
        metadata.setLevel(Level.LOW);
        return metadata;
    }
}