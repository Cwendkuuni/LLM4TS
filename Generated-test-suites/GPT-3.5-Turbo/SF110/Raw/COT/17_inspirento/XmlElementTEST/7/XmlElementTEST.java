
import static org.junit.Assert.*;
import org.junit.Before;
import org.junit.Test;
import java.util.*;

public class XmlElementTest {
    
    private XmlElement xmlElement;
    
    @Before
    public void setUp() {
        xmlElement = new XmlElement();
    }
    
    @Test
    public void testAddAttribute() {
        assertNull(xmlElement.addAttribute("attr1", "value1"));
        
        assertEquals("value1", xmlElement.getAttribute("attr1"));
        
        assertEquals("value1", xmlElement.addAttribute("attr1", "value2"));
        
        assertNull(xmlElement.addAttribute(null, "value3"));
        
        assertNull(xmlElement.addAttribute("attr3", null));
        
        assertNull(xmlElement.addAttribute(null, null));
        
        assertNull(xmlElement.getAttribute(null));
    }
    
    @Test
    public void testGetAttribute() {
        xmlElement.addAttribute("attr1", "value1");
        
        assertEquals("value1", xmlElement.getAttribute("attr1"));
        
        assertNull(xmlElement.getAttribute("attr2"));
        
        assertEquals("default", xmlElement.getAttribute("attr2", "default"));
    }
    
    @Test
    public void testGetAttributes() {
        Hashtable<String, String> attributes = new Hashtable<String, String>();
        attributes.put("attr1", "value1");
        attributes.put("attr2", "value2");
        
        xmlElement.setAttributes(attributes);
        
        assertEquals(attributes, xmlElement.getAttributes());
    }
    
    @Test
    public void testGetAttributeNames() {
        xmlElement.addAttribute("attr1", "value1");
        xmlElement.addAttribute("attr2", "value2");
        
        Enumeration enumeration = xmlElement.getAttributeNames();
        
        List<String> attributeNames = new ArrayList<String>();
        while (enumeration.hasMoreElements()) {
            attributeNames.add((String) enumeration.nextElement());
        }
        
        assertTrue(attributeNames.contains("attr1"));
        assertTrue(attributeNames.contains("attr2"));
        assertFalse(attributeNames.contains("attr3"));
    }
    
    @Test
    public void testAddElement() {
        XmlElement child = new XmlElement("child");

        assertTrue(xmlElement.addElement(child));
        assertTrue(xmlElement.getElements().contains(child));
    }
    
    @Test
    public void testRemoveElementByElement() {
        XmlElement child1 = new XmlElement("child1");
        XmlElement child2 = new XmlElement("child2");
        
        xmlElement.addElement(child1);
        xmlElement.addElement(child2);
        
        assertEquals(child1, xmlElement.removeElement(child1));
        assertFalse(xmlElement.getElements().contains(child1));
    }
    
    @Test
    public void testRemoveElementByIndex() {
        XmlElement child1 = new XmlElement("child1");
        XmlElement child2 = new XmlElement("child2");
        
        xmlElement.addElement(child1);
        xmlElement.addElement(child2);
        
        assertEquals(child2, xmlElement.removeElement(1));
        assertFalse(xmlElement.getElements().contains(child2));
    }
    
    @Test
    public void testRemoveAllElements() {
        XmlElement child1 = new XmlElement("child1");
        XmlElement child2 = new XmlElement("child2");
        
        xmlElement.addElement(child1);
        xmlElement.addElement(child2);
        
        xmlElement.removeAllElements();
        
        assertTrue(xmlElement.getElements().isEmpty());
    }
    
    @Test
    public void testRemoveFromParent() {
        XmlElement child = new XmlElement("child");
        
        xmlElement.addElement(child);
        
        child.removeFromParent();
        
        assertFalse(xmlElement.getElements().contains(child));
        assertNull(child.getParent());
    }
    
    @Test
    public void testAppend() {
        XmlElement child1 = new XmlElement("child1");
        XmlElement child2 = new XmlElement("child2");
        
        xmlElement.addElement(child1);
        xmlElement.append(child2);
        
        assertFalse(xmlElement.getElements().contains(child1));
        assertTrue(xmlElement.getElements().contains(child2));
        assertEquals(xmlElement, child2.getParent());
    }
    
    @Test
    public void testInsertElement() {
        XmlElement child1 = new XmlElement("child1");
        XmlElement child2 = new XmlElement("child2");
        
        xmlElement.addElement(child1);
        xmlElement.insertElement(child2, 0);
        
        assertTrue(xmlElement.getElements().contains(child2));
        assertEquals(xmlElement, child2.getParent());
    }
    
    @Test
    public void testGetElements() {
        XmlElement child1 = new XmlElement("child1");
        XmlElement child2 = new XmlElement("child2");
        
        xmlElement.addElement(child1);
        xmlElement.addElement(child2);
        
        List<XmlElement> elements = xmlElement.getElements();
        
        assertTrue(elements.contains(child1));
        assertTrue(elements.contains(child2));
    }
    
    @Test
    public void testCount() {
        XmlElement child1 = new XmlElement("child1");
        XmlElement child2 = new XmlElement("child2");
        
        xmlElement.addElement(child1);
        xmlElement.addElement(child2);
        
        assertEquals(2, xmlElement.count());
    }
    
    @Test
    public void testGetElementByPath() {
        XmlElement child1 = new XmlElement("child1");
        XmlElement child2 = new XmlElement("child2");
        XmlElement child3 = new XmlElement("child3");
        
        xmlElement.addElement(child1);
        child1.addElement(child2);
        child2.addElement(child3);
        
        assertEquals(child1, xmlElement.getElement("child1"));
        assertEquals(child2, xmlElement.getElement("child1.child2"));
        assertEquals(child3, xmlElement.getElement("child1.child2.child3"));
        assertNull(xmlElement.getElement("child2"));
        assertNull(xmlElement.getElement("child1.child3"));
    }
    
    @Test
    public void testGetElementByIndex() {
        XmlElement child1 = new XmlElement("child1");
        XmlElement child2 = new XmlElement("child2");
        
        xmlElement.addElement(child1);
        xmlElement.addElement(child2);
        
        assertEquals(child1, xmlElement.getElement(0));
        assertEquals(child2, xmlElement.getElement(1));
    }
    
    @Test
    public void testAddSubElementByPath() {
        XmlElement child1 = xmlElement.addSubElement("child1");
        
        assertEquals(child1, xmlElement.getElement(0));
        assertEquals(child1, xmlElement.addSubElement("child1.child2").getParent());
    }
    
    @Test
    public void testAddSubElementByElement() {
        XmlElement child1 = new XmlElement("child1");
        
        assertEquals(child1, xmlElement.addSubElement(child1));
        assertEquals(child1, xmlElement.getElement(0));
    }
    
    @Test
    public void testAddSubElementByNameAndData() {
        XmlElement child1 = xmlElement.addSubElement("child1", "data1");
        
        assertEquals(child1, xmlElement.getElement(0));
        assertEquals("data1", child1.getData());
    }
    
    @Test
    public void testSetParent() {
        XmlElement parent = new XmlElement("parent");
        
        xmlElement.setParent(parent);
        
        assertEquals(parent, xmlElement.getParent());
    }
    
    @Test
    public void testGetData() {
        xmlElement.setData("data");
        
        assertEquals("data", xmlElement.getData());
    }
    
    @Test
    public void testGetName() {
        xmlElement.setName("name");
        
        assertEquals("name", xmlElement.getName());
    }
    
    @Test
    public void testPrintNode() {
        XmlElement child1 = new XmlElement("child1");
        XmlElement child2 = new XmlElement("child2");
        child2.addAttribute("attr1", "value1");
        child2.setData("data1");
        
        xmlElement.addElement(child1);
        xmlElement.addElement(child2);
        
        xmlElement.printNode(xmlElement, "");
    }
    
    @Test
    public void testClone() {
        XmlElement child1 = new XmlElement("child1");
        XmlElement child2 = new XmlElement("child2");
        
        xmlElement.addElement(child1);
        xmlElement.addElement(child2);
        
        XmlElement clone = (XmlElement) xmlElement.clone();
        
        assertEquals(xmlElement, clone);
        assertNotSame(xmlElement.getElements(), clone.getElements());
    }
    
    @Test
    public void testNotifyObservers() {
        TestObserver observer = new TestObserver();
        
        xmlElement.addObserver(observer);
        xmlElement.notifyObservers();
        
        assertTrue(observer.notified);
    }
    
    @Test
    public void testEquals() {
        XmlElement element1 = new XmlElement("name1");
        XmlElement element2 = new XmlElement("name2");
        XmlElement element3 = new XmlElement("name1");
        
        assertFalse(element1.equals(element2));
        assertTrue(element1.equals(element3));
    }
    
    @Test
    public void testHashCode() {
        XmlElement element1 = new XmlElement("name1");
        XmlElement element2 = new XmlElement("name2");
        XmlElement element3 = new XmlElement("name1");
        
        assertNotEquals(element1.hashCode(), element2.hashCode());
        assertEquals(element1.hashCode(), element3.hashCode());
    }
    
    class TestObserver implements Observer {
        
        public boolean notified = false;
        
        public void update(Observable o, Object arg) {
            notified = true;
        }
    }
    
}

