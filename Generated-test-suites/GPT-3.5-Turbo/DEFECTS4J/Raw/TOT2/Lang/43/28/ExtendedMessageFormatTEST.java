import static org.junit.Assert.*;
import org.junit.Test;
import org.apache.commons.lang.text.ExtendedMessageFormat;

public class ExtendedMessageFormatTest {

    @Test
    public void testConstructor_DefaultLocale() {
        // Typical Use Case
        ExtendedMessageFormat emf = new ExtendedMessageFormat("Hello {0}");
        assertNotNull(emf);
        
        // Edge Cases
        // Test with empty pattern
        emf = new ExtendedMessageFormat("");
        assertNotNull(emf);
        
        // Error Scenarios
        // Test with null pattern
        try {
            emf = new ExtendedMessageFormat(null);
            fail("Expected IllegalArgumentException");
        } catch (IllegalArgumentException e) {
            // Expected
        }
    }

    @Test
    public void testConstructor_CustomLocale() {
        // Typical Use Case
        ExtendedMessageFormat emf = new ExtendedMessageFormat("Hello {0}", Locale.US);
        assertNotNull(emf);

        // Edge Cases
        // Test with non-default locale
        emf = new ExtendedMessageFormat("Hello {0}", Locale.GERMANY);
        assertNotNull(emf);
    }

    @Test
    public void testApplyPattern() {
        // Typical Use Case
        ExtendedMessageFormat emf = new ExtendedMessageFormat("Hello {0}");
        emf.applyPattern("Hi {0}");

        // Edge Cases
        // Test with empty pattern
        emf.applyPattern("");

        // Error Scenarios
        // Test with invalid pattern
        try {
            emf.applyPattern("Hello {"); // missing closing brace
            fail("Expected IllegalArgumentException");
        } catch (IllegalArgumentException e) {
            // Expected
        }
    }

    @Test
    public void testToPattern() {
        // Typical Use Case
        ExtendedMessageFormat emf = new ExtendedMessageFormat("Hello {0}");
        assertEquals("Hello {0}", emf.toPattern());

        // Edge Cases
        // Test with empty pattern
        emf = new ExtendedMessageFormat("");
        assertEquals("", emf.toPattern());
    }

    @Test
    public void testSetFormat_UnsupportedOperationException() {
        ExtendedMessageFormat emf = new ExtendedMessageFormat("Hello {0}");
        try {
            emf.setFormat(0, null);
            fail("Expected UnsupportedOperationException");
        } catch (UnsupportedOperationException e) {
            // Expected
        }
    }

    @Test
    public void testSetFormatByArgumentIndex_UnsupportedOperationException() {
        ExtendedMessageFormat emf = new ExtendedMessageFormat("Hello {0}");
        try {
            emf.setFormatByArgumentIndex(0, null);
            fail("Expected UnsupportedOperationException");
        } catch (UnsupportedOperationException e) {
            // Expected
        }
    }

    @Test
    public void testSetFormats_UnsupportedOperationException() {
        ExtendedMessageFormat emf = new ExtendedMessageFormat("Hello {0}");
        try {
            emf.setFormats(new Format[] { null });
            fail("Expected UnsupportedOperationException");
        } catch (UnsupportedOperationException e) {
            // Expected
        }
    }

    @Test
    public void testSetFormatsByArgumentIndex_UnsupportedOperationException() {
        ExtendedMessageFormat emf = new ExtendedMessageFormat("Hello {0}");
        try {
            emf.setFormatsByArgumentIndex(new Format[] { null });
            fail("Expected UnsupportedOperationException");
        } catch (UnsupportedOperationException e) {
            // Expected
        }
    }
}