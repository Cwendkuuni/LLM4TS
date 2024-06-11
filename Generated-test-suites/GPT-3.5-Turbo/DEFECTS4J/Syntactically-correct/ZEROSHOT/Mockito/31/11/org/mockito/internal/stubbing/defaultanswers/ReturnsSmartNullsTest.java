import static org.junit.Assert.*;
import org.junit.Test;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.invocation.InvocationOnMock;
import org.mockito.stubbing.Answer;

public class ReturnsSmartNullsTest {

    @Test
    public void testAnswer() throws Throwable {
        ReturnsSmartNulls returnsSmartNulls = new ReturnsSmartNulls();
        InvocationOnMock invocation = Mockito.mock(InvocationOnMock.class);
        Object result = returnsSmartNulls.answer(invocation);
        assertNull(result);
    }
    
    @Test
    public void testIntercept() throws Throwable {
        ReturnsSmartNulls returnsSmartNulls = new ReturnsSmartNulls();
        InvocationOnMock invocation = Mockito.mock(InvocationOnMock.class);
        ReturnsSmartNulls.ThrowingInterceptor interceptor = returnsSmartNulls.new ThrowingInterceptor(invocation);
        
        Method method = ReturnsSmartNulls.class.getDeclaredMethod("intercept", Object.class, Method.class, Object[].class, org.mockito.cglib.proxy.MethodProxy.class);
        Object[] args = new Object[]{};
        org.mockito.cglib.proxy.MethodProxy proxy = Mockito.mock(org.mockito.cglib.proxy.MethodProxy.class);
        
        Object result = interceptor.intercept(returnsSmartNulls, method, args, proxy);
        assertNotNull(result);
        assertEquals("SmartNull returned by unstubbed intercept() method on mock", result);
    }
    
    @Test
    public void testFormatMethodCall() throws Throwable {
        ReturnsSmartNulls returnsSmartNulls = new ReturnsSmartNulls();
        InvocationOnMock invocation = Mockito.mock(InvocationOnMock.class);
        ReturnsSmartNulls.ThrowingInterceptor interceptor = returnsSmartNulls.new ThrowingInterceptor(invocation);
        
        String methodCall = interceptor.formatMethodCall();
        assertEquals("intercept()", methodCall);
    }
    
    @Test
    public void testAnswerWithDelegateNotNull() throws Throwable {
        ReturnsSmartNulls returnsSmartNulls = new ReturnsSmartNulls();
        InvocationOnMock invocation = Mockito.mock(InvocationOnMock.class);
        Answer<Object> delegate = Mockito.mock(Answer.class);
        Mockito.when(delegate.answer(invocation)).thenReturn("Mock answer");
        
        returnsSmartNulls.delegate = delegate;
        Object result = returnsSmartNulls.answer(invocation);
        assertEquals("Mock answer", result);
    }
    
    @Test
    public void testAnswerWithDelegateNull() throws Throwable {
        ReturnsSmartNulls returnsSmartNulls = new ReturnsSmartNulls();
        InvocationOnMock invocation = Mockito.mock(InvocationOnMock.class);
        Answer<Object> delegate = Mockito.mock(Answer.class);
        Mockito.when(delegate.answer(invocation)).thenReturn(null);
        
        returnsSmartNulls.delegate = delegate;
        ClassImposterizer.INSTANCE = Mockito.mock(ClassImposterizer.class);
        Object result = returnsSmartNulls.answer(invocation);
        assertNull(result);
    }

    @Test
    public void testAnswerWithImposterizableType() throws Throwable {
        ReturnsSmartNulls returnsSmartNulls = new ReturnsSmartNulls();
        InvocationOnMock invocation = Mockito.mock(InvocationOnMock.class);
        Answer<Object> delegate = Mockito.mock(Answer.class);
        Mockito.when(delegate.answer(invocation)).thenReturn(null);
        
        returnsSmartNulls.delegate = delegate;
        Class<?> type = Object.class;
        ClassImposterizer.INSTANCE = Mockito.mock(ClassImposterizer.class);
        Mockito.when(ClassImposterizer.INSTANCE.canImposterise(type)).thenReturn(true);
        Mockito.when(ClassImposterizer.INSTANCE.imposterise(Mockito.any(ReturnsSmartNulls.ThrowingInterceptor.class), Mockito.eq(type))).thenReturn("Mock imposterized object");
        
        Object result = returnsSmartNulls.answer(invocation);
        assertEquals("Mock imposterized object", result);
    }
    
    @Test
    public void testAnswerWithNonImposterizableType() throws Throwable {
        ReturnsSmartNulls returnsSmartNulls = new ReturnsSmartNulls();
        InvocationOnMock invocation = Mockito.mock(InvocationOnMock.class);
        Answer<Object> delegate = Mockito.mock(Answer.class);
        Mockito.when(delegate.answer(invocation)).thenReturn(null);
        
        returnsSmartNulls.delegate = delegate;
        Class<?> type = Object.class;
        ClassImposterizer.INSTANCE = Mockito.mock(ClassImposterizer.class);
        Mockito.when(ClassImposterizer.INSTANCE.canImposterise(type)).thenReturn(false);
        
        Object result = returnsSmartNulls.answer(invocation);
        assertNull(result);
    }

}