package com.oceanbase.odc.service.session.interceptor;

import static org.junit.Assert.*;
import static org.mockito.Mockito.*;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.Optional;
import java.util.concurrent.atomic.AtomicBoolean;
import java.util.concurrent.atomic.AtomicReference;
import java.util.stream.Collectors;

import org.junit.Test;

import com.oceanbase.odc.core.session.ConnectionSession;
import com.oceanbase.odc.core.shared.constant.DialectType;
import com.oceanbase.odc.core.sql.execute.SqlExecuteStages;
import com.oceanbase.odc.core.sql.execute.model.SqlExecuteStatus;
import com.oceanbase.odc.core.sql.execute.model.SqlTuple;
import com.oceanbase.odc.core.sql.parser.AbstractSyntaxTree;
import com.oceanbase.odc.service.iam.auth.AuthenticationFacade;
import com.oceanbase.odc.service.regulation.ruleset.RuleService;
import com.oceanbase.odc.service.regulation.ruleset.SqlConsoleRuleService;
import com.oceanbase.odc.service.regulation.ruleset.model.Rule;
import com.oceanbase.odc.service.regulation.ruleset.model.Rule.RuleViolation;
import com.oceanbase.odc.service.regulation.ruleset.model.SqlConsoleRules;
import com.oceanbase.odc.service.session.model.SqlAsyncExecuteReq;
import com.oceanbase.odc.service.session.model.SqlAsyncExecuteResp;
import com.oceanbase.odc.service.session.model.SqlExecuteResult;
import com.oceanbase.odc.service.session.model.SqlTuplesWithViolation;
import com.oceanbase.tools.dbbrowser.parser.constant.SqlType;
import com.oceanbase.tools.dbbrowser.parser.result.BasicResult;

public class SqlConsoleInterceptorTest {

    private SqlConsoleInterceptor interceptor = new SqlConsoleInterceptor();

    @Test
    public void testDoPreHandle() {
        SqlAsyncExecuteReq request = mock(SqlAsyncExecuteReq.class);
        SqlAsyncExecuteResp response = mock(SqlAsyncExecuteResp.class);
        ConnectionSession session = mock(ConnectionSession.class);
        Map<String, Object> context = new HashMap<>();

        boolean result = interceptor.doPreHandle(request, response, session, context);

        assertTrue(result);
    }

    @Test
    public void testHandle() {
        SqlAsyncExecuteReq request = mock(SqlAsyncExecuteReq.class);
        SqlAsyncExecuteResp response = mock(SqlAsyncExecuteResp.class);
        ConnectionSession session = mock(ConnectionSession.class);
        Map<String, Object> context = new HashMap<>();

        boolean result = interceptor.handle(request, response, session, context);

        assertTrue(result);
    }

    @Test
    public void testGetExecuteStageName() {
        String stageName = interceptor.getExecuteStageName();

        assertEquals(SqlExecuteStages.SQL_CONSOLE_RULE, stageName);
    }

    @Test
    public void testDoAfterCompletion() {
        SqlExecuteResult response = new SqlExecuteResult();
        ConnectionSession session = mock(ConnectionSession.class);
        Map<String, Object> context = new HashMap<>();

        interceptor.doAfterCompletion(response, session, context);

        // Add assertions here
    }

    @Test
    public void testIsIndividualTeam() {
        boolean result = interceptor.isIndividualTeam();

        // Add assertions here
    }

    @Test
    public void testDetermineSqlType() {
        SqlTuple sqlTuple = mock(SqlTuple.class);
        DialectType dialectType = DialectType.MYSQL;

        BasicResult result = interceptor.determineSqlType(sqlTuple, dialectType);

        // Add assertions here
    }

    @Test
    public void testGetOrder() {
        int order = interceptor.getOrder();

        assertEquals(2, order);
    }
}