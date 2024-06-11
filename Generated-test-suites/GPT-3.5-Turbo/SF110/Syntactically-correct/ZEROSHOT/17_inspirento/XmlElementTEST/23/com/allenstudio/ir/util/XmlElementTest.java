import org.junit.Before;
import org.junit.Test;

import java.util.*;

import static org.junit.Assert.*;

public class XmlElementTest {

    private XmlElement xmlElement;
    private Hashtable<String, String> attributes;
    private List<XmlElement> subElements;

    @Before
    public void setUp() {
        xmlElement = new XmlElement();
        attributes = new Hashtable<String, String>();
        subElements = new ArrayList<XmlElement>();
    }

    @Test
    public void testAddAttribute() {
        assertNull(xmlElement.addAttribute("name", "value"));
        assertEquals("value", xmlElement.getAttribute("name"));
    }

    @Test
    public void testGetAttribute() {
        assertNull(xmlElement.getAttribute("name"));
        xmlElement.addAttribute("name", "value");
        assertEquals("value", xmlElement.getAttribute("name"));
    }

    @Test
    public void testGetAttributeWithDefaultValue() {
        assertEquals("defaultValue", xmlElement.getAttribute("name", "defaultValue"));
        xmlElement.addAttribute("name", "value");
        assertEquals("value", xmlElement.getAttribute("name", "defaultValue"));
    }

    @Test
    public void testGetAttributes() {
        assertEquals(attributes, xmlElement.getAttributes());
    }

    @Test
    public void testSetAttributes() {
        xmlElement.setAttributes(attributes);
        assertEquals(attributes, xmlElement.getAttributes());
    }

    @Test
    public void testGetAttributeNames() {
        assertEquals(attributes.keys(), xmlElement.getAttributeNames());
    }

    @Test
    public void testAddElement() {
        XmlElement e = new XmlElement("element");
        assertTrue(xmlElement.addElement(e));
        assertEquals(e, xmlElement.getElements().get(0));
    }

    @Test
    public void testRemoveElementByInstance() {
        XmlElement e = new XmlElement("element");
        xmlElement.addElement(e);
        assertEquals(e, xmlElement.removeElement(e));
        assertFalse(xmlElement.getElements().contains(e));
    }

    @Test
    public void testRemoveElementByIndex() {
        XmlElement e = new XmlElement("element");
        xmlElement.addElement(e);
        assertEquals(e, xmlElement.removeElement(0));
        assertFalse(xmlElement.getElements().contains(e));
    }

    @Test
    public void testRemoveAllElements() {
        XmlElement e1 = new XmlElement("element1");
        XmlElement e2 = new XmlElement("element2");
        xmlElement.addElement(e1);
        xmlElement.addElement(e2);
        xmlElement.removeAllElements();
        assertTrue(xmlElement.getElements().isEmpty());
    }

    @Test
    public void testRemoveFromParent() {
        XmlElement parent = new XmlElement("parent");
        XmlElement child = new XmlElement("child");
        parent.addElement(child);
        child.removeFromParent();
        assertFalse(parent.getElements().contains(child));
        assertNull(child.getParent());
    }

    @Test
    public void testAppend() {
        XmlElement parent = new XmlElement("parent");
        XmlElement child = new XmlElement("child");
        child.removeFromParent();
        parent.append(child);
        assertTrue(parent.getElements().contains(child));
        assertEquals(parent, child.getParent());
    }

    @Test
    public void testInsertElement() {
        XmlElement e1 = new XmlElement("element1");
        XmlElement e2 = new XmlElement("element2");
        xmlElement.addElement(e1);
        xmlElement.insertElement(e2, 0);
        assertEquals(e2, xmlElement.getElements().get(0));
    }

    @Test
    public void testGetElements() {
        assertEquals(subElements, xmlElement.getElements());
    }

    @Test
    public void testCount() {
        assertEquals(0, xmlElement.count());
        xmlElement.addElement(new XmlElement("element"));
        assertEquals(1, xmlElement.count());
    }

    @Test
    public void testGetElement() {
        XmlElement e1 = new XmlElement("element1");
        XmlElement e2 = new XmlElement("element2");
        xmlElement.addElement(e1);
        xmlElement.addElement(e2);
        assertEquals(e1, xmlElement.getElement("element1"));
        assertEquals(e2, xmlElement.getElement("element2"));
    }

    @Test
    public void testGetElementWithNestedPath() {
        XmlElement e1 = new XmlElement("element1");
        XmlElement e2 = new XmlElement("element2");
        XmlElement e3 = new XmlElement("element3");
        e2.addElement(e3);
        xmlElement.addElement(e1);
        xmlElement.addElement(e2);
        assertEquals(e3, xmlElement.getElement("element2.element3"));
    }

    @Test
    public void testGetElementWithInvalidPath() {
        XmlElement e1 = new XmlElement("element1");
        xmlElement.addElement(e1);
        assertNull(xmlElement.getElement(".element1"));
    }

    @Test
    public void testGetElementByIndex() {
        XmlElement e1 = new XmlElement("element1");
        XmlElement e2 = new XmlElement("element2");
        xmlElement.addElement(e1);
        xmlElement.addElement(e2);
        assertEquals(e2, xmlElement.getElement(1));
    }

    @Test
    public void testAddSubElementWithStringPath() {
        XmlElement e1 = xmlElement.addSubElement("element1.element2");
        assertNotNull(xmlElement.getElement("element1.element2"));
        assertEquals(e1, xmlElement.getElement("element1.element2"));
    }

    @Test
    public void testAddSubElementWithXmlElement() {
        XmlElement e = new XmlElement("element");
        xmlElement.addSubElement(e);
        assertTrue(xmlElement.getElements().contains(e));
        assertEquals(xmlElement, e.getParent());
    }

    @Test
    public void testAddSubElementWithStringNameAndData() {
        XmlElement e = xmlElement.addSubElement("element", "data");
        assertTrue(xmlElement.getElements().contains(e));
        assertEquals(xmlElement, e.getParent());
        assertEquals("data", e.getData());
    }

    @Test
    public void testSetParent() {
        XmlElement parent = new XmlElement("parent");
        xmlElement.setParent(parent);
        assertEquals(parent, xmlElement.getParent());
    }

    @Test
    public void testSetData() {
        xmlElement.setData("data");
        assertEquals("data", xmlElement.getData());
    }

    @Test
    public void testGetName() {
        xmlElement.setName("name");
        assertEquals("name", xmlElement.getName());
    }

    @Test
    public void testPrintNode() {
        XmlElement node = new XmlElement("element");
        String indent = "    ";
//        TODO: Update the test when the implementation of the printNode method is provided
//        printNode(node, indent);
    }

    @Test
    public void testClone() {
        XmlElement clone = (XmlElement) xmlElement.clone();
        assertEquals(xmlElement, clone);
        assertNotSame(xmlElement, clone);
    }

    @Test
    public void testNotifyObservers() {
        xmlElement.notifyObservers();
        assertTrue(xmlElement.hasChanged());
    }

    @Test
    public void testEqualsWithNull() {
        assertFalse(xmlElement.equals(null));
    }

    @Test
    public void testEqualsWithDifferentClass() {
        assertFalse(xmlElement.equals("string"));
    }

    @Test
    public void testEqualsWithDifferentAttributes() {
        XmlElement other = new XmlElement();
        other.addAttribute("name", "value");
        assertFalse(xmlElement.equals(other));
    }

    @Test
    public void testEqualsWithDifferentData() {
        XmlElement other = new XmlElement();
        other.setData("data");
        assertFalse(xmlElement.equals(other));
    }

    @Test
    public void testEqualsWithDifferentName() {
        XmlElement other = new XmlElement();
        other.setName("name");
        assertFalse(xmlElement.equals(other));
    }

    @Test
    public void testEqualsWithDifferentSubElements() {
        XmlElement other = new XmlElement();
        other.addElement(new XmlElement("element"));
        assertFalse(xmlElement.equals(other));
    }

    @Test
    public void testHashCode() {
        int hashCode = 23;
        if (attributes != null) {
            hashCode += attributes.hashCode() * 13;
        }
        if (xmlElement.getData() != null) {
            hashCode += xmlElement.getData().hashCode() * 17;
        }
        if (xmlElement.getName() != null) {
            hashCode += xmlElement.getName().hashCode() * 29;
        }
        if (subElements != null) {
            hashCode += subElements.hashCode() * 57;
        }
        assertEquals(hashCode, xmlElement.hashCode());
    }

}