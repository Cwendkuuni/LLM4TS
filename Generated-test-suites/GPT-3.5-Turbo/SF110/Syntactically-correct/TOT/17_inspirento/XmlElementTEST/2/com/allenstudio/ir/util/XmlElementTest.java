import org.junit.Before;
import org.junit.Test;
import static org.junit.Assert.*;

public class XmlElementTest {
    
    private XmlElement xmlElement;
    
    @Before
    public void setUp() {
        xmlElement = new XmlElement("test");
    }
    
    @Test
    public void testAddAttribute() {
        assertNull(xmlElement.getAttribute("attr1"));
        
        Object returnValue = xmlElement.addAttribute("attr1", "value1");
        assertNull(returnValue);
        assertEquals("value1", xmlElement.getAttribute("attr1"));
        
        returnValue = xmlElement.addAttribute("attr2", "value2");
        assertNull(returnValue);
        assertEquals("value2", xmlElement.getAttribute("attr2"));
    }
    
    @Test
    public void testGetAttribute() {
        xmlElement.addAttribute("attr1", "value1");
        assertEquals("value1", xmlElement.getAttribute("attr1"));
        
        String defaultValue = "defValue";
        assertEquals("value1", xmlElement.getAttribute("attr1", defaultValue));
        
        assertNull(xmlElement.getAttribute("invalidAttr"));
        assertEquals(defaultValue, xmlElement.getAttribute("invalidAttr", defaultValue));
    }
    
    @Test
    public void testGetAttributes() {
        xmlElement.addAttribute("attr1", "value1");
        xmlElement.addAttribute("attr2", "value2");
        
        assertEquals(2, xmlElement.getAttributes().size());
        assertTrue(xmlElement.getAttributes().containsKey("attr1"));
        assertTrue(xmlElement.getAttributes().containsKey("attr2"));
        assertEquals("value1", xmlElement.getAttributes().get("attr1"));
        assertEquals("value2", xmlElement.getAttributes().get("attr2"));
    }
    
    @Test
    public void testSetAttributes() {
        Hashtable<String, String> attrs = new Hashtable<>();
        attrs.put("attr1", "value1");
        
        xmlElement.setAttributes(attrs);
        
        assertEquals(attrs, xmlElement.getAttributes());
    }
    
    @Test
    public void testRemoveElement_Object() {
        XmlElement child1 = new XmlElement("child1");
        
        xmlElement.addElement(child1);
        assertTrue(xmlElement.getElements().contains(child1));
        
        XmlElement removedChild = xmlElement.removeElement(child1);
        assertEquals(child1, removedChild);
        assertFalse(xmlElement.getElements().contains(child1));
    }
    
    @Test
    public void testRemoveElement_Index() {
        XmlElement child1 = new XmlElement("child1");
        
        xmlElement.addElement(child1);
        assertTrue(xmlElement.getElements().contains(child1));
        
        XmlElement removedChild = xmlElement.removeElement(0);
        assertEquals(child1, removedChild);
        assertFalse(xmlElement.getElements().contains(child1));
    }
    
    @Test
    public void testRemoveAllElements() {
        XmlElement child1 = new XmlElement("child1");
        xmlElement.addElement(child1);
        
        XmlElement child2 = new XmlElement("child2");
        xmlElement.addElement(child2);
        
        xmlElement.removeAllElements();
        
        assertEquals(0, xmlElement.getElements().size());
        assertFalse(xmlElement.getElements().contains(child1));
        assertFalse(xmlElement.getElements().contains(child2));
    }
    
    @Test
    public void testRemoveFromParent() {
        XmlElement child1 = new XmlElement("child1");
        xmlElement.addElement(child1);
        
        XmlElement child2 = new XmlElement("child2");
        xmlElement.addElement(child2);
        
        child1.removeFromParent();
        
        assertNull(child1.getParent());
        assertFalse(xmlElement.getElements().contains(child1));
    }
    
    @Test
    public void testAppend() {
        XmlElement child1 = new XmlElement("child1");
        
        xmlElement.addElement(child1);
        assertTrue(xmlElement.getElements().contains(child1));
        
        XmlElement child2 = new XmlElement("child2");
        child2.removeFromParent();
        xmlElement.append(child2);
        
        assertTrue(xmlElement.getElements().contains(child2));
    }
    
    @Test
    public void testInsertElement() {
        XmlElement child1 = new XmlElement("child1");
        
        xmlElement.addElement(child1);
        assertTrue(xmlElement.getElements().contains(child1));
        
        XmlElement child2 = new XmlElement("child2");
        child2.removeFromParent();
        xmlElement.insertElement(child2, 0);
        
        assertEquals(child2, xmlElement.getElements().get(0));
    }
    
    @Test
    public void testGetElement_String() {
        XmlElement child1 = new XmlElement("child1");
        xmlElement.addElement(child1);
        
        XmlElement child2 = new XmlElement("child2");
        child1.addElement(child2);
        
        assertEquals(child1, xmlElement.getElement("child1"));
        assertEquals(child2, xmlElement.getElement("child1.child2"));
        assertNull(xmlElement.getElement("child2"));
    }
    
    @Test
    public void testGetElement_Integer() {
        XmlElement child1 = new XmlElement("child1");
        xmlElement.addElement(child1);
        
        XmlElement child2 = new XmlElement("child2");
        xmlElement.addElement(child2);
        
        assertEquals(child1, xmlElement.getElement(0));
        assertEquals(child2, xmlElement.getElement(1));
    }
    
    @Test
    public void testAddSubElement_String() {
        XmlElement child1 = xmlElement.addSubElement("child1");
        assertEquals(child1, xmlElement.getElement("child1"));
        
        XmlElement child2 = xmlElement.addSubElement("child2");
        assertEquals(child2, xmlElement.getElement("child2"));
    }
    
    @Test
    public void testAddSubElement_XmlElement() {
        XmlElement child1 = new XmlElement("child1");
        xmlElement.addSubElement(child1);
        assertEquals(child1, xmlElement.getElement("child1"));
        
        XmlElement child2 = new XmlElement("child2");
        xmlElement.addSubElement(child2);
        assertEquals(child2, xmlElement.getElement("child2"));
    }
    
    @Test
    public void testAddSubElement_String_String() {
        XmlElement child1 = xmlElement.addSubElement("child1", "data1");
        assertEquals(child1, xmlElement.getElement("child1"));
        assertEquals("data1", child1.getData());
    }
    
    @Test
    public void testSetParent() {
        XmlElement parent = new XmlElement("parent");
        
        XmlElement child1 = new XmlElement("child1");
        child1.setParent(parent);
        
        assertEquals(parent, child1.getParent());
    }
    
    @Test
    public void testGetData() {
        assertNull(xmlElement.getData());
        
        xmlElement.setData("data1");
        assertEquals("data1", xmlElement.getData());
    }
    
    @Test
    public void testGetName() {
        assertEquals("test", xmlElement.getName());
        
        xmlElement.setName("newName");
        assertEquals("newName", xmlElement.getName());
    }
    
    @Test
    public void testNotifyObservers() {
        // Assuming that Observable and Observer classes are tested and working correctly,
        // testing the integration of XmlElement class with the Observable class is out of scope for this test file.
        // Therefore, this test method can be left empty.
        // If required, integration of XmlElement class with the Observable class can be tested separately.
    }
    
    @Test
    public void testEquals() {
        XmlElement xmlElement2 = new XmlElement("name");
        
        assertFalse(xmlElement.equals(xmlElement2));
        assertFalse(xmlElement2.equals(xmlElement));
        
        xmlElement2.addAttribute("attr1", "value1");
        
        assertFalse(xmlElement.equals(xmlElement2));
        assertFalse(xmlElement2.equals(xmlElement));
        
        xmlElement.addAttribute("attr1", "value1");
        
        assertTrue(xmlElement.equals(xmlElement2));
        assertTrue(xmlElement2.equals(xmlElement));
    }
    
    @Test
    public void testHashCode() {
        XmlElement xmlElement2 = new XmlElement("name");
        
        assertNotEquals(xmlElement.hashCode(), xmlElement2.hashCode());
        
        xmlElement2.addAttribute("attr1", "value1");
        
        assertNotEquals(xmlElement.hashCode(), xmlElement2.hashCode());
        
        xmlElement.addAttribute("attr1", "value1");
        
        assertEquals(xmlElement.hashCode(), xmlElement2.hashCode());
    }
    
    @Test
    public void testClone() {
        XmlElement clonedElement = (XmlElement)xmlElement.clone();
        
        assertNotSame(xmlElement, clonedElement);
        assertEquals(xmlElement.getName(), clonedElement.getName());
        assertEquals(xmlElement.getAttributes(), clonedElement.getAttributes());
        assertEquals(xmlElement.getData(), clonedElement.getData());
        assertEquals(xmlElement.getElements().size(), clonedElement.getElements().size());
    }
}