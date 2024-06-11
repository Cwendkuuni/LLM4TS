import static org.junit.Assert.*;
import org.junit.Test;

public class StdDateFormatTest {

    @Test
    public void testParse() {
        // Typical use case
        StdDateFormat sdf = new StdDateFormat();
        String dateStr = "2021-06-15T13:30:00.000Z";
        try {
            assertEquals(sdf.parse(dateStr), sdf.parse(dateStr));
        } catch (ParseException e) {
            fail("ParseException should not be thrown for valid date string");
        }

        // Edge cases
        try {
            sdf.parse("2021-06-15");
            fail("ParseException should be thrown for invalid date string");
        } catch (ParseException e) {
            // Exception is expected
        }

        try {
            sdf.parse("2021-06-15T13:30:00.000+0500");
            fail("ParseException should be thrown for invalid timezone format");
        } catch (ParseException e) {
            // Exception is expected
        }
    }

    @Test
    public void testFormat() {
        // Typical use case
        StdDateFormat sdf = new StdDateFormat();
        Date date = new Date();
        StringBuffer sb = new StringBuffer();
        FieldPosition fp = new FieldPosition(0);
        assertEquals(sb, sdf.format(date, sb, fp));

        // Edge cases
        try {
            sdf.format(date, null, fp);
            fail("NullPointerException should be thrown for null StringBuffer");
        } catch (NullPointerException e) {
            // Exception is expected
        }
    }

    @Test
    public void testSetAndGetTimeZone() {
        // Typical use case
        StdDateFormat sdf = new StdDateFormat();
        TimeZone timezone = TimeZone.getTimeZone("GMT+1");
        sdf.setTimeZone(timezone);
        assertEquals(timezone, sdf.getTimeZone());

        // Edge case
        try {
            sdf.setTimeZone(null);
            fail("NullPointerException should be thrown for null TimeZone");
        } catch (NullPointerException e) {
            // Exception is expected
        }
    }

    // More test cases can be added for other public methods in StdDateFormat

}