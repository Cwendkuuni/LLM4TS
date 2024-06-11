import static org.junit.Assert.*;
import org.junit.Test;

public class StdDateFormatTest {

    @Test
    public void testParse() {
        // Typical use case
        StdDateFormat dateFormat = new StdDateFormat();
        String dateStr = "2021-10-15T08:30:00.000Z";
        try {
            assertEquals(dateFormat.parse(dateStr).getTime(), 1634280600000L);
        } catch (Exception e) {
            fail("Exception thrown for valid date string: " + dateStr);
        }
        
        // Edge cases
        dateStr = "2021-10-15"; // Date without time
        try {
            assertEquals(dateFormat.parse(dateStr).getTime(), 1634256000000L);
        } catch (Exception e) {
            fail("Exception thrown for valid date string: " + dateStr);
        }
        
        dateStr = "2021-10-15T08:30:00.000+05:30"; // Date with timezone offset
        try {
            assertEquals(dateFormat.parse(dateStr).getTime(), 1634250600000L);
        } catch (Exception e) {
            fail("Exception thrown for valid date string: " + dateStr);
        }
        
        // Error scenario
        dateStr = "not_a_date_string";
        try {
            dateFormat.parse(dateStr);
            fail("No exception thrown for invalid date string: " + dateStr);
        } catch (ParseException e) {
            // Expected ParseException
        }
    }

    @Test
    public void testFormat() {
        // Typical use case
        StdDateFormat dateFormat = new StdDateFormat();
        Date date = new Date(1634280600000L);
        StringBuffer buffer = new StringBuffer();
        FieldPosition fieldPosition = new FieldPosition(0);
        assertEquals("2021-10-15T08:30:00.000Z", dateFormat.format(date, buffer, fieldPosition).toString());
    }

    // Add other test cases for remaining methods as needed

}