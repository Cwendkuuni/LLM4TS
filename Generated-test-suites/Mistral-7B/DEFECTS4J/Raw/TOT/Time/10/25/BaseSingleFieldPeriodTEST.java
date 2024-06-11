import org.joda.time.BaseSingleFieldPeriod;
import org.joda.time.Chronology;
import org.joda.time.DurationFieldType;
import org.joda.time.MutablePeriod;
import org.joda.time.Period;
import org.joda.time.PeriodType;
import org.joda.time.DateTimeUtils;
import org.junit.Test;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertThrows;

public class BaseSingleFieldPeriodTest {

    @Test
    public void testDaysPeriod() {
        BaseSingleFieldPeriod daysPeriod = new BaseSingleFieldPeriod(2) {
            @Override
            public DurationFieldType getFieldType() {
                return DurationFieldType.DAYS;
            }

            @Override
            public PeriodType getPeriodType() {
                return PeriodType.standard();
            }
        };

        // Positive test
        assertEquals(2, daysPeriod.getValue());
        assertEquals(PeriodType.standard(), daysPeriod.getPeriodType());

        // Test equals
        BaseSingleFieldPeriod anotherDaysPeriod = new BaseSingleFieldPeriod(2) {
            @Override
            public DurationFieldType getFieldType() {
                return DurationFieldType.DAYS;
            }

            @Override
            public PeriodType getPeriodType() {
                return PeriodType.standard();
            }
        };
        assertEquals(daysPeriod, anotherDaysPeriod);

        // Test not equals with different field type
        BaseSingleFieldPeriod hoursPeriod = new BaseSingleFieldPeriod(24 * 2) {
            @Override
            public DurationFieldType getFieldType() {
                return DurationFieldType.HOURS;
            }

            @Override
            public PeriodType getPeriodType() {
                return PeriodType.standard();
            }
        };
        assertNotEquals(daysPeriod, hoursPeriod);

        // Test compareTo
        BaseSingleFieldPeriod smallerDaysPeriod = new BaseSingleFieldPeriod(1) {
            @Override
            public DurationFieldType getFieldType() {
                return DurationFieldType.DAYS;
            }

            @Override
            public PeriodType getPeriodType() {
                return PeriodType.standard();
            }
        };
        assertEquals(-1, daysPeriod.compareTo(smallerDaysPeriod));

        BaseSingleFieldPeriod greaterDaysPeriod = new BaseSingleFieldPeriod(3) {
            @Override
            public DurationFieldType getFieldType() {
                return DurationFieldType.DAYS;
            }

            @Override
            public PeriodType getPeriodType() {
                return PeriodType.standard();
            }
        };
        assertEquals(1, daysPeriod.compareTo(greaterDaysPeriod));

        // Test invalid instant
        assertThrows(IllegalArgumentException.class, () -> new BaseSingleFieldPeriod(2) {
            @Override
            public DurationFieldType getFieldType() {
                return DurationFieldType.DAYS;
            }

            @Override
            public PeriodType getPeriodType() {
                return PeriodType.standard();
            }

            @Override
            protected int between(ReadableInstant start, ReadableInstant end, DurationFieldType field) {
                throw new UnsupportedOperationException();
            }
        }.between(null, DateTimeUtils.currentTime(), DurationFieldType.DAYS));
    }

    @Test
    public void testHoursPeriod() {
        BaseSingleFieldPeriod hoursPeriod = new BaseSingleFieldPeriod(12) {
            @Override
            public DurationFieldType getFieldType() {
                return DurationFieldType.HOURS;
            }

            @Override
            public PeriodType getPeriodType() {
                return PeriodType.standard();
            }
        };

        // Positive test
        assertEquals(12, hoursPeriod.getValue());
        assertEquals(PeriodType.standard(), hoursPeriod.getPeriodType());

        // Test equals
        BaseSingleFieldPeriod anotherHoursPeriod = new BaseSingleFieldPeriod(12) {
            @Override
            public DurationFieldType getFieldType() {
                return DurationFieldType.HOURS;
            }

            @Override
            public PeriodType getPeriodType() {
                return PeriodType.standard();
            }
        };
        assertEquals(hoursPeriod, anotherHoursPeriod);

        // Test not equals with different field type
        BaseSingleFieldPeriod daysPeriod = new BaseSingleFieldPeriod(24) {
            @Override
            public DurationFieldType getFieldType() {
                return DurationFieldType.DAYS;
            }

            @Override
            public PeriodType getPeriodType() {
                return PeriodType.standard();
            }
        };
        assertNotEquals(hoursPeriod, daysPeriod);

        // Test compareTo
        BaseSingleFieldPeriod smallerHoursPeriod = new BaseSingleFieldPeriod(11) {
            @Override
            public DurationFieldType getFieldType() {
                return DurationFieldType.HOURS;
            }

            @Override
            public PeriodType getPeriodType() {
                return PeriodType.standard();
            }
        };
        assertEquals(-1, hoursPeriod.compareTo(smallerHoursPeriod));

        BaseSingleFieldPeriod greaterHoursPeriod = new BaseSingleFieldPeriod(13) {
            @Override
            public DurationFieldType getFieldType() {
                return DurationFieldType.HOURS;
            }

            @Override
            public PeriodType getPeriodType() {
                return PeriodType.standard();
            }
        };
        assertEquals(1, hoursPeriod.compareTo(greaterHoursPeriod));

        // Test invalid instant
        assertThrows(IllegalArgumentException.class, () -> new BaseSingleFieldPeriod(2) {
            @Override
            public DurationFieldType getFieldType() {
                return DurationFieldType.HOURS;
            }

            @Override
            public PeriodType getPeriodType() {
                return PeriodType.standard();
            }

            @Override
            protected int between(ReadableInstant start, ReadableInstant end, DurationFieldType field) {
                throw new UnsupportedOperationException();
            }
        }.between(null, DateTimeUtils.currentTime(), DurationFieldType.HOURS));
    }

    @Test
    public void testMinutesPeriod() {
        BaseSingleFieldPeriod minutesPeriod = new BaseSingleFieldPeriod(60) {
            @Override
            public DurationFieldType getFieldType() {
                return DurationFieldType.MINUTES;
            }

            @Override
            public PeriodType getPeriodType() {
                return PeriodType.standard();
            }
        };

        // Positive test
        assertEquals(60, minutesPeriod.getValue());
        assertEquals(PeriodType.standard(), minutesPeriod.getPeriodType());

        // Test equals
        BaseSingleFieldPeriod anotherMinutesPeriod = new BaseSingleFieldPeriod(60) {
            @Override
            public DurationFieldType getFieldType() {
                return DurationFieldType.MINUTES;
            }

            @Override
            public PeriodType getPeriodType() {
                return PeriodType.standard();
            }
        };
        assertEquals(minutesPeriod, anotherMinutesPeriod);

        // Test not equals with different field type
        BaseSingleFieldPeriod hoursPeriod = new BaseSingleFieldPeriod(60 * 2) {
            @Override
            public DurationFieldType getFieldType() {
                return DurationFieldType.HOURS;
            }

            @Override
            public PeriodType getPeriodType() {
                return PeriodType.standard();
            }
        };
        assertNotEquals(minutesPeriod, hoursPeriod);

        // Test compareTo
        BaseSingleFieldPeriod smallerMinutesPeriod = new BaseSingleFieldPeriod(59) {
            @Override
            public DurationFieldType getFieldType() {
                return DurationFieldType.MINUTES;
            }

            @Override
            public PeriodType getPeriodType() {
                return PeriodType.standard();
            }
        };
        assertEquals(-1, minutesPeriod.compareTo(smallerMinutesPeriod));

        BaseSingleFieldPeriod greaterMinutesPeriod = new BaseSingleFieldPeriod(61) {
            @Override
            public DurationFieldType getFieldType() {
                return DurationFieldType.MINUTES;
            }

            @Override
            public PeriodType getPeriodType() {
                return PeriodType.standard();
            }
        };
        assertEquals(1, minutesPeriod.compareTo(greaterMinutesPeriod));

        // Test invalid instant
        assertThrows(IllegalArgumentException.class, () -> new BaseSingleFieldPeriod(2) {
            @Override
            public DurationFieldType getFieldType() {
                return DurationFieldType.MINUTES;
            }

            @Override
            public PeriodType getPeriodType() {
                return PeriodType.standard();
            }

            @Override
            protected int between(ReadableInstant start, ReadableInstant end, DurationFieldType field) {
                throw new UnsupportedOperationException();
            }
        }.between(null, DateTimeUtils.currentTime(), DurationFieldType.MINUTES));
    }

    @Test
    public void testStandardPeriod() {
        BaseSingleFieldPeriod standardPeriod = new BaseSingleFieldPeriod(5) {
            @Override
            public DurationFieldType getFieldType() {
                return DurationFieldType.DAYS;
            }

            @Override
            public PeriodType getPeriodType() {
                return PeriodType.standard();
            }
        };

        assertEquals(5, standardPeriod.getValue());
        assertEquals(PeriodType.standard(), standardPeriod.getPeriodType());

        BaseSingleFieldPeriod standardPeriodFromStandardPeriod = standardPeriod.toPeriod();
        assertEquals(standardPeriod, standardPeriodFromStandardPeriod);

        MutablePeriod mutablePeriod = standardPeriod.toMutablePeriod();
        assertEquals(standardPeriod, mutablePeriod);
    }
}