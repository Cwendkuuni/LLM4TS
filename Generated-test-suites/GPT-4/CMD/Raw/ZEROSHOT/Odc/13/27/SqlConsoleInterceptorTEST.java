
import com.oceanbase.odc.service.session.model.SqlTuplesWithViolation;
import com.oceanbase.odc.core.shared.constant.DialectType;
import com.oceanbase.odc.core.sql.execute.model.SqlExecuteStatus;
import com.oceanbase.odc.core.sql.execute.model.SqlExecuteResult;
import com.oceanbase.odc.core.sql.parser.AbstractSyntaxTree;
import com.oceanbase.odc.core.sql.parser.AbstractSyntaxTreeFactories;
import com.oceanbase.odc.service.session.model.SqlAsyncExecuteReq;
import com.oceanbase.odc.service.session.model.SqlAsyncExecuteResp;
import com.oceanbase.odc.service.session.model.SqlTuple;
import com.oceanbase.tools.sqlparser.SyntaxErrorException;
import com.oceanbase.odc.service.session.ConnectionSession;
import com.oceanbase.odc.service.regulation.ruleset.model.Rule;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.runners.MockitoJUnitRunner;

import java.util.*;

import static org.junit.Assert.*;
import static org.mockito.Mockito.*;

@RunWith(MockitoJUnitRunner.class)
public class SqlConsoleInterceptorTest {

    @InjectMocks
    private SqlConsoleInterceptor sqlConsoleInterceptor;

    @Mock
    private ConnectionSession connectionSession;

    @Mock
    private SqlAsyncExecuteReq request;

    @Mock
    private SqlAsyncExecuteResp response;

    @Mock
    private SqlExecuteResult result;

    @Before
    public void setUp() {
        when(connectionSession.getDialectType()).thenReturn(DialectType.STANDARD);
        when(request.getQueryLimit()).thenReturn(100);
        when(response.getStatus()).thenReturn(SqlExecuteStatus.SUCCESS);
    }

    @Test
    public void testPreHandle() {
        Map<String, Object> context = new HashMap<>();
        boolean result = sqlConsoleInterceptor.doPreHandle(request, response, connectionSession, context);
        assertTrue(result);
    }

    @Test
    public void testHandle() {
        Map<String, Object> context = new HashMap<>();
        boolean result = sqlConsoleInterceptor.doPreHandle(request, response, connectionSession, context);
        assertTrue(result);
    }

    @Test
    public void testAfterCompletion() {
        Map<String, Object> context = new HashMap<>();
        sqlConsoleInterceptor.doAfterCompletion(result, connectionSession, context);
        verify(result, atLeastOnce()).setStatus(any());
    }

    @Test
    public void testIsIndividualTeam() {
        assertFalse(sqlConsoleInterceptor.isIndividualTeam());
    }

    @Test
    public void testDetermineSqlType() {
        SqlTuple sqlTuple = new SqlTuple();
        try{
            sqlConsoleInterceptor.determineSqlType(sqlTuple, DialectType.STANDARD);
        }
        catch (Exception e){
            assertTrue(e instanceof SyntaxErrorException);
        }
    }

    @Test
    public void testGetOrder() {
        assertEquals(2, sqlConsoleInterceptor.getOrder());
    }
}
