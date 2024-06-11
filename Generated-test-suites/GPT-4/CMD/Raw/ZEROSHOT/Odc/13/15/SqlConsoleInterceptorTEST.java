
package com.oceanbase.odc.service.session.interceptor;

import org.junit.Before;
import org.junit.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import static org.junit.Assert.*;
import static org.mockito.Mockito.*;
import java.util.*;

import com.oceanbase.odc.core.session.ConnectionSession;
import com.oceanbase.odc.service.iam.auth.AuthenticationFacade;
import com.oceanbase.odc.service.regulation.ruleset.RuleService;
import com.oceanbase.odc.service.regulation.ruleset.SqlConsoleRuleService;
import com.oceanbase.odc.service.session.model.SqlAsyncExecuteReq;
import com.oceanbase.odc.service.session.model.SqlAsyncExecuteResp;

public class SqlConsoleInterceptorTest {

    @InjectMocks
    SqlConsoleInterceptor sqlConsoleInterceptor;

    @Mock
    private AuthenticationFacade authenticationFacade;

    @Mock
    private RuleService ruleService;

    @Mock
    private SqlConsoleRuleService sqlConsoleRuleService;

    @Mock
    ConnectionSession session;

    @Mock
    SqlAsyncExecuteReq request;

    @Mock
    SqlAsyncExecuteResp response;

    Map<String, Object> context;

    @Before
    public void setUp() {
        MockitoAnnotations.initMocks(this);
        context = new HashMap<>();
    }

    @Test
    public void testDoPreHandle() {
        when(response.getSqls()).thenReturn(new ArrayList<>());
        boolean result = sqlConsoleInterceptor.doPreHandle(request, response, session, context);
        assertTrue(result);
    }

    @Test
    public void testDeterminSqlType() {
        BasicResult basicResult = sqlConsoleInterceptor.determineSqlType(new SqlTuple(), DialectType.MYSQL);
        assertNotNull(basicResult);
    }

    @Test
    public void testDoAfterCompletion() {
        SqlExecuteResult sqlExecuteResult = new SqlExecuteResult();
        sqlConsoleInterceptor.doAfterCompletion(sqlExecuteResult, session, context);
        assertTrue(sqlExecuteResult.getAllowExport());
    }

    @Test
    public void testisIndividualTeam() {
        boolean individualTeam = sqlConsoleInterceptor.isIndividualTeam();
        assertFalse(individualTeam);
    }

    @Test
    public void testGetOrder() {
        int order = sqlConsoleInterceptor.getOrder();
        assertEquals(2, order);
    }
}
