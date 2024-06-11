package org.apache.commons.lang3.text;

import java.text.*;

import org.junit.Test;

import static org.junit.Assert.*;

public class ExtendedMessageFormatTest {

    @Test
    public void testEmptyPatternConstructor() {
        ExtendedMessageFormat emf = new ExtendedMessageFormat("");
        assertEquals("", emf.toPattern());
    }

    @Test
    public void testPatternAndLocaleConstructor() {
        ExtendedMessageFormat emf = new ExtendedMessageFormat("Hello {0}", Locale.US);
        assertEquals("Hello {0}", emf.toPattern());
    }

    @Test
    public void testPatternAndRegistryConstructor() {
        ExtendedMessageFormat emf = new ExtendedMessageFormat("Hello {0}", null);
        assertEquals("Hello {0}", emf.toPattern());
    }

    @Test
    public void testPatternAndLocaleAndRegistryConstructor() {
        ExtendedMessageFormat emf = new ExtendedMessageFormat("Hello {0}", Locale.US, null);
        assertEquals("Hello {0}", emf.toPattern());
    }

    @Test
    public void testApplyPatternWithoutRegistry() {
        ExtendedMessageFormat emf = new ExtendedMessageFormat("");
        emf.applyPattern("Hello {0}");
        assertEquals("Hello {0}", emf.toPattern());
    }

    @Test
    public void testApplyPatternWithRegistry() {
        ExtendedMessageFormat emf = new ExtendedMessageFormat("", null);
        emf.applyPattern("Hello {0}");
        assertEquals("Hello {0}", emf.toPattern());
    }

    @Test
    public void testSetFormat() {
        ExtendedMessageFormat emf = new ExtendedMessageFormat("");
        try {
            emf.setFormat(0, null);
            fail("Exception expected");
        } catch (UnsupportedOperationException e) {
            // Do nothing
        }
    }

    @Test
    public void testSetFormatByArgumentIndex() {
        ExtendedMessageFormat emf = new ExtendedMessageFormat("");
        try {
            emf.setFormatByArgumentIndex(0, null);
            fail("Exception expected");
        } catch (UnsupportedOperationException e) {
            // Do nothing
        }
    }

    @Test
    public void testSetFormats() {
        ExtendedMessageFormat emf = new ExtendedMessageFormat("");
        try {
            emf.setFormats(new Format[]{});
            fail("Exception expected");
        } catch (UnsupportedOperationException e) {
            // Do nothing
        }
    }

    @Test
    public void testSetFormatsByArgumentIndex() {
        ExtendedMessageFormat emf = new ExtendedMessageFormat("");
        try {
            emf.setFormatsByArgumentIndex(new Format[]{});
            fail("Exception expected");
        } catch (UnsupportedOperationException e) {
            // Do nothing
        }
    }

    @Test
    public void testEquals() {
        ExtendedMessageFormat emf1 = new ExtendedMessageFormat("");
        ExtendedMessageFormat emf2 = new ExtendedMessageFormat("");
        assertEquals(emf1, emf2);
    }

    @Test
    public void testHashCode() {
        ExtendedMessageFormat emf1 = new ExtendedMessageFormat("");
        ExtendedMessageFormat emf2 = new ExtendedMessageFormat("");
        assertEquals(emf1.hashCode(), emf2.hashCode());
    }

    @Test
    public void testGetFormat() {
        ExtendedMessageFormat emf = new ExtendedMessageFormat("");
        assertNull(emf.getFormat(""));
    }

    @Test
    public void testReadArgumentIndex() {
        ExtendedMessageFormat emf = new ExtendedMessageFormat("");
        assertEquals(0, emf.readArgumentIndex("{0}", new ParsePosition(0)));
    }

    @Test
    public void testParseFormatDescription() {
        ExtendedMessageFormat emf = new ExtendedMessageFormat("");
        assertEquals("", emf.parseFormatDescription("", new ParsePosition(0)));
    }

    @Test
    public void testInsertFormats() {
        ExtendedMessageFormat emf = new ExtendedMessageFormat("");
        assertEquals("", emf.insertFormats("", new ArrayList<String>()));
    }

    @Test
    public void testSeekNonWs() {
        ExtendedMessageFormat emf = new ExtendedMessageFormat("");
        emf.seekNonWs("", new ParsePosition(0));
    }
    
    @Test
    public void testNext() {
        ExtendedMessageFormat emf = new ExtendedMessageFormat("");
        emf.next(new ParsePosition(0));
    }
    
    @Test
    public void testAppendQuotedString() {
        ExtendedMessageFormat emf = new ExtendedMessageFormat("");
        assertNull(emf.appendQuotedString("", new ParsePosition(0), null, true));
    }
    
    @Test
    public void testGetQuotedString() {
        ExtendedMessageFormat emf = new ExtendedMessageFormat("");
        emf.getQuotedString("", new ParsePosition(0), true);
    }
    
    @Test
    public void testContainsElements() {
        ExtendedMessageFormat emf = new ExtendedMessageFormat("");
        assertFalse(emf.containsElements(null));
    }

}