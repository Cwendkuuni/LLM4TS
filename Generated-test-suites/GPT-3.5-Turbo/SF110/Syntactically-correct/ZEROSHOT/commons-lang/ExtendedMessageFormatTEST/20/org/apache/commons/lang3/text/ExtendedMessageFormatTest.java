package org.apache.commons.lang3.text;

import org.junit.Assert;
import org.junit.Test;
import java.text.*;
import java.util.*;
import org.apache.commons.lang3.*;
import java.io.*;

public class ExtendedMessageFormatTest {

    @Test
    public void testConstructor1() {
        ExtendedMessageFormat format = new ExtendedMessageFormat("Test Pattern");
        Assert.assertEquals(format.toPattern(), "Test Pattern");
    }
    
    @Test
    public void testConstructor2() {
        Locale locale = new Locale("en");
        ExtendedMessageFormat format = new ExtendedMessageFormat("Test Pattern", locale);
        Assert.assertEquals(format.toPattern(), "Test Pattern");
        Assert.assertEquals(format.getLocale(), locale);
    }
    
    @Test
    public void testConstructor3() {
        Map<String, FormatFactory> registry = new HashMap<>();
        registry.put("name", new FormatFactory() {
            public Format getFormat(String name, String args, Locale locale) {
                return DateFormat.getDateInstance(DateFormat.SHORT, locale);
            }
        });
        ExtendedMessageFormat format = new ExtendedMessageFormat("Test Pattern", registry);
        Assert.assertEquals(format.toPattern(), "Test Pattern");
        Assert.assertEquals(format.getFormats().length, 0);
    }
    
    @Test
    public void testConstructor4() {
        Locale locale = new Locale("en");
        Map<String, FormatFactory> registry = new HashMap<>();
        registry.put("name", new FormatFactory() {
            public Format getFormat(String name, String args, Locale locale) {
                return DateFormat.getDateInstance(DateFormat.SHORT, locale);
            }
        });
        ExtendedMessageFormat format = new ExtendedMessageFormat("Test Pattern", locale, registry);
        Assert.assertEquals(format.toPattern(), "Test Pattern");
        Assert.assertEquals(format.getLocale(), locale);
        Assert.assertEquals(format.getFormats().length, 0);
    }
    
    @Test
    public void testToPattern() {
        ExtendedMessageFormat format = new ExtendedMessageFormat("Test Pattern");
        Assert.assertEquals(format.toPattern(), "Test Pattern");
    }
    
    @Test
    public void testApplyPatternWithoutRegistry() {
        ExtendedMessageFormat format = new ExtendedMessageFormat("Test {0}");
        format.applyPattern("Test {0}, {1}");
        Assert.assertEquals(format.toPattern(), "Test {0}, {1}");
    }
    
    @Test
    public void testApplyPatternWithRegistry() {
        Map<String, FormatFactory> registry = new HashMap<>();
        registry.put("name", new FormatFactory() {
            public Format getFormat(String name, String args, Locale locale) {
                return DateFormat.getDateInstance(DateFormat.SHORT, locale);
            }
        });
        ExtendedMessageFormat format = new ExtendedMessageFormat("Test {0}", registry);
        format.applyPattern("Test {0}, {1}");
        Assert.assertEquals(format.toPattern(), "Test {0}, {1}");
        Assert.assertEquals(format.getFormats().length, 0);
    }
    
    @Test(expected = UnsupportedOperationException.class)
    public void testSetFormat() {
        ExtendedMessageFormat format = new ExtendedMessageFormat("Test {0}");
        format.setFormat(0, DateFormat.getDateInstance(DateFormat.SHORT));
    }
    
    @Test(expected = UnsupportedOperationException.class)
    public void testSetFormatByArgumentIndex() {
        ExtendedMessageFormat format = new ExtendedMessageFormat("Test {0}");
        format.setFormatByArgumentIndex(0, DateFormat.getDateInstance(DateFormat.SHORT));
    }
    
    @Test(expected = UnsupportedOperationException.class)
    public void testSetFormats() {
        ExtendedMessageFormat format = new ExtendedMessageFormat("Test {0}");
        format.setFormats(new Format[]{DateFormat.getDateInstance(DateFormat.SHORT)});
    }
    
    @Test(expected = UnsupportedOperationException.class)
    public void testSetFormatsByArgumentIndex() {
        ExtendedMessageFormat format = new ExtendedMessageFormat("Test {0}");
        format.setFormatsByArgumentIndex(new Format[]{DateFormat.getDateInstance(DateFormat.SHORT)});
    }
    
    @Test
    public void testEquals() {
        ExtendedMessageFormat format1 = new ExtendedMessageFormat("Test {0}");
        ExtendedMessageFormat format2 = new ExtendedMessageFormat("Test {0}");
        Assert.assertTrue(format1.equals(format2));
    }
    
    @Test
    public void testHashCode() {
        ExtendedMessageFormat format = new ExtendedMessageFormat("Test {0}");
        Assert.assertEquals(format.hashCode(), format.toPattern().hashCode());
    }
    
    @Test
    public void testGetFormat() {
        Map<String, FormatFactory> registry = new HashMap<>();
        registry.put("date", new FormatFactory() {
            public Format getFormat(String name, String args, Locale locale) {
                return DateFormat.getDateInstance(DateFormat.SHORT, locale);
            }
        });
        ExtendedMessageFormat format = new ExtendedMessageFormat("Test {0,date}", registry);
        Assert.assertTrue(format.getFormat("date") instanceof SimpleDateFormat);
    }
    
    @Test
    public void testReadArgumentIndex() {
        ExtendedMessageFormat format = new ExtendedMessageFormat("Test {0}, {1}");
        ParsePosition pos = new ParsePosition(0);
        int index = format.readArgumentIndex("Test {1}, {0}", pos);
        Assert.assertEquals(index, 1);
    }
    
    @Test
    public void testParseFormatDescription() {
        ExtendedMessageFormat format = new ExtendedMessageFormat("Test {0,date}", null);
        ParsePosition pos = new ParsePosition(6);
        String description = format.parseFormatDescription("Test {0,date}", pos);
        Assert.assertEquals(description, "date");
    }
    
    @Test
    public void testInsertFormats() {
        ExtendedMessageFormat format = new ExtendedMessageFormat("Test {0}", null);
        ArrayList<String> customPatterns = new ArrayList<>();
        customPatterns.add("date");
        String result = format.insertFormats("Test {0}", customPatterns);
        Assert.assertEquals(result, "Test {0}");
        customPatterns.add(null);
        result = format.insertFormats("Test {0}", customPatterns);
        Assert.assertEquals(result, "Test {0}");
    }
    
    @Test
    public void testSeekNonWs() {
        ExtendedMessageFormat format = new ExtendedMessageFormat("Test {0}", null);
        ParsePosition pos = new ParsePosition(0);
        format.seekNonWs("   Test {0}", pos);
        Assert.assertEquals(pos.getIndex(), 3);
    }
    
    @Test
    public void testNext() {
        ExtendedMessageFormat format = new ExtendedMessageFormat("Test {0}", null);
        ParsePosition pos = new ParsePosition(0);
        format.next(pos);
        Assert.assertEquals(pos.getIndex(), 1);
    }
    
    @Test
    public void testAppendQuotedStringWithEscapingOn() {
        ExtendedMessageFormat format = new ExtendedMessageFormat("Test {0}", null);
        ParsePosition pos = new ParsePosition(0);
        StringBuilder result = format.appendQuotedString("Test ''", pos, new StringBuilder(), true);
        Assert.assertEquals(result.toString(), "'");
    }
    
    @Test
    public void testAppendQuotedStringWithEscapingOff() {
        ExtendedMessageFormat format = new ExtendedMessageFormat("Test {0}", null);
        ParsePosition pos = new ParsePosition(0);
        StringBuilder result = format.appendQuotedString("Test ''", pos, new StringBuilder(), false);
        Assert.assertEquals(result.toString(), "''");
    }
    
    @Test
    public void testContainsElementsWithEmptyCollection() {
        ExtendedMessageFormat format = new ExtendedMessageFormat("Test {0}", null);
        ArrayList<String> collection = new ArrayList<>();
        boolean result = format.containsElements(collection);
        Assert.assertFalse(result);
    }

    @Test
    public void testContainsElementsWithNonEmptyCollection() {
        ExtendedMessageFormat format = new ExtendedMessageFormat("Test {0}", null);
        ArrayList<String> collection = new ArrayList<>();
        collection.add("element");
        boolean result = format.containsElements(collection);
        Assert.assertTrue(result);
    }
}