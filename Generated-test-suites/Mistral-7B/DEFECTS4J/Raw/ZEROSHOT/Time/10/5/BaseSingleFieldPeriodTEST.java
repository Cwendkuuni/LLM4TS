import org.joda.time.BaseSingleFieldPeriod;
import org.joda.time.DateTimeUtils;
import org.joda.time.DurationField;
import org.joda.time.DurationFieldType;
import org.joda.time.MutablePeriod;
import org.joda.time.Period;
import org.joda.time.PeriodType;
import org.joda.time.Chronology;
import org.joda.time.Instant;
import org.joda.time.LocalDate;
import org.joda.time.LocalDateTime;
import org.junit.Test;
import static org.junit.Assert.*;

public class BaseSingleFieldPeriodTest {

    @Test
    public void testBetween() {
        // Test between method with Instant
        Chronology isoChrono = DateTimeUtils.getInstantChronology();
        Instant start = isoChrono.parseInstant("2015-01-01T00:00:00.001Z");
        Instant end = isoChrono.parseInstant("2015-01-01T00:01:00.002Z");
        DurationFieldType days = DurationFieldType.days();
        int period = BaseSingleFieldPeriod.between(start, end, days);
        assertEquals(1, period);

        // Test between method with LocalDateTime
        LocalDateTime startDateTime = new LocalDateTime(2015, 1, 1, 0, 0, 1, 0, isoChrono);
        LocalDateTime endDateTime = new LocalDateTime(2015, 1, 1, 0, 1, 0, 0, isoChrono);
        period = BaseSingleFieldPeriod.between(startDateTime, endDateTime, days);
        assertEquals(1, period);

        // Test between method with LocalDate
        LocalDate startDate = new LocalDate(2015, 1, 1);
        LocalDate endDate = new LocalDate(2015, 1, 2);
        period = BaseSingleFieldPeriod.between(startDate, endDate, days);
        assertEquals(1, period);
    }

    @Test
    public void testStandardPeriodIn() {
        // Test standardPeriodIn method with Period
        Period period = new Period(1, 2, 3, 4, 5, 6);
        Chronology isoChrono = DateTimeUtils.getInstantChronology();
        long millisPerUnit = isoChrono.getMillisPerDay();
        int periodInDays = BaseSingleFieldPeriod.standardPeriodIn(period, millisPerUnit);
        assertEquals(7, periodInDays);

        // Test standardPeriodIn method with MutablePeriod
        MutablePeriod mutablePeriod = new MutablePeriod();
        mutablePeriod.setYears(1).setMonths(2).setWeeks(3).setDays(4).setHours(5).setMinutes(6).setSeconds(7);
        periodInDays = BaseSingleFieldPeriod.standardPeriodIn(mutablePeriod, millisPerUnit);
        assertEquals(7, periodInDays);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testBetweenWithNullArguments() {
        // Test between method with null Instant
        Chronology isoChrono = DateTimeUtils.getInstantChronology();
        Instant start = null;
        Instant end = isoChrono.parseInstant("2015-01-01T00:00:00.001Z");
        DurationFieldType days = DurationFieldType.days();
        BaseSingleFieldPeriod.between(start, end, days);

        // Test between method with null LocalDateTime
        LocalDateTime startDateTime = null;
        LocalDateTime endDateTime = new LocalDateTime(2015, 1, 1, 0, 0, 1, 0, isoChrono);
        BaseSingleFieldPeriod.between(startDateTime, endDateTime, days);

        // Test between method with null LocalDate
        LocalDate startDate = null;
        LocalDate endDate = new LocalDate(2015, 1, 1);
        BaseSingleFieldPeriod.between(startDate, endDate, days);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testBetweenWithInvalidArguments() {
        // Test between method with different chronologies
        Chronology gregorianChrono = DateTimeUtils.getGregorianChronology();
        Instant start = gregorianChrono.parseInstant("2015-01-01T00:00:00.001Z");
        Instant end = isoChrono.parseInstant("2015-01-01T00:00:00.001Z");
        DurationFieldType days = DurationFieldType.days();
        BaseSingleFieldPeriod.between(start, end, days);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testStandardPeriodInWithNullPeriod() {
        Chronology isoChrono = DateTimeUtils.getInstantChronology();
        long millisPerUnit = isoChrono.getMillisPerDay();
        BaseSingleFieldPeriod.standardPeriodIn(null, millisPerUnit);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testStandardPeriodInWithPeriodWithImpreciseFields() {
        Period period = new Period(1, 2, 3, 4, 5, 6, new Months(2, 3));
        Chronology isoChrono = DateTimeUtils.getInstantChronology();
        long millisPerUnit = isoChrono.getMillisPerDay();
        BaseSingleFieldPeriod.standardPeriodIn(period, millisPerUnit);
    }
}