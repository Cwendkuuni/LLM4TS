
package com.allenstudio.ir.util;

import org.junit.Test;
import static org.junit.Assert.*;

import java.util.Enumeration;

public class XmlElementTest {

    @Test
    public void testGetAttributeWithDefault() {
        XmlElement element = new XmlElement("test");
        assertEquals("defaultValue", element.getAttribute("nonExisting", "defaultValue"));
    }

    @Test
    public void testAddElement() {
        XmlElement parent = new XmlElement("parent");
        XmlElement child = new XmlElement("child");
        assertTrue(parent.addElement(child));
    }

    @Test
    public void testRemoveElementByIndex() {
        XmlElement parent = new XmlElement("parent");
        XmlElement child = new XmlElement("child");
        parent.addElement(child);

        assertEquals(child, parent.removeElement(0));
    }

    @Test
    public void testEquals() {
        XmlElement element1 = new XmlElement("test");
        XmlElement element2 = new XmlElement("test");
        assertTrue(element1.equals(element2));
    }

}
