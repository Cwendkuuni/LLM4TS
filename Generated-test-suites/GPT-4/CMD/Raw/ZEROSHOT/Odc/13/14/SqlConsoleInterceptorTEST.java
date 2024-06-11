
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import static org.junit.Assert.*;
import static org.mockito.Mockito.*;

import java.util.*;

import com.oceanbase.odc.service.iam.auth.AuthenticationFacade;
import com.oceanbase.odc.service.regulation.ruleset.RuleService;
import com.oceanbase.odc.service.regulation.ruleset.SqlConsoleRuleService;
import com.oceanbase.odc.service.session.model.*;

@RunWith(SpringJUnit4ClassRunner.class)
public class SqlConsoleInterceptorTest {

    @InjectMocks
    private SqlConsoleInterceptor sqlConsoleInterceptor;

    @Mock
    private AuthenticationFacade authenticationFacade;
    @Mock
    private RuleService ruleService;
    @Mock
    private SqlConsoleRuleService sqlConsoleRuleService;
    @Mock
    private SqlAsyncExecuteReq sqlAsyncExecuteReq;
    @Mock
    private SqlAsyncExecuteResp sqlAsyncExecuteResp;
    @Mock
    private ConnectionSession connectionSession;

    @Before
    public void setUp() {
        MockitoAnnotations.initMocks(this);
    }

    @Test
    public void testDoPreHandle() {
        Map<String, Object> context = new HashMap<>();
        when(connectionSession.getDialectType()).thenReturn(DialectType.MYSQL);
        assertTrue(sqlConsoleInterceptor.doPreHandle(sqlAsyncExecuteReq, sqlAsyncExecuteResp, connectionSession, context));
    }

    @Test
    public void testHandle() {
        Map<String, Object> context = new HashMap<>();
        when(connectionSession.getDialectType()).thenReturn(DialectType.MYSQL);
        assertTrue(sqlConsoleInterceptor.handle(sqlAsyncExecuteReq, sqlAsyncExecuteResp, connectionSession, context));
    }

    @Test
    public void testGetExecuteStageName() {
        assertEquals(SqlExecuteStages.SQL_CONSOLE_RULE, sqlConsoleInterceptor.getExecuteStageName());
    }

    @Test
    public void testDoAfterCompletion() {
        Map<String, Object> context = new HashMap<>();
        SqlExecuteResult sqlExecuteResult = new SqlExecuteResult();
        sqlExecuteResult.setStatus(SqlExecuteStatus.SUCCESS);
        sqlConsoleInterceptor.doAfterCompletion(sqlExecuteResult, connectionSession, context);
        assertTrue(sqlExecuteResult.isAllowExport());
    }

    @Test
    public void testIsIndividualTeam() {
        when(authenticationFacade.currentUser()).thenReturn(new User(OrganizationType.INDIVIDUAL));
        assertTrue(sqlConsoleInterceptor.isIndividualTeam());
    }

    @Test
    public void testDetermineSqlType() {
        SqlTuple sqlTuple = new SqlTuple();
        sqlTuple.setSqlId("1");
        sqlTuple.setOriginalSql("SELECT * FROM TEST");
        BasicResult basicResult = sqlConsoleInterceptor.determineSqlType(sqlTuple, DialectType.MYSQL);
        assertEquals(SqlType.SELECT, basicResult.getSqlType());
    }

    @Test
    public void testGetOrder() {
        assertEquals(2, sqlConsoleInterceptor.getOrder());
    }
}
