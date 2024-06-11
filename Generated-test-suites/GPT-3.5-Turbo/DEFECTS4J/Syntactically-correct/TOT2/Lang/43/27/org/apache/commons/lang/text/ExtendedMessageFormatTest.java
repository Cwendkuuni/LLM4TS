import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;

import org.junit.Test;

public class ExtendedMessageFormatTest {

    private ExtendedMessageFormat extendedMessageFormat;

    @Test
    public void testConstructor() {
        extendedMessageFormat = new ExtendedMessageFormat("Test {0}", Locale.getDefault());
        assertNotNull(extendedMessageFormat);
    }

    @Test
    public void testNullPatternConstructor() {
        try {
            extendedMessageFormat = new ExtendedMessageFormat(null, Locale.getDefault());
        } catch (IllegalArgumentException e) {
            assertNull(extendedMessageFormat);
        }
    }

    @Test
    public void testApplyPattern() {
        extendedMessageFormat = new ExtendedMessageFormat("Test {0}", Locale.getDefault());
        extendedMessageFormat.applyPattern("Test {0,number,#}");
        assertEquals("Test {0,number,#}", extendedMessageFormat.toPattern());
    }

    @Test(expected = UnsupportedOperationException.class)
    public void testSetFormatException() {
        extendedMessageFormat = new ExtendedMessageFormat("Test {0}", Locale.getDefault());
        extendedMessageFormat.setFormat(0, null);
    }

    @Test
    public void testSetFormatByArgumentIndexException() {
        extendedMessageFormat = new ExtendedMessageFormat("Test {0}", Locale.getDefault());
        try {
            extendedMessageFormat.setFormatByArgumentIndex(0, null);
        } catch (UnsupportedOperationException e) {
            assertNotNull(e);
        }
    }

}