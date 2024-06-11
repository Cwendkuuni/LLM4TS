import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.fail;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.TimeZone;

import org.junit.Test;

public class StdDateFormatTest {

    @Test
    public void testParseISO8601() {
        StdDateFormat dateFormat = new StdDateFormat();
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss.SSSZ");

        try {
            Date expectedDate = sdf.parse("2022-09-15T18:30:45.123+0000");
            Date actualDate = dateFormat.parse("2022-09-15T18:30:45.123Z");

            assertNotNull(actualDate);
            assertEquals(expectedDate, actualDate);
        } catch (ParseException e) {
            fail("ParseException thrown");
        }
    }

    @Test
    public void testParseRFC1123() {
        StdDateFormat dateFormat = new StdDateFormat();
        SimpleDateFormat sdf = new SimpleDateFormat("EEE, dd MMM yyyy HH:mm:ss zzz", Locale.US);

        try {
            Date expectedDate = sdf.parse("Wed, 15 Sep 2021 15:30:45 GMT");
            Date actualDate = dateFormat.parse("Wed, 15 Sep 2021 15:30:45 GMT");

            assertNotNull(actualDate);
            assertEquals(expectedDate, actualDate);
        } catch (ParseException e) {
            fail("ParseException thrown");
        }
    }

    @Test
    public void testCustomTimezone() {
        TimeZone customTimeZone = TimeZone.getTimeZone("Europe/Berlin");

        StdDateFormat dateFormat = new StdDateFormat(customTimeZone);

        assertEquals(customTimeZone, dateFormat.getTimeZone());
    }

    @Test
    public void testParseInvalidDate() {
        StdDateFormat dateFormat = new StdDateFormat();

        try {
            dateFormat.parse("invaliddate");
            fail("Expected ParseException not thrown");
        } catch (ParseException e) {
            // Expected
        }
    }
}