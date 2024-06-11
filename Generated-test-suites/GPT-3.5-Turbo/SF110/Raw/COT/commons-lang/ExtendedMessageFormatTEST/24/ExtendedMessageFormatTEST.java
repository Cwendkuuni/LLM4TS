import org.junit.Test;
import java.util.Locale;
import java.util.Map;

public class ExtendedMessageFormatTest {

    @Test
    public void testExtendedMessageFormat() {
        ExtendedMessageFormat messageFormat = new ExtendedMessageFormat("Hello, {0}!");
        String result = messageFormat.format(new Object[]{"World"});
        assertEquals("Hello, World!", result);
    }

    @Test
    public void testExtendedMessageFormatWithLocale() {
        ExtendedMessageFormat messageFormat = new ExtendedMessageFormat("Hello, {0}!", Locale.US);
        String result = messageFormat.format(new Object[]{"World"});
        assertEquals("Hello, World!", result);
    }

    @Test
    public void testExtendedMessageFormatWithRegistry() {
        Map<String, FormatFactory> registry = new HashMap<>();
        registry.put("custom", new CustomFormatFactory());
        ExtendedMessageFormat messageFormat = new ExtendedMessageFormat("Hello, {0,custom}!", registry);
        String result = messageFormat.format(new Object[]{"World"});
        assertEquals("Hello, CustomFormatted: World!", result);
    }

    @Test
    public void testExtendedMessageFormatWithLocaleAndRegistry() {
        Map<String, FormatFactory> registry = new HashMap<>();
        registry.put("custom", new CustomFormatFactory());
        ExtendedMessageFormat messageFormat = new ExtendedMessageFormat("Hello, {0,custom}!", Locale.US, registry);
        String result = messageFormat.format(new Object[]{"World"});
        assertEquals("Hello, CustomFormatted: World!", result);
    }

    @Test
    public void testToPattern() {
        ExtendedMessageFormat messageFormat = new ExtendedMessageFormat("Hello, {0}!");
        String pattern = messageFormat.toPattern();
        assertEquals("Hello, {0}!", pattern);
    }

    @Test
    public void testApplyPattern() {
        ExtendedMessageFormat messageFormat = new ExtendedMessageFormat("Hello, {0}!");
        messageFormat.applyPattern("Goodbye, {0}!");
        String pattern = messageFormat.toPattern();
        assertEquals("Goodbye, {0}!", pattern);
    }

    @Test(expected = UnsupportedOperationException.class)
    public void testSetFormat() {
        ExtendedMessageFormat messageFormat = new ExtendedMessageFormat("Hello, {0}!");
        messageFormat.setFormat(0, new DecimalFormat());
    }

    @Test(expected = UnsupportedOperationException.class)
    public void testSetFormatByArgumentIndex() {
        ExtendedMessageFormat messageFormat = new ExtendedMessageFormat("Hello, {0}!");
        messageFormat.setFormatByArgumentIndex(0, new DecimalFormat());
    }

    @Test(expected = UnsupportedOperationException.class)
    public void testSetFormats() {
        ExtendedMessageFormat messageFormat = new ExtendedMessageFormat("Hello, {0}!");
        messageFormat.setFormats(new Format[]{new DecimalFormat()});
    }

    @Test(expected = UnsupportedOperationException.class)
    public void testSetFormatsByArgumentIndex() {
        ExtendedMessageFormat messageFormat = new ExtendedMessageFormat("Hello, {0}!");
        messageFormat.setFormatsByArgumentIndex(new Format[]{new DecimalFormat()});
    }

    @Test
    public void testEquals() {
        ExtendedMessageFormat messageFormat1 = new ExtendedMessageFormat("Hello, {0}!");
        ExtendedMessageFormat messageFormat2 = new ExtendedMessageFormat("Hello, {0}!");
        assertEquals(messageFormat1, messageFormat2);
    }

    @Test
    public void testHashCode() {
        ExtendedMessageFormat messageFormat1 = new ExtendedMessageFormat("Hello, {0}!");
        ExtendedMessageFormat messageFormat2 = new ExtendedMessageFormat("Hello, {0}!");
        assertEquals(messageFormat1.hashCode(), messageFormat2.hashCode());
    }

    private class CustomFormatFactory implements FormatFactory {
        @Override
        public Format getFormat(String name, String args, Locale locale) {
            return new DecimalFormat("CustomFormatted: " + args);
        }
    }
}