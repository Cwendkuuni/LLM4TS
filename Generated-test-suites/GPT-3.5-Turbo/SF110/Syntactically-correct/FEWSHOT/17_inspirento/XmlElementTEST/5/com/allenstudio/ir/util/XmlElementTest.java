package com.allenstudio.ir.util;

import org.junit.Test;

import java.util.Hashtable;
import java.util.List;

import static org.junit.Assert.*;

public class XmlElementTest {

    @Test
    public void testAddAttribute() {
        XmlElement xmlElement = new XmlElement();
        assertNull(xmlElement.addAttribute(null, null));
        assertNull(xmlElement.addAttribute("name", null));
        assertNull(xmlElement.addAttribute(null, "value"));
        assertEquals("value", xmlElement.addAttribute("name", "value"));
    }

    @Test
    public void testGetAttribute() {
        XmlElement xmlElement = new XmlElement();
        xmlElement.addAttribute("name1", "value1");
        xmlElement.addAttribute("name2", "value2");

        assertNull(xmlElement.getAttribute(null));
        assertNull(xmlElement.getAttribute("nonexistent"));
        assertEquals("value1", xmlElement.getAttribute("name1"));
        assertEquals("value2", xmlElement.getAttribute("name2"));
    }

    @Test
    public void testGetAttributeWithDefaultValue() {
        XmlElement xmlElement = new XmlElement();
        xmlElement.getAttribute("name", "defaultValue");

        assertEquals("defaultValue", xmlElement.getAttribute("name", "defaultValue"));
        assertNull(xmlElement.getAttribute("name", null));
    }

    @Test
    public void testGetAttributes() {
        XmlElement xmlElement = new XmlElement();
        Hashtable<String, String> attributes = new Hashtable<>();
        xmlElement.setAttributes(attributes);

        assertEquals(attributes, xmlElement.getAttributes());
    }

    @Test
    public void testGetAttributeNames() {
        XmlElement xmlElement = new XmlElement();
        xmlElement.addAttribute("name1", "value1");
        xmlElement.addAttribute("name2", "value2");

        assertNotNull(xmlElement.getAttributeNames());
    }

    @Test
    public void testAddElement() {
        XmlElement xmlElement = new XmlElement();
        XmlElement subElement = new XmlElement("subElement");

        assertTrue(xmlElement.addElement(subElement));
        assertEquals(subElement, xmlElement.getElement(0));
    }

    @Test
    public void testRemoveElementAtIndex() {
        XmlElement xmlElement = new XmlElement();
        XmlElement subElement = new XmlElement("subElement");
        xmlElement.addElement(subElement);

        assertEquals(subElement, xmlElement.removeElement(0));
    }

    @Test
    public void testRemoveAllElements() {
        XmlElement xmlElement = new XmlElement();
        XmlElement subElement = new XmlElement("subElement");
        xmlElement.addElement(subElement);

        xmlElement.removeAllElements();
        assertEquals(0, xmlElement.count());
    }

    @Test
    public void testRemoveFromParent() {
        XmlElement xmlElement1 = new XmlElement();
        XmlElement xmlElement2 = new XmlElement();
        xmlElement1.addElement(xmlElement2);

        XmlElement parent = xmlElement2.getParent();
        xmlElement2.removeFromParent();

        assertNull(xmlElement2.getParent());
        assertNull(parent.getElement(0));
    }

    @Test
    public void testAppend() {
        XmlElement parent = new XmlElement();
        XmlElement child = new XmlElement("child");
        child.setParent(parent);
        XmlElement grandChild = new XmlElement("grandChild");
        grandChild.addElement(new XmlElement("greatGrandChild"));
        child.addElement(grandChild);

        grandChild.removeFromParent();
        parent.append(grandChild);
        assertEquals(grandChild, parent.getElement(0));
    }

    @Test
    public void testInsertElement() {
        XmlElement xmlElement = new XmlElement();
        XmlElement subElement1 = new XmlElement("subElement1");
        xmlElement.addElement(subElement1);

        XmlElement subElement2 = new XmlElement("subElement2");
        xmlElement.insertElement(subElement2, 0);

        assertEquals(subElement2, xmlElement.getElement(0));
    }

    @Test
    public void testGetElement() {
        XmlElement xmlElement = new XmlElement();
        XmlElement subElement1 = new XmlElement("subElement1");
        xmlElement.addElement(subElement1);

        assertNull(xmlElement.getElement("nonexistent"));
        assertNull(xmlElement.getElement(null));
        assertEquals(subElement1, xmlElement.getElement("subElement1"));
    }

    @Test
    public void testGetElementAtIndex() {
        XmlElement xmlElement = new XmlElement();
        XmlElement subElement = new XmlElement("subElement");
        xmlElement.addElement(subElement);

        assertEquals(subElement, xmlElement.getElement(0));
    }

    @Test
    public void testAddSubElementWithPath() {
        XmlElement xmlElement = new XmlElement();
        XmlElement subElement = new XmlElement();
        xmlElement.addSubElement(subElement, "path.subPath");

        assertEquals(subElement, xmlElement.getElement("path.subPath"));
    }

    @Test
    public void testAddSubElement() {
        XmlElement xmlElement = new XmlElement();
        XmlElement subElement = new XmlElement();
        xmlElement.addSubElement(subElement);

        assertEquals(subElement, xmlElement.getElement(0));
    }

    @Test
    public void testAddSubElementWithNameAndData() {
        XmlElement xmlElement = new XmlElement();
        xmlElement.addSubElement("name", "data");

        assertEquals("name", xmlElement.getElement(0).getName());
        assertEquals("data", xmlElement.getElement(0).getData());
    }

    @Test
    public void testSetParent() {
        XmlElement parent = new XmlElement();
        XmlElement child = new XmlElement();
        child.setParent(parent);

        assertEquals(parent, child.getParent());
    }

    @Test
    public void testGetData() {
        XmlElement xmlElement = new XmlElement();
        xmlElement.setData("data");

        assertEquals("data", xmlElement.getData());
    }

    @Test
    public void testGetName() {
        XmlElement xmlElement = new XmlElement();
        xmlElement.setName("name");

        assertEquals("name", xmlElement.getName());
    }

    @Test
    public void testPrintNode() {
        assertNull(null, null);  // TODO: Implement test case
    }

    @Test
    public void testClone() {
        assertNull(null, null);  // TODO: Implement test case
    }

    @Test
    public void testNotifyObservers() {
        assertNull(null, null);  // TODO: Implement test case
    }

    @Test
    public void testEquals() {
        assertNull(null, null);  // TODO: Implement test case
    }

    @Test
    public void testHashCode() {
        assertNull(null, null);  // TODO: Implement test case
    }
}