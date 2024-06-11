package org.apache.commons.lang.text;

import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertSame;
import static org.junit.Assert.assertTrue;

import java.text.MessageFormat;
import java.text.ParsePosition;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.Locale;
import java.util.Map;

import org.apache.commons.lang.Validate;
import org.junit.Test;

public class ExtendedMessageFormatTest {

    @Test
    public void testNewInstanceWithEmptyPattern() {
        ExtendedMessageFormat format = new ExtendedMessageFormat("");
        assertNull(format.getFormats());
        assertNull(format.toPattern());
    }

    @Test
    public void testNewInstanceWithPatternAndDefaultLocale() {
        ExtendedMessageFormat format = new ExtendedMessageFormat("{0} {1}");
        assertFalse(Collections.isEmpty(format.getFormats()));
        assertSame(MessageFormat.class, format.getFormats()[0].getClass());
        assertSame(MessageFormat.class, format.getFormats()[1].getClass());
        assertEquals("{0} {1}", format.toPattern());
    }

    @Test
    public void testNewInstanceWithPatternAndCustomRegistry() {
        Map<String, FormatFactory> registry = new HashMap<>();
        registry.put("customFormat", new FormatFactory() {
            @Override
            public Format getFormat(String name, String pattern, Locale locale) {
                return new SimpleFormat("Custom Format");
            }
        });

        ExtendedMessageFormat format = new ExtendedMessageFormat("{0, format=\"customFormat\"}", registry);
        assertFalse(Collections.isEmpty(format.getFormats()));
        assertSame(SimpleFormat.class, format.getFormats()[0].getClass());
        assertEquals("{0, format=\"customFormat\"}", format.toPattern());
    }

    @Test
    public void testNewInstanceWithPatternAndEmptyRegistry() {
        ExtendedMessageFormat format = new ExtendedMessageFormat("{0} {1}", Collections.emptyMap());
        assertFalse(Collections.isEmpty(format.getFormats()));
        assertSame(MessageFormat.class, format.getFormats()[0].getClass());
        assertSame(MessageFormat.class, format.getFormats()[1].getClass());
        assertEquals("{0} {1}", format.toPattern());
    }

    @Test(expected = IllegalArgumentException.class)
    public void testNewInstanceWithInvalidPattern() {
        new ExtendedMessageFormat("{0} {1} {2}"); // invalid pattern
    }

    @Test(expected = IllegalArgumentException.class)
    public void testNewInstanceWithInvalidFormatName() {
        new ExtendedMessageFormat("{0, format=\"invalidFormat\"}", Collections.emptyMap());
    }

    @Test(expected = IllegalArgumentException.class)
    public void testNewInstanceWithInvalidFormatStyle() {
        new ExtendedMessageFormat("{0, format=\"customFormat\", style=\"invalidStyle\"}", Collections.emptyMap());
    }

    @Test(expected = IllegalArgumentException.class)
    public void testNewInstanceWithInvalidFormatNameAndStyle() {
        new ExtendedMessageFormat("{0, format=\"invalidFormat\", style=\"invalidStyle\"}", Collections.emptyMap());
    }

    @Test
    public void testApplyPatternWithEmptyPattern() {
        ExtendedMessageFormat format = new ExtendedMessageFormat("");
        format.applyPattern("");
        assertNull(format.getFormats());
        assertNull(format.toPattern());
    }

    @Test
    public void testApplyPatternWithSimplePattern() {
        ExtendedMessageFormat format = new ExtendedMessageFormat("{0} {1}");
        format.applyPattern("{0} {1}");
        assertEquals("{0} {1}", format.toPattern());
        assertFalse(Collections.isEmpty(format.getFormats()));
        assertSame(MessageFormat.class, format.getFormats()[0].getClass());
        assertSame(MessageFormat.class, format.getFormats()[1].getClass());
    }

    @Test
    public void testApplyPatternWithCustomPatterns() {
        Map<String, FormatFactory> registry = new HashMap<>();
        registry.put("customFormat", new FormatFactory() {
            @Override
            public Format getFormat(String name, String pattern, Locale locale) {
                return new SimpleFormat("Custom Format");
            }
        });

        ExtendedMessageFormat format = new ExtendedMessageFormat("{0, format=\"customFormat\"}", registry);
        format.applyPattern("{0, format=\"customFormat\"}");
        assertEquals("{0, format=\"customFormat\"}", format.toPattern());
        assertFalse(Collections.isEmpty(format.getFormats()));
        assertSame(SimpleFormat.class, format.getFormats()[0].getClass());
    }

    @Test(expected = IllegalArgumentException.class)
    public void testApplyPatternWithInvalidPattern() {
        ExtendedMessageFormat format = new ExtendedMessageFormat("{0} {1}");
        format.applyPattern("{0} {1} {2}"); // invalid pattern
    }

    @Test(expected = IllegalArgumentException.class)
    public void testApplyPatternWithInvalidFormatName() {
        Map<String, FormatFactory> registry = new HashMap<>();
        registry.put("customFormat", new FormatFactory() {
            @Override
            public Format getFormat(String name, String pattern, Locale locale) {
                return new SimpleFormat("Custom Format");
            }
        });

        ExtendedMessageFormat format = new ExtendedMessageFormat("{0, format=\"customFormat\"}", registry);
        format.applyPattern("{0, format=\"invalidFormat\"}");
    }

    @Test(expected = IllegalArgumentException.class)
    public void testApplyPatternWithInvalidFormatStyle() {
        Map<String, FormatFactory> registry = new HashMap<>();
        registry.put("customFormat", new FormatFactory() {
            @Override
            public Format getFormat(String name, String pattern, Locale locale) {
                return new SimpleFormat("Custom Format");
            }
        });

        ExtendedMessageFormat format = new ExtendedMessageFormat("{0, format=\"customFormat\", style=\"invalidStyle\"}", registry);
        format.applyPattern("{0, format=\"customFormat\", style=\"invalidStyle\"}");
    }

    @Test(expected = IllegalArgumentException.class)
    public void testApplyPatternWithInvalidFormatNameAndStyle() {
        Map<String, FormatFactory> registry = new HashMap<>();
        registry.put("customFormat", new FormatFactory() {
            @Override
            public Format getFormat(String name, String pattern, Locale locale) {
                return new SimpleFormat("Custom Format");
            }
        });

        ExtendedMessageFormat format = new ExtendedMessageFormat("{0, format=\"customFormat\", style=\"invalidStyle\"}", registry);
        format.applyPattern("{0, format=\"invalidFormat\", style=\"invalidStyle\"}");
    }

    @Test
    public void testSetFormat() {
        Map<String, FormatFactory> registry = new HashMap<>();
        registry.put("customFormat", new FormatFactory() {
            @Override
            public Format getFormat(String name, String pattern, Locale locale) {
                return new SimpleFormat("Custom Format");
            }
        });

        ExtendedMessageFormat format = new ExtendedMessageFormat("{0, format=\"customFormat\"}", registry);
        format.applyPattern("{0, format=\"customFormat\"}");
        Format customFormat = format.getFormats()[0];
        Format newFormat = new SimpleFormat("New Format");
        format.setFormat(0, newFormat);
        assertFalse(format.getFormats()[0].equals(customFormat));
        assertTrue(format.getFormats()[0].equals(newFormat));
        assertEquals("{0, format=\"customFormat\"}", format.toPattern());
    }

    @Test(expected = UnsupportedOperationException.class)
    public void testSetFormatByArgumentIndex() {
        ExtendedMessageFormat format = new ExtendedMessageFormat("{0} {1}");
        format.setFormatByArgumentIndex(0, new SimpleFormat("New Format"));
    }

    @Test(expected = UnsupportedOperationException.class)
    public void testSetFormats() {
        ExtendedMessageFormat format = new ExtendedMessageFormat("{0} {1}");
        Format[] newFormats = {new SimpleFormat("New Format 1"), new SimpleFormat("New Format 2")};
        format.setFormats(newFormats);
    }

    @Test(expected = UnsupportedOperationException.class)
    public void testSetFormatsByArgumentIndex() {
        ExtendedMessageFormat format = new ExtendedMessageFormat("{0} {1}");
        Format[] newFormats = {new SimpleFormat("New Format 1"), new SimpleFormat("New Format 2")};
        format.setFormatsByArgumentIndex(newFormats);
    }

    @Test
    public void testToString() {
        ExtendedMessageFormat format = new ExtendedMessageFormat("{0} {1}", Collections.emptyMap());
        assertEquals("ExtendedMessageFormat[pattern={0} {1}, arguments={}, formats={}]", format.toString());
    }

    private static class SimpleFormat implements Format {
        private final String formatName;

        public SimpleFormat(String formatName) {
            this.formatName = formatName;
        }

        @Override
        public String format(Object obj, Locale locale, String format, ParsePosition parsePosition) {
            return formatName;
        }

        @Override
        public Object parseObject(String source, ParsePosition parsePosition) {
            throw new UnsupportedOperationException();
        }
    }

    private static class FormatFactory {
        public Format getFormat(String name, String pattern, Locale locale) {
            return new SimpleFormat(name);
        }
    }
}