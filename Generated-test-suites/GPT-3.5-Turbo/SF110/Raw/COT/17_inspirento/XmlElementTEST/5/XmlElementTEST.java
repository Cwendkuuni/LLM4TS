import static org.junit.Assert.*;
import org.junit.Test;

public class XmlElementTest {
    
    @Test
    public void testDefaultConstructor() {
        XmlElement element = new XmlElement();
        assertNotNull(element);
        assertEquals(0, element.getAttributes().size());
        assertEquals(0, element.count());
        assertNull(element.getParent());
    }
    
    @Test
    public void testConstructorWithName() {
        XmlElement element = new XmlElement("element");
        assertNotNull(element);
        assertEquals("element", element.getName());
        assertEquals(0, element.getAttributes().size());
        assertEquals(0, element.count());
        assertNull(element.getParent());
    }
    
    @Test
    public void testConstructorWithNameAndAttributes() {
        Hashtable<String, String> attributes = new Hashtable<>();
        attributes.put("attr1", "value1");
        attributes.put("attr2", "value2");
        XmlElement element = new XmlElement("element", attributes);
        assertNotNull(element);
        assertEquals("element", element.getName());
        assertEquals(attributes, element.getAttributes());
        assertEquals(0, element.count());
        assertNull(element.getParent());
    }
    
    // and so on for all constructors
    
    @Test
    public void testAddAttribute() {
        XmlElement element = new XmlElement();
        Object returnValue = element.addAttribute("attr1", "value1");
        assertNull(returnValue);
        assertEquals("value1", element.getAttribute("attr1"));
    }
    
    @Test
    public void testGetAttribute() {
        XmlElement element = new XmlElement();
        element.addAttribute("attr1", "value1");
        assertEquals("value1", element.getAttribute("attr1"));
    }
    
    // and so on for all public methods
    
    @Test
    public void testEquals() {
        XmlElement element1 = new XmlElement("element");
        XmlElement element2 = new XmlElement("element");
        assertTrue(element1.equals(element2));
    }
    
    @Test
    public void testHashCode() {
        XmlElement element = new XmlElement("element");
        int expectedHashCode = 23 + "element".hashCode() * 29;
        assertEquals(expectedHashCode, element.hashCode());
    }
    
    @Test
    public void testClone() {
        XmlElement element1 = new XmlElement("element");
        XmlElement element2 = (XmlElement)element1.clone();
        assertEquals(element1, element2);
    }
}