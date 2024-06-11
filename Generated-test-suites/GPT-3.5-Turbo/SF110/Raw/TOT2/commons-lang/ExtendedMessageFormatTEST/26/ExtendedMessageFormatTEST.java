import org.junit.Test;
import static org.junit.Assert.*;

public class ExtendedMessageFormatTest {
    
    @Test
    public void testExtendedMessageFormatConstructorWithPatternAndLocale() {
        ExtendedMessageFormat emf = new ExtendedMessageFormat("Test {0} message", Locale.US);
        assertNotNull(emf);
    }
    
    @Test
    public void testToPattern() {
        ExtendedMessageFormat emf = new ExtendedMessageFormat("Test {0} message");
        assertEquals("Test {0} message", emf.toPattern());
    }
    
    @Test
    public void testApplyPatternWithRegistry() {
        Map<String, FormatFactory> registry = new HashMap<>();
        registry.put("test", new TestFormatFactory());
        ExtendedMessageFormat emf = new ExtendedMessageFormat("Test {0} message", registry);
        assertEquals("Test {0} message", emf.toPattern());
    }
    
    @Test
    public void testEquals() {
        ExtendedMessageFormat emf1 = new ExtendedMessageFormat("Test {0} message");
        ExtendedMessageFormat emf2 = new ExtendedMessageFormat("Test {0} message");
        assertTrue(emf1.equals(emf2));
    }
    
    @Test
    public void testHashCode() {
        ExtendedMessageFormat emf = new ExtendedMessageFormat("Test {0} message");
        int hashCode = emf.hashCode();
        assertNotNull(hashCode);
    }
    
    @Test
    public void testGetFormat() {
        ExtendedMessageFormat emf = new ExtendedMessageFormat("Test {0} message");
        assertNull(emf.getFormat("invalid"));
    }
    
    // Add more test cases for other methods as needed
    
}

class TestFormatFactory implements FormatFactory {
  
    @Override
    public Format getFormat(String name, String args, Locale locale) {
        return null;
    }
    
}