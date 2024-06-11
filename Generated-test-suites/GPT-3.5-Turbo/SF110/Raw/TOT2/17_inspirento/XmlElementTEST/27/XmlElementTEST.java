import static org.junit.Assert.*;
import org.junit.Test;

public class XmlElementTest {

    @Test
    public void testAddAttribute() {
        XmlElement element = new XmlElement();
        assertEquals(null, element.addAttribute("test", "value"));
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

    // Add additional test cases for each method

}