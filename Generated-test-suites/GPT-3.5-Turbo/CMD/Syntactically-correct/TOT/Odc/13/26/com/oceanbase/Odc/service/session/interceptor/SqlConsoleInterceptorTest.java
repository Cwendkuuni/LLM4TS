import org.junit.Before;
import org.junit.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import java.util.HashMap;
import java.util.Map;

import static org.junit.Assert.*;
import static org.mockito.Mockito.when;

public class SqlConsoleInterceptorTest {

    @Mock
    private AuthenticationFacade authenticationFacade;
    
    @Mock
    private RuleService ruleService;
    
    @Mock
    private SqlConsoleRuleService sqlConsoleRuleService;
    
    @InjectMocks
    private SqlConsoleInterceptor sqlConsoleInterceptor;

    @Before
    public void setUp() {
        MockitoAnnotations.initMocks(this);
    }

    @Test
    public void testDoPreHandle() {
        SqlAsyncExecuteReq request = new SqlAsyncExecuteReq();
        SqlAsyncExecuteResp response = new SqlAsyncExecuteResp();
        ConnectionSession session = new ConnectionSession();
        Map<String, Object> context = new HashMap<>();

        when(sqlConsoleRuleService.isForbidden("NOT_ALLOWED_CREATE_PL", session)).thenReturn(false);
        when(sqlConsoleRuleService.isForbidden("ALLOW_SQL_TYPES", session)).thenReturn(true);

        boolean result = sqlConsoleInterceptor.doPreHandle(request, response, session, context);

        assertTrue(result);
    }

    @Test
    public void testDoAfterCompletion() {
        SqlExecuteResult response = new SqlExecuteResult();
        ConnectionSession session = new ConnectionSession();
        Map<String, Object> context = new HashMap<>();

        when(authenticationFacade.currentUser()).thenReturn(new User("JohnDoe", OrganizationType.COMPANY));

        sqlConsoleInterceptor.doAfterCompletion(response, session, context);

        assertTrue(response.isAllowExport());
    }

    @Test
    public void testDetermineSqlTypeWithSyntaxError() {
        SqlTuple sqlTuple = new SqlTuple("SELECT * FROM table;", "id-123");
        DialectType dialectType = DialectType.MYSQL;

        BasicResult result = sqlConsoleInterceptor.determineSqlType(sqlTuple, dialectType);

        assertNotNull(result);
        assertEquals(SqlType.UNKNOWN, result.getSqlType());
        assertTrue(result.isSyntaxError());
    }

}