package org.joda.time.base;

import org.junit.Test;
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
import org.junit.Assert;

/**
 * JUnit test for BaseSingleFieldPeriod class.
 */
public class BaseSingleFieldPeriodTest {

    /**
     * Test case for between method with instant arguments.
     */
    @Test
    public void testBetweenInstant() {
        ReadableInstant start = DateTimeUtils.parseInstant("2015-01-01T00:00:01.01Z");
        ReadableInstant end = DateTimeUtils.parseInstant("2015-01-01T01:02:03.03Z");
        DurationFieldType days = DurationFieldType.DAYS;

        int expected = BaseSingleFieldPeriod.between(start, end, days);
        int actual = BaseSingleFieldPeriod.between(start, end, days);

        Assert.assertEquals("Unexpected number of days between instants", expected, actual);
    }

    /**
     * Test case for between method with partial arguments.
     */
    @Test
    public void testBetweenPartial() {
        ReadablePeriod zeroInstance = Period.ZERO;
        ReadablePartial start = DateTimeUtils.parseLocalDate("2015-01-01").toLocalDateTime().toLocalDateTime().toLocalDate();
        ReadablePartial end = DateTimeUtils.parseLocalDate("2015-01-02").toLocalDateTime().toLocalDate();

        int expected = BaseSingleFieldPeriod.between(start, end, zeroInstance);
        int actual = BaseSingleFieldPeriod.between(start, end, zeroInstance);

        Assert.assertEquals("Unexpected number of days between partials", expected, actual);
    }

    /**
     * Test case for standardPeriodIn method.
     */
    @Test
    public void testStandardPeriodIn() {
        ReadablePeriod period = Period.parse("PT1H30M");
        long millisPerUnit = PeriodType.standard().getMillisPerDay();

        int expected = BaseSingleFieldPeriod.standardPeriodIn(period, millisPerUnit);
        int actual = BaseSingleFieldPeriod.standardPeriodIn(period, millisPerUnit);

        Assert.assertEquals("Unexpected number of standard days in period", expected, actual);
    }

    /**
     * Test case for BaseSingleFieldPeriod constructor.
     */
    @Test
    public void testBaseSingleFieldPeriodConstruction() {
        DurationFieldType days = DurationFieldType.DAYS;
        BaseSingleFieldPeriod period = new BaseSingleFieldPeriod(5) {
            @Override
            public DurationFieldType getFieldType() {
                return days;
            }

            @Override
            public PeriodType getPeriodType() {
                return PeriodType.standard();
            }
        };

        int expected = 5;
        int actual = period.getValue();

        Assert.assertEquals("Unexpected period value", expected, actual);
    }

    /**
     * Test case for getFieldType method.
     */
    @Test
    public void testGetFieldType() {
        DurationFieldType days = DurationFieldType.DAYS;
        BaseSingleFieldPeriod period = new BaseSingleFieldPeriod(5) {
            @Override
            public DurationFieldType getFieldType() {
                return days;
            }

            @Override
            public PeriodType getPeriodType() {
                return PeriodType.standard();
            }
        };

        DurationFieldType expected = days;
        DurationFieldType actual = period.getFieldType();

        Assert.assertEquals("Unexpected field type", expected, actual);
    }

    /**
     * Test case for getPeriodType method.
     */
    @Test
    public void testGetPeriodType() {
        DurationFieldType days = DurationFieldType.DAYS;
        BaseSingleFieldPeriod period = new BaseSingleFieldPeriod(5) {
            @Override
            public DurationFieldType getFieldType() {
                return days;
            }

            @Override
            public PeriodType getPeriodType() {
                return PeriodType.standard();
            }
        };

        PeriodType expected = PeriodType.standard();
        PeriodType actual = period.getPeriodType();

        Assert.assertEquals("Unexpected period type", expected, actual);
    }

    /**
     * Test case for size method.
     */
    @Test
    public void testSize() {
        DurationFieldType days = DurationFieldType.DAYS;
        BaseSingleFieldPeriod period = new BaseSingleFieldPeriod(5) {
            @Override
            public DurationFieldType getFieldType() {
                return days;
            }

            @Override
            public PeriodType getPeriodType() {
                return PeriodType.standard();
            }
        };

        int expected = 1;
        int actual = period.size();

        Assert.assertEquals("Unexpected number of fields", expected, actual);
    }

    /**
     * Test case for getFieldType method with invalid index.
     */
    @Test(expected = IndexOutOfBoundsException.class)
    public void testGetFieldTypeInvalidIndex() {
        DurationFieldType days = DurationFieldType.DAYS;
        BaseSingleFieldPeriod period = new BaseSingleFieldPeriod(5) {
            @Override
            public DurationFieldType getFieldType() {
                return days;
            }

            @Override
            public PeriodType getPeriodType() {
                return PeriodType.standard();
            }
        };

        period.getFieldType(2);
    }

    /**
     * Test case for getValue method with invalid index.
     */
    @Test(expected = IndexOutOfBoundsException.class)
    public void testGetValueInvalidIndex() {
        DurationFieldType days = DurationFieldType.DAYS;
        BaseSingleFieldPeriod period = new BaseSingleFieldPeriod(5) {
            @Override
            public DurationFieldType getFieldType() {
                return days;
            }

            @Override
            public PeriodType getPeriodType() {
                return PeriodType.standard();
            }
        };

        period.getValue(2);
    }

    /**
     * Test case for equals method with null argument.
     */
    @Test
    public void testEqualsNull() {
        BaseSingleFieldPeriod period = new BaseSingleFieldPeriod(5) {
            @Override
            public DurationFieldType getFieldType() {
                return DurationFieldType.DAYS;
            }

            @Override
            public PeriodType getPeriodType() {
                return PeriodType.standard();
            }
        };

        Assert.assertFalse("Unexpected equals result with null argument", period.equals(null));
    }

    /**
     * Test case for equals method with different class argument.
     */
    @Test
    public void testEqualsDifferentClass() {
        BaseSingleFieldPeriod period = new BaseSingleFieldPeriod(5) {
            @Override
            public DurationFieldType getFieldType() {
                return DurationFieldType.DAYS;
            }

            @Override
            public PeriodType getPeriodType() {
                return PeriodType.standard();
            }
        };

        Object other = new Object();

        Assert.assertFalse("Unexpected equals result with different class argument", period.equals(other));
    }

    /**
     * Test case for equals method with same value and type argument.
     */
    @Test
    public void testEqualsSameValueAndType() {
        BaseSingleFieldPeriod period1 = new BaseSingleFieldPeriod(5) {
            @Override
            public DurationFieldType getFieldType() {
                return DurationFieldType.DAYS;
            }

            @Override
            public PeriodType getPeriodType() {
                return PeriodType.standard();
            }
        };

        BaseSingleFieldPeriod period2 = new BaseSingleFieldPeriod(5) {
            @Override
            public DurationFieldType getFieldType() {
                return DurationFieldType.DAYS;
            }

            @Override
            public PeriodType getPeriodType() {
                return PeriodType.standard();
            }
        };

        Assert.assertTrue("Unexpected equals result with same value and type argument", period1.equals(period2));
    }

    /**
     * Test case for equals method with different value and same type argument.
     */
    @Test
    public void testEqualsDifferentValueAndSameType() {
        BaseSingleFieldPeriod period1 = new BaseSingleFieldPeriod(5) {
            @Override
            public DurationFieldType getFieldType() {
                return DurationFieldType.DAYS;
            }

            @Override
            public PeriodType getPeriodType() {
                return PeriodType.standard();
            }
        };

        BaseSingleFieldPeriod period2 = new BaseSingleFieldPeriod(3) {
            @Override
            public DurationFieldType getFieldType() {
                return DurationFieldType.DAYS;
            }

            @Override
            public PeriodType getPeriodType() {
                return PeriodType.standard();
            }
        };

        Assert.assertFalse("Unexpected equals result with different value and same type argument", period1.equals(period2));
    }

    /**
     * Test case for equals method with same value but different type argument.
     */
    @Test
    public void testEqualsSameValueButDifferentType() {
        BaseSingleFieldPeriod period1 = new BaseSingleFieldPeriod(5) {
            @Override
            public DurationFieldType getFieldType() {
                return DurationFieldType.DAYS;
            }

            @Override
            public PeriodType getPeriodType() {
                return PeriodType.standard();
            }
        };

        BaseSingleFieldPeriod period2 = new BaseSingleFieldPeriod(5) {
            @Override
            public DurationFieldType getFieldType() {
                return DurationFieldType.HOURS;
            }

            @Override
            public PeriodType getPeriodType() {
                return PeriodType.standard();
            }
        };

        Assert.assertFalse("Unexpected equals result with same value but different type argument", period1.equals(period2));
    }

    /**
     * Test case for hashCode method.
     */
    @Test
    public void testHashCode() {
        BaseSingleFieldPeriod period = new BaseSingleFieldPeriod(5) {
            @Override
            public DurationFieldType getFieldType() {
                return DurationFieldType.DAYS;
            }

            @Override
            public PeriodType getPeriodType() {
                return PeriodType.standard();
            }
        };

        int expected = 17 * 27 + 5;
        int actual = period.hashCode();

        Assert.assertEquals("Unexpected hash code", expected, actual);
    }
}