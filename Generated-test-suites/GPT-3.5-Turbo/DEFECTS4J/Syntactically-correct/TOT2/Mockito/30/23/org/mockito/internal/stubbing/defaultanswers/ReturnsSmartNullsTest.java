import static org.junit.Assert.*;
import org.junit.Test;
import org.mockito.*;
import org.mockito.cglib.proxy.MethodInterceptor;
import org.mockito.internal.debugging.Location;
import org.mockito.invocation.InvocationOnMock;
import org.mockito.stubbing.Answer;

public class ReturnsSmartNullsTest {
    
    @Test
    public void testAnswer_DefaultReturnValueNotNull() throws Throwable {
        ReturnsSmartNulls returnsSmartNulls = new ReturnsSmartNulls();
        InvocationOnMock invocation = Mockito.mock(InvocationOnMock.class);
        Mockito.when(invocation.getMethod().getReturnType()).thenReturn(String.class);
        assertNotNull(returnsSmartNulls.answer(invocation));
    }
    
    @Test
    public void testAnswer_DefaultReturnValueNull() throws Throwable {
        ReturnsSmartNulls returnsSmartNulls = new ReturnsSmartNulls();
        InvocationOnMock invocation = Mockito.mock(InvocationOnMock.class);
        Mockito.when(invocation.getMethod().getReturnType()).thenReturn(Void.class);
        assertNull(returnsSmartNulls.answer(invocation));
    }

    @Test
    public void testAnswer_ClassImposterizerCanImposterise() throws Throwable {
        ReturnsSmartNulls returnsSmartNulls = new ReturnsSmartNulls();
        InvocationOnMock invocation = Mockito.mock(InvocationOnMock.class);
        Mockito.when(invocation.getMethod().getReturnType()).thenReturn(Object.class);
        assertNotNull(returnsSmartNulls.answer(invocation));
    }

    @Test
    public void testAnswer_MethodInterceptorThrowsException() {
        ReturnsSmartNulls returnsSmartNulls = new ReturnsSmartNulls();
        InvocationOnMock invocation = Mockito.mock(InvocationOnMock.class);
        Mockito.when(invocation.getMethod().getName()).thenReturn("testMethod");
        Location location = new Location();
        ThrowingInterceptor throwingInterceptor = returnsSmartNulls.new ThrowingInterceptor(invocation);
        RuntimeException exception = assertThrows(RuntimeException.class, () -> throwingInterceptor.intercept(null, null, null, null));
        assertEquals("SmartNull returned by unstubbed testMethod method on mock", exception.getMessage());
    }

    @Test
    public void testFormatMethodCall() {
        ReturnsSmartNulls returnsSmartNulls = new ReturnsSmartNulls();
        InvocationOnMock invocation = Mockito.mock(InvocationOnMock.class);
        Mockito.when(invocation.getMethod()).thenReturn(ReturnsSmartNulls.class.getDeclaredMethod("answer", InvocationOnMock.class));
        Mockito.when(invocation.getArguments()).thenReturn(new Object[]{"arg1", "arg2"});
        ThrowingInterceptor throwingInterceptor = returnsSmartNulls.new ThrowingInterceptor(invocation);
        assertEquals("answer(arg1, arg2)", throwingInterceptor.formatMethodCall());
    }
}