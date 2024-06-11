package org.mockito.internal.stubbing.defaultanswers.test;

import org.junit.Test;
import static org.mockito.Mockito.*;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;
import static org.junit.Assert.*;

public class ReturnsSmartNullsTest {
    @Test
    public void testConstructor() {
        new ReturnsSmartNulls();
    }

    @Test
    public void testAnswerWithNonNullReturnValue() {
        ReturnsSmartNulls returnsSmartNulls = new ReturnsSmartNulls();
        when(mock.methodName()).thenAnswer(returnsSmartNulls);

        Object result = mock.methodName();
        assertNotNull(result);
    }

    @Test
    public void testAnswerWithNullReturnValue() {
        ReturnsSmartNulls returnsSmartNulls = new ReturnsSmartNulls();
        when(mock.methodName()).thenAnswer(returnsSmartNulls);

        Object result = mock.methodName();
        assertNull(result);
    }

    @Test
    public void testAnswerWithMockableReturnType() {
        Class mockClass = Mockito.mock(String.class);
        ReturnsSmartNulls returnsSmartNulls = new ReturnsSmartNulls();
        when(mock.methodName()).thenAnswer(returnsSmartNulls);

        Object result = mock.methodName();
        assertEquals("SmartNull returned by unstubbed methodName() method on mock", result);
    }

    @Test(expected = UnsupportedOperationException.class)
    public void testAnswerWithNonMockableFinalReturnType() {
        final int i = 5;
        ReturnsSmartNulls returnsSmartNulls = new ReturnsSmartNulls();
        when(mock.methodName()).thenAnswer(returnsSmartNulls);

        mock.methodName();
    }

    @Test(expected = Throwable.class)
    public void testAnswerWithThrowableException() {
        ReturnsSmartNulls returnsSmartNulls = new ReturnsSmartNulls();
        when(mock.methodName()).thenAnswer(returnsSmartNulls);

        mock.methodName();
    }
}