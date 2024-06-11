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

public class BaseSingleFieldPeriodTest {

    @Test
    public void testBetween() {
        ReadableInstant start = DateTimeUtils.parseInstant("2012-01-01T00:00:00.0Z");
        ReadableInstant end = DateTimeUtils.parseInstant("2012-01-02T00:00:00.0Z");
        int daysBetween = BaseSingleFieldPeriod.between(start, end, DurationFieldType.days());
        assertEquals(1, daysBetween);
    }

    @Test
    public void testBetweenPartial() {
        ReadablePartial start = new LocalDate(2012, 1, 1);
        ReadablePartial end = new LocalDate(2012, 1, 2);
        ReadablePeriod zeroInstance = Period.ZERO;
        int daysBetween = BaseSingleFieldPeriod.between(start, end, zeroInstance);
        assertEquals(1, daysBetween);
    }

    @Test
    public void testStandardPeriodIn() {
        ReadablePeriod period = Period.days(1).withHours(2).withMinutes(30).withSeconds(60);
        int hours = BaseSingleFieldPeriod.standardPeriodIn(period, DurationFieldType.hours().getMillisPerUnit());
        assertEquals(24 + 2, hours);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testStandardPeriodInImprecisePeriod() {
        ReadablePeriod period = Period.months(1).withDays(15);
        BaseSingleFieldPeriod.standardPeriodIn(period, DurationFieldType.hours().getMillisPerUnit());
    }

    @Test
    public void testCreate() {
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

        int days = period.getValue();
        assertEquals(1, days);
        assertEquals(DurationFieldType.days(), period.getFieldType());
        assertEquals(PeriodType.standard(), period.getPeriodType());
        assertEquals(1, period.size());
    }

    @Test
    public void testCreateWithNegativeValue() {
        BaseSingleFieldPeriod period = new BaseSingleFieldPeriod(-1) {
            @Override
            public DurationFieldType getFieldType() {
                return DurationFieldType.days();
            }

            @Override
            public PeriodType getPeriodType() {
                return PeriodType.standard();
            }
        };

        int days = period.getValue();
        assertEquals(-1, days);
    }

    @Test
    public void testGetValue() {
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

        int days = period.getValue();
        assertEquals(1, days);
    }

    @Test
    public void testSetValue() {
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

        period.setValue(2);
        int days = period.getValue();
        assertEquals(2, days);
    }

    @Test
    public void testToPeriod() {
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

        Period toPeriod = period.toPeriod();
        assertEquals(1, toPeriod.getDays());
        assertEquals(0, toPeriod.getHours());
        assertEquals(0, toPeriod.getMinutes());
        assertEquals(0, toPeriod.getSeconds());
        assertEquals(0, toPeriod.getMillis());
        assertEquals(1, toPeriod.size());
    }

    @Test
    public void testToMutablePeriod() {
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

        MutablePeriod toMutablePeriod = period.toMutablePeriod();
        assertEquals(1, toMutablePeriod.getDays());
        assertEquals(0, toMutablePeriod.getHours());
        assertEquals(0, toMutablePeriod.getMinutes());
        assertEquals(0, toMutablePeriod.getSeconds());
        assertEquals(0, toMutablePeriod.getMillis());
        assertEquals(1, toMutablePeriod.size());
    }

    @Test
    public void testEquals() {
        BaseSingleFieldPeriod period1 = new BaseSingleFieldPeriod(1) {
            @Override
            public DurationFieldType getFieldType() {
                return DurationFieldType.days();
            }

            @Override
            public PeriodType getPeriodType() {
                return PeriodType.standard();
            }
        };

        BaseSingleFieldPeriod period2 = new BaseSingleFieldPeriod(1) {
            @Override
            public DurationFieldType getFieldType() {
                return DurationFieldType.days();
            }

            @Override
            public PeriodType getPeriodType() {
                return PeriodType.standard();
            }
        };

        BaseSingleFieldPeriod period3 = new BaseSingleFieldPeriod(2) {
            @Override
            public DurationFieldType getFieldType() {
                return DurationFieldType.days();
            }

            @Override
            public PeriodType getPeriodType() {
                return PeriodType.standard();
            }
        };

        assertEquals(period1, period1);
        assertEquals(period1, period2);
        assertFalse(period1.equals(period3));
        assertFalse(period2.equals(period3));
        assertFalse(period1.equals(new Object()));
    }

    @Test
    public void testHashCode() {
        BaseSingleFieldPeriod period1 = new BaseSingleFieldPeriod(1) {
            @Override
            public DurationFieldType getFieldType() {
                return DurationFieldType.days();
            }

            @Override
            public PeriodType getPeriodType() {
                return PeriodType.standard();
            }
        };

        BaseSingleFieldPeriod period2 = new BaseSingleFieldPeriod(1) {
            @Override
            public DurationFieldType getFieldType() {
                return DurationFieldType.days();
            }

            @Override
            public PeriodType getPeriodType() {
                return PeriodType.standard();
            }
        };

        BaseSingleFieldPeriod period3 = new BaseSingleFieldPeriod(2) {
            @Override
            public DurationFieldType getFieldType() {
                return DurationFieldType.days();
            }

            @Override
            public PeriodType getPeriodType() {
                return PeriodType.standard();
            }
        };

        int hashCode1 = period1.hashCode();
        int hashCode2 = period2.hashCode();
        int hashCode3 = period3.hashCode();

        assertEquals(hashCode1, hashCode1);
        assertEquals(hashCode1, hashCode2);
        assertNotEquals(hashCode1, hashCode3);
        assertNotEquals(hashCode2, hashCode3);
    }

    @Test
    public void testCompareTo() {
        BaseSingleFieldPeriod period1 = new BaseSingleFieldPeriod(1) {
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

        BaseSingleFieldPeriod period3 = new BaseSingleFieldPeriod(1) {
            @Override
            public DurationFieldType getFieldType() {
                return DurationFieldType.days();
            }

            @Override
            public PeriodType getPeriodType() {
                return PeriodType.standard();
            }
        };

        int result = period1.compareTo(period3);
        assertEquals(0, result);

        result = period1.compareTo(period2);
        assertEquals(-1, result);

        result = period2.compareTo(period1);
        assertEquals(1, result);

        result = period2.compareTo(period2);
        assertEquals(0, result);
    }

}