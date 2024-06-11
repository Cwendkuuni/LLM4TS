
import org.junit.Before;
import org.junit.Test;

import java.util.*;

import static org.junit.Assert.*;

public class XmlElementTest {

    private XmlElement xmlElement;

    @Before
    public void setUp() {
        xmlElement = new XmlElement();
    }

    @Test
    public void testAddAttribute() {
        assertNull(xmlElement.addAttribute("name", "value"));
        assertEquals("value", xmlElement.getAttribute("name"));
    }

    @Test
    public void testGetAttribute() {
        xmlElement.addAttribute("name", "value");
        assertEquals("value", xmlElement.getAttribute("name"));
    }

    @Test
    public void testGetAttributeWithDefaultValue() {
        assertEquals("defaultValue", xmlElement.getAttribute("name", "defaultValue"));
    }

    @Test
    public void testGetAttributes() {
        Hashtable<String, String> attributes = new Hashtable<>();
        attributes.put("name1", "value1");
        attributes.put("name2", "value2");
        xmlElement.setAttributes(attributes);

        assertEquals(attributes, xmlElement.getAttributes());
    }

    @Test
    public void testSetAttributes() {
        Hashtable<String, String> attributes = new Hashtable<>();
        attributes.put("name1", "value1");
        attributes.put("name2", "value2");
        xmlElement.setAttributes(attributes);

        assertEquals(attributes, xmlElement.getAttributes());
    }

    @Test
    public void testGetAttributeNames() {
        Hashtable<String, String> attributes = new Hashtable<>();
        attributes.put("name1", "value1");
        attributes.put("name2", "value2");
        xmlElement.setAttributes(attributes);

        Enumeration<String> attributeNames = xmlElement.getAttributeNames();
        List<String> expectedNames = Collections.list(attributeNames);

        assertEquals(expectedNames.size(), attributes.size());
        assertTrue(expectedNames.containsAll(attributes.keySet()));
    }

    @Test
    public void testAddElement() {
        XmlElement element = new XmlElement("element");
        assertTrue(xmlElement.addElement(element));
        assertEquals(1, xmlElement.count());
    }

    @Test
    public void testRemoveElementByObject() {
        XmlElement element = new XmlElement("element");
        xmlElement.addElement(element);
        assertEquals(element, xmlElement.removeElement(element));
        assertEquals(0, xmlElement.count());
    }

    @Test
    public void testRemoveElementByIndex() {
        XmlElement element1 = new XmlElement("element1");
        XmlElement element2 = new XmlElement("element2");
        xmlElement.addElement(element1);
        xmlElement.addElement(element2);

        assertEquals(element1, xmlElement.removeElement(0));
        assertEquals(element2, xmlElement.getElement(0));
    }

    @Test
    public void testRemoveAllElements() {
        XmlElement element1 = new XmlElement("element1");
        XmlElement element2 = new XmlElement("element2");
        xmlElement.addElement(element1);
        xmlElement.addElement(element2);

        xmlElement.removeAllElements();
        assertEquals(0, xmlElement.count());
    }

    @Test
    public void testRemoveFromParent() {
        XmlElement element1 = new XmlElement("element1");
        xmlElement.addElement(element1);

        element1.removeFromParent();
        assertEquals(0, xmlElement.count());
        assertNull(element1.getParent());
    }

    @Test
    public void testAppend() {
        XmlElement element1 = new XmlElement("element1");
        XmlElement element2 = new XmlElement("element2");

        element1.append(element2);
        assertEquals(1, element1.count());
        assertEquals(element1, element2.getParent());
    }

    @Test
    public void testInsertElement() {
        XmlElement element1 = new XmlElement("element1");
        XmlElement element2 = new XmlElement("element2");
        XmlElement element3 = new XmlElement("element3");

        xmlElement.addElement(element1);
        xmlElement.addElement(element3);
        xmlElement.insertElement(element2, 1);

        assertEquals(element2, xmlElement.getElement(1));
        assertEquals(xmlElement, element2.getParent());
    }

    @Test
    public void testGetElements() {
        XmlElement element1 = new XmlElement("element1");
        XmlElement element2 = new XmlElement("element2");

        xmlElement.addElement(element1);
        xmlElement.addElement(element2);

        List<XmlElement> elements = xmlElement.getElements();
        assertEquals(2, elements.size());
        assertTrue(elements.contains(element1));
        assertTrue(elements.contains(element2));
    }

    @Test
    public void testCount() {
        XmlElement element1 = new XmlElement("element1");
        XmlElement element2 = new XmlElement("element2");

        xmlElement.addElement(element1);
        xmlElement.addElement(element2);

        assertEquals(2, xmlElement.count());
    }

    @Test
    public void testGetElementByPath() {
        XmlElement parent = new XmlElement("parent");
        XmlElement child1 = new XmlElement("child1");
        XmlElement child2 = new XmlElement("child2");
        XmlElement grandchild = new XmlElement("grandchild");
        child2.addSubElement(grandchild);
        parent.addElement(child1);
        parent.addElement(child2);

        assertEquals(parent, parent.getElement("."));
        assertEquals(child1, parent.getElement("child1"));
        assertEquals(grandchild, parent.getElement("child2.grandchild"));
    }

    @Test
    public void testGetElementByIndex() {
        XmlElement element1 = new XmlElement("element1");
        XmlElement element2 = new XmlElement("element2");

        xmlElement.addElement(element1);
        xmlElement.addElement(element2);

        assertEquals(element1, xmlElement.getElement(0));
        assertEquals(element2, xmlElement.getElement(1));
    }

    @Test
    public void testAddSubElementByPath() {
        XmlElement element1 = xmlElement.addSubElement("element1");

        assertEquals(element1, xmlElement.getElement("element1"));
    }

    @Test
    public void testAddSubElementByObject() {
        XmlElement element = new XmlElement("element");
        xmlElement.addSubElement(element);

        assertEquals(element, xmlElement.getElement(0));
    }

    @Test
    public void testAddSubElementWithNameAndData() {
        XmlElement element = xmlElement.addSubElement("element", "data");

        assertEquals(element, xmlElement.getElement(0));
        assertEquals("data", element.getData());
    }

    @Test
    public void testSetParent() {
        XmlElement parent = new XmlElement("parent");
        XmlElement child = new XmlElement("child");
        parent.addSubElement(child);

        assertEquals(parent, child.getParent());
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
        // TODO: Implement test
    }

    @Test
    public void testClone() {
        XmlElement clonedElement = (XmlElement) xmlElement.clone();

        assertNotSame(xmlElement, clonedElement);
        assertEquals(xmlElement, clonedElement);
    }

    @Test
    public void testNotifyObservers() {
        // TODO: Implement test
    }

    @Test
    public void testEquals() {
        XmlElement element1 = new XmlElement("element");
        XmlElement element2 = new XmlElement("element");

        assertTrue(element1.equals(element2));
    }

    @Test
    public void testHashCode() {
        // TODO: Implement test
    }
}

