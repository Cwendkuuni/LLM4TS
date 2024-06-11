import org.joda.time.BaseSingleFieldPeriod;
import org.joda.time.Chronology;
import org.joda.time.DurationFieldType;
import org.joda.time.MutablePeriod;
import org.joda.time.Period;
import org.joda.time.base.BasicDayPeriod;
import org.joda.time.base.BasicHourPeriod;
import org.joda.time.base.BasicMinutePeriod;
import org.joda.time.base.BasicSecondPeriod;
import org.junit.Test;
import static org.junit.Assert.*;

public class BaseSingleFieldPeriodTest {

    @Test
    public void testBasicDayPeriod() {
        BaseSingleFieldPeriod period = new BasicDayPeriod(1);
        assertEquals(DurationFieldType.days(), period.getFieldType());
        assertEquals(PeriodType.standard(), period.getPeriodType());
        assertEquals(1, period.getValue());
        assertEquals(1, period.size());

        assertFalse(period.isSupported(DurationFieldType.hours()));
        assertFalse(period.isSupported(DurationFieldType.minutes()));
        assertFalse(period.isSupported(DurationFieldType.seconds()));

        Chronology chrono = ISOChronology.getInstanceUTC();
        long millis = chrono.getMillis(2012, 1, 1).getMillis() + 1000L * 24 * 60 * 60;
        ReadableInstant start = new DateTime(chrono, millis);
        ReadableInstant end = new DateTime(chrono, millis + 1000L * 24 * 60 * 60);
        int daysBetween = BaseSingleFieldPeriod.between(start, end, DurationFieldType.days());
        assertEquals(1, daysBetween);

        Period p = period.toPeriod();
        assertEquals(PeriodType.standard(), p.getPeriodType());
        assertEquals(1, p.getValue(DurationFieldType.days()));
        assertEquals(0, p.getValue(DurationFieldType.hours()));
        assertEquals(0, p.getValue(DurationFieldType.minutes()));
        assertEquals(0, p.getValue(DurationFieldType.seconds()));

        MutablePeriod mp = period.toMutablePeriod();
        assertEquals(PeriodType.standard(), mp.getPeriodType());
        assertEquals(1, mp.getValue(DurationFieldType.days()));
        assertEquals(0, mp.getValue(DurationFieldType.hours()));
        assertEquals(0, mp.getValue(DurationFieldType.minutes()));
        assertEquals(0, mp.getValue(DurationFieldType.seconds()));

        assertEquals(period, period.toPeriod().toBasePeriod());
        assertEquals(period, period.toMutablePeriod().toBasePeriod());

        assertEquals(period, new BasicDayPeriod(1));
        assertEquals(period, new BasicDayPeriod(-1));
        assertEquals(period, new BasicDayPeriod(0));
        assertEquals(period, new BasicDayPeriod(1000));

        assertEquals(period, period.negate());

        assertEquals(0, period.compareTo(new BasicDayPeriod(0)));
        assertEquals(1, period.compareTo(new BasicDayPeriod(1)));
        assertEquals(-1, period.compareTo(new BasicDayPeriod(-1)));
        assertEquals(-1, period.compareTo(new BasicDayPeriod(2)));
    }

    @Test
    public void testBasicHourPeriod() {
        BaseSingleFieldPeriod period = new BasicHourPeriod(1);
        assertEquals(DurationFieldType.hours(), period.getFieldType());
        assertEquals(PeriodType.standard(), period.getPeriodType());
        assertEquals(1, period.getValue());
        assertEquals(1, period.size());

        assertFalse(period.isSupported(DurationFieldType.days()));
        assertFalse(period.isSupported(DurationFieldType.minutes()));
        assertFalse(period.isSupported(DurationFieldType.seconds()));

        Chronology chrono = ISOChronology.getInstanceUTC();
        long millis = chrono.getMillis(2012, 1, 1, 12, 0, 0, 0, 0).getMillis();
        ReadableInstant start = new DateTime(chrono, millis);
        ReadableInstant end = new DateTime(chrono, millis + 1000L * 60);
        int hoursBetween = BaseSingleFieldPeriod.between(start, end, DurationFieldType.hours());
        assertEquals(1, hoursBetween);

        Period p = period.toPeriod();
        assertEquals(PeriodType.standard(), p.getPeriodType());
        assertEquals(0, p.getValue(DurationFieldType.days()));
        assertEquals(1, p.getValue(DurationFieldType.hours()));
        assertEquals(0, p.getValue(DurationFieldType.minutes()));
        assertEquals(0, p.getValue(DurationFieldType.seconds()));

        MutablePeriod mp = period.toMutablePeriod();
        assertEquals(PeriodType.standard(), mp.getPeriodType());
        assertEquals(0, mp.getValue(DurationFieldType.days()));
        assertEquals(1, mp.getValue(DurationFieldType.hours()));
        assertEquals(0, mp.getValue(DurationFieldType.minutes()));
        assertEquals(0, mp.getValue(DurationFieldType.seconds()));

        assertEquals(period, period.toPeriod().toBasePeriod());
        assertEquals(period, period.toMutablePeriod().toBasePeriod());

        assertEquals(period, new BasicHourPeriod(1));
        assertEquals(period, new BasicHourPeriod(-1));
        assertEquals(period, new BasicHourPeriod(0));
        assertEquals(period, new BasicHourPeriod(1000));

        assertEquals(period, period.negate());

        assertEquals(0, period.compareTo(new BasicHourPeriod(0)));
        assertEquals(1, period.compareTo(new BasicHourPeriod(1)));
        assertEquals(-1, period.compareTo(new BasicHourPeriod(-1)));
        assertEquals(-1, period.compareTo(new BasicHourPeriod(2)));
    }

    @Test
    public void testBasicMinutePeriod() {
        BaseSingleFieldPeriod period = new BasicMinutePeriod(1);
        assertEquals(DurationFieldType.minutes(), period.getFieldType());
        assertEquals(PeriodType.standard(), period.getPeriodType());
        assertEquals(1, period.getValue());
        assertEquals(1, period.size());

        assertFalse(period.isSupported(DurationFieldType.days()));
        assertFalse(period.isSupported(DurationFieldType.hours()));
        assertFalse(period.isSupported(DurationFieldType.seconds()));

        Chronology chrono = ISOChronology.getInstanceUTC();
        long millis = chrono.getMillis(2012, 1, 1, 12, 30, 0, 0, 0).getMillis();
        ReadableInstant start = new DateTime(chrono, millis);
        ReadableInstant end = new DateTime(chrono, millis + 1000L * 60);
        int minutesBetween = BaseSingleFieldPeriod.between(start, end, DurationFieldType.minutes());
        assertEquals(1, minutesBetween);

        Period p = period.toPeriod();
        assertEquals(PeriodType.standard(), p.getPeriodType());
        assertEquals(0, p.getValue(DurationFieldType.days()));
        assertEquals(0, p.getValue(DurationFieldType.hours()));
        assertEquals(1, p.getValue(DurationFieldType.minutes()));
        assertEquals(0, p.getValue(DurationFieldType.seconds()));

        MutablePeriod mp = period.toMutablePeriod();
        assertEquals(PeriodType.standard(), mp.getPeriodType());
        assertEquals(0, mp.getValue(DurationFieldType.days()));
        assertEquals(0, mp.getValue(DurationFieldType.hours()));
        assertEquals(1, mp.getValue(DurationFieldType.minutes()));
        assertEquals(0, mp.getValue(DurationFieldType.seconds()));

        assertEquals(period, period.toPeriod().toBasePeriod());
        assertEquals(period, period.toMutablePeriod().toBasePeriod());

        assertEquals(period, new BasicMinutePeriod(1));
        assertEquals(period, new BasicMinutePeriod(-1));
        assertEquals(period, new BasicMinutePeriod(0));
        assertEquals(period, new BasicMinutePeriod(1000));

        assertEquals(period, period.negate());

        assertEquals(0, period.compareTo(new BasicMinutePeriod(0)));
        assertEquals(1, period.compareTo(new BasicMinutePeriod(1)));
        assertEquals(-1, period.compareTo(new BasicMinutePeriod(-1)));
        assertEquals(-1, period.compareTo(new BasicMinutePeriod(2)));
    }

    @Test
    public void testBasicSecondPeriod() {
        BaseSingleFieldPeriod period = new BasicSecondPeriod(1);
        assertEquals(DurationFieldType.seconds(), period.getFieldType());
        assertEquals(PeriodType.standard(), period.getPeriodType());
        assertEquals(1, period.getValue());
        assertEquals(1, period.size());

        assertFalse(period.isSupported(DurationFieldType.days()));
        assertFalse(period.isSupported(DurationFieldType.hours()));
        assertFalse(period.isSupported(DurationFieldType.minutes()));

        Chronology chrono = ISOChronology.getInstanceUTC();
        long millis = chrono.getMillis(2012, 1, 1, 12, 30, 59, 599, 999).getMillis();
        ReadableInstant start = new DateTime(chrono, millis);
        ReadableInstant end = new DateTime(chrono, millis + 1000L);
        int secondsBetween = BaseSingleFieldPeriod.between(start, end, DurationFieldType.seconds());
        assertEquals(1, secondsBetween);

        Period p = period.toPeriod();
        assertEquals(PeriodType.standard(), p.getPeriodType());
        assertEquals(0, p.getValue(DurationFieldType.days()));
        assertEquals(0, p.getValue(DurationFieldType.hours()));
        assertEquals(0, p.getValue(DurationFieldType.minutes()));
        assertEquals(1, p.getValue(DurationFieldType.seconds()));

        MutablePeriod mp = period.toMutablePeriod();
        assertEquals(PeriodType.standard(), mp.getPeriodType());
        assertEquals(0, mp.getValue(DurationFieldType.days()));
        assertEquals(0, mp.getValue(DurationFieldType.hours()));
        assertEquals(0, mp.getValue(DurationFieldType.minutes()));
        assertEquals(1, mp.getValue(DurationFieldType.seconds()));

        assertEquals(period, period.toPeriod().toBasePeriod());
        assertEquals(period, period.toMutablePeriod().toBasePeriod());

        assertEquals(period, new BasicSecondPeriod(1));
        assertEquals(period, new BasicSecondPeriod(-1));
        assertEquals(period, new BasicSecondPeriod(0));
        assertEquals(period, new BasicSecondPeriod(1000));

        assertEquals(period, period.negate());

        assertEquals(0, period.compareTo(new BasicSecondPeriod(0)));
        assertEquals(1, period.compareTo(new BasicSecondPeriod(1)));
        assertEquals(-1, period.compareTo(new BasicSecondPeriod(-1)));
        assertEquals(-1, period.compareTo(new BasicSecondPeriod(2)));
    }

    @Test(expected = IllegalArgumentException.class)
    public void testNegativeValue() {
        new BasicDayPeriod(-2);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testNullValue() {
        new BasicDayPeriod(null);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testMissingChronology() {
        try {
            BaseSingleFieldPeriod period = new BaseSingleFieldPeriod(1) {
                @Override
                protected Chronology getInstantChronology(ReadableInstant instant) {
                    return null;
                }
            };
            fail();
        } catch (IllegalArgumentException e) {
            // expected
        }
    }

    @Test(expected = IllegalArgumentException.class)
    public void testNullChronology() {
        try {
            BaseSingleFieldPeriod period = new BaseSingleFieldPeriod(1) {
                @Override
                protected Chronology getInstantChronology(ReadableInstant instant) {
                    return null;
                }
            };
            period.setValue(-1);
            fail();
        } catch (IllegalArgumentException e) {
            // expected
        }
    }

    @Test(expected = IllegalArgumentException.class)
    public void testNullDurationFieldType() {
        new BaseSingleFieldPeriod(1) {
            @Override
            protected DurationFieldType getFieldType() {
                return null;
            }
        };
    }

    @Test(expected = IllegalArgumentException.class)
    public void testNullPeriodType() {
        new BaseSingleFieldPeriod(1) {
            @Override
            protected PeriodType getPeriodType() {
                return null;
            }
        };
    }

    @Test(expected = IllegalArgumentException.class)
    public void testNullZeroInstance() {
        new BaseSingleFieldPeriod(1) {
            @Override
            protected ReadablePeriod zeroInstance() {
                return null;
            }
        };
    }

}