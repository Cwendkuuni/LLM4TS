import org.junit.Before;
import org.junit.Test;
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
        Object result = xmlElement.addAttribute("name", "value");
        assertNull(result);
        result = xmlElement.addAttribute("name", null);
        assertNull(result);
    }

    @Test
    public void testGetAttribute() {
        assertNull(xmlElement.getAttribute("name"));
    }

    @Test
    public void testGetAttributeWithDefaultValue() {
        String defaultValue = "default";
        assertEquals(defaultValue, xmlElement.getAttribute("name", defaultValue));
    }

    @Test
    public void testGetAttributes() {
        Hashtable<String, String> attributes = new Hashtable<>();
        assertEquals(attributes, xmlElement.getAttributes());
    }

    @Test
    public void testSetAttributes() {
        Hashtable<String, String> attributes = new Hashtable<>();
        xmlElement.setAttributes(attributes);
        assertEquals(attributes, xmlElement.getAttributes());
    }

    @Test
    public void testGetAttributeNames() {
        assertNotNull(xmlElement.getAttributeNames());
    }

    @Test
    public void testAddElement() {
        XmlElement subElement = new XmlElement();
        assertTrue(xmlElement.addElement(subElement));
    }

    @Test
    public void testRemoveElementByObject() {
        XmlElement subElement = new XmlElement();
        xmlElement.addElement(subElement);
        XmlElement removedElement = xmlElement.removeElement(subElement);
        assertEquals(subElement, removedElement);
    }

    @Test
    public void testRemoveElementByIndex() {
        XmlElement subElement = new XmlElement();
        xmlElement.addElement(subElement);
        XmlElement removedElement = xmlElement.removeElement(0);
        assertEquals(subElement, removedElement);
    }

    @Test
    public void testRemoveAllElements() {
        xmlElement.addElement(new XmlElement());
        xmlElement.addElement(new XmlElement());
        xmlElement.addElement(new XmlElement());
        xmlElement.removeAllElements();
        assertEquals(0, xmlElement.count());
    }

    @Test
    public void testRemoveFromParent() {
        XmlElement parent = new XmlElement();
        XmlElement subElement = new XmlElement();
        parent.addElement(subElement);
        subElement.removeFromParent();
        assertNull(subElement.getParent());
    }

    @Test
    public void testAppend() {
        XmlElement subElement = new XmlElement();
        subElement.setData("data");
        xmlElement.append(subElement);
        assertEquals(subElement, xmlElement.getElement(0));
    }

    @Test
    public void testInsertElement() {
        XmlElement subElement = new XmlElement();
        xmlElement.addElement(new XmlElement());
        xmlElement.addElement(new XmlElement());
        xmlElement.insertElement(subElement, 1);
        assertEquals(subElement, xmlElement.getElement(1));
    }

    @Test
    public void testGetElements() {
        List<XmlElement> elements = new Vector<>();
        assertEquals(elements, xmlElement.getElements());
    }

    @Test
    public void testCount() {
        assertEquals(0, xmlElement.count());
    }

    @Test
    public void testGetElementWithPath() {
        XmlElement subElement = new XmlElement();
        subElement.setName("name");
        xmlElement.addElement(subElement);
        XmlElement result = xmlElement.getElement("name");
        assertEquals(subElement, result);
    }

    @Test
    public void testGetElementWithIndex() {
        XmlElement subElement = new XmlElement();
        xmlElement.addElement(subElement);
        XmlElement result = xmlElement.getElement(0);
        assertEquals(subElement, result);
    }

    @Test
    public void testAddSubElementWithPath() {
        XmlElement subElement = xmlElement.addSubElement("path.name");
        assertNotNull(subElement);
    }

    @Test
    public void testAddSubElementWithXmlElement() {
        XmlElement element = new XmlElement();
        XmlElement subElement = xmlElement.addSubElement(element);
        assertNotNull(subElement);
        assertEquals(xmlElement, subElement.getParent());
    }

    @Test
    public void testAddSubElementWithNameAndData() {
        String name = "name";
        String data = "data";
        XmlElement subElement = xmlElement.addSubElement(name, data);
        assertNotNull(subElement);
        assertEquals(xmlElement, subElement.getParent());
        assertEquals(data, subElement.getData());
    }

    @Test
    public void testSetParent() {
        XmlElement parent = new XmlElement();
        xmlElement.setParent(parent);
        assertEquals(parent, xmlElement.getParent());
    }

    @Test
    public void testGetData() {
        assertNull(xmlElement.getData());
    }

    @Test
    public void testGetDataWithName() {
        String data = "data";
        xmlElement.setData(data);
        assertEquals(data, xmlElement.getData());
    }

    @Test
    public void testGetName() {
        assertNull(xmlElement.getName());
    }

    @Test
    public void testPrintNode() {
        // Unable to test printNode method as it directly prints to console
    }

    @Test
    public void testClone() {
        XmlElement clone = (XmlElement) xmlElement.clone();
        assertEquals(xmlElement, clone);
        assertNotSame(xmlElement, clone);
    }

    @Test
    public void testSetName() {
        String name = "name";
        xmlElement.setName(name);
        assertEquals(name, xmlElement.getName());
    }
    
    @Test
    public void testNotifyObservers() {
        xmlElement.notifyObservers(); // Unable to assert notifyObservers method as it is void
        assertTrue(xmlElement.hasChanged());
    }

    @Test
    public void testEquals() {
        XmlElement xmlElement1 = new XmlElement();
        XmlElement xmlElement2 = new XmlElement();
        assertTrue(xmlElement1.equals(xmlElement2));
    }

    @Test
    public void testHashCode() {
        XmlElement xmlElement1 = new XmlElement();
        XmlElement xmlElement2 = new XmlElement();
        assertEquals(xmlElement1.hashCode(), xmlElement2.hashCode());
    }
}