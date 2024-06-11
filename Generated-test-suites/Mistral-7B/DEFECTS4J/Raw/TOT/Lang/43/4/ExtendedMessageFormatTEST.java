package org.apache.commons.lang.text;

import org.apache.commons.lang.Validate;
import org.junit.Test;
import static org.junit.Assert.*;

import java.text.MessageFormat;
import java.text.ParseException;
import java.util.HashMap;
import java.util.Map;

/**
 * Unit tests for {@link ExtendedMessageFormat}.
 */
public class ExtendedMessageFormatTest {

    /**
     * Test creating an ExtendedMessageFormat with no registry.
     */
    @Test
    public void testNoRegistry() {
        String pattern = "Hello {0, choice, 0#Hello|1#Hi} {1}!";
        ExtendedMessageFormat format = new ExtendedMessageFormat(pattern);
        assertNotNull(format);
        assertEquals(pattern, format.toPattern());
    }

    /**
     * Test creating an ExtendedMessageFormat with a registry of one format.
     */
    @Test
    public void testSimpleRegistry() {
        String pattern = "Hello {0, choice, 0#Hello|1#Hi} {1, format:myFormat}!";
        Map<String, FormatFactory> registry = new HashMap<>();
        registry.put("myFormat", new FormatFactory() {
            @Override
            public Format getFormat(String name, String arg, Locale locale) {
                return new Format() {
                    @Override
                    public String format(Object argObj) {
                        return "FORMATTED [" + argObj + "]";
                    }
                };
            }
        });
        ExtendedMessageFormat format = new ExtendedMessageFormat(pattern, registry);
        assertNotNull(format);
        assertEquals(pattern, format.toPattern());

        Object[] args = new Object[]{"John", "Doe"};
        String formatted = format.format(args);
        assertEquals("Hello [John] FORMATTED [Doe]!", formatted);

        ParsePosition pos = new ParsePosition(0);
        try {
            format.parseObject(formatted, args);
            fail();
        } catch (ParseException expected) {
            // expected
        }
    }

    /**
     * Test creating an ExtendedMessageFormat with a registry of multiple formats.
     */
    @Test
    public void testMultipleRegistry() {
        String pattern = "Date: {0, format:dateFormat}|{1, format:timeFormat}";
        Map<String, FormatFactory> registry = new HashMap<>();
        registry.put("dateFormat", new FormatFactory() {
            @Override
            public Format getFormat(String name, String arg, Locale locale) {
                return new SimpleDateFormat("dd-MM-yyyy");
            }
        });
        registry.put("timeFormat", new FormatFactory() {
            @Override
            public Format getFormat(String name, String arg, Locale locale) {
                return new SimpleDateFormat("HH:mm:ss");
            }
        });
        ExtendedMessageFormat format = new ExtendedMessageFormat(pattern, registry);
        assertNotNull(format);
        assertEquals(pattern, format.toPattern());

        Object[] args = new Object[]{new java.util.Date(), new java.util.Date()};
        String formatted = format.format(args);
        assertEquals("Date: 12-12-2022|14:30:45", formatted);

        ParsePosition pos = new ParsePosition(0);
        Object[] parsedArgs = format.parseObjects(formatted, null);
        assertNotNull(parsedArgs);
        assertEquals(2, parsedArgs.length);
        assertTrue(parsedArgs[0] instanceof java.util.Date);
        assertTrue(parsedArgs[1] instanceof java.util.Date);
    }

    /**
     * Test an invalid format with no registry.
     */
    @Test(expected = IllegalArgumentException.class)
    public void testInvalidFormatNoRegistry() {
        String pattern = "Hello {0, choice, 0#Hello|1#Hi} {1, format:myFormat}!";
        new ExtendedMessageFormat(pattern);
    }

    /**
     * Test an invalid format with a non-existent format in the registry.
     */
    @Test(expected = IllegalArgumentException.class)
    public void testInvalidFormatRegistry() {
        String pattern = "Hello {0, choice, 0#Hello|1#Hi} {1, format:nonExistent}!";
        Map<String, FormatFactory> registry = new HashMap<>();
        new ExtendedMessageFormat(pattern, registry);
    }

    /**
     * Test an invalid format with an invalid format name in the registry.
     */
    @Test(expected = IllegalArgumentException.class)
    public void testInvalidFormatNameRegistry() {
        String pattern = "Hello {0, choice, 0#Hello|1#Hi} {1, format:myFormat}!";
        Map<String, FormatFactory> registry = new HashMap<>();
        registry.put("myFormat", (FormatFactory) null);
        new ExtendedMessageFormat(pattern, registry);
    }

    /**
     * Test an invalid format with an invalid format style in the registry.
     */
    @Test(expected = IllegalArgumentException.class)
    public void testInvalidFormatStyleRegistry() {
        String pattern = "Hello {0, choice, 0#Hello|1#Hi} {1, format:myFormat, style:invalidStyle}!";
        Map<String, FormatFactory> registry = new HashMap<>();
        registry.put("myFormat", new FormatFactory() {
            @Override
            public Format getFormat(String name, String arg, Locale locale) {
                return new Format() {
                    @Override
                    public String format(Object argObj) {
                        return "FORMATTED [" + argObj + "]";
                    }
                };
            }
        });
        new ExtendedMessageFormat(pattern, registry);
    }

    /**
     * Test setting an unsupported method.
     */
    @Test(expected = UnsupportedOperationException.class)
    public void testUnsupportedMethod() {
        String pattern = "Hello {0}!";
        ExtendedMessageFormat format = new ExtendedMessageFormat(pattern);
        format.setFormat(0, new Format() {});
    }

    /**
     * Test setting a format by argument index.
     */
    @Test(expected = UnsupportedOperationException.class)
    public void testSetFormatByArgumentIndex() {
        String pattern = "Hello {0}!";
        ExtendedMessageFormat format = new ExtendedMessageFormat(pattern);
        format.setFormatsByArgumentIndex(new Format[]{new Format() {}});
    }

    /**
     * Test setting multiple formats.
     */
    @Test(expected = UnsupportedOperationException.class)
    public void testSetFormats() {
        String pattern = "Hello {0}!";
        ExtendedMessageFormat format = new ExtendedMessageFormat(pattern);
        Format[] formats = new Format[]{new Format() {}};
        format.setFormats(formats);
    }
}