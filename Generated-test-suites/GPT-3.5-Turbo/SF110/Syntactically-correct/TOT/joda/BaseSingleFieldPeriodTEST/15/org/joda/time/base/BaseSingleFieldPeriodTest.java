import org.junit.Before;
import org.junit.Test;

import static org.junit.Assert.*;

public class BaseSingleFieldPeriodTest {

    private BaseSingleFieldPeriod period;

    @Before
    public void setUp() {
        period = new TestSingleFieldPeriod(10); // TestSingleFieldPeriod extends BaseSingleFieldPeriod
    }

    // Test getValue method
    @Test
    public void testGetValue() {
        assertEquals(10, period.getValue());
    }

    // Test setValue method
    @Test
    public void testSetValue() {
        period.setValue(5);
        assertEquals(5, period.getValue());
    }

    // Test getFieldType method
    @Test
    public void testGetFieldType() {
        assertEquals(DurationFieldType.seconds(), period.getFieldType());
    }

    // Test getPeriodType method
    @Test
    public void testGetPeriodType() {
        assertEquals(PeriodType.standard(), period.getPeriodType());
    }

    // Test size method
    @Test
    public void testSize() {
        assertEquals(1, period.size());
    }

    // Test getFieldType with index out of bounds
    @Test(expected = IndexOutOfBoundsException.class)
    public void testGetFieldTypeOutOfBounds() {
        period.getFieldType(1);
    }

    // Test getValue with index out of bounds
    @Test(expected = IndexOutOfBoundsException.class)
    public void testGetValueOutOfBounds() {
        period.getValue(1);
    }

    // Test get method with matching field type
    @Test
    public void testGetMatchingFieldType() {
        assertEquals(10, period.get(DurationFieldType.seconds()));
    }

    // Test get method with non-matching field type
    @Test
    public void testGetNonMatchingFieldType() {
        assertEquals(0, period.get(DurationFieldType.minutes()));
    }

    // Test isSupported method with supported field type
    @Test
    public void testIsSupportedSupportedFieldType() {
        assertTrue(period.isSupported(DurationFieldType.seconds()));
    }

    // Test isSupported method with unsupported field type
    @Test
    public void testIsSupportedUnsupportedFieldType() {
        assertFalse(period.isSupported(DurationFieldType.minutes()));
    }

    // Test toPeriod method
    @Test
    public void testToPeriod() {
        Period expectedPeriod = new Period().withSeconds(10);
        assertEquals(expectedPeriod, period.toPeriod());
    }

    // Test toMutablePeriod method
    @Test
    public void testToMutablePeriod() {
        MutablePeriod expectedMutablePeriod = new MutablePeriod().withSeconds(10);
        assertEquals(expectedMutablePeriod, period.toMutablePeriod());
    }

    // Test equals method with same object
    @Test
    public void testEqualsSameObject() {
        assertTrue(period.equals(period));
    }

    // Test equals method with same value and period type
    @Test
    public void testEqualsSameValueAndPeriodType() {
        BaseSingleFieldPeriod equalPeriod = new TestSingleFieldPeriod(10);
        assertTrue(period.equals(equalPeriod));
    }

    // Test equals method with different value
    @Test
    public void testEqualsDifferentValue() {
        BaseSingleFieldPeriod differentValuePeriod = new TestSingleFieldPeriod(5);
        assertFalse(period.equals(differentValuePeriod));
    }

    // Test equals method with different period type
    @Test
    public void testEqualsDifferentPeriodType() {
        BaseSingleFieldPeriod differentPeriodTypePeriod = new TestSingleFieldPeriod(10) {
            @Override
            public PeriodType getPeriodType() {
                return PeriodType.dayTime();
            }
        };
        assertFalse(period.equals(differentPeriodTypePeriod));
    }

    // Test hashCode method
    @Test
    public void testHashCode() {
        int expectedHashCode = 27 * (27 * 17 + period.getValue()) + period.getFieldType().hashCode();
        assertEquals(expectedHashCode, period.hashCode());
    }

    // Test compareTo method with greater value
    @Test
    public void testCompareToGreaterValue() {
        BaseSingleFieldPeriod greaterValuePeriod = new TestSingleFieldPeriod(20);
        assertEquals(-1, period.compareTo(greaterValuePeriod));
    }

    // Test compareTo method with smaller value
    @Test
    public void testCompareToSmallerValue() {
        BaseSingleFieldPeriod smallerValuePeriod = new TestSingleFieldPeriod(5);
        assertEquals(1, period.compareTo(smallerValuePeriod));
    }

    // Test compareTo method with same value
    @Test
    public void testCompareToSameValue() {
        BaseSingleFieldPeriod sameValuePeriod = new TestSingleFieldPeriod(10);
        assertEquals(0, period.compareTo(sameValuePeriod));
    }

    private static class TestSingleFieldPeriod extends BaseSingleFieldPeriod {
        protected TestSingleFieldPeriod(int iPeriod) {
            super(iPeriod);
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