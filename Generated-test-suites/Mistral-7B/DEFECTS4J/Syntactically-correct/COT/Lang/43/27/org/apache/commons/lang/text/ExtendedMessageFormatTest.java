package org.apache.commons.lang.text;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertThrows;
import static org.junit.Assert.assertTrue;

import java.text.MessageFormat;
import java.text.ParsePosition;
import java.util.Collections;
import java.util.HashMap;
import java.util.Locale;
import java.util.Map;

import org.apache.commons.lang3.Validate;
import org.apache.commons.lang3.builder.ToStringBuilder;
import org.junit.Before;
import org.junit.Test;

public class ExtendedMessageFormatTest {

    private static final String DUMMY_PATTERN = "";
    private static final String ESCAPED_QUOTE = "''";
    private static final char START_FMT = ',';
    private static final char END_FE = '}';
    private static final char START_FE_WITHOUT_S = '{';
    private static final char START_FMT_WITHOUT_S = ',';
    private static final char QUOTE = '\'';

    private ExtendedMessageFormat messageFormat;
    private Map<String, FormatFactory> registry;

    @Before
    public void setUp() {
        String pattern = "Hello {0} {1} {2}{3} {4}";
        registry = new HashMap<>();
        registry.put("customFormat", new CustomFormatFactory());
        messageFormat = new ExtendedMessageFormat(pattern, registry);
    }

    @Test
    public void testConstructor() {
        assertEquals("", messageFormat.toPattern());
        assertEquals(Locale.getDefault(), messageFormat.getLocale());
    }

    @Test
    public void testConstructorWithPattern() {
        String pattern = "Hello {0} {1} {2}{3} {4}";
        messageFormat = new ExtendedMessageFormat(pattern, Locale.getDefault());
        assertEquals(pattern, messageFormat.toPattern());
    }

    @Test
    public void testConstructorWithPatternAndRegistry() {
        String pattern = "Hello {0} {1} {2}{3} {4}";
        messageFormat = new ExtendedMessageFormat(pattern, Locale.getDefault(), registry);
        assertEquals(pattern, messageFormat.toPattern());
    }

    @Test
    public void testConstructorWithPatternAndRegistryWithNullPattern() {
        assertThrows(IllegalArgumentException.class, () -> {
            messageFormat = new ExtendedMessageFormat(null, Locale.getDefault(), registry);
        });
    }

    @Test
    public void testApplyPattern() {
        String pattern = "Hello {0} {1} {2}{3} {4}";
        messageFormat = new ExtendedMessageFormat(pattern, Locale.getDefault());
        messageFormat.applyPattern(pattern);
        assertEquals(pattern, messageFormat.toPattern());
    }

    @Test
    public void testApplyPatternWithRegistry() {
        String pattern = "Hello {0, customFormat} {1} {2}{3} {4}";
        messageFormat = new ExtendedMessageFormat(pattern, Locale.getDefault(), registry);
        messageFormat.applyPattern(pattern);
        String expectedPattern = "Hello {0, customFormat} {1} {2}{3} {4}";
        assertEquals(expectedPattern, messageFormat.toPattern());
    }

    @Test
    public void testNegativeArgumentIndex() {
        assertThrows(IllegalArgumentException.class, () -> {
            messageFormat.applyPattern("Hello {-1}");
        });
    }

    @Test
    public void testEmptyFormatDescription() {
        assertThrows(IllegalArgumentException.class, () -> {
            messageFormat.applyPattern("Hello {}", registry);
        });
    }

    @Test
    public void testMissingFormatDescription() {
        assertThrows(IllegalArgumentException.class, () -> {
            messageFormat.applyPattern("Hello {0} {}", registry);
        });
    }

    @Test
    public void testInvalidFormatDescription() {
        assertThrows(IllegalArgumentException.class, () -> {
            messageFormat.applyPattern("Hello {0, invalidFormat} {}", registry);
        });
    }

    @Test
    public void testFormatWithNullArguments() {
        String pattern = "Hello {0} {1} {2}{3} {4}";
        Object[] arguments = new Object[5];
        String formattedMessage = messageFormat.format(arguments);
        assertEquals("", formattedMessage);
    }

    @Test
    public void testFormatWithArguments() {
        String pattern = "Hello {0} {1} {2}{3} {4}";
        Object[] arguments = new Object[]{"Alice", "Bob", "123", "456", "789"};
        String formattedMessage = messageFormat.format(arguments);
        String expectedMessage = "Hello Alice Bob 123456 789";
        assertEquals(expectedMessage, formattedMessage);
    }

    @Test
    public void testFormatWithMissingArgument() {
        String pattern = "Hello {0} {1} {2}{3} {4}";
        Object[] arguments = new Object[]{null, "Bob", "123", "456", "789"};
        String formattedMessage = messageFormat.format(arguments);
        assertEquals("Hello Bob {}", formattedMessage);
    }

    @Test
    public void testFormatWithInvalidArgumentType() {
        String pattern = "Hello {0} {1, number, integer} {2}{3} {4}";
        Object[] arguments = new Object[]{"Alice", "Bob", 123L, "456", "789"};
        String formattedMessage = messageFormat.format(arguments);
        assertEquals("Hello Alice Bob 123456 789", formattedMessage);
    }

    @Test
    public void testFormatWithNullFormatFactory() {
        String pattern = "Hello {0, nullFormatFactory} {1} {2}{3} {4}";
        Map<String, FormatFactory> registry = Collections.emptyMap();
        messageFormat = new ExtendedMessageFormat(pattern, Locale.getDefault(), registry);
        Object[] arguments = new Object[]{"Alice", "Bob", "123", "456", "789"};
        assertThrows(IllegalArgumentException.class, () -> {
            messageFormat.format(arguments);
        });
    }

    @Test
    public void testFormatWithEmptyFormatFactory() {
        String pattern = "Hello {0, emptyFormatFactory} {1} {2}{3} {4}";
        FormatFactory emptyFormatFactory = new EmptyFormatFactory();
        registry.put("emptyFormatFactory", emptyFormatFactory);
        messageFormat = new ExtendedMessageFormat(pattern, Locale.getDefault(), registry);
        Object[] arguments = new Object[]{"Alice", "Bob", "123", "456", "789"};
        String formattedMessage = messageFormat.format(arguments);
        assertEquals("Hello {} {}", formattedMessage);
    }

    @Test
    public void testFormatWithInvalidFormatFactory() {
        String pattern = "Hello {0, invalidFormatFactory} {1} {2}{3} {4}";
        FormatFactory invalidFormatFactory = new InvalidFormatFactory();
        registry.put("invalidFormatFactory", invalidFormatFactory);
        messageFormat = new ExtendedMessageFormat(pattern, Locale.getDefault(), registry);
        Object[] arguments = new Object[]{"Alice", "Bob", "123", "456", "789"};
        String formattedMessage = messageFormat.format(arguments);
        assertEquals("Hello {} {}", formattedMessage);
    }

    @Test
    public void testFormatWithInvalidFormatFactoryArgument() {
        String pattern = "Hello {0, invalidFormatFactory[argument]} {1} {2}{3} {4}";
        FormatFactory invalidFormatFactory = new InvalidFormatFactory();
        registry.put("invalidFormatFactory", invalidFormatFactory);
        messageFormat = new ExtendedMessageFormat(pattern, Locale.getDefault(), registry);
        Object[] arguments = new Object[]{"Alice", "Bob", "123", "456", "789"};
        assertThrows(IllegalArgumentException.class, () -> {
            messageFormat.format(arguments, "invalidArgument");
        });
    }

    @Test
    public void testFormatWithMissingFormatFactoryArgument() {
        String pattern = "Hello {0, formatFactory[0]} {1} {2}{3} {4}";
        FormatFactory formatFactory = new EmptyFormatFactory();
        registry.put("formatFactory", formatFactory);
        messageFormat = new ExtendedMessageFormat(pattern, Locale.getDefault(), registry);
        Object[] arguments = new Object[]{"Alice", "Bob", "123", "456", "789"};
        String formattedMessage = messageFormat.format(arguments);
        assertEquals("Hello {} {}", formattedMessage);
    }

    @Test
    public void testFormatWithMissingFormatFactoryArgumentIndex() {
        String pattern = "Hello {0, formatFactory[3]} {1} {2}{3} {4}";
        FormatFactory formatFactory = new EmptyFormatFactory();
        registry.put("formatFactory", formatFactory);
        messageFormat = new ExtendedMessageFormat(pattern, Locale.getDefault(), registry);
        Object[] arguments = new Object[]{"Alice", "Bob", "123", "456", "789"};
        String formattedMessage = messageFormat.format(arguments);
        assertEquals("Hello {} {}", formattedMessage);
    }

    @Test
    public void testFormatWithMissingFormatFactoryArgumentNull() {
        String pattern = "Hello {0, formatFactory[null]} {1} {2}{3} {4}";
        FormatFactory formatFactory = new EmptyFormatFactory();
        registry.put("formatFactory", formatFactory);
        messageFormat = new ExtendedMessageFormat(pattern, Locale.getDefault(), registry);
        Object[] arguments = new Object[]{"Alice", "Bob", "123", "456", null};
        String formattedMessage = messageFormat.format(arguments);
        assertEquals("Hello {} {}", formattedMessage);
    }

    @Test
    public void testGetFormat() {
        String pattern = "Hello {0, customFormat} {1} {2}{3} {4}";
        messageFormat = new ExtendedMessageFormat(pattern, Locale.getDefault(), registry);
        Format format = messageFormat.getFormat(0);
        assertTrue(format instanceof CustomFormat);
    }

    @Test
    public void testGetFormatWithInvalidFormatName() {
        String pattern = "Hello {0, invalidFormatName} {1} {2}{3} {4}";
        messageFormat = new ExtendedMessageFormat(pattern, Locale.getDefault(), registry);
        assertNull(messageFormat.getFormat(0));
    }

    @Test
    public void testSetFormat() {
        String pattern = "Hello {0, customFormat} {1} {2}{3} {4}";
        messageFormat = new ExtendedMessageFormat(pattern, Locale.getDefault(), registry);
        Format customFormat = new CustomFormat();
        messageFormat.setFormat(0, customFormat);
        Format format = messageFormat.getFormat(0);
        assertSame(customFormat, format);
    }

    @Test
    public void testSetFormatWithInvalidFormatIndex() {
        String pattern = "Hello {0} {1} {2}{3} {4}";
        messageFormat = new ExtendedMessageFormat(pattern, Locale.getDefault(), registry);
        Format customFormat = new CustomFormat();
        assertThrows(UnsupportedOperationException.class, () -> {
            messageFormat.setFormat(2, customFormat);
        });
    }

    @Test
    public void testSetFormats() {
        String pattern = "Hello {0} {1} {2}{3} {4}";
        Format[] formats = new Format[5];
        messageFormat = new ExtendedMessageFormat(pattern, Locale.getDefault(), registry);
        messageFormat.setFormats(formats);
        Format format = messageFormat.getFormat(0);
        assertNull(format);
    }

    @Test
    public void testSetFormatsWithInvalidFormatIndex() {
        String pattern = "Hello {0} {1} {2}{3} {4}";
        Format[] formats = new Format[5];
        messageFormat = new ExtendedMessageFormat(pattern, Locale.getDefault(), registry);
        assertThrows(UnsupportedOperationException.class, () -> {
            messageFormat.setFormatsByArgumentIndex(new Format[5]);
        });
    }

    private static class CustomFormatFactory implements FormatFactory {
        @Override
        public Format getFormat(String formatName, String formatStyle, Locale locale) {
            return new CustomFormat();
        }

        @Override
        public String toString() {
            return ToStringBuilder.reflectionToString(this);
        }
    }

    private static class CustomFormat implements Format {
        @Override
        public Object parseObject(String source, ParsePosition pos) {
            return "CustomFormat";
        }

        @Override
        public String format(Object obj, Locale locale, String format,
                ParsePosition pos) {
            return "CustomFormatted: " + obj.toString();
        }

        @Override
        public String toString() {
            return ToStringBuilder.reflectionToString(this);
        }
    }

    private static class EmptyFormatFactory implements FormatFactory {
        @Override
        public Format getFormat(String formatName, String formatStyle, Locale locale) {
            return null;
        }

        @Override
        public String toString() {
            return ToStringBuilder.reflectionToString(this);
        }
    }

    private static class InvalidFormatFactory implements FormatFactory {
        @Override
        public Format getFormat(String formatName, String formatStyle, Locale locale) {
            throw new UnsupportedOperationException();
        }

        @Override
        public String toString() {
            return ToStringBuilder.reflectionToString(this);
        }
    }
}