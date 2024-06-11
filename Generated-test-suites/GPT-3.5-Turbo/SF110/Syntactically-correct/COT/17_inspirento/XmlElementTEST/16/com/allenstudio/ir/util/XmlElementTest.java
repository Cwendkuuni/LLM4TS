
import org.junit.*;
import static org.junit.Assert.*;
import java.util.*;

public class XmlElementTest {
    
    @Test
    public void testAddAttribute() {
        XmlElement xmlElement = new XmlElement("test");
        assertNull(xmlElement.addAttribute(null, "value"));
        assertNull(xmlElement.addAttribute("name", null));
        assertNull(xmlElement.addAttribute(null, null));
        assertNull(xmlElement.addAttribute("name", "value"));
    }
    
    @Test
    public void testGetAttribute() {
        XmlElement xmlElement = new XmlElement("test");
        assertNull(xmlElement.getAttribute(null));
        assertNull(xmlElement.getAttribute("name"));
        
        xmlElement.addAttribute("name", "value");
        assertEquals("value", xmlElement.getAttribute("name"));
    }
    
    @Test
    public void testGetAttributeWithDefaultValue() {
        XmlElement xmlElement = new XmlElement("test");
        assertNull(xmlElement.getAttribute(null, "default"));
        assertEquals("default", xmlElement.getAttribute("name", "default"));
        
        xmlElement.addAttribute("name", "value");
        assertEquals("value", xmlElement.getAttribute("name", "default"));
    }
    
    @Test
    public void testGetAttributes() {
        XmlElement xmlElement = new XmlElement("test");
        Hashtable<String, String> attributes = new Hashtable<>();
        assertEquals(attributes, xmlElement.getAttributes());
        
        xmlElement.addAttribute("name", "value");
        attributes.put("name", "value");
        assertEquals(attributes, xmlElement.getAttributes());
    }
    
    @Test
    public void testSetAttributes() {
        XmlElement xmlElement = new XmlElement("test");
        Hashtable<String, String> attributes = new Hashtable<>();
        attributes.put("name", "value");
        
        xmlElement.setAttributes(attributes);
        assertEquals(attributes, xmlElement.getAttributes());
    }
    
    @Test
    public void testAddElement() {
        XmlElement xmlElement = new XmlElement("test");
        XmlElement subElement = new XmlElement("sub");
        
        assertTrue(xmlElement.addElement(subElement));
        assertEquals(subElement, xmlElement.getElements().get(0));
    }
    
    @Test
    public void testRemoveElementByObject() {
        XmlElement xmlElement = new XmlElement("test");
        XmlElement subElement = new XmlElement("sub");
        xmlElement.addElement(subElement);
        
        assertEquals(subElement, xmlElement.removeElement(subElement));
        assertEquals(0, xmlElement.getElements().size());
    }
    
    @Test
    public void testRemoveElementByIndex() {
        XmlElement xmlElement = new XmlElement("test");
        XmlElement subElement = new XmlElement("sub");
        xmlElement.addElement(subElement);
        
        assertEquals(subElement, xmlElement.removeElement(0));
        assertEquals(0, xmlElement.getElements().size());
    }
    
    @Test
    public void testRemoveAllElements() {
        XmlElement xmlElement = new XmlElement("test");
        xmlElement.addElement(new XmlElement("sub1"));
        xmlElement.addElement(new XmlElement("sub2"));
        xmlElement.addElement(new XmlElement("sub3"));
        
        xmlElement.removeAllElements();
        assertEquals(0, xmlElement.getElements().size());
    }
    
    @Test
    public void testRemoveFromParent() {
        XmlElement parent = new XmlElement("parent");
        XmlElement xmlElement = new XmlElement("test");
        parent.addElement(xmlElement);
        
        xmlElement.removeFromParent();
        assertEquals(0, parent.getElements().size());
        assertNull(xmlElement.getParent());
    }
    
    @Test
    public void testAppend() {
        XmlElement parent = new XmlElement("parent");
        XmlElement xmlElement = new XmlElement("test");
        
        xmlElement.appendToParent();
        assertTrue(parent.getElements().contains(xmlElement));
        assertEquals(parent, xmlElement.getParent());
    }
    
    // Continue adding test cases for other methods...
    
}

