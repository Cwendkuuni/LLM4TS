import org.joda.time.BaseSingleFieldPeriod;
import org.joda.time.DurationFieldType;
import org.joda.time.MutablePeriod;
import org.joda.time.Period;
import org.junit.Test;
import static org.junit.Assert.*;

public class BaseSingleFieldPeriodTest {

    @Test
    public void testConstructorWithPeriod() {
        BaseSingleFieldPeriod period = new BaseSingleFieldPeriod(5);
        assertEquals(5, period.getValue());
        assertEquals(DurationFieldType.DAYS, period.getFieldType());
    }

    @Test(expected = IllegalArgumentException.class)
    public void testConstructorWithNegativePeriod() {
        new BaseSingleFieldPeriod(-5);
    }

    @Test
    public void testGetValue() {
        BaseSingleFieldPeriod period = new BaseSingleFieldPeriod(5);
        assertEquals(5, period.getValue());
    }

    @Test
    public void testSetValue() {
        BaseSingleFieldPeriod period = new BaseSingleFieldPeriod(5);
        period.setValue(10);
        assertEquals(10, period.getValue());
    }

    @Test(expected = IllegalArgumentException.class)
    public void testSetValueWithNegativeValue() {
        BaseSingleFieldPeriod period = new BaseSingleFieldPeriod(5);
        period.setValue(-1);
    }

    @Test
    public void testGetFieldType() {
        BaseSingleFieldPeriod period = new BaseSingleFieldPeriod(5);
        assertEquals(DurationFieldType.DAYS, period.getFieldType());
    }

    @Test
    public void testGetPeriodType() {
        BaseSingleFieldPeriod period = new BaseSingleFieldPeriod(5);
        PeriodType periodType = period.getPeriodType();
        assertTrue(periodType instanceof PeriodType);
    }

    @Test
    public void testSize() {
        BaseSingleFieldPeriod period = new BaseSingleFieldPeriod(5);
        assertEquals(1, period.size());
    }

    @Test(expected = IndexOutOfBoundsException.class)
    public void testGetFieldTypeWithInvalidIndex() {
        BaseSingleFieldPeriod period = new BaseSingleFieldPeriod(5);
        period.getFieldType(2);
    }

    @Test
    public void testGetValueWithInvalidIndex() {
        BaseSingleFieldPeriod period = new BaseSingleFieldPeriod(5);
        period.getValue(2);
        assertEquals(0, period.getValue(2));
    }

    @Test
    public void testGetValueWithValidIndex() {
        BaseSingleFieldPeriod period = new BaseSingleFieldPeriod(5);
        assertEquals(5, period.getValue(0));
    }

    @Test
    public void testGetWithValidFieldType() {
        BaseSingleFieldPeriod period = new BaseSingleFieldPeriod(5);
        assertEquals(5, period.get(DurationFieldType.DAYS));
    }

    @Test
    public void testIsSupportedWithValidFieldType() {
        BaseSingleFieldPeriod period = new BaseSingleFieldPeriod(5);
        assertTrue(period.isSupported(DurationFieldType.DAYS));
    }

    @Test
    public void testIsSupportedWithInvalidFieldType() {
        BaseSingleFieldPeriod period = new BaseSingleFieldPeriod(5);
        assertFalse(period.isSupported(DurationFieldType.HOURS));
    }

    @Test
    public void testToPeriod() {
        BaseSingleFieldPeriod period = new BaseSingleFieldPeriod(5);
        Period expected = new Period(5, PeriodUnit.DAYS);
        Period actual = period.toPeriod();
        assertEquals(expected, actual);
    }

    @Test
    public void testToMutablePeriod() {
        BaseSingleFieldPeriod period = new BaseSingleFieldPeriod(5);
        MutablePeriod expected = new MutablePeriod(5, PeriodUnit.DAYS);
        MutablePeriod actual = period.toMutablePeriod();
        assertEquals(expected, actual);
    }

    @Test
    public void testEqualsWithSameObject() {
        BaseSingleFieldPeriod period = new BaseSingleFieldPeriod(5);
        assertTrue(period.equals(period));
    }

    @Test
    public void testEqualsWithDifferentType() {
        BaseSingleFieldPeriod period = new BaseSingleFieldPeriod(5);
        assertFalse(period.equals(new Object()));
    }

    @Test
    public void testEqualsWithDifferentValue() {
        BaseSingleFieldPeriod period1 = new BaseSingleFieldPeriod(5);
        BaseSingleFieldPeriod period2 = new BaseSingleFieldPeriod(3);
        assertFalse(period1.equals(period2));
    }

    @Test
    public void testEqualsWithSameValueAndSameType() {
        BaseSingleFieldPeriod period1 = new BaseSingleFieldPeriod(5);
        BaseSingleFieldPeriod period2 = new BaseSingleFieldPeriod(5);
        assertTrue(period1.equals(period2));
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
    public void testCompareToWithSamePeriod() {
        BaseSingleFieldPeriod period1 = new BaseSingleFieldPeriod(5);
        BaseSingleFieldPeriod period2 = new BaseSingleFieldPeriod(5);
        assertEquals(0, period1.compareTo(period2));
    }

    @Test
    public void testCompareToWithSmallerPeriod() {
        BaseSingleFieldPeriod period1 = new BaseSingleFieldPeriod(5);
        BaseSingleFieldPeriod period2 = new BaseSingleFieldPeriod(3);
        assertEquals(-2, period1.compareTo(period2));
    }

    @Test
    public void testCompareToWithLargerPeriod() {
        BaseSingleFieldPeriod period1 = new BaseSingleFieldPeriod(5);
        BaseSingleFieldPeriod period2 = new BaseSingleFieldPeriod(7);
        assertEquals(2, period1.compareTo(period2));
    }
}