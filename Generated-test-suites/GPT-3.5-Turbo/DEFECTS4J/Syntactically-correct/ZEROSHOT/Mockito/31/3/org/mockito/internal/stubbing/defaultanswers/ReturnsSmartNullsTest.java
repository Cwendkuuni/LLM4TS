import org.junit.Test;
import org.mockito.Mockito;
import org.mockito.invocation.InvocationOnMock;
import org.mockito.stubbing.Answer;
import org.mockito.exceptions.Reporter;
import org.mockito.internal.creation.jmock.ClassImposterizer;
import org.mockito.internal.debugging.Location;
import org.mockito.internal.util.ObjectMethodsGuru;
import org.mockito.cglib.proxy.MethodInterceptor;
import org.mockito.cglib.proxy.MethodProxy;

import static org.junit.Assert.*;
import static org.mockito.Mockito.*;

public class ReturnsSmartNullsTest {

    @Test
    public void testAnswer_defaultReturnValueNotNull() throws Throwable {
        ReturnsMoreEmptyValues delegate = mock(ReturnsMoreEmptyValues.class);
        InvocationOnMock invocation = mock(InvocationOnMock.class);
        Object defaultReturnValue = new Object();
        
        when(delegate.answer(invocation)).thenReturn(defaultReturnValue);
        
        ReturnsSmartNulls returnsSmartNulls = new ReturnsSmartNulls(delegate);
        Object result = returnsSmartNulls.answer(invocation);
        
        assertEquals(defaultReturnValue, result);
    }
    
    @Test
    public void testAnswer_defaultReturnValueNull() throws Throwable {
        ReturnsMoreEmptyValues delegate = mock(ReturnsMoreEmptyValues.class);
        InvocationOnMock invocation = mock(InvocationOnMock.class);
        
        when(delegate.answer(invocation)).thenReturn(null);
        
        ReturnsSmartNulls returnsSmartNulls = new ReturnsSmartNulls(delegate);
        Object result = returnsSmartNulls.answer(invocation);
        
        assertNull(result);
    }
    
    @Test
    public void testAnswer_typeMockable() throws Throwable {
        ReturnsMoreEmptyValues delegate = mock(ReturnsMoreEmptyValues.class);
        InvocationOnMock invocation = mock(InvocationOnMock.class);
        Method method = mock(Method.class);
        Class<?> returnType = mock(Class.class);
        Object proxy = new Object();
        ThrowingInterceptor throwingInterceptor = mock(ThrowingInterceptor.class);
        
        when(delegate.answer(invocation)).thenReturn(null);
        when(invocation.getMethod()).thenReturn(method);
        when(method.getReturnType()).thenReturn(returnType);
        when(ClassImposterizer.INSTANCE.canImposterise(returnType)).thenReturn(true);
        when(ClassImposterizer.INSTANCE.imposterise(throwingInterceptor, returnType)).thenReturn(proxy);
        
        ReturnsSmartNulls returnsSmartNulls = new ReturnsSmartNulls(delegate);
        Object result = returnsSmartNulls.answer(invocation);
        
        assertEquals(proxy, result);
    }
    
    @Test
    public void testAnswer_typeNotMockable() throws Throwable {
        ReturnsMoreEmptyValues delegate = mock(ReturnsMoreEmptyValues.class);
        InvocationOnMock invocation = mock(InvocationOnMock.class);
        Method method = mock(Method.class);
        Class<?> returnType = mock(Class.class);
        
        when(delegate.answer(invocation)).thenReturn(null);
        when(invocation.getMethod()).thenReturn(method);
        when(method.getReturnType()).thenReturn(returnType);
        when(ClassImposterizer.INSTANCE.canImposterise(returnType)).thenReturn(false);
        
        ReturnsSmartNulls returnsSmartNulls = new ReturnsSmartNulls(delegate);
        Object result = returnsSmartNulls.answer(invocation);
        
        assertNull(result);
    }

}