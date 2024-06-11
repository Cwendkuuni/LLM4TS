import org.joda.time.DateTimeUtils;
import org.joda.time.Period;
import org.joda.time.PeriodType;
import org.junit.Before;
import org.junit.Test;
import static org.junit.Assert.*;

public class BaseSingleFieldPeriodTest {

    private BaseSingleFieldPeriod period;

    @Before
    public void setUp() {
        DateTimeUtils.setCurrentMillisFixed(0L);
        period = new BaseSingleFieldPeriodTestImpl(1);
    }

    @Test
    public void testGetValue() {
        assertEquals(1, period.getValue());
    }

    @Test
    public void testSetValue() {
        period.setValue(2);
        assertEquals(2, period.getValue());
    }

    @Test
    public void testGetFieldType() {
        assertEquals(DurationFieldType.testType(), period.getFieldType());
    }

    @Test
    public void testGetPeriodType() {
        assertEquals(PeriodType.standard(), period.getPeriodType());
    }

    @Test
    public void testSize() {
        assertEquals(1, period.size());
    }

    @Test
    public void testGetFieldTypeAtIndex() {
        assertEquals(DurationFieldType.testType(), period.getFieldType(0));
    }

    @Test
    public void testGetValueAtIndex() {
        assertEquals(1, period.getValue(0));
    }

    @Test
    public void testGetWithMatchingType() {
        assertEquals(1, period.get(DurationFieldType.testType()));
    }

    @Test
    public void testGetWithNonMatchingType() {
        assertEquals(0, period.get(DurationFieldType.minutes()));
    }

    @Test
    public void testIsSupportedWithMatchingType() {
        assertTrue(period.isSupported(DurationFieldType.testType()));
    }

    @Test
    public void testIsSupportedWithNonMatchingType() {
        assertFalse(period.isSupported(DurationFieldType.minutes()));
    }

    @Test
    public void testToPeriod() {
        Period p = period.toPeriod();
        assertEquals(1, p.get(DurationFieldType.testType()));
    }

    @Test
    public void testToMutablePeriod() {
        MutablePeriod p = period.toMutablePeriod();
        assertEquals(1, p.get(DurationFieldType.testType()));
    }

    @Test
    public void testEqualsWithSamePeriod() {
        BaseSingleFieldPeriod other = new BaseSingleFieldPeriodTestImpl(1);
        assertTrue(period.equals(other));
    }

    @Test
    public void testEqualsWithDifferentPeriod() {
        BaseSingleFieldPeriod other = new BaseSingleFieldPeriodTestImpl(2);
        assertFalse(period.equals(other));
    }

    @Test
    public void testEqualsWithDifferentClass() {
        Object other = new Object();
        assertFalse(period.equals(other));
    }

    @Test
    public void testHashCode() {
        BaseSingleFieldPeriod other = new BaseSingleFieldPeriodTestImpl(1);
        assertEquals(period.hashCode(), other.hashCode());
    }

    @Test(expected = ClassCastException.class)
    public void testCompareToWithDifferentClass() {
        Object other = new Object();
        period.compareTo(other);
    }

    @Test
    public void testCompareToWithGreaterValue() {
        BaseSingleFieldPeriod other = new BaseSingleFieldPeriodTestImpl(2);
        assertEquals(-1, period.compareTo(other));
    }

    @Test
    public void testCompareToWithLesserValue() {
        BaseSingleFieldPeriod other = new BaseSingleFieldPeriodTestImpl(0);
        assertEquals(1, period.compareTo(other));
    }

    @Test
    public void testCompareToWithEqualValue() {
        BaseSingleFieldPeriod other = new BaseSingleFieldPeriodTestImpl(1);
        assertEquals(0, period.compareTo(other));
    }

    private class BaseSingleFieldPeriodTestImpl extends BaseSingleFieldPeriod {

        protected BaseSingleFieldPeriodTestImpl(int period) {
            super(period);
        }

        @Override
        public DurationFieldType getFieldType() {
            return DurationFieldType.testType();
        }

        @Override
        public PeriodType getPeriodType() {
            return PeriodType.standard();
        }
    }
}