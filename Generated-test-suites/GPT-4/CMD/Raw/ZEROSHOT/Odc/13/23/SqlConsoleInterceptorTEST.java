
import com.oceanbase.odc.core.session.ConnectionSession;
import com.oceanbase.odc.core.shared.constant.DialectType;
import com.oceanbase.odc.core.sql.execute.model.SqlExecuteStatus;
import com.oceanbase.odc.core.sql.execute.model.SqlTuple;
import com.oceanbase.odc.service.iam.auth.AuthenticationFacade;
import com.oceanbase.odc.service.session.model.SqlAsyncExecuteReq;
import com.oceanbase.odc.service.session.model.SqlAsyncExecuteResp;
import com.oceanbase.odc.service.session.model.SqlExecuteResult;
import com.oceanbase.tools.dbbrowser.parser.result.BasicResult;
import com.oceanbase.tools.dbbrowser.parser.constant.SqlType;
import org.junit.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.MockitoJUnit;
import org.mockito.junit.MockitoRule;
import org.junit.Rule;
import org.junit.Assert;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;

import java.util.HashMap;
import java.util.Map;
import java.util.Optional;

public class SqlConsoleInterceptorTest {
    
    @Rule
    public MockitoRule rule = MockitoJUnit.rule();

    @InjectMocks
    private SqlConsoleInterceptor sqlConsoleInterceptor;

    @Mock
    private AuthenticationFacade authenticationFacade;

    @Mock
    private ConnectionSession connectionSession;

    @Mock
    private SqlAsyncExecuteReq sqlAsyncExecuteReq;

    @Mock
    private SqlAsyncExecuteResp sqlAsyncExecuteResp;

    @Mock
    private SqlExecuteResult sqlExecuteResult;

    @Mock
    private SqlTuple sqlTuple;

    @Mock
    private BasicResult basicResult;

    @Test
    public void testGetOrder() {
        int order = sqlConsoleInterceptor.getOrder();
        Assert.assertEquals(2, order);
    }

    @Test
    public void testIsIndividualTeam() {
        when(authenticationFacade.currentUser().getOrganizationType()).thenReturn(
                OrganizationType.INDIVIDUAL);
        boolean isIndividual = sqlConsoleInterceptor.isIndividualTeam();
        Assert.assertTrue(isIndividual);
    }

    @Test
    public void testDetermineSqlType() {
        when(sqlTuple.getAst()).thenReturn(null);
        when(sqlTuple.initAst(AbstractSyntaxTreeFactories.getAstFactory(DialectType.MYSQL, 0))).thenReturn(null);
        BasicResult result = sqlConsoleInterceptor.determineSqlType(sqlTuple, DialectType.MYSQL);
        Assert.assertEquals(SqlType.OTHERS, result.getSqlType());
    }
}
