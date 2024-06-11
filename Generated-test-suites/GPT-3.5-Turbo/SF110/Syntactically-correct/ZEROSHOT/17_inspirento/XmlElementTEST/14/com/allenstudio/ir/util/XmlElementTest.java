import org.junit.*;
import static org.junit.Assert.*;
import java.util.*;

public class XmlElementTest {
    
    @Test
    public void testAddAttribute() {
        XmlElement xmlElement = new XmlElement();
        Object returnValue = xmlElement.addAttribute("name", "value");
        assertNull(returnValue);
    }
    
    @Test
    public void testGetAttribute() {
        XmlElement xmlElement = new XmlElement();
        xmlElement.addAttribute("name", "value");
        String attributeValue = xmlElement.getAttribute("name");
        assertEquals("value", attributeValue);
    }
    
    @Test
    public void testGetAttributeWithDefaultValue() {
        XmlElement xmlElement = new XmlElement();
        String defaultValue = "default";
        String attributeValue = xmlElement.getAttribute("name", defaultValue);
        assertEquals(defaultValue, attributeValue);
    }
    
    @Test
    public void testGetAttributes() {
        XmlElement xmlElement = new XmlElement();
        Hashtable<String, String> attributes = new Hashtable<>();
        attributes.put("name1", "value1");
        attributes.put("name2", "value2");
        xmlElement.setAttributes(attributes);
        Hashtable<String, String> returnedAttributes = xmlElement.getAttributes();
        assertEquals(attributes, returnedAttributes);
    }
    
    @Test
    public void testSetAttributes() {
        XmlElement xmlElement = new XmlElement();
        Hashtable<String, String> attributes = new Hashtable<>();
        attributes.put("name1", "value1");
        attributes.put("name2", "value2");
        xmlElement.setAttributes(attributes);
        Hashtable<String, String> returnedAttributes = xmlElement.getAttributes();
        assertEquals(attributes, returnedAttributes);
    }
    
    @Test
    public void testGetAttributeNames() {
        XmlElement xmlElement = new XmlElement();
        Hashtable<String, String> attributes = new Hashtable<>();
        attributes.put("name1", "value1");
        attributes.put("name2", "value2");
        xmlElement.setAttributes(attributes);
        Enumeration<String> attributeNames = xmlElement.getAttributeNames();
        List<String> expectedAttributeNames = Collections.list(attributeNames);
        assertEquals(Collections.list(attributes.keys()), expectedAttributeNames);
    }
    
    @Test
    public void testAddElement() {
        XmlElement xmlElement = new XmlElement();
        XmlElement subElement = new XmlElement("sub");
        boolean isAdded = xmlElement.addElement(subElement);
        assertTrue(isAdded);
    }
    
    @Test
    public void testRemoveElementByElement() {
        XmlElement xmlElement = new XmlElement();
        XmlElement subElement = new XmlElement("sub");
        xmlElement.addElement(subElement);
        XmlElement removedElement = xmlElement.removeElement(subElement);
        assertEquals(subElement, removedElement);
    }
    
    @Test
    public void testRemoveElementByIndex() {
        XmlElement xmlElement = new XmlElement();
        XmlElement subElement = new XmlElement("sub");
        xmlElement.addElement(subElement);
        XmlElement removedElement = xmlElement.removeElement(0);
        assertEquals(subElement, removedElement);
    }
    
    @Test
    public void testRemoveAllElements() {
        XmlElement xmlElement = new XmlElement();
        XmlElement subElement1 = new XmlElement("sub1");
        XmlElement subElement2 = new XmlElement("sub2");
        xmlElement.addElement(subElement1);
        xmlElement.addElement(subElement2);
        xmlElement.removeAllElements();
        List<XmlElement> subElements = xmlElement.getElements();
        assertEquals(0, subElements.size());
    }
    
    @Test
    public void testRemoveFromParent() {
        XmlElement parent = new XmlElement();
        XmlElement child = new XmlElement("child");
        parent.addElement(child);
        child.removeFromParent();
        assertNull(child.getParent());
    }
    
    @Test
    public void testAppend() {
        XmlElement parent1 = new XmlElement("parent1");
        XmlElement parent2 = new XmlElement("parent2");
        XmlElement child = new XmlElement("child");
        parent1.addElement(child);
        child.removeFromParent();
        parent2.append(child);
        assertEquals(parent2, child.getParent());
    }
    
    @Test
    public void testInsertElement() {
        XmlElement xmlElement = new XmlElement();
        XmlElement subElement1 = new XmlElement("sub1");
        XmlElement subElement2 = new XmlElement("sub2");
        xmlElement.addElement(subElement1);
        xmlElement.insertElement(subElement2, 1);
        List<XmlElement> subElements = xmlElement.getElements();
        assertEquals(subElement2, subElements.get(1));
    }
    
    @Test
    public void testGetElements() {
        XmlElement xmlElement = new XmlElement();
        XmlElement subElement1 = new XmlElement("sub1");
        XmlElement subElement2 = new XmlElement("sub2");
        xmlElement.addElement(subElement1);
        xmlElement.addElement(subElement2);
        List<XmlElement> subElements = xmlElement.getElements();
        assertEquals(2, subElements.size());
    }
    
    @Test
    public void testCount() {
        XmlElement xmlElement = new XmlElement();
        XmlElement subElement1 = new XmlElement("sub1");
        XmlElement subElement2 = new XmlElement("sub2");
        xmlElement.addElement(subElement1);
        xmlElement.addElement(subElement2);
        int count = xmlElement.count();
        assertEquals(2, count);
    }
    
    @Test
    public void testGetElementByPath() {
        XmlElement xmlElement = new XmlElement();
        XmlElement parent = new XmlElement("parent");
        XmlElement child = new XmlElement("child");
        parent.addElement(child);
        xmlElement.addElement(parent);
        XmlElement returnedElement = xmlElement.getElement("parent.child");
        assertEquals(child, returnedElement);
    }
    
    @Test
    public void testGetElementByIndex() {
        XmlElement xmlElement = new XmlElement();
        XmlElement parent = new XmlElement("parent");
        XmlElement child = new XmlElement("child");
        parent.addElement(child);
        xmlElement.addElement(parent);
        XmlElement returnedElement = xmlElement.getElement(0);
        assertEquals(parent, returnedElement);
    }
    
    @Test
    public void testAddSubElementWithPath() {
        XmlElement xmlElement = new XmlElement();
        XmlElement addedElement = xmlElement.addSubElement("parent.child");
        assertEquals("child", addedElement.getName());
    }
    
    @Test
    public void testAddSubElementWithXmlElement() {
        XmlElement xmlElement = new XmlElement();
        XmlElement subElement = new XmlElement("child");
        XmlElement addedElement = xmlElement.addSubElement(subElement);
        assertEquals(subElement, addedElement);
    }
    
    @Test
    public void testAddSubElementWithNameAndData() {
        XmlElement xmlElement = new XmlElement();
        XmlElement addedElement = xmlElement.addSubElement("child", "data");
        assertEquals("child", addedElement.getName());
        assertEquals("data", addedElement.getData());
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
        XmlElement xmlElement = new XmlElement("element", "data");
        String data = xmlElement.getData();
        assertEquals("data", data);
    }
    
    @Test
    public void testGetName() {
        XmlElement xmlElement = new XmlElement("element");
        String name = xmlElement.getName();
        assertEquals("element", name);
    }
    
    @Test
    public void testPrintNode() {
        XmlElement xmlElement1 = new XmlElement("element1");
        XmlElement xmlElement2 = new XmlElement("element2");
        xmlElement1.addElement(xmlElement2);
        
        // Redirect System.out
        ByteArrayOutputStream outContent = new ByteArrayOutputStream();
        System.setOut(new PrintStream(outContent));
        
        XmlElement.printNode(xmlElement1, "");
        
        // Restore System.out
        System.setOut(System.out);
        
        String expectedOutput = "element1\n" +
                                "element2\n";
        assertEquals(expectedOutput, outContent.toString());
    }
    
    @Test
    public void testClone() {
        XmlElement xmlElement1 = new XmlElement("element1");
        xmlElement1.addAttribute("name", "value");
        
        XmlElement xmlElement2 = (XmlElement)xmlElement1.clone();
        assertEquals(xmlElement1, xmlElement2);
    }
    
    @Test
    public void testNotifyObservers() {
        XmlElement xmlElement = new XmlElement();
        TestObserver observer = new TestObserver();
        xmlElement.addObserver(observer);
        xmlElement.notifyObservers();
        assertTrue(observer.isNotified());
    }
    
    @Test
    public void testEquals() {
        XmlElement xmlElement1 = new XmlElement("element1");
        xmlElement1.addAttribute("name", "value");
        
        XmlElement xmlElement2 = new XmlElement("element1");
        xmlElement2.addAttribute("name", "value");
        
        assertTrue(xmlElement1.equals(xmlElement2));
    }
    
    @Test
    public void testHashCode() {
        XmlElement xmlElement = new XmlElement("element");
        int expectedHashCode = 23 + xmlElement.getAttributes().hashCode() * 13 +
                               xmlElement.getData().hashCode() * 17 +
                               xmlElement.getName().hashCode() * 29 +
                               xmlElement.getElements().hashCode() * 57;
        int hashCode = xmlElement.hashCode();
        assertEquals(expectedHashCode, hashCode);
    }
}

class TestObserver implements Observer {
    private boolean notified = false;
    
    public void update(Observable o, Object arg) {
        notified = true;
    }
    
    public boolean isNotified() {
        return notified;
    }
}