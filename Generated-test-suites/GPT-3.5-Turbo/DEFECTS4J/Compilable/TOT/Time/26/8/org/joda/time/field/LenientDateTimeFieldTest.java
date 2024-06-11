package org.joda.time.field;
import static org.junit.Assert.*;
import org.junit.Test;
import org.joda.time.DateTime;
import org.joda.time.DateTimeField;
import org.joda.time.field.LenientDateTimeField;
import org.joda.time.chrono.ISOChronology;

public class LenientDateTimeFieldTest {
    
    private static final DateTimeField field = ISOChronology.getInstance().year();
    private static final DateTimeField lenientField = LenientDateTimeField.getInstance(field, ISOChronology.getInstance());
    
    @Test
    public void testIsLenient_true() {
        assertTrue(lenientField.isLenient());
    }
    
    @Test
    public void testSet_outOfBoundsValue() {
        DateTime dt = new DateTime(2022, 1, 1, 0, 0);
        long instantBefore = dt.getMillis();
        
        long updatedInstant = lenientField.set(dt.getMillis(), 5000); // set value to 5000, which is out of bounds
        
        DateTime updatedDt = new DateTime(updatedInstant);
        int updatedValue = updatedDt.getYear();
        long instantAfter = updatedDt.getMillis();
        
        assertEquals(5000, updatedValue);
        assertEquals(instantAfter, instantBefore);
    }
    
    @Test
    public void testSet_withinBoundsValue() {
        DateTime dt = new DateTime(2022, 1, 1, 0, 0);
        long instantBefore = dt.getMillis();
        
        long updatedInstant = lenientField.set(dt.getMillis(), 2025); // set value to 2025, which is within bounds
        
        DateTime updatedDt = new DateTime(updatedInstant);
        int updatedValue = updatedDt.getYear();
        long instantAfter = updatedDt.getMillis();
        
        assertEquals(2025, updatedValue);
        assertEquals(instantAfter, instantBefore);
    }
    
    @Test
    public void testSet_negativeValue() {
        DateTime dt = new DateTime(2022, 1, 1, 0, 0);
        
        long updatedInstant = lenientField.set(dt.getMillis(), -500); // set value to -500, which is a negative value
        
        DateTime updatedDt = new DateTime(updatedInstant);
        int updatedValue = updatedDt.getYear();
        
        assertEquals(-500, updatedValue);
    }
    
    @Test
    public void testSet_largeValue() {
        DateTime dt = new DateTime(2022, 1, 1, 0, 0);
        
        long updatedInstant = lenientField.set(dt.getMillis(), 500000); // set value to 500000, which is a large value
        
        DateTime updatedDt = new DateTime(updatedInstant);
        int updatedValue = updatedDt.getYear();
        
        assertEquals(500000, updatedValue);
    }
}