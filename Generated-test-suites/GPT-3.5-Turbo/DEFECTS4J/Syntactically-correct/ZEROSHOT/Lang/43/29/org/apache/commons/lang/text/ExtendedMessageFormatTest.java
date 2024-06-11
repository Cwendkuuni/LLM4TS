import org.junit.Test;
import static org.junit.Assert.*;

public class ExtendedMessageFormatTest {

    private ExtendedMessageFormat extendedMessageFormat;

    @Test
    public void testApplyPattern() {
        extendedMessageFormat = new ExtendedMessageFormat("");
        extendedMessageFormat.applyPattern("Hello {0}");
    }

    @Test
    public void testSetFormat() {
        extendedMessageFormat = new ExtendedMessageFormat("");
        try {
            extendedMessageFormat.setFormat(0, null);
            fail("UnsupportedOperationException not thrown.");
        } catch (UnsupportedOperationException e) {
            // expected
        }
    }

    @Test
    public void testSetFormatByArgumentIndex() {
        extendedMessageFormat = new ExtendedMessageFormat("");
        try {
            extendedMessageFormat.setFormatByArgumentIndex(0, null);
            fail("UnsupportedOperationException not thrown.");
        } catch (UnsupportedOperationException e) {
            // expected
        }
    }

    @Test
    public void testSetFormats() {
        extendedMessageFormat = new ExtendedMessageFormat("");
        try {
            extendedMessageFormat.setFormats(null);
            fail("UnsupportedOperationException not thrown.");
        } catch (UnsupportedOperationException e) {
            // expected
        }
    }

    @Test
    public void testSetFormatsByArgumentIndex() {
        extendedMessageFormat = new ExtendedMessageFormat("");
        try {
            extendedMessageFormat.setFormatsByArgumentIndex(null);
            fail("UnsupportedOperationException not thrown.");
        } catch (UnsupportedOperationException e) {
            // expected
        }
    }

    @Test
    public void testToPattern() {
        extendedMessageFormat = new ExtendedMessageFormat("Hello {0}");
        String pattern = extendedMessageFormat.toPattern();
        assertEquals("Hello {0}", pattern);
    }
}