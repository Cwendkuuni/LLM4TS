import static org.junit.Assert.*;

import org.joda.time.DateTime;
import org.joda.time.DurationFieldType;
import org.joda.time.LocalDate;
import org.joda.time.LocalTime;
import org.joda.time.Period;
import org.junit.Test;

public class BaseSingleFieldPeriodTest {

    @Test
    public void testBetweenWithInstant() {
        DateTime start = new DateTime(2021, 1, 1, 0, 0);
        DateTime end = new DateTime(2021, 1, 10, 0, 0);
        int result = BaseSingleFieldPeriod.between(start, end, DurationFieldType.days());
        assertEquals(9, result);
    }

    @Test
    public void testBetweenWithPartial() {
        LocalDate start = new LocalDate(2021, 1, 1);
        LocalDate end = new LocalDate(2021, 1, 10);
        Period zeroInstance = new Period();
        int result = BaseSingleFieldPeriod.between(start, end, zeroInstance);
        assertEquals(9, result);
    }

    @Test
    public void testStandardPeriodIn() {
        Period period = new Period(2, 15, 30, 0); // 2 days, 15 hours, 30 minutes
        int result = BaseSingleFieldPeriod.standardPeriodIn(period, 3600000); // 1 hour in milliseconds
        assertEquals(63, result);
    }

    @Test
    public void testGetValue() {
        BaseSingleFieldPeriod period = new BaseSingleFieldPeriod(5);
        assertEquals(5, period.getValue());
    }

    @Test
    public void testSetvalue() {
        BaseSingleFieldPeriod period = new BaseSingleFieldPeriod(0);
        period.setValue(10);
        assertEquals(10, period.getValue());
    }

    @Test
    public void testGetFieldType() {
        BaseSingleFieldPeriod period = new BaseSingleFieldPeriod(5) {
            @Override
            public DurationFieldType getFieldType() {
                return DurationFieldType.days();
            }

            @Override
            public PeriodType getPeriodType() {
                return null;
            }
        };
        assertEquals(DurationFieldType.days(), period.getFieldType());
    }

    @Test
    public void testGetPeriodType() {
        BaseSingleFieldPeriod period = new BaseSingleFieldPeriod(5) {
            @Override
            public DurationFieldType getFieldType() {
                return null;
            }

            @Override
            public PeriodType getPeriodType() {
                return PeriodType.dayTime();
            }
        };
        assertEquals(PeriodType.dayTime(), period.getPeriodType());
    }

    @Test
    public void testSize() {
        BaseSingleFieldPeriod period = new BaseSingleFieldPeriod(5);
        assertEquals(1, period.size());
    }

    @Test
    public void testGetFieldTypeAtIndex() {
        BaseSingleFieldPeriod period = new BaseSingleFieldPeriod(5) {
            @Override
            public DurationFieldType getFieldType() {
                return DurationFieldType.days();
            }
        };
        assertEquals(DurationFieldType.days(), period.getFieldType(0));
    }

    @Test
    public void testGetValueAtIndex() {
        BaseSingleFieldPeriod period = new BaseSingleFieldPeriod(5);
        assertEquals(5, period.getValue(0));
    }

    @Test
    public void testGetFieldTypeByType() {
        BaseSingleFieldPeriod period = new BaseSingleFieldPeriod(5) {
            @Override
            public DurationFieldType getFieldType() {
                return DurationFieldType.days();
            }
        };
        assertEquals(5, period.get(DurationFieldType.days()));
        assertEquals(0, period.get(DurationFieldType.hours()));
    }

    @Test
    public void testIsSupported() {
        BaseSingleFieldPeriod period = new BaseSingleFieldPeriod(5) {
            @Override
            public DurationFieldType getFieldType() {
                return DurationFieldType.days();
            }
        };
        assertTrue(period.isSupported(DurationFieldType.days()));
        assertFalse(period.isSupported(DurationFieldType.hours()));
    }

    @Test
    public void testToPeriod() {
        BaseSingleFieldPeriod period = new BaseSingleFieldPeriod(5);
        assertEquals(new Period(5), period.toPeriod());
    }

    @Test
    public void testToMutablePeriod() {
        BaseSingleFieldPeriod period = new BaseSingleFieldPeriod(5);
        assertEquals(new MutablePeriod().add(period), period.toMutablePeriod());
    }

    @Test
    public void testEquals() {
        BaseSingleFieldPeriod period1 = new BaseSingleFieldPeriod(5) {
            @Override
            public PeriodType getPeriodType() {
                return PeriodType.dayTime();
            }
        };
        BaseSingleFieldPeriod period2 = new BaseSingleFieldPeriod(5) {
            @Override
            public PeriodType getPeriodType() {
                return PeriodType.dayTime();
            }
        };
        BaseSingleFieldPeriod period3 = new BaseSingleFieldPeriod(10) {
            @Override
            public PeriodType getPeriodType() {
                return PeriodType.dayTime();
            }
        };
        assertTrue(period1.equals(period2));
        assertFalse(period1.equals(period3));
    }

    @Test
    public void testHashCode() {
        BaseSingleFieldPeriod period = new BaseSingleFieldPeriod(5);
        assertEquals(914241625, period.hashCode());
    }

    @Test
    public void testCompareTo() {
        BaseSingleFieldPeriod period1 = new BaseSingleFieldPeriod(5);
        BaseSingleFieldPeriod period2 = new BaseSingleFieldPeriod(10);
        assertEquals(-1, period1.compareTo(period2));
        assertEquals(1, period2.compareTo(period1));
        assertEquals(0, period1.compareTo(new BaseSingleFieldPeriod(5)));
    }

}