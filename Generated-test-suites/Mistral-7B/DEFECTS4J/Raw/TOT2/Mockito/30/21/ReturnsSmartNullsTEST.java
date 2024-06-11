package org.mockito.internal.stubbing.defaultanswers;

import org.junit.jupiter.api.*;
import static org.junit.jupiter.api.Assertions.*;
import org.mockito.*;
import org.mockito.ArgumentMatchers.*;
import org.mockito.Mockito.*;

public class ReturnsSmartNullsTest {

    @Test
    public void testAnswerMethod() {
        ReturnsSmartNulls returnsSmartNulls = Mockito.mock(ReturnsSmartNulls.class);
        InvocationOnMock invocation = Mockito.mock(InvocationOnMock.class);
        Object result = returnsSmartNulls.answer(invocation);
        // Regular check for the result being null
        assertNull(result);
    }

    @Test
    public void testAnswerMethod_ReturnsObject() {
        ReturnsSmartNulls returnsSmartNulls = Mockito.mock(ReturnsSmartNulls.class);
        InvocationOnMock invocation = Mockito.mock(InvocationOnMock.class);
        Object defaultReturnValue = new Object();
        when(returnsSmartNulls.delegate.answer(invocation)).thenReturn(defaultReturnValue);
        Object result = returnsSmartNulls.answer(invocation);
        // Regular check for the result being the same as the default return value
        assertEquals(defaultReturnValue, result);
    }

    @Test
    public void testAnswerMethod_TypeIsNotMockable() {
        ReturnsSmartNulls returnsSmartNulls = Mockito.mock(ReturnsSmartNulls.class);
        InvocationOnMock invocation = Mockito.mock(InvocationOnMock.class);
        Class<String> stringType = String.class;
        ClassImposterizer classImposterizer = Mockito.mock(ClassImposterizer.class);
        when(ClassImposterizer.INSTANCE).thenReturn(classImposterizer);
        when(classImposterizer.canImposterise(stringType)).thenReturn(false);
        Object result = returnsSmartNulls.answer(invocation);
        // Regular check for the result being null
        assertNull(result);
    }

    @Test
    public void testConstructor() {
        // Check the constructor is not accessible as it is package-private
        assertThrows(IllegalAccessException.class, () -> new ReturnsSmartNulls());
    }

    @Test
    public void testGetClass() {
        ReturnsSmartNulls returnsSmartNulls = Mockito.mock(ReturnsSmartNulls.class);
        assertEquals(ReturnsSmartNulls.class, returnsSmartNulls.getClass());
    }

    @Test
    public void testHashCode() {
        ReturnsSmartNulls returnsSmartNulls1 = Mockito.mock(ReturnsSmartNulls.class);
        ReturnsSmartNulls returnsSmartNulls2 = Mockito.mock(ReturnsSmartNulls.class);
        assertNotSame(returnsSmartNulls1.hashCode(), returnsSmartNulls2.hashCode());
    }

    @Test
    public void testEquals() {
        ReturnsSmartNulls returnsSmartNulls1 = Mockito.mock(ReturnsSmartNulls.class);
        ReturnsSmartNulls returnsSmartNulls2 = Mockito.mock(ReturnsSmartNulls.class);
        assertFalse(returnsSmartNulls1.equals(returnsSmartNulls2));
    }

    @Test
    public void testToString() {
        ReturnsSmartNulls returnsSmartNulls = Mockito.mock(ReturnsSmartNulls.class);
        String result = returnsSmartNulls.toString();
        // Regular check for the result being a String starting with "ReturnsSmartNulls@"
        assertTrue(result.startsWith("ReturnsSmartNulls@"));
    }

    @Test
    public void testSerialVersionUID() {
        ReturnsSmartNulls returnsSmartNulls = Mockito.mock(ReturnsSmartNulls.class);
        long result = returnsSmartNulls.serialVersionUID();
        // Regular check for the result being a long equal to the given constant
        assertEquals(7618312406617949441L, result);
    }
}