import org.junit.Before;
import org.junit.Test;
import static org.junit.Assert.*;

import java.util.*;

public class XmlElementTest {

    private XmlElement xmlElement;
    private XmlElement parentElement;
    
    @Before
    public void setup() {
        xmlElement = new XmlElement();
        parentElement = new XmlElement("parent");
    }
    
    @Test
    public void testAddAttribute() {
        xmlElement.addAttribute("name1", "value1");
        xmlElement.addAttribute("name2", "value2");
        assertEquals(xmlElement.getAttribute("name1"), "value1");
        assertEquals(xmlElement.getAttribute("name2"), "value2");
    }
    
    @Test
    public void testGetAttribute() {
        xmlElement.addAttribute("name1", "value1");
        xmlElement.addAttribute("name2", "value2");
        assertEquals(xmlElement.getAttribute("name1"), "value1");
        assertEquals(xmlElement.getAttribute("name2"), "value2");
        
        assertEquals(xmlElement.getAttribute("name3"), null);
        assertEquals(xmlElement.getAttribute("name3", "default"), "default");
    }
    
    @Test
    public void testGetAttributes() {
        xmlElement.addAttribute("name1", "value1");
        xmlElement.addAttribute("name2", "value2");
        
        Hashtable<String, String> attributes = new Hashtable<String, String>();
        attributes.put("name1", "value1");
        attributes.put("name2", "value2");
        
        assertEquals(xmlElement.getAttributes(), attributes);
    }
    
    @Test
    public void testSetAttributes() {
        Hashtable<String, String> attributes = new Hashtable<String, String>();
        attributes.put("name1", "value1");
        attributes.put("name2", "value2");
        
        xmlElement.setAttributes(attributes);
        
        assertEquals(xmlElement.getAttributes(), attributes);
    }
    
    @Test
    public void testGetAttributeNames() {
        xmlElement.addAttribute("name1", "value1");
        xmlElement.addAttribute("name2", "value2");
        
        Enumeration<String> attributeNames = xmlElement.getAttributeNames();
        HashSet<String> expectedNames = new HashSet<String>();
        expectedNames.add("name1");
        expectedNames.add("name2");
        
        while(attributeNames.hasMoreElements()) {
            String attributeName = attributeNames.nextElement();
            assertTrue(expectedNames.contains(attributeName));
        }
    }
    
    @Test
    public void testAddElement() {
        XmlElement element = new XmlElement("element");
        
        assertTrue(xmlElement.addElement(element));
        assertTrue(xmlElement.getElements().contains(element));
        
        assertEquals(element.getParent(), xmlElement);
    }
    
    @Test
    public void testRemoveElementByObject() {
        XmlElement element1 = new XmlElement("element1");
        XmlElement element2 = new XmlElement("element2");
        
        xmlElement.addElement(element1);
        xmlElement.addElement(element2);
        
        assertEquals(xmlElement.removeElement(element1), element1);
        assertFalse(xmlElement.getElements().contains(element1));
    }
    
    @Test
    public void testRemoveElementByIndex() {
        XmlElement element1 = new XmlElement("element1");
        XmlElement element2 = new XmlElement("element2");
        
        xmlElement.addElement(element1);
        xmlElement.addElement(element2);
        
        assertEquals(xmlElement.removeElement(0), element1);
        assertFalse(xmlElement.getElements().contains(element1));
    }
    
    @Test
    public void testRemoveAllElements() {
        XmlElement element1 = new XmlElement("element1");
        XmlElement element2 = new XmlElement("element2");
        
        xmlElement.addElement(element1);
        xmlElement.addElement(element2);
        
        xmlElement.removeAllElements();
        
        assertTrue(xmlElement.getElements().isEmpty());
    }
    
    @Test
    public void testRemoveFromParent() {
        XmlElement element = new XmlElement("element");
        
        xmlElement.addElement(element);
        
        element.removeFromParent();
        
        assertFalse(xmlElement.getElements().contains(element));
        assertNull(element.getParent());
    }
    
    @Test
    public void testAppend() {
        XmlElement element = new XmlElement("element");
        
        element.appendToParent(xmlElement);
        
        assertTrue(xmlElement.getElements().contains(element));
        assertEquals(element.getParent(), xmlElement);
    }
    
    @Test
    public void testInsertElement() {
        XmlElement element1 = new XmlElement("element1");
        XmlElement element2 = new XmlElement("element2");
        
        xmlElement.addElement(element1);
        xmlElement.addElement(element2);
        
        XmlElement newElement = new XmlElement("newElement");
        xmlElement.insertElement(newElement, 1);
        
        assertEquals(xmlElement.getElement(1), newElement);
    }
    
    @Test
    public void testGetElements() {
        XmlElement element1 = new XmlElement("element1");
        XmlElement element2 = new XmlElement("element2");
        
        xmlElement.addElement(element1);
        xmlElement.addElement(element2);
        
        List<XmlElement> elements = new ArrayList<XmlElement>();
        elements.add(element1);
        elements.add(element2);
        
        assertEquals(xmlElement.getElements(), elements);
    }
    
    @Test
    public void testCount() {
        XmlElement element1 = new XmlElement("element1");
        XmlElement element2 = new XmlElement("element2");
        
        xmlElement.addElement(element1);
        xmlElement.addElement(element2);
        
        assertEquals(xmlElement.count(), 2);
    }
    
    @Test
    public void testGetElementByName() {
        XmlElement element1 = new XmlElement("element1");
        XmlElement element2 = new XmlElement("element2");
        
        xmlElement.addElement(element1);
        xmlElement.addElement(element2);
        
        assertEquals(xmlElement.getElement("element1"), element1);
        assertEquals(xmlElement.getElement("element2"), element2);
        assertNull(xmlElement.getElement("element3"));
    }
    
    @Test
    public void testGetElementByIndex() {
        XmlElement element1 = new XmlElement("element1");
        XmlElement element2 = new XmlElement("element2");
        
        xmlElement.addElement(element1);
        xmlElement.addElement(element2);
        
        assertEquals(xmlElement.getElement(0), element1);
        assertEquals(xmlElement.getElement(1), element2);
    }
    
    @Test
    public void testAddSubElementWithPath() {
        xmlElement.addSubElement("parent.element1.element2");
        
        XmlElement parent = xmlElement.getElement("parent");
        XmlElement child1 = parent.getElement("element1");
        XmlElement child2 = child1.getElement("element2");
        
        assertNotNull(parent);
        assertNotNull(child1);
        assertNotNull(child2);
        
        assertEquals(child2.getParent(), child1);
    }
    
    @Test
    public void testAddSubElementWithXmlElement() {
        XmlElement subElement = new XmlElement("subElement");
        
        xmlElement.addSubElement(subElement);
        
        assertTrue(xmlElement.getElements().contains(subElement));
        assertEquals(subElement.getParent(), xmlElement);
    }
    
    @Test
    public void testAddSubElementWithNameAndData() {
        String name = "element";
        String data = "data";
        
        XmlElement subElement = xmlElement.addSubElement(name, data);
        
        assertTrue(xmlElement.getElements().contains(subElement));
        assertEquals(subElement.getParent(), xmlElement);
        assertEquals(subElement.getName(), name);
        assertEquals(subElement.getData(), data);
    }
    
    @Test
    public void testSetParent() {
        xmlElement.setParent(parentElement);
        
        assertEquals(xmlElement.getParent(), parentElement);
    }
    
    @Test
    public void testSetData() {
        String data = "data";
        
        xmlElement.setData(data);
        
        assertEquals(xmlElement.getData(), data);
    }
    
    @Test
    public void testGetName() {
        String name = "element";
        
        xmlElement.setName(name);
        
        assertEquals(xmlElement.getName(), name);
    }
    
    @Test
    public void testPrintNode() {
        XmlElement element1 = new XmlElement("element1");
        XmlElement element2 = new XmlElement("element2");
        
        xmlElement.addElement(element1);
        xmlElement.addElement(element2);
        
        xmlElement.printNode(xmlElement, "");
        
        // PrintNode is a void method, so can't test the output.
    }
    
    @Test
    public void testClone() throws CloneNotSupportedException {
        xmlElement.addAttribute("name", "value");
        xmlElement.setData("data");
        
        XmlElement cloneElement = (XmlElement) xmlElement.clone();
        
        assertEquals(xmlElement, cloneElement);
        assertNotSame(xmlElement.getAttributes(), cloneElement.getAttributes());
        assertNotSame(xmlElement.getElements(), cloneElement.getElements());
        assertNotSame(xmlElement.getElements().get(0), cloneElement.getElements().get(0));
        assertNotSame(xmlElement.getElements().get(1), cloneElement.getElements().get(1));
    }
    
    @Test
    public void testNotifyObservers() {
        xmlElement.notifyObservers();
        
        assertTrue(xmlElement.hasChanged());
    }
    
    @Test
    public void testEquals() {
        XmlElement element1 = new XmlElement("element1");
        XmlElement element2 = new XmlElement("element2");
        
        element1.addAttribute("name", "value");
        element1.setData("data");
        
        element2.addAttribute("name", "value");
        element2.setData("data");
        
        assertTrue(element1.equals(element2));
        
        element2.addAttribute("name", "new value");
        
        assertFalse(element1.equals(element2));
        
        assertFalse(element1.equals(new Object()));
    }
    
    @Test
    public void testHashCode() {
        XmlElement element1 = new XmlElement("element1");
        XmlElement element2 = new XmlElement("element2");
        
        element1.addAttribute("name", "value");
        element1.setData("data");
        
        element2.addAttribute("name", "value");
        element2.setData("data");
        
        assertEquals(element1.hashCode(), element2.hashCode());

        element2.addAttribute("name", "new value");
        
        assertNotEquals(element1.hashCode(), element2.hashCode());
    }
}