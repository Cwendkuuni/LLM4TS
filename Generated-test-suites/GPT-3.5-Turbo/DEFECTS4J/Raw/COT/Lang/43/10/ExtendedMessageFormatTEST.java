
import org.junit.Assert;
import org.junit.Test;

public class ExtendedMessageFormatTest {

    @Test
    public void testApplyPattern() {
        ExtendedMessageFormat format = new ExtendedMessageFormat("{0} {1} {2}");
        String pattern = format.toPattern();
        Assert.assertEquals("{0} {1} {2}", pattern);
    }

    @Test
    public void testSetFormat() {
        ExtendedMessageFormat format = new ExtendedMessageFormat("{0}");
        try {
            format.setFormat(0, null);
            Assert.fail("Expected UnsupportedOperationException");
        } catch (UnsupportedOperationException e) {
            // expected
        }
    }

    @Test
    public void testSetFormatByArgumentIndex() {
        ExtendedMessageFormat format = new ExtendedMessageFormat("{0}");
        try {
            format.setFormatByArgumentIndex(0, null);
            Assert.fail("Expected UnsupportedOperationException");
        } catch (UnsupportedOperationException e) {
            // expected
        }
    }

    @Test
    public void testSetFormats() {
        ExtendedMessageFormat format = new ExtendedMessageFormat("{0}");
        try {
            format.setFormats(new Format[1]);
            Assert.fail("Expected UnsupportedOperationException");
        } catch (UnsupportedOperationException e) {
            // expected
        }
    }

    @Test
    public void testSetFormatsByArgumentIndex() {
        ExtendedMessageFormat format = new ExtendedMessageFormat("{0}");
        try {
            format.setFormatsByArgumentIndex(new Format[1]);
            Assert.fail("Expected UnsupportedOperationException");
        } catch (UnsupportedOperationException e) {
            // expected
        }
    }

}
