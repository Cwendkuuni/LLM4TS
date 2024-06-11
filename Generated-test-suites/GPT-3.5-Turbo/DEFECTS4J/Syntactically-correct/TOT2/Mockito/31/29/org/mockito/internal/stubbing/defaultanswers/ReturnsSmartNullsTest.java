import org.junit.Test;
import org.mockito.internal.stubbing.defaultanswers.ReturnsSmartNulls;
import org.mockito.invocation.Invocation;
import static org.junit.Assert.*;
import static org.mockito.Mockito.mock;

public class ReturnsSmartNullsTest {

    @Test
    public void testAnswerReturnsSmartNull() throws Throwable {
        ReturnsSmartNulls returnsSmartNulls = new ReturnsSmartNulls();

        Invocation invocation = mock(Invocation.class);
        Object result = returnsSmartNulls.answer(invocation);

        assertNotNull(result);
    }

    @Test
    public void testThrowingInterceptor() throws Throwable {
        ReturnsSmartNulls.ThrowingInterceptor interceptor = new ReturnsSmartNulls().new ThrowingInterceptor(null);
        Method method = ReturnsSmartNulls.class.getDeclaredMethod("intercept", Object.class, Method.class, Object[].class, MethodProxy.class);

        Object result = interceptor.intercept(null, method, new Object[0], null);

        assertNotNull(result);
    }

    @Test
    public void testAnswerWithReturnValue() throws Throwable {
        ReturnsSmartNulls returnsSmartNulls = new ReturnsSmartNulls();

        Invocation invocation = mock(Invocation.class);
        Object returnValue = new Object();
        when(invocation.getMethod().getReturnType()).thenReturn(returnValue.getClass());

        Object result = returnsSmartNulls.answer(invocation);

        assertEquals(returnValue, result);
    }

    @Test
    public void testAnswerWithoutReturnValue() throws Throwable {
        ReturnsSmartNulls returnsSmartNulls = new ReturnsSmartNulls();

        Invocation invocation = mock(Invocation.class);
        when(invocation.getMethod().getReturnType()).thenReturn(Void.class);

        Object result = returnsSmartNulls.answer(invocation);

        assertNull(result);
    }

    @Test
    public void testAnswerWithFinalReturnType() throws Throwable {
        ReturnsSmartNulls returnsSmartNulls = new ReturnsSmartNulls();

        Invocation invocation = mock(Invocation.class);
        when(invocation.getMethod().getReturnType()).thenReturn(String.class);

        Object result = returnsSmartNulls.answer(invocation);

        assertNull(result);
    }

}