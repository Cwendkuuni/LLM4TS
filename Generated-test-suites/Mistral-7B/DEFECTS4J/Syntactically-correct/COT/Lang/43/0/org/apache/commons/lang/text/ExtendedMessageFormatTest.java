import org.apache.commons.lang.text.ExtendedMessageFormat;
import org.apache.commons.lang.text.FormatFactory;
import org.apache.commons.lang.text.MessageFormat;
import org.junit.Test;

import java.text.ParseException;
import java.util.HashMap;
import java.util.Locale;
import java.util.Map;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertThrows;
import static org.junit.Assert.assertTrue;

public class ExtendedMessageFormatTest {

    

    @Test
    public void testEmptyConstructor() {
        ExtendedMessageFormat format = new ExtendedMessageFormat();
        String pattern = format.toPattern();
        assertEquals("", pattern);
    }

    @Test
    public void testLocaleConstructor() {
        ExtendedMessageFormat format = new ExtendedMessageFormat("{0}", Locale.US);
        String pattern = format.toPattern();
        assertEquals("{0}", pattern);
    }

    @Test
    public void testMapConstructor() {
        FormatFactory formatFactory = new FormatFactory() {
            @Override
            public Format getFormat(String name, String arg, Locale locale) {
                return new MessageFormat(name, locale);
            }
        };

        Map<String, FormatFactory> registry = new HashMap<>();
        registry.put("testFormat", formatFactory);

        ExtendedMessageFormat format = new ExtendedMessageFormat("{testFormat(0)}{1}", registry);
        String pattern = format.toPattern();
        assertEquals("{testFormat(0)}{1}", pattern);
    }

    @Test
    public void testToPatternMethod() {
        ExtendedMessageFormat format = new ExtendedMessageFormat("{0}", Locale.US);
        String pattern = format.toPattern();
        assertEquals("{0}", pattern);
    }

    @Test
    public void testApplyPatternMethodWithEmptyPattern() {
        ExtendedMessageFormat format = new ExtendedMessageFormat("");
        assertThrows(IllegalArgumentException.class, () -> format.applyPattern(""));
    }

    @Test
    public void testApplyPatternMethodWithInvalidPattern() {
        ExtendedMessageFormat format = new ExtendedMessageFormat("");
        assertThrows(IllegalArgumentException.class, () -> format.applyPattern("{0}"));
    }

    @Test
    public void testSetFormatMethod() {
        // UnsupportedOperationException
        ExtendedMessageFormat format = new ExtendedMessageFormat("");
        assertThrows(UnsupportedOperationException.class, () -> format.setFormat(0, new MessageFormat("")));
    }

    @Test
    public void testSetFormatByArgumentIndexMethod() {
        // UnsupportedOperationException
        ExtendedMessageFormat format = new ExtendedMessageFormat("");
        assertThrows(UnsupportedOperationException.class, () -> format.setFormatByArgumentIndex(0, new MessageFormat("")));
    }

    @Test
    public void testSetFormatsMethod() {
        // UnsupportedOperationException
        ExtendedMessageFormat format = new ExtendedMessageFormat("");
        Format[] formats = new Format[1];
        formats[0] = new MessageFormat("{0}");
        assertThrows(UnsupportedOperationException.class, () -> format.setFormats(formats));
    }

    @Test
    public void testSetFormatsByArgumentIndexMethod() {
        // UnsupportedOperationException
        ExtendedMessageFormat format = new ExtendedMessageFormat("");
        Format[] formats = new Format[1];
        formats[0] = new MessageFormat("{0}");
        assertThrows(UnsupportedOperationException.class, () -> format.setFormatsByArgumentIndex(formats));
    }

    @Test
    public void testGetFormatMethod() {
        ExtendedMessageFormat format = new ExtendedMessageFormat("{testFormat(0)}");
        format.applyPattern("{testFormat(0)}");
        Format testFormat = format.getFormat("testFormat");
        assertEquals(MessageFormat.class, testFormat.getClass());
    }

    @Test
    public void testGetFormatMethodWithNullFormatFactory() {
        ExtendedMessageFormat format = new ExtendedMessageFormat("{testFormat(0)}");
        format.applyPattern("{testFormat(0)}");
        Format testFormat = format.getFormat("testFormat");
        assertNull(testFormat);
    }

    @Test
    public void testFormatMethod() throws ParseException {
        FormatFactory formatFactory = new FormatFactory() {
            @Override
            public Format getFormat(String name, String arg, Locale locale) {
                return new MessageFormat(name, locale);
            }
        };

        Map<String, FormatFactory> registry = new HashMap<>();
        registry.put("testFormat", formatFactory);

        ExtendedMessageFormat format = new ExtendedMessageFormat("{testFormat(0)}{1}", registry);
        format.applyPattern("{testFormat(0)}{1}");

        String formatMessage = format.format(new Object[]{"Hello", "World"});
        assertEquals("HelloWorld", formatMessage);
    }

    @Test
    public void testFormatMethodWithNullObject() {
        ExtendedMessageFormat format = new ExtendedMessageFormat("{0}");
        format.applyPattern("{0}");

        String formatMessage = format.format(null);
        assertNull(formatMessage);
    }

    @Test
    public void testFormatMethodWithEmptyObjectArray() {
        ExtendedMessageFormat format = new ExtendedMessageFormat("{0} {1}");
        format.applyPattern("{0} {1}");

        String formatMessage = format.format(new Object[0]);
        assertEquals("", formatMessage);
    }

    @Test
    public void testFormatMethodWithNullFormat() {
        ExtendedMessageFormat format = new ExtendedMessageFormat("{testFormat(0)}");
        format.applyPattern("{testFormat(0)}");

        assertThrows(NullPointerException.class, () -> format.format(new Object[0]));
    }

    @Test
    public void testFormatMethodWithNullFormatFactory() {
        ExtendedMessageFormat format = new ExtendedMessageFormat("{testFormat(0)}");
        format.applyPattern("{testFormat(0)}");

        Map<String, FormatFactory> registry = new HashMap<>();
        registry.put("testFormat", null);

        assertThrows(NullPointerException.class, () -> format.format(new Object[0], registry));
    }

    @Test
    public void testFormatMethodWithInvalidFormatIndex() {
        ExtendedMessageFormat format = new ExtendedMessageFormat("{testFormat(0)}");
        format.applyPattern("{testFormat(0)}");

        Map<String, FormatFactory> registry = new HashMap<>();
        registry.put("testFormat", new FormatFactory() {
            @Override
            public Format getFormat(String name, String arg, Locale locale) {
                return null;
            }
        });

        assertThrows(IndexOutOfBoundsException.class, () -> format.format(new Object[0], registry, 1));
    }

    @Test
    public void testFormatMethodWithNullLocale() {
        ExtendedMessageFormat format = new ExtendedMessageFormat("{testFormat(0)}");
        format.applyPattern("{testFormat(0)}");

        Map<String, FormatFactory> registry = new HashMap<>();
        registry.put("testFormat", new FormatFactory() {
            @Override
            public Format getFormat(String name, String arg, Locale locale) {
                return new MessageFormat(name, locale);
            }
        });

        String formatMessage = format.format(new Object[0], registry, null);
        assertNull(formatMessage);
    }

    @Test
    public void testGetFormatsMethod() {
        ExtendedMessageFormat format = new ExtendedMessageFormat("{testFormat(0)}");
        format.applyPattern("{testFormat(0)}");

        Format[] formats = format.getFormats();
        assertTrue(formats.length > 0);
        assertEquals(MessageFormat.class, formats[0].getClass());
    }

    @Test
    public void testGetFormatsMethodWithNullFormatFactory() {
        ExtendedMessageFormat format = new ExtendedMessageFormat("{testFormat(0)}");
        format.applyPattern("{testFormat(0)}");

        assertNull(format.getFormats());
    }

    
}