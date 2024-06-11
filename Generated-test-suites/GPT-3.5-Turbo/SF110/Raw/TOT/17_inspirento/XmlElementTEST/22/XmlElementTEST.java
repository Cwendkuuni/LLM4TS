
package com.allenstudio.ir.util;

import org.junit.Before;
import org.junit.Test;

import java.util.Hashtable;
import java.util.List;
import java.util.Vector;

import static org.junit.Assert.*;

public class XmlElementTest {

    private XmlElement xmlElement;

    @Before
    public void setUp() {
        xmlElement = new XmlElement();
    }

    @Test
    public void testAddAttribute_Success() {
        assertEquals(null, xmlElement.addAttribute("key1", "value1"));
    }

    @Test
    public void testAddAttribute_NullValue() {
        assertEquals(null, xmlElement.addAttribute("key1", null));
    }

    @Test
    public void testGetAttribute_Success() {
        xmlElement.addAttribute("key1", "value1");
        assertEquals("value1", xmlElement.getAttribute("key1"));
    }

    @Test
    public void testGetAttribute_Null() {
        assertEquals(null, xmlElement.getAttribute("key1"));
    }

    @Test
    public void testGetAttribute_DefaultValueDoesNotExist() {
        assertEquals("defaultValue", xmlElement.getAttribute("key1", "defaultValue"));
    }

    @Test
    public void testGetAttribute_DefaultValueExists() {
        xmlElement.addAttribute("key1", "value1");
        assertEquals("value1", xmlElement.getAttribute("key1", "defaultValue"));
    }

    @Test
    public void testGetAttributes() {
        Hashtable<String, String> attributes = new Hashtable<>();
        attributes.put("key1", "value1");
        xmlElement.setAttributes(attributes);
        assertEquals(attributes, xmlElement.getAttributes());
    }

    @Test
    public void testSetAttributes() {
        Hashtable<String, String> attributes = new Hashtable<>();
        attributes.put("key1", "value1");
        xmlElement.setAttributes(attributes);
        assertEquals(attributes, xmlElement.getAttributes());
    }

    // Other test cases for the remaining methods of XmlElement class

    @Test
    public void testEquals_NullObject() {
        assertFalse(xmlElement.equals(null));
    }

    @Test
    public void testEquals_SameObject() {
        assertTrue(xmlElement.equals(xmlElement));
    }

    @Test
    public void testEquals_DifferentClassObject() {
        Object obj = new Object();
        assertFalse(xmlElement.equals(obj));
    }

    @Test
    public void testEquals_DifferentAttributes() {
        XmlElement other = new XmlElement();
        other.addAttribute("key1", "value1");
        assertFalse(xmlElement.equals(other));
    }

    @Test
    public void testEquals_SameAttributes() {
        XmlElement other = new XmlElement();
        xmlElement.addAttribute("key1", "value1");
        other.addAttribute("key1", "value1");
        assertTrue(xmlElement.equals(other));
    }

    @Test
    public void testAddElement_Success() {
        XmlElement element = new XmlElement();
        assertTrue(xmlElement.addElement(element));
        assertEquals(1, xmlElement.count());
    }

    @Test
    public void testAddElement_Failure() {
        assertFalse(xmlElement.addElement(null));
        assertEquals(0, xmlElement.count());
    }

    @Test
    public void testRemoveElement_Success() {
        XmlElement element = new XmlElement();
        xmlElement.addElement(element);
        assertEquals(element, xmlElement.removeElement(element));
        assertEquals(0, xmlElement.count());
    }

    @Test
    public void testRemoveElement_Failure() {
        XmlElement element1 = new XmlElement();
        XmlElement element2 = new XmlElement();
        xmlElement.addElement(element1);

        assertNull(xmlElement.removeElement(element2));
        assertEquals(1, xmlElement.count());
    }

    @Test
    public void testRemoveElementByIndex_Success() {
        XmlElement element = new XmlElement();
        xmlElement.addElement(element);
        assertEquals(element, xmlElement.removeElement(0));
        assertEquals(0, xmlElement.count());
    }

    @Test
    public void testRemoveElementByIndex_Failure() {
        XmlElement element = new XmlElement();
        xmlElement.addElement(element);

        assertNull(xmlElement.removeElement(1));
        assertEquals(1, xmlElement.count());
    }

    @Test
    public void testRemoveAllElements() {
        XmlElement element1 = new XmlElement();
        XmlElement element2 = new XmlElement();
        xmlElement.addElement(element1);
        xmlElement.addElement(element2);

        xmlElement.removeAllElements();
        assertEquals(0, xmlElement.count());
    }

    @Test
    public void testRemoveFromParent_Success() {
        XmlElement parentElement = new XmlElement();
        parentElement.addElement(xmlElement);
        xmlElement.removeFromParent();
        assertEquals(0, parentElement.count());
        assertNull(xmlElement.getParent());
    }

    @Test
    public void testRemoveFromParent_NoParent() {
        xmlElement.removeFromParent();
    }

    @Test
    public void testAppend() {
        XmlElement element = new XmlElement();
        element.setParent(xmlElement);
        xmlElement.append(element);
        assertEquals(1, xmlElement.count());
    }

    @Test
    public void testInsertElement_Success() {
        XmlElement element1 = new XmlElement();
        XmlElement element2 = new XmlElement();
        xmlElement.addElement(element1);
        xmlElement.insertElement(element2, 0);
        assertEquals(2, xmlElement.count());
        assertEquals(element2, xmlElement.getElement(0));
    }

    @Test
    public void testInsertElement_WrongIndex() {
        XmlElement element = new XmlElement();
        xmlElement.addElement(element);

        xmlElement.insertElement(new XmlElement(), 2);
        assertEquals(1, xmlElement.count());
    }

    @Test
    public void testGetElement_Success() {
        XmlElement element = new XmlElement();
        element.setName("element1");
        xmlElement.addElement(element);
        assertEquals(element, xmlElement.getElement("element1"));
    }

    @Test
    public void testGetElement_Null() {
        assertNull(xmlElement.getElement("element1"));
    }

    @Test
    public void testGetElement_NestedElement() {
        XmlElement parentElement = new XmlElement();
        xmlElement.addElement(parentElement);
        XmlElement childElement = new XmlElement();
        parentElement.addElement(childElement);
        assertEquals(childElement, xmlElement.getElement("parent.child"));
    }

    @Test
    public void testGetElementByIndex() {
        XmlElement element = new XmlElement();
        xmlElement.addElement(element);
        assertEquals(element, xmlElement.getElement(0));
    }

    @Test
    public void testAddSubElement_ExistingPath() {
        XmlElement parentElement = new XmlElement();
        xmlElement.addElement(parentElement);
        parentElement.addElement(new XmlElement("child1"));

        XmlElement subElement = new XmlElement("child1");
        assertNull(xmlElement.addSubElement(subElement));
        assertEquals(1, parentElement.count());
    }

    @Test
    public void testAddSubElement_NewPath() {
        XmlElement parentElement = new XmlElement();
        xmlElement.addElement(parentElement);

        XmlElement subElement = new XmlElement("child1");
        assertEquals(subElement, xmlElement.addSubElement(subElement));
        assertEquals(1, parentElement.count());
    }

    @Test
    public void testAddSubElementWithParent_Success() {
        XmlElement parentElement = new XmlElement();
        xmlElement.addElement(parentElement);
        XmlElement childElement = new XmlElement();

        assertEquals(childElement, xmlElement.addSubElement(childElement));
        assertEquals(xmlElement, childElement.getParent());
        assertEquals(1, parentElement.count());
    }

    @Test
    public void testAddSubElementWithNameAndData() {
        XmlElement subElement = xmlElement.addSubElement("child1", "data");
        assertEquals(1, xmlElement.count());
        assertEquals("child1", subElement.getName());
        assertEquals("data", subElement.getData());
    }

    @Test
    public void testSetParent() {
        XmlElement parentElement = new XmlElement();
        xmlElement.setParent(parentElement);
        assertEquals(parentElement, xmlElement.getParent());
    }

    @Test
    public void testGetData() {
        xmlElement.setData("data");
        assertEquals("data", xmlElement.getData());
    }

    @Test
    public void testSetName() {
        xmlElement.setName("name");
        assertEquals("name", xmlElement.getName());
    }

    @Test
    public void testPrintNode() {
        XmlElement node = new XmlElement("node");
        node.setData("data");
        node.addAttribute("attr1", "value1");

        XmlElement.printNode(node, "");
    }

    @Test
    public void testClone() {
        XmlElement element = new XmlElement("element");
        element.setData("data");
        element.addAttribute("attr1", "value1");

        XmlElement clone = (XmlElement) element.clone();
        assertNotSame(element, clone);
        assertEquals(element, clone);
    }

    @Test
    public void testNotifyObservers() {
        // assertion for the behavior of notifyObservers() method
    }
}

