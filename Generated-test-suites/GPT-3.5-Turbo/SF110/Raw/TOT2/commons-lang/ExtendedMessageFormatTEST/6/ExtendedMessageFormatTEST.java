import static org.junit.Assert.*;
import org.junit.Test;
import java.util.*;

public class ExtendedMessageFormatTest {

    @Test
    public void testConstructorWithDefaultLocale() {
        ExtendedMessageFormat emf = new ExtendedMessageFormat("Test");
        assertEquals(Locale.getDefault(), emf.getLocale());
    }

    @Test
    public void testConstructorWithSpecificLocale() {
        Locale locale = Locale.US;
        ExtendedMessageFormat emf = new ExtendedMessageFormat("Test", locale);
        assertEquals(locale, emf.getLocale());
    }

    @Test
    public void testConstructorWithRegistry() {
        Map<String, FormatFactory> registry = new HashMap<>();
        registry.put("test", new FormatFactory());
        ExtendedMessageFormat emf = new ExtendedMessageFormat("Test", registry);
        assertEquals(registry, emf.getRegistry());
    }

    // Test edge cases for applyPattern method
  
    // Test edge cases for setFormat method
  
    // Test edge cases for setFormatByArgumentIndex method
  
    // Test edge cases for setFormats method
  
    // Test edge cases for setFormatsByArgumentIndex method
  
    // Test typical use case for equals method
  
    // Test edge cases for equals method
  
    // Test typical use case for hashCode method
  
    // Test edge cases for hashCode method
}