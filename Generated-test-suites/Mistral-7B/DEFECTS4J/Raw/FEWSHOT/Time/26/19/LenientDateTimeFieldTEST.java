package org.joda.time.field;

import static org.junit.Assert.assertEquals;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

import org.joda.time.Chronology;
import org.joda.time.DateTimeField;
import org.joda.time.DateTimeFieldType;
import org.joda.time.DurationFieldType;
import org.joda.time.LocalDateTime;
import org.joda.time.LocalTime;
import org.joda.time.MutableDateTime;
import org.joda.time.MutableDateTimeZone;
import org.joda.time.field.FieldUtils;
import org.joda.time.format.DateTimeFormat;
import org.joda.time.format.DateTimeFormatter;
import org.junit.Before;
import org.junit.Test;

public class LenientDateTimeFieldTest {

    private static final DateTimeFieldType DAY_OF_MONTH = DateTimeFieldType.dayOfMonth();
    private static final DateTimeFieldType HOUR_OF_DAY = DateTimeFieldType.hourOfDay();
    private static final DateTimeFieldType MINUTE_OF_HOUR = DateTimeFieldType.minuteOfHour();
    private static final DateTimeFieldType SECOND_OF_MINUTE = DateTimeFieldType.secondOfMinute();
    private static final DateTimeFieldType MILLISECOND_OF_SECOND = DateTimeFieldType.millisOfSecond();

    private Chronology utc = mock(Chronology.class);
    private Chronology base = mock(Chronology.class);
    private DateTimeField strictField;
    private DateTimeField lenientField;

    @Before
    public void setUp() {
        when(base.getID()).thenReturn("BaseID");
        when(utc.getID()).thenReturn("UTCID");
        when(utc.getZone()).thenReturn(mock(MutableDateTimeZone.class));

        strictField = new StrictDateTimeField(DAY_OF_MONTH, 30, base);
        lenientField = LenientDateTimeField.getInstance(strictField, base);
    }

    @Test
    public void testInstance() {
        assertEquals(base, lenientField.getBaseDateTime());
        assertEquals(STRICT, getFieldType(strictField));
        assertEquals(LENIENT, getFieldType(lenientField));
    }

    @Test
    public void testIsLenient() {
        assertTrue(lenientField.isLenient());
    }

    @Test
    public void testGetBase() {
        assertEquals(base, lenientField.getBaseDateTime());
    }

    @Test
    public void testGetType() {
        assertEquals(DAY_OF_MONTH, lenientField.getType());
    }

    @Test
    public void testGetID() {
        assertEquals(base.getID() + "_LENIENT", lenientField.getID());
    }

    @Test
    public void testGetWrappedField() {
        assertEquals(strictField, lenientField.getWrappedField());
    }

    @Test
    public void testGetName() {
        assertEquals(strictField.getName() + "_LENIENT", lenientField.getName());
    }

    @Test
    public void testGetPossibleValues() {
        assertEquals(strictField.getPossibleValues(), lenientField.getPossibleValues());
    }

    @Test
    public void testGetMinimumValue() {
        assertEquals(strictField.getMinimumValue(), lenientField.getMinimumValue());
    }

    @Test
    public void testGetMaximumValue() {
        assertEquals(strictField.getMaximumValue(), lenientField.getMaximumValue());
    }

    @Test
    public void testGetMinimumValueInclusive() {
        assertEquals(strictField.isMinimumValueInclusive(), lenientField.isMinimumValueInclusive());
    }

    @Test
    public void testGetMaximumValueInclusive() {
        assertEquals(strictField.isMaximumValueInclusive(), lenientField.isMaximumValueInclusive());
    }

    @Test
    public void testGetFixedValue() {
        assertEquals(strictField.getFixedValue(), lenientField.getFixedValue());
    }

    @Test
    public void testAdd() {
        MutableDateTime dateTime = new MutableDateTime(LocalDateTime.now(), utc);
        LocalDateTime expected = new LocalDateTime(LocalDate.of(2023, 1, 31), LocalTime.MIDNIGHT);

        long result = lenientField.set(dateTime.getMillis(), 31);
        assertEquals(expected.toDate().getTime(), result);
        assertEquals(expected.getDayOfMonth(), lenientField.get(result));
    }

    @Test
    public void testAddWithInvalidValue() {
        MutableDateTime dateTime = new MutableDateTime(LocalDateTime.now(), utc);
        MutableDateTime expected = new MutableDateTime(LocalDateTime.now(), utc);

        long result = lenientField.set(dateTime.getMillis(), 32);
        assertEquals(expected.getMillis(), result);
        assertEquals(lenientField.get(dateTime.getMillis()), lenientField.get(result));
    }

    @Test
    public void testAddWithNegativeValue() {
        MutableDateTime dateTime = new MutableDateTime(LocalDateTime.now(), utc);
        MutableDateTime expected = new MutableDateTime(LocalDateTime.now(), utc);

        long result = lenientField.set(dateTime.getMillis(), -1);
        assertEquals(expected.getMillis(), result);
        assertEquals(lenientField.get(dateTime.getMillis()), lenientField.get(result));
    }

    @Test
    public void testAddWithValueGreaterThanMaximum() {
        MutableDateTime dateTime = new MutableDateTime(LocalDateTime.now(), utc);
        MutableDateTime expected = new MutableDateTime(LocalDateTime.now(), utc);

        long result = lenientField.set(dateTime.getMillis(), Integer.MAX_VALUE + 1);
        assertEquals(expected.getMillis(), result);
        assertEquals(lenientField.get(dateTime.getMillis()), lenientField.get(result));
    }

    @Test
    public void testAddWithValueLessThanMinimum() {
        MutableDateTime dateTime = new MutableDateTime(LocalDateTime.now(), utc);
        MutableDateTime expected = new MutableDateTime(LocalDateTime.now(), utc);

        long result = lenientField.set(dateTime.getMillis(), Integer.MIN_VALUE - 1);
        assertEquals(expected.getMillis(), result);
        assertEquals(lenientField.get(dateTime.getMillis()), lenientField.get(result));
    }

    @Test
    public void testAddWithDateTimeField() {
        MutableDateTime dateTime1 = new MutableDateTime(LocalDateTime.now(), utc);
        MutableDateTime dateTime2 = new MutableDateTime(LocalDateTime.now(), utc);
        MutableDateTime expected = new MutableDateTime(LocalDateTime.now(), utc);

        long result = lenientField.set(dateTime1.getMillis(), 1);
        lenientField.set(result, 1);

        long result2 = lenientField.set(dateTime2.getMillis(), 1);
        lenientField.set(result2, 1);

        assertEquals(expected.getMillis(), result + result2);
        assertEquals(lenientField.get(result + result2), lenientField.get(result) + lenientField.get(result2));
    }

    @Test
    public void testAddWithDurationField() {
        MutableDateTime dateTime = new MutableDateTime(LocalDateTime.now(), utc);
        LocalDateTime expected = new LocalDateTime(LocalDate.of(2023, 1, 31), LocalTime.MIDNIGHT);
        LocalTime duration = new LocalTime(1, 1, 1);
        DurationFieldType durationType = DurationFieldType.standardHours();

        long result = lenientField.set(dateTime.getMillis(), 24 * 2);
        assertEquals(expected.toDate().getTime(), result);
        assertEquals(expected.getDayOfMonth(), lenientField.get(result));
        assertEquals(expected.getHourOfDay(), 0);
        assertEquals(expected.getMinuteOfHour(), 0);
        assertEquals(expected.getSecondOfMinute(), 0);
        assertEquals(expected.getMillisOfSecond(), 0);

        long durationMillis = durationType.getLength(expected.toLocalDate().toLocalDate());
        assertEquals(-durationMillis, FieldUtils.safeSubtract(result, dateTime.getMillis()));
    }

    @Test
    public void testToLocalField() {
        DateTimeFormatter formatter = DateTimeFormat.forPattern("yyyy-MM-dd HH:mm:ss.SSS");
        LocalDateTime expected = new LocalDateTime(LocalDate.of(2023, 1, 31), LocalTime.MIDNIGHT);
        String format = "2023-01-31 00:00:01.001";

        MutableDateTime dateTime = new MutableDateTime(LocalDateTime.parse(format, formatter), utc);
        DateTimeField lenientField = LenientDateTimeField.getInstance(HOUR_OF_DAY, 23, base);

        long result = lenientField.set(dateTime.getMillis(), 23);
        assertEquals(expected.getMillis(), result);
        assertEquals(expected.getHourOfDay(), lenientField.get(result));
    }

    private static int getFieldType(DateTimeField field) {
        if (field instanceof StrictDateTimeField) {
            return STRICT;
        }
        if (field instanceof LenientDateTimeField) {
            return LENIENT;
        }
        throw new IllegalArgumentException("Unknown field type: " + field.getClass().getName());
    }
}