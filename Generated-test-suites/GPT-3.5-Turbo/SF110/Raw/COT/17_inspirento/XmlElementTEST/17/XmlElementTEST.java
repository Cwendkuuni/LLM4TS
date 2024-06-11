import org.junit.Before;
import org.junit.Test;

import java.util.Enumeration;
import java.util.Hashtable;
import java.util.List;
import java.util.Vector;

import static org.junit.Assert.*;

public class XmlElementTest {

    private XmlElement element;

    @Before
    public void setUp() {
        element = new XmlElement();
    }

    @Test
    public void testAddAttribute() {
        element.addAttribute("name", "value");
        assertEquals("value", element.getAttribute("name"));
    }

    @Test
    public void testGetAttribute() {
        element.addAttribute("name", "value");
        assertEquals("value", element.getAttribute("name")); 
    }

    @Test
    public void testGetAttributeWithDefaultValue() {
        assertEquals("default", element.getAttribute("name", "default")); 
    }

    @Test
    public void testGetAttributes() {
        Hashtable<String, String> attributes = new Hashtable<>();
        element.setAttributes(attributes);
        assertEquals(attributes, element.getAttributes()); 
    }

    @Test
    public void testSetAttributes() {
        Hashtable<String, String> attributes = new Hashtable<>();
        element.setAttributes(attributes);
        assertEquals(attributes, element.getAttributes()); 
    }

    @Test
    public void testGetAttributeNames() {
        element.addAttribute("name1", "value1");
        element.addAttribute("name2", "value2");
        Enumeration attributeNames = element.getAttributeNames();
        assertEquals("name1", attributeNames.nextElement()); 
        assertEquals("name2", attributeNames.nextElement()); 
        assertFalse(attributeNames.hasMoreElements());
    }

    @Test
    public void testAddElement() {
        XmlElement childElement = new XmlElement();
        assertTrue(element.addElement(childElement));
        assertEquals(childElement, element.getElements().get(0));
    }

    @Test
    public void testRemoveElement() {
        XmlElement childElement = new XmlElement();
        element.addElement(childElement);
        assertEquals(childElement, element.removeElement(childElement));
    }

    @Test
    public void testRemoveElementByIndex() {
        XmlElement childElement = new XmlElement();
        element.addElement(childElement);
        assertEquals(childElement, element.removeElement(0));
    }

    @Test
    public void testRemoveAllElements() {
        element.addElement(new XmlElement());
        element.addElement(new XmlElement());
        element.removeAllElements();
        assertTrue(element.getElements().isEmpty());
    }

    @Test
    public void testRemoveFromParent() {
        XmlElement parentElement = new XmlElement();
        element.setParent(parentElement);
        element.removeFromParent();
        assertNull(element.getParent());
        assertTrue(parentElement.getElements().isEmpty());
    }

    @Test
    public void testAppend() {
        XmlElement childElement = new XmlElement();
        childElement.setName("Child");
        element.setName("Parent");
        element.addElement(childElement);
        element.append(childElement);
        assertEquals(2, element.count());
        assertEquals(childElement, element.getElement(1));
    }

    @Test
    public void testInsertElement() {
        XmlElement childElement1 = new XmlElement();
        childElement1.setName("Child1");
        XmlElement childElement2 = new XmlElement();
        childElement2.setName("Child2");
        element.setName("Parent");
        element.addElement(childElement1);
        element.insertElement(childElement2, 1);
        assertEquals(2, element.count());
        assertEquals(childElement2, element.getElement(1));
    }

    @Test
    public void testGetElements() {
        XmlElement childElement1 = new XmlElement();
        element.addElement(childElement1);
        assertSame(childElement1, element.getElements().get(0));
    }

    @Test
    public void testCount() {
        XmlElement childElement1 = new XmlElement();
        XmlElement childElement2 = new XmlElement();
        element.addElement(childElement1);
        element.addElement(childElement2);
        assertEquals(2, element.count());
    }

    @Test
    public void testGetElement() {
        XmlElement childElement1 = new XmlElement();
        XmlElement childElement2 = new XmlElement();
        childElement1.setName("Child1");
        childElement2.setName("Child2");
        element.addElement(childElement1);
        element.addElement(childElement2);
        assertEquals(childElement1, element.getElement("Child1"));
        assertEquals(childElement2, element.getElement("Child2"));
    }

    @Test
    public void testGetElementByIndex() {
        XmlElement childElement1 = new XmlElement();
        XmlElement childElement2 = new XmlElement();
        childElement1.setName("Child1");
        childElement2.setName("Child2");
        element.addElement(childElement1);
        element.addElement(childElement2);
        assertEquals(childElement1, element.getElement(0));
        assertEquals(childElement2, element.getElement(1));
    }

    @Test
    public void testAddSubElementWithPath() {
        XmlElement subElement1 = element.addSubElement("Parent.Child1");
        XmlElement subElement2 = element.addSubElement("Parent.Child2");
        assertEquals("Child1", subElement1.getName());
        assertEquals("Child2", subElement2.getName());
        assertEquals(2, element.count());
    }

    @Test
    public void testAddSubElement() {
        XmlElement subElement = new XmlElement();
        element.addSubElement(subElement);
        assertEquals(subElement, element.getElements().get(0));
    }

    @Test
    public void testAddSubElementWithNameAndData() {
        XmlElement subElement = element.addSubElement("Child", "data");
        assertEquals("Child", subElement.getName());
        assertEquals("data", subElement.getData());
        assertEquals(subElement, element.getElements().get(0));
    }

    @Test
    public void testSetParent() {
        XmlElement parentElement = new XmlElement();
        element.setParent(parentElement);
        assertEquals(parentElement, element.getParent());
    }

    @Test
    public void testGetData() {
        element.setData("data");
        assertEquals("data", element.getData());
    }

    @Test
    public void testGetName() {
        element.setName("name");
        assertEquals("name", element.getName());
    }

    @Test
    public void testPrintNode() {
        XmlElement node = new XmlElement();
        node.setName("Node");
        node.addAttribute("attribute1", "value1");
        node.addAttribute("attribute2", "value2");
        XmlElement child1 = new XmlElement();
        child1.setName("Child1");
        XmlElement child2 = new XmlElement();
        child2.setName("Child2");
        node.addElement(child1);
        node.addElement(child2);
        assertEquals("Node", node.getName());
        // Add more assertions on the printed output
    }

    @Test
    public void testClone() {
        XmlElement clone = (XmlElement) element.clone();
        assertEquals(element.getAttributes(), clone.getAttributes());
        assertEquals(element.getName(), clone.getName());
        assertEquals(element.getData(), clone.getData());
        assertEquals(element.count(), clone.count());
    }

    @Test
    public void testNotifyObservers() {
        // Create a dummy observer and check if it is notified
    }

    @Test
    public void testEquals() {
        XmlElement element1 = new XmlElement();
        XmlElement element2 = element1;
        assertTrue(element1.equals(element2));
        assertFalse(element1.equals(null));
        assertFalse(element1.equals(""));
    }

    @Test
    public void testHashCode() {
        // Test the hashCode implementation
    }
}