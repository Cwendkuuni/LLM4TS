import static org.junit.Assert.*;
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

public class ReturnsSmartNullsTest {

    @Test
    public void testAnswer() throws Throwable {
        ReturnsSmartNulls smartNulls = new ReturnsSmartNulls();
        InvocationOnMock invocation = Mockito.mock(InvocationOnMock.class);
        Object result = smartNulls.answer(invocation);

        // Verify that the returned value is not null
        assertNotNull(result);
    }
    
    @Test
    public void testThrowingInterceptor() throws Throwable {
        ReturnsSmartNulls smartNulls = new ReturnsSmartNulls();
        InvocationOnMock invocation = Mockito.mock(InvocationOnMock.class);
        ReturnsSmartNulls.ThrowingInterceptor interceptor = smartNulls.new ThrowingInterceptor(invocation);

        // Call the intercept method
        Object obj = new Object();
        Method method = obj.getClass().getMethod("toString");
        Object[] args = new Object[]{};
        MethodProxy proxy = Mockito.mock(MethodProxy.class);
        Object result = interceptor.intercept(obj, method, args, proxy);

        // Verify that the result contains the expected message
        assertTrue(result instanceof String);
        assertTrue(((String) result).contains("SmartNull returned by unstubbed"));
    }
    
    @Test
    public void testFormatMethodCall() throws Throwable {
        ReturnsSmartNulls smartNulls = new ReturnsSmartNulls();
        InvocationOnMock invocation = Mockito.mock(InvocationOnMock.class);
        ReturnsSmartNulls.ThrowingInterceptor interceptor = smartNulls.new ThrowingInterceptor(invocation);

        // Set the arguments for the invocation
        Object[] args = new Object[]{"arg1", "arg2"};
        Mockito.when(invocation.getArguments()).thenReturn(args);

        // Set the method for the invocation
        Method method = Mockito.mock(Method.class);
        Mockito.when(invocation.getMethod()).thenReturn(method);
        Mockito.when(method.getName()).thenReturn("methodName");

        // Call the formatMethodCall method
        String result = interceptor.formatMethodCall();

        // Verify that the result contains the expected method name and arguments
        assertTrue(result.contains("methodName"));
        assertTrue(result.contains("arg1"));
        assertTrue(result.contains("arg2"));
    }
    
    @Test
    public void testReturnsSmartNullsWithOrdinaryNull() throws Throwable {
        ReturnsSmartNulls smartNulls = new ReturnsSmartNulls();
        InvocationOnMock invocation = Mockito.mock(InvocationOnMock.class);
        Answer<Object> delegate = Mockito.mock(Answer.class);
        Mockito.when(delegate.answer(invocation)).thenReturn(null);
        Mockito.when(invocation.getMethod()).thenReturn(getClass().getMethod("toString"));

        // Call the answer method
        Object result = smartNulls.answer(invocation);

        // Verify that the result is null
        assertNull(result);
    }
    
    @Test
    public void testReturnsSmartNullsWithSmartNull() throws Throwable {
        ReturnsSmartNulls smartNulls = new ReturnsSmartNulls();
        InvocationOnMock invocation = Mockito.mock(InvocationOnMock.class);
        Answer<Object> delegate = Mockito.mock(Answer.class);
        Mockito.when(delegate.answer(invocation)).thenReturn(new ReturnsSmartNulls.SmartNull());
        Mockito.when(invocation.getMethod()).thenReturn(Mockito.mock(Method.class));

        // Call the answer method
        Object result = smartNulls.answer(invocation);

        // Verify that the result is an instance of SmartNull
        assertTrue(result instanceof ReturnsSmartNulls.SmartNull);
    }
    
    @Test
    public void testReturnsSmartNullsWithImposterizeTrue() throws Throwable {
        ReturnsSmartNulls smartNulls = new ReturnsSmartNulls();
        Class<?> type = Mockito.mock(Class.class);
        Mockito.when(type.getName()).thenReturn("Type");
        Mockito.when(ClassImposterizer.INSTANCE.canImposterise(type)).thenReturn(true);
        InvocationOnMock invocation = Mockito.mock(InvocationOnMock.class);
        Answer<Object> delegate = Mockito.mock(Answer.class);
        Mockito.when(delegate.answer(invocation)).thenReturn(null);
        Mockito.when(invocation.getMethod()).thenReturn(Mockito.mock(Method.class));

        // Call the answer method
        Object result = smartNulls.answer(invocation);

        // Verify that the result is an instance of ThrowingInterceptor
        assertTrue(result instanceof ReturnsSmartNulls.ThrowingInterceptor);
    }
    
    @Test
    public void testReturnsSmartNullsWithImposterizeFalse() throws Throwable {
        ReturnsSmartNulls smartNulls = new ReturnsSmartNulls();
        Class<?> type = Mockito.mock(Class.class);
        Mockito.when(type.getName()).thenReturn("Type");
        Mockito.when(ClassImposterizer.INSTANCE.canImposterise(type)).thenReturn(false);
        InvocationOnMock invocation = Mockito.mock(InvocationOnMock.class);
        Answer<Object> delegate = Mockito.mock(Answer.class);
        Mockito.when(delegate.answer(invocation)).thenReturn(null);
        Mockito.when(invocation.getMethod()).thenReturn(Mockito.mock(Method.class));

        // Call the answer method
        Object result = smartNulls.answer(invocation);

        // Verify that the result is null
        assertNull(result);
    }

}