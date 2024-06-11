import static org.mockito.Mockito.*;

import org.junit.Before;
import org.junit.Test;
import org.mockito.invocation.InvocationOnMock;

import java.lang.reflect.Method;

public class ReturnsSmartNullsTest {

    private ReturnsSmartNulls returnsSmartNulls;

    @Before
    public void setup() {
        returnsSmartNulls = new ReturnsSmartNulls();
    }

    @Test
    public void testAnswer_OrdinaryReturnValue() throws Throwable {
        InvocationOnMock invocation = mock(InvocationOnMock.class);
        when(invocation.getMethod().getReturnType()).thenReturn(String.class);
        
        Object result = returnsSmartNulls.answer(invocation);
        
        assertNull(result);
        
        verify(invocation).getMethod().getReturnType();
    }
    
    @Test
    public void testAnswer_CanImposterise() throws Throwable {
        InvocationOnMock invocation = mock(InvocationOnMock.class);
        Method method = mock(Method.class);
        Class<?> returnType = mock(Class.class);
        
        when(invocation.getMethod()).thenReturn(method);
        when(method.getReturnType()).thenReturn(returnType);
        when(ClassImposterizer.INSTANCE.canImposterise(returnType)).thenReturn(true);
        
        Object result = returnsSmartNulls.answer(invocation);
        
        assertNull(result);
        
        verify(invocation).getMethod();
        verify(method).getReturnType();
        verify(ClassImposterizer.INSTANCE).canImposterise(returnType);
        verify(ClassImposterizer.INSTANCE).imposterise(any(), eq(returnType));
    }
    
    @Test
    public void testAnswer_NullableType() throws Throwable {
        InvocationOnMock invocation = mock(InvocationOnMock.class);
        Method method = mock(Method.class);
        
        when(invocation.getMethod()).thenReturn(method);
        when(method.getReturnType()).thenReturn(Integer.class);
        when(ClassImposterizer.INSTANCE.canImposterise(Integer.class)).thenReturn(false);
        
        Object result = returnsSmartNulls.answer(invocation);
        
        assertNotNull(result);
        assertTrue(result instanceof Integer);
        
        verify(invocation).getMethod();
        verify(method).getReturnType();
        verify(ClassImposterizer.INSTANCE).canImposterise(Integer.class);
    }
    
    @Test(expected = NullPointerException.class)
    public void testAnswer_NullDefaultReturnValue() throws Throwable {
        InvocationOnMock invocation = mock(InvocationOnMock.class);
        
        when(invocation.getMethod().getReturnType()).thenReturn(Integer.class);
        when(returnsSmartNulls.answer(invocation)).thenReturn(null);
        
        returnsSmartNulls.answer(invocation);
        
        verify(invocation).getMethod().getReturnType();
    }
    
    @Test
    public void testAnswer_ToStringMethod() throws Throwable {
        InvocationOnMock invocation = mock(InvocationOnMock.class);
        Method method = String.class.getMethod("toString");
        
        when(invocation.getMethod()).thenReturn(method);
        when(new ObjectMethodsGuru().isToString(method)).thenReturn(true);
        
        Object result = returnsSmartNulls.answer(invocation);
        
        assertNotNull(result);
        assertTrue(result instanceof String);
        assertEquals("SmartNull returned by unstubbed toString() method on mock", result);
        
        verify(invocation).getMethod();
        verify(new ObjectMethodsGuru()).isToString(method);
    }
    
    @Test(expected = NullPointerException.class)
    public void testAnswer_ThrowException() throws Throwable {
        InvocationOnMock invocation = mock(InvocationOnMock.class);
        
        when(invocation.getMethod().getReturnType()).thenReturn(Integer.class);
        when(new Reporter().smartNullPointerException(any())).thenThrow(NullPointerException.class);
        
        returnsSmartNulls.answer(invocation);
        
        verify(invocation).getMethod().getReturnType();
        verify(new Reporter()).smartNullPointerException(any());
    }
}