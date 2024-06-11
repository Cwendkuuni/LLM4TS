package org.apache.commons.lang.text;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertThrows;

import java.text.ParseException;
import java.text.ParsePosition;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Locale;
import java.util.Map;

import org.apache.commons.lang.Validate;

import org.apache.commons.lang3.StringUtils;
import org.apache.commons.lang3.text.StrMatcher;

import org.junit.Test;

/**
 * JUnit test file for ExtendedMessageFormat class.
 */
public class ExtendedMessageFormatTest {

    /**
     * Test constructor with default locale and empty pattern.
     */
    @Test
    public void testDefaultConstructor() {
        ExtendedMessageFormat fmt = new ExtendedMessageFormat("");
        assertNotNull(fmt);
        assertEquals("", fmt.toPattern());
    }

    /**
     * Test constructor with default locale and non-empty pattern.
     */
    @Test
    public void testConstructorWithPattern() {
        ExtendedMessageFormat fmt = new ExtendedMessageFormat("{0} {1} {2}", Locale.getDefault());
        assertNotNull(fmt);
        assertEquals("{0} {1} {2}", fmt.toPattern());
    }

    /**
     * Test constructor with default locale and registry.
     */
    @Test
    public void testConstructorWithPatternAndRegistry() {
        Map<String, FormatFactory> registry = new HashMap<>();
        ExtendedMessageFormat fmt = new ExtendedMessageFormat("{0} {1} {2}", Locale.ROOT, registry);
        assertNotNull(fmt);
        assertEquals("{0} {1} {2}", fmt.toPattern());
        assertNull(fmt.registry);
    }

    /**
     * Test constructor with custom locale, empty pattern and registry.
     */
    @Test
    public void testConstructorWithLocaleAndEmptyPatternAndRegistry() {
        Map<String, FormatFactory> registry = new HashMap<>();
        ExtendedMessageFormat fmt = new ExtendedMessageFormat("", new Locale("fr", "FR"), registry);
        assertNotNull(fmt);
        assertEquals("", fmt.toPattern());
        assertNull(fmt.registry);
    }

    /**
     * Test constructor with custom locale, non-empty pattern and registry.
     */
    @Test
    public void testConstructorWithLocaleAndPatternAndRegistry() {
        Map<String, FormatFactory> registry = new HashMap<>();
        registry.put("MyCustomFormat", new FormatFactory() {
            @Override
            public Format getFormat(String name, String format, Locale locale) {
                return new MyFormat();
            }
        });
        ExtendedMessageFormat fmt = new ExtendedMessageFormat("{0, format=\"MyCustomFormat\"}", new Locale("fr", "FR"), registry);
        assertNotNull(fmt);
        assertEquals("{0, format=\"MyCustomFormat\"}", fmt.toPattern());
        assertNotNull(fmt.registry);
    }

    /**
     * Test toPattern method.
     */
    @Test
    public void testToPattern() {
        ExtendedMessageFormat fmt = new ExtendedMessageFormat("{0} {1} {2}", Locale.getDefault());
        assertEquals("{0} {1} {2}", fmt.toPattern());
        fmt.applyPattern("{0, format=\"MyFormat\"} {1} {2}");
        assertEquals("{0, format=\"MyFormat\"} {1} {2}", fmt.toPattern());
    }

    /**
     * Test illegal argument exception when applying an empty pattern.
     */
    @Test
    public void testIllegalArgumentExceptionWhenApplyingEmptyPattern() {
        ExtendedMessageFormat fmt = new ExtendedMessageFormat("", Locale.getDefault());
        assertThrows(IllegalArgumentException.class, () -> fmt.applyPattern(""));
    }

    /**
     * Test illegal argument exception when applying a pattern containing an invalid escape sequence.
     */
    @Test
    public void testIllegalArgumentExceptionWhenApplyingPatternWithInvalidEscapeSequence() {
        ExtendedMessageFormat fmt = new ExtendedMessageFormat("{0} {1} {2}", Locale.getDefault());
        assertThrows(IllegalArgumentException.class, () -> fmt.applyPattern("{0} {1} {2}{3}"));
    }

    /**
     * Test illegal argument exception when applying a pattern containing an invalid custom format name.
     */
    @Test
    public void testIllegalArgumentExceptionWhenApplyingPatternWithInvalidCustomFormatName() {
        Map<String, FormatFactory> registry = new HashMap<>();
        registry.put("MyFormat", new FormatFactory() {
            @Override
            public Format getFormat(String name, String format, Locale locale) {
                return new MyFormat();
            }
        });
        ExtendedMessageFormat fmt = new ExtendedMessageFormat("", Locale.getDefault(), registry);
        assertThrows(IllegalArgumentException.class, () -> fmt.applyPattern("{0, format=\"InvalidFormat\"}"));
    }

    /**
     * Test illegal argument exception when applying a pattern containing a missing custom format name.
     */
    @Test
    public void testIllegalArgumentExceptionWhenApplyingPatternWithMissingCustomFormatName() {
        Map<String, FormatFactory> registry = new HashMap<>();
        ExtendedMessageFormat fmt = new ExtendedMessageFormat("", Locale.getDefault(), registry);
        assertThrows(IllegalArgumentException.class, () -> fmt.applyPattern("{0, format=\"MissingFormat\"}"));
    }

    /**
     * Test parsePosition.
     */
    @Test
    public void testParsePosition() {
        ExtendedMessageFormat fmt = new ExtendedMessageFormat("{0} {1} {2}", Locale.getDefault());
        String pattern = "{0, format=\"MyFormat\"} {1} {2}";
        ParsePosition pos = new ParsePosition(0);
        fmt.applyPattern(pattern);
        int index = 0;
        char[] c = pattern.toCharArray();
        while (pos.getIndex() < pattern.length()) {
            switch (c[pos.getIndex()]) {
            case '{':
                index++;
                int argumentIndex = fmt.readArgumentIndex(pattern, next(pos));
                assertEquals(index, argumentIndex);
                break;
            case '}':
                index--;
                break;
            case ' ':
                // skip spaces
                break;
            default:
                // parse other characters
                break;
            }
            next(pos);
        }
        assertEquals(0, index);
    }

    /**
     * Test toPattern method with custom formatting.
     */
    @Test
    public void testToPatternWithCustomFormatting() {
        Map<String, FormatFactory> registry = new HashMap<>();
        registry.put("MyFormat", new FormatFactory() {
            @Override
            public Format getFormat(String name, String format, Locale locale) {
                return new MyFormat();
            }
        });
        ExtendedMessageFormat fmt = new ExtendedMessageFormat("", Locale.getDefault(), registry);
        fmt.applyPattern("{0, format=\"MyFormat\"} {1} {2}");
        assertEquals("{0, format=\"MyFormat\"} {1} {2}", fmt.toPattern());
    }

    /**
     * Test getFormat method.
     */
    @Test
    public void testGetFormat() {
        Map<String, FormatFactory> registry = new HashMap<>();
        registry.put("MyFormat", new FormatFactory() {
            @Override
            public Format getFormat(String name, String format, Locale locale) {
                return new MyFormat();
            }
        });
        ExtendedMessageFormat fmt = new ExtendedMessageFormat("", Locale.getDefault(), registry);
        fmt.applyPattern("{0, format=\"MyFormat\"} {1} {2}");
        Format format = fmt.getFormat(1);
        assertNotNull(format);
        assertEquals(MyFormat.class, format.getClass());
    }

    /**
     * Test getFormat method when no custom format is found.
     */
    @Test
    public void testGetFormatWhenNoCustomFormatIsFound() {
        Map<String, FormatFactory> registry = new HashMap<>();
        ExtendedMessageFormat fmt = new ExtendedMessageFormat("", Locale.getDefault(), registry);
        fmt.applyPattern("{0, format=\"MyFormat\"} {1} {2}");
        Format format = fmt.getFormat(3);
        assertNull(format);
    }

    /**
     * Test illegal argument exception when setting format.
     */
    @Test
    public void testIllegalArgumentExceptionWhenSettingFormat() {
        ExtendedMessageFormat fmt = new ExtendedMessageFormat("", Locale.getDefault());
        assertThrows(UnsupportedOperationException.class, () -> fmt.setFormat(1, new MyFormat()));
    }

    /**
     * Test illegal argument exception when setting format by argument index.
     */
    @Test
    public void testIllegalArgumentExceptionWhenSettingFormatByArgumentIndex() {
        ExtendedMessageFormat fmt = new ExtendedMessageFormat("", Locale.getDefault());
        assertThrows(UnsupportedOperationException.class, () -> fmt.setFormatByArgumentIndex(1, new MyFormat()));
    }

    /**
     * Test illegal argument exception when setting formats.
     */
    @Test
    public void testIllegalArgumentExceptionWhenSettingFormats() {
        ExtendedMessageFormat fmt = new ExtendedMessageFormat("", Locale.getDefault());
        Format[] formats = new Format[1];
        formats[0] = new MyFormat();
        assertThrows(UnsupportedOperationException.class, () -> fmt.setFormats(formats));
    }

    /**
     * Test illegal argument exception when setting formats by argument index.
     */
    @Test
    public void testIllegalArgumentExceptionWhenSettingFormatsByArgumentIndex() {
        ExtendedMessageFormat fmt = new ExtendedMessageFormat("", Locale.getDefault());
        Format[] formats = new Format[1];
        formats[0] = new MyFormat();
        assertThrows(UnsupportedOperationException.class, () -> fmt.setFormatsByArgumentIndex(formats));
    }

    /**
     * Custom format factory.
     */
    private static class FormatFactory {
        public abstract Format getFormat(String name, String format, Locale locale);
    }

    /**
     * Custom format.
     */
    private static class MyFormat extends Format {
        @Override
        public String format(Object obj, Locale locale, String format, ParsePosition pos) {
            throw new UnsupportedOperationException();
        }

        @Override
        public Object parseObject(String source, ParsePosition pos) {
            throw new UnsupportedOperationException();
        }

        @Override
        public String toString() {
            return StringUtils.EMPTY;
        }
    }

    /**
     * Helper method to advance parse position by 1.
     *
     * @param pos parse position
     * @return parse position
     */
    private static ParsePosition next(ParsePosition pos) {
        pos.setIndex(pos.getIndex() + 1);
        return pos;
    }
}