import org.junit.Test;
import static org.junit.Assert.*;

public class ExtendedMessageFormatTest {
    
    @Test
    public void testApplyPattern() {
        ExtendedMessageFormat emf = new ExtendedMessageFormat("Hello {0}");
        emf.applyPattern("Hello {0}, {1}");
        assertEquals("Hello {0}, {1}", emf.toPattern());
    }
    
    @Test
    public void testSetFormat() {
        try {
            ExtendedMessageFormat emf = new ExtendedMessageFormat("Hello {0}");
            emf.setFormat(0, new MessageFormat(""));
            fail("UnsupportedOperationException should be thrown");
        } catch (UnsupportedOperationException e) {
            // expected
        }
    }
    
    @Test
    public void testSetFormatByArgumentIndex() {
        try {
            ExtendedMessageFormat emf = new ExtendedMessageFormat("Hello {0}");
            emf.setFormatByArgumentIndex(0, new MessageFormat(""));
            fail("UnsupportedOperationException should be thrown");
        } catch (UnsupportedOperationException e) {
            // expected
        }
    }
    
    @Test
    public void testSetFormats() {
        try {
            ExtendedMessageFormat emf = new ExtendedMessageFormat("Hello {0}");
            emf.setFormats(new Format[] { new MessageFormat("") });
            fail("UnsupportedOperationException should be thrown");
        } catch (UnsupportedOperationException e) {
            // expected
        }
    }
    
    @Test
    public void testSetFormatsByArgumentIndex() {
        try {
            ExtendedMessageFormat emf = new ExtendedMessageFormat("Hello {0}");
            emf.setFormatsByArgumentIndex(new Format[] { new MessageFormat("") });
            fail("UnsupportedOperationException should be thrown");
        } catch (UnsupportedOperationException e) {
            // expected
        }
    }
    
}