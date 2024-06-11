import org.junit.Before;
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

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.mockito.Mockito.*;

public class ReturnsSmartNullsTest {

    private ReturnsSmartNulls returnsSmartNulls;
    private Answer<Object> delegateMock;

    @Before
    public void setup() {
        returnsSmartNulls = new ReturnsSmartNulls();
        delegateMock = mock(Answer.class);
    }

    @Test
    public void testAnswerReturnsNull() throws Throwable {
        InvocationOnMock invocationMock = mock(InvocationOnMock.class);
        when(delegateMock.answer(invocationMock)).thenReturn(null);

        Object result = returnsSmartNulls.answer(invocationMock);

        assertNull(result);
    }

    @Test
    public void testAnswerReturnsDefaultReturnValue() throws Throwable {
        Object defaultReturnValueMock = mock(Object.class);
        InvocationOnMock invocationMock = mock(InvocationOnMock.class);
        when(delegateMock.answer(invocationMock)).thenReturn(defaultReturnValueMock);

        Object result = returnsSmartNulls.answer(invocationMock);

        assertNotNull(result);
        assertEquals(defaultReturnValueMock, result);
    }

    @Test
    public void testAnswerReturnsImposterizedValue() throws Throwable {
        Object returnValueMock = mock(Object.class);
        InvocationOnMock invocationMock = mock(InvocationOnMock.class);
        when(delegateMock.answer(invocationMock)).thenReturn(null);

        ClassImposterizer classImposterizerMock = mock(ClassImposterizer.class);
        when(ClassImposterizer.INSTANCE.canImposterise(returnValueMock.getClass())).thenReturn(true);
        when(ClassImposterizer.INSTANCE.imposterise(any(MethodInterceptor.class), eq(returnValueMock.getClass())))
                .thenReturn(returnValueMock);

        Object result = returnsSmartNulls.answer(invocationMock);

        assertNotNull(result);
        assertEquals(returnValueMock, result);
    }

    @Test
    public void testAnswerThrowsExceptionForToStringMethod() throws Throwable {
        InvocationOnMock invocationMock = mock(InvocationOnMock.class);
        when(new ObjectMethodsGuru().isToString(invocationMock.getMethod())).thenReturn(true);
        when(invocationMock.getArguments()).thenReturn(new Object[]{});

        Object result = returnsSmartNulls.answer(invocationMock);

        assertNotNull(result);
        assertEquals("SmartNull returned by unstubbed method on mock", result);
    }

    @Test(expected = Reporter.SmartNullPointerException.class)
    public void testAnswerThrowsExceptionForOtherMethods() throws Throwable {
        InvocationOnMock invocationMock = mock(InvocationOnMock.class);
        when(new ObjectMethodsGuru().isToString(invocationMock.getMethod())).thenReturn(false);

        returnsSmartNulls.answer(invocationMock);
    }
}