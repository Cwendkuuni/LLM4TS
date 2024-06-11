import org.joda.time.Chronology;
import org.joda.time.DateTimeFieldType;
import org.joda.time.LocalDate;
import org.joda.time.LocalDateTime;
import org.joda.time.LocalTime;
import org.joda.time.MutableDateTime;
import org.joda.time.DateTimeConstants;
import org.junit.Test;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertThrows;
import static org.junit.Assert.assertTrue;

import org.joda.time.field.LenientDateTimeField;
import org.joda.time.field.StrictDateTimeField;

public class LenientDateTimeFieldTest {

    @Test
    public void testGetInstance() {
        Chronology chronology = DateTimeConstants.UTC;
        DateTimeField strictField = new StrictDateTimeField(DateTimeFieldType.dayOfMonth(), chronology);
        LenientDateTimeField lenientField = LenientDateTimeField.getInstance(strictField, chronology);
        assertEquals(lenientField, LenientDateTimeField.getInstance(lenientField, chronology));
        assertFalse(strictField.isLenient());
        assertTrue(lenientField.isLenient());
    }

    @Test
    public void testSetOutOfBoundsValue() {
        Chronology chronology = DateTimeConstants.UTC;
        DateTimeField strictField = new StrictDateTimeField(DateTimeFieldType.dayOfMonth(), chronology);
        LenientDateTimeField lenientField = LenientDateTimeField.getInstance(strictField, chronology);
        MutableDateTime dateTime = new MutableDateTime(1, 1, 2022, 0, 0, 0, 0, chronology);

        // Test setting a value less than the minimum
        long minValue = lenientField.getMinimumValue();
        long result = lenientField.set(dateTime.getMillis(), (int) minValue);
        assertEquals(minValue, getValue(lenientField, dateTime));
        assertEquals(result, dateTime.getMillis());

        // Test setting a value greater than the maximum
        long maxValue = lenientField.getMaximumValue();
        result = lenientField.set(dateTime.getMillis(), (int) maxValue);
        assertEquals(maxValue, getValue(lenientField, dateTime));
        assertEquals(result, dateTime.getMillis());
    }

    @Test
    public void testSetValueWithinBounds() {
        Chronology chronology = DateTimeConstants.UTC;
        DateTimeField strictField = new StrictDateTimeField(DateTimeFieldType.dayOfMonth(), chronology);
        LenientDateTimeField lenientField = LenientDateTimeField.getInstance(strictField, chronology);
        MutableDateTime dateTime = new MutableDateTime(1, 1, 2022, 0, 0, 0, 0, chronology);

        int valueToSet = 15; // Within range
        long result = lenientField.set(dateTime.getMillis(), valueToSet);
        assertEquals(valueToSet, getValue(lenientField, dateTime));
        assertEquals(result, dateTime.getMillis());
    }

    @Test(expected = IllegalArgumentException.class)
    public void testSetNullDateTime() {
        Chronology chronology = DateTimeConstants.UTC;
        DateTimeField lenientField = new LenientDateTimeField(new StrictDateTimeField(DateTimeFieldType.dayOfMonth(), chronology), chronology);
        lenientField.set(null, 1);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testSetNegativeValue() {
        Chronology chronology = DateTimeConstants.UTC;
        DateTimeField lenientField = new LenientDateTimeField(new StrictDateTimeField(DateTimeFieldType.dayOfMonth(), chronology), chronology);
        MutableDateTime dateTime = new MutableDateTime(1, 1, 2022, 0, 0, 0, 0, chronology);
        lenientField.set(dateTime.getMillis(), -1);
    }

    @Test(expected = IllegalStateException.class)
    public void testSetValueWithDifferentChronology() {
        Chronology baseChronology = DateTimeConstants.UTC;
        Chronology differentChronology = DateTimeConstants.PROLEPTIC_GREGORIAN;
        DateTimeField strictField = new StrictDateTimeField(DateTimeFieldType.dayOfMonth(), baseChronology);
        LenientDateTimeField lenientField = LenientDateTimeField.getInstance(strictField, baseChronology);
        MutableDateTime dateTime = new MutableDateTime(1, 1, 2022, 0, 0, 0, 0, baseChronology);
        lenientField.set(dateTime.getMillis(), 1, differentChronology);
    }

    private long getValue(DateTimeField lenientField, MutableDateTime dateTime) {
        return lenientField.get(dateTime.toLong());
    }
}