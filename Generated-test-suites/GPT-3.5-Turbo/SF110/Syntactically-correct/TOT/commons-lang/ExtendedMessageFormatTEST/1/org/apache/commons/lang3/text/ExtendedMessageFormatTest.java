import static org.junit.Assert.*;
import org.junit.Test;

public class ExtendedMessageFormatTest {

    @Test
    public void testExtendedMessageFormat() {
        // typical use case
        ExtendedMessageFormat emf1 = new ExtendedMessageFormat("Hello, {0}!");
        assertEquals("Hello, World!", emf1.format(new Object[]{"World"}));
        
        // edge case: empty pattern
        ExtendedMessageFormat emf2 = new ExtendedMessageFormat("");
        assertEquals("", emf2.format(new Object[]{}));
        
        // error scenario: null pattern
        try {
            ExtendedMessageFormat emf3 = new ExtendedMessageFormat(null);
            fail("Expected IllegalArgumentException");
        } catch (IllegalArgumentException e) {
            assertEquals("Invalid format argument index at position 0: null", e.getMessage());
        }
    }
    
    @Test
    public void testToPattern() {
        ExtendedMessageFormat emf = new ExtendedMessageFormat("Hello, {0}!");
        assertEquals("Hello, {0}!", emf.toPattern());
    }
    
    @Test
    public void testApplyPattern() {
        ExtendedMessageFormat emf = new ExtendedMessageFormat("");
        emf.applyPattern("Hello, {0}!");
        assertEquals("Hello, {0}!", emf.toPattern());
    }
    
    @Test(expected = UnsupportedOperationException.class)
    public void testSetFormat() {
        ExtendedMessageFormat emf = new ExtendedMessageFormat("Hello, {0}!");
        emf.setFormat(0, null);
    }
    
    @Test(expected = UnsupportedOperationException.class)
    public void testSetFormatByArgumentIndex() {
        ExtendedMessageFormat emf = new ExtendedMessageFormat("Hello, {0}!");
        emf.setFormatByArgumentIndex(0, null);
    }
    
    @Test(expected = UnsupportedOperationException.class)
    public void testSetFormats() {
        ExtendedMessageFormat emf = new ExtendedMessageFormat("Hello, {0}!");
        emf.setFormats(new Format[]{null});
    }
    
    @Test(expected = UnsupportedOperationException.class)
    public void testSetFormatsByArgumentIndex() {
        ExtendedMessageFormat emf = new ExtendedMessageFormat("Hello, {0}!");
        emf.setFormatsByArgumentIndex(new Format[]{null});
    }
    
    @Test
    public void testEquals() {
        ExtendedMessageFormat emf1 = new ExtendedMessageFormat("Hello, {0}!");
        ExtendedMessageFormat emf2 = new ExtendedMessageFormat("Hello, {0}!");
        assertTrue(emf1.equals(emf2));
    }
    
    @Test
    public void testHashCode() {
        ExtendedMessageFormat emf1 = new ExtendedMessageFormat("Hello, {0}!");
        ExtendedMessageFormat emf2 = new ExtendedMessageFormat("Hello, {0}!");
        assertEquals(emf1.hashCode(), emf2.hashCode());
    }
    
    @Test
    public void testGetFormat() {
        ExtendedMessageFormat emf = new ExtendedMessageFormat("Hello, {0}!");
        assertEquals(null, emf.getFormat("invalid"));
    }
    
    @Test
    public void testReadArgumentIndex() {
        ExtendedMessageFormat emf = new ExtendedMessageFormat("Hello, {0}!");
        assertEquals(0, emf.readArgumentIndex("{0}", new ParsePosition(0)));
    }
    
    @Test
    public void testParseFormatDescription() {
        ExtendedMessageFormat emf = new ExtendedMessageFormat("Hello, {0}!");
        assertEquals("0", emf.parseFormatDescription("{0}", new ParsePosition(0)));
    }
    
    @Test
    public void testInsertFormats() {
        ExtendedMessageFormat emf = new ExtendedMessageFormat("Hello, {0}!");
        assertEquals("Hello, {0}!", emf.insertFormats("Hello, {0}!", new ArrayList<String>()));
    }
    
    @Test
    public void testSeekNonWs() {
        ExtendedMessageFormat emf = new ExtendedMessageFormat("Hello, {0}!");
        emf.seekNonWs("   Hello, {0}!", new ParsePosition(0));
        assertEquals(3, pos.getIndex());
    }
    
    @Test
    public void testNext() {
        ExtendedMessageFormat emf = new ExtendedMessageFormat("Hello, {0}!");
        ParsePosition pos = new ParsePosition(0);
        emf.next(pos);
        assertEquals(1, pos.getIndex());
    }
    
    @Test
    public void testAppendQuotedString() {
        ExtendedMessageFormat emf = new ExtendedMessageFormat("Hello, {0}!");
        StringBuilder sb = new StringBuilder();
        emf.appendQuotedString("'Hello'", new ParsePosition(0), sb, false);
        assertEquals("'Hello'", sb.toString());
    }
    
    @Test
    public void testGetQuotedString() {
        ExtendedMessageFormat emf = new ExtendedMessageFormat("Hello, {0}!");
        emf.getQuotedString("'Hello'", new ParsePosition(0), false);
        // no assertion for void methods
    }
    
    @Test
    public void testContainsElements() {
        ExtendedMessageFormat emf = new ExtendedMessageFormat("Hello, {0}!");
        assertFalse(emf.containsElements(null));
        
        ArrayList<String> list1 = new ArrayList<>();
        assertFalse(emf.containsElements(list1));
        
        ArrayList<String> list2 = new ArrayList<>();
        list2.add(null);
        assertTrue(emf.containsElements(list2));
    }
}