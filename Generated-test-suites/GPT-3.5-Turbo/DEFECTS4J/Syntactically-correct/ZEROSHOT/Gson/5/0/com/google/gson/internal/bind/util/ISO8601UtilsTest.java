import org.junit.Test;
import static org.junit.Assert.*;
import java.text.ParseException;
import java.text.ParsePosition;
import java.util.*;

public class ISO8601UtilsTest {

    @Test
    public void testFormat() {
        // Test case 1: format date with default timezone and no millis precision
        Date date1 = new Date();
        String expected1 = ISO8601Utils.format(date1, false, TimeZone.getTimeZone("UTC"));
        String actual1 = ISO8601Utils.format(date1);
        assertEquals(expected1, actual1);

        // Test case 2: format date with timezone and millis precision
        Date date2 = new GregorianCalendar(2022, Calendar.JANUARY, 15, 12, 30, 45).getTime();
        String expected2 = "2022-01-15T12:30:45.000Z";
        String actual2 = ISO8601Utils.format(date2, true, TimeZone.getTimeZone("UTC"));
        assertEquals(expected2, actual2);
    }

    @Test
    public void testParse() throws ParseException {
        // Test case 1: parse date with default timezone
        String date1 = "2022-01-15T12:30:45Z";
        Date expected1 = new GregorianCalendar(2022, Calendar.JANUARY, 15, 12, 30, 45).getTime();
        ParsePosition pos1 = new ParsePosition(0);
        Date actual1 = ISO8601Utils.parse(date1, pos1);
        assertEquals(expected1, actual1);
        assertEquals(date1.length(), pos1.getIndex());

        // Test case 2: parse date with timezone and milliseconds
        String date2 = "2022-01-15T12:30:45.123+02:00";
        Date expected2 = new GregorianCalendar(2022, Calendar.JANUARY, 15, 12, 30, 45).getTime();
        expected2.setTime(expected2.getTime() - 2 * 60 * 60 * 1000 - 123);
        ParsePosition pos2 = new ParsePosition(0);
        Date actual2 = ISO8601Utils.parse(date2, pos2);
        assertEquals(expected2, actual2);
        assertEquals(date2.length(), pos2.getIndex());
    }
}