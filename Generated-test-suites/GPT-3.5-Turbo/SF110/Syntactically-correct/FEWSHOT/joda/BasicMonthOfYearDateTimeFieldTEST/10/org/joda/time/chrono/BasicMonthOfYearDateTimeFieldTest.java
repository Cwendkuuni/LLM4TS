package org.joda.time.chrono;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

import org.junit.Before;
import org.junit.Test;
import org.joda.time.field.*;
import org.joda.time.*;

public class BasicMonthOfYearDateTimeFieldTest {
    private BasicMonthOfYearDateTimeField field;
    private BasicChronology chronology;

    @Before
    public void setUp() {
        chronology = new BasicChronology();
        field = new BasicMonthOfYearDateTimeField(chronology, 6);
    }

    @Test
    public void testGet() {
        long n = 1566879600000L;
        assertEquals(8, field.get(n));
        
        n = 1569879600000L;
        assertEquals(10, field.get(n));
    }

    @Test
    public void testAdd() {
        long n = 1566879600000L;
        long result = field.add(n, 3);
        assertEquals(1572543600000L, result);
        
        result = field.add(n, 0);
        assertEquals(n, result);
    }

    @Test
    public void testIsLeap() {
        long n = 1566879600000L;
        assertFalse(field.isLeap(n));
        
        n = 1569879600000L;
        assertTrue(field.isLeap(n));
    }

    @Test
    public void testGetLeapAmount() {
        long n = 1566879600000L;
        assertEquals(0, field.getLeapAmount(n));
        
        n = 1569879600000L;
        assertEquals(1, field.getLeapAmount(n));
    }

    @Test
    public void testGetMinimumValue() {
        assertEquals(1, field.getMinimumValue());
    }

    @Test
    public void testGetMaximumValue() {
        assertEquals(12, field.getMaximumValue());
    }
}