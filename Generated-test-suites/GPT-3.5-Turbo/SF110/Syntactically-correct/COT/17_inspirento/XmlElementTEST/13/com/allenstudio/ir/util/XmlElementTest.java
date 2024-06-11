
import org.junit.Test;
import static org.junit.Assert.*;

public class XmlElementTest {
    
    @Test
    public void testAddAttribute() {
        XmlElement element = new XmlElement();
        Object returnValue = element.addAttribute("name", "value");
        assertNull(returnValue);
        assertEquals("value", element.getAttribute("name"));
    }
    
    @Test
    public void testGetAttribute() {
        XmlElement element = new XmlElement();
        element.addAttribute("name", "value");
        String attribute = element.getAttribute("name");
        assertEquals("value", attribute);
    }
    
    @Test
    public void testGetAttributeWithDefaultValue() {
        XmlElement element = new XmlElement();
        String attribute = element.getAttribute("name", "defaultValue");
        assertEquals("defaultValue", attribute);
    }
    
    @Test
    public void testGetAttributes() {
        XmlElement element = new XmlElement();
        element.addAttribute("name1", "value1");
        element.addAttribute("name2", "value2");
        assertEquals(2, element.getAttributes().size());
        assertEquals("value1", element.getAttributes().get("name1"));
        assertEquals("value2", element.getAttributes().get("name2"));
    }
    
    @Test
    public void testSetAttributes() {
        XmlElement element = new XmlElement();
        Hashtable<String, String> attrs = new Hashtable<String, String>();
        attrs.put("name1", "value1");
        attrs.put("name2", "value2");
        element.setAttributes(attrs);
        assertEquals(2, element.getAttributes().size());
        assertEquals("value1", element.getAttributes().get("name1"));
        assertEquals("value2", element.getAttributes().get("name2"));
    }
    
    @Test
    public void testGetAttributeNames() {
        XmlElement element = new XmlElement();
        element.addAttribute("name1", "value1");
        element.addAttribute("name2", "value2");
        element.addAttribute("name3", "value3");
        Enumeration enumeration = element.getAttributeNames();
        List<String> names = new ArrayList<>();
        while (enumeration.hasMoreElements()) {
            names.add((String) enumeration.nextElement());
        }
        assertEquals(3, names.size());
        assertTrue(names.contains("name1"));
        assertTrue(names.contains("name2"));
        assertTrue(names.contains("name3"));
    }
    
    @Test
    public void testAddElement() {
        XmlElement element = new XmlElement();
        XmlElement subElement = new XmlElement();
        boolean added = element.addElement(subElement);
        assertTrue(added);
        assertEquals(1, element.getElements().size());
        assertEquals(subElement, element.getElements().get(0));
    }
    
    @Test
    public void testRemoveElement() {
        XmlElement element = new XmlElement();
        XmlElement subElement = new XmlElement();
        element.addElement(subElement);
        XmlElement removedElement = element.removeElement(subElement);
        assertEquals(subElement, removedElement);
        assertTrue(element.getElements().isEmpty());
    }
    
    @Test
    public void testRemoveElementByIndex() {
        XmlElement element = new XmlElement();
        XmlElement subElement = new XmlElement();
        element.addElement(subElement);
        XmlElement removedElement = element.removeElement(0);
        assertEquals(subElement, removedElement);
        assertTrue(element.getElements().isEmpty());
    }
    
    @Test
    public void testRemoveAllElements() {
        XmlElement element = new XmlElement();
        element.addElement(new XmlElement());
        element.addElement(new XmlElement());
        element.addElement(new XmlElement());
        element.removeAllElements();
        assertTrue(element.getElements().isEmpty());
    }
    
    @Test
    public void testRemoveFromParent() {
        XmlElement parent = new XmlElement();
        XmlElement element = new XmlElement();
        parent.addElement(element);
        element.removeFromParent();
        assertTrue(parent.getElements().isEmpty());
        assertNull(element.getParent());
    }
    
    @Test
    public void testAppend() {
        XmlElement parent = new XmlElement();
        XmlElement element = new XmlElement();
        element.setData("data");
        parent.append(element);
        assertEquals(1, parent.getElements().size());
        assertEquals(element, parent.getElements().get(0));
    }
    
    @Test
    public void testInsertElement() {
        XmlElement parent = new XmlElement();
        XmlElement element1 = new XmlElement();
        XmlElement element2 = new XmlElement();
        parent.addElement(element1);
        parent.insertElement(element2, 0);
        assertEquals(2, parent.getElements().size());
        assertEquals(element2, parent.getElements().get(0));
    }
    
    @Test
    public void testGetElements() {
        XmlElement parent = new XmlElement();
        XmlElement element1 = new XmlElement();
        XmlElement element2 = new XmlElement();
        parent.addElement(element1);
        parent.addElement(element2);
        assertEquals(2, parent.getElements().size());
        assertEquals(element1, parent.getElements().get(0));
        assertEquals(element2, parent.getElements().get(1));
    }
    
    @Test
    public void testCount() {
        XmlElement parent = new XmlElement();
        parent.addElement(new XmlElement());
        parent.addElement(new XmlElement());
        parent.addElement(new XmlElement());
        assertEquals(3, parent.count());
    }
    
    @Test
    public void testGetElementWithPath() {
        XmlElement parent = new XmlElement();
        XmlElement child1 = new XmlElement("child1");
        XmlElement child2 = new XmlElement("child2");
        parent.addElement(child1);
        child1.addElement(child2);
        XmlElement element = parent.getElement("child1.child2");
        assertEquals(child2, element);
    }
    
    @Test
    public void testGetElementByIndex() {
        XmlElement parent = new XmlElement();
        XmlElement child1 = new XmlElement();
        XmlElement child2 = new XmlElement();
        parent.addElement(child1);
        parent.addElement(child2) ;
        XmlElement element = parent.getElement(1);
        assertEquals(child2, element);
    }
    
    @Test
    public void testAddSubElementByPath() {
        XmlElement parent = new XmlElement();
        XmlElement child1 = parent.addSubElement("child1");
        assertNotNull(parent.getElement("child1"));
        assertEquals(child1, parent.getElement("child1"));
    }
    
    @Test
    public void testAddSubElement() {
        XmlElement parent = new XmlElement();
        XmlElement child1 = new XmlElement();
        parent.addSubElement(child1);
        assertNotNull(parent.getElement(child1.getName()));
        assertEquals(child1, parent.getElement(child1.getName()));
    }
    
    @Test
    public void testAddSubElementWithNameAndData() {
        XmlElement parent = new XmlElement();
        XmlElement child1 = parent.addSubElement("child1", "data1");
        assertNotNull(parent.getElement("child1"));
        assertEquals(child1, parent.getElement("child1"));
        assertEquals("data1", child1.getData());
    }
    
    @Test
    public void testSetParent() {
        XmlElement parent = new XmlElement();
        XmlElement child1 = new XmlElement();
        child1.setParent(parent);
        assertEquals(parent, child1.getParent());
    }
    
    @Test
    public void testGetData() {
        XmlElement element = new XmlElement();
        element.setData("data");
        String data = element.getData();
        assertEquals("data", data);
    }
    
    @Test
    public void testGetName() {
        XmlElement element = new XmlElement("name");
        String name = element.getName();
        assertEquals("name", name);
    }
    
    @Test
    public void testPrintNode() {
        // cannot be tested using JUnit as it requires console output
    }
    
    @Test
    public void testClone() {
        XmlElement element1 = new XmlElement("name");
        element1.addAttribute("attribute", "value");
        XmlElement element2 = new XmlElement();
        element2.setData("data");
        element1.addElement(element2);
        XmlElement clone = (XmlElement) element1.clone();
        assertNotNull(clone);
        assertNotSame(element1, clone);
        assertEquals(element1, clone);
    }
    
    @Test
    public void testNotifyObservers() {
        XmlElement element = new XmlElement();
        TestObserver observer = new TestObserver();
        element.addObserver(observer);
        element.notifyObservers();
        assertTrue(observer.isNotified());
    }
    
    private class TestObserver implements Observer {
        private boolean notified = false;
        
        @Override
        public void update(Observable o, Object arg) {
            notified = true;
        }
        
        public boolean isNotified() {
            return notified;
        }
    }
    
    @Test
    public void testEquals() {
        XmlElement element1 = new XmlElement("name");
        element1.addAttribute("attribute", "value");
        XmlElement element2 = new XmlElement("name");
        element2.addAttribute("attribute", "value");
        assertTrue(element1.equals(element2));
    }
    
    @Test
    public void testHashCode() {
        XmlElement element1 = new XmlElement("name");
        element1.addAttribute("attribute", "value");
        XmlElement element2 = new XmlElement("name");
        element2.addAttribute("attribute", "value");
        assertEquals(element1.hashCode(), element2.hashCode());
    }
}

