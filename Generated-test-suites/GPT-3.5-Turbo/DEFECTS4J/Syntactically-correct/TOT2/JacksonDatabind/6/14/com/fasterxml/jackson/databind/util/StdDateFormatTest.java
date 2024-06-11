import static org.junit.Assert.*;
import org.junit.Test;

public class StdDateFormatTest {
    
    @Test
    public void testSetTimeZone() {
        StdDateFormat sdf = new StdDateFormat();
        TimeZone newTimeZone = TimeZone.getTimeZone("PST");
        sdf.setTimeZone(newTimeZone);
        assertEquals(newTimeZone, sdf.getTimeZone());
    }
    
    @Test
    public void testParseISO8601() throws ParseException {
        StdDateFormat sdf = new StdDateFormat();
        Date date = sdf.parse("2022-05-15T10:30:00.000Z");
        assertNotNull(date);
    }
    
    @Test
    public void testParseRFC1123() throws ParseException {
        StdDateFormat sdf = new StdDateFormat();
        Date date = sdf.parse("Sun, 15 May 2022 10:30:00 GMT");
        assertNotNull(date);
    }
    
    @Test
    public void testFormatISO8601() {
        StdDateFormat sdf = new StdDateFormat();
        StringBuffer sb = new StringBuffer();
        FieldPosition fp = new FieldPosition(0);
        Date date = new Date();
        StringBuffer formattedDate = sdf.format(date, sb, fp);
        assertNotNull(formattedDate);
    }
    
    @Test
    public void testWithTimeZone() {
        StdDateFormat sdf = new StdDateFormat();
        TimeZone newTimeZone = TimeZone.getTimeZone("EST");
        StdDateFormat newSdf = sdf.withTimeZone(newTimeZone);
        assertEquals(newTimeZone, newSdf.getTimeZone());
    }
    
    @Test
    public void testWithLocale() {
        StdDateFormat sdf = new StdDateFormat();
        Locale newLocale = Locale.FRANCE;
        StdDateFormat newSdf = sdf.withLocale(newLocale);
        assertEquals(newLocale, newSdf.getLocale());
    }
    
}