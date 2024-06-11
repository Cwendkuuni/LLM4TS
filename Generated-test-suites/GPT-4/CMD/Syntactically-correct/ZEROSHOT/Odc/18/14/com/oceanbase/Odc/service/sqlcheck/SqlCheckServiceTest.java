
package com.oceanbase.odc.service.sqlcheck;

import org.junit.Assert;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mockito;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.test.context.junit4.SpringRunner;

import java.util.Collections;
import java.util.List;

@RunWith(SpringRunner.class)
public class SqlCheckServiceTest {

    @MockBean
    private RuleService ruleService;
    @MockBean
    private EnvironmentService environmentService;
    @MockBean
    private ConnectionSession session;
    @MockBean
    private SqlCheckReq req;

    @Test
    public void testCheck() {
        SqlCheckService service = new SqlCheckService();
        service.ruleService = this.ruleService;
        service.environmentService = this.environmentService;

        Mockito.when(ConnectionSessionUtil.getRuleSetId(session)).thenReturn(null);

        List<CheckResult> results = service.check(session, req);

        Assert.assertTrue(results.isEmpty());
    }

    @Test
    public void testCheckWithEnvironmentId() {
        SqlCheckService service = new SqlCheckService();
        service.ruleService = this.ruleService;
        service.environmentService = this.environmentService;

        ConnectionConfig connectionConfig = Mockito.mock(ConnectionConfig.class);
        Mockito.when(connectionConfig.getDialectType()).thenReturn(DialectType.CUSTOM);

        Mockito.when(this.environmentService.detail(Mockito.anyLong())).thenReturn(new Environment());

        List<CheckViolation> checkViolations = service.check(1L, "test",
                Collections.singletonList(new OffsetString("test", 1)), connectionConfig);

        Assert.assertTrue(checkViolations.isEmpty());
    }
  
    @Test
    public void testGetRulesWithSession() {
        SqlCheckService service = new SqlCheckService();
        service.ruleService = this.ruleService;
        service.environmentService = this.environmentService;

        Mockito.when(this.session.getDialectType()).thenReturn(DialectType.CUSTOM);
        Mockito.when(this.session.getSyncJdbcExecutor(ConnectionSessionConstants.CONSOLE_DS_KEY)).thenReturn(new JdbcTemplate());

        List<SqlCheckRule> sqlCheckRules = service.getRules(Collections.emptyList(), session);

        Assert.assertTrue(sqlCheckRules.isEmpty());
    }
  
    // You can add more test methods here for other public methods in SqlCheckService class.
}
