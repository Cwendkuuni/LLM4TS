import org.joda.time.LocalDateTime;
import org.joda.time.Months;
import org.joda.time.Period;
import org.junit.Test;

import static org.junit.Assert.*;

public class BaseSingleFieldPeriodTest {

    @Test
    public void testBetweenStartAndEndInstant() {
        LocalDateTime start = new LocalDateTime(2022, 1, 1, 0, 0);
        LocalDateTime end = new LocalDateTime(2022, 1, 10, 23, 59);
        int result = BaseSingleFieldPeriod.between(start, end, Months.months());
        assertEquals(0, result);
    }

    @Test
    public void testBetweenStartAndEndPartial() {
        Period period = new Period().withYears(1);
        int result = BaseSingleFieldPeriod.between(period, period, period);
        assertEquals(0, result);
    }

    @Test
    public void testStandardPeriodIn() {
        Period period = new Period().withHours(24);
        int result = BaseSingleFieldPeriod.standardPeriodIn(period, 86400000);
        assertEquals(1, result);
    }

    @Test
    public void testGetValue() {
        BaseSingleFieldPeriod baseSingleFieldPeriod = new TestSingleFieldPeriod(10);
        assertEquals(10, baseSingleFieldPeriod.getValue());
    }

    @Test
    public void testGetFieldType() {
        BaseSingleFieldPeriod baseSingleFieldPeriod = new TestSingleFieldPeriod(10);
        assertEquals(Months.months().getFieldType(), baseSingleFieldPeriod.getFieldType());
    }

    @Test
    public void testGetPeriodType() {
        BaseSingleFieldPeriod baseSingleFieldPeriod = new TestSingleFieldPeriod(10);
        assertEquals(PeriodType.standard(), baseSingleFieldPeriod.getPeriodType());
    }

    @Test
    public void testSize() {
        BaseSingleFieldPeriod baseSingleFieldPeriod = new TestSingleFieldPeriod(10);
        assertEquals(1, baseSingleFieldPeriod.size());
    }

    @Test
    public void testGetFieldTypeAtIndex() {
        BaseSingleFieldPeriod baseSingleFieldPeriod = new TestSingleFieldPeriod(10);
        assertEquals(Months.months().getFieldType(), baseSingleFieldPeriod.getFieldType(0));
    }

    @Test
    public void testGetFieldTypeAtInvalidIndex() {
        BaseSingleFieldPeriod baseSingleFieldPeriod = new TestSingleFieldPeriod(10);
        try {
            baseSingleFieldPeriod.getFieldType(1);
            fail("Expected IndexOutOfBoundsException");
        } catch (IndexOutOfBoundsException e) {
            // Expected
        }
    }

    @Test
    public void testGetValueAtIndex() {
        BaseSingleFieldPeriod baseSingleFieldPeriod = new TestSingleFieldPeriod(10);
        assertEquals(10, baseSingleFieldPeriod.getValue(0));
    }

    @Test
    public void testGetValueAtInvalidIndex() {
        BaseSingleFieldPeriod baseSingleFieldPeriod = new TestSingleFieldPeriod(10);
        try {
            baseSingleFieldPeriod.getValue(1);
            fail("Expected IndexOutOfBoundsException");
        } catch (IndexOutOfBoundsException e) {
            // Expected
        }
    }

    @Test
    public void testGet() {
        BaseSingleFieldPeriod baseSingleFieldPeriod = new TestSingleFieldPeriod(10);
        assertEquals(10, baseSingleFieldPeriod.get(Months.months()));
    }

    @Test
    public void testIsSupported() {
        BaseSingleFieldPeriod baseSingleFieldPeriod = new TestSingleFieldPeriod(10);
        assertTrue(baseSingleFieldPeriod.isSupported(Months.months()));
    }

    @Test
    public void testEquals() {
        BaseSingleFieldPeriod baseSingleFieldPeriod1 = new TestSingleFieldPeriod(10);
        BaseSingleFieldPeriod baseSingleFieldPeriod2 = new TestSingleFieldPeriod(10);
        assertTrue(baseSingleFieldPeriod1.equals(baseSingleFieldPeriod2));
    }

    @Test
    public void testHashCode() {
        BaseSingleFieldPeriod baseSingleFieldPeriod = new TestSingleFieldPeriod(10);
        assertEquals(121195970, baseSingleFieldPeriod.hashCode()); // Hash code calculation based on test case data
    }

    @Test
    public void testCompareTo() {
        BaseSingleFieldPeriod baseSingleFieldPeriod1 = new TestSingleFieldPeriod(10);
        BaseSingleFieldPeriod baseSingleFieldPeriod2 = new TestSingleFieldPeriod(5);
        assertEquals(1, baseSingleFieldPeriod1.compareTo(baseSingleFieldPeriod2));
    }

    private static class TestSingleFieldPeriod extends BaseSingleFieldPeriod {
        public TestSingleFieldPeriod(int period) {
            super(period);
        }

        @Override
        public DurationFieldType getFieldType() {
            return Months.months().getFieldType();
        }

        @Override
        public PeriodType getPeriodType() {
            return PeriodType.standard();
        }
    }
}