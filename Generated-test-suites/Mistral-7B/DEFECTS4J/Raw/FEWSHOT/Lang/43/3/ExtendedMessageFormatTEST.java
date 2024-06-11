package org.apache.commons.lang.text;

import static org.junit.Assert.*;

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
    public void testCreateEmptyWithDefaultPatternAndLocale() {
        ExtendedMessageFormat format = new ExtendedMessageFormat("");
        assertEquals("", format.toPattern());
    }

    @Test
    public void testCreateWithDefaultPatternAndLocale() {
        ExtendedMessageFormat format = new ExtendedMessageFormat("{0}");
        assertEquals("{0}", format.toPattern());
    }

    @Test
    public void testCreateWithPatternAndDefaultLocale() {
        ExtendedMessageFormat format = new ExtendedMessageFormat("{0}, {1}", Locale.getDefault());
        assertEquals("{0}, {1}", format.toPattern());
    }

    @Test
    public void testCreateWithPatternAndCustomLocale() {
        ExtendedMessageFormat format = new ExtendedMessageFormat("{0}, {1}", new Locale("en", "US"));
        assertEquals("{0}, {1}", format.toPattern());
    }

    @Test
    public void testCreateWithCustomRegistry() {
        FormatFactory factory = new FormatFactory() {
            @Override
            public Format getFormat(String name, String args, Locale locale) {
                return new SimpleDateFormat(name);
            }
        };

        Map<String, FormatFactory> registry = new HashMap<>();
        registry.put("date", factory);

        ExtendedMessageFormat format = new ExtendedMessageFormat("{date,date,short '{0}'}", Collections.emptyMap(), registry);
        assertEquals("{date,date,short '{0}'}", format.toPattern());
    }

    @Test
    public void testApplyPatternWithNoFormats() {
        String pattern = "{0}{1}";
        ExtendedMessageFormat format = new ExtendedMessageFormat(pattern, null);
        assertEquals("{0}{1}", format.toPattern());
    }

    @Test
    public void testApplyPatternWithFormats() {
        String pattern = "{0}{1}{2,date,short '{0}'}";
        ExtendedMessageFormat format = new ExtendedMessageFormat(pattern, null);
        String expectedPattern = "{0}{1}{2,date,short '{dd.MM.yy}'}";
        assertEquals(expectedPattern, format.toPattern());
    }

    @Test
    public void testApplyPatternWithMissingFormat() {
        String pattern = "{0}{1}{3,date,short '{0}'}";
        ExtendedMessageFormat format = new ExtendedMessageFormat(pattern, null);
        String expectedPattern = "{0}{1}{3,date}";
        assertEquals(expectedPattern, format.toPattern());
    }

    @Test
    public void testApplyPatternWithInvalidFormat() {
        String pattern = "{0}{1}{3,invalidFormat}";
        ExtendedMessageFormat format = new ExtendedMessageFormat(pattern, null);
        assertNotNull(format.getErrorMessage());
        assertEquals("", format.toPattern());
    }

    @Test
    public void testSetFormat() {
        String pattern = "{0}{1}{2,date,short '{0}'}";
        ExtendedMessageFormat format = new ExtendedMessageFormat(pattern, null);
        Format customFormat = new SimpleDateFormat("yyyy-MM-dd");
        format.setFormat("date", customFormat);
        String expectedPattern = "{0}{1}{2,date,short '{yyyy-MM-dd}'}";
        assertEquals(expectedPattern, format.toPattern());
    }

    @Test
    public void testSetFormatByArgumentIndex() {
        String pattern = "{0}{1}{2,date,short '{0}'}";
        ExtendedMessageFormat format = new ExtendedMessageFormat(pattern, null);
        Format customFormat = new SimpleDateFormat("yyyy-MM-dd");
        format.setFormatByArgumentIndex(1, customFormat);
        String expectedPattern = "{0}{2,date,short '{yyyy-MM-dd}'}{1}";
        assertEquals(expectedPattern, format.toPattern());
    }

    @Test
    public void testSetFormats() {
        String pattern = "{0}{1}{2,date,short '{0}'}";
        ExtendedMessageFormat format = new ExtendedMessageFormat(pattern, null);
        Format[] customFormats = new Format[1];
        customFormats[0] = new SimpleDateFormat("yyyy-MM-dd");
        format.setFormats(customFormats);
        String expectedPattern = "{0}{1}{2,date,short '{yyyy-MM-dd}'}";
        assertEquals(expectedPattern, format.toPattern());
    }

    @Test
    public void testSetFormatsByArgumentIndex() {
        String pattern = "{0}{1}{2,date,short '{0}'}";
        ExtendedMessageFormat format = new ExtendedMessageFormat(pattern, null);
        Format[] customFormats = new Format[1];
        customFormats[0] = new SimpleDateFormat("yyyy-MM-dd");
        format.setFormatsByArgumentIndex(1, customFormats);
        String expectedPattern = "{0}{2,date,short '{yyyy-MM-dd}'}{1}";
        assertEquals(expectedPattern, format.toPattern());
    }

    @Test
    public void testParseWithCorrectFormat() {
        String pattern = "{0}{1}{2,date,short '{0}'}";
        ExtendedMessageFormat format = new ExtendedMessageFormat(pattern, null);
        String input = "Foo {1,date,short '2022-03-15'} Bar";
        Object[] args = {"Foo", "15-Mar-2022", "Bar"};
        ParsePosition pos = new ParsePosition(0);
        format.format(args, new Object[3], pos);
        assertEquals(input, format.format(args));
        assertEquals(input, format.format(args, null, pos));
    }

    @Test
    public void testParseWithMissingFormat() {
        String pattern = "{0}{1}{3,date,short '{0}'}";
        ExtendedMessageFormat format = new ExtendedMessageFormat(pattern, null);
        String input = "Foo {1,date} Bar";
        Object[] args = {"Foo", "15-Mar-2022", "Bar"};
        ParsePosition pos = new ParsePosition(0);
        format.parseObject(input, args, pos);
        assertEquals("Foo 15-Mar-2022 Bar", format.format(args));
        assertEquals("Foo 15-Mar-2022", format.format(args, null, pos));
    }

    @Test
    public void testParseWithInvalidFormat() {
        String pattern = "{0}{1}{3,invalidFormat}";
        ExtendedMessageFormat format = new ExtendedMessageFormat(pattern, null);
        String input = "Foo {1,invalidFormat} Bar";
        Object[] args = {"Foo", "15-Mar-2022", "Bar"};
        ParsePosition pos = new ParsePosition(0);
        assertFalse(format.parseObject(input, args, pos) instanceof Object[]);
        assertEquals("", format.format(args));
        assertEquals("", format.format(args, null, pos));
    }
}