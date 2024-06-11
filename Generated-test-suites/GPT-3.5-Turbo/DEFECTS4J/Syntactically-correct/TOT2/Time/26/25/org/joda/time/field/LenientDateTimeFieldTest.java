import static org.junit.Assert.*;
import org.junit.Test;
import org.joda.time.DateTimeField;
import org.joda.time.chrono.ISOChronology;
import org.joda.time.field.LenientDateTimeField;

public class LenientDateTimeFieldTest {

    @Test
    public void testGetInstance() {
        DateTimeField field = ISOChronology.getInstance().hourOfDay();
        DateTimeField lenientField = LenientDateTimeField.getInstance(field, ISOChronology.getInstance());
        assertTrue(lenientField instanceof LenientDateTimeField);
    }

    @Test
    public void testIsLenient() {
        DateTimeField field = ISOChronology.getInstance().hourOfDay();
        DateTimeField lenientField = LenientDateTimeField.getInstance(field, ISOChronology.getInstance());
        assertTrue(lenientField.isLenient());
    }

    @Test
    public void testSet() {
        DateTimeField field = ISOChronology.getInstance().hourOfDay();
        DateTimeField lenientField = LenientDateTimeField.getInstance(field, ISOChronology.getInstance());
        
        long instant = System.currentTimeMillis();
        int value = 10;
        
        long result = lenientField.set(instant, value);
        assertEquals(value, lenientField.get(result));
    }

    @Test
    public void testSetEdgeCase() {
        DateTimeField field = ISOChronology.getInstance().hourOfDay();
        DateTimeField lenientField = LenientDateTimeField.getInstance(field, ISOChronology.getInstance());
        
        long instant = System.currentTimeMillis();
        int value = 24; // hourOfDay range is 0-23
        
        long result = lenientField.set(instant, value);
        assertEquals(value % 24, lenientField.get(result));
    }

    @Test
    public void testSetErrorCase() {
        DateTimeField field = null;
        DateTimeField lenientField = LenientDateTimeField.getInstance(field, ISOChronology.getInstance());
        
        assertNull(lenientField);
    }
}