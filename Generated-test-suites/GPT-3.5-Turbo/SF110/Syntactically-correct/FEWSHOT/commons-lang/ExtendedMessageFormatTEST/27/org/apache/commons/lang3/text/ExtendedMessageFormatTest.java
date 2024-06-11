package org.apache.commons.lang3.text;

import org.junit.Test;

import java.text.Format;
import java.text.NumberFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.HashMap;
import java.util.Locale;
import java.util.Map;

import static org.junit.Assert.*;

public class ExtendedMessageFormatTest {

    private static final String PATTERN = "This is a test: {0,date,long}, {1,number,currency}, {2,choice,0#none|1#one|1<{2,number,integer}}, {3,time}";
    private static final Object[] ARGUMENTS = {new SimpleDateFormat("yyyy-MM-dd").parse("2021-10-20"), 1000.55, 2, new SimpleDateFormat("HH:mm:ss").parse("18:30:00")};

    @Test
    public void testConstructorWithPatternAndLocaleAndRegistry() throws ParseException {
        Map<String, FormatFactory> registry = new HashMap<>();
        registry.put("currency", new FormatFactory() {
            @Override
            public Format getFormat(String name, String arguments, Locale locale) {
                return NumberFormat.getCurrencyInstance(locale);
            }
        });

        ExtendedMessageFormat format = new ExtendedMessageFormat(PATTERN, Locale.US, registry);
        assertEquals(PATTERN, format.toPattern());
        assertArrayEquals(ARGUMENTS, format.parse(format.format(ARGUMENTS)));
    }

    @Test
    public void testConstructorWithPatternAndRegistry() throws ParseException {
        Map<String, FormatFactory> registry = new HashMap<>();
        registry.put("date", new FormatFactory() {
            @Override
            public Format getFormat(String name, String arguments, Locale locale) {
                return new SimpleDateFormat(arguments, locale);
            }
        });

        ExtendedMessageFormat format = new ExtendedMessageFormat(PATTERN, registry);
        assertEquals(PATTERN, format.toPattern());
        assertArrayEquals(ARGUMENTS, format.parse(format.format(ARGUMENTS)));
    }

    @Test
    public void testConstructorWithPatternAndLocale() throws ParseException {
        ExtendedMessageFormat format = new ExtendedMessageFormat(PATTERN, Locale.US);
        assertEquals(PATTERN, format.toPattern());
        assertArrayEquals(ARGUMENTS, format.parse(format.format(ARGUMENTS)));
    }

    @Test
    public void testConstructorWithPattern() throws ParseException {
        ExtendedMessageFormat format = new ExtendedMessageFormat(PATTERN);
        assertEquals(PATTERN, format.toPattern());
        assertArrayEquals(ARGUMENTS, format.parse(format.format(ARGUMENTS)));
    }

    @Test(expected = UnsupportedOperationException.class)
    public void testSetFormat() {
        ExtendedMessageFormat format = new ExtendedMessageFormat(PATTERN);
        format.setFormat(1, NumberFormat.getCurrencyInstance());
    }

    @Test(expected = UnsupportedOperationException.class)
    public void testSetFormatByArgumentIndex() {
        ExtendedMessageFormat format = new ExtendedMessageFormat(PATTERN);
        format.setFormatByArgumentIndex(1, NumberFormat.getCurrencyInstance());
    }

    @Test(expected = UnsupportedOperationException.class)
    public void testSetFormats() {
        ExtendedMessageFormat format = new ExtendedMessageFormat(PATTERN);
        format.setFormats(new Format[]{null});
    }

    @Test(expected = UnsupportedOperationException.class)
    public void testSetFormatsByArgumentIndex() {
        ExtendedMessageFormat format = new ExtendedMessageFormat(PATTERN);
        format.setFormatsByArgumentIndex(new Format[]{null});
    }

    @Test
    public void testEquals() {
        ExtendedMessageFormat format1 = new ExtendedMessageFormat(PATTERN);
        ExtendedMessageFormat format2 = new ExtendedMessageFormat(PATTERN);
        ExtendedMessageFormat format3 = new ExtendedMessageFormat(PATTERN, Locale.US);
        ExtendedMessageFormat format4 = new ExtendedMessageFormat(PATTERN, Locale.US);
        ExtendedMessageFormat format5 = new ExtendedMessageFormat("Different pattern");

        assertEquals(format1, format1);
        assertEquals(format1, format2);
        assertEquals(format3, format3);
        assertEquals(format3, format4);

        assertNotEquals(format1, null);
        assertNotEquals(format1, format3);
        assertNotEquals(format3, format5);
    }

    @Test
    public void testHashCode() {
        ExtendedMessageFormat format1 = new ExtendedMessageFormat(PATTERN);
        ExtendedMessageFormat format2 = new ExtendedMessageFormat(PATTERN);
        ExtendedMessageFormat format3 = new ExtendedMessageFormat(PATTERN, Locale.US);
        ExtendedMessageFormat format4 = new ExtendedMessageFormat(PATTERN, Locale.US);
        ExtendedMessageFormat format5 = new ExtendedMessageFormat("Different pattern");

        assertEquals(format1.hashCode(), format2.hashCode());
        assertEquals(format3.hashCode(), format4.hashCode());

        assertNotEquals(format1.hashCode(), format3.hashCode());
        assertNotEquals(format3.hashCode(), format5.hashCode());
    }

    @Test
    public void testFormatAndParse() throws ParseException {
        ExtendedMessageFormat format = new ExtendedMessageFormat(PATTERN);
        String formatted = format.format(ARGUMENTS);

        assertArrayEquals(ARGUMENTS, format.parse(formatted));
    }
}