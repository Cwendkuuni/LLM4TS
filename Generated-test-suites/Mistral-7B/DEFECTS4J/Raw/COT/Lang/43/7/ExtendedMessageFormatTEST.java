package org.apache.commons.lang.text; // package import

import static org.junit.Assert.assertEquals; // import static assertions
import static org.junit.Assert.assertNull; // import static assertions
import static org.junit.Assert.assertThrows; // import static assertions

import java.text.ParseException; // import ParseException
import java.text.ParsePosition; // import ParsePosition
import java.util.ArrayList; // import ArrayList
import java.util.HashMap; // import HashMap
import java.util.Locale; // import Locale
import java.util.Map; // import Map

import org.apache.commons.lang.FormatFactory; // import FormatFactory
import org.apache.commons.lang.Validate; // import Validate
import org.apache.commons.lang.text.ExtendedMessageFormat; // import class under test
import org.apache.commons.lang.text.MessageFormat; // import base class

import org.junit.Test; // import JUnit Test annotation

/**
 * Test class for ExtendedMessageFormat.
 */
public class ExtendedMessageFormatTest {
    // 

    /**
     * Test constructor with default locale and empty pattern.
     */
    @Test
    public void testEmptyPattern() {
        ExtendedMessageFormat format = new ExtendedMessageFormat("");
        assertEquals("", format.toPattern());
    }

    /**
     * Test constructor with default locale, non-empty pattern, and null registry.
     */
    @Test
    public void testConstructorWithPatternAndNullRegistry() {
        ExtendedMessageFormat format = new ExtendedMessageFormat("{}", Locale.getDefault());
        assertEquals("{}", format.toPattern());
    }

    /**
     * Test constructor with default locale, non-empty pattern, and empty registry.
     */
    @Test
    public void testConstructorWithPatternAndEmptyRegistry() {
        ExtendedMessageFormat format = new ExtendedMessageFormat("{}", Locale.getDefault(), new HashMap<>());
        assertEquals("{}", format.toPattern());
    }

    /**
     * Test constructor with default locale, non-empty pattern, and custom registry.
     */
    @Test
    public void testConstructorWithPatternAndCustomRegistry() {
        Map<String, FormatFactory> registry = new HashMap<>();
        registry.put("customFormat", new FormatFactory() {
            @Override
            public Format getFormat(String name, String argument, Locale locale) {
                return new MessageFormat("{0} {1}", locale);
            }
        });

        ExtendedMessageFormat format = new ExtendedMessageFormat("{customFormat, date, short} {}", Locale.getDefault(), registry);
        assertEquals("{customFormat, date, short} {}", format.toPattern());
        assertEquals("Mon Aug 23 2021 {}", format.getFormat());
    }

    /**
     * Test constructor with specified locale and non-empty pattern, and null registry.
     */
    @Test
    public void testConstructorWithPatternAndLocaleAndNullRegistry() {
        ExtendedMessageFormat format = new ExtendedMessageFormat("{}", new Locale("en", "US"), null);
        assertEquals("{}", format.toPattern());
    }

    /**
     * Test constructor with specified locale and non-empty pattern, and empty registry.
     */
    @Test
    public void testConstructorWithPatternAndLocaleAndEmptyRegistry() {
        ExtendedMessageFormat format = new ExtendedMessageFormat("{}", new Locale("en", "US"), new HashMap<>());
        assertEquals("{}", format.toPattern());
    }

    /**
     * Test constructor with specified locale, non-empty pattern, and custom registry.
     */
    @Test
    public void testConstructorWithPatternAndLocaleAndCustomRegistry() {
        Map<String, FormatFactory> registry = new HashMap<>();
        registry.put("customFormat", new FormatFactory() {
            @Override
            public Format getFormat(String name, String argument, Locale locale) {
                return new MessageFormat("{0} {1}", locale);
            }
        });

        ExtendedMessageFormat format = new ExtendedMessageFormat("{customFormat, date, short} {}", new Locale("en", "US"), registry);
        assertEquals("{customFormat, date, short} {}", format.toPattern());
        assertEquals("Mon Aug 23 2021 {}", format.getFormat().format(new Object[]{null, "Test"}));
    }

    /**
     * Test method applyPattern.
     */
    @Test
    public void testApplyPattern() {
        Map<String, FormatFactory> registry = new HashMap<>();
        registry.put("customFormat", new FormatFactory() {
            @Override
            public Format getFormat(String name, String argument, Locale locale) {
                return new MessageFormat("{0} {1}", locale);
            }
        });

        ExtendedMessageFormat format = new ExtendedMessageFormat("{customFormat, date, short} {}", Locale.getDefault(), registry);
        String pattern = format.toPattern();

        assertEquals("{customFormat, date, short} {}", pattern);

        format.applyPattern("{customFormat, date, short} {customFormat, number, decimal}");
        pattern = format.toPattern();

        assertEquals("{customFormat, date, short} {customFormat, number, decimal}", pattern);
    }

    /**
     * Test method toPattern.
     */
    @Test
    public void testToPattern() {
        ExtendedMessageFormat format = new ExtendedMessageFormat("{}", Locale.getDefault());
        assertEquals("{}", format.toPattern());

        format = new ExtendedMessageFormat("{customFormat} {}", Locale.getDefault(), new HashMap<>());
        assertEquals("{customFormat} {}", format.toPattern());

        Map<String, FormatFactory> registry = new HashMap<>();
        registry.put("customFormat", new FormatFactory() {
            @Override
            public Format getFormat(String name, String argument, Locale locale) {
                return new MessageFormat("{0} {1}", locale);
            }
        });

        format = new ExtendedMessageFormat("{customFormat, date, short} {}", Locale.getDefault(), registry);
        assertEquals("{customFormat, date, short}", format.toPattern());

        format = new ExtendedMessageFormat("{customFormat, date, short} {customFormat, number, decimal}", Locale.getDefault(), registry);
        assertEquals("{customFormat, date, short} {customFormat, number, decimal}", format.toPattern());
    }

    /**
     * Test method format.
     */
    @Test
    public void testFormat() {
        Map<String, FormatFactory> registry = new HashMap<>();
        registry.put("customFormat", new FormatFactory() {
            @Override
            public Format getFormat(String name, String argument, Locale locale) {
                return new MessageFormat("{0} {1}", locale);
            }
        });

        ExtendedMessageFormat format = new ExtendedMessageFormat("{customFormat, date, short} {}", Locale.getDefault(), registry);
        String expected = "Mon Aug 23 2021 {}";
        Object[] args = new Object[]{"Test", "Test2"};

        String actual = format.format(args);
        assertEquals(expected, actual);

        expected = "Mon Aug 23 2021 Test Test2";
        actual = format.format(args, new Object[]{"Test", "Test2"});
        assertEquals(expected, actual);
    }

    /**
     * Test method format with null arguments.
     */
    @Test
    public void testFormatWithNullArguments() {
        Map<String, FormatFactory> registry = new HashMap<>();
        registry.put("customFormat", new FormatFactory() {
            @Override
            public Format getFormat(String name, String argument, Locale locale) {
                return new MessageFormat("{0} {1}", locale);
            }
        });

        ExtendedMessageFormat format = new ExtendedMessageFormat("{customFormat, date, short} {}", Locale.getDefault(), registry);

        String actual = format.format(new Object[]{null, null});
        assertNull(actual);

        actual = format.format(new Object[]{null}, new Object[]{null});
        assertNull(actual);

        try {
            actual = format.format(null);
            assertThrows(IllegalArgumentException.class, () -> format.format(null));
            assertNull(actual);
        } catch (IllegalArgumentException e) {
            // Pass
        }
    }

    /**
     * Test method format with invalid format name.
     */
    @Test
    public void testFormatWithInvalidFormatName() {
        Map<String, FormatFactory> registry = new HashMap<>();
        registry.put("customFormat", new FormatFactory() {
            @Override
            public Format getFormat(String name, String argument, Locale locale) {
                throw new UnsupportedOperationException();
            }
        });

        ExtendedMessageFormat format = new ExtendedMessageFormat("{customFormat, invalidFormat} {}", Locale.getDefault(), registry);

        String[] args = new String[]{"Test"};

        try {
            format.format(args);
            assertThrows(FormatException.class, () -> format.format(args));
        } catch (FormatException e) {
            // Pass
        }

        try {
            format.format(args, new String[]{"Test"});
            assertThrows(FormatException.class, () -> format.format(args, new String[]{"Test"}));
        } catch (FormatException e) {
            // Pass
        }
    }

    /**
     * Test method format with invalid format argument.
     */
    @Test
    public void testFormatWithInvalidFormatArgument() {
        Map<String, FormatFactory> registry = new HashMap<>();
        registry.put("customFormat", new FormatFactory() {
            @Override
            public Format getFormat(String name, String argument, Locale locale) {
                throw new UnsupportedOperationException();
            }
        });

        ExtendedMessageFormat format = new ExtendedMessageFormat("{customFormat, number, invalid} {}", Locale.getDefault(), registry);

        String[] args = new String[]{"Test"};
        Object[] obArgs = new Object[]{"Test"};

        try {
            format.format(args);
            assertThrows(FormatException.class, () -> format.format(args));
        } catch (FormatException e) {
            // Pass
        }

        try {
            format.format(args, obArgs);
            assertThrows(ParseException.class, () -> format.format(args, obArgs));
        } catch (ParseException e) {
            // Pass
        }

        try {
            format.format(obArgs);
            assertThrows(IllegalArgumentException.class, () -> format.format(obArgs));
        } catch (IllegalArgumentException e) {
            // Pass
        }

        try {
            format.format(new Object[]{null}, obArgs);
            assertThrows(NullPointerException.class, () -> format.format(new Object[]{null}, obArgs));
        } catch (NullPointerException e) {
            // Pass
        }
    }

    /**
     * Test method setFormat.
     */
    @Test(expected = UnsupportedOperationException.class)
    public void testSetFormat() {
        ExtendedMessageFormat format = new ExtendedMessageFormat("{}", Locale.getDefault());
        format.setFormat(0, new MessageFormat("Test"));
    }

    /**
     * Test method setFormatByArgumentIndex.
     */
    @Test(expected = UnsupportedOperationException.class)
    public void testSetFormatByArgumentIndex() {
        ExtendedMessageFormat format = new ExtendedMessageFormat("{}", Locale.getDefault());
        format.setFormatByArgumentIndex(0, new MessageFormat("Test"));
    }

    /**
     * Test method setFormats.
     */
    @Test(expected = UnsupportedOperationException.class)
    public void testSetFormats() {
        ExtendedMessageFormat format = new ExtendedMessageFormat("{}", Locale.getDefault());
        Format[] formats = new Format[1];
        formats[0] = new MessageFormat("Test");
        format.setFormats(formats);
    }

    /**
     * Test method setFormatsByArgumentIndex.
     */
    @Test(expected = UnsupportedOperationException.class)
    public void testSetFormatsByArgumentIndex() {
        ExtendedMessageFormat format = new ExtendedMessageFormat("{}", Locale.getDefault());
        Format[] formats = new Format[1];
        formats[0] = new MessageFormat("Test");
        format.setFormatsByArgumentIndex(0, formats);
    }

    // 
}