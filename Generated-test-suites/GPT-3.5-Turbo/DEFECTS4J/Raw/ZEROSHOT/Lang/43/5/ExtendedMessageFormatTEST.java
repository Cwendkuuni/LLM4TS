import org.junit.Test;
import static org.junit.Assert.*;

public class ExtendedMessageFormatTest {

    @Test
    public void testToPattern() {
        ExtendedMessageFormat format = new ExtendedMessageFormat("{0,number,#.##}");
        assertEquals("{0,number,#.##}", format.toPattern());
    }

    @Test
    public void testApplyPattern() {
        ExtendedMessageFormat format = new ExtendedMessageFormat("{0,number,#.##}");
        format.applyPattern("{1,number,#.##}");
        assertEquals("{1,number,#.##}", format.toPattern());
    }

    @Test
    public void testSetFormat() {
        ExtendedMessageFormat format = new ExtendedMessageFormat("{0,number,#.##}");
        try {
            format.setFormat(0, null);
            fail("Expected UnsupportedOperationException");
        } catch (UnsupportedOperationException e) {
            // expected
        }
    }

    @Test
    public void testSetFormatByArgumentIndex() {
        ExtendedMessageFormat format = new ExtendedMessageFormat("{0,number,#.##}");
        try {
            format.setFormatByArgumentIndex(0, null);
            fail("Expected UnsupportedOperationException");
        } catch (UnsupportedOperationException e) {
            // expected
        }
    }

    @Test
    public void testSetFormats() {
        ExtendedMessageFormat format = new ExtendedMessageFormat("{0,number,#.##}");
        try {
            format.setFormats(null);
            fail("Expected UnsupportedOperationException");
        } catch (UnsupportedOperationException e) {
            // expected
        }
    }

    @Test
    public void testSetFormatsByArgumentIndex() {
        ExtendedMessageFormat format = new ExtendedMessageFormat("{0,number,#.##}");
        try {
            format.setFormatsByArgumentIndex(null);
            fail("Expected UnsupportedOperationException");
        } catch (UnsupportedOperationException e) {
            // expected
        }
    }
}