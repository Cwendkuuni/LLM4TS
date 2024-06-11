import org.junit.Test;
import static org.junit.Assert.*;

public class FastDateFormatTest {

    @Test
    public void testGetInstance() {
        // Basic functionality test
        FastDateFormat instance = FastDateFormat.getInstance();
        assertNotNull(instance);

        // Edge cases and exception handling
        assertNull(FastDateFormat.getInstance(null));
        assertNull(FastDateFormat.getInstance("", null));
        assertNull(FastDateFormat.getInstance("", null, null));
        assertNull(FastDateFormat.getInstance("", TimeZone.getDefault(), null));
    }
    
    @Test
    public void testGetDateInstance() {
        // Basic functionality test
        FastDateFormat instance = FastDateFormat.getDateInstance(FastDateFormat.FULL);
        assertNotNull(instance);

        // Edge cases and exception handling
        assertNull(FastDateFormat.getDateInstance(-1));
        assertNull(FastDateFormat.getDateInstance(FastDateFormat.FULL, null));
        assertNull(FastDateFormat.getDateInstance(FastDateFormat.FULL, TimeZone.getDefault()));
    }
    
    @Test
    public void testGetTimeInstance() {
        // Basic functionality test
        FastDateFormat instance = FastDateFormat.getTimeInstance(FastDateFormat.LONG);
        assertNotNull(instance);

        // Edge cases and exception handling
        assertNull(FastDateFormat.getTimeInstance(-1));
        assertNull(FastDateFormat.getTimeInstance(FastDateFormat.LONG, null));
        assertNull(FastDateFormat.getTimeInstance(FastDateFormat.LONG, TimeZone.getDefault()));
    }
    
    @Test
    public void testGetDateTimeInstance() {
        // Basic functionality test
        FastDateFormat instance = FastDateFormat.getDateTimeInstance(FastDateFormat.MEDIUM, FastDateFormat.SHORT);
        assertNotNull(instance);

        // Edge cases and exception handling
        assertNull(FastDateFormat.getDateTimeInstance(11, 11));
        assertNull(FastDateFormat.getDateTimeInstance(FastDateFormat.MEDIUM, FastDateFormat.SHORT, null));
        assertNull(FastDateFormat.getDateTimeInstance(FastDateFormat.MEDIUM, FastDateFormat.SHORT, TimeZone.getDefault()));
    }

    // More test cases can be added similar to the above
    
}