import org.junit.Before;
import org.junit.Test;
import static org.junit.Assert.*;
import org.mockito.Mockito;
import org.mockito.cglib.proxy.MethodInterceptor;
import org.mockito.cglib.proxy.MethodProxy;
import org.mockito.exceptions.Reporter;
import org.mockito.internal.creation.jmock.ClassImposterizer;
import org.mockito.internal.debugging.Location;
import org.mockito.internal.util.ObjectMethodsGuru;
import org.mockito.invocation.InvocationOnMock;
import org.mockito.stubbing.Answer;

public class ReturnsSmartNullsTest {

    private ReturnsSmartNulls returnsSmartNulls;
    private InvocationOnMock invocation;

    @Before
    public void setUp() {
        returnsSmartNulls = new ReturnsSmartNulls();
        invocation = Mockito.mock(InvocationOnMock.class);
    }

    @Test
    public void testAnswer() throws Throwable {
        assertTrue(returnsSmartNulls instanceof Answer);
        Object answer = returnsSmartNulls.answer(invocation);
        assertNull(answer);
    }

    @Test
    public void testThrowingInterceptorToString() throws Throwable {
        ReturnsSmartNulls.ThrowingInterceptor throwingInterceptor = returnsSmartNulls.new ThrowingInterceptor(invocation);
        Method toStringMethod = Object.class.getDeclaredMethod("toString");
        Object result = throwingInterceptor.intercept(null, toStringMethod, null, null);
        assertEquals("SmartNull returned by unstubbed toString() method on mock", result);
    }

    @Test(expected = NullPointerException.class)
    public void testThrowingInterceptorIntercept() throws Throwable {
        ReturnsSmartNulls.ThrowingInterceptor throwingInterceptor = returnsSmartNulls.new ThrowingInterceptor(invocation);
        Method method = Object.class.getDeclaredMethod("hashCode");
        throwingInterceptor.intercept(null, method, null, null);
    }

    @Test
    public void testReturnsSmartNulls() throws Throwable {
        ReturnsSmartNulls delegate = new ReturnsSmartNulls();
        Object defaultReturnValue = delegate.answer(invocation);
        assertNull(defaultReturnValue);

        Method method = Object.class.getDeclaredMethod("toString");
        Mockito.doReturn(String.class).when(invocation).getMethod().getReturnType();
        Object returnValue = delegate.answer(invocation);
        assertNull(returnValue);

        Mockito.doReturn(Integer.class).when(invocation).getMethod().getReturnType();
        returnValue = delegate.answer(invocation);
        assertNull(returnValue);
    }
}