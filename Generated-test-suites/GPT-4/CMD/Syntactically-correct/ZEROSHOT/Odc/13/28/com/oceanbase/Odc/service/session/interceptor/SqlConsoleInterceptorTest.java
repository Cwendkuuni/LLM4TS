
package com.oceanbase.odc.service.session.interceptor;

import com.oceanbase.odc.core.session.ConnectionSession;
import com.oceanbase.odc.core.shared.constant.DialectType;
import com.oceanbase.odc.service.iam.auth.AuthenticationFacade;
import com.oceanbase.odc.service.regulation.ruleset.RuleService;
import com.oceanbase.odc.service.regulation.ruleset.SqlConsoleRuleService;
import com.oceanbase.odc.service.session.model.SqlAsyncExecuteReq;
import com.oceanbase.odc.service.session.model.SqlAsyncExecuteResp;
import com.oceanbase.odc.service.session.model.SqlExecuteResult;
import com.oceanbase.tools.dbbrowser.parser.result.BasicResult;
import org.junit.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.Collections;
import java.util.HashMap;
import java.util.Map;

import static org.junit.Assert.*;
import static org.mockito.Mockito.*;

public class SqlConsoleInterceptorTest{

    @Mock
    private AuthenticationFacade authenticationFacade;

    @Mock
    private RuleService ruleService;

    @Mock
    private SqlConsoleRuleService sqlConsoleRuleService;

    @InjectMocks
    private SqlConsoleInterceptor sqlConsoleInterceptor;

    @Test
    public void testDoPreHandle() {
        SqlAsyncExecuteReq request = new SqlAsyncExecuteReq();
        SqlAsyncExecuteResp response = new SqlAsyncExecuteResp();
        ConnectionSession session = mock(ConnectionSession.class);
        Map context = new HashMap<String, Object>();

        assertTrue(sqlConsoleInterceptor.doPreHandle(request, response, session, context));
    }

    @Test
    public void testGetOrder(){
        int order = sqlConsoleInterceptor.getOrder();
        assertEquals(2, order);
    }

    @Test
    public void testDoAfterCompletion() {
        SqlExecuteResult response = new SqlExecuteResult();
        ConnectionSession session = mock(ConnectionSession.class);
        Map context = new HashMap<String, Object>();
        sqlConsoleInterceptor.doAfterCompletion(response, session, context);
    }

    @Test
    public void testGetExecuteStageName(){
        String stageName = sqlConsoleInterceptor.getExecuteStageName();
        assertEquals("SQL_CONSOLE_RULE", stageName);
    }

    @Test
    public void testDetermineSqlType() {
        ConnectionSession connectionSession = mock(ConnectionSession.class);
        BasicResult result = sqlConsoleInterceptor.determineSqlType(null, DialectType.MYSQL);
        assertNotNull(result);
    }
}
