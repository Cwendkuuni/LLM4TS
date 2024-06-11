import static org.mockito.Mockito.*;
import org.junit.Test;

public class ReturnsSmartNullsTest {

    @Test
    public void testAnswer_nullReturnValue() {
        ReturnsMoreEmptyValues delegate = mock(ReturnsMoreEmptyValues.class);
        when(delegate.answer(any(InvocationOnMock.class))).thenReturn(null);

        ReturnsSmartNulls smartNulls = new ReturnsSmartNulls();
        InvocationOnMock invocation = mock(InvocationOnMock.class);
        Object result = smartNulls.answer(invocation);

        assertNull(result);
    }

    @Test
    public void testAnswer_defaultReturnValue() {
        ReturnsMoreEmptyValues delegate = mock(ReturnsMoreEmptyValues.class);
        when(delegate.answer(any(InvocationOnMock.class))).thenReturn("default");

        ReturnsSmartNulls smartNulls = new ReturnsSmartNulls();
        InvocationOnMock invocation = mock(InvocationOnMock.class);
        Object result = smartNulls.answer(invocation);

        assertEquals("default", result);
    }

    @Test
    public void testAnswer_mockableReturnType() {
        ReturnsMoreEmptyValues delegate = mock(ReturnsMoreEmptyValues.class);
        when(delegate.answer(any(InvocationOnMock.class))).thenReturn(null);

        ReturnsSmartNulls smartNulls = new ReturnsSmartNulls();
        InvocationOnMock invocation = mock(InvocationOnMock.class);
        Method method = mock(Method.class);
        when(invocation.getMethod()).thenReturn(method);
        Class<?> returnType = mock(Class.class);
        when(method.getReturnType()).thenReturn(returnType);
        when(ClassImposterizer.INSTANCE.canImposterise(returnType)).thenReturn(true);

        Object result = smartNulls.answer(invocation);

        assertNotNull(result);
        verify(ClassImposterizer.INSTANCE, times(1)).imposterise(any(ThrowsInterceptor.class), eq(returnType));
    }

    @Test
    public void testAnswer_nonMockableReturnType() {
        ReturnsMoreEmptyValues delegate = mock(ReturnsMoreEmptyValues.class);
        when(delegate.answer(any(InvocationOnMock.class))).thenReturn(null);

        ReturnsSmartNulls smartNulls = new ReturnsSmartNulls();
        InvocationOnMock invocation = mock(InvocationOnMock.class);
        Method method = mock(Method.class);
        when(invocation.getMethod()).thenReturn(method);
        Class<?> returnType = mock(Class.class);
        when(method.getReturnType()).thenReturn(returnType);
        when(ClassImposterizer.INSTANCE.canImposterise(returnType)).thenReturn(false);

        Object result = smartNulls.answer(invocation);

        assertNull(result);
        verify(ClassImposterizer.INSTANCE, times(0)).imposterise(any(ThrowsInterceptor.class), eq(returnType));
    }
}