package com.allenstudio.ir.util;

import org.junit.Before;
import org.junit.Test;

import java.util.Hashtable;

import static org.junit.Assert.*;

public class XmlElementTest {
    
    private XmlElement xmlElement;
    
    @Before
    public void setup() {
        xmlElement = new XmlElement("test");
    }
    
    @Test
    public void testAddAttribute_validInput_success() {
        assertNull(xmlElement.addAttribute("attr1", "value1"));
        assertEquals("value1", xmlElement.getAttribute("attr1"));
    }
    
    @Test
    public void testAddAttribute_nullName_returnsNull() {
        assertNull(xmlElement.addAttribute(null, "value1"));
    }
    
    @Test
    public void testAddAttribute_nullValue_returnsNull() {
        assertNull(xmlElement.addAttribute("attr1", null));
    }
    
    @Test
    public void testGetAttribute_validAttribute_returnsValue() {
        xmlElement.addAttribute("attr1", "value1");
        assertEquals("value1", xmlElement.getAttribute("attr1"));
    }
    
    @Test
    public void testGetAttribute_invalidAttribute_returnsNull() {
        assertNull(xmlElement.getAttribute("attr1"));
    }
    
    @Test
    public void testGetAttribute_defaultValueExists_returnsDefaultValue() {
        xmlElement.addAttribute("attr1", "value1");
        assertEquals("value1", xmlElement.getAttribute("attr1", "defaultValue"));
    }
    
    @Test
    public void testGetAttribute_defaultValueDoesNotExist_returnsNull() {
        assertNull(xmlElement.getAttribute("attr2", "defaultValue"));
    }
    
    @Test
    public void testGetAttributes_validInput_success() {
        Hashtable<String, String> attributes = new Hashtable<>();
        attributes.put("attr1", "value1");
        attributes.put("attr2", "value2");
        xmlElement.setAttributes(attributes);
        assertEquals(attributes, xmlElement.getAttributes());
    }
    
    @Test
    public void testSetAttributes_validInput_success() {
        Hashtable<String, String> attributes = new Hashtable<>();
        attributes.put("attr1", "value1");
        attributes.put("attr2", "value2");
        xmlElement.setAttributes(attributes);
        assertEquals(attributes, xmlElement.getAttributes());
    }
    
    @Test
    public void testGetAttributeNames_validInput_success() {
        Hashtable<String, String> attributes = new Hashtable<>();
        attributes.put("attr1", "value1");
        attributes.put("attr2", "value2");
        xmlElement.setAttributes(attributes);
        assertEquals(attributes.keys(), xmlElement.getAttributeNames());
    }
    
    @Test
    public void testAddElement_validInput_success() {
        XmlElement childElement = new XmlElement("child");
        assertTrue(xmlElement.addElement(childElement));
        assertTrue(xmlElement.getElements().contains(childElement));
    }
    
    @Test
    public void testRemoveElement_validInput_success() {
        XmlElement childElement = new XmlElement("child");
        xmlElement.addElement(childElement);
        assertEquals(childElement, xmlElement.removeElement(childElement));
        assertFalse(xmlElement.getElements().contains(childElement));
    }
    
    @Test
    public void testRemoveElement_invalidInput_returnsNull() {
        XmlElement childElement = new XmlElement("child");
        assertNull(xmlElement.removeElement(childElement));
    }
    
    @Test
    public void testRemoveElementByIndex_validIndex_success() {
        XmlElement childElement = new XmlElement("child");
        xmlElement.addElement(childElement);
        assertEquals(childElement, xmlElement.removeElement(0));
        assertFalse(xmlElement.getElements().contains(childElement));
    }
    
    @Test
    public void testRemoveElementByIndex_invalidIndex_returnsNull() {
        assertNull(xmlElement.removeElement(0));
    }
    
    @Test
    public void testRemoveAllElements_validInput_success() {
        XmlElement childElement = new XmlElement("child");
        xmlElement.addElement(childElement);
        xmlElement.removeAllElements();
        assertTrue(xmlElement.getElements().isEmpty());
    }
    
    @Test
    public void testRemoveFromParent_rootElement_noChanges() {
        XmlElement childElement = new XmlElement("child");
        XmlElement parentElement = new XmlElement("parent");
        parentElement.addElement(childElement);
        childElement.removeFromParent();
        assertFalse(parentElement.getElements().isEmpty());
    }
    
    @Test
    public void testRemoveFromParent_childElement_success() {
        XmlElement childElement = new XmlElement("child");
        xmlElement.addElement(childElement);
        childElement.removeFromParent();
        assertTrue(xmlElement.getElements().isEmpty());
    }
    
    @Test
    public void testAppend_validInput_success() {
        XmlElement childElement = new XmlElement("child");
        childElement.setParent(xmlElement);
        xmlElement.append(childElement);
        assertTrue(xmlElement.getElements().contains(childElement));
    }
    
    @Test
    public void testInsertElement_validInput_success() {
        XmlElement childElement1 = new XmlElement("child1");
        XmlElement childElement2 = new XmlElement("child2");
        xmlElement.addElement(childElement1);
        xmlElement.insertElement(childElement2, 0);
        assertEquals(childElement2, xmlElement.getElement(0));
    }
    
    @Test
    public void testGetElements_validInput_success() {
        XmlElement childElement1 = new XmlElement("child1");
        XmlElement childElement2 = new XmlElement("child2");
        xmlElement.addElement(childElement1);
        xmlElement.addElement(childElement2);
        assertEquals(2, xmlElement.getElements().size());
        assertTrue(xmlElement.getElements().contains(childElement1));
        assertTrue(xmlElement.getElements().contains(childElement2));
    }
    
    @Test
    public void testCount_validInput_success() {
        XmlElement childElement1 = new XmlElement("child1");
        XmlElement childElement2 = new XmlElement("child2");
        xmlElement.addElement(childElement1);
        xmlElement.addElement(childElement2);
        assertEquals(2, xmlElement.count());
    }
    
    @Test
    public void testGetElement_pathExists_success() {
        XmlElement childElement1 = new XmlElement("child1");
        XmlElement childElement2 = new XmlElement("child2");
        xmlElement.addElement(childElement1);
        xmlElement.addElement(childElement2);
        assertEquals(childElement2, xmlElement.getElement("child2"));
    }
    
    @Test
    public void testGetElement_pathDoesNotExist_returnsNull() {
        XmlElement childElement1 = new XmlElement("child1");
        xmlElement.addElement(childElement1);
        assertNull(xmlElement.getElement("child2"));
    }
    
    @Test
    public void testGetElementByIndex_validIndex_success() {
        XmlElement childElement1 = new XmlElement("child1");
        xmlElement.addElement(childElement1);
        assertEquals(childElement1, xmlElement.getElement(0));
    }
    
    @Test
    public void testAddSubElement_pathValid_success() {
        XmlElement subElement = xmlElement.addSubElement("path1.path2.path3");
        assertNotNull(subElement);
        assertEquals(subElement, xmlElement.getElement("path1.path2.path3"));
    }
    
    @Test
    public void testAddSubElement_elementValid_success() {
        XmlElement childElement = new XmlElement("child");
        xmlElement.addSubElement(childElement);
        assertEquals(childElement, xmlElement.getElement("child"));
    }
    
    @Test
    public void testAddSubElement_nameAndDataValid_success() {
        XmlElement subElement = xmlElement.addSubElement("subElement", "data");
        assertNotNull(subElement);
        assertEquals("data", subElement.getData());
        assertEquals(subElement, xmlElement.getElement("subElement"));
    }
    
    @Test
    public void testSetParent_validInput_success() {
        XmlElement parentElement = new XmlElement("parent");
        xmlElement.setParent(parentElement);
        assertEquals(parentElement, xmlElement.getParent());
    }
    
    @Test
    public void testGetData_validInput_success() {
        xmlElement.setData("data");
        assertEquals("data", xmlElement.getData());
    }
    
    @Test
    public void testSetName_validInput_success() {
        xmlElement.setName("name");
        assertEquals("name", xmlElement.getName());
    }
    
    @Test
    public void testClone_validInput_success() {
        XmlElement clonedElement = (XmlElement) xmlElement.clone();
        assertEquals(xmlElement, clonedElement);
        assertNotSame(xmlElement, clonedElement);
    }
    
    @Test
    public void testEquals_sameXmlElements_true() {
        XmlElement xmlElement1 = new XmlElement("test");
        XmlElement xmlElement2 = new XmlElement("test");
        assertEquals(xmlElement1, xmlElement2);
    }
    
    @Test
    public void testEquals_differentXmlElements_false() {
        XmlElement xmlElement1 = new XmlElement("test");
        XmlElement xmlElement2 = new XmlElement("test1");
        assertNotEquals(xmlElement1, xmlElement2);
    }
    
    @Test
    public void testHashCode_sameXmlElements_equalsHashCode() {
        XmlElement xmlElement1 = new XmlElement("test");
        XmlElement xmlElement2 = new XmlElement("test");
        assertEquals(xmlElement1.hashCode(), xmlElement2.hashCode());
    }
    
    @Test
    public void testHashCode_differentXmlElements_notEqualsHashCode() {
        XmlElement xmlElement1 = new XmlElement("test");
        XmlElement xmlElement2 = new XmlElement("test1");
        assertNotEquals(xmlElement1.hashCode(), xmlElement2.hashCode());
    }
}