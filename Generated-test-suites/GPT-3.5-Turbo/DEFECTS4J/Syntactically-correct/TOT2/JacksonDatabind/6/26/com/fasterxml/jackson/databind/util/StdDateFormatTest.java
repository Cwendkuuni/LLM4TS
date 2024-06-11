import org.junit.Test;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertTrue;

public class StdDateFormatTest {

    @Test
    public void testParse_ValidDate_ReturnsDateObject() {
        StdDateFormat dateFormat = new StdDateFormat();
        String dateStr = "2022-12-31T23:59:59.999Z";
        Date result = dateFormat.parse(dateStr);
        assertNotNull(result);
    }

    @Test
    public void testParse_InvalidDate_ThrowsException() {
        StdDateFormat dateFormat = new StdDateFormat();
        String dateStr = "invalid_date";
        ParseException exception = assertThrows(ParseException.class, () -> dateFormat.parse(dateStr));
        assertTrue(exception.getMessage().contains("Can not parse date"));
    }

    @Test
    public void testFormat_ValidDate_ReturnsFormattedString() {
        StdDateFormat dateFormat = new StdDateFormat();
        Date date = new Date();
        StringBuffer buffer = new StringBuffer();
        FieldPosition field = new FieldPosition(0);
        StringBuffer result = dateFormat.format(date, buffer, field);
        assertNotNull(result);
        assertTrue(result.length() > 0);
    }

    @Test
    public void testSetTimeZone_ValidTimeZone_SetsTimeZone() {
        StdDateFormat dateFormat = new StdDateFormat();
        TimeZone timeZone = TimeZone.getTimeZone("GMT+1");
        dateFormat.setTimeZone(timeZone);
        assertEquals(timeZone, dateFormat.getTimeZone());
    }

    // Add more test cases for other public methods in the StdDateFormat class

}