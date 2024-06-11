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
        assertEquals(DurationFieldType.DAYS, period.getFieldType());
    }

    @Test
    public void testGetFieldType() {
        BaseSingleFieldPeriod period = new BaseSingleFieldPeriod(5);
        assertEquals(DurationFieldType.DAYS, period.getFieldType());
        // Test with a subclass
        MySingleFieldPeriod myPeriod = new MySingleFieldPeriod(5);
        assertEquals(DurationFieldType.HOURS, myPeriod.getFieldType());
    }

    @Test
    public void testGetPeriodType() {
        BaseSingleFieldPeriod period = new BaseSingleFieldPeriod(5);
        assertEquals(PeriodType.standard(), period.getPeriodType());
    }

    @Test
    public void testSize() {
        BaseSingleFieldPeriod period = new BaseSingleFieldPeriod(5);
        assertEquals(1, period.size());
    }

    @Test
    public void testGetValue() {
        BaseSingleFieldPeriod period = new BaseSingleFieldPeriod(5);
        assertEquals(5, period.getValue());
    }

    @Test
    public void testGetValueWithFieldType() {
        BaseSingleFieldPeriod period = new BaseSingleFieldPeriod(5);
        assertEquals(5, period.get(DurationFieldType.DAYS));
        assertEquals(0, period.get(DurationFieldType.HOURS));
    }

    @Test
    public void testIsSupported() {
        BaseSingleFieldPeriod period = new BaseSingleFieldPeriod(5);
        assertTrue(period.isSupported(DurationFieldType.DAYS));
        assertFalse(period.isSupported(DurationFieldType.HOURS));
    }

    @Test
    public void testToPeriod() {
        BaseSingleFieldPeriod period = new BaseSingleFieldPeriod(5);
        Period result = period.toPeriod();
        assertEquals(5, result.getHours());
        assertEquals(0, result.getMinutes());
        assertEquals(0, result.getSeconds());
        assertEquals(0, result.getMillis());
    }

    @Test
    public void testToMutablePeriod() {
        BaseSingleFieldPeriod period = new BaseSingleFieldPeriod(5);
        MutablePeriod result = period.toMutablePeriod();
        result.add(DurationFieldType.HOURS, 1);
        assertEquals(6, result.getHours());
        assertEquals(0, result.getMinutes());
        assertEquals(0, result.getSeconds());
        assertEquals(0, result.getMillis());
    }

    @Test(expected = IllegalArgumentException.class)
    public void testEqualsNull() {
        BaseSingleFieldPeriod period = new BaseSingleFieldPeriod(5);
        assertFalse(period.equals(null));
    }

    @Test
    public void testEqualsSameInstance() {
        BaseSingleFieldPeriod period = new BaseSingleFieldPeriod(5);
        assertTrue(period.equals(period));
    }

    @Test
    public void testEqualsDifferentInstance() {
        BaseSingleFieldPeriod period1 = new BaseSingleFieldPeriod(5);
        BaseSingleFieldPeriod period2 = new BaseSingleFieldPeriod(10);
        assertFalse(period1.equals(period2));
    }

    @Test
    public void testEqualsDifferentType() {
        BaseSingleFieldPeriod period1 = new BaseSingleFieldPeriod(5);
        Period period2 = Period.ofDays(5);
        assertFalse(period1.equals(period2));
    }

    @Test
    public void testHashCode() {
        BaseSingleFieldPeriod period1 = new BaseSingleFieldPeriod(5);
        BaseSingleFieldPeriod period2 = new BaseSingleFieldPeriod(5);
        assertEquals(period1.hashCode(), period2.hashCode());
    }

    @Test
    public void testCompareToZero() {
        BaseSingleFieldPeriod period = new BaseSingleFieldPeriod(5);
        assertEquals(0, period.compareTo(new BaseSingleFieldPeriod(0)));
    }

    @Test
    public void testCompareToNegative() {
        BaseSingleFieldPeriod period = new BaseSingleFieldPeriod(5);
        assertEquals(-1, period.compareTo(new BaseSingleFieldPeriod(10)));
    }

    @Test
    public void testCompareToPositive() {
        BaseSingleFieldPeriod period = new BaseSingleFieldPeriod(5);
        assertEquals(1, period.compareTo(new BaseSingleFieldPeriod(0)));
    }

    @Test
    public void testBetween() {
        ReadableInstant start = DateTimeUtils.currentInstant();
        ReadableInstant end = start.plus(Duration.standardMinutes(1));
        int result = BaseSingleFieldPeriod.between(start, end, DurationFieldType.MINUTES);
        assertEquals(1, result);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testBetweenNullStart() {
        ReadableInstant start = null;
        ReadableInstant end = DateTimeUtils.currentInstant();
        BaseSingleFieldPeriod.between(start, end, DurationFieldType.MINUTES);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testBetweenNullEnd() {
        ReadableInstant start = DateTimeUtils.currentInstant();
        ReadableInstant end = null;
        BaseSingleFieldPeriod.between(start, end, DurationFieldType.MINUTES);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testBetweenDifferentTypes() {
        ReadableInstant start = DateTimeUtils.currentInstant();
        ReadablePartial end = DateTimeUtils.currentLocalDate();
        BaseSingleFieldPeriod.between(start, end, DurationFieldType.MINUTES);
    }

    @Test
    public void testStandardPeriodIn() {
        ReadablePeriod period = Period.ofDays(1).withHours(2);
        int result = BaseSingleFieldPeriod.standardPeriodIn(period, Duration.oneHour().toMillis());
        assertEquals(3, result);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testStandardPeriodInImprecisePeriod() {
        ReadablePeriod period = Period.ofMonths(1);
        long millisPerUnit = Duration.oneHour().toMillis();
        BaseSingleFieldPeriod.standardPeriodIn(period, millisPerUnit);
    }
}

class MySingleFieldPeriod extends BaseSingleFieldPeriod {

    public MySingleFieldPeriod(int period) {
        super(period);
    }

    @Override
    public DurationFieldType getFieldType() {
        return DurationFieldType.HOURS;
    }
}