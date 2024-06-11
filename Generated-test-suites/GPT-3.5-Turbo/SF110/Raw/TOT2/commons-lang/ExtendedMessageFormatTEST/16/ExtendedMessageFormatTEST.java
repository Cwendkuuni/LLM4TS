import org.junit.Test;
import static org.junit.Assert.*;

public class ExtendedMessageFormatTest {
    
    @Test
    public void testToPattern() {
        ExtendedMessageFormat emf = new ExtendedMessageFormat("Test");
        assertEquals("Test", emf.toPattern());
    }
    
    @Test
    public void testApplyPattern() {
        ExtendedMessageFormat emf = new ExtendedMessageFormat("Test");
        emf.applyPattern("NewTest");
        assertEquals("NewTest", emf.toPattern());
    }
    
    @Test(expected = IllegalArgumentException.class)
    public void testApplyPatternWithInvalidPattern() {
        ExtendedMessageFormat emf = new ExtendedMessageFormat("Test");
        emf.applyPattern("Invalid {pattern}");
    }
    
    @Test(expected = UnsupportedOperationException.class)
    public void testSetFormat() {
        ExtendedMessageFormat emf = new ExtendedMessageFormat("Test");
        emf.setFormat(0, null);
    }
    
    @Test
    public void testEquals() {
        ExtendedMessageFormat emf1 = new ExtendedMessageFormat("Test");
        ExtendedMessageFormat emf2 = new ExtendedMessageFormat("Test");
        assertTrue(emf1.equals(emf2));
    }
    
    @Test
    public void testHashCode() {
        ExtendedMessageFormat emf = new ExtendedMessageFormat("Test");
        assertEquals(emf.hashCode(), emf.hashCode());
    }
    
    @Test
    public void testGetFormat() {
        ExtendedMessageFormat emf = new ExtendedMessageFormat("Test");
        assertNull(emf.getFormat("InvalidFormat"));
    }
    
    @Test(expected = IllegalArgumentException.class)
    public void testReadArgumentIndexWithInvalidPattern() {
        ExtendedMessageFormat emf = new ExtendedMessageFormat("Test");
        emf.readArgumentIndex("InvalidPattern", null);
    }
    
    @Test(expected = IllegalArgumentException.class)
    public void testParseFormatDescriptionWithInvalidPattern() {
        ExtendedMessageFormat emf = new ExtendedMessageFormat("Test");
        emf.parseFormatDescription("InvalidPattern", null);
    }
    
    @Test
    public void testInsertFormats() {
        ExtendedMessageFormat emf = new ExtendedMessageFormat("Test");
        assertEquals("Test", emf.insertFormats("Test", null));
    }
    
    @Test
    public void testSeekNonWs() {
        ExtendedMessageFormat emf = new ExtendedMessageFormat("Test");
        emf.seekNonWs("Test", null);
    }
    
    @Test
    public void testNext() {
        ExtendedMessageFormat emf = new ExtendedMessageFormat("Test");
        assertNotNull(emf.next(null));
    }
    
    @Test
    public void testAppendQuotedString() {
        ExtendedMessageFormat emf = new ExtendedMessageFormat("Test");
        assertNull(emf.appendQuotedString("Test", null, null, true));
    }
    
    @Test
    public void testGetQuotedString() {
        ExtendedMessageFormat emf = new ExtendedMessageFormat("Test");
        emf.getQuotedString("Test", null, true);
    }
    
    @Test
    public void testContainsElementsWithEmptyCollection() {
        ExtendedMessageFormat emf = new ExtendedMessageFormat("Test");
        assertFalse(emf.containsElements(new ArrayList<>()));
    }

}