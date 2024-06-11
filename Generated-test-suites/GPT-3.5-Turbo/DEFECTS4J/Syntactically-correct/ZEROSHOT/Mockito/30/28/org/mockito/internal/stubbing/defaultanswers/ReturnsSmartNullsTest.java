import static org.junit.Assert.*;
import static org.mockito.Mockito.*;

import org.junit.Test;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.cglib.proxy.MethodInterceptor;
import org.mockito.cglib.proxy.MethodProxy;
import org.mockito.exceptions.Reporter;
import org.mockito.internal.creation.jmock.ClassImposterizer;
import org.mockito.internal.debugging.Location;
import org.mockito.internal.stubbing.defaultanswers.ReturnsMoreEmptyValues;
import org.mockito.invocation.InvocationOnMock;
import org.mockito.stubbing.Answer;

public class ReturnsSmartNullsTest {

    @Test
    public void testAnswer() throws Throwable {
        ReturnsSmartNulls returnsSmartNulls = new ReturnsSmartNulls();
        
        // Creating mock for InvocationOnMock
        InvocationOnMock invocation = mock(InvocationOnMock.class);
        
        Object result = returnsSmartNulls.answer(invocation);
        
        assertNull(result);
    }

    @Test
    public void testThrowingInterceptor_intercept() throws Throwable {
        ReturnsSmartNulls returnsSmartNulls = new ReturnsSmartNulls();
        
        // Creating mock for InvocationOnMock
        InvocationOnMock invocation = mock(InvocationOnMock.class);
        
        // Creating mock for Method
        Method method = mock(Method.class);
        
        // Creating array of Objects
        Object[] args = new Object[5];
        
        // Creating mock for MethodProxy
        MethodProxy proxy = mock(MethodProxy.class);
        
        Object result = returnsSmartNulls.new ThrowingInterceptor(invocation).intercept(null, method, args, proxy);
        
        assertTrue(result instanceof String);
    }

    @Test
    public void testThrowingInterceptor_formatMethodCall() throws Throwable {
        ReturnsSmartNulls returnsSmartNulls = new ReturnsSmartNulls();
        
        // Creating mock for InvocationOnMock
        InvocationOnMock invocation = mock(InvocationOnMock.class);
        
        // Setting up mock for InvocationOnMock.getMethod()
        Method method = mock(Method.class);
        when(invocation.getMethod()).thenReturn(method);
        
        // Creating array of Objects
        Object[] args = new Object[2];
        args[0] = "arg1";
        args[1] = "arg2";
        when(invocation.getArguments()).thenReturn(args);
        
        String result = returnsSmartNulls.new ThrowingInterceptor(invocation).formatMethodCall();
        
        assertEquals("method(arg1, arg2)", result);
    }

    @Test
    public void testAnswer_defaultReturnValueNotNull() throws Throwable {
        ReturnsSmartNulls returnsSmartNulls = new ReturnsSmartNulls();
        
        // Creating mock for InvocationOnMock
        InvocationOnMock invocation = mock(InvocationOnMock.class);
        
        // Creating mock for ReturnsMoreEmptyValues
        ReturnsMoreEmptyValues delegate = mock(ReturnsMoreEmptyValues.class);
        when(delegate.answer(invocation)).thenReturn(new Object());
        
        Object result = returnsSmartNulls.answer(invocation);
        
        assertNotNull(result);
    }
    
    @Test
    public void testAnswer_defaultReturnValueNull() throws Throwable {
        ReturnsSmartNulls returnsSmartNulls = new ReturnsSmartNulls();
        
        // Creating mock for InvocationOnMock
        InvocationOnMock invocation = mock(InvocationOnMock.class);
        
        // Creating mock for ReturnsMoreEmptyValues
        ReturnsMoreEmptyValues delegate = mock(ReturnsMoreEmptyValues.class);
        when(delegate.answer(invocation)).thenReturn(null);
        
        // Creating mock for Method
        Method method = mock(Method.class);
        when(invocation.getMethod()).thenReturn(method);
        
        // Creating mock for ClassImposterizer
        ClassImposterizer imposterizer = mock(ClassImposterizer.class);
        when(ClassImposterizer.INSTANCE.canImposterise(any(Class.class))).thenReturn(true);
        when(ClassImposterizer.INSTANCE.imposterise(any(MethodInterceptor.class), any(Class.class))).thenReturn(null);
        
        Object result = returnsSmartNulls.answer(invocation);
        
        assertNull(result);
    }
    
    @Test
    public void testAnswer_typeNotMockable() throws Throwable {
        ReturnsSmartNulls returnsSmartNulls = new ReturnsSmartNulls();
        
        // Creating mock for InvocationOnMock
        InvocationOnMock invocation = mock(InvocationOnMock.class);
        
        // Creating mock for ReturnsMoreEmptyValues
        ReturnsMoreEmptyValues delegate = mock(ReturnsMoreEmptyValues.class);
        when(delegate.answer(invocation)).thenReturn(null);
        
        // Creating mock for Method
        Method method = mock(Method.class);
        when(invocation.getMethod()).thenReturn(method);
        
        // Creating mock for ClassImposterizer
        ClassImposterizer imposterizer = mock(ClassImposterizer.class);
        when(ClassImposterizer.INSTANCE.canImposterise(any(Class.class))).thenReturn(false);
        
        Object result = returnsSmartNulls.answer(invocation);
        
        assertNull(result);
    }

}