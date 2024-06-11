package com.allenstudio.ir.util;
import static org.junit.Assert.*;
import org.junit.Before;
import org.junit.Test;
import java.util.Hashtable;
import java.util.Enumeration;

public class XmlElementTest {

    private XmlElement xmlElement;

    @Before
    public void setUp() {
        xmlElement = new XmlElement();
    }

    @Test
    public void testAddAttribute() {
        assertNull(xmlElement.addAttribute(null, null));
        assertEquals("value1", xmlElement.addAttribute("key1", "value1"));
    }

    @Test
    public void testGetAttribute() {
        xmlElement.addAttribute("key1", "value1");
        assertEquals("value1", xmlElement.getAttribute("key1"));
    }

    @Test
    public void testGetAttributeWithDefault() {
        assertEquals("defaultValue", xmlElement.getAttribute("key2", "defaultValue"));
    }

    @Test
    public void testGetAttributes() {
        Hashtable<String, String> attributes = new Hashtable<>();
        attributes.put("key1", "value1");
        xmlElement.setAttributes(attributes);
        assertEquals(attributes, xmlElement.getAttributes());
    }

    @Test
    public void testAddElementAndRemoveElement() {
        XmlElement element = new XmlElement("element1");
        xmlElement.addElement(element);
        assertEquals(1, xmlElement.count());
        assertEquals(element, xmlElement.removeElement(0));
    }
    
    @Test
    public void testAppendAndInsertElement() {
        XmlElement element1 = new XmlElement("element1");
        XmlElement element2 = new XmlElement("element2");
        xmlElement.append(element1);
        xmlElement.insertElement(element2, 0);
        assertEquals(element2, xmlElement.getElement(0));
    }

    @Test
    public void testAddSubElement() {
        XmlElement subElement = xmlElement.addSubElement("parent.child");
        assertNotNull(subElement);
        assertEquals("parent", xmlElement.getElement("parent").getName());
    }

}