import org.joda.time.*;
import org.junit.Before;
import org.junit.Test;

import static org.junit.Assert.*;

public class BaseSingleFieldPeriodTest {

    private BaseSingleFieldPeriod baseSingleFieldPeriod;

    @Before
    public void setUp() {
        baseSingleFieldPeriod = new MockSingleFieldPeriod(5);
    }

    @Test
    public void testGetValue() {
        assertEquals(5, baseSingleFieldPeriod.getValue());
    }

    @Test
    public void testSetValue() {
        baseSingleFieldPeriod.setValue(10);
        assertEquals(10, baseSingleFieldPeriod.getValue());
    }

    @Test
    public void testGetFieldType() {
        DurationFieldType fieldType = baseSingleFieldPeriod.getFieldType();
        assertNotNull(fieldType);
        assertEquals(DurationFieldType.class, fieldType.getClass());
    }

    @Test
    public void testGetPeriodType() {
        PeriodType periodType = baseSingleFieldPeriod.getPeriodType();
        assertNotNull(periodType);
        assertEquals(PeriodType.class, periodType.getClass());
    }

    @Test
    public void testSize() {
        assertEquals(1, baseSingleFieldPeriod.size());
    }

    @Test
    public void testGetFieldTypeWithValidIndex() {
        DurationFieldType fieldType = baseSingleFieldPeriod.getFieldType(0);
        assertNotNull(fieldType);
        assertEquals(DurationFieldType.class, fieldType.getClass());
    }

    @Test(expected = IndexOutOfBoundsException.class)
    public void testGetFieldTypeWithInvalidIndex() {
        baseSingleFieldPeriod.getFieldType(1);
    }

    @Test
    public void testGetValueWithValidIndex() {
        int value = baseSingleFieldPeriod.getValue(0);
        assertEquals(5, value);
    }

    @Test(expected = IndexOutOfBoundsException.class)
    public void testGetValueWithInvalidIndex() {
        baseSingleFieldPeriod.getValue(1);
    }

    @Test
    public void testGetWithMatchingFieldType() {
        int value = baseSingleFieldPeriod.get(baseSingleFieldPeriod.getFieldType());
        assertEquals(5, value);
    }

    @Test
    public void testGetWithNonMatchingFieldType() {
        int value = baseSingleFieldPeriod.get(DurationFieldType.weeks());
        assertEquals(0, value);
    }

    @Test
    public void testIsSupportedWithMatchingFieldType() {
        assertTrue(baseSingleFieldPeriod.isSupported(baseSingleFieldPeriod.getFieldType()));
    }

    @Test
    public void testIsSupportedWithNonMatchingFieldType() {
        assertFalse(baseSingleFieldPeriod.isSupported(DurationFieldType.weeks()));
    }

    @Test
    public void testToPeriod() {
        Period period = baseSingleFieldPeriod.toPeriod();
        assertNotNull(period);
        assertEquals(0, period.getYears());
        assertEquals(0, period.getMonths());
        assertEquals(0, period.getWeeks());
        assertEquals(0, period.getDays());
        assertEquals(0, period.getHours());
        assertEquals(0, period.getMinutes());
        assertEquals(0, period.getSeconds());
        assertEquals(0, period.getMillis());
    }

    @Test
    public void testToMutablePeriod() {
        MutablePeriod mutablePeriod = baseSingleFieldPeriod.toMutablePeriod();
        assertNotNull(mutablePeriod);
        assertEquals(0, mutablePeriod.getYears());
        assertEquals(0, mutablePeriod.getMonths());
        assertEquals(0, mutablePeriod.getWeeks());
        assertEquals(0, mutablePeriod.getDays());
        assertEquals(0, mutablePeriod.getHours());
        assertEquals(0, mutablePeriod.getMinutes());
        assertEquals(0, mutablePeriod.getSeconds());
        assertEquals(0, mutablePeriod.getMillis());
    }

    @Test
    public void testEqualsWithEqualPeriod() {
        BaseSingleFieldPeriod other = new MockSingleFieldPeriod(5);
        assertTrue(baseSingleFieldPeriod.equals(other));
    }

    @Test
    public void testEqualsWithUnequalPeriod() {
        BaseSingleFieldPeriod other = new MockSingleFieldPeriod(10);
        assertFalse(baseSingleFieldPeriod.equals(other));
    }

    @Test
    public void testEqualsWithNonPeriodObject() {
        assertFalse(baseSingleFieldPeriod.equals("not a period"));
    }

    @Test
    public void testEqualsWithNull() {
        assertFalse(baseSingleFieldPeriod.equals(null));
    }

    @Test
    public void testHashCode() {
        int hashCode = baseSingleFieldPeriod.hashCode();
        assertEquals(802, hashCode);
    }

    @Test
    public void testCompareToWithGreaterPeriod() {
        BaseSingleFieldPeriod other = new MockSingleFieldPeriod(10);
        int result = baseSingleFieldPeriod.compareTo(other);
        assertEquals(-1, result);
    }

    @Test
    public void testCompareToWithSmallerPeriod() {
        BaseSingleFieldPeriod other = new MockSingleFieldPeriod(3);
        int result = baseSingleFieldPeriod.compareTo(other);
        assertEquals(1, result);
    }

    @Test
    public void testCompareToWithEqualPeriod() {
        BaseSingleFieldPeriod other = new MockSingleFieldPeriod(5);
        int result = baseSingleFieldPeriod.compareTo(other);
        assertEquals(0, result);
    }

    @Test(expected = ClassCastException.class)
    public void testCompareToWithDifferentClass() {
        BaseSingleFieldPeriod other = new MockDifferentSingleFieldPeriod(5);
        baseSingleFieldPeriod.compareTo(other);
    }

    private class MockSingleFieldPeriod extends BaseSingleFieldPeriod {

        public MockSingleFieldPeriod(int period) {
            super(period);
        }

        @Override
        public DurationFieldType getFieldType() {
            return DurationFieldType.minutes();
        }

        @Override
        public PeriodType getPeriodType() {
            return PeriodType.standard();
        }
    }

    private class MockDifferentSingleFieldPeriod extends BaseSingleFieldPeriod {

        public MockDifferentSingleFieldPeriod(int period) {
            super(period);
        }

        @Override
        public DurationFieldType getFieldType() {
            return DurationFieldType.seconds();
        }

        @Override
        public PeriodType getPeriodType() {
            return PeriodType.standard();
        }
    }

}