import static org.junit.Assert.*;
import org.junit.Test;
import java.util.Calendar;
import java.util.TimeZone;
import java.util.Locale;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;

public class FastDateFormatTest {
    
    @Test
    public void testGetInstance() {
        FastDateFormat format = FastDateFormat.getInstance();
        assertNotNull(format);
    }
    
    @Test
    public void testGetInstanceWithPattern() {
        FastDateFormat format = FastDateFormat.getInstance("yyyy-MM-dd");
        assertNotNull(format);
    }
    
    @Test
    public void testGetInstanceWithPatternAndTimeZone() {
        TimeZone timeZone = TimeZone.getTimeZone("UTC");
        FastDateFormat format = FastDateFormat.getInstance("yyyy-MM-dd", timeZone);
        assertNotNull(format);
    }
    
    @Test
    public void testGetInstanceWithPatternAndLocale() {
        Locale locale = Locale.US;
        FastDateFormat format = FastDateFormat.getInstance("yyyy-MM-dd", locale);
        assertNotNull(format);
    }
    
    @Test
    public void testGetInstanceWithPatternTimeZoneAndLocale() {
        TimeZone timeZone = TimeZone.getTimeZone("UTC");
        Locale locale = Locale.US;
        FastDateFormat format = FastDateFormat.getInstance("yyyy-MM-dd", timeZone, locale);
        assertNotNull(format);
    }
    
    @Test
    public void testGetDateInstance() {
        FastDateFormat format = FastDateFormat.getDateInstance(FastDateFormat.MEDIUM);
        assertNotNull(format);
    }
    
    @Test
    public void testGetDateInstanceWithLocale() {
        Locale locale = Locale.US;
        FastDateFormat format = FastDateFormat.getDateInstance(FastDateFormat.MEDIUM, locale);
        assertNotNull(format);
    }
    
    @Test
    public void testGetDateInstanceWithTimeZone() {
        TimeZone timeZone = TimeZone.getTimeZone("UTC");
        FastDateFormat format = FastDateFormat.getDateInstance(FastDateFormat.MEDIUM, timeZone);
        assertNotNull(format);
    }
    
    @Test
    public void testGetDateInstanceWithTimeZoneAndLocale() {
        TimeZone timeZone = TimeZone.getTimeZone("UTC");
        Locale locale = Locale.US;
        FastDateFormat format = FastDateFormat.getDateInstance(FastDateFormat.MEDIUM, timeZone, locale);
        assertNotNull(format);
    }
    
    @Test
    public void testGetTimeInstance() {
        FastDateFormat format = FastDateFormat.getTimeInstance(FastDateFormat.MEDIUM);
        assertNotNull(format);
    }
    
    @Test
    public void testGetTimeInstanceWithLocale() {
        Locale locale = Locale.US;
        FastDateFormat format = FastDateFormat.getTimeInstance(FastDateFormat.MEDIUM, locale);
        assertNotNull(format);
    }
    
    @Test
    public void testGetTimeInstanceWithTimeZone() {
        TimeZone timeZone = TimeZone.getTimeZone("UTC");
        FastDateFormat format = FastDateFormat.getTimeInstance(FastDateFormat.MEDIUM, timeZone);
        assertNotNull(format);
    }
    
    @Test
    public void testGetTimeInstanceWithTimeZoneAndLocale() {
        TimeZone timeZone = TimeZone.getTimeZone("UTC");
        Locale locale = Locale.US;
        FastDateFormat format = FastDateFormat.getTimeInstance(FastDateFormat.MEDIUM, timeZone, locale);
        assertNotNull(format);
    }
    
    @Test
    public void testGetDateTimeInstance() {
        FastDateFormat format = FastDateFormat.getDateTimeInstance(FastDateFormat.MEDIUM, FastDateFormat.SHORT);
        assertNotNull(format);
    }
    
    @Test
    public void testGetDateTimeInstanceWithLocale() {
        Locale locale = Locale.US;
        FastDateFormat format = FastDateFormat.getDateTimeInstance(FastDateFormat.MEDIUM, FastDateFormat.SHORT, locale);
        assertNotNull(format);
    }
    
    @Test
    public void testGetDateTimeInstanceWithTimeZone() {
        TimeZone timeZone = TimeZone.getTimeZone("UTC");
        FastDateFormat format = FastDateFormat.getDateTimeInstance(FastDateFormat.MEDIUM, FastDateFormat.SHORT, timeZone);
        assertNotNull(format);
    }
    
    @Test
    public void testGetDateTimeInstanceWithTimeZoneAndLocale() {
        TimeZone timeZone = TimeZone.getTimeZone("UTC");
        Locale locale = Locale.US;
        FastDateFormat format = FastDateFormat.getDateTimeInstance(FastDateFormat.MEDIUM, FastDateFormat.SHORT, timeZone, locale);
        assertNotNull(format);
    }
    
    @Test
    public void testFormat() {
        FastDateFormat format = FastDateFormat.getInstance("yyyy-MM-dd");
        Calendar calendar = Calendar.getInstance();
        calendar.set(2022, 1, 1);
        String formattedDate = format.format(calendar.getTime());
        assertEquals("2022-02-01", formattedDate);
    }
    
    @Test
    public void testFormatWithMilliSeconds() {
        FastDateFormat format = FastDateFormat.getInstance("yyyy-MM-dd HH:mm:ss.SSS");
        Calendar calendar = Calendar.getInstance();
        calendar.set(2022, 1, 1, 10, 30, 45);
        calendar.set(Calendar.MILLISECOND, 500);
        String formattedDate = format.format(calendar.getTime());
        assertEquals("2022-02-01 10:30:45.500", formattedDate);
    }
    
    @Test
    public void testFormatWithInvalidDate() {
        FastDateFormat format = FastDateFormat.getInstance("yyyy-MM-dd");
        String formattedDate = format.format((Date)null);
        assertNull(formattedDate);
    }
    
    @Test
    public void testFormatWithInvalidPattern() {
        FastDateFormat format = FastDateFormat.getInstance("invalidPattern");
        Calendar calendar = Calendar.getInstance();
        calendar.set(2022, 1, 1);
        String formattedDate = format.format(calendar.getTime());
        assertNull(formattedDate);
    }
    
    @Test
    public void testParse() throws ParseException {
        FastDateFormat format = FastDateFormat.getInstance("yyyy-MM-dd");
        String dateString = "2022-02-01";
        Date date = format.parse(dateString);
        
        Calendar calendar = Calendar.getInstance();
        calendar.set(2022, 1, 1);
        Date expectedDate = calendar.getTime();
        
        assertEquals(expectedDate, date);
    }
    
    @Test
    public void testParseWithParsePosition() {
        FastDateFormat format = FastDateFormat.getInstance("yyyy-MM-dd");
        String dateString = "2022-02-01DD";
        ParsePosition pos = new ParsePosition(0);
        Date date = format.parse(dateString, pos);
        
        assertNull(date);
    }
    
}