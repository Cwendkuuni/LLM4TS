package org.apache.commons.lang3.text;

import java.text.*;
import org.apache.commons.lang3.*;
import java.io.*;
import static org.junit.Assert.*;
import org.junit.Test;

public class ExtendedMessageFormatTest {

    @Test
    public void testConstructorWithPatternAndLocale() {
        ExtendedMessageFormat format = new ExtendedMessageFormat("Hello {0}", Locale.ENGLISH);
        assertEquals("Hello {0}", format.toPattern());
    }

    @Test
    public void testConstructorWithPatternAndRegistry() {
        Map<String, FormatFactory> registry = new HashMap<>();
        registry.put("custom", new CustomFormatFactory());
        ExtendedMessageFormat format = new ExtendedMessageFormat("Hello {0,custom}", registry);
        assertEquals("Hello {0,custom}", format.toPattern());
    }

    @Test
    public void testConstructorWithPatternLocaleAndRegistry() {
        Map<String, FormatFactory> registry = new HashMap<>();
        registry.put("custom", new CustomFormatFactory());
        ExtendedMessageFormat format = new ExtendedMessageFormat("Hello {0,custom}", Locale.ENGLISH, registry);
        assertEquals("Hello {0,custom}", format.toPattern());
    }

    @Test
    public void testApplyPatternWithNullRegistry() {
        ExtendedMessageFormat format = new ExtendedMessageFormat("");
        format.applyPattern("Test {0}");
        assertEquals("Test {0}", format.toPattern());
    }

    @Test
    public void testApplyPatternWithCustomFormats() {
        Map<String, FormatFactory> registry = new HashMap<>();
        registry.put("custom", new CustomFormatFactory());
        ExtendedMessageFormat format = new ExtendedMessageFormat("", registry);
        format.applyPattern("Test {0,custom}");
        assertEquals("Test {0,custom}", format.toPattern());
    }

    @Test
    public void testEqualsWithSameObject() {
        ExtendedMessageFormat format = new ExtendedMessageFormat("");
        assertTrue(format.equals(format));
    }

    @Test
    public void testEqualsWithNullObject() {
        ExtendedMessageFormat format = new ExtendedMessageFormat("");
        assertFalse(format.equals(null));
    }

    @Test
    public void testEqualsWithDifferentClass() {
        ExtendedMessageFormat format = new ExtendedMessageFormat("");
        assertFalse(format.equals(""));
    }

    @Test
    public void testEqualsWithDifferentPattern() {
        ExtendedMessageFormat format1 = new ExtendedMessageFormat("Hello {0}");
        ExtendedMessageFormat format2 = new ExtendedMessageFormat("Test {0}");
        assertFalse(format1.equals(format2));
    }

    @Test
    public void testEqualsWithDifferentRegistry() {
        Map<String, FormatFactory> registry1 = new HashMap<>();
        registry1.put("custom", new CustomFormatFactory());
        Map<String, FormatFactory> registry2 = new HashMap<>();
        registry2.put("custom", new CustomFormatFactory());
        ExtendedMessageFormat format1 = new ExtendedMessageFormat("", registry1);
        ExtendedMessageFormat format2 = new ExtendedMessageFormat("", registry2);
        assertFalse(format1.equals(format2));
    }

    @Test
    public void testHashCode() {
        Map<String, FormatFactory> registry = new HashMap<>();
        registry.put("custom", new CustomFormatFactory());
        ExtendedMessageFormat format = new ExtendedMessageFormat("", registry);
        assertEquals(31 * format.hashCode() + ObjectUtils.hashCode(registry), format.hashCode());
    }

    private class CustomFormatFactory implements FormatFactory {
        @Override
        public Format getFormat(String name, String args, Locale locale) {
            return null;
        }
    }
}