package org.joda.time.base;

import static org.junit.Assert.*;
import java.io.*;
import org.joda.time.chrono.*;
import org.joda.time.field.*;
import org.joda.time.*;
import org.junit.Before;
import org.junit.Test;

public class BaseSingleFieldPeriodTest {

    private static final String CLASS_NAME = "BaseSingleFieldPeriod";

    private BaseSingleFieldPeriod obj;

    @Before
    public void setUp() {
        obj = new BaseSingleFieldPeriod(0);
    }

    @Test
    public void testGetFieldType() {
        assertEquals(null, obj.getFieldType());
    }

    @Test
    public void testGetPeriodType() {
        assertEquals(null, obj.getPeriodType());
    }

    @Test
    public void testSize() {
        assertEquals(1, obj.size());
    }

    @Test(expected = IndexOutOfBoundsException.class)
    public void testGetFieldTypeWithIndexOutOfBounds() {
        obj.getFieldType(1);
    }

    @Test(expected = IndexOutOfBoundsException.class)
    public void testGetValueWithIndexOutOfBounds() {
        obj.getValue(1);
    }

    @Test
    public void testGet() {
        assertEquals(0, obj.get(null));
    }

    @Test
    public void testIsSupported() {
        assertEquals(false, obj.isSupported(null));
    }

    @Test
    public void testToPeriod() {
        assertNotNull(obj.toPeriod());
    }

    @Test
    public void testToMutablePeriod() {
        assertNotNull(obj.toMutablePeriod());
    }

    @Test
    public void testEquals() {
        assertTrue(obj.equals(obj));
        assertFalse(obj.equals(null));
        assertFalse(obj.equals(new Object()));
        assertFalse(obj.equals(new BaseSingleFieldPeriod(1)));
    }

    @Test
    public void testHashCode() {
        assertEquals(0, obj.hashCode());
    }

    @Test(expected = ClassCastException.class)
    public void testCompareToWithDifferentClass() {
        obj.compareTo(new ClassCastException());
    }

    @Test
    public void testCompareToWithGreaterValue() {
        assertEquals(1, obj.compareTo(new BaseSingleFieldPeriod(1)));
    }

    @Test
    public void testCompareToWithSmallerValue() {
        assertEquals(-1, obj.compareTo(new BaseSingleFieldPeriod(-1)));
    }

    @Test
    public void testCompareToWithEqualValue() {
        assertEquals(0, obj.compareTo(new BaseSingleFieldPeriod(0)));
    }
}