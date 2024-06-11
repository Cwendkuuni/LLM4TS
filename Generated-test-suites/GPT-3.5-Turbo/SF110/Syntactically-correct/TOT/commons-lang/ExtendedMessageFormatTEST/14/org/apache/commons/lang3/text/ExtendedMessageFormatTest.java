import org.junit.Test;
import static org.junit.Assert.*;

public class ExtendedMessageFormatTest {

    @Test
    public void testConstructor_withPatternAndLocale() {
        // Typical use case
        ExtendedMessageFormat format = new ExtendedMessageFormat("Today is {0,date,EEEE}", Locale.US);
        assertEquals("Today is Monday", format.format(new Object[] { new Date(120, 6, 13) }));

        // Edge case
        format = new ExtendedMessageFormat("", Locale.US);
        assertEquals("", format.format(new Object[] {}));

        // Error scenario
        try {
            format = new ExtendedMessageFormat(null, Locale.US);
            fail("An exception should have been thrown");
        } catch (NullPointerException e) {
            // expected
        }
    }

    @Test
    public void testConstructor_withPatternAndRegistry() {
        // Typical use case
        Map<String, FormatFactory> registry = new HashMap<>();
        registry.put("custom", new CustomFormatFactory());
        ExtendedMessageFormat format = new ExtendedMessageFormat("{0,custom}", registry);
        assertEquals("Custom Format: Test", format.format(new Object[] { "Test" }));

        // Edge case
        format = new ExtendedMessageFormat("", registry);
        assertEquals("", format.format(new Object[] {}));

        // Error scenario
        try {
            format = new ExtendedMessageFormat(null, registry);
            fail("An exception should have been thrown");
        } catch (NullPointerException e) {
            // expected
        }
    }

    @Test
    public void testToPattern() {
        // Typical use case
        ExtendedMessageFormat format = new ExtendedMessageFormat("{0,number,#.####}");
        assertEquals("{0,number,#.####}", format.toPattern());

        // Edge case
        format = new ExtendedMessageFormat("");
        assertEquals("", format.toPattern());
    }

    // Additional test methods for other public methods in ExtendedMessageFormat class

    public static class CustomFormatFactory implements FormatFactory {
        @Override
        public Format getFormat(String name, String arguments, Locale locale) {
            if ("custom".equals(name)) {
                return new CustomFormat();
            }
            return null;
        }
    }

    public static class CustomFormat extends Format {
        @Override
        public StringBuffer format(Object obj, StringBuffer toAppendTo, FieldPosition pos) {
            if (obj instanceof String) {
                toAppendTo.append("Custom Format: ").append(obj);
            }
            return toAppendTo;
        }

        @Override
        public Object parseObject(String source, ParsePosition pos) {
            return null;
        }
    }

}