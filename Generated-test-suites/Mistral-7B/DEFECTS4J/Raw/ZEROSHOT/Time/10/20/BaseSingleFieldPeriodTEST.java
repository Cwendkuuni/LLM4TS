import org.joda.time.BaseSingleFieldPeriod;
import org.joda.time.Chronology;
import org.joda.time.DurationField;
import org.joda.time.DurationFieldType;
import org.joda.time.MutablePeriod;
import org.joda.time.Period;
import org.joda.time.PeriodType;
import org.joda.time.ReadableInstant;
import org.joda.time.ReadablePeriod;
import org.joda.time.chrono.ISOChronology;
import org.junit.Assert;
import org.junit.Test;

public class BaseSingleFieldPeriodTest {

    @Test
    public void testBetween() {
        ReadableInstant start = ISOChronology.getInstanceUTC().parseInstant("2016-01-01T00:00:00.000Z");
        ReadableInstant end = ISOChronology.getInstanceUTC().parseInstant("2016-01-02T00:00:00.000Z");
        int period = BaseSingleFieldPeriod.between(start, end, DurationFieldType.days());
        Assert.assertEquals(1, period);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testBetweenNull() {
        BaseSingleFieldPeriod.between(null, null, DurationFieldType.days());
    }

    @Test
    public void testStandardPeriodIn() {
        ReadablePeriod period = Period.days(1).withHours(1).withMinutes(1).withSeconds(1);
        int hours = BaseSingleFieldPeriod.standardPeriodIn(period, 1L);
        Assert.assertEquals(25, hours);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testStandardPeriodInNullPeriod() {
        BaseSingleFieldPeriod.standardPeriodIn(null, 1L);
    }

    @Test
    public void testBaseSingleFieldPeriod() {
        BaseSingleFieldPeriod period = new BaseSingleFieldPeriod(1) {
            @Override
            public DurationFieldType getFieldType() {
                return DurationFieldType.days();
            }

            @Override
            public PeriodType getPeriodType() {
                return PeriodType.standard();
            }
        };

        Assert.assertEquals(1, period.getValue());
        Assert.assertEquals(DurationFieldType.days(), period.getFieldType());
        Assert.assertEquals(PeriodType.standard(), period.getPeriodType());
        Assert.assertEquals(1, period.size());
        Assert.assertEquals(true, period.isSupported(DurationFieldType.days()));
        Assert.assertEquals(false, period.isSupported(DurationFieldType.weeks()));

        Period p = period.toPeriod();
        Assert.assertEquals(1, p.getDays());

        MutablePeriod mp = period.toMutablePeriod();
        Assert.assertEquals(1, mp.getDays());
    }

    @Test(expected = IllegalArgumentException.class)
    public void testBaseSingleFieldPeriodInvalidFieldType() {
        new BaseSingleFieldPeriod(1) {
            @Override
            public DurationFieldType getFieldType() {
                return null;
            }
        };
    }

    @Test(expected = IllegalArgumentException.class)
    public void testBaseSingleFieldPeriodZeroLength() {
        new BaseSingleFieldPeriod(0) {
            @Override
            public DurationFieldType getFieldType() {
                return DurationFieldType.days();
            }
        };
    }

    @Test
    public void testBaseSingleFieldPeriodEquals() {
        BaseSingleFieldPeriod period1 = new BaseSingleFieldPeriod(1) {
            @Override
            public DurationFieldType getFieldType() {
                return DurationFieldType.days();
            }
        };
        BaseSingleFieldPeriod period2 = new BaseSingleFieldPeriod(1) {
            @Override
            public DurationFieldType getFieldType() {
                return DurationFieldType.days();
            }
        };

        Assert.assertEquals(period1, period2);
        Assert.assertNotEquals(period1, new Object());
    }

    @Test
    public void testBaseSingleFieldPeriodHashCode() {
        BaseSingleFieldPeriod period1 = new BaseSingleFieldPeriod(1) {
            @Override
            public DurationFieldType getFieldType() {
                return DurationFieldType.days();
            }
        };
        BaseSingleFieldPeriod period2 = new BaseSingleFieldPeriod(1) {
            @Override
            public DurationFieldType getFieldType() {
                return DurationFieldType.days();
            }
        };

        Assert.assertEquals(period1.hashCode(), period2.hashCode());
        Assert.assertNotEquals(period1.hashCode(), new Object().hashCode());
    }

    @Test
    public void testBaseSingleFieldPeriodCompareTo() {
        BaseSingleFieldPeriod period1 = new BaseSingleFieldPeriod(1) {
            @Override
            public DurationFieldType getFieldType() {
                return DurationFieldType.days();
            }
        };
        BaseSingleFieldPeriod period2 = new BaseSingleFieldPeriod(2) {
            @Override
            public DurationFieldType getFieldType() {
                return DurationFieldType.days();
            }
        };

        Assert.assertEquals(-1, period1.compareTo(period2));
        Assert.assertEquals(0, period1.compareTo(period1));
        Assert.assertEquals(1, period2.compareTo(period1));
    }
}