import org.junit.*;
import static org.junit.Assert.*;

public class ExtendedMessageFormatTest {

    @Test
    public void testToPattern() {
        ExtendedMessageFormat emf = new ExtendedMessageFormat("Hello {0}, today is {1,date,full}");
        String pattern = emf.toPattern();
        assertEquals("Hello {0}, today is {1,date,full}", pattern);
    }

    @Test
    public void testApplyPattern() {
        ExtendedMessageFormat emf = new ExtendedMessageFormat("");
        emf.applyPattern("Hello {0}, today is {1,date,full}");
        String pattern = emf.toPattern();
        assertEquals("Hello {0}, today is {1,date,full}", pattern);
    }

    @Test
    public void testSetFormat() {
        ExtendedMessageFormat emf = new ExtendedMessageFormat("Hello {0}, today is {1,date,full}");
        try {
            emf.setFormat(0, null);
            fail("Expected UnsupportedOperationException to be thrown");
        } catch (UnsupportedOperationException e) {
            // Expected exception
        }
    }

    @Test
    public void testSetFormatByArgumentIndex() {
        ExtendedMessageFormat emf = new ExtendedMessageFormat("Hello {0}, today is {1,date,full}");
        try {
            emf.setFormatByArgumentIndex(0, null);
            fail("Expected UnsupportedOperationException to be thrown");
        } catch (UnsupportedOperationException e) {
            // Expected exception
        }
    }

    @Test
    public void testSetFormats() {
        ExtendedMessageFormat emf = new ExtendedMessageFormat("Hello {0}, today is {1,date,full}");
        try {
            emf.setFormats(null);
            fail("Expected UnsupportedOperationException to be thrown");
        } catch (UnsupportedOperationException e) {
            // Expected exception
        }
    }

    @Test
    public void testSetFormatsByArgumentIndex() {
        ExtendedMessageFormat emf = new ExtendedMessageFormat("Hello {0}, today is {1,date,full}");
        try {
            emf.setFormatsByArgumentIndex(null);
            fail("Expected UnsupportedOperationException to be thrown");
        } catch (UnsupportedOperationException e) {
            // Expected exception
        }
    }

}