
import com.oceanbase.odc.core.session.ConnectionSession;
import com.oceanbase.odc.core.shared.constant.DialectType;
import com.oceanbase.odc.core.shared.constant.OrganizationType;
import com.oceanbase.odc.service.iam.auth.AuthenticationFacade;
import com.oceanbase.odc.service.session.model.SqlAsyncExecuteReq;
import com.oceanbase.odc.service.session.model.SqlAsyncExecuteResp;
import com.oceanbase.odc.service.session.model.SqlTuple;
import com.oceanbase.tools.dbbrowser.parser.constant.SqlType;
import com.oceanbase.tools.sqlparser.SyntaxErrorException;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.context.junit4.SpringRunner;

import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;

import static org.junit.Assert.*;
import static org.mockito.Mockito.*;

@RunWith(SpringRunner.class)
@SpringBootTest
public class SqlConsoleInterceptorTest {

    @Autowired
    private SqlConsoleInterceptor sqlConsoleInterceptor;

    @MockBean
    private ConnectionSession connectionSession;

    @MockBean
    private AuthenticationFacade authenticationFacade;

    @MockBean
    private SqlAsyncExecuteReq request;

    @MockBean
    private SqlAsyncExecuteResp response;

    @Test
    public void testDoPreHandle() {
        Map<String, Object> context = new HashMap<>();
        SqlTuple sqlTuple = new SqlTuple();
        response.setSqls(Arrays.asList(sqlTuple));

        when(authenticationFacade.currentUser()).thenReturn(null);

        boolean result = sqlConsoleInterceptor.doPreHandle(request, response, connectionSession, context);
        assertEquals(true, result);
    }

    @Test
    public void testGetExecuteStageName() {
        String result = sqlConsoleInterceptor.getExecuteStageName();
        assertEquals("SQL_CONSOLE_RULE", result);
    }

    @Test
    public void testDoAfterCompletion() {
        when(authenticationFacade.currentUser()).thenReturn(OrganizationType.INDIVIDUAL);

        sqlConsoleInterceptor.doAfterCompletion(null, connectionSession, new HashMap<String, Object>());
        verifyNoInteractions(connectionSession);
    }

    @Test
    public void testIsIndividualTeam() {
        when(authenticationFacade.currentUser()).thenReturn(OrganizationType.INDIVIDUAL);

        boolean result = sqlConsoleInterceptor.isIndividualTeam();
        assertEquals(true, result);
    }

    @Test
    public void testDetermineSqlType() {
        SqlTuple sqlTuple = new SqlTuple();
        sqlTuple.initAst(null);

        BasicResult result = sqlConsoleInterceptor.determineSqlType(sqlTuple, DialectType.MYSQL);
        assertEquals(SqlType.UNKNOWN, result.getSqlType());
    }

    @Test
    public void testGetOrder() {
        int result = sqlConsoleInterceptor.getOrder();
        assertEquals(2, result);
    }

    @Test(expected = SyntaxErrorException.class)
    public void testDetermineSqlTypeThrowsSyntaxErrorException() {
        SqlTuple sqlTuple = mock(SqlTuple.class);

        sqlConsoleInterceptor.determineSqlType(sqlTuple, DialectType.MYSQL);
    }
}
