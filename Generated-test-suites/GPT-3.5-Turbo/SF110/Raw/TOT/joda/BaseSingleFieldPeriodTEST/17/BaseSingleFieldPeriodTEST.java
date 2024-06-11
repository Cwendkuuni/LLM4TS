import org.joda.time.*;
import org.joda.time.chrono.ISOChronology;
import org.joda.time.field.FieldUtils;
import org.joda.time.base.BaseSingleFieldPeriod;

import static org.junit.Assert.*;
import org.junit.Test;

public class BaseSingleFieldPeriodTest {

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

    @Test
    public void testGetFieldType() {
        BaseSingleFieldPeriod period = new BaseSingleFieldPeriod(5);
        assertEquals(null, period.getFieldType());
    }

    @Test
    public void testGetPeriodType() {
        BaseSingleFieldPeriod period = new BaseSingleFieldPeriod(5);
        assertEquals(null, period.getPeriodType());
    }

    @Test
    public void testSize() {
        BaseSingleFieldPeriod period = new BaseSingleFieldPeriod(5);
        assertEquals(1, period.size());
    }

    @Test(expected = IndexOutOfBoundsException.class)
    public void testGetFieldTypeWithInvalidIndex() {
        BaseSingleFieldPeriod period = new BaseSingleFieldPeriod(5);
        period.getFieldType(1);
    }

    @Test(expected = IndexOutOfBoundsException.class)
    public void testGetValueWithInvalidIndex() {
        BaseSingleFieldPeriod period = new BaseSingleFieldPeriod(5);
        period.getValue(1);
    }

    @Test
    public void testGet() {
        BaseSingleFieldPeriod period = new BaseSingleFieldPeriod(5);
        assertEquals(5, period.get(period.getFieldType()));
    }

    @Test
    public void testIsSupported() {
        BaseSingleFieldPeriod period = new BaseSingleFieldPeriod(5);
        assertTrue(period.isSupported(period.getFieldType()));
    }

    @Test
    public void testToPeriod() {
        BaseSingleFieldPeriod period = new BaseSingleFieldPeriod(5);
        Period expected = new Period().withField(period.getFieldType(), period.getValue());
        assertEquals(expected, period.toPeriod());
    }

    @Test
    public void testToMutablePeriod() {
        BaseSingleFieldPeriod period = new BaseSingleFieldPeriod(5);
        MutablePeriod expected = new MutablePeriod();
        expected.add(period);
        assertEquals(expected, period.toMutablePeriod());
    }

    @Test
    public void testEquals() {
        BaseSingleFieldPeriod period1 = new BaseSingleFieldPeriod(5);
        BaseSingleFieldPeriod period2 = new BaseSingleFieldPeriod(10);
        assertFalse(period1.equals(period2));
    }

    @Test
    public void testHashCode() {
        BaseSingleFieldPeriod period = new BaseSingleFieldPeriod(5);
        int expected = 27 * (27 * 17 + period.getValue()) + period.getFieldType().hashCode();
        assertEquals(expected, period.hashCode());
    }

    @Test
    public void testCompareTo() {
        BaseSingleFieldPeriod period1 = new BaseSingleFieldPeriod(5);
        BaseSingleFieldPeriod period2 = new BaseSingleFieldPeriod(10);
        assertTrue(period1.compareTo(period2) < 0);
    }
}