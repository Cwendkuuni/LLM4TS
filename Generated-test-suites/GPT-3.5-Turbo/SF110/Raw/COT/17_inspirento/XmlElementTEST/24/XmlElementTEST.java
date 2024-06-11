
import org.junit.Test;
import java.util.*;

public class XmlElementTest {
    
    @Test
    public void testAddAttribute() {
        XmlElement element = new XmlElement();
        element.addAttribute("key", "value");
        assertEquals("value", element.getAttribute("key"));
    }
    
    @Test
    public void testGetAttribute() {
        XmlElement element = new XmlElement();
        element.addAttribute("key", "value");
        assertEquals("value", element.getAttribute("key"));
    }
    
    @Test
    public void testGetAttributeWithDefault() {
        XmlElement element = new XmlElement();
        assertEquals("default", element.getAttribute("key", "default"));
    }
    
    @Test
    public void testGetAttributes() {
        XmlElement element = new XmlElement();
        element.addAttribute("key1", "value1");
        element.addAttribute("key2", "value2");
        Hashtable<String, String> expected = new Hashtable<>();
        expected.put("key1", "value1");
        expected.put("key2", "value2");
        assertEquals(expected, element.getAttributes());
    }
    
    @Test
    public void testSetAttributes() {
        XmlElement element = new XmlElement();
        Hashtable<String, String> attributes = new Hashtable<>();
        attributes.put("key1", "value1");
        attributes.put("key2", "value2");
        element.setAttributes(attributes);
        assertEquals(attributes, element.getAttributes());
    }
    
    @Test
    public void testGetAttributeNames() {
        XmlElement element = new XmlElement();
        element.addAttribute("key1", "value1");
        element.addAttribute("key2", "value2");
        Enumeration<String> expected = element.getAttributes().keys();
        assertEquals(expected, element.getAttributeNames());
    }
    
    @Test
    public void testAddElement() {
        XmlElement element = new XmlElement();
        XmlElement subElement = new XmlElement("sub");
        assertTrue(element.addElement(subElement));
        assertEquals(subElement, element.getElement(0));
    }
    
    // Additional test cases for other methods...
    
}

