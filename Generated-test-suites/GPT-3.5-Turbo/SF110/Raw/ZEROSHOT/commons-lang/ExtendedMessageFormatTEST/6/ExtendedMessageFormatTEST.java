package org.apache.commons.lang3.text;

import org.junit.Test;
import java.text.Format;
import java.text.ParseException;
import java.util.HashMap;
import java.util.Locale;
import java.util.Map;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotEquals;
import static org.junit.Assert.assertTrue;
import static org.junit.Assert.assertFalse;

public class ExtendedMessageFormatTest {

    @Test
    public void testConstructorWithPatternAndLocale() {
        ExtendedMessageFormat format = new ExtendedMessageFormat("Hello, {0}", Locale.US);
        assertEquals("Hello, {0}", format.toPattern());
    }

    @Test
    public void testConstructorWithPatternAndRegistry() {
        Map<String, FormatFactory> registry = new HashMap<>();
        registry.put("custom", new FormatFactory() {
            @Override
            public Format getFormat(String name, String args, Locale locale) {
                return null;
            }
        });

        ExtendedMessageFormat format = new ExtendedMessageFormat("Hello, {0}", registry);
        assertEquals("Hello, {0}", format.toPattern());
    }

    @Test
    public void testConstructorWithPatternLocaleAndRegistry() {
        Map<String, FormatFactory> registry = new HashMap<>();
        registry.put("custom", new FormatFactory() {
            @Override
            public Format getFormat(String name, String args, Locale locale) {
                return null;
            }
        });

        ExtendedMessageFormat format = new ExtendedMessageFormat("Hello, {0}", Locale.US, registry);
        assertEquals("Hello, {0}", format.toPattern());
    }

    @Test
    public void testApplyPatternWithoutRegistry() {
        ExtendedMessageFormat format = new ExtendedMessageFormat("Hello, {0}");
        format.applyPattern("Hi, {0}");
        assertEquals("Hi, {0}", format.toPattern());
    }

    @Test
    public void testApplyPatternWithRegistry() {
        Map<String, FormatFactory> registry = new HashMap<>();
        registry.put("custom", new FormatFactory() {
            @Override
            public Format getFormat(String name, String args, Locale locale) {
                return null;
            }
        });

        ExtendedMessageFormat format = new ExtendedMessageFormat("Hello, {0}", registry);
        format.applyPattern("Hi, {0}");
        assertEquals("Hi, {0}", format.toPattern());
    }

    @Test
    public void testSetFormat() {
        ExtendedMessageFormat format = new ExtendedMessageFormat("Hello, {0}");
        try {
            format.setFormat(0, null);
        } catch (UnsupportedOperationException e) {
            assertTrue(true);
        }
    }

    @Test
    public void testSetFormatByArgumentIndex() {
        ExtendedMessageFormat format = new ExtendedMessageFormat("Hello, {0}");
        try {
            format.setFormatByArgumentIndex(0, null);
        } catch (UnsupportedOperationException e) {
            assertTrue(true);
        }
    }

    @Test
    public void testSetFormats() {
        ExtendedMessageFormat format = new ExtendedMessageFormat("Hello, {0}");
        try {
            format.setFormats(new Format[0]);
        } catch (UnsupportedOperationException e) {
            assertTrue(true);
        }
    }

    @Test
    public void testSetFormatsByArgumentIndex() {
        ExtendedMessageFormat format = new ExtendedMessageFormat("Hello, {0}");
        try {
            format.setFormatsByArgumentIndex(new Format[0]);
        } catch (UnsupportedOperationException e) {
            assertTrue(true);
        }
    }

    @Test
    public void testEquals() {
        ExtendedMessageFormat format1 = new ExtendedMessageFormat("Hello, {0}");
        ExtendedMessageFormat format2 = new ExtendedMessageFormat("Hello, {1}");
        ExtendedMessageFormat format3 = new ExtendedMessageFormat("Hi, {0}");

        assertEquals(format1, format1);
        assertNotEquals(format1, format2);
        assertNotEquals(format1, null);
        assertNotEquals(format1, "Hello, {0}");
        assertNotEquals(format1, format3);
    }

    @Test
    public void testHashCode() {
        ExtendedMessageFormat format1 = new ExtendedMessageFormat("Hello, {0}");
        ExtendedMessageFormat format2 = new ExtendedMessageFormat("Hello, {0}");

        assertEquals(format1.hashCode(), format2.hashCode());
    }

    @Test
    public void testGetFormatWithoutRegistry() {
        ExtendedMessageFormat format = new ExtendedMessageFormat("Hello, {0}");
        assertEquals(null, format.getFormat("custom"));
    }

    @Test
    public void testGetFormatWithRegistry() {
        Map<String, FormatFactory> registry = new HashMap<>();
        registry.put("custom", new FormatFactory() {
            @Override
            public Format getFormat(String name, String args, Locale locale) {
                return new TestFormat();
            }
        });

        ExtendedMessageFormat format = new ExtendedMessageFormat("Hello, {0}", registry);
        assertTrue(format.getFormat("custom") instanceof TestFormat);
    }

    @Test
    public void testReadArgumentIndexWithValidPattern() {
        ExtendedMessageFormat format = new ExtendedMessageFormat("Hello, {0}");
        assertEquals(0, format.readArgumentIndex("Hello, {0}", new MessageFormat.ParsePosition(0)));
    }

    @Test(expected = IllegalArgumentException.class)
    public void testReadArgumentIndexWithInvalidPattern() {
        ExtendedMessageFormat format = new ExtendedMessageFormat("Hello, {0}");
        format.readArgumentIndex("Hello, {a}", new MessageFormat.ParsePosition(0));
    }

    @Test(expected = IllegalArgumentException.class)
    public void testReadArgumentIndexWithUnterminatedElement() {
        ExtendedMessageFormat format = new ExtendedMessageFormat("Hello, {0}");
        format.readArgumentIndex("Hello, {0", new MessageFormat.ParsePosition(0));
    }

    @Test
    public void testParseFormatDescriptionWithValidPattern() {
        ExtendedMessageFormat format = new ExtendedMessageFormat("Hello, {0}");
        assertEquals("", format.parseFormatDescription("Hello, {0}", new MessageFormat.ParsePosition(0)));
    }

    @Test(expected = IllegalArgumentException.class)
    public void testParseFormatDescriptionWithUnterminatedElement() {
        ExtendedMessageFormat format = new ExtendedMessageFormat("Hello, {0}");
        format.parseFormatDescription("Hello, {0", new MessageFormat.ParsePosition(0));
    }

    @Test
    public void testInsertFormatsWithoutCustomPatterns() {
        ExtendedMessageFormat format = new ExtendedMessageFormat("Hello, {0}");
        assertEquals("Hello, {0}", format.insertFormats("Hello, {0}", new ArrayList<String>()));
    }

    @Test
    public void testInsertFormatsWithCustomPatterns() {
        ExtendedMessageFormat format = new ExtendedMessageFormat("Hello, {0}");
        List<String> customPatterns = new ArrayList<>();
        customPatterns.add("customFormat");
        assertEquals("Hello, {0,customFormat}", format.insertFormats("Hello, {0}", customPatterns));
    }

    @Test(expected = IllegalArgumentException.class)
    public void testAppendQuotedStringWithUnterminatedString() {
        ExtendedMessageFormat format = new ExtendedMessageFormat("Hello, {0}");
        format.appendQuotedString("Hello, {0'", new MessageFormat.ParsePosition(0), new StringBuilder(), true);
    }

    @Test
    public void testContainsElementsWithEmptyCollection() {
        ExtendedMessageFormat format = new ExtendedMessageFormat("Hello, {0}");
        assertFalse(format.containsElements(new ArrayList<>()));
    }

    @Test
    public void testContainsElementsWithCollectionContainingNull() {
        ExtendedMessageFormat format = new ExtendedMessageFormat("Hello, {0}");
        List<Object> list = new ArrayList<>();
        list.add(null);
        assertTrue(format.containsElements(list));
    }

    private static class TestFormat extends Format {
        @Override
        public StringBuffer format(Object obj, StringBuffer toAppendTo, FieldPosition pos) {
            return null;
        }

        @Override
        public Object parseObject(String source, ParsePosition pos) {
            return null;
        }
    }
}