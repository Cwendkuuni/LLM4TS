package org.joda.time.base; // package of the class under test

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNotEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertThrows;
import static org.junit.Assert.assertTrue;

import org.joda.time.BaseDateTime;
import org.joda.time.Chronology;
import org.joda.time.DurationField;
import org.joda.time.DurationFieldType;
import org.joda.time.MutablePeriod;
import org.joda.time.Period;
import org.joda.time.PeriodType;
import org.joda.time.ReadableInstant;
import org.joda.time.ReadablePeriod;
import org.joda.time.chrono.ISOChronology;
import org.joda.time.field.FieldUtils;
import org.joda.time.field.LongFields;
import org.joda.time.format.DateTimeFormat;
import org.joda.time.format.DateTimeFormatter;

import org.junit.Before;
import org.junit.Test;

/**
 * Test class for BaseSingleFieldPeriod.
 */
public class BaseSingleFieldPeriodTest {

    //----------------------------------------------------------
    /** Test period instance */
    private BaseSingleFieldPeriod period;

    //----------------------------------------------------------
    /**
     * Set up the test environment.
     */
    @Before
    public void setUp() throws Exception {
        period = new BaseSingleFieldPeriod(365) {
            @Override
            public DurationFieldType getFieldType() {
                return LongFields.DAYS;
            }

            @Override
            public PeriodType getPeriodType() {
                return PeriodType.standard();
            }
        };
    }

    //----------------------------------------------------------
    /**
     * Test constructor.
     */
    @Test
    public void testConstructor() {
        assertNotNull(period);
    }

    //----------------------------------------------------------
    /**
     * Test getValue.
     */
    @Test
    public void testGetValue() {
        assertEquals(365, period.getValue());
    }

    //----------------------------------------------------------
    /**
     * Test getFieldType.
     */
    @Test
    public void testGetFieldType() {
        assertEquals(LongFields.DAYS, period.getFieldType());
    }

    //----------------------------------------------------------
    /**
     * Test getPeriodType.
     */
    @Test
    public void testGetPeriodType() {
        assertEquals(PeriodType.standard(), period.getPeriodType());
    }

    //----------------------------------------------------------
    /**
     * Test size.
     */
    @Test
    public void testSize() {
        assertEquals(1, period.size());
    }

    //----------------------------------------------------------
    /**
     * Test getFieldType(int).
     */
    @Test
    public void testGetFieldTypeInt() {
        assertEquals(LongFields.DAYS, period.getFieldType(0));
    }

    //----------------------------------------------------------
    /**
     * Test getValue(int).
     */
    @Test
    public void testGetValueInt() {
        assertEquals(365, period.getValue(0));
    }

    //----------------------------------------------------------
    /**
     * Test get(DurationFieldType).
     */
    @Test
    public void testGet() {
        assertEquals(365, period.get(LongFields.DAYS));
        assertEquals(0, period.get(LongFields.HOURS));
    }

    //----------------------------------------------------------
    /**
     * Test isSupported(DurationFieldType).
     */
    @Test
    public void testIsSupported() {
        assertTrue(period.isSupported(LongFields.DAYS));
        assertFalse(period.isSupported(LongFields.HOURS));
    }

    //----------------------------------------------------------
    /**
     * Test toPeriod.
     */
    @Test
    public void testToPeriod() {
        Period p = period.toPeriod();
        assertEquals(365L, p.getHours());
        assertEquals(0, p.getMinutes());
        assertEquals(0, p.getSeconds());
        assertEquals(0, p.getMillis());
    }

    //----------------------------------------------------------
    /**
     * Test toMutablePeriod.
     */
    @Test
    public void testToMutablePeriod() {
        MutablePeriod mp = period.toMutablePeriod();
        assertEquals(365, mp.getDays());
        assertNull(mp.getHours());
        assertNull(mp.getMinutes());
        assertNull(mp.getSeconds());
        assertNull(mp.getMillis());
    }

    //----------------------------------------------------------
    /**
     * Test equals.
     */
    @Test
    public void testEquals() {
        BaseSingleFieldPeriod other = new BaseSingleFieldPeriod(365) {
            @Override
            public DurationFieldType getFieldType() {
                return LongFields.DAYS;
            }

            @Override
            public PeriodType getPeriodType() {
                return PeriodType.standard();
            }
        };
        assertTrue(period.equals(other));

        Object obj = new Object();
        assertFalse(period.equals(obj));

        BaseSingleFieldPeriod other2 = new BaseSingleFieldPeriod(366) {
            @Override
            public DurationFieldType getFieldType() {
                return LongFields.DAYS;
            }

            @Override
            public PeriodType getPeriodType() {
                return PeriodType.standard();
            }
        };
        assertFalse(period.equals(other2));
    }

    //----------------------------------------------------------
    /**
     * Test hashCode.
     */
    @Test
    public void testHashCode() {
        int hashCode = period.hashCode();
        BaseSingleFieldPeriod other = new BaseSingleFieldPeriod(365) {
            @Override
            public DurationFieldType getFieldType() {
                return LongFields.DAYS;
            }

            @Override
            public PeriodType getPeriodType() {
                return PeriodType.standard();
            }
        };
        assertEquals(hashCode, other.hashCode());
    }

    //----------------------------------------------------------
    /**
     * Test compareTo.
     */
    @Test
    public void testCompareTo() {
        BaseSingleFieldPeriod other = new BaseSingleFieldPeriod(366) {
            @Override
            public DurationFieldType getFieldType() {
                return LongFields.DAYS;
            }

            @Override
            public PeriodType getPeriodType() {
                return PeriodType.standard();
            }
        };
        int comparison = period.compareTo(other);
        assertTrue(comparison < 0);
    }

    //----------------------------------------------------------
    /**
     * Test between.
     */
    @Test
    public void testBetween() {
        BaseDateTime start = new BaseDateTime(0L, ISOChronology.getInstanceUTC());
        BaseDateTime end = new BaseDateTime(86400000L * 365 + 86400000L, ISOChronology.getInstanceUTC());
        assertEquals(365, BaseSingleFieldPeriod.between(start, end, LongFields.DAYS));
    }

    //----------------------------------------------------------
    /**
     * Test between with nulls.
     */
    @Test(expected = IllegalArgumentException.class)
    public void testBetweenNulls() {
        BaseSingleFieldPeriod.between(null, null, LongFields.DAYS);
    }

    //----------------------------------------------------------
    /**
     * Test standardPeriodIn.
     */
    @Test
    public void testStandardPeriodIn() {
        ReadablePeriod period = Period.ofDays(365);
        assertEquals(365, BaseSingleFieldPeriod.standardPeriodIn(period, 86400000L));
    }

    //----------------------------------------------------------
    /**
     * Test standardPeriodIn with null.
     */
    @Test(expected = IllegalArgumentException.class)
    public void testStandardPeriodInNull() {
        BaseSingleFieldPeriod.standardPeriodIn(null, 86400000L);
    }

}