import org.junit.Test;
import org.mockito.invocation.InvocationOnMock;
import org.mockito.stubbing.Answer;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.mockito.internal.util.reflection.Whitebox;

import static org.junit.Assert.*;
import static org.mockito.Mockito.*;

public class ReturnsSmartNullsTest {

    @Mock
    InvocationOnMock invocationMock;

    @Test
    public void testAnswerReturnsSmartNullWhenReturnValueIsNull() throws Throwable {
        ReturnsSmartNulls returnsSmartNulls = new ReturnsSmartNulls();
        Object returnValue = returnsSmartNulls.answer(invocationMock);

        assertNull("Return value should be null", returnValue);

        verify(invocationMock).getMethod();
    }

    @Test
    public void testAnswerReturnsDefaultReturnValueIfNotNull() throws Throwable {
        ReturnsMoreEmptyValues delegateMock = mock(ReturnsMoreEmptyValues.class);
        when(delegateMock.answer(any(InvocationOnMock.class))).thenReturn("DefaultReturnValue");

        ReturnsSmartNulls returnsSmartNulls = new ReturnsSmartNulls();
        Whitebox.setInternalState(returnsSmartNulls, "delegate", delegateMock);

        Object returnValue = returnsSmartNulls.answer(invocationMock);

        assertEquals("Return value should be \"DefaultReturnValue\"", "DefaultReturnValue", returnValue);

        verify(invocationMock).getMethod();
        verify(delegateMock).answer(any(InvocationOnMock.class));
    }

    @Test
    public void testAnswerReturnsSmartNullWhenReturnTypeIsMockable() throws Throwable {
        Method methodMock = mock(Method.class);
        when(invocationMock.getMethod()).thenReturn(methodMock);
        when(methodMock.getReturnType()).thenReturn(Object.class);

        ClassImposterizer classImposterizerMock = mock(ClassImposterizer.class);
        whenNew(ClassImposterizer.class).withNoArguments().thenReturn(classImposterizerMock);
        when(classImposterizerMock.canImposterise(eq(Object.class))).thenReturn(true);

        MethodInterceptor interceptorMock = mock(MethodInterceptor.class);
        whenNew(ThrowingInterceptor.class).withArguments(eq(invocationMock)).thenReturn(interceptorMock);

        ReturnsSmartNulls returnsSmartNulls = new ReturnsSmartNulls();
        Whitebox.setInternalState(returnsSmartNulls, "delegate", mock(ReturnsMoreEmptyValues.class));

        Object returnValue = returnsSmartNulls.answer(invocationMock);

        assertNotNull("Return value should not be null", returnValue);

        verify(invocationMock).getMethod();
        verifyNew(ThrowingInterceptor.class).withArguments(invocationMock);
        verify(classImposterizerMock).imposterise(interceptorMock, Object.class);
    }

    @Test
    public void testAnswerReturnsNullWhenReturnTypeIsNotMockable() throws Throwable {
        Method methodMock = mock(Method.class);
        when(invocationMock.getMethod()).thenReturn(methodMock);
        when(methodMock.getReturnType()).thenReturn(String.class);

        ClassImposterizer classImposterizerMock = mock(ClassImposterizer.class);
        whenNew(ClassImposterizer.class).withNoArguments().thenReturn(classImposterizerMock);
        when(classImposterizerMock.canImposterise(eq(String.class))).thenReturn(false);

        ReturnsSmartNulls returnsSmartNulls = new ReturnsSmartNulls();
        Whitebox.setInternalState(returnsSmartNulls, "delegate", mock(ReturnsMoreEmptyValues.class));

        Object returnValue = returnsSmartNulls.answer(invocationMock);

        assertNull("Return value should be null", returnValue);

        verify(invocationMock).getMethod();
        verifyNoMoreInteractions(classImposterizerMock);
    }

    @Test
    public void testAnswerInterceptsToStringMethodAndReturnsSmartNullString() throws Throwable {
        ObjectMethodsGuru guruMock = mock(ObjectMethodsGuru.class);
        whenNew(ObjectMethodsGuru.class).withNoArguments().thenReturn(guruMock);
        when(guruMock.isToString(any(Method.class))).thenReturn(true);

        ReturnsSmartNulls returnsSmartNulls = new ReturnsSmartNulls();
        Whitebox.setInternalState(returnsSmartNulls, "delegate", mock(ReturnsMoreEmptyValues.class));

        Object returnValue = returnsSmartNulls.answer(invocationMock);

        assertEquals("Return value should be a string containing method name", "SmartNull returned by unstubbed method on mock", returnValue);

        verify(guruMock).isToString(any(Method.class));
    }

}