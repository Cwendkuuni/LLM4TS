import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertTrue;
import static org.junit.Assert.assertFalse;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.concurrent.atomic.AtomicReference;

import org.junit.Before;
import org.junit.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import com.oceanbase.odc.core.session.ConnectionSession;
import com.oceanbase.odc.core.session.ConnectionSessionUtil;
import com.oceanbase.odc.core.shared.constant.DialectType;
import com.oceanbase.odc.service.iam.auth.AuthenticationFacade;
import com.oceanbase.odc.service.regulation.ruleset.RuleService;
import com.oceanbase.odc.service.regulation.ruleset.SqlConsoleRuleService;
import com.oceanbase.odc.service.session.model.SqlAsyncExecuteReq;
import com.oceanbase.odc.service.session.model.SqlAsyncExecuteResp;
import com.oceanbase.odc.service.session.model.SqlExecuteResult;
import com.oceanbase.odc.service.session.model.SqlTuple;
import com.oceanbase.odc.service.session.model.SqlTuplesWithViolation;

import static org.mockito.Mockito.when;

public class SqlConsoleInterceptorTest {

	@InjectMocks
	SqlConsoleInterceptor interceptor;

	@Mock
	AuthenticationFacade authenticationFacade;

	@Mock
	RuleService ruleService;

	@Mock
	SqlConsoleRuleService sqlConsoleRuleService;

	@Mock
	ConnectionSession session;

	@Mock
	SqlAsyncExecuteReq request;

	@Mock
	SqlAsyncExecuteResp response;

	@Mock
	SqlExecuteResult executeResult;

	@Mock
	ConnectionSessionUtil connectionSessionUtil;

	@Before  
	public void initMocks() {
		MockitoAnnotations.initMocks(this);
	}

	@Test
	public void testDoPreHandle() {
		Map<String, Object> context = new HashMap<>();
		
		when(interceptor.handle(request, response, session, context)).thenReturn(true);

		boolean result = interceptor.doPreHandle(request, response, session, context);
		assertTrue(result);

		context.put(SqlCheckInterceptor.SQL_CHECK_INTERCEPTED, Boolean.TRUE);

		result = interceptor.doPreHandle(request, response, session, context);
		assertTrue(result);

		context.put(SqlCheckInterceptor.SQL_CHECK_INTERCEPTED, Boolean.FALSE);
		result = interceptor.doPreHandle(request, response, session, context);
		assertFalse(result);
	}

	@Test
	public void testDoAfterCompletion() {
		Map<String, Object> context = new HashMap<>();
		when(executeResult.getStatus()).thenReturn(SqlExecuteStatus.SUCCESS);
		
		interceptor.doAfterCompletion(executeResult, session, context);
		assertTrue(executeResult.getAllowExport());
	}

	@Test
	public void testGetOrder() {
		int result = interceptor.getOrder();
		assertEquals(2, result);
	}

        // You will have to write more test methods for other methods in the SqlConsoleInterceptor class
}