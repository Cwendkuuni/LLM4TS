
package com.allenstudio.ir.util;

import org.junit.Test;

import static org.junit.Assert.*;

public class XmlElementTest {

    @Test
    public void testAddAttribute() {
        XmlElement element = new XmlElement();
        assertNull(element.addAttribute(null, "value"));
        assertNull(element.addAttribute("name", null));
        assertNull(element.addAttribute(null, null));
        assertNull(element.addAttribute("", "value"));
        assertNull(element.addAttribute("name", ""));
        assertEquals("value1", element.addAttribute("name1", "value1"));
    }

    @Test
    public void testGetAttribute() {
        XmlElement element = new XmlElement();
        assertNull(element.getAttribute(null));
        assertNull(element.getAttribute("name"));
        element.addAttribute("name", "value");
        assertEquals("value", element.getAttribute("name"));
    }

    @Test
    public void testGetAttributeWithDefaultValue() {
        XmlElement element = new XmlElement();
        assertEquals("default", element.getAttribute("name", "default"));
        element.addAttribute("name", "value");
        assertEquals("value", element.getAttribute("name", "default"));
    }

    @Test
    public void testGetAttributes() {
        XmlElement element = new XmlElement();
        assertNotNull(element.getAttributes());
        assertEquals(0, element.getAttributes().size());
        element.addAttribute("name", "value");
        assertEquals("value", element.getAttributes().get("name"));
    }

    @Test
    public void testSetAttributes() {
        XmlElement element = new XmlElement();
        Hashtable<String, String> attributes = new Hashtable<>();
        attributes.put("name1", "value1");
        attributes.put("name2", "value2");
        element.setAttributes(attributes);
        assertEquals(attributes, element.getAttributes());
    }

    @Test
    public void testGetAttributeNames() {
        XmlElement element = new XmlElement();
        assertNotNull(element.getAttributeNames());
    }

    @Test
    public void testAddElement() {
        XmlElement element = new XmlElement();
        XmlElement subElement = new XmlElement("subElement");
        assertTrue(element.addElement(subElement));
        assertEquals(subElement, element.getElements().get(0));
    }

    @Test
    public void testRemoveElementByObject() {
        XmlElement element = new XmlElement();
        XmlElement subElement = new XmlElement("subElement");
        element.addElement(subElement);
        assertEquals(subElement, element.removeElement(subElement));
        assertTrue(element.getElements().isEmpty());
    }

    @Test
    public void testRemoveElementByIndex() {
        XmlElement element = new XmlElement();
        XmlElement subElement1 = new XmlElement("subElement1");
        XmlElement subElement2 = new XmlElement("subElement2");
        element.addElement(subElement1);
        element.addElement(subElement2);
        assertEquals(subElement1, element.removeElement(0));
        assertEquals(1, element.getElements().size());
        assertEquals(subElement2, element.getElements().get(0));
    }

    @Test
    public void testRemoveAllElements() {
        XmlElement element = new XmlElement();
        element.addElement(new XmlElement("subElement1"));
        element.addElement(new XmlElement("subElement2"));
        element.removeAllElements();
        assertTrue(element.getElements().isEmpty());
    }

    @Test
    public void testRemoveFromParent() {
        XmlElement parentElement = new XmlElement();
        XmlElement childElement = new XmlElement();
        parentElement.addElement(childElement);
        childElement.removeFromParent();
        assertTrue(parentElement.getElements().isEmpty());
        assertNull(childElement.getParent());
    }

    @Test
    public void testAppend() {
        XmlElement parentElement = new XmlElement();
        XmlElement childElement = new XmlElement();
        childElement.setName("childElement");
        childElement.setData("data");
        parentElement.append(childElement);
        assertEquals(childElement, parentElement.getElements().get(0));
        assertNull(childElement.getParent());
    }

    @Test
    public void testInsertElement() {
        XmlElement element = new XmlElement();
        XmlElement subElement1 = new XmlElement("subElement1");
        XmlElement subElement2 = new XmlElement("subElement2");
        subElement2.setData("data");
        element.addElement(subElement1);
        element.insertElement(subElement2, 0);
        assertEquals(subElement2, element.getElements().get(0));
    }

    @Test
    public void testGetElements() {
        XmlElement element = new XmlElement();
        assertNotNull(element.getElements());
        assertTrue(element.getElements().isEmpty());
        element.addElement(new XmlElement("subElement1"));
        assertFalse(element.getElements().isEmpty());
    }

    @Test
    public void testCount() {
        XmlElement element = new XmlElement();
        assertEquals(0, element.count());
        element.addElement(new XmlElement("subElement1"));
        assertEquals(1, element.count());
    }

    @Test
    public void testGetElementByPath() {
        XmlElement element = new XmlElement();
        element.addElement(new XmlElement("subElement1"));
        element.addElement(new XmlElement("subElement2"));
        element.addElement(new XmlElement("subElement3"));
        element.addElement(new XmlElement("subElement4"));
        assertEquals("subElement2", element.getElement("subElement2").getName());
        assertEquals("subElement3", element.getElement("subElement3").getName());
        assertNull(element.getElement("nonExistentElement"));
    }

    @Test
    public void testGetElementByIndex() {
        XmlElement element = new XmlElement();
        element.addElement(new XmlElement("subElement1"));
        element.addElement(new XmlElement("subElement2"));
        element.addElement(new XmlElement("subElement3"));
        element.addElement(new XmlElement("subElement4"));
        assertEquals("subElement1", element.getElement(0).getName());
        assertEquals("subElement2", element.getElement(1).getName());
    }

    @Test
    public void testAddSubElementByPath() {
        XmlElement element = new XmlElement();
        assertNull(element.addSubElement(""));
        XmlElement subElement1 = element.addSubElement("subElement1");
        assertNotNull(subElement1);
        assertEquals("subElement1", subElement1.getName());
        XmlElement subElement2 = element.addSubElement("subElement1.subElement2");
        assertNotNull(subElement2);
        assertEquals("subElement2", subElement2.getName());
        assertEquals(subElement2, subElement1.getElements().get(0));
    }

    @Test
    public void testAddSubElementByObject() {
        XmlElement element = new XmlElement();
        XmlElement subElement = new XmlElement();
        assertNotNull(element.addSubElement(subElement));
        assertEquals(subElement, element.getElements().get(0));
    }

    @Test
    public void testAddSubElementByNameAndData() {
        XmlElement element = new XmlElement();
        XmlElement subElement = element.addSubElement("subElement1", "data");
        assertNotNull(subElement);
        assertEquals("subElement1", subElement.getName());
        assertEquals("data", subElement.getData());
        assertEquals(subElement, element.getElements().get(0));
    }

    @Test
    public void testSetParent() {
        XmlElement parentElement = new XmlElement();
        XmlElement childElement = new XmlElement();
        childElement.setParent(parentElement);
        assertEquals(parentElement, childElement.getParent());
    }

    @Test
    public void testGetData() {
        XmlElement element = new XmlElement();
        assertNull(element.getData());
        element.setData("data");
        assertEquals("data", element.getData());
    }

    @Test
    public void testGetName() {
        XmlElement element = new XmlElement();
        assertNull(element.getName());
        element.setName("name");
        assertEquals("name", element.getName());
    }
}
