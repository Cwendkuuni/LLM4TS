package org.apache.commons.lang.text;

import static org.junit.Assert.assertArrayEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertTrue;

import java.text.MessageFormat;
import java.text.ParsePosition;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.Locale;
import java.util.Map;

import org.apache.commons.cli.Util;
import org.apache.commons.lang.text.ExtendedMessageFormat;

public class ExtendedMessageFormatTest {

    @Test
    public void testCreateDefault() {
        ExtendedMessageFormat format = new ExtendedMessageFormat("{0}");
        assertNotNull(format);
        assertEquals("", format.toPattern());
    }

    @Test
    public void testCreateLocaleSpecified() {
        ExtendedMessageFormat format = new ExtendedMessageFormat("{0}", Locale.US);
        assertNotNull(format);
        assertEquals("", format.toPattern());
    }

    @Test
    public void testCreateRegistrySpecified() {
        Map<String, FormatFactory> registry = new HashMap<>();
        registry.put("MyFormat", new FormatFactory() {
            @Override
            public Format getFormat(String name, String arguments, Locale locale) {
                return new MyFormat();
            }
        });

        ExtendedMessageFormat format = new ExtendedMessageFormat("{0, format=MyFormat}", registry);
        assertNotNull(format);
        assertEquals("{0, format=MyFormat}", format.toPattern());
    }

    @Test
    public void testApplyPattern() {
        String pattern = "{0, format=MyFormat} {1}";
        ExtendedMessageFormat format = new ExtendedMessageFormat(pattern, null);

        assertNull(format.getFormats());
        assertEquals(pattern, format.toPattern());

        format.applyPattern(pattern);
        Format[] formats = format.getFormats();
        assertNotNull(formats);
        assertEquals(1, formats.length);
        assertTrue(formats[0] instanceof MyFormat);
        assertEquals("{0, format=MyFormat}", format.toPattern());
    }

    @Test(expected = IllegalArgumentException.class)
    public void testApplyPatternInvalidPattern() {
        String pattern = "{0, format=MyFormat} {1} {2}";
        ExtendedMessageFormat format = new ExtendedMessageFormat(pattern, null);
        format.applyPattern(pattern);
    }

    @Test
    public void testFormat() {
        String pattern = "{0, format=MyFormat} {1}";
        ExtendedMessageFormat format = new ExtendedMessageFormat(pattern, null);
        format.applyPattern(pattern);

        format.format(new Object[]{"foo", "bar"});
        String result = format.toPattern();
        assertEquals("{0, format=MyFormat} {1}", result);

        String formatted = format.format(new Object[]{"foo", "bar"});
        assertEquals("foo bar", formatted);
    }

    @Test
    public void testFormatWithEmptyRegistry() {
        ExtendedMessageFormat format = new ExtendedMessageFormat("{0, format=MyFormat}", Collections.emptyMap());
        format.applyPattern("{0, format=MyFormat} {1}");

        try {
            format.format(new Object[]{"foo", "bar"});
            fail("Expected UnsupportedOperationException");
        } catch (UnsupportedOperationException e) {
            // expected
        }
    }

    @Test
    public void testFormatWithNullRegistry() {
        ExtendedMessageFormat format = new ExtendedMessageFormat("{0, format=MyFormat}", null);
        format.applyPattern("{0, format=MyFormat} {1}");

        try {
            format.format(new Object[]{"foo", "bar"});
            fail("Expected UnsupportedOperationException");
        } catch (UnsupportedOperationException e) {
            // expected
        }
    }

    @Test
    public void testFormatWithFormatNotFound() {
        Map<String, FormatFactory> registry = new HashMap<>();
        registry.put("MyFormat1", new FormatFactory() {
            @Override
            public Format getFormat(String name, String arguments, Locale locale) {
                return new MyFormat1();
            }
        });

        String pattern = "{0, format=MyFormat2} {1}";
        ExtendedMessageFormat format = new ExtendedMessageFormat(pattern, registry);
        format.applyPattern(pattern);

        try {
            format.format(new Object[]{"foo", "bar"});
            fail("Expected UnsupportedOperationException");
        } catch (UnsupportedOperationException e) {
            // expected
        }
    }

    @Test
    public void testFormatWithFormatArgument() {
        Map<String, FormatFactory> registry = new HashMap<>();
        registry.put("MyFormat", new FormatFactory() {
            @Override
            public Format getFormat(String name, String arguments, Locale locale) {
                return new MyFormat();
            }
        });

        String pattern = "{0, format=MyFormat, number, decimalFormat=#.##} {1}";
        ExtendedMessageFormat format = new ExtendedMessageFormat(pattern, registry);
        format.applyPattern(pattern);

        Object[] args = new Object[]{123.45f, "bar"};
        String formatted = format.format(args);
        assertEquals("123.45 bar", formatted);
    }

    @Test
    public void testFormatWithFormatStyleArgument() {
        Map<String, FormatFactory> registry = new HashMap<>();
        registry.put("MyFormat", new FormatFactory() {
            @Override
            public Format getFormat(String name, String arguments, Locale locale) {
                return new MyFormat();
            }
        });

        String pattern = "{0, format=MyFormat, format=upperCase} {1}";
        ExtendedMessageFormat format = new ExtendedMessageFormat(pattern, registry);
        format.applyPattern(pattern);

        Object[] args = new Object[]{"foo", "bar"};
        String formatted = format.format(args);
        assertEquals("FOO bar", formatted);
    }

    @Test
    public void testFormatWithFormatAndStyleArgument() {
        Map<String, FormatFactory> registry = new HashMap<>();
        registry.put("MyFormat", new FormatFactory() {
            @Override
            public Format getFormat(String name, String arguments, Locale locale) {
                return new MyFormat();
            }
        });

        String pattern = "{0, format=MyFormat, format=upperCase, number, decimalFormat=#} {1}";
        ExtendedMessageFormat format = new ExtendedMessageFormat(pattern, registry);
        format.applyPattern(pattern);

        Object[] args = new Object[]{123, "bar"};
        String formatted = format.format(args);
        assertEquals("FOO 123 bar", formatted);
    }

    @Test
    public void testFormatWithMultipleFormats() {
        Map<String, FormatFactory> registry = new HashMap<>();
        registry.put("MyFormat1", new FormatFactory() {
            @Override
            public Format getFormat(String name, String arguments, Locale locale) {
                return new MyFormat1();
            }
        });
        registry.put("MyFormat2", new FormatFactory() {
            @Override
            public Format getFormat(String name, String arguments, Locale locale) {
                return new MyFormat2();
            }
        });

        String pattern = "{0, format=MyFormat1} {1, format=MyFormat2}";
        ExtendedMessageFormat format = new ExtendedMessageFormat(pattern, registry);
        format.applyPattern(pattern);

        Object[] args = new Object[]{"foo", "bar"};
        String formatted = format.format(args);
        assertEquals("foob ar", formatted);
    }

    private static class MyFormat implements Format {
        @Override
        public StringBuffer format(Object obj, StringBuffer to, Format formatter, Locale locale) {
            to.append("MyFormat");
            return to;
        }

        @Override
        public Object parseObject(String source, ParsePosition pos) {
            pos.setIndex(pos.getIndex() + 7);
            return null;
        }

        @Override
        public String pattern() {
            return null;
        }
    }

    private static class MyFormat1 implements Format {
        @Override
        public StringBuffer format(Object obj, StringBuffer to, Format formatter, Locale locale) {
            to.append("MyFormat1");
            return to;
        }

        @Override
        public Object parseObject(String source, ParsePosition pos) {
            pos.setIndex(pos.getIndex() + 7);
            return null;
        }

        @Override
        public String pattern() {
            return null;
        }
    }

    private static class MyFormat2 implements Format {
        @Override
        public StringBuffer format(Object obj, StringBuffer to, Format formatter, Locale locale) {
            to.append("MyFormat2");
            return to;
        }

        @Override
        public Object parseObject(String source, ParsePosition pos) {
            pos.setIndex(pos.getIndex() + 7);
            return null;
        }

        @Override
        public String pattern() {
            return null;
        }
    }

    private static class FormatFactory {
        public Format getFormat(String name, String arguments, Locale locale) {
            return null;
        }
    }
}