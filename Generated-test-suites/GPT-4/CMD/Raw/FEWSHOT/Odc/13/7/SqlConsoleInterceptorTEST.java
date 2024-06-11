
package com.oceanbase.odc.service.session.interceptor;

import static org.junit.Assert.assertEquals;
import static org.mockito.Mockito.*;

import java.util.*;

import com.oceanbase.odc.core.sql.execute.model.SqlExecuteStatus;
import com.oceanbase.odc.core.sql.execute.model.SqlTuple;
import com.oceanbase.odc.service.iam.auth.AuthenticationFacade;
import com.oceanbase.odc.service.regulation.ruleset.RuleService;
import com.oceanbase.odc.service.regulation.ruleset.SqlConsoleRuleService;
import com.oceanbase.odc.service.session.model.*;
import org.junit.Before;
import org.junit.Test;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

public class SqlConsoleInterceptorTest {
    private SqlConsoleInterceptor interceptor;

    @Mock
    private AuthenticationFacade authenticationFacade;

    @Mock
    private RuleService ruleService;

    @Mock
    private SqlConsoleRuleService sqlConsoleRuleService;

    @Before
    public void setup() {
        MockitoAnnotations.initMocks(this);
        interceptor = new SqlConsoleInterceptor();
        interceptor.setAuthenticationFacade(authenticationFacade);
        interceptor.setRuleService(ruleService);
        interceptor.setSqlConsoleRuleService(sqlConsoleRuleService);
    }

    @Test
    public void testHandle() {
        SqlAsyncExecuteReq request = mock(SqlAsyncExecuteReq.class);
        SqlAsyncExecuteResp response = mock(SqlAsyncExecuteResp.class);
        ConnectionSession session = mock(ConnectionSession.class);
        Map<String, Object> context = new HashMap<>();
        context.put("NEED_SQL_CONSOLE_CHECK", false);
        boolean result = interceptor.handle(request, response, session, context);
        assertEquals(true, result);
    }

    @Test
    public void testGetOrder() {
        assertEquals(2, interceptor.getOrder());
    }

    @Test
    public void testDoAfterCompletionResponseWithSqlExecuteStatusNotSuccess() {
        SqlExecuteResult sqlExecuteResult = new SqlExecuteResult();
        sqlExecuteResult.setStatus(SqlExecuteStatus.UNKNOWN);
        ConnectionSession session = mock(ConnectionSession.class);
        Map<String, Object> context = new HashMap<>();
        interceptor.doAfterCompletion(sqlExecuteResult,session,context);
        assertEquals(false, sqlExecuteResult.isAllowExport());
    }

    @Test
    public void testIsIndividualTeam_True() {
        when(authenticationFacade.currentUser().getOrganizationType()).thenReturn(OrganizationType.INDIVIDUAL);
        boolean result = interceptor.isIndividualTeam();
        assertEquals(true, result);
    }

    @Test
    public void testIsIndividualTeam_False() {
        when(authenticationFacade.currentUser().getOrganizationType()).thenReturn(OrganizationType.UNIVERSITY);
        boolean result = interceptor.isIndividualTeam();
        assertEquals(false, result);
    }

    @Test
    public void testDetermineSqlType_SyntaxErrorException() {
        SqlTuple sqlTuple = mock(SqlTuple.class);
        DialectType dialectType = DialectType.OB;
        BasicResult result = interceptor.determineSqlType(sqlTuple, dialectType);
        assertEquals(SqlType.OTHERS, result.getSqlType());
    }
}
