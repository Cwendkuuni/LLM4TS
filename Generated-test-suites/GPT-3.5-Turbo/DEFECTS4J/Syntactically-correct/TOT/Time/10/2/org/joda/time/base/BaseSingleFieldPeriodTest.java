import static org.junit.Assert.*;
import org.junit.Before;
import org.junit.Test;
import org.joda.time.DateTime;
import org.joda.time.LocalDate;

public class BaseSingleFieldPeriodTest {
    
    private BaseSingleFieldPeriod period;

    @Before
    public void setUp() {
        period = new BaseSingleFieldPeriodTestImpl(5);
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
        assertEquals(DurationFieldType.TEST, period.getFieldType());
    }

    @Test
    public void testGetPeriodType() {
        assertEquals(PeriodType.TEST, period.getPeriodType());
    }

    @Test
    public void testSize() {
        assertEquals(1, period.size());
    }

    @Test
    public void testGetFieldTypeIndex() {
        assertEquals(DurationFieldType.TEST, period.getFieldType(0));
    }

    @Test
    public void testGetValueIndex() {
        assertEquals(5, period.getValue(0));
    }

    @Test
    public void testGet() {
        assertEquals(5, period.get(DurationFieldType.TEST));
        assertEquals(0, period.get(DurationFieldType.MINUTE));
    }

    @Test
    public void testIsSupported() {
        assertTrue(period.isSupported(DurationFieldType.TEST));
        assertFalse(period.isSupported(DurationFieldType.MONTH));
    }

    @Test
    public void testToPeriod() {
        Period expectedPeriod = new Period(0, 0, 0, 0, 0, 0, 0, 5);
        assertEquals(expectedPeriod, period.toPeriod());
    }

    @Test
    public void testToMutablePeriod() {
        MutablePeriod expectedPeriod = new MutablePeriod();
        expectedPeriod.setTest(5);
        assertEquals(expectedPeriod, period.toMutablePeriod());
    }

    @Test
    public void testEquals() {
        BaseSingleFieldPeriod period2 = new BaseSingleFieldPeriodTestImpl(5);
        BaseSingleFieldPeriod period3 = new BaseSingleFieldPeriodTestImpl(10);

        assertTrue(period.equals(period));
        assertTrue(period.equals(period2));
        assertFalse(period.equals(period3));
        assertFalse(period.equals(null));
        assertFalse(period.equals("test"));
    }

    @Test
    public void testHashCode() {
        assertEquals(142, period.hashCode());
    }

    @Test
    public void testCompareTo() {
        BaseSingleFieldPeriod period2 = new BaseSingleFieldPeriodTestImpl(5);
        BaseSingleFieldPeriod period3 = new BaseSingleFieldPeriodTestImpl(10);

        assertEquals(0, period.compareTo(period));
        assertEquals(0, period.compareTo(period2));
        assertTrue(period.compareTo(period3) < 0);
    }

    private class BaseSingleFieldPeriodTestImpl extends BaseSingleFieldPeriod {

        private BaseSingleFieldPeriodTestImpl(int period) {
            super(period);
        }

        @Override
        public DurationFieldType getFieldType() {
            return DurationFieldType.TEST;
        }

        @Override
        public PeriodType getPeriodType() {
            return PeriodType.TEST;
        }

    }

}