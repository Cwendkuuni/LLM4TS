import org.joda.time.BaseSingleFieldPeriod;
import org.joda.time.Chronology;
import org.joda.time.DurationField;
import org.joda.time.DurationFieldType;
import org.joda.time.MutablePeriod;
import org.joda.time.Period;
import org.joda.time.PeriodType;
import org.joda.time.DateTimeUtils;
import org.junit.Test;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotEquals;

public class BaseSingleFieldPeriodTest {

    @Test
    public void testBetween() {
        ReadableInstant start = DateTimeUtils.parseLocalDate("2013-01-01T00:00:00.000Z").toInstant();
        ReadableInstant end = DateTimeUtils.parseLocalDate("2013-01-05T00:00:00.000Z").toInstant();
        assertEquals(3, BaseSingleFieldPeriod.between(start, end, DurationFieldType.days()));

        ReadableInstant start2 = DateTimeUtils.parseLocalDate("2013-01-01T00:00:00.000Z").toInstant();
        ReadableInstant end2 = DateTimeUtils.parseLocalDate("2013-01-05T12:34:56.789Z").toInstant();
        assertEquals(4, BaseSingleFieldPeriod.between(start2, end2, DurationFieldType.days()));

        ReadablePartial start3 = DateTimeUtils.parseLocalDate("2013-01-01").toLocalDate();
        ReadablePartial end3 = DateTimeUtils.parseLocalDate("2013-01-05").toLocalDate();
        assertEquals(3, BaseSingleFieldPeriod.between(start3, end3, Period.days()));

        ReadablePartial start4 = DateTimeUtils.parseLocalDate("2013-01-01").toLocalDate();
        ReadablePartial end4 = DateTimeUtils.parseLocalDate("2013-01-10").toLocalDate();
        assertEquals(9, BaseSingleFieldPeriod.between(start4, end4, Period.days()));
    }

    @Test
    public void testStandardPeriodIn() {
        ReadablePeriod period = Period.days(1).years(1).months(2).hours(3).minutes(45).seconds(60).millis(789);
        assertEquals(3675, BaseSingleFieldPeriod.standardPeriodIn(period, 1000));
    }

    @Test(expected = IllegalArgumentException.class)
    public void testStandardPeriodInInvalidPeriod() {
        ReadablePeriod period = Period.months(2).years(-1);
        BaseSingleFieldPeriod.standardPeriodIn(period, 1000);
    }

    @Test
    public void testNewInstance() {
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

        assertEquals(3, period.getValue());
        assertEquals(DurationFieldType.days(), period.getFieldType());
        assertEquals(PeriodType.standard(), period.getPeriodType());
        assertEquals(1, period.size());
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

        Period result = period.toPeriod();
        assertEquals(3, result.getYears());
        assertEquals(0, result.getMonths());
        assertEquals(0, result.getWeeks());
        assertEquals(0, result.getDays());
        assertEquals(0, result.getHours());
        assertEquals(0, result.getMinutes());
        assertEquals(0, result.getSeconds());
        assertEquals(0, result.getMillis());
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

        MutablePeriod result = period.toMutablePeriod();
        assertEquals(3, result.getYears());
        assertEquals(0, result.getMonths());
        assertEquals(0, result.getWeeks());
        assertEquals(0, result.getDays());
        assertEquals(0, result.getHours());
        assertEquals(0, result.getMinutes());
        assertEquals(0, result.getSeconds());
        assertEquals(0, result.getMillis());
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

        BaseSingleFieldPeriod period4 = new BaseSingleFieldPeriod(3) {
            @Override
            public DurationFieldType getFieldType() {
                return DurationFieldType.days();
            }

            @Override
            public PeriodType getPeriodType() {
                return PeriodType.standard();
            }
        };

        BaseSingleFieldPeriod period5 = new BaseSingleFieldPeriod(3) {
            @Override
            public DurationFieldType getFieldType() {
                return DurationFieldType.weeks();
            }

            @Override
            public PeriodType getPeriodType() {
                return PeriodType.standard();
            }
        };

        assertEquals(period1, period1);
        assertEquals(period1, period2);
        assertNotEquals(period1, period3);
        assertNotEquals(period1, period4);
        assertNotEquals(period1, period5);
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

        assertEquals(period1.hashCode(), period1.hashCode());
        assertEquals(period1.hashCode(), period2.hashCode());
        assertNotEquals(period1.hashCode(), period3.hashCode());
    }
}