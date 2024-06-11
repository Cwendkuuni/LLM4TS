
package com.oceanbase.odc.service.sqlcheck;

import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertTrue;
import static org.mockito.Mockito.*;

import com.oceanbase.odc.core.datasource.SingleConnectionDataSource;
import com.oceanbase.odc.core.session.ConnectionSession;
import com.oceanbase.odc.service.collaboration.environment.EnvironmentService;
import com.oceanbase.odc.service.collaboration.environment.model.Environment;
import com.oceanbase.odc.service.sqlcheck.model.CheckViolation;
import com.oceanbase.odc.service.sqlcheck.model.SqlCheckReq;
import com.oceanbase.odc.service.sqlcheck.rule.SqlCheckRule;
import com.oceanbase.odc.service.regulation.ruleset.RuleService;
import com.oceanbase.odc.service.regulation.ruleset.model.Rule;
import org.junit.Before;
import org.junit.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.jdbc.core.JdbcTemplate;

import java.util.Collections;
import java.util.List;

public class SqlCheckServiceTest {

    @InjectMocks
    private SqlCheckService sqlCheckService;

    @Mock
    private RuleService ruleService;

    @Mock
    private EnvironmentService environmentService;

    @Mock
    private ConnectionSession session;

    @Before
    public void setUp() {
        MockitoAnnotations.initMocks(this);
    }

    // Test for check method with ConnectionSession and SqlCheckReq parameters
    @Test
    public void testCheckWithSessionAndReq() {
        SqlCheckReq request = mock(SqlCheckReq.class);

        List<CheckResult> result = sqlCheckService.check(session, request);

        assertNotNull(result);
    }
  
    // Test for check method with Long, String, List and ConnectionConfig parameters
    @Test
    public void testCheckWithEnvironmentAndDatabase() {
        Long environmentId = 1L;
        String databaseName = "database";
        List<OffsetString> sqls = Collections.singletonList(new OffsetString("query", 1));
        ConnectionConfig config = mock(ConnectionConfig.class);
        
        List<CheckViolation> result = sqlCheckService.check(environmentId, databaseName, sqls, config);
        
        assertNotNull(result);
    }

    @Test
    public void testGetRulesWithRulesAndConnectionSession() {
        List<Rule> rules = Collections.singletonList(new Rule());
        List<SqlCheckRule> sqlCheckRuleList = sqlCheckService.getRules(rules, session);

        assertNotNull(sqlCheckRuleList);
    }

    @Test
    public void testGetRulesWithRulesAndDialectTypeAndJdbcOperations() {
        List<Rule> rules = Collections.singletonList(new Rule());
        DialectType dialectType = mock(DialectType.class);
        JdbcOperations jdbc = mock(JdbcTemplate.class);

        List<SqlCheckRule> sqlCheckRuleList = sqlCheckService.getRules(rules, dialectType, jdbc);

        assertNotNull(sqlCheckRuleList);
    }

    @Test
    public void testFullFillRiskLevel() {
        List<Rule> rules = Collections.singletonList(new Rule());
        List<CheckViolation> violations = Collections.singletonList(new CheckViolation());
        
        List<Rule> result = sqlCheckService.fullFillRiskLevel(rules, violations);
        
        assertNotNull(result);
    }
}
