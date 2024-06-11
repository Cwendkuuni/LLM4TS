
package org.mockito.internal.stubbing.defaultanswers;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNull;

import org.junit.Before;
import org.junit.Test;
import org.mockito.invocation.InvocationOnMock;

public class ReturnsSmartNullsTest {
    private ReturnsSmartNulls returnsSmartNulls;

    @Before
    public void setUp() {
        returnsSmartNulls = new ReturnsSmartNulls();
    }

    @Test
    public void testAnswer() {
        InvocationOnMock invocation = mock(InvocationOnMock.class);
        assertNull(returnsSmartNulls.answer(invocation));
    }

    @Test
    public void testAnswerWithDefaultReturnValue() {
        InvocationOnMock invocation = mock(InvocationOnMock.class);
        when(invocation.getMethod().getReturnType()).thenReturn(Object.class);
        assertEquals(new Object(), returnsSmartNulls.answer(invocation));
    }
}
