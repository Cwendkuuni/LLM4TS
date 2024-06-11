package com.oceanbase.odc.service.session.interceptor;

import static org.junit.Assert.*;
import static org.mockito.Mockito.*;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.runners.MockitoJUnitRunner;

import com.oceanbase.odc.core.session.ConnectionSession;
import com.oceanbase.odc.core.sql.execute.SqlExecuteStages;
import com.oceanbase.odc.service.iam.auth.AuthenticationFacade;
import com.oceanbase.odc.service.regulation.ruleset.RuleService;
import com.oceanbase.odc.service.regulation.ruleset.SqlConsoleRuleService;
import com.oceanbase.odc.service.session.model.SqlAsyncExecuteReq;
import com.oceanbase.odc.service.session.model.SqlAsyncExecuteResp;
import com.oceanbase.odc.service.session.model.SqlExecuteResult;
import com.oceanbase.odc.service.session.model.SqlTuple;
import com.oceanbase.tools.dbbrowser.parser.result.BasicResult;

@RunWith(MockitoJUnitRunner.class)
public class SqlConsoleInterceptorTest {

    @Mock
    private ConnectionSession session;
    @Mock
    private RuleService ruleService;
    @Mock
    private SqlConsoleRuleService sqlConsoleRuleService;
    @Mock
    private AuthenticationFacade authenticationFacade;
    
    @InjectMocks
    private SqlConsoleInterceptor sqlConsoleInterceptor;
    
    private Map<String, Object> context;
    private SqlAsyncExecuteReq request;
    private SqlAsyncExecuteResp response;
    
    @Before
    public void setUp() {
        context = new HashMap<>();
        request = new SqlAsyncExecuteReq();
        response = new SqlAsyncExecuteResp();
        List<SqlTuple> tuples = new ArrayList<>();
        response.setSqlTuples(tuples);
    }

    @Test
    public void testDoPreHandle() {
        boolean result = sqlConsoleInterceptor.doPreHandle(request, response, session, context);
        assertTrue("The return value should be true by default", result);
    }
    
    @Test
    public void testDoAfterCompletion() {

         when(sqlConsoleRuleService.isForbidden(any(), any())).thenReturn(false);

        SqlExecuteResult executeResult = new SqlExecuteResult();

        sqlConsoleInterceptor.doAfterCompletion(executeResult, session, context);

        assertTrue("Should be editable by default", executeResult.getResultSetMetaData().isEditable());
        assertTrue("Should be allowed to export by default", executeResult.isAllowExport());
    }
    
    @Test
    public void testGetOrder() {
        int result = sqlConsoleInterceptor.getOrder();
        assertEquals("Constructor sets the order to 2 by default", 2, result);
    }
    
    @Test
    public void testGetExecuteStageName() {
        String result = sqlConsoleInterceptor.getExecuteStageName();
        assertEquals("Should be the same as defined in SqlExecuteStages", SqlExecuteStages.SQL_CONSOLE_RULE, result);
    }
}
