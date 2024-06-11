import static org.junit.Assert.*;
import org.junit.Test;
import java.util.Hashtable;
import java.util.Enumeration;

public class XmlElementTest {
    
    @Test
    public void testAddAttribute() {
        XmlElement element = new XmlElement();
        Object returnValue = element.addAttribute("key", "value");
        assertEquals("Adding attribute should return null on first addition", null, returnValue);
        
        returnValue = element.addAttribute("key", "newValue");
        assertEquals("Adding attribute should return previous value on overwrite", "value", returnValue);
    }
    
    @Test
    public void testGetAttribute() {
        XmlElement element = new XmlElement();
        element.addAttribute("key", "value");
        assertEquals("Getting attribute should return correct value", "value", element.getAttribute("key"));
    }
    
    @Test
    public void testGetAttributeDefault() {
        XmlElement element = new XmlElement();
        String defaultValue = "defaultValue";
        assertEquals("Getting attribute with default value should return default value when attribute is not found", defaultValue, element.getAttribute("nonExistentKey", defaultValue));
    }

    // Add more test cases for other methods
    
}