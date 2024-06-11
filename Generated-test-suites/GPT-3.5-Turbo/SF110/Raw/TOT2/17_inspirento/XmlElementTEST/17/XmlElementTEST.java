package com.allenstudio.ir.util;
import org.junit.Test;

import java.util.*;

import static org.junit.Assert.*;

public class XmlElementTest {

    @Test
    public void testAddAttribute() {
        XmlElement xmlElement = new XmlElement();
        assertNull(xmlElement.addAttribute("key1", "value1"));
        assertNotNull(xmlElement.addAttribute(null, "value2"));
    }

    @Test
    public void testGetAttribute() {
        XmlElement xmlElement = new XmlElement();
        xmlElement.addAttribute("key1", "value1");
        assertEquals("value1", xmlElement.getAttribute("key1"));
        assertNull(xmlElement.getAttribute("key2"));
    }

    @Test
    public void testGetAttributeWithDefaultValue() {
        XmlElement xmlElement = new XmlElement();
        assertEquals("default", xmlElement.getAttribute("key1", "default"));
    }

    // Add more test cases for other methods...

}