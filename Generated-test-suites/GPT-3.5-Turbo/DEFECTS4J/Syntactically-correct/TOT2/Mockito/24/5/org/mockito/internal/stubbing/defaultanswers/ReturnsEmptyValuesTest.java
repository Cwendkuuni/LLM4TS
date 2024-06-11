import org.junit.Test;
import static org.junit.Assert.*;
import static org.mockito.Mockito.*;

public class ReturnsEmptyValuesTest {

    @Test
    public void testAnswer() {
        ReturnsEmptyValues returnsEmptyValues = new ReturnsEmptyValues();

        // Test for toString() method
        String expectedString = "Mock for Object, hashCode: " + returnsEmptyValues.hashCode();
        assertEquals(expectedString, returnsEmptyValues.answer(mock(InvocationOnMock.class)));

        // Test for compareTo method
        assertEquals(1, returnsEmptyValues.answer(mock(InvocationOnMock.class)));
    }
}