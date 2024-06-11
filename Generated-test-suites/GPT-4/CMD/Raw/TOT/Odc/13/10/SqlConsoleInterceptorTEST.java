package com.oceanbase.odc.service.session.interceptor;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.junit.MockitoJUnitRunner;
import org.springframework.test.util.ReflectionTestUtils;
import com.oceanbase.odc.service.session.model.*;

import static org.junit.Assert.*;
import static org.mockito.Mockito.*;

@RunWith(MockitoJUnitRunner.class)
public class SqlConsoleInterceptorTest {

    @Mock
    private AuthenticationFacade mockFacade;

    @Mock
    private RuleService mockRuleService;

    @Mock
    private SqlConsoleRuleService mockRuleService;

    @Mock
    private SqlAsyncExecuteReq request;

    @Mock
    private SqlAsyncExecuteResp response;

    @Mock
    private ConnectionSession session;

    private SqlConsoleInterceptor interceptor;

    @Before
    public void setUp() {
        interceptor = new SqlConsoleInterceptor();
        ReflectionTestUtils.setField(interceptor, "authenticationFacade", mockFacade);
        ReflectionTestUtils.setField(interceptor, "ruleService", mockRuleService);
        ReflectionTestUtils.setField(interceptor, "sqlConsoleRuleService", mockRuleService);
    }

    @Test
    public void testDoPreHandle() {
        // TODO: define test cases for doPreHandle method
    }

    @Test
    public void testDoAfterCompletion() {
        // TODO: define test cases for doAfterCompletion method
    }

    @Test
    public void testDetermineSqlType() {
        // TODO: define test cases for determineSqlType method
    }

}