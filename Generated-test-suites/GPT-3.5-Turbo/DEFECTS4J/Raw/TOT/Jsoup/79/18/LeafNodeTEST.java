

import org.junit.Test;
import org.jsoup.nodes.*;
import static org.junit.Assert.*;

public class LeafNodeTest {
    
    @Test
    public void testHasAttributes() {
        // Typical use case
        Element element = new Element("div");
        element.attr("id", "test");
        
        assertTrue(element.hasAttr("id"));
        
        // Edge case
        element.removeAttr("id");
        assertFalse(element.hasAttr("id"));
        
        // Error scenario
        assertNull(element.attr("class"));
    }
    
    @Test
    public void testAttributes() {
        // Typical use case
        Element element1 = new Element("div");
        element1.attr("id", "test");
        element1.attr("class", "test");
        
        Attributes attributes = element1.attributes();
        
        assertNotNull(attributes);
        assertEquals(2, attributes.size());
        
        // Edge case
        Element element2 = new Element("div");
        
        attributes = element2.attributes();
        
        assertNotNull(attributes);
        assertEquals(1, attributes.size()); // default attribute
        
        // Error scenario
        Element element3 = new Element("div");
        element3.removeAttr("id");
        
        attributes = element3.attributes();
        
        assertNotNull(attributes);
        assertEquals(0, attributes.size());
    }
    
    @Test
    public void testCoreValue() {
        // Typical use case
        Element element = new Element("div");
        element.attr("id", "test");
        
        assertEquals("test", element.coreValue());
        
        // Edge case
        Element element2 = new Element("div");
        
        assertNull(element2.coreValue());
        
        // Error scenario
        Element element3 = new Element("div");
        element3.removeAttr("id");
        
        assertNull(element3.coreValue());
    }
    
    @Test
    public void testAttr() {
        // Typical use case
        Element element = new Element("div");
        element.attr("id", "test");
        
        assertEquals("test", element.attr("id"));
        
        // Edge case
        Element element2 = new Element("div");
        
        assertNull(element2.attr("id"));
        
        // Error scenario
        Element element3 = new Element("div");
        element3.removeAttr("id");
        
        assertNull(element3.attr("id"));
    }
    
    @Test
    public void testRemoveAttr() {
        // Typical use case
        Element element = new Element("div");
        element.attr("id", "test");
        
        element.removeAttr("id");
        
        assertFalse(element.hasAttr("id"));
        
        // Edge case
        Element element2 = new Element("div");
        
        element2.removeAttr("id");
        
        assertFalse(element2.hasAttr("id"));
        
        // Error scenario
        Element element3 = new Element("div");
        element3.removeAttr("class");
    }
    
    @Test
    public void testAbsUrl() {
        // Typical use case
        Element element = new Element("a");
        element.attr("href", "/about.html");
        element.baseUri("https://www.example.com/");
        
        assertEquals("https://www.example.com/about.html", element.absUrl("href"));
        
        // Edge case
        Element element2 = new Element("a");
        element2.attr("href", "/about.html");
        
        assertEquals("/about.html", element2.absUrl("href"));
        
        // Error scenario
        Element element3 = new Element("a");
        
        assertNull(element3.absUrl("href"));
    }
}

