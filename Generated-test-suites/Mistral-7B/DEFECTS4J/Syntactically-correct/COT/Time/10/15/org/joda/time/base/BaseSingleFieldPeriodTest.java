import org.junit.Test;
import static org.junit.Assert.*;
import org.joda.time.BaseSingleFieldPeriod;
import org.joda.time.DurationFieldType;

public class BaseSingleFieldPeriodTest {

    @Test
    public void testConstructor() {
        BaseSingleFieldPeriod period = new BaseSingleFieldPeriod(5);
        int expected = 5;
        int actual = period.getValue();
        assertEquals(expected, actual);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testConstructorWithNegativeValue() {
        new BaseSingleFieldPeriod(-5);
    }

    @Test
    public void testGetFieldType() {
        BaseSingleFieldPeriod period = new BaseSingleFieldPeriod(5);
        DurationFieldType expected = DurationFieldType.DAYS;
        DurationFieldType actual = period.getFieldType();
        assertEquals(expected, actual);
    }

    @Test
    public void testGetPeriodType() {
        BaseSingleFieldPeriod period = new BaseSingleFieldPeriod(5);
        PeriodType expected = PeriodType.standard();
        PeriodType actual = period.getPeriodType();
        assertEquals(expected, actual);
    }

    @Test
    public void testSize() {
        BaseSingleFieldPeriod period = new BaseSingleFieldPeriod(5);
        int expected = 1;
        int actual = period.size();
        assertEquals(expected, actual);
    }

    @Test(expected = IndexOutOfBoundsException.class)
    public void testGetFieldTypeInvalidIndex() {
        BaseSingleFieldPeriod period = new BaseSingleFieldPeriod(5);
        period.getFieldType(1);
    }

    @Test(expected = IndexOutOfBoundsException.class)
    public void testGetValueInvalidIndex() {
        BaseSingleFieldPeriod period = new BaseSingleFieldPeriod(5);
        period.getValue(1);
    }

    @Test
    public void testGet() {
        BaseSingleFieldPeriod period = new BaseSingleFieldPeriod(5, DurationFieldType.DAYS);
        int expected = 5;
        int actual = period.get(DurationFieldType.DAYS);
        assertEquals(expected, actual);
    }

    @Test
    public void testIsSupported() {
        BaseSingleFieldPeriod period = new BaseSingleFieldPeriod(5, DurationFieldType.DAYS);
        boolean expected = true;
        boolean actual = period.isSupported(DurationFieldType.DAYS);
        assertEquals(expected, actual);
    }

    @Test
    public void testIsSupportedInvalidFieldType() {
        BaseSingleFieldPeriod period = new BaseSingleFieldPeriod(5, DurationFieldType.DAYS);
        boolean expected = false;
        boolean actual = period.isSupported(DurationFieldType.HOURS);
        assertEquals(expected, actual);
    }

    @Test
    public void testToPeriod() {
        BaseSingleFieldPeriod period = new BaseSingleFieldPeriod(5, DurationFieldType.DAYS);
        Period expected = new Period(5, PeriodType.standard());
        Period actual = period.toPeriod();
        assertEquals(expected, actual);
    }

    @Test
    public void testToMutablePeriod() {
        BaseSingleFieldPeriod period = new BaseSingleFieldPeriod(5, DurationFieldType.DAYS);
        MutablePeriod expected = new MutablePeriod(5, PeriodType.standard());
        MutablePeriod actual = period.toMutablePeriod();
        assertEquals(expected, actual);
    }

    @Test
    public void testEquals() {
        BaseSingleFieldPeriod period1 = new BaseSingleFieldPeriod(5, DurationFieldType.DAYS);
        BaseSingleFieldPeriod period2 = new BaseSingleFieldPeriod(5, DurationFieldType.DAYS);
        Object other = new Object();
        BaseSingleFieldPeriod period3 = new BaseSingleFieldPeriod(6, DurationFieldType.DAYS);

        assertTrue(period1.equals(period1));
        assertTrue(period1.equals(period2));
        assertFalse(period1.equals(other));
        assertFalse(period1.equals(period3));
    }

    @Test
    public void testHashCode() {
        BaseSingleFieldPeriod period1 = new BaseSingleFieldPeriod(5, DurationFieldType.DAYS);
        BaseSingleFieldPeriod period2 = new BaseSingleFieldPeriod(5, DurationFieldType.DAYS);
        Object other = new Object();
        BaseSingleFieldPeriod period3 = new BaseSingleFieldPeriod(6, DurationFieldType.DAYS);

        int hashCode1 = period1.hashCode();
        int hashCode2 = period2.hashCode();
        int hashCodeOther = other.hashCode();
        int hashCode3 = period3.hashCode();

        assertEquals(hashCode1, hashCode2);
        assertNotEquals(hashCode1, hashCodeOther);
        assertNotEquals(hashCode1, hashCode3);
    }

    @Test
    public void testCompareToSameInstance() {
        BaseSingleFieldPeriod period1 = new BaseSingleFieldPeriod(5, DurationFieldType.DAYS);
        int result = period1.compareTo(period1);
        assertEquals(0, result);
    }

    @Test
    public void testCompareToDifferentValue() {
        BaseSingleFieldPeriod period1 = new BaseSingleFieldPeriod(5, DurationFieldType.DAYS);
        BaseSingleFieldPeriod period2 = new BaseSingleFieldPeriod(6, DurationFieldType.DAYS);
        int result = period1.compareTo(period2);
        assertEquals(-1, result);
    }

    @Test
    public void testCompareToSameValue() {
        BaseSingleFieldPeriod period1 = new BaseSingleFieldPeriod(5, DurationFieldType.DAYS);
        BaseSingleFieldPeriod period2 = new BaseSingleFieldPeriod(5, DurationFieldType.DAYS);
        int result = period1.compareTo(period2);
        assertEquals(0, result);
    }

    @Test
    public void testCompareToNull() {
        BaseSingleFieldPeriod period1 = new BaseSingleFieldPeriod(5, DurationFieldType.DAYS);
        int result = period1.compareTo(null);
        assertEquals(-1, result);
    }

    @Test(expected = NullPointerException.class)
    public void testCompareToNullObject() {
        BaseSingleFieldPeriod period1 = new BaseSingleFieldPeriod(5, DurationFieldType.DAYS);
        null.compareTo(period1);
    }

    @Test(expected = ClassCastException.class)
    public void testCompareToDifferentClass() {
        BaseSingleFieldPeriod period1 = new BaseSingleFieldPeriod(5, DurationFieldType.DAYS);
        String other = new String("test");
        int result = period1.compareTo(other);
    }
}