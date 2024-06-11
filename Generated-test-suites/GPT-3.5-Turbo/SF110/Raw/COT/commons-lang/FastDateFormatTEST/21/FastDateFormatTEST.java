import static org.junit.Assert.*;
import org.junit.Test;
import java.util.*;
import java.text.*;

public class FastDateFormatTest {

    @Test
    public void testGetInstance() {
        FastDateFormat dateFormat = FastDateFormat.getInstance();
        assertNotNull(dateFormat);
    }

    @Test
    public void testGetInstanceWithPattern() {
        FastDateFormat dateFormat = FastDateFormat.getInstance("yyyy-MM-dd");
        assertNotNull(dateFormat);
    }

    @Test
    public void testGetInstanceWithPatternAndTimeZone() {
        FastDateFormat dateFormat = FastDateFormat.getInstance("yyyy-MM-dd", TimeZone.getTimeZone("GMT"));
        assertNotNull(dateFormat);
    }

    @Test
    public void testGetInstanceWithPatternAndLocale() {
        FastDateFormat dateFormat = FastDateFormat.getInstance("yyyy-MM-dd", Locale.US);
        assertNotNull(dateFormat);
    }

    @Test
    public void testGetInstanceWithPatternTimeZoneAndLocale() {
        FastDateFormat dateFormat = FastDateFormat.getInstance("yyyy-MM-dd", TimeZone.getTimeZone("GMT"), Locale.US);
        assertNotNull(dateFormat);
    }

    @Test
    public void testGetDateInstance() {
        FastDateFormat dateFormat = FastDateFormat.getDateInstance(FastDateFormat.MEDIUM);
        assertNotNull(dateFormat);
    }

    @Test
    public void testGetDateInstanceWithLocale() {
        FastDateFormat dateFormat = FastDateFormat.getDateInstance(FastDateFormat.MEDIUM, Locale.US);
        assertNotNull(dateFormat);
    }
    
    @Test
    public void testGetDateInstanceWithTimeZone() {
        FastDateFormat dateFormat = FastDateFormat.getDateInstance(FastDateFormat.MEDIUM, TimeZone.getTimeZone("GMT"));
        assertNotNull(dateFormat);
    }
    
    @Test
    public void testGetDateInstanceWithTimeZoneAndLocale() {
        FastDateFormat dateFormat = FastDateFormat.getDateInstance(FastDateFormat.MEDIUM, TimeZone.getTimeZone("GMT"), Locale.US);
        assertNotNull(dateFormat);
    }
    
    @Test
    public void testGetTimeInstance() {
        FastDateFormat dateFormat = FastDateFormat.getTimeInstance(FastDateFormat.MEDIUM);
        assertNotNull(dateFormat);
    }

    @Test
    public void testGetTimeInstanceWithLocale() {
        FastDateFormat dateFormat = FastDateFormat.getTimeInstance(FastDateFormat.MEDIUM, Locale.US);
        assertNotNull(dateFormat);
    }
    
    @Test
    public void testGetTimeInstanceWithTimeZone() {
        FastDateFormat dateFormat = FastDateFormat.getTimeInstance(FastDateFormat.MEDIUM, TimeZone.getTimeZone("GMT"));
        assertNotNull(dateFormat);
    }
    
    @Test
    public void testGetTimeInstanceWithTimeZoneAndLocale() {
        FastDateFormat dateFormat = FastDateFormat.getTimeInstance(FastDateFormat.MEDIUM, TimeZone.getTimeZone("GMT"), Locale.US);
        assertNotNull(dateFormat);
    }
    
    @Test
    public void testGetDateTimeInstance() {
        FastDateFormat dateFormat = FastDateFormat.getDateTimeInstance(FastDateFormat.MEDIUM, FastDateFormat.MEDIUM);
        assertNotNull(dateFormat);
    }

    @Test
    public void testGetDateTimeInstanceWithLocale() {
        FastDateFormat dateFormat = FastDateFormat.getDateTimeInstance(FastDateFormat.MEDIUM, FastDateFormat.MEDIUM, Locale.US);
        assertNotNull(dateFormat);
    }
    
    @Test
    public void testGetDateTimeInstanceWithTimeZone() {
        FastDateFormat dateFormat = FastDateFormat.getDateTimeInstance(FastDateFormat.MEDIUM, FastDateFormat.MEDIUM, TimeZone.getTimeZone("GMT"));
        assertNotNull(dateFormat);
    }
    
    @Test
    public void testGetDateTimeInstanceWithTimeZoneAndLocale() {
        FastDateFormat dateFormat = FastDateFormat.getDateTimeInstance(FastDateFormat.MEDIUM, FastDateFormat.MEDIUM, TimeZone.getTimeZone("GMT"), Locale.US);
        assertNotNull(dateFormat);
    }
    
    @Test
    public void testFormatObjectWithNullObject() {
        FastDateFormat dateFormat = FastDateFormat.getInstance();
        StringBuffer buffer = new StringBuffer();
        assertNull(dateFormat.format(null, buffer, null));
    }
    
    @Test
    public void testFormatObjectWithNonDateObject() {
        FastDateFormat dateFormat = FastDateFormat.getInstance();
        StringBuffer buffer = new StringBuffer();
        assertEquals(buffer, dateFormat.format("hello", buffer, null));
    }
    
    @Test
    public void testFormatLong() {
        FastDateFormat dateFormat = FastDateFormat.getInstance();
        String formattedDate = dateFormat.format(System.currentTimeMillis());
        assertNotNull(formattedDate);
    }
    
    @Test
    public void testFormatDate() {
        FastDateFormat dateFormat = FastDateFormat.getInstance();
        String formattedDate = dateFormat.format(new Date());
        assertNotNull(formattedDate);
    }
    
    @Test
    public void testFormatCalendar() {
        FastDateFormat dateFormat = FastDateFormat.getInstance();
        String formattedDate = dateFormat.format(Calendar.getInstance());
        assertNotNull(formattedDate);
    }
    
    @Test
    public void testParse() throws ParseException {
        FastDateFormat dateFormat = FastDateFormat.getInstance();
        Date parsedDate = dateFormat.parse("2021-01-01");
        assertNotNull(parsedDate);
    }
    
    @Test
    public void testParseWithParsePosition() {
        FastDateFormat dateFormat = FastDateFormat.getInstance();
        ParsePosition parsePos = new ParsePosition(0);
        Date parsedDate = dateFormat.parse("2021-01-01", parsePos);
        assertNotNull(parsedDate);
        assertEquals(10, parsePos.getIndex());
    }
    
    @Test
    public void testParseObject() {
        FastDateFormat dateFormat = FastDateFormat.getInstance();
        ParsePosition parsePos = new ParsePosition(0);
        Object parsedObject = dateFormat.parseObject("2021-01-01", parsePos);
        assertNotNull(parsedObject);
        assertEquals(10, parsePos.getIndex());
    }

    @Test
    public void testGetters() {
        FastDateFormat dateFormat = FastDateFormat.getInstance();
        assertNotNull(dateFormat.getPattern());
        assertNotNull(dateFormat.getTimeZone());
        assertNotNull(dateFormat.getLocale());
        assertNotNull(dateFormat.getMaxLengthEstimate());
    }

    @Test
    public void testEquals() {
        FastDateFormat dateFormat1 = FastDateFormat.getInstance();
        FastDateFormat dateFormat2 = FastDateFormat.getInstance();
        assertTrue(dateFormat1.equals(dateFormat2));
    }

    @Test
    public void testHashCode() {
        FastDateFormat dateFormat1 = FastDateFormat.getInstance();
        FastDateFormat dateFormat2 = FastDateFormat.getInstance();
        assertEquals(dateFormat1.hashCode(), dateFormat2.hashCode());
    }

    @Test
    public void testToString() {
        FastDateFormat dateFormat = FastDateFormat.getInstance();
        assertEquals("FastDateFormat[MMMM dd, yyyy - GMT]", dateFormat.toString());
    }

}