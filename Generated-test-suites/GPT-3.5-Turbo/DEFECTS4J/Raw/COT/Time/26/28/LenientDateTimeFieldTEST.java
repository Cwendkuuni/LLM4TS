import org.joda.time.Chronology;
import org.joda.time.DateTimeField;
import org.joda.time.LocalTime;
import org.junit.Test;

import static org.junit.Assert.assertEquals;

public class LenientDateTimeFieldTest {

    private Chronology baseChronology;
    private DateTimeField dateTimeField;

    @Test
    public void testGetInstance_NullField_ReturnsNull() {
        DateTimeField result = LenientDateTimeField.getInstance(null, baseChronology);
        assertEquals(null, result);
    }

    @Test
    public void testGetInstance_StrictField_ReturnsWrappedField() {
        DateTimeField strictField = new StrictDateTimeField(dateTimeField, baseChronology);
        DateTimeField result = LenientDateTimeField.getInstance(strictField, baseChronology);
        assertEquals(dateTimeField, result);
    }

    @Test
    public void testGetInstance_LenientField_ReturnsSameField() {
        DateTimeField lenientField = new LenientDateTimeField(dateTimeField, baseChronology);
        DateTimeField result = LenientDateTimeField.getInstance(lenientField, baseChronology);
        assertEquals(lenientField, result);
    }

    @Test
    public void testGetInstance_NonLenientField_ReturnsLenientField() {
        DateTimeField result = LenientDateTimeField.getInstance(dateTimeField, baseChronology);
        assertEquals(LenientDateTimeField.class, result.getClass());
    }

    @Test
    public void testIsLenient_ReturnsTrue() {
        LenientDateTimeField lenientDateTimeField = new LenientDateTimeField(dateTimeField, baseChronology);
        boolean result = lenientDateTimeField.isLenient();
        assertEquals(true, result);
    }

    @Test
    public void testSet_InBoundsValue_ReturnsUpdatedValue() {
        LenientDateTimeField lenientDateTimeField = new LenientDateTimeField(dateTimeField, baseChronology);
        long instant = new LocalTime(2021, 1, 1, 0, 0).toDateTime().getMillis();
        int value = 10;
        long result = lenientDateTimeField.set(instant, value);
        assertEquals(instant + value, result);
    }

    @Test
    public void testSet_OutOfBoundsValue_ReturnsUpdatedValueWithAddition() {
        LenientDateTimeField lenientDateTimeField = new LenientDateTimeField(dateTimeField, baseChronology);
        long instant = new LocalTime(2021, 1, 1, 0, 0).toDateTime().getMillis();
        int value = 100;
        long result = lenientDateTimeField.set(instant, value);
        assertEquals(instant + value, result);
    }
}