import static org.junit.Assert.*;
import org.joda.time.Chronology;
import org.joda.time.DateTimeField;
import org.joda.time.chrono.LenientChronology;
import org.joda.time.field.LenientDateTimeField;
import org.joda.time.field.StrictDateTimeField;
import org.junit.Test;

public class LenientDateTimeFieldTest {

    @Test
    public void getInstance_shouldReturnNullForNullField() {
        DateTimeField field = null;
        Chronology base = new LenientChronology();
        
        DateTimeField result = LenientDateTimeField.getInstance(field, base);
        
        assertNull(result);
    }
    
    @Test
    public void getInstance_shouldReturnSameFieldIfAlreadyLenient() {
        DateTimeField field = new LenientDateTimeField(null, null);
        Chronology base = new LenientChronology();
        
        DateTimeField result = LenientDateTimeField.getInstance(field, base);
        
        assertSame(field, result);
    }
    
    @Test
    public void getInstance_shouldReturnLenientFieldIfFieldIsStrict() {
        DateTimeField field = new StrictDateTimeField(null, null);
        Chronology base = new LenientChronology();
        
        DateTimeField result = LenientDateTimeField.getInstance(field, base);
        
        assertTrue(result instanceof LenientDateTimeField);
    }
    
    @Test
    public void getInstance_shouldReturnLenientFieldIfFieldIsNotLenient() {
        DateTimeField field = new SomeDateTimeField(null, null);
        Chronology base = new LenientChronology();
        
        DateTimeField result = LenientDateTimeField.getInstance(field, base);
        
        assertTrue(result instanceof LenientDateTimeField);
    }
    
    @Test
    public void constructor_shouldSetBase() {
        DateTimeField field = new SomeDateTimeField(null, null);
        Chronology base = new LenientChronology();
        
        LenientDateTimeField lenientField = new LenientDateTimeField(field, base);
        
        assertSame(base, lenientField.getBase());
    }
    
    @Test
    public void isLenient_shouldReturnTrue() {
        DateTimeField field = new SomeDateTimeField(null, null);
        Chronology base = new LenientChronology();
        
        LenientDateTimeField lenientField = new LenientDateTimeField(field, base);
        
        assertTrue(lenientField.isLenient());
    }
    
    @Test
    public void set_shouldAddDifferenceToCurrentValue() {
        DateTimeField field = new SomeDateTimeField(null, null);
        Chronology base = new LenientChronology();
        long instant = 123456789;
        int value = 10;
        
        LenientDateTimeField lenientField = new LenientDateTimeField(field, base);
        long result = lenientField.set(instant, value);
        
        assertEquals(value, lenientField.get(result));
    }
}