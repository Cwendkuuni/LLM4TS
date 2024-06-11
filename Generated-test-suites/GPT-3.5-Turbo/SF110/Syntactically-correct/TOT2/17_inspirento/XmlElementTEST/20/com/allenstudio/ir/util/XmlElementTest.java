package com.allenstudio.ir.util;

import static org.junit.Assert.*;
import org.junit.Test;

public class XmlElementTest {

    @Test
    public void testAddAttribute() {
        // typical use case
        XmlElement element = new XmlElement();
        Object result = element.addAttribute("key", "value");
        assertEquals("value", element.getAttribute("key"));
        assertNotNull(result);
        
        // edge cases
        result = element.addAttribute(null, "value");
        assertNull(result);
        result = element.addAttribute("key", null);
        assertNull(result);
    }
    
    @Test
    public void testRemoveElement() {
        // typical use case
        XmlElement parentElement = new XmlElement();
        XmlElement childElement = new XmlElement("child");
        parentElement.addElement(childElement);
        
        XmlElement removedElement = parentElement.removeElement(childElement);
        assertNull(parentElement.getElement("child"));
        assertEquals(childElement, removedElement);
        
        // edge cases
        removedElement = parentElement.removeElement(0);
        assertNull(removedElement);
    }
    
    // Add more test cases for other methods as per the requirements
    
}