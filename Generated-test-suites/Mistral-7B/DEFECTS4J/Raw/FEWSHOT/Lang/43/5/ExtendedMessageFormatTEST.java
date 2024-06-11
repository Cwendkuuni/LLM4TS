package org.apache.commons.lang.text;

import static org.junit.Assert.assertArrayEquals;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;

import java.text.MessageFormat;
import java.text.ParsePosition;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.commons.cli.util.DummyFormatFactory;
import org.apache.commons.lang3.StringUtils;
import org.junit.Test;

public class ExtendedMessageFormatTest {

    @Test
    public void testCreateFormatWithRegistry() {
        String pattern = "{0,date,EEE MMM d, ''yy} {1} {2}";
        ExtendedMessageFormat formatter = new ExtendedMessageFormat(pattern,
                new HashMap<String, DummyFormatFactory>() {{
                    put("date", new DummyFormatFactory());
                }});
        assertNotNull(formatter.getRegistry());
        assertEquals(1, formatter.getRegistry().size());
        assertEquals("date", formatter.getRegistry().keySet().iterator().next());
    }

    @Test
    public void testCreateFormatWithDefaultRegistry() {
        String pattern = "{0,date,EEE MMM d, ''yy} {1} {2}";
        ExtendedMessageFormat formatter = new ExtendedMessageFormat(pattern);
        assertNull(formatter.getRegistry());
    }

    @Test
    public void testApplyPatternWithEmptyPattern() {
        ExtendedMessageFormat formatter = new ExtendedMessageFormat( "");
        assertEquals("", formatter.toPattern());
    }

    @Test(expected = IllegalArgumentException.class)
    public void testApplyPatternWithInvalidPattern1() {
        ExtendedMessageFormat formatter = new ExtendedMessageFormat( "{}" );
    }

    @Test(expected = IllegalArgumentException.class)
    public void testApplyPatternWithInvalidPattern2() {
        ExtendedMessageFormat formatter = new ExtendedMessageFormat( "foo{}" );
    }

    @Test(expected = IllegalArgumentException.class)
    public void testApplyPatternWithInvalidPattern3() {
        ExtendedMessageFormat formatter = new ExtendedMessageFormat( "{}foo" );
    }

    @Test
    public void testApplyPattern() {
        String pattern = "{0} {1} {2}";
        ExtendedMessageFormat formatter = new ExtendedMessageFormat(pattern, Locale.ENGLISH);

        Object[] args = {"Hello", "world", "!"};
        String formatted = formatter.format(args);
        assertEquals("Hello world !", formatted);

        String expectedPattern = "{0}{1}{2}";
        String actualPattern = formatter.toPattern();
        assertEquals(expectedPattern, actualPattern);
    }

    @Test
    public void testApplyPatternWithCustomFormats() {
        String pattern = "{0,customDate} {1} {2,customNumber}";
        ExtendedMessageFormat formatter = new ExtendedMessageFormat(pattern, Locale.ENGLISH, Collections.singletonMap("customDate", new DummyDateFormatFactory()));

        Object[] args = {"2023-03-25", "world", "3.14"};
        String formatted = formatter.format(args);
        assertEquals("25-Mar-23 world 3.14", formatted);

        String expectedPattern = "{0,customDate}{1}{2,customNumber}";
        String actualPattern = formatter.toPattern();
        assertEquals(expectedPattern, actualPattern);
    }

    @Test
    public void testSetFormatByArgumentIndex() {
        ExtendedMessageFormat formatter = new ExtendedMessageFormat("{0} {1} {2}", Locale.ENGLISH);

        try {
            formatter.setFormatByArgumentIndex(1, new DummyFormat());
            fail("Expected UnsupportedOperationException");
        } catch (UnsupportedOperationException e) {
            // expected
        }
    }

    @Test
    public void testSetFormat() {
        ExtendedMessageFormat formatter = new ExtendedMessageFormat("{0} {1} {2}", Locale.ENGLISH);

        try {
            formatter.setFormat(1, new DummyFormat());
            fail("Expected UnsupportedOperationException");
        } catch (UnsupportedOperationException e) {
            // expected
        }
    }

    @Test
    public void testSetFormatsByArgumentIndex() {
        ExtendedMessageFormat formatter = new ExtendedMessageFormat("{0} {1} {2}", Locale.ENGLISH);

        try {
            formatter.setFormatsByArgumentIndex(new DummyFormat[3]);
            fail("Expected UnsupportedOperationException");
        } catch (UnsupportedOperationException e) {
            // expected
        }
    }

    @Test
    public void testSetFormats() {
        ExtendedMessageFormat formatter = new ExtendedMessageFormat("{0} {1} {2}", Locale.ENGLISH);

        try {
            formatter.setFormats(new DummyFormat[3]);
            fail("Expected UnsupportedOperationException");
        } catch (UnsupportedOperationException e) {
            // expected
        }
    }

    @Test
    public void testFormatWithFormats() {
        String pattern = "{0,format1} {1,format2} {2,format3}";
        ExtendedMessageFormat formatter = new ExtendedMessageFormat(pattern, Locale.ENGLISH);

        Object[] args = {"Hello", "world", "!"};
        Object[] formats = {new DummyFormat(), new DummyFormat(), null};

        String formatted = formatter.format(args, formats);
        assertEquals("Hello world !", formatted);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testFormatWithNullArgument() {
        ExtendedMessageFormat formatter = new ExtendedMessageFormat("{0}", Locale.ENGLISH);
        Object[] args = {null};
        formatter.format(args);
    }

    @Test
    public void testParse() {
        String pattern = "{0,date,EEE MMM d, ''yy} {1} {2,number,#,0.##}";
        ExtendedMessageFormat formatter = new ExtendedMessageFormat(pattern, Locale.ENGLISH);

        Object[] args = {"2023-03-25", "world", "3.14"};
        Object[] parsedArgs = formatter.parse(args);

        assertEquals(3, parsedArgs.length);
        assertEquals("25-Mar-23", parsedArgs[0]);
        assertEquals("world", parsedArgs[1]);
        assertEquals(3.14, parsedArgs[2]);
    }

    @Test(expected = ParseException.class)
    public void testParseWithInvalidFormat() {
        String pattern = "{0,date,EEE MMM d, ''yy} {1} {2,foo}";
        ExtendedMessageFormat formatter = new ExtendedMessageFormat(pattern, Locale.ENGLISH);

        Object[] args = {"2023-03-25", "world", "3.14"};
        formatter.parse(args);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testParseWithNullArgs() {
        String pattern = "{0} {1} {2}";
        ExtendedMessageFormat formatter = new ExtendedMessageFormat(pattern, Locale.ENGLISH);

        Object[] parsedArgs = formatter.parse(null);
    }

    @Test
    public void testFormatWithNullFormats() {
        String pattern = "{0} {1} {2}";
        ExtendedMessageFormat formatter = new ExtendedMessageFormat(pattern, Locale.ENGLISH);

        Object[] args = {"Hello", "world", "!"};
        String formatted = formatter.format(args, (Object[]) null);
        assertEquals("Hello world !", formatted);
    }

    @Test
    public void testFormatWithEmptyFormats() {
        String pattern = "{0} {1} {2}";
        ExtendedMessageFormat formatter = new ExtendedMessageFormat(pattern, Locale.ENGLISH);

        Object[] args = {"Hello", "world", "!"};
        String formatted = formatter.format(args, new Object[3]);
        assertEquals("Hello world !", formatted);
    }

    @Test
    public void testFormatWithFormatsLessThanArguments() {
        String pattern = "{0} {1} {2}";
        ExtendedMessageFormat formatter = new ExtendedMessageFormat(pattern, Locale.ENGLISH);

        Object[] args = {"Hello", "world", "!"};
        Object[] formats = new Object[2];

        String formatted = formatter.format(args, formats);
        assertEquals("Hello world !", formatted);
    }

    @Test
    public void testFormatWithFormatsMoreThanArguments() {
        String pattern = "{0} {1} {2}";
        ExtendedMessageFormat formatter = new ExtendedMessageFormat(pattern, Locale.ENGLISH);

        Object[] args = {"Hello", "world"};
        Object[] formats = {new DummyFormat(), new DummyFormat(), new DummyFormat()};

        String formatted = formatter.format(args, formats);
        assertEquals("Hello world", formatted);
    }

    @Test
    public void testFormatWithFormatsContainingNull() {
        String pattern = "{0} {1} {2}";
        ExtendedMessageFormat formatter = new ExtendedMessageFormat(pattern, Locale.ENGLISH);

        Object[] args = {"Hello", "world"};
        Object[] formats = {null, new DummyFormat(), new DummyFormat()};

        try {
            formatter.format(args, formats);
            fail("Expected IllegalArgumentException");
        } catch (IllegalArgumentException e) {
            // expected
        }
    }

    @Test
    public void testFormatWithFormatsContainingNonFormats() {
        String pattern = "{0} {1} {2}";
        ExtendedMessageFormat formatter = new ExtendedMessageFormat(pattern, Locale.ENGLISH);

        Object[] args = {"Hello", "world"};
        Object[] formats = {"Hello", new DummyFormat(), new DummyFormat()};

        try {
            formatter.format(args, formats);
            fail("Expected IllegalArgumentException");
        } catch (IllegalArgumentException e) {
            // expected
        }
    }

    @Test
    public void testParseWithFormatsLessThanArguments() {
        String pattern = "{0} {1} {2}";
        ExtendedMessageFormat formatter = new ExtendedMessageFormat(pattern, Locale.ENGLISH);

        Object[] args = {"Hello", "world", "!"};
        Object[] parsedArgs = formatter.parse(args, new Object[2]);

        assertEquals(3, parsedArgs.length);
        assertEquals("Hello", parsedArgs[0]);
        assertEquals("world", parsedArgs[1]);
        assertEquals("!", parsedArgs[2]);
    }

    @Test
    public void testParseWithFormatsMoreThanArguments() {
        String pattern = "{0} {1} {2}";
        ExtendedMessageFormat formatter = new ExtendedMessageFormat(pattern, Locale.ENGLISH);

        Object[] args = {"Hello", "world"};
        Object[] parsedArgs = formatter.parse(args, new Object[4]);

        assertEquals(3, parsedArgs.length);
        assertEquals("Hello", parsedArgs[0]);
        assertEquals("world", parsedArgs[1]);
        assertNull(parsedArgs[2]);
    }

    @Test
    public void testParseWithFormatsContainingNull() {
        String pattern = "{0} {1} {2}";
        ExtendedMessageFormat formatter = new ExtendedMessageFormat(pattern, Locale.ENGLISH);

        Object[] args = {"Hello", "world", "!"};
        Object[] parsedArgs = formatter.parse(args, new Object[] {null, null, null});

        assertEquals(3, parsedArgs.length);
        assertEquals("Hello", parsedArgs[0]);
        assertEquals("world", parsedArgs[1]);
        assertEquals("!", parsedArgs[2]);
    }

    @Test
    public void testParseWithFormatsContainingNonFormats() {
        String pattern = "{0} {1} {2}";
        ExtendedMessageFormat formatter = new ExtendedMessageFormat(pattern, Locale.ENGLISH);

        Object[] args = {"Hello", "world", "!"};
        Object[] parsedArgs = formatter.parse(args, new Object[] {"Hello", null, null});

        assertEquals(3, parsedArgs.length);
        assertEquals("Hello", parsedArgs[0]);
        assertEquals("world", parsedArgs[1]);
        assertNull(parsedArgs[2]);
    }

    private static class DummyFormatFactory implements FormatFactory {
        @Override
        public Format getFormat(String formatName, String format, Locale locale) {
            if (formatName.equals("date")) {
                return new DummyDateFormat();
            }
            if (formatName.equals("number")) {
                return new DummyNumberFormat("#,##0.##");
            }
            throw new IllegalArgumentException("Unknown format: " + formatName);
        }
    }

    private static class DummyDateFormat extends Format {
        @Override
        public StringBuffer format(Object text, StringBuffer toAppendTo, FieldPosition pos) {
            toAppendTo.append("CustomDate");
            return toAppendTo;
        }

        @Override
        public Object parseObject(String source, ParsePosition pos) {
            return "CustomDate";
        }
    }

    private static class DummyNumberFormat extends Format {
        @Override
        public StringBuffer format(Object object, StringBuffer toAppendTo, FieldPosition pos) {
            toAppendTo.append("CustomNumber");
            return toAppendTo;
        }

        @Override
        public Object parseObject(String source, ParsePosition pos) {
            return "CustomNumber";
        }
    }
}