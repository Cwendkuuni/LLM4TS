
import org.joda.time.format.ISOPeriodFormat;
import org.junit.Test;
import static org.junit.Assert.assertEquals;

public class MutablePeriodTest {

    @Test
    public void testParse() {
        // Typical use case
        String input = "P1Y2M3DT4H5M6S";
        MutablePeriod expected = new MutablePeriod(1, 2, 3, 4, 5, 6, 0, 0);

        MutablePeriod result = MutablePeriod.parse(input);

        assertEquals(expected, result);
    }

    @Test
    public void testParseEmptyString() {
        // Edge case: Empty string
        String input = "";
        
        try {
            MutablePeriod result = MutablePeriod.parse(input);
            // Fail if no exception is thrown
            fail("Expected RuntimeException but did not occur");
        } catch (RuntimeException e) {
            // Pass if exception is caught
        }
    }

    @Test
    public void testParseInvalidString() {
        // Edge case: Invalid string
        String input = "InvalidInput";
        
        try {
            MutablePeriod result = MutablePeriod.parse(input);
            // Fail if no exception is thrown
            fail("Expected RuntimeException but did not occur");
        } catch (RuntimeException e) {
            // Pass if exception is caught
        }
    }

    @Test
    public void testParseNullString() {
        // Edge case: Null string
        String input = null;
        
        try {
            MutablePeriod result = MutablePeriod.parse(input);
            // Fail if no exception is thrown
            fail("Expected NullPointerException but did not occur");
        } catch (NullPointerException e) {
            // Pass if exception is caught
        }
    }
}
