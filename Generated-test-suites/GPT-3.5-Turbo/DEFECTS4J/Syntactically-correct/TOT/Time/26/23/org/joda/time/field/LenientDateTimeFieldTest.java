import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNull;

import org.joda.time.Chronology;
import org.joda.time.DateTime;
import org.joda.time.DateTimeField;
import org.joda.time.chrono.ISOChronology;
import org.junit.Before;
import org.junit.Test;

public class LenientDateTimeFieldTest {
    
    private LenientDateTimeField lenientDateTimeField;
    private DateTimeField delegatedDateTimeField;
    private Chronology base;
    
    @Before
    public void setup() {
        delegatedDateTimeField = ISOChronology.getInstance().dayOfMonth();
        base = ISOChronology.getInstanceUTC();
        lenientDateTimeField = new LenientDateTimeField(delegatedDateTimeField, base);
    }
    
    @Test
    public void testGetInstance_NullField_ReturnsNull() {
        DateTimeField result = LenientDateTimeField.getInstance(null, base);
        assertNull(result);
    }
    
    @Test
    public void testGetInstance_StrictField_ReturnsWrappedField() {
        DateTimeField strictField = new StrictDateTimeField(delegatedDateTimeField);
        DateTimeField result = LenientDateTimeField.getInstance(strictField, base);
        // Verify if the returned field is the wrapped field
        assertEquals(delegatedDateTimeField, result);
    }
    
    @Test
    public void testSet_ValueWithinBounds_ReturnsUpdatedInstant() {
        DateTime initialDateTime = new DateTime(2021, 1, 15, 0, 0);
        long initialInstant = initialDateTime.getMillis();
        int value = 20;
        
        long updatedInstant = lenientDateTimeField.set(initialInstant, value);
        
        DateTime expectedDateTime = new DateTime(2021, 1, value, 0, 0);
        long expectedInstant = expectedDateTime.getMillis();
        assertEquals(expectedInstant, updatedInstant);
    }
    
    @Test
    public void testSet_ValueOutOfBounds_ReturnsUpdatedInstantWithAddedDifference() {
        DateTime initialDateTime = new DateTime(2021, 1, 15, 0, 0);
        long initialInstant = initialDateTime.getMillis();
        int value = 50;
        
        long updatedInstant = lenientDateTimeField.set(initialInstant, value);
        
        DateTime expectedDateTime = new DateTime(2021, 2, 19, 0, 0);
        long expectedInstant = expectedDateTime.getMillis();
        assertEquals(expectedInstant, updatedInstant);
    }
    
    @Test
    public void testSet_ValueOutOfBoundsNegative_ReturnsUpdatedInstantWithAddedDifference() {
        DateTime initialDateTime = new DateTime(2021, 1, 15, 0, 0);
        long initialInstant = initialDateTime.getMillis();
        int value = -5;
        
        long updatedInstant = lenientDateTimeField.set(initialInstant, value);
        
        DateTime expectedDateTime = new DateTime(2020, 12, 26, 0, 0);
        long expectedInstant = expectedDateTime.getMillis();
        assertEquals(expectedInstant, updatedInstant);
    }
    
    @Test
    public void testSet_ValueOutOfBoundsMaxDate_ReturnsUpdatedInstantWithAddedDifference() {
        DateTime initialDateTime = new DateTime(Long.MAX_VALUE);
        long initialInstant = initialDateTime.getMillis();
        int value = 25;
        
        long updatedInstant = lenientDateTimeField.set(initialInstant, value);
        
        DateTime expectedDateTime = new DateTime(Long.MIN_VALUE + 24, 1, value, 0, 0);
        long expectedInstant = expectedDateTime.getMillis();
        assertEquals(expectedInstant, updatedInstant);
    }
    
    @Test
    public void testSet_ValueOutOfBoundsMinDate_ReturnsUpdatedInstantWithAddedDifference() {
        DateTime initialDateTime = new DateTime(Long.MIN_VALUE);
        long initialInstant = initialDateTime.getMillis();
        int value = 5;
        
        long updatedInstant = lenientDateTimeField.set(initialInstant, value);
        
        DateTime expectedDateTime = new DateTime(Long.MIN_VALUE + 4, 1, 1, 0, 0);
        long expectedInstant = expectedDateTime.getMillis();
        assertEquals(expectedInstant, updatedInstant);
    }
    
}