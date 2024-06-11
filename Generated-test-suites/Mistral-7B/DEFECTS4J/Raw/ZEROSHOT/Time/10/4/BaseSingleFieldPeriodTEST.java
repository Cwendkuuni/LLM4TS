import org.junit.Test;
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
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertSame;
import static org.junit.Assert.assertTrue;

public class BaseSingleFieldPeriodTest {

    @Test
    public void testBetween() {
        // Test with Instant
        ReadableInstant start = DateTimeUtils.parseLocalDate("2006-01-01").toInstant();
        ReadableInstant end = DateTimeUtils.parseLocalDate("2006-01-03").toInstant();
        assertEquals(7, BaseSingleFieldPeriod.between(start, end, DurationFieldType.days()));

        // Test with Period
        Period period = new Period(3, 4, 5);
        assertEquals(12, BaseSingleFieldPeriod.between(DateTimeUtils.currentTime(), DateTimeUtils.currentTime().plus(period), DurationFieldType.days()));

        // Test with LocalDate
        ReadablePartial startDate = DateTimeUtils.parseLocalDate("2006-01-01");
        ReadablePartial endDate = DateTimeUtils.parseLocalDate("2006-01-03");
        assertEquals(7, BaseSingleFieldPeriod.between(startDate, endDate, new Period(0, 0, 0)));

        // Test with invalid arguments
        assertNull(BaseSingleFieldPeriod.between(null, null, DurationFieldType.days()));
        assertNull(BaseSingleFieldPeriod.between(DateTimeUtils.parseLocalDate("2006-01-01"), null, DurationFieldType.days()));
        assertNull(BaseSingleFieldPeriod.between(null, DateTimeUtils.parseLocalDate("2006-01-01"), DurationFieldType.days()));
        assertNull(BaseSingleFieldPeriod.between(DateTimeUtils.parseLocalDate("2006-01-01"), DateTimeUtils.parseLocalDate("2006-01-01"), null));
    }

    @Test
    public void testStandardPeriodIn() {
        // Test with valid Period
        Period period = new Period(3, 4, 5);
        assertEquals(127200000, BaseSingleFieldPeriod.standardPeriodIn(period, ChronoUnit.MILLIS.getNum()));

        // Test with invalid Period
        Period invalidPeriod = new Period(2, 29, 59);
        assertNull(BaseSingleFieldPeriod.standardPeriodIn(invalidPeriod, ChronoUnit.MILLIS.getNum()));
    }

    @Test
    public void testNewInstance() {
        // Test new instance with valid arguments
        BaseSingleFieldPeriod period = new BaseSingleFieldPeriod(7);
        assertEquals(7, period.getValue());
        assertEquals(DurationFieldType.days(), period.getFieldType());

        // Test new instance with null arguments
        assertNull(new BaseSingleFieldPeriod(null));
    }

    @Test
    public void testGetValue() {
        BaseSingleFieldPeriod period = new BaseSingleFieldPeriod(123);
        assertEquals(123, period.getValue());
    }

    @Test
    public void testSetValue() {
        BaseSingleFieldPeriod period = new BaseSingleFieldPeriod(456);
        period.setValue(789);
        assertEquals(789, period.getValue());
    }

    @Test
    public void testGetFieldType() {
        BaseSingleFieldPeriod period = new BaseSingleFieldPeriod(123);
        DurationFieldType type = new DurationFieldType() {
            @Override
            public String getName() {
                return "test";
            }

            @Override
            public DurationField getField(Chronology chrono) {
                return new DurationField() {
                    @Override
                    public int getDifference(long value, long base) {
                        return 0;
                    }

                    @Override
                    public int getValue(ReadableInstant instant) {
                        return 0;
                    }

                    @Override
                    public int getUnitMillis() {
                        return 0;
                    }

                    @Override
                    public DurationFieldType getBaseUnit() {
                        return DurationFieldType.days();
                    }

                    @Override
                    public int getPreciseValue(ReadableInstant instant) {
                        return 0;
                    }

                    @Override
                    public boolean isSupported(ReadableInstant instant) {
                        return true;
                    }

                    @Override
                    public String toString() {
                        return "test";
                    }
                };
            }
        };
        assertEquals(type, period.getFieldType());
    }

    @Test
    public void testGetPeriodType() {
        BaseSingleFieldPeriod period = new BaseSingleFieldPeriod(123);
        PeriodType type = new PeriodType() {
            @Override
            public String getID() {
                return "test";
            }

            @Override
            public String getName() {
                return "test";
            }

            @Override
            public PeriodTypeBase getBaseType() {
                return null;
            }

            @Override
            public int getLength(DurationFieldType field) {
                return 0;
            }
        };
        assertEquals(type, period.getPeriodType());
    }

    @Test
    public void testSize() {
        BaseSingleFieldPeriod period = new BaseSingleFieldPeriod(123);
        assertEquals(1, period.size());
    }

    @Test
    public void testGetFieldTypeAt() {
        BaseSingleFieldPeriod period = new BaseSingleFieldPeriod(123);
        DurationFieldType type = new DurationFieldType() {
            @Override
            public String getName() {
                return "test";
            }
        };
        assertEquals(type, period.getFieldType(0));
    }

    @Test(expected = IndexOutOfBoundsException.class)
    public void testGetValueAt() {
        BaseSingleFieldPeriod period = new BaseSingleFieldPeriod(123);
        period.getValue(1);
    }

    @Test
    public void testToPeriod() {
        BaseSingleFieldPeriod period = new BaseSingleFieldPeriod(123);
        Period result = period.toPeriod();
        assertEquals(123, result.getHours());
    }

    @Test
    public void testToMutablePeriod() {
        BaseSingleFieldPeriod period = new BaseSingleFieldPeriod(123);
        MutablePeriod periodMutable = period.toMutablePeriod();
        assertEquals(123, periodMutable.getHours());
    }

    @Test
    public void testEquals() {
        BaseSingleFieldPeriod period1 = new BaseSingleFieldPeriod(123);
        BaseSingleFieldPeriod period2 = new BaseSingleFieldPeriod(123);
        BaseSingleFieldPeriod period3 = new BaseSingleFieldPeriod(124);
        BaseSingleFieldPeriod period4 = new BaseSingleFieldPeriod(123);
        assertEquals(period1, period1);
        assertEquals(period1, period2);
        assertEquals(period1, period4);
        assertFalse(period1.equals(period3));
    }

    @Test
    public void testHashCode() {
        BaseSingleFieldPeriod period1 = new BaseSingleFieldPeriod(123);
        BaseSingleFieldPeriod period2 = new BaseSingleFieldPeriod(123);
        assertEquals(period1.hashCode(), period2.hashCode());
    }

    @Test
    public void testCompareTo() {
        BaseSingleFieldPeriod period1 = new BaseSingleFieldPeriod(123);
        BaseSingleFieldPeriod period2 = new BaseSingleFieldPeriod(124);
        BaseSingleFieldPeriod period3 = new BaseSingleFieldPeriod(123);
        assertEquals(0, period1.compareTo(period3));
        assertEquals(-1, period1.compareTo(period2));
        assertEquals(1, period2.compareTo(period1));
    }
}