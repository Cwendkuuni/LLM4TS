package org.apache.commons.lang3.text;

import org.junit.Test;
import static org.junit.Assert.*;

public class ExtendedMessageFormatTest {
    
    @Test
    public void testConstructor1() {
        ExtendedMessageFormat format = new ExtendedMessageFormat("Hello");
        assertEquals("Hello", format.toPattern());
    }
    
    @Test
    public void testConstructor2() {
        ExtendedMessageFormat format = new ExtendedMessageFormat("Hello", Locale.US);
        assertEquals("Hello", format.toPattern());
        assertEquals(Locale.US, format.getLocale());
    }
    
    @Test
    public void testConstructor3() {
        ExtendedMessageFormat format = new ExtendedMessageFormat("Hello", new HashMap<>());
        assertEquals("Hello", format.toPattern());
    }
    
    @Test
    public void testConstructor4() {
        ExtendedMessageFormat format = new ExtendedMessageFormat("Hello", Locale.US, new HashMap<>());
        assertEquals("Hello", format.toPattern());
        assertEquals(Locale.US, format.getLocale());
    }
    
    @Test
    public void testApplyPattern() {
        ExtendedMessageFormat format = new ExtendedMessageFormat("Hello {0}");
        format.applyPattern("Hello {0}, {1}");
        assertEquals("Hello {0}, {1}", format.toPattern());
    }
    
    @Test(expected = IllegalArgumentException.class)
    public void testApplyPattern_InvalidFormat() {
        ExtendedMessageFormat format = new ExtendedMessageFormat("Hello {0}");
        format.applyPattern("Hello {0, invalid}");
    }
    
    @Test
    public void testSetFormat_NotSupported() {
        ExtendedMessageFormat format = new ExtendedMessageFormat("Hello {0}");
        try {
            format.setFormat(0, new DecimalFormat());
            fail("UnsupportedOperationException not thrown");
        } catch (UnsupportedOperationException e) {
            // Exception is expected
        }
    }
    
    @Test
    public void testSetFormatByArgumentIndex_NotSupported() {
        ExtendedMessageFormat format = new ExtendedMessageFormat("Hello {0}");
        try {
            format.setFormatByArgumentIndex(0, new DecimalFormat());
            fail("UnsupportedOperationException not thrown");
        } catch (UnsupportedOperationException e) {
            // Exception is expected
        }
    }
    
    @Test
    public void testSetFormats_NotSupported() {
        ExtendedMessageFormat format = new ExtendedMessageFormat("Hello {0}");
        try {
            format.setFormats(new Format[]{new DecimalFormat()});
            fail("UnsupportedOperationException not thrown");
        } catch (UnsupportedOperationException e) {
            // Exception is expected
        }
    }
    
    @Test
    public void testSetFormatsByArgumentIndex_NotSupported() {
        ExtendedMessageFormat format = new ExtendedMessageFormat("Hello {0}");
        try {
            format.setFormatsByArgumentIndex(new Format[]{new DecimalFormat()});
            fail("UnsupportedOperationException not thrown");
        } catch (UnsupportedOperationException e) {
            // Exception is expected
        }
    }
    
    @Test
    public void testEquals() {
        ExtendedMessageFormat format1 = new ExtendedMessageFormat("Hello");
        ExtendedMessageFormat format2 = new ExtendedMessageFormat("Hello");
        ExtendedMessageFormat format3 = new ExtendedMessageFormat("World");
        
        assertTrue(format1.equals(format2));
        assertFalse(format1.equals(format3));
    }
    
    @Test
    public void testHashCode() {
        ExtendedMessageFormat format1 = new ExtendedMessageFormat("Hello");
        ExtendedMessageFormat format2 = new ExtendedMessageFormat("Hello");
        
        assertEquals(format1.hashCode(), format2.hashCode());
    }
    
    // Additional test cases for potential edge cases and exception handling scenarios
    
    @Test
    public void testConstructor_NullPattern() {
        ExtendedMessageFormat format = new ExtendedMessageFormat(null);
        assertEquals("", format.toPattern());
    }
    
    @Test
    public void testApplyPattern_NoRegistry() {
        ExtendedMessageFormat format = new ExtendedMessageFormat("Hello {0}");
        format.applyPattern("Hello {0, optional}");
        assertEquals("Hello {0, optional}", format.toPattern());
    }
    
    @Test(expected = IllegalArgumentException.class)
    public void testApplyPattern_UnreadableFormatElement() {
        ExtendedMessageFormat format = new ExtendedMessageFormat("Hello {0}");
        format.applyPattern("Hello {0,");
    }
    
    @Test(expected = IllegalArgumentException.class)
    public void testApplyPattern_UnterminatedFormatElement() {
        ExtendedMessageFormat format = new ExtendedMessageFormat("Hello {0}");
        format.applyPattern("Hello {0");
    }
    
    @Test
    public void testGetFormat_NoRegistry() {
        ExtendedMessageFormat format = new ExtendedMessageFormat("Hello {0}");
        assertNull(format.getFormat("optional"));
    }
    
    @Test
    public void testReadArgumentIndex() {
        String pattern = "Hello {0} {1}";
        ParsePosition pos = new ParsePosition(0);
        ExtendedMessageFormat format = new ExtendedMessageFormat("");
        int index = format.readArgumentIndex(pattern, pos);
        assertEquals(0, index);
        assertEquals(6, pos.getIndex());
        
        pos = new ParsePosition(7);
        index = format.readArgumentIndex(pattern, pos);
        assertEquals(1, index);
        assertEquals(13, pos.getIndex());
    }
    
    @Test(expected = IllegalArgumentException.class)
    public void testReadArgumentIndex_InvalidIndex() {
        String pattern = "Hello {a}";
        ParsePosition pos = new ParsePosition(0);
        ExtendedMessageFormat format = new ExtendedMessageFormat("");
        format.readArgumentIndex(pattern, pos);
    }
    
    @Test(expected = IllegalArgumentException.class)
    public void testReadArgumentIndex_UnterminatedFormatElement() {
        String pattern = "Hello {";
        ParsePosition pos = new ParsePosition(0);
        ExtendedMessageFormat format = new ExtendedMessageFormat("");
        format.readArgumentIndex(pattern, pos);
    }
    
    @Test
    public void testParseFormatDescription() {
        String pattern = "Hello {0,number}";
        ParsePosition pos = new ParsePosition(6);
        ExtendedMessageFormat format = new ExtendedMessageFormat("");
        String description = format.parseFormatDescription(pattern, pos);
        assertEquals("number", description);
        assertEquals(14, pos.getIndex());
    }
    
    @Test(expected = IllegalArgumentException.class)
    public void testParseFormatDescription_UnterminatedFormatElement() {
        String pattern = "Hello {0, number";
        ParsePosition pos = new ParsePosition(6);
        ExtendedMessageFormat format = new ExtendedMessageFormat("");
        format.parseFormatDescription(pattern, pos);
    }
    
    @Test
    public void testInsertFormats() {
        ArrayList<String> customPatterns = new ArrayList<>();
        customPatterns.add("optional1");
        customPatterns.add(null);
        customPatterns.add("optional3");
        
        String pattern = "Hello {0}, {1}";
        ExtendedMessageFormat format = new ExtendedMessageFormat("");
        String result = format.insertFormats(pattern, customPatterns);
        assertEquals("Hello {0,optional1}, {1}", result);
    }
    
    @Test
    public void testContainsElements() {
        ArrayList<String> list1 = new ArrayList<>();
        ArrayList<String> list2 = new ArrayList<>();
        list2.add("value");
        
        ExtendedMessageFormat format = new ExtendedMessageFormat("");
        assertFalse(format.containsElements(null));
        assertFalse(format.containsElements(list1));
        assertTrue(format.containsElements(list2));
    }
}