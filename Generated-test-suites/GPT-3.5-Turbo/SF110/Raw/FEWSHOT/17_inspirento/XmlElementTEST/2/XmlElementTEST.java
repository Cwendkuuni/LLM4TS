import static org.junit.Assert.*;
import org.junit.Test;

public class XmlElementTest {
    @Test
    public void testAddAttribute() {
        XmlElement element = new XmlElement("element");
        element.addAttribute("key1", "value1");
        
        assertEquals("value1", element.getAttribute("key1"));
    }
    
    @Test
    public void testAddAttribute_nullValues() {
        XmlElement element = new XmlElement("element");
        element.addAttribute(null, "value1");
        element.addAttribute("key1", null);
        
        assertNull(element.getAttribute(null));
        assertNull(element.getAttribute("key1", "defaultValue"));
    }
    
    @Test
    public void testGetAttribute() {
        XmlElement element = new XmlElement("element");
        element.addAttribute("key1", "value1");
        
        assertEquals("value1", element.getAttribute("key1"));
    }
    
    // Add test cases for other methods in XmlElement class
    
}