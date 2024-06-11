import org.joda.time.Chronology;
import org.joda.time.DateTimeField;
import org.joda.time.chrono.BaseChronology;
import org.joda.time.chrono.ISOChronology;
import org.joda.time.field.LenientDateTimeField;
import org.joda.time.field.StrictDateTimeField;
import org.junit.Before;
import org.junit.Test;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;

public class LenientDateTimeFieldTest {

    private DateTimeField lenientField;
    private Chronology baseChronology;

    @Before
    public void setup() {
        baseChronology = ISOChronology.getInstance();
        lenientField = LenientDateTimeField.getInstance(baseChronology.millisOfSecond(), baseChronology);
    }

    @Test
    public void testGet() {
        long instant = baseChronology.getDateTimeMillis(2021, 1, 1, 12, 30, 45, 123);
        int expectedValue = 123;
        
        assertEquals(expectedValue, lenientField.get(instant));
    }

    @Test
    public void testSet_validValue() {
        long instant = baseChronology.getDateTimeMillis(2021, 1, 1, 12, 30, 45, 123);
        int value = 456;
        long expectedInstant = baseChronology.getDateTimeMillis(2021, 1, 1, 12, 30, 45, value);

        assertEquals(expectedInstant, lenientField.set(instant, value));
    }

    @Test
    public void testSet_outOfBoundsValue() {
        long instant = baseChronology.getDateTimeMillis(2021, 1, 1, 12, 30, 45, 123);
        int value = 1000; // out of bounds value
        long expectedInstant = baseChronology.getDateTimeMillis(2021, 1, 1, 12, 30, 45, 1000);

        assertEquals(expectedInstant, lenientField.set(instant, value));
    }

    @Test
    public void testSet_negativeValue() {
        long instant = baseChronology.getDateTimeMillis(2021, 1, 1, 12, 30, 45, 123);
        int value = -50; // negative value
        long expectedInstant = baseChronology.getDateTimeMillis(2021, 1, 1, 12, 30, 45, -50);

        assertEquals(expectedInstant, lenientField.set(instant, value));
    }

    @Test
    public void testSet_maxValue() {
        long instant = baseChronology.getDateTimeMillis(2021, 1, 1, 12, 30, 45, 123);
        int value = lenientField.getMaximumValue();
        long expectedInstant = baseChronology.getDateTimeMillis(2021, 1, 1, 12, 30, 45, value);

        assertEquals(expectedInstant, lenientField.set(instant, value));
    }

    @Test
    public void testSet_minValue() {
        long instant = baseChronology.getDateTimeMillis(2021, 1, 1, 12, 30, 45, 123);
        int value = lenientField.getMinimumValue();
        long expectedInstant = baseChronology.getDateTimeMillis(2021, 1, 1, 12, 30, 45, value);

        assertEquals(expectedInstant, lenientField.set(instant, value));
    }

    @Test
    public void testGetInstance_withNullField() {
        DateTimeField nullField = null;
        assertNull(LenientDateTimeField.getInstance(nullField, baseChronology));
    }

    @Test
    public void testGetInstance_withStrictField() {
        DateTimeField strictField = new StrictDateTimeField(baseChronology.millisOfSecond());
        DateTimeField resultField = LenientDateTimeField.getInstance(strictField, baseChronology);

        assertEquals(baseChronology.millisOfSecond(), resultField);
    }

    @Test
    public void testGetInstance_withLenientField() {
        DateTimeField lenientField = new LenientDateTimeField(baseChronology.millisOfSecond(), baseChronology);
        DateTimeField resultField = LenientDateTimeField.getInstance(lenientField, baseChronology);

        assertEquals(lenientField, resultField);
    }

    @Test
    public void testGetInstance_withNonLenientField() {
        DateTimeField nonLenientField = baseChronology.millisOfSecond();
        DateTimeField resultField = LenientDateTimeField.getInstance(nonLenientField, baseChronology);

        assertNotNull(resultField);
        assertEquals(LenientDateTimeField.class, resultField.getClass());
        assertEquals(nonLenientField, StrictDateTimeField.class.cast(resultField).getWrappedField());
    }

}