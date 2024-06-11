package com.allenstudio.ir.util;
import static org.junit.Assert.*;
import org.junit.Test;
import java.util.Hashtable;
import java.util.List;
import java.util.Enumeration;

public class XmlElementTest {

    @Test
    public void testAddAttribute() {
        XmlElement element = new XmlElement("test");
        Object returnValue = element.addAttribute("name", "value");
        assertEquals("Value should be returned when adding attribute", "value", returnValue);
    }

    @Test
    public void testGetAttribute() {
        XmlElement element = new XmlElement("test");
        element.addAttribute("name", "value");
        assertEquals("Should return correct attribute value", "value", element.getAttribute("name"));
    }

    @Test
    public void testGetAttributeDefault() {
        XmlElement element = new XmlElement("test");
        assertEquals("Should return default value if attribute not found", "default", element.getAttribute("nonexistent", "default"));
    }

    // Additional test cases for other methods can be added here

}