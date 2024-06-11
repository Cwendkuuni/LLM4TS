package org.joda.time.base;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertThrows;
import static org.junit.Assert.assertTrue;

import org.joda.time.BaseDateTime;
import org.joda.time.Chronology;
import org.joda.time.DurationField;
import org.joda.time.DurationFieldType;
import org.joda.time.DurationFieldTypeint;
import org.joda.time.MutablePeriod;
import org.joda.time.Period;
import org.joda.time.PeriodType;
import org.joda.time.base.BaseSingleFieldPeriod;
import org.joda.time.field.FieldUtils;
import org.joda.time.field.FixedPeriod;

/**
 * Test cases for BaseSingleFieldPeriod class.
 */
public class BaseSingleFieldPeriodTest {

    /**
     * Test case for the BaseSingleFieldPeriod class with int value and days field type.
     */
    public void testBaseSingleFieldPeriodInt() {
        BaseSingleFieldPeriod period = new FixedPeriod(3, DurationFieldType.dayOfMonth());

        // Test basic functionality
        assertEquals("Period value is incorrect.", 3, period.getValue());
        assertEquals("Field type is incorrect.", DurationFieldType.dayOfMonth(), period.getFieldType());
        assertEquals("Period type is incorrect.", PeriodType.standard(), period.getPeriodType());
        assertEquals("Size is incorrect.", 1, period.size());
        assertEquals("Actual value is incorrect.", 3, period.getValue(0));
        assertEquals("Actual field type is incorrect.", DurationFieldType.dayOfMonth(), period.getFieldType(0));
        assertEquals("Actual period type is incorrect.", PeriodType.standard(), period.getPeriodType());

        // Test equals method
        BaseSingleFieldPeriod anotherPeriod = new FixedPeriod(3, DurationFieldType.dayOfMonth());
        assertTrue("Expected periods to be equal.", period.equals(anotherPeriod));
        assertFalse("Expected periods to be unequal.", period.equals(null));

        // Test hashCode method
        int periodHashCode = period.hashCode();
        int anotherPeriodHashCode = anotherPeriod.hashCode();
        int equalHashCode = periodHashCode; // Assign the same value for the test
        assertTrue("Expected periods to have the same hashcode.", periodHashCode == anotherPeriodHashCode);

        // Test compareTo method
        BaseSingleFieldPeriod smallerPeriod = new FixedPeriod(2, DurationFieldType.dayOfMonth());
        BaseSingleFieldPeriod largerPeriod = new FixedPeriod(4, DurationFieldType.dayOfMonth());
        int comparisonResult = period.compareTo(smallerPeriod);
        assertTrue("Expected period to be greater.", comparisonResult > 0);
        comparisonResult = period.compareTo(largerPeriod);
        assertTrue("Expected period to be smaller.", comparisonResult < 0);
        comparisonResult = smallerPeriod.compareTo(period);
        assertTrue("Expected smaller period to be smaller.", comparisonResult < 0);
        comparisonResult = largerPeriod.compareTo(period);
        assertTrue("Expected larger period to be greater.", comparisonResult > 0);

        // Test get method
        assertEquals("Expected to get 3 days.", 3, period.get(DurationFieldType.dayOfMonth()));
        assertNull("Expected null when querying unsupported field type.", period.get(DurationFieldType.hourOfDay()));

        // Test isSupported method
        assertTrue("Expected to support dayOfMonth field type.", period.isSupported(DurationFieldType.dayOfMonth()));
        assertFalse("Expected not to support hourOfDay field type.", period.isSupported(DurationFieldType.hourOfDay()));

        // Test add method on a MutablePeriod
        MutablePeriod mutablePeriod = new MutablePeriod();
        mutablePeriod.add(period);
        assertEquals("Expected to get the same period after adding it to a MutablePeriod.", period, mutablePeriod);

        // Test standardPeriodIn method
        long millisPerUnit = 1000 * 60 * 60 * 24; // 1 day in milliseconds
        assertEquals("Expected 3 days to be converted to 3 days in hours.", 3 * 24, BaseSingleFieldPeriod.standardPeriodIn(period, millisPerUnit));

        // Test between method for instants
        ReadableInstant start = new BaseDateTime(2023, 1, 1, 0, 0, 0, 0, ISOChronology.getInstanceUTC()).toInstant();
        ReadableInstant end = new BaseDateTime(2023, 1, 3, 0, 0, 0, 0, ISOChronology.getInstanceUTC()).toInstant();
        assertEquals("Expected period to have a value of 3 days.", 3, BaseSingleFieldPeriod.between(start, end, DurationFieldType.dayOfMonth()));

        // Test between method for partials
        ReadablePartial startPartial = new BaseDateTime(2023, 1, 1, 0, 0, 0, 0, ISOChronology.getInstanceUTC());
        ReadablePartial endPartial = new BaseDateTime(2023, 1, 3, 0, 0, 0, 0, ISOChronology.getInstanceUTC());
        assertEquals("Expected period to have a value of 3 days.", 3, BaseSingleFieldPeriod.between(startPartial, endPartial, period));

        // Test toPeriod method
        Period periodToPeriod = period.toPeriod();
        assertEquals("Expected period to have the same value after conversion to Period.", 3, periodToPeriod.getDays());

        // Test toMutablePeriod method
        MutablePeriod mutablePeriodToPeriod = new MutablePeriod();
        mutablePeriodToPeriod.add(period);
        assertEquals("Expected period to have the same value after conversion to MutablePeriod.", 3, mutablePeriodToPeriod.getDays());

        // Test exception cases
        assertThrows("Expected IllegalArgumentException when instantiating with zero argument.", IllegalArgumentException.class, () -> new BaseSingleFieldPeriod(0));

        // Test exception cases
        assertThrows("Expected IllegalArgumentException when instantiating with a non supported field type.", IllegalArgumentException.class, () -> new BaseSingleFieldPeriod(new FixedDuration(3, DurationFieldTypeType.hourOfDay()), DurationFieldType.dayOfMonth()));

        // Test exception cases
        assertThrows("Expected IllegalArgumentException when instantiating with a negative value.", IllegalArgumentException.class, () -> new FixedPeriod(-3, DurationFieldType.dayOfMonth()));

        // Test exception cases
        assertThrows("Expected IllegalArgumentException when instantiating with a non supported period type.", IllegalArgumentException.class, () -> new FixedPeriod(3, new DurationType(), ISOChronology.getInstanceUTC()));
    }
}