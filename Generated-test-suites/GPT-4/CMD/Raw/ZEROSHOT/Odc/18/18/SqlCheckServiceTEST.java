
package com.oceanbase.odc.service.sqlcheck;

import com.oceanbase.odc.core.datasource.SingleConnectionDataSource;
import com.oceanbase.odc.core.session.ConnectionSession;
import com.oceanbase.odc.service.collaboration.environment.EnvironmentService;
import com.oceanbase.odc.service.collaboration.environment.model.Environment;
import com.oceanbase.odc.service.connection.model.ConnectionConfig;
import com.oceanbase.odc.service.regulation.ruleset.RuleService;
import com.oceanbase.odc.service.regulation.ruleset.model.Rule;
import com.oceanbase.odc.service.regulation.ruleset.model.RuleMetadata;
import com.oceanbase.odc.service.sqlcheck.rule.SqlCheckRule;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.mockito.junit.MockitoJUnitRunner;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;

import java.util.ArrayList;
import java.util.List;

@RunWith(MockitoJUnitRunner.class)
public class SqlCheckServiceTest {

    @InjectMocks
    private SqlCheckService sqlCheckService;

    @Mock
    private RuleService ruleService;

    @Mock
    private EnvironmentService environmentService;

    @Mock
    private ConnectionSession connectionSession;

    private ConnectionConfig connectionConfig;

    @Before
    public void setup() {
        MockitoAnnotations.openMocks(this);
        connectionConfig = new ConnectionConfig();
        connectionConfig.setDialectType("DialectType");
        connectionConfig.setDatabaseName("databaseName");
    }

    @Test
    public void checkSessionAndReqTest() {
        List<CheckResult> checkResult = sqlCheckService.check(connectionSession, new SqlCheckReq());
        assertNotNull(checkResult);
    }

    @Test
    public void checkEnvironmentIdAndDatabaseNameTest() {
        List<Rule> ruleList = new ArrayList<>();
        Rule rule1 = new Rule();
        rule1.setMetadata(new RuleMetadata());
        rule1.getMetadata().setType("SQL_CHECK");
        
        Rule rule2 = new Rule();
        rule2.setMetadata(new RuleMetadata());
        rule2.getMetadata().setType("SQL_CHECK");
        ruleList.add(rule1);
        ruleList.add(rule2);

        // Mocking the calls...
        when(environmentService.detail(any(Long.class))).thenReturn(new Environment());
        when(ruleService.list(any(Long.class), any())).thenReturn(ruleList);

        List<CheckViolation> checkViolationList = sqlCheckService.check(1L, "databaseName", new ArrayList<>(), connectionConfig);
        assertNotNull(checkViolationList);
    }

    @Test
    public void testGetRules() {
        List<Rule> ruleList = new ArrayList<>();
      
        Rule rule1 = new Rule();
        rule1.setMetadata(new RuleMetadata());
        rule1.getMetadata().setType("SQL_CHECK");

        ruleList.add(rule1);
        List<SqlCheckRule> sqlCheckRuleList = sqlCheckService.getRules(ruleList, DialectType.MYSQL, connectionSession);
        assertNotNull(sqlCheckRuleList);
    }

    @Test
    public void testFullFillRiskLevel() {
        CheckViolation v1 = new CheckViolation();
        CheckViolation v2 = new CheckViolation();
        v1.setType(CheckViolation.Type.SELECT);
        v2.setType(CheckViolation.Type.INSERT);

        List<CheckViolation> violationList = new ArrayList<>();
        violationList.add(v1);
        violationList.add(v2);

        List<Rule> ruleList = sqlCheckService.fullFillRiskLevel(new ArrayList<>(), violationList);
        assertNotNull(ruleList);
    }
}
