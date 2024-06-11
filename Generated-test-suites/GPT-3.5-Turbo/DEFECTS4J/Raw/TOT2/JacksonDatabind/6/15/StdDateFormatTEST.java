import static org.junit.Assert.*;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.JUnit4;

import com.fasterxml.jackson.databind.util.StdDateFormat;

@RunWith(JUnit4.class)
public class StdDateFormatTest {

    @Test
    public void testParse() {
        StdDateFormat dateFormat = new StdDateFormat();
        String dateString = "2021-10-15T08:30:00.000Z";
        try {
            assertNotNull(dateFormat.parse(dateString));
        } catch (Exception e) {
            fail("Exception thrown while parsing date: " + e.getMessage());
        }
    }

    @Test
    public void testParseWithInvalidDate() {
        StdDateFormat dateFormat = new StdDateFormat();
        String invalidDateString = "15-10-2021";
        try {
            assertNull(dateFormat.parse(invalidDateString));
        } catch (Exception e) {
            fail("Exception thrown while parsing invalid date: " + e.getMessage());
        }
    }

    @Test
    public void testFormat() {
        StdDateFormat dateFormat = new StdDateFormat();
        try {
            assertNotNull(dateFormat.format(new Date()));
        } catch (Exception e) {
            fail("Exception thrown while formatting date: " + e.getMessage());
        }
    }

    @Test
    public void testParseWithNullDate() {
        StdDateFormat dateFormat = new StdDateFormat();
        String nullDateString = null;
        try {
            assertNull(dateFormat.parse(nullDateString));
        } catch (Exception e) {
            fail("Exception thrown while parsing null date: " + e.getMessage());
        }
    }

    @Test
    public void testParseWithEmptyDate() {
        StdDateFormat dateFormat = new StdDateFormat();
        String emptyDateString = "";
        try {
            assertNull(dateFormat.parse(emptyDateString));
        } catch (Exception e) {
            fail("Exception thrown while parsing empty date: " + e.getMessage());
        }
    }
}