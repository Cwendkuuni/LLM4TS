package com.allenstudio.ir.util;
import static org.junit.Assert.*;
import org.junit.Test;

public class XmlElementTest {
    
    @Test
    public void testAddAttribute() {
        XmlElement element = new XmlElement();
        Object returnValue = element.addAttribute("attr1", "value1");
        assertEquals("Return value should be null", null, returnValue);
    }
    
    @Test
    public void testGetAttribute() {
        XmlElement element = new XmlElement();
        element.addAttribute("attr1", "value1");
        assertEquals("Attribute value should be 'value1'", "value1", element.getAttribute("attr1"));
    }
    
    @Test
    public void testRemoveElementByIndex() {
        XmlElement parentElement = new XmlElement();
        XmlElement childElement = new XmlElement("child");
        parentElement.addElement(childElement);
        assertEquals("Removed element should be the child element", childElement, parentElement.removeElement(0));
    }
    
    @Test
    public void testAddSubElement() {
        XmlElement parentElement = new XmlElement("parent");
        XmlElement childElement = parentElement.addSubElement("child1.child2");
        assertEquals("Parent element of the child element should be the parent element", parentElement, childElement.getParent());
    }
    
    @Test
    public void testClone() {
        XmlElement originalElement = new XmlElement("original");
        XmlElement clonedElement = (XmlElement)originalElement.clone();
        assertNotSame("Cloned element should not be the same as the original element", originalElement, clonedElement);
    }
}