import org.junit.*;
import static org.junit.Assert.*;
import java.util.*;

public class XmlElementTest {

    private XmlElement xmlElement;

    @Before
    public void setup() {
        xmlElement = new XmlElement("test_element");

        // Sample Attributes for testing
        xmlElement.addAttribute("attr1", "value1");
        xmlElement.addAttribute("attr2", null);
        xmlElement.addAttribute(null, "value3");
        xmlElement.addAttribute(null, null);

        // Sample Subelements for testing
        xmlElement.addElement(new XmlElement("sub1"));
        xmlElement.addElement(new XmlElement("sub1"));
        xmlElement.addElement(new XmlElement("sub3", "data"));

        // Observer for testing notifications
        xmlElement.addObserver(new Observer() {
            @Override
            public void update(Observable observable, Object arg) {
                System.out.println("Observable state has changed");
            }
        });
    }

    @Test
    public void testAddAttribute() {
        assertEquals("value1", xmlElement.getAttribute("attr1"));
        assertNull(xmlElement.getAttribute("attr2")); // Null attribute validation
        assertNull(xmlElement.getAttribute("attr3")); // Non-existent attribute validation
        assertNull(xmlElement.getAttribute(null)); // Null input validation
    }

    @Test
    public void testGetAttribute() {
        assertEquals("value1", xmlElement.getAttribute("attr1"));
        assertEquals("default_value", xmlElement.getAttribute("attr2", "default_value")); // Default value validation
        assertNull(xmlElement.getAttribute("attr3")); // Non-existent attribute validation
        assertNull(xmlElement.getAttribute(null)); // Null input validation
    }

    @Test
    public void testGetAttributes() {
        Hashtable<String, String> attributes = xmlElement.getAttributes();
        assertEquals(4, attributes.size());
        assertTrue(attributes.containsKey("attr1"));
        assertTrue(attributes.containsKey("attr2"));
        assertTrue(attributes.containsKey(null));
        assertFalse(attributes.containsKey("attr3")); // Non-existent attribute validation
    }

    @Test
    public void testSetAttributes() {
        Hashtable<String, String> attributes = new Hashtable<>();
        attributes.put("new_attr1", "new_value1");
        attributes.put("new_attr2", "new_value2");

        xmlElement.setAttributes(attributes);

        attributes = xmlElement.getAttributes();
        assertEquals(2, attributes.size());
        assertTrue(attributes.containsKey("new_attr1"));
        assertTrue(attributes.containsKey("new_attr2"));
        assertFalse(attributes.containsKey("attr1")); // Original attribute validation
    }

    @Test
    public void testGetAttributeNames() {
        Enumeration attributeNames = xmlElement.getAttributeNames();
        int count = 0;
        while (attributeNames.hasMoreElements()) {
            count++;
            attributeNames.nextElement();
        }
        assertEquals(4, count);
    }

    @Test
    public void testAddElement() {
        XmlElement subElement = new XmlElement("sub4");
        assertTrue(xmlElement.addElement(subElement));
        assertTrue(xmlElement.getElements().contains(subElement));
    }

    @Test
    public void testRemoveElement_Object() {
        XmlElement subElement = new XmlElement("sub1");
        assertNotNull(xmlElement.removeElement(subElement));
        assertFalse(xmlElement.getElements().contains(subElement));
    }

    @Test
    public void testRemoveElement_Index() {
        int index = 0;
        XmlElement subElement = xmlElement.getElement(index);
        assertNotNull(xmlElement.removeElement(index));
        assertFalse(xmlElement.getElements().contains(subElement));
    }

    @Test
    public void testRemoveAllElements() {
        xmlElement.removeAllElements();
        assertEquals(0, xmlElement.count());
    }

    @Test
    public void testRemoveFromParent() {
        XmlElement subElement = xmlElement.getElement(0);
        subElement.removeFromParent();
        assertFalse(xmlElement.getElements().contains(subElement));
    }

    @Test
    public void testAppend() {
        XmlElement subElement = new XmlElement("sub4");
        subElement.setParent(xmlElement);
        subElement.append(subElement);
        assertTrue(xmlElement.getElements().contains(subElement));
    }

    @Test
    public void testInsertElement() {
        XmlElement subElement = new XmlElement("sub4");
        int index = 1;
        xmlElement.insertElement(subElement, index);
        assertEquals(subElement, xmlElement.getElement(index));
    }

    @Test
    public void testGetElements() {
        List<XmlElement> subElements = xmlElement.getElements();
        assertEquals(3, subElements.size());
        assertTrue(subElements.contains(new XmlElement("sub1")));
        assertTrue(subElements.contains(new XmlElement("sub2")));
        assertTrue(subElements.contains(new XmlElement("sub3", "data")));
    }

    @Test
    public void testCount() {
        assertEquals(3, xmlElement.count());
    }

    @Test
    public void testGetElement_String() {
        XmlElement subElement = new XmlElement("sub1");
        assertEquals(subElement, xmlElement.getElement("sub1"));
        assertNull(xmlElement.getElement("sub4")); // Non-existent subelement validation
    }

    @Test
    public void testGetElement_int() {
        XmlElement subElement = new XmlElement("sub1");
        assertEquals(subElement, xmlElement.getElement(0));
    }

    @Test
    public void testAddSubElement_String() {
        XmlElement subElement = xmlElement.addSubElement("sub1");
        assertTrue(xmlElement.getElements().contains(subElement));
    }

    @Test
    public void testAddSubElement_XmlElement() {
        XmlElement subElement = new XmlElement("sub4");
        xmlElement.addSubElement(subElement);
        assertTrue(xmlElement.getElements().contains(subElement));
    }

    @Test
    public void testAddSubElement_String_String() {
        XmlElement subElement = xmlElement.addSubElement("sub4", "sub_data");
        assertTrue(xmlElement.getElements().contains(subElement));
    }

    @Test
    public void testSetParent() {
        XmlElement parent = new XmlElement("parent");
        xmlElement.setParent(parent);
        assertEquals(parent, xmlElement.getParent());
    }

    @Test
    public void testGetData() {
        xmlElement.setData("data");
        assertEquals("data", xmlElement.getData());
    }

    @Test
    public void testGetName() {
        assertEquals("test_element", xmlElement.getName());
    }

    @Test
    public void testPrintNode() {
        // Redirecting System.out to validate console output
        ByteArrayOutputStream outContent = new ByteArrayOutputStream();
        System.setOut(new PrintStream(outContent));

        XmlElement.printNode(xmlElement, "");
        String expectedOutput = "test_element\nattr1:value1\nattr2:null\nnull:null\nsub1\nsub1\nsub3 = 'data'\n";
        assertEquals(expectedOutput, outContent.toString());
    }

    @Test
    public void testClone() {
        XmlElement clonedElement = (XmlElement) xmlElement.clone();
        assertEquals(xmlElement, clonedElement); // Deep clone validation
        assertNotSame(xmlElement, clonedElement); // Object reference validation
    }

    @Test
    public void testSetName() {
        xmlElement.setName("new_test_element");
        assertEquals("new_test_element", xmlElement.getName());
    }

    @Test
    public void testEquals() {
        XmlElement equalElement = new XmlElement("test_element");

        equalElement.addAttribute("attr1", "value1");
        equalElement.addAttribute("attr2", null);
        equalElement.addElement(new XmlElement("sub1"));
        equalElement.addElement(new XmlElement("sub1"));
        equalElement.addElement(new XmlElement("sub3", "data"));

        assertTrue(xmlElement.equals(equalElement));
        assertFalse(xmlElement.equals(new XmlElement("different_test_element"))); // Name validation
        assertFalse(xmlElement.equals(null)); // Null input validation
        assertFalse(xmlElement.equals("not_a_XmlElement")); // Type validation
    }

    @Test
    public void testHashCode() {
        int expectedHashCode = 23 +
                xmlElement.getAttributes().hashCode() * 13 +
                xmlElement.getData().hashCode() * 17 +
                xmlElement.getName().hashCode() * 29 +
                xmlElement.getElements().hashCode() * 57;

        assertEquals(expectedHashCode, xmlElement.hashCode());
    }

    @Test
    public void testNotifyObservers() {
        // Redirecting System.out to validate console output
        ByteArrayOutputStream outContent = new ByteArrayOutputStream();
        System.setOut(new PrintStream(outContent));

        xmlElement.notifyObservers();
        assertEquals("Observable state has changed\n", outContent.toString());
    }
}