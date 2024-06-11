import org.junit.Test;
import org.mockito.invocation.InvocationOnMock;

import static org.junit.Assert.assertEquals;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

public class ReturnsEmptyValuesTest {
    private ReturnsEmptyValues returnsEmptyValues = new ReturnsEmptyValues();

    @Test
    public void testAnswerToString() {
        InvocationOnMock invocation = mock(InvocationOnMock.class);
        when(invocation.getMethod()).thenReturn(Object.class.getDeclaredMethods()[0]); // Assuming toString() method is the first method in the class
        Object result = returnsEmptyValues.answer(invocation);
        assertEquals("Mock for Object, hashCode: 0", result);
    }

    @Test
    public void testAnswerCompareTo() {
        InvocationOnMock invocation = mock(InvocationOnMock.class);
        when(invocation.getMethod()).thenReturn(Comparable.class.getDeclaredMethods()[0]); // Assuming compareTo() method is the first method in the interface
        Object result = returnsEmptyValues.answer(invocation);
        assertEquals(1, result);
    }
}