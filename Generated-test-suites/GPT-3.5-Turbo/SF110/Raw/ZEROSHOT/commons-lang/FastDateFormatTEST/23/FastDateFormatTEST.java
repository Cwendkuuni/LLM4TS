import org.junit.Test;

import java.text.ParseException;
import java.util.Calendar;
import java.util.Date;
import java.util.TimeZone;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

public class FastDateFormatTest {

    private FastDateFormat fastDateFormat;

    @Test
    public void testGetInstance() {
        fastDateFormat = FastDateFormat.getInstance();
        assertTrue(fastDateFormat instanceof FastDateFormat);
    }

    @Test
    public void testGetInstanceWithPattern() {
        fastDateFormat = FastDateFormat.getInstance("yyyy-MM-dd");
        assertTrue(fastDateFormat instanceof FastDateFormat);
    }
    
    @Test
    public void testGetInstanceWithPatternAndTimeZone() {
        TimeZone timeZone = TimeZone.getTimeZone("GMT");
        fastDateFormat = FastDateFormat.getInstance("yyyy-MM-dd", timeZone);
        assertTrue(fastDateFormat instanceof FastDateFormat);
    }
    
    @Test
    public void testGetInstanceWithPatternAndLocale() {
        fastDateFormat = FastDateFormat.getInstance("yyyy-MM-dd", Locale.US);
        assertTrue(fastDateFormat instanceof FastDateFormat);
    }
    
    @Test
    public void testGetInstanceWithPatternTimeZoneAndLocale() {
        TimeZone timeZone = TimeZone.getTimeZone("GMT");
        fastDateFormat = FastDateFormat.getInstance("yyyy-MM-dd", timeZone, Locale.US);
        assertTrue(fastDateFormat instanceof FastDateFormat);
    }
    
    @Test
    public void testGetDateInstance() {
        fastDateFormat = FastDateFormat.getDateInstance(FastDateFormat.SHORT);
        assertTrue(fastDateFormat instanceof FastDateFormat);
    }
    
    @Test
    public void testGetDateInstanceWithLocale() {
        fastDateFormat = FastDateFormat.getDateInstance(FastDateFormat.SHORT, Locale.US);
        assertTrue(fastDateFormat instanceof FastDateFormat);
    }
    
    @Test
    public void testGetDateInstanceWithTimeZone() {
        TimeZone timeZone = TimeZone.getTimeZone("GMT");
        fastDateFormat = FastDateFormat.getDateInstance(FastDateFormat.SHORT, timeZone);
        assertTrue(fastDateFormat instanceof FastDateFormat);
    }
    
    @Test
    public void testGetDateInstanceWithTimeZoneAndLocale() {
        TimeZone timeZone = TimeZone.getTimeZone("GMT");
        fastDateFormat = FastDateFormat.getDateInstance(FastDateFormat.SHORT, timeZone, Locale.US);
        assertTrue(fastDateFormat instanceof FastDateFormat);
    }
    
    @Test
    public void testGetTimeInstance() {
        fastDateFormat = FastDateFormat.getTimeInstance(FastDateFormat.SHORT);
        assertTrue(fastDateFormat instanceof FastDateFormat);
    }
    
    @Test
    public void testGetTimeInstanceWithLocale() {
        fastDateFormat = FastDateFormat.getTimeInstance(FastDateFormat.SHORT, Locale.US);
        assertTrue(fastDateFormat instanceof FastDateFormat);
    }
    
    @Test
    public void testGetTimeInstanceWithTimeZone() {
        TimeZone timeZone = TimeZone.getTimeZone("GMT");
        fastDateFormat = FastDateFormat.getTimeInstance(FastDateFormat.SHORT, timeZone);
        assertTrue(fastDateFormat instanceof FastDateFormat);
    }
    
    @Test
    public void testGetTimeInstanceWithTimeZoneAndLocale() {
        TimeZone timeZone = TimeZone.getTimeZone("GMT");
        fastDateFormat = FastDateFormat.getTimeInstance(FastDateFormat.SHORT, timeZone, Locale.US);
        assertTrue(fastDateFormat instanceof FastDateFormat);
    }
    
    @Test
    public void testGetDateTimeInstance() {
        fastDateFormat = FastDateFormat.getDateTimeInstance(FastDateFormat.SHORT, FastDateFormat.SHORT);
        assertTrue(fastDateFormat instanceof FastDateFormat);
    }
    
    @Test
    public void testGetDateTimeInstanceWithLocale() {
        fastDateFormat = FastDateFormat.getDateTimeInstance(FastDateFormat.SHORT, FastDateFormat.SHORT, Locale.US);
        assertTrue(fastDateFormat instanceof FastDateFormat);
    }
    
    @Test
    public void testGetDateTimeInstanceWithTimeZone() {
        TimeZone timeZone = TimeZone.getTimeZone("GMT");
        fastDateFormat = FastDateFormat.getDateTimeInstance(FastDateFormat.SHORT, FastDateFormat.SHORT, timeZone);
        assertTrue(fastDateFormat instanceof FastDateFormat);
    }
    
    @Test
    public void testGetDateTimeInstanceWithTimeZoneAndLocale() {
        TimeZone timeZone = TimeZone.getTimeZone("GMT");
        fastDateFormat = FastDateFormat.getDateTimeInstance(FastDateFormat.SHORT, FastDateFormat.SHORT, timeZone, Locale.US);
        assertTrue(fastDateFormat instanceof FastDateFormat);
    }
    
    @Test
    public void testFormatObjectStringBufferFieldPosition() {
        fastDateFormat = FastDateFormat.getInstance();
        StringBuffer toAppendTo = new StringBuffer();
        FieldPosition pos = new FieldPosition(0);
        StringBuffer stringBuffer = fastDateFormat.format(new Date(), toAppendTo, pos);
        assertTrue(stringBuffer instanceof StringBuffer);
    }
    
    @Test
    public void testFormatLong() {
        fastDateFormat = FastDateFormat.getInstance();
        String formattedString = fastDateFormat.format(System.currentTimeMillis());
        assertTrue(formattedString instanceof String);
    }
    
    @Test
    public void testFormatDate() {
        fastDateFormat = FastDateFormat.getInstance();
        String formattedString = fastDateFormat.format(new Date());
        assertTrue(formattedString instanceof String);
    }
    
    @Test
    public void testFormatCalendar() {
        fastDateFormat = FastDateFormat.getInstance();
        Calendar calendar = Calendar.getInstance();
        String formattedString = fastDateFormat.format(calendar);
        assertTrue(formattedString instanceof String);
    }
    
    @Test
    public void testFormatLongStringBuffer() {
        fastDateFormat = FastDateFormat.getInstance();
        StringBuffer stringBuffer = fastDateFormat.format(System.currentTimeMillis(), new StringBuffer());
        assertTrue(stringBuffer instanceof StringBuffer);
    }
    
    @Test
    public void testFormatDateStringBuffer() {
        fastDateFormat = FastDateFormat.getInstance();
        StringBuffer stringBuffer = fastDateFormat.format(new Date(), new StringBuffer());
        assertTrue(stringBuffer instanceof StringBuffer);
    }
    
    @Test
    public void testFormatCalendarStringBuffer() {
        fastDateFormat = FastDateFormat.getInstance();
        Calendar calendar = Calendar.getInstance();
        StringBuffer stringBuffer = fastDateFormat.format(calendar, new StringBuffer());
        assertTrue(stringBuffer instanceof StringBuffer);
    }
    
    @Test
    public void testParseString() throws ParseException {
        fastDateFormat = FastDateFormat.getInstance();
        Date parsedDate = fastDateFormat.parse("2022-01-01");
        assertTrue(parsedDate instanceof Date);
    }
    
    @Test
    public void testParseStringParsePosition() {
        fastDateFormat = FastDateFormat.getInstance();
        ParsePosition pos = new ParsePosition(0);
        Date parsedDate = fastDateFormat.parse("2022-01-01", pos);
        assertTrue(parsedDate instanceof Date);
    }
    
    @Test
    public void testParseObjectStringParsePosition() {
        fastDateFormat = FastDateFormat.getInstance();
        ParsePosition pos = new ParsePosition(0);
        Object parsedObject = fastDateFormat.parseObject("2022-01-01", pos);
        assertTrue(parsedObject instanceof Object);
    }
    
    @Test
    public void testGetPattern() {
        fastDateFormat = FastDateFormat.getInstance();
        String pattern = fastDateFormat.getPattern();
        assertEquals(pattern, "yyyy-MM-dd HH:mm:ss");
    }
    
    @Test
    public void testGetTimeZone() {
        fastDateFormat = FastDateFormat.getInstance();
        TimeZone timeZone = fastDateFormat.getTimeZone();
        assertTrue(timeZone instanceof TimeZone);
    }
    
    @Test
    public void testGetLocale() {
        fastDateFormat = FastDateFormat.getInstance();
        Locale locale = fastDateFormat.getLocale();
        assertTrue(locale instanceof Locale);
    }
    
    @Test
    public void testGetMaxLengthEstimate() {
        fastDateFormat = FastDateFormat.getInstance();
        int maxLengthEstimate = fastDateFormat.getMaxLengthEstimate();
        assertTrue(maxLengthEstimate instanceof int);
    }
    
    @Test
    public void testEquals() {
        fastDateFormat = FastDateFormat.getInstance();
        FastDateFormat other = FastDateFormat.getInstance();
        boolean isEquals = fastDateFormat.equals(other);
        assertTrue(isEquals);
    }
    
    @Test
    public void testHashCode() {
        fastDateFormat = FastDateFormat.getInstance();
        int hashCode = fastDateFormat.hashCode();
        assertTrue(hashCode instanceof int);
    }
    
    @Test
    public void testToString() {
        fastDateFormat = FastDateFormat.getInstance();
        String toString = fastDateFormat.toString();
        assertTrue(toString instanceof String);
    }
    
    @Test
    public void testApplyRules() {
        fastDateFormat = FastDateFormat.getInstance();
        Calendar calendar = Calendar.getInstance();
        StringBuffer stringBuffer = fastDateFormat.applyRules(calendar, new StringBuffer());
        assertTrue(stringBuffer instanceof StringBuffer);
    }
}