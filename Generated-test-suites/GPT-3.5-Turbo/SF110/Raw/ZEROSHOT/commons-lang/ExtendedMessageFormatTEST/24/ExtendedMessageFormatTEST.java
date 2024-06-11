package org.apache.commons.lang3.text;

import java.text.Format;
import java.text.FormatFactory;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Locale;
import java.util.Map;
import org.junit.*;
import static org.junit.Assert.*;

public class ExtendedMessageFormatTest {
    
    @Test
    public void testConstructor() {
        ExtendedMessageFormat emf = new ExtendedMessageFormat("", Locale.getDefault());
        assertNotNull(emf);
    }
    
    @Test
    public void testToPattern() {
        ExtendedMessageFormat emf = new ExtendedMessageFormat("");
        assertEquals("", emf.toPattern());
    }
    
    @Test
    public void testSetFormat() {
        ExtendedMessageFormat emf = new ExtendedMessageFormat("");
        try {
            emf.setFormat(0, null);
            fail("Should thrown UnsupportedOperationException");
        } catch (UnsupportedOperationException e) {
            // Expected
        }
    }
    
    @Test
    public void testSetFormatByArgumentIndex() {
        ExtendedMessageFormat emf = new ExtendedMessageFormat("");
        try {
            emf.setFormatByArgumentIndex(0, null);
            fail("Should thrown UnsupportedOperationException");
        } catch (UnsupportedOperationException e) {
            // Expected
        }
    }
    
    @Test
    public void testSetFormats() {
        ExtendedMessageFormat emf = new ExtendedMessageFormat("");
        try {
            emf.setFormats(null);
            fail("Should thrown UnsupportedOperationException");
        } catch (UnsupportedOperationException e) {
            // Expected
        }
    }
    
    @Test
    public void testSetFormatsByArgumentIndex() {
        ExtendedMessageFormat emf = new ExtendedMessageFormat("");
        try {
            emf.setFormatsByArgumentIndex(null);
            fail("Should thrown UnsupportedOperationException");
        } catch (UnsupportedOperationException e) {
            // Expected
        }
    }
    
    @Test
    public void testEquals() {
        ExtendedMessageFormat emf1 = new ExtendedMessageFormat("");
        ExtendedMessageFormat emf2 = new ExtendedMessageFormat("");
        assertTrue(emf1.equals(emf2));
        
        emf1 = new ExtendedMessageFormat("pattern", Locale.getDefault(), null);
        emf2 = new ExtendedMessageFormat("pattern", Locale.getDefault(), null);
        assertTrue(emf1.equals(emf2));
        
        emf1 = new ExtendedMessageFormat("pattern1", Locale.getDefault(), null);
        emf2 = new ExtendedMessageFormat("pattern2", Locale.getDefault(), null);
        assertFalse(emf1.equals(emf2));
        
        emf1 = new ExtendedMessageFormat("pattern", Locale.getDefault(), null);
        emf2 = new ExtendedMessageFormat("pattern", Locale.FRANCE, null);
        assertFalse(emf1.equals(emf2));
        
        emf1 = new ExtendedMessageFormat("pattern", Locale.getDefault(), null);
        emf2 = new ExtendedMessageFormat("pattern", Locale.getDefault(), new HashMap<>());
        assertFalse(emf1.equals(emf2));
    }
    
    @Test
    public void testHashCode() {
        ExtendedMessageFormat emf1 = new ExtendedMessageFormat("");
        ExtendedMessageFormat emf2 = new ExtendedMessageFormat("");
        assertEquals(emf1.hashCode(), emf2.hashCode());
        
        emf1 = new ExtendedMessageFormat("pattern", Locale.getDefault(), null);
        emf2 = new ExtendedMessageFormat("pattern", Locale.getDefault(), null);
        assertEquals(emf1.hashCode(), emf2.hashCode());
        
        emf1 = new ExtendedMessageFormat("pattern", Locale.getDefault(), null);
        emf2 = new ExtendedMessageFormat("pattern2", Locale.getDefault(), null);
        assertNotEquals(emf1.hashCode(), emf2.hashCode());
        
        emf1 = new ExtendedMessageFormat("pattern", Locale.getDefault(), null);
        emf2 = new ExtendedMessageFormat("pattern", Locale.getDefault(), new HashMap<>());
        assertNotEquals(emf1.hashCode(), emf2.hashCode());
    }
    
    @Test
    public void testAppendQuotedString() {
        ExtendedMessageFormat emf = new ExtendedMessageFormat("");
        StringBuilder sb = new StringBuilder();
        emf.appendQuotedString("text", new ParsePosition(0), sb, false);
        assertEquals("text", sb.toString());
    }
    
    @Test
    public void testGetQuotedString() {
        ExtendedMessageFormat emf = new ExtendedMessageFormat("");
        emf.getQuotedString("", new ParsePosition(0), false);
    }
    
    @Test
    public void testContainsElements() {
        ExtendedMessageFormat emf = new ExtendedMessageFormat("");
        ArrayList<String> list = new ArrayList<>();
        assertFalse(emf.containsElements(list));
        
        list.add(null);
        assertFalse(emf.containsElements(list));
        
        list.add("element");
        assertTrue(emf.containsElements(list));
    }
    
    @Test
    public void testApplyPattern() {
        ExtendedMessageFormat emf = new ExtendedMessageFormat("");
        emf.applyPattern("");
        assertEquals("", emf.toPattern());
        
        emf.registry = new HashMap<>();
        emf.applyPattern("");
        assertEquals("", emf.toPattern());
        
        emf.applyPattern("{0}");
        assertEquals("{0}", emf.toPattern());
        
        emf.applyPattern("'{0}'");
        assertEquals("'{0}'", emf.toPattern());
        
        emf.applyPattern("{0,number}");
        assertEquals("{0,number}", emf.toPattern());
        
        emf.applyPattern("{1,date,format}");
        assertEquals("{1,date,format}", emf.toPattern());
        
        emf.applyPattern("{0},{1}");
        assertEquals("{0},{1}", emf.toPattern());
        
        emf.applyPattern("[{0},{1}]");
        assertEquals("[{0},{1}]", emf.toPattern());
    }
    
    // More test methods for other methods in ExtendedMessageFormat class
    
}