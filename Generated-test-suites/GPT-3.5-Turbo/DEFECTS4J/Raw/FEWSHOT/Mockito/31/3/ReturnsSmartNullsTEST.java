package org.mockito.internal.stubbing.defaultanswers;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;

import org.junit.Test;
import org.mockito.internal.creation.jmock.ClassImposterizer;
import org.mockito.internal.debugging.Location;
import org.mockito.internal.util.ObjectMethodsGuru;
import org.mockito.invocation.InvocationOnMock;
import org.mockito.stubbing.Answer;

public class ReturnsSmartNullsTest {
    private final Answer<Object> delegate = new ReturnsMoreEmptyValues();

    @Test
    public void testAnswer_NullReturnValue() throws Throwable {
        InvocationOnMock invocation = null; // Enter invocation object here
        assertNull(new ReturnsSmartNulls().answer(invocation));
    }

    @Test
    public void testAnswer_DefaultReturnValue() throws Throwable {
        InvocationOnMock invocation = null; // Enter invocation object here
        assertNotNull(new ReturnsSmartNulls().answer(invocation));
    }

    @Test
    public void testAnswer_MockitoSmartNullReturnValue() throws Throwable {
        InvocationOnMock invocation = null; // Enter invocation object here
        assertNotNull(new ReturnsSmartNulls().answer(invocation));
    }

    @Test
    public void testAnswer_FinalClassReturnValue() throws Throwable {
        InvocationOnMock invocation = null; // Enter invocation object here
        assertNull(new ReturnsSmartNulls().answer(invocation));
    }

    @Test
    public void testAnswer_ReturnValueWithToString() throws Throwable {
        InvocationOnMock invocation = null; // Enter invocation object here
        assertEquals("SmartNull returned by unstubbed method on mock",
                new ReturnsSmartNulls().answer(invocation));
    }
}