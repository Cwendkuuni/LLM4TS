import static org.junit.Assert.*;
import static org.mockito.Mockito.*;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.concurrent.atomic.AtomicBoolean;
import java.util.concurrent.atomic.AtomicReference;

import org.junit.Before;
import org.junit.Test;

import com.oceanbase.odc.core.session.ConnectionSession;
import com.oceanbase.odc.core.shared.constant.DialectType;
import com.oceanbase.odc.service.session.interceptor.SqlConsoleInterceptor;
import com.oceanbase.odc.service.session.model.SqlAsyncExecuteReq;
import com.oceanbase.odc.service.session.model.SqlAsyncExecuteResp;
import com.oceanbase.odc.tools.dbbrowser.parser.result.BasicResult;
import com.oceanbase.tools.dbbrowser.parser.constant.SqlType;
import com.oceanbase.odc.core.sql.execute.model.SqlTuple;
import com.oceanbase.odc.core.sql.execute.model.SqlExecuteResult;
import com.oceanbase.odc.core.sql.execute.SqlExecuteStages;
import com.oceanbase.odc.core.sql.execute.model.SqlExecuteStatus;

public class SqlConsoleInterceptorTest {

    private SqlConsoleInterceptor sqlConsoleInterceptor;
    private SqlAsyncExecuteReq request;
    private SqlAsyncExecuteResp response;
    private ConnectionSession session;
    private Map<String, Object> context;

    @Before
    public void setup() {
        sqlConsoleInterceptor = new SqlConsoleInterceptor();
        request = mock(SqlAsyncExecuteReq.class);
        response = mock(SqlAsyncExecuteResp.class);
        session = mock(ConnectionSession.class);
        context = new HashMap<>();
    }

    @Test
    public void testDoPreHandle_AllowsExecution() {
        // Given
        when(session.getRuleSetId()).thenReturn(1L);
        when(sqlConsoleRuleService.getProperties(eq(1L), eq(SqlConsoleRules.MAX_EXECUTE_SQLS), any(DialectType.class), eq(Integer.class)))
            .thenReturn(Optional.of(5));

        // When
        boolean result = sqlConsoleInterceptor.doPreHandle(request, response, session, context);

        // Then
        assertTrue(result);
    }

    @Test
    public void testDoPreHandle_DeniesExecution() {
        // Given
        when(session.getRuleSetId()).thenReturn(1L);
        when(sqlConsoleRuleService.getProperties(eq(1L), eq(SqlConsoleRules.MAX_EXECUTE_SQLS), any(DialectType.class), eq(Integer.class)))
            .thenReturn(Optional.of(3));

        // When
        boolean result = sqlConsoleInterceptor.doPreHandle(request, response, session, context);

        // Then
        assertFalse(result);
    }

    @Test
    public void testDoAfterCompletion_AllowsExport() {
        // Given
        SqlExecuteResult executeResult = new SqlExecuteResult();
        executeResult.setStatus(SqlExecuteStatus.SUCCESS);
        when(session.getRuleSetId()).thenReturn(1L);
        when(sqlConsoleRuleService.isForbidden(eq(SqlConsoleRules.NOT_ALLOWED_EXPORT_RESULTSET), eq(session)))
            .thenReturn(false);

        // When
        sqlConsoleInterceptor.doAfterCompletion(executeResult, session, context);

        // Then
        assertTrue(executeResult.isAllowExport());
    }

    @Test
    public void testDoAfterCompletion_DeniesExport() {
        // Given
        SqlExecuteResult executeResult = new SqlExecuteResult();
        executeResult.setStatus(SqlExecuteStatus.SUCCESS);
        when(session.getRuleSetId()).thenReturn(1L);
        when(sqlConsoleRuleService.isForbidden(eq(SqlConsoleRules.NOT_ALLOWED_EXPORT_RESULTSET), eq(session)))
            .thenReturn(true);

        // When
        sqlConsoleInterceptor.doAfterCompletion(executeResult, session, context);

        // Then
        assertFalse(executeResult.isAllowExport());
    }

    @Test
    public void testDetermineSqlType_ReturnsBasicResult() {
        // Given
        SqlTuple sqlTuple = new SqlTuple();
        sqlTuple.setAst(mock(AbstractSyntaxTree.class));
        when(sqlTuple.getAst().getParseResult()).thenReturn(new BasicResult(SqlType.SELECT));

        // When
        BasicResult result = sqlConsoleInterceptor.determineSqlType(sqlTuple, DialectType.MYSQL);

        // Then
        assertNotNull(result);
        assertEquals(SqlType.SELECT, result.getSqlType());
    }
}