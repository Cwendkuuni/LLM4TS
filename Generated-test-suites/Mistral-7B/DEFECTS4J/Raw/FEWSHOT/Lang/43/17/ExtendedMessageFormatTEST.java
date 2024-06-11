package org.apache.commons.lang.text;

import static org.junit.Assert.assertArrayEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertTrue;

import java.text.ParsePosition;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

import org.apache.commons.cli.FormatFactory; // Assuming FormatFactory is a mockable class
import org.apache.commons.lang.text.ExtendedMessageFormat;
import org.apache.commons.lang.text.MessageFormat;
import org.junit.Test;

public class ExtendedMessageFormatTest {

    @Test
    public void testCreateExtendedMessageFormat() {
        String pattern = "Hello {0, number, currency} and {1, choice, 0#Good|1#Bad} world.";
        ExtendedMessageFormat emf = new ExtendedMessageFormat(pattern);
        assertTrue(emf instanceof ExtendedMessageFormat);
        assertNull(emf.toPattern());
    }

    @Test(expected = IllegalArgumentException.class)
    public void testBadPattern() {
        String pattern = "Hello {0, number, currency} and {1, choice, 0#Good|1#Bad} world}"; // Missing closing brace
        new ExtendedMessageFormat(pattern);
    }

    @Test
    public void testApplyPattern() {
        String pattern = "Hello {0, number, currency} and {1, choice, 0#Good|1#Bad} {2}.";
        ExtendedMessageFormat emf = new ExtendedMessageFormat(pattern, new HashMap<>());
        String[] args = {"10.50", "world", "Moon"};
        emf.applyPattern(pattern);
        String formatted = emf.format(args);
        assertTrue(formatted.contains("Hello 10.50 and Good world Moon."));
    }

    @Test
    public void testGetFormat() {
        FormatFactory mockFormatFactory = mock(FormatFactory.class); // Assuming mockito is used
        String pattern = "Hello {Foo, format, MyFormat}.";
        ExtendedMessageFormat emf = new ExtendedMessageFormat(pattern, new HashMap<>(), new HashMap<>());
        emf.registry = new HashMap<>();
        emf.registry.put("MyFormat", mockFormatFactory);
        when(mockFormatFactory.getFormat("MyFormat", null, Locale.getDefault()))
                .thenReturn(new MessageFormat("{0}"));

        Object[] args = {"Test"};
        String formatted = emf.format(args);
        assertTrue(formatted.contains("Hello Test"));
        verify(mockFormatFactory).getFormat("MyFormat", null, Locale.getDefault());
    }

    @Test
    public void testGetFormatWithArguments() {
        FormatFactory mockFormatFactory = mock(FormatFactory.class); // Assuming mockito is used
        String pattern = "Hello {Foo, format, MyFormat, arg1={1}}.";
        ExtendedMessageFormat emf = new ExtendedMessageFormat(pattern, new HashMap<>(), new HashMap<>());
        emf.registry = new HashMap<>();
        emf.registry.put("MyFormat", mockFormatFactory);
        when(mockFormatFactory.getFormat("MyFormat", null, Locale.getDefault()))
                .thenReturn(new MessageFormat("{1}"));

        Object[] args = {"Test", "Arg"};
        String formatted = emf.format(args);
        assertTrue(formatted.contains("Arg"));
        verify(mockFormatFactory).getFormat("MyFormat", new Object[]{"Arg"}, Locale.getDefault());
    }

    @Test(expected = UnsupportedOperationException.class)
    public void testSetFormat() {
        ExtendedMessageFormat emf = new ExtendedMessageFormat("Hello {0}.");
        emf.setFormat(0, new MessageFormat("{1}"));
    }

    @Test(expected = UnsupportedOperationException.class)
    public void testSetFormatByArgumentIndex() {
        ExtendedMessageFormat emf = new ExtendedMessageFormat("Hello {0}.");
        emf.setFormatByArgumentIndex(0, new MessageFormat("{1}"));
    }

    @Test(expected = UnsupportedOperationException.class)
    public void testSetFormats() {
        ExtendedMessageFormat emf = new ExtendedMessageFormat("Hello {0}.");
        Format[] formats = new MessageFormat[1];
        formats[0] = new MessageFormat("{1}");
        emf.setFormats(formats);
    }

    @Test(expected = UnsupportedOperationException.class)
    public void testSetFormatsByArgumentIndex() {
        ExtendedMessageFormat emf = new ExtendedMessageFormat("Hello {0}.");
        Format[] formats = new MessageFormat[1];
        formats[0] = new MessageFormat("{1}");
        emf.setFormatsByArgumentIndex(new Format[1], 0);
    }
}