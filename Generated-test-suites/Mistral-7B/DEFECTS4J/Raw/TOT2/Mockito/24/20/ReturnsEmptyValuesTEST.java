import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verify;

public class ReturnsEmptyValuesTest {

    @Test
    public void testAnswerToStringMethod() {
        Object mock = mock(String.class);
        ReturnsEmptyValues returnsEmptyValues = Mockito.mock(ReturnsEmptyValues.class);
        String expected = "Mock for String, hashCode: " + mock.hashCode();
        verify(returnsEmptyValues).answer(Mockito.argThat(inv -> inv.getMethod().getName().equals("toString"))); // use argThat to match method name
        Mockito.verify(returnsEmptyValues).answer(Mockito.any()); // test for other methods

        Mockito.verify(mock).toString();
        Mockito.verify(mock).hashCode();

        String result = (String) returnsEmptyValues.answer(Mockito.argThat(inv -> inv.getMethod().getName().equals("toString")));
        assertEquals(expected, result);
    }

    // Add another test case for the compareToMethod
    @Test
    public void testAnswerCompareToMethod() {
        Object mock = mock(Comparable.class);
        ReturnsEmptyValues returnsEmptyValues = Mockito.mock(ReturnsEmptyValues.class);

        Mockito.verify(mock).compareTo(Mockito.any());

        // Test with the same object
        int result = (int) returnsEmptyValues.answer(Mockito.argThat(inv -> inv.getMethod().getName().equals("compareTo")));
        assertEquals(0, result);

        // Test with different objects
        Mockito.when(mock.compareTo(Mockito.any())).thenReturn(1);
        result = (int) returnsEmptyValues.answer(Mockito.argThat(inv -> inv.getMethod().getName().equals("compareTo")));
        assertNotEquals(0, result);
    }
}