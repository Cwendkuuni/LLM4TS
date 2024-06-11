import static org.junit.Assert.*;
import org.junit.Before;
import org.junit.Test;

public class ExtendedMessageFormatTest {
    private ExtendedMessageFormat extendedMessageFormat;
    
    @Before
    public void setUp() {
        extendedMessageFormat = new ExtendedMessageFormat("");
    }
    
    @Test
    public void testToPattern() {
        String pattern = "Hello {0}, how are you {1}?";
        extendedMessageFormat.applyPattern(pattern);
        
        String result = extendedMessageFormat.toPattern();
        
        assertEquals(pattern, result);
    }
    
    @Test
    public void testApplyPatternWithoutRegistry() {
        String pattern = "Hello {0}, how are you {1}?";
        
        extendedMessageFormat.applyPattern(pattern);
        
        String result = extendedMessageFormat.toPattern();
        
        assertEquals(pattern, result);
    }
    
    @Test
    public void testApplyPatternWithRegistry() {
        String pattern = "Hello {0}, how are you {1}?";
        Map<String, FormatFactory> registry = new HashMap<>();
        registry.put("date", new SimpleDateFormat.FormatFactory());
        
        extendedMessageFormat = new ExtendedMessageFormat("", registry);
        extendedMessageFormat.applyPattern(pattern);
        
        String result = extendedMessageFormat.toPattern();
        
        assertEquals(pattern, result);
    }
    
    @Test
    public void testEquals() {
        String pattern = "Hello {0}, how are you {1}?";
        extendedMessageFormat.applyPattern(pattern);
        
        ExtendedMessageFormat other = new ExtendedMessageFormat(pattern);
        boolean result = extendedMessageFormat.equals(other);
        
        assertTrue(result);
    }
    
    @Test
    public void testNotEquals() {
        String pattern1 = "Hello {0}, how are you {1}?";
        String pattern2 = "Hi {0}, are you {1}?";
        
        extendedMessageFormat.applyPattern(pattern1);
        ExtendedMessageFormat other = new ExtendedMessageFormat(pattern2);
        boolean result = extendedMessageFormat.equals(other);
        
        assertFalse(result);
    }
    
    @Test
    public void testHashCode() {
        String pattern = "Hello {0}, how are you {1}?";
        extendedMessageFormat.applyPattern(pattern);
        
        int hashCode = extendedMessageFormat.hashCode();
        
        assertNotNull(hashCode);
    }
    
    @Test(expected = UnsupportedOperationException.class)
    public void testSetFormat() {
        extendedMessageFormat.setFormat(0, null);
    }
    
    @Test(expected = UnsupportedOperationException.class)
    public void testSetFormatByArgumentIndex() {
        extendedMessageFormat.setFormatByArgumentIndex(0, null);
    }
    
    @Test(expected = UnsupportedOperationException.class)
    public void testSetFormats() {
        extendedMessageFormat.setFormats(null);
    }
    
    @Test(expected = UnsupportedOperationException.class)
    public void testSetFormatsByArgumentIndex() {
        extendedMessageFormat.setFormatsByArgumentIndex(null);
    }
}