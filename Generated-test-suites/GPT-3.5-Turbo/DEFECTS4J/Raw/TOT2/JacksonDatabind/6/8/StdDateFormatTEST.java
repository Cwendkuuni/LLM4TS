import static org.junit.Assert.*;
import org.junit.Test;

public class StdDateFormatTest {

    @Test
    public void testParse() {
        StdDateFormat sdf = new StdDateFormat();
        try {
            sdf.parse("2021-01-01T00:00:00.000Z");
            sdf.parse("Tue, 01 Jan 2021 00:00:00 GMT");
            sdf.parse("2021-01-01");
        } catch (ParseException e) {
            fail("ParseException should not be thrown");
        }
    }

    @Test
    public void testLooksLikeISO8601() {
        StdDateFormat sdf = new StdDateFormat();
        assertTrue(sdf.looksLikeISO8601("2021-01-01T00:00:00.000Z"));
        assertFalse(sdf.looksLikeISO8601("January 01, 2021"));
    }

    @Test
    public void testParseAsISO8601() {
        StdDateFormat sdf = new StdDateFormat();
        ParsePosition pos = new ParsePosition(0);
        assertNotNull(sdf.parseAsISO8601("2021-01-01T00:00:00.000Z", pos));
    }

    @Test
    public void testParseAsRFC1123() {
        StdDateFormat sdf = new StdDateFormat();
        ParsePosition pos = new ParsePosition(0);
        assertNotNull(sdf.parseAsRFC1123("Tue, 01 Jan 2021 00:00:00 GMT", pos));
    }

    @Test
    public void testFormat() {
        StdDateFormat sdf = new StdDateFormat();
        StringBuffer buffer = new StringBuffer();
        FieldPosition fp = new FieldPosition(0);
        assertEquals("2021-01-01T00:00:00.000Z", sdf.format(new Date(), buffer, fp).toString());
    }

    @Test
    public void testSetTimeZone() {
        StdDateFormat sdf = new StdDateFormat();
        sdf.setTimeZone(TimeZone.getTimeZone("PST"));
        assertEquals("PST", sdf.getTimeZone().getID());
    }

    @Test
    public void testWithTimeZone() {
        StdDateFormat sdf = new StdDateFormat();
        assertEquals("GMT", sdf.withTimeZone(TimeZone.getTimeZone("GMT")).getTimeZone().getID());
    }

    @Test
    public void testWithLocale() {
        StdDateFormat sdf = new StdDateFormat();
        assertEquals("fr_FR", sdf.withLocale(Locale.FRANCE).getLocale().toString());
    }

    @Test
    public void testClone() {
        StdDateFormat sdf = new StdDateFormat();
        StdDateFormat cloneSdf = sdf.clone();
        assertNotSame(sdf, cloneSdf);
    }

    @Test
    public void testGetDefaultTimeZone() {
        assertEquals("GMT", StdDateFormat.getDefaultTimeZone().getID());
    }

    @Test
    public void testGetISO8601Format() {
        assertEquals("GMT", StdDateFormat.getISO8601Format(TimeZone.getTimeZone("GMT"), Locale.US).getTimeZone().getID());
    }

    @Test
    public void testGetRFC1123Format() {
        assertEquals("GMT", StdDateFormat.getRFC1123Format(TimeZone.getTimeZone("GMT"), Locale.US).getTimeZone().getID());
    }

}