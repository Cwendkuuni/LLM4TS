
import org.junit.Test;
import static org.junit.Assert.*;

public class BaseSingleFieldPeriodTest {

    @Test
    public void testGetValue() {
        BaseSingleFieldPeriod period = new BaseSingleFieldPeriod(10);
        assertEquals(10, period.getValue());
    }

    @Test
    public void testSetValue() {
        BaseSingleFieldPeriod period = new BaseSingleFieldPeriod(10);
        period.setValue(5);
        assertEquals(5, period.getValue());
    }

    @Test
    public void testGetFieldType() {
        BaseSingleFieldPeriod period = new BaseSingleFieldPeriod(10);
        assertNotNull(period.getFieldType());
    }

    @Test
    public void testGetPeriodType() {
        BaseSingleFieldPeriod period = new BaseSingleFieldPeriod(10);
        assertNotNull(period.getPeriodType());
    }

    @Test
    public void testSize() {
        BaseSingleFieldPeriod period = new BaseSingleFieldPeriod(10);
        assertEquals(1, period.size());
    }

    @Test
    public void testGetFieldTypeAtIndexZero() {
        BaseSingleFieldPeriod period = new BaseSingleFieldPeriod(10);
        assertNotNull(period.getFieldType(0));
        assertThrows(IndexOutOfBoundsException.class, () -> period.getFieldType(1));
    }

    @Test
    public void testGetValueAtIndexZero() {
        BaseSingleFieldPeriod period = new BaseSingleFieldPeriod(10);
        assertEquals(10, period.getValue(0));
        assertThrows(IndexOutOfBoundsException.class, () -> period.getValue(1));
    }

    @Test
    public void testGet() {
        BaseSingleFieldPeriod period = new BaseSingleFieldPeriod(10);
        assertEquals(10, period.get(period.getFieldType()));
        assertEquals(0, period.get(DurationFieldType.years()));
    }

    @Test
    public void testIsSupported() {
        BaseSingleFieldPeriod period = new BaseSingleFieldPeriod(10);
        assertTrue(period.isSupported(period.getFieldType()));
        assertFalse(period.isSupported(DurationFieldType.years()));
    }

    @Test
    public void testToPeriod() {
        BaseSingleFieldPeriod period = new BaseSingleFieldPeriod(10);
        assertNotNull(period.toPeriod());
    }

    @Test
    public void testToMutablePeriod() {
        BaseSingleFieldPeriod period = new BaseSingleFieldPeriod(10);
        assertNotNull(period.toMutablePeriod());
    }

    @Test
    public void testEquals() {
        BaseSingleFieldPeriod period1 = new BaseSingleFieldPeriod(10);
        BaseSingleFieldPeriod period2 = new BaseSingleFieldPeriod(10);
        BaseSingleFieldPeriod period3 = new BaseSingleFieldPeriod(5);
        assertTrue(period1.equals(period2));
        assertFalse(period1.equals(period3));
    }

    @Test
    public void testHashCode() {
        BaseSingleFieldPeriod period = new BaseSingleFieldPeriod(10);
        assertEquals(27*(27*17+10)+period.getFieldType().hashCode(), period.hashCode());
    }

    @Test
    public void testCompareTo() {
        BaseSingleFieldPeriod period1 = new BaseSingleFieldPeriod(10);
        BaseSingleFieldPeriod period2 = new BaseSingleFieldPeriod(5);
        assertEquals(1, period1.compareTo(period2));
        assertEquals(-1, period2.compareTo(period1));
        assertEquals(0, period1.compareTo(period1));
        assertThrows(ClassCastException.class, () -> period1.compareTo(new Object()));
    }
}
