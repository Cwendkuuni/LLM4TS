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
        assertEquals(10, period.getValue());

        period.setValue(20);
        assertEquals(20, period.getValue());
    }

    @Test
    public void testGetFieldType() {
        BaseSingleFieldPeriod period = new BaseSingleFieldPeriod(10);
        assertEquals(DurationFieldType.class, period.getFieldType().getClass());
    }

    @Test
    public void testGetPeriodType() {
        BaseSingleFieldPeriod period = new BaseSingleFieldPeriod(10);
        assertEquals(PeriodType.class, period.getPeriodType().getClass());
    }

    @Test
    public void testSize() {
        BaseSingleFieldPeriod period = new BaseSingleFieldPeriod(10);
        assertEquals(1, period.size());
    }

    @Test
    public void testGetFieldTypeAtIndex() {
        BaseSingleFieldPeriod period = new BaseSingleFieldPeriod(10);
        assertEquals(DurationFieldType.class, period.getFieldType(0).getClass());
        try {
            period.getFieldType(1);
        } catch (IndexOutOfBoundsException e) {
            assertEquals("1", e.getMessage());
        }
    }

    @Test
    public void testGetValueAtIndex() {
        BaseSingleFieldPeriod period = new BaseSingleFieldPeriod(10);
        assertEquals(10, period.getValue(0));
        try {
            period.getValue(1);
        } catch (IndexOutOfBoundsException e) {
            assertEquals("1", e.getMessage());
        }
    }

    @Test
    public void testGet() {
        BaseSingleFieldPeriod period = new BaseSingleFieldPeriod(10);
        assertEquals(10, period.get(period.getFieldType()));
        assertEquals(0, period.get(DurationFieldType.days()));
    }

    @Test
    public void testIsSupported() {
        BaseSingleFieldPeriod period = new BaseSingleFieldPeriod(10);
        assertTrue(period.isSupported(period.getFieldType()));
        assertFalse(period.isSupported(DurationFieldType.days()));
    }

    @Test
    public void testToPeriod() {
        BaseSingleFieldPeriod period = new BaseSingleFieldPeriod(10);
        Period expectedPeriod = Period.ZERO.withFields(period);
        assertEquals(expectedPeriod, period.toPeriod());
    }

    @Test
    public void testToMutablePeriod() {
        BaseSingleFieldPeriod period = new BaseSingleFieldPeriod(10);
        MutablePeriod expectedPeriod = new MutablePeriod();
        expectedPeriod.add(period);
        assertEquals(expectedPeriod, period.toMutablePeriod());
    }

    @Test
    public void testEquals() {
        BaseSingleFieldPeriod period1 = new BaseSingleFieldPeriod(10);
        BaseSingleFieldPeriod period2 = new BaseSingleFieldPeriod(10);
        BaseSingleFieldPeriod period3 = new BaseSingleFieldPeriod(20);

        assertTrue(period1.equals(period2));
        assertFalse(period1.equals(period3));
    }

    @Test
    public void testHashCode() {
        BaseSingleFieldPeriod period1 = new BaseSingleFieldPeriod(10);
        BaseSingleFieldPeriod period2 = new BaseSingleFieldPeriod(10);
        BaseSingleFieldPeriod period3 = new BaseSingleFieldPeriod(20);

        assertEquals(period1.hashCode(), period2.hashCode());
        assertNotEquals(period1.hashCode(), period3.hashCode());
    }

    @Test
    public void testCompareTo() {
        BaseSingleFieldPeriod period1 = new BaseSingleFieldPeriod(10);
        BaseSingleFieldPeriod period2 = new BaseSingleFieldPeriod(20);
        BaseSingleFieldPeriod period3 = new BaseSingleFieldPeriod(10);

        assertTrue(period1.compareTo(period2) < 0);
        assertTrue(period2.compareTo(period1) > 0);
        assertTrue(period1.compareTo(period3) == 0);
    }
}