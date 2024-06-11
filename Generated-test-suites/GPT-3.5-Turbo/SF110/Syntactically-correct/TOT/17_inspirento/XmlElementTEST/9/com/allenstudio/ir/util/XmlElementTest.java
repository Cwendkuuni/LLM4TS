package com.allenstudio.ir.util;

import org.junit.*;
import static org.junit.Assert.*;

public class XmlElementTest {
    
    XmlElement element;
    XmlElement parent;
    
    @Before
    public void setUp() {
        element = new XmlElement("TestElement");
        parent = new XmlElement("ParentElement");
        parent.addElement(element);
    }
    
    @Test
    public void testAddAttribute() {
        assertEquals(null, element.addAttribute("Attr1", "Value1"));
        assertEquals("Value1", element.addAttribute("Attr1", "Value1")); // Duplicate attribute
    }
    
    @Test
    public void testGetAttribute() {
        element.addAttribute("Attr1", "Value1");
        assertEquals("Value1", element.getAttribute("Attr1"));
        assertEquals("DefaultValue", element.getAttribute("Attr2", "DefaultValue")); // Non-existent attribute with default value
    }
    
    @Test
    public void testGetAttributes() {
        element.addAttribute("Attr1", "Value1");
        assertEquals("Value1", element.getAttributes().get("Attr1"));
    }
    
    @Test
    public void testSetAttributes() {
        XmlElement newElement = new XmlElement("NewElement");
        element.setAttributes(newElement.getAttributes());
        assertEquals(newElement.getAttributes(), element.getAttributes());
    }
    
    @Test
    public void testGetAttributeNames() {
        element.addAttribute("Attr1", "Value1");
        element.addAttribute("Attr2", "Value2");
        assertTrue(element.getAttributeNames().hasMoreElements());
        assertEquals("Attr1", element.getAttributeNames().nextElement());
        assertEquals("Attr2", element.getAttributeNames().nextElement());
    }
    
    @Test
    public void testAddElement() {
        XmlElement childElement = new XmlElement("ChildElement");
        assertTrue(element.addElement(childElement));
    }
    
    @Test
    public void testRemoveElementWithElement() {
        XmlElement childElement = new XmlElement("ChildElement");
        element.addElement(childElement);
        assertEquals(childElement, element.removeElement(childElement));
        assertFalse(element.getElements().contains(childElement));
    }
    
    @Test
    public void testRemoveElementWithIndex() {
        XmlElement childElement = new XmlElement("ChildElement");
        element.addElement(childElement);
        assertEquals(childElement, element.removeElement(0));
        assertFalse(element.getElements().contains(childElement));
    }
    
    @Test
    public void testRemoveAllElements() {
        XmlElement childElement1 = new XmlElement("Child1");
        XmlElement childElement2 = new XmlElement("Child2");
        element.addElement(childElement1);
        element.addElement(childElement2);
        element.removeAllElements();
        assertEquals(0, element.count());
    }
    
    @Test
    public void testRemoveFromParent() {
        element.removeFromParent();
        assertNull(element.getParent());
    }
    
    @Test
    public void testAppend() {
        XmlElement childElement = new XmlElement("ChildElement");
        childElement.setParent(parent);
        element.append(childElement);
        assertEquals(element, childElement.getParent());
    }
    
    @Test
    public void testInsertElement() {
        XmlElement childElement = new XmlElement("ChildElement");
        childElement.setParent(parent);
        element.insertElement(childElement, 0);
        assertEquals(element, childElement.getParent());
        assertEquals(childElement, element.getElement(0));
    }
    
    @Test
    public void testGetElements() {
        XmlElement childElement = new XmlElement("ChildElement");
        element.addElement(childElement);
        assertEquals(childElement, element.getElements().get(0));
    }
    
    @Test
    public void testCount() {
        XmlElement childElement1 = new XmlElement("Child1");
        XmlElement childElement2 = new XmlElement("Child2");
        element.addElement(childElement1);
        element.addElement(childElement2);
        assertEquals(2, element.count());
    }
    
    @Test
    public void testGetElementWithString() {
        XmlElement childElement = new XmlElement("ChildElement");
        element.addElement(childElement);
        assertEquals(childElement, element.getElement("ChildElement"));
        assertNull(element.getElement("NonExistentElement"));
    }
    
    @Test
    public void testGetElementWithIndex() {
        XmlElement childElement = new XmlElement("ChildElement");
        element.addElement(childElement);
        assertEquals(childElement, element.getElement(0));
    }
    
    @Test
    public void testAddSubElementWithString() {
        XmlElement childElement = element.addSubElement("ChildElement");
        assertEquals("ChildElement", childElement.getName());
    }
    
    @Test
    public void testAddSubElementWithElement() {
        XmlElement childElement = new XmlElement("ChildElement");
        element.addSubElement(childElement);
        assertEquals(element, childElement.getParent());
    }
    
    @Test
    public void testAddSubElementWithNameAndData() {
        XmlElement childElement = element.addSubElement("ChildElement", "Data");
        assertEquals("ChildElement", childElement.getName());
        assertEquals("Data", childElement.getData());
    }
    
    @Test
    public void testSetParent() {
        element.setParent(parent);
        assertEquals(parent, element.getParent());
    }
    
    @Test
    public void testGetData() {
        element.setData("Data");
        assertEquals("Data", element.getData());
    }
    
    @Test
    public void testGetName() {
        assertEquals("TestElement", element.getName());
    }
    
    @Test
    public void testPrintNode() {
        // To be implemented
        // This method does not have any specific output that can be tested
    }
    
    @Test
    public void testClone() {
        XmlElement clonedElement = (XmlElement) element.clone();
        assertEquals(element, clonedElement);
    }
    
    @Test
    public void testSetName() {
        element.setName("NewName");
        assertEquals("NewName", element.getName());
    }
    
    @Test
    public void testNotifyObservers() {
        // To be implemented
        // This method overrides a java method and does not have any specific output that can be tested
    }
    
    @Test
    public void testHashCode() {
        XmlElement element1 = new XmlElement("Element1");
        XmlElement element2 = new XmlElement("Element2");
        assertNotEquals(element1.hashCode(), element2.hashCode());
    }
    
    @Test
    public void testEquals() {
        XmlElement element1 = new XmlElement("Element1");
        XmlElement element2 = new XmlElement("Element2");
        assertFalse(element1.equals(element2));
    }
    
}