import org.junit.Test;
import static org.junit.Assert.*;

public class FastDateFormatTest {

    @Test
    public void testGetInstance() {
        FastDateFormat instance = FastDateFormat.getInstance();
        assertNotNull(instance);
    }

    @Test
    public void testGetInstanceWithString() {
        FastDateFormat instance = FastDateFormat.getInstance("yyyy-MM-dd HH:mm:ss");
        assertNotNull(instance);
    }

    @Test
    public void testGetInstanceWithStringAndTimeZone() {
        FastDateFormat instance = FastDateFormat.getInstance("yyyy-MM-dd HH:mm:ss", TimeZone.getTimeZone("GMT"));
        assertNotNull(instance);
    }

    // Other getInstance tests ...

    @Test
    public void testGetDateInstance() {
        FastDateFormat instance = FastDateFormat.getDateInstance(FastDateFormat.FULL);
        assertNotNull(instance);
    }

    // Other getDateInstance tests ...

    @Test
    public void testFormatObject() {
        FastDateFormat instance = FastDateFormat.getDateInstance(FastDateFormat.FULL);
        StringBuffer buffer = new StringBuffer();
        FieldPosition pos = new FieldPosition(0);
        assertEquals("Expected formatted date", buffer.toString(), instance.format(new Date(), buffer, pos).toString());
    }

    // Other format tests ...

    @Test
    public void testParse() throws ParseException {
        FastDateFormat instance = FastDateFormat.getInstance("yyyy-MM-dd HH:mm:ss");
        Date parsedDate = instance.parse("2022-01-01 12:00:00");
        assertNotNull(parsedDate);
    }

    // Other parse tests ...

    @Test
    public void testEquals() {
        FastDateFormat instance1 = FastDateFormat.getInstance();
        FastDateFormat instance2 = FastDateFormat.getInstance();
        assertEquals("Expected instances to be equal", instance1, instance2);
    }

    // Other equals and hashCode tests ...

}