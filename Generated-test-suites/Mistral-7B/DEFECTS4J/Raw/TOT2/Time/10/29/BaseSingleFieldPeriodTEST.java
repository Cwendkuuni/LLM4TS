import org.junit.Test;
import org.joda.time.BaseSingleFieldPeriod;
import org.joda.time.DurationFieldType;
import org.joda.time.MutablePeriod;
import static org.junit.Assert.*;

public class BaseSingleFieldPeriodTest {

    @Test
    public void testSimpleMethod() {
        BaseSingleFieldPeriod period = new BaseSingleFieldPeriod(5);
        DurationFieldType fieldType = period.getFieldType();
        assertEquals(5, period.getValue());
        assertEquals(fieldType, period.getFieldType());
        assertEquals(DurationFieldType.DAYS, fieldType);
        Period p = period.toPeriod();
        assertEquals(5, p.getSeconds());
        assertEquals(5, period.compareTo(new BaseSingleFieldPeriod(5)));
        assertEquals(0, period.compareTo(new BaseSingleFieldPeriod(5)));
        assertEquals(5L, period.toMutablePeriod().getMillis());
    }

    @Test(expected = IllegalArgumentException.class)
    public void testGetFieldsWithDifferentSize() {
        ReadablePartial start = new org.joda.time.LocalDate(2012, 1, 1);
        ReadablePartial end = new org.joda.time.LocalDate(2012, 1, 3);
        int value = BaseSingleFieldPeriod.between(start, end, null);
        new BaseSingleFieldPeriod(value);
    }

    @Test
    public void testStandardPeriodIn() {
        ReadablePeriod period = new MutablePeriod();
        period.setYears(2);
        period.setMonths(-3);
        period.setWeeks(1);
        period.setDays(4);
        long millisPerUnit = 86400000L;
        int value = BaseSingleFieldPeriod.standardPeriodIn(period, millisPerUnit);
        assertEquals(331, value);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testStandardPeriodInWithImprecisePeriod() {
        ReadablePeriod period = new MutablePeriod();
        period.setSeconds(2);
        period.setMillis(100);
        long millisPerUnit = 1000L;
        BaseSingleFieldPeriod.standardPeriodIn(period, millisPerUnit);
    }

    @Test
    public void testGetValueForFieldTypeThatIsNotSupported() {
        BaseSingleFieldPeriod period = new BaseSingleFieldPeriod(1);
        DurationFieldType fieldType = DurationFieldType.HOURS;
        assertEquals(0, period.get(fieldType));
    }

    @Test
    public void testIsSupportedForFieldTypeThatIsSupported() {
        BaseSingleFieldPeriod period = new BaseSingleFieldPeriod(1);
        DurationFieldType fieldType = DurationFieldType.DAYS;
        assertTrue(period.isSupported(fieldType));
    }

    @Test
    public void testIsSupportedForFieldTypeThatIsNotSupported() {
        BaseSingleFieldPeriod period = new BaseSingleFieldPeriod(1);
        DurationFieldType fieldType = DurationFieldType.HOURS;
        assertFalse(period.isSupported(fieldType));
    }

    @Test
    public void testToPeriodReturnsZeroPeriodForZeroValue() {
        BaseSingleFieldPeriod period = new BaseSingleFieldPeriod(0);
        Period p = period.toPeriod();
        assertEquals(Period.ZERO, p);
    }

    @Test
    public void testToPeriodConvertsValuesToStandardDurations() {
        BaseSingleFieldPeriod period = new BaseSingleFieldPeriod(7);
        Period p = period.toPeriod();
        assertEquals(7, p.getDays());
    }

    @Test
    public void testToMutablePeriodReturnsNewInstance() {
        BaseSingleFieldPeriod period = new BaseSingleFieldPeriod(5);
        MutablePeriod mp = period.toMutablePeriod();
        assertNotSame(period, mp);
    }
}