package com.allenstudio.ir.util;
import static org.junit.Assert.*;
import org.junit.Test;

public class XmlElementTest {

    // Test cases for the XmlElement class

    @Test
    public void testAddAttribute() {
        XmlElement element = new XmlElement();
        assertNull(element.addAttribute(null, "value"));
        assertNull(element.addAttribute("name", null));
    }

    @Test
    public void testGetAttribute() {
        XmlElement element = new XmlElement();
        assertNull(element.getAttribute("nonexistent"));
    }

    @Test
    public void testGetAttributeWithDefaultValue() {
        XmlElement element = new XmlElement();
        assertEquals("default", element.getAttribute("nonexistent", "default"));
    }

    @Test
    public void testAddElement() {
        XmlElement parent = new XmlElement();
        XmlElement child = new XmlElement("child");
        assertTrue(parent.addElement(child));
    }

    @Test
    public void testRemoveElement() {
        XmlElement parent = new XmlElement();
        XmlElement child = new XmlElement("child");
        parent.addElement(child);
        assertEquals(child, parent.removeElement(child));
    }

    @Test
    public void testRemoveElementAtIndex() {
        XmlElement parent = new XmlElement();
        XmlElement child = new XmlElement("child");
        parent.addElement(child);
        assertEquals(child, parent.removeElement(0));
    }

    @Test
    public void testGetElement() {
        XmlElement parent = new XmlElement();
        XmlElement child = new XmlElement("child");
        parent.addElement(child);
        assertEquals(child, parent.getElement(0));
    }

    @Test
    public void testCount() {
        XmlElement parent = new XmlElement();
        assertEquals(0, parent.count());
    }

    @Test
    public void testAppend() {
        XmlElement parent = new XmlElement();
        XmlElement child = new XmlElement("child");
        parent.append(child);
        assertEquals(child, parent.getElement(0));
    }

    @Test
    public void testAddSubElement() {
        XmlElement parent = new XmlElement();
        XmlElement child = parent.addSubElement("child");
        assertEquals("child", child.getName());
    }

    @Test
    public void testSetParent() {
        XmlElement parent = new XmlElement();
        XmlElement child = new XmlElement("child");
        child.setParent(parent);
        assertEquals(parent, child.getParent());
    }

    @Test
    public void testDataMethods() {
        XmlElement element = new XmlElement("element", "data");
        assertEquals("data", element.getData());
        element.setData("new data");
        assertEquals("new data", element.getData());
    }

}