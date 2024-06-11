package org.example;

import static org.junit.Assert.*;
import org.junit.Before;
import org.junit.Test;
import org.joda.time.*;
import static org.joda.time.field.FieldUtils.*;
import static org.joda.time.format.DateTimeFormat.*;
import static org.joda.time.format.DateTimeFormatter.*;

public class LenientDateTimeFieldTest {

    DateTimeField lenientField;
    LocalDateTime dateTime;

    @Before
    public void setUp() {
        lenientField = LenientDateTimeField.getInstance(new DateTimeField(1) {
            @Override
            public boolean isLenient() {
                return false;
            }
        }, Chronology.UTC);
        dateTime = new LocalDateTime(2016, 1, 23, 15, 20, 0);
    }

    @Test
    public void testIsLenient() {
        assertTrue(lenientField.isLenient());
    }

    @Test
    public void testSet_ValidInput() {
        long instant = dateTime.toInstant().getMillis();
        int value = 1;

        long expected = instant + 60_000; // 1 minute
        long result = lenientField.set(instant, value);

        assertEquals(expected, result);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testSet_OutOfBoundsInstants() {
        long instant = Long.MIN_VALUE; // Negative value
        int value = 1;

        lenientField.set(instant, value);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testSet_OutOfBoundsValues() {
        long instant = dateTime.toInstant().getMillis();
        int value = Integer.MAX_VALUE;

        lenientField.set(instant, value);
    }

    @Test(expected = NullPointerException.class)
    public void testSet_NullInstant() {
        int value = 1;

        lenientField.set(null, value);
    }

    @Test(expected = NullPointerException.class)
    public void testSet_NullValue() {
        long instant = dateTime.toInstant().getMillis();

        lenientField.set(instant, null);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testSet_NullChronology() {
        long instant = dateTime.toInstant().getMillis();
        int value = 1;

        lenientField = LenientDateTimeField.getInstance(lenientField, null);
        lenientField.set(instant, value);
    }

    @Test
    public void testGetInstance() {
        DateTimeField lenientField2 = LenientDateTimeField.getInstance(lenientField, Chronology.UTC);

        assertSame(lenientField, lenientField2);
    }

    @Test
    public void testGetWrappedField() {
        DateTimeField wrappedField = lenientField.getWrappedField();

        assertNotNull(wrappedField);
        assertFalse(wrappedField.isLenient());
    }
}