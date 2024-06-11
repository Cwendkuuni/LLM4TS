package com.allenstudio.ir.util;

import org.junit.Before;
import org.junit.Test;
import static org.junit.Assert.*;

import java.util.*;

public class XmlElementTest {

    private XmlElement xmlElement;

    @Before
    public void setUp() {
        xmlElement = new XmlElement();
    }

    @Test
    public void testAddAttribute() {
        assertNull(xmlElement.addAttribute("attr1", "value1"));
    }

    @Test
    public void testGetAttribute() {
        xmlElement.addAttribute("attr1", "value1");
        assertEquals("value1", xmlElement.getAttribute("attr1"));
    }

    @Test
    public void testGetAttributeWithDefaultValue() {
        assertEquals("defaultValue", xmlElement.getAttribute("attr1", "defaultValue"));
    }

    @Test
    public void testGetAttributes() {
        Hashtable<String, String> attributes = new Hashtable<>();
        attributes.put("attr1", "value1");
        attributes.put("attr2", "value2");
        xmlElement.setAttributes(attributes);
        assertEquals(attributes, xmlElement.getAttributes());
    }

    @Test
    public void testSetAttributes() {
        Hashtable<String, String> attributes = new Hashtable<>();
        attributes.put("attr1", "value1");
        attributes.put("attr2", "value2");
        xmlElement.setAttributes(attributes);
        assertEquals(attributes, xmlElement.getAttributes());
    }

    @Test
    public void testGetAttributeNames() {
        Hashtable<String, String> attributes = new Hashtable<>();
        attributes.put("attr1", "value1");
        attributes.put("attr2", "value2");
        xmlElement.setAttributes(attributes);

        Enumeration attributeNames = xmlElement.getAttributeNames();

        List<String> resultAttributeNames = new ArrayList<>();
        while (attributeNames.hasMoreElements()) {
            resultAttributeNames.add((String) attributeNames.nextElement());
        }

        assertEquals(Arrays.asList("attr1", "attr2"), resultAttributeNames);
    }

    @Test
    public void testAddElement() {
        XmlElement subElement = new XmlElement("subElement");
        assertTrue(xmlElement.addElement(subElement));
    }

    @Test
    public void testRemoveElementByObject() {
        XmlElement subElement = new XmlElement("subElement");
        xmlElement.addElement(subElement);
        
        assertEquals(subElement, xmlElement.removeElement(subElement));
        assertEquals(0, xmlElement.getElements().size());
    }

    @Test
    public void testRemoveElementByIndex() {
        XmlElement subElement1 = new XmlElement("subElement1");
        XmlElement subElement2 = new XmlElement("subElement2");
        xmlElement.addElement(subElement1);
        xmlElement.addElement(subElement2);
        
        assertEquals(subElement1, xmlElement.removeElement(0));
        assertEquals(1, xmlElement.getElements().size());
    }

    @Test
    public void testRemoveAllElements() {
        XmlElement subElement1 = new XmlElement("subElement1");
        XmlElement subElement2 = new XmlElement("subElement2");
        xmlElement.addElement(subElement1);
        xmlElement.addElement(subElement2);

        xmlElement.removeAllElements();
        assertEquals(0, xmlElement.getElements().size());
    }

    @Test
    public void testRemoveFromParent() {
        XmlElement parentElement = new XmlElement("parentElement");
        XmlElement subElement = new XmlElement("subElement");

        parentElement.addElement(subElement);

        subElement.removeFromParent();
        
        assertNull(subElement.getParent());
        assertEquals(0, parentElement.getElements().size());
    }

    @Test
    public void testAppend() {
        XmlElement parentElement = new XmlElement("parentElement");
        XmlElement subElement1 = new XmlElement("subElement1");
        XmlElement subElement2 = new XmlElement("subElement2");

        parentElement.addElement(subElement1);
        parentElement.append(subElement2);

        assertEquals(2, parentElement.getElements().size());
        assertEquals(parentElement, subElement2.getParent());
    }

    @Test
    public void testInsertElement() {
        XmlElement subElement1 = new XmlElement("subElement1");
        XmlElement subElement2 = new XmlElement("subElement2");
        XmlElement subElement3 = new XmlElement("subElement3");

        xmlElement.addElement(subElement1);
        xmlElement.addElement(subElement3);
        xmlElement.insertElement(subElement2, 1);

        assertEquals(3, xmlElement.getElements().size());
        assertEquals(subElement2, xmlElement.getElement(1));
    }

    @Test
    public void testGetElements() {
        XmlElement subElement1 = new XmlElement("subElement1");
        XmlElement subElement2 = new XmlElement("subElement2");

        xmlElement.addElement(subElement1);
        xmlElement.addElement(subElement2);

        assertEquals(Arrays.asList(subElement1, subElement2), xmlElement.getElements());
    }

    @Test
    public void testCount() {
        XmlElement subElement1 = new XmlElement("subElement1");
        XmlElement subElement2 = new XmlElement("subElement2");

        xmlElement.addElement(subElement1);
        xmlElement.addElement(subElement2);

        assertEquals(2, xmlElement.count());
    }

    @Test
    public void testGetElementByPath() {
        XmlElement subElement1 = new XmlElement("subElement1");
        XmlElement subElement2 = new XmlElement("subElement2");
        XmlElement subElement3 = new XmlElement("subElement3");

        xmlElement.addElement(subElement1);
        subElement1.addElement(subElement2);
        subElement2.addElement(subElement3);

        assertEquals(subElement2, xmlElement.getElement("subElement1.subElement2"));
    }

    @Test
    public void testGetElementByIndex() {
        XmlElement subElement1 = new XmlElement("subElement1");
        XmlElement subElement2 = new XmlElement("subElement2");

        xmlElement.addElement(subElement1);
        xmlElement.addElement(subElement2);

        assertEquals(subElement2, xmlElement.getElement(1));
    }

    @Test
    public void testAddSubElementByPath() {
        XmlElement parentElement = new XmlElement("parentElement");
        XmlElement subElement1 = parentElement.addSubElement("subElement1.subElement2.subElement3");
        
        assertEquals(3, parentElement.getElements().size());
        assertEquals("subElement1", parentElement.getElement("subElement1").getName());
        assertEquals("subElement2", parentElement.getElement("subElement1.subElement2").getName());
        assertEquals("subElement3", parentElement.getElement("subElement1.subElement2.subElement3").getName());
    }

    @Test
    public void testAddSubElementByObject() {
        XmlElement subElement = new XmlElement("subElement");
        XmlElement resultElement = xmlElement.addSubElement(subElement);

        assertEquals(subElement, resultElement);
        assertEquals(1, xmlElement.getElements().size());
        assertEquals(xmlElement, subElement.getParent());
    }

    @Test
    public void testAddSubElementByNameAndData() {
        XmlElement subElement = xmlElement.addSubElement("subElement", "data");

        assertEquals("subElement", subElement.getName());
        assertEquals("data", subElement.getData());
        assertEquals(1, xmlElement.getElements().size());
        assertEquals(xmlElement, subElement.getParent());
    }

    @Test
    public void testSetParent() {
        XmlElement parentElement = new XmlElement("parentElement");

        xmlElement.setParent(parentElement);

        assertEquals(parentElement, xmlElement.getParent());
    }

    @Test
    public void testGetData() {
        xmlElement.setData("data");

        assertEquals("data", xmlElement.getData());
    }

    @Test
    public void testGetName() {
        xmlElement.setName("name");

        assertEquals("name", xmlElement.getName());
    }

    @Test
    public void testClone() {
        XmlElement subElement1 = new XmlElement("subElement1");
        XmlElement subElement2 = new XmlElement("subElement2");
        xmlElement.addElement(subElement1);
        xmlElement.addElement(subElement2);

        XmlElement clonedElement = (XmlElement) xmlElement.clone();

        assertNotSame(xmlElement, clonedElement);
        assertEquals(xmlElement.getElements(), clonedElement.getElements());
    }

    @Test
    public void testNotifyObservers() {
        xmlElement.addObserver(new Observer() {
            @Override
            public void update(Observable o, Object arg) {
                //do something
            }
        });

        xmlElement.notifyObservers();
        assertTrue(xmlElement.hasChanged());
    }

    @Test
    public void testEquals() {
        XmlElement xmlElement1 = new XmlElement("element1");
        xmlElement1.addAttribute("attr1", "value1");
        xmlElement1.setData("data1");

        XmlElement xmlElement2 = new XmlElement("element1");
        xmlElement2.addAttribute("attr1", "value1");
        xmlElement2.setData("data1");

        assertTrue(xmlElement1.equals(xmlElement2));
    }

    @Test
    public void testHashCode() {
        XmlElement xmlElement1 = new XmlElement("element1");
        xmlElement1.addAttribute("attr1", "value1");
        xmlElement1.setData("data1");

        XmlElement xmlElement2 = new XmlElement("element1");
        xmlElement2.addAttribute("attr1", "value1");
        xmlElement2.setData("data1");

        assertEquals(xmlElement1.hashCode(), xmlElement2.hashCode());
    }
}