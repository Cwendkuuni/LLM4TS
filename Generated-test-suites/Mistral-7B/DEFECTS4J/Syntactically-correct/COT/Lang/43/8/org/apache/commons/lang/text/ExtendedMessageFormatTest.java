import org.apache.commons.lang.text.ExtendedMessageFormat;
import org.apache.commons.lang.text.FormatFactory;
import org.junit.Test;
import static org.junit.Assert.*;

public class ExtendedMessageFormatTest {

    @Test
    public void testDefaultConstructor() {
        ExtendedMessageFormat format = new ExtendedMessageFormat("");
        assertNull(format.toPattern());
    }

    @Test
    public void testConstructorWithPattern() {
        ExtendedMessageFormat format = new ExtendedMessageFormat("{0}", Locale.ENGLISH);
        String pattern = format.toPattern();
        assertEquals("{0}", pattern);
    }

    @Test
    public void testConstructorWithPatternAndRegistry() {
        Map<String, FormatFactory> registry = new HashMap<>();
        FormatFactory formatFactory = new FormatFactory() {
            @Override
            public Format getFormat(String name, String arguments, Locale locale) {
                return new MessageFormat("{0} {1}", locale);
            }
        };
        registry.put("MyFormat", formatFactory);
        ExtendedMessageFormat format = new ExtendedMessageFormat("{ MyFormat 0 }{1}", Locale.ENGLISH, registry);
        String pattern = format.toPattern();
        assertEquals("{ MyFormat 0 }{1}", pattern);
    }

    @Test
    public void testToPattern() {
        ExtendedMessageFormat format = new ExtendedMessageFormat("{0}", Locale.ENGLISH);
        String pattern = format.toPattern();
        assertEquals("{0}", pattern);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testApplyPatternWithInvalidPattern() {
        ExtendedMessageFormat format = new ExtendedMessageFormat("{0} {1} {3}", Locale.ENGLISH);
        format.applyPattern("{0} {1}");
    }

    @Test(expected = IllegalArgumentException.class)
    public void testApplyPatternWithMissingFormatName() {
        ExtendedMessageFormat format = new ExtendedMessageFormat("{0} {0}", Locale.ENGLISH);
        format.applyPattern("{0}");
    }

    @Test(expected = IllegalArgumentException.class)
    public void testApplyPatternWithMissingFormatStyle() {
        ExtendedMessageFormat format = new ExtendedMessageFormat("{0, number} {0}", Locale.ENGLISH);
        format.applyPattern("{0}");
    }

    @Test(expected = IllegalArgumentException.class)
    public void testApplyPatternWithMissingFormatFactory() {
        Map<String, FormatFactory> registry = new HashMap<>();
        ExtendedMessageFormat format = new ExtendedMessageFormat("{MyFormat 0}", Locale.ENGLISH, registry);
        format.applyPattern("{0}");
    }

    @Test
    public void testGetFormat() {
        Map<String, FormatFactory> registry = new HashMap<>();
        FormatFactory formatFactory = new FormatFactory() {
            @Override
            public Format getFormat(String name, String arguments, Locale locale) {
                return new MessageFormat("{0} {1}", locale);
            }
        };
        registry.put("MyFormat", formatFactory);
        ExtendedMessageFormat format = new ExtendedMessageFormat("{ MyFormat 0 }{1}", Locale.ENGLISH, registry);
        Format format1 = format.getFormat(0);
        assertEquals(MessageFormat.class, format1.getClass());
    }

    @Test
    public void testFormat() {
        ExtendedMessageFormat format = new ExtendedMessageFormat("{0, number} {1}", Locale.ENGLISH);
        Object[] args = new Object[]{"123", "abc"};
        String formatted = format.format(args);
        assertEquals("123 abc", formatted);
    }

    @Test
    public void testFormatWithRegistry() {
        Map<String, FormatFactory> registry = new HashMap<>();
        FormatFactory formatFactory = new FormatFactory() {
            @Override
            public Format getFormat(String name, String arguments, Locale locale) {
                return new MessageFormat("{0} {1}", locale);
            }
        };
        registry.put("MyFormat", formatFactory);
        ExtendedMessageFormat format = new ExtendedMessageFormat("{ MyFormat 0 }{1}", Locale.ENGLISH, registry);
        Object[] args = new Object[]{"123", "abc"};
        String formatted = format.format(args);
        assertEquals("123 abc", formatted);
    }
}