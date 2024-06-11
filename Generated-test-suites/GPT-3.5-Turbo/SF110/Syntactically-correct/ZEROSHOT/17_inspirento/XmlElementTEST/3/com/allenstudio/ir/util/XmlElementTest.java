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
        Object returnValue = xmlElement.addAttribute("attributeName", "attributeValue");
        assertNull(returnValue);
    }

    @Test
    public void testGetAttribute() {
        xmlElement.addAttribute("attributeName", "attributeValue");
        String attributeValue = xmlElement.getAttribute("attributeName");
        assertEquals("attributeValue", attributeValue);
    }

    @Test
    public void testGetAttributeDefaultValue() {
        String defaultValue = "defaultValue";
        String attributeValue = xmlElement.getAttribute("attributeName", defaultValue);
        assertEquals(defaultValue, attributeValue);
    }

    @Test
    public void testGetAttributes() {
        Hashtable<String, String> attributes = xmlElement.getAttributes();
        assertNotNull(attributes);
    }

    @Test
    public void testSetAttributes() {
        Hashtable<String, String> attributes = new Hashtable<>();
        xmlElement.setAttributes(attributes);
        assertEquals(attributes, xmlElement.getAttributes());
    }

    @Test
    public void testGetAttributeNames() {
        xmlElement.addAttribute("attribute1", "value1");
        xmlElement.addAttribute("attribute2", "value2");
        xmlElement.addAttribute("attribute3", "value3");

        assertEquals(3, xmlElement.getAttributeNames().size());
    }

    @Test
    public void testAddElement() {
        XmlElement element = new XmlElement();
        assertTrue(xmlElement.addElement(element));
    }

    @Test
    public void testRemoveElementByObject() {
        XmlElement element = new XmlElement();
        xmlElement.addElement(element);

        assertEquals(element, xmlElement.removeElement(element));
    }

    @Test
    public void testRemoveElementByIndex() {
        XmlElement element = new XmlElement();
        xmlElement.addElement(element);

        assertEquals(element, xmlElement.removeElement(0));
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
        XmlElement parentElement = new XmlElement();
        XmlElement childElement = new XmlElement();

        parentElement.addElement(childElement);

        childElement.removeFromParent();

        assertNull(childElement.getParent());
    }

    @Test
    public void testAppend() {
        XmlElement parentElement = new XmlElement();
        XmlElement childElement = new XmlElement();

        childElement.removeFromParent();

        parentElement.append(childElement);

        assertEquals(parentElement, childElement.getParent());
    }

    @Test
    public void testInsertElement() {
        XmlElement parentElement = new XmlElement();
        XmlElement element1 = new XmlElement();
        XmlElement element2 = new XmlElement();
        XmlElement element3 = new XmlElement();

        element1.removeFromParent();
        element2.removeFromParent();
        element3.removeFromParent();

        parentElement.insertElement(element1, 0);
        parentElement.insertElement(element2, 1);
        parentElement.insertElement(element3, 2);

        assertEquals(parentElement, element1.getParent());
        assertEquals(parentElement, element2.getParent());
        assertEquals(parentElement, element3.getParent());
    }

    @Test
    public void testGetElements() {
        assertEquals(Vector.class, xmlElement.getElements().getClass());
    }

    @Test
    public void testCount() {
        xmlElement.addElement(new XmlElement());
        xmlElement.addElement(new XmlElement());
        xmlElement.addElement(new XmlElement());

        assertEquals(3, xmlElement.count());
    }

    @Test
    public void testGetElement() {
        XmlElement element1 = new XmlElement("element1");
        element1.addElement(new XmlElement("subElement1"));
        element1.addElement(new XmlElement("subElement2"));

        xmlElement.addElement(element1);

        assertNotNull(xmlElement.getElement("element1"));
    }

    @Test
    public void testGetElementByIndex() {
        XmlElement element1 = new XmlElement("element1");

        xmlElement.addElement(element1);

        assertEquals(element1, xmlElement.getElement(0));
    }

    @Test
    public void testAddSubElementByPath() {
        XmlElement subElement = xmlElement.addSubElement("element1.subElement1.subElement2");

        assertNotNull(subElement);
        assertNotNull(xmlElement.getElement("element1.subElement1.subElement2"));
    }

    @Test
    public void testAddSubElement() {
        XmlElement subElement = new XmlElement("subElement");

        xmlElement.addSubElement(subElement);

        assertEquals(xmlElement, subElement.getParent());
    }

    @Test
    public void testAddSubElementWithNameAndData() {
        XmlElement subElement = xmlElement.addSubElement("subElement", "data");

        assertEquals("data", subElement.getData());
    }

    @Test
    public void testSetParent() {
        XmlElement parentElement = new XmlElement();
        XmlElement childElement = new XmlElement();

        childElement.setParent(parentElement);

        assertEquals(parentElement, childElement.getParent());
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
    public void testPrintNode() {
        XmlElement node = new XmlElement("node");
        node.setData("data");

        XmlElement subElement1 = new XmlElement("subElement1");
        subElement1.addAttribute("attribute1", "value1");
        subElement1.setData("subData1");
        node.addElement(subElement1);

        XmlElement subElement2 = new XmlElement("subElement2");
        subElement2.addAttribute("attribute2", "value2");
        subElement2.addData("subData2.1");
        subElement2.addData("subData2.2");
        node.addElement(subElement2);

        String expected = "node = 'data'\n" +
                "subElement1 = 'subData1'\n" +
                "attribute1:value1\n" +
                "subElement2 = 'subData2.1'\n" +
                "subElement2 = 'subData2.2'\n" +
                "attribute2:value2\n";
        assertEquals(expected, node.printNode(node, ""));

    }

    @Test
    public void testClone() {
        xmlElement.setName("name");
        xmlElement.addAttribute("attribute1", "value1");
        xmlElement.addData("data1");
        xmlElement.addElement(new XmlElement());

        XmlElement clone = (XmlElement) xmlElement.clone();

        assertEquals(xmlElement, clone);
    }

    @Test
    public void testNotifyObservers() {
        xmlElement.addObserver(observer);

        xmlElement.notifyObservers();

        //TODO: Verify observer has been notified
    }

    private final Observer observer = new Observer() {
        @Override
        public void update(Observable o, Object arg) {
            System.out.println("Observer notified");
        }
    };

    private boolean equals(Object obj1, Object obj2) {
        if (obj1 == null && obj2 == null) {
            return true;
        } else if (obj1 != null && obj2 != null) {
            return obj1.equals(obj2);
        }
        return false;
    }

    @Test
    public void testEquals() {
        XmlElement xmlElement1 = new XmlElement();
        xmlElement1.setName("name");
        xmlElement1.addAttribute("attribute1", "value1");
        xmlElement1.addData("data1");
        xmlElement1.addElement(new XmlElement());

        XmlElement xmlElement2 = new XmlElement();
        xmlElement2.setName("name");
        xmlElement2.addAttribute("attribute1", "value1");
        xmlElement2.addData("data1");
        xmlElement2.addElement(new XmlElement());

        assertTrue(xmlElement1.equals(xmlElement2));
    }

    @Test
    public void testHashCode() {
        XmlElement xmlElement1 = new XmlElement();
        xmlElement1.setName("name");
        xmlElement1.addAttribute("attribute1", "value1");
        xmlElement1.addData("data1");
        xmlElement1.addElement(new XmlElement());

        XmlElement xmlElement2 = new XmlElement();
        xmlElement2.setName("name");
        xmlElement2.addAttribute("attribute1", "value1");
        xmlElement2.addData("data1");
        xmlElement2.addElement(new XmlElement());

        assertEquals(xmlElement1.hashCode(), xmlElement2.hashCode());
    }
}