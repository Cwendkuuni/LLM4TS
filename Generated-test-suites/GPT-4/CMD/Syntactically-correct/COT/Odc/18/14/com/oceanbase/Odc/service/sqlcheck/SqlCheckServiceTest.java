package com.oceanbase.odc.service.sqlcheck;
import com.oceanbase.odc.core.session.ConnectionSession;
import com.oceanbase.odc.service.connection.model.ConnectionConfig;
import com.oceanbase.odc.service.sqlcheck.model.SqlCheckReq;
import com.oceanbase.odc.core.sql.split.OffsetString;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import java.util.Collections;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;
import static org.mockito.Mockito.when;


@RunWith(SpringJUnit4ClassRunner.class)
public class SqlCheckServiceTest {

    private static ConnectionSession session;
    private static SqlCheckReq req;
    private static Long environmentId;
    private static String databaseName;
    private static ConnectionConfig config;
    private static List<OffsetString> sqls;

    @Mock
    private RuleService ruleService;

    @Mock
    private EnvironmentService environmentService;

    @InjectMocks
    private SqlCheckService sqlCheckService;
    
    @Before
    public void setUp() throws Exception {
        session = new ConnectionSession();
        req = new SqlCheckReq();
        environmentId = 1L;
        databaseName = "testDb";
        config = new ConnectionConfig();
        sqls = new ArrayList<>();
    }

    @Test
    public void testCheck() {
        when(ruleService.list(any(), any())).thenReturn(Collections.emptyList());
        List<CheckResult> resp = sqlCheckService.check(session, req);
        assertEquals(resp.size(), 0);
    }

    @Test
    public void testCheckWithEnv() {
        when(environmentService.detail(any())).thenReturn(new Environment());
        when(ruleService.list(any(), any())).thenReturn(Collections.emptyList());
        List<CheckViolation> resp = sqlCheckService.check(environmentId, databaseName, sqls, config);
        assertEquals(resp.size(), 0);
    }

    @Test
    public void testFullFillRiskLevel() {
        List<CheckViolation> checkViolations = new ArrayList<>();
        List<Rule> rules = sqlCheckService.fullFillRiskLevel(new ArrayList<>(), checkViolations);
        assertEquals(rules.size(), 0);
    }

    @Test(expected=IllegalArgumentException.class)
    public void testGetRulesWithNoRuleServiceList() {
        sqlCheckService.getRules(Collections.emptyList(), ConnectionSessionConstants.CONSOLE_DS_KEY, null);
    }

    @Test(expected=NullPointerException.class)
    public void testCheckWithNullSession() {
        sqlCheckService.check(null, req);
    }
}