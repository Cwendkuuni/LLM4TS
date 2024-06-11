import org.junit.Before;
import org.junit.Test;
import static org.junit.Assert.*;

public class XmlElementTest {

    private XmlElement xmlElement;

    @Before
    public void setUp() {
        xmlElement = new XmlElement();
    }

    @Test
    public void testAddAttribute() {
        assertNull(xmlElement.addAttribute(null, null));
        assertNull(xmlElement.addAttribute(null, "value"));
        assertNull(xmlElement.addAttribute("name", null));

        Object returnValue = xmlElement.addAttribute("name", "value");
        assertNull(returnValue);

        returnValue = xmlElement.addAttribute("name", "new value");
        assertNotNull(returnValue);
        assertEquals("value", returnValue);
    }

    @Test
    public void testGetAttribute() {
        assertNull(xmlElement.getAttribute(null));
        assertNull(xmlElement.getAttribute("name"));

        xmlElement.addAttribute("name", "value");
        assertEquals("value", xmlElement.getAttribute("name"));
    }

    @Test
    public void testGetAttribute_withDefaultValue() {
        assertNull(xmlElement.getAttribute(null, "default"));
        assertEquals("default", xmlElement.getAttribute("name", "default"));

        xmlElement.addAttribute("name", "value");
        assertEquals("value", xmlElement.getAttribute("name", "default"));
    }

    @Test
    public void testGetAttributes() {
        assertNotNull(xmlElement.getAttributes());
        assertTrue(xmlElement.getAttributes().isEmpty());

        xmlElement.addAttribute("name", "value");
        assertFalse(xmlElement.getAttributes().isEmpty());
        assertEquals("value", xmlElement.getAttributes().get("name"));
    }

    @Test
    public void testSetAttributes() {
        Hashtable<String, String> attributes = new Hashtable<>();
        attributes.put("name", "value");

        xmlElement.setAttributes(attributes);
        assertEquals(attributes, xmlElement.getAttributes());
    }

    @Test
    public void testGetAttributeNames() {
        assertNotNull(xmlElement.getAttributeNames());
        assertFalse(xmlElement.getAttributeNames().hasMoreElements());

        xmlElement.addAttribute("name", "value");
        assertTrue(xmlElement.getAttributeNames().hasMoreElements());
        assertEquals("name", xmlElement.getAttributeNames().nextElement());
    }

    @Test
    public void testAddElement() {
        XmlElement subElement = new XmlElement("sub");

        assertTrue(xmlElement.addElement(subElement));
        assertEquals(1, xmlElement.count());
        assertEquals(subElement, xmlElement.getElement(0));

        XmlElement sameSubElement = new XmlElement("sub");
        assertFalse(xmlElement.addElement(sameSubElement));
    }
    
    // ...

    @Test
    public void testEquals() {
        XmlElement xmlElement1 = new XmlElement();
        assertTrue(xmlElement.equals(xmlElement1));

        XmlElement xmlElement2 = new XmlElement("name1");
        assertFalse(xmlElement.equals(xmlElement2));
    }

    @Test
    public void testHashCode() {
        int hashCode = 23;

        assertEquals(hashCode, xmlElement.hashCode());

        Hashtable<String, String> attributes = new Hashtable<>();
        attributes.put("name", "value");
        xmlElement.setAttributes(attributes);
        hashCode += attributes.hashCode() * 13;

        assertEquals(hashCode, xmlElement.hashCode());

        xmlElement.setData("data");
        hashCode += xmlElement.getData().hashCode() * 17;

        assertEquals(hashCode, xmlElement.hashCode());

        xmlElement.setName("name");
        hashCode += xmlElement.getName().hashCode() * 29;

        assertEquals(hashCode, xmlElement.hashCode());

        xmlElement.addElement(new XmlElement("sub"));
        hashCode += xmlElement.getElements().hashCode() * 57;

        assertEquals(hashCode, xmlElement.hashCode());
    }

}