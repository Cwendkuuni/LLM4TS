import static org.junit.Assert.*;
import org.junit.Before;
import org.junit.Test;

public class XmlElementTest {
    
    private XmlElement xmlElement;
    
    @Before
    public void setUp() {
        xmlElement = new XmlElement();
    }
    
    @Test
    public void testAddAttribute() {
        assertNull(xmlElement.addAttribute(null, null));
        assertNull(xmlElement.addAttribute("key", "value"));
    }
    
    @Test
    public void testGetAttribute() {
        assertNull(xmlElement.getAttribute("key"));
        assertEquals("default", xmlElement.getAttribute("key", "default"));
    }
    
    @Test
    public void testSetAttributes() {
        xmlElement.setAttributes(new Hashtable<String, String>());
        assertNotNull(xmlElement.getAttributes());
    }
    
    @Test
    public void testAddElement() {
        XmlElement e = new XmlElement("test");
        assertTrue(xmlElement.addElement(e));
    }
    
    @Test
    public void testRemoveElement() {
        XmlElement e = new XmlElement("test");
        xmlElement.addElement(e);
        assertEquals(e, xmlElement.removeElement(e));
    }
    
    @Test
    public void testRemoveElementAtIndex() {
        XmlElement e = new XmlElement("test");
        xmlElement.addElement(e);
        assertEquals(e, xmlElement.removeElement(0));
    }
    
    @Test
    public void testRemoveAllElements() {
        XmlElement e = new XmlElement("test");
        xmlElement.addElement(e);
        xmlElement.removeAllElements();
        assertEquals(0, xmlElement.count());
    }
    
    @Test
    public void testRemoveFromParent() {
        XmlElement parent = new XmlElement("parent");
        XmlElement child = new XmlElement("child");
        parent.addElement(child);
        child.removeFromParent();
        assertNull(child.getParent());
    }
    
    @Test
    public void testAppend() {
        XmlElement parent = new XmlElement("parent");
        XmlElement child = new XmlElement("child");
        child.removeFromParent();
        parent.append(child);
        assertEquals(parent, child.getParent());
    }
    
    @Test
    public void testInsertElement() {
        XmlElement e1 = new XmlElement("test1");
        XmlElement e2 = new XmlElement("test2");
        xmlElement.addElement(e1);
        xmlElement.insertElement(e2, 0);
        assertEquals(e2, xmlElement.getElement(0));
    }
    
    @Test
    public void testGetElement() {
        XmlElement e1 = new XmlElement("test1");
        XmlElement e2 = new XmlElement("test2");
        xmlElement.addElement(e1);
        xmlElement.addElement(e2);
        assertEquals(e1, xmlElement.getElement(0));
        assertEquals(e2, xmlElement.getElement("test2"));
    }
    
    @Test
    public void testAddSubElement() {
        XmlElement subElement = xmlElement.addSubElement("test.sub.element");
        assertEquals(subElement, xmlElement.getElement("test").getElement("sub").getElement("element"));
    }
    
    @Test
    public void testAddSubElementWithData() {
        XmlElement subElement = xmlElement.addSubElement("test", "data");
        assertEquals("data", subElement.getData());
    }
    
    @Test
    public void testSetParent() {
        XmlElement parent = new XmlElement("parent");
        xmlElement.setParent(parent);
        assertEquals(parent, xmlElement.getParent());
    }
    
    @Test
    public void testDataOperations() {
        xmlElement.setName("test");
        xmlElement.setData("data");
        assertEquals("test", xmlElement.getName());
        assertEquals("data", xmlElement.getData());
    }
    
    // Add more test cases for Object clone(), equals(), hashCode(), notifyObservers(), and other methods as needed
    
}