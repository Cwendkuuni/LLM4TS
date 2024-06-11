import static org.junit.Assert.*;
import static org.mockito.Mockito.*;

import org.junit.Test;
import org.mockito.cglib.proxy.MethodInterceptor;
import org.mockito.exceptions.verification.SmartNullPointerException;
import org.mockito.invocation.InvocationOnMock;

public class ReturnsSmartNullsTest {

    @Test
    public void testAnswerWithNonNullDefaultReturnValue() throws Throwable {
        ReturnsSmartNulls returnsSmartNulls = new ReturnsSmartNulls();
        InvocationOnMock invocation = mock(InvocationOnMock.class);

        Object defaultReturnValue = "default";
        when(invocation.getMethod().getReturnType()).thenReturn(String.class);
        when(invocation.getMethod().getName()).thenReturn("mockMethod");
        when(delegate.answer(invocation)).thenReturn(defaultReturnValue);

        Object result = returnsSmartNulls.answer(invocation);
        
        assertNotNull(result);
        assertEquals(defaultReturnValue, result);
    }

    @Test
    public void testAnswerWithNullDefaultReturnValueAndImposterizableType() throws Throwable {
        ReturnsSmartNulls returnsSmartNulls = new ReturnsSmartNulls();
        InvocationOnMock invocation = mock(InvocationOnMock.class);

        when(invocation.getMethod().getReturnType()).thenReturn(Void.class);
        when(ClassImposterizer.INSTANCE.canImposterise(Void.class)).thenReturn(true);

        Object result = returnsSmartNulls.answer(invocation);
        
        assertNotNull(result);
        assertTrue(result instanceof MethodInterceptor);
    }

    @Test(expected = SmartNullPointerException.class)
    public void testAnswerWithNullDefaultReturnValueAndNonImposterizableType() throws Throwable {
        ReturnsSmartNulls returnsSmartNulls = new ReturnsSmartNulls();
        InvocationOnMock invocation = mock(InvocationOnMock.class);

        when(invocation.getMethod().getReturnType()).thenReturn(Object.class);
        when(ClassImposterizer.INSTANCE.canImposterise(Object.class)).thenReturn(false);

        returnsSmartNulls.answer(invocation);
    }

}