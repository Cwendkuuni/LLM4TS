import static org.junit.Assert.*;
import org.junit.Test;
import java.util.Hashtable;

public class XmlElementTest {

    @Test
    public void testAddAttribute() {
        XmlElement element = new XmlElement();
        Object returnValue = element.addAttribute("key", "value");
        assertEquals("Value should be null", null, returnValue);
    }

    @Test
    public void testGetAttribute() {
        XmlElement element = new XmlElement();
        element.addAttribute("key", "value");
        String retrievedValue = element.getAttribute("key");
        assertEquals("Retrieved value should be 'value'", "value", retrievedValue);
    }

    @Test
    public void testRemoveElementByIndex() {
        XmlElement parent = new XmlElement("parent");
        XmlElement child = new XmlElement("child");
        parent.addElement(child);
        XmlElement removedElement = parent.removeElement(0);
        assertEquals("Removed element should be the same as the child", child, removedElement);
    }

    // Add additional test cases for other methods...

}