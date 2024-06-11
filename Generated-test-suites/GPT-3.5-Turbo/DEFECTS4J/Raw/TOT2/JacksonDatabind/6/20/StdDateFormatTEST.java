import static org.junit.Assert.*;
import org.junit.Test;

public class StdDateFormatTest {

    @Test
    public void testParse_ISO8601Date() {
        StdDateFormat dateFormat = new StdDateFormat();
        String isoDate = "2021-10-15T10:30:00.123Z";
        try {
            Date parsedDate = dateFormat.parse(isoDate);
            
            Calendar cal = Calendar.getInstance();
            cal.setTime(parsedDate);
            
            assertEquals(2021, cal.get(Calendar.YEAR));
            assertEquals(Calendar.OCTOBER, cal.get(Calendar.MONTH));
            assertEquals(15, cal.get(Calendar.DAY_OF_MONTH));
            assertEquals(10, cal.get(Calendar.HOUR_OF_DAY));
            assertEquals(30, cal.get(Calendar.MINUTE));
            assertEquals(0, cal.get(Calendar.SECOND));
            assertEquals(123, cal.get(Calendar.MILLISECOND));
        } catch (ParseException e) {
            fail("Should not throw ParseException for valid ISO8601 date format");
        }
    }

    @Test
    public void testParse_RFC1123Date() {
        StdDateFormat dateFormat = new StdDateFormat();
        String rfc1123Date = "Thu, 15 Oct 2021 10:30:00 GMT";
        try {
            Date parsedDate = dateFormat.parse(rfc1123Date);
            
            Calendar cal = Calendar.getInstance();
            cal.setTime(parsedDate);
            
            assertEquals(2021, cal.get(Calendar.YEAR));
            assertEquals(Calendar.OCTOBER, cal.get(Calendar.MONTH));
            assertEquals(15, cal.get(Calendar.DAY_OF_MONTH));
            assertEquals(10, cal.get(Calendar.HOUR_OF_DAY));
            assertEquals(30, cal.get(Calendar.MINUTE));
            assertEquals(0, cal.get(Calendar.SECOND));
            assertEquals("GMT", cal.getTimeZone().getID());
        } catch (ParseException e) {
            fail("Should not throw ParseException for valid RFC1123 date format");
        }
    }

    // Add more test cases for edge cases and exception scenarios
    
}