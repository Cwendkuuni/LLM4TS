import org.joda.time.DateTimeZone;
import org.joda.time.LocalDateTime;
import org.joda.time.chrono.GregorianChronology;
import org.joda.time.field.LenientDateTimeField;
import org.joda.time.field.StrictDateTimeField;
import org.junit.Before;
import org.junit.Test;
import static org.junit.Assert.*;

public class LenientDateTimeFieldTest {

    private LenientDateTimeField lenientField;

    @Before
    public void setUp() {
        lenientField = new LenientDateTimeField(new StrictDateTimeField(GregorianChronology.getInstance().minuteOfDay()), GregorianChronology.getInstance());
    }

    @Test
    public void testIsLenient() {
        assertTrue(lenientField.isLenient());
    }

    @Test
    public void testSet() {
        long initialInstant = new LocalDateTime(2022, 11, 15, 10, 30).toDateTime(DateTimeZone.UTC).getMillis();
        int value = 45;
        long newInstant = lenientField.set(initialInstant, value);
        assertEquals(new LocalDateTime(newInstant, DateTimeZone.UTC).getMinuteOfDay(), value);
    }

    @Test(expected = NullPointerException.class)
    public void testGetInstanceWithNullField() {
        LenientDateTimeField.getInstance(null, GregorianChronology.getInstance());
    }

    @Test
    public void testGetInstanceWithStrictField() {
        StrictDateTimeField strictField = new StrictDateTimeField(GregorianChronology.getInstance().minuteOfDay());
        DateTimeField lenientFieldInstance = LenientDateTimeField.getInstance(strictField, GregorianChronology.getInstance());
        assertTrue(lenientFieldInstance instanceof LenientDateTimeField);
    }

    @Test
    public void testGetInstanceWithLenientField() {
        LenientDateTimeField originalField = new LenientDateTimeField(GregorianChronology.getInstance().minuteOfDay(), GregorianChronology.getInstance());
        DateTimeField lenientFieldInstance = LenientDateTimeField.getInstance(originalField, GregorianChronology.getInstance());
        assertEquals(originalField, lenientFieldInstance);
    }
}