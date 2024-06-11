import org.joda.time.BaseSingleFieldPeriod;
import org.joda.time.DurationFieldType;
import org.joda.time.MutablePeriod;
import org.joda.time.Period;
import org.joda.time.PeriodType;
import org.joda.time.DateTimeUtils;
import org.junit.Test;
import static org.junit.Assert.*;

public class BaseSingleFieldPeriodTest {

    @Test
    public void testBaseSingleFieldPeriod() {
        // Test creating an instance with a given period
        BaseSingleFieldPeriod period = new BaseSingleFieldPeriod(5);
        assertEquals(5, period.getValue());
        assertEquals(PeriodType.standard(), period.getPeriodType());
        assertEquals(DurationFieldType.DAYS, period.getFieldType());

        // Test creating a mutable period from the immutable period
        MutablePeriod mutablePeriod = period.toMutablePeriod();
        mutablePeriod.setFields(period);
        assertEquals(mutablePeriod, period.toPeriod());

        // Test converting to a Period object
        Period convertedPeriod = period.toPeriod();
        assertEquals(5, convertedPeriod.getDays());
        assertEquals(PeriodType.standard(), convertedPeriod.getPeriodType());

        // Test equals and hashCode methods
        BaseSingleFieldPeriod period1 = new BaseSingleFieldPeriod(5);
        BaseSingleFieldPeriod period2 = new BaseSingleFieldPeriod(5);
        BaseSingleFieldPeriod period3 = new BaseSingleFieldPeriod(6);
        assertEquals(period1, period1);
        assertEquals(period1, period2);
        assertNotSame(period1, period3);
        assertEquals(period1.hashCode(), period2.hashCode());
        assertNotEquals(period1.hashCode(), period3.hashCode());

        // Test comparison methods
        assertEquals(0, period1.compareTo(period1));
        assertEquals(1, period1.compareTo(period3));
        assertEquals(-1, period3.compareTo(period1));

        // Test getting the value of a field
        DurationFieldType fieldType = DurationFieldType.HOURS;
        assertEquals(0, period.get(fieldType));
        period = new BaseSingleFieldPeriod(10);
        assertEquals(10, period.get(fieldType));

        // Test checking if a field is supported
        assertTrue(period.isSupported(fieldType));
        assertFalse(period.isSupported(DurationFieldType.MINUTES));

        // Test creating a new instance using the static method between
        ReadableInstant start = DateTimeUtils.parseLocalDate("2022-01-01T00:00:00.000Z").toInstant();
        ReadableInstant end = DateTimeUtils.parseLocalDate("2022-01-02T00:00:00.000Z").toInstant();
        BaseSingleFieldPeriod betweenPeriod = BaseSingleFieldPeriod.between(start, end, DurationFieldType.DAYS);
        assertEquals(1, betweenPeriod.getValue());

        // Test creating a new instance using the standardPeriodIn method
        ReadablePeriod standardPeriod = Period.parse("P1D");
        BaseSingleFieldPeriod standardPeriodInstance = new BaseSingleFieldPeriod(BaseSingleFieldPeriod.standardPeriodIn(standardPeriod, DateTimeUtils.MILLIS_PER_DAY));
        assertEquals(1, standardPeriodInstance.getValue());
    }
}