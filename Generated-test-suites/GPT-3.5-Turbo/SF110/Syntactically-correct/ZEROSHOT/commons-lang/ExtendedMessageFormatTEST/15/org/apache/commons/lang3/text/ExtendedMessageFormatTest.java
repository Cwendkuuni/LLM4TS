package org.apache.commons.lang3.text;

import org.junit.Test;
import static org.junit.Assert.*;
import java.text.*;
import java.util.*;
import org.apache.commons.lang3.*;
import java.io.*;

public class ExtendedMessageFormatTest {
    
    @Test
    public void testConstructor1() {
        ExtendedMessageFormat format = new ExtendedMessageFormat("Test");
        String expectedPattern = "Test";
        assertEquals(expectedPattern, format.toPattern());
    }
    
    @Test
    public void testConstructor2() {
        ExtendedMessageFormat format = new ExtendedMessageFormat("Test", Locale.US);
        String expectedPattern = "Test";
        assertEquals(expectedPattern, format.toPattern());
        assertEquals(Locale.US, format.getLocale());
    }
    
    @Test
    public void testConstructor3() {
        Map<String, FormatFactory> registry = new HashMap<>();
        registry.put("test", new TestFormatFactory());
        ExtendedMessageFormat format = new ExtendedMessageFormat("Test", registry);
        String expectedPattern = "Test";
        assertEquals(expectedPattern, format.toPattern());
        assertEquals(registry, format.getRegistry());
    }
    
    @Test
    public void testConstructor4() {
        Map<String, FormatFactory> registry = new HashMap<>();
        registry.put("test", new TestFormatFactory());
        ExtendedMessageFormat format = new ExtendedMessageFormat("Test", Locale.US, registry);
        String expectedPattern = "Test";
        assertEquals(expectedPattern, format.toPattern());
        assertEquals(Locale.US, format.getLocale());
        assertEquals(registry, format.getRegistry());
    }
    
    @Test
    public void testApplyPattern() {
        ExtendedMessageFormat format = new ExtendedMessageFormat("");
        format.applyPattern("Test {0,number,#} {1,date} {2,time}");
        String expectedPattern = "Test {0,number,#} {1,date} {2,time}";
        assertEquals(expectedPattern, format.toPattern());
    }
    
    @Test(expected = UnsupportedOperationException.class)
    public void testSetFormat() {
        ExtendedMessageFormat format = new ExtendedMessageFormat("");
        format.setFormat(0, NumberFormat.getInstance());
    }
    
    @Test(expected = UnsupportedOperationException.class)
    public void testSetFormatByArgumentIndex() {
        ExtendedMessageFormat format = new ExtendedMessageFormat("");
        format.setFormatByArgumentIndex(0, NumberFormat.getInstance());
    }
    
    @Test(expected = UnsupportedOperationException.class)
    public void testSetFormats() {
        ExtendedMessageFormat format = new ExtendedMessageFormat("");
        format.setFormats(new Format[]{NumberFormat.getInstance()});
    }
    
    @Test(expected = UnsupportedOperationException.class)
    public void testSetFormatsByArgumentIndex() {
        ExtendedMessageFormat format = new ExtendedMessageFormat("");
        format.setFormatsByArgumentIndex(new Format[]{NumberFormat.getInstance()});
    }
    
    @Test
    public void testEquals() {
        ExtendedMessageFormat format1 = new ExtendedMessageFormat("Test");
        ExtendedMessageFormat format2 = new ExtendedMessageFormat("Test");
        assertTrue(format1.equals(format2));
    }
    
    @Test
    public void testHashCode() {
        ExtendedMessageFormat format = new ExtendedMessageFormat("Test");
        int expectedHashCode = format.toPattern().hashCode() + format.getRegistry().hashCode();
        assertEquals(expectedHashCode, format.hashCode());
    }
    
    @Test
    public void testGetFormat() {
        Map<String, FormatFactory> registry = new HashMap<>();
        registry.put("test", new TestFormatFactory());
        ExtendedMessageFormat format = new ExtendedMessageFormat("");
        format.setRegistry(registry);
        Format result = format.getFormat("test");
        assertNotNull(result);
        assertTrue(result instanceof TestFormat);
    }
    
    @Test
    public void testReadArgumentIndex() {
        ExtendedMessageFormat format = new ExtendedMessageFormat("");
        ParsePosition pos = new ParsePosition(0);
        String pattern = "Test {0} {1} {2}";
        int result = format.readArgumentIndex(pattern, pos);
        int expectedIndex = 0;
        assertEquals(expectedIndex, result);
        expectedIndex = 1;
        pos.setIndex(pattern.indexOf("{1}"));
        result = format.readArgumentIndex(pattern, pos);
        assertEquals(expectedIndex, result);
        expectedIndex = 2;
        pos.setIndex(pattern.indexOf("{2}"));
        result = format.readArgumentIndex(pattern, pos);
        assertEquals(expectedIndex, result);
    }
    
    @Test
    public void testParseFormatDescription() {
        ExtendedMessageFormat format = new ExtendedMessageFormat("");
        ParsePosition pos = new ParsePosition(0);
        String pattern = "{0,number,###}";
        String result = format.parseFormatDescription(pattern, pos);
        String expectedDescription = "number,###";
        assertEquals(expectedDescription, result);
    }
    
    @Test
    public void testInsertFormats() {
        ExtendedMessageFormat format = new ExtendedMessageFormat("");
        ArrayList<String> customPatterns = new ArrayList<>();
        customPatterns.add("number,###");
        customPatterns.add("date,yyyy-MM-dd");
        String pattern = "Test {0} {1} {2}";
        String result = format.insertFormats(pattern, customPatterns);
        String expectedPattern = "Test {0,number,###} {1,date,yyyy-MM-dd} {2}";
        assertEquals(expectedPattern, result);
    }
    
    @Test
    public void testSeekNonWs() {
        ExtendedMessageFormat format = new ExtendedMessageFormat("");
        ParsePosition pos = new ParsePosition(0);
        String pattern = "   Test ";
        format.seekNonWs(pattern, pos);
        int expectedIndex = 3;
        assertEquals(expectedIndex, pos.getIndex());
    }
    
    @Test
    public void testNext() {
        ExtendedMessageFormat format = new ExtendedMessageFormat("");
        ParsePosition pos = new ParsePosition(0);
        format.next(pos);
        int expectedIndex = 1;
        assertEquals(expectedIndex, pos.getIndex());
    }
    
    @Test
    public void testAppendQuotedString() {
        ExtendedMessageFormat format = new ExtendedMessageFormat("");
        ParsePosition pos = new ParsePosition(0);
        StringBuilder appendTo = new StringBuilder();
        String pattern = "Test 'quoted string'";
        StringBuilder result = format.appendQuotedString(pattern, pos, appendTo, true);
        String expectedString = "quoted string";
        assertEquals(expectedString, result.toString());
    }
    
    @Test
    public void testGetQuotedString() {
        ExtendedMessageFormat format = new ExtendedMessageFormat("");
        ParsePosition pos = new ParsePosition(0);
        String pattern = "Test 'quoted string'";
        format.getQuotedString(pattern, pos, true);
        int expectedIndex = pattern.indexOf("'") + 1;
        assertEquals(expectedIndex, pos.getIndex());
    }
    
    @Test
    public void testContainsElements() {
        ExtendedMessageFormat format = new ExtendedMessageFormat("");
        List<String> list1 = new ArrayList<>();
        List<String> list2 = new ArrayList<>();
        list2.add("test");
        assertFalse(format.containsElements(list1));
        assertTrue(format.containsElements(list2));
    }
    
    class TestFormat extends Format {
        @Override
        public StringBuffer format(Object obj, StringBuffer toAppendTo, FieldPosition pos) {
            return null;
        }

        @Override
        public Object parseObject(String source, ParsePosition pos) {
            return null;
        }
    }
    
    class TestFormatFactory extends FormatFactory {
        @Override
        public Format getFormat(String name, String arguments, Locale locale) {
            return new TestFormat();
        }
    }
}