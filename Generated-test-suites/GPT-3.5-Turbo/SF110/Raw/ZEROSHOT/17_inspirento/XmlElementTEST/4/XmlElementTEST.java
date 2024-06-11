import org.junit.Before;
import org.junit.Test;

import java.util.Enumeration;
import java.util.Hashtable;
import java.util.List;
import java.util.Vector;

import static org.junit.Assert.*;

public class XmlElementTest {

    private XmlElement xmlElement;

    @Before
    public void setUp() {
        xmlElement = new XmlElement();
    }

    @Test
    public void testAddAttribute() {
        // Test null value and name
        assertNull(xmlElement.addAttribute(null, null));

        // Test non-null value and null name
        assertNull(xmlElement.addAttribute(null, "value"));

        // Test null value and non-null name
        assertNull(xmlElement.addAttribute("name", null));

        // Test non-null value and name
        assertNull(xmlElement.addAttribute("name", "value"));
    }

    @Test
    public void testGetAttribute() {
        assertNull(xmlElement.getAttribute("name"));
    }

    @Test
    public void testGetAttributeWithValue() {
        xmlElement.addAttribute("name", "value");
        assertEquals("value", xmlElement.getAttribute("name"));
    }

    @Test
    public void testGetAttributeWithDefaultValue() {
        assertNull(xmlElement.getAttribute("name", "defaultValue"));
    }

    @Test
    public void testGetAttributeWithDefaultValueAndExistingAttribute() {
        xmlElement.addAttribute("name", "value");
        assertEquals("value", xmlElement.getAttribute("name", "defaultValue"));
    }

    @Test
    public void testGetAttributes() {
        Hashtable<String, String> attributes = new Hashtable<>();
        xmlElement.setAttributes(attributes);
        assertSame(attributes, xmlElement.getAttributes());
    }

    @Test
    public void testSetAttributes() {
        Hashtable<String, String> attributes = new Hashtable<>();
        xmlElement.setAttributes(attributes);
        assertSame(attributes, xmlElement.getAttributes());
    }

    @Test
    public void testGetAttributeNames() {
        assertNotNull(xmlElement.getAttributeNames());
    }

    @Test
    public void testAddElement() {
        XmlElement element = new XmlElement("name");
        assertTrue(xmlElement.addElement(element));
    }

    @Test
    public void testRemoveElement() {
        XmlElement element = new XmlElement("name");
        xmlElement.addElement(element);
        assertSame(element, xmlElement.removeElement(element));
    }

    @Test
    public void testRemoveElementByIndex() {
        XmlElement element = new XmlElement("name");
        xmlElement.addElement(element);
        assertSame(element, xmlElement.removeElement(0));
    }

    @Test
    public void testRemoveAllElements() {
        xmlElement.addElement(new XmlElement("name1"));
        xmlElement.addElement(new XmlElement("name2"));
        xmlElement.addElement(new XmlElement("name3"));
        xmlElement.removeAllElements();
        assertEquals(0, xmlElement.count());
    }

    @Test
    public void testRemoveFromParentWithNullParent() {
        xmlElement.removeFromParent();
        assertNull(xmlElement.getParent());
    }

    @Test
    public void testRemoveFromParentWithValidParent() {
        XmlElement parent = new XmlElement("parent");
        parent.addElement(xmlElement);
        xmlElement.removeFromParent();
        assertNull(xmlElement.getParent());
        assertEquals(0, parent.count());
    }

    @Test
    public void testAppend() {
        XmlElement element = new XmlElement("name");
        xmlElement.append(element);
        assertNull(element.getParent());
        assertEquals(1, xmlElement.count());
    }

    @Test
    public void testInsertElement() {
        XmlElement element1 = new XmlElement("name1");
        xmlElement.append(element1);

        XmlElement element2 = new XmlElement("name2");
        xmlElement.insertElement(element2, 0);

        assertEquals(2, xmlElement.count());
        assertSame(element2, xmlElement.getElement(0));
    }

    @Test
    public void testGetElements() {
        assertNotNull(xmlElement.getElements());
    }

    @Test
    public void testCount() {
        assertEquals(0, xmlElement.count());
    }

    @Test
    public void testGetElement() {
        assertNull(xmlElement.getElement("path"));
    }

    @Test
    public void testGetElementWithValidPath() {
        XmlElement element = new XmlElement("name");
        xmlElement.addElement(element);
        assertSame(element, xmlElement.getElement("name"));
    }

    @Test
    public void testGetElementWithNestedPath() {
        XmlElement element1 = xmlElement.addSubElement("name1");
        xmlElement.addSubElement(element1).addElement(new XmlElement("name2"));
        assertSame(element1.getElement("name2"), xmlElement.getElement("name1.name2"));
    }

    @Test
    public void testGetElementByIndex() {
        XmlElement element = new XmlElement("name");
        xmlElement.addElement(element);
        assertSame(element, xmlElement.getElement(0));
    }

    @Test
    public void testAddSubElementWithStringPath() {
        XmlElement element = xmlElement.addSubElement("path");
        assertNotNull(element);
        assertSame(element, xmlElement.getElement("path"));
    }

    @Test
    public void testAddSubElementWithXmlElement() {
        XmlElement subElement = new XmlElement("subElement");
        xmlElement.addSubElement(subElement);
        assertSame(subElement, xmlElement.getElement(0));
    }

    @Test
    public void testAddSubElementWithStringNameAndData() {
        xmlElement.addSubElement("name", "data");
        assertEquals("data", xmlElement.getElement(0).getData());
    }

    @Test
    public void testSetParent() {
        XmlElement parent = new XmlElement("parent");
        xmlElement.setParent(parent);
        assertSame(parent, xmlElement.getParent());
    }

    @Test
    public void testGetData() {
        assertNull(xmlElement.getData());
    }

    @Test
    public void testSetData() {
        xmlElement.setData("data");
        assertEquals("data", xmlElement.getData());
    }

    @Test
    public void testGetName() {
        assertNull(xmlElement.getName());
    }

    @Test
    public void testSetName() {
        xmlElement.setName("name");
        assertEquals("name", xmlElement.getName());
    }

    @Test
    public void testPrintNodeWithEmptyData() {
        XmlElement node = new XmlElement("node");
        node.addAttribute("attribute", "value");
        assertEquals("node\nattribute:value\n", node.printNode(node, ""));
    }

    @Test
    public void testPrintNodeWithData() {
        XmlElement node = new XmlElement("node", "data");
        node.addAttribute("attribute", "value");
        assertEquals("node = 'data'\nattribute:value\n", node.printNode(node, ""));
    }

    @Test
    public void testClone() throws CloneNotSupportedException {
        XmlElement clone = (XmlElement) xmlElement.clone();
        assertEquals(xmlElement, clone);
        assertNotSame(xmlElement, clone);
    }

    @Test
    public void testNotifyObservers() {
        xmlElement.addObserver((o, arg) -> {
            // Do nothing
        });
        xmlElement.notifyObservers();
        assertTrue(xmlElement.hasChanged());
    }

}