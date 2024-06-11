package org.apache.commons.lang.text;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertThrows;
import static org.junit.Assert.assertTrue;

import java.text.ParseException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Locale;
import java.util.Map;

import org.apache.commons.lang.Validate;

import org.apache.commons.lang.text.ExtendedMessageFormat;
import org.apache.commons.lang.text.FormatFactory;

/**
 * Test class for {@link ExtendedMessageFormat}.
 */
public class ExtendedMessageFormatTest {

    /**
     * Test creating a simple ExtendedMessageFormat with no custom formatting.
     */
    @org.junit.Test
    public void testSimpleFormat() {
        ExtendedMessageFormat format = new ExtendedMessageFormat("Hello, {0}!");
        String expected = "Hello, John!";
        String actual = format.format(new Object[]{"John"});
        assertEquals(expected, actual);
    }

    /**
     * Test creating an ExtendedMessageFormat with a custom formatting.
     */
    @org.junit.Test
    public void testCustomFormat() {
        Map<String, FormatFactory> registry = new HashMap<>();
        registry.put("CUSTOM", new FormatFactory() {
            @Override
            public Format getFormat(String name, String argument, Locale locale) {
                return new MyFormat();
            }
        });
        ExtendedMessageFormat format = new ExtendedMessageFormat("Hello, {CUSTOM}{0}!", null, registry);
        String expected = "Hello, [CustomFormat:John]!";
        String actual = format.format(new Object[]{"John"});
        assertEquals(expected, actual);
    }

    /**
     * Test creating an ExtendedMessageFormat with missing custom format.
     */
    @org.junit.Test
    public void testMissingFormat() {
        Map<String, FormatFactory> registry = new HashMap<>();
        ExtendedMessageFormat format = new ExtendedMessageFormat("Hello, {MISSING}{0}!", null, registry);
        assertThrows(IllegalArgumentException.class, () -> format.format(new Object[]{"John"}));
    }

    /**
     * Test creating an ExtendedMessageFormat with invalid pattern.
     */
    @org.junit.Test
    public void testInvalidPattern() {
        ExtendedMessageFormat format = assertThrows(IllegalArgumentException.class,
                () -> new ExtendedMessageFormat(""));
        Validate.isTrue(format.getMessage().startsWith("Unreadable format element"));
    }

    /**
     * Test creating an ExtendedMessageFormat with invalid format argument index.
     */
    @org.junit.Test
    public void testInvalidFormatArgumentIndex() {
        ExtendedMessageFormat format = assertThrows(IllegalArgumentException.class,
                () -> new ExtendedMessageFormat("Hello, {-1}{0}!", null));
        Validate.isTrue(format.getMessage().startsWith("Invalid format argument index"));
    }

    /**
     * Test creating an ExtendedMessageFormat with a custom format that throws an exception.
     */
    @org.junit.Test
    public void testCustomFormatException() {
        Map<String, FormatFactory> registry = new HashMap<>();
        registry.put("EXCEPTION", new FormatFactory() {
            @Override
            public Format getFormat(String name, String argument, Locale locale) {
                throw new RuntimeException("CustomFormatException");
            }
        });
        ExtendedMessageFormat format = assertThrows(RuntimeException.class,
                () -> new ExtendedMessageFormat("Hello, {EXCEPTION}{0}!", null, registry));
        assertTrue(format.getMessage().contains("CustomFormatException"));
    }

    /**
     * Test creating an ExtendedMessageFormat with a custom format that returns null.
     */
    @org.junit.Test
    public void testCustomFormatNull() {
        Map<String, FormatFactory> registry = new HashMap<>();
        registry.put("NULL", new FormatFactory() {
            @Override
            public Format getFormat(String name, String argument, Locale locale) {
                return null;
            }
        });
        ExtendedMessageFormat format = new ExtendedMessageFormat("Hello, {NULL}{0}!", null, registry);
        assertNull(format.getFormats());
    }

    /**
     * Test creating an ExtendedMessageFormat with multiple custom formats.
     */
    @org.junit.Test
    public void testMultipleCustomFormats() {
        Map<String, FormatFactory> registry = new HashMap<>();
        registry.put("CUSTOM1", new FormatFactory() {
            @Override
            public Format getFormat(String name, String argument, Locale locale) {
                return new MyFormat1();
            }
        });
        registry.put("CUSTOM2", new FormatFactory() {
            @Override
            public Format getFormat(String name, String argument, Locale locale) {
                return new MyFormat2();
            }
        });
        ExtendedMessageFormat format = new ExtendedMessageFormat("Hello, {CUSTOM1}{0}{CUSTOM2}{1}!", null, registry);
        String expected = "Hello, []John[]{}!";
        String actual = format.format(new Object[]{"John", "Jane"});
        assertEquals(expected, actual);
    }

    /**
     * Test creating an ExtendedMessageFormat with a custom format that is not thread-safe.
     */
    @org.junit.Test
    public void testCustomFormatNotThreadSafe() {
        Map<String, FormatFactory> registry = new HashMap<>();
        registry.put("NOT_THREAD_SAFE", new FormatFactory() {
            private int count;

            @Override
            public Format getFormat(String name, String argument, Locale locale) {
                return new NotThreadSafeFormat(count++);
            }
        });
        ExtendedMessageFormat format1 = new ExtendedMessageFormat("{NOT_THREAD_SAFE}{0}", null, registry);
        ExtendedMessageFormat format2 = new ExtendedMessageFormat("{NOT_THREAD_SAFE}{0}", null, registry);
        String expected1 = Integer.toString(0);
        String expected2 = Integer.toString(1);
        String actual1 = format1.format(new Object[]{Integer.valueOf(0)});
        String actual2 = format2.format(new Object[]{Integer.valueOf(1)});
        assertEquals(expected1, actual1);
        assertEquals(expected2, actual2);
        assertFalse(actual1.equals(actual2));
    }

    /**
     * Test creating an ExtendedMessageFormat with a custom format that is not thread-safe and using same argument.
     */
    @org.junit.Test
    public void testCustomFormatNotThreadSafeSameArg() {
        Map<String, FormatFactory> registry = new HashMap<>();
        registry.put("NOT_THREAD_SAFE", new FormatFactory() {
            private int count;

            @Override
            public Format getFormat(String name, String argument, Locale locale) {
                return new NotThreadSafeFormat(count++);
            }
        });
        ExtendedMessageFormat format1 = new ExtendedMessageFormat("{NOT_THREAD_SAFE}{0}", null, registry);
        ExtendedMessageFormat format2 = new ExtendedMessageFormat("{NOT_THREAD_SAFE}{0}", null, registry);
        String expected = Integer.toString(0);
        String actual1 = format1.format(new Object[]{Integer.valueOf(0)});
        String actual2 = format2.format(new Object[]{Integer.valueOf(0)});
        assertEquals(expected, actual1);
        assertEquals(expected, actual2);
        assertTrue(actual1.equals(actual2));
    }

    /**
     * Test creating an ExtendedMessageFormat with a custom format that is not thread-safe and using different arguments.
     */
    @org.junit.Test
    public void testCustomFormatNotThreadSafeDifferentArgs() {
        Map<String, FormatFactory> registry = new HashMap<>();
        registry.put("NOT_THREAD_SAFE", new FormatFactory() {
            private int count;

            @Override
            public Format getFormat(String name, String argument, Locale locale) {
                return new NotThreadSafeFormat(count++);
            }
        });
        ExtendedMessageFormat format1 = new ExtendedMessageFormat("{NOT_THREAD_SAFE}{0}", null, registry);
        ExtendedMessageFormat format2 = new ExtendedMessageFormat("{NOT_THREAD_SAFE}{1}", null, registry);
        String actual1 = format1.format(new Object[]{Integer.valueOf(0)});
        String actual2 = format2.format(new Object[]{Integer.valueOf(1)});
        assertFalse(actual1.equals(actual2));
    }

    /**
     * FormatFactory implementation for testing.
     */
    public static class FormatFactory {

        public interface Format {
        }

        public static class MyFormat implements Format {
            @Override
            public String format(Object o) throws ParseException {
                if (o != null) {
                    return "[CustomFormat:" + o.toString() + "]";
                } else {
                    return "";
                }
            }
        }

        public static class MyFormat1 implements Format {
            @Override
            public String format(Object o) throws ParseException {
                if (o != null) {
                    return "[" + o.toString() + "1]";
                } else {
                    return "";
                }
            }
        }

        public static class MyFormat2 implements Format {
            @Override
            public String format(Object o) throws ParseException {
                if (o != null) {
                    return "[" + o.toString() + "2]";
                } else {
                    return "";
                }
            }
        }

        public static class NotThreadSafeFormat implements Format {
            private int id;

            public NotThreadSafeFormat(int id) {
                this.id = id;
            }

            @Override
            public String format(Object o) throws ParseException {
                Thread.yield();
                return Integer.toString(id);
            }
        }
    }
}