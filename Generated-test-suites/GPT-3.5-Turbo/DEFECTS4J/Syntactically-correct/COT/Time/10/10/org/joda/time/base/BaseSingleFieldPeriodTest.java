import org.joda.time.DurationFieldType;
import org.joda.time.ReadableInstant;
import org.joda.time.ReadablePartial;
import org.junit.Before;
import org.junit.Test;

import static org.junit.Assert.*;

public class BaseSingleFieldPeriodTest {

    private BaseSingleFieldPeriod period;

    @Before
    public void setUp() {
        period = new BaseSingleFieldPeriodImpl(5);
    }

    @Test
    public void testGetValue() {
        assertEquals(5, period.getValue());
    }

    @Test
    public void testSetValue() {
        period.setValue(10);
        assertEquals(10, period.getValue());
    }

    @Test
    public void testGetFieldType() {
        assertEquals(DurationFieldType.days(), period.getFieldType());
    }

    @Test
    public void testGetPeriodType() {
        assertEquals(BaseSingleFieldPeriodImpl.PERIOD_TYPE, period.getPeriodType());
    }

    @Test
    public void testSize() {
        assertEquals(1, period.size());
    }

    @Test
    public void testGetFieldTypeAtIndex() {
        assertEquals(DurationFieldType.days(), period.getFieldType(0));
    }

    @Test
    public void testGetValueAtIndex() {
        assertEquals(5, period.getValue(0));
    }

    @Test
    public void testGet() {
        assertEquals(5, period.get(DurationFieldType.days()));
        assertEquals(0, period.get(DurationFieldType.hours()));
    }

    @Test
    public void testIsSupported() {
        assertTrue(period.isSupported(DurationFieldType.days()));
        assertFalse(period.isSupported(DurationFieldType.hours()));
    }

    @Test
    public void testToPeriod() {
        assertEquals(new Period(0, 0, 0, 5, 0, 0, 0, 0), period.toPeriod());
    }

    @Test
    public void testToMutablePeriod() {
        assertEquals(new MutablePeriod(0, 0, 0, 5, 0, 0, 0, 0), period.toMutablePeriod());
    }

    @Test
    public void testEquals() {
        assertTrue(period.equals(new BaseSingleFieldPeriodImpl(5)));
        assertFalse(period.equals(new BaseSingleFieldPeriodImpl(10)));
        assertFalse(period.equals(null));
        assertFalse(period.equals(""));
    }

    @Test
    public void testHashCode() {
        assertEquals(137, period.hashCode());
    }

    @Test
    public void testCompareTo() {
        assertTrue(period.compareTo(new BaseSingleFieldPeriodImpl(3)) > 0);
        assertTrue(period.compareTo(new BaseSingleFieldPeriodImpl(5)) == 0);
        assertTrue(period.compareTo(new BaseSingleFieldPeriodImpl(10)) < 0);
    }

    private class BaseSingleFieldPeriodImpl extends BaseSingleFieldPeriod {

        public static final PeriodType PERIOD_TYPE = PeriodType.standard();

        public BaseSingleFieldPeriodImpl(int period) {
            super(period);
        }

        @Override
        public DurationFieldType getFieldType() {
            return DurationFieldType.days();
        }

        @Override
        public PeriodType getPeriodType() {
            return PERIOD_TYPE;
        }
    }
}