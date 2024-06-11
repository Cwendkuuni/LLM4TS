package org.joda.time.field.test;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertTrue;
import static org.junit.Assume.assumeNotNull;

import org.joda.time.Chronology;
import org.joda.time.DateTimeField;
import org.joda.time.DateTimeFields;
import org.joda.time.FieldUtils;
import org.joda.time.GregorianChronology;
import org.joda.time.LocalDateTime;
import org.joda.time.MinuteOfYear;
import org.joda.time.chrono.StandardChronologyFactory;

import org.junit.Before;
import org.junit.Test;

public class LenientDateTimeFieldTest {

    private static final Chronology DAYLIGHT_SAVING_CHRONOLOGY = new StandardChronologyFactory().getChronology("America/Los_Angeles");

    private DateTimeField lenientField;
    private DateTimeField strictField;

    @Before
    public void setUp() {
        strictField = new MinuteOfYear();
        lenientField = new LenientDateTimeField(strictField, GregorianChronology.getInstance());
    }

    @Test
    public void testGetInstance() {
        DateTimeField field = new MinuteOfYear();
        Chronology base = GregorianChronology.getInstance();
        DateTimeField lenientField = LenientDateTimeField.getInstance(field, base);
        assertNotNull(lenientField);
        assertEquals(field, lenientField.getWrappedField());
        assertEquals(base, lenientField.iBase);
    }

    @Test
    public void testLenientDateTimeFieldConstruction() {
        DateTimeField field = new MinuteOfYear();
        Chronology base = GregorianChronology.getInstance();
        LenientDateTimeField lenientField = new LenientDateTimeField(field, base);
        assertEquals(field, lenientField.getWrappedField());
        assertEquals(base, lenientField.iBase);
    }

    @Test
    public void testIsLenient() {
        assertTrue(lenientField.isLenient());
    }

    @Test
    public void testSet() {
        long instant = 1651154800000L; // Fri, 21 Jan 2016 12:00:00 GMT
        int value = 30; // 30 minutes
        long expectedInstant = 1651156400000L; // Fri, 21 Jan 2016 12:30:00 GMT
        long actualInstant = lenientField.set(instant, value);
        assertEquals(expectedInstant, actualInstant);
    }

    @Test
    public void testSetWithNegativeAndPositiveDifferences() {
        LenientDateTimeField lenientField = new LenientDateTimeField(new MinuteOfYear(), GregorianChronology.getInstance());
        long instant = 1651154800000L; // Fri, 21 Jan 2016 12:00:00 GMT
        // Test negative difference
        int value = -30; // 30 minutes backwards
        long expectedInstant = 1651151800000L; // Fri, 21 Jan 2016 11:30:00 GMT
        long actualInstant = lenientField.set(instant, value);
        assertEquals(expectedInstant, actualInstant);
        // Test positive difference
        value = 60; // 1 hour
        expectedInstant = 1651158400000L; // Fri, 21 Jan 2016 13:00:00 GMT
        actualInstant = lenientField.set(instant, value);
        assertEquals(expectedInstant, actualInstant);
    }

    @Test(expected = NullPointerException.class)
    public void testGetInstanceWithNullDateTimeField() {
        LenientDateTimeField.getInstance(null, GregorianChronology.getInstance());
    }

    @Test(expected = NullPointerException.class)
    public void testSetWithNullChronology() {
        LenientDateTimeField lenientField = new LenientDateTimeField(strictField, null);
        long instant = 1651154800000L; // Fri, 21 Jan 2016 12:00:00 GMT
        int value = 30; // 30 minutes
        lenientField.set(instant, value);
    }

    @Test
    public void testGetInstanceWithLenientDateTimeField() {
        DateTimeField field = new LenientDateTimeField(new MinuteOfYear(), GregorianChronology.getInstance());
        LenientDateTimeField lenientField = LenientDateTimeField.getInstance(field, GregorianChronology.getInstance());
        assertEquals(field, lenientField);
    }

    @Test
    public void testLenientDateTimeFieldConstructionWithNullWrappedField() {
        DateTimeField field = new DateTimeField(null, "Test", null) {
            @Override
            public int getValue(long instant) {
                return 0;
            }

            @Override
            public void setValue(long instant, int value) {
            }

            @Override
            public DateTimeField getWrappedField() {
                return null;
            }

            @Override
            public DateTimeField setWrapped(DateTimeField field) {
                return this;
            }
        };
        Chronology base = GregorianChronology.getInstance();
        try {
            new LenientDateTimeField(field, base);
        } catch (NullPointerException e) {
            // Expected result
        }
    }
}
//