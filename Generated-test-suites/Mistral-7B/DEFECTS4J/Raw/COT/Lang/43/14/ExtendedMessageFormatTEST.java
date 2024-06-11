import org.apache.commons.lang.text.ExtendedMessageFormat;
import org.apache.commons.lang.text.FormatFactory;
import org.junit.Test;
import static org.junit.Assert.*;

public class ExtendedMessageFormatTest {
    // Test case for constructor with no arguments
    @Test
    public void testExtendedMessageFormatConstructorWithNoArguments() {
        String pattern = "Hello {0}!";
        ExtendedMessageFormat format = new ExtendedMessageFormat(pattern);
        assertEquals("", format.toPattern());
    }

    // Test case for constructor with a locale argument
    @Test
    public void testExtendedMessageFormatConstructorWithLocaleArgument() {
        String pattern = "Hello {0}, welcome to {1}!";
        ExtendedMessageFormat format = new ExtendedMessageFormat(pattern, Locale.US);
        assertEquals("Hello {0}, welcome to {1}!", format.toPattern());
    }

    // Test case for constructor with a map argument
    @Test
    public void testExtendedMessageFormatConstructorWithMapArgument() {
        Map<String, FormatFactory> registry = new HashMap<>();
        registry.put("customFormat", new CustomFormatFactory());
        String pattern = "Hello {0}! Your custom format is {customFormat}.";
        ExtendedMessageFormat format = new ExtendedMessageFormat(pattern, null, registry);
        assertEquals("Hello {0}! Your custom format is {}.", format.toPattern());
    }

    // Test case for toPattern() method
    @Test
    public void testToPattern() {
        String pattern = "Hello {0}!";
        ExtendedMessageFormat format = new ExtendedMessageFormat(pattern);
        assertEquals("", format.toPattern());

        pattern = "Hello {0}, welcome to {1}!";
        format = new ExtendedMessageFormat(pattern, Locale.US);
        assertEquals("Hello {0}, welcome to {1}!", format.toPattern());

        Map<String, FormatFactory> registry = new HashMap<>();
        registry.put("customFormat", new CustomFormatFactory());
        pattern = "Hello {0}! Your custom format is {customFormat}.";
        format = new ExtendedMessageFormat(pattern, null, registry);
        assertEquals("Hello {0}! Your custom format is {}.", format.toPattern());
    }

    // Test case for applyPattern() method
    @Test(expected = IllegalArgumentException.class)
    public void testApplyPatternWithBadPattern() {
        String pattern = "Hello {0}; // this should throw an exception";
        ExtendedMessageFormat format = new ExtendedMessageFormat(pattern, null);
    }

    // Test case for getFormat() method
    @Test
    public void testGetFormat() {
        Map<String, FormatFactory> registry = new HashMap<>();
        registry.put("customFormat", new CustomFormatFactory());
        ExtendedMessageFormat format = new ExtendedMessageFormat("{customFormat}", null, registry);
        Format getFormat = format.getFormat(0);
        assertNotNull(getFormat);
        assertEquals("customFormat", getFormat.getClass().getName());
    }

    // Test case for containsElements() method
    @Test
    public void testContainsElements() {
        Collection<Format> formats = new ArrayList<>();
        formats.add(new Format() {});
        assertTrue(ExtendedMessageFormat.containsElements(formats));

        formats.clear();
        assertFalse(ExtendedMessageFormat.containsElements(formats));
    }

    // Test case for setFormat() method (UnsupportedOperationException)
    @Test(expected = UnsupportedOperationException.class)
    public void testSetFormat() {
        ExtendedMessageFormat format = new ExtendedMessageFormat("", null, null);
        format.setFormat(0, new Format() {});
    }

    // Test case for setFormatByArgumentIndex() method (UnsupportedOperationException)
    @Test(expected = UnsupportedOperationException.class)
    public void testSetFormatByArgumentIndex() {
        ExtendedMessageFormat format = new ExtendedMessageFormat("", null, null);
        format.setFormatByArgumentIndex(0, new Format() {});
    }

    // Test case for setFormats() method (UnsupportedOperationException)
    @Test(expected = UnsupportedOperationException.class)
    public void testSetFormats() {
        ExtendedMessageFormat format = new ExtendedMessageFormat("", null, null);
        Format[] formats = new Format[1];
        formats[0] = new Format() {};
        format.setFormats(formats);
    }

    // Test case for setFormatsByArgumentIndex() method (UnsupportedOperationException)
    @Test(expected = UnsupportedOperationException.class)
    public void testSetFormatsByArgumentIndex() {
        ExtendedMessageFormat format = new ExtendedMessageFormat("", null, null);
        Format[] formats = new Format[1];
        formats[0] = new Format() {};
        format.setFormatsByArgumentIndex(new Format[1], 0);
    }

    // Custom FormatFactory and Format for testing
    static class CustomFormatFactory implements FormatFactory {
        @Override
        public Format getFormat(String name, String style, Locale locale) {
            return new CustomFormat();
        }
    }

    static class CustomFormat implements Format {
        @Override
        public Object format(Object obj, Locale locale, String format, ParsePosition parsePosition) {
            return obj.toString();
        }

        @Override
        public String toString() {
            return "customFormat";
        }
    }
}