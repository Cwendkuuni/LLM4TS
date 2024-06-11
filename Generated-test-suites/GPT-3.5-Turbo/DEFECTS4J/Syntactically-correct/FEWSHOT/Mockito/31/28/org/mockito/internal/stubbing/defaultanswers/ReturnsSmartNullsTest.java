/*
 * Test for ReturnsSmartNulls.answer() method
 * Test cases cover all possible scenarios for the method
 */

import static org.junit.Assert.*;
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

public class ReturnsSmartNullsTest {
    @Test
    public void testAnswer_thenReturnValueNotNull() throws Throwable {
        // Setup
        InvocationOnMock invocation = Mockito.mock(InvocationOnMock.class);
        Answer<Object> delegate = Mockito.mock(Answer.class);
        Mockito.when(delegate.answer(invocation)).thenReturn(new Object());

        // Execute
        ReturnsSmartNulls returnsSmartNulls = new ReturnsSmartNulls();
        Object result = returnsSmartNulls.answer(invocation);

        // Verify
        assertNotNull(result);
        Mockito.verify(delegate).answer(invocation);
    }

    @Test
    public void testAnswer_thenReturnValueNull_CanImposterizeTrue() throws Throwable {
        // Setup
        InvocationOnMock invocation = Mockito.mock(InvocationOnMock.class);
        Answer<Object> delegate = Mockito.mock(Answer.class);
        Mockito.when(delegate.answer(invocation)).thenReturn(null);
        Method method = getClass().getMethod("testAnswer_thenReturnValueNull_CanImposterizeTrue");

        // Execute
        ReturnsSmartNulls returnsSmartNulls = new ReturnsSmartNulls();
        Object result = returnsSmartNulls.answer(invocation);

        // Verify
        assertNull(result);
        Mockito.verify(delegate).answer(invocation);
        assertTrue(ClassImposterizer.INSTANCE.canImposterise(method.getReturnType()));
    }

    @Test
    public void testAnswer_thenReturnValueNull_CanImposterizeFalse() throws Throwable {
        // Setup
        InvocationOnMock invocation = Mockito.mock(InvocationOnMock.class);
        Answer<Object> delegate = Mockito.mock(Answer.class);
        Mockito.when(delegate.answer(invocation)).thenReturn(null);
        Method method = getClass().getMethod("testAnswer_thenReturnValueNull_CanImposterizeFalse");

        // Execute
        ReturnsSmartNulls returnsSmartNulls = new ReturnsSmartNulls();
        Object result = returnsSmartNulls.answer(invocation);

        // Verify
        assertNull(result);
        Mockito.verify(delegate).answer(invocation);
        assertFalse(ClassImposterizer.INSTANCE.canImposterise(method.getReturnType()));
    }

    @Test
    public void testAnswer_thenReturnValueNull_isToStringMethod() throws Throwable {
        // Setup
        InvocationOnMock invocation = Mockito.mock(InvocationOnMock.class);
        Answer<Object> delegate = Mockito.mock(Answer.class);
        Mockito.when(delegate.answer(invocation)).thenReturn(null);
        Method method = Object.class.getMethod("toString");

        // Execute
        ReturnsSmartNulls returnsSmartNulls = new ReturnsSmartNulls();
        Object result = returnsSmartNulls.answer(invocation);

        // Verify
        assertNotNull(result);
        assertEquals("SmartNull returned by unstubbed toString() method on mock", result);
        Mockito.verify(delegate).answer(invocation);
    }

    @Test(expected = Reporter.SmartNullPointerException.class)
    public void testAnswer_thenReturnValueNull_notToStringMethod() throws Throwable {
        // Setup
        InvocationOnMock invocation = Mockito.mock(InvocationOnMock.class);
        Answer<Object> delegate = Mockito.mock(Answer.class);
        Mockito.when(delegate.answer(invocation)).thenReturn(null);
        Method method = getClass().getMethod("testAnswer_thenReturnValueNull_notToStringMethod");

        // Execute
        ReturnsSmartNulls returnsSmartNulls = new ReturnsSmartNulls();
        returnsSmartNulls.answer(invocation);

        // Verify
        Mockito.verify(delegate).answer(invocation);
    }
}