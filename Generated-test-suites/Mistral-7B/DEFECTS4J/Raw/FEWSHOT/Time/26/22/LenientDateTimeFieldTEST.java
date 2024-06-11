package org.joda.time.field;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;

import org.joda.time.Chronology;
import org.joda.time.DateTimeFieldType;
import org.joda.time.LocalDate;
import org.joda.time.LocalDateTime;
import org.joda.time.LocalTime;
import org.joda.time.MutableDateTime;
import org.joda.time.ZoneLib;
import org.joda.time.field.BaseLenientDateTimeField;
import org.joda.time.field.BaseStrictDateTimeField;
import org.joda.time.field.DateTimeField;
import org.joda.time.field.DateTimeFields;
import org.joda.time.field.FieldUtils;
import org.joda.time.format.DateTimeFormat;
import org.joda.time.chrono.BasicGregorianChronology;

public class LenientDateTimeFieldTest {

    private static final Chronology base = new BasicGregorianChronology();

    @Test
    public void testGetInstance() {
        DateTimeField strictField = DateTimeFields.minuteOfDay();
        DateTimeField lenientField1 = LenientDateTimeField.getInstance(strictField, base);
        DateTimeField lenientField2 = LenientDateTimeField.getInstance(lenientField1, base);
        assertEquals(lenientField1, lenientField2);

        DateTimeField strictField2 = new BaseStrictDateTimeField(DateTimeFieldType.minuteOfDay(), 1, base) {
            @Override
            public String getName() {
                return "Minute of Day (Strict)";
            }
        };
        DateTimeField lenientField3 = LenientDateTimeField.getInstance(strictField2, base);
        assertNotSame(strictField2, lenientField3);
        assertEquals(lenientField1.getClass(), lenientField3.getClass());

        DateTimeField nullField = null;
        assertNull(LenientDateTimeField.getInstance(nullField, base));
    }

    @Test
    public void testIsLenient() {
        DateTimeField strictField = DateTimeField.ofType(DateTimeFieldType.minuteOfDay());
        assertFalse(strictField.isLenient());

        LenientDateTimeField lenientField = new LenientDateTimeField(strictField, base);
        assertTrue(lenientField.isLenient());
    }

    @Test
    public void testSet() {
        DateTimeField field = DateTimeFields.minuteOfDay();
        LenientDateTimeField lenientField = new LenientDateTimeField(field, base);

        MutableDateTime dateTime = new MutableDateTime(2012, 1, 1, 0, 0, 0, 0, base);
        long instant = dateTime.getMillis();

        // Test valid values
        int value = 5;
        long expectedInstant = instant + (60 * 500); // 50 minutes
        long result = lenientField.set(instant, value);
        assertEquals(expectedInstant, result);

        // Test out of bounds values
        value = 120;
        expectedInstant = instant + (60 * 120 * 60 * 24 * 30); // 120 days
        result = lenientField.set(instant, value);
        assertEquals(expectedInstant, result);

        value = 121;
        assertNull(lenientField.set(instant, value));
    }

    @Test
    public void testSetWithNullDateTime() {
        DateTimeField field = DateTimeFields.minuteOfDay();
        LenientDateTimeField lenientField = new LenientDateTimeField(field, base);

        assertNull(lenientField.set(null, 5));
    }

    @Test
    public void testSetWithNullField() {
        LenientDateTimeField lenientField = new LenientDateTimeField(null, base);

        assertNull(lenientField.set(0, 5));
    }

    @Test(expected = UnsupportedOperationException.class)
    public void testSetWithMutableDateTime() {
        DateTimeField field = DateTimeFields.minuteOfDay();
        LenientDateTimeField lenientField = new LenientDateTimeField(field, base);

        MutableDateTime dateTime = new MutableDateTime(2012, 1, 1, 0, 0, 0, 0, base);
        lenientField.set(dateTime, 5);
    }

    @Test
    public void testAdd() {
        DateTimeField field = DateTimeFields.minuteOfDay();
        LenientDateTimeField lenientField = new LenientDateTimeField(field, base);

        MutableDateTime dateTime = new MutableDateTime(2012, 1, 1, 0, 0, 0, 0, base);
        long instant = dateTime.getMillis();

        int value = 5;
        long expectedInstant = instant + (60 * 500); // 50 minutes
        MutableDateTime resultDateTime = new MutableDateTime(instant);
        lenientField.add(resultDateTime, value);
        assertEquals(expectedInstant, resultDateTime.getMillis());

        // Test out of bounds values
        value = 120;
        long expectedInstant2 = instant + (60 * 120 * 60 * 24 * 30); // 120 days
        resultDateTime = new MutableDateTime(instant);
        lenientField.add(resultDateTime, value);
        assertEquals(expectedInstant2, resultDateTime.getMillis());
    }

    @Test
    public void testAddWithNullDateTime() {
        DateTimeField field = DateTimeFields.minuteOfDay();
        LenientDateTimeField lenientField = new LenientDateTimeField(field, base);

        MutableDateTime dateTime = null;
        lenientField.add(dateTime, 5);
        assertNull(dateTime);
    }

    @Test
    public void testGetName() {
        DateTimeField field = DateTimeFields.minuteOfDay();
        LenientDateTimeField lenientField = new LenientDateTimeField(field, base);
        assertEquals(field.getName(), lenientField.getName());

        BaseLenientDateTimeField lenientFieldBase = new BaseLenientDateTimeField(DateTimeFieldType.minuteOfDay(), 1, base) {
            @Override
            public String getName() {
                return "Minute of Day (Lenient)";
            }
        };
        assertEquals(lenientFieldBase.getName(), lenientField.getName());

        BaseStrictDateTimeField strictField = new BaseStrictDateTimeField(DateTimeFieldType.minuteOfDay(), 1, base) {
            @Override
            public String getName() {
                return "Minute of Day (Strict)";
            }
        };
        assertNotSame(strictField.getName(), lenientField.getName());
    }
}