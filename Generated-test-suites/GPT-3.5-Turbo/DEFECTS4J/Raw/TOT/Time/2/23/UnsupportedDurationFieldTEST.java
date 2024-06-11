import org.joda.time.*;
import org.joda.time.field.UnsupportedDurationField;
import org.junit.Test;
import static org.junit.Assert.*;

public class UnsupportedDurationFieldTest {

    @Test
    public void testGetInstance() {
        // test when type is null
        try {
            UnsupportedDurationField.getInstance(null);
            fail("Expected IllegalArgumentException");
        } catch (IllegalArgumentException e) {
            // pass
        }
        
        // test when type is not null
        DurationFieldType type = DurationFieldType.centuries();
        UnsupportedDurationField field = UnsupportedDurationField.getInstance(type);
        assertSame(type, field.getType());
    }

    @Test
    public void testIsSupported() {
        DurationFieldType type = DurationFieldType.years();
        UnsupportedDurationField field = UnsupportedDurationField.getInstance(type);
        
        // test when field is not supported
        assertFalse(field.isSupported());
    }
    
    @Test
    public void testIsPrecise() {
        DurationFieldType type = DurationFieldType.months();
        UnsupportedDurationField field = UnsupportedDurationField.getInstance(type);
        
        // test when field is precise
        assertTrue(field.isPrecise());
    }
    
    @Test
    public void testGetValue() {
        DurationFieldType type = DurationFieldType.weeks();
        UnsupportedDurationField field = UnsupportedDurationField.getInstance(type);
        
        // test getValue(long duration)
        try {
            field.getValue(100);
            fail("Expected UnsupportedOperationException");
        } catch (UnsupportedOperationException e) {
            // pass
        }
        
        // test getValueAsLong(long duration)
        try {
            field.getValueAsLong(100);
            fail("Expected UnsupportedOperationException");
        } catch (UnsupportedOperationException e) {
            // pass
        }
        
        // test getValue(long duration, long instant)
        try {
            field.getValue(100, 1000);
            fail("Expected UnsupportedOperationException");
        } catch (UnsupportedOperationException e) {
            // pass
        }
        
        // test getValueAsLong(long duration, long instant)
        try {
            field.getValueAsLong(100, 1000);
            fail("Expected UnsupportedOperationException");
        } catch (UnsupportedOperationException e) {
            // pass
        }
    }
    
    @Test
    public void testGetMillis() {
        DurationFieldType type = DurationFieldType.days();
        UnsupportedDurationField field = UnsupportedDurationField.getInstance(type);
        
        // test getMillis(int value)
        try {
            field.getMillis(100);
            fail("Expected UnsupportedOperationException");
        } catch (UnsupportedOperationException e) {
            // pass
        }
        
        // test getMillis(long value)
        try {
            field.getMillis(100L);
            fail("Expected UnsupportedOperationException");
        } catch (UnsupportedOperationException e) {
            // pass
        }
        
        // test getMillis(int value, long instant)
        try {
            field.getMillis(100, 1000L);
            fail("Expected UnsupportedOperationException");
        } catch (UnsupportedOperationException e) {
            // pass
        }
        
        // test getMillis(long value, long instant)
        try {
            field.getMillis(100L, 1000L);
            fail("Expected UnsupportedOperationException");
        } catch (UnsupportedOperationException e) {
            // pass
        }
    }
    
    @Test
    public void testAdd() {
        DurationFieldType type = DurationFieldType.hours();
        UnsupportedDurationField field = UnsupportedDurationField.getInstance(type);
        
        // test add(long instant, int value)
        try {
            field.add(1000L, 10);
            fail("Expected UnsupportedOperationException");
        } catch (UnsupportedOperationException e) {
            // pass
        }
        
        // test add(long instant, long value)
        try {
            field.add(1000L, 10L);
            fail("Expected UnsupportedOperationException");
        } catch (UnsupportedOperationException e) {
            // pass
        }
    }
    
    @Test
    public void testGetDifference() {
        DurationFieldType type = DurationFieldType.minutes();
        UnsupportedDurationField field = UnsupportedDurationField.getInstance(type);
        
        // test getDifference(long minuendInstant, long subtrahendInstant)
        try {
            field.getDifference(1000L, 500L);
            fail("Expected UnsupportedOperationException");
        } catch (UnsupportedOperationException e) {
            // pass
        }
        
        // test getDifferenceAsLong(long minuendInstant, long subtrahendInstant)
        try {
            field.getDifferenceAsLong(1000L, 500L);
            fail("Expected UnsupportedOperationException");
        } catch (UnsupportedOperationException e) {
            // pass
        }
    }
    
    @Test
    public void testGetUnitMillis() {
        DurationFieldType type = DurationFieldType.seconds();
        UnsupportedDurationField field = UnsupportedDurationField.getInstance(type);
        
        // test getUnitMillis()
        assertEquals(0L, field.getUnitMillis());
    }
    
    @Test
    public void testCompareTo() {
        DurationFieldType type1 = DurationFieldType.millis();
        DurationFieldType type2 = DurationFieldType.seconds();
        UnsupportedDurationField field1 = UnsupportedDurationField.getInstance(type1);
        UnsupportedDurationField field2 = UnsupportedDurationField.getInstance(type2);
        
        // test compareTo(DurationField durationField)
        assertEquals(0, field1.compareTo(field2));
    }
}