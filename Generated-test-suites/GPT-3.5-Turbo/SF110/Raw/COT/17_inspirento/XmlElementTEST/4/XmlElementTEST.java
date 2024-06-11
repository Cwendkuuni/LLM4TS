import static org.junit.Assert.*;
import org.junit.Test;

public class XmlElementTest {
    
    @Test
    public void testAddAttribute() {
        XmlElement xmlElement = new XmlElement();
        Object returnValue = xmlElement.addAttribute("name", "value");
        assertNull(returnValue);
        assertEquals("value", xmlElement.getAttribute("name"));
    }
    
    @Test
    public void testGetAttribute() {
        XmlElement xmlElement = new XmlElement();
        xmlElement.addAttribute("name", "value");
        assertEquals("value", xmlElement.getAttribute("name"));
    }
    
    @Test
    public void testGetAttributeWithDefaultValue() {
        XmlElement xmlElement = new XmlElement();
        String defaultValue = "default";
        String attributeName = "name";
        assertNull(xmlElement.getAttribute(attributeName));
        assertEquals(defaultValue, xmlElement.getAttribute(attributeName, defaultValue));
        assertEquals(defaultValue, xmlElement.getAttribute(attributeName));
    }
    
    @Test
    public void testGetAttributes() {
        XmlElement xmlElement = new XmlElement();
        assertNotNull(xmlElement.getAttributes());
    }
    
    @Test
    public void testSetAttributes() {
        XmlElement xmlElement = new XmlElement();
        xmlElement.setAttributes(null);
        assertNull(xmlElement.getAttributes());
        
        Hashtable<String, String> attributes = new Hashtable<>();
        xmlElement.setAttributes(attributes);
        assertEquals(attributes, xmlElement.getAttributes());
    }
    
    @Test
    public void testGetAttributeNames() {
        XmlElement xmlElement = new XmlElement();
        assertNotNull(xmlElement.getAttributeNames());
    }
    
    @Test
    public void testAddElement() {
        XmlElement parent = new XmlElement();
        XmlElement child = new XmlElement();
        child.setName("child");
        
        assertTrue(parent.addElement(child));
        assertEquals(parent, child.getParent());
        assertEquals(1, parent.count());
        assertEquals(child, parent.getElement(0));
    }
    
    @Test
    public void testRemoveElementByObject() {
        XmlElement parent = new XmlElement();
        XmlElement child = new XmlElement();
        child.setName("child");
        
        assertTrue(parent.addElement(child));
        XmlElement removedElement = parent.removeElement(child);
        
        assertNull(removedElement.getParent());
        assertEquals(0, parent.count());
    }
    
    @Test
    public void testRemoveElementByIndex() {
        XmlElement parent = new XmlElement();
        XmlElement child = new XmlElement();
        child.setName("child");
        
        assertTrue(parent.addElement(child));
        XmlElement removedElement = parent.removeElement(0);
        
        assertNull(removedElement.getParent());
        assertEquals(0, parent.count());
    }
    
    @Test
    public void testRemoveAllElements() {
        XmlElement parent = new XmlElement();
        XmlElement child1 = new XmlElement();
        XmlElement child2 = new XmlElement();
        child1.setName("child1");
        child2.setName("child2");
        
        parent.addElement(child1);
        parent.addElement(child2);
        
        parent.removeAllElements();
        
        assertEquals(0, parent.count());
    }
    
    @Test
    public void testRemoveFromParent() {
        XmlElement parent = new XmlElement();
        XmlElement child = new XmlElement();
        child.setName("child");
        
        parent.addElement(child);
        
        child.removeFromParent();
        
        assertNull(child.getParent());
        assertEquals(0, parent.count());
    }
    
    @Test
    public void testAppend() {
        XmlElement parent1 = new XmlElement();
        XmlElement child = new XmlElement();
        child.setName("child");
        
        parent1.addElement(child);
        
        XmlElement parent2 = new XmlElement();
        
        parent2.append(child);
        
        assertEquals(parent2, child.getParent());
        assertEquals(0, parent1.count());
        assertEquals(1, parent2.count());
    }
    
    @Test
    public void testInsertElement() {
        XmlElement parent = new XmlElement();
        XmlElement child1 = new XmlElement();
        child1.setName("child1");
        
        parent.addElement(child1);
        
        XmlElement child2 = new XmlElement();
        child2.setName("child2");
        
        parent.insertElement(child2, 0);
        
        assertEquals(parent, child1.getParent());
        assertEquals(parent, child2.getParent());
        assertEquals(child2, parent.getElement(0));
        assertEquals(child1, parent.getElement(1));
    }
    
    @Test
    public void testGetElements() {
        XmlElement parent = new XmlElement();
        assertNotNull(parent.getElements());
    }
    
    @Test
    public void testCount() {
        XmlElement parent = new XmlElement();
        assertEquals(0, parent.count());
        
        XmlElement child = new XmlElement();
        child.setName("child");
        
        parent.addElement(child);
        
        assertEquals(1, parent.count());
    }
    
    @Test
    public void testGetElement() {
        XmlElement parent = new XmlElement();
        XmlElement child1 = new XmlElement();
        child1.setName("child1");
        XmlElement child2 = new XmlElement();
        child2.setName("child2");
        XmlElement child3 = new XmlElement();
        child3.setName("child3");
        
        parent.addElement(child1);
        parent.addElement(child2);
        child1.addElement(child3);
        
        assertEquals(child1, parent.getElement("child1"));
        assertEquals(child2, parent.getElement("child2"));
        assertEquals(child3, parent.getElement("child1.child3"));
        assertNull(parent.getElement("child3"));
    }
    
    @Test
    public void testGetElementByIndex() {
        XmlElement parent = new XmlElement();
        XmlElement child1 = new XmlElement();
        child1.setName("child1");
        XmlElement child2 = new XmlElement();
        child2.setName("child2");
        XmlElement child3 = new XmlElement();
        child3.setName("child3");
        
        parent.addElement(child1);
        parent.addElement(child2);
        parent.addElement(child3);
        
        assertEquals(child1, parent.getElement(0));
        assertEquals(child2, parent.getElement(1));
        assertEquals(child3, parent.getElement(2));
    }
    
    @Test
    public void testAddSubElement() {
        XmlElement parent = new XmlElement();
        
        XmlElement subElement = parent.addSubElement("sub.element");
        
        assertNotNull(subElement);
        assertEquals("sub", parent.getElement("sub").getName());
        assertEquals("element", parent.getElement("sub.element").getName());
        
    }
    
    @Test
    public void testAddSubElementWithXmlElement() {
        XmlElement parent = new XmlElement();
        XmlElement subElement = new XmlElement("sub.element");
        
        parent.addSubElement(subElement);
        
        assertEquals(parent, subElement.getParent());
        assertEquals(subElement, parent.getElement("sub.element"));
    }
    
    @Test
    public void testAddSubElementWithNameAndData() {
        XmlElement parent = new XmlElement();
        String name = "sub.element";
        String data = "data";
        
        XmlElement subElement = parent.addSubElement(name, data);
        
        assertNotNull(subElement);
        assertEquals(name, subElement.getName());
        assertEquals(data, subElement.getData());
        assertEquals(parent, subElement.getParent());
        assertEquals(subElement, parent.getElement(name));
    }
    
    @Test
    public void testSetParent() {
        XmlElement parent1 = new XmlElement();
        XmlElement parent2 = new XmlElement();
        XmlElement child = new XmlElement();
        child.setName("child");
        
        parent1.addElement(child);
        
        child.setParent(parent2);
        
        assertNull(parent1.getElement("child"));
        assertEquals(parent2, child.getParent());
        assertEquals(child, parent2.getElement("child"));
    }
    
    @Test
    public void testGetData() {
        XmlElement xmlElement = new XmlElement();
        assertNull(xmlElement.getData());
    }
    
    @Test
    public void testSetName() {
        XmlElement xmlElement = new XmlElement();
        
        String name = "name";
        xmlElement.setName(name);
        
        assertEquals(name, xmlElement.getName());
    }
    
    @Test
    public void testNotifyObservers() {
        // Not testing this method as it is hard to test its functionality without deeper knowledge of the system.
    }
    
    @Test
    public void testEquals() {
        XmlElement xmlElement1 = new XmlElement();
        XmlElement xmlElement2 = new XmlElement();
        
        assertTrue(xmlElement1.equals(xmlElement2));
        
        xmlElement2.setName("name");
        assertFalse(xmlElement1.equals(xmlElement2));
    }
    
    @Test
    public void testHashCode() {
        // Not testing this method as it is hard to test its functionality without deeper knowledge of the system.
    }
}