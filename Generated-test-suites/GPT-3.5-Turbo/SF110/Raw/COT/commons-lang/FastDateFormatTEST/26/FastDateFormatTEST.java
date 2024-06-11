import static org.junit.Assert.*;
import org.junit.Test;
import java.util.*;
import java.text.*;

public class FastDateFormatTest {
    
    @Test
    public void testGetInstance() {
        FastDateFormat instance = FastDateFormat.getInstance();
        assertNotNull(instance);
    }
    
    @Test
    public void testGetInstanceWithString() {
        FastDateFormat instance = FastDateFormat.getInstance("yyyy-MM-dd");
        assertNotNull(instance);
    }
    
    @Test
    public void testGetInstanceWithStringAndTimeZone() {
        FastDateFormat instance = FastDateFormat.getInstance("yyyy-MM-dd", TimeZone.getTimeZone("GMT"));
        assertNotNull(instance);
    }
    
    @Test
    public void testGetInstanceWithStringAndLocale() {
        FastDateFormat instance = FastDateFormat.getInstance("yyyy-MM-dd", Locale.US);
        assertNotNull(instance);
    }
    
    @Test
    public void testGetInstanceWithStringTimeZoneAndLocale() {
        FastDateFormat instance = FastDateFormat.getInstance("yyyy-MM-dd", TimeZone.getTimeZone("GMT"), Locale.US);
        assertNotNull(instance);
    }
    
    @Test
    public void testGetDateInstance() {
        FastDateFormat instance = FastDateFormat.getDateInstance(FastDateFormat.MEDIUM);
        assertNotNull(instance);
    }
    
    @Test
    public void testGetDateInstanceWithLocale() {
        FastDateFormat instance = FastDateFormat.getDateInstance(FastDateFormat.MEDIUM, Locale.US);
        assertNotNull(instance);
    }
    
    @Test
    public void testGetDateInstanceWithTimeZone() {
        FastDateFormat instance = FastDateFormat.getDateInstance(FastDateFormat.MEDIUM, TimeZone.getTimeZone("GMT"));
        assertNotNull(instance);
    }
    
    @Test
    public void testGetDateInstanceWithTimeZoneAndLocale() {
        FastDateFormat instance = FastDateFormat.getDateInstance(FastDateFormat.MEDIUM, TimeZone.getTimeZone("GMT"), Locale.US);
        assertNotNull(instance);
    }
    
    @Test
    public void testGetTimeInstance() {
        FastDateFormat instance = FastDateFormat.getTimeInstance(FastDateFormat.MEDIUM);
        assertNotNull(instance);
    }
    
    @Test
    public void testGetTimeInstanceWithLocale() {
        FastDateFormat instance = FastDateFormat.getTimeInstance(FastDateFormat.MEDIUM, Locale.US);
        assertNotNull(instance);
    }
    
    @Test
    public void testGetTimeInstanceWithTimeZone() {
        FastDateFormat instance = FastDateFormat.getTimeInstance(FastDateFormat.MEDIUM, TimeZone.getTimeZone("GMT"));
        assertNotNull(instance);
    }
    
    @Test
    public void testGetTimeInstanceWithTimeZoneAndLocale() {
        FastDateFormat instance = FastDateFormat.getTimeInstance(FastDateFormat.MEDIUM, TimeZone.getTimeZone("GMT"), Locale.US);
        assertNotNull(instance);
    }
    
    @Test
    public void testGetDateTimeInstance() {
        FastDateFormat instance = FastDateFormat.getDateTimeInstance(FastDateFormat.MEDIUM, FastDateFormat.MEDIUM);
        assertNotNull(instance);
    }
    
    @Test
    public void testGetDateTimeInstanceWithLocale() {
        FastDateFormat instance = FastDateFormat.getDateTimeInstance(FastDateFormat.MEDIUM, FastDateFormat.MEDIUM, Locale.US);
        assertNotNull(instance);
    }
    
    @Test
    public void testGetDateTimeInstanceWithTimeZone() {
        FastDateFormat instance = FastDateFormat.getDateTimeInstance(FastDateFormat.MEDIUM, FastDateFormat.MEDIUM, TimeZone.getTimeZone("GMT"));
        assertNotNull(instance);
    }
    
    @Test
    public void testGetDateTimeInstanceWithTimeZoneAndLocale() {
        FastDateFormat instance = FastDateFormat.getDateTimeInstance(FastDateFormat.MEDIUM, FastDateFormat.MEDIUM, TimeZone.getTimeZone("GMT"), Locale.US);
        assertNotNull(instance);
    }
    
    @Test
    public void testFormatObject() {
        String pattern = "yyyy-MM-dd";
        FastDateFormat instance = FastDateFormat.getInstance(pattern);
        StringBuffer buffer = new StringBuffer();
        FieldPosition position = new FieldPosition(0);
        assertNotNull(instance.format(new Object(), buffer, position));
    }
    
    @Test
    public void testFormatLong() {
        String pattern = "yyyy-MM-dd";
        FastDateFormat instance = FastDateFormat.getInstance(pattern);
        long millis = 0L;
        assertNotNull(instance.format(millis));
    }
    
    @Test
    public void testFormatDate() {
        String pattern = "yyyy-MM-dd";
        FastDateFormat instance = FastDateFormat.getInstance(pattern);
        Date date = new Date();
        assertNotNull(instance.format(date));
    }
    
    @Test
    public void testFormatCalendar() {
        String pattern = "yyyy-MM-dd";
        FastDateFormat instance = FastDateFormat.getInstance(pattern);
        Calendar calendar = Calendar.getInstance();
        assertNotNull(instance.format(calendar));
    }
    
    @Test
    public void testFormatLongBuffer() {
        String pattern = "yyyy-MM-dd";
        FastDateFormat instance = FastDateFormat.getInstance(pattern);
        long millis = 0L;
        StringBuffer buffer = new StringBuffer();
        assertNotNull(instance.format(millis, buffer));
    }
    
    @Test
    public void testFormatDateBuffer() {
        String pattern = "yyyy-MM-dd";
        FastDateFormat instance = FastDateFormat.getInstance(pattern);
        Date date = new Date();
        StringBuffer buffer = new StringBuffer();
        assertNotNull(instance.format(date, buffer));
    }
    
    @Test
    public void testFormatCalendarBuffer() {
        String pattern = "yyyy-MM-dd";
        FastDateFormat instance = FastDateFormat.getInstance(pattern);
        Calendar calendar = Calendar.getInstance();
        StringBuffer buffer = new StringBuffer();
        assertNotNull(instance.format(calendar, buffer));
    }
    
    @Test
    public void testParse() throws ParseException {
        String pattern = "yyyy-MM-dd";
        FastDateFormat instance = FastDateFormat.getInstance(pattern);
        String source = "2020-01-01";
        assertNotNull(instance.parse(source));
    }
    
    @Test
    public void testParseWithParsePosition() {
        String pattern = "yyyy-MM-dd";
        FastDateFormat instance = FastDateFormat.getInstance(pattern);
        String source = "2020-01-01";
        ParsePosition position = new ParsePosition(0);
        assertNotNull(instance.parse(source, position));
    }
    
    @Test
    public void testParseObjectWithParsePosition() {
        String pattern = "yyyy-MM-dd";
        FastDateFormat instance = FastDateFormat.getInstance(pattern);
        String source = "2020-01-01";
        ParsePosition position = new ParsePosition(0);
        assertNotNull(instance.parseObject(source, position));
    }
    
    @Test
    public void testGetPattern() {
        String pattern = "yyyy-MM-dd";
        FastDateFormat instance = FastDateFormat.getInstance(pattern);
        assertNotNull(instance.getPattern());
    }
    
    @Test
    public void testGetTimeZone() {
        String pattern = "yyyy-MM-dd";
        FastDateFormat instance = FastDateFormat.getInstance(pattern);
        assertNotNull(instance.getTimeZone());
    }
    
    @Test
    public void testGetLocale() {
        String pattern = "yyyy-MM-dd";
        FastDateFormat instance = FastDateFormat.getInstance(pattern);
        assertNotNull(instance.getLocale());
    }
    
    @Test
    public void testGetMaxLengthEstimate() {
        String pattern = "yyyy-MM-dd";
        FastDateFormat instance = FastDateFormat.getInstance(pattern);
        assertTrue(instance.getMaxLengthEstimate() >= 0);
    }
    
    @Test
    public void testEquals() {
        String pattern = "yyyy-MM-dd";
        FastDateFormat instance1 = FastDateFormat.getInstance(pattern);
        FastDateFormat instance2 = FastDateFormat.getInstance(pattern);
        assertTrue(instance1.equals(instance2));
    }
    
    @Test
    public void testHashCode() {
        String pattern = "yyyy-MM-dd";
        FastDateFormat instance = FastDateFormat.getInstance(pattern);
        assertNotNull(instance.hashCode());
    }
    
    @Test
    public void testToString() {
        String pattern = "yyyy-MM-dd";
        FastDateFormat instance = FastDateFormat.getInstance(pattern);
        assertNotNull(instance.toString());
    }
}