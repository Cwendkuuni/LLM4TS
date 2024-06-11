package com.allenstudio.ir.util;
import static org.junit.Assert.*;
import org.junit.Test;
import java.util.Hashtable;
import java.util.Enumeration;

public class XmlElementTest {

    @Test
    public void testAddAttribute() {
        XmlElement element = new XmlElement();
        Object result = element.addAttribute("testAttr", "123");
        assertEquals("First add attribute should return null", null, result);

        result = element.addAttribute("testAttr", "456");
        assertEquals("Replacing attribute should return previous value", "123", result);
    }

    @Test
    public void testGetAttribute() {
        XmlElement element = new XmlElement();
        element.addAttribute("testAttr", "123");
        assertEquals("Getting existing attribute should return its value", "123", element.getAttribute("testAttr"));
        assertEquals("Getting non-existing attribute should return null", null, element.getAttribute("nonExistingAttr"));
    }

    // Additional test cases for other methods can be added here

}