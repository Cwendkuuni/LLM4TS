import org.junit.Assert;
import org.junit.Test;
import org.joda.time.BaseSingleFieldPeriod;
import org.joda.time.DurationFieldType;

public class BaseSingleFieldPeriodTest {

    @Test
    public void testConstructorWithInteger() {
        BaseSingleFieldPeriod period = new BaseSingleFieldPeriod(10);
        Assert.assertEquals(10, period.getValue());
    }

    @Test(expected = IllegalArgumentException.class)
    public void testConstructorWithNullValue() {
        new BaseSingleFieldPeriod(null);
    }

    @Test
    public void testGetFieldType() {
        BaseSingleFieldPeriod period = new BaseSingleFieldPeriod(10);
        DurationFieldType fieldType = period.getFieldType();
        Assert.assertNotNull(fieldType);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testGetFieldTypeWithNull() {
        BaseSingleFieldPeriod period = new BaseSingleFieldPeriod(10);
        Assert.assertNull(period.getFieldType(1));
    }

    @Test
    public void testGetPeriodType() {
        BaseSingleFieldPeriod period = new BaseSingleFieldPeriod(10);
        PeriodType periodType = period.getPeriodType();
        Assert.assertNotNull(periodType);
    }

    @Test(expected = IndexOutOfBoundsException.class)
    public void testGetValueWithInvalidIndex() {
        BaseSingleFieldPeriod period = new BaseSingleFieldPeriod(10);
        period.getValue(2);
    }

    @Test(expected = IndexOutOfBoundsException.class)
    public void testGetFieldTypeWithInvalidIndex() {
        BaseSingleFieldPeriod period = new BaseSingleFieldPeriod(10);
        period.getFieldType(2);
    }

    @Test
    public void testGetValue() {
        BaseSingleFieldPeriod period = new BaseSingleFieldPeriod(10);
        int value = period.getValue();
        Assert.assertEquals(10, value);
    }

    @Test
    public void testSetValue() {
        BaseSingleFieldPeriod period = new BaseSingleFieldPeriod(10);
        period.setValue(5);
        int value = period.getValue();
        Assert.assertEquals(5, value);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testSetValueWithNegativeValue() {
        BaseSingleFieldPeriod period = new BaseSingleFieldPeriod(10);
        period.setValue(-5);
    }

    @Test
    public void testSize() {
        BaseSingleFieldPeriod period = new BaseSingleFieldPeriod(10);
        int size = period.size();
        Assert.assertEquals(1, size);
    }

    @Test(expected = IndexOutOfBoundsException.class)
    public void testGetFieldTypeWithInvalidIndex2() {
        BaseSingleFieldPeriod period = new BaseSingleFieldPeriod(10);
        period.getFieldType(2);
    }

    @Test
    public void testIsSupported() {
        BaseSingleFieldPeriod period = new BaseSingleFieldPeriod(10);
        DurationFieldType fieldType = DurationFieldType.weeks();
        boolean supported = period.isSupported(fieldType);
        Assert.assertFalse(supported);
    }

    @Test
    public void testIsSupportedWithSameFieldType() {
        BaseSingleFieldPeriod period = new BaseSingleFieldPeriod(10);
        DurationFieldType fieldType = period.getFieldType();
        boolean supported = period.isSupported(fieldType);
        Assert.assertTrue(supported);
    }

    @Test
    public void testToPeriod() {
        BaseSingleFieldPeriod period = new BaseSingleFieldPeriod(10);
        Period result = period.toPeriod();
        Assert.assertNotNull(result);
        Assert.assertEquals(10, result.getMillis());
    }

    @Test
    public void testToMutablePeriod() {
        BaseSingleFieldPeriod period = new BaseSingleFieldPeriod(10);
        MutablePeriod result = period.toMutablePeriod();
        Assert.assertNotNull(result);
        Assert.assertEquals(10, result.getValue(period.getFieldType()));
    }

    @Test
    public void testEquals() {
        BaseSingleFieldPeriod period1 = new BaseSingleFieldPeriod(10);
        BaseSingleFieldPeriod period2 = new BaseSingleFieldPeriod(10);
        BaseSingleFieldPeriod period3 = new BaseSingleFieldPeriod(5);
        BaseSingleFieldPeriod period4 = new BaseSingleFieldPeriod(10);
        BaseSingleFieldPeriod period5 = null;

        Assert.assertTrue(period1.equals(period1));
        Assert.assertTrue(period1.equals(period4));
        Assert.assertFalse(period1.equals(period2));
        Assert.assertFalse(period1.equals(period3));
        Assert.assertFalse(period1.equals(period5));
    }

    @Test
    public void testHashCode() {
        BaseSingleFieldPeriod period1 = new BaseSingleFieldPeriod(10);
        BaseSingleFieldPeriod period2 = new BaseSingleFieldPeriod(10);
        BaseSingleFieldPeriod period3 = new BaseSingleFieldPeriod(5);

        int hashCode1 = period1.hashCode();
        int hashCode2 = period1.hashCode();
        int hashCode3 = period2.hashCode();
        int hashCode4 = period3.hashCode();

        Assert.assertEquals(hashCode1, hashCode2);
        Assert.assertNotEquals(hashCode1, hashCode3);
    }

    @Test
    public void testCompareToWithSamePeriod() {
        BaseSingleFieldPeriod period1 = new BaseSingleFieldPeriod(10);
        BaseSingleFieldPeriod period2 = new BaseSingleFieldPeriod(10);

        int result = period1.compareTo(period2);
        Assert.assertEquals(0, result);
    }

    @Test
    public void testCompareToWithGreaterPeriod() {
        BaseSingleFieldPeriod period1 = new BaseSingleFieldPeriod(10);
        BaseSingleFieldPeriod period2 = new BaseSingleFieldPeriod(15);

        int result = period1.compareTo(period2);
        Assert.assertEquals(-5, result);
    }

    @Test
    public void testCompareToWithSmallerPeriod() {
        BaseSingleFieldPeriod period1 = new BaseSingleFieldPeriod(10);
        BaseSingleFieldPeriod period2 = new BaseSingleFieldPeriod(5);

        int result = period1.compareTo(period2);
        Assert.assertEquals(5, result);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testBetweenWithNullStart() {
        BaseSingleFieldPeriod period = new BaseSingleFieldPeriod(10);
        ReadableInstant start = null;
        ReadableInstant end = new Instant(0L);
        DurationFieldType field = DurationFieldType.weeks();
        int result = BaseSingleFieldPeriod.between(start, end, field);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testBetweenWithNullEnd() {
        BaseSingleFieldPeriod period = new BaseSingleFieldPeriod(10);
        ReadableInstant start = new Instant(0L);
        ReadableInstant end = null;
        DurationFieldType field = DurationFieldType.weeks();
        int result = BaseSingleFieldPeriod.between(start, end, field);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testBetweenWithInvalidFieldType() {
        BaseSingleFieldPeriod period = new BaseSingleFieldPeriod(10);
        ReadableInstant start = new Instant(0L);
        ReadableInstant end = new Instant(0L);
        DurationFieldType field = new DurationFieldType("invalid");
        int result = BaseSingleFieldPeriod.between(start, end, field);
    }

    @Test
    public void testBetweenWithValidValues() {
        BaseSingleFieldPeriod period = new BaseSingleFieldPeriod(10);
        long startMillis = 1516048461000L;
        long endMillis = 1516048521000L;
        DurationFieldType field = DurationFieldType.weeks();
        int result = BaseSingleFieldPeriod.between(new Instant(startMillis), new Instant(endMillis), field);
        Assert.assertEquals(1, result);
    }

    @Test
    public void testStandardPeriodInWithValidPeriod() {
        BaseSingleFieldPeriod period = new BaseSingleFieldPeriod(1234567);
        long millisPerUnit = 1000 * 60 * 60 * 24 * 7;
        int result = BaseSingleFieldPeriod.standardPeriodIn(period, millisPerUnit);
        Assert.assertEquals(180, result);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testStandardPeriodInWithNullPeriod() {
        long millisPerUnit = 1000 * 60 * 60 * 24 * 7;
        BaseSingleFieldPeriod period = null;
        int result = BaseSingleFieldPeriod.standardPeriodIn(period, millisPerUnit);
    }

    @Test
    public void testStandardPeriodInWithEmptyPeriod() {
        BaseSingleFieldPeriod period = new BaseSingleFieldPeriod(0);
        long millisPerUnit = 1000 * 60 * 60 * 24 * 7;
        int result = BaseSingleFieldPeriod.standardPeriodIn(period, millisPerUnit);
        Assert.assertEquals(0, result);
    }

    @Test
    public void testStandardPeriodInWithPeriodContainingNonPreciseValues() {
        BaseSingleFieldPeriod period = new BaseSingleFieldPeriod(new Period(2, 3, 4, 5, 6, 7, 8));
        long millisPerUnit = 1000 * 60 * 60 * 24 * 30;
        int result = BaseSingleFieldPeriod.standardPeriodIn(period, millisPerUnit);
        Assert.assertEquals(0, result);
    }

}