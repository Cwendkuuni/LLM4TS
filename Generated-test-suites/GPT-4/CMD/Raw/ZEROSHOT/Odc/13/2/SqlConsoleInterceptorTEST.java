
import com.oceanbase.odc.core.session.ConnectionSession;
import com.oceanbase.odc.core.session.ConnectionSessionUtil;
import com.oceanbase.odc.core.shared.constant.DialectType;
import com.oceanbase.odc.core.sql.execute.model.SqlExecuteStatus;
import com.oceanbase.odc.service.session.model.SqlAsyncExecuteReq;
import com.oceanbase.odc.service.session.model.SqlAsyncExecuteResp;
import com.oceanbase.odc.service.session.model.SqlTuplesWithViolation;
import com.oceanbase.odc.service.session.model.SqlExecuteResult;
import com.oceanbase.odc.service.regulation.ruleset.model.Rule;
import com.oceanbase.odc.service.regulation.ruleset.model.Rule.RuleViolation;
import com.oceanbase.tools.sqlparser.SyntaxErrorException;
import com.oceanbase.tools.dbbrowser.parser.constant.SqlType;
import com.oceanbase.tools.dbbrowser.parser.result.BasicResult;
import static org.junit.Assert.*;
import static org.mockito.Mockito.*;
import org.junit.Before;
import org.junit.Test;
import org.springframework.boot.test.mock.mockito.MockBean;
import java.util.*;
import java.util.concurrent.atomic.AtomicReference;

public class SqlConsoleInterceptorTest {

    @MockBean
    private SqlConsoleInterceptor interceptor;
    private SqlAsyncExecuteReq request;
    private SqlAsyncExecuteResp response;
    private ConnectionSession session;
    private SqlExecuteResult executeResult;
    private Map<String, Object> context;

    @Before
    public void setup() {
        request = new SqlAsyncExecuteReq();
        response = new SqlAsyncExecuteResp();
        session = mock(ConnectionSession.class);
        executeResult = new SqlExecuteResult();
        context = new HashMap<>();
    }

    @Test
    public void testDoPreHandle() {
        boolean interceptedResult = interceptor.doPreHandle(request, response, session, context);
        assertFalse(interceptedResult);
    }

    @Test
    public void testDetermineSqlType() {
        SqlTuple sqlTuple = mock(SqlTuple.class);
        BasicResult result = interceptor.determineSqlType(sqlTuple, DialectType.OKSQL);
        assertNotNull(result);
    }

    @Test
    public void testGetOrder() {
        int expectedOrder = 2;
        assertEquals(expectedOrder, interceptor.getOrder());
    }

    @Test(expected = SyntaxErrorException.class)
    public void testDetermineSqlTypeThrowsException() {
        SqlTuple sqlTuple = mock(SqlTuple.class);
        doThrow(SyntaxErrorException.class).when(sqlTuple).initAst(any());
        interceptor.determineSqlType(sqlTuple, DialectType.OKSQL);
    }
    
    @Test
    public void testDoAfterCompletion() {
        interceptor.doAfterCompletion(executeResult, session, context);
        assertTrue(executeResult.isAllowExport());
    }

    @Test
    public void testHandle() {
        context.put(SqlConsoleInterceptor.NEED_SQL_CONSOLE_CHECK, Boolean.FALSE);
        boolean allowExecute = interceptor.handle(request, response, session, context);
        assertTrue(allowExecute);
    }

    @Test
    public void testHandleWithRuleViolation() {
        context.put(SqlConsoleInterceptor.NEED_SQL_CONSOLE_CHECK, Boolean.TRUE);
        ConnectionSessionUtil.setRuleSetId(session, 10L);
        List<SqlTuple> tuples = new ArrayList<>();
        response.setSqls(tuples);
        boolean allowExecute = interceptor.handle(request, response, session, context);
        assertFalse(allowExecute);
    }

    @Test
    public void testGetExecuteStageName() {
        String expectedResult = "sql_console_rule";
        assertEquals(expectedResult, interceptor.getExecuteStageName());
    }
}
