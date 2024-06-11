import static org.junit.Assert.*;
import org.junit.Before;
import org.junit.Test;
import java.util.Hashtable;
import java.util.Enumeration;

public class XmlElementTest {

    private XmlElement testElement;

    @Before
    public void setUp() {
        testElement = new XmlElement("TestElement");
    }

    @Test
    public void testAddAttribute() {
        Object returnValue = testElement.addAttribute("key", "value");
        assertNotNull(returnValue);
    }

    @Test
    public void testGetAttribute() {
        testElement.addAttribute("key", "value");
        assertEquals("value", testElement.getAttribute("key"));
    }

    @Test
    public void testGetAttributeWithDefaultValue() {
        assertEquals("default", testElement.getAttribute("nonExistent", "default"));
    }

    @Test
    public void testGetAttributes() {
        testElement.addAttribute("key", "value");
        Hashtable<String, String> attributes = testElement.getAttributes();
        assertNotNull(attributes);
        assertEquals("value", attributes.get("key"));
    }

    @Test
    public void testSetAttributes() {
        Hashtable<String, String> attributes = new Hashtable<>();
        attributes.put("key", "value");
        testElement.setAttributes(attributes);
        assertEquals(attributes, testElement.getAttributes());
    }

    @Test
    public void testAddElement() {
        XmlElement element = new XmlElement("SubElement");
        assertTrue(testElement.addElement(element));
    }

    @Test
    public void testRemoveElement() {
        XmlElement element = new XmlElement("SubElement");
        testElement.addElement(element);
        assertEquals(element, testElement.removeElement(element));
    }

    // More test cases for other methods can be added

}