package org.apache.commons.lang.text;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;

import java.text.Format;
import java.text.MessageFormat;
import java.util.HashMap;
import java.util.Locale;
import java.util.Map;

import org.junit.Test;

public class ExtendedMessageFormatTest {

    @Test
    public void testConstructorWithPatternAndLocale() {
        ExtendedMessageFormat emf = new ExtendedMessageFormat("Test {0}", Locale.getDefault());
        assertNotNull(emf);
    }

    @Test
    public void testConstructorWithPatternAndRegistryAndLocale() {
        Map<String, FormatFactory> registry = new HashMap<>();
        ExtendedMessageFormat emf = new ExtendedMessageFormat("Test {0}", Locale.getDefault(), registry);
        assertNotNull(emf);
    }

    @Test
    public void testConstructorWithPattern() {
        ExtendedMessageFormat emf = new ExtendedMessageFormat("Test {0}");
        assertNotNull(emf);
    }

    @Test
    public void testToPattern() {
        ExtendedMessageFormat emf = new ExtendedMessageFormat("Test {0}");
        assertEquals("Test {0}", emf.toPattern());
    }

    @Test
    public void testApplyPattern() {
        ExtendedMessageFormat emf = new ExtendedMessageFormat("Test {0}");
        emf.applyPattern("New Test {0}");
        assertEquals("New Test {0}", emf.toPattern());
    }

    @Test(expected = UnsupportedOperationException.class)
    public void testSetFormat() {
        ExtendedMessageFormat emf = new ExtendedMessageFormat("Test {0}");
        emf.setFormat(0, null);
    }

    @Test(expected = UnsupportedOperationException.class)
    public void testSetFormatByArgumentIndex() {
        ExtendedMessageFormat emf = new ExtendedMessageFormat("Test {0}");
        emf.setFormatByArgumentIndex(0, null);
    }

    @Test(expected = UnsupportedOperationException.class)
    public void testSetFormats() {
        ExtendedMessageFormat emf = new ExtendedMessageFormat("Test {0}");
        emf.setFormats(new Format[]{null});
    }

    @Test(expected = UnsupportedOperationException.class)
    public void testSetFormatsByArgumentIndex() {
        ExtendedMessageFormat emf = new ExtendedMessageFormat("Test {0}");
        emf.setFormatsByArgumentIndex(new Format[]{null});
    }

    @Test
    public void testGetFormat() {
        ExtendedMessageFormat emf = new ExtendedMessageFormat("Test {0}");
        Map<String, FormatFactory> registry = new HashMap<>();
        registry.put("testFormat", (name, args, locale) -> null);
        emf.applyPattern("Test {0,testFormat}");
        assertNotNull(emf.getFormat("testFormat"));
        assertNull(emf.getFormat("unknownFormat"));
    }

    @Test
    public void testInsertFormats() {
        ExtendedMessageFormat emf = new ExtendedMessageFormat("Test {0}");
        assertEquals("Test {0}", emf.insertFormats("Test {0}", null));
    }

    @Test
    public void testContainsElements() {
        ExtendedMessageFormat emf = new ExtendedMessageFormat("Test {0}");
        assertFalse(emf.containsElements(null));
    }
}