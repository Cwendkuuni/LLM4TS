import org.junit.BeforeClass;
import org.junit.Test;
import static org.junit.Assert.*;

public class FastDateFormatTest {

    private static FastDateFormat fastDateFormat;
    
    @BeforeClass
    public static void setUpClass() {
        fastDateFormat = FastDateFormat.getInstance("yyyy-MM-dd HH:mm:ss", TimeZone.getTimeZone("GMT"));
    }
    
    @Test
    public void testFormat_Object_StringBuffer_FieldPosition() {
        Object obj = new Object();
        StringBuffer toAppendTo = new StringBuffer();
        FieldPosition pos = new FieldPosition(FastDateFormat.YEAR_FIELD);
        
        StringBuffer result = fastDateFormat.format(obj, toAppendTo, pos);
        
        assertEquals("", result.toString());
        assertEquals(0, pos.getBeginIndex());
        assertEquals(0, pos.getEndIndex());
    }
    
    @Test
    public void testFormat_long_String() {
        long millis = 0L;
        
        String result = fastDateFormat.format(millis);
        
        assertEquals("1970-01-01 00:00:00", result);
    }
    
    @Test
    public void testFormat_Date_String() {
        Date date = new Date(0L);
        
        String result = fastDateFormat.format(date);
        
        assertEquals("1970-01-01 00:00:00", result);
    }
    
    @Test
    public void testFormat_Calendar_String() {
        Calendar calendar = Calendar.getInstance();
        calendar.setTimeInMillis(0L);
        
        String result = fastDateFormat.format(calendar);
        
        assertEquals("1970-01-01 00:00:00", result);
    }
    
    @Test
    public void testFormat_long_StringBuffer() {
        long millis = 0L;
        StringBuffer buf = new StringBuffer();
        
        StringBuffer result = fastDateFormat.format(millis, buf);
        
        assertEquals("1970-01-01 00:00:00", result.toString());
    }
    
    @Test
    public void testFormat_Date_StringBuffer() {
        Date date = new Date(0L);
        StringBuffer buf = new StringBuffer();
        
        StringBuffer result = fastDateFormat.format(date, buf);
        
        assertEquals("1970-01-01 00:00:00", result.toString());
    }
    
    @Test
    public void testFormat_Calendar_StringBuffer() {
        Calendar calendar = Calendar.getInstance();
        calendar.setTimeInMillis(0L);
        StringBuffer buf = new StringBuffer();
        
        StringBuffer result = fastDateFormat.format(calendar, buf);
        
        assertEquals("1970-01-01 00:00:00", result.toString());
    }
    
    @Test
    public void testParse_String() throws ParseException {
        String source = "1970-01-01 00:00:00";
        
        Date result = fastDateFormat.parse(source);
        
        assertEquals(new Date(0L), result);
    }
    
    @Test
    public void testParse_String_ParsePosition() {
        String source = "1970-01-01 00:00:00";
        ParsePosition pos = new ParsePosition(0);
        
        Date result = fastDateFormat.parse(source, pos);
        
        assertEquals(new Date(0L), result);
        assertEquals(source.length(), pos.getIndex());
    }
    
    @Test
    public void testParseObject_String_ParsePosition() {
        String source = "1970-01-01 00:00:00";
        ParsePosition pos = new ParsePosition(0);
        
        Object result = fastDateFormat.parseObject(source, pos);
        
        assertEquals(new Date(0L), result);
        assertEquals(source.length(), pos.getIndex());
    }
    
    @Test
    public void testGetPattern() {
        String pattern = fastDateFormat.getPattern();
        
        assertEquals("yyyy-MM-dd HH:mm:ss", pattern);
    }
    
    @Test
    public void testGetTimeZone() {
        TimeZone timeZone = fastDateFormat.getTimeZone();
        
        assertEquals(TimeZone.getTimeZone("GMT"), timeZone);
    }
    
    @Test
    public void testGetLocale() {
        Locale locale = fastDateFormat.getLocale();
        
        assertEquals(Locale.getDefault(), locale);
    }
    
    @Test
    public void testGetMaxLengthEstimate() {
        int maxLengthEstimate = fastDateFormat.getMaxLengthEstimate();
        
        assertEquals(19, maxLengthEstimate);
    }
    
    @Test
    public void testEquals() {
        FastDateFormat sameFormat = FastDateFormat.getInstance("yyyy-MM-dd HH:mm:ss", TimeZone.getTimeZone("GMT"));
        FastDateFormat differentFormat = FastDateFormat.getInstance("yyyy-MM-dd", TimeZone.getTimeZone("GMT"));
        
        assertTrue(fastDateFormat.equals(sameFormat));
        assertFalse(fastDateFormat.equals(differentFormat));
    }
    
    @Test
    public void testHashCode() {
        FastDateFormat sameFormat = FastDateFormat.getInstance("yyyy-MM-dd HH:mm:ss", TimeZone.getTimeZone("GMT"));
        
        assertEquals(sameFormat.hashCode(), fastDateFormat.hashCode());
    }
    
    @Test
    public void testToString() {
        String toString = fastDateFormat.toString();
        
        assertEquals("FastDateFormat[yyyy-MM-dd HH:mm:ss,en_US,GMT]", toString);
    }
    
    @Test
    public void testApplyRules() {
        Calendar calendar = Calendar.getInstance();
        calendar.setTimeInMillis(0L);
        StringBuffer buf = new StringBuffer();
        
        StringBuffer result = fastDateFormat.applyRules(calendar, buf);
        
        assertEquals("1970-01-01 00:00:00", result.toString());
    }
    
}