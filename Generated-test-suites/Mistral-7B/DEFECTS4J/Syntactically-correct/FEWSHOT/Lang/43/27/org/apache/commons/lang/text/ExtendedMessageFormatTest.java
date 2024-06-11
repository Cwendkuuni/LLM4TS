package org.apache.commons.lang.text;

import static org.junit.Assert.assertArrayEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertTrue;
import static org.junit.Assert.assertThrows;

import java.text.MessageFormat;
import java.text.ParsePosition;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.Map;

import org.junit.Test;

public class ExtendedMessageFormatTest {

    @Test
    public void testCreateInstance() {
        String pattern = "Hello {0,choice,0#Foo|1#Bar}";
        ExtendedMessageFormat fmt = new ExtendedMessageFormat(pattern);
        assertTrue(fmt.registry == null);
        assertEquals("", fmt.toPattern());
    }

    @Test
    public void testCreateInstanceWithRegistry() {
        String pattern = "Hello {0,choice,0#Foo|1#Bar}";
        Map<String, FormatFactory> registry = new HashMap<>();
        registry.put("Foo", (msg, arg) -> new FooFormat());
        registry.put("Bar", (msg, arg) -> new BarFormat());
        ExtendedMessageFormat fmt = new ExtendedMessageFormat(pattern, registry);
        assertTrue(fmt.registry != null);
        assertEquals("Hello {0,choice,0#Foo|1#Bar}", fmt.toPattern());
    }

    @Test
    public void testApplyPatternWithNoCustomFormats() {
        String pattern = "Hello {0,choice,0#Foo|1#Bar}";
        ExtendedMessageFormat fmt = new ExtendedMessageFormat(pattern, Collections.emptyMap());
        String result = fmt.format(new Object[]{"Foo"});
        assertEquals("Hello Foo", result);
        assertEquals("Hello {0,choice,0#Foo|1#Bar}", fmt.toPattern());
    }

    @Test
    public void testApplyPatternWithCustomFormat() {
        String pattern = "Hello {0,choice,0#Foo|1#Bar}";
        Map<String, FormatFactory> registry = new HashMap<>();
        registry.put("Foo", (msg, arg) -> new FooFormat());
        ExtendedMessageFormat fmt = new ExtendedMessageFormat(pattern, registry);
        String result = fmt.format(new Object[]{"Foo"});
        assertEquals("Hello Foo", result);
        assertEquals("Hello {0,choice,0#Foo|1#Bar}", fmt.toPattern());
    }

    @Test
    public void testApplyPatternWithInvalidFormatName() {
        String pattern = "Hello {0,choice,0#Foo|1#Bar}";
        Map<String, FormatFactory> registry = new HashMap<>();
        ExtendedMessageFormat fmt = new ExtendedMessageFormat(pattern, registry);
        assertThrows(IllegalArgumentException.class, () -> fmt.format(new Object[]{"InvalidFormat"}));
        assertEquals("Hello {0,choice,0#Foo|1#Bar}", fmt.toPattern());
    }

    @Test
    public void testApplyPatternWithMultipleCustomFormats() {
        String pattern = "Hello {0,choice,0#{Foo}|1#{Bar}}";
        Map<String, FormatFactory> registry = new HashMap<>();
        registry.put("Foo", (msg, arg) -> new FooFormat());
        registry.put("Bar", (msg, arg) -> new BarFormat());
        ExtendedMessageFormat fmt = new ExtendedMessageFormat(pattern, registry);
        String result = fmt.format(new Object[]{"Foo"});
        assertEquals("Hello Foo", result);
        assertEquals("Hello {0,choice,0#{Foo}|1#{Bar}}", fmt.toPattern());
    }

    @Test
    public void testSetFormatByArgumentIndex() {
        String pattern = "Hello {0,choice,0#Foo|1#Bar}";
        Map<String, FormatFactory> registry = new HashMap<>();
        registry.put("Foo", (msg, arg) -> new FooFormat());
        registry.put("Bar", (msg, arg) -> new BarFormat());
        ExtendedMessageFormat fmt = new ExtendedMessageFormat(pattern, registry);
        fmt.applyPattern(pattern);
        Format oldFormat = fmt.getFormats()[0];
        Format newFormat = new DifferentFormat();
        fmt.setFormatByArgumentIndex(0, newFormat);
        Format newFormat2 = fmt.getFormats()[0];
        assertFalse(Objects.equals(oldFormat, newFormat));
        assertTrue(Objects.equals(newFormat, newFormat2));
    }

    @Test
    public void testSetFormatByArgumentIndexWithInvalidIndex() {
        String pattern = "Hello {0,choice,0#Foo|1#Bar}";
        Map<String, FormatFactory> registry = new HashMap<>();
        registry.put("Foo", (msg, arg) -> new FooFormat());
        registry.put("Bar", (msg, arg) -> new BarFormat());
        ExtendedMessageFormat fmt = new ExtendedMessageFormat(pattern, registry);
        fmt.applyPattern(pattern);
        assertThrows(IndexOutOfBoundsException.class, () -> fmt.setFormatByArgumentIndex(2, new Object() {}));
    }

    @Test
    public void testSetFormatByArgumentIndexWithNullFormat() {
        String pattern = "Hello {0,choice,0#Foo|1#Bar}";
        Map<String, FormatFactory> registry = new HashMap<>();
        registry.put("Foo", (msg, arg) -> new FooFormat());
        registry.put("Bar", (msg, arg) -> new BarFormat());
        ExtendedMessageFormat fmt = new ExtendedMessageFormat(pattern, registry);
        fmt.applyPattern(pattern);
        assertThrows(NullPointerException.class, () -> fmt.setFormatByArgumentIndex(0, null));
    }

    @Test
    public void testSetFormatsByArgumentIndex() {
        String pattern = "Hello {0,choice,0#Foo|1#Bar}";
        Map<String, FormatFactory> registry = new HashMap<>();
        registry.put("Foo", (msg, arg) -> new FooFormat());
        registry.put("Bar", (msg, arg) -> new BarFormat());
        ExtendedMessageFormat fmt = new ExtendedMessageFormat(pattern, registry);
        fmt.applyPattern(pattern);
        Format[] oldFormats = fmt.getFormats();
        Format[] newFormats = {new FooFormat(), new BarFormat()};
        fmt.setFormatsByArgumentIndex(newFormats);
        Format[] newFormats2 = fmt.getFormats();
        assertArrayEquals(newFormats, newFormats2);
    }

    @Test
    public void testSetFormatsByArgumentIndexWithTooFewFormats() {
        String pattern = "Hello {0,choice,0#Foo|1#Bar}";
        Map<String, FormatFactory> registry = new HashMap<>();
        registry.put("Foo", (msg, arg) -> new FooFormat());
        registry.put("Bar", (msg, arg) -> new BarFormat());
        ExtendedMessageFormat fmt = new ExtendedMessageFormat(pattern, registry);
        fmt.applyPattern(pattern);
        assertThrows(IllegalArgumentException.class, () -> fmt.setFormatsByArgumentIndex(new Format[1]));
    }

    @Test
    public void testSetFormatsByArgumentIndexWithTooManyFormats() {
        String pattern = "Hello {0,choice,0#Foo|1#Bar}";
        Map<String, FormatFactory> registry = new HashMap<>();
        registry.put("Foo", (msg, arg) -> new FooFormat());
        registry.put("Bar", (msg, arg) -> new BarFormat());
        ExtendedMessageFormat fmt = new ExtendedMessageFormat(pattern, registry);
        fmt.applyPattern(pattern);
        assertThrows(IllegalArgumentException.class, () -> fmt.setFormatsByArgumentIndex(new Format[3]));
    }

    @Test
    public void testFormatWithCustomFormats() {
        String pattern = "Hello {0,choice,0#{Foo}|1#{Bar}}";
        Map<String, FormatFactory> registry = new HashMap<>();
        registry.put("Foo", (msg, arg) -> new FooFormat());
        registry.put("Bar", (msg, arg) -> new BarFormat());
        ExtendedMessageFormat fmt = new ExtendedMessageFormat(pattern, registry);
        fmt.applyPattern(pattern);
        String result = fmt.format(new Object[]{"Foo"});
        assertEquals("Hello Foo", result);
        String result2 = fmt.format(new Object[]{"Bar"});
        assertEquals("Hello Bar", result2);
    }

    @Test
    public void testFormatWithEmptyPattern() {
        ExtendedMessageFormat fmt = new ExtendedMessageFormat("");
        assertNull(fmt.toPattern());
    }

    @Test
    public void testFormatWithEmptyPatternAndCustomFormats() {
        Map<String, FormatFactory> registry = new HashMap<>();
        registry.put("Foo", (msg, arg) -> new FooFormat());
        ExtendedMessageFormat fmt = new ExtendedMessageFormat("", registry);
        assertNull(fmt.toPattern());
    }

    @Test
    public void testParsePosition() {
        String pattern = "Hello {0,choice,0#Foo|1#Bar}";
        ExtendedMessageFormat fmt = new ExtendedMessageFormat(pattern, Collections.emptyMap());
        ParsePosition pos = new ParsePosition(0);
        fmt.parse(new Object[]{"Foo"}, pos);
        assertEquals(pos.getIndex(), pattern.length());
        assertEquals(pos.getErrorIndex(), -1);
    }

    @Test
    public void testParsePositionWithInvalidFormatName() {
        String pattern = "Hello {0,choice,0#Foo|1#Bar}";
        ExtendedMessageFormat fmt = new ExtendedMessageFormat(pattern, Collections.emptyMap());
        ParsePosition pos = new ParsePosition(0);
        fmt.parse(new Object[]{"InvalidFormat"}, pos);
        assertEquals(pos.getIndex(), 0);
        assertEquals(pos.getErrorIndex(), 11);
    }

    @Test
    public void testParsePositionWithMissingFormatName() {
        String pattern = "Hello {0,choice,0#{}}";
        ExtendedMessageFormat fmt = new ExtendedMessageFormat(pattern, Collections.emptyMap());
        ParsePosition pos = new ParsePosition(0);
        fmt.parse(new Object[]{"Foo"}, pos);
        assertEquals(pos.getIndex(), 0);
        assertEquals(pos.getErrorIndex(), 11);
    }

    interface FormatFactory {
        Format getFormat(String name, String style, Locale locale);
    }

    static class FooFormat extends Format {
        @Override
        public String format(Object arg, Locale locale, String pattern, ParsePosition pos) {
            return "Foo";
        }

        @Override
        public Object parseObject(String source, ParsePosition pos) {
            return null;
        }
    }

    static class BarFormat extends Format {
        @Override
        public String format(Object arg, Locale locale, String pattern, ParsePosition pos) {
            return "Bar";
        }

        @Override
        public Object parseObject(String source, ParsePosition pos) {
            return null;
        }
    }

    static class DifferentFormat extends Format {
        @Override
        public String format(Object arg, Locale locale, String pattern, ParsePosition pos) {
            throw new UnsupportedOperationException();
        }

        @Override
        public Object parseObject(String source, ParsePosition pos) {
            throw new UnsupportedOperationException();
        }
    }
}