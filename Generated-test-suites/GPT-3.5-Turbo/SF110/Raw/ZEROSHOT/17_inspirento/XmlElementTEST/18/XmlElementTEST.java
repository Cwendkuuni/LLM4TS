import org.junit.Before;
import org.junit.Test;
import static org.junit.Assert.*;

public class XmlElementTest {

    private XmlElement element;

    @Before
    public void setUp() {
        element = new XmlElement();
    }

    @Test
    public void testAddAttribute() {
        assertNull(element.addAttribute("name", "value"));
        assertEquals("value", element.getAttribute("name"));
    }

    @Test
    public void testGetAttribute() {
        element.addAttribute("name", "value");
        assertEquals("value", element.getAttribute("name"));
    }

    @Test
    public void testGetAttributeWithDefaultValue() {
        assertEquals("default", element.getAttribute("name", "default"));
        element.addAttribute("name", "value");
        assertEquals("value", element.getAttribute("name", "default"));
    }

    @Test
    public void testGetAttributes() {
        assertNotNull(element.getAttributes());
    }

    @Test
    public void testSetAttributes() {
        element.setAttributes(new Hashtable<String, String>());
        assertNotNull(element.getAttributes());
    }

    @Test
    public void testGetAttributeNames() {
        assertNotNull(element.getAttributeNames());
    }

    @Test
    public void testAddElement() {
        XmlElement subElement = new XmlElement();
        assertTrue(element.addElement(subElement));
        assertEquals(subElement, element.getElement(0));
    }

    @Test
    public void testRemoveElementByObject() {
        XmlElement subElement = new XmlElement();
        element.addElement(subElement);
        assertEquals(subElement, element.removeElement(subElement));
    }

    @Test
    public void testRemoveElementByIndex() {
        XmlElement subElement = new XmlElement();
        element.addElement(subElement);
        assertEquals(subElement, element.removeElement(0));
    }

    @Test
    public void testRemoveAllElements() {
        element.addElement(new XmlElement());
        element.addElement(new XmlElement());
        element.addElement(new XmlElement());
        element.removeAllElements();
        assertEquals(0, element.count());
    }

    @Test
    public void testRemoveFromParent() {
        XmlElement parent = new XmlElement();
        XmlElement subElement = new XmlElement();
        parent.addElement(subElement);
        subElement.removeFromParent();
        assertEquals(0, parent.count());
        assertNull(subElement.getParent());
    }

    @Test
    public void testAppend() {
        XmlElement parent = new XmlElement();
        XmlElement subElement = new XmlElement();
        subElement.setName("subElement");
        parent.append(subElement);
        assertEquals(subElement, parent.getElement("subElement"));
    }

    @Test
    public void testInsertElement() {
        XmlElement parent = new XmlElement();
        XmlElement subElement = new XmlElement();
        subElement.setName("subElement");
        parent.insertElement(subElement, 0);
        assertEquals(subElement, parent.getElement("subElement"));
    }

    @Test
    public void testGetElements() {
        assertNotNull(element.getElements());
    }

    @Test
    public void testCount() {
        assertEquals(0, element.count());
        element.addElement(new XmlElement());
        assertEquals(1, element.count());
    }

    @Test
    public void testGetElementWithPath() {
        XmlElement subElement = new XmlElement();
        subElement.setName("subElement");
        element.addElement(subElement);
        assertEquals(subElement, element.getElement("subElement"));
    }

    @Test
    public void testGetElementWithIndex() {
        XmlElement subElement = new XmlElement();
        element.addElement(subElement);
        assertEquals(subElement, element.getElement(0));
    }

    @Test
    public void testAddSubElementWithPath() {
        XmlElement subElement = element.addSubElement("path.to.subElement");
        assertNotNull(subElement);
        assertEquals(subElement, element.getElement("path.to.subElement"));
    }

    @Test
    public void testAddSubElementWithXmlElement() {
        XmlElement subElement = new XmlElement();
        assertEquals(subElement, element.addSubElement(subElement));
        assertEquals(subElement, element.getElement(0));
    }

    @Test
    public void testAddSubElementWithNameAndData() {
        XmlElement subElement = element.addSubElement("name", "data");
        assertNotNull(subElement);
        assertEquals(subElement, element.getElement(0));
        assertEquals("data", subElement.getData());
    }

    @Test
    public void testSetParent() {
        XmlElement parent = new XmlElement();
        element.setParent(parent);
        assertEquals(parent, element.getParent());
    }

    @Test
    public void testGetData() {
        element.setData("data");
        assertEquals("data", element.getData());
    }

    @Test
    public void testGetName() {
        element.setName("name");
        assertEquals("name", element.getName());
    }

    @Test
    public void testPrintNode() {
        XmlElement node = new XmlElement();
        node.setName("node");
        node.setData("data");
        node.addAttribute("attribute", "value");
        XmlElement subElement = new XmlElement();
        subElement.setName("subElement");
        subElement.setData("subData");
        node.addElement(subElement);
        XmlElement.printNode(node, "");
        // Verify the printed output manually
    }

    @Test
    public void testClone() {
        XmlElement clone = (XmlElement) element.clone();
        assertEquals(element, clone);
        assertNotSame(element, clone);
        assertNotSame(element.getAttributes(), clone.getAttributes());
        assertNotSame(element.getElements(), clone.getElements());
    }

    @Test
    public void testEquals() {
        XmlElement other = new XmlElement();
        assertFalse(element.equals(null));
        assertFalse(element.equals(new Object()));
        assertFalse(element.equals(other));
        element.setAttributes(new Hashtable<String, String>());
        other.setAttributes(new Hashtable<String, String>());
        assertFalse(element.equals(other));
        element.setData("data");
        other.setData("data");
        assertFalse(element.equals(other));
        element.setName("name");
        other.setName("name");
        assertFalse(element.equals(other));
        element.addElement(new XmlElement());
        other.addElement(new XmlElement());
        assertFalse(element.equals(other));
        other.removeElement(0);
        assertFalse(element.equals(other));
        element.removeElement(0);
        assertTrue(element.equals(other));
    }

    @Test
    public void testHashCode() {
        XmlElement other = new XmlElement();
        assertEquals(element.hashCode(), other.hashCode());
        element.setAttributes(new Hashtable<String, String>());
        other.setAttributes(new Hashtable<String, String>());
        assertEquals(element.hashCode(), other.hashCode());
        element.setData("data");
        other.setData("data");
        assertEquals(element.hashCode(), other.hashCode());
        element.setName("name");
        other.setName("name");
        assertEquals(element.hashCode(), other.hashCode());
        element.addElement(new XmlElement());
        other.addElement(new XmlElement());
        assertEquals(element.hashCode(), other.hashCode());
    }

}