package org.apache.commons.lang.text;

import static org.junit.Assert.assertArrayEquals;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertSame;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

import java.text.Format;
import java.text.ParsePosition;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Collections;
import java.util.HashMap;
import java.util.Locale;
import java.util.Map;

import org.apache.commons.lang.text.ExtendedMessageFormat;
import org.apache.commons.lang.text.FormatFactory;

/**
 * JUnit test cases for ExtendedMessageFormat class.
 */
public class ExtendedMessageFormatTest {

    /**
     * Test ExtendedMessageFormat with no custom format factories.
     */
    @Test
    public void testEmpty() {
        String pattern = "Hello {0}, your age is {1}.";
        ExtendedMessageFormat fmt = new ExtendedMessageFormat(pattern);
        Object[] args = {"John", 25};
        Object result = fmt.format(args);
        assertEquals("Hello John, your age is 25.", result.toString());
    }

    /**
     * Test ExtendedMessageFormat with custom format factories.
     */
    @Test
    public void testCustomFormats() {
        String pattern = "Hello {0}, your date of birth is {1}.";
        Map<String, FormatFactory> registry = new HashMap<>();

        registry.put("date", new FormatFactory() {
            @Override
            public Format getFormat(String name, String style, Locale locale) {
                if ("date".equals(name)) {
                    if ("date".equals(style)) {
                        SimpleDateFormat sdf = new SimpleDateFormat("dd MMM yyyy", Locale.ENGLISH);
                        return sdf;
                    }
                }
                throw new IllegalArgumentException("Invalid format name or style.");
            }
        });

        ExtendedMessageFormat fmt = new ExtendedMessageFormat(pattern, null, registry);
        Object[] args = {"John", Calendar.getInstance()};
        Object result = fmt.format(args);
        assertEquals("Hello John, your date of birth is 04 Aug 2023.", result.toString());

        // Test with null argument
        args[1] = null;
        result = fmt.format(args);
        assertNull(result);
    }

    /**
     * Test ExtendedMessageFormat with invalid format names.
     */
    @Test(expected = IllegalArgumentException.class)
    public void testInvalidFormatName() {
        String pattern = "Hello {0}, your age is {1}.";
        Map<String, FormatFactory> registry = new HashMap<>();

        registry.put("date", new FormatFactory() {
            @Override
            public Format getFormat(String name, String style, Locale locale) {
                if ("date".equals(name)) {
                    SimpleDateFormat sdf = new SimpleDateFormat("dd MMM yyyy", Locale.ENGLISH);
                    return sdf;
                }
                throw new IllegalArgumentException("Invalid format name or style.");
            }
        });

        ExtendedMessageFormat fmt = new ExtendedMessageFormat(pattern, null, registry);
        Object[] args = {"John", "invalid"};
        fmt.format(args);
    }

    /**
     * Test ExtendedMessageFormat with invalid format styles.
     */
    @Test(expected = IllegalArgumentException.class)
    public void testInvalidFormatStyle() {
        String pattern = "Hello {0}, your age is {1}.";
        Map<String, FormatFactory> registry = new HashMap<>();

        registry.put("date", new FormatFactory() {
            @Override
            public Format getFormat(String name, String style, Locale locale) {
                if ("date".equals(name)) {
                    SimpleDateFormat sdf = new SimpleDateFormat("dd MMM yyyy", Locale.ENGLISH);
                    return sdf;
                }
                throw new IllegalArgumentException("Invalid format name or style.");
            }
        });

        ExtendedMessageFormat fmt = new ExtendedMessageFormat(pattern, null, registry);
        Object[] args = {"John", "date invalid"};
        fmt.format(args);
    }

    /**
     * Test ExtendedMessageFormat with duplicate format names.
     */
    @Test(expected = IllegalArgumentException.class)
    public void testDuplicateFormatNames() {
        String pattern = "Hello {0}, your age is {1}, your date of birth is {1}.";
        Map<String, FormatFactory> registry = new HashMap<>();

        registry.put("date", new FormatFactory() {
            @Override
            public Format getFormat(String name, String style, Locale locale) {
                if ("date".equals(name)) {
                    SimpleDateFormat sdf = new SimpleDateFormat("dd MMM yyyy", Locale.ENGLISH);
                    return sdf;
                }
                throw new IllegalArgumentException("Invalid format name or style.");
            }
        });

        registry.put("date", new FormatFactory() {
            @Override
            public Format getFormat(String name, String style, Locale locale) {
                throw new UnsupportedOperationException();
            }
        });

        ExtendedMessageFormat fmt = new ExtendedMessageFormat(pattern, null, registry);
        Object[] args = {"John", Calendar.getInstance()};
        fmt.format(args);
    }

    /**
     * Test ExtendedMessageFormat with missing format factories.
     */
    @Test(expected = IllegalArgumentException.class)
    public void testMissingFormatFactories() {
        String pattern = "Hello {0}, your date of birth is {1}.";
        Map<String, FormatFactory> registry = new HashMap<>();

        ExtendedMessageFormat fmt = new ExtendedMessageFormat(pattern, null, registry);
        Object[] args = {"John", Calendar.getInstance()};
        fmt.format(args);

        registry.put("date", new FormatFactory() {
            @Override
            public Format getFormat(String name, String style, Locale locale) {
                throw new UnsupportedOperationException();
            }
        });

        Object result = fmt.format(args);
        assertEquals("Hello John, your date of birth is ", result.toString());
    }

    /**
     * Test ExtendedMessageFormat with getFormat method.
     */
    @Test
    public void testGetFormat() {
        String pattern = "Hello {0}, your age is {1,date}.";
        Map<String, FormatFactory> registry = new HashMap<>();

        registry.put("date", new FormatFactory() {
            @Override
            public Format getFormat(String name, String style, Locale locale) {
                if ("date".equals(name)) {
                    SimpleDateFormat sdf = new SimpleDateFormat("dd MMM yyyy", Locale.ENGLISH);
                    return sdf;
                }
                throw new IllegalArgumentException("Invalid format name or style.");
            }
        });

        ExtendedMessageFormat fmt = new ExtendedMessageFormat(pattern, null, registry);
        Object[] args = {"John", Calendar.getInstance()};
        Format format = fmt.getFormat("date", "date");
        assertSame(SimpleDateFormat.class, format.getClass());
    }

    /**
     * Test ExtendedMessageFormat with setFormatByArgumentIndex method.
     */
    @Test
    public void testSetFormatByArgumentIndex() {
        String pattern = "Hello {0,date}, your age is {1}.";
        Map<String, FormatFactory> registry = new HashMap<>();

        registry.put("date", new FormatFactory() {
            @Override
            public Format getFormat(String name, String style, Locale locale) {
                if ("date".equals(name)) {
                    SimpleDateFormat sdf = new SimpleDateFormat("dd MMM yyyy", Locale.ENGLISH);
                    return sdf;
                }
                throw new IllegalArgumentException("Invalid format name or style.");
            }
        });

        ExtendedMessageFormat fmt = new ExtendedMessageFormat(pattern, null, registry);
        Object[] args = {"John", Calendar.getInstance()};

        // Set format for first argument
        Format format = new SimpleDateFormat("dd MMM yyyy", Locale.ENGLISH);
        fmt.setFormatByArgumentIndex(0, format);
        Object result = fmt.format(args);
        assertEquals("Hello 04 Aug 2023, your age is 25.", result.toString());

        // Make sure original format is replaced
        Format oldFormat = fmt.getFormat(0, null);
        assertNull(oldFormat);
    }

    /**
     * Test ExtendedMessageFormat with setFormat method.
     */
    @Test(expected = UnsupportedOperationException.class)
    public void testSetFormat() {
        String pattern = "Hello {0}, your age is {1}.";
        Map<String, FormatFactory> registry = new HashMap<>();

        ExtendedMessageFormat fmt = new ExtendedMessageFormat(pattern, null, registry);
        fmt.setFormat(0, new SimpleDateFormat("dd MMM yyyy", Locale.ENGLISH));
    }

    /**
     * Test ExtendedMessageFormat with setFormatsByArgumentIndex method.
     */
    @Test(expected = UnsupportedOperationException.class)
    public void testSetFormatsByArgumentIndex() {
        String pattern = "Hello {0}, your age is {1}.";
        Map<String, FormatFactory> registry = new HashMap<>();

        ExtendedMessageFormat fmt = new ExtendedMessageFormat(pattern, null, registry);
        Format[] formats = new Format[2];
        formats[0] = new SimpleDateFormat("dd MMM yyyy", Locale.ENGLISH);
        formats[1] = new SimpleDateFormat("dd MMM yyyy", Locale.ENGLISH);

        fmt.setFormatsByArgumentIndex(new ArrayList<>(Collections.asList(formats)));
    }

    /**
     * Test ExtendedMessageFormat with setFormats method.
     */
    @Test(expected = UnsupportedOperationException.class)
    public void testSetFormats() {
        String pattern = "Hello {0}, your age is {1}.";
        Map<String, FormatFactory> registry = new HashMap<>();

        ExtendedMessageFormat fmt = new ExtendedMessageFormat(pattern, null, registry);
        Format[] formats = new Format[2];
        formats[0] = new SimpleDateFormat("dd MMM yyyy", Locale.ENGLISH);
        formats[1] = new SimpleDateFormat("dd MMM yyyy", Locale.ENGLISH);

        fmt.setFormats(formats);
    }
}