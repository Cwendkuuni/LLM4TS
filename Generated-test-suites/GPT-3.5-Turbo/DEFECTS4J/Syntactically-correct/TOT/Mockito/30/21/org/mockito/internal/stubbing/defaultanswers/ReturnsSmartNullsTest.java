import static org.junit.Assert.*;
import org.junit.Test;
import org.mockito.internal.stubbing.defaultanswers.ReturnsSmartNulls;
import org.mockito.invocation.InvocationOnMock;
import org.mockito.stubbing.Answer;

public class ReturnsSmartNullsTest {

    @Test
    public void testAnswer_constructor() {
        ReturnsSmartNulls returnsSmartNulls = new ReturnsSmartNulls();
        assertNotNull(returnsSmartNulls);
    }
    
    @Test
    public void testAnswer_answer_validDefaultReturnValue() throws Throwable {
        ReturnsSmartNulls returnsSmartNulls = new ReturnsSmartNulls();
        InvocationOnMock invocation = createMockInvocation();
        Object result = returnsSmartNulls.answer(invocation);
        assertNull(result);
    }
    
    @Test
    public void testAnswer_answer_nonMockableReturnType() throws Throwable {
        ReturnsSmartNulls returnsSmartNulls = new ReturnsSmartNulls();
        InvocationOnMock invocation = createMockInvocation();
        invocation.getMethod().setReturnType(String.class);
        Object result = returnsSmartNulls.answer(invocation);
        assertNull(result);
    }
    
    @Test
    public void testThrowingInterceptor_intercept_toStringMethod() throws Throwable {
        ReturnsSmartNulls returnsSmartNulls = new ReturnsSmartNulls();
        
        InvocationOnMock invocation1 = createMockInvocation();
        invocation1.getMethod().setName("toString");
        Object result1 = returnsSmartNulls.new ThrowingInterceptor(invocation1).intercept(null, invocation1.getMethod(), null, null);
        assertTrue(result1 instanceof String);
        assertTrue(result1.contains("SmartNull returned by unstubbed"));
        
        InvocationOnMock invocation2 = createMockInvocation();
        invocation2.getMethod().setName("tostring");
        Object result2 = returnsSmartNulls.new ThrowingInterceptor(invocation2).intercept(null, invocation2.getMethod(), null, null);
        assertTrue(result2 instanceof String);
        assertFalse(result2.contains("SmartNull returned by unstubbed"));
    }
    
    @Test(expected = NullPointerException.class)
    public void testThrowingInterceptor_intercept_allMethodsExceptToString() throws Throwable {
        ReturnsSmartNulls returnsSmartNulls = new ReturnsSmartNulls();
        InvocationOnMock invocation = createMockInvocation();
        returnsSmartNulls.new ThrowingInterceptor(invocation).intercept(null, null, null, null);
    }
    
    private InvocationOnMock createMockInvocation() throws NoSuchMethodException {
        Object[] args = new Object[] {"arg1", "arg2"};
        return org.mockito.internal.util.MockUtil.createInvocationMatcher(Object.class.getDeclaredMethod("toString"), args).getInvocation();
    }
}