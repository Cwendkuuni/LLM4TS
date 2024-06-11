import org.junit.Before;
import org.junit.Test;
import org.mockito.invocation.InvocationOnMock;
import org.mockito.stubbing.Answer;

import static org.mockito.Mockito.*;

public class ReturnsSmartNullsTest {

    private ReturnsSmartNulls returnsSmartNulls;
    private InvocationOnMock invocationOnMock;

    @Before
    public void setUp() {
        returnsSmartNulls = new ReturnsSmartNulls();
        invocationOnMock = mock(InvocationOnMock.class);
    }

    @Test
    public void testAnswer_nullDefaultReturnValue() throws Throwable {
        when(invocationOnMock.getMethod()).thenReturn(ReturnsSmartNulls.class.getDeclaredMethod("intercept", Object.class, Method.class, Object[].class, MethodProxy.class));
        
        Object result = returnsSmartNulls.answer(invocationOnMock);
        
        assertNull(result);
    }
    
    @Test
    public void testAnswer_defaultReturnValueNotNull() throws Throwable {
        Answer<Object> delegate = mock(Answer.class);
        when(delegate.answer(invocationOnMock)).thenReturn("DefaultReturnValue");
        
        when(invocationOnMock.getMethod()).thenReturn(ReturnsSmartNulls.class.getDeclaredMethod("answer", InvocationOnMock.class));
        
        returnsSmartNulls.answer(invocationOnMock);
        
        verify(delegate).answer(invocationOnMock);
    }
    
    @Test
    public void testAnswer_typeCanBeImposterised() throws Throwable {
        Class<?> type = ReturnsSmartNulls.class;
        Method method = ReturnsSmartNulls.class.getDeclaredMethod("formatMethodCall");
        
        when(invocationOnMock.getMethod()).thenReturn(method);
        when(method.getReturnType()).thenReturn(type);
        
        Object result = returnsSmartNulls.answer(invocationOnMock);
        
        assertNotNull(result);
    }

    @Test
    public void testAnswer_typeCannotBeImposterised() throws Throwable {
        Class<?> type = String.class;
        Method method = ReturnsSmartNulls.class.getDeclaredMethod("intercept", Object.class, Method.class, Object[].class, MethodProxy.class);
        
        when(invocationOnMock.getMethod()).thenReturn(method);
        when(method.getReturnType()).thenReturn(type);
        
        Object result = returnsSmartNulls.answer(invocationOnMock);
        
        assertNull(result);
    }
}