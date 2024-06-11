package com.oceanbase.odc.service.session.interceptor;

import static org.junit.Assert.*;
import static org.mockito.Mockito.*;

import com.oceanbase.odc.core.session.ConnectionSession;
import com.oceanbase.odc.core.sql.execute.model.SqlTuple;
import com.oceanbase.odc.service.iam.auth.AuthenticationFacade;
import com.oceanbase.odc.service.regulation.ruleset.RuleService;
import com.oceanbase.odc.service.regulation.ruleset.SqlConsoleRuleService;
import com.oceanbase.odc.service.session.model.SqlAsyncExecuteReq;
import com.oceanbase.odc.service.session.model.SqlAsyncExecuteResp;
import com.oceanbase.odc.service.session.model.SqlExecuteResult;
import com.oceanbase.tools.dbbrowser.parser.result.BasicResult;
import com.oceanbase.tools.sqlparser.SyntaxErrorException;

import org.junit.Before;
import org.junit.Test;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import java.util.*;

public class SqlConsoleInterceptorTest {
    @Mock
    private AuthenticationFacade authenticationFacade;

    @Mock
    private RuleService ruleService;

    @Mock
    private SqlConsoleRuleService sqlConsoleRuleService;

    @Mock
    private SqlAsyncExecuteReq request;

    @Mock
    private SqlAsyncExecuteResp response;

    @Mock
    private ConnectionSession session;

    private Map<String, Object> context;

    private SqlConsoleInterceptor sqlConsoleInterceptor;

    @Before
    public void setUp() throws Exception {
        MockitoAnnotations.initMocks(this);

        sqlConsoleInterceptor = new SqlConsoleInterceptor();
        sqlConsoleInterceptor.setAuthenticationFacade(authenticationFacade);
        sqlConsoleInterceptor.setRuleService(ruleService);
        sqlConsoleInterceptor.setSqlConsoleRuleService(sqlConsoleRuleService);

        context = new HashMap<>();
    }

    @Test
    public void testDoPreHandle() {
        when(context.get(SqlConsoleInterceptor.NEED_SQL_CONSOLE_CHECK)).thenReturn(Boolean.FALSE);
        boolean res = sqlConsoleInterceptor.doPreHandle(request, response, session, context);
        
        assertTrue(res);
    }

    @Test
    public void testGetOrder() {
        assertEquals(2, sqlConsoleInterceptor.getOrder());
    }

    @Test
    public void testDoAfterCompletion() {
        SqlExecuteResult executeResult = mock(SqlExecuteResult.class);
        when(executeResult.getStatus()).thenReturn(SqlExecuteStatus.SUCCESS);

        sqlConsoleInterceptor.doAfterCompletion(executeResult, session, context);

        verify(executeResult, times(1)).setAllowExport(true);
    }
}