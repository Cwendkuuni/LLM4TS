import org.junit.Test;

import static org.junit.Assert.*;

public class FastDateFormatTest {

    @Test
    public void testGetInstance() {
        FastDateFormat instance = FastDateFormat.getInstance();
        assertNotNull(instance);
    }
    
    @Test
    public void testGetInstanceWithPattern() {
        String pattern = "yyyy-MM-dd HH:mm:ss";
        FastDateFormat instance = FastDateFormat.getInstance(pattern);
        assertNotNull(instance);
        assertEquals(pattern, instance.getPattern());
    }
    
    @Test
    public void testGetInstanceWithPatternAndTimeZone() {
        String pattern = "yyyy-MM-dd HH:mm:ss";
        String timeZoneId = "Europe/Paris";
        TimeZone timeZone = TimeZone.getTimeZone(timeZoneId);
        
        FastDateFormat instance = FastDateFormat.getInstance(pattern, timeZone);
        assertNotNull(instance);
        assertEquals(pattern, instance.getPattern());
        assertEquals(timeZone, instance.getTimeZone());
    }
    
    @Test
    public void testGetInstanceWithPatternAndLocale() {
        String pattern = "yyyy-MM-dd HH:mm:ss";
        Locale locale = Locale.US;
        
        FastDateFormat instance = FastDateFormat.getInstance(pattern, locale);
        assertNotNull(instance);
        assertEquals(pattern, instance.getPattern());
        assertEquals(locale, instance.getLocale());
    }
    
    @Test
    public void testGetInstanceWithPatternTimeZoneAndLocale() {
        String pattern = "yyyy-MM-dd HH:mm:ss";
        String timeZoneId = "Europe/Paris";
        TimeZone timeZone = TimeZone.getTimeZone(timeZoneId);
        Locale locale = Locale.US;
        
        FastDateFormat instance = FastDateFormat.getInstance(pattern, timeZone, locale);
        assertNotNull(instance);
        assertEquals(pattern, instance.getPattern());
        assertEquals(timeZone, instance.getTimeZone());
        assertEquals(locale, instance.getLocale());
    }
    
    @Test
    public void testGetDateInstance() {
        int style = FastDateFormat.SHORT;
        FastDateFormat instance = FastDateFormat.getDateInstance(style);
        assertNotNull(instance);
        assertEquals(style, instance.getCalendar().getCalendarType());
    }
    
    // ... (continue with similar tests for other methods)
    
    @Test
    public void testApplyRules() {
        // Test typical use case
        Calendar calendar = Calendar.getInstance();
        StringBuffer buf = new StringBuffer();
        
        FastDateFormat instance = FastDateFormat.getInstance();
        StringBuffer result = instance.applyRules(calendar, buf);
        
        assertNotNull(result);
        assertTrue(result.length() > 0);
    }
    
    // ... (continue with more tests for other methods)
    
}