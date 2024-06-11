import static org.junit.Assert.*;
import java.text.DateFormat;
import java.text.ParseException;
import java.util.Date;
import java.util.Locale;
import java.util.TimeZone;

import org.junit.Test;

import com.fasterxml.jackson.databind.util.StdDateFormat;

public class StdDateFormatTest {

    private static final StdDateFormat STD_DATE_FORMAT = StdDateFormat.instance;

    @Test
    public void testDefaultConstructor() {
        StdDateFormat stdDateFormat = new StdDateFormat();
        assertEquals("DateFormat StdDateFormat (default)", stdDateFormat.toString());
    }

    @Test
    public void testWithTimeZoneConstructor() {
        StdDateFormat stdDateFormat = new StdDateFormat(TimeZone.getDefault(), Locale.getDefault());
        assertEquals("DateFormat StdDateFormat (timezone: " + TimeZone.getDefault() + ", locale: " + Locale.getDefault() + ")", stdDateFormat.toString());
    }

    @Test(expected = IllegalArgumentException.class)
    public void testWithNullTimeZoneConstructor() {
        new StdDateFormat(null, Locale.getDefault());
    }

    @Test(expected = IllegalArgumentException.class)
    public void testWithNullLocaleConstructor() {
        new StdDateFormat(TimeZone.getDefault(), null);
    }

    @Test
    public void testWithTimeZoneAndLocaleConstructor() {
        StdDateFormat stdDateFormat = new StdDateFormat(TimeZone.getTimeZone("UTC"), Locale.SIMPLIFIED_CHINESE);
        assertEquals("DateFormat StdDateFormat (timezone: UTC, locale: zh_CN)", stdDateFormat.toString());
    }

    @Test
    public void testGetDefaultTimeZone() {
        assertEquals("GMT", StdDateFormat.getDefaultTimeZone().getDisplayName());
    }

    @Test
    public void testGetBlueprintISO8601Format() {
        DateFormat blueprintISO8601Format = StdDateFormat.getBlueprintISO8601Format();
        assertEquals("DateFormat (timezone: GMT, locale: US)", blueprintISO8601Format.toString());
    }

    @Test(expected = UnsupportedOperationException.class)
    public void testGetBlueprintISO8601FormatInstance() {
        StdDateFormat.getBlueprintISO8601Format();
        fail("getBlueprintISO8601Format should not return an instance");
    }

    @Test
    public void testGetISO8601Format() {
        DateFormat iso8601Format = StdDateFormat.getISO8601Format(TimeZone.getTimeZone("UTC"), Locale.getDefault());
        assertEquals("DateFormat (timezone: UTC, locale: US)", iso8601Format.toString());
    }

    @Test
    public void testGetISO8601FormatWithCustomLocale() {
        DateFormat iso8601Format = StdDateFormat.getISO8601Format(TimeZone.getTimeZone("UTC"), Locale.SIMPLIFIED_CHINESE);
        assertEquals("DateFormat (timezone: UTC, locale: zh_CN)", iso8601Format.toString());
    }

    @Test
    public void testParseWithISO8601Format() throws ParseException {
        Date date = STD_DATE_FORMAT.parse("2022-12-31T23:59:59.999Z");
        assertNotNull(date);
        assertEquals(2022, date.getYear());
        assertEquals(11, date.getMonth());
        assertEquals(30, date.getDate());
        assertEquals(23, date.getHours());
        assertEquals(59, date.getMinutes());
        assertEquals(59, date.getSeconds());
        assertEquals(999, date.getMilliseconds());
        assertEquals(0, date.getTimezoneOffset());
    }

    @Test(expected = ParseException.class)
    public void testParseInvalidDateWithISO8601Format() throws ParseException {
        STD_DATE_FORMAT.parse("2022-12-31T23:59:59.999A");
        fail("parse should throw ParseException");
    }

    @Test
    public void testFormatWithISO8601Format() {
        Date date = new Date(1670454399000L);
        String formattedDate = STD_DATE_FORMAT.format(date);
        assertEquals("2023-03-15T10:39:59.000Z", formattedDate);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testFormatWithNullDate() {
        String formattedDate = STD_DATE_FORMAT.format(null);
        fail("format should throw IllegalArgumentException");
    }

    @Test
    public void testGetRFC1123Format() {
        DateFormat rfc1123Format = StdDateFormat.getRFC1123Format(TimeZone.getTimeZone("UTC"), Locale.getDefault());
        assertEquals("DateFormat (timezone: UTC, locale: US)", rfc1123Format.toString());
    }

    @Test
    public void testGetRFC1123FormatWithCustomLocale() {
        DateFormat rfc1123Format = StdDateFormat.getRFC1123Format(TimeZone.getTimeZone("UTC"), Locale.SIMPLIFIED_CHINESE);
        assertEquals("DateFormat (timezone: UTC, locale: zh_CN)", rfc1123Format.toString());
    }

    @Test
    public void testParseWithRFC1123Format() throws ParseException {
        Date date = STD_DATE_FORMAT.parse("Wed, 31 Dec 2022 23:59:59 GMT");
        assertNotNull(date);
        assertEquals(2022, date.getYear());
        assertEquals(11, date.getMonth());
        assertEquals(30, date.getDate());
        assertEquals(23, date.getHours());
        assertEquals(59, date.getMinutes());
        assertEquals(59, date.getSeconds());
        assertEquals(0, date.getTimezoneOffset());
    }

    @Test
    public void testParseWithRFC1123FormatAndCustomLocale() throws ParseException {
        Date date = STD_DATE_FORMAT.parse("Wed, 31 Dec 2022 23:59:59 GMT", Locale.SIMPLIFIED_CHINESE);
        assertNotNull(date);
        assertEquals(2022, date.getYear());
        assertEquals(11, date.getMonth());
        assertEquals(30, date.getDate());
        assertEquals(23, date.getHours());
        assertEquals(59, date.getMinutes());
        assertEquals(59, date.getSeconds());
        assertEquals(0, date.getTimezoneOffset());
    }

    @Test(expected = ParseException.class)
    public void testParseInvalidDateWithRFC1123Format() throws ParseException {
        STD_DATE_FORMAT.parse("Wed, 31 Dec 2022 23:59:59 A");
        fail("parse should throw ParseException");
    }

    @Test
    public void testFormatWithRFC1123Format() {
        Date date = new Date(1670454399000L);
        String formattedDate = STD_DATE_FORMAT.format(date, new StringBuffer(), new FieldPosition(0));
        assertEquals("Wed, 15 Mar 2023 10:39:59 GMT", formattedDate.toString());
    }

    @Test(expected = IllegalArgumentException.class)
    public void testFormatWithNullDateAndStringBuffer() {
        StringBuffer stringBuffer = new StringBuffer();
        String formattedDate = STD_DATE_FORMAT.format(null, stringBuffer, new FieldPosition(0));
        fail("format should throw IllegalArgumentException");
    }

    @Test(expected = IllegalArgumentException.class)
    public void testFormatWithNullStringBuffer() {
        String formattedDate = STD_DATE_FORMAT.format(new Date(), null, new FieldPosition(0));
        fail("format should throw IllegalArgumentException");
    }

    @Test(expected = IllegalArgumentException.class)
    public void testFormatWithNullFieldPosition() {
        String formattedDate = STD_DATE_FORMAT.format(new Date(), new StringBuffer(), null);
        fail("format should throw IllegalArgumentException");
    }
}