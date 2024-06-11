package com.oceanbase.odc.service.sqlcheck;

import static org.junit.Assert.*;
import static org.mockito.Mockito.*;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import javax.validation.Valid;
import javax.validation.constraints.NotNull;

import org.apache.commons.collections4.CollectionUtils;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.mockito.ArgumentsMatchers;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcOperations;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.simple.SimpleJdbcTemplate;
import org.springframework.stereotype.Component;
import org.springframework.validation.annotation.Validated;
import org.springframework.validation.beanvalidation.LocalValidatorFactoryBean;

import com.oceanbase.odc.core.authority.util.SkipAuthorize;
import com.oceanbase.odc.core.datasource.SingleConnectionDataSource;
import com.oceanbase.odc.core.session.ConnectionSession;
import com.oceanbase.odc.core.session.ConnectionSessionConstants;
import com.oceanbase.odc.core.sql.split.OffsetString;
import com.oceanbase.odc.service.collaboration.environment.Environment;
import com.oceanbase.odc.service.collaboration.environment.EnvironmentService;
import com.oceanbase.odc.service.connection.model.ConnectionConfig;
import com.oceanbase.odc.service.regulation.ruleset.Rule;
import com.oceanbase.odc.service.regulation.ruleset.RuleMetadata;
import com.oceanbase.odc.service.regulation.ruleset.RuleService;
import com.oceanbase.odc.service.regulation.ruleset.RuleType;
import com.oceanbase.odc.service.regulation.ruleset.model.CheckResult;
import com.oceanbase.odc.service.regulation.ruleset.model.CheckViolation;
import com.oceanbase.odc.service.regulation.ruleset.model.QueryRuleMetadataParams;
import com.oceanbase.odc.service.regulation.ruleset.model.Rule.RuleViolation;
import com.oceanbase.odc.service.regulation.ruleset.model.Rule.RuleLevel;
import com.oceanbase.odc.service.regulation.ruleset.model.RuleMetadata.RuleMetadataType;
import com.oceanbase.odc.service.regulation.ruleset.model.RuleMetadata.RuleTypeName;
import com.oceanbase.odc.service.session.factory.OBConsoleDataSourceFactory;
import com.oceanbase.odc.service.sqlcheck.model.CheckResult.CheckResultStatus;
import com.oceanbase.odc.service.sqlcheck.model.CheckViolation.CheckViolationType;
import com.oceanbase.odc.service.sqlcheck.model.SqlCheckReq;
import com.oceanbase.odc.service.sqlcheck.rule.SqlCheckRules;
import com.oceanbase.odc.service.sqlcheck.rule.SqlCheckRule;
import com.oceanbase.odc.service.sqlcheck.rule.SqlCheckRuleFactory;
import com.oceanbase.odc.service.sqlcheck.rule.SqlCheckRuleType;
import com.oceanbase.odc.service.sqlcheck.util.SqlCheckUtil;
import com.oceanbase.odc.service.sqlcheck.util.SqlChecker;

@Component
@Validated
@SkipAuthorize("inside connect session")
public class SqlCheckServiceTest {

    @Mock
    private EnvironmentService environmentService;
    @Mock
    private RuleService ruleService;
    @Mock
    private LocalValidatorFactoryBean validator;

    private SqlCheckService sqlCheckService;
    private ConnectionSession session;
    private ConnectionConfig config;
    private SimpleJdbcTemplate jdbcTemplate;
    private SingleConnectionDataSource dataSource;

    @Before
    public void initMocks() {
        MockitoAnnotations.initMocks(this);
        sqlCheckService = new SqlCheckService();
        sqlCheckService.ruleService = ruleService;
        sqlCheckService.environmentService = environmentService;
        sqlCheckService.validator = validator;
        session = mock(ConnectionSession.class);
        config = mock(ConnectionConfig.class);
        jdbcTemplate = mock(SimpleJdbcTemplate.class);
        dataSource = mock(SingleConnectionDataSource.class);
        when(dataSource.getJdbcOperations()).thenReturn(jdbcTemplate);
    }

    @Test
    public void testCheck_withEmptyRuleSet_returnsEmptyCheckResultList() {
        Long ruleSetId = null;
        List<Rule> rules = Collections.emptyList();
        when(ruleService.list(eq(ruleSetId), any(QueryRuleMetadataParams.class))).thenReturn(rules);
        CheckResult result = sqlCheckService.check(session, new SqlCheckReq());
        assertTrue(result.getCheckResults().isEmpty());
    }

    @Test
    public void testCheck_withValidScriptContentAndEmptyRuleSet_returnsEmptyCheckViolationList() {
        Long ruleSetId = null;
        List<Rule> rules = Collections.emptyList();
        when(ruleService.list(eq(ruleSetId), any(QueryRuleMetadataParams.class))).thenReturn(rules);
        String scriptContent = "SELECT * FROM users WHERE id = ?";
        sqlCheckService.check(session, new SqlCheckReq().setScriptContent(scriptContent));
        assertTrue(sqlCheckService.checkViolations.isEmpty());
    }

    @Test
    public void testCheck_withValidScriptContentAndNonEmptyRuleSet_returnsCheckViolationList() {
        Long ruleSetId = 1L;
        List<Rule> rules = Arrays.asList(
                new Rule(),
                new Rule());
        RuleMetadata metadata1 = new RuleMetadata();
        metadata1.setName("SqlCheckRule1");
        metadata1.setType(RuleTypeName.SQL_CHECK);
        RuleMetadata metadata2 = new RuleMetadata();
        metadata2.setName("SqlCheckRule2");
        metadata2.setType(RuleTypeName.SQL_CHECK);
        rules.get(0).setMetadata(metadata1);
        rules.get(1).setMetadata(metadata2);
        when(ruleService.list(eq(ruleSetId), any(QueryRuleMetadataParams.class))).thenReturn(rules);
        String scriptContent = "SELECT * FROM users WHERE id = ?";
        SqlCheckRule sqlCheckRule1 = mock(SqlCheckRule.class);
        when(sqlCheckRule1.check(eq(scriptContent))).thenReturn(new CheckViolation("test1", CheckViolationType.TEST));
        SqlCheckRule sqlCheckRule2 = mock(SqlCheckRule.class);
        when(sqlCheckRule2.check(eq(scriptContent))).thenReturn(new CheckViolation("test2", CheckViolationType.TEST));
        List<SqlCheckRule> sqlCheckRules = Arrays.asList(sqlCheckRule1, sqlCheckRule2);
        when(ruleService.list(eq(ruleSetId), any(QueryRuleMetadataParams.class))).thenReturn(rules);
        when(ruleService.listRules(eq(ruleSetId), any(RuleCheckReq.class))).thenReturn(sqlCheckRules);
        List<CheckResult> result = sqlCheckService.check(session, new SqlCheckReq().setScriptContent(scriptContent));
        assertFalse(result.getCheckResults().isEmpty());
        assertEquals(2, result.getCheckResults().size());
        CheckResult checkResult1 = result.getCheckResults().get(0);
        CheckViolation violation1 = checkResult1.getViolations().get(0);
        assertEquals("test1", violation1.getMessage());
        CheckResult checkResult2 = result.getCheckResults().get(1);
        CheckViolation violation2 = checkResult2.getViolations().get(0);
        assertEquals("test2", violation2.getMessage());
    }

    @Test
    public void testCheck_withValidScriptContentAndNonEmptyRuleSet_returnsCheckViolationWithCorrectLevel() {
        Long ruleSetId = 1L;
        List<Rule> rules = Arrays.asList(
                new Rule(),
                new Rule());
        RuleMetadata metadata1 = new RuleMetadata();
        metadata1.setName("SqlCheckRule1");
        metadata1.setType(RuleTypeName.SQL_CHECK);
        metadata1.setLevel(RuleLevel.LOW);
        RuleMetadata metadata2 = new RuleMetadata();
        metadata2.setName("SqlCheckRule2");
        metadata2.setType(RuleTypeName.SQL_CHECK);
        metadata2.setLevel(RuleLevel.HIGH);
        rules.get(0).setMetadata(metadata1);
        rules.get(1).setMetadata(metadata2);
        when(ruleService.list(eq(ruleSetId), any(QueryRuleMetadataParams.class))).thenReturn(rules);
        String scriptContent = "SELECT * FROM users WHERE id = ?";
        SqlCheckRule sqlCheckRule1 = mock(SqlCheckRule.class);
        when(sqlCheckRule1.check(eq(scriptContent))).thenReturn(new CheckViolation("test1", CheckViolationType.TEST));
        SqlCheckRule sqlCheckRule2 = mock(SqlCheckRule.class);
        when(sqlCheckRule2.check(eq(scriptContent))).thenReturn(new CheckViolation("test2", CheckViolationType.TEST));
        List<SqlCheckRule> sqlCheckRules = Arrays.asList(sqlCheckRule1, sqlCheckRule2);
        when(ruleService.list(eq(ruleSetId), any(QueryRuleMetadataParams.class))).thenReturn(rules);
        when(ruleService.listRules(eq(ruleSetId), any(RuleCheckReq.class))).thenReturn(sqlCheckRules);
        List<CheckResult> result = sqlCheckService.check(session, new SqlCheckReq().setScriptContent(scriptContent));
        assertFalse(result.getCheckResults().isEmpty());
        assertEquals(2, result.getCheckResults().size());
        CheckResult checkResult1 = result.getCheckResults().get(0);
        CheckViolation violation1 = checkResult1.getViolations().get(0);
        assertEquals(RuleLevel.LOW.getValue(), violation1.getLevel());
        CheckResult checkResult2 = result.getCheckResults().get(1);
        CheckViolation violation2 = checkResult2.getViolations().get(0);
        assertEquals(RuleLevel.HIGH.getValue(), violation2.getLevel());
    }

    @Test
    public void testCheck_withInvalidScriptContent_throwsException() {
        Long ruleSetId = 1L;
        List<Rule> rules = Arrays.asList(new Rule());
        when(ruleService.list(eq(ruleSetId), any(QueryRuleMetadataParams.class))).thenReturn(rules);
        String scriptContent = "SELECT * FROM users WHERE id = ";
        assertThrows(IllegalArgumentException.class, () -> {
            sqlCheckService.check(session, new SqlCheckReq().setScriptContent(scriptContent));
        });
    }

    @Test
    public void testCheck_withEmptyScriptContent_returnsEmptyCheckViolationList() {
        Long ruleSetId = 1L;
        List<Rule> rules = Arrays.asList(new Rule());
        when(ruleService.list(eq(ruleSetId), any(QueryRuleMetadataParams.class))).thenReturn(rules);
        String scriptContent = "";
        CheckResult result = sqlCheckService.check(session, new SqlCheckReq().setScriptContent(scriptContent));
        assertTrue(result.getCheckResults().isEmpty());
    }

    @Test
    public void testCheck_withEmptyScriptContent_returnsEmptyCheckResultList() {
        Long ruleSetId = 1L;
        List<Rule> rules = Arrays.asList(new Rule());
        when(ruleService.list(eq(ruleSetId), any(QueryRuleMetadataParams.class))).thenReturn(rules);
        String scriptContent = "";
        sqlCheckService.check(session, new SqlCheckReq().setScriptContent(scriptContent));
        assertTrue(sqlCheckService.checkViolations.isEmpty());
    }

    @Test
    public void testCheck_withSqlCheckRuleType_returnsCheckViolationList() {
        Long ruleSetId = 1L;
        List<Rule> rules = Arrays.asList(new Rule());
        when(ruleService.list(eq(ruleSetId), any(QueryRuleMetadataParams.class))).thenReturn(rules);
        SqlCheckRuleFactory sqlCheckRuleFactory = mock(SqlCheckRuleFactory.class);
        SqlCheckRuleType sqlCheckRuleType = mock(SqlCheckRuleType.class);
        when(ruleService.listRules(eq(ruleSetId), any(RuleCheckReq.class))).thenReturn(Arrays.asList(sqlCheckRuleFactory));
        when(sqlCheckRuleFactory.create(any(), any(), any(), any(), any(), any())).thenReturn(sqlCheckRuleType);
        String scriptContent = "SELECT * FROM users WHERE id = ?";
        List<CheckResult> result = sqlCheckService.check(session, new SqlCheckReq().setScriptContent(scriptContent));
        assertFalse(result.getCheckResults().isEmpty());
        assertEquals(1, result.getCheckResults().size());
        CheckResult checkResult = result.getCheckResults().get(0);
        CheckViolation violation = checkResult.getViolations().get(0);
        assertEquals("Test violation message", violation.getMessage());
        assertEquals(CheckResultStatus.FAILED, checkResult.getStatus());
    }

    @Test
    public void testCheck_withSqlCheckRuleTypeAndEmptyScriptContent_returnsEmptyCheckViolationList() {
        Long ruleSetId = 1L;
        List<Rule> rules = Arrays.asList(new Rule());
        when(ruleService.list(eq(ruleSetId), any(QueryRuleMetadataParams.class))).thenReturn(rules);
        SqlCheckRuleFactory sqlCheckRuleFactory = mock(SqlCheckRuleFactory.class);
        SqlCheckRuleType sqlCheckRuleType = mock(SqlCheckRuleType.class);
        when(ruleService.listRules(eq(ruleSetId), any(RuleCheckReq.class))).thenReturn(Arrays.asList(sqlCheckRuleFactory));
        when(sqlCheckRuleFactory.create(any(), any(), any(), any(), any(), any())).thenReturn(sqlCheckRuleType);
        String scriptContent = "";
        List<CheckResult> result = sqlCheckService.check(session, new SqlCheckReq().setScriptContent(scriptContent));
        assertTrue(result.getCheckResults().isEmpty());
    }

    @Test
    public void testCheck_withSqlCheckRuleTypeAndEmptyScriptContent_returnsEmptyCheckViolationList() {
        Long ruleSetId = 1L;
        List<Rule> rules = Arrays.asList(new Rule());
        when(ruleService.list(eq(ruleSetId), any(QueryRuleMetadataParams.class))).thenReturn(rules);
        SqlCheckRuleFactory sqlCheckRuleFactory = mock(SqlCheckRuleFactory.class);
        SqlCheckRuleType sqlCheckRuleType = mock(SqlCheckRuleType.class);
        when(ruleService.listRules(eq(ruleSetId), any(RuleCheckReq.class))).thenReturn(Arrays.asList(sqlCheckRuleFactory));
        when(sqlCheckRuleFactory.create(any(), any(), any(), any(), any(), any())).thenReturn(sqlCheckRuleType);
        String scriptContent = "";
        sqlCheckService.check(session, new SqlCheckReq().setScriptContent(scriptContent));
        assertTrue(sqlCheckService.checkViolations.isEmpty());
    }

    @After
    public void resetMocks() {
        sqlCheckService = new SqlCheckService();
        reset(environmentService, ruleService, validator);
    }
}