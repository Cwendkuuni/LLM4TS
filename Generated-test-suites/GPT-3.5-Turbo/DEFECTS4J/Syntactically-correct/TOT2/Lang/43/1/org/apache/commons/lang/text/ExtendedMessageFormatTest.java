import org.junit.Test;
import static org.junit.Assert.*;
import org.apache.commons.lang.text.ExtendedMessageFormat;
import java.text.Format;
import java.util.HashMap;
import java.util.Map;
import java.util.Locale;

public class ExtendedMessageFormatTest {

    @Test
    public void testConstructorDefaultLocale() {
        ExtendedMessageFormat emf = new ExtendedMessageFormat("Test {0}", Locale.getDefault());
        assertNotNull(emf);
    }

    @Test
    public void testConstructorCustomLocale() {
        ExtendedMessageFormat emf = new ExtendedMessageFormat("Test {0}", Locale.US);
        assertNotNull(emf);
    }

    @Test
    public void testConstructorCustomRegistry() {
        Map<String, FormatFactory> registry = new HashMap<>();
        ExtendedMessageFormat emf = new ExtendedMessageFormat("Test {0}", registry);
        assertNotNull(emf);
    }

    @Test
    public void testConstructorCustomPattern() {
        Map<String, FormatFactory> registry = new HashMap<>();
        registry.put("customFormat", new CustomFormatFactory());
        ExtendedMessageFormat emf = new ExtendedMessageFormat("Test {0,customFormat}", registry);
        assertNotNull(emf);
    }

    @Test
    public void testToPattern() {
        ExtendedMessageFormat emf = new ExtendedMessageFormat("Test {0}");
        assertEquals("Test {0}", emf.toPattern());
    }

    @Test
    public void testApplyPattern() {
        ExtendedMessageFormat emf = new ExtendedMessageFormat("Test {0}");
        emf.applyPattern("New {0}");
        assertEquals("New {0}", emf.toPattern());
    }

    @Test(expected = UnsupportedOperationException.class)
    public void testSetFormat() {
        ExtendedMessageFormat emf = new ExtendedMessageFormat("Test {0}");
        emf.setFormat(0, new CustomFormat());
    }

    @Test(expected = UnsupportedOperationException.class)
    public void testSetFormatByArgumentIndex() {
        ExtendedMessageFormat emf = new ExtendedMessageFormat("Test {0}");
        emf.setFormatByArgumentIndex(0, new CustomFormat());
    }

    @Test(expected = UnsupportedOperationException.class)
    public void testSetFormats() {
        ExtendedMessageFormat emf = new ExtendedMessageFormat("Test {0}");
        emf.setFormats(new Format[]{new CustomFormat()});
    }

    @Test(expected = UnsupportedOperationException.class)
    public void testSetFormatsByArgumentIndex() {
        ExtendedMessageFormat emf = new ExtendedMessageFormat("Test {0}");
        emf.setFormatsByArgumentIndex(new Format[]{new CustomFormat()});
    }

    private static class CustomFormat implements Format {
        @Override
        public StringBuffer format(Object obj, StringBuffer toAppendTo, FieldPosition pos) {
            return toAppendTo.append("CustomFormat: ").append(obj.toString());
        }

        @Override
        public Object parseObject(String source, ParsePosition pos) {
            return null;
        }
    }

    private static class CustomFormatFactory implements FormatFactory {
        @Override
        public Format getFormat(String name, String arguments, Locale locale) {
            return new CustomFormat();
        }
    }
}