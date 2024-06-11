import static org.junit.Assert.*;
import org.junit.Test;
import java.text.ParseException;
import java.util.*;

public class FastDateFormatTest {

    @Test
    public void testGetInstance() {
        FastDateFormat instance = FastDateFormat.getInstance();
        assertNotNull(instance);
    }

    @Test
    public void testGetInstanceWithPattern() {
        FastDateFormat instance = FastDateFormat.getInstance("yyyy-MM-dd");
        assertNotNull(instance);
    }

    // More test cases for other getInstance methods can be added here

    @Test
    public void testFormatObject() {
        FastDateFormat instance = FastDateFormat.getInstance();
        StringBuffer buffer = new StringBuffer();
        assertEquals(instance.format(new Date(), buffer, new FieldPosition(0)), buffer);
    }

    // More test cases for other format methods can be added here

    @Test(expected = ParseException.class)
    public void testParseWithInvalidSource() throws ParseException {
        FastDateFormat instance = FastDateFormat.getInstance();
        instance.parse("invalid source");
    }

    // More test cases for other parse methods and error scenarios can be added here

    @Test
    public void testGetPattern() {
        FastDateFormat instance = FastDateFormat.getInstance("yyyy-MM-dd");
        assertEquals("yyyy-MM-dd", instance.getPattern());
    }

    // More test cases for other getters and edge cases can be added here

    @Test
    public void testEquals() {
        FastDateFormat instance1 = FastDateFormat.getInstance();
        FastDateFormat instance2 = FastDateFormat.getInstance();
        assertTrue(instance1.equals(instance2));
    }

    // More test cases for other methods can be added here

}