
import com.oceanbase.odc.core.session.ConnectionSession;
import com.oceanbase.odc.core.shared.constant.DialectType;
import com.oceanbase.odc.core.sql.execute.model.SqlExecuteStatus;
import com.oceanbase.odc.core.sql.execute.model.SqlTuple;
import com.oceanbase.odc.service.session.model.SqlAsyncExecuteReq;
import com.oceanbase.odc.service.session.model.SqlAsyncExecuteResp;
import com.oceanbase.odc.service.session.model.SqlExecuteResult;
import com.oceanbase.tools.dbbrowser.parser.result.BasicResult;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.MockitoJUnitRunner;
import java.util.HashMap;
import static org.mockito.Mockito.when;

@RunWith(MockitoJUnitRunner.class)
public class SqlConsoleInterceptorTest {

    @InjectMocks
    private SqlConsoleInterceptor interceptor;

    @Mock
    private AuthenticationFacade authenticationFacade;

    @Mock
    private RuleService ruleService;

    @Mock
    private SqlConsoleRuleService sqlConsoleRuleService;

    @Mock
    private ConnectionSession connectionSession;

    @Mock
    private SqlAsyncExecuteReq request;

    @Mock
    private SqlAsyncExecuteResp response;

    private HashMap<String, Object> context;

    @Before
    public void setup() {
        context = new HashMap<>();
    }

    @Test
    public void testDoPreHandle() {
        boolean result = interceptor.doPreHandle(request, response, connectionSession, context);
        Assert.assertTrue(result);
    }

    @Test
    public void testDoAfterCompletion() {
        SqlExecuteResult response = new SqlExecuteResult();
        response.setStatus(SqlExecuteStatus.SUCCESS);
        interceptor.doAfterCompletion(response, connectionSession, context);
        Assert.assertTrue(!response.isAllowExport());
    }

    @Test
    public void testGetOrder() {
        int order = interceptor.getOrder();
        Assert.assertEquals(2, order);
    }

    @Test
    public void testDetermineSqlType() {
        SqlTuple sqlTuple = new SqlTuple();
        sqlTuple.setAst(null);
        sqlTuple.setSqlId("test");
        BasicResult expectedBasicResult = new BasicResult();
        BasicResult actualBasicResult = interceptor.determineSqlType(sqlTuple, DialectType.MYSQL);
        Assert.assertEquals(expectedBasicResult.getSqlType(), actualBasicResult.getSqlType());
    }
}
