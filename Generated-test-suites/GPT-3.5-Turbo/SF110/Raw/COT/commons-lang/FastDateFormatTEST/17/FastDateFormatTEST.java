
import org.junit.Test;
import java.text.ParseException;
import java.util.Calendar;
import java.util.Date;
import java.util.Locale;
import java.util.TimeZone;
import static org.junit.Assert.*;

public class FastDateFormatTest {

    @Test
    public void testGetInstance() {
        FastDateFormat dateFormat = FastDateFormat.getInstance();
        assertNotNull(dateFormat);
    }
    
    @Test
    public void testGetInstanceWithPattern() {
        FastDateFormat dateFormat = FastDateFormat.getInstance("yyyy/MM/dd");
        assertNotNull(dateFormat);
    }
    
    @Test
    public void testGetInstanceWithPatternAndTimeZone() {
        TimeZone timeZone = TimeZone.getTimeZone("GMT");
        FastDateFormat dateFormat = FastDateFormat.getInstance("yyyy/MM/dd", timeZone);
        assertNotNull(dateFormat);
    }
    
    @Test
    public void testGetInstanceWithPatternAndLocale() {
        Locale locale = Locale.US;
        FastDateFormat dateFormat = FastDateFormat.getInstance("yyyy/MM/dd", locale);
        assertNotNull(dateFormat);
    }
    
    @Test
    public void testGetInstanceWithPatternTimeZoneAndLocale() {
        TimeZone timeZone = TimeZone.getTimeZone("GMT");
        Locale locale = Locale.US;
        FastDateFormat dateFormat = FastDateFormat.getInstance("yyyy/MM/dd", timeZone, locale);
        assertNotNull(dateFormat);
    }

    @Test
    public void testGetInstanceWithEmptyPattern() {
        FastDateFormat dateFormat = FastDateFormat.getInstance("");
        assertNotNull(dateFormat);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testGetInstanceWithInvalidPattern() {
        FastDateFormat dateFormat = FastDateFormat.getInstance("yyyy/mm/dd");
    }

    @Test(expected = IllegalArgumentException.class)
    public void testGetInstanceWithNullTimeZone() {
        FastDateFormat dateFormat = FastDateFormat.getInstance("yyyy/MM/dd", null);
    }

    @Test
    public void testFormatWithNullObject() {
        FastDateFormat dateFormat = FastDateFormat.getInstance("yyyy/MM/dd");
        StringBuffer toAppendTo = new StringBuffer();
        FieldPosition pos = new FieldPosition(0);
        StringBuffer result = dateFormat.format(null, toAppendTo, pos);
        assertNotNull(result);
        assertEquals("", result.toString());
    }

    @Test
    public void testParseWithEmptySource() throws ParseException {
        FastDateFormat dateFormat = FastDateFormat.getInstance("yyyy/MM/dd");
        Date date = dateFormat.parse("");
        assertNull(date);
    }

    // more test cases for other methods...

}
