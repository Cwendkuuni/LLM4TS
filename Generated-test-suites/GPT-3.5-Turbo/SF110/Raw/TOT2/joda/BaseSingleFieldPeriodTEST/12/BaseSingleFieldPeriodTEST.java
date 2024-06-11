import org.joda.time.DateTimeFieldType;
import org.junit.Test;

public class BaseSingleFieldPeriodTest {

    @Test
    public void testGetValue() {
        BaseSingleFieldPeriod period = new BaseSingleFieldPeriod(10);
        assertEquals(10, period.getValue());
    }

    @Test
    public void testGetFieldType() {
        BaseSingleFieldPeriod period = new BaseSingleFieldPeriod(5);
        assertEquals(DateTimeFieldType.days(), period.getFieldType());
    }

    @Test
    public void testSize() {
        BaseSingleFieldPeriod period = new BaseSingleFieldPeriod(3);
        assertEquals(1, period.size());
    }

    @Test
    public void testSetValue() {
        BaseSingleFieldPeriod period = new BaseSingleFieldPeriod(5);
        period.setValue(8);
        assertEquals(8, period.getValue());
    }

    @Test(expected = IllegalArgumentException.class)
    public void testGetFieldTypeOutOfBoundsException() {
        BaseSingleFieldPeriod period = new BaseSingleFieldPeriod(2);
        period.getFieldType(1); // Index out of bounds
    }

    @Test(expected = IllegalArgumentException.class)
    public void testGetFieldSafeTon() {
        BaseSingleFieldPeriod period = new BaseSingleFieldPeriod(3);
        period.getValue(1); // Index out of bounds
    }

    @Test
    public void testEquals() {
        BaseSingleFieldPeriod period1 = new BaseSingleFieldPeriod(10);
        BaseSingleFieldPeriod period2 = new BaseSingleFieldPeriod(10);
        assertTrue(period1.equals(period2));
    }

    @Test
    public void testCompareTo() {
        BaseSingleFieldPeriod period1 = new BaseSingleFieldPeriod(5);
        BaseSingleFieldPeriod period2 = new BaseSingleFieldPeriod(8);
        assertEquals(-1, period1.compareTo(period2));
    }
}