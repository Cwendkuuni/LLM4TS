import static org.junit.Assert.*;
import static org.mockito.Mockito.*;

import java.sql.ResultSetMetaData;
import java.util.Arrays;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;

import org.junit.Before;
import org.junit.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import com.oceanbase.odc.core.session.ConnectionSession;
import com.oceanbase.odc.core.session.ConnectionSessionUtil;
import com.oceanbase.odc.core.session.model.SqlAsyncExecuteResp;
import com.oceanbase.odc.core.session.model.SqlExecuteResult;
import com.oceanbase.odc.core.sql.execute.model.SqlExecuteStatus;
import com.oceanbase.odc.core.sql.execute.model.SqlTuple;
import com.oceanbase.odc.core.sql.parser.AbstractSyntaxTree;
import com.oceanbase.odc.core.sql.parser.AbstractSyntaxTreeFactories;
import com.oceanbase.odc.service.iam.auth.AuthenticationFacade;
import com.oceanbase.odc.service.iam.model.CurrentUser;
import com.oceanbase.odc.service.regulation.ruleset.RuleService;
import com.oceanbase.odc.service.regulation.ruleset.SqlConsoleRuleService;
import com.oceanbase.odc.service.regulation.ruleset.model.Rule;
import com.oceanbase.odc.service.regulation.ruleset.model.RuleViolation;
import com.oceanbase.odc.service.regulation.ruleset.model.SqlConsoleRules;
import com.oceanbase.odc.service.session.interceptor.BaseTimeConsumingInterceptor;
import com.oceanbase.odc.service.session.interceptor.SqlConsoleInterceptor;
import com.oceanbase.tools.dbbrowser.parser.constant.SqlType;
import com.oceanbase.tools.sqlparser.SyntaxErrorException;

/**
 * ClassName: SqlConsoleInterceptorTest <br/>
 * Description: <br/>
 * date: 2023/7/17 16:27 <br/>
 *
 * @author Lebie
 * @version 1.0.0
 * @since JDK 1.8
 */
@Slf4j
public class SqlConsoleInterceptorTest {

    @Mock
    private AuthenticationFacade authenticationFacade;
    @Mock
    private RuleService ruleService;
    @Mock
    private SqlConsoleRuleService sqlConsoleRuleService;

    @InjectMocks
    private SqlConsoleInterceptor sqlConsoleInterceptor;

    private ConnectionSession connectionSession;
    private CurrentUser currentUser;

    @Before
    public void setup() {
        MockitoAnnotations.initMocks(this);
        connectionSession = mock(ConnectionSession.class);
        currentUser = new CurrentUser();
        currentUser.setOrganizationType(OrganizationType.TEAM);
        when(authenticationFacade.currentUser()).thenReturn(currentUser);
    }

    @Test
    public void testHandleTrue() {
        // arrange
        Long ruleSetId = 1L;
        when(ConnectionSessionUtil.getRuleSetId(connectionSession)).thenReturn(ruleSetId);
        SqlAsyncExecuteResp sqlAsyncExecuteResp = new SqlAsyncExecuteResp();

        // act
        boolean actual = sqlConsoleInterceptor.handle(
                mock(SqlAsyncExecuteReq.class), sqlAsyncExecuteResp, connectionSession, new HashMap<>());

        // assert
        assertTrue(actual);
    }

    @Test
    public void testHandleFalse() {
        // arrange
        Long ruleSetId = 1L;
        when(ConnectionSessionUtil.getRuleSetId(connectionSession)).thenReturn(ruleSetId);
        SqlAsyncExecuteResp sqlAsyncExecuteResp = new SqlAsyncExecuteResp();
        sqlAsyncExecuteResp.setSqls(Arrays.asList(mock(SqlTuplesWithViolation.class)));

        // act
        boolean actual = sqlConsoleInterceptor.handle(
                mock(SqlAsyncExecuteReq.class), sqlAsyncExecuteResp, connectionSession, new HashMap<>());

        // assert
        assertFalse(actual);
    }

    @Test
    public void testDoPreHandleTrue() {
        // arrange
        Long ruleSetId = 1L;
        when(ConnectionSessionUtil.getRuleSetId(connectionSession)).thenReturn(ruleSetId);

        // act
        Map<String, Object> context = new HashMap<>();
        sqlConsoleInterceptor.doPreHandle(mock(SqlAsyncExecuteReq.class), mock(SqlAsyncExecuteResp.class),
                connectionSession, context);

        // assert
        assertTrue(context.get(SqlConsoleInterceptor.SQL_CONSOLE_INTERCEPTED));
    }

    @Test
    public void testDoPreHandleFalse() {
        // arrange
        Long ruleSetId = 1L;
        when(ConnectionSessionUtil.getRuleSetId(connectionSession)).thenReturn(ruleSetId);
        Map<String, Object> context = new HashMap<>();
        context.put(SqlCheckInterceptor.SQL_CHECK_INTERCEPTED, true);

        // act
        sqlConsoleInterceptor.doPreHandle(mock(SqlAsyncExecuteReq.class), mock(SqlAsyncExecuteResp.class),
                connectionSession, context);

        // assert
        assertTrue(context.get(SqlConsoleInterceptor.SQL_CONSOLE_INTERCEPTED) && context.get(SqlCheckInterceptor.SQL_CHECK_INTERCEPTED));
    }

    @Test
    public void testDoPreHandleFalseNoRuleSet() {
        // arrange
        when(ConnectionSessionUtil.getRuleSetId(connectionSession)).thenReturn(null);

        // act
        Map<String, Object> context = new HashMap<>();

        // assert
        sqlConsoleInterceptor.doPreHandle(mock(SqlAsyncExecuteReq.class), mock(SqlAsyncExecuteResp.class),
                connectionSession, context);

        // assert
        assertFalse(context.containsKey(SqlConsoleInterceptor.SQL_CONSOLE_INTERCEPTED));
    }

    @Test
    public void testDoAfterCompletion() {
        // arrange
        SqlExecuteResult sqlExecuteResult = new SqlExecuteResult();
        sqlExecuteResult.setStatus(SqlExecuteStatus.SUCCESS);
        sqlExecuteResult.setAllowExport(true);

        // act
        sqlConsoleInterceptor.doAfterCompletion(sqlExecuteResult, connectionSession, new HashMap<>());

        // assert
        assertTrue(sqlExecuteResult.isAllowExport());
    }

    @Test
    public void testDoAfterCompletionFalse() {
        // arrange
        SqlExecuteResult sqlExecuteResult = new SqlExecuteResult();
        sqlExecuteResult.setStatus(SqlExecuteStatus.SUCCESS);

        // act
        when(sqlConsoleRuleService.isForbidden(SqlConsoleRules.NOT_ALLOWED_EDIT_RESULTSET, connectionSession))
                .thenReturn(true);
        sqlConsoleInterceptor.doAfterCompletion(sqlExecuteResult, connectionSession, new HashMap<>());

        // assert
        assertFalse(sqlExecuteResult.isAllowExport());

        verify(sqlExecuteResult).setAllowExport(false);
    }

    @Test
    public void testDoAfterCompletionAllowEdit() {
        // arrange
        SqlExecuteResult sqlExecuteResult = new SqlExecuteResult();
        sqlExecuteResult.setStatus(SqlExecuteStatus.SUCCESS);

        // act
        when(sqlConsoleRuleService.isForbidden(SqlConsoleRules.NOT_ALLOWED_EDIT_RESULTSET, connectionSession))
                .thenReturn(false);
        sqlConsoleInterceptor.doAfterCompletion(sqlExecuteResult, connectionSession, new HashMap<>());

        // assert
        assertTrue(sqlExecuteResult.isAllowExport());
    }

    @Test
    public void testGetExecuteStageName() {
        // act
        String actual = sqlConsoleInterceptor.getExecuteStageName();

        // assert
        assertEquals(SqlExecuteStages.SQL_CONSOLE_RULE, actual);
    }

    @Test
    public void testDetermineSqlTypeSuccess() {
        // arrange
        SqlTuple sqlTuple = new SqlTuple();
        sqlTuple.setAst(mock(AbstractSyntaxTree.class));
        sqlTuple.setOriginalSql("SELECT * FROM user WHERE name = 'test'");

        // act
        BasicResult actual = sqlConsoleInterceptor.determineSqlType(sqlTuple, DialectType.MYSQL);

        // assert
        assertEquals(SqlType.SELECT, actual.getSqlType());
    }

    @Test
    public void testDetermineSqlTypeFail() {
        // arrange
        SqlTuple sqlTuple = new SqlTuple();
        sqlTuple.setAst(null);
        sqlTuple.setOriginalSql("SELECT * FROM user WHERE name = 'test'");

        // act
        BasicResult actual = sqlConsoleInterceptor.determineSqlType(sqlTuple, DialectType.MYSQL);

        // assert
        assertEquals(SqlType.OTHERS, actual.getSqlType());
    }

    @Test
    public void testDetermineSqlTypeSyntaxError() {
        // arrange
        SqlTuple sqlTuple = new SqlTuple();
        sqlTuple.setAst(mock(AbstractSyntaxTree.class));
        sqlTuple.setOriginalSql("SELECT * FROM user WHERE name = test");
        doThrow(new SyntaxErrorException("test")).when(sqlTuple.getAst()).getParseResult();

        // act
        BasicResult actual = sqlConsoleInterceptor.determineSqlType(sqlTuple, DialectType.MYSQL);

        // assert
        assertEquals(SqlType.OTHERS, actual.getSqlType());
        assertTrue(actual.isSyntaxError());
    }
}