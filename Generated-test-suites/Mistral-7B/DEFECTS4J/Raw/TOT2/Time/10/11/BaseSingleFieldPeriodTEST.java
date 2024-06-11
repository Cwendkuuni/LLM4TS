package org.joda.time.base;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertThrows;

import org.joda.time.BaseSingleFieldPeriod;
import org.joda.time.Chronology;
import org.joda.time.DateTimeUtils;
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
import org.junit.Test;

/**
 * Test cases for the {@link BaseSingleFieldPeriod} class.
 */
public class BaseSingleFieldPeriodTest {

    /** The serial version UID for the test class. */
    private static final long serialVersionUID = 6267842359720L;

    /** A test period instance. */
    private BaseSingleFieldPeriod testPeriod;

    /** An testing instant with the epoch value. */
    private static final ReadableInstant EPOCH = DateTimeUtils.parseLocalDate("1970-01-01").toInstant();

    /** A testing instant with a later value. */
    private static final ReadableInstant LATER = EPOCH.plus(Duration.standardHours(1));

    /** A testing instant with an earlier value. */
    private static final ReadableInstant EARLIER = EPOCH.minus(Duration.standardHours(1));

    /** A testing period with value 2 days. */
    private static final BaseSingleFieldPeriod TEST_PERIOD_2_DAYS = new BaseSingleFieldPeriod(2) {
        @Override
        public DurationFieldType getFieldType() {
            return DurationFieldType.DAYS;
        }

        @Override
        public PeriodType getPeriodType() {
            return PeriodType.standard();
        }
    };

    /** A testing period with value 3 hours. */
    private static final BaseSingleFieldPeriod TEST_PERIOD_3_HOURS = new BaseSingleFieldPeriod(3) {
        @Override
        public DurationFieldType getFieldType() {
            return DurationFieldType.HOURS;
        }

        @Override
        public PeriodType getPeriodType() {
            return PeriodType.standard();
        }
    };

    /**
     * Set up the test environment.
     */
    @SuppressWarnings("unchecked")
    @Before
    public void setUp() {
        testPeriod = new BaseSingleFieldPeriod(0) {
            @Override
            public DurationFieldType getFieldType() {
                return DurationFieldType.DAYS;
            }

            @Override
            public PeriodType getPeriodType() {
                return PeriodType.standard();
            }
        };
    }

    /**
     * Test the method {@link BaseSingleFieldPeriod#between(ReadableInstant, ReadableInstant, DurationFieldType)}.
     */
    @Test
    public void testBetween_ReadableInstant_ReadableInstant_DurationFieldType() {
        int daysBetween = BaseSingleFieldPeriod.between(EPOCH, LATER, DurationFieldType.DAYS);
        assertEquals(1, daysBetween);

        daysBetween = BaseSingleFieldPeriod.between(LATER, EPOCH, DurationFieldType.DAYS);
        assertEquals(-1, daysBetween);

        daysBetween = BaseSingleFieldPeriod.between(EPOCH, EPOCH, DurationFieldType.DAYS);
        assertEquals(0, daysBetween);

        daysBetween = BaseSingleFieldPeriod.between(EPOCH, null, DurationFieldType.DAYS);
        assertThrows(IllegalArgumentException.class, () -> daysBetween);

        daysBetween = BaseSingleFieldPeriod.between(null, EPOCH, DurationFieldType.DAYS);
        assertThrows(IllegalArgumentException.class, () -> daysBetween);

        daysBetween = BaseSingleFieldPeriod.between(EPOCH, EPOCH, null);
        assertThrows(IllegalArgumentException.class, () -> daysBetween);

        daysBetween = BaseSingleFieldPeriod.between(EPOCH, EPOCH, DurationFieldType.HOURS);
        assertEquals(0, daysBetween);
    }

    /**
     * Test the method {@link BaseSingleFieldPeriod#between(ReadablePartial, ReadablePartial, ReadablePeriod)}.
     */
    @Test
    public void testBetween_ReadablePartial_ReadablePartial_ReadablePeriod() {
        ReadablePeriod zeroPeriod = Period.ZERO;
        int daysBetween = BaseSingleFieldPeriod.between(DateTimeUtils.parseLocalDate("2022-01-01"), DateTimeUtils.parseLocalDate("2022-01-02"), zeroPeriod);
        assertEquals(1, daysBetween);

        daysBetween = BaseSingleFieldPeriod.between(DateTimeUtils.parseLocalDate("2022-01-02"), DateTimeUtils.parseLocalDate("2022-01-01"), zeroPeriod);
        assertEquals(-1, daysBetween);

        daysBetween = BaseSingleFieldPeriod.between(DateTimeUtils.parseLocalDate("2022-01-01"), DateTimeUtils.parseLocalDate("2022-01-01"), zeroPeriod);
        assertEquals(0, daysBetween);

        daysBetween = BaseSingleFieldPeriod.between(DateTimeUtils.parseLocalDate("2022-01-01"), null, zeroPeriod);
        assertThrows(IllegalArgumentException.class, () -> daysBetween);

        daysBetween = BaseSingleFieldPeriod.between(null, DateTimeUtils.parseLocalDate("2022-01-01"), zeroPeriod);
        assertThrows(IllegalArgumentException.class, () -> daysBetween);

        daysBetween = BaseSingleFieldPeriod.between(DateTimeUtils.parseLocalDate("2022-01-01"), DateTimeUtils.parseLocalDate("2022-01-01"), null);
        assertThrows(IllegalArgumentException.class, () -> daysBetween);

        daysBetween = BaseSingleFieldPeriod.between(DateTimeUtils.parseLocalDate("2022-01-01"), DateTimeUtils.parseLocalDate("2022-01-01"), new BaseSingleFieldPeriod(1) {
            @Override
            public DurationFieldType getFieldType() {
                return DurationFieldType.HOURS;
            }

            @Override
            public PeriodType getPeriodType() {
                return PeriodType.standard();
            }
        });
        assertEquals(0, daysBetween);
    }

    /**
     * Test the method {@link BaseSingleFieldPeriod#standardPeriodIn(ReadablePeriod, long)}.
     */
    @Test
    public void testStandardPeriodIn_ReadablePeriod_long() {
        long millisPerUnit = 1000L;
        int standardPeriod = BaseSingleFieldPeriod.standardPeriodIn(TEST_PERIOD_2_DAYS, millisPerUnit);
        assertEquals(2 * 24L * 60L * 60L * 1000L, standardPeriod * millisPerUnit);

        standardPeriod = BaseSingleFieldPeriod.standardPeriodIn(TEST_PERIOD_3_HOURS, millisPerUnit);
        assertEquals(3 * 60L * 60L * 1000L, standardPeriod * millisPerUnit);

        standardPeriod = BaseSingleFieldPeriod.standardPeriodIn(new BaseSingleFieldPeriod(0), millisPerUnit);
        assertEquals(0, standardPeriod);

        standardPeriod = BaseSingleFieldPeriod.standardPeriodIn(null, millisPerUnit);
        assertThrows(IllegalArgumentException.class, () -> standardPeriod);

        standardPeriod = BaseSingleFieldPeriod.standardPeriodIn(new BaseSingleFieldPeriod(1), millisPerUnit * 2);
        assertEquals(0, standardPeriod);
    }

    /**
     * Test the constructor {@link BaseSingleFieldPeriod(int)}.
     */
    @Test
    public void testConstructor_int() {
        BaseSingleFieldPeriod period = new BaseSingleFieldPeriod(3);
        assertEquals(DurationFieldType.HOURS, period.getFieldType());
        assertEquals(PeriodType.standard(), period.getPeriodType());
        assertEquals(3, period.getValue());
    }

    /**
     * Test the method {@link BaseSingleFieldPeriod#getValue()}.
     */
    @Test
    public void testValue() {
        int value = testPeriod.getValue();
        assertEquals(0, value);

        testPeriod.setValue(123);
        value = testPeriod.getValue();
        assertEquals(123, value);
    }

    /**
     * Test the method {@link BaseSingleFieldPeriod#setValue(int)}.
     */
    @Test
    public void testSetValue() {
        testPeriod.setValue(123);
        assertEquals(123, testPeriod.getValue());

        try {
            testPeriod.setValue(-1);
        } catch (IllegalArgumentException e) {
            // expected
        }

        try {
            testPeriod.setValue((int) Long.MAX_VALUE + 1);
        } catch (IllegalArgumentException e) {
            // expected
        }
    }

    /**
     * Test the method {@link BaseSingleFieldPeriod#getFieldType()}.
     */
    @Test
    public void testGetFieldType() {
        DurationFieldType fieldType = testPeriod.getFieldType();
        assertEquals(DurationFieldType.DAYS, fieldType);
    }

    /**
     * Test the method {@link BaseSingleFieldPeriod#getPeriodType()}.
     */
    @Test
    public void testGetPeriodType() {
        PeriodType periodType = testPeriod.getPeriodType();
        assertEquals(PeriodType.standard(), periodType);
    }

    /**
     * Test the method {@link BaseSingleFieldPeriod#size()}.
     */
    @Test
    public void testSize() {
        assertEquals(1, testPeriod.size());
    }

    /**
     * Test the method {@link BaseSingleFieldPeriod#getFieldType(int)}.
     */
    @Test
    public void testGetFieldType_int() {
        DurationFieldType fieldType = testPeriod.getFieldType(0);
        assertEquals(DurationFieldType.DAYS, fieldType);
    }

    /**
     * Test the method {@link BaseSingleFieldPeriod#getValue(int)}.
     */
    @Test
    public void testValue_int() {
        int value = testPeriod.getValue(0);
        assertEquals(0, value);

        testPeriod.setValue(123);
        value = testPeriod.getValue(0);
        assertEquals(123, value);
    }

    /**
     * Test the method {@link BaseSingleFieldPeriod#get(DurationFieldType)}.
     */
    @Test
    public void testGet_DurationFieldType() {
        int value = testPeriod.get(LongFields.MILLISECONDS);
        assertEquals(0, value);

        testPeriod.setValue(123);
        value = testPeriod.get(LongFields.MILLISECONDS);
        assertEquals(123, value);

        try {
            testPeriod.get(DurationFieldType.HOURS);
        } catch (IllegalArgumentException e) {
            // expected
        }
    }

    /**
     * Test the method {@link BaseSingleFieldPeriod#isSupported(DurationFieldType)}.
     */
    @Test
    public void testIsSupported_DurationFieldType() {
        assertTrue(testPeriod.isSupported(DurationFieldType.DAYS));

        try {
            testPeriod.isSupported(DurationFieldType.HOURS);
        } catch (IllegalArgumentException e) {
            // expected
        }
    }

    /**
     * Test the method {@link BaseSingleFieldPeriod#toPeriod()}.
     */
    @Test
    public void testToPeriod() {
        Period period = testPeriod.toPeriod();
        assertEquals(PeriodType.standard(), period.getPeriodType());
        assertEquals(testPeriod.getValue(), period.getValue(DurationFieldType.DAYS));
    }

    /**
     * Test the method {@link BaseSingleFieldPeriod#toMutablePeriod()}.
     */
    @Test
    public void testToMutablePeriod() {
        MutablePeriod mutablePeriod = testPeriod.toMutablePeriod();
        mutablePeriod.add(DurationFieldType.DAYS, 1);
        assertEquals(1, mutablePeriod.getValue(DurationFieldType.DAYS));
    }

    /**
     * Test the method {@link BaseSingleFieldPeriod#equals(Object)}.
     */
    @Test
    public void testEquals_Object() {
        BaseSingleFieldPeriod period1 = new BaseSingleFieldPeriod(1);
        BaseSingleFieldPeriod period2 = new BaseSingleFieldPeriod(1);
        BaseSingleFieldPeriod period3 = new BaseSingleFieldPeriod(2);

        assertTrue(period1.equals(period1));
        assertTrue(period1.equals(period2));
        assertFalse(period1.equals(period3));
        assertFalse(period1.equals(null));
        assertFalse(period1.equals("test"));
    }

    /**
     * Test the method {@link BaseSingleFieldPeriod#hashCode()}.
     */
    @Test
    public void testHashCode() {
        BaseSingleFieldPeriod period1 = new BaseSingleFieldPeriod(1);
        BaseSingleFieldPeriod period2 = new BaseSingleFieldPeriod(1);
        BaseSingleFieldPeriod period3 = new BaseSingleFieldPeriod(2);

        assertEquals(period1.hashCode(), period1.hashCode());
        assertEquals(period1.hashCode(), period2.hashCode());
        assertNotEquals(period1.hashCode(), period3.hashCode());
        assertNotEquals(period1.hashCode(), null.hashCode());
        assertNotEquals(period1.hashCode(), "test".hashCode());
    }

    /**
     * Test the method {@link BaseSingleFieldPeriod#compareTo(BaseSingleFieldPeriod)}.
     */
    @Test
    public void testCompareTo_BaseSingleFieldPeriod() {
        BaseSingleFieldPeriod period1 = new BaseSingleFieldPeriod(1);
        BaseSingleFieldPeriod period2 = new BaseSingleFieldPeriod(1);
        BaseSingleFieldPeriod period3 = new BaseSingleFieldPeriod(2);

        assertEquals(0, period1.compareTo(period1));
        assertEquals(0, period1.compareTo(period2));
        assertEquals(1, period2.compareTo(period1));
        assertEquals(-1, period3.compareTo(period1));
        assertNotEquals(1, period1.compareTo(period3));
        assertNotEquals(-1, period1.compareTo(period3));
    }

    /**
     * Test the method {@link BaseSingleFieldPeriod#toString()}.
     */
    @Test
    public void testToString() {
        BaseSingleFieldPeriod period = new BaseSingleFieldPeriod(123);
        assertEquals("P1D", period.toString());
    }
}