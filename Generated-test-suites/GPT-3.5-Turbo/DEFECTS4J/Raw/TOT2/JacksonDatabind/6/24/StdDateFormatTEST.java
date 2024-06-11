import org.junit.Test;
import static org.junit.Assert.*;
import java.text.ParseException;
import java.util.*;

public class StdDateFormatTest {

    @Test
    public void testParse_ISO8601() throws ParseException {
        StdDateFormat format = new StdDateFormat();
        Date parsedDate = format.parse("2021-08-19T15:30:00.000Z");
        assertNotNull(parsedDate);
    }

    @Test
    public void testParse_RFC1123() throws ParseException {
        StdDateFormat format = new StdDateFormat();
        Date parsedDate = format.parse("Thu, 19 Aug 2021 15:30:00 GMT");
        assertNotNull(parsedDate);
    }

    @Test
    public void testParse_InvalidFormat() {
        StdDateFormat format = new StdDateFormat();
        try {
            Date parsedDate = format.parse("Invalid Date");
            fail("ParseException should have been thrown");
        } catch (ParseException e) {
            assertTrue(true);
        }
    }

    @Test
    public void testFormat_ISO8601() {
        StdDateFormat format = new StdDateFormat();
        Date date = new Date();
        StringBuffer sb = new StringBuffer();
        String formattedDate = format.format(date, sb, new FieldPosition(0)).toString();
        assertNotNull(formattedDate);
    }

    @Test
    public void testSetTimeZone() {
        StdDateFormat format = new StdDateFormat();
        TimeZone newTimeZone = TimeZone.getTimeZone("PST");
        format.setTimeZone(newTimeZone);
        assertEquals(newTimeZone, format.getTimeZone());
    }
}