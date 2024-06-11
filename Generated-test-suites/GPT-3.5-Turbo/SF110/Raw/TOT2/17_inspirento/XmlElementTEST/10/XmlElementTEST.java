import org.junit.Test;
import static org.junit.Assert.*;
import java.util.Hashtable;
import java.util.List;
import java.util.Vector;

public class XmlElementTest {
    
    @Test
    public void testDefaultConstructor() {
        XmlElement element = new XmlElement();
        assertNotNull(element);
        assertTrue(element.getAttributes().isEmpty());
        assertTrue(element.getElements().isEmpty());
        assertNull(element.getData());
        assertNull(element.getParent());
    }
    
    @Test
    public void testParameterizedConstructor() {
        Hashtable<String, String> attributes = new Hashtable<>();
        attributes.put("key", "value");
        XmlElement element = new XmlElement("test", attributes);
        
        assertNotNull(element);
        assertEquals("test", element.getName());
        assertEquals(attributes, element.getAttributes());
        assertTrue(element.getElements().isEmpty());
        assertNull(element.getData());
        assertNull(element.getParent());
    }
    
    @Test
    public void testAddAttribute() {
        XmlElement element = new XmlElement();
        Object value = element.addAttribute("key", "value");
        
        assertEquals("value", element.getAttribute("key"));
        assertEquals("value", value);
    }
    
    // Add more test cases for other methods
    
}