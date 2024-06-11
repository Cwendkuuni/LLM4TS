import static org.junit.Assert.*;
import org.junit.Test;

public class BaseSingleFieldPeriodTest {

    @Test
    public void testBetweenWithReadableInstant() {
        // Test with valid inputs
        ReadableInstant start = new DateTime(2019, 1, 1, 0, 0, 0);
        ReadableInstant end = new DateTime(2020, 1, 1, 0, 0, 0);
        int result = BaseSingleFieldPeriod.between(start, end, DurationFieldType.years());
        assertEquals(result, 1);
        
        // Test with null inputs
        try {
            BaseSingleFieldPeriod.between(null, end, DurationFieldType.years());
            fail("Expected IllegalArgumentException");
        } catch (IllegalArgumentException e) {
            // Exception expected
        }
    }
    
    @Test
    public void testBetweenWithReadablePartial() {
        // Test with valid inputs
        ReadablePartial start = new LocalDate(2019, 1, 1);
        ReadablePartial end = new LocalDate(2020, 1, 1);
        ReadablePeriod period = new Period(1, 0, 0, 0);
        int result = BaseSingleFieldPeriod.between(start, end, period);
        assertEquals(result, 1);
        
        // Test with null inputs
        try {
            BaseSingleFieldPeriod.between(start, null, period);
            fail("Expected IllegalArgumentException");
        } catch (IllegalArgumentException e) {
            // Exception expected
        }
        
        // Test with different set of fields in ReadablePartial objects
        ReadablePartial start2 = new LocalDate(2019, 1, 1);
        ReadablePartial end2 = new LocalDate(2020, 1, 1, 12, 0, 0);
        try {
            BaseSingleFieldPeriod.between(start2, end2, period);
            fail("Expected IllegalArgumentException");
        } catch (IllegalArgumentException e) {
            // Exception expected
        }
        
        // Test with non-contiguous ReadablePartial objects
        ReadablePartial start3 = new LocalDate(2019, 1, 1);
        ReadablePartial end3 = new LocalDate(2020, 1, 1).withYearOfEra(1);
        try {
            BaseSingleFieldPeriod.between(start3, end3, period);
            fail("Expected IllegalArgumentException");
        } catch (IllegalArgumentException e) {
            // Exception expected
        }
    }
    
    @Test
    public void testStandardPeriodIn() {
        // Test with valid inputs
        ReadablePeriod period = new Period(1, 2, 3, 4);
        int result = BaseSingleFieldPeriod.standardPeriodIn(period, 1);
        assertEquals(result, 373200000); 
        
        // Test with null input
        int result2 = BaseSingleFieldPeriod.standardPeriodIn(null, 1);
        assertEquals(result2, 0);
        
        // Test with imprecise field in the period
        ReadablePeriod period2 = new Period(0, 0, 1, 0, 0, 0, 0, 0);
        try {
            BaseSingleFieldPeriod.standardPeriodIn(period2, 1);
            fail("Expected IllegalArgumentException");
        } catch (IllegalArgumentException e) {
            // Exception expected
        }
    }
    
    @Test
    public void testGetValue() {
        BaseSingleFieldPeriod period = new Years(5);
        int result = period.getValue();
        assertEquals(result, 5);
        
        period.setValue(10);
        int result2 = period.getValue();
        assertEquals(result2, 10);
    }
    
    @Test
    public void testGetFieldType() {
        BaseSingleFieldPeriod period = new Years(5);
        DurationFieldType result = period.getFieldType();
        assertEquals(result, DurationFieldType.years());
    }
    
    @Test
    public void testGetPeriodType() {
        BaseSingleFieldPeriod period = new Years(5);
        PeriodType result = period.getPeriodType();
        assertEquals(result, PeriodType.standard());
    }
    
    @Test
    public void testSize() {
        BaseSingleFieldPeriod period = new Years(5);
        int result = period.size();
        assertEquals(result, 1);
    }
    
    @Test
    public void testGetFieldTypeWithIndex() {
        BaseSingleFieldPeriod period = new Years(5);
        DurationFieldType result = period.getFieldType(0);
        assertEquals(result, DurationFieldType.years());
        
        try {
            period.getFieldType(1);
            fail("Expected IndexOutOfBoundsException");
        } catch (IndexOutOfBoundsException e) {
            // Exception expected
        }
    }
    
    @Test
    public void testGetValueWithIndex() {
        BaseSingleFieldPeriod period = new Years(5);
        int result = period.getValue(0);
        assertEquals(result, 5);
        
        try {
            period.getValue(1);
            fail("Expected IndexOutOfBoundsException");
        } catch (IndexOutOfBoundsException e) {
            // Exception expected
        }
    }
    
    @Test
    public void testGet() {
        BaseSingleFieldPeriod period = new Years(5);
        int result = period.get(DurationFieldType.years());
        assertEquals(result, 5);
        
        int result2 = period.get(DurationFieldType.months());
        assertEquals(result2, 0);
    }
    
    @Test
    public void testIsSupported() {
        BaseSingleFieldPeriod period = new Years(5);
        boolean result = period.isSupported(DurationFieldType.years());
        assertTrue(result);
        
        boolean result2 = period.isSupported(DurationFieldType.months());
        assertFalse(result2);
    }
    
    @Test
    public void testToPeriod() {
        BaseSingleFieldPeriod period = new Years(5);
        Period result = period.toPeriod();
        Period expected = new Period(5, 0, 0, 0);
        assertEquals(result, expected);
    }
    
    @Test
    public void testToMutablePeriod() {
        BaseSingleFieldPeriod period = new Years(5);
        MutablePeriod result = period.toMutablePeriod();
        MutablePeriod expected = new MutablePeriod();
        expected.setYears(5);
        assertEquals(result, expected);
    }
    
    @Test
    public void testEquals() {
        BaseSingleFieldPeriod period1 = new Years(5);
        BaseSingleFieldPeriod period2 = new Years(5);
        assertTrue(period1.equals(period2));
        
        BaseSingleFieldPeriod period3 = new Days(5);
        assertFalse(period1.equals(period3));
    }
    
    @Test
    public void testHashCode() {
        BaseSingleFieldPeriod period = new Years(5);
        int result = period.hashCode();
        assertEquals(result, 1525);
    }
    
    @Test
    public void testCompareTo() {
        BaseSingleFieldPeriod period1 = new Years(5);
        BaseSingleFieldPeriod period2 = new Years(5);
        assertEquals(period1.compareTo(period2), 0);
        
        BaseSingleFieldPeriod period3 = new Years(10);
        assertTrue(period1.compareTo(period3) < 0);
        
        BaseSingleFieldPeriod period4 = new Years(3);
        assertTrue(period1.compareTo(period4) > 0);
        
        try {
            period1.compareTo(new Days(5));
            fail("Expected ClassCastException");
        } catch (ClassCastException e) {
            // Exception expected
        }
    }
}