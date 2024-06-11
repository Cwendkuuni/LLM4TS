import static org.junit.Assert.*;
import static org.mockito.Mockito.*;

import org.junit.Test;
import org.mockito.cglib.proxy.MethodInterceptor;
import org.mockito.internal.creation.jmock.ClassImposterizer;
import org.mockito.internal.util.ObjectMethodsGuru;
import org.mockito.invocation.InvocationOnMock;
import org.mockito.internal.stubbing.defaultanswers.ReturnsSmartNulls;
import org.mockito.exceptions.Reporter;
import org.mockito.stubbing.Answer;

public class ReturnsSmartNullsTest {

    @Test
    public void testAnswer_ReturnsSmartNulls() throws Throwable {
        ReturnsSmartNulls smartNulls = new ReturnsSmartNulls();
        InvocationOnMock invocation = mock(InvocationOnMock.class);
        Answer<Object> result = smartNulls.answer(invocation);
        
        assertNotNull(result);
        assertTrue(result instanceof Answer);
    }

    @Test
    public void testInterceptor_intercept_ThrowsException() throws Throwable {
        ReturnsSmartNulls.ThrowingInterceptor interceptor = new ReturnsSmartNulls().new ThrowingInterceptor(null);
        Object result = interceptor.intercept(null, null, null, null);
        
        assertNotNull(result);
        assertEquals("SmartNull returned by unstubbed null() method on mock", result);
    }

    @Test
    public void testClassImposterizer_canImposterise_ReturnsTrue() {
        ClassImposterizer imposterizer = ClassImposterizer.INSTANCE;
        Class<?> type = String.class;
        
        assertTrue(imposterizer.canImposterise(type));
    }

    @Test
    public void testObjectMethodsGuru_isToString_ReturnsTrue() throws NoSuchMethodException {
        ObjectMethodsGuru guru = new ObjectMethodsGuru();
        Method method = Object.class.getDeclaredMethod("toString");
        boolean result = guru.isToString(method);
        
        assertTrue(result);
    }

    @Test(expected = NullPointerException.class)
    public void testReporter_smartNullPointerException_ThrowsException() {
        new Reporter().smartNullPointerException(null);
    }

}