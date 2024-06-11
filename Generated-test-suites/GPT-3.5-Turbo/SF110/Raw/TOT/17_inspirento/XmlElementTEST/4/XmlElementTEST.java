import org.junit.Test;
import static org.junit.Assert.*;
import java.util.*;

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
    public void testGetAttributeDefaultValue() {
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
        Hashtable<String, String> attrs = new Hashtable<>();
        attrs.put("name", "value");
        element.setAttributes(attrs);
        assertEquals(attrs, element.getAttributes());
    }
    
    @Test
    public void testGetAttributeNames() {
        XmlElement element = new XmlElement();
        element.addAttribute("name1", "value1");
        element.addAttribute("name2", "value2");
        Enumeration names = element.getAttributeNames();
        assertTrue(names.hasMoreElements());
        assertEquals("name1", names.nextElement());
        assertEquals("name2", names.nextElement());
        assertFalse(names.hasMoreElements());
    }
    
    @Test
    public void testAddElement() {
        XmlElement element1 = new XmlElement("element1");
        XmlElement element2 = new XmlElement("element2");
        assertTrue(element1.addElement(element2));
    }
    
    @Test
    public void testRemoveElementByElement() {
        XmlElement element1 = new XmlElement("element1");
        XmlElement element2 = new XmlElement("element2");
        element1.addElement(element2);
        assertEquals(element2, element1.removeElement(element2));
        assertEquals(0, element1.count());
    }
    
    @Test
    public void testRemoveElementByIndex() {
        XmlElement element1 = new XmlElement("element1");
        XmlElement element2 = new XmlElement("element2");
        element1.addElement(element2);
        assertEquals(element2, element1.removeElement(0));
        assertEquals(0, element1.count());
    }
    
    @Test
    public void testRemoveAllElements() {
        XmlElement element1 = new XmlElement("element1");
        XmlElement element2 = new XmlElement("element2");
        element1.addElement(element2);
        element1.removeAllElements();
        assertEquals(0, element1.count());
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
        XmlElement parent = new XmlElement("parent");
        XmlElement child = new XmlElement("child");
        child.append(parent);
        assertEquals(1, parent.count());
        assertEquals(parent, child.getParent());
    }
    
    @Test
    public void testInsertElement() {
        XmlElement parent = new XmlElement("parent");
        XmlElement child1 = new XmlElement("child1");
        parent.addElement(child1);
        XmlElement child2 = new XmlElement("child2");
        parent.insertElement(child2, 0);
        assertEquals(2, parent.count());
        assertEquals(child2, parent.getElement(0));
        assertEquals(child1, parent.getElement(1));
        assertEquals(parent, child2.getParent());
    }
    
    @Test
    public void testGetElements() {
        XmlElement parent = new XmlElement("parent");
        XmlElement child1 = new XmlElement("child1");
        parent.addElement(child1);
        List<XmlElement> elements = parent.getElements();
        assertEquals(1, elements.size());
        assertTrue(elements.contains(child1));
    }
    
    @Test
    public void testCount() {
        XmlElement parent = new XmlElement("parent");
        parent.addElement(new XmlElement("child1"));
        parent.addElement(new XmlElement("child2"));
        assertEquals(2, parent.count());
    }
    
    @Test
    public void testGetElement() {
        XmlElement parent = new XmlElement("parent");
        parent.addElement(new XmlElement("child1"));
        parent.addElement(new XmlElement("child2"));
        assertEquals(parent.getElement("child1"), parent.getElement(0));
    }
    
    @Test
    public void testAddSubElementWithPath() {
        XmlElement parent = new XmlElement("parent");
        XmlElement child1 = parent.addSubElement("child1");
        assertEquals(1, parent.count());
        assertEquals(child1, parent.getElement(0));
    }
    
    @Test
    public void testAddSubElementWithElement() {
        XmlElement parent = new XmlElement("parent");
        XmlElement child = new XmlElement("child");
        parent.addSubElement(child);
        assertEquals(1, parent.count());
        assertEquals(child, parent.getElement(0));
        assertEquals(parent, child.getParent());
    }
    
    @Test
    public void testAddSubElementWithNameAndData() {
        XmlElement parent = new XmlElement("parent");
        XmlElement child = parent.addSubElement("child", "data");
        assertEquals(1, parent.count());
        assertEquals(child, parent.getElement(0));
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
        XmlElement element = new XmlElement("element", "data");
        assertEquals("data", element.getData());
    }
    
    @Test
    public void testSetName() {
        XmlElement element = new XmlElement();
        element.setName("name");
        assertEquals("name", element.getName());
    }
    
    @Test
    public void testPrintNode() {
        XmlElement element = new XmlElement("element", "data");
        element.addAttribute("attr1", "value1");
        element.addAttribute("attr2", "value2");
        XmlElement child = new XmlElement("child");
        element.addElement(child);
        assertEquals("element = 'data'\nattr1:value1\nattr2:value2\n    child\n", element.printNode(element, ""));
    }
    
    @Test
    public void testClone() {
        XmlElement element1 = new XmlElement("element1");
        element1.addAttribute("attr1", "value1");
        XmlElement element2 = new XmlElement("element2");
        element1.addElement(element2);
        
        XmlElement clone = element1.clone();
        assertEquals(element1, clone);
        assertNotSame(element1, clone);
        assertEquals(element1.getAttributes(), clone.getAttributes());
        assertNotSame(element1.getAttributes(), clone.getAttributes());
        assertEquals(element1.getElements().size(), clone.getElements().size());
        assertNotSame(element1.getElements(), clone.getElements());
        assertEquals(element2, clone.getElement(0));
        assertNotSame(element2, clone.getElement(0));
        assertEquals(element1, element2.getParent());
        assertEquals(clone, clone.getElement(0).getParent());
    }
    
    @Test
    public void testEquals() {
        XmlElement element1 = new XmlElement("element");
        element1.addAttribute("attr", "value");
        element1.setData("data");
        XmlElement element2 = new XmlElement("element");
        element2.addAttribute("attr", "value");
        element2.setData("data");
        assertEquals(true, element1.equals(element2));
    }
    
    @Test
    public void testHashCode() {
        XmlElement element = new XmlElement("element");
        element.addAttribute("attr", "value");
        element.setData("data");
        int expectedHashCode = 23 + element.getAttributes().hashCode() * 13 + element.getData().hashCode() * 17 + element.getName().hashCode() * 29 + element.getElements().hashCode() * 57;
        assertEquals(expectedHashCode, element.hashCode());
    }
}