
package com.allenstudio.ir.util;

import org.junit.Test;
import java.util.*;
import static org.junit.Assert.*;

public class XmlElementTest {

    @Test
    public void testAddAttribute() {
        XmlElement element = new XmlElement();
        Object returnValue = element.addAttribute("attr1", "value1");
        assertNull(returnValue);
        returnValue = element.addAttribute("attr1", "value2");
        assertEquals("value1", returnValue);
        returnValue = element.addAttribute("attr2", "value3");
        assertNull(returnValue);
    }

    @Test
    public void testGetAttribute() {
        XmlElement element = new XmlElement();
        element.addAttribute("attr1", "value1");
        element.addAttribute("attr2", "value2");
        assertEquals("value1", element.getAttribute("attr1"));
        assertEquals("value2", element.getAttribute("attr2"));
    }

    @Test
    public void testGetAttributeWithDefault() {
        XmlElement element = new XmlElement();
        assertEquals("defaultValue", element.getAttribute("attr1", "defaultValue"));
        element.addAttribute("attr1", "value1");
        assertEquals("value1", element.getAttribute("attr1", "defaultValue"));
    }

    @Test
    public void testGetAttributes() {
        XmlElement element = new XmlElement();
        element.addAttribute("attr1", "value1");
        element.addAttribute("attr2", "value2");
        Hashtable<String, String> attributes = new Hashtable<>();
        attributes.put("attr1", "value1");
        attributes.put("attr2", "value2");
        assertEquals(attributes, element.getAttributes());
    }

    @Test
    public void testSetAttributes() {
        XmlElement element = new XmlElement();
        Hashtable<String, String> attributes = new Hashtable<>();
        attributes.put("attr1", "value1");
        attributes.put("attr2", "value2");
        element.setAttributes(attributes);
        assertEquals(attributes, element.getAttributes());
    }

    @Test
    public void testGetAttributeNames() {
        XmlElement element = new XmlElement();
        element.addAttribute("attr1", "value1");
        element.addAttribute("attr2", "value2");
        Enumeration attributeNames = element.getAttributeNames();
        List<String> names = new ArrayList<>();
        while (attributeNames.hasMoreElements()) {
            names.add(attributeNames.nextElement().toString());
        }
        assertEquals(2, names.size());
        assertTrue(names.contains("attr1"));
        assertTrue(names.contains("attr2"));
    }

    @Test
    public void testAddElement() {
        XmlElement parentElement = new XmlElement();
        XmlElement childElement = new XmlElement("child");
        assertTrue(parentElement.addElement(childElement));
        assertEquals(1, parentElement.count());
        assertEquals(parentElement, childElement.getParent());
    }

    @Test
    public void testRemoveElementByObject() {
        XmlElement parentElement = new XmlElement();
        XmlElement childElement = new XmlElement("child");
        parentElement.addElement(childElement);
        XmlElement removedElement = parentElement.removeElement(childElement);
        assertEquals(childElement, removedElement);
        assertEquals(0, parentElement.count());
        assertNull(childElement.getParent());
    }

    @Test
    public void testRemoveElementByIndex() {
        XmlElement parentElement = new XmlElement();
        XmlElement childElement = new XmlElement("child");
        parentElement.addElement(childElement);
        XmlElement removedElement = parentElement.removeElement(0);
        assertEquals(childElement, removedElement);
        assertEquals(0, parentElement.count());
        assertNull(childElement.getParent());
    }

    @Test
    public void testRemoveAllElements() {
        XmlElement parentElement = new XmlElement();
        parentElement.addElement(new XmlElement("child1"));
        parentElement.addElement(new XmlElement("child2"));
        parentElement.removeAllElements();
        assertEquals(0, parentElement.count());
    }

    @Test
    public void testRemoveFromParent() {
        XmlElement parentElement = new XmlElement();
        XmlElement childElement = new XmlElement("child");
        parentElement.addElement(childElement);
        childElement.removeFromParent();
        assertEquals(0, parentElement.count());
        assertNull(childElement.getParent());
    }

    @Test
    public void testAppend() {
        XmlElement parentElement = new XmlElement();
        XmlElement childElement = new XmlElement("child");
        childElement.setData("data");
        parentElement.append(childElement);
        assertEquals("data", childElement.getData());
        assertEquals(1, parentElement.count());
        assertEquals(parentElement, childElement.getParent());
    }

    @Test
    public void testInsertElement() {
        XmlElement parentElement = new XmlElement();
        XmlElement child1 = new XmlElement("child1");
        XmlElement child2 = new XmlElement("child2");
        parentElement.addElement(child1);
        parentElement.insertElement(child2, 1);
        assertEquals(child2, parentElement.getElement(1));
        assertEquals(2, parentElement.count());
        assertEquals(parentElement, child2.getParent());
    }

    @Test
    public void testGetElements() {
        XmlElement parentElement = new XmlElement();
        XmlElement child1 = new XmlElement("child1");
        XmlElement child2 = new XmlElement("child2");
        parentElement.addElement(child1);
        parentElement.addElement(child2);
        List<XmlElement> elements = new ArrayList<>();
        elements.add(child1);
        elements.add(child2);
        assertEquals(elements, parentElement.getElements());
    }

    @Test
    public void testCount() {
        XmlElement parentElement = new XmlElement();
        parentElement.addElement(new XmlElement("child1"));
        parentElement.addElement(new XmlElement("child2"));
        assertEquals(2, parentElement.count());
    }

    @Test
    public void testGetElementByPath() {
        XmlElement parentElement = new XmlElement();
        XmlElement child1 = new XmlElement("child1");
        parentElement.addElement(child1);
        XmlElement child2 = new XmlElement("child2");
        XmlElement child3 = new XmlElement("child3");
        child1.addElement(child2);
        child2.addElement(child3);
        assertEquals(child1, parentElement.getElement("child1"));
        assertEquals(child2, parentElement.getElement("child1.child2"));
        assertEquals(child3, parentElement.getElement("child1.child2.child3"));
        assertNull(parentElement.getElement("child1.child2.child3.child4"));
    }

    @Test
    public void testGetElementByIndex() {
        XmlElement parentElement = new XmlElement();
        XmlElement child1 = new XmlElement("child1");
        parentElement.addElement(child1);
        XmlElement child2 = new XmlElement("child2");
        parentElement.addElement(child2);
        assertEquals(child1, parentElement.getElement(0));
        assertEquals(child2, parentElement.getElement(1));
    }

    @Test
    public void testAddSubElementByPath() {
        XmlElement parentElement = new XmlElement();
        XmlElement child1 = parentElement.addSubElement("child1.child2.child3");
        assertNotNull(child1);
        assertEquals(parentElement, child1.getParent());
        assertEquals("child3", child1.getName());
        XmlElement child4 = parentElement.addSubElement("child4");
        assertNotNull(child4);
        assertEquals(parentElement, child4.getParent());
        assertEquals("child4", child4.getName());
    }

    @Test
    public void testAddSubElementByObject() {
        XmlElement parentElement = new XmlElement();
        XmlElement childElement = new XmlElement("child");
        XmlElement addedElement = parentElement.addSubElement(childElement);
        assertEquals(childElement, addedElement);
        assertEquals(parentElement, childElement.getParent());
    }

    @Test
    public void testAddSubElementByNameAndData() {
        XmlElement parentElement = new XmlElement();
        XmlElement childElement = parentElement.addSubElement("child", "data");
        assertEquals(parentElement, childElement.getParent());
        assertEquals("child", childElement.getName());
        assertEquals("data", childElement.getData());
    }

    @Test
    public void testSetParent() {
        XmlElement parentElement = new XmlElement();
        XmlElement childElement = new XmlElement("child");
        childElement.setParent(parentElement);
        assertEquals(parentElement, childElement.getParent());
    }

    @Test
    public void testGetData() {
        XmlElement element = new XmlElement("element", "data");
        assertEquals("data", element.getData());
    }

    @Test
    public void testSetName() {
        XmlElement element = new XmlElement();
        element.setName("newName");
        assertEquals("newName", element.getName());
    }

    @Test
    public void testNotifyObservers() {
        XmlElement element = new XmlElement();
        TestObserver observer = new TestObserver();
        element.addObserver(observer);
        element.notifyObservers();
        assertTrue(observer.notified);
        element.deleteObserver(observer);
    }

    private class TestObserver implements Observer {
        public boolean notified = false;
        public void update(Observable o, Object arg) {
            notified = true;
        }
    }
}
