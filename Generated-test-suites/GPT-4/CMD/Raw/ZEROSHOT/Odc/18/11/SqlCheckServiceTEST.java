
package com.oceanbase.odc.service.sqlcheck;

import static org.mockito.Mockito.*;
import static org.junit.Assert.*;

import com.oceanbase.odc.core.datasource.SingleConnectionDataSource;
import com.oceanbase.odc.core.session.ConnectionSession;
import com.oceanbase.odc.core.session.ConnectionSessionConstants;
import com.oceanbase.odc.core.sql.split.OffsetString;
import com.oceanbase.odc.service.collaboration.environment.EnvironmentService;
import com.oceanbase.odc.service.collaboration.environment.model.Environment;
import com.oceanbase.odc.service.connection.model.ConnectionConfig;
import com.oceanbase.odc.service.regulation.ruleset.RuleService;
import com.oceanbase.odc.service.regulation.ruleset.model.QueryRuleMetadataParams;
import com.oceanbase.odc.service.sqlcheck.model.CheckResult;
import com.oceanbase.odc.service.sqlcheck.model.CheckViolation;
import com.oceanbase.odc.service.sqlcheck.model.SqlCheckReq;
import com.oceanbase.odc.service.sqlcheck.rule.SqlCheckRule;

import java.util.Arrays;
import java.util.List;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.MockitoJUnitRunner;
import org.springframework.jdbc.core.JdbcOperations;
import org.springframework.jdbc.core.JdbcTemplate;

@RunWith(MockitoJUnitRunner.class)
public class SqlCheckServiceTest {

    @Mock
    private RuleService ruleService;

    @Mock
    private EnvironmentService environmentService;

    @InjectMocks
    private SqlCheckService sqlCheckService;

    @Mock
    private ConnectionSession session;

    @Mock
    private SqlCheckReq req;

    @Mock
    private ConnectionConfig config;

    @Mock
    private Environment env;

    @Mock
    private Rule rule;

    @Mock
    private SqlCheckRule sqlCheckRule;

    @Mock
    private CheckViolation checkViolation;
    
    @Mock
    private JdbcOperations jdbcOperations;

    @Before
    public void setup() {
        //Stubbing behaviors
        when(session.getSyncJdbcExecutor(ConnectionSessionConstants.CONSOLE_DS_KEY)).thenReturn(jdbcOperations);
        when(ruleService.list(anyLong(), any())).thenReturn(Arrays.asList(rule));
        
        sqlCheckService = new SqlCheckService();
        sqlCheckService.setRuleService(ruleService);
        sqlCheckService.setEnvironmentService(environmentService);
    }

    @Test
    public void testCheckWithSessionAndRequest() {
        List<CheckResult> checkResults = sqlCheckService.check(session, req);
        assertNotNull(checkResults);
    }
    
    @Test
    public void testCheckWithEnvironmentIdDatabaseNameSqlsAndConfig() {
        List<CheckViolation> checkViolations = sqlCheckService.check(1L, "testDB", Arrays.asList(new OffsetString()), config);
        assertNotNull(checkViolations);
    }

    @Test
    public void testGetRulesWithRulesSession() {
        List<SqlCheckRule> rules = sqlCheckService.getRules(Arrays.asList(rule), session);
        assertNotNull(rules);
    }

    @Test
    public void testGetRulesWithRulesDialectTypeAndJdbc() {
        List<SqlCheckRule> rules = sqlCheckService.getRules(Arrays.asList(rule), DialectType.MYSQL, new JdbcTemplate());
        assertNotNull(rules);
    }

    @Test
    public void testFullFillRiskLevel() {
        List<Rule> fullFilledRules = sqlCheckService.fullFillRiskLevel(Arrays.asList(rule), Arrays.asList(checkViolation));
        assertNotNull(fullFilledRules);
    }
}
