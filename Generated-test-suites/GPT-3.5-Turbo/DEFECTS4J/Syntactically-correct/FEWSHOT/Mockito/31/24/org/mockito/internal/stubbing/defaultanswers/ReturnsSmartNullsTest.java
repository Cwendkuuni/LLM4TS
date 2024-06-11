import org.junit.Test;
import org.mockito.cglib.proxy.MethodProxy;
import org.mockito.exceptions.Reporter;
import org.mockito.internal.creation.jmock.ClassImposterizer;
import org.mockito.internal.debugging.Location;
import org.mockito.invocation.InvocationOnMock;
import org.mockito.stubbing.Answer;

import java.lang.reflect.Method;

import static org.junit.Assert.*;
import static org.mockito.Mockito.*;

public class ReturnsSmartNullsTest {
    
    @Test
    public void testAnswer() throws Throwable {
        ReturnsMoreEmptyValues delegate = mock(ReturnsMoreEmptyValues.class);
        when(delegate.answer(any(InvocationOnMock.class))).thenReturn(null);
        
        ReturnsSmartNulls returnsSmartNulls = new ReturnsSmartNulls(delegate);
        
        InvocationOnMock invocation = mock(InvocationOnMock.class);
        
        Object result = returnsSmartNulls.answer(invocation);
        
        assertNull(result);
        verify(delegate).answer(invocation);
    }
    
    @Test
    public void testAnswerWithReturnValue() throws Throwable {
        ReturnsMoreEmptyValues delegate = mock(ReturnsMoreEmptyValues.class);
        when(delegate.answer(any(InvocationOnMock.class))).thenReturn("value");
        
        ReturnsSmartNulls returnsSmartNulls = new ReturnsSmartNulls(delegate);
        
        InvocationOnMock invocation = mock(InvocationOnMock.class);
        
        Object result = returnsSmartNulls.answer(invocation);
        
        assertEquals("value", result);
        verify(delegate).answer(invocation);
    }
    
    @Test
    public void testAnswerWithNonMockableReturnType() throws Throwable {
        ReturnsMoreEmptyValues delegate = mock(ReturnsMoreEmptyValues.class);
        when(delegate.answer(any(InvocationOnMock.class))).thenReturn(null);
        
        ClassImposterizer imposterizer = mock(ClassImposterizer.class);
        when(imposterizer.canImposterise(String.class)).thenReturn(false);
        
        ReturnsSmartNulls returnsSmartNulls = new ReturnsSmartNulls(delegate, imposterizer);
        
        InvocationOnMock invocation = mock(InvocationOnMock.class);
        Method method = mock(Method.class);
        when(invocation.getMethod()).thenReturn(method);
        when(method.getReturnType()).thenReturn(String.class);
        
        Object result = returnsSmartNulls.answer(invocation);
        
        assertNull(result);
        verify(delegate).answer(invocation);
        verify(imposterizer).canImposterise(String.class);
    }
    
    @Test
    public void testAnswerWithMockableReturnType() throws Throwable {
        ReturnsMoreEmptyValues delegate = mock(ReturnsMoreEmptyValues.class);
        when(delegate.answer(any(InvocationOnMock.class))).thenReturn(null);
        
        ClassImposterizer imposterizer = mock(ClassImposterizer.class);
        when(imposterizer.canImposterise(String.class)).thenReturn(true);
        
        Answers.ThrowsInterceptor interceptor = mock(Answers.ThrowsInterceptor.class);
        when(imposterizer.imposterise(any(Answer.class), any(Class.class))).thenReturn(interceptor);
        
        ReturnsSmartNulls returnsSmartNulls = new ReturnsSmartNulls(delegate, imposterizer);
        
        InvocationOnMock invocation = mock(InvocationOnMock.class);
        Method method = mock(Method.class);
        when(invocation.getMethod()).thenReturn(method);
        when(method.getReturnType()).thenReturn(String.class);
        
        Object result = returnsSmartNulls.answer(invocation);
        
        assertNotNull(result);
        assertEquals(interceptor, result);
        verify(delegate).answer(invocation);
        verify(imposterizer).canImposterise(String.class);
        verify(imposterizer).imposterise(any(Answer.class), any(Class.class));
    }
    
    @Test
    public void testAnswerWithToStringMethod() throws Throwable {
        ReturnsMoreEmptyValues delegate = mock(ReturnsMoreEmptyValues.class);
        when(delegate.answer(any(InvocationOnMock.class))).thenReturn(null);
        
        ReturnsSmartNulls returnsSmartNulls = new ReturnsSmartNulls(delegate);
        
        InvocationOnMock invocation = mock(InvocationOnMock.class);
        Method method = mock(Method.class);
        when(invocation.getMethod()).thenReturn(method);
        when(new ObjectMethodsGuru().isToString(method)).thenReturn(true);
        
        Object result = returnsSmartNulls.answer(invocation);
        
        assertEquals("SmartNull returned by unstubbed toString() method on mock", result);
        verify(delegate).answer(invocation);
        verify(new ObjectMethodsGuru()).isToString(method);
    }
    
    @Test
    public void testAnswerWithNonToStringMethod() throws Throwable {
        ReturnsMoreEmptyValues delegate = mock(ReturnsMoreEmptyValues.class);
        when(delegate.answer(any(InvocationOnMock.class))).thenReturn(null);
        
        ReturnsSmartNulls returnsSmartNulls = new ReturnsSmartNulls(delegate);
        
        InvocationOnMock invocation = mock(InvocationOnMock.class);
        Method method = mock(Method.class);
        when(invocation.getMethod()).thenReturn(method);
        when(new ObjectMethodsGuru().isToString(method)).thenReturn(false);
        
        Location location = mock(Location.class);
        Reporter reporter = mock(Reporter.class);
        doAnswer(invocationOnMock -> {
            assertEquals("SmartNull returned by unstubbed methodCall() method on mock", invocationOnMock.getArguments()[0]);
            return null;
        }).when(reporter).smartNullPointerException(location);
        
        Object result = returnsSmartNulls.answer(invocation);
        
        assertNull(result);
        verify(delegate).answer(invocation);
        verify(new ObjectMethodsGuru()).isToString(method);
        verify(reporter).smartNullPointerException(location);
    }
}