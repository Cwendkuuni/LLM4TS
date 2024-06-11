package org.joda.time.field;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotSame;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

import org.joda.time.Chronology;
import org.joda.time.DateTimeFieldType;
import org.joda.time.DurationFieldType;
import org.joda.time.FieldUtils;
import org.joda.time.LocalDateTime;
import org.joda.time.LocalTime;
import org.joda.time.MutableDateTime;
import org.joda.time.MutableLocalDateTime;
import org.joda.time.DateTimeConstants;
import org.joda.time.ZoneUtils;
import org.joda.time.field.BaseDateTimeField;
import org.joda.time.field.DateTimeFields;
import org.joda.time.field.FixedPeriod;
import org.joda.time.field.LenientDateTimeField;
import org.joda.time.field.StrictDateTimeField;
import org.joda.time.chrono.ISOChronology;
import org.junit.Before;
import org.junit.Test;

public class LenientDateTimeFieldTest {

    private Chronology base;
    private DateTimeField field;
    private DateTimeField lenientField;

    @Before
    public void setup() {
        base = new ISOChronology();
        field = DateTimeFields.minuteOfHour();
        lenientField = LenientDateTimeField.getInstance(field, base);
    }

    @Test
    public void testGetInstance() {
        assertNotSame(field, lenientField);
        assertSame(field.getWrappedField(), lenientField.getWrappedField());
    }

    @Test
    public void testIsLenient() {
        assertTrue(lenientField.isLenient());
    }

    @Test
    public void testSet() {
        MutableDateTime dateTime = new MutableDateTime(base, DateTimeConstants.MINUTE_OF_DAY_1);
        MutableLocalDateTime localDateTime = new MutableLocalDateTime(base, DateTimeConstants.MINUTE_OF_HOUR_0, 0, DateTimeConstants.HOUR_OF_DAY_1, 0);

        long initialInstant = dateTime.getMillis();
        long initialLocalInstant = localDateTime.getMillis();

        int value = 100;
        long newInstant = lenientField.set(initialInstant, value);
        long newLocalInstant = localDateTime.plus(new FixedPeriod(value, DateTimeFieldType.minutes()));

        assertEquals(newLocalInstant, newInstant);
        assertEquals(initialInstant + value * 60 * 1000, newInstant);
    }

    @Test
    public void testSetWithWrappedField() {
        DateTimeField wrappedField = BaseDateTimeField.instance(DateTimeFieldType.dayOfYear(), 20);
        DateTimeField lenientWrappedField = LenientDateTimeField.getInstance(wrappedField, base);
        MutableDateTime dateTime = new MutableDateTime(base, 2016, DateTimeConstants.JANUARY, 1, 2, 3, 0, 0);

        long initialInstant = dateTime.getMillis();
        int value = 50;
        long newInstant = lenientWrappedField.set(initialInstant, value);

        assertEquals(initialInstant + value * DateTimeConstants.DAYS_PER_YEAR, newInstant);
    }

    @Test
    public void testSetWithNull() {
        assertNull(lenientField.set(null, 0));
    }

    @Test
    public void testSetWithChronology() {
        Chronology otherChronology = mock(Chronology.class);
        DateTimeField lenientFieldWithOtherChronology = LenientDateTimeField.getInstance(field, otherChronology);
        MutableDateTime dateTime = new MutableDateTime(base, DateTimeConstants.MINUTE_OF_DAY_1);

        long initialInstant = dateTime.getMillis();
        int value = 100;
        long newInstant = lenientFieldWithOtherChronology.set(initialInstant, value);

        // lenient field with different chronology should behave as a new field
        assertNotSame(initialInstant, newInstant);
    }

    @Test
    public void testSetWithUTC() {
        Chronology utcChronology = ISOChronology.UTC;
        DateTimeField lenientFieldWithUTC = LenientDateTimeField.getInstance(field, utcChronology);
        MutableDateTime utcDateTime = new MutableDateTime(utcChronology, DateTimeConstants.MINUTE_OF_DAY_1);

        long initialInstant = utcDateTime.getMillis();
        int value = 100;
        long newInstant = lenientFieldWithUTC.set(initialInstant, value);

        assertEquals(initialInstant + value * 60 * 1000, newInstant);
    }

    @Test
    public void testSetWithOffset() {
        Chronology offsetChronology = ZoneUtils.forOffsetHours(1);
        DateTimeField lenientFieldWithOffset = LenientDateTimeField.getInstance(field, offsetChronology);
        MutableDateTime offsetDateTime = new MutableDateTime(offsetChronology, DateTimeConstants.MINUTE_OF_DAY_1);

        long initialInstant = offsetDateTime.getMillis();
        int value = 100;
        long newInstant = lenientFieldWithOffset.set(initialInstant, value);

        long expectedNewInstant = initialInstant + value * 60 * 1000 + (1000 * 60);
        assertEquals(expectedNewInstant, newInstant);
    }

    @Test
    public void testSetWithDST() {
        Chronology dstChronology = ZoneUtils.forID("Europe/London");
        DateTimeField lenientFieldWithDST = LenientDateTimeField.getInstance(field, dstChronology);
        MutableDateTime dstDateTime = new MutableDateTime(dstChronology, DateTimeConstants.MINUTE_OF_DAY_1);

        long initialInstant = dstDateTime.getMillis();
        int value = 100;
        long newInstant = lenientFieldWithDST.set(initialInstant, value);

        long expectedNewInstant = initialInstant + value * 60 * 1000 + (1000 * 60 * (dstChronology.getOffsetAt(initialInstant) / 60));
        assertEquals(expectedNewInstant, newInstant);
    }

    @Test
    public void testSetWithWrongChronology() {
        Chronology wrongChronology = mock(Chronology.class);
        DateTimeField lenientFieldWithWrongChronology = LenientDateTimeField.getInstance(field, wrongChronology);
        MutableDateTime dateTime = new MutableDateTime(base, DateTimeConstants.MINUTE_OF_DAY_1);

        long initialInstant = dateTime.getMillis();
        int value = 100;
        long newInstant = lenientFieldWithWrongChronology.set(initialInstant, value);

        // lenient field with different chronology should behave as a new field
        assertNotSame(initialInstant, newInstant);
    }

    @Test
    public void testSetWithUnsupportedChronology() {
        Chronology unsupportedChronology = mock(Chronology.class);
        DateTimeField lenientFieldWithUnsupportedChronology = LenientDateTimeField.getInstance(field, unsupportedChronology);

        assertEquals(Long.MIN_VALUE, lenientFieldWithUnsupportedChronology.set(0, 0));
    }

    @Test
    public void testSetWithTooLargeValue() {
        MutableDateTime dateTime = new MutableDateTime(base, DateTimeConstants.MINUTE_OF_DAY_1);

        long initialInstant = dateTime.getMillis();
        int value = Integer.MAX_VALUE + 1;
        long newInstant = lenientField.set(initialInstant, value);

        assertNotSame(initialInstant, newInstant);
    }

    @Test
    public void testSetWithTooSmallValue() {
        MutableDateTime dateTime = new MutableDateTime(base, DateTimeConstants.MINUTE_OF_DAY_1);

        long initialInstant = dateTime.getMillis();
        int value = Integer.MIN_VALUE;
        long newInstant = lenientField.set(initialInstant, value);

        assertNotSame(initialInstant, newInstant);
    }

    @Test
    public void testSetWithZeroValue() {
        MutableDateTime dateTime = new MutableDateTime(base, DateTimeConstants.MINUTE_OF_DAY_1);

        long initialInstant = dateTime.getMillis();
        long newInstant = lenientField.set(initialInstant, 0);

        assertEquals(initialInstant, newInstant);
    }

    @Test
    public void testSetWithZeroValueWithDifferentChronology() {
        Chronology otherChronology = mock(Chronology.class);
        DateTimeField lenientFieldWithOtherChronology = LenientDateTimeField.getInstance(field, otherChronology);
        MutableDateTime dateTime = new MutableDateTime(base, DateTimeConstants.MINUTE_OF_DAY_1);

        long initialInstant = dateTime.getMillis();
        long newInstant = lenientFieldWithOtherChronology.set(initialInstant, 0);

        // lenient field with different chronology should behave as a new field
        assertNotSame(initialInstant, newInstant);
    }

    @Test
    public void testSetWithNegativeValue() {
        MutableDateTime dateTime = new MutableDateTime(base, DateTimeConstants.MINUTE_OF_DAY_1);

        long initialInstant = dateTime.getMillis();
        int value = -1;
        long newInstant = lenientField.set(initialInstant, value);

        assertNotSame(initialInstant, newInstant);
    }

    @Test
    public void testSetWithNegativeValueWithDifferentChronology() {
        Chronology otherChronology = mock(Chronology.class);
        DateTimeField lenientFieldWithOtherChronology = LenientDateTimeField.getInstance(field, otherChronology);
        MutableDateTime dateTime = new MutableDateTime(base, DateTimeConstants.MINUTE_OF_DAY_1);

        long initialInstant = dateTime.getMillis();
        int value = -1;
        long newInstant = lenientFieldWithOtherChronology.set(initialInstant, value);

        // lenient field with different chronology should behave as a new field
        assertNotSame(initialInstant, newInstant);
    }

    @Test
    public void testSetWithOverflowValue() {
        Chronology chronology = mock(Chronology.class);
        when(chronology.getZone()).thenReturn(ZoneUtils.forOffsetHours(DateTimeConstants.DAYS_PER_YEAR * 10));
        DateTimeField lenientFieldWithOverflowChronology = LenientDateTimeField.getInstance(field, chronology);

        MutableDateTime dateTime = new MutableDateTime(chronology, DateTimeConstants.MINUTE_OF_DAY_1);
        long initialInstant = dateTime.getMillis();
        int value = Integer.MAX_VALUE + 1;
        long newInstant = lenientFieldWithOverflowChronology.set(initialInstant, value);

        // lenient field with overflowing chronology should wrap around and return valid value
        long expectedNewInstant = initialInstant + value * 60 * 1000 - (1000 * 60 * DateTimeConstants.DAYS_PER_YEAR * 10);
        assertEquals(expectedNewInstant, newInstant);
    }

    @Test
    public void testSetWithUnderflowValue() {
        Chronology chronology = mock(Chronology.class);
        when(chronology.getZone()).thenReturn(ZoneUtils.forOffsetHours(-DateTimeConstants.DAYS_PER_YEAR * 10));
        DateTimeField lenientFieldWithUnderflowChronology = LenientDateTimeField.getInstance(field, chronology);

        MutableDateTime dateTime = new MutableDateTime(chronology, DateTimeConstants.MINUTE_OF_DAY_1);
        long initialInstant = dateTime.getMillis();
        int value = Integer.MIN_VALUE;
        long newInstant = lenientFieldWithUnderflowChronology.set(initialInstant, value);

        // lenient field with underflowing chronology should wrap around and return valid value
        long expectedNewInstant = initialInstant + value * 60 * 1000 + (1000 * 60 * DateTimeConstants.DAYS_PER_YEAR * 10);
        assertEquals(expectedNewInstant, newInstant);
    }
}