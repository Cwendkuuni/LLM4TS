import static org.junit.Assert.*;
import org.junit.Test;

public class BaseSingleFieldPeriodTest {

    @Test
    public void testGetValue() {
        BaseSingleFieldPeriod period = new ConcreteSingleFieldPeriod(5);
        assertEquals(5, period.getValue());
    }

    @Test
    public void testSetvalue() {
        BaseSingleFieldPeriod period = new ConcreteSingleFieldPeriod(5);
        period.setValue(10);
        assertEquals(10, period.getValue());
    }

    @Test
    public void testGetFieldType() {
        BaseSingleFieldPeriod period = new ConcreteSingleFieldPeriod(5);
        assertEquals(DurationFieldType.days(), period.getFieldType());
    }

    @Test
    public void testGetPeriodType() {
        BaseSingleFieldPeriod period = new ConcreteSingleFieldPeriod(5);
        assertEquals(PeriodType.standard(), period.getPeriodType());
    }

    @Test
    public void testSize() {
        BaseSingleFieldPeriod period = new ConcreteSingleFieldPeriod(5);
        assertEquals(1, period.size());
    }

    @Test
    public void testGetFieldTypeAtIndex() {
        BaseSingleFieldPeriod period = new ConcreteSingleFieldPeriod(5);
        assertEquals(DurationFieldType.days(), period.getFieldType(0));
    }

    @Test
    public void testGetValueAtIndex() {
        BaseSingleFieldPeriod period = new ConcreteSingleFieldPeriod(5);
        assertEquals(5, period.getValue(0));
    }

    @Test
    public void testGetField() {
        BaseSingleFieldPeriod period = new ConcreteSingleFieldPeriod(5);
        assertEquals(5, period.get(DurationFieldType.days()));
    }

    @Test
    public void testIsSupported() {
        BaseSingleFieldPeriod period = new ConcreteSingleFieldPeriod(5);
        assertTrue(period.isSupported(DurationFieldType.days()));
        assertFalse(period.isSupported(DurationFieldType.hours()));
    }

    @Test
    public void testToPeriod() {
        BaseSingleFieldPeriod period = new ConcreteSingleFieldPeriod(5);
        Period expected = Period.days(5);
        assertEquals(expected, period.toPeriod());
    }

    @Test
    public void testToMutablePeriod() {
        BaseSingleFieldPeriod period = new ConcreteSingleFieldPeriod(5);
        MutablePeriod mutablePeriod = period.toMutablePeriod();
        assertEquals(5, mutablePeriod.getDays());
    }

    @Test
    public void testEquals() {
        BaseSingleFieldPeriod period1 = new ConcreteSingleFieldPeriod(5);
        BaseSingleFieldPeriod period2 = new ConcreteSingleFieldPeriod(5);
        assertEquals(period1, period2);
    }

    @Test
    public void testHashCode() {
        BaseSingleFieldPeriod period = new ConcreteSingleFieldPeriod(5);
        int expectedHash = 31 * (31 + 5) + DurationFieldType.days().hashCode();
        assertEquals(expectedHash, period.hashCode());
    }

    @Test
    public void testCompareTo() {
        BaseSingleFieldPeriod period1 = new ConcreteSingleFieldPeriod(5);
        BaseSingleFieldPeriod period2 = new ConcreteSingleFieldPeriod(10);
        assertTrue(period2.compareTo(period1) > 0);
        assertEquals(0, period1.compareTo(period1));
    }

}