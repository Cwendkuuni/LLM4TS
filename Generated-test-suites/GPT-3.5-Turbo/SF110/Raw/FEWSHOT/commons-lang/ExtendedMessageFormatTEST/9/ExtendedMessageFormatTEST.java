package org.apache.commons.lang3.text;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertTrue;

import org.junit.*;
import java.text.*;
import java.util.*;
import org.apache.commons.lang3.*;
import java.io.*;

public class ExtendedMessageFormatTest {
    @Test
    public void testConstructorWithPatternAndLocale() {
        String pattern = "Hello, {0}!";
        Locale locale = Locale.US;
        ExtendedMessageFormat format = new ExtendedMessageFormat(pattern, locale);
        assertEquals(pattern, format.toPattern());
        assertEquals(locale, format.getLocale());
        assertNull(format.registry);
    }

    @Test
    public void testConstructorWithPatternAndRegistry() {
        String pattern = "Hello, {0}!";
        Map<String, FormatFactory> registry = new HashMap<>();
        registry.put("number", NumberFormat::getInstance);
        ExtendedMessageFormat format = new ExtendedMessageFormat(pattern, registry);
        assertEquals(pattern, format.toPattern());
        assertNull(format.getLocale());
        assertEquals(registry, format.registry);
    }

    @Test
    public void testConstructorWithPatternLocaleAndRegistry() {
        String pattern = "Hello, {0}!";
        Locale locale = Locale.US;
        Map<String, FormatFactory> registry = new HashMap<>();
        registry.put("number", NumberFormat::getInstance);
        ExtendedMessageFormat format = new ExtendedMessageFormat(pattern, locale, registry);
        assertEquals(pattern, format.toPattern());
        assertEquals(locale, format.getLocale());
        assertEquals(registry, format.registry);
    }

    @Test
    public void testToPattern() {
        String pattern = "Hello, {0}!";
        ExtendedMessageFormat format = new ExtendedMessageFormat(pattern);
        assertEquals(pattern, format.toPattern());
    }

    @Test
    public void testApplyPatternWithoutRegistry() {
        String pattern = "Hello, {0}!";
        String expectedPattern = "Hello, {0}!";
        ExtendedMessageFormat format = new ExtendedMessageFormat("");
        format.applyPattern(pattern);
        assertEquals(expectedPattern, format.toPattern());
    }

    @Test
    public void testApplyPatternWithRegistry() {
        String pattern = "Hello, {0}!";
        Map<String, FormatFactory> registry = new HashMap<>();
        registry.put("number", NumberFormat::getInstance);
        String expectedPattern = "Hello, {0,number}!";
        ExtendedMessageFormat format = new ExtendedMessageFormat("", registry);
        format.applyPattern(pattern);
        assertEquals(expectedPattern, format.toPattern());
    }

    @Test
    public void testSetFormat() {
        ExtendedMessageFormat format = new ExtendedMessageFormat("");
        try {
            format.setFormat(0, NumberFormat.getCurrencyInstance());
            assertTrue(false);
        } catch (UnsupportedOperationException e) {
            assertTrue(true);
        }
    }

    @Test
    public void testSetFormatByArgumentIndex() {
        ExtendedMessageFormat format = new ExtendedMessageFormat("");
        try {
            format.setFormatByArgumentIndex(0, NumberFormat.getCurrencyInstance());
            assertTrue(false);
        } catch (UnsupportedOperationException e) {
            assertTrue(true);
        }
    }

    @Test
    public void testSetFormats() {
        ExtendedMessageFormat format = new ExtendedMessageFormat("");
        try {
            format.setFormats(new Format[]{NumberFormat.getCurrencyInstance()});
            assertTrue(false);
        } catch (UnsupportedOperationException e) {
            assertTrue(true);
        }
    }

    @Test
    public void testSetFormatsByArgumentIndex() {
        ExtendedMessageFormat format = new ExtendedMessageFormat("");
        try {
            format.setFormatsByArgumentIndex(new Format[]{NumberFormat.getCurrencyInstance()});
            assertTrue(false);
        } catch (UnsupportedOperationException e) {
            assertTrue(true);
        }
    }

    @Test
    public void testEquals() {
        ExtendedMessageFormat format1 = new ExtendedMessageFormat("");
        ExtendedMessageFormat format2 = new ExtendedMessageFormat("");
        ExtendedMessageFormat format3 = new ExtendedMessageFormat("Hello, {0}!");
        ExtendedMessageFormat format4 = new ExtendedMessageFormat("", Locale.US);
        ExtendedMessageFormat format5 = new ExtendedMessageFormat("", Locale.US);
        format5.applyPattern("Hello, {0}!");
        ExtendedMessageFormat format6 = new ExtendedMessageFormat("", null, new HashMap<>());
        ExtendedMessageFormat format7 = new ExtendedMessageFormat("", null, new HashMap<>());
        format7.applyPattern("Hello, {0,number}!");

        assertEquals(format1, format1);
        assertEquals(format1, format2);
        assertNotEquals(format1, format3);
        assertNotEquals(format1, format4);
        assertNotEquals(format1, format5);
        assertNotEquals(format1, format6);
        assertNotEquals(format1, format7);
    }

    @Test
    public void testHashCode() {
        ExtendedMessageFormat format1 = new ExtendedMessageFormat("");
        ExtendedMessageFormat format2 = new ExtendedMessageFormat("");
        ExtendedMessageFormat format3 = new ExtendedMessageFormat("Hello, {0}!");
        ExtendedMessageFormat format4 = new ExtendedMessageFormat("", Locale.US);
        ExtendedMessageFormat format5 = new ExtendedMessageFormat("", Locale.US);
        format5.applyPattern("Hello, {0}!");
        ExtendedMessageFormat format6 = new ExtendedMessageFormat("", null, new HashMap<>());
        ExtendedMessageFormat format7 = new ExtendedMessageFormat("", null, new HashMap<>());
        format7.applyPattern("Hello, {0,number}!");

        assertEquals(format1.hashCode(), format1.hashCode());
        assertEquals(format1.hashCode(), format2.hashCode());
        assertNotEquals(format1.hashCode(), format3.hashCode());
        assertNotEquals(format1.hashCode(), format4.hashCode());
        assertNotEquals(format1.hashCode(), format5.hashCode());
        assertNotEquals(format1.hashCode(), format6.hashCode());
        assertNotEquals(format1.hashCode(), format7.hashCode());
    }

    // TODO: add comprehensive test cases for all methods in the class

}