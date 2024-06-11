import org.junit.Test;
import org.junit.Assert.*;
import org.joda.time.BaseSingleFieldPeriod;
import org.joda.time.DurationFieldType;
import org.joda.time.Period;
import org.joda.time.MutablePeriod;

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
        int value = period.getValue();
        assertEquals(5, value);
    }

    @Test
    public void testGetValueInvalidIndex() {
        BaseSingleFieldPeriod period = new BaseSingleFieldPeriod(5);
        try {
            period.getValue(1);
            fail("IndexOutOfBoundsException expected");
        } catch (IndexOutOfBoundsException e) {
            // expected
        }
    }

    @Test
    public void testGetFieldTypeInvalidIndex() {
        BaseSingleFieldPeriod period = new BaseSingleFieldPeriod(5);
        try {
            period.getFieldType(1);
            fail("IndexOutOfBoundsException expected");
        } catch (IndexOutOfBoundsException e) {
            // expected
        }
    }

    @Test
    public void testToPeriod() {
        BaseSingleFieldPeriod period = new BaseSingleFieldPeriod(5);
        Period result = period.toPeriod();
        int value = result.getDays();
        assertEquals(5, value);
    }

    @Test
    public void testToMutablePeriod() {
        BaseSingleFieldPeriod period = new BaseSingleFieldPeriod(5);
        MutablePeriod result = period.toMutablePeriod();
        int value = result.get(DurationFieldType.DAYS);
        assertEquals(5, value);
    }

    @Test
    public void testEquals() {
        BaseSingleFieldPeriod period1 = new BaseSingleFieldPeriod(5);
        BaseSingleFieldPeriod period2 = new BaseSingleFieldPeriod(5);
        BaseSingleFieldPeriod period3 = new BaseSingleFieldPeriod(6);
        BaseSingleFieldPeriod period4 = null;
        BaseSingleFieldPeriod period5 = new BaseSingleFieldPeriod("foo".getClass(), 5);

        assertTrue(period1.equals(period1));
        assertTrue(period1.equals(period2));
        assertFalse(period1.equals(period3));
        assertFalse(period1.equals(period4));
        assertFalse(period1.equals(period5));
    }

    @Test
    public void testHashCode() {
        BaseSingleFieldPeriod period1 = new BaseSingleFieldPeriod(5);
        BaseSingleFieldPeriod period2 = new BaseSingleFieldPeriod(5);
        BaseSingleFieldPeriod period3 = new BaseSingleFieldPeriod(6);
        BaseSingleFieldPeriod period4 = null;
        BaseSingleFieldPeriod period5 = new BaseSingleFieldPeriod("foo".getClass(), 5);

        int hashCode1 = period1.hashCode();
        int hashCode2 = period1.hashCode();
        int hashCode3 = period1.hashCode();
        int hashCode4 = period2.hashCode();
        int hashCode5 = period3.hashCode();
        int hashCode6 = period4.hashCode();
        int hashCode7 = period5.hashCode();

        assertEquals(hashCode1, hashCode2);
        assertEquals(hashCode1, hashCode3);
        assertEquals(hashCode2, hashCode4);
        assertNotSame(hashCode1, hashCode5);
        assertNotSame(hashCode1, hashCode6);
        assertNotSame(hashCode1, hashCode7);
    }

    @Test
    public void testCompareToEqual() {
        BaseSingleFieldPeriod period1 = new BaseSingleFieldPeriod(5);
        BaseSingleFieldPeriod period2 = new BaseSingleFieldPeriod(5);
        int result = period1.compareTo(period2);
        assertEquals(0, result);
    }

    @Test
    public void testCompareToGreater() {
        BaseSingleFieldPeriod period1 = new BaseSingleFieldPeriod(5);
        BaseSingleFieldPeriod period2 = new BaseSingleFieldPeriod(3);
        int result = period1.compareTo(period2);
        assertEquals(2, result);
    }

    @Test
    public void testCompareToLess() {
        BaseSingleFieldPeriod period1 = new BaseSingleFieldPeriod(3);
        BaseSingleFieldPeriod period2 = new BaseSingleFieldPeriod(5);
        int result = period1.compareTo(period2);
        assertEquals(-2, result);
    }

    @Test(expected = ClassCastException.class)
    public void testCompareToDifferentType() {
        BaseSingleFieldPeriod period1 = new BaseSingleFieldPeriod(5);
        Object period2 = new String("foo");
        period1.compareTo(period2);
    }

    @Test(expected = NullPointerException.class)
    public void testCompareToNull() {
        BaseSingleFieldPeriod period1 = new BaseSingleFieldPeriod(5);
        period1.compareTo(null);
    }
}