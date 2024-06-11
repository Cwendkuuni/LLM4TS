
package com.oceanbase.odc.service.session.interceptor;

import com.oceanbase.odc.core.session.ConnectionSession;
import com.oceanbase.odc.core.session.ConnectionSessionUtil;
import com.oceanbase.odc.core.sql.execute.SqlExecuteStages;
import com.oceanbase.odc.service.iam.auth.AuthenticationFacade;
import com.oceanbase.odc.service.regulation.ruleset.SqlConsoleRuleService;
import com.oceanbase.odc.service.session.model.SqlAsyncExecuteReq;
import com.oceanbase.odc.service.session.model.SqlAsyncExecuteResp;
import com.oceanbase.odc.service.session.model.SqlExecuteResult;

import org.junit.Before;
import org.junit.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.junit.Assert;
import java.util.HashMap;

import static org.mockito.Mockito.*;

public class SqlConsoleInterceptorTest {
    @Mock
    private AuthenticationFacade authenticationFacade;
    @Mock
    private SqlConsoleRuleService sqlConsoleRuleService;

    @InjectMocks
    private SqlConsoleInterceptor underTest;

    @Before
    public void setUp() {
        MockitoAnnotations.initMocks(this);
    }

    @Test
    public void doPreHandleTest() {
        SqlAsyncExecuteReq request = mock(SqlAsyncExecuteReq.class);
        SqlAsyncExecuteResp response = mock(SqlAsyncExecuteResp.class);
        ConnectionSession session = mock(ConnectionSession.class);
        HashMap<String, Object> context = new HashMap<>();

        Boolean result = underTest.doPreHandle(request, response, session, context);

        Assert.assertTrue(result);
        // Add your assert and verify checks here.
    }

    @Test
    public void handleTest() {
        // Similar to doPreHandleTest method above, add your initialization for input parameters here

        Boolean result = underTest.handle(request, response, session, context);

        Assert.assertNotNull(result);
        // Add your assert and verify checks here.
    }
    
    @Test
    public void getExecuteStageNameTest() {
        String result = underTest.getExecuteStageName();
        
        Assert.assertEquals(SqlExecuteStages.SQL_CONSOLE_RULE, result);
    }
    
    @Test
    public void doAfterCompletionTest() {
        SqlExecuteResult response = mock(SqlExecuteResult.class);
        ConnectionSession session = mock(ConnectionSession.class);
        HashMap<String, Object> context = new HashMap<>();

        underTest.doAfterCompletion(response, session, context);
        
        // Add verify checks for your method calls here,
        // For instance `verify(session, times(1)).getMethod();`
    }
    
    @Test
    public void isIndividualTeamTest() {
        Boolean result = underTest.isIndividualTeam();

        Assert.assertNotNull(result);
        // Add your assert and verify checks here.
    }
    
    @Test
    public void determineSqlTypeTest() {
        // Similar to handleTest method above, add your initialization for input parameters here

        BasicResult result = underTest.determineSqlType(sqlTuple, dialectType);
        
        Assert.assertNotNull(result);
        // Add your assert and verify checks here.
    }

    @Test
    public void getOrderTest() {
        int result = underTest.getOrder();
        
        Assert.assertEquals(2, result);
    }
}
