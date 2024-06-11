import static org.junit.Assert.*;
import org.junit.Test;
import org.joda.time.DurationFieldType;
import org.joda.time.field.UnsupportedDurationField;

public class UnsupportedDurationFieldTest {

    // Test for getType() method
    @Test
    public void testGetType() {
        UnsupportedDurationField durationField = UnsupportedDurationField.getInstance(DurationFieldType.eras());
        assertEquals(DurationFieldType.eras(), durationField.getType());
    }
    
    // Test for getName() method
    @Test
    public void testGetName() {
        UnsupportedDurationField durationField = UnsupportedDurationField.getInstance(DurationFieldType.eras());
        assertEquals("eras", durationField.getName());
    }
    
    // Test for isSupported() method
    @Test(expected = UnsupportedOperationException.class)
    public void testIsSupported() {
        UnsupportedDurationField durationField = UnsupportedDurationField.getInstance(DurationFieldType.eras());
        durationField.isSupported();
    }
    
    // Test for isPrecise() method
    @Test
    public void testIsPrecise() {
        UnsupportedDurationField durationField = UnsupportedDurationField.getInstance(DurationFieldType.eras());
        assertTrue(durationField.isPrecise());
    }
  
    // Test for getValue() method
    @Test(expected = UnsupportedOperationException.class)
    public void testGetValue() {
        UnsupportedDurationField durationField = UnsupportedDurationField.getInstance(DurationFieldType.eras());
        durationField.getValue(10);
    }
  
    // Add more test cases for other methods as needed
  
}