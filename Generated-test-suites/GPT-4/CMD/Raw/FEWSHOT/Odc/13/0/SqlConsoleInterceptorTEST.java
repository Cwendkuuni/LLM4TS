package com.oceanbase.odc.service.session.interceptor;

import com.oceanbase.odc.core.session.ConnectionSession;
import com.oceanbase.odc.core.sql.execute.model.SqlTuple;
import com.oceanbase.odc.service.regulation.ruleset.model.Rule;
import com.oceanbase.odc.service.session.model.SqlAsyncExecuteReq;
import com.oceanbase.odc.service.session.model.SqlAsyncExecuteResp;
import com.oceanbase.odc.service.session.model.SqlExecuteResult;
import com.oceanbase.tools.dbbrowser.parser.constant.SqlType;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;
import static org.mockito.Mockito.*;

public class SqlConsoleInterceptorTest {

    @Mock
    private ConnectionSession mockedSession;
    @Mock
    private SqlAsyncExecuteReq mockedRequest;
    @Mock
    private SqlAsyncExecuteResp mockedResponse;
    @Mock
    private AuthenticationFacade mockedAuthFacade;
    @Mock
    private RuleService ruleService;
    @Mock
    private SqlConsoleRuleService sqlConsoleRuleService;
    
    @InjectMocks
    private SqlConsoleInterceptor interceptor;

    @Before
    public void setUp() {
        MockitoAnnotations.initMocks(this);

        SqlTuple sqlTuple = new SqlTuple();
        sqlTuple.setSql("test");
        when(mockedResponse.getSqls()).thenReturn(Arrays.asList(sqlTuple));
    }

    @After
    public void tearDown() {
        verifyNoMoreInteractions(mockedSession, mockedRequest, mockedResponse);
    }

    @Test
    public void testDoPreHandle() {
        Map<String, Object> context = new HashMap<>();
        context.put(SqlCheckInterceptor.SQL_CHECK_INTERCEPTED, true);

        assertTrue(interceptor.doPreHandle(mockedRequest, mockedResponse, mockedSession, context));
    }
    
    @Test
    public void testDoAfterCompletion() {
        Map<String, Object> context = new HashMap<>();
        SqlExecuteResult executeResult = new SqlExecuteResult();
        executeResult.setStatus(SqlExecuteStatus.SUCCESS);
        interceptor.doAfterCompletion(executeResult, mockedSession, context);
        
        assertTrue(executeResult.isAllowExport());
    }

    @Test
    public void testDetermineSqlType() {
        SqlTuple sqlTuple = new SqlTuple();
        sqlTuple.setSql("test");
        
        BasicResult result = interceptor.determineSqlType(sqlTuple, DialectType.MYSQL);
        
        assertEquals(SqlType.OTHERS, result.getSqlType());
    }
    
    @Test
    public void testInterceptionOrder() {
        int expectedOrder = 2;
        
        assertEquals(expectedOrder, interceptor.getOrder());
    }
}