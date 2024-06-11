import org.joda.time.chrono.LenientChronology;
import org.joda.time.field.LenientDateTimeField;
import org.joda.time.field.LenientDateTimeField;
import org.joda.time.DateTimeField;
import org.joda.time.Chronology;
import org.junit.Before;
import org.junit.Test;
import static org.junit.Assert.*;

public class LenientDateTimeFieldTest {
    
    private DateTimeField lenientField;
    private Chronology baseChronology;
    
    @Before
    public void setup() {
        baseChronology = LenientChronology.getInstance();
        lenientField = LenientDateTimeField.getInstance(baseChronology.dateTime(), baseChronology);
    }
    
    @Test
    public void testGetInstance_NullField_ReturnNull() {
        assertNull(LenientDateTimeField.getInstance(null, baseChronology));
    }
    
    @Test
    public void testGetInstance_LenientField_ReturnSameField() {
        DateTimeField field = lenientField;
        assertEquals(field, LenientDateTimeField.getInstance(field, baseChronology));
    }
    
    @Test
    public void testGetInstance_StrictField_ReturnLenientField() {
        DateTimeField strictField = baseChronology.monthOfYear();
        DateTimeField lenientField = LenientDateTimeField.getInstance(strictField, baseChronology);
        
        assertNotNull(lenientField);
        assertEquals(strictField, lenientField.getWrappedField());
    }
    
    @Test
    public void testGetInstance_NonLenientField_ReturnLenientField() {
        DateTimeField nonLenientField = baseChronology.millisOfSecond();
        DateTimeField lenientField = LenientDateTimeField.getInstance(nonLenientField, baseChronology);
        
        assertNotNull(lenientField);
        assertEquals(nonLenientField, lenientField.getWrappedField());
    }
    
    @Test
    public void testIsLenient_ReturnTrue() {
        assertTrue(lenientField.isLenient());
    }
    
    @Test
    public void testSet_OutOfBoundsValue_AddsDifferenceToCurrentValue() {
        long instant = baseChronology.dateTime().getMillis();
        int value = 100;
        long expectedInstant = baseChronology.dateTime().add(instant, value - baseChronology.millisOfSecond().get(instant));
        
        assertEquals(expectedInstant, lenientField.set(instant, value));
    }
    
    @Test
    public void testSet_InBoundsValue_ReturnsInstantWithNewValue() {
        long instant = baseChronology.dateTime().getMillis();
        int value = baseChronology.millisOfSecond().getMaximumValue();
        long expectedInstant = baseChronology.dateTime().set(instant, value);
        
        assertEquals(expectedInstant, lenientField.set(instant, value));
    }
    
    @Test
    public void testSet_NegativeValue_AddsDifferenceToCurrentValue() {
        long instant = baseChronology.dateTime().getMillis();
        int value = -100;
        long expectedInstant = baseChronology.dateTime().add(instant, value - baseChronology.millisOfSecond().get(instant));
        
        assertEquals(expectedInstant, lenientField.set(instant, value));
    }
}