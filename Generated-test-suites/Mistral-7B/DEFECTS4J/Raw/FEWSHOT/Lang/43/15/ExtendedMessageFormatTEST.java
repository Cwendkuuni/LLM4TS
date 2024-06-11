package org.apache.commons.lang.text;

import static org.junit.Assert.assertArrayEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertSame;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

import java.text.ParseException;
import java.util.HashMap;
import java.util.Locale;
import java.util.Map;

import org.apache.commons.cli.FormatFactory;
import org.apache.commons.lang.text.ExtendedMessageFormat;
import org.apache.commons.lang.text.MessageFormat;
import org.junit.Test;

/**
 * Test cases for {@link ExtendedMessageFormat}.
 */
public class ExtendedMessageFormatTest {

    private static class TestFormatFactory implements FormatFactory {
        private final String formatName;

        public TestFormatFactory(String formatName) {
            this.formatName = formatName;
        }

        @Override
        public Format getFormat(String formatName, String argument, Locale locale) {
            if (formatName.equals(this.formatName)) {
                return new TestFormat();
            }
            return null;
        }

        @Override
        public String getFormatName() {
            return formatName;
        }
    }

    private static class TestFormat implements Format {
        @Override
        public String format(Object obj, Locale locale) {
            return "TestFormat: " + obj;
        }

        @Override
        public Object parseObject(String source, ParsePosition parsePosition)
                throws ParseException, IllegalFormatException {
            if (source == null) {
                throw new ParseException("NULL source", parsePosition);
            }
            parsePosition.setIndex(parsePosition.getIndex() + source.length());
            return "TestFormat: " + source;
        }

        @Override
        public String toString() {
            return "TestFormat";
        }
    }

    /**
     * Test constructor.
     */
    @Test
    public void testConstructor() {
        ExtendedMessageFormat fmt = new ExtendedMessageFormat("{} {}", new HashMap<>());
        assertEquals("", fmt.toPattern());
        assertEquals("{}", fmt.format(null, Locale.US));
        assertEquals("{} {}", new Object[] { "foo" }, fmt.format(null, Locale.US));
    }

    /**
     * Test constructor with registry.
     */
    @Test
    public void testConstructorWithRegistry() {
        Map<String, FormatFactory> registry = new HashMap<>();
        registry.put("test", new TestFormatFactory("test"));
        ExtendedMessageFormat fmt = new ExtendedMessageFormat("{test}", new Locale("en", "US"), registry);
        assertEquals("{test}", fmt.toPattern());
        assertEquals("TestFormat: null", fmt.format(null, Locale.US));
        assertEquals("TestFormat: foo", fmt.format(new Object[] { "foo" }, Locale.US));
    }

    /**
     * Test applyPattern method with custom format.
     */
    @Test
    public void testApplyPatternWithCustomFormat() {
        Map<String, FormatFactory> registry = new HashMap<>();
        registry.put("test", new TestFormatFactory("test"));
        ExtendedMessageFormat fmt = new ExtendedMessageFormat("{test}", new Locale("en", "US"), registry);
        String pattern = "{test} {0} {test}";
        fmt.applyPattern(pattern);
        assertEquals("TestFormat {0} TestFormat", fmt.toPattern());
        Object[] args = {"bar"};
        assertEquals("TestFormat bar TestFormat", fmt.format(args, Locale.US));
    }

    /**
     * Test applyPattern method with missing format.
     */
    @Test(expected = IllegalArgumentException.class)
    public void testApplyPatternWithMissingFormat() {
        Map<String, FormatFactory> registry = new HashMap<>();
        ExtendedMessageFormat fmt = new ExtendedMessageFormat("{missingFormat}", new Locale("en", "US"), registry);
        String pattern = "{missingFormat} {0} {missingFormat}";
        fmt.applyPattern(pattern);
    }

    /**
     * Test applyPattern method with invalid argument index.
     */
    @Test(expected = IllegalArgumentException.class)
    public void testApplyPatternWithInvalidArgumentIndex() {
        Map<String, FormatFactory> registry = new HashMap<>();
        ExtendedMessageFormat fmt = new ExtendedMessageFormat("{test}", new Locale("en", "US"), registry);
        String pattern = "{test} {-1} {test}";
        fmt.applyPattern(pattern);
    }

    /**
     * Test setFormat method.
     */
    @Test(expected = UnsupportedOperationException.class)
    public void testSetFormat() {
        ExtendedMessageFormat fmt = new ExtendedMessageFormat("{} {}", new HashMap<>());
        fmt.setFormat(0, new TestFormat());
    }

    /**
     * Test setFormatByArgumentIndex method.
     */
    @Test(expected = UnsupportedOperationException.class)
    public void testSetFormatByArgumentIndex() {
        ExtendedMessageFormat fmt = new ExtendedMessageFormat("{} {}", new HashMap<>());
        fmt.setFormatByArgumentIndex(0, new TestFormat());
    }

    /**
     * Test setFormats method.
     */
    @Test(expected = UnsupportedOperationException.class)
    public void testSetFormats() {
        ExtendedMessageFormat fmt = new ExtendedMessageFormat("{} {}", new HashMap<>());
        Format[] formats = new TestFormat[1];
        formats[0] = new TestFormat();
        fmt.setFormats(formats);
    }

    /**
     * Test setFormatsByArgumentIndex method.
     */
    @Test(expected = UnsupportedOperationException.class)
    public void testSetFormatsByArgumentIndex() {
        ExtendedMessageFormat fmt = new ExtendedMessageFormat("{} {}", new HashMap<>());
        Format[] formats = new TestFormat[1];
        formats[0] = new TestFormat();
        fmt.setFormatsByArgumentIndex(new Object[] { 0 }, formats);
    }

    /**
     * Test toPattern method.
     */
    @Test
    public void testToPattern() {
        Map<String, FormatFactory> registry = new HashMap<>();
        registry.put("test", new TestFormatFactory("test"));
        ExtendedMessageFormat fmt = new ExtendedMessageFormat("{test}", new Locale("en", "US"), registry);
        fmt.applyPattern("{test} {0} {test}");
        assertEquals("TestFormat {0} TestFormat", fmt.toPattern());
    }
}