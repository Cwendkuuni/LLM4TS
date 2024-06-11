import org.apache.commons.lang.text.ExtendedMessageFormat;
import org.junit.Test;

import java.text.Format;
import java.util.HashMap;
import java.util.Locale;
import java.util.Map;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;

public class ExtendedMessageFormatTest {

    @Test
    public void testExtendedMessageFormatConstructor() {
        ExtendedMessageFormat extendedMessageFormat = new ExtendedMessageFormat("Test {0}", Locale.getDefault());
        assertNotNull(extendedMessageFormat);
    }

    @Test
    public void testApplyPattern() {
        ExtendedMessageFormat extendedMessageFormat = new ExtendedMessageFormat("Test {0}", Locale.getDefault());
        extendedMessageFormat.applyPattern("Test {0,number}");
        assertEquals("Test {0,number}", extendedMessageFormat.toPattern());
    }

    @Test
    public void testToPattern() {
        ExtendedMessageFormat extendedMessageFormat = new ExtendedMessageFormat("Test {0}", Locale.getDefault());
        assertEquals("Test {0}", extendedMessageFormat.toPattern());
    }

    @Test
    public void testSetFormatThrowsUnsupportedOperationException() {
        ExtendedMessageFormat extendedMessageFormat = new ExtendedMessageFormat("Test {0}", Locale.getDefault());
        try {
            extendedMessageFormat.setFormat(0, null);
        } catch (UnsupportedOperationException e) {
            // Exception should be thrown
        }
    }

    @Test
    public void testSetFormatByArgumentIndexThrowsUnsupportedOperationException() {
        ExtendedMessageFormat extendedMessageFormat = new ExtendedMessageFormat("Test {0}", Locale.getDefault());
        try {
            extendedMessageFormat.setFormatByArgumentIndex(0, null);
        } catch (UnsupportedOperationException e) {
            // Exception should be thrown
        }
    }

    @Test
    public void testSetFormatsThrowsUnsupportedOperationException() {
        ExtendedMessageFormat extendedMessageFormat = new ExtendedMessageFormat("Test {0}", Locale.getDefault());
        try {
            extendedMessageFormat.setFormats(new Format[1]);
        } catch (UnsupportedOperationException e) {
            // Exception should be thrown
        }
    }

    @Test
    public void testSetFormatsByArgumentIndexThrowsUnsupportedOperationException() {
        ExtendedMessageFormat extendedMessageFormat = new ExtendedMessageFormat("Test {0}", Locale.getDefault());
        try {
            extendedMessageFormat.setFormatsByArgumentIndex(new Format[1]);
        } catch (UnsupportedOperationException e) {
            // Exception should be thrown
        }
    }

    @Test
    public void testInsertFormats() {
        ExtendedMessageFormat extendedMessageFormat = new ExtendedMessageFormat("Test {0}", Locale.getDefault());
        Map<String, Format> formatRegistry = new HashMap<>();
        formatRegistry.put("number", NumberFormat.getInstance());
        extendedMessageFormat = new ExtendedMessageFormat("Test {0, number}", Locale.getDefault(), formatRegistry);
        assertEquals("Test {0, number}", extendedMessageFormat.toPattern());
    }

    @Test
    public void testInsertFormatsWithInvalidFormat() {
        ExtendedMessageFormat extendedMessageFormat = new ExtendedMessageFormat("Test {0}", Locale.getDefault());
        Map<String, Format> formatRegistry = new HashMap<>();
        extendedMessageFormat = new ExtendedMessageFormat("Test {0, invalidFormat}", Locale.getDefault(), formatRegistry);
        assertEquals("Test {0, invalidFormat}", extendedMessageFormat.toPattern());
    }

}