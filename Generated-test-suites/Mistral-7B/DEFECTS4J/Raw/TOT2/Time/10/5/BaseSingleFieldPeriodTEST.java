package org.joda.time.base;

import org.junit.Test;
import static org.junit.Assert.*;

import java.io.Serializable;
import java.util.Date;

import org.joda.time.Chronology;
import org.joda.time.DateTimeUtils;
import org.joda.time.DurationField;
import org.joda.time.DurationFieldType;
import org.joda.time.MutablePeriod;
import org.joda.time.Period;
import org.joda.time.PeriodType;
import org.joda.time.ReadableInstant;
import org.joda.time.ReadablePartial;
import org.joda.time.ReadablePeriod;
import org.joda.time.chrono.ISOChronology;
import org.joda.time.field.FieldUtils;

/**
 * Test cases for BaseSingleFieldPeriod class.
 */
public class BaseSingleFieldPeriodTest {

    /**
     * Test constructor.
     */
    @Test
    public void testConstructor() {
        // Create a BaseSingleFieldPeriod instance
        BaseSingleFieldPeriod period = new BaseSingleFieldPeriod(5);
        // Test getters
        assertEquals(5, period.getValue());
        DurationFieldType fieldType = period.getFieldType();
        assertTrue(fieldType instanceof DurationFieldType);
        PeriodType periodType = period.getPeriodType();
        assertTrue(periodType instanceof PeriodType);
        // Test size
        assertEquals(1, period.size());
        // Test equals
        BaseSingleFieldPeriod equalPeriod = new BaseSingleFieldPeriod(5);
        assertTrue(period.equals(equalPeriod));
        BaseSingleFieldPeriod notEqualPeriod = new BaseSingleFieldPeriod(6);
        assertFalse(period.equals(notEqualPeriod));
        Object notBaseSingleFieldPeriod = new Object();
        assertFalse(period.equals(notBaseSingleFieldPeriod));
        // Test hashCode
        int periodHashCode = period.hashCode();
        int equalPeriodHashCode = equalPeriod.hashCode();
        assertEquals(periodHashCode, equalPeriodHashCode);
        // Test compareTo
        BaseSingleFieldPeriod smallerPeriod = new BaseSingleFieldPeriod(4);
        assertTrue(period.compareTo(smallerPeriod) > 0);
        BaseSingleFieldPeriod largerPeriod = new BaseSingleFieldPeriod(6);
        assertTrue(period.compareTo(largerPeriod) < 0);
        // Test get
        DurationField field = DateTimeUtils.getDurationFieldType("days");
        int days = period.get(field);
        assertEquals(5, days);
        // Test isSupported
        assertTrue(period.isSupported(field));
        DurationFieldType notSupportedField = DateTimeUtils.getDurationFieldType("months");
        assertFalse(period.isSupported(notSupportedField));
        // Test toPeriod
        Period periodInstance = period.toPeriod();
        assertEquals(5, periodInstance.getYears());
        assertEquals(0, periodInstance.getMonths());
        assertEquals(5, periodInstance.getDays());
        assertEquals(PeriodType.standard(), periodInstance.getPeriodType());
        // Test toMutablePeriod
        MutablePeriod mutablePeriod = period.toMutablePeriod();
        assertEquals(5, mutablePeriod.getYears());
        assertEquals(0, mutablePeriod.getMonths());
        assertEquals(5, mutablePeriod.getDays());
        assertEquals(PeriodType.standard(), mutablePeriod.getPeriodType());
    }

    /**
     * Test between method with ReadableInstant.
     */
    @Test
    public void testBetweenWithReadableInstant() {
        // Arrange
        ReadableInstant start = new DateTime(2022, 1, 1, 0, 0, 0, 0, ISOChronology.getInstanceUTC());
        ReadableInstant end = new DateTime(2022, 1, 3, 0, 0, 0, 0, ISOChronology.getInstanceUTC());
        BaseSingleFieldPeriod period = new BaseSingleFieldPeriod(between(start, end, DurationFieldType.days()));

        // Act & Assert
        assertEquals(2, period.getValue());
    }

    /**
     * Test between method with ReadablePartial.
     */
    @Test
    public void testBetweenWithReadablePartial() {
        // Arrange
        ReadablePartial start = new LocalDate(2022, 1, 1);
        ReadablePartial end = new LocalDate(2022, 1, 3);
        BaseSingleFieldPeriod period = new BaseSingleFieldPeriod(between(start, end, BaseSingleFieldPeriod.class));

        // Act & Assert
        assertEquals(2, period.getValue());
    }

    /**
     * Test standardPeriodIn method.
     */
    @Test
    public void testStandardPeriodIn() {
        // Arrange
        ReadablePeriod period = new Period(2, 3, 4, 5, 6, 7, 8, PeriodType.standard());

        // Act
        int days = BaseSingleFieldPeriod.standardPeriodIn(period, PeriodType.days.getMillisPerDay());

        // Assert
        assertEquals(17, days);
    }
}