package com.allenstudio.ir.util;
import static org.junit.Assert.*;
import org.junit.Test;

public class XmlElementTest {

    @Test
    public void testAddAttribute() {
        XmlElement element = new XmlElement();
        assertNull(element.addAttribute("key", "value"));
        assertEquals("value", element.getAttribute("key"));
    }

    @Test
    public void testGetAttribute_defaultValue() {
        XmlElement element = new XmlElement();
        assertEquals("default", element.getAttribute("key", "default"));
    }

    // Additional test cases for other methods can be added here

    @Test
    public void testRemoveElement() {
        XmlElement parent = new XmlElement("parent");
        XmlElement child = new XmlElement("child");
        parent.addElement(child);
        
        assertEquals(child, parent.removeElement(child));
        assertTrue(parent.getElements().isEmpty());
    }

    @Test
    public void testAddSubElement_path() {
        XmlElement parent = new XmlElement("parent");
        parent.addSubElement("child1.child2");
        
        assertEquals(1, parent.count());
        assertEquals("child1", parent.getElement(0).getName());
        assertEquals("child2", parent.getElement(0).getElement(0).getName());
    }
}