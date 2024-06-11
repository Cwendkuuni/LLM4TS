import org.junit.Test;
import org.junit.Assert.*;
import org.joda.time.BaseSingleFieldPeriod;
import org.joda.time.DurationFieldType;

public class BaseSingleFieldPeriodTest {

    @Test
    public void testGetFieldType() {
        BaseSingleFieldPeriod period = new MySingleFieldPeriod(5);
        DurationFieldType fieldType = period.getFieldType();
        Assert.assertSame(DurationFieldType.days(), fieldType);
    }

    @Test
    public void testGetPeriodType() {
        BaseSingleFieldPeriod period = new MySingleFieldPeriod(5);
        PeriodType periodType = period.getPeriodType();
        Assert.assertSame(PeriodType.standard(), periodType);
    }

    @Test
    public void testSize() {
        BaseSingleFieldPeriod period = new MySingleFieldPeriod(5);
        int size = period.size();
        Assert.assertEquals(1, size);
    }

    @Test(expected = IndexOutOfBoundsException.class)
    public void testGetValueNegativeIndex() {
        BaseSingleFieldPeriod period = new MySingleFieldPeriod(5);
        period.getValue(-1);
    }

    @Test(expected = IndexOutOfBoundsException.class)
    public void testGetFieldTypeNegativeIndex() {
        BaseSingleFieldPeriod period = new MySingleFieldPeriod(5);
        period.getFieldType(-1);
    }

    @Test
    public void testGetValue() {
        BaseSingleFieldPeriod period = new MySingleFieldPeriod(5);
        int value = period.getValue();
        Assert.assertEquals(5, value);
    }

    @Test
    public void testIsSupported() {
        BaseSingleFieldPeriod period = new MySingleFieldPeriod(5);
        DurationFieldType fieldType = DurationFieldType.hours();
        boolean supported = period.isSupported(fieldType);
        Assert.assertFalse(supported);

        DurationFieldType fieldType2 = DurationFieldType.days();
        boolean supported2 = period.isSupported(fieldType2);
        Assert.assertTrue(supported2);
    }

    @Test
    public void testToPeriod() {
        BaseSingleFieldPeriod period = new MySingleFieldPeriod(5);
        Period periodObj = period.toPeriod();
        Assert.assertEquals(5, periodObj.getDays());
    }

    @Test
    public void testToMutablePeriod() {
        BaseSingleFieldPeriod period = new MySingleFieldPeriod(5);
        MutablePeriod mutablePeriod = period.toMutablePeriod();
        Assert.assertEquals(5, mutablePeriod.getDays());
    }

    @Test
    public void testEquals() {
        BaseSingleFieldPeriod period1 = new MySingleFieldPeriod(5);
        BaseSingleFieldPeriod period2 = new MySingleFieldPeriod(5);
        BaseSingleFieldPeriod period3 = new MySingleFieldPeriod(6);
        BaseSingleFieldPeriod period4 = null;

        Assert.assertTrue(period1.equals(period1));
        Assert.assertTrue(period1.equals(period2));
        Assert.assertFalse(period1.equals(period3));
        Assert.assertFalse(period1.equals(period4));
    }

    @Test
    public void testHashCode() {
        BaseSingleFieldPeriod period1 = new MySingleFieldPeriod(5);
        BaseSingleFieldPeriod period2 = new MySingleFieldPeriod(5);
        BaseSingleFieldPeriod period3 = new MySingleFieldPeriod(6);

        int hashCode1 = period1.hashCode();
        int hashCode2 = period1.hashCode();
        int hashCode3 = period2.hashCode();

        Assert.assertEquals(hashCode1, hashCode2);
        Assert.assertNotEqual(hashCode1, hashCode3);
    }

    @Test
    public void testCompareTo() {
        BaseSingleFieldPeriod period1 = new MySingleFieldPeriod(5);
        BaseSingleFieldPeriod period2 = new MySingleFieldPeriod(6);
        BaseSingleFieldPeriod period3 = new MySingleFieldPeriod(5);

        int compareTo1 = period1.compareTo(period2);
        int compareTo2 = period2.compareTo(period1);
        int compareTo3 = period1.compareTo(period3);

        Assert.lessThan(0, compareTo1);
        Assert.greaterThan(0, compareTo2);
        Assert.equalTo(0, compareTo3);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testBetweenNullStart() {
        ReadableInstant start = null;
        ReadableInstant end = new Instant(System.currentTimeMillis());
        DurationFieldType field = DurationFieldType.days();
        BaseSingleFieldPeriod.between(start, end, field);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testBetweenNullEnd() {
        ReadableInstant start = new Instant(System.currentTimeMillis());
        ReadableInstant end = null;
        DurationFieldType field = DurationFieldType.days();
        BaseSingleFieldPeriod.between(start, end, field);
    }

    @Test
    public void testStandardPeriodIn() {
        BaseSingleFieldPeriod period = new MySingleFieldPeriod(7);
        long millisPerUnit = 86400000L;
        int periodInUnits = BaseSingleFieldPeriod.standardPeriodIn(period, millisPerUnit);
        Assert.assertEquals(7, periodInUnits);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testStandardPeriodInInvalidPeriod() {
        BaseSingleFieldPeriod period = new MySingleFieldPeriod(new int[]{1, 2, 3});
        long millisPerUnit = 86400000L;
        BaseSingleFieldPeriod.standardPeriodIn(period, millisPerUnit);
    }

}

class MySingleFieldPeriod extends BaseSingleFieldPeriod {
    MySingleFieldPeriod(int period) {
        super(period);
    }
}