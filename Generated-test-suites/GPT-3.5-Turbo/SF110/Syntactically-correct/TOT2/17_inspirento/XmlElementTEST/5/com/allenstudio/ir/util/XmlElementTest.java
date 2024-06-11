package com.allenstudio.ir.util;
import static org.junit.Assert.*;
import org.junit.Test;

public class XmlElementTest {

    @Test
    public void testAddAttribute() {
        XmlElement element = new XmlElement();
        Object returnValue = element.addAttribute("key", "value");
        assertNotNull(returnValue);
    }

    @Test
    public void testGetAttribute() {
        XmlElement element = new XmlElement();
        element.addAttribute("key", "value");
        String attribute = element.getAttribute("key");
        assertEquals("value", attribute);
    }

    @Test
    public void testGetAttributeWithDefaultValue() {
        XmlElement element = new XmlElement();
        String attribute = element.getAttribute("key", "default");
        assertEquals("default", attribute);
    }

    // Add more individual test cases for each method in the XmlElement class
    // ... 

}