package com.allenstudio.ir.util;
import static org.junit.Assert.*;
import org.junit.Test;

public class XmlElementTest {

    @Test
    public void testAddAttribute() {
        XmlElement element = new XmlElement("test");
        assertNull(element.getAttribute("test"));
        element.addAttribute("test", "value");
        assertEquals("value", element.getAttribute("test"));
    }

    @Test
    public void testGetAttribute() {
        XmlElement element = new XmlElement("test");
        assertNull(element.getAttribute("test"));
        element.addAttribute("test", "value");
        assertEquals("value", element.getAttribute("test"));
    }

    @Test
    public void testGetAttributeWithDefaultValue() {
        XmlElement element = new XmlElement("test");
        assertEquals("default", element.getAttribute("test", "default"));
    }

    @Test(expected = NullPointerException.class)
    public void testGetAttributeNullName() {
       XmlElement element = new XmlElement("test");
       element.getAttribute(null);
    }

    @Test
    public void testAddElement() {
        XmlElement parent = new XmlElement("parent");
        XmlElement child = new XmlElement("child");
        assertTrue(parent.addElement(child));
        assertEquals(child, parent.getElement(0));
    }

    // Additional test methods for other public methods in the XmlElement class...

}