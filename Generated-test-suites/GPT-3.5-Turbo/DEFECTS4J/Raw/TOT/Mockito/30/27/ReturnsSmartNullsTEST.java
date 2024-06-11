import org.junit.Test;

import static org.mockito.Mockito.*;
import static org.junit.Assert.*;

public class ReturnsSmartNullsTest {

    @Test
    public void testAnswer_OrdinaryReturnValues() throws Throwable {
        ReturnsSmartNulls returnsSmartNulls = new ReturnsSmartNulls();

        InvocationOnMock invocation = mock(InvocationOnMock.class);
        Object returnValue = returnsSmartNulls.answer(invocation);

        assertNotNull("Returned value should not be null", returnValue);
    }

    @Test
    public void testAnswer_NonMockableReturnType() throws Throwable {
        ReturnsSmartNulls returnsSmartNulls = new ReturnsSmartNulls();

        InvocationOnMock invocation = mock(InvocationOnMock.class);
        Method method = InvocationOnMock.class.getMethod("getInvocation", null); // A method with non-mockable return type

        when(invocation.getMethod()).thenReturn(method);

        Object returnValue = returnsSmartNulls.answer(invocation);

        assertNull("Returned value should be null", returnValue);
    }

    @Test
    public void testAnswer_ToStringMethod() throws Throwable {
        ReturnsSmartNulls returnsSmartNulls = new ReturnsSmartNulls();

        InvocationOnMock invocation = mock(InvocationOnMock.class);
        Method method = Object.class.getMethod("toString", null); // toString method

        when(invocation.getMethod()).thenReturn(method);

        Object returnValue = returnsSmartNulls.answer(invocation);

        assertTrue("Returned value should be a string", returnValue instanceof String);
        assertEquals("SmartNull returned by unstubbed toString method on mock", returnValue);
    }

    @Test(expected = NullPointerException.class)
    public void testAnswer_NullDefaultReturnValue() throws Throwable {
        ReturnsSmartNulls returnsSmartNulls = new ReturnsSmartNulls();

        InvocationOnMock invocation = mock(InvocationOnMock.class);

        returnsSmartNulls.answer(invocation);
    }

}