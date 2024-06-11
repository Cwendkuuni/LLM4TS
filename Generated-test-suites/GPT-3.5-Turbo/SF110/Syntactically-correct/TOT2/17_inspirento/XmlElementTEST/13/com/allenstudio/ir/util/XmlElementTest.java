package com.allenstudio.ir.util;
import org.junit.Test;
import static org.junit.Assert.*;
import java.util.Hashtable;
import java.util.Enumeration;

public class XmlElementTest {

    @Test
    public void testAddAttribute() {
        XmlElement testElement = new XmlElement();
        Object result = testElement.addAttribute("key", "value");
        assertEquals("Adding attribute should return null", null, result);
    }

    @Test
    public void testGetAttribute() {
        XmlElement testElement = new XmlElement();
        testElement.addAttribute("key", "value");
        String result = testElement.getAttribute("key");
        assertEquals("Getting attribute should return the correct value", "value", result);
    }

    @Test
    public void testGetAttributeDefault() {
        XmlElement testElement = new XmlElement();
        String result = testElement.getAttribute("key", "default");
        assertEquals("Getting attribute with default value should return default if attribute not found", "default", result);
    }

    // Add more test cases for the remaining public methods

}