import org.joda.time.BaseSingleFieldPeriod;
import org.joda.time.Chronology;
import org.joda.time.DateTimeUtils;
import org.joda.time.DurationField;
import org.joda.time.DurationFieldType;
import org.joda.time.MutablePeriod;
import org.joda.time.Period;
import org.joda.time.PeriodType;
import org.joda.time.ReadableInstant;
import org.joda.time.ReadablePartial;
import org.joda.time.chrono.ISOChronology;
import org.junit.Assert;
import org.junit.Test;

public class BaseSingleFieldPeriodTest {

    @Test
    public void testBetween() {
        ReadableInstant start = DateTimeUtils.parseLocalDate(2015, 1, 1).toInstant();
        ReadableInstant end = DateTimeUtils.parseLocalDate(2015, 1, 3).toInstant();
        DurationFieldType days = DurationFieldType.days();
        int period = BaseSingleFieldPeriod.between(start, end, days);
        Assert.assertEquals(2, period);

        ReadablePartial startPartial = DateTimeUtils.parseLocalDate(2015, 1, 1);
        ReadablePartial endPartial = DateTimeUtils.parseLocalDate(2015, 1, 3);
        ReadablePeriod zeroInstance = Period.ZERO;
        period = BaseSingleFieldPeriod.between(startPartial, endPartial, zeroInstance);
        Assert.assertEquals(2, period.getValue(0));
    }

    @Test
    public void testStandardPeriodIn() {
        ReadablePeriod period = new Period(2, 3, 4, 5, 6, 7, 8);
        long millisPerUnit = 1000 * 60 * 60 * 24; // 1 day
        int hours = BaseSingleFieldPeriod.standardPeriodIn(period, millisPerUnit);
        Assert.assertEquals(114, hours);
    }

    @Test
    public void testCreateInstance() {
        BaseSingleFieldPeriod period = new BaseSingleFieldPeriod(3) {
            @Override
            public DurationFieldType getFieldType() {
                return DurationFieldType.days();
            }

            @Override
            public PeriodType getPeriodType() {
                return PeriodType.standard();
            }
        };
        Assert.assertEquals(3, period.getValue(0));
    }

    @Test
    public void testSize() {
        BaseSingleFieldPeriod period = new BaseSingleFieldPeriod(3) {
            @Override
            public DurationFieldType getFieldType() {
                return DurationFieldType.days();
            }

            @Override
            public PeriodType getPeriodType() {
                return PeriodType.standard();
            }
        };
        Assert.assertEquals(1, period.size());
    }

    @Test
    public void testGetValue() {
        BaseSingleFieldPeriod period = new BaseSingleFieldPeriod(3) {
            @Override
            public DurationFieldType getFieldType() {
                return DurationFieldType.days();
            }

            @Override
            public PeriodType getPeriodType() {
                return PeriodType.standard();
            }
        };
        Assert.assertEquals(3, period.getValue(0));
    }

    @Test
    public void testGetFieldType() {
        BaseSingleFieldPeriod period = new BaseSingleFieldPeriod(3) {
            @Override
            public DurationFieldType getFieldType() {
                return DurationFieldType.days();
            }

            @Override
            public PeriodType getPeriodType() {
                return PeriodType.standard();
            }
        };
        Assert.assertEquals(DurationFieldType.days(), period.getFieldType());
    }

    @Test
    public void testGetPeriodType() {
        BaseSingleFieldPeriod period = new BaseSingleFieldPeriod(3) {
            @Override
            public DurationFieldType getFieldType() {
                return DurationFieldType.days();
            }

            @Override
            public PeriodType getPeriodType() {
                return PeriodType.standard();
            }
        };
        Assert.assertEquals(PeriodType.standard(), period.getPeriodType());
    }

    @Test
    public void testToPeriod() {
        BaseSingleFieldPeriod period = new BaseSingleFieldPeriod(3) {
            @Override
            public DurationFieldType getFieldType() {
                return DurationFieldType.days();
            }

            @Override
            public PeriodType getPeriodType() {
                return PeriodType.standard();
            }
        };
        Period p = period.toPeriod();
        Assert.assertEquals(3, p.getDays());
    }

    @Test
    public void testToMutablePeriod() {
        BaseSingleFieldPeriod period = new BaseSingleFieldPeriod(3) {
            @Override
            public DurationFieldType getFieldType() {
                return DurationFieldType.days();
            }

            @Override
            public PeriodType getPeriodType() {
                return PeriodType.standard();
            }
        };
        MutablePeriod mp = period.toMutablePeriod();
        Assert.assertEquals(3, mp.getDays());
    }

    @Test
    public void testEquals() {
        BaseSingleFieldPeriod period1 = new BaseSingleFieldPeriod(3) {
            @Override
            public DurationFieldType getFieldType() {
                return DurationFieldType.days();
            }

            @Override
            public PeriodType getPeriodType() {
                return PeriodType.standard();
            }
        };
        BaseSingleFieldPeriod period2 = new BaseSingleFieldPeriod(3) {
            @Override
            public DurationFieldType getFieldType() {
                return DurationFieldType.days();
            }

            @Override
            public PeriodType getPeriodType() {
                return PeriodType.standard();
            }
        };
        BaseSingleFieldPeriod period3 = new BaseSingleFieldPeriod(4) {
            @Override
            public DurationFieldType getFieldType() {
                return DurationFieldType.days();
            }

            @Override
            public PeriodType getPeriodType() {
                return PeriodType.standard();
            }
        };
        BaseSingleFieldPeriod period4 = new Period(3, 0, 0, 0, 0, 0, 0);
        Assert.assertTrue(period1.equals(period2));
        Assert.assertFalse(period1.equals(period3));
        Assert.assertTrue(period1.equals(period4));
    }

    @Test
    public void testHashCode() {
        BaseSingleFieldPeriod period1 = new BaseSingleFieldPeriod(3) {
            @Override
            public DurationFieldType getFieldType() {
                return DurationFieldType.days();
            }

            @Override
            public PeriodType getPeriodType() {
                return PeriodType.standard();
            }
        };
        BaseSingleFieldPeriod period2 = new BaseSingleFieldPeriod(3) {
            @Override
            public DurationFieldType getFieldType() {
                return DurationFieldType.days();
            }

            @Override
            public PeriodType getPeriodType() {
                return PeriodType.standard();
            }
        };
        BaseSingleFieldPeriod period3 = new BaseSingleFieldPeriod(4) {
            @Override
            public DurationFieldType getFieldType() {
                return DurationFieldType.days();
            }

            @Override
            public PeriodType getPeriodType() {
                return PeriodType.standard();
            }
        };
        BaseSingleFieldPeriod period4 = new Period(3, 0, 0, 0, 0, 0, 0);
        Assert.assertEquals(17 * 27 * 3 + 3, period1.hashCode());
        Assert.assertEquals(17 * 27 * 3 + 3, period2.hashCode());
        Assert.assertEquals(17 * 27 * 4 + 4, period3.hashCode());
        Assert.assertEquals(17 * 27 * 3 + 3, period4.hashCode());
    }

    @Test
    public void testCompareTo() {
        BaseSingleFieldPeriod period1 = new BaseSingleFieldPeriod(3) {
            @Override
            public DurationFieldType getFieldType() {
                return DurationFieldType.days();
            }

            @Override
            public PeriodType getPeriodType() {
                return PeriodType.standard();
            }
        };
        BaseSingleFieldPeriod period2 = new BaseSingleFieldPeriod(2) {
            @Override
            public DurationFieldType getFieldType() {
                return DurationFieldType.days();
            }

            @Override
            public PeriodType getPeriodType() {
                return PeriodType.standard();
            }
        };
        BaseSingleFieldPeriod period3 = new BaseSingleFieldPeriod(4) {
            @Override
            public DurationFieldType getFieldType() {
                return DurationFieldType.days();
            }

            @Override
            public PeriodType getPeriodType() {
                return PeriodType.standard();
            }
        };
        Assert.assertEquals(1, period1.compareTo(period2));
        Assert.assertEquals(-1, period2.compareTo(period1));
        Assert.assertEquals(1, period1.compareTo(period3));
    }
}