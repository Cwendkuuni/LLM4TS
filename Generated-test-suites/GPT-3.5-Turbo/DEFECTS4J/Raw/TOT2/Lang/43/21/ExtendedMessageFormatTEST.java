import static org.junit.Assert.*;
import org.junit.Test;

public class ExtendedMessageFormatTest {

    @Test
    public void testApplyPattern() {
        // Typical use case
        ExtendedMessageFormat emf = new ExtendedMessageFormat("Hello {0}");
        assertEquals("Hello {0}", emf.toPattern());
        
        // Edge case with custom format
        emf.applyPattern("Hello {0,number,#.##}");
        assertEquals("Hello {0,number,#.##}", emf.toPattern());
        
        // Error scenario with invalid pattern
        try {
            emf.applyPattern("Invalid pattern");
            fail("Expected IllegalArgumentException");
        } catch (IllegalArgumentException e) {
            // Expected
        }
    }

    @Test
    public void testGetFormat() {
        // Typical use case
        ExtendedMessageFormat emf = new ExtendedMessageFormat("Hello {0}");
        assertNotNull(emf.getFormat("number,#"));

        // Edge case with unknown format
        assertNull(emf.getFormat("unknown"));

        // Edge case with empty description
        assertNull(emf.getFormat(""));

        // Error scenario with null description
        assertNull(emf.getFormat(null));
    }

    @Test
    public void testInsertFormats() {
        // Typical use case
        ExtendedMessageFormat emf = new ExtendedMessageFormat("Hello {0}");
        assertNull(emf.insertFormats("Hello {0}", null));

        // Edge case with custom patterns
        ArrayList<String> customPatterns = new ArrayList<>();
        customPatterns.add("number,#");
        assertEquals("Hello {0,number,#}", emf.insertFormats("Hello {0}", customPatterns));

        // Error scenario with empty pattern
        assertEquals("", emf.insertFormats("", null));
    }
}