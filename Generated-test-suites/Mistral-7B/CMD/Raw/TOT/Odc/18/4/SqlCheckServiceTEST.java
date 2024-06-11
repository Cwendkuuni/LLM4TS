package com.oceanbase.odc.service.sqlcheck;

import static org.junit.Assert.*;
import static org.mockito.Mockito.*;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import org.apache.commons.collections4.CollectionUtils;
import org.apache.commons.dbcp2.BasicDataSource;
import org.apache.commons.dbcp2.JdbcConnectionPoolDataSource;
import org.junit.Before;
import org.junit.Test;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcOperations;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Component;
import org.springframework.validation.annotation.Validated;

import com.oceanbase.odc.core.authority.util.SkipAuthorize;
import com.oceanbase.odc.core.datasource.SingleConnectionDataSource;
import com.oceanbase.odc.core.session.ConnectionSession;
import com.oceanbase.odc.core.session.ConnectionSessionConstants;
import com.oceanbase.odc.core.shared.constant.DialectType;
import com.oceanbase.odc.service.collaboration.environment.Environment;
import com.oceanbase.odc.service.collaboration.environment.EnvironmentService;
import com.oceanbase.odc.service.connection.model.ConnectionConfig;
import com.oceanbase.odc.service.regulation.ruleset.Rule;
import com.oceanbase.odc.service.regulation.ruleset.RuleMetadata;
import com.oceanbase.odc.service.regulation.ruleset.RuleService;
import com.oceanbase.odc.service.regulation.ruleset.model.QueryRuleMetadataParams;
import com.oceanbase.odc.service.regulation.ruleset.model.Rule.RuleViolation;
import com.oceanbase.odc.service.regulation.ruleset.model.RuleType;
import com.oceanbase.odc.service.session.factory.OBConsoleDataSourceFactory;
import com.oceanbase.odc.service.sqlcheck.model.CheckResult;
import com.oceanbase.odc.service.sqlcheck.model.CheckViolation;
import com.oceanbase.odc.service.sqlcheck.rule.SqlCheckRule;
import com.oceanbase.odc.service.sqlcheck.rule.SqlCheckRuleFactory;
import com.oceanbase.odc.service.sqlcheck.rule.SimpleSqlCheckRuleFactory;
import com.oceanbase.odc.service.sqlcheck.util.SqlCheckUtil;
import com.oceanbase.odc.service.sqlcheck.util.SqlChecker;
import com.oceanbase.odc.service.sqlcheck.util.SqlCheckContext;

@Component
@Validated
@SkipAuthorize
public class SqlCheckServiceTest {

    private static final String DATABASE_NAME = "test_db";
    private static final String RULE_SET_NAME = "test_rule_set";
    private static final String SQL_CHECK_RULE_NAME = "test_sql_check_rule";
    private static final String CHECK_SCRIPT = "SELECT 1 FROM DUAL;";

    private RuleService ruleService;
    private EnvironmentService environmentService;
    private Rule rule;
    private JdbcTemplate jdbcTemplate;
    private ConnectionSession session;
    private SqlCheckService sqlCheckService;

    @Before
    public void setup() {
        ruleService = mock(RuleService.class);
        environmentService = mock(EnvironmentService.class);
        jdbcTemplate = new JdbcTemplate(new JdbcConnectionPoolDataSource());
        session = mock(ConnectionSession.class);
        sqlCheckService = new SqlCheckService() {
            {
                this.ruleService = ruleService;
                this.environmentService = environmentService;
            }
        };
        when(session.getDialectType()).thenReturn(DialectType.MYSQL);
        when(session.getSyncJdbcExecutor(ConnectionSessionConstants.CONSOLE_DS_KEY)).thenReturn(jdbcTemplate);
        when(session.getRuleSetId()).thenReturn(1L);

        RuleMetadata ruleMetadata = new RuleMetadata();
        ruleMetadata.setName(SQL_CHECK_RULE_NAME);
        ruleMetadata.setType(RuleType.SQL_CHECK);
        rule = new Rule();
        rule.setMetadata(ruleMetadata);
        rule.setEnabled(true);

        SqlCheckRuleFactory ruleFactory = new SimpleSqlCheckRuleFactory();
        List<SqlCheckRule> sqlCheckRules = Arrays.asList(ruleFactory.createRule(rule));
        when(ruleService.list(1L, QueryRuleMetadataParams.builder().build())).thenReturn(Arrays.asList(rule));
        when(ruleService.list(1L, QueryRuleMetadataParams.builder().build())).thenReturn(Arrays.asList(rule));
        when(ruleService.list(anyLong(), any(QueryRuleMetadataParams.class))).thenReturn(Collections.emptyList());

        when(environmentService.detail(anyLong())).thenReturn(new Environment());

        when(ruleService.list(anyLong(), any(QueryRuleMetadataParams.class))).thenAnswer(i -> {
            if (i.getArguments()[0].equals(1L)) {
                return Arrays.asList(rule);
            } else {
                return Collections.emptyList();
            }
        });
    }

    @Test
    public void testCheck_ValidScript() {
        CheckResult result = sqlCheckService.check(session, new SqlCheckReq());
        assertTrue(CollectionUtils.isEmpty(result.getCheckViolations()));
    }

    @Test
    public void testCheck_InvalidScript() {
        List<CheckViolation> checkViolations = Arrays.asList(new CheckViolation("test", "Test violation", RuleType.SQL_CHECK, "test_rule_set", 1, 1, 1, false));
        when(rule.getMetadata().getName()).thenReturn("test");
        when(rule.getViolation(0)).thenReturn(checkViolations.get(0).getViolation());

        List<Rule> violatedRules = sqlCheckService.fullFillRiskLevel(Arrays.asList(rule), Collections.emptyList());
        assertEquals(1, violatedRules.size());
        assertEquals(RuleViolation.ERROR, violatedRules.get(0).getViolation().getType());

        CheckResult result = sqlCheckService.check(session, new SqlCheckReq());
        assertEquals(1, result.getCheckViolations().size());
        assertEquals("Test violation", result.getCheckViolations().get(0).getMessage());
        assertEquals("test_rule_set", result.getCheckViolations().get(0).getRuleSetName());
        assertEquals(1, result.getCheckViolations().get(0).getRuleId());
        assertEquals(RuleType.SQL_CHECK, result.getCheckViolations().get(0).getRuleType());
        assertEquals(1, result.getCheckViolations().get(0).getLineNumber());
        assertFalse(result.getCheckViolations().get(0).isSilent());
    }

    @Test
    public void testCheck_EmptyScript() {
        CheckResult result = sqlCheckService.check(session, new SqlCheckReq());
        assertTrue(CollectionUtils.isEmpty(result.getCheckViolations()));
    }

    @Test
    public void testCheck_NullScript() {
        CheckResult result = sqlCheckService.check(session, null);
        assertTrue(CollectionUtils.isEmpty(result.getCheckViolations()));
    }

    @Test
    public void testCheck_EnvironmentScript() {
        BasicDataSource envDataSource = new BasicDataSource();
        envDataSource.setDriverClassName("com.mysql.jdbc.Driver");
        envDataSource.setUrl("jdbc:mysql://localhost:3306/test_db");
        envDataSource.setUsername("test_user");
        envDataSource.setPassword("test_password");

        OBConsoleDataSourceFactory factory = new OBConsoleDataSourceFactory(new ConnectionConfig(), true, false);
        when(environmentService.detail(anyLong())).thenReturn(new Environment().setRulesetId(1L));
        when(factory.getDataSource()).thenReturn(envDataSource);

        List<CheckViolation> checkViolations = Arrays.asList(new CheckViolation("test", "Test violation", RuleType.SQL_CHECK, "test_rule_set", 1, 1, 1, false));
        when(rule.getMetadata().getName()).thenReturn("test");
        when(rule.getViolation(0)).thenReturn(checkViolations.get(0).getViolation());

        List<Rule> violatedRules = sqlCheckService.fullFillRiskLevel(Arrays.asList(rule), Collections.emptyList());
        assertEquals(1, violatedRules.size());
        assertEquals(RuleViolation.ERROR, violatedRules.get(0).getViolation().getType());

        CheckResult result = sqlCheckService.check(1L, DATABASE_NAME, Arrays.asList(new OffsetString(CHECK_SCRIPT)), new ConnectionConfig());
        assertEquals(1, result.getCheckViolations().size());
        assertEquals("Test violation", result.getCheckViolations().get(0).getMessage());
        assertEquals("test_rule_set", result.getCheckViolations().get(0).getRuleSetName());
        assertEquals(1, result.getCheckViolations().get(0).getRuleId());
        assertEquals(RuleType.SQL_CHECK, result.getCheckViolations().get(0).getRuleType());
        assertEquals(1, result.getCheckViolations().get(0).getLineNumber());
        assertFalse(result.getCheckViolations().get(0).isSilent());
    }
}