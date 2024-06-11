import org.junit.Test;
import static org.junit.Assert.*;

import java.util.Hashtable;
import java.util.List;

public class XmlElementTest {

    @Test
    public void testAddAttribute() {
        XmlElement element = new XmlElement();
        Object returnValue = element.addAttribute("name", "value");
        assertNull(returnValue);
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
        String attribute = element.getAttribute("name", "default");
        assertEquals("default", attribute);
    }
    
    @Test
    public void testGetAttributes() {
        XmlElement element = new XmlElement();
        Hashtable<String, String> attributes = new Hashtable<>();
        attributes.put("name", "value");
        element.setAttributes(attributes);
        Hashtable<String, String> result = element.getAttributes();
        assertEquals(attributes, result);
    }
    
    @Test
    public void testSetAttributes() {
        XmlElement element = new XmlElement();
        Hashtable<String, String> attributes = new Hashtable<>();
        attributes.put("name", "value");
        element.setAttributes(attributes);
        Hashtable<String, String> result = element.getAttributes();
        assertEquals(attributes, result);
    }
    
    @Test
    public void testGetAttributeNames() {
        XmlElement element = new XmlElement();
        element.addAttribute("name1", "value1");
        element.addAttribute("name2", "value2");
        Enumeration<String> names = element.getAttributeNames();
        List<String> expectedNames = Arrays.asList("name1", "name2");
        List<String> actualNames = Collections.list(names);
        assertEquals(expectedNames, actualNames);
    }
    
    @Test
    public void testAddElement() {
        XmlElement parent = new XmlElement();
        XmlElement child = new XmlElement("child");
        boolean result = parent.addElement(child);
        assertTrue(result);
    }
    
    @Test
    public void testRemoveElementByObject() {
        XmlElement parent = new XmlElement();
        XmlElement child = new XmlElement("child");
        parent.addElement(child);
        XmlElement result = parent.removeElement(child);
        assertEquals(child, result);
    }
    
    @Test
    public void testRemoveElementByIndex() {
        XmlElement parent = new XmlElement();
        XmlElement child = new XmlElement("child");
        parent.addElement(child);
        XmlElement result = parent.removeElement(0);
        assertEquals(child, result);
    }
    
    @Test
    public void testRemoveAllElements() {
        XmlElement parent = new XmlElement();
        XmlElement child1 = new XmlElement("child1");
        XmlElement child2 = new XmlElement("child2");
        parent.addElement(child1);
        parent.addElement(child2);
        parent.removeAllElements();
        List<XmlElement> result = parent.getElements();
        assertTrue(result.isEmpty());
    }
    
    @Test
    public void testRemoveFromParent() {
        XmlElement parent = new XmlElement();
        XmlElement child = new XmlElement("child");
        parent.addElement(child);
        child.removeFromParent();
        List<XmlElement> result = parent.getElements();
        assertTrue(result.isEmpty());
    }
    
    @Test
    public void testAppend() {
        XmlElement parent = new XmlElement();
        XmlElement child = new XmlElement("child");
        child.removeFromParent();
        parent.append(child);
        List<XmlElement> result = parent.getElements();
        assertEquals(1, result.size());
        assertEquals(child, result.get(0));
    }
    
    @Test
    public void testInsertElement() {
        XmlElement parent = new XmlElement();
        XmlElement child1 = new XmlElement("child1");
        parent.addElement(child1);
        XmlElement child2 = new XmlElement("child2");
        parent.insertElement(child2, 0);
        List<XmlElement> result = parent.getElements();
        assertEquals(2, result.size());
        assertEquals(child2, result.get(0));
        assertEquals(child1, result.get(1));
    }
    
    @Test
    public void testGetElements() {
        XmlElement parent = new XmlElement();
        XmlElement child1 = new XmlElement("child1");
        XmlElement child2 = new XmlElement("child2");
        parent.addElement(child1);
        parent.addElement(child2);
        List<XmlElement> result = parent.getElements();
        List<XmlElement> expected = Arrays.asList(child1, child2);
        assertEquals(expected, result);
    }
    
    @Test
    public void testCount() {
        XmlElement parent = new XmlElement();
        XmlElement child1 = new XmlElement("child1");
        XmlElement child2 = new XmlElement("child2");
        parent.addElement(child1);
        parent.addElement(child2);
        int count = parent.count();
        assertEquals(2, count);
    }
    
    @Test
    public void testGetElementByPath() {
        XmlElement parent = new XmlElement();
        XmlElement child1 = new XmlElement("child1");
        XmlElement child11 = new XmlElement("child11");
        XmlElement child12 = new XmlElement("child12");
        parent.addElement(child1);
        child1.addElement(child11);
        child1.addElement(child12);
        XmlElement result = parent.getElement("child1.child11");
        assertEquals(child11, result);
    }
    
    @Test
    public void testGetElementByIndex() {
        XmlElement parent = new XmlElement();
        XmlElement child1 = new XmlElement("child1");
        parent.addElement(child1);
        XmlElement result = parent.getElement(0);
        assertEquals(child1, result);
    }
    
    @Test
    public void testAddSubElementByPath() {
        XmlElement parent = new XmlElement();
        XmlElement child = parent.addSubElement("child1.child2");
        assertEquals("child2", child.getName());
        assertEquals(parent, child.getParent());
        assertEquals(1, parent.count());
    }
    
    @Test
    public void testAddSubElementByObject() {
        XmlElement parent = new XmlElement();
        XmlElement child = new XmlElement("child");
        parent.addSubElement(child);
        assertEquals(parent, child.getParent());
        assertEquals(1, parent.count());
    }
    
    @Test
    public void testAddSubElementByNameAndData() {
        XmlElement parent = new XmlElement();
        XmlElement child = parent.addSubElement("child", "data");
        assertEquals("child", child.getName());
        assertEquals("data", child.getData());
        assertEquals(parent, child.getParent());
        assertEquals(1, parent.count());
    }
    
    @Test
    public void testSetParent() {
        XmlElement parent = new XmlElement();
        XmlElement child = new XmlElement("child");
        child.setParent(parent);
        assertEquals(parent, child.getParent());
    }
    
    @Test
    public void testGetData() {
        XmlElement element = new XmlElement();
        element.setData("data");
        String result = element.getData();
        assertEquals("data", result);
    }
    
    @Test
    public void testGetName() {
        XmlElement element = new XmlElement("name");
        String result = element.getName();
        assertEquals("name", result);
    }
    
    @Test
    public void testPrintNode() {
        XmlElement element = new XmlElement("name", "data");
        element.addAttribute("attribute1", "value1");
        element.addAttribute("attribute2", "value2");
        XmlElement child = new XmlElement("child");
        element.addElement(child);
        
        System.out.println("###Printing XML structure###");
        printNode(element, "");
        System.out.println("###End of XML structure###");
    }
    
    @Test
    public void testClone() {
        XmlElement element = new XmlElement("name", "data");
        element.addAttribute("attribute1", "value1");
        element.addAttribute("attribute2", "value2");
        XmlElement child = new XmlElement("child");
        element.addElement(child);
        
        XmlElement clone = (XmlElement) element.clone();
        assertEquals(element, clone);
    }
    
    @Test
    public void testEquals() {
        XmlElement element1 = new XmlElement("name", "data");
        element1.addAttribute("attribute1", "value1");
        
        XmlElement element2 = new XmlElement("name", "data");
        element2.addAttribute("attribute1", "value1");
        
        assertTrue(element1.equals(element2));
    }
    
    @Test
    public void testHashCode() {
        XmlElement element = new XmlElement("name", "data");
        element.addAttribute("attribute1", "value1");
        
        int hashCode = element.hashCode();
        
        assertEquals(hashCode, element.hashCode());
    }
}