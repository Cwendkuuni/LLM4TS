

import static org.junit.Assert.*;

import java.util.Enumeration;
import java.util.Hashtable;

import org.junit.Before;
import org.junit.Test;

public class XmlElementTest {

    private XmlElement xmlElement;

    @Before
    public void setup() {
        xmlElement = new XmlElement();
    }
    
    @Test
    public void testAddAttribute() {
        Object returnValue = xmlElement.addAttribute("name", "value");
        assertNull(returnValue);
        
        returnValue = xmlElement.addAttribute("name", "value2");
        assertEquals("value", returnValue);
    }
    
    @Test
    public void testGetAttribute() {
        xmlElement.addAttribute("name", "value");
        String attributeValue = xmlElement.getAttribute("name");
        assertEquals("value", attributeValue);
    }
    
    @Test
    public void testGetAttribute_withDefaultValue() {
        String defaultValue = "defaultValue";
        String attributeValue = xmlElement.getAttribute("name", defaultValue);
        assertEquals(defaultValue, attributeValue);
    }
    
    @Test
    public void testGetAttributes() {
        Hashtable<String, String> attributes = xmlElement.getAttributes();
        assertNotNull(attributes);
    }
    
    @Test
    public void testSetAttributes() {
        Hashtable<String, String> attributes = new Hashtable<String, String>();
        xmlElement.setAttributes(attributes);
        assertEquals(attributes, xmlElement.getAttributes());
    }
    
    @Test
    public void testGetAttributeNames() {
        Enumeration attributeNames = xmlElement.getAttributeNames();
        assertNotNull(attributeNames);
    }
    
    @Test
    public void testAddElement() {
        XmlElement childElement = new XmlElement("child");
        boolean added = xmlElement.addElement(childElement);
        assertTrue(added);
        assertTrue(xmlElement.getElements().contains(childElement));
        assertEquals(xmlElement, childElement.getParent());
    }
    
    @Test
    public void testRemoveElement_byElement() {
        XmlElement childElement = new XmlElement("child");
        xmlElement.addElement(childElement);
        
        XmlElement removedElement = xmlElement.removeElement(childElement);
        assertEquals(childElement, removedElement);
        assertFalse(xmlElement.getElements().contains(childElement));
    }
    
    @Test
    public void testRemoveElement_byIndex() {
        XmlElement childElement = new XmlElement("child");
        xmlElement.addElement(childElement);
        
        XmlElement removedElement = xmlElement.removeElement(0);
        assertEquals(childElement, removedElement);
        assertFalse(xmlElement.getElements().contains(childElement));
    }
    
    @Test
    public void testRemoveAllElements() {
        XmlElement childElement = new XmlElement("child");
        xmlElement.addElement(childElement);
        
        xmlElement.removeAllElements();
        assertEquals(0, xmlElement.count());
    }
    
    @Test
    public void testRemoveFromParent() {
        XmlElement parentElement = new XmlElement("parent");
        xmlElement.setParent(parentElement);
        
        xmlElement.removeFromParent();
        assertNull(xmlElement.getParent());
    }
    
    @Test
    public void testAppend() {
        XmlElement parentElement = new XmlElement("parent");
        xmlElement.removeFromParent();
        
        parentElement.append(xmlElement);
        assertTrue(parentElement.getElements().contains(xmlElement));
        assertEquals(parentElement, xmlElement.getParent());
    }
    
    @Test
    public void testInsertElement() {
        XmlElement childElement1 = new XmlElement("child1");
        XmlElement childElement2 = new XmlElement("child2");
        xmlElement.addElement(childElement1);
        
        xmlElement.insertElement(childElement2, 0);
        assertTrue(xmlElement.getElements().contains(childElement2));
        assertTrue(xmlElement.getElements().indexOf(childElement2) == 0);
        
    }
    
    @Test
    public void testGetElements() {
        assertNotNull(xmlElement.getElements());
    }
    
    @Test
    public void testCount() {
        assertEquals(0, xmlElement.count());
    }
    
    @Test
    public void testGetElement() {
        XmlElement childElement = new XmlElement("child");
        xmlElement.addElement(childElement);
        
        XmlElement queriedElement = xmlElement.getElement("child");
        assertEquals(childElement, queriedElement);
    }
    
    @Test
    public void testGetElement_withNestedPath() {
        XmlElement childElement1 = new XmlElement("child1");
        xmlElement.addElement(childElement1);
        
        XmlElement childElement2 = new XmlElement("child2");
        childElement1.addElement(childElement2);
        
        XmlElement queriedElement = xmlElement.getElement("child1.child2");
        assertEquals(childElement2, queriedElement);
    }
    
    @Test
    public void testGetElement_withInvalidPath() {
        XmlElement childElement = new XmlElement("child");
        xmlElement.addElement(childElement);
        
        XmlElement queriedElement = xmlElement.getElement("invalid");
        assertNull(queriedElement);
    }
    
    @Test
    public void testGetElement_byIndex() {
        XmlElement childElement = new XmlElement("child");
        xmlElement.addElement(childElement);
        
        XmlElement queriedElement = xmlElement.getElement(0);
        assertEquals(childElement, queriedElement);
    }
    
    @Test
    public void testAddSubElement_withNestedPath() {
        XmlElement parentElement = new XmlElement("parent");
        xmlElement.removeFromParent();
        
        XmlElement childElement = parentElement.addSubElement("child1.child2");
        assertTrue(parentElement.getElements().contains(childElement));
        assertEquals(childElement, parentElement.getElement("child1.child2"));
    }
    
    @Test
    public void testAddSubElement_byElement() {
        XmlElement childElement = new XmlElement("child");
        xmlElement.addSubElement(childElement);
        assertTrue(xmlElement.getElements().contains(childElement));
    }
    
    @Test
    public void testAddSubElement_byNameAndData() {
        String name = "name";
        String data = "data";
        XmlElement childElement = xmlElement.addSubElement(name, data);
        assertTrue(xmlElement.getElements().contains(childElement));
        assertEquals(name, childElement.getName());
        assertEquals(data, childElement.getData());
    }
    
    @Test
    public void testSetParent() {
        XmlElement parentElement = new XmlElement("parent");
        xmlElement.setParent(parentElement);
        assertEquals(parentElement, xmlElement.getParent());
    }
    
    @Test
    public void testGetData() {
        assertNull(xmlElement.getData());
    }
    
    @Test
    public void testSetName() {
        String name = "name";
        xmlElement.setName(name);
        assertEquals(name, xmlElement.getName());
    }
    
    @Test
    public void testNotifyObservers() {
        xmlElement.notifyObservers();
        assertTrue(xmlElement.hasChanged());
    }
    
    @Test
    public void testEquals_sameObject() {
        assertTrue(xmlElement.equals(xmlElement));
    }
    
    @Test
    public void testEquals_nullObject() {
        assertFalse(xmlElement.equals(null));
    }
    
    @Test
    public void testEquals_differentClass() {
        assertFalse(xmlElement.equals("stringValue"));
    }
    
    @Test
    public void testEquals_differentAttributes() {
        XmlElement xmlElement2 = new XmlElement();
        assertFalse(xmlElement.equals(xmlElement2));
    }
    
    @Test
    public void testEquals_sameAttributes() {
        Hashtable<String, String> attributes = new Hashtable<String, String>();
        attributes.put("name", "value");
        xmlElement.setAttributes(attributes);
        
        XmlElement xmlElement2 = new XmlElement();
        xmlElement2.setAttributes(attributes);
        
        assertTrue(xmlElement.equals(xmlElement2));
    }
    
    @Test
    public void testEquals_differentData() {
        xmlElement.setData("data1");
        
        XmlElement xmlElement2 = new XmlElement();
        xmlElement2.setData("data2");
        
        assertFalse(xmlElement.equals(xmlElement2));
    }
    
    @Test
    public void testEquals_sameData() {
        xmlElement.setData("data");
        
        XmlElement xmlElement2 = new XmlElement();
        xmlElement2.setData("data");
        
        assertTrue(xmlElement.equals(xmlElement2));
    }
    
    @Test
    public void testEquals_differentName() {
        xmlElement.setName("name1");
        
        XmlElement xmlElement2 = new XmlElement();
        xmlElement2.setName("name2");
        
        assertFalse(xmlElement.equals(xmlElement2));
    }
    
    @Test
    public void testEquals_sameName() {
        xmlElement.setName("name");
        
        XmlElement xmlElement2 = new XmlElement();
        xmlElement2.setName("name");
        
        assertTrue(xmlElement.equals(xmlElement2));
    }
    
    @Test
    public void testEquals_differentSubElements() {
        XmlElement childElement1 = new XmlElement("child1");
        xmlElement.addElement(childElement1);
        
        XmlElement xmlElement2 = new XmlElement();
        assertTrue(childElement1.getElements().isEmpty());
        assertFalse(xmlElement.equals(xmlElement2));
    }
    
    @Test
    public void testEquals_sameSubElements() {
        XmlElement childElement1 = new XmlElement("child1");
        xmlElement.addElement(childElement1);
        
        XmlElement xmlElement2 = new XmlElement();
        xmlElement2.addElement(childElement1);
        
        assertTrue(xmlElement.equals(xmlElement2));
    }
    
    @Test
    public void testHashCode() {
        Hashtable<String, String> attributes = new Hashtable<String, String>();
        attributes.put("name", "value");
        xmlElement.setAttributes(attributes);
        
        xmlElement.setData("data");
        xmlElement.setName("name");
        
        XmlElement childElement1 = new XmlElement("child1");
        xmlElement.addElement(childElement1);
        
        int expectedHashCode = 23 + attributes.hashCode() * 13 + "data".hashCode() * 17 + "name".hashCode() * 29 
            + xmlElement.getElements().hashCode() * 57;
        assertEquals(expectedHashCode, xmlElement.hashCode());
    }
}

