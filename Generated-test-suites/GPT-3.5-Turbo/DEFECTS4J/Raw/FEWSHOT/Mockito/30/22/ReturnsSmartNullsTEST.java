
package org.mockito.internal.stubbing.defaultanswers;

import org.junit.Test;
import org.mockito.internal.creation.jmock.ClassImposterizer;

import java.lang.reflect.Method;

import static org.junit.Assert.*;
import static org.mockito.Mockito.*;

public class ReturnsSmartNullsTest {
    private ReturnsSmartNulls returnsSmartNulls = new ReturnsSmartNulls();

    @Test
    public void testAnswer() throws Throwable {
        // Test when the default return value is not null
        Answer<Object> delegate = mock(Answer.class);
        when(delegate.answer(any())).thenReturn("DefaultReturnValue");
        Method method = ReturnsSmartNulls.class.getDeclaredMethod("answer", InvocationOnMock.class);
        InvocationOnMock invocationOnMock = mock(InvocationOnMock.class);
        when(invocationOnMock.getMethod()).thenReturn(method);
        Object result = returnsSmartNulls.answer(invocationOnMock);
        assertNotNull(result);
        assertEquals("DefaultReturnValue", result);

        // Test when the default return value is null and the return type can be mocked
        when(delegate.answer(any())).thenReturn(null);
        method = ReturnsSmartNulls.class.getDeclaredMethod("answer", InvocationOnMock.class);
        invocationOnMock = mock(InvocationOnMock.class);
        when(invocationOnMock.getMethod()).thenReturn(method);
        result = returnsSmartNulls.answer(invocationOnMock);
        assertNotNull(result);
        assertTrue(result.getClass().getName().startsWith("org.mockito.internal.stubbing.defaultanswers."));

        // Test when the default return value is null and the return type cannot be mocked
        ClassImposterizer imposterizer = mock(ClassImposterizer.class);
        when(imposterizer.canImposterise(any(Class.class))).thenReturn(false);
        method = ReturnsSmartNulls.class.getDeclaredMethod("answer", InvocationOnMock.class);
        invocationOnMock = mock(InvocationOnMock.class);
        when(invocationOnMock.getMethod()).thenReturn(method);
        result = returnsSmartNulls.answer(invocationOnMock);
        assertNull(result);
    }
}
