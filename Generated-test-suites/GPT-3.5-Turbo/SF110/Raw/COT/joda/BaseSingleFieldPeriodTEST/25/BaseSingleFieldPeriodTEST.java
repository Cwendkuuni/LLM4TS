import org.joda.time.base.*;
import org.joda.time.chrono.*;
import org.joda.time.field.*;
import org.joda.time.*;
import org.junit.*;
import static org.junit.Assert.*;

public class BaseSingleFieldPeriodTest {

    @Test
    public void testBetween() {
        ReadableInstant instant1 = new DateTime(2020, 1, 1, 0, 0);
        ReadableInstant instant2 = new DateTime(2021, 1, 1, 0, 0);
        DurationFieldType type = DurationFieldType.years();
        
        int result = BaseSingleFieldPeriod.between(instant1, instant2, type);
        
        assertEquals(result, 1);
    }
    
    @Test
    public void testBetween_NullInstant1() {
        ReadableInstant instant1 = null;
        ReadableInstant instant2 = new DateTime(2021, 1, 1, 0, 0);
        DurationFieldType type = DurationFieldType.years();
        
        try {
            int result = BaseSingleFieldPeriod.between(instant1, instant2, type);
            fail("Exception expected");
        } catch (IllegalArgumentException e) {
            assertEquals("ReadableInstant objects must not be null", e.getMessage());
        }
    }
    
    @Test
    public void testBetween_NullInstant2() {
        ReadableInstant instant1 = new DateTime(2020, 1, 1, 0, 0);
        ReadableInstant instant2 = null;
        DurationFieldType type = DurationFieldType.years();
        
        try {
            int result = BaseSingleFieldPeriod.between(instant1, instant2, type);
            fail("Exception expected");
        } catch (IllegalArgumentException e) {
            assertEquals("ReadableInstant objects must not be null", e.getMessage());
        }
    }
    
    @Test
    public void testBetween_NullType() {
        ReadableInstant instant1 = new DateTime(2020, 1, 1, 0, 0);
        ReadableInstant instant2 = new DateTime(2021, 1, 1, 0, 0);
        DurationFieldType type = null;
        
        try {
            int result = BaseSingleFieldPeriod.between(instant1, instant2, type);
            fail("Exception expected");
        } catch (IllegalArgumentException e) {
            assertEquals("Type must not be null", e.getMessage());
        }
    }
    
    @Test
    public void testBetween_ReadablePartial() {
        ReadablePartial partial1 = new LocalDate(2020, 1, 1);
        ReadablePartial partial2 = new LocalDate(2021, 1, 1);
        ReadablePeriod period = Duration.standardDays(365);
        
        int result = BaseSingleFieldPeriod.between(partial1, partial2, period);
        
        assertEquals(result, 1);
    }
    
    @Test
    public void testBetween_NullPartial1() {
        ReadablePartial partial1 = null;
        ReadablePartial partial2 = new LocalDate(2021, 1, 1);
        ReadablePeriod period = Duration.standardDays(365);
        
        try {
            int result = BaseSingleFieldPeriod.between(partial1, partial2, period);
            fail("Exception expected");
        } catch (IllegalArgumentException e) {
            assertEquals("ReadablePartial objects must not be null", e.getMessage());
        }
    }
    
    @Test
    public void testBetween_NullPartial2() {
        ReadablePartial partial1 = new LocalDate(2020, 1, 1);
        ReadablePartial partial2 = null;
        ReadablePeriod period = Duration.standardDays(365);
        
        try {
            int result = BaseSingleFieldPeriod.between(partial1, partial2, period);
            fail("Exception expected");
        } catch (IllegalArgumentException e) {
            assertEquals("ReadablePartial objects must not be null", e.getMessage());
        }
    }
    
    @Test
    public void testBetween_DifferentFields() {
        ReadablePartial partial1 = new LocalDate(2020, 1, 1);
        ReadablePartial partial2 = new LocalDateTime(2021, 1, 1, 0, 0);
        ReadablePeriod period = Duration.standardDays(365);
        
        try {
            int result = BaseSingleFieldPeriod.between(partial1, partial2, period);
            fail("Exception expected");
        } catch (IllegalArgumentException e) {
            assertEquals("ReadablePartial objects must have the same set of fields", e.getMessage());
        }
    }
    
    @Test
    public void testBetween_NotContiguous() {
        ReadablePartial partial1 = new LocalDate(2020, 1, 1);
        ReadablePartial partial2 = new LocalDate(2021, 1, 1);
        ReadablePeriod period = Duration.standardDays(365);
        
        try {
            int result = BaseSingleFieldPeriod.between(partial1, partial2, period);
            fail("Exception expected");
        } catch (IllegalArgumentException e) {
            assertEquals("ReadablePartial objects must be contiguous", e.getMessage());
        }
    }
    
    @Test
    public void testStandardPeriodIn() {
        ReadablePeriod period = Duration.standardDays(365);
        long n = 24 * 60 * 60 * 1000;
        
        int result = BaseSingleFieldPeriod.standardPeriodIn(period, n);
        
        assertEquals(result, 365);
    }
    
    @Test
    public void testStandardPeriodIn_NullPeriod() {
        ReadablePeriod period = null;
        long n = 24 * 60 * 60 * 1000;
        
        int result = BaseSingleFieldPeriod.standardPeriodIn(period, n);
        
        assertEquals(result, 0);
    }
    
    @Test
    public void testStandardPeriodIn_NotPrecise() {
        ReadablePeriod period = new Period().withFieldAdded(DurationFieldType.days(), 1);
        long n = 24 * 60 * 60 * 1000;
        
        try {
            int result = BaseSingleFieldPeriod.standardPeriodIn(period, n);
            fail("Exception expected");
        } catch (IllegalArgumentException e) {
            assertEquals("Cannot convert period to duration as days is not precise in the period P1D", e.getMessage());
        }
    }
    
    @Test
    public void testGetValue() {
        BaseSingleFieldPeriod period = new MockSingleFieldPeriod(5);
        
        int result = period.getValue();
        
        assertEquals(result, 5);
    }
    
    @Test
    public void testSetValue() {
        BaseSingleFieldPeriod period = new MockSingleFieldPeriod(5);
        
        period.setValue(10);
        
        int result = period.getValue();
        
        assertEquals(result, 10);
    }
    
    @Test
    public void testGetFieldType() {
        BaseSingleFieldPeriod period = new MockSingleFieldPeriod(5);
        
        DurationFieldType result = period.getFieldType();
        
        assertEquals(result, DurationFieldType.days());
    }
    
    @Test
    public void testGetPeriodType() {
        BaseSingleFieldPeriod period = new MockSingleFieldPeriod(5);
        
        PeriodType result = period.getPeriodType();
        
        assertEquals(result, PeriodType.days());
    }
    
    @Test
    public void testSize() {
        BaseSingleFieldPeriod period = new MockSingleFieldPeriod(5);
        
        int result = period.size();
        
        assertEquals(result, 1);
    }
    
    @Test
    public void testGetFieldType_IndexOutOfBounds() {
        BaseSingleFieldPeriod period = new MockSingleFieldPeriod(5);
        
        try {
            DurationFieldType result = period.getFieldType(1);
            fail("Exception expected");
        } catch (IndexOutOfBoundsException e) {
            assertEquals("1", e.getMessage());
        }
    }
    
    @Test
    public void testGetValue_IndexOutOfBounds() {
        BaseSingleFieldPeriod period = new MockSingleFieldPeriod(5);
        
        try {
            int result = period.getValue(1);
            fail("Exception expected");
        } catch (IndexOutOfBoundsException e) {
            assertEquals("1", e.getMessage());
        }
    }
    
    @Test
    public void testGet() {
        BaseSingleFieldPeriod period = new MockSingleFieldPeriod(5);
        
        int result = period.get(DurationFieldType.days());
        
        assertEquals(result, 5);
    }
    
    @Test
    public void testGet_NotSupported() {
        BaseSingleFieldPeriod period = new MockSingleFieldPeriod(5);
        DurationFieldType type = DurationFieldType.weeks();
        
        int result = period.get(type);
        
        assertEquals(result, 0);
    }
    
    @Test
    public void testIsSupported() {
        BaseSingleFieldPeriod period = new MockSingleFieldPeriod(5);
        DurationFieldType type = DurationFieldType.days();
        
        boolean result = period.isSupported(type);
        
        assertTrue(result);
    }
    
    @Test
    public void testIsSupported_NotSupported() {
        BaseSingleFieldPeriod period = new MockSingleFieldPeriod(5);
        DurationFieldType type = DurationFieldType.weeks();
        
        boolean result = period.isSupported(type);
        
        assertFalse(result);
    }
    
    @Test
    public void testToPeriod() {
        BaseSingleFieldPeriod period = new MockSingleFieldPeriod(5);
        
        Period result = period.toPeriod();
        
        assertEquals(result, Period.days(5));
    }
    
    @Test
    public void testToMutablePeriod() {
        BaseSingleFieldPeriod period = new MockSingleFieldPeriod(5);
        
        MutablePeriod result = period.toMutablePeriod();
        
        assertEquals(result, new MutablePeriod().withFieldAdded(DurationFieldType.days(), 5));
    }
    
    @Test
    public void testEquals_True() {
        BaseSingleFieldPeriod period1 = new MockSingleFieldPeriod(5);
        BaseSingleFieldPeriod period2 = new MockSingleFieldPeriod(5);
        
        boolean result = period1.equals(period2);
        
        assertTrue(result);
    }
    
    @Test
    public void testEquals_False_DifferentType() {
        BaseSingleFieldPeriod period1 = new MockSingleFieldPeriod(5);
        BaseSingleFieldPeriod period2 = new MockSingleFieldPeriod2(5);
        
        boolean result = period1.equals(period2);
        
        assertFalse(result);
    }
    
    @Test
    public void testEquals_False_DifferentValue() {
        BaseSingleFieldPeriod period1 = new MockSingleFieldPeriod(5);
        BaseSingleFieldPeriod period2 = new MockSingleFieldPeriod(10);
        
        boolean result = period1.equals(period2);
        
        assertFalse(result);
    }
    
    @Test
    public void testHashCode() {
        BaseSingleFieldPeriod period = new MockSingleFieldPeriod(5);
        
        int result = period.hashCode();
        
        assertEquals(result, 27 * (27 * 17 + 5) + DurationFieldType.days().hashCode());
    }
    
    @Test
    public void testCompareTo_GreaterThan() {
        BaseSingleFieldPeriod period1 = new MockSingleFieldPeriod(10);
        BaseSingleFieldPeriod period2 = new MockSingleFieldPeriod(5);
        
        int result = period1.compareTo(period2);
        
        assertEquals(result, 1);
    }
    
    @Test
    public void testCompareTo_LessThan() {
        BaseSingleFieldPeriod period1 = new MockSingleFieldPeriod(5);
        BaseSingleFieldPeriod period2 = new MockSingleFieldPeriod(10);
        
        int result = period1.compareTo(period2);
        
        assertEquals(result, -1);
    }
    
    @Test
    public void testCompareTo_Equal() {
        BaseSingleFieldPeriod period1 = new MockSingleFieldPeriod(5);
        BaseSingleFieldPeriod period2 = new MockSingleFieldPeriod(5);
        
        int result = period1.compareTo(period2);
        
        assertEquals(result, 0);
    }
    
    @Test
    public void testCompareTo_DifferentType() {
        BaseSingleFieldPeriod period1 = new MockSingleFieldPeriod(5);
        BaseSingleFieldPeriod period2 = new MockSingleFieldPeriod2(5);
        
        try {
            int result = period1.compareTo(period2);
            fail("Exception expected");
        } catch (ClassCastException e) {
            assertEquals("class BaseSingleFieldPeriod cannot be compared to class MockSingleFieldPeriod2", e.getMessage());
        }
    }
    
    private static class MockSingleFieldPeriod extends BaseSingleFieldPeriod {
    
        public MockSingleFieldPeriod(int iPeriod) {
            super(iPeriod);
        }
    
        @Override
        public DurationFieldType getFieldType() {
            return DurationFieldType.days();
        }
    
        @Override
        public PeriodType getPeriodType() {
            return PeriodType.days();
        }
    }
    
    private static class MockSingleFieldPeriod2 extends BaseSingleFieldPeriod {
    
        public MockSingleFieldPeriod2(int iPeriod) {
            super(iPeriod);
        }
    
        @Override
        public DurationFieldType getFieldType() {
            return DurationFieldType.weeks();
        }
    
        @Override
        public PeriodType getPeriodType() {
            return PeriodType.weeks();
        }
    }
}