import org.junit.Test;
import static org.junit.Assert.*;
import org.joda.time.BaseSingleFieldPeriod;
import org.joda.time.DurationFieldType;
import org.joda.time.MutablePeriod;
import org.joda.time.Period;

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
        assertEquals(1, period.size());
    }

    @Test
    public void testGetFieldTypeAndValue() {
        BaseSingleFieldPeriod period = new BaseSingleFieldPeriod(5);
        DurationFieldType fieldType = period.getFieldType();
        int value = period.getValue(0);
        assertEquals(DurationFieldType.DAYS, fieldType);
        assertEquals(5, value);
    }

    @Test
    public void testGetValue() {
        BaseSingleFieldPeriod period = new BaseSingleFieldPeriod(5);
        int value = period.getValue();
        assertEquals(5, value);
    }

    @Test
    public void testSetValue() {
        BaseSingleFieldPeriod period = new BaseSingleFieldPeriod(5);
        period.setValue(10);
        assertEquals(10, period.getValue());
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
        assertEquals(5, result.getDays());
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
    public void testEqualsOtherInstance() {
        BaseSingleFieldPeriod period1 = new BaseSingleFieldPeriod(5);
        BaseSingleFieldPeriod period2 = new BaseSingleFieldPeriod(10);
        assertFalse(period1.equals(period2));
    }

    @Test
    public void testHashCode() {
        BaseSingleFieldPeriod period = new BaseSingleFieldPeriod(5);
        int hashCode1 = period.hashCode();
        BaseSingleFieldPeriod period2 = new BaseSingleFieldPeriod(5);
        int hashCode2 = period2.hashCode();
        assertEquals(hashCode1, hashCode2);
    }

    @Test
    public void testCompareToZero() {
        BaseSingleFieldPeriod period1 = new BaseSingleFieldPeriod(5);
        BaseSingleFieldPeriod period2 = new BaseSingleFieldPeriod(0);
        int compareResult = period1.compareTo(period2);
        assertTrue(compareResult > 0);
    }

    @Test
    public void testCompareToEqual() {
        BaseSingleFieldPeriod period1 = new BaseSingleFieldPeriod(5);
        BaseSingleFieldPeriod period2 = new BaseSingleFieldPeriod(5);
        int compareResult = period1.compareTo(period2);
        assertEquals(0, compareResult);
    }

    @Test
    public void testCompareToGreater() {
        BaseSingleFieldPeriod period1 = new BaseSingleFieldPeriod(10);
        BaseSingleFieldPeriod period2 = new BaseSingleFieldPeriod(5);
        int compareResult = period1.compareTo(period2);
        assertTrue(compareResult > 0);
    }

    @Test
    public void testBetween() {
        long startMillis = 1539324000000L;
        long endMillis = 1539325600000L;
        int result = BaseSingleFieldPeriod.between(DateTimeUtils.toInstant(startMillis), DateTimeUtils.toInstant(endMillis), DurationFieldType.DAYS);
        assertEquals(1, result);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testBetweenNullStart() {
        long endMillis = 1539325600000L;
        BaseSingleFieldPeriod.between(null, DateTimeUtils.toInstant(endMillis), DurationFieldType.DAYS);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testBetweenNullEnd() {
        long startMillis = 1539324000000L;
        BaseSingleFieldPeriod.between(DateTimeUtils.toInstant(startMillis), null, DurationFieldType.DAYS);
    }

    @Test
    public void testStandardPeriodIn() {
        ReadablePeriod period = new Period(2, 3, 4, 5, 6, 7);
        int result = BaseSingleFieldPeriod.standardPeriodIn(period, 1);
        assertEquals(65, result);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testStandardPeriodInNullPeriod() {
        long millisPerUnit = 1;
        BaseSingleFieldPeriod.standardPeriodIn(null, millisPerUnit);
    }

}