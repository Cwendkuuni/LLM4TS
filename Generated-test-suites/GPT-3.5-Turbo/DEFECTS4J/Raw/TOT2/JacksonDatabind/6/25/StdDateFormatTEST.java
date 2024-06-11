import static org.junit.Assert.*;
import org.junit.Test;

public class StdDateFormatTest {

    @Test
    public void testParseISO8601() {
        StdDateFormat stdDateFormat = new StdDateFormat();
        Date date = stdDateFormat.parse("2021-09-30T20:30:45.123Z");
        assertNotNull(date);
        
        // Add more test cases for different ISO8601 formats
    }

    @Test
    public void testParseRFC1123() {
        StdDateFormat stdDateFormat = new StdDateFormat();
        Date date = stdDateFormat.parse("Thu, 30 Sep 2021 20:30:45 GMT");
        assertNotNull(date);
        
        // Add more test cases for different RFC1123 formats
    }

    @Test
    public void testParseInvalidDate() {
        StdDateFormat stdDateFormat = new StdDateFormat();
        
        try {
            Date date = stdDateFormat.parse("Invalid Date");
            fail("ParseException expected");
        } catch (ParseException e) {
            // Expected ParseException
        }
    }

    @Test
    public void testFormatISO8601() {
        StdDateFormat stdDateFormat = new StdDateFormat();
        Date date = new Date();
        String formattedDate = stdDateFormat.format(date);
        assertNotNull(formattedDate);
        
        // Add more test cases for formatting different dates
    }

    // Add more test cases for other public methods in StdDateFormat class

}