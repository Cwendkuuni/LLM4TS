import org.junit.Test;
import org.joda.time.BaseSingleFieldPeriod;
import org.joda.time.DurationFieldType;
import org.joda.time.MutablePeriod;
import org.joda.time.Period;
import org.joda.time.PeriodType;
import org.joda.time.chrono.ISOChronology;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertThrows;

public class BaseSingleFieldPeriodTest {

    @Test
    public void testBasicFunctionality() {
        BaseSingleFieldPeriod period = new BaseSingleFieldPeriod(5);
        DurationFieldType fieldType = period.getFieldType();
        PeriodType periodType = period.getPeriodType();

        assertEquals(5, period.getValue());
        assertEquals(fieldType, period.getFieldType());
        assertEquals(periodType, period.getPeriodType());
        assertEquals(1, period.size());
        assertEquals(fieldType, period.getFieldType(0));

        Period p = period.toPeriod();
        MutablePeriod mp = period.toMutablePeriod();

        assertEquals(p, mp.toImmutable());
    }

    @Test
    public void testGetFieldTypeAndPeriodType() {
        BaseSingleFieldPeriod period = new BaseSingleFieldPeriod(5, DurationFieldType.HOURS);
        assertEquals(DurationFieldType.HOURS, period.getFieldType());
        assertEquals(PeriodType.standard(), period.getPeriodType());

        period = new BaseSingleFieldPeriod(5, DurationFieldType.DAYS);
        assertEquals(DurationFieldType.DAYS, period.getFieldType());
        assertEquals(PeriodType.standard(), period.getPeriodType());

        period = new BaseSingleFieldPeriod(5, DurationFieldType.MINUTES);
        assertEquals(DurationFieldType.MINUTES, period.getFieldType());
        assertEquals(PeriodType.standard(), period.getPeriodType());

        period = new BaseSingleFieldPeriod(5, DurationFieldType.MILLISECONDS);
        assertEquals(DurationFieldType.MILLISECONDS, period.getFieldType());
        assertEquals(PeriodType.standard(), period.getPeriodType());
    }

    @Test
    public void testGetValue() {
        BaseSingleFieldPeriod period = new BaseSingleFieldPeriod(5);
        assertEquals(5, period.getValue());

        period = new BaseSingleFieldPeriod(0);
        assertEquals(0, period.getValue());

        period = new BaseSingleFieldPeriod(-5);
        assertEquals(-5, period.getValue());
    }

    @Test
    public void testSetValue() {
        BaseSingleFieldPeriod period = new BaseSingleFieldPeriod(5);
        period.setValue(10);
        assertEquals(10, period.getValue());
    }

    @Test(expected = UnsupportedOperationException.class)
    public void testSetValueThrowsException() {
        BaseSingleFieldPeriod period = new BaseSingleFieldPeriod(5);
        period.setFinal(); // assume there is a method to set it final
        period.setValue(10);
    }

    @Test
    public void testToPeriod() {
        BaseSingleFieldPeriod period = new BaseSingleFieldPeriod(5);
        Period p = period.toPeriod();
        assertEquals(5, p.get(DurationFieldType.DAYS));
        assertEquals(PeriodType.standard(), p.getPeriodType());
    }

    @Test
    public void testToMutablePeriod() {
        BaseSingleFieldPeriod period = new BaseSingleFieldPeriod(5);
        MutablePeriod mp = period.toMutablePeriod();
        assertEquals(5, mp.get(DurationFieldType.DAYS));
        assertEquals(PeriodType.standard(), mp.getPeriodType());
    }

    @Test
    public void testEquals() {
        BaseSingleFieldPeriod period1 = new BaseSingleFieldPeriod(5);
        BaseSingleFieldPeriod period2 = new BaseSingleFieldPeriod(5);

        assertEquals(period1, period2);
        assertEquals(period1.hashCode(), period2.hashCode());

        BaseSingleFieldPeriod period3 = new BaseSingleFieldPeriod(6);
        assertNotEquals(period1, period3);
        assertNotEquals(period1.hashCode(), period3.hashCode());

        BaseSingleFieldPeriod period4 = null;
        assertNotEquals(period1, period4);
        assertNotEquals(period1.hashCode(), period4.hashCode());
    }

    @Test
    public void testCompareTo() {
        BaseSingleFieldPeriod period1 = new BaseSingleFieldPeriod(5);
        BaseSingleFieldPeriod period2 = new BaseSingleFieldPeriod(3);
        BaseSingleFieldPeriod period3 = new BaseSingleFieldPeriod(5);

        assertEquals(0, period1.compareTo(period3));
        assertEquals(2, period1.compareTo(period2));
    }

    @Test
    public void testBetween() {
        ReadableInstant start = ISOChronology.getInstanceUTC().getUTCInstants().get(0);
        ReadableInstant end = ISOChronology.getInstanceUTC().getUTCInstants().get(1);

        int periodValue = BaseSingleFieldPeriod.between(start, end, DurationFieldType.MILLISECONDS);
        BaseSingleFieldPeriod period = new BaseSingleFieldPeriod(periodValue);

        long expected = 86400000L; // one day in milliseconds
        assertEquals(expected, period.getValue() * getFieldType(DurationFieldType.MILLISECONDS).getUnitMillis());
    }

    @Test
    public void testStandardPeriodIn() {
        ReadablePeriod period = Period.parse("PT2S"); // 2 seconds
        int millisPerUnit = 1000;
        int standardPeriodInMilliseconds = BaseSingleFieldPeriod.standardPeriodIn(period, millisPerUnit);
        assertEquals(2, standardPeriodInMilliseconds);
    }

    private DurationFieldType getFieldType(DurationFieldType fieldType) {
        return new BaseSingleFieldPeriod(0).getFieldType();
    }

    @Test(expected = IllegalArgumentException.class)
    public void testBetweenInvalidInstants() {
        ReadableInstant start = null;
        ReadableInstant end = ISOChronology.getInstanceUTC().getUTCInstants().get(0);
        BaseSingleFieldPeriod.between(start, end, DurationFieldType.MILLISECONDS);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testBetweenInvalidFieldType() {
        ReadableInstant start = ISOChronology.getInstanceUTC().getUTCInstants().get(0);
        ReadableInstant end = ISOChronology.getInstanceUTC().getUTCInstants().get(1);
        BaseSingleFieldPeriod.between(start, end, null);
    }
}