package org.apache.commons.lang.text;

import org.apache.commons.lang3.ArrayUtils;
import org.apache.commons.lang3.StringUtils;
import org.apache.commons.lang3.test.TestUtils;
import org.junit.Before;
import org.junit.Test;
import static org.junit.Assert.*;

import java.text.Format;
import java.text.MessageFormat;
import java.text.ParsePosition;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Locale;
import java.util.Map;

/**
 * Tests for {@link ExtendedMessageFormat}.
 */
public class ExtendedMessageFormatTest {

    private ExtendedMessageFormat format;

    @Before
    public void setUp() {
        Map<String, FormatFactory> registry = new HashMap<>();
        registry.put("format1", new FormatFactory() {
            @Override
            public Format getFormat(String name, String arguments, Locale locale) {
                if (name.equals("format1")) {
                    return new TestFormat();
                }
                return null;
            }
        });
        format = new ExtendedMessageFormat("{0, format1} {1}", new Locale("en", "US"), registry);
    }

    @Test
    public void testSimpleFormat() {
        Object[] args = new Object[]{"Hello", "World"};
        String formattedMessage = format.format(args);
        assertEquals("Hello World", formattedMessage);
    }

    @Test
    public void testEmptyFormat() {
        Object[] args = new Object[0];
        String formattedMessage = format.format(args);
        assertTrue(StringUtils.isEmpty(formattedMessage));
    }

    @Test
    public void testErrorFormat() {
        Object[] args = new Object[]{1, 2, 3};
        try {
            format.format(args);
            fail("Expected IllegalStateException");
        } catch (IllegalArgumentException e) {
            // Expected
        }
    }

    @Test
    public void testFormatRegistry() {
        Object[] args = new Object[]{"{0, format1} {1}", new Object[]{"Hello", "World"}};
        String formattedMessage = format.format(args);
        assertEquals("Hello World", formattedMessage);
    }

    @Test
    public void testFormatRegistryMissingFormat() {
        Map<String, FormatFactory> registry = new HashMap<>(format.getRegistry());
        registry.remove("format1");
        ExtendedMessageFormat newFormat = new ExtendedMessageFormat("{0, format1}", new Locale("en", "US"), registry);
        Object[] args = new Object[]{"{0, format1}", new Object[]{"Hello"}};
        try {
            newFormat.format(args);
            fail("Expected IllegalArgumentException");
        } catch (IllegalArgumentException e) {
            // Expected
        }
    }

    @Test
    public void testFormatRegistryWithEmptyFormatName() {
        Map<String, FormatFactory> registry = new HashMap<>();
        registry.put("", new FormatFactory() {
            @Override
            public Format getFormat(String name, String arguments, Locale locale) {
                return new TestFormat();
            }
        });
        ExtendedMessageFormat newFormat = new ExtendedMessageFormat("{0}", new Locale("en", "US"), registry);
        Object[] args = new Object[]{"Hello"};
        String formattedMessage = newFormat.format(args);
        assertEquals("Hello", formattedMessage);
    }

    @Test
    public void testFormatRegistryWithEmptyRegistry() {
        ExtendedMessageFormat newFormat = new ExtendedMessageFormat("{0}", new Locale("en", "US"), Collections.emptyMap());
        Object[] args = new Object[]{"Hello"};
        try {
            newFormat.format(args);
            fail("Expected IllegalArgumentException");
        } catch (IllegalArgumentException e) {
            // Expected
        }
    }

    @Test
    public void testFormatRegistryWithNullFormatFactory() {
        Map<String, FormatFactory> registry = new HashMap<>();
        registry.put("format1", null);
        ExtendedMessageFormat newFormat = new ExtendedMessageFormat("{0, format1}", new Locale("en", "US"), registry);
        Object[] args = new Object[]{"Hello"};
        try {
            newFormat.format(args);
            fail("Expected IllegalArgumentException");
        } catch (IllegalArgumentException e) {
            // Expected
        }
    }

    @Test
    public void testFormatRegistryWithFormatFactoryError() {
        Map<String, FormatFactory> registry = new HashMap<>();
        registry.put("format1", new FormatFactory() {
            @Override
            public Format getFormat(String name, String arguments, Locale locale) {
                throw new RuntimeException("Test error");
            }
        });
        ExtendedMessageFormat newFormat = new ExtendedMessageFormat("{0, format1}", new Locale("en", "US"), registry);
        Object[] args = new Object[]{"Hello"};
        try {
            newFormat.format(args);
            fail("Expected IllegalStateException");
        } catch (IllegalStateException e) {
            // Expected
        }
    }

    @Test
    public void testFormatRegistryWithFormatFactoryFormatError() {
        Map<String, FormatFactory> registry = new HashMap<>();
        registry.put("format1", new FormatFactory() {
            @Override
            public Format getFormat(String name, String arguments, Locale locale) {
                if (name.equals("format1")) {
                    Format format = new TestFormat();
                    format.setInvalid(true);
                    return format;
                }
                return null;
            }
        });
        ExtendedMessageFormat newFormat = new ExtendedMessageFormat("{0, format1}", new Locale("en", "US"), registry);
        try {
            newFormat.applyPattern("{0, format1}");
            fail("Expected IllegalArgumentException");
        } catch (IllegalArgumentException e) {
            // Expected
        }
    }

    /**
     * A simple test format for testing.
     */
    public static class TestFormat extends Format {
        @Override
        public StringBuffer format(StringBuffer toAppendTo, Object arg, Locale locale) {
            toAppendTo.append("TestFormat");
            return toAppendTo;
        }

        @Override
        public Object parseObject(String source, ParsePosition pos) {
            if (pos.getIndex() >= source.length()) {
                pos.setErrorIndex(pos.getIndex());
                pos.setErrorMessage("Unexpected end of string");
                return null;
            }
            pos.setIndex(pos.getIndex() + 13);
            return new String("TestFormat");
        }

        public boolean isInvalid() {
            return invalid;
        }

        public void setInvalid(boolean invalid) {
            this.invalid = invalid;
        }

        private boolean invalid = false;
    }
}