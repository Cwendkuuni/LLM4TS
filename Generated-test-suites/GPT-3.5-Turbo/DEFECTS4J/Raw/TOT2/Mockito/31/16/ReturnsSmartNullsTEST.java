import static org.junit.Assert.*;
import static org.mockito.Mockito.*;

import org.junit.Test;
import org.mockito.cglib.proxy.MethodProxy;
import org.mockito.exceptions.verification.smart.MockitoException;
import org.mockito.internal.invocation.Invocation;
import org.mockito.invocation.InvocationOnMock;

public class ReturnsSmartNullsTest {

    @Test
    public void testAnswer() throws Throwable {
        ReturnsSmartNulls returnsSmartNulls = new ReturnsSmartNulls();

        ReturnsMoreEmptyValues delegate = mock(ReturnsMoreEmptyValues.class);
        when(delegate.answer(any(InvocationOnMock.class))).thenReturn(null);

        InvocationOnMock invocation1 = mock(InvocationOnMock.class);
        when(invocation1.getMethod()).thenReturn(SomeClass.class.getMethod("someMethod"));
        Object result1 = returnsSmartNulls.answer(invocation1);
        assertNull(result1);

        InvocationOnMock invocation2 = mock(InvocationOnMock.class);
        when(invocation2.getMethod()).thenReturn(SomeClass.class.getMethod("toString"));
        Object result2 = returnsSmartNulls.answer(invocation2);
        assertEquals("SmartNull returned by unstubbed toString() method on mock", result2);
    }

    @Test
    public void testAnswerWithUnmockableType() throws Throwable {
        ReturnsSmartNulls returnsSmartNulls = new ReturnsSmartNulls();

        InvocationOnMock invocation = mock(InvocationOnMock.class);
        when(invocation.getMethod()).thenReturn(SomeClass.class.getMethod("finalMethod"));
        Object result = returnsSmartNulls.answer(invocation);
        assertNull(result);
    }

    @Test(expected = MockitoException.class)
    public void testAnswerThrowsException() throws Throwable {
        ReturnsSmartNulls returnsSmartNulls = new ReturnsSmartNulls();

        InvocationOnMock invocation = mock(InvocationOnMock.class);
        when(invocation.getMethod()).thenThrow(MockitoException.class);
        Object result = returnsSmartNulls.answer(invocation);
    }

}