package org.joda.time.field;

import org.joda.time.Chronology;

import org.joda.time.DateTimeField;

import org.joda.time.chrono.LenientChronology;

import org.joda.time.DateTimeFieldType;

import static org.junit.Assert.assertEquals;

import static org.junit.Assert.assertNull;

import org.junit.Test;

public class LenientDateTimeFieldTest {

    @Test

    public void testGetInstance_returnsNull() {

        assertNull(LenientDateTimeField.getInstance(null, null));

    }

    @Test

    public void testGetInstance_returnsUnmodifiedField() {

        Chronology base = new LenientChronology();

        DateTimeField field = base.dayOfMonth();

        assertEquals(field, LenientDateTimeField.getInstance(field, base));

    }

    @Test

    public void testGetInstance_returnsLenientField() {

        Chronology base = new LenientChronology();

        DateTimeField field = base.dayOfMonth().withMaximumValue();

        assertEquals(LenientDateTimeField.class, LenientDateTimeField.getInstance(field, base).getClass());

    }

    @Test

    public void testIsLenient() {

        Chronology base = new LenientChronology();

        DateTimeField field = base.dayOfMonth().withMaximumValue();

        LenientDateTimeField lenientField = LenientDateTimeField.getInstance(field, base);

        assertEquals(true, lenientField.isLenient());

    }

    @Test

    public void testSet() {

        Chronology base = new LenientChronology();

        DateTimeField field = base.dayOfMonth().withMaximumValue();

        LenientDateTimeField lenientField = LenientDateTimeField.getInstance(field, base);

        long instant = 1577836800000L;

        int value = 5;

        long expected = 1578398399999L;

        assertEquals(expected, lenientField.set(instant, value));

    }

}