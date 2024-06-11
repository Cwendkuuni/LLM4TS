import org.junit.Test;
import org.mockito.invocation.InvocationOnMock;

import static org.junit.Assert.*;
import static org.mockito.Mockito.*;

public class ReturnsSmartNullsTest {

    private ReturnsSmartNulls returnsSmartNulls = new ReturnsSmartNulls();

    @Test
    public void testAnswer_defaultReturnValueNotNull() throws Throwable {
        InvocationOnMock invocation = mock(InvocationOnMock.class);
        Object defaultReturnValue = new Object();

        Object result = returnsSmartNulls.answer(invocation);

        assertEquals(defaultReturnValue, result);
    }

    @Test
    public void testAnswer_defaultReturnValueNull_mockableType() throws Throwable {
        InvocationOnMock invocation = mock(InvocationOnMock.class);
        Class<?> returnType = mock(Class.class);
        when(invocation.getMethod().getReturnType()).thenReturn(returnType);
        when(ClassImposterizer.INSTANCE.canImposterise(returnType)).thenReturn(true);

        Object result = returnsSmartNulls.answer(invocation);

        assertNotNull(result);
    }

    @Test
    public void testAnswer_defaultReturnValueNull_nonMockableType() throws Throwable {
        InvocationOnMock invocation = mock(InvocationOnMock.class);
        Class<?> returnType = mock(Class.class);
        when(invocation.getMethod().getReturnType()).thenReturn(returnType);
        when(ClassImposterizer.INSTANCE.canImposterise(returnType)).thenReturn(false);

        Object result = returnsSmartNulls.answer(invocation);

        assertNull(result);
    }
}