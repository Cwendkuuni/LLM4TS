package org.apache.commons.lang.text;

import static org.junit.Assert.assertArrayEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertSame;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertThrows;
import static org.junit.Assert.assertTrue;

import java.text.ParseException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Locale;
import java.util.Map;

import org.apache.commons.cli.FormatFactory;
import org.apache.commons.lang.Validate;

public class ExtendedMessageFormatTest {

    @Test
    public void testNewInstance() {
        ExtendedMessageFormat fmt = new ExtendedMessageFormat("{0}");
        assertEquals("{0}", fmt.toPattern());
    }

    @Test
    public void testNewInstanceWithLocale() {
        ExtendedMessageFormat fmt = new ExtendedMessageFormat("{0}", Locale.US);
        assertEquals("{0}", fmt.toPattern());
    }

    @Test
    public void testNewInstanceWithRegistry() {
        Map<String, FormatFactory> registry = new HashMap<>();
        registry.put("myFormat", new MyFormatFactory());
        ExtendedMessageFormat fmt = new ExtendedMessageFormat("{myFormat,date,yyyy-MM-dd}", registry);
        assertEquals("{myFormat,date,yyyy-MM-dd}", fmt.toPattern());
    }

    @Test
    public void testApplyPattern() {
        String pattern = "{0} {1,choice,0#foo|1#bar}";
        ExtendedMessageFormat fmt = new ExtendedMessageFormat(pattern);
        Object[] args = new Object[]{"hello", 1};
        String result = fmt.format(args);
        assertEquals("hello bar", result);
    }

    @Test
    public void testApplyPatternWithRegistry() {
        Map<String, FormatFactory> registry = new HashMap<>();
        registry.put("myFormat", new MyFormatFactory());
        ExtendedMessageFormat fmt = new ExtendedMessageFormat("{myFormat,choice,0#foo|1#bar}", registry);
        Object[] args = new Object[]{"hello", 1};
        String result = fmt.format(args);
        assertEquals("hello bar", result);
    }

    @Test
    public void testSetLocale() {
        ExtendedMessageFormat fmt = new ExtendedMessageFormat("{0}", Locale.US);
        fmt.setLocale(Locale.UK);
        assertEquals(Locale.UK, fmt.getLocale());
    }

    @Test
    public void testSetFormat() {
        MyFormatFactory factory = new MyFormatFactory();
        Map<String, FormatFactory> registry = new HashMap<>();
        registry.put("myFormat", factory);
        ExtendedMessageFormat fmt = new ExtendedMessageFormat("{myFormat}", registry);
        Format expectedFormat = factory.getFormat("myFormat", null, Locale.getDefault());
        fmt.setFormat(0, expectedFormat);
        Format resultFormat = fmt.getFormat(0);
        assertSame(expectedFormat, resultFormat);
    }

    @Test
    public void testSetFormatByArgumentIndex() {
        MyFormatFactory factory = new MyFormatFactory();
        Map<String, FormatFactory> registry = new HashMap<>();
        registry.put("myFormat", factory);
        ExtendedMessageFormat fmt = new ExtendedMessageFormat("{myFormat}", registry);
        Format expectedFormat = factory.getFormat("myFormat", null, Locale.getDefault());
        fmt.setFormatByArgumentIndex(0, expectedFormat);
        Format resultFormat = fmt.getFormat(0);
        assertSame(expectedFormat, resultFormat);
    }

    @Test
    public void testSetFormats() {
        ThrowingFormatFactory factory = new ThrowingFormatFactory();
        Map<String, FormatFactory> registry = new HashMap<>();
        registry.put("myFormat", factory);
        ExtendedMessageFormat fmt = new ExtendedMessageFormat("{myFormat}", registry);
        Format[] expectedFormats = new Format[]{factory.getFormat("myFormat", null, Locale.getDefault())};
        Format[] resultFormats = fmt.getFormats();
        assertArrayEquals(expectedFormats, resultFormats);
    }

    @Test
    public void testSetFormatsByArgumentIndex() {
        ThrowingFormatFactory factory = new ThrowingFormatFactory();
        Map<String, FormatFactory> registry = new HashMap<>();
        registry.put("myFormat", factory);
        ExtendedMessageFormat fmt = new ExtendedMessageFormat("{myFormat}", registry);
        Format[] expectedFormats = new Format[]{factory.getFormat("myFormat", null, Locale.getDefault())};
        Format[] resultFormats = fmt.getFormatsByArgumentIndex(new int[]{0});
        assertArrayEquals(expectedFormats, resultFormats);
    }

    @Test
    public void testSetFormatsWithInvalidIndex() {
        ThrowingFormatFactory factory = new ThrowingFormatFactory();
        Map<String, FormatFactory> registry = new HashMap<>();
        registry.put("myFormat", factory);
        ExtendedMessageFormat fmt = new ExtendedMessageFormat("{myFormat}", registry);
        assertThrows(UnsupportedOperationException.class, () -> {
            fmt.setFormatsByArgumentIndex(new int[]{1});
        });
    }

    @Test
    public void testFormat() {
        MyFormatFactory factory = new MyFormatFactory();
        Map<String, FormatFactory> registry = new HashMap<>();
        registry.put("myFormat", factory);
        ExtendedMessageFormat fmt = new ExtendedMessageFormat("{myFormat}", registry);
        Object[] args = new Object[]{"hello", 1};
        String result = fmt.format(args);
        assertEquals("hello bar", result);
    }

    @Test
    public void testFormatWithInvalidArgumentIndex() {
        MyFormatFactory factory = new MyFormatFactory();
        Map<String, FormatFactory> registry = new HashMap<>();
        registry.put("myFormat", factory);
        ExtendedMessageFormat fmt = new ExtendedMessageFormat("{myFormat}", registry);
        Object[] args = new Object[]{"hello"};
        assertThrows(IllegalArgumentException.class, () -> {
            fmt.format(args);
        });
    }

    @Test
    public void testFormatWithInvalidFormatIndex() {
        MyFormatFactory factory = new MyFormatFactory();
        Map<String, FormatFactory> registry = new HashMap<>();
        registry.put("myFormat", factory);
        ExtendedMessageFormat fmt = new ExtendedMessageFormat("{myFormat}", registry);
        Object[] args = new Object[]{"hello", 1};
        assertThrows(IllegalArgumentException.class, () -> {
            fmt.format(args, 1);
        });
    }

    @Test
    public void testParse() throws ParseException {
        MyFormatFactory factory = new MyFormatFactory();
        Map<String, FormatFactory> registry = new HashMap<>();
        registry.put("myFormat", factory);
        ExtendedMessageFormat fmt = new ExtendedMessageFormat("{myFormat}", registry);
        Object[] args = new Object[]{"hello", 1};
        ParsePosition pos = new ParsePosition(0);
        fmt.parseObject(new StringReader("hello {myFormat,date,yyyy-MM-dd} bar"), pos, args);
        assertEquals("hello 2023-03-23 bar", pos.getErrorMessage());
        assertEquals("hello Thu Mar 23 00:00:00 CET 2023 bar", fmt.format(args));
    }

    @Test
    public void testParseWithInvalidFormatIndex() throws ParseException {
        MyFormatFactory factory = new MyFormatFactory();
        Map<String, FormatFactory> registry = new HashMap<>();
        registry.put("myFormat", factory);
        ExtendedMessageFormat fmt = new ExtendedMessageFormat("{myFormat}", registry);
        Object[] args = new Object[]{"hello", 1};
        ParsePosition pos = new ParsePosition(0);
        assertThrows(ParseException.class, () -> {
            fmt.parseObject(new StringReader("hello {myFormat,date,yyyy-MM-dd} bar"), pos, args);
        });
    }

    @Test
    public void testParseWithInvalidLocale() throws ParseException {
        MyFormatFactory factory = new MyFormatFactory();
        Map<String, FormatFactory> registry = new HashMap<>();
        registry.put("myFormat", factory);
        ExtendedMessageFormat fmt = new ExtendedMessageFormat("{myFormat}", registry);
        Object[] args = new Object[]{"hello", 1};
        ParsePosition pos = new ParsePosition(0);
        assertThrows(ParseException.class, () -> {
            fmt.parseObject(new StringReader("hello {myFormat,date,CCCC} bar"), pos, args);
        });
    }

    @Test
    public void testParseWithInvalidPattern() throws ParseException {
        MyFormatFactory factory = new MyFormatFactory();
        Map<String, FormatFactory> registry = new HashMap<>();
        registry.put("myFormat", factory);
        ExtendedMessageFormat fmt = new ExtendedMessageFormat("{myFormat}", registry);
        Object[] args = new Object[]{"hello", 1};
        ParsePosition pos = new ParsePosition(0);
        assertThrows(IllegalArgumentException.class, () -> {
            fmt.parseObject(new StringReader("hello {myFormat} bar"), pos, args);
        });
    }

    @Test
    public void testToPattern() {
        MyFormatFactory factory = new MyFormatFactory();
        Map<String, FormatFactory> registry = new HashMap<>();
        registry.put("myFormat", factory);
        ExtendedMessageFormat fmt = new ExtendedMessageFormat("{myFormat}", registry);
        assertEquals("{myFormat}", fmt.toPattern());
    }

    @Test
    public void testToPatternWithRegistry() {
        MyFormatFactory factory = new MyFormatFactory();
        Map<String, FormatFactory> registry = new HashMap<>();
        registry.put("myFormat", factory);
        ExtendedMessageFormat fmt = new ExtendedMessageFormat("{myFormat}", registry);
        assertEquals("{myFormat}", fmt.toPattern());
    }

    @Test
    public void testToPatternWithCustomFormat() {
        MyFormatFactory factory = new MyFormatFactory();
        Map<String, FormatFactory> registry = new HashMap<>();
        registry.put("myFormat", factory);
        ExtendedMessageFormat fmt = new ExtendedMessageFormat("{myFormat,date,yyyy-MM-dd}", registry);
        assertEquals("{myFormat,date,yyyy-MM-dd}", fmt.toPattern());
    }

    @Test
    public void testToPatternWithCustomFormatAndStyle() {
        MyFormatFactory factory = new MyFormatFactory();
        Map<String, FormatFactory> registry = new HashMap<>();
        registry.put("myFormat", factory);
        ExtendedMessageFormat fmt = new ExtendedMessageFormat("{myFormat,date,full,yyyy-MM-dd}", registry);
        assertEquals("{myFormat,date,full,yyyy-MM-dd}", fmt.toPattern());
    }

    @Test
    public void testToString() {
        ExtendedMessageFormat fmt = new ExtendedMessageFormat("{0} {1,choice,0#foo|1#bar}");
        String expected = "ExtendedMessageFormat[pattern=#{0} {1,choice,0#foo|1#bar}, arguments={}, locale=java.util.Locale[language=, country=, variant=], format={}, bundle={}]";
        String result = fmt.toString();
        assertEquals(expected, result);
    }

    @Test
    public void testHashCode() {
        ExtendedMessageFormat fmt1 = new ExtendedMessageFormat("{0} {1,choice,0#foo|1#bar}");
        ExtendedMessageFormat fmt2 = new ExtendedMessageFormat("{0} {1,choice,0#foo|1#bar}");
        int expected = fmt1.hashCode();
        int result = fmt2.hashCode();
        assertEquals(expected, result);
    }

    @Test
    public void testEquals() {
        ExtendedMessageFormat fmt1 = new ExtendedMessageFormat("{0} {1,choice,0#foo|1#bar}");
        ExtendedMessageFormat fmt2 = new ExtendedMessageFormat("{0} {1,choice,0#foo|1#bar}");
        boolean expected = true;
        boolean result = fmt1.equals(fmt2);
        assertEquals(expected, result);
    }

    @Test
    public void testEqualsWithNull() {
        ExtendedMessageFormat fmt = new ExtendedMessageFormat("{0} {1,choice,0#foo|1#bar}");
        boolean expected = false;
        boolean result = fmt.equals(null);
        assertEquals(expected, result);
    }

    @Test
    public void testEqualsWithDifferentClass() {
        ExtendedMessageFormat fmt = new ExtendedMessageFormat("{0} {1,choice,0#foo|1#bar}");
        Object obj = new Object();
        boolean expected = false;
        boolean result = fmt.equals(obj);
        assertEquals(expected, result);
    }

    @Test
    public void testEqualsWithSamePatternButDifferentLocale() {
        ExtendedMessageFormat fmt1 = new ExtendedMessageFormat("{0} {1,choice,0#foo|1#bar}", Locale.US);
        ExtendedMessageFormat fmt2 = new ExtendedMessageFormat("{0} {1,choice,0#foo|1#bar}", Locale.UK);
        boolean expected = false;
        boolean result = fmt1.equals(fmt2);
        assertEquals(expected, result);
    }

    @Test
    public void testEqualsWithDifferentPattern() {
        ExtendedMessageFormat fmt1 = new ExtendedMessageFormat("{0} {1,choice,0#foo|1#bar}");
        ExtendedMessageFormat fmt2 = new ExtendedMessageFormat("{0} {1,choice,0#bar|1#foo}");
        boolean expected = false;
        boolean result = fmt1.equals(fmt2);
        assertEquals(expected, result);
    }

    @Test
    public void testClone() {
        ExtendedMessageFormat fmt = new ExtendedMessageFormat("{0} {1,choice,0#foo|1#bar}");
        ExtendedMessageFormat clone = (ExtendedMessageFormat) fmt.clone();
        assertEquals(fmt.toPattern(), clone.toPattern());
    }

    private static class MyFormatFactory implements FormatFactory {

        @Override
        public Format getFormat(String name, String arguments, Locale locale) {
            if ("myFormat".equals(name)) {
                return new MyFormat();
            } else {
                throw new IllegalArgumentException("Unsupported format: " + name);
            }
        }

    }

    private static class MyFormat extends Format {

        @Override
        public Object parseObject(String source, ParsePosition pos) {
            return pos.getIndex() == -1 ? null : Integer.valueOf(pos.getIndex());
        }

        @Override
        public String format(Object obj, Locale locale, String format, Object[] args, FieldPosition pos) {
            int number = ((Integer) obj).intValue();
            return String.valueOf(number + 1);
        }

        @Override
        public String format(Object obj, Locale locale) {
            throw new UnsupportedOperationException("format(Object, Locale) is not supported");
        }

        @Override
        public Class[] getParameterTypes() {
            return new Class[]{Object.class, Locale.class};
        }

        @Override
        public String getPattern() {
            return "";
        }

    }

}

class StringReader extends CharArrayReader {

    StringReader(char[] buffer) {
        super(buffer, 0, buffer.length);
    }

}