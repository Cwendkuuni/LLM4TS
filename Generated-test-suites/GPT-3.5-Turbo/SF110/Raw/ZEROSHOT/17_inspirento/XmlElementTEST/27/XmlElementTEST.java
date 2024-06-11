import static org.junit.Assert.*;
import org.junit.Test;

public class XmlElementTest {
    
    @Test
    public void testAddAttribute() {
        XmlElement element = new XmlElement();
        assertNull(element.addAttribute("name", "value"));
    }
    
    @Test
    public void testGetAttribute() {
        XmlElement element = new XmlElement();
        element.addAttribute("name", "value");
        assertEquals("value", element.getAttribute("name"));
    }
    
    @Test
    public void testGetAttributeWithDefaultValue() {
        XmlElement element = new XmlElement();
        assertEquals("default", element.getAttribute("name", "default"));
    }
    
    @Test
    public void testGetAttributes() {
        XmlElement element = new XmlElement();
        assertNotNull(element.getAttributes());
    }
    
    @Test
    public void testSetAttributes() {
        XmlElement element = new XmlElement();
        Hashtable<String, String> attrs = new Hashtable<String, String>();
        attrs.put("name", "value");
        element.setAttributes(attrs);
        assertEquals(attrs, element.getAttributes());
    }
    
    @Test
    public void testGetAttributeNames() {
        XmlElement element = new XmlElement();
        assertNotNull(element.getAttributeNames());
    }
    
    @Test
    public void testAddElement() {
        XmlElement element = new XmlElement();
        XmlElement subElement = new XmlElement("sub");
        assertTrue(element.addElement(subElement));
        assertEquals(1, element.count());
    }
    
    @Test
    public void testRemoveElement() {
        XmlElement element = new XmlElement();
        XmlElement subElement = new XmlElement("sub");
        element.addElement(subElement);
        assertNotNull(element.removeElement(subElement));
        assertEquals(0, element.count());
    }
    
    @Test
    public void testRemoveElementByIndex() {
        XmlElement element = new XmlElement();
        XmlElement subElement = new XmlElement("sub");
        element.addElement(subElement);
        assertNotNull(element.removeElement(0));
        assertEquals(0, element.count());
    }
    
    @Test
    public void testRemoveAllElements() {
        XmlElement element = new XmlElement();
        XmlElement subElement = new XmlElement("sub");
        element.addElement(subElement);
        element.removeAllElements();
        assertEquals(0, element.count());
    }
    
    @Test
    public void testRemoveFromParent() {
        XmlElement parent = new XmlElement();
        XmlElement subElement = new XmlElement("sub");
        parent.addElement(subElement);
        subElement.removeFromParent();
        assertEquals(0, parent.count());
        assertNull(subElement.getParent());
    }
    
    @Test
    public void testAppend() {
        XmlElement parent = new XmlElement();
        XmlElement subElement = new XmlElement("sub");
        subElement.setParent(parent);
        subElement.append(new XmlElement("sub2"));
        assertEquals(1, parent.count());
    }
    
    @Test
    public void testInsertElement() {
        XmlElement element = new XmlElement();
        XmlElement subElement = new XmlElement("sub");
        element.addElement(subElement);
        element.insertElement(new XmlElement("sub2"), 0);
        assertEquals(2, element.count());
    }
    
    @Test
    public void testGetElements() {
        XmlElement element = new XmlElement();
        assertNotNull(element.getElements());
    }
    
    @Test
    public void testCount() {
        XmlElement element = new XmlElement();
        assertEquals(0, element.count());
    }
    
    @Test
    public void testGetElementByName() {
        XmlElement element = new XmlElement();
        XmlElement subElement = new XmlElement("sub");
        element.addElement(subElement);
        assertEquals(subElement, element.getElement("sub"));
    }
    
    @Test
    public void testGetElementByIndex() {
        XmlElement element = new XmlElement();
        XmlElement subElement = new XmlElement("sub");
        element.addElement(subElement);
        assertEquals(subElement, element.getElement(0));
    }
    
    @Test
    public void testAddSubElementByPath() {
        XmlElement element = new XmlElement();
        XmlElement subElement = element.addSubElement("path.to.sub");
        assertNotNull(subElement);
        assertEquals(1, element.count());
    }
    
    @Test
    public void testAddSubElement() {
        XmlElement element = new XmlElement();
        XmlElement subElement = new XmlElement("sub");
        assertEquals(subElement, element.addSubElement(subElement));
        assertEquals(1, element.count());
    }
    
    @Test
    public void testAddSubElementWithNameAndData() {
        XmlElement element = new XmlElement();
        XmlElement subElement = element.addSubElement("sub", "data");
        assertEquals(1, element.count());
        assertEquals("data", subElement.getData());
    }
    
    @Test
    public void testSetParent() {
        XmlElement element = new XmlElement();
        XmlElement parent = new XmlElement("parent");
        element.setParent(parent);
        assertEquals(parent, element.getParent());
    }
    
    @Test
    public void testGetData() {
        XmlElement element = new XmlElement("name", "data");
        assertEquals("data", element.getData());
    }
    
    @Test
    public void testSetName() {
        XmlElement element = new XmlElement();
        element.setName("name");
        assertEquals("name", element.getName());
    }
    
    @Test
    public void testNotifyObservers() {
        XmlElement element = new XmlElement();
        TestObserver observer = new TestObserver();
        element.addObserver(observer);
        element.notifyObservers();
        assertTrue(observer.isNotified());
    }
    
    @Test
    public void testEquals() {
        XmlElement element1 = new XmlElement("name", "data");
        XmlElement element2 = new XmlElement("name", "data");
        assertTrue(element1.equals(element2));
    }
    
    @Test
    public void testHashCode() {
        XmlElement element = new XmlElement("name", "data");
        assertEquals(269455, element.hashCode());
    }
    
    private class TestObserver implements java.util.Observer {
        private boolean notified = false;
        
        @Override
        public void update(Observable o, Object arg) {
            notified = true;
        }
        
        public boolean isNotified() {
            return notified;
        }
    }
}