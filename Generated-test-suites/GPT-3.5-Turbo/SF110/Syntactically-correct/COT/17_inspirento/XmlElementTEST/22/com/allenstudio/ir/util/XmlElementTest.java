
import org.junit.Test;
import static org.junit.Assert.*;

public class XmlElementTest {

    @Test
    public void testAddAttribute() {
        XmlElement xmlElement = new XmlElement();
        Object returnValue = xmlElement.addAttribute("attr1", "value1");
        assertNull(returnValue);
        assertEquals("value1", xmlElement.getAttribute("attr1"));
    }

    @Test
    public void testGetAttribute() {
        XmlElement xmlElement = new XmlElement();
        xmlElement.addAttribute("attr1", "value1");
        assertEquals("value1", xmlElement.getAttribute("attr1"));
    }

    @Test
    public void testGetAttributeWithDefaultValue() {
        XmlElement xmlElement = new XmlElement();
        String defaultValue = "defaultValue";
        String result = xmlElement.getAttribute("attr1", defaultValue);
        assertEquals(defaultValue, result);
        assertEquals(defaultValue, xmlElement.getAttribute("attr1"));
    }

    @Test
    public void testGetAttributes() {
        XmlElement xmlElement = new XmlElement();
        assertNotNull(xmlElement.getAttributes());
    }

    @Test
    public void testSetAttributes() {
        XmlElement xmlElement = new XmlElement();
        Hashtable<String, String> attributes = new Hashtable<>();
        xmlElement.setAttributes(attributes);
        assertEquals(attributes, xmlElement.getAttributes());
    }

    @Test
    public void testGetAttributeNames() {
        XmlElement xmlElement = new XmlElement();
        assertNotNull(xmlElement.getAttributeNames());
        assertFalse(xmlElement.getAttributeNames().hasMoreElements());
    }

    @Test
    public void testAddElement() {
        XmlElement xmlElement = new XmlElement();
        XmlElement childElement = new XmlElement();
        assertTrue(xmlElement.addElement(childElement));
        assertEquals(childElement, xmlElement.getElements().get(0));
        assertEquals(xmlElement, childElement.getParent());
    }

    @Test
    public void testRemoveElementByObject() {
        XmlElement xmlElement = new XmlElement();
        XmlElement childElement = new XmlElement();
        xmlElement.addElement(childElement);
        XmlElement removed = xmlElement.removeElement(childElement);
        assertEquals(childElement, removed);
        assertTrue(xmlElement.getElements().isEmpty());
        assertNull(childElement.getParent());
    }

    @Test
    public void testRemoveElementByIndex() {
        XmlElement xmlElement = new XmlElement();
        XmlElement childElement = new XmlElement();
        xmlElement.addElement(childElement);
        XmlElement removed = xmlElement.removeElement(0);
        assertEquals(childElement, removed);
        assertTrue(xmlElement.getElements().isEmpty());
        assertNull(childElement.getParent());
    }

    @Test
    public void testRemoveAllElements() {
        XmlElement xmlElement = new XmlElement();
        XmlElement childElement1 = new XmlElement();
        XmlElement childElement2 = new XmlElement();
        xmlElement.addElement(childElement1);
        xmlElement.addElement(childElement2);
        xmlElement.removeAllElements();
        assertTrue(xmlElement.getElements().isEmpty());
        assertNull(childElement1.getParent());
        assertNull(childElement2.getParent());
    }

    @Test
    public void testRemoveFromParent() {
        XmlElement parentElement = new XmlElement();
        XmlElement childElement = new XmlElement();
        parentElement.addElement(childElement);
        childElement.removeFromParent();
        assertTrue(parentElement.getElements().isEmpty());
        assertNull(childElement.getParent());
    }

    @Test
    public void testAppend() {
        XmlElement parentElement = new XmlElement();
        XmlElement childElement1 = new XmlElement();
        XmlElement childElement2 = new XmlElement();
        parentElement.addElement(childElement1);
        childElement2.removeFromParent();
        parentElement.append(childElement2);
        assertEquals(childElement2, parentElement.getElements().get(0));
        assertNull(childElement1.getParent());
        assertEquals(parentElement, childElement2.getParent());
    }

    @Test
    public void testInsertElement() {
        XmlElement xmlElement = new XmlElement();
        XmlElement childElement1 = new XmlElement();
        xmlElement.addElement(childElement1);
        XmlElement childElement2 = new XmlElement();
        xmlElement.insertElement(childElement2, 0);
        assertEquals(childElement2, xmlElement.getElements().get(0));
        assertEquals(childElement1, xmlElement.getElements().get(1));
        assertNull(childElement1.getParent());
        assertEquals(xmlElement, childElement2.getParent());
    }

    @Test
    public void testGetElements() {
        XmlElement xmlElement = new XmlElement();
        assertNotNull(xmlElement.getElements());
    }

    @Test
    public void testCount() {
        XmlElement xmlElement = new XmlElement();
        assertEquals(0, xmlElement.count());
        xmlElement.addElement(new XmlElement());
        assertEquals(1, xmlElement.count());
    }

    @Test
    public void testGetElement() {
        XmlElement xmlElement = new XmlElement();
        XmlElement childElement = new XmlElement("child");
        xmlElement.addElement(childElement);
        assertEquals(childElement, xmlElement.getElement("child"));
    }

    @Test
    public void testGetElementWithNestedPath() {
        XmlElement xmlElement = new XmlElement();
        XmlElement childElement1 = new XmlElement("child1");
        XmlElement childElement2 = new XmlElement("child2");
        childElement1.addElement(childElement2);
        xmlElement.addElement(childElement1);
        assertEquals(childElement2, xmlElement.getElement("child1.child2"));
    }

    @Test
    public void testGetElementWithNonExistingPath() {
        XmlElement xmlElement = new XmlElement();
        assertNull(xmlElement.getElement("nonExistingChild"));
    }

    @Test
    public void testGetElementWithEmptyPath() {
        XmlElement xmlElement = new XmlElement("root");
        assertEquals(xmlElement, xmlElement.getElement(""));
    }

    @Test
    public void testGetElementByIndex() {
        XmlElement xmlElement = new XmlElement();
        XmlElement childElement1 = new XmlElement();
        XmlElement childElement2 = new XmlElement();
        xmlElement.addElement(childElement1);
        xmlElement.addElement(childElement2);
        assertEquals(childElement2, xmlElement.getElement(1));
    }

    @Test
    public void testAddSubElementWithNestedPath() {
        XmlElement xmlElement = new XmlElement();
        XmlElement childElement1 = xmlElement.addSubElement("child1.child11");
        XmlElement childElement2 = xmlElement.addSubElement("child1.child12");
        assertNull(xmlElement.getElement("nonExistingChild"));
        assertEquals(childElement1, xmlElement.getElement("child1"));
        assertEquals(childElement2, xmlElement.getElement("child1.child12"));
    }

    @Test
    public void testAddSubElementWithXmlElement() {
        XmlElement xmlElement = new XmlElement();
        XmlElement childElement = new XmlElement();
        xmlElement.addSubElement(childElement);
        assertEquals(childElement, xmlElement.getElements().get(0));
        assertEquals(xmlElement, childElement.getParent());
    }

    @Test
    public void testAddSubElementWithXmlElementAndData() {
        XmlElement xmlElement = new XmlElement();
        XmlElement childElement = xmlElement.addSubElement("child", "data");
        assertEquals(childElement, xmlElement.getElements().get(0));
        assertEquals(xmlElement, childElement.getParent());
        assertEquals("data", childElement.getData());
    }

    @Test
    public void testSetParent() {
        XmlElement xmlElement1 = new XmlElement();
        XmlElement xmlElement2 = new XmlElement();
        assertNull(xmlElement1.getParent());
        assertNull(xmlElement2.getParent());
        xmlElement1.setParent(xmlElement2);
        assertEquals(xmlElement2, xmlElement1.getParent());
        assertNull(xmlElement2.getParent());
    }

    @Test
    public void testGetData() {
        XmlElement xmlElement = new XmlElement();
        assertNull(xmlElement.getData());
        xmlElement.setData("data");
        assertEquals("data", xmlElement.getData());
    }

    @Test
    public void testGetName() {
        XmlElement xmlElement = new XmlElement("name");
        assertEquals("name", xmlElement.getName());
    }

    @Test
    public void testPrintNode() {
        XmlElement rootElement = new XmlElement("root");
        XmlElement childElement1 = rootElement.addSubElement("child1");
        childElement1.addAttribute("attr1", "value1");
        XmlElement childElement2 = rootElement.addSubElement("child2");
        childElement1.addAttribute("attr2", "value2");
        XmlElement childElement3 = childElement1.addSubElement("child3");
        childElement3.setData("data");
        XmlElement.printNode(rootElement, "");
    }

    @Test
    public void testClone() {
        XmlElement xmlElement = new XmlElement("name");
        XmlElement childElement = new XmlElement();
        childElement.addData("data");
        xmlElement.addElement(childElement);

        XmlElement cloneElement = (XmlElement) xmlElement.clone();

        assertEquals(xmlElement, cloneElement);
        assertNotSame(xmlElement, cloneElement);
        assertNotSame(xmlElement.getElements().get(0), cloneElement.getElements().get(0));
    }

    @Test
    public void testEqualsWithSameObject() {
        XmlElement xmlElement = new XmlElement();
        assertTrue(xmlElement.equals(xmlElement));
    }

    @Test
    public void testEqualsWithDifferentObjectType() {
        XmlElement xmlElement = new XmlElement();
        assertFalse(xmlElement.equals("string"));
    }

    @Test
    public void testEqualsWithDifferentXmlElement() {
        XmlElement xmlElement1 = new XmlElement("name1");
        XmlElement xmlElement2 = new XmlElement("name2");
        assertFalse(xmlElement1.equals(xmlElement2));
    }

    @Test
    public void testHashCode() {
        XmlElement xmlElement = new XmlElement();
        assertNotNull(xmlElement.hashCode());
    }
}

