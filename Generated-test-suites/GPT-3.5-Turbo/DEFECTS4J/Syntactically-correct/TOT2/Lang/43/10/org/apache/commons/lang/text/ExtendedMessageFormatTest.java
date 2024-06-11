import org.junit.Test;
import static org.junit.Assert.*;

public class ExtendedMessageFormatTest {
    
    @Test
    public void testConstructorWithPattern() {
        ExtendedMessageFormat emf = new ExtendedMessageFormat("Hello {0}");
        assertNotNull(emf);
    }
    
    @Test
    public void testConstructorWithPatternAndLocale() {
        ExtendedMessageFormat emf = new ExtendedMessageFormat("Hello {0}", Locale.getDefault());
        assertNotNull(emf);
    }
    
    @Test
    public void testConstructorWithPatternAndRegistry() {
        Map registry = new HashMap<>();
        ExtendedMessageFormat emf = new ExtendedMessageFormat("Hello {0}", registry);
        assertNotNull(emf);
    }
    
    @Test
    public void testConstructorWithPatternLocaleAndRegistry() {
        Map registry = new HashMap<>();
        ExtendedMessageFormat emf = new ExtendedMessageFormat("Hello {0}", Locale.getDefault(), registry);
        assertNotNull(emf);
    }
    
    @Test
    public void testApplyPattern() {
        ExtendedMessageFormat emf = new ExtendedMessageFormat("Hello {0}");
        emf.applyPattern("Hi {0}");
        assertEquals("Hi {0}", emf.toPattern());
    }
    
    @Test(expected = IllegalArgumentException.class)
    public void testApplyInvalidPattern() {
        ExtendedMessageFormat emf = new ExtendedMessageFormat("Hello {0}");
        emf.applyPattern("Hi {0, invalidPattern}");
    }
    
    @Test
    public void testGetCustomFormat() {
        Map registry = new HashMap<>();
        registry.put("customFormat", new CustomFormatFactory());
        
        ExtendedMessageFormat emf = new ExtendedMessageFormat("Hello {0, customFormat}");
        emf.applyPattern("Hi {0, customFormat}");
        
        Format format = emf.getFormat("customFormat");
        assertEquals(CustomFormat.class, format.getClass());
    }
    
    @Test(expected = IllegalArgumentException.class)
    public void testInvalidArgumentIndex() {
        ExtendedMessageFormat emf = new ExtendedMessageFormat("Hello {0}");
        int index = emf.readArgumentIndex("Hello {invalidIndex}", new ParsePosition(0));
    }
    
    @Test(expected = IllegalArgumentException.class)
    public void testInvalidFormatDescription() {
        ExtendedMessageFormat emf = new ExtendedMessageFormat("Hello {0}");
        String desc = emf.parseFormatDescription("Hello {0, invalidDesc}", new ParsePosition(0));
    }
    
    @Test
    public void testInsertFormats() {
        ArrayList customPatterns = new ArrayList<>();
        customPatterns.add("formatA");
        customPatterns.add(null);
        
        ExtendedMessageFormat emf = new ExtendedMessageFormat("Hello {0}");
        String result = emf.insertFormats("Hello {0, formatA}", customPatterns);
        
        assertEquals("Hello {0, formatA}", result);
    }
    
    @Test
    public void testContainsElements() {
        ExtendedMessageFormat emf = new ExtendedMessageFormat("Hello {0}");
        assertFalse(emf.containsElements(null));
        
        Collection<String> coll1 = new ArrayList<>();
        assertFalse(emf.containsElements(coll1));
        
        Collection<String> coll2 = new ArrayList<>();
        coll2.add(null);
        assertFalse(emf.containsElements(coll2));
        
        Collection<String> coll3 = new ArrayList<>();
        coll3.add("Element1");
        assertTrue(emf.containsElements(coll3));
    }
}