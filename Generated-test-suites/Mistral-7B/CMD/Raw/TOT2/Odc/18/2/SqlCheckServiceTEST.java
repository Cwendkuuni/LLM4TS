package com.oceanbase.odc.service.sqlcheck;

import static org.junit.Assert.*;
import static org.mockito.Mockito.*;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import org.apache.commons.collections4.CollectionUtils;
import org.junit.Before;
import org.junit.Test;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
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
import com.oceanbase.odc.service.regulation.ruleset.Model.QueryRuleMetadataParams;
import com.oceanbase.odc.service.regulation.ruleset.Rule;
import com.oceanbase.odc.service.regulation.ruleset.RuleService;
import com.oceanbase.odc.service.sqlcheck.model.CheckResult;
import com.oceanbase.odc.service.sqlcheck.model.CheckViolation;
import com.oceanbase.odc.service.sqlcheck.model.SqlCheckReq;
import com.oceanbase.odc.service.sqlcheck.rule.SqlCheckRules;
import com.oceanbase.odc.service.session.factory.OBConsoleDataSourceFactory;
import com.oceanbase.odc.service.sqlcheck.SqlCheckService;
import com.oceanbase.odc.service.sqlcheck.SqlCheckUtil;
import com.oceanbase.odc.service.sqlcheck.rule.SqlCheckRule;
import com.oceanbase.odc.service.sqlcheck.rule.SqlCheckRuleFactory;

@SuppressWarnings("unchecked")
public class SqlCheckServiceTest {

    private static final String TEST_SCRIPT = "SELECT * FROM test_table WHERE id = ?";
    private static final String TEST_ENV_ID = "testEnvId";
    private static final String TEST_DATABASE_NAME = "testDatabase";
    private static final ConnectionConfig TEST_CONFIG = new ConnectionConfig();
    private static final DialectType TEST_DIALECT_TYPE = DialectType.MYSQL;
    private static final RuleService TEST_RULE_SERVICE = Mockito.mock(RuleService.class);
    private static final EnvironmentService TEST_ENV_SERVICE = Mockito.mock(EnvironmentService.class);
    private static final OBConsoleDataSourceFactory TEST_DATA_SOURCE_FACTORY = Mockito.mock(OBConsoleDataSourceFactory.class);

    @Autowired
    private SqlCheckService sqlCheckService;
    @Autowired
    private RuleService ruleService;
    @Autowired
    private EnvironmentService environmentService;
    @Autowired
    private SqlCheckRules sqlCheckRules;

    @Before
    public void setup() {
        when(ruleService.list(anyLong(), any(QueryRuleMetadataParams.class))).thenReturn( Arrays.asList(new Rule()) );
        when(environmentService.detail(TEST_ENV_ID)).thenReturn(new Environment());
        when(TEST_DATA_SOURCE_FACTORY.getDataSource()).thenReturn(Mockito.mock(SingleConnectionDataSource.class));
    }

    @Test
    public void testCheckWithConnectionSession() {
        ConnectionSession session = Mockito.mock(ConnectionSession.class);
        SqlCheckReq req = new SqlCheckReq();
        req.setScriptContent(TEST_SCRIPT);
        CheckResult result = sqlCheckService.check(session, req);
        assertTrue(CollectionUtils.isEmpty(result.getCheckViolations()));
    }

    @Test
    public void testCheckWithEnvironmentId() {
        List<OffsetString> sqls = Arrays.asList(new OffsetString(TEST_SCRIPT));
        CheckResult result = sqlCheckService.check(TEST_ENV_ID, TEST_DATABASE_NAME, sqls, TEST_CONFIG);
        assertTrue(CollectionUtils.isEmpty(result.getCheckViolations()));
    }

    @Test
    public void testGetRules() {
        List<Rule> rules = Arrays.asList(new Rule());
        JdbcTemplate jdbc = Mockito.mock(JdbcTemplate.class);
        List<SqlCheckRule> rulesFromService = sqlCheckService.getRules(rules, TEST_DIALECT_TYPE, jdbc);
        assertTrue(CollectionUtils.isEmpty(rulesFromService));
    }

    @Test
    public void testFullFillRiskLevel() {
        List<Rule> rules = Arrays.asList(new Rule());
        List<CheckViolation> violations = new ArrayList<>();
        Rule violatedRule = new Rule();
        CheckViolation violation = new CheckViolation();
        violation.setType(Mockito.mock(Class.class));
        violation.setMessage("Test violation message");
        violation.setRiskLevel(1);
        violations.add(violation);
        List<Rule> result = sqlCheckService.fullFillRiskLevel(rules, violations);
        assertEquals(1, result.size());
        Rule expectedResult = new Rule();
        expectedResult.setLevel(1);
        assertEquals(expectedResult, result.get(0));
    }
}