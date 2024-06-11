import static org.junit.Assert.*;

import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Locale;
import java.util.TimeZone;

import org.junit.Test;

import com.fasterxml.jackson.databind.util.StdDateFormat;

public class StdDateFormatTest {

    private static final StdDateFormat STD_DATE_FORMAT = StdDateFormat.instance;

    @Test
    public void testDefaultFormat() throws ParseException {
        Date date = STD_DATE_FORMAT.parse("2022-03-30T14:25:36.789Z");
        assertEquals(Calendar.getInstance().getTime(), date);
    }

    @Test(expected = ParseException.class)
    public void testInvalidFormat() {
        STD_DATE_FORMAT.parse("InvalidFormat");
    }

    @Test
    public void testCustomTimeZone() {
        StdDateFormat customTimeZoneFormat = new StdDateFormat(TimeZone.getTimeZone("America/Los_Angeles"), Locale.US);
        Date date = customTimeZoneFormat.parse("2022-03-30T14:25:36.789Z");
        assertEquals(Calendar.getInstance(TimeZone.getTimeZone("America/Los_Angeles")).getTime(), date);
    }

    @Test
    public void testCustomLocale() {
        StdDateFormat customLocaleFormat = new StdDateFormat(TimeZone.getDefault(), Locale.FRENCH);
        Date date = customLocaleFormat.parse("2022-03-30T14:25:36.789Z");
        String expectedDateFormat = "mer. 30 mars 2022 14:25:36.789";
        SimpleDateFormat formatter = new SimpleDateFormat("EEE, dd MMM yyyy HH:mm:ss.SSS", Locale.FRENCH);
        assertEquals(formatter.format(date), expectedDateFormat);
    }

    @Test
    public void testCustomFormat() throws ParseException {
        StdDateFormat customFormat = new StdDateFormat(TimeZone.getDefault(), Locale.US);
        customFormat.setDateFormatString("yyyy-MM-dd HH:mm:ss.SSSZ");
        Date date = customFormat.parse("2022-03-30 14:25:36.789Z");
        assertEquals(Calendar.getInstance().getTime(), date);
    }

    @Test(expected = ParseException.class)
    public void testInvalidCustomFormat() {
        StdDateFormat customFormat = new StdDateFormat(TimeZone.getDefault(), Locale.US);
        customFormat.setDateFormatString("yyyy-MM-dd HH:mm:ss.SSSXXX");
        customFormat.parse("2022-03-30T14:25:36.789Z");
    }

    @Test
    public void testParseISO8601() throws ParseException {
        Date date = STD_DATE_FORMAT.parse("2022-03-30T14:25:36.789Z");
        assertEquals(Calendar.getInstance().getTime(), date);
    }

    @Test
    public void testParseISO8601WithMilliseconds() throws ParseException {
        Date date = STD_DATE_FORMAT.parse("2022-03-30T14:25:36.789Z");
        assertEquals(Calendar.getInstance().getTime(), date);
    }

    @Test
    public void testParseISO8601WithoutTimezone() throws ParseException {
        Date date = STD_DATE_FORMAT.parse("2022-03-30T14:25:36.789");
        assertEquals(Calendar.getInstance().getTime(), date);
    }

    @Test
    public void testParseISO8601WithTimezone() throws ParseException {
        StdDateFormat customTimeZoneFormat = new StdDateFormat(TimeZone.getTimeZone("America/Los_Angeles"), Locale.US);
        Date date = customTimeZoneFormat.parse("2022-03-30T14:25:36.789-07:00");
        assertEquals(Calendar.getInstance(TimeZone.getTimeZone("America/Los_Angeles")).getTime(), date);
    }

    @Test
    public void testParseISO8601WithTimezoneAndLocale() throws ParseException {
        StdDateFormat customTimeZoneAndLocaleFormat = new StdDateFormat(TimeZone.getTimeZone("America/Los_Angeles"), Locale.FRENCH);
        Date date = customTimeZoneAndLocaleFormat.parse("2022-03-30T14:25:36.789-07:00");
        String expectedDateFormat = "mer. 30 mars 2022 07:25:36.789";
        SimpleDateFormat formatter = new SimpleDateFormat("EEE, dd MMM yyyy HH:mm:ss.SSS", Locale.FRENCH);
        assertEquals(formatter.format(date), expectedDateFormat);
    }

    @Test(expected = ParseException.class)
    public void testParseInvalidISO8601() throws ParseException {
        STD_DATE_FORMAT.parse("InvalidFormat");
    }

    @Test
    public void testParseRFC1123() throws ParseException {
        Date date = STD_DATE_FORMAT.parse("Wed, 30 Mar 2022 14:25:36 GMT");
        assertEquals(Calendar.getInstance().getTime(), date);
    }

    @Test
    public void testParseRFC1123WithTimezone() throws ParseException {
        StdDateFormat customTimeZoneFormat = new StdDateFormat(TimeZone.getTimeZone("America/Los_Angeles"), Locale.US);
        Date date = customTimeZoneFormat.parse("Wed, 30 Mar 2022 14:25:36 GMT");
        assertEquals(Calendar.getInstance(TimeZone.getTimeZone("America/Los_Angeles")).getTime(), date);
    }

    @Test(expected = ParseException.class)
    public void testParseInvalidRFC1123() throws ParseException {
        STD_DATE_FORMAT.parse("InvalidFormat");
    }

}