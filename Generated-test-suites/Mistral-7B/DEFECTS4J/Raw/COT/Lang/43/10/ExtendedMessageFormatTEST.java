package org.apache.commons.lang.text; // import statements go here

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertThrows;

import java.text.ParsePosition;
import java.util.HashMap;
import java.util.Map;

import org.apache.commons.lang.Validate;
import org.junit.Test;

public class ExtendedMessageFormatTest {

    @Test
    public void testEmptyConstructor() {
        ExtendedMessageFormat format = new ExtendedMessageFormat("");
        assertEquals("", format.toPattern());
        assertNull(format.registry);
    }

    @Test
    public void testConstructorWithPattern() {
        String pattern = "{0,date} {1,choice,0#Hello|1#Goodbye}.";
        ExtendedMessageFormat format = new ExtendedMessageFormat(pattern);
        assertEquals(pattern, format.toPattern());
        assertNull(format.registry);
    }

    @Test
    public void testConstructorWithPatternAndLocale() {
        String pattern = "{0,date} {1,choice,0#Hello|1#Goodbye}.";
        ExtendedMessageFormat format = new ExtendedMessageFormat(pattern, Locale.US);
        assertEquals(pattern, format.toPattern());
        assertNull(format.registry);
    }

    @Test
    public void testConstructorWithPatternAndRegistry() {
        String pattern = "{0,date} {1,choice,0#Hello|1#Goodbye}.";
        Map<String, FormatFactory> registry = new HashMap<>();
        FormatFactory formatFactory = mock(FormatFactory.class);
        registry.put("date", formatFactory);
        ExtendedMessageFormat format = new ExtendedMessageFormat(pattern, registry);
        assertEquals(pattern, format.toPattern());
        assertEquals(registry, format.registry);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testInvalidPattern() {
        new ExtendedMessageFormat("{0}");
    }

    @Test(expected = IllegalArgumentException.class)
    public void testInvalidRegistryKey() {
        String pattern = "{0,date} {1,choice,0#Hello|1#Goodbye}.";
        Map<String, FormatFactory> registry = new HashMap<>();
        registry.put("invalidKey", mock(FormatFactory.class));
        new ExtendedMessageFormat(pattern, registry);
    }

    @Test
    public void testApplyPattern() {
        String pattern = "{0,choice,0#Hello|1#Goodbye} {1,date}.";
        ExtendedMessageFormat format = new ExtendedMessageFormat(pattern);

        String input = "Goodbye 25-Jan-2023";
        Object[] args = {"Goodbye", "25-Jan-2023"};
        Object result = format.format(args);
        assertEquals("Goodbye 25-Jan-2023", result.toString());
    }

    @Test
    public void testApplyPatternWithRegistry() {
        String pattern = "{0,choice,0#Hello|1#Goodbye} {1,date,full}.";
        Map<String, FormatFactory> registry = new HashMap<>();
        FormatFactory dateFormatFactory = mock(FormatFactory.class);
        registry.put("date", dateFormatFactory);
        ExtendedMessageFormat format = new ExtendedMessageFormat(pattern, registry);

        String input = "Goodbye 25-Jan-2023";
        Object[] args = {"Goodbye", "25-Jan-2023"};
        Object result = format.format(args);
        assertEquals("Goodbye 25-Jan-2023", result.toString());

        ParsePosition pos = new ParsePosition(0);
        Format dateFormat = dateFormatFactory.getFormat("date", null, Locale.US);
        String datePattern = dateFormat.toPattern();
        assertEquals("dd-MMM-yyyy h:mm a", datePattern);
    }

    @Test(expected = UnsupportedOperationException.class)
    public void testSetFormat() {
        ExtendedMessageFormat format = new ExtendedMessageFormat("{0}");
        format.setFormat(0, mock(Format.class));
    }

    @Test(expected = UnsupportedOperationException.class)
    public void testSetFormatByArgumentIndex() {
        ExtendedMessageFormat format = new ExtendedMessageFormat("{0}");
        format.setFormatByArgumentIndex(0, mock(Format.class));
    }

    @Test(expected = UnsupportedOperationException.class)
    public void testSetFormats() {
        ExtendedMessageFormat format = new ExtendedMessageFormat("{0}");
        format.setFormats(new Format[0]);
    }

    @Test(expected = UnsupportedOperationException.class)
    public void testSetFormatsByArgumentIndex() {
        ExtendedMessageFormat format = new ExtendedMessageFormat("{0}");
        format.setFormatsByArgumentIndex(new Format[0]);
    }
}