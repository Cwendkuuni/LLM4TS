package org.junit.joda.time.base;

import static org.junit.Assert.*;

import java.io.Serializable;
import java.util.Calendar;
import java.util.GregorianCalendar;

import org.joda.time.BaseSingleFieldPeriod;
import org.joda.time.BaseSingleFieldPeriod.BetweenResult;
import org.joda.time.Chronology;
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
import org.joda.time.field.FieldType;
import org.joda.time.format.DateTimeFormat;
import org.junit.Test;

/**
 * Test class for BaseSingleFieldPeriod.
 */
public class BaseSingleFieldPeriodTest {

    /**
     * Test the between method with instants.
     */
    @Test
    public void testBetweenInstant() {
        Chronology chrono = ISOChronology.getInstanceUTC();
        long start = chrono.getMillis(new GregorianCalendar(2010, 1, 1, 0, 0, 0).getTimeInMillis());
        long end = chrono.getMillis(new GregorianCalendar(2011, 1, 1, 0, 0, 0).getTimeInMillis());
        BetweenResult result = BaseSingleFieldPeriod.between(
                new GregorianCalendar(2010, 1, 1, 0, 0, 0).getTime(),
                new GregorianCalendar(2011, 1, 1, 0, 0, 0).getTime(),
                DurationFieldType.days());
        int expected = 365;
        int actual = result.getPeriod();
        assertEquals("Between Instants - Days", expected, actual);
    }

    /**
     * Test the between method with partials.
     */
    @Test
    public void testBetweenPartial() {
        ReadablePeriod zero = Period.ZERO;

        ReadablePartial start = new GregorianCalendar(2010, 1, 1).toReadablePartial();
        ReadablePartial end = new GregorianCalendar(2011, 1, 1).toReadablePartial();

        BetweenResult result = BaseSingleFieldPeriod.between(start, end, zero);
        int expected = 365;
        int actual = result.getPeriod();
        assertEquals("Between Partials - Days", expected, actual);
    }

    /**
     * Test the standardPeriodIn method.
     */
    @Test
    public void testStandardPeriodIn() {
        ReadablePeriod period = new MutablePeriod(1, DurationFieldType.days(), PeriodType.standard()).toImmutable();
        int millisPerUnit = 86400000; // 1 day = 86400000 milliseconds
        int expected = 1;
        int actual = BaseSingleFieldPeriod.standardPeriodIn(period, millisPerUnit);
        assertEquals("Standard Period in - Days", expected, actual);
    }

    /**
     * Test constructor.
     */
    @Test
    public void testConstructor() {
        BaseSingleFieldPeriod period = new BaseSingleFieldPeriod(365);
        int expected = 365;
        int actual = period.getValue();
        assertEquals("Constructor - Value", expected, actual);
    }

    /**
     * Test getters.
     */
    @Test
    public void testGetters() {
        BaseSingleFieldPeriod period = new BaseSingleFieldPeriod(365);
        DurationFieldType fieldType = DurationFieldType.days();
        PeriodType periodType = PeriodType.standard();

        assertEquals("Getters - FieldType", fieldType, period.getFieldType());
        assertEquals("Getters - PeriodType", periodType, period.getPeriodType());
        assertEquals("Getters - Size", 1, period.size());
        assertEquals("Getters - FieldType(0)", fieldType, period.getFieldType(0));
        assertEquals("Getters - Value(0)", 365, period.getValue(0));
    }

    /**
     * Test toPeriod method.
     */
    @Test
    public void testToPeriod() {
        BaseSingleFieldPeriod period = new BaseSingleFieldPeriod(365);
        Period expected = new Period(365, DurationFieldType.days(), PeriodType.standard());
        Period actual = period.toPeriod();
        assertEquals("ToPeriod - Period", expected, actual);
    }

    /**
     * Test toMutablePeriod method.
     */
    @Test
    public void testToMutablePeriod() {
        BaseSingleFieldPeriod period = new BaseSingleFieldPeriod(365);
        MutablePeriod expected = new MutablePeriod(365, DurationFieldType.days(), PeriodType.standard());
        MutablePeriod actual = period.toMutablePeriod();
        assertEquals("ToMutablePeriod - MutablePeriod", expected, actual);
    }

    /**
     * Test equals method.
     */
    @Test
    public void testEquals() {
        BaseSingleFieldPeriod period1 = new BaseSingleFieldPeriod(365);
        BaseSingleFieldPeriod period2 = new BaseSingleFieldPeriod(365);
        BaseSingleFieldPeriod period3 = new BaseSingleFieldPeriod(366);

        assertTrue("Equals - Equal", period1.equals(period1));
        assertTrue("Equals - Equal", period1.equals(period2));
        assertFalse("Equals - Not Equal", period1.equals(period3));
    }

    /**
     * Test hashCode method.
     */
    @Test
    public void testHashCode() {
        BaseSingleFieldPeriod period1 = new BaseSingleFieldPeriod(365);
        BaseSingleFieldPeriod period2 = new BaseSingleFieldPeriod(365);
        BaseSingleFieldPeriod period3 = new BaseSingleFieldPeriod(366);

        int expected1 = 352115345;
        int expected2 = 352115345;
        int actual1 = period1.hashCode();
        int actual2 = period2.hashCode();
        assertEquals("HashCode - Equal", expected1, actual1);
        assertEquals("HashCode - Equal", expected2, actual2);
        assertNotEquals("HashCode - Not Equal", expected1, actual3.hashCode());
    }

    /**
     * Test compareTo method.
     */
    @Test
    public void testCompareTo() {
        BaseSingleFieldPeriod period1 = new BaseSingleFieldPeriod(365);
        BaseSingleFieldPeriod period2 = new BaseSingleFieldPeriod(366);
        BaseSingleFieldPeriod period3 = new BaseSingleFieldPeriod(365);

        int expected1 = -1;
        int expected2 = 1;
        int actual1 = period1.compareTo(period2);
        int actual2 = period2.compareTo(period1);
        int actual3 = period1.compareTo(period3);

        assertEquals("CompareTo - Period1 < Period2", expected1, actual1);
        assertEquals("CompareTo - Period2 > Period1", expected2, actual2);
        assertEquals("CompareTo - Period1 == Period1", 0, actual3);
    }
}