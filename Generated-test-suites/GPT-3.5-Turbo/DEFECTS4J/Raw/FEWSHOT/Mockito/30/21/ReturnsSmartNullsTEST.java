
package org.mockito.internal.stubbing.defaultanswers;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNull;
import static org.mockito.Mockito.mock;

import org.junit.Test;
import org.mockito.internal.creation.jmock.ClassImposterizer;
import org.mockito.internal.util.ObjectMethodsGuru;
import org.mockito.stubbing.Answer;

public class ReturnsSmartNullsTest {

    @Test
    public void testAnswer() {
        ReturnsSmartNulls smartNulls = new ReturnsSmartNulls();
        Object returnValue = smartNulls.answer(null);
        assertNull(returnValue);
    }

    @Test
    public void testAnswerWithMockableReturnType() {
        ReturnsSmartNulls smartNulls = new ReturnsSmartNulls();
        InvocationOnMock invocation = mock(InvocationOnMock.class);
        Object returnValue = smartNulls.answer(invocation);
        assertNull(returnValue);
    }

    @Test
    public void testAnswerWithNonMockableReturnType() {
        ReturnsSmartNulls smartNulls = new ReturnsSmartNulls();
        InvocationOnMock invocation = mock(InvocationOnMock.class);
        invocation.getMethod().setReturnType(Void.TYPE);
        Object returnValue = smartNulls.answer(invocation);
        assertNull(returnValue);
    }

    @Test
    public void testAnswerWithToStringMethod() {
        ReturnsSmartNulls smartNulls = new ReturnsSmartNulls();
        InvocationOnMock invocation = mock(InvocationOnMock.class);
        invocation.getMethod().setName("toString");
        Object returnValue = smartNulls.answer(invocation);
        assertEquals("SmartNull returned by unstubbed toString method on mock", returnValue);
    }
}
