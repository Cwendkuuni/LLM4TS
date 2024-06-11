import org.junit.Test;
import org.mockito.Mockito;
import static org.mockito.Mockito.*;

public class ReturnsEmptyValuesTest {

    @Test
    public void testToStringMethod() {
        Object mock = Mockito.mock(String.class);
        Object result = new ReturnsEmptyValues().answer(Mockito.argThat(m -> m.getMethod().getName().equals("toString")));
        String expected = "Mock for " + String.class.getSimpleName() + ", hashCode: " + mock.hashCode();
        assertEquals(expected, result);
    }

    @Test
    public void testCompareToMethod() {
        Object mock1 = Mockito.mock(Comparable.class);
        Object mock2 = Mockito.mock(Comparable.class);
        when(mock1.compareTo(mock2)).thenReturn(1); // any value other than 0
        int result = new ReturnsEmptyValues().answer(Mockito.argThat(m -> m.getName().equals("compareTo"))).compareTo(mock2);
        assertEquals(1, result);
    }

    // Add test case for returnValueFor() method here
}