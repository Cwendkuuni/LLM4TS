import static org.junit.Assert.*;
import org.joda.time.DurationFieldType;
import org.joda.time.Period;
import org.joda.time.MutablePeriod;
import org.junit.Before;
import org.junit.Test;

public class BaseSingleFieldPeriodTest {

    private BaseSingleFieldPeriod period;

    @Before
    public void setUp() {
        period = new BaseSingleFieldPeriodImpl(10);
    }

    @Test
    public void testGetFieldType() {
        assertEquals(DurationFieldType.class, period.getFieldType().getClass());
    }

    @Test
    public void testGetPeriodType() {
        assertEquals(Period.class, period.getPeriodType().getClass());
    }

    @Test
    public void testSize() {
        assertEquals(1, period.size());
    }

    @Test(expected = IndexOutOfBoundsException.class)
    public void testGetFieldType_withInvalidIndex() {
        period.getFieldType(1);
    }

    @Test(expected = IndexOutOfBoundsException.class)
    public void testGetValue_withInvalidIndex() {
        period.getValue(1);
    }

    @Test
    public void testGet_withMatchingFieldType() {
        assertEquals(10, period.get(period.getFieldType()));
    }

    @Test
    public void testGet_withDifferentFieldType() {
        assertEquals(0, period.get(DurationFieldType.eras()));
    }

    @Test
    public void testIsSupported_withMatchingFieldType() {
        assertTrue(period.isSupported(period.getFieldType()));
    }

    @Test
    public void testIsSupported_withDifferentFieldType() {
        assertFalse(period.isSupported(DurationFieldType.eras()));
    }

    @Test
    public void testToPeriod() {
        Period result = period.toPeriod();
        assertEquals(10, result.get(period.getFieldType()));
    }

    @Test
    public void testToMutablePeriod() {
        MutablePeriod result = period.toMutablePeriod();
        assertEquals(10, result.get(period.getFieldType()));
    }

    @Test
    public void testEquals_withSameInstance() {
        assertTrue(period.equals(period));
    }

    @Test
    public void testEquals_withDifferentType() {
        assertFalse(period.equals(new Object()));
    }

    @Test
    public void testEquals_withMatchingFieldAndValue() {
        BaseSingleFieldPeriod equalPeriod = new BaseSingleFieldPeriodImpl(10);
        assertTrue(period.equals(equalPeriod));
    }

    @Test
    public void testEquals_withDifferentField() {
        BaseSingleFieldPeriod differentFieldPeriod = new BaseSingleFieldPeriodImpl(10) {
            @Override
            public DurationFieldType getFieldType() {
                return DurationFieldType.months();
            }
        };
        assertFalse(period.equals(differentFieldPeriod));
    }

    @Test
    public void testEquals_withDifferentValue() {
        BaseSingleFieldPeriod differentValuePeriod = new BaseSingleFieldPeriodImpl(20);
        assertFalse(period.equals(differentValuePeriod));
    }

    @Test
    public void testHashCode() {
        BaseSingleFieldPeriod equalPeriod = new BaseSingleFieldPeriodImpl(10);
        assertEquals(equalPeriod.hashCode(), period.hashCode());
    }

    @Test
    public void testCompareTo_withEqualPeriod() {
        BaseSingleFieldPeriod equalPeriod = new BaseSingleFieldPeriodImpl(10);
        assertEquals(0, period.compareTo(equalPeriod));
    }

    @Test
    public void testCompareTo_withGreaterPeriod() {
        BaseSingleFieldPeriod greaterPeriod = new BaseSingleFieldPeriodImpl(20);
        assertEquals(-1, period.compareTo(greaterPeriod));
    }

    @Test
    public void testCompareTo_withSmallerPeriod() {
        BaseSingleFieldPeriod smallerPeriod = new BaseSingleFieldPeriodImpl(5);
        assertEquals(1, period.compareTo(smallerPeriod));
    }

    private class BaseSingleFieldPeriodImpl extends BaseSingleFieldPeriod {

        protected BaseSingleFieldPeriodImpl(int iPeriod) {
            super(iPeriod);
        }

        @Override
        public DurationFieldType getFieldType() {
            return DurationFieldType.days();
        }

        @Override
        public PeriodType getPeriodType() {
            return PeriodType.yearWeekDayTime();
        }
    }
}