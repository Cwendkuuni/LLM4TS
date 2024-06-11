import static org.junit.Assert.*;
import org.junit.Test;
import org.mockito.cglib.proxy.MethodInterceptor;
import org.mockito.invocation.InvocationOnMock;

public class ReturnsSmartNullsTest {

    @Test
    public void testAnswer_ReturnsSmartNulls() throws Throwable {
        ReturnsSmartNulls returnsSmartNulls = new ReturnsSmartNulls();
        InvocationOnMock invocation = new InvocationOnMock(null, null, null);

        Object result = returnsSmartNulls.answer(invocation);

        assertNotNull(result);
        assertTrue(result instanceof MethodInterceptor);
    }
}