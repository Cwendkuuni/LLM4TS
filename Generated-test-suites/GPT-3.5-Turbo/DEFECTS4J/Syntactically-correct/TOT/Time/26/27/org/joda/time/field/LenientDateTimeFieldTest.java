import static org.junit.Assert.*;
import org.junit.Before;
import org.junit.Test;
import org.joda.time.chrono.GregorianChronology;
import org.joda.time.DateTimeField;
import org.joda.time.DateTimeUtils;
import org.joda.time.DateTimeZone;
import org.joda.time.field.LenientDateTimeField;

public class LenientDateTimeFieldTest {
    private static final DateTimeZone UTC = DateTimeZone.UTC;
    private static final DateTimeZone LONDON = DateTimeZone.forID("Europe/London");
    private static final DateTimeZone NEWYORK = DateTimeZone.forID("America/New_York");
    
    private static final Chronology CHRONOLOGY = GregorianChronology.getInstance(LONDON);
    
    private static LenientDateTimeField field;
    
    @Before
    public void setUp() {
        field = (LenientDateTimeField)LenientDateTimeField.getInstance(CHRONOLOGY.millisOfSecond(), CHRONOLOGY);
    }
    
    @Test
    public void testGet() {
        // Typical use case
        assertEquals(0, field.get(0L));
        
        // Edge case
        assertEquals(999, field.get(999L));
        
        // Error scenario
        assertEquals(-1, field.get(-1L));
    }
    
    @Test
    public void testSet() {
        // Typical use case
        assertEquals(50L, field.set(0L, 50));
        
        // Edge case
        assertEquals(999L, field.set(0L, 999));
        
        // Error scenario
        assertEquals(-1L, field.set(-1L, 50));
    }
    
    @Test
    public void testGetWrappedField() {
        // Typical use case
        DateTimeField wrappedField = field.getWrappedField();
        assertNotNull(wrappedField);
        assertTrue(wrappedField.isLenient());
        
        // Edge case
        DateTimeField edgeField = field.getWrappedField();
        assertNotNull(edgeField);
        
        // Error scenario
        DateTimeField errorField = field.getWrappedField();
        assertNotNull(errorField);
        
    }
}