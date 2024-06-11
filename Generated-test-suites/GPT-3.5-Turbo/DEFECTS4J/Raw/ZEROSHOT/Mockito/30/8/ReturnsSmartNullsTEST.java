import static org.junit.Assert.*;
import static org.mockito.Mockito.*;

import org.junit.Test;
import org.mockito.cglib.proxy.MethodProxy;
import org.mockito.exceptions.Reporter;
import org.mockito.internal.creation.jmock.ClassImposterizer;
import org.mockito.internal.debugging.Location;
import org.mockito.internal.util.ObjectMethodsGuru;
import org.mockito.invocation.InvocationOnMock;
import org.mockito.stubbing.Answer;

public class ReturnsSmartNullsTest {
    
    @Test
    public void testAnswer_defaultReturnValueNotNull_returnsDefaultReturnValue() throws Throwable {
        ReturnsSmartNulls rs = new ReturnsSmartNulls();
        InvocationOnMock invocation = mock(InvocationOnMock.class);
        when(invocation.getMethod()).thenReturn(ReturnsSmartNulls.class.getMethod("answer", InvocationOnMock.class));
        Answer<Object> delegate = mock(Answer.class);
        when(delegate.answer(invocation)).thenReturn(new Object());
        MethodProxy methodProxy = mock(MethodProxy.class);
        when(methodProxy.invokeSuper(any(), any())).thenReturn(null);
        
        Object result = rs.answer(invocation, methodProxy);
        
        assertEquals(new Object(), result);
    }
    
    @Test
    public void testAnswer_defaultReturnValueNull_mockableReturnType_returnsProxy() throws Throwable {
        ReturnsSmartNulls rs = new ReturnsSmartNulls();
        InvocationOnMock invocation = mock(InvocationOnMock.class);
        when(invocation.getMethod()).thenReturn(ReturnsSmartNulls.class.getMethod("answer", InvocationOnMock.class));
        Answer<Object> delegate = mock(Answer.class);
        when(delegate.answer(invocation)).thenReturn(null);
        MethodProxy methodProxy = mock(MethodProxy.class);
        when(methodProxy.invokeSuper(any(), any())).thenReturn(null);
        
        Object result = rs.answer(invocation, methodProxy);
        
        assertNotNull(result);
    }
    
    @Test
    public void testThrowingInterceptor_intercept_toStringMethod_returnsSmartNullString() throws Throwable {
        ReturnsSmartNulls rs = new ReturnsSmartNulls();
        InvocationOnMock invocation = mock(InvocationOnMock.class);
        when(invocation.getMethod()).thenReturn(ReturnsSmartNulls.class.getMethod("toString"));
        MethodProxy methodProxy = mock(MethodProxy.class);
        when(methodProxy.invokeSuper(any(), any())).thenReturn(null);
        
        Object result = rs.new ThrowingInterceptor(invocation).intercept(null, null, null, methodProxy);
        
        assertEquals("SmartNull returned by unstubbed toString method on mock", result);
    }
    
    @Test(expected = RuntimeException.class)
    public void testThrowingInterceptor_intercept_nonToStringMethod_throwsException() throws Throwable {
        ReturnsSmartNulls rs = new ReturnsSmartNulls();
        InvocationOnMock invocation = mock(InvocationOnMock.class);
        when(invocation.getMethod()).thenReturn(ReturnsSmartNulls.class.getMethod("answer", InvocationOnMock.class));
        MethodProxy methodProxy = mock(MethodProxy.class);
        when(methodProxy.invokeSuper(any(), any())).thenReturn(null);
        
        rs.new ThrowingInterceptor(invocation).intercept(null, null, null, methodProxy);
    }
    
    @Test
    public void testFormatMethodCall_invocationWithArguments_returnsFormattedMethodCall() throws Throwable {
        ReturnsSmartNulls rs = new ReturnsSmartNulls();
        InvocationOnMock invocation = mock(InvocationOnMock.class);
        when(invocation.getMethod()).thenReturn(ReturnsSmartNulls.class.getMethod("answer", InvocationOnMock.class));
        when(invocation.getArguments()).thenReturn(new Object[] {1, 2, 3});
        
        String result = rs.new ThrowingInterceptor(invocation).formatMethodCall();
        
        assertEquals("answer(1, 2, 3)", result);
    }
    
    @Test(expected = AssertionError.class)
    public void testAnswer_delegateWithException_throwsException() throws Throwable {
        ReturnsSmartNulls rs = new ReturnsSmartNulls();
        InvocationOnMock invocation = mock(InvocationOnMock.class);
        when(invocation.getMethod()).thenReturn(ReturnsSmartNulls.class.getMethod("answer", InvocationOnMock.class));
        Answer<Object> delegate = mock(Answer.class);
        when(delegate.answer(invocation)).thenThrow(new RuntimeException());
        
        rs.answer(invocation, null);
    }
    
    @Test
    public void testAnswer_defaultReturnValueNotNull_returnsDefaultReturnValue2() throws Throwable {
        ReturnsSmartNulls rs = new ReturnsSmartNulls();
        InvocationOnMock invocation = mock(InvocationOnMock.class);
        when(invocation.getMethod()).thenReturn(ReturnsSmartNulls.class.getMethod("answer", InvocationOnMock.class));
        Answer<Object> delegate = mock(Answer.class);
        when(delegate.answer(invocation)).thenReturn(new Object());
        MethodProxy methodProxy = mock(MethodProxy.class);
        when(methodProxy.invokeSuper(any(), any())).thenReturn(null);
        
        Object result = rs.answer(invocation, methodProxy);
        
        assertEquals(new Object(), result);
    }
    
    @Test
    public void testAnswer_defaultReturnValueNull_mockableReturnType_returnsProxy2() throws Throwable {
        ReturnsSmartNulls rs = new ReturnsSmartNulls();
        InvocationOnMock invocation = mock(InvocationOnMock.class);
        when(invocation.getMethod()).thenReturn(ReturnsSmartNulls.class.getMethod("answer", InvocationOnMock.class));
        Answer<Object> delegate = mock(Answer.class);
        when(delegate.answer(invocation)).thenReturn(null);
        MethodProxy methodProxy = mock(MethodProxy.class);
        when(methodProxy.invokeSuper(any(), any())).thenReturn(null);
        
        Object result = rs.answer(invocation, methodProxy);
        
        assertNotNull(result);
    }
    
    @Test
    public void testThrowingInterceptor_intercept_toStringMethod_returnsSmartNullString2() throws Throwable {
        ReturnsSmartNulls rs = new ReturnsSmartNulls();
        InvocationOnMock invocation = mock(InvocationOnMock.class);
        when(invocation.getMethod()).thenReturn(ReturnsSmartNulls.class.getMethod("toString"));
        MethodProxy methodProxy = mock(MethodProxy.class);
        when(methodProxy.invokeSuper(any(), any())).thenReturn(null);
        
        Object result = rs.new ThrowingInterceptor(invocation).intercept(null, null, null, methodProxy);
        
        assertEquals("SmartNull returned by unstubbed toString method on mock", result);
    }
    
    @Test(expected = RuntimeException.class)
    public void testThrowingInterceptor_intercept_nonToStringMethod_throwsException2() throws Throwable {
        ReturnsSmartNulls rs = new ReturnsSmartNulls();
        InvocationOnMock invocation = mock(InvocationOnMock.class);
        when(invocation.getMethod()).thenReturn(ReturnsSmartNulls.class.getMethod("answer", InvocationOnMock.class));
        MethodProxy methodProxy = mock(MethodProxy.class);
        when(methodProxy.invokeSuper(any(), any())).thenReturn(null);
        
        rs.new ThrowingInterceptor(invocation).intercept(null, null, null, methodProxy);
    }
    
    @Test
    public void testFormatMethodCall_invocationWithArguments_returnsFormattedMethodCall2() throws Throwable {
        ReturnsSmartNulls rs = new ReturnsSmartNulls();
        InvocationOnMock invocation = mock(InvocationOnMock.class);
        when(invocation.getMethod()).thenReturn(ReturnsSmartNulls.class.getMethod("answer", InvocationOnMock.class));
        when(invocation.getArguments()).thenReturn(new Object[] {1, 2, 3});
        
        String result = rs.new ThrowingInterceptor(invocation).formatMethodCall();
        
        assertEquals("answer(1, 2, 3)", result);
    }
    
    @Test(expected = AssertionError.class)
    public void testAnswer_delegateWithException_throwsException2() throws Throwable {
        ReturnsSmartNulls rs = new ReturnsSmartNulls();
        InvocationOnMock invocation = mock(InvocationOnMock.class);
        when(invocation.getMethod()).thenReturn(ReturnsSmartNulls.class.getMethod("answer", InvocationOnMock.class));
        Answer<Object> delegate = mock(Answer.class);
        when(delegate.answer(invocation)).thenThrow(new RuntimeException());
        
        rs.answer(invocation, null);
    }
}