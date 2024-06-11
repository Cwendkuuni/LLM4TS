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
        Object returnValue = xmlElement.addAttribute("key1", "value1");
        assertEquals("value1", xmlElement.getAttributes().get("key1"));
    }

    @Test
    public void testGetAttribute() {
        xmlElement.addAttribute("key1", "value1");
        assertEquals("value1", xmlElement.getAttribute("key1"));
    }

    @Test
    public void testGetAttributeWithDefaultValue() {
        assertEquals("default", xmlElement.getAttribute("nonexistent", "default"));
    }

    @Test
    public void testSetAttributes() {
        Hashtable<String, String> attrs = new Hashtable<String, String>();
        attrs.put("key1", "value1");
        xmlElement.setAttributes(attrs);
        assertEquals(attrs, xmlElement.getAttributes());
    }

    @Test
    public void testAddElement() {
        XmlElement childElement = new XmlElement("child");
        assertTrue(xmlElement.addElement(childElement));
    }

    @Test
    public void testRemoveElementByObject() {
        XmlElement childElement = new XmlElement("child");
        xmlElement.addElement(childElement);
        assertNotNull(xmlElement.removeElement(childElement));
    }

    @Test
    public void testRemoveElementByIndex() {
        XmlElement childElement = new XmlElement("child");
        xmlElement.addElement(childElement);
        assertNotNull(xmlElement.removeElement(0));
    }

    @Test
    public void testRemoveAllElements() {
        xmlElement.removeAllElements();
        assertTrue(xmlElement.getElements().isEmpty());
    }

    @Test
    public void testRemoveFromParent() {
        XmlElement childElement = new XmlElement("child");
        xmlElement.addElement(childElement);
        childElement.removeFromParent();
        assertNull(childElement.getParent());
    }

    @Test
    public void testAppend() {
        XmlElement childElement = new XmlElement("child");
        xmlElement.append(childElement);
        assertEquals(xmlElement, childElement.getParent());
    }

    @Test
    public void testInsertElement() {
        XmlElement childElement = new XmlElement("child");
        xmlElement.insertElement(childElement, 0);
        assertEquals(childElement, xmlElement.getElement(0));
    }

    @Test
    public void testCount() {
        XmlElement childElement = new XmlElement("child");
        xmlElement.addElement(childElement);
        assertEquals(1, xmlElement.count());
    }

    @Test
    public void testAddSubElement() {
        XmlElement subElement = xmlElement.addSubElement("parent.child1.child2");
        assertNotNull(subElement);
    }

    @Test
    public void testAddSubElementWithElement() {
        XmlElement subElement = new XmlElement("sub");
        assertNotNull(xmlElement.addSubElement(subElement));
    }

    @Test
    public void testAddSubElementWithNameAndData() {
        XmlElement subElement = xmlElement.addSubElement("child", "data");
        assertNotNull(subElement);
        assertEquals("data", subElement.getData());
    }

    @Test
    public void testClone() {
        XmlElement clonedElement = (XmlElement) xmlElement.clone();
        assertNotNull(clonedElement);
        assertNotSame(xmlElement, clonedElement);
    }

    @Test
    public void testSetAndGetParent() {
        XmlElement parentElement = new XmlElement("parent");
        xmlElement.setParent(parentElement);
        assertEquals(parentElement, xmlElement.getParent());
    }

    @Test
    public void testSetAndGetData() {
        xmlElement.setData("sample data");
        assertEquals("sample data", xmlElement.getData());
    }

    @Test
    public void testSetName() {
        xmlElement.setName("elementName");
        assertEquals("elementName", xmlElement.getName());
    }

    @Test
    public void testNotifyObservers() {
        xmlElement.notifyObservers();
        assertTrue(xmlElement.hasChanged());
    }

    @Test
    public void testEquals() {
        XmlElement otherElement = new XmlElement();
        otherElement.addAttribute("key1", "value1");
        otherElement.setData("data");
        otherElement.setName("name");
        otherElement.addElement(new XmlElement("child"));
        assertTrue(xmlElement.equals(otherElement));
    }

    @Test
    public void testHashCode() {
        assertNotNull(xmlElement.hashCode());
    }
}