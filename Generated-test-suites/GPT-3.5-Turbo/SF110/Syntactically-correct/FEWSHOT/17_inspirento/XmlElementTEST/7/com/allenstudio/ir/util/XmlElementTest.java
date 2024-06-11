import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNull;

import org.junit.Test;

public class XmlElementTest {
    
    @Test
    public void testAddAttribute() {
        XmlElement element = new XmlElement();
        assertEquals(null, element.addAttribute(null, null));
        assertEquals(null, element.addAttribute("name", null));
        assertEquals(null, element.addAttribute(null, "value"));
        assertEquals("previousValue", element.addAttribute("name", "value"));
    }
    
    @Test
    public void testGetAttribute() {
        XmlElement element = new XmlElement();
        element.addAttribute("name", "value");
        assertEquals("value", element.getAttribute("name"));
        assertEquals("defaultValue", element.getAttribute("unknown", "defaultValue"));
    }
    
    @Test
    public void testGetAttributes() {
        XmlElement element = new XmlElement();
        assertEquals(0, element.getAttributes().size());
        element.addAttribute("name1", "value1");
        element.addAttribute("name2", "value2");
        assertEquals(2, element.getAttributes().size());
    }
    
    @Test
    public void testSetAttributes() {
        XmlElement element = new XmlElement();
        Hashtable<String, String> attributes = new Hashtable<>();
        attributes.put("name1", "value1");
        attributes.put("name2", "value2");
        element.setAttributes(attributes);
        assertEquals(attributes, element.getAttributes());
    }
    
    @Test
    public void testGetAttributeNames() {
        XmlElement element = new XmlElement();
        element.addAttribute("name1", "value1");
        element.addAttribute("name2", "value2");
        Enumeration<String> names = element.getAttributeNames();
        assertEquals("name1", names.nextElement());
        assertEquals("name2", names.nextElement());
    }
    
    @Test
    public void testAddElement() {
        XmlElement element = new XmlElement();
        XmlElement child = new XmlElement("child");
        assertEquals(true, element.addElement(child));
    }
    
    @Test
    public void testRemoveElement() {
        XmlElement parent = new XmlElement();
        XmlElement child = new XmlElement("child");
        parent.addElement(child);
        assertEquals(child, parent.removeElement(child));
    }
    
    @Test
    public void testRemoveElementByIndex() {
        XmlElement parent = new XmlElement();
        XmlElement child = new XmlElement("child");
        parent.addElement(child);
        assertEquals(child, parent.removeElement(0));
    }
    
    @Test
    public void testRemoveAllElements() {
        XmlElement parent = new XmlElement();
        XmlElement child1 = new XmlElement("child1");
        XmlElement child2 = new XmlElement("child2");
        parent.addElement(child1);
        parent.addElement(child2);
        parent.removeAllElements();
        assertEquals(0, parent.count());
    }
    
    @Test
    public void testRemoveFromParent() {
        XmlElement parent = new XmlElement();
        XmlElement child = new XmlElement("child");
        parent.addElement(child);
        child.removeFromParent();
        assertEquals(0, parent.count());
    }
    
    @Test
    public void testAppend() {
        XmlElement parent1 = new XmlElement();
        XmlElement parent2 = new XmlElement();
        XmlElement child1 = new XmlElement("child1");
        XmlElement child2 = new XmlElement("child2");
        parent1.addElement(child1);
        parent2.addElement(child2);
        child2.append(child1);
        assertEquals(1, parent2.count());
    }
    
    @Test
    public void testInsertElement() {
        XmlElement parent = new XmlElement();
        XmlElement child1 = new XmlElement("child1");
        XmlElement child2 = new XmlElement("child2");
        parent.addElement(child1);
        parent.insertElement(child2, 0);
        assertEquals(child2, parent.getElement(0));
    }
    
    @Test
    public void testGetElements() {
        XmlElement parent = new XmlElement();
        XmlElement child1 = new XmlElement("child1");
        XmlElement child2 = new XmlElement("child2");
        parent.addElement(child1);
        parent.addElement(child2);
        assertEquals(2, parent.getElements().size());
    }
    
    @Test
    public void testCount() {
        XmlElement parent = new XmlElement();
        XmlElement child1 = new XmlElement("child1");
        XmlElement child2 = new XmlElement("child2");
        parent.addElement(child1);
        parent.addElement(child2);
        assertEquals(2, parent.count());
    }
    
    @Test
    public void testGetElementByPath() {
        XmlElement parent = new XmlElement();
        XmlElement child1 = new XmlElement("child1");
        XmlElement child2 = new XmlElement("child2");
        parent.addElement(child1);
        parent.addElement(child2);
        assertEquals(child1, parent.getElement("child1"));
    }
    
    @Test
    public void testGetElementByIndex() {
        XmlElement parent = new XmlElement();
        XmlElement child1 = new XmlElement("child1");
        XmlElement child2 = new XmlElement("child2");
        parent.addElement(child1);
        parent.addElement(child2);
        assertEquals(child1, parent.getElement(0));
    }
    
    @Test
    public void testAddSubElementByPath() {
        XmlElement parent = new XmlElement();
        parent.addSubElement("child1");
        assertEquals(1, parent.count());
    }
    
    @Test
    public void testAddSubElementByElement() {
        XmlElement parent = new XmlElement();
        XmlElement child = new XmlElement("child");
        parent.addSubElement(child);
        assertEquals(1, parent.count());
    }
    
    @Test
    public void testAddSubElementByNameAndData() {
        XmlElement parent = new XmlElement();
        parent.addSubElement("child", "data");
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
        XmlElement element = new XmlElement("name", "data");
        assertEquals("data", element.getData());
    }
    
    @Test
    public void testGetName() {
        XmlElement element = new XmlElement("name");
        assertEquals("name", element.getName());
    }
    
    @Test
    public void testClone() {
        XmlElement parent = new XmlElement();
        XmlElement child = new XmlElement("child");
        parent.addElement(child);
        XmlElement clone = (XmlElement) parent.clone();
        assertEquals(parent, clone);
    }
    
    @Test
    public void testNotifyObservers() {
        XmlElement element = new XmlElement();
        element.addObserver(new Observer());
        element.notifyObservers();
        assertEquals(true, element.hasChanged());
    }
    
    @Test
    public void testEquals() {
        XmlElement element1 = new XmlElement();
        element1.addAttribute("name", "value");
        XmlElement element2 = new XmlElement();
        element2.addAttribute("name", "value");
        assertEquals(true, element1.equals(element2));
    }
    
    @Test
    public void testHashCode() {
        XmlElement element = new XmlElement();
        assertEquals(23, element.hashCode());
    }
}