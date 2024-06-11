import static org.mockito.Mockito.*;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.concurrent.atomic.AtomicBoolean;
import java.util.concurrent.atomic.AtomicReference;

import org.junit.Before;
import org.junit.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import com.oceanbase.odc.core.session.ConnectionSession;
import com.oceanbase.odc.core.shared.constant.DialectType;
import com.oceanbase.odc.service.iam.auth.AuthenticationFacade;
import com.oceanbase.odc.service.regulation.ruleset.RuleService;
import com.oceanbase.odc.service.regulation.ruleset.SqlConsoleRuleService;
import com.oceanbase.odc.service.session.interceptor.SqlConsoleInterceptor;
import com.oceanbase.odc.service.session.model.SqlAsyncExecuteReq;
import com.oceanbase.odc.service.session.model.SqlAsyncExecuteResp;
import com.oceanbase.odc.service.session.model.SqlExecuteResult;
import com.oceanbase.odc.service.session.model.SqlTuplesWithViolation;

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
    private ConnectionSession connectionSession;

    @Before
    public void setup() {
        MockitoAnnotations.initMocks(this);
    }

    @Test
    public void testDoPreHandle() {
        SqlAsyncExecuteReq request = new SqlAsyncExecuteReq();
        SqlAsyncExecuteResp response = new SqlAsyncExecuteResp();
        Map<String, Object> context = new HashMap<>();
        when(connectionSession.getDialectType()).thenReturn(DialectType.STANDARD);
        when(connectionSession.getRuleSetId()).thenReturn(123L);
        when(sqlConsoleRuleService.isForbidden(any(), any())).thenReturn(false);

        boolean result = sqlConsoleInterceptor.doPreHandle(request, response, connectionSession, context);

        assertTrue(result);
    }

    @Test
    public void testDoAfterCompletion() {
        SqlExecuteResult response = new SqlExecuteResult();
        Map<String, Object> context = new HashMap<>();
        when(connectionSession.getOrganizationType()).thenReturn(OrganizationType.INDIVIDUAL);
        response.setStatus(SqlExecuteStatus.SUCCESS);

        sqlConsoleInterceptor.doAfterCompletion(response, connectionSession, context);

        assertTrue(response.isAllowExport());
    }

    @Test
    public void testIsIndividualTeam() {
        when(authenticationFacade.currentUser().getOrganizationType()).thenReturn(OrganizationType.INDIVIDUAL);

        boolean result = sqlConsoleInterceptor.isIndividualTeam();

        assertTrue(result);
    }

}