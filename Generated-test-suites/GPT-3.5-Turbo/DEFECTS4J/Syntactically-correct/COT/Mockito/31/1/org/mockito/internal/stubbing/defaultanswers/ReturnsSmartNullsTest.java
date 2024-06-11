import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.mockito.Mockito;
import org.mockito.exceptions.Reporter;
import org.mockito.invocation.InvocationOnMock;
import org.mockito.stubbing.Answer;

public class ReturnsSmartNullsTest {

    private ReturnsSmartNulls returnsSmartNulls;

    @Before
    public void setUp() {
        returnsSmartNulls = new ReturnsSmartNulls();
    }

    @Test
    public void testAnswer() throws Throwable {
        InvocationOnMock invocation = Mockito.mock(InvocationOnMock.class);
        Object result = returnsSmartNulls.answer(invocation);
        Assert.assertNull(result);
    }

    @Test
    public void testThrowingInterceptor() throws Throwable {
        ReturnsSmartNulls.ThrowingInterceptor interceptor = returnsSmartNulls.new ThrowingInterceptor(null);
        Object obj = new Object();
        Object[] args = new Object[0];
        Object result = interceptor.intercept(obj, Object.class.getMethod("hashCode"), args, null);
        Assert.assertEquals("SmartNull returned by unstubbed hashCode() method on mock", result);
    }

    @Test
    public void testFormatMethodCall() {
        ReturnsSmartNulls.ThrowingInterceptor interceptor = returnsSmartNulls.new ThrowingInterceptor(null);
        InvocationOnMock invocation = Mockito.mock(InvocationOnMock.class);
        Mockito.when(invocation.getMethod()).thenReturn(Object.class.getMethod("toString"));
        String result = interceptor.formatMethodCall();
        Assert.assertEquals("toString()", result);
    }

    @Test(expected = Reporter.class)
    public void testInterceptSmartNullPointerException() throws Throwable {
        ReturnsSmartNulls.ThrowingInterceptor interceptor = returnsSmartNulls.new ThrowingInterceptor(null);
        Object obj = new Object();
        Object[] args = new Object[0];
        interceptor.intercept(obj, Object.class.getMethod("hashCode"), args, null);
    }
}