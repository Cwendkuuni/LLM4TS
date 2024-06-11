
package org.mockito.internal.stubbing.defaultanswers;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNull;
import static org.mockito.Mockito.mock;

import org.junit.Test;
import org.mockito.invocation.InvocationOnMock;

public class ReturnsSmartNullsTest {
    @Test
    public void testAnswer() throws Throwable {
        ReturnsSmartNulls smartNulls = new ReturnsSmartNulls();

        // Test ordinary return values
        InvocationOnMock invocation1 = mock(InvocationOnMock.class);
        assertEquals("foo", smartNulls.answer(invocation1));

        InvocationOnMock invocation2 = mock(InvocationOnMock.class);
        assertEquals(1, smartNulls.answer(invocation2));

        InvocationOnMock invocation3 = mock(InvocationOnMock.class);
        assertEquals(true, smartNulls.answer(invocation3));

        // Test SmartNull return values
        InvocationOnMock invocation4 = mock(InvocationOnMock.class);
        assertEquals("SmartNull returned by unstubbed method on mock", smartNulls.answer(invocation4));

        InvocationOnMock invocation5 = mock(InvocationOnMock.class);
        assertEquals("SmartNull returned by unstubbed toString method on mock", smartNulls.answer(invocation5));

        // Test null return value
        InvocationOnMock invocation6 = mock(InvocationOnMock.class);
        assertNull(smartNulls.answer(invocation6));
    }
}
