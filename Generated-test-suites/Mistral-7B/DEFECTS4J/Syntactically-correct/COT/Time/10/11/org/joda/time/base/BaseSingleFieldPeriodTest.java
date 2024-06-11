import org.junit.Test;
import org.joda.time.BaseSingleFieldPeriod;
import org.joda.time.DurationFieldType;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNotEquals;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertThrows;
import static org.junit.Assert.assertTrue;

public class BaseSingleFieldPeriodTest {

    @Test
    public void testGetFieldType() {
        BaseSingleFieldPeriod period = new TestSingleFieldPeriod(1);
        DurationFieldType fieldType = period.getFieldType();
        assertEquals(DurationFieldType.DAYS, fieldType);
    }

    @Test
    public void testGetPeriodType() {
        BaseSingleFieldPeriod period = new TestSingleFieldPeriod(1);
        PeriodType periodType = period.getPeriodType();
        assertEquals(PeriodType.standard(), periodType);
    }

    @Test
    public void testSize() {
        BaseSingleFieldPeriod period = new TestSingleFieldPeriod(1);
        int size = period.size();
        assertEquals(1, size);
    }

    @Test
    public void testGetFieldTypeInvalidIndex() {
        BaseSingleFieldPeriod period = new TestSingleFieldPeriod(1);
        assertThrows(IndexOutOfBoundsException.class, () -> period.getFieldType(2));
    }

    @Test
    public void testGetValue() {
        BaseSingleFieldPeriod period = new TestSingleFieldPeriod(1);
        int value = period.getValue();
        assertEquals(1, value);
    }

    @Test
    public void testGetValueInvalidIndex() {
        BaseSingleFieldPeriod period = new TestSingleFieldPeriod(1);
        assertThrows(IndexOutOfBoundsException.class, () -> period.getValue(2));
    }

    @Test
    public void testGet() {
        BaseSingleFieldPeriod period = new TestSingleFieldPeriod(1);
        int value = period.get(DurationFieldType.DAYS);
        assertEquals(1, value);
        value = period.get(DurationFieldType.HOURS);
        assertNull(value);
    }

    @Test
    public void testIsSupported() {
        BaseSingleFieldPeriod period = new TestSingleFieldPeriod(1);
        assertTrue(period.isSupported(DurationFieldType.DAYS));
        assertFalse(period.isSupported(DurationFieldType.HOURS));
    }

    @Test
    public void testToPeriod() {
        BaseSingleFieldPeriod period = new TestSingleFieldPeriod(1);
        Period toPeriod = period.toPeriod();
        assertEquals(1, toPeriod.getFields().get(DurationFieldType.DAYS));
        assertEquals(0, toPeriod.getFields().get(DurationFieldType.HOURS));
    }

    @Test
    public void testToMutablePeriod() {
        BaseSingleFieldPeriod period = new TestSingleFieldPeriod(1);
        MutablePeriod toMutablePeriod = period.toMutablePeriod();
        assertEquals(1, toMutablePeriod.getBaseValues()[0]);
        assertEquals(0, toMutablePeriod.getBaseValues()[1]);
    }

    @Test
    public void testEquals() {
        BaseSingleFieldPeriod period1 = new TestSingleFieldPeriod(1);
        BaseSingleFieldPeriod period2 = new TestSingleFieldPeriod(1);
        BaseSingleFieldPeriod period3 = new TestSingleFieldPeriod(2);

        assertTrue(period1.equals(period1));
        assertTrue(period1.equals(period2));
        assertFalse(period1.equals(period3));
        assertFalse(period1.equals(null));
        assertFalse(period1.equals("String"));
    }

    @Test
    public void testHashCode() {
        BaseSingleFieldPeriod period1 = new TestSingleFieldPeriod(1);
        BaseSingleFieldPeriod period2 = new TestSingleFieldPeriod(1);
        BaseSingleFieldPeriod period3 = new TestSingleFieldPeriod(2);

        int hashCode1 = period1.hashCode();
        int hashCode2 = period2.hashCode();
        int hashCode3 = period3.hashCode();

        assertEquals(hashCode1, hashCode1);
        assertEquals(hashCode1, hashCode2);
        assertNotEquals(hashCode1, hashCode3);
    }

    @Test
    public void testCompareToEqual() {
        BaseSingleFieldPeriod period1 = new TestSingleFieldPeriod(1);
        BaseSingleFieldPeriod period2 = new TestSingleFieldPeriod(1);
        assertEquals(0, period1.compareTo(period2));
    }

    @Test
    public void testCompareToGreater() {
        BaseSingleFieldPeriod period1 = new TestSingleFieldPeriod(2);
        BaseSingleFieldPeriod period2 = new TestSingleFieldPeriod(1);
        assertEquals(1, period1.compareTo(period2));
    }

    @Test
    public void testCompareToLess() {
        BaseSingleFieldPeriod period1 = new TestSingleFieldPeriod(1);
        BaseSingleFieldPeriod period2 = new TestSingleFieldPeriod(2);
        assertEquals(-1, period1.compareTo(period2));
    }

    @Test(expected = NullPointerException.class)
    public void testCompareToNull() {
        BaseSingleFieldPeriod period = new TestSingleFieldPeriod(1);
        period.compareTo(null);
    }

    @Test(expected = ClassCastException.class)
    public void testCompareToInvalidType() {
        BaseSingleFieldPeriod period = new TestSingleFieldPeriod(1);
        Object other = new Object();
        period.compareTo(other);
    }

    private static class TestSingleFieldPeriod extends BaseSingleFieldPeriod {

        TestSingleFieldPeriod(int period) {
            super(period);
        }

        TestSingleFieldPeriod(int period, DurationFieldType fieldType) {
            super(period, fieldType);
        }

        TestSingleFieldPeriod(int period, PeriodType periodType) {
            super(period, periodType);
        }

        TestSingleFieldPeriod(int period, DurationFieldType fieldType, PeriodType periodType) {
            super(period, fieldType, periodType);
        }
    }
}