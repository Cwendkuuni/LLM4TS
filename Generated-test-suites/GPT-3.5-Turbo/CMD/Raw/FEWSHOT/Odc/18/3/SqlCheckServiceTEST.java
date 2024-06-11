package com.oceanbase.odc.service.sqlcheck;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;

import org.junit.Before;
import org.junit.Test;
import org.mockito.Mock;

import com.oceanbase.odc.core.authority.util.SkipAuthorize;
import com.oceanbase.odc.core.datasource.SingleConnectionDataSource;
import com.oceanbase.odc.service.collaboration.environment.EnvironmentService;
import com.oceanbase.odc.service.collaboration.environment.model.Environment;
import com.oceanbase.odc.service.connection.model.ConnectionConfig;
import com.oceanbase.odc.service.regulation.ruleset.RuleService;
import com.oceanbase.odc.service.regulation.ruleset.model.QueryRuleMetadataParams;
import com.oceanbase.odc.service.regulation.ruleset.model.Rule;
import com.oceanbase.odc.service.regulation.ruleset.model.RuleMetadata;
import com.oceanbase.odc.service.regulation.ruleset.model.RuleType;
import com.oceanbase.odc.service.session.factory.OBConsoleDataSourceFactory;
import com.oceanbase.odc.service.sqlcheck.model.CheckViolation;
import com.oceanbase.odc.service.sqlcheck.model.CheckResult;
import com.oceanbase.odc.service.sqlcheck.model.SqlCheckReq;
import com.oceanbase.odc.service.sqlcheck.rule.SqlCheckRules;
import com.oceanbase.odc.core.sql.split.OffsetString;

import javax.validation.Valid;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.NonNull;
import java.util.Collections;
import java.util.List;

public class SqlCheckServiceTest {

    private SqlCheckService sqlCheckService;

    @Mock
    private RuleService ruleService;

    @Mock
    private EnvironmentService environmentService;

    @Before
    public void setUp() {
        sqlCheckService = new SqlCheckService();
        sqlCheckService.ruleService = ruleService;
        sqlCheckService.environmentService = environmentService;
    }

    @Test
    public void testCheckWithSessionAndRequest() {
        ConnectionSession session = createMockConnectionSession();
        SqlCheckReq req = createMockSqlCheckReq();
        List<CheckResult> checkResults = sqlCheckService.check(session, req);
        assertNotNull(checkResults);
    }

    @Test
    public void testCheckWithEnvIdAndSqlsAndConfig() {
        Long environmentId = 1L;
        String databaseName = "testDB";
        List<OffsetString> sqls = createMockSqlList();
        ConnectionConfig config = createMockConnectionConfig();
        List<CheckViolation> checkViolations = sqlCheckService.check(environmentId, databaseName, sqls, config);
        assertNotNull(checkViolations);
    }

    @Test
    public void testGetRulesWithSession() {
        List<Rule> rules = createMockRules();
        ConnectionSession session = createMockConnectionSession();
        List<SqlCheckRule> sqlCheckRules = sqlCheckService.getRules(rules, session);
        assertNotNull(sqlCheckRules);
    }

    @Test
    public void testFullFillRiskLevel() {
        List<Rule> rules = createMockRules();
        List<CheckViolation> violations = createMockCheckViolations();
        List<Rule> violatedRules = sqlCheckService.fullFillRiskLevel(rules, violations);
        assertNotNull(violatedRules);
    }

    private ConnectionSession createMockConnectionSession() {
        // Create mock ConnectionSession
        return new ConnectionSession();
    }

    private SqlCheckReq createMockSqlCheckReq() {
        // Create mock SqlCheckReq
        return new SqlCheckReq();
    }

    private List<OffsetString> createMockSqlList() {
        // Create mock list of OffsetString objects
        return Collections.emptyList();
    }

    private ConnectionConfig createMockConnectionConfig() {
        // Create mock ConnectionConfig
        return new ConnectionConfig();
    }

    private List<Rule> createMockRules() {
        // Create mock list of Rule objects
        return Collections.emptyList();
    }

    private List<CheckViolation> createMockCheckViolations() {
        // Create mock list of CheckViolation objects
        return Collections.emptyList();
    }
}