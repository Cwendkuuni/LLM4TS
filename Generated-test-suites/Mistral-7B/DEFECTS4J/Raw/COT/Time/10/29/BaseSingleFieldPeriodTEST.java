import org.joda.time.BaseSingleFieldPeriod;
import org.joda.time.DurationFieldType;
import org.joda.time.MutablePeriod;
import org.joda.time.Period;
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

    @Test(expected = IndexOutOfBoundsException.class)
    public void testGetFieldTypeWrongIndex() {
        BaseSingleFieldPeriod period = new BaseSingleFieldPeriod(5);
        period.getFieldType(2);
    }

    @Test(expected = IndexOutOfBoundsException.class)
    public void testgetValueWrongIndex() {
        BaseSingleFieldPeriod period = new BaseSingleFieldPeriod(5);
        period.getValue(2);
    }

    @Test
    public void testGetValue() {
        BaseSingleFieldPeriod period = new BaseSingleFieldPeriod(5);
        int value = period.getValue();
        assertEquals(5, value);
    }

    @Test
    public void testEquals() {
        BaseSingleFieldPeriod period1 = new BaseSingleFieldPeriod(5);
        BaseSingleFieldPeriod period2 = new BaseSingleFieldPeriod(5);
        BaseSingleFieldPeriod period3 = new BaseSingleFieldPeriod(6);
        BaseSingleFieldPeriod period4 = new BaseSingleFieldPeriod(5, DurationFieldType.HOURS);
        assertEquals(period1, period1);
        assertEquals(period1, period2);
        assertFalse(period1.equals(period3));
        assertFalse(period1.equals(period4));
    }

    @Test
    public void testHashCode() {
        BaseSingleFieldPeriod period1 = new BaseSingleFieldPeriod(5);
        BaseSingleFieldPeriod period2 = new BaseSingleFieldPeriod(5);
        BaseSingleFieldPeriod period3 = new BaseSingleFieldPeriod(6);
        BaseSingleFieldPeriod period4 = new BaseSingleFieldPeriod(5, DurationFieldType.HOURS);
        int hashCode1 = period1.hashCode();
        int hashCode2 = period1.hashCode();
        int hashCode3 = period3.hashCode();
        int hashCode4 = period4.hashCode();
        assertEquals(hashCode1, hashCode2);
        assertNotEquals(hashCode1, hashCode3);
        assertNotEquals(hashCode1, hashCode4);
    }

    @Test
    public void testCompareTo() {
        BaseSingleFieldPeriod period1 = new BaseSingleFieldPeriod(5);
        BaseSingleFieldPeriod period2 = new BaseSingleFieldPeriod(5);
        BaseSingleFieldPeriod period3 = new BaseSingleFieldPeriod(6);
        BaseSingleFieldPeriod period4 = new BaseSingleFieldPeriod(4);
        assertEquals(0, period1.compareTo(period2));
        assertEquals(-1, period1.compareTo(period3));
        assertEquals(1, period3.compareTo(period1));
        assertEquals(-1, period1.compareTo(period4));
        assertEquals(1, period4.compareTo(period1));
    }

    @Test
    public void testToPeriod() {
        BaseSingleFieldPeriod period = new BaseSingleFieldPeriod(5);
        Period result = period.toPeriod();
        assertEquals(5, result.getDays());
    }

    @Test
    public void testToMutablePeriod() {
        BaseSingleFieldPeriod period = new BaseSingleFieldPeriod(5);
        MutablePeriod result = period.toMutablePeriod();
        result.add(Period.DAYS, 2);
        assertEquals(7, result.toPeriod().getDays());
    }

    @Test(expected = IllegalArgumentException.class)
    public void testBetweenNullStart() {
        BaseSingleFieldPeriod.between(null, null, DurationFieldType.DAYS);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testBetweenNullEnd() {
        ReadableInstant start = null;
        BaseSingleFieldPeriod.between(start, null, DurationFieldType.DAYS);
    }

    @Test
    public void testBetween() {
        ReadableInstant start = DateTimeUtils.parseDate("2015-01-01T00:00:01.123Z").toInstant();
        ReadableInstant end = DateTimeUtils.parseDate("2015-01-02T00:00:02.123Z").toInstant();
        int result = BaseSingleFieldPeriod.between(start, end, DurationFieldType.DAYS);
        assertEquals(2, result);
    }

    @Test
    public void testStandardPeriodInNullPeriod() {
        ReadablePeriod period = null;
        long millisPerUnit = 1000 * 60 * 60 * 24;
        int result = BaseSingleFieldPeriod.standardPeriodIn(period, millisPerUnit);
        assertEquals(0, result);
    }

    @Test
    public void testStandardPeriodIn() {
        ReadablePeriod period = Period.parse("5 days, 6 hours, 7 minutes, 8 seconds");
        long millisPerUnit = 1000 * 60 * 60 * 24;
        int result = BaseSingleFieldPeriod.standardPeriodIn(period, millisPerUnit);
        assertEquals(12, result);
    }

}