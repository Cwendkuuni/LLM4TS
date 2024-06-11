import org.junit.Before;
import org.junit.Test;

import java.util.*;

import static org.junit.Assert.*;

public class XmlElementTest {
    
    private XmlElement xmlElement;
    
    @Before
    public void setUp() {
        xmlElement = new XmlElement();
    }
    
    @Test
    public void testAddAttribute() {
        assertNull(xmlElement.addAttribute("name", "value"));
        assertEquals("value", xmlElement.getAttribute("name"));
    }
    
    @Test
    public void testGetAttribute() {
        xmlElement.addAttribute("name", "value");
        assertEquals("value", xmlElement.getAttribute("name"));
    }
    
    @Test
    public void testGetAttributeWithDefaultValue() {
        assertEquals("default", xmlElement.getAttribute("name", "default"));
    }
    
    @Test
    public void testGetAttributes() {
        Hashtable<String, String> attributes = new Hashtable<>();
        xmlElement.setAttributes(attributes);
        assertEquals(attributes, xmlElement.getAttributes());
    }
    
    @Test
    public void testGetAttributeNames() {
        Enumeration attributeNames = xmlElement.getAttributeNames();
        assertFalse(attributeNames.hasMoreElements());
    }
    
    @Test
    public void testAddElement() {
        XmlElement element = new XmlElement("subElement");
        assertTrue(xmlElement.addElement(element));
        assertEquals(element, xmlElement.getElements().get(0));
    }
    
    @Test
    public void testRemoveElementByObject() {
        XmlElement element = new XmlElement("subElement");
        xmlElement.addElement(element);
        assertEquals(element, xmlElement.removeElement(element));
        assertEquals(0, xmlElement.count());
    }
    
    @Test
    public void testRemoveElementByIndex() {
        XmlElement element = new XmlElement("subElement");
        xmlElement.addElement(element);
        assertEquals(element, xmlElement.removeElement(0));
        assertEquals(0, xmlElement.count());
    }
    
    @Test
    public void testRemoveAllElements() {
        xmlElement.addElement(new XmlElement("subElement1"));
        xmlElement.addElement(new XmlElement("subElement2"));
        xmlElement.removeAllElements();
        assertEquals(0, xmlElement.count());
    }
    
    @Test
    public void testRemoveFromParent() {
        XmlElement parent = new XmlElement("parent");
        XmlElement child = new XmlElement("child");
        parent.addElement(child);
        child.removeFromParent();
        assertEquals(0, parent.count());
        assertNull(child.getParent());
    }
    
    @Test
    public void testAppend() {
        XmlElement parent = new XmlElement("parent");
        XmlElement child = new XmlElement("child");
        child.removeFromParent();
        parent.append(child);
        assertEquals(parent, child.getParent());
        assertEquals(child, parent.getElements().get(0));
    }
    
    @Test
    public void testInsertElement() {
        XmlElement parent = new XmlElement("parent");
        XmlElement child1 = new XmlElement("child1");
        XmlElement child2 = new XmlElement("child2");
        parent.addElement(child1);
        parent.insertElement(child2, 0);
        assertEquals(parent, child2.getParent());
        assertEquals(child2, parent.getElements().get(0));
    }
    
    @Test
    public void testCount() {
        xmlElement.addElement(new XmlElement("subElement1"));
        xmlElement.addElement(new XmlElement("subElement2"));
        assertEquals(2, xmlElement.count());
    }
    
    @Test
    public void testGetElement() {
        XmlElement subElement = new XmlElement("subElement");
        xmlElement.addElement(subElement);
        assertEquals(subElement, xmlElement.getElement("subElement"));
    }
    
    @Test
    public void testGetElementByIndex() {
        XmlElement subElement = new XmlElement("subElement");
        xmlElement.addElement(subElement);
        assertEquals(subElement, xmlElement.getElement(0));
    }
    
    @Test
    public void testAddSubElementWithPath() {
        xmlElement.addSubElement("path.to.subElement");
        assertNotNull(xmlElement.getElement("path.to.subElement"));
    }
    
    @Test
    public void testAddSubElement() {
        XmlElement subElement = new XmlElement("subElement");
        xmlElement.addSubElement(subElement);
        assertEquals(subElement, xmlElement.getElements().get(0));
    }
    
    @Test
    public void testAddSubElementWithNameAndData() {
        XmlElement subElement = xmlElement.addSubElement("subElement", "data");
        assertEquals("subElement", subElement.getName());
        assertEquals("data", subElement.getData());
    }
    
    @Test
    public void testSetParent() {
        XmlElement parent = new XmlElement("parent");
        xmlElement.setParent(parent);
        assertEquals(parent, xmlElement.getParent());
    }
    
    @Test
    public void testSetData() {
        xmlElement.setData("data");
        assertEquals("data", xmlElement.getData());
    }
    
    @Test
    public void testGetName() {
        assertEquals(null, xmlElement.getName());
        xmlElement.setName("name");
        assertEquals("name", xmlElement.getName());
    }
    
    @Test
    public void testEquals() {
        XmlElement element1 = new XmlElement("element");
        XmlElement element2 = new XmlElement("element");
        assertTrue(element1.equals(element2));
    }
    
    @Test
    public void testHashCode() {
        XmlElement element1 = new XmlElement("element");
        XmlElement element2 = new XmlElement("element");
        assertEquals(element1.hashCode(), element2.hashCode());
    }
}