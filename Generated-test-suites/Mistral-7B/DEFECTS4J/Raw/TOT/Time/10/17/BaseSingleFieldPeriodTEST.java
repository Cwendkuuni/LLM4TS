import org.junit.Test;
import org.joda.time.BaseSingleFieldPeriod;
import org.joda.time.DurationFieldType;
import org.joda.time.MutablePeriod;
import org.joda.time.Period;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertThrows;

public class BaseSingleFieldPeriodTest {

    @Test
    public void testBaseSingleFieldPeriod() {
        // Test instant to instant period
        ReadableInstant start = DateTimeUtils.parseLocalDateTime("1972-01-01T00:00:00.000Z").toInstant();
        ReadableInstant end = DateTimeUtils.parseLocalDateTime("1972-01-02T00:01:00.001Z").toInstant();
        BaseSingleFieldPeriod period = BaseSingleFieldPeriod.between(start, end, DurationFieldType.DAYS);
        assertEquals(1, period.getValue());
        assertEquals(DurationFieldType.DAYS, period.getFieldType());
        assertEquals(PeriodType.standard(), period.getPeriodType());
        assertEquals(Period.ONE_DAY, period.toPeriod());

        // Test contiguous partial to partial period
        ReadablePartial startPartial = DateTimeUtils.parseLocalDate("1972-01-01").toLocalDateTimeAtStartOfDay().toLocalDate();
        ReadablePartial endPartial = DateTimeUtils.parseLocalDate("1972-01-02").toLocalDateTimeAtStartOfDay().toLocalDate();
        BaseSingleFieldPeriod periodPartial = BaseSingleFieldPeriod.between(startPartial, endPartial, Period.DAYS);
        assertEquals(1, periodPartial.getValue());
        assertEquals(DurationFieldType.DAYS, periodPartial.getFieldType());
        assertEquals(PeriodType.standard(), periodPartial.getPeriodType());
        assertEquals(Period.ONE_DAY, periodPartial.toPeriod());

        // Test non-contiguous partial to partial period
        ReadablePartial startPartialNonContiguous = DateTimeUtils.parseLocalDate("1972-01-01").toLocalDateTimeAtStartOfDay().toLocalDate();
        ReadablePartial endPartialNonContiguous = DateTimeUtils.parseLocalDate("1972-01-03").toLocalDateTimeAtStartOfDay().toLocalDate();
        assertThrows(IllegalArgumentException.class, () -> BaseSingleFieldPeriod.between(startPartialNonContiguous, endPartialNonContiguous, Period.DAYS));

        // Test null instant
        assertThrows(IllegalArgumentException.class, () -> BaseSingleFieldPeriod.between(null, end, DurationFieldType.DAYS));
        assertThrows(IllegalArgumentException.class, () -> BaseSingleFieldPeriod.between(start, null, DurationFieldType.DAYS));

        // Test null partial
        assertThrows(IllegalArgumentException.class, () -> BaseSingleFieldPeriod.between(null, endPartial, Period.DAYS));
        assertThrows(IllegalArgumentException.class, () -> BaseSingleFieldPeriod.between(startPartial, null, Period.DAYS));

        // Test invalid chronology
        ReadableInstant invalidStart = DateTimeUtils.parseLocalDateTime("1972-01-01T00:00:00.000Z").toInstant();
        Chronology invalidChronology = DateTimeUtils.getInstantChronology(invalidStart).plusProvidedFields(DateTimeConstants.YEARS, 100);
        assertNull(invalidChronology.getCalendarInstance());
        assertThrows(IllegalArgumentException.class, () -> BaseSingleFieldPeriod.between(start, end, DurationFieldType.DAYS, invalidChronology));

        // Test null field type
        assertThrows(NullPointerException.class, () -> BaseSingleFieldPeriod.between(start, end, null));

        // Test zero period
        BaseSingleFieldPeriod zeroPeriod = new BaseSingleFieldPeriod(0);
        assertEquals(0, zeroPeriod.getValue());
        assertEquals(DurationFieldType.DAYS, zeroPeriod.getFieldType());
        assertEquals(PeriodType.standard(), zeroPeriod.getPeriodType());
        assertEquals(Period.ZERO, zeroPeriod.toPeriod());
    }
}