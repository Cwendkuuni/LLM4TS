import static org.junit.Assert.*;
import static org.mockito.Mockito.*;

import org.junit.Test;
import org.mockito.internal.stubbing.defaultanswers.ReturnsEmptyValues;
import org.mockito.invocation.InvocationOnMock;

import java.lang.reflect.Method;
import java.util.*;

public class ReturnsEmptyValuesTest {

    ReturnsEmptyValues returnsEmptyValues = new ReturnsEmptyValues();

    @Test
    public void testAnswerToStringMethod() {
        Method method = ReturnsEmptyValues.class.getDeclaredMethods()[2]; // assuming toString method is the 3rd method
        InvocationOnMock invocation = mock(InvocationOnMock.class);
        when(invocation.getMethod()).thenReturn(method);

        Object result = returnsEmptyValues.answer(invocation);
        assertNotNull(result);
        assertTrue(result instanceof String);
        assertTrue(((String) result).contains("Mock for "));
    }

    @Test
    public void testAnswerCompareToMethod() {
        Method method = ReturnsEmptyValues.class.getDeclaredMethods()[1]; // assuming compareTo method is the 2nd method
        InvocationOnMock invocation = mock(InvocationOnMock.class);
        when(invocation.getMethod()).thenReturn(method);

        Object result = returnsEmptyValues.answer(invocation);
        assertNotNull(result);
        assertEquals(1, (int) result);
    }

    @Test
    public void testAnswerPrimitiveReturnType() {
        Method method = ReturnsEmptyValues.class.getDeclaredMethods()[3]; // assuming first method with primitive return type
        InvocationOnMock invocation = mock(InvocationOnMock.class);
        when(invocation.getMethod()).thenReturn(method);

        Object result = returnsEmptyValues.answer(invocation);
        assertNotNull(result);
        assertTrue(result.getClass().isPrimitive());
    }

    // Add more test cases for other methods and edge cases as needed

}