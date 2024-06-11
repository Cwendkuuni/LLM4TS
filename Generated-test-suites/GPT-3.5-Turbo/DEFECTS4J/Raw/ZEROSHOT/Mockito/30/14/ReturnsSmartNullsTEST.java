import org.junit.Test;
import org.mockito.Mockito;
import org.mockito.cglib.proxy.MethodInterceptor;
import org.mockito.cglib.proxy.MethodProxy;
import org.mockito.exceptions.Reporter;
import org.mockito.internal.creation.jmock.ClassImposterizer;
import org.mockito.internal.debugging.Location;
import org.mockito.internal.util.ObjectMethodsGuru;
import org.mockito.invocation.InvocationOnMock;
import org.mockito.stubbing.Answer;
import java.lang.reflect.Method;
import java.util.Arrays;
import static org.junit.Assert.*;

public class ReturnsSmartNullsTest {

    @Test
    public void testAnswer() throws Throwable {
        ReturnsSmartNulls returnsSmartNulls = new ReturnsSmartNulls();

        InvocationOnMock invocation = Mockito.mock(InvocationOnMock.class);
        Mockito.when(invocation.getMethod()).thenReturn(null);
        Object result = returnsSmartNulls.answer(invocation);
        assertNull(result);
        
        invocation = Mockito.mock(InvocationOnMock.class);
        Mockito.when(invocation.getMethod()).thenReturn(ObjectMethodsGuru.class.getMethod("isToString", Method.class));
        Mockito.when(invocation.getArguments()).thenReturn(new Object[]{"arg1", "arg2"});
        result = returnsSmartNulls.answer(invocation);
        assertEquals("SmartNull returned by unstubbed isToString method on mock", result);
        
        invocation = Mockito.mock(InvocationOnMock.class);
        Mockito.when(invocation.getMethod()).thenReturn(null);
        Mockito.when(invocation.getArguments()).thenReturn(new Object[]{"arg1", "arg2"});
        result = returnsSmartNulls.answer(invocation);
        assertNull(result);
    }
}