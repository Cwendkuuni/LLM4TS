import static org.junit.Assert.*;
import org.junit.Test;

public class LenientDateTimeFieldTest {

    /**
     * Test for getInstance method that returns a lenient version of the given field
     */
    @Test
    public void testGetInstance() {
        // Create a DateTimeField and Chronology for testing
        DateTimeField field = new StrictDateTimeField();
        Chronology base = new BaseChronology();
        
        // Test if getInstance returns a lenient DateTimeField if the input field is strict
        DateTimeField result = LenientDateTimeField.getInstance(field, base);
        assertTrue(result instanceof LenientDateTimeField);
        
        // Test if getInstance returns the same DateTimeField if it is already lenient
        DateTimeField result2 = LenientDateTimeField.getInstance(result, base);
        assertEquals(result, result2);
    }

    /**
     * Test for isLenient method that checks if the field is lenient
     */
    @Test
    public void testIsLenient() {
        DateTimeField field = new LenientDateTimeField(new StrictDateTimeField(), new BaseChronology());
        assertTrue(field.isLenient());
    }

    /**
     * Test for set method that sets values which may be out of bounds
     */
    @Test
    public void testSet() {
        // Create a LenientDateTimeField for testing
        LenientDateTimeField field = new LenientDateTimeField(new StrictDateTimeField(), new BaseChronology());
        
        // Test setting a value that is within bounds
        long instant = 1000;
        int value = 5;
        long result = field.set(instant, value);
        assertEquals(result, instant + value);
        
        // Test setting a value that is out of bounds
        long instant2 = 2000;
        int value2 = 10;
        long result2 = field.set(instant2, value2);
        assertEquals(result2, instant2 + value2);
    }

}