import org.joda.time.BaseSingleFieldPeriod;
import org.joda.time.DurationFieldType;
import org.joda.time.MutablePeriod;
import org.joda.time.Period;
import org.joda.time.PeriodType;
import org.junit.Test;
import static org.junit.Assert.*;

public class BaseSingleFieldPeriodTest {

    @Test
    public void testConstructor() {
        BaseSingleFieldPeriod period = new BaseSingleFieldPeriod(5);
        assertEquals(5, period.getValue());
    }

    @Test
    public void testGetFieldType() {
        BaseSingleFieldPeriod period = new BaseSingleFieldPeriod(5);
        DurationFieldType fieldType = period.getFieldType();
        assertEquals(DurationFieldType.DAYS, fieldType);
    }

    @Test
    public void testGetPeriodType() {
        BaseSingleFieldPeriod period = new BaseSingleFieldPeriod(5);
        PeriodType periodType = period.getPeriodType();
        assertEquals(PeriodType.standard(), periodType);
    }

    @Test
    public void testSize() {
        BaseSingleFieldPeriod period = new BaseSingleFieldPeriod(5);
        int size = period.size();
        assertEquals(1, size);
    }

    @Test
    public void testGetValue() {
        BaseSingleFieldPeriod period = new BaseSingleFieldPeriod(5);
        int value = period.getValue(0);
        assertEquals(5, value);
    }

    @Test(expected = IndexOutOfBoundsException.class)
    public void testGetValueOutOfBounds() {
        BaseSingleFieldPeriod period = new BaseSingleFieldPeriod(5);
        int value = period.getValue(1);
    }

    @Test
    public void testGet() {
        BaseSingleFieldPeriod period = new BaseSingleFieldPeriod(5);
        int value = period.get(DurationFieldType.DAYS);
        assertEquals(5, value);
        value = period.get(DurationFieldType.HOURS);
        assertEquals(0, value);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testGetInvalidFieldType() {
        BaseSingleFieldPeriod period = new BaseSingleFieldPeriod(5);
        period.get(DurationFieldType.MONTHS);
    }

    @Test
    public void testEquals() {
        BaseSingleFieldPeriod period1 = new BaseSingleFieldPeriod(5);
        BaseSingleFieldPeriod period2 = new BaseSingleFieldPeriod(5);
        BaseSingleFieldPeriod period3 = new BaseSingleFieldPeriod(6);
        assertEquals(period1, period1);
        assertEquals(period1, period2);
        assertNotEquals(period1, period3);
    }

    @Test
    public void testHashCode() {
        BaseSingleFieldPeriod period1 = new BaseSingleFieldPeriod(5);
        BaseSingleFieldPeriod period2 = new BaseSingleFieldPeriod(5);
        BaseSingleFieldPeriod period3 = new BaseSingleFieldPeriod(6);
        int hashCode1 = period1.hashCode();
        int hashCode2 = period1.hashCode();
        int hashCode3 = period2.hashCode();
        int hashCode4 = period3.hashCode();
        assertEquals(hashCode1, hashCode2);
        assertNotEquals(hashCode1, hashCode3);
        assertNotEquals(hashCode1, hashCode4);
    }

    @Test
    public void testCompareTo() {
        BaseSingleFieldPeriod period1 = new BaseSingleFieldPeriod(5);
        BaseSingleFieldPeriod period2 = new BaseSingleFieldPeriod(3);
        BaseSingleFieldPeriod period3 = new BaseSingleFieldPeriod(6);
        int compareTo1 = period1.compareTo(period1);
        int compareTo2 = period1.compareTo(period2);
        int compareTo3 = period1.compareTo(period3);
        assertEquals(0, compareTo1);
        assertEquals(2, compareTo2);
        assertEquals(-1, compareTo3);
    }

    @Test
    public void testToPeriod() {
        BaseSingleFieldPeriod period = new BaseSingleFieldPeriod(5);
        Period toPeriod = period.toPeriod();
        assertEquals(5, toPeriod.getYears());
        assertEquals(0, toPeriod.getMonths());
        assertEquals(0, toPeriod.getDays());
        assertEquals(0, toPeriod.getHours());
        assertEquals(0, toPeriod.getMinutes());
        assertEquals(0, toPeriod.getSeconds());
        assertEquals(0, toPeriod.getMillis());
    }

    @Test
    public void testToMutablePeriod() {
        BaseSingleFieldPeriod period = new BaseSingleFieldPeriod(5);
        MutablePeriod toMutablePeriod = period.toMutablePeriod();
        assertEquals(5, toMutablePeriod.getYears());
        assertEquals(0, toMutablePeriod.getMonths());
        assertEquals(0, toMutablePeriod.getDays());
        assertEquals(0, toMutablePeriod.getHours());
        assertEquals(0, toMutablePeriod.getMinutes());
        assertEquals(0, toMutablePeriod.getSeconds());
        assertEquals(0, toMutablePeriod.getMillis());
    }

    @Test
    public void testBetween() {
        long startMillis = 1516051200000L; // 2017-12-27 12:00:00.000Z
        long endMillis = 1516057600000L; // 2017-12-27 16:00:00.000Z
        int daysBetween = BaseSingleFieldPeriod.between(DateTimeUtils.asReadableInstant(startMillis),
                DateTimeUtils.asReadableInstant(endMillis), DurationFieldType.DAYS);
        assertEquals(4, daysBetween);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testBetweenInvalidStart() {
        long startMillis = 0L;
        long endMillis = 1516051200000L; // 2017-12-27 12:00:00.000Z
        int daysBetween = BaseSingleFieldPeriod.between(null, DateTimeUtils.asReadableInstant(endMillis),
                DurationFieldType.DAYS);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testBetweenInvalidEnd() {
        long startMillis = 1516051200000L; // 2017-12-27 12:00:00.000Z
        long endMillis = 0L;
        int daysBetween = BaseSingleFieldPeriod.between(DateTimeUtils.asReadableInstant(startMillis),
                null, DurationFieldType.DAYS);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testBetweenInvalidFieldType() {
        long startMillis = 1516051200000L; // 2017-12-27 12:00:00.000Z
        long endMillis = 1516057600000L; // 2017-12-27 16:00:00.000Z
        int daysBetween = BaseSingleFieldPeriod.between(DateTimeUtils.asReadableInstant(startMillis),
                DateTimeUtils.asReadableInstant(endMillis), DurationFieldType.MONTHS);
    }

    @Test
    public void testStandardPeriodIn() {
        ReadablePeriod period = new MutablePeriod().withDays(5).standardizeInUTC();
        long millisPerUnit = 86400000L;
        int days = BaseSingleFieldPeriod.standardPeriodIn(period, millisPerUnit);
        assertEquals(5, days);
    }

    @Test
    public void testStandardPeriodInZeroPeriod() {
        ReadablePeriod period = Period.ZERO;
        long millisPerUnit = 86400000L;
        int days = BaseSingleFieldPeriod.standardPeriodIn(period, millisPerUnit);
        assertEquals(0, days);
    }

}