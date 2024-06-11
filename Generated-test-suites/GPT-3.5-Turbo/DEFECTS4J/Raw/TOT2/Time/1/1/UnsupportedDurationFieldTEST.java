import static org.junit.Assert.*;
import org.junit.Test;
import org.joda.time.DurationFieldType;

public class UnsupportedDurationFieldTest {
    
    @Test
    public void testGetInstance() {
        // typical use case
        UnsupportedDurationField field = UnsupportedDurationField.getInstance(DurationFieldType.seconds());
        assertNotNull(field);
        
        // edge case of null field type
        field = UnsupportedDurationField.getInstance(null);
        assertNotNull(field);
    }
    
    @Test
    public void testGetType() {
        UnsupportedDurationField field = UnsupportedDurationField.getInstance(DurationFieldType.seconds());
        assertEquals(DurationFieldType.seconds(), field.getType());
    }
    
    @Test
    public void testName() {
        UnsupportedDurationField field = UnsupportedDurationField.getInstance(DurationFieldType.minutes());
        assertEquals("minutes", field.getName());
    }
    
    @Test
    public void testIsSupported() {
        UnsupportedDurationField field = UnsupportedDurationField.getInstance(DurationFieldType.hours());
        assertFalse(field.isSupported());
    }
    
    @Test
    public void testIsPrecise() {
        UnsupportedDurationField field = UnsupportedDurationField.getInstance(DurationFieldType.days());
        assertTrue(field.isPrecise());
    }
    
    // Add more test cases for other methods as per the requirements
    
}