package com.allenstudio.ir.util;

import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

import java.util.Hashtable;
import java.util.List;
import java.util.Vector;

public class XmlElementTest {
    private XmlElement xmlElement;

    @Before
    public void setUp() {
        xmlElement = new XmlElement();
    }

    @Test
    public void testAddAttribute() {
        Object returnValue = xmlElement.addAttribute("name", "value");
        Assert.assertNull(returnValue);

        returnValue = xmlElement.addAttribute("name", "newValue");
        Assert.assertEquals("value", returnValue);
    }

    @Test
    public void testGetAttribute() {
        xmlElement.addAttribute("name", "value");
        Assert.assertEquals("value", xmlElement.getAttribute("name"));

        Assert.assertNull(xmlElement.getAttribute("nonexistent"));
        Assert.assertEquals("default", xmlElement.getAttribute("nonexistent", "default"));
    }

    @Test
    public void testGetAttributes() {
        Hashtable<String, String> attributes = new Hashtable<>();
        attributes.put("name", "value");

        xmlElement.setAttributes(attributes);
        Assert.assertEquals(attributes, xmlElement.getAttributes());
    }

    @Test
    public void testRemoveElement() {
        XmlElement child = new XmlElement("child");
        xmlElement.addElement(child);

        XmlElement removedChild = xmlElement.removeElement(child);
        Assert.assertEquals(child, removedChild);
        Assert.assertEquals(0, xmlElement.count());

        xmlElement.addElement(child);
        removedChild = xmlElement.removeElement(0);
        Assert.assertEquals(child, removedChild);
        Assert.assertEquals(0, xmlElement.count());
    }

    @Test
    public void testRemoveAllElements() {
        XmlElement child1 = new XmlElement("child1");
        XmlElement child2 = new XmlElement("child2");
        xmlElement.addElement(child1);
        xmlElement.addElement(child2);

        xmlElement.removeAllElements();
        Assert.assertEquals(0, xmlElement.count());
    }

    @Test
    public void testRemoveFromParent() {
        XmlElement parent = new XmlElement("parent");
        parent.addElement(xmlElement);

        xmlElement.removeFromParent();
        Assert.assertEquals(0, parent.count());
        Assert.assertNull(xmlElement.getParent());
    }

    @Test
    public void testAppend() {
        XmlElement child = new XmlElement("child");
        XmlElement parent = new XmlElement("parent");
        child.setParent(parent);

        child.append(xmlElement);
        Assert.assertEquals(1, child.count());
        Assert.assertEquals(0, xmlElement.count());
        Assert.assertNull(xmlElement.getParent());
    }

    @Test
    public void testInsertElement() {
        XmlElement child1 = new XmlElement("child1");
        XmlElement child2 = new XmlElement("child2");

        for (int i = 0; i < 3; i++) {
            xmlElement.addElement(child1);
        }

        xmlElement.insertElement(child2, 1);
        Assert.assertEquals(child2, xmlElement.getElement(1));
        Assert.assertEquals(4, xmlElement.count());
    }

    @Test
    public void testGetElement() {
        XmlElement child = new XmlElement("child");
        xmlElement.addElement(child);

        Assert.assertEquals(child, xmlElement.getElement("child"));
        Assert.assertNull(xmlElement.getElement("nonexistent"));
        Assert.assertNull(xmlElement.getElement("child.nonexistent"));
    }

    @Test
    public void testAddSubElement() {
        XmlElement child = xmlElement.addSubElement("child");
        Assert.assertEquals(child, xmlElement.getElement("child"));

        xmlElement.addSubElement("child1.child2.child3");
        Assert.assertNotNull(xmlElement.getElement("child1"));
        Assert.assertNotNull(xmlElement.getElement("child1.child2"));
        Assert.assertNotNull(xmlElement.getElement("child1.child2.child3"));
    }

    @Test
    public void testClone() {
        XmlElement child = new XmlElement("child");
        xmlElement.addElement(child);

        XmlElement clone = (XmlElement) xmlElement.clone();
        Assert.assertEquals(xmlElement, clone);
    }

    @Test
    public void testSetParent() {
        XmlElement parent = new XmlElement("parent");
        xmlElement.setParent(parent);

        Assert.assertEquals(parent, xmlElement.getParent());
    }

    @Test
    public void testSetData() {
        xmlElement.setData("data");
        Assert.assertEquals("data", xmlElement.getData());
    }

    @Test
    public void testGetName() {
        xmlElement.setName("name");
        Assert.assertEquals("name", xmlElement.getName());
    }

    @Test
    public void testEquals() {
        XmlElement other = new XmlElement();
        Assert.assertTrue(xmlElement.equals(other));

        other.setName("name");
        Assert.assertFalse(xmlElement.equals(other));
    }

    @Test
    public void testHashCode() {
        xmlElement.setName("name");
        Assert.assertNotEquals(23, xmlElement.hashCode());
    }

    @Test
    public void testPrintNode() {
        // Test not implemented
    }
}