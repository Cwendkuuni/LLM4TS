
package com.oceanbase.odc.service.session.interceptor;

import static org.junit.Assert.*;
import static org.mockito.Mockito.*;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Objects;

import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.junit.Before;
import org.junit.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.MockitoJUnitRunner;

import com.oceanbase.odc.service.iam.auth.AuthenticationFacade;
import com.oceanbase.odc.service.regulation.ruleset.RuleService;
import com.oceanbase.odc.service.regulation.ruleset.SqlConsoleRuleService;
import com.oceanbase.odc.core.shared.constant.DialectType;
import com.oceanbase.odc.service.session.model.SqlAsyncExecuteReq;
import com.oceanbase.odc.service.session.model.SqlAsyncExecuteResp;
import com.oceanbase.odc.service.session.model.SqlExecuteResult;
import com.oceanbase.odc.core.session.ConnectionSession;
import com.oceanbase.tools.dbbrowser.parser.constant.SqlType;

@RunWith(MockitoJUnitRunner.class)
@SpringBootTest
public class SqlConsoleInterceptorTest {

    @InjectMocks
    SqlConsoleInterceptor sqlConsoleInterceptor;

    @Mock
    AuthenticationFacade authenticationFacade;

    @Mock
    RuleService ruleService;

    @Mock
    SqlConsoleRuleService sqlConsoleRuleService;

    @Mock
    SqlAsyncExecuteReq sqlAsyncExecuteReq;

    @Mock
    SqlAsyncExecuteResp sqlAsyncExecuteResp;

    @Mock
    ConnectionSession connectionSession;

    @Mock
    SqlExecuteResult sqlExecuteResult;

    private Map<String, Object> context;

    @Before
    public void setup() {
        context = new HashMap<>();
    }

    @Test
    public void testDoPreHandle() {
        when(handle(sqlAsyncExecuteReq, sqlAsyncExecuteResp, connectionSession, context)).thenReturn(true);
        boolean result = sqlConsoleInterceptor.doPreHandle(sqlAsyncExecuteReq, sqlAsyncExecuteResp, connectionSession, context);
        assertTrue(result);
    }

    @Test
    public void testDoAfterCompletion() {
        sqlConsoleInterceptor.doAfterCompletion(sqlExecuteResult, connectionSession, context);
        verify(sqlExecuteResult, times(1)).setAllowExport(Objects.nonNull(sqlExecuteResult.getAllowExport()));
    }

    @Test
    public void testIsIndividualTeam() {
        when(authenticationFacade.currentUser().getOrganizationType()).thenReturn(OrganizationType.INDIVIDUAL);
        boolean result = sqlConsoleInterceptor.isIndividualTeam();
        assertTrue(result);
    }

    @Test
    public void testDetermineSqlType() {
        SqlTuple sqlTuple = mock(SqlTuple.class);
        BasicResult result = sqlConsoleInterceptor.determineSqlType(sqlTuple, DialectType.MYSQL);
        assertEquals(SqlType.UNKNOWN, result.getSqlType());
    }

    @Test
    public void testGetOrder() {
        int result = sqlConsoleInterceptor.getOrder();
        assertEquals(2, result);
    }
}
