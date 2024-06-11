
import static org.junit.Assert.*;
import java.util.*;
import org.junit.*;

public class XmlElementTest {
    
    @Test
    public void testAddAttribute() {
        XmlElement element = new XmlElement("test");
        Object returnValue = element.addAttribute("name", "John Doe");
        
        assertNotNull(returnValue);
        assertNull(element.getAttribute("age"));
        assertEquals("John Doe", element.getAttribute("name"));
    }
    
    @Test
    public void testGetAttribute() {
        XmlElement element = new XmlElement("test");
        element.addAttribute("name", "John Doe");
        
        assertEquals("John Doe", element.getAttribute("name"));
        assertEquals("Jane Smith", element.getAttribute("name", "Jane Smith"));
        assertNull(element.getAttribute("age"));
    }
    
    @Test
    public void testGetAttributes() {
        XmlElement element = new XmlElement("test");
        element.addAttribute("name", "John Doe");
        element.addAttribute("age", "25");
        
        Hashtable<String, String> attributes = element.getAttributes();
        
        assertEquals(2, attributes.size());
        assertEquals("John Doe", attributes.get("name"));
        assertEquals("25", attributes.get("age"));
    }
    
    @Test
    public void testSetAttributes() {
        XmlElement element = new XmlElement("test");
        Hashtable<String, String> attributes = new Hashtable<>();
        attributes.put("name", "John Doe");
        attributes.put("age", "25");
        
        element.setAttributes(attributes);
        
        assertEquals(2, element.getAttributes().size());
        assertEquals("John Doe", element.getAttribute("name"));
        assertEquals("25", element.getAttribute("age"));
    }
    
    @Test
    public void testAddElement() {
        XmlElement parent = new XmlElement("parent");
        XmlElement child = new XmlElement("child");
        
        assertTrue(parent.addElement(child));
        assertEquals(1, parent.count());
        assertEquals(child, parent.getElement(0));
    }
    
    @Test
    public void testRemoveElement() {
        XmlElement parent = new XmlElement("parent");
        XmlElement child = new XmlElement("child");
        
        parent.addElement(child);
        XmlElement removedElement = parent.removeElement(child);
        
        assertEquals(child, removedElement);
        assertEquals(0, parent.count());
    }
    
    @Test
    public void testRemoveElementByIndex() {
        XmlElement parent = new XmlElement("parent");
        XmlElement child1 = new XmlElement("child1");
        XmlElement child2 = new XmlElement("child2");
        
        parent.addElement(child1);
        parent.addElement(child2);
        
        XmlElement removedElement = parent.removeElement(1);
        
        assertEquals(child2, removedElement);
        assertEquals(1, parent.count());
        assertEquals(child1, parent.getElement(0));
    }
    
    @Test
    public void testRemoveAllElements() {
        XmlElement parent = new XmlElement("parent");
        XmlElement child1 = new XmlElement("child1");
        XmlElement child2 = new XmlElement("child2");
        
        parent.addElement(child1);
        parent.addElement(child2);
        
        parent.removeAllElements();
        
        assertEquals(0, parent.count());
    }
    
    @Test
    public void testRemoveFromParent() {
        XmlElement parent = new XmlElement("parent");
        XmlElement child = new XmlElement("child");
        
        parent.addElement(child);
        child.removeFromParent();
        
        assertEquals(0, parent.count());
        assertNull(child.getParent());
    }
    
    @Test
    public void testAppend() {
        XmlElement parent1 = new XmlElement("parent1");
        XmlElement parent2 = new XmlElement("parent2");
        XmlElement child = new XmlElement("child");
        
        parent1.addElement(child);
        parent2.append(child);
        
        assertEquals(1, parent2.count());
        assertEquals(null, parent1.getElement(0));
        assertEquals(child, parent2.getElement(0));
    }
    
    @Test
    public void testInsertElement() {
        XmlElement parent = new XmlElement("parent");
        XmlElement child1 = new XmlElement("child1");
        XmlElement child2 = new XmlElement("child2");
        XmlElement child3 = new XmlElement("child3");
        
        parent.addElement(child1);
        parent.addElement(child3);
        
        parent.insertElement(child2, 1);
        
        assertEquals(3, parent.count());
        assertEquals(child1, parent.getElement(0));
        assertEquals(child2, parent.getElement(1));
        assertEquals(child3, parent.getElement(2));
    }
    
    @Test
    public void testGetElement() {
        XmlElement parent = new XmlElement("parent");
        XmlElement child1 = new XmlElement("child1");
        XmlElement child2 = new XmlElement("child2");
        XmlElement child3 = new XmlElement("child3");
        
        parent.addElement(child1);
        parent.addElement(child2);
        parent.addElement(child3);
        
        assertEquals(child2, parent.getElement("child2"));
        assertEquals(child3, parent.getElement("child3"));
        assertNull(parent.getElement("child4"));
    }
    
    @Test
    public void testGetElementByIndex() {
        XmlElement parent = new XmlElement("parent");
        XmlElement child1 = new XmlElement("child1");
        XmlElement child2 = new XmlElement("child2");
        XmlElement child3 = new XmlElement("child3");
        
        parent.addElement(child1);
        parent.addElement(child2);
        parent.addElement(child3);
        
        assertEquals(child2, parent.getElement(1));
        assertEquals(child3, parent.getElement(2));
    }
    
    @Test
    public void testAddSubElementWithPath() {
        XmlElement element = new XmlElement("element");
        
        XmlElement child = element.addSubElement("path.to.child");
        
        assertNotNull(child);
        assertEquals("child", child.getName());
        assertEquals("element", child.getParent().getName());
    }
    
    @Test
    public void testAddSubElementWithXmlElement() {
        XmlElement parent = new XmlElement("parent");
        XmlElement child = new XmlElement("child");
        
        XmlElement addedChild = parent.addSubElement(child);
        
        assertNotNull(addedChild);
        assertEquals(child, addedChild);
        assertEquals(parent, child.getParent());
    }
    
    @Test
    public void testAddSubElementWithString() {
        XmlElement parent = new XmlElement("parent");
        
        XmlElement child = parent.addSubElement("child", "data");
        
        assertNotNull(child);
        assertEquals("child", child.getName());
        assertEquals("data", child.getData());
        assertEquals(parent, child.getParent());
    }
    
    @Test
    public void testSetParent() {
        XmlElement parent = new XmlElement("parent");
        XmlElement child = new XmlElement("child");
        
        child.setParent(parent);
        
        assertEquals(parent, child.getParent());
    }
    
    @Test
    public void testGetData() {
        XmlElement element = new XmlElement("element");
        element.setData("data");
        
        assertEquals("data", element.getData());
    }
    
    @Test
    public void testGetName() {
        XmlElement element = new XmlElement("element");
        
        assertEquals("element", element.getName());
    }
    
    @Test
    public void testPrintNode() {
        XmlElement parent = new XmlElement("parent");
        XmlElement child1 = new XmlElement("child1");
        XmlElement child2 = new XmlElement("child2");
        parent.addElement(child1);
        parent.addElement(child2);
        
        XmlElement.printNode(parent, "");
    }
    
    @Test
    public void testClone() {
        XmlElement parent = new XmlElement("parent");
        XmlElement child1 = new XmlElement("child1");
        XmlElement child2 = new XmlElement("child2");
        parent.addElement(child1);
        parent.addElement(child2);
        
        XmlElement clone = (XmlElement) parent.clone();
        
        assertEquals(parent, clone);
        assertNotSame(parent, clone);
    }
    
    @Test
    public void testNotifyObservers() {
        XmlElement element = new XmlElement("element");
        
        TestObserver observer = new TestObserver();
        element.addObserver(observer);
        element.notifyObservers();
        
        assertTrue(observer.isNotified());
        assertEquals(element, observer.getObservable());
    }
    
    @Test
    public void testEquals() {
        XmlElement element1 = new XmlElement("element");
        XmlElement element2 = new XmlElement("element");
        XmlElement element3 = new XmlElement("element3");
        
        assertEquals(element1, element2);
        assertNotEquals(element1, element3);
    }
    
    @Test
    public void testHashCode() {
        XmlElement element1 = new XmlElement("element");
        XmlElement element2 = new XmlElement("element");
        XmlElement element3 = new XmlElement("element3");
        
        assertEquals(element1.hashCode(), element2.hashCode());
        assertNotEquals(element1.hashCode(), element3.hashCode());
    }
    
    private class TestObserver implements Observer {
        private boolean notified;
        private Object observable;
        
        @Override
        public void update(Observable o, Object arg) {
            notified = true;
            observable = o;
        }
        
        public boolean isNotified() {
            return notified;
        }
        
        public Object getObservable() {
            return observable;
        }
    }
}
