import org.junit.Assert;
import org.junit.Test;
import org.mockito.invocation.InvocationOnMock;
import org.mockito.internal.debugging.Location;
import org.mockito.stubbing.Answer;

import static org.mockito.Mockito.*;

public class ReturnsSmartNullsTest {

    @Test
    public void testAnswer() throws Throwable {
        ReturnsSmartNulls returnsSmartNulls = new ReturnsSmartNulls();
        
        // Create mock InvocationOnMock object
        InvocationOnMock invocation = mock(InvocationOnMock.class);
        
        // Test when defaultReturnValue is not null
        when(invocation.getMethod().getReturnType()).thenReturn(String.class);
        when(invocation.getMethod().getName()).thenReturn("testMethod");
        when(invocation.getMethod().getDeclaringClass()).thenReturn(ReturnsSmartNulls.class);
        Object result = returnsSmartNulls.answer(invocation);
        
        Assert.assertNotNull(result);
        
        // Test when defaultReturnValue is null and type can be imposterised
        when(invocation.getMethod().getReturnType()).thenReturn(Integer.class);
        when(invocation.getMethod().getName()).thenReturn("testMethod");
        when(invocation.getMethod().getDeclaringClass()).thenReturn(ReturnsSmartNulls.class);
        ClassImposterizer classImposterizer = mock(ClassImposterizer.class);
        when(classImposterizer.canImposterise(Integer.class)).thenReturn(true);
        when(classImposterizer.imposterise(any(ThrowsInterceptor.class), eq(Integer.class))).thenReturn(new Object());
        
        result = returnsSmartNulls.answer(invocation);
        
        Assert.assertNotNull(result);
        
        // Test when defaultReturnValue is null and type cannot be imposterised
        when(invocation.getMethod().getReturnType()).thenReturn(Long.class);
        when(invocation.getMethod().getName()).thenReturn("testMethod");
        when(invocation.getMethod().getDeclaringClass()).thenReturn(ReturnsSmartNulls.class);
        ClassImposterizer classImposterizer = mock(ClassImposterizer.class);
        when(classImposterizer.canImposterise(Long.class)).thenReturn(false);
        
        result = returnsSmartNulls.answer(invocation);
        
        Assert.assertNull(result);
    }

    @Test
    public void testThrowingInterceptor() throws Throwable {
        ReturnsSmartNulls returnsSmartNulls = new ReturnsSmartNulls();
        
        // Create mock InvocationOnMock object
        InvocationOnMock invocation = mock(InvocationOnMock.class);
        
        // Test when method is toString
        Method method = mock(Method.class);
        when(method.getName()).thenReturn("toString");
        when(invocation.getMethod()).thenReturn(method);
        
        Object result = returnsSmartNulls.new ThrowingInterceptor(invocation).intercept(null, null, null, null);
        
        Assert.assertNotNull(result);
        Assert.assertEquals("SmartNull returned by unstubbed toString() method on mock", result);
        
        // Test when method is not toString
        method = mock(Method.class);
        when(method.getName()).thenReturn("testMethod");
        when(invocation.getMethod()).thenReturn(method);
        
        Location location = mock(Location.class);
        when(invocation.getArguments()).thenReturn(new Object[] { location });
        
        result = returnsSmartNulls.new ThrowingInterceptor(invocation).intercept(null, null, null, null);
        
        Assert.assertNull(result);
        verify(new Reporter()).smartNullPointerException(location);
    }
}