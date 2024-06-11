import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNull;

import org.junit.Test;

public class ISO8601UtilsTest {
    @Test
    public void testFormat() {
        // Test case 1
        Date date1 = new Date(0);
        String result1 = ISO8601Utils.format(date1);
        assertEquals("1970-01-01T00:00:00Z", result1);

        // Test case 2
        Date date2 = new Date(1514764800000L);
        String result2 = ISO8601Utils.format(date2, true);
        assertEquals("2018-01-01T00:00:00.000Z", result2);
    }

    @Test
    public void testParse() throws ParseException {
        // Test case 1
        String input1 = "1970-01-01T00:00:00Z";
        ParsePosition pos1 = new ParsePosition(0);
        Date result1 = ISO8601Utils.parse(input1, pos1);
        assertEquals(0, pos1.getIndex());
        assertEquals(0, result1.getTime());

        // Test case 2
        String input2 = "2018-01-01T00:00:00.000Z";
        ParsePosition pos2 = new ParsePosition(0);
        Date result2 = ISO8601Utils.parse(input2, pos2);
        assertEquals(0, pos2.getIndex());
        assertEquals(1514764800000L, result2.getTime());
    }
}