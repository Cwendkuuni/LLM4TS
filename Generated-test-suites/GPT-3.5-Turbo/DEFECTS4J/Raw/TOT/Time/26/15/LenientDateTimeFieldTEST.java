import org.joda.time.*;
import org.joda.time.field.*;
import org.junit.Before;
import org.junit.Test;
import static org.junit.Assert.*;

public class LenientDateTimeFieldTest {
    
    private static final Chronology BASE = ISOChronology.getInstanceUTC();
    private LenientDateTimeField lenientField;
    private DateTimeField strictField;
    private Chronology lenientChronology;
    
    @Before
    public void setUp() {
        strictField = new StrictDateTimeField(BASE.minuteOfHour());
        lenientField = new LenientDateTimeField(strictField, BASE);
        lenientChronology = LenientChronology.getInstance(BASE);
    }
    
    @Test
    public void testSet_ValidValue() {
        DateTime dt = new DateTime(2000, 1, 1, 0, 0, 0, 0);
        long instant = dt.getMillis();
        
        instant = lenientField.set(instant, 30);
        assertEquals(30, lenientField.get(instant));
    }
    
    @Test
    public void testSet_OutOfBoundsValue() {
        DateTime dt = new DateTime(2000, 1, 1, 0, 0, 0, 0);
        long instant = dt.getMillis();
        
        instant = lenientField.set(instant, 70);
        assertEquals(10, lenientField.get(instant));
    }
    
    @Test
    public void testSet_LowerBound() {
        DateTime dt = new DateTime(2000, 1, 1, 0, 0, 0, 0);
        long instant = dt.getMillis();
        
        instant = lenientField.set(instant, 0);
        assertEquals(0, lenientField.get(instant));
    }
    
    @Test
    public void testSet_UpperBound() {
        DateTime dt = new DateTime(2000, 1, 1, 0, 0, 0, 0);
        long instant = dt.getMillis();
        
        instant = lenientField.set(instant, 59);
        assertEquals(59, lenientField.get(instant));
    }
    
    @Test
    public void testSet_NegativeValue() {
        DateTime dt = new DateTime(2000, 1, 1, 0, 0, 0, 0);
        long instant = dt.getMillis();
        
        instant = lenientField.set(instant, -30);
        assertEquals(30, lenientField.get(instant));
    }
    
    @Test
    public void testSet_ZeroDifference() {
        DateTime dt = new DateTime(2000, 1, 1, 0, 0, 0, 0);
        long instant = dt.getMillis();
        
        instant = lenientField.set(instant, 0);
        assertEquals(0, lenientField.get(instant));
    }
    
    @Test
    public void testSet_PositiveDifference() {
        DateTime dt = new DateTime(2000, 1, 1, 0, 0, 0, 0);
        long instant = dt.getMillis();
        
        instant = lenientField.set(instant, 10);
        assertEquals(10, lenientField.get(instant));
    }
    
    @Test
    public void testGetInstance_StrictField() {
        DateTimeField field = BASE.dayOfMonth();
        DateTimeField lenientField = LenientDateTimeField.getInstance(field, BASE);
        assertSame(field, lenientField);
    }
    
    @Test
    public void testGetInstance_NullField() {
        DateTimeField lenientField = LenientDateTimeField.getInstance(null, BASE);
        assertNull(lenientField);
    }
    
    @Test
    public void testGetInstance_LenientField() {
        DateTimeField field = new LenientDateTimeField(BASE.dayOfMonth(), BASE);
        DateTimeField lenientField = LenientDateTimeField.getInstance(field, BASE);
        assertSame(field, lenientField);
    }
    
    @Test
    public void testGetInstance_StrictFieldWrapped() {
        DateTimeField strictField = new StrictDateTimeField(BASE.dayOfMonth());
        DateTimeField lenientField = LenientDateTimeField.getInstance(strictField, BASE);
        assertSame(strictField.getWrappedField(), lenientField);
    }
    
    @Test
    public void testIsLenient() {
        assertTrue(lenientField.isLenient());
    }
}