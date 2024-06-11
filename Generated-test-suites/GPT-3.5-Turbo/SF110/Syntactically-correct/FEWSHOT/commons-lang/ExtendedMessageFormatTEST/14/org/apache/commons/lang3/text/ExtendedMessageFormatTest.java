import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertArrayEquals;

import org.apache.commons.lang3.text.ExtendedMessageFormat;
import org.junit.Test;

public class ExtendedMessageFormatTest {
   
    @Test
    public void testConstructor1() {
        ExtendedMessageFormat emf = new ExtendedMessageFormat("");
        assertEquals("", emf.toPattern());
    }

    @Test
    public void testConstructor2() {
        ExtendedMessageFormat emf = new ExtendedMessageFormat("", Locale.getDefault());
        assertEquals("", emf.toPattern());
    }

    @Test
    public void testConstructor3() {
        ExtendedMessageFormat emf = new ExtendedMessageFormat("", null);
        assertEquals("", emf.toPattern());
    }
    
    @Test
    public void testConstructor4() {
        ExtendedMessageFormat emf = new ExtendedMessageFormat("", Locale.getDefault(), null);
        assertEquals("", emf.toPattern());
    }
    
    @Test
    public void testConstructor5() {
        ExtendedMessageFormat emf = new ExtendedMessageFormat(".[0-9]{2}");
        assertEquals(".[0-9]{2}", emf.toPattern());
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
    public void testSetFormat() {
        ExtendedMessageFormat emf = new ExtendedMessageFormat("");
        Throwable exception = null;
        try {
            emf.setFormat(0, null);
        } catch (UnsupportedOperationException e) {
            exception = e;
        }
        assertNotNull(exception);
    }
    
    @Test
    public void testSetFormatByArgumentIndex() {
        ExtendedMessageFormat emf = new ExtendedMessageFormat("");
        Throwable exception = null;
        try {
            emf.setFormatByArgumentIndex(0, null);
        } catch (UnsupportedOperationException e) {
            exception = e;
        }
        assertNotNull(exception);
    }
    
    @Test
    public void testSetFormats() {
        ExtendedMessageFormat emf = new ExtendedMessageFormat("");
        Throwable exception = null;
        try {
            emf.setFormats(null);
        } catch (UnsupportedOperationException e) {
            exception = e;
        }
        assertNotNull(exception);
    }
    
    @Test
    public void testSetFormatsByArgumentIndex() {
        ExtendedMessageFormat emf = new ExtendedMessageFormat("");
        Throwable exception = null;
        try {
            emf.setFormatsByArgumentIndex(null);
        } catch (UnsupportedOperationException e) {
            exception = e;
        }
        assertNotNull(exception);
    }
    
    @Test
    public void testToPattern() {
        ExtendedMessageFormat emf = new ExtendedMessageFormat(".[0-9]{2}");
        assertEquals(".[0-9]{2}", emf.toPattern());
    }
    
    @Test
    public void testApplyPattern() {
        ExtendedMessageFormat emf = new ExtendedMessageFormat("");
        emf.applyPattern(".[0-9]{2}");
        assertEquals(".[0-9]{2}", emf.toPattern());
    }
    
    @Test
    public void testContainsElements() {
        ExtendedMessageFormat emf = new ExtendedMessageFormat("");
        assertEquals(false, emf.containsElements(null));
    }
}