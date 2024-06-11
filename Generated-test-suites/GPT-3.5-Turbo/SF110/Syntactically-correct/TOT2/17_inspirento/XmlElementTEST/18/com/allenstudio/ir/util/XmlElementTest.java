package com.allenstudio.ir.util;
import static org.junit.Assert.*;
import org.junit.Test;

public class XmlElementTest {
    
    @Test
    public void testAddAttribute() {
        XmlElement element = new XmlElement("test");
        Object result = element.addAttribute("key", "value");
        assertNotNull(result);
    }
    
    @Test
    public void testGetAttribute() {
        XmlElement element = new XmlElement("test");
        element.addAttribute("key", "value");
        String value = element.getAttribute("key");
        assertEquals("value", value);
    }
    
    @Test
    public void testRemoveElement_Object() {
        XmlElement parent = new XmlElement("parent");
        XmlElement child = new XmlElement("child");
        parent.addElement(child);
        XmlElement removed = parent.removeElement(child);
        assertEquals(child, removed);
    }
    
    @Test
    public void testRemoveElement_Int() {
        XmlElement parent = new XmlElement("parent");
        XmlElement child = new XmlElement("child");
        parent.addElement(child);
        XmlElement removed = parent.removeElement(0);
        assertEquals(child, removed);
    }
    
    @Test
    public void testGetElement_Path() {
        XmlElement parent = new XmlElement("parent");
        XmlElement child = new XmlElement("child");
        parent.addElement(child);
        XmlElement found = parent.getElement("child");
        assertEquals(child, found);
    }
    
    @Test
    public void testAddSubElement_Path() {
        XmlElement parent = new XmlElement("parent");
        XmlElement child = parent.addSubElement("child");
        assertEquals("child", child.getName());
    }
    
    @Test
    public void testClone() {
        XmlElement original = new XmlElement("original");
        XmlElement clone = (XmlElement)original.clone();
        assertNotSame(original, clone);
    }
    
    // Edge cases and error scenarios testing can be added here as needed
    
}