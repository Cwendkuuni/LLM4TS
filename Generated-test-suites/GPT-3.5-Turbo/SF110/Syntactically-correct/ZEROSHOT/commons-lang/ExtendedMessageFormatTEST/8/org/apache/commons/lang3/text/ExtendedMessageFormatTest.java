package org.apache.commons.lang3.text;

import org.junit.*;
import java.text.*;
import java.util.*;
import org.apache.commons.lang3.*;
import java.io.*;

public class ExtendedMessageFormatTest {

    @Test
    public void testConstructor1() {
        ExtendedMessageFormat emf = new ExtendedMessageFormat("Test message");
        String result = emf.toPattern();
        Assert.assertEquals("Test message", result);
    }

    @Test
    public void testConstructor2() {
        ExtendedMessageFormat emf = new ExtendedMessageFormat("Test message", Locale.ENGLISH);
        String result = emf.toPattern();
        Assert.assertEquals("Test message", result);

        Locale locale = emf.getLocale();
        Assert.assertEquals(Locale.ENGLISH, locale);
    }

    @Test
    public void testConstructor3() {
        Map<String, FormatFactory> registry = new HashMap<String, FormatFactory>();
        ExtendedMessageFormat emf = new ExtendedMessageFormat("Test message", registry);
        String result = emf.toPattern();
        Assert.assertEquals("Test message", result);

        Map<String, ? extends FormatFactory> actualRegistry = emf.registry;
        Assert.assertEquals(registry, actualRegistry);
    }

    @Test
    public void testConstructor4() {
        Map<String, FormatFactory> registry = new HashMap<String, FormatFactory>();
        ExtendedMessageFormat emf = new ExtendedMessageFormat("Test message", Locale.ENGLISH, registry);
        String result = emf.toPattern();
        Assert.assertEquals("Test message", result);

        Locale locale = emf.getLocale();
        Assert.assertEquals(Locale.ENGLISH, locale);

        Map<String, ? extends FormatFactory> actualRegistry = emf.registry;
        Assert.assertEquals(registry, actualRegistry);
    }

    @Test
    public void testToPattern() {
        ExtendedMessageFormat emf = new ExtendedMessageFormat("Test message");
        String result = emf.toPattern();
        Assert.assertEquals("Test message", result);
    }

    @Test
    public void testApplyPattern() {
        ExtendedMessageFormat emf = new ExtendedMessageFormat("Test message");
        emf.applyPattern("New test message");
        String result = emf.toPattern();
        Assert.assertEquals("New test message", result);
    }

    @Test(expected = UnsupportedOperationException.class)
    public void testSetFormat() {
        ExtendedMessageFormat emf = new ExtendedMessageFormat("Test message");
        emf.setFormat(1, null);
    }

    @Test(expected = UnsupportedOperationException.class)
    public void testSetFormatByArgumentIndex() {
        ExtendedMessageFormat emf = new ExtendedMessageFormat("Test message");
        emf.setFormatByArgumentIndex(1, null);
    }

    @Test(expected = UnsupportedOperationException.class)
    public void testSetFormats() {
        ExtendedMessageFormat emf = new ExtendedMessageFormat("Test message");
        emf.setFormats(null);
    }

    @Test(expected = UnsupportedOperationException.class)
    public void testSetFormatsByArgumentIndex() {
        ExtendedMessageFormat emf = new ExtendedMessageFormat("Test message");
        emf.setFormatsByArgumentIndex(null);
    }

    @Test
    public void testEquals() {
        ExtendedMessageFormat emf1 = new ExtendedMessageFormat("Test message");
        ExtendedMessageFormat emf2 = new ExtendedMessageFormat("Test message");
        boolean result = emf1.equals(emf2);
        Assert.assertTrue(result);
    }

    @Test
    public void testHashCode() {
        ExtendedMessageFormat emf = new ExtendedMessageFormat("Test message");
        int hashCode = emf.hashCode();
        Assert.assertNotNull(hashCode);
    }

    private Format getFormat(String desc) {
        ExtendedMessageFormat emf = new ExtendedMessageFormat("Test message");
        Format format = emf.getFormat(desc);
        return format;
    }

    private int readArgumentIndex(String pattern, ParsePosition pos) {
        ExtendedMessageFormat emf = new ExtendedMessageFormat("Test message");
        int argumentIndex = emf.readArgumentIndex(pattern, pos);
        return argumentIndex;
    }

    private String parseFormatDescription(String pattern, ParsePosition pos) {
        ExtendedMessageFormat emf = new ExtendedMessageFormat("Test message");
        String formatDescription = emf.parseFormatDescription(pattern, pos);
        return formatDescription;
    }

    private String insertFormats(String pattern, ArrayList<String> customPatterns) {
        ExtendedMessageFormat emf = new ExtendedMessageFormat("Test message");
        String result = emf.insertFormats(pattern, customPatterns);
        return result;
    }

    private void seekNonWs(String pattern, ParsePosition pos) {
        ExtendedMessageFormat emf = new ExtendedMessageFormat("Test message");
        emf.seekNonWs(pattern, pos);
    }

    private ParsePosition next(ParsePosition pos) {
        ExtendedMessageFormat emf = new ExtendedMessageFormat("Test message");
        ParsePosition result = emf.next(pos);
        return result;
    }

    private StringBuilder appendQuotedString(String pattern, ParsePosition pos, StringBuilder appendTo, boolean escapingOn) {
        ExtendedMessageFormat emf = new ExtendedMessageFormat("Test message");
        StringBuilder result = emf.appendQuotedString(pattern, pos, appendTo, escapingOn);
        return result;
    }

    private void getQuotedString(String pattern, ParsePosition pos, boolean escapingOn) {
        ExtendedMessageFormat emf = new ExtendedMessageFormat("Test message");
        emf.getQuotedString(pattern, pos, escapingOn);
    }

    private boolean containsElements(Collection<?> coll) {
        ExtendedMessageFormat emf = new ExtendedMessageFormat("Test message");
        boolean result = emf.containsElements(coll);
        return result;
    }
}