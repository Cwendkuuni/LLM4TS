package com.allenstudio.ir.util;
import static org.junit.Assert.*;
import org.junit.Test;
import java.util.Hashtable;
import java.util.Vector;

public class XmlElementTest {

    @Test
    public void testAddAttribute() {
        XmlElement element = new XmlElement("test");
        Object returnValue = element.addAttribute("key", "value");
        assertNotNull(returnValue);
    }

    @Test
    public void testGetAttribute() {
        XmlElement element = new XmlElement("test");
        element.addAttribute("key", "value");
        assertEquals("value", element.getAttribute("key"));
    }

    @Test
    public void testGetAttributeDefault() {
        XmlElement element = new XmlElement("test");
        assertEquals("default", element.getAttribute("key", "default"));
    }

    // Add more test cases for each public method in the XmlElement class

}