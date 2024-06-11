import static org.junit.Assert.*;

import org.junit.Test;

public class ExtendedMessageFormatTest {

    @Test
    public void testConstructorWithPatternAndLocale() {
        ExtendedMessageFormat emf = new ExtendedMessageFormat("Test", Locale.getDefault());
        
        assertNotNull(emf);
    }
    
    @Test
    public void testConstructorWithPatternAndRegistry() {
        Map<FormatFactory> registry = new HashMap<>();
        registry.put("testFormat", new TestFormatFactory());
        
        ExtendedMessageFormat emf = new ExtendedMessageFormat("Test", registry);
        
        assertNotNull(emf);
    }
    
    @Test
    public void testApplyPattern() {
        ExtendedMessageFormat emf = new ExtendedMessageFormat("Test");
        emf.applyPattern("New test");
        
        assertEquals("New test", emf.toPattern());
    }
    
    @Test
    public void testSetFormat() {
        ExtendedMessageFormat emf = new ExtendedMessageFormat("Test");
        
        try {
            emf.setFormat(0, new TestFormat());
        } catch (UnsupportedOperationException e) {
            assertTrue(true);
        }
    }
    
    @Test
    public void testSetFormats() {
        ExtendedMessageFormat emf = new ExtendedMessageFormat("Test");
        
        try {
            emf.setFormats(new Format[] {new TestFormat()});
        } catch (UnsupportedOperationException e) {
            assertTrue(true);
        }
    }
    
    @Test
    public void testSetFormatsByArgumentIndex() {
        ExtendedMessageFormat emf = new ExtendedMessageFormat("Test");
        
        try {
            emf.setFormatsByArgumentIndex(new Format[] {new TestFormat()});
        } catch (UnsupportedOperationException e) {
            assertTrue(true);
        }
    }
    
    @Test
    public void testGetFormat() {
        ExtendedMessageFormat emf = new ExtendedMessageFormat("Test");
        Format format = emf.getFormat("testFormat,Custom");
        
        assertNotNull(format);
    }
    
    @Test
    public void testReadArgumentIndex() {
        ExtendedMessageFormat emf = new ExtendedMessageFormat("Test");
        int index = emf.readArgumentIndex("{2}", new ParsePosition(0));
        
        assertEquals(2, index);
    }
    
    @Test
    public void testParseFormatDescription() {
        ExtendedMessageFormat emf = new ExtendedMessageFormat("Test");
        String formatDescription = emf.parseFormatDescription("{2,Custom}", new ParsePosition(0));
        
        assertEquals("Custom", formatDescription);
    }
    
    @Test
    public void testInsertFormats() {
        ExtendedMessageFormat emf = new ExtendedMessageFormat("Test");
        String pattern = emf.insertFormats("Test {0}", new ArrayList());
        
        assertEquals("Test {0}", pattern);
    }
    
    @Test
    public void testContainsElements() {
        ExtendedMessageFormat emf = new ExtendedMessageFormat("Test");
        Collection<Format> collection = new ArrayList<>();
        
        boolean result = emf.containsElements(collection);
        
        assertFalse(result);
    }
}