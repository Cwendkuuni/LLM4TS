import org.junit.Test;
import static org.junit.Assert.*;

public class LenientDateTimeFieldTest {
    
    @Test
    public void testGetInstance() {
        DateTimeField field = new LenientDateTimeField();
        Chronology base = new Chronology();
        DateTimeField result = LenientDateTimeField.getInstance(field, base);
        assertTrue(result instanceof LenientDateTimeField);
    }
    
    @Test
    public void testIsLenient() {
        DateTimeField field = new LenientDateTimeField();
        boolean result = field.isLenient();
        assertTrue(result);
    }
    
    @Test
    public void testSet() {
        DateTimeField field = new LenientDateTimeField();
        long instant = 0;
        int value = 1;
        long result = field.set(instant, value);
        assertEquals(1, result);
    }
}