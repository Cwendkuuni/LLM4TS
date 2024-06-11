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
        FastDateFormat instance = FastDateFormat.getInstance("yyyy-MM-dd HH:mm:ss");
        assertNotNull(instance);
    }
    
    // Add test cases for other getInstance methods
    
    @Test
    public void testFormatLongMillis() {
        FastDateFormat format = FastDateFormat.getInstance("yyyy-MM-dd HH:mm:ss");
        String result = format.format(1598914800000L); // "2020-09-01 12:00:00"
        assertEquals("2020-09-01 12:00:00", result);
    }
    
    // Add more format test cases
    
    @Test
    public void testParse() throws ParseException {
        FastDateFormat format = FastDateFormat.getInstance("yyyy-MM-dd HH:mm:ss");
        Date date = format.parse("2020-09-01 12:00:00");
        assertNotNull(date);
    }
    
    // Add more parse test cases
    
    // Add test cases for other public methods
    
    @Test
    public void testEdgeCases() {
        // Add test cases for edge cases
    }
    
    @Test
    public void testExceptionHandling() {
        // Add test cases for exception handling scenarios
    }
}