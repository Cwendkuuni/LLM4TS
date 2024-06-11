
import org.junit.Test;
import static org.junit.Assert.*;

public class BaseSingleFieldPeriodTest {

    @Test
    public void testBetweenWithTwoReadableInstantObjectsAndDurationFieldType() {
        // Test case 1: Null ReadableInstant objects
        try {
            BaseSingleFieldPeriod.between(null, null, DurationFieldType.days());
            fail("Expected IllegalArgumentException to be thrown");
        } catch (IllegalArgumentException ex) {
            assertEquals("ReadableInstant objects must not be null", ex.getMessage());
        }
        
        // Test case 2: ReadableInstant objects with different chronologies
        ReadableInstant instant1 = new DateTime();
        ReadableInstant instant2 = new DateTime().withChronology(ISOChronology.getInstanceUTC());
        try {
            BaseSingleFieldPeriod.between(instant1, instant2, DurationFieldType.days());
            fail("Expected IllegalArgumentException to be thrown");
        } catch (IllegalArgumentException ex) {
            assertEquals("ReadableInstant objects must have the same set of fields", ex.getMessage());
        }

        // Test case 3: Valid inputs
        ReadableInstant instant3 = new DateTime().withDate(2020, 1, 1);
        ReadableInstant instant4 = new DateTime().withDate(2020, 1, 31);
        int result = BaseSingleFieldPeriod.between(instant3, instant4, DurationFieldType.months());
        assertEquals(0, result);
    }
    
    @Test
    public void testBetweenWithTwoReadablePartialObjectsAndReadablePeriod() {
        // Test case 1: Null ReadablePartial objects
        try {
            BaseSingleFieldPeriod.between(null, null, Period.weeks());
            fail("Expected IllegalArgumentException to be thrown");
        } catch (IllegalArgumentException ex) {
            assertEquals("ReadablePartial objects must not be null", ex.getMessage());
        }
        
        // Test case 2: ReadablePartial objects with different sizes
        ReadablePartial partial1 = new Partial(DateTimeFieldType.year(), 2020);
        ReadablePartial partial2 = new Partial(DateTimeFieldType.yearMonth(), 2020, 1);
        try {
            BaseSingleFieldPeriod.between(partial1, partial2, Period.years());
            fail("Expected IllegalArgumentException to be thrown");
        } catch (IllegalArgumentException ex) {
            assertEquals("ReadablePartial objects must have the same set of fields", ex.getMessage());
        }
        
        // Test case 3: ReadablePartial objects with different field types
        ReadablePartial partial3 = new Partial(DateTimeFieldType.yearMonth(), 2020, 1);
        ReadablePartial partial4 = new Partial(DateTimeFieldType.monthOfYear(), 2020, 1);
        try {
            BaseSingleFieldPeriod.between(partial3, partial4, Period.months());
            fail("Expected IllegalArgumentException to be thrown");
        } catch (IllegalArgumentException ex) {
            assertEquals("ReadablePartial objects must have the same set of fields", ex.getMessage());
        }
        
        // Test case 4: ReadablePartial objects that are discontinuous
        ReadablePartial partial5 = new Partial(DateTimeFieldType.year(), 2020);
        ReadablePartial partial6 = new Partial(DateTimeFieldType.monthOfYear(), 2020);
        try {
            BaseSingleFieldPeriod.between(partial5, partial6, Period.years());
            fail("Expected IllegalArgumentException to be thrown");
        } catch (IllegalArgumentException ex) {
            assertEquals("ReadablePartial objects must be contiguous", ex.getMessage());
        }
        
        // Test case 5: Valid inputs
        ReadablePartial partial7 = new Partial(DateTimeFieldType.yearMonthDay(), 2020, 1, 1);
        ReadablePartial partial8 = new Partial(DateTimeFieldType.yearMonthDay(), 2020, 1, 31);
        int result = BaseSingleFieldPeriod.between(partial7, partial8, Period.days());
        assertEquals(30, result);
    }
    
    @Test
    public void testStandardPeriodInWithReadablePeriodAndLong() {
        // Test case 1: Null ReadablePeriod
        int result1 = BaseSingleFieldPeriod.standardPeriodIn(null, 86400000L);
        assertEquals(0, result1);
        
        // Test case 2: ReadablePeriod with unsupported field
        ReadablePeriod period2 = new Period(1, 0, 0, 0);
        try {
            BaseSingleFieldPeriod.standardPeriodIn(period2, 86400000L);
            fail("Expected IllegalArgumentException to be thrown");
        } catch (IllegalArgumentException ex) {
            assertEquals("Cannot convert period to duration as years is not precise in the period P1M", ex.getMessage());
        }
        
        // Test case 3: Valid inputs
        ReadablePeriod period3 = new Period(0, 1, 0, 0, 0, 0, 0, 0);
        int result3 = BaseSingleFieldPeriod.standardPeriodIn(period3, 86400000L);
        assertEquals(1, result3);
    }
    
    @Test
    public void testEqualsWithObject() {
        // Test case 1: Same object
        BaseSingleFieldPeriod period1 = new Years(1);
        assertTrue(period1.equals(period1));
        
        // Test case 2: Object of different type
        assertFalse(period1.equals("string"));
        
        // Test case 3: Object of same type with different period type
        BaseSingleFieldPeriod period2 = new Months(1);
        assertFalse(period1.equals(period2));
        
        // Test case 4: Object of same type with same period type but different value
        BaseSingleFieldPeriod period3 = new Months(2);
        assertFalse(period1.equals(period3));
        
        // Test case 5: Object of same type with same period type and same value
        BaseSingleFieldPeriod period4 = new Years(1);
        assertTrue(period1.equals(period4));
    }
    
    @Test
    public void testHashCode() {
        // Test case 1: Same object
        BaseSingleFieldPeriod period1 = new Years(1);
        assertTrue(period1.hashCode() == period1.hashCode());
        
        // Test case 2: Object of different type
        BaseSingleFieldPeriod period2 = new Months(1);
        assertFalse(period1.hashCode() == period2.hashCode());
        
        // Test case 3: Object of same type with different value
        BaseSingleFieldPeriod period3 = new Years(2);
        assertFalse(period1.hashCode() == period3.hashCode());
        
        // Test case 4: Object of same type with same value
        BaseSingleFieldPeriod period4 = new Years(1);
        assertTrue(period1.hashCode() == period4.hashCode());
    }
    
    @Test
    public void testCompareToWithBaseSingleFieldPeriod() {
        // Test case 1: Object of different type
        BaseSingleFieldPeriod period1 = new Years(1);
        BaseSingleFieldPeriod period2 = new Months(1);
        try {
            period1.compareTo(period2);
            fail("Expected ClassCastException to be thrown");
        } catch (ClassCastException ex) {
            assertEquals("class Years cannot be compared to class Months", ex.getMessage());
        }
        
        // Test case 2: Object of same type with smaller value
        BaseSingleFieldPeriod period3 = new Years(2);
        int result1 = period1.compareTo(period3);
        assertEquals(-1, result1);
        
        // Test case 3: Object of same type with greater value
        BaseSingleFieldPeriod period4 = new Years(1);
        int result2 = period1.compareTo(period4);
        assertEquals(0, result2);
        
        // Test case 4: Object of same type with same value
        BaseSingleFieldPeriod period5 = new Years(1);
        int result3 = period1.compareTo(period5);
        assertEquals(0, result3);
    }

}
