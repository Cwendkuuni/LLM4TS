package org.joda.time.base;

import org.junit.Test;
import org.joda.time.BaseDateTime;
import org.joda.time.Chronology;
import org.joda.time.Duration;
import org.joda.time.DurationField;
import org.joda.time.DurationFieldType;
import org.joda.time.MutablePeriod;
import org.joda.time.Period;
import org.joda.time.PeriodType;
import org.joda.time.ReadableInstant;
import org.joda.time.ReadablePartial;
import org.joda.time.ReadablePeriod;
import org.joda.time.chrono.ISOChronology;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertSame;
import static org.junit.Assert.assertTrue;

/**
 * Tests for {@link BaseSingleFieldPeriod}.
 */
public class BaseSingleFieldPeriodTest {

    /** The test period. */
    private static BaseSingleFieldPeriod testPeriod;

    /** The test period type. */
    private static PeriodType testPeriodType;

    /** The test field type. */
    private static DurationFieldType testFieldType;

    /**
     * Sets up the test.
     */
    @SuppressWarnings("unchecked")
    @Before
    public void setUp() {
        testPeriodType = PeriodType.standard();
        testFieldType = DurationFieldType.DAYS;
        testPeriod = new Period(1, testFieldType);
    }

    /**
     * Test constructor - BaseSingleFieldPeriod.
     */
    @Test
    public void testConstructor() {
        assertEquals(testPeriodType, testPeriod.getPeriodType());
        assertEquals(testFieldType, testPeriod.getFieldType());
        assertEquals(1, testPeriod.getValue());
        assertEquals(testPeriodType, testPeriod.toPeriod().getPeriodType());
        assertEquals(testFieldType, testPeriod.toPeriod().getFieldType(0));
        assertEquals(1, testPeriod.toPeriod().getValue(0));
    }

    /**
     * Test get Field Type.
     */
    @Test
    public void testGetFieldType() {
        assertEquals(testFieldType, testPeriod.getFieldType());
    }

    /**
     * Test get Period Type.
     */
    @Test
    public void testGetPeriodType() {
        assertEquals(testPeriodType, testPeriod.getPeriodType());
    }

    /**
     * Test size.
     */
    @Test
    public void testSize() {
        assertEquals(1, testPeriod.size());
    }

    /**
     * Test get Field Type at.
     */
    @Test
    public void testGetFieldTypeAt() {
        assertEquals(testFieldType, testPeriod.getFieldType(0));
    }

    /**
     * Test get Value at.
     */
    @Test
    public void testGetValueAt() {
        assertEquals(1, testPeriod.getValue(0));
    }

    /**
     * Test get.
     */
    @Test
    public void testGet() {
        assertEquals(1, testPeriod.get(testFieldType));
    }

    /**
     * Test is Supported.
     */
    @Test
    public void testIsSupported() {
        assertTrue(testPeriod.isSupported(testFieldType));
        assertFalse(testPeriod.isSupported(DurationFieldType.INSTANT()));
    }

    /**
     * Test to Period.
     */
    @Test
    public void testToPeriod() {
        Period period = testPeriod.toPeriod();
        assertEquals(testPeriodType, period.getPeriodType());
        assertEquals(testFieldType, period.getFieldType(0));
        assertEquals(1, period.getValue(0));
    }

    /**
     * Test to Mutable Period.
     */
    @Test
    public void testToMutablePeriod() {
        MutablePeriod mutablePeriod = testPeriod.toMutablePeriod();
        assertEquals(testPeriodType, mutablePeriod.getPeriodType());
        assertEquals(testFieldType, mutablePeriod.getFieldType());
        assertEquals(1, mutablePeriod.getValue(0));
    }

    /**
     * Test equals.
     */
    @Test
    public void testEquals() {
        BaseSingleFieldPeriod testPeriod2 = new Period(1, testFieldType);
        assertTrue(testPeriod.equals(testPeriod2));
        assertFalse(testPeriod.equals(null));
        assertFalse(testPeriod.equals(new Object()));
        assertFalse(testPeriod.equals(new Period(1, DurationFieldType.INSTANT())));
        BaseSingleFieldPeriod testPeriod3 = new Period(2, testFieldType);
        assertFalse(testPeriod.equals(testPeriod3));
    }

    /**
     * Test hash code.
     */
    @Test
    public void testHashCode() {
        int hashCode = testPeriod.hashCode();
        BaseSingleFieldPeriod testPeriod2 = new Period(1, testFieldType);
        assertEquals(hashCode, testPeriod2.hashCode());
    }

    /**
     * Test compare to.
     */
    @Test
    public void testCompareTo() {
        BaseSingleFieldPeriod testPeriod2 = new Period(1, testFieldType);
        assertEquals(0, testPeriod.compareTo(testPeriod2));

        BaseSingleFieldPeriod testPeriod3 = new Period(2, testFieldType);
        assertEquals(1, testPeriod.compareTo(testPeriod3));

        BaseSingleFieldPeriod testPeriod4 = new Period(0, testFieldType);
        assertEquals(-1, testPeriod.compareTo(testPeriod4));

        BaseSingleFieldPeriod testPeriod5 = new Period(3, testFieldType);
        assertEquals(-1, testPeriod.compareTo(testPeriod5));
    }

    /**
     * Test between.
     */
    @Test
    public void testBetween() {
        ReadableInstant start = new BaseDateTime(0, ISOChronology.getInstanceUTC()).toInstant();
        ReadableInstant end = new BaseDateTime(1, ISOChronology.getInstanceUTC()).toInstant();
        int between = BaseSingleFieldPeriod.between(start, end, testFieldType);
        assertEquals(1, between);
    }

    /**
     * Test between with partials.
     */
    @Test
    public void testBetweenWithPartials() {
        ReadablePartial start = new org.joda.time.LocalDate(2013, 1, 1);
        ReadablePartial end = new org.joda.time.LocalDate(2013, 1, 2);
        int between = BaseSingleFieldPeriod.between(start, end, testPeriodType.getBasePeriod());
        assertEquals(1, between);
    }

    /**
     * Test standard Period In.
     */
    @Test
    public void testStandardPeriodIn() {
        long millisPerUnit = Chronology.getInstanceUTC().getMillisPerDay();
        int standardPeriodIn = BaseSingleFieldPeriod.standardPeriodIn(testPeriod, millisPerUnit);
        assertEquals(1, standardPeriodIn);
    }
}