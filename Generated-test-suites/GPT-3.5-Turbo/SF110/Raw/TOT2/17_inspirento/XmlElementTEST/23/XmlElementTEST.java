import org.junit.Test;
import static org.junit.Assert.*;
import java.util.Hashtable;
import java.util.Enumeration;

public class XmlElementTest {

    @Test
    public void testAddAttribute() {
        XmlElement element = new XmlElement();
        assertNull(element.addAttribute("test", "value"));
        assertEquals("value", element.addAttribute("test", "new value"));
    }

    @Test
    public void testGetAttribute() {
        XmlElement element = new XmlElement();
        element.addAttribute("test", "value");
        assertEquals("value", element.getAttribute("test"));
    }

    @Test
    public void testGetAttributeDefault() {
        XmlElement element = new XmlElement();
        assertEquals("default", element.getAttribute("test", "default"));
    }

    // Add more test cases for other methods...

    // Edge cases and error scenarios testing

    @Test
    public void testAddElement() {
        XmlElement parent = new XmlElement();
        XmlElement child = new XmlElement("child");
        assertTrue(parent.addElement(child));
    }

    @Test
    public void testRemoveElementByObject() {
        XmlElement parent = new XmlElement();
        XmlElement child = new XmlElement("child");
        parent.addElement(child);
        assertEquals(child, parent.removeElement(child));
    }

    // More edge cases and error scenarios testing...

}