
package com.oceanbase.odc.service.session.interceptor;

import org.junit.jupiter.api.Test;
import org.springframework.test.util.ReflectionTestUtils;
import org.mockito.Mockito;
import static org.junit.jupiter.api.Assertions.*;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;

import com.oceanbase.odc.core.session.ConnectionSession;
import com.oceanbase.odc.core.shared.constant.DialectType;
import com.oceanbase.odc.service.regulation.ruleset.RuleService;
import com.oceanbase.odc.service.regulation.ruleset.SqlConsoleRuleService;
import com.oceanbase.odc.service.session.model.SqlAsyncExecuteReq;
import com.oceanbase.odc.service.session.model.SqlAsyncExecuteResp;
import com.oceanbase.odc.service.session.model.SqlExecuteResult;
import com.oceanbase.odc.service.regulation.ruleset.model.Rule;

public class SqlConsoleInterceptorTest {

    @Test
    public void testDoPreHandle() {
        SqlAsyncExecuteReq request = new SqlAsyncExecuteReq();
        SqlAsyncExecuteResp response = new SqlAsyncExecuteResp();

        ConnectionSession session = Mockito.mock(ConnectionSession.class);
        RuleService ruleService = Mockito.mock(RuleService.class);
        SqlConsoleRuleService sqlConsoleRuleService = Mockito.mock(SqlConsoleRuleService.class);

        SqlConsoleInterceptor interceptor = new SqlConsoleInterceptor();
        ReflectionTestUtils.setField(interceptor, "ruleService", ruleService);
        ReflectionTestUtils.setField(interceptor, "sqlConsoleRuleService", sqlConsoleRuleService);

        List<SqlTuple> sqlTuples = new ArrayList<>();
        SqlTuple sqlTuple = new SqlTuple();
        sqlTuples.add(sqlTuple);

        response.setSqlTuples(sqlTuples);

        Map<String, Object> context = new HashMap<>();
        context.put(SqlConsoleInterceptor.NEED_SQL_CONSOLE_CHECK, Boolean.FALSE);

        assertTrue(interceptor.doPreHandle(request, response, session, context));
    }

    @Test
    public void testDoAfterCompletion(){
        SqlExecuteResult response = new SqlExecuteResult();
        ConnectionSession session = Mockito.mock(ConnectionSession.class);
        Map<String, Object> context = new HashMap<>();

        RuleService ruleService = Mockito.mock(RuleService.class);
        SqlConsoleRuleService sqlConsoleRuleService = Mockito.mock(SqlConsoleRuleService.class);

        SqlConsoleInterceptor interceptor = new SqlConsoleInterceptor();
        ReflectionTestUtils.setField(interceptor, "ruleService", ruleService);
        ReflectionTestUtils.setField(interceptor, "sqlConsoleRuleService", sqlConsoleRuleService);

        Mockito.when(sqlConsoleRuleService.isForbidden(SqlConsoleRules.NOT_ALLOWED_EDIT_RESULTSET, session)).thenReturn(true);
        Mockito.when(sqlConsoleRuleService.isForbidden(SqlConsoleRules.NOT_ALLOWED_EXPORT_RESULTSET, session)).thenReturn(true);

        interceptor.doAfterCompletion(response, session, context);

        assertFalse(response.isAllowExport());
    }

    @Test
    public void testIsIndividualTeam() {
        SqlConsoleInterceptor interceptor = new SqlConsoleInterceptor();

        Mockito.when(authenticationFacade.currentUser().getOrganizationType()).thenReturn(OrganizationType.INDIVIDUAL);

        assertTrue(interceptor.isIndividualTeam());
    }

    @Test
    public void testDetermineSqlType() {
        SqlTuple sqlTuple = new SqlTuple();
        sqlTuple.setAst(null);
        
        SqlConsoleInterceptor interceptor = new SqlConsoleInterceptor();
        BasicResult result = interceptor.determineSqlType(sqlTuple, DialectType.MySQL);
        
        assertEquals(SqlType.OTHERS, result.getSqlType());
    }

    @Test
    public void testGetOrder() {
        SqlConsoleInterceptor interceptor = new SqlConsoleInterceptor();
        assertEquals(2, interceptor.getOrder());
    }
}
