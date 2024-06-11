// Combined JUnit 4 test file by merging all the proposed test cases

import org.junit.Test;
import static org.junit.Assert.*;

public class FastDateFormatTest {
    
    @Test
    public void testGetInstance() {
        FastDateFormat instance = FastDateFormat.getInstance();
        assertNotNull(instance);
    }
    
    @Test
    public void testFormatMillis() {
        FastDateFormat instance = FastDateFormat.getInstance();
        String formatted = instance.format(System.currentTimeMillis());
        assertNotNull(formatted);
    }
    
    @Test
    public void testParse() throws ParseException {
        FastDateFormat instance = FastDateFormat.getInstance();
        Date parsedDate = instance.parse("2021-09-20");
        assertNotNull(parsedDate);
    }
    
    @Test
    public void testFormatDate() {
        FastDateFormat instance = FastDateFormat.getInstance();
        String formatted = instance.format(new Date());
        assertNotNull(formatted);
    }
    
    @Test
    public void testGetTimeInstance() {
        FastDateFormat instance = FastDateFormat.getTimeInstance(FastDateFormat.LONG);
        assertNotNull(instance);
    }
    
    @Test
    public void testFormatCalendar() {
        FastDateFormat instance = FastDateFormat.getInstance();
        String formatted = instance.format(Calendar.getInstance());
        assertNotNull(formatted);
    }
    
    @Test
    public void testParseWithPos() {
        FastDateFormat instance = FastDateFormat.getInstance();
        ParsePosition pos = new ParsePosition(0);
        Date parsedDate = instance.parse("2021-09-20", pos);
        assertNotNull(parsedDate);
    }

}