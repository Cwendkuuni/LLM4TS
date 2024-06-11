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

import static org.junit.Assert.assertEquals;
import static org.mockito.Mockito.mock;

public class ReturnsSmartNullsTest {
    
    @Test
    public void testAnswer_withUnstubbedMethodCall_returnsSmartNull() throws Throwable {
        ReturnsSmartNulls answer = new ReturnsSmartNulls();
        InvocationOnMock invocation = mock(InvocationOnMock.class);
        Method method = Object.class.getMethod("toString");
        Object[] args = {};
        MethodProxy proxy = mock(MethodProxy.class);
      
        Object result = answer.answer(invocation);
      
        assertEquals("SmartNull returned by unstubbed toString() method on mock", result);
    }
    
    @Test
    public void testAnswer_withUnmockableReturnType_returnsNull() throws Throwable {
        ReturnsSmartNulls answer = new ReturnsSmartNulls();
        InvocationOnMock invocation = mock(InvocationOnMock.class);
        Method method = TestClass.class.getMethod("unmockableMethod");
        Object[] args = {};
        MethodProxy proxy = mock(MethodProxy.class);
      
        Object result = answer.answer(invocation);
      
        assertEquals(null, result);
    }
    
    @Test
    public void testAnswer_withMockableReturnType_returnsSmartNull() throws Throwable {
        ReturnsSmartNulls answer = new ReturnsSmartNulls();
        InvocationOnMock invocation = mock(InvocationOnMock.class);
        Method method = TestClass.class.getMethod("mockableMethod");
        Object[] args = {};
        MethodProxy proxy = mock(MethodProxy.class);
      
        Object result = answer.answer(invocation);
      
        assertEquals("SmartNull returned by unstubbed mockableMethod() method on mock", result);
    }
    
    private static class TestClass {
        public void unmockableMethod() {
            // do nothing
        }
        
        public String mockableMethod() {
            return null;
        }
    }
}