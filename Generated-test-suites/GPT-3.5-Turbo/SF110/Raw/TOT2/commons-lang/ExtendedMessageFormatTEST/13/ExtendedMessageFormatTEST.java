import org.junit.Test;
import static org.junit.Assert.*;

public class ExtendedMessageFormatTest {
    
    @Test
    public void testConstructorWithPatternAndLocale() {
        ExtendedMessageFormat emf = new ExtendedMessageFormat("Test", Locale.ENGLISH);
        assertNotNull(emf);
        assertEquals("Test", emf.toPattern());
    }
    
    @Test
    public void testConstructorWithPatternAndRegistry() {
        HashMap<String, FormatFactory> registry = new HashMap<>();
        registry.put("test", new DateFormatFactory());
        
        ExtendedMessageFormat emf = new ExtendedMessageFormat("Test", registry);
        assertNotNull(emf);
        assertEquals("Test", emf.toPattern());
    }
    
    // Add more test cases for other public methods here
    
    @Test
    public void testEquals() {
        ExtendedMessageFormat emf1 = new ExtendedMessageFormat("Test", Locale.ENGLISH);
        ExtendedMessageFormat emf2 = new ExtendedMessageFormat("Test", Locale.ENGLISH);
        
        assertTrue(emf1.equals(emf2));
    }
    
    @Test
    public void testHashCode() {
        ExtendedMessageFormat emf = new ExtendedMessageFormat("Test", Locale.ENGLISH);
        
        assertEquals(emf.hashCode(), emf.hashCode());
    }
    
    // Add test cases for edge cases and exception handling
    
}