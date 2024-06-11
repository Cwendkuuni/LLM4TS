package org.joda.time.field.test;

import org.joda.time.Chronology;
import org.joda.time.DateTimeConstants;
import org.joda.time.DateTimeField;
import org.joda.time.DateTimeFieldType;
import org.joda.time.LocalDateTime;
import org.joda.time.MutableDateTime;
import org.joda.time.ZoneUtils;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.JUnit4;
import static org.junit.Assert.*;

/**
 * Test class for LenientDateTimeField.
 */
@RunWith(JUnit4.class)
public class LenientDateTimeFieldTest {

    private DateTimeField field;
    private Chronology chronology;

    @Before
    public void setUp() {
        field = DateTimeFieldType.dayOfMonth();
        chronology = DateTimeConstants.ISO_8601_DATE_TIME;
    }

    @Test
    public void testGetInstance() {
        DateTimeField lenientField = LenientDateTimeField.getInstance(field, chronology);
        assertNotNull(lenientField);
        assertTrue(lenientField instanceof LenientDateTimeField);
        DateTimeField strictField = new StrictDateTimeField(field, chronology);
        DateTimeField lenientFieldFromStrict = LenientDateTimeField.getInstance(strictField, chronology);
        assertEquals(lenientField, lenientFieldFromStrict);
    }

    @Test
    public void testIsLenient() {
        LenientDateTimeField lenientField = new LenientDateTimeField(field, chronology);
        assertTrue(lenientField.isLenient());
    }

    @Test(expected = IllegalArgumentException.class)
    public void testSetWithNullInstant() {
        LenientDateTimeField lenientField = new LenientDateTimeField(field, chronology);
        lenientField.set(null, DateTimeConstants.DAY_OF_MONTH);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testSetWithNullValue() {
        LenientDateTimeField lenientField = new LenientDateTimeField(field, chronology);
        lenientField.set(DateTimeConstants.MILLIS_PER_DAY, null);
    }

    @Test
    public void testSetWithinBounds() {
        LenientDateTimeField lenientField = new LenientDateTimeField(field, chronology);
        MutableDateTime dateTime = new MutableDateTime(DateTimeConstants.MINUTE_OF_DAY, DateTimeConstants.HOUR_OF_DAY, 0, 1, 1, 2000, 1, chronology);
        long instant = dateTime.getMillis();
        int value = get(instant);
        long result = lenientField.set(instant, value);
        assertEquals(instant, result);
    }

    @Test
    public void testSetOutOfBoundsAddition() {
        LenientDateTimeField lenientField = new LenientDateTimeField(field, chronology);
        MutableDateTime dateTime = new MutableDateTime(DateTimeConstants.MINUTE_OF_DAY, DateTimeConstants.HOUR_OF_DAY, 0, 1, 1, 2000, 1, chronology);
        long instant = dateTime.getMillis();
        int value = get(instant) + 1;
        long result = lenientField.set(instant, value);
        // The result should be the new value, not the original value with addition.
        // The exact value depends on the field and chronology.
        long expectedValue = getType().getMaximumValue(FieldUtils.getLocalMillis(instant, chronology)) + 1;
        assertEquals(expectedValue, result);
    }

    @Test
    public void testSetOutOfBoundsSubtraction() {
        LenientDateTimeField lenientField = new LenientDateTimeField(field, chronology);
        MutableDateTime dateTime = new MutableDateTime(DateTimeConstants.MINUTE_OF_DAY, DateTimeConstants.HOUR_OF_DAY, 0, 1, 1, 2000, 1, chronology);
        long instant = dateTime.getMillis();
        int value = get(instant) - 1;
        long result = lenientField.set(instant, value);
        // The result should be the new value, not the original value with subtraction.
        // The exact value depends on the field and chronology.
        long expectedValue = getType().getMinimumValue(FieldUtils.getLocalMillis(instant, chronology)) - 1;
        assertEquals(expectedValue, result);
    }

    @Test
    public void testSetWithWrappedField() {
        DateTimeField strictField = new StrictDateTimeField(field, chronology);
        LenientDateTimeField lenientField = LenientDateTimeField.getInstance(strictField, chronology);
        MutableDateTime dateTime = new MutableDateTime(DateTimeConstants.MINUTE_OF_DAY, DateTimeConstants.HOUR_OF_DAY, 0, 1, 1, 2000, 1, chronology);
        long instant = dateTime.getMillis();
        int value = get(instant) + 1;
        long result = lenientField.set(instant, value);
        // The result should be the new value, not the original value with addition.
        // The exact value depends on the field and chronology.
        long expectedValue = getType().getMaximumValue(FieldUtils.getLocalMillis(instant, chronology)) + 1;
        assertEquals(expectedValue, result);
    }

    @Test(expected = ArithmeticException.class)
    public void testSetWithOverflow() {
        LenientDateTimeField lenientField = new LenientDateTimeField(field, chronology);
        MutableDateTime dateTime = new MutableDateTime(DateTimeConstants.MINUTE_OF_DAY, DateTimeConstants.HOUR_OF_DAY, 0, 1, 1, 2000, 1, chronology);
        long instant = dateTime.getMillis();
        int value = getType().getMaximumValue(FieldUtils.getLocalMillis(instant, chronology)) + 1;
        lenientField.set(instant, value);
    }

    private int get(long instant) {
        return FieldUtils.get(field, new MutableDateTime(instant, chronology).toLocalDateTime(), null);
    }

    private DateTimeFieldType getType() {
        return (DateTimeFieldType) field;
    }
}