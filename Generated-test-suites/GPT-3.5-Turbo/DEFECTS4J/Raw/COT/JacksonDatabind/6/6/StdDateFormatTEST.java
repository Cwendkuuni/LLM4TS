import static org.junit.Assert.*;
import org.junit.Test;

public class StdDateFormatTest {
    
    @Test
    public void testParse() throws Exception {
        StdDateFormat format = new StdDateFormat();
        String dateStr = "2021-06-30T10:20:30.123Z";
        assertNotNull(format.parse(dateStr));
    }
    
    @Test
    public void testParseInvalidFormat() throws Exception {
        StdDateFormat format = new StdDateFormat();
        String dateStr = "06/30/2021";
        try {
            format.parse(dateStr);
            fail("ParseException should have been thrown");
        } catch (ParseException e) {
            assertEquals("Can not parse date \"06/30/2021\": not compatible with any of standard forms (\"yyyy-MM-dd'T'HH:mm:ss.SSSZ\", \"yyyy-MM-dd'T'HH:mm:ss.SSS'Z'\", \"EEE, dd MMM yyyy HH:mm:ss zzz\", \"yyyy-MM-dd\")", e.getMessage());
        }
    }
    
    @Test
    public void testFormat() {
        StdDateFormat format = new StdDateFormat();
        assertNotNull(format.format(new Date()));
    }
    
    @Test
    public void testFormatWithDifferentTimeZone() {
        StdDateFormat format = new StdDateFormat().withTimeZone(TimeZone.getTimeZone("PST"));
        assertNotNull(format.format(new Date()));
    }
}