import static org.junit.Assert.*;
import org.junit.Test;
import org.joda.time.DateTimeField;
import org.joda.time.DateTimeFieldType;
import org.joda.time.chrono.ISOChronology;

public class LenientDateTimeFieldTest {

    @Test
    public void testIsLenient() {
        LenientDateTimeField field = new LenientDateTimeField(
                ISOChronology.getInstance().year(), ISOChronology.getInstance());
        assertTrue(field.isLenient());
    }

    @Test
    public void testSet() {
        LenientDateTimeField field = new LenientDateTimeField(
                ISOChronology.getInstance().dayOfMonth(), ISOChronology.getInstance());
        long instant = System.currentTimeMillis();
        int value = 30;
        long result = field.set(instant, value);
        assertEquals(ISOChronology.getInstance().dayOfMonth().set(instant, value), result);
    }

    // Edge case: setting a negative value
    @Test
    public void testSetNegativeValue() {
        LenientDateTimeField field = new LenientDateTimeField(
                ISOChronology.getInstance().monthOfYear(), ISOChronology.getInstance());
        long instant = System.currentTimeMillis();
        int value = -1;
        long result = field.set(instant, value);
        assertEquals(ISOChronology.getInstance().monthOfYear().set(instant, value), result);
    }

    // Exception handling: passing null field
    @Test(expected = NullPointerException.class)
    public void testGetInstanceWithNullField() {
        LenientDateTimeField.getInstance(null, ISOChronology.getInstance());
    }
}