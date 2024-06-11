import static org.junit.Assert.*;
import org.junit.Test;
import org.apache.commons.lang.text.ExtendedMessageFormat;

public class ExtendedMessageFormatTest {
    
    @Test
    public void testConstructorWithPatternAndLocale() {
        ExtendedMessageFormat emf = new ExtendedMessageFormat("{0,number,#.##}", Locale.US);
        assertNotNull(emf);
    }
    
    @Test
    public void testConstructorWithPatternAndRegistry() {
        Map<String, FormatFactory> registry = new HashMap<>();
        registry.put("customFormat", new CustomFormatFactory());
        
        ExtendedMessageFormat emf = new ExtendedMessageFormat("{0,customFormat}", registry);
        assertNotNull(emf);
    }
    
    @Test
    public void testToPattern() {
        ExtendedMessageFormat emf = new ExtendedMessageFormat("{0,number,#.##}");
        assertEquals("{0,number,#.##}", emf.toPattern());
    }
    
    @Test
    public void testApplyPattern() {
        ExtendedMessageFormat emf = new ExtendedMessageFormat("{0,number,#.##}");
        emf.applyPattern("{0,date,yyyy-MM-dd}");
        assertEquals("{0,date,yyyy-MM-dd}", emf.toPattern());
    }
    
    @Test(expected = IllegalArgumentException.class)
    public void testApplyPatternInvalidPattern() {
        ExtendedMessageFormat emf = new ExtendedMessageFormat("{0,number,#.##}");
        emf.applyPattern("{0invalidpattern}");
    }
    
    @Test
    public void testSetFormatUnsupportedOperation() {
        ExtendedMessageFormat emf = new ExtendedMessageFormat("{0,number,#.##}");
        try {
            emf.setFormat(0, new DecimalFormat("#.##"));
            fail("Should throw UnsupportedOperationException");
        } catch (UnsupportedOperationException e) {
            assertTrue(true);
        }
    }
    
    @Test
    public void testSetFormatByArgumentIndexUnsupportedOperation() {
        ExtendedMessageFormat emf = new ExtendedMessageFormat("{0,number,#.##}");
        try {
            emf.setFormatByArgumentIndex(0, new DecimalFormat("#.##"));
            fail("Should throw UnsupportedOperationException");
        } catch (UnsupportedOperationException e) {
            assertTrue(true);
        }
    }
    
    @Test
    public void testConsecutivePatterns() {
        ExtendedMessageFormat emf = new ExtendedMessageFormat("{0,number,#.##} {1,date,yyyy-MM-dd}");
        assertNotNull(emf);
    }
}