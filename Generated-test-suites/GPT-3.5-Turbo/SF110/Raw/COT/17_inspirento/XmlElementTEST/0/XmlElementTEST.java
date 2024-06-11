
import org.junit.*;
import static org.junit.Assert.*;
import java.util.*;

public class XmlElementTest {
    
    @Test
    public void testAddAttribute() {
        XmlElement element = new XmlElement();
        element.addAttribute("name", "John");
        assertEquals("John", element.getAttribute("name"));
    }
    
    @Test
    public void testGetAttribute() {
        XmlElement element = new XmlElement();
        element.addAttribute("name", "John");
        assertEquals("John", element.getAttribute("name"));
    }
    
    @Test
    public void testGetAttributeWithDefaultValue() {
        XmlElement element = new XmlElement();
        String defaultValue = "Default";
        assertEquals(defaultValue, element.getAttribute("name", defaultValue));
    }
    
    @Test
    public void testGetAttributes() {
        XmlElement element = new XmlElement();
        element.addAttribute("name", "John");
        Hashtable<String, String> attrs = element.getAttributes();
        assertNotNull(attrs);
        assertEquals(1, attrs.size());
    }
    
    @Test
    public void testSetAttributes() {
        XmlElement element = new XmlElement();
        Hashtable<String, String> attrs = new Hashtable<>();
        attrs.put("name", "John");
        element.setAttributes(attrs);
        assertEquals(attrs, element.getAttributes());
    }
    
    @Test
    public void testGetAttributeNames() {
        XmlElement element = new XmlElement();
        element.addAttribute("name", "John");
        Enumeration names = element.getAttributeNames();
        assertNotNull(names);
        assertTrue(names.hasMoreElements());
    }
    
    @Test
    public void testAddElement() {
        XmlElement parentElement = new XmlElement();
        XmlElement childElement = new XmlElement("child");
        assertTrue(parentElement.addElement(childElement));
        assertEquals(childElement, parentElement.getElement(0));
    }
    
    @Test
    public void testRemoveElementByObject() {
        XmlElement parentElement = new XmlElement();
        XmlElement childElement = new XmlElement("child");
        parentElement.addElement(childElement);
        assertNotNull(parentElement.removeElement(childElement));
        assertTrue(parentElement.getElements().isEmpty());
    }
    
    @Test
    public void testRemoveElementByIndex() {
        XmlElement parentElement = new XmlElement();
        XmlElement childElement = new XmlElement("child");
        parentElement.addElement(childElement);
        assertNotNull(parentElement.removeElement(0));
        assertTrue(parentElement.getElements().isEmpty());
    }
    
    @Test
    public void testRemoveAllElements() {
        XmlElement parentElement = new XmlElement();
        XmlElement childElement1 = new XmlElement("child1");
        XmlElement childElement2 = new XmlElement("child2");
        parentElement.addElement(childElement1);
        parentElement.addElement(childElement2);
        parentElement.removeAllElements();
        assertTrue(parentElement.getElements().isEmpty());
    }
    
    @Test
    public void testRemoveFromParent() {
        XmlElement parentElement = new XmlElement();
        XmlElement childElement = new XmlElement("child");
        parentElement.addElement(childElement);
        childElement.removeFromParent();
        assertNull(childElement.getParent());
        assertTrue(parentElement.getElements().isEmpty());
    }
    
    @Test
    public void testAppend() {
        XmlElement parentElement = new XmlElement();
        XmlElement childElement = new XmlElement("child");
        childElement.removeFromParent();
        parentElement.append(childElement);
        assertEquals(childElement, parentElement.getElement(0));
    }
    
    @Test
    public void testInsertElement() {
        XmlElement parentElement = new XmlElement();
        XmlElement childElement1 = new XmlElement("child1");
        XmlElement childElement2 = new XmlElement("child2");
        parentElement.append(childElement1);
        parentElement.insertElement(childElement2, 1);
        assertEquals(childElement2, parentElement.getElement(1));
    }
    
    @Test
    public void testGetElements() {
        XmlElement parentElement = new XmlElement();
        XmlElement childElement1 = new XmlElement("child1");
        XmlElement childElement2 = new XmlElement("child2");
        parentElement.addElement(childElement1);
        parentElement.addElement(childElement2);
        List<XmlElement> elements = parentElement.getElements();
        assertNotNull(elements);
        assertEquals(2, elements.size());
    }
    
    @Test
    public void testCount() {
        XmlElement parentElement = new XmlElement();
        XmlElement childElement1 = new XmlElement("child1");
        XmlElement childElement2 = new XmlElement("child2");
        parentElement.addElement(childElement1);
        parentElement.addElement(childElement2);
        assertEquals(2, parentElement.count());
    }
    
    @Test
    public void testGetElementByPath() {
        XmlElement parentElement = new XmlElement();
        XmlElement childElement1 = new XmlElement("child1");
        XmlElement childElement2 = new XmlElement("child2");
        parentElement.addElement(childElement1);
        parentElement.addElement(childElement2);
        assertEquals(childElement1, parentElement.getElement("child1"));
    }
    
    @Test
    public void testGetElementByIndex() {
        XmlElement parentElement = new XmlElement();
        XmlElement childElement1 = new XmlElement("child1");
        XmlElement childElement2 = new XmlElement("child2");
        parentElement.addElement(childElement1);
        parentElement.addElement(childElement2);
        assertEquals(childElement2, parentElement.getElement(1));
    }
    
    @Test
    public void testAddSubElementByPath() {
        XmlElement parentElement = new XmlElement();
        XmlElement childElement = parentElement.addSubElement("child");
        assertNotNull(childElement);
        assertEquals(childElement, parentElement.getElement("child"));
    }
    
    @Test
    public void testAddSubElementByObject() {
        XmlElement parentElement = new XmlElement();
        XmlElement childElement = new XmlElement("child");
        parentElement.addSubElement(childElement);
        assertEquals(childElement, parentElement.getElement("child"));
    }
    
    @Test
    public void testAddSubElementWithData() {
        XmlElement parentElement = new XmlElement();
        XmlElement childElement = parentElement.addSubElement("child", "data");
        assertNotNull(childElement);
        assertEquals(childElement, parentElement.getElement("child"));
        assertEquals("data", childElement.getData());
    }
    
    @Test
    public void testSetParent() {
        XmlElement parentElement = new XmlElement();
        XmlElement childElement = new XmlElement("child");
        childElement.setParent(parentElement);
        assertEquals(parentElement, childElement.getParent());
    }
    
    @Test
    public void testGetData() {
        XmlElement element = new XmlElement("child", "data");
        assertEquals("data", element.getData());
    }
    
    @Test
    public void testGetName() {
        XmlElement element = new XmlElement("child");
        assertEquals("child", element.getName());
    }
    
    @Test
    public void testPrintNode() {
        XmlElement rootElement = new XmlElement("root");
        XmlElement childElement = new XmlElement("child", "data");
        rootElement.addSubElement(childElement);
        childElement.addAttribute("name", "John");
        ByteArrayOutputStream outContent = new ByteArrayOutputStream();
        System.setOut(new PrintStream(outContent));
        XmlElement.printNode(rootElement, "");
        String expectedOutput = "root\n    child = 'data'\n    name:John\n";
        assertEquals(expectedOutput, outContent.toString());
    }
    
    @Test
    public void testClone() {
        XmlElement element = new XmlElement("child");
        XmlElement clone = (XmlElement)element.clone();
        assertEquals(element, clone);
        assertNotSame(element, clone);
    }
    
    @Test
    public void testSetName() {
        XmlElement element = new XmlElement("oldName");
        element.setName("newName");
        assertEquals("newName", element.getName());
    }
    
    @Test
    public void testNotifyObservers() {
        XmlElement element = new XmlElement();
        MockObserver observer = new MockObserver();
        element.addObserver(observer);
        element.notifyObservers();
        assertTrue(observer.isNotified());
    }
    
    private class MockObserver implements Observer {
        private boolean notified;
        
        public boolean isNotified() {
            return notified;
        }
        
        @Override
        public void update(Observable o, Object arg) {
            notified = true;
        }
    }
    
    @Test
    public void testEquals() {
        XmlElement element1 = new XmlElement("root");
        XmlElement element2 = new XmlElement("root");
        assertEquals(element1, element2);
    }
    
    @Test
    public void testHashCode() {
        XmlElement element = new XmlElement("root");
        assertNotNull(element.hashCode());
    }
    
    @Test
    public void testEqualsWithNullObject() {
        XmlElement element = new XmlElement("root");
        assertFalse(element.equals(null));
    }
}
