package org.joda.time.field;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

import org.joda.time.Chronology;
import org.joda.time.DateTimeField;
import org.joda.time.DateTimeUtils;
import org.joda.time.LocalDate;
import org.joda.time.LocalDateTime;
import org.joda.time.LocalTime;
import org.joda.time.ReadablePartial;
import org.joda.time.chrono.ISOChronology;
import org.joda.time.chrono.LenientChronology;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;

public class LenientDateTimeFieldTest {
    private Chronology base;
    private DateTimeField field;

    @Before
    public void setUp() {
        base = LenientChronology.getInstance(ISOChronology.getInstanceUTC());
        field = new LenientDateTimeField(new ISOChronology().dayOfWeek(), base);
    }

    @After
    public void tearDown() {
        base = null;
        field = null;
    }

    @Test
    public void testGet() {
        LocalDateTime dt = new LocalDateTime(2019, 1, 1, 12, 0, 0);
        assertEquals(2, field.get(dt));

        // Test out of bounds values
        assertEquals(3, field.get(dt.minusDays(7))); // -5 % 7 = 3
        assertEquals(3, field.get(dt.plusDays(7))); // 9 % 7 = 3
    }

    @Test
    public void testSet() {
        LocalDateTime dt = new LocalDateTime(2019, 1, 1, 12, 0, 0);

        // Set with valid value
        LocalDateTime newDt = field.set(dt, 5);
        assertEquals(5, field.get(newDt));

        // Set with out of bounds value
        newDt = field.set(dt, 8);
        assertEquals(1, field.get(newDt));

        // Set with negative value
        newDt = field.set(dt, -3);
        assertEquals(4, field.get(newDt));
    }

    @Test
    public void testIsLenient() {
        assertTrue(field.isLenient());
    }

    @Test
    public void testGetInstance() {
        DateTimeField strictField = new LenientDateTimeField(new ISOChronology().dayOfWeek(), base);
        DateTimeField lenientField = LenientDateTimeField.getInstance(strictField, base);

        assertTrue(lenientField instanceof LenientDateTimeField);
        assertEquals(strictField, lenientField);

        lenientField = LenientDateTimeField.getInstance(null, base);
        assertNull(lenientField);
    }
}