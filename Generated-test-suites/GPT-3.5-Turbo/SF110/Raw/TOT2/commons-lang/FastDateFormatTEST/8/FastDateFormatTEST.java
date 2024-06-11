import org.junit.Test;
import static org.junit.Assert.*;
import java.util.*;
import java.text.*;

public class FastDateFormatTest {

    @Test
    public void testGetInstance() {
        FastDateFormat instance = FastDateFormat.getInstance();
        assertNotNull(instance);
    }

    @Test
    public void testGetDateInstance() {
        FastDateFormat instance = FastDateFormat.getDateInstance(FastDateFormat.FULL);
        assertNotNull(instance);
    }

    @Test
    public void testGetTimeInstance() {
        FastDateFormat instance = FastDateFormat.getTimeInstance(FastDateFormat.LONG);
        assertNotNull(instance);
    }

    @Test
    public void testGetDateTimeInstance() {
        FastDateFormat instance = FastDateFormat.getDateTimeInstance(FastDateFormat.MEDIUM, FastDateFormat.SHORT);
        assertNotNull(instance);
    }

    @Test
    public void testFormatLong() {
        FastDateFormat instance = FastDateFormat.getInstance();
        String formattedDate = instance.format(1619439895000L);
        assertEquals("04/26/2021 09:38 AM", formattedDate);
    }

    @Test
    public void testParse() throws ParseException {
        FastDateFormat instance = FastDateFormat.getInstance("MM/dd/yyyy");
        Date parsedDate = instance.parse("04/26/2021");
        Calendar calendar = Calendar.getInstance();
        calendar.setTime(parsedDate);
        assertEquals(2021, calendar.get(Calendar.YEAR));
        assertEquals(Calendar.APRIL, calendar.get(Calendar.MONTH));
        assertEquals(26, calendar.get(Calendar.DAY_OF_MONTH));
    }

    @Test
    public void testEquals() {
        FastDateFormat instance1 = FastDateFormat.getInstance();
        FastDateFormat instance2 = FastDateFormat.getInstance();
        assertTrue(instance1.equals(instance2));
    }

    // Edge cases and error scenarios could be added here

}