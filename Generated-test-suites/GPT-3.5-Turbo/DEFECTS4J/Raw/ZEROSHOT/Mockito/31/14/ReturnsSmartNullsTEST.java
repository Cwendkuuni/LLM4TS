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
import static org.junit.Assert.*;
import static org.mockito.Mockito.*;

public class ReturnsSmartNullsTest {
    
    @Test
    public void testAnswer_WithDefaultReturnValue() throws Throwable {
        // Setup
        ReturnsMoreEmptyValues delegate = mock(ReturnsMoreEmptyValues.class);
        ReturnsSmartNulls returnsSmartNulls = new ReturnsSmartNulls();
        InvocationOnMock invocation = mock(InvocationOnMock.class);
        
        // Stubbing
        when(delegate.answer(invocation)).thenReturn(null);
        
        // Execution
        Object result = returnsSmartNulls.answer(invocation);
        
        // Verification
        assertNull(result);
        verify(delegate).answer(invocation);
    }
    
    @Test
    public void testAnswer_WithNonMockableReturnType() throws Throwable {
        // Setup
        ReturnsMoreEmptyValues delegate = mock(ReturnsMoreEmptyValues.class);
        ReturnsSmartNulls returnsSmartNulls = new ReturnsSmartNulls();
        InvocationOnMock invocation = mock(InvocationOnMock.class);
        Method method = mock(Method.class);
        when(invocation.getMethod()).thenReturn(method);
        when(method.getReturnType()).thenReturn(String.class);
        
        // Stubbing
        when(delegate.answer(invocation)).thenReturn(null);
        
        // Execution
        Object result = returnsSmartNulls.answer(invocation);
        
        // Verification
        assertNull(result);
        verify(delegate).answer(invocation);
    }
    
    @Test
    public void testAnswer_WithMockableReturnType() throws Throwable {
        // Setup
        ReturnsMoreEmptyValues delegate = mock(ReturnsMoreEmptyValues.class);
        ReturnsSmartNulls returnsSmartNulls = new ReturnsSmartNulls();
        InvocationOnMock invocation = mock(InvocationOnMock.class);
        Method method = mock(Method.class);
        when(invocation.getMethod()).thenReturn(method);
        when(method.getReturnType()).thenReturn(Object.class);
        
        // Stubbing
        when(delegate.answer(invocation)).thenReturn(null);
        when(ClassImposterizer.INSTANCE.canImposterise(Object.class)).thenReturn(true);
        
        // Execution
        Object result = returnsSmartNulls.answer(invocation);
        
        // Verification
        assertNotNull(result);
        verify(delegate).answer(invocation);
        verify(ClassImposterizer.INSTANCE).canImposterise(Object.class);
        verify(ClassImposterizer.INSTANCE).imposterise(any(MethodInterceptor.class), eq(Object.class));
    }
    
    @Test
    public void testThrowingInterceptor_InterceptToStringMethod() throws Throwable {
        // Setup
        ReturnsSmartNulls.ThrowingInterceptor throwingInterceptor = new ReturnsSmartNulls().new ThrowingInterceptor(null);
        Method method = String.class.getDeclaredMethod("toString");
        Object[] args = new Object[0];
        MethodProxy proxy = mock(MethodProxy.class);
        
        // Execution
        Object result = throwingInterceptor.intercept(null, method, args, proxy);
        
        // Verification
        assertNotNull(result);
        assertTrue(result instanceof String);
        verifyNoInteractions(proxy);
    }
    
    @Test(expected = Throwable.class)
    public void testThrowingInterceptor_InterceptNonToStringMethod() throws Throwable {
        // Setup
        ReturnsSmartNulls.ThrowingInterceptor throwingInterceptor = new ReturnsSmartNulls().new ThrowingInterceptor(null);
        Method method = Object.class.getDeclaredMethod("hashCode");
        Object[] args = new Object[0];
        MethodProxy proxy = mock(MethodProxy.class);
        
        // Execution
        throwingInterceptor.intercept(null, method, args, proxy);
    }
    
    @Test
    public void testThrowsSmartNullPointerException() throws Throwable {
        // Setup
        ReturnsSmartNulls. ThrowingInterceptor throwingInterceptor = new ReturnsSmartNulls().new ThrowingInterceptor(null);
        Location location = new Location();
        Reporter reporter = mock(Reporter.class);
        Method method = mock(Method.class);
        when(method.getName()).thenReturn("testMethod");
        InvocationOnMock invocation = mock(InvocationOnMock.class);
        when(invocation.getMethod()).thenReturn(method);
        
        // Execution
        Object result = throwingInterceptor.intercept(null, method, null, null);
        
        // Verification
        assertNull(result);
        verify(reporter).smartNullPointerException(location);
    }
}