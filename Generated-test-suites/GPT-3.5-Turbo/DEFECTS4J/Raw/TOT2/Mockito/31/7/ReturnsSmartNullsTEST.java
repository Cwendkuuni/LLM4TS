import static org.junit.Assert.*;
import org.junit.Test;
import org.mockito.Mockito;
import org.mockito.invocation.InvocationOnMock;
import org.mockito.cglib.proxy.MethodProxy;

public class ReturnsSmartNullsTest {

    @Test
    public void testAnswer() throws Throwable {
        ReturnsSmartNulls returnsSmartNulls = new ReturnsSmartNulls();

        InvocationOnMock invocation = Mockito.mock(InvocationOnMock.class);
        MethodProxy methodProxy = Mockito.mock(MethodProxy.class);

        Object result = returnsSmartNulls.answer(invocation);

        assertNull(result);
    }

    @Test
    public void testThrowingInterceptor() throws Throwable {
        ReturnsSmartNulls. ThrowingInterceptor throwingInterceptor = new ReturnsSmartNulls().new ThrowingInterceptor(Mockito.mock(InvocationOnMock.class));

        Object obj = new Object();
        MethodProxy methodProxy = Mockito.mock(MethodProxy.class);

        Object[] args = new Object[0];

        Exception exception = null;

        try {
            throwingInterceptor.intercept(obj, Object.class.getMethod("hashCode"), args, methodProxy);
        } catch (Exception e) {
            exception = e;
        }

        assertNotNull(exception);
        assertTrue(exception instanceof RuntimeException);
    }

}