
package com.oceanbase.odc.service.session.interceptor;

import com.oceanbase.odc.core.session.ConnectionSession;
import com.oceanbase.odc.core.shared.constant.DialectType;
import com.oceanbase.odc.service.iam.auth.AuthenticationFacade;
import com.oceanbase.odc.service.regulation.ruleset.RuleService;
import com.oceanbase.odc.service.regulation.ruleset.SqlConsoleRuleService;
import com.oceanbase.odc.service.session.model.SqlAsyncExecuteReq;
import com.oceanbase.odc.service.session.model.SqlAsyncExecuteResp;
import com.oceanbase.odc.service.session.model.SqlExecuteResult;
import org.junit.Before;
import org.junit.Test;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import java.util.HashMap;

import static org.junit.Assert.assertEquals;
import static org.mockito.Mockito.when;

/**
 * @author [Tester Name]
 */
public class SqlConsoleInterceptorTest {

    @Mock
    private AuthenticationFacade authenticationFacade;
    @Mock
    private RuleService ruleService;
    @Mock
    private SqlConsoleRuleService sqlConsoleRuleService;
    @Mock
    private SqlAsyncExecuteReq sqlAsyncExecuteReq;
    @Mock
    private SqlAsyncExecuteResp sqlAsyncExecuteResp;
    @Mock
    private ConnectionSession connectionSession;
    
    private SqlConsoleInterceptor sqlConsoleInterceptor;

    @Before
    public void setup() {
        MockitoAnnotations.initMocks(this);
        sqlConsoleInterceptor = new SqlConsoleInterceptor(authenticationFacade, ruleService, sqlConsoleRuleService);
    }

    @Test
    public void testDoPreHandle() {
        HashMap<String, Object> context = new HashMap<>();
        when(connectionSession.getDialectType()).thenReturn(DialectType.HIVE);

        boolean result = sqlConsoleInterceptor.doPreHandle(sqlAsyncExecuteReq, sqlAsyncExecuteResp, connectionSession, context);
        assertEquals(true, result);
    }

    @Test
    public void testDoAfterCompletion() {
        HashMap<String, Object> context = new HashMap<>();
        SqlExecuteResult response = new SqlExecuteResult();

        sqlConsoleInterceptor.doAfterCompletion(response, connectionSession, context);

        assertEquals(true, response.isAllowExport());
    }

    @Test
    public void testIsIndividualTeam() {
        boolean result = sqlConsoleInterceptor.isIndividualTeam();
        assertEquals(true, result);
    }

    @Test
    public void testGetOrder() {
        int result = sqlConsoleInterceptor.getOrder();
        assertEquals(2, result);
    }
}
