import static org.junit.Assert.*;
import org.junit.Test;

public class StdDateFormatTest {

    @Test
    public void testLooksLikeISO8601() {
        StdDateFormat stdDateFormat = new StdDateFormat();
        
        assertTrue(stdDateFormat.looksLikeISO8601("2021-10-15T13:30:00.000Z"));
        assertTrue(stdDateFormat.looksLikeISO8601("2021-10-15T13:30:00Z"));
        assertFalse(stdDateFormat.looksLikeISO8601("2021/10/15 13:30:00"));
    }

    @Test
    public void testParseAsISO8601() {
        StdDateFormat stdDateFormat = new StdDateFormat();
        ParsePosition pos = new ParsePosition(0);
        Date date = stdDateFormat.parseAsISO8601("2021-10-15T13:30:00.000Z", pos);
        assertNotNull(date);
        
        pos = new ParsePosition(0);
        date = stdDateFormat.parseAsISO8601("2021-10-15T13:30:00Z", pos);
        assertNotNull(date);
    }

    @Test
    public void testParseAsRFC1123() {
        StdDateFormat stdDateFormat = new StdDateFormat();
        ParsePosition pos = new ParsePosition(0);
        Date date = stdDateFormat.parseAsRFC1123("Fri, 15 Oct 2021 13:30:00 GMT", pos);
        assertNotNull(date);
    }

    @Test
    public void testParse() {
        StdDateFormat stdDateFormat = new StdDateFormat();
        try {
            Date date = stdDateFormat.parse("2021-10-15 13:30:00");
            fail("Expected ParseException");
        } catch (ParseException e) {
            // Expected
        }
    }

    @Test
    public void testFormat() {
        StdDateFormat stdDateFormat = new StdDateFormat();
        StringBuffer buffer = new StringBuffer();
        FieldPosition position = new FieldPosition(0);
        Date date = new Date();
        StringBuffer formattedDate = stdDateFormat.format(date, buffer, position);
        assertNotNull(formattedDate);
    }

    // Add more test cases for other methods and edge cases

}