import org.apache.commons.math.stat.Frequency;
import org.junit.Test;
import static org.junit.Assert.*;

public class FrequencyTest {

    @Test
    public void testAddValue() {
        Frequency frequency = new Frequency();
        frequency.addValue(1);
        frequency.addValue(1);
        frequency.addValue(2);
        frequency.addValue(2);
        frequency.addValue('a');
        frequency.addValue('a');

        // Test basic functionality
        assertEquals(4, frequency.getCount(1));
        assertEquals(4, frequency.getCount(1L));
        assertEquals(4, frequency.getCount(1l));
        assertEquals(4, frequency.getCount(1.0));
        assertEquals(4, frequency.getCount(1.0L));
        assertEquals(4, frequency.getCount(1.0d));
        assertEquals(2, frequency.getCount(2));
        assertEquals(2, frequency.getCount(2L));
        assertEquals(2, frequency.getCount(2.0));
        assertEquals(2, frequency.getCount(2.0L));
        assertEquals(2, frequency.getCount(2.0d));
        assertEquals(2, frequency.getCount('a'));
        assertEquals(2, frequency.getCount('a', Character.CASE_INSENSITIVE_ORDER));

        // Test edge cases and exception handling
        try {
            frequency.addValue(null);
            fail("Should have thrown IllegalArgumentException");
        } catch (IllegalArgumentException e) {
            // Expected
        }

        try {
            frequency.addValue('z');
            fail("Should have thrown IllegalArgumentException");
        } catch (IllegalArgumentException e) {
            // Expected
        }

        try {
            frequency.addValue(new Object());
            fail("Should have thrown IllegalArgumentException");
        } catch (IllegalArgumentException e) {
            // Expected
        }

        try {
            frequency.addValue("string");
            fail("Should have thrown IllegalArgumentException");
        } catch (IllegalArgumentException e) {
            // Expected
        }

        try {
            frequency.addValue(new Integer(1).intValue()); // int value
            fail("Should have thrown IllegalArgumentException");
        } catch (IllegalArgumentException e) {
            // Expected
        }

        try {
            frequency.addValue(new Long("1").longValue()); // string as long
            fail("Should have thrown NumberFormatException");
        } catch (NumberFormatException e) {
            // Expected
        }
    }

    // Add more test cases for other methods in the class here...

    // 
    // Test code here...
    // 
}