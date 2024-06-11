package com.allenstudio.ir.util;

import org.junit.Test;
import static org.junit.Assert.*;

public class XmlElementTest {

    @Test
    public void testAddAttribute() {
        XmlElement element = new XmlElement("test");
        assertNull(element.addAttribute(null, "value"));
        assertEquals("value", element.addAttribute("name", "value"));
    }

    @Test
    public void testGetAttribute() {
        XmlElement element = new XmlElement("test");
        element.addAttribute("name", "value");
        assertEquals("value", element.getAttribute("name"));
    }

    @Test
    public void testGetAttributeWithDefault() {
        XmlElement element = new XmlElement("test");
        assertEquals("default", element.getAttribute("name", "default"));
    }

    // Add more test cases for other methods

}