package com.allenstudio.ir.util;

import org.junit.Test;
import static org.junit.Assert.*;
import java.util.Hashtable;
import java.util.List;
import java.util.Vector;

public class XmlElementTest {

    @Test
    public void testAddAttribute() {
        XmlElement xmlElement = new XmlElement();
        Object returnValue = xmlElement.addAttribute("name", "value");
        assertNull(returnValue);
    }

    @Test
    public void testGetAttribute() {
        XmlElement xmlElement = new XmlElement();
        xmlElement.addAttribute("name", "value");
        String attribute = xmlElement.getAttribute("name");
        assertEquals("value", attribute);
    }

    @Test
    public void testGetAttribute_withDefaultValue() {
        XmlElement xmlElement = new XmlElement();
        String attribute = xmlElement.getAttribute("name", "defaultValue");
        assertEquals("defaultValue", attribute);
    }

    @Test
    public void testGetAttributes() {
        XmlElement xmlElement = new XmlElement();
        Hashtable<String, String> attributes = new Hashtable<String, String>();
        xmlElement.setAttributes(attributes);
        Hashtable<String, String> result = xmlElement.getAttributes();
        assertEquals(attributes, result);
    }

    @Test
    public void testSetAttributes() {
        XmlElement xmlElement = new XmlElement();
        Hashtable<String, String> attributes = new Hashtable<String, String>();
        xmlElement.setAttributes(attributes);
        Hashtable<String, String> result = xmlElement.getAttributes();
        assertEquals(attributes, result);
    }

    @Test
    public void testGetAttributeNames() {
        XmlElement xmlElement = new XmlElement();
        Hashtable<String, String> attributes = new Hashtable<String, String>();
        attributes.put("name1", "value1");
        attributes.put("name2", "value2");
        xmlElement.setAttributes(attributes);
        Enumeration result = xmlElement.getAttributeNames();
        assertNotNull(result);
    }

    @Test
    public void testAddElement() {
        XmlElement xmlElement = new XmlElement();
        XmlElement subElement = new XmlElement("subElement");
        boolean result = xmlElement.addElement(subElement);
        assertTrue(result);
    }

    @Test
    public void testRemoveElement_withElement() {
        XmlElement xmlElement = new XmlElement();
        XmlElement subElement = new XmlElement("subElement");
        xmlElement.addElement(subElement);
        XmlElement result = xmlElement.removeElement(subElement);
        assertEquals(subElement, result);
    }

    @Test
    public void testRemoveElement_withIndex() {
        XmlElement xmlElement = new XmlElement();
        XmlElement subElement = new XmlElement("subElement");
        xmlElement.addElement(subElement);
        XmlElement result = xmlElement.removeElement(0);
        assertEquals(subElement, result);
    }

    @Test
    public void testRemoveAllElements() {
        XmlElement xmlElement = new XmlElement();
        XmlElement subElement1 = new XmlElement("subElement1");
        XmlElement subElement2 = new XmlElement("subElement2");
        xmlElement.addElement(subElement1);
        xmlElement.addElement(subElement2);
        xmlElement.removeAllElements();
        int count = xmlElement.count();
        assertEquals(0, count);
    }

    @Test
    public void testRemoveFromParent() {
        XmlElement xmlElement = new XmlElement();
        XmlElement subElement = new XmlElement("subElement");
        subElement.setParent(xmlElement);
        subElement.removeFromParent();
        XmlElement parent = subElement.getParent();
        assertNull(parent);
    }

    @Test
    public void testAppend() {
        XmlElement xmlElement = new XmlElement();
        XmlElement subElement = new XmlElement("subElement");
        subElement.removeFromParent();
        xmlElement.append(subElement);
        XmlElement result = (XmlElement)xmlElement.getElements().get(0);
        assertEquals(subElement, result);
    }

    @Test
    public void testInsertElement() {
        XmlElement xmlElement = new XmlElement();
        XmlElement subElement1 = new XmlElement("subElement1");
        XmlElement subElement2 = new XmlElement("subElement2");
        xmlElement.addElement(subElement1);
        xmlElement.insertElement(subElement2, 0);
        XmlElement result = xmlElement.getElement(0);
        assertEquals(subElement2, result);
    }

    @Test
    public void testGetElements() {
        XmlElement xmlElement = new XmlElement();
        Vector<XmlElement> subElements = new Vector<XmlElement>();
        XmlElement subElement1 = new XmlElement("subElement1");
        XmlElement subElement2 = new XmlElement("subElement2");
        subElements.add(subElement1);
        subElements.add(subElement2);
        xmlElement.setElements(subElements);
        List<XmlElement> result = xmlElement.getElements();
        assertEquals(subElements, result);
    }

    @Test
    public void testCount() {
        XmlElement xmlElement = new XmlElement();
        Vector<XmlElement> subElements = new Vector<XmlElement>();
        XmlElement subElement1 = new XmlElement("subElement1");
        XmlElement subElement2 = new XmlElement("subElement2");
        subElements.add(subElement1);
        subElements.add(subElement2);
        xmlElement.setElements(subElements);
        int count = xmlElement.count();
        assertEquals(2, count);
    }

    @Test
    public void testGetElement() {
        XmlElement xmlElement = new XmlElement();
        XmlElement subElement1 = new XmlElement("subElement1");
        XmlElement subElement2 = new XmlElement("subElement2");
        xmlElement.addElement(subElement1);
        xmlElement.addElement(subElement2);
        XmlElement result = xmlElement.getElement(1);
        assertEquals(subElement2, result);
    }

    @Test
    public void testAddSubElement_withString() {
        XmlElement xmlElement = new XmlElement();
        String path = "subElement1.subElement2";
        XmlElement subElement = xmlElement.addSubElement(path);
        assertNotNull(subElement);
    }

    @Test
    public void testAddSubElement_withElement() {
        XmlElement xmlElement = new XmlElement();
        XmlElement subElement = new XmlElement("subElement");
        XmlElement result = xmlElement.addSubElement(subElement);
        assertEquals(subElement, result);
    }

    @Test
    public void testAddSubElement_withNameAndData() {
        XmlElement xmlElement = new XmlElement();
        XmlElement subElement = xmlElement.addSubElement("subElement", "data");
        assertNotNull(subElement);
    }

    @Test
    public void testSetParent() {
        XmlElement xmlElement = new XmlElement();
        XmlElement parent = new XmlElement("parent");
        xmlElement.setParent(parent);
        XmlElement result = xmlElement.getParent();
        assertEquals(parent, result);
    }

    @Test
    public void testGetData() {
        XmlElement xmlElement = new XmlElement();
        xmlElement.setData("data");
        String result = xmlElement.getData();
        assertEquals("data", result);
    }

    @Test
    public void testGetName() {
        XmlElement xmlElement = new XmlElement();
        xmlElement.setName("name");
        String result = xmlElement.getName();
        assertEquals("name", result);
    }

    @Test
    public void testClone() {
        XmlElement xmlElement1 = new XmlElement();
        xmlElement1.setName("name1");
        xmlElement1.addAttribute("attr1", "value1");
        XmlElement xmlElement2 = (XmlElement) xmlElement1.clone();
        assertEquals(xmlElement1, xmlElement2);
    }

    @Test
    public void testEquals_withSameObject() {
        XmlElement xmlElement = new XmlElement();
        boolean result = xmlElement.equals(xmlElement);
        assertTrue(result);
    }

    @Test
    public void testEquals_withNullObject() {
        XmlElement xmlElement = new XmlElement();
        boolean result = xmlElement.equals(null);
        assertFalse(result);
    }

    @Test
    public void testEquals_withDifferentObject() {
        XmlElement xmlElement1 = new XmlElement();
        XmlElement xmlElement2 = new XmlElement();
        boolean result = xmlElement1.equals(xmlElement2);
        assertFalse(result);
    }

    @Test
    public void testEquals_withSameAttributes() {
        XmlElement xmlElement1 = new XmlElement();
        xmlElement1.addAttribute("attr1", "value1");
        XmlElement xmlElement2 = new XmlElement();
        xmlElement2.addAttribute("attr1", "value1");
        boolean result = xmlElement1.equals(xmlElement2);
        assertTrue(result);
    }

    @Test
    public void testHashCode() {
        XmlElement xmlElement = new XmlElement();
        int hashCode = xmlElement.hashCode();
        assertNotNull(hashCode);
    }
}