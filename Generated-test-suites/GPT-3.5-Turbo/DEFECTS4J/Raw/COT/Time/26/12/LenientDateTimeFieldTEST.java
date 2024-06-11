
import static org.junit.Assert.*;
import org.junit.Test;
import org.joda.time.Chronology;
import org.joda.time.DateTimeField;
import org.joda.time.DateTimeFieldType;
import org.joda.time.Instant;
import org.joda.time.chrono.ISOChronology;
import org.joda.time.field.LenientDateTimeField;

public class LenientDateTimeFieldTest {

    private DateTimeField createDateTimeField() {
        Chronology base = ISOChronology.getInstanceUTC();
        DateTimeFieldType fieldType = DateTimeFieldType.seconds();
        return new LenientDateTimeField(fieldType.getField(base), base);
    }

    @Test
    public void testIsLenient() {
        DateTimeField field = createDateTimeField();
        assertTrue(field.isLenient());
    }

    @Test
    public void testSet() {
        DateTimeField field = createDateTimeField();
        long instant = new Instant().getMillis();
        int value = 10;
        long result = field.set(instant, value);
        assertEquals(instant + value * 1000, result);
    }

    @Test
    public void testSetWithNegativeValue() {
        DateTimeField field = createDateTimeField();
        long instant = new Instant().getMillis();
        int value = -10;
        long result = field.set(instant, value);
        assertEquals(instant - value * 1000, result);
    }

    @Test
    public void testSetWithZeroValue() {
        DateTimeField field = createDateTimeField();
        long instant = new Instant().getMillis();
        int value = 0;
        long result = field.set(instant, value);
        assertEquals(instant, result);
    }

    @Test
    public void testSetWithMaxValue() {
        DateTimeField field = createDateTimeField();
        long instant = new Instant().getMillis();
        int value = Integer.MAX_VALUE;
        long result = field.set(instant, value);
        assertEquals(instant + value * 1000L, result);
    }

    @Test
    public void testSetWithMinValue() {
        DateTimeField field = createDateTimeField();
        long instant = new Instant().getMillis();
        int value = Integer.MIN_VALUE;
        long result = field.set(instant, value);
        assertEquals(instant + value * 1000L, result);
    }
}
