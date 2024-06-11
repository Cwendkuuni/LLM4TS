
import org.junit.Test;
import org.junit.Assert;
import java.util.Hashtable;
import java.util.List;
import java.util.Enumeration;

public class XmlElementTest {

    @Test
    public void testAddAttribute() {
        XmlElement element = new XmlElement();
        Object returnValue = element.addAttribute("name", "value");
        Assert.assertNull(returnValue);
        Assert.assertEquals("value", element.getAttribute("name"));
    }
    
    @Test
    public void testGetAttribute() {
        XmlElement element = new XmlElement();
        element.addAttribute("name", "value");
        String attribute = element.getAttribute("name");
        Assert.assertEquals("value", attribute);
    }
    
    @Test
    public void testGetAttributeWithDefaultValue() {
        XmlElement element = new XmlElement();
        String defaultValue = "defaultValue";
        String attribute = element.getAttribute("name", defaultValue);
        Assert.assertEquals(defaultValue, attribute);
    }
    
    @Test
    public void testGetAttributes() {
        XmlElement element = new XmlElement();
        Hashtable<String, String> attributes = new Hashtable<>();
        attributes.put("name1", "value1");
        attributes.put("name2", "value2");
        element.setAttributes(attributes);
        Hashtable<String, String> retrievedAttributes = element.getAttributes();
        Assert.assertEquals(attributes, retrievedAttributes);
    }
    
    @Test
    public void testGetAttributeNames() {
        XmlElement element = new XmlElement();
        element.addAttribute("name1", "value1");
        element.addAttribute("name2", "value2");
        Enumeration attributeNames = element.getAttributeNames();
        Assert.assertTrue(attributeNames.hasMoreElements());
    }
    
    @Test
    public void testAddElement() {
        XmlElement parent = new XmlElement("parent");
        XmlElement child = new XmlElement("child");
        boolean result = parent.addElement(child);
        Assert.assertTrue(result);
        List<XmlElement> elements = parent.getElements();
        Assert.assertTrue(elements.contains(child));
    }
    
    @Test
    public void testRemoveElementByObject() {
        XmlElement parent = new XmlElement("parent");
        XmlElement child = new XmlElement("child");
        parent.addElement(child);
        XmlElement removedElement = parent.removeElement(child);
        Assert.assertEquals(child, removedElement);
        List<XmlElement> elements = parent.getElements();
        Assert.assertFalse(elements.contains(child));
    }
    
    @Test
    public void testRemoveElementByIndex() {
        XmlElement parent = new XmlElement("parent");
        XmlElement child = new XmlElement("child");
        parent.addElement(child);
        XmlElement removedElement = parent.removeElement(0);
        Assert.assertEquals(child, removedElement);
        List<XmlElement> elements = parent.getElements();
        Assert.assertFalse(elements.contains(child));
    }
    
    @Test
    public void testRemoveAllElements() {
        XmlElement parent = new XmlElement("parent");
        XmlElement child1 = new XmlElement("child1");
        XmlElement child2 = new XmlElement("child2");
        parent.addElement(child1);
        parent.addElement(child2);
        parent.removeAllElements();
        List<XmlElement> elements = parent.getElements();
        Assert.assertTrue(elements.isEmpty());
    }
    
    @Test
    public void testRemoveFromParent() {
        XmlElement parent = new XmlElement("parent");
        XmlElement child = new XmlElement("child");
        parent.addElement(child);
        child.removeFromParent();
        List<XmlElement> elements = parent.getElements();
        Assert.assertFalse(elements.contains(child));
        Assert.assertNull(child.getParent());
    }
    
    @Test
    public void testAppend() {
        XmlElement parent = new XmlElement("parent");
        XmlElement child = new XmlElement("child");
        child.setParent(null);
        parent.append(child);
        List<XmlElement> elements = parent.getElements();
        Assert.assertTrue(elements.contains(child));
        Assert.assertEquals(parent, child.getParent());
    }
    
    @Test
    public void testInsertElement() {
        XmlElement parent = new XmlElement("parent");
        XmlElement child1 = new XmlElement("child1");
        XmlElement child2 = new XmlElement("child2");
        parent.addElement(child1);
        parent.insertElement(child2, 0);
        List<XmlElement> elements = parent.getElements();
        Assert.assertEquals(child2, elements.get(0));
        Assert.assertEquals(parent, child2.getParent());
    }
    
    @Test
    public void testGetElements() {
        XmlElement parent = new XmlElement("parent");
        XmlElement child1 = new XmlElement("child1");
        XmlElement child2 = new XmlElement("child2");
        parent.addElement(child1);
        parent.addElement(child2);
        List<XmlElement> elements = parent.getElements();
        Assert.assertEquals(2, elements.size());
        Assert.assertEquals(child1, elements.get(0));
        Assert.assertEquals(child2, elements.get(1));
    }
    
    @Test
    public void testCount() {
        XmlElement parent = new XmlElement("parent");
        XmlElement child = new XmlElement("child");
        parent.addElement(child);
        int count = parent.count();
        Assert.assertEquals(1, count);
    }
    
    @Test
    public void testGetElement() {
        XmlElement parent = new XmlElement("parent");
        XmlElement child = new XmlElement("child");
        parent.addElement(child);
        XmlElement retrievedElement = parent.getElement("child");
        Assert.assertEquals(child, retrievedElement);
    }
    
    @Test
    public void testGetElementWithSubElements() {
        XmlElement parent = new XmlElement("parent");
        XmlElement child1 = new XmlElement("child1");
        XmlElement child2 = new XmlElement("child2");
        parent.addElement(child1);
        parent.addElement(child2);
        XmlElement retrievedElement = parent.getElement("child2");
        Assert.assertEquals(child2, retrievedElement);
    }
    
    @Test
    public void testGetElementWithInvalidPath() {
        XmlElement parent = new XmlElement("parent");
        XmlElement child = new XmlElement("child");
        parent.addElement(child);
        XmlElement retrievedElement = parent.getElement("invalidPath");
        Assert.assertNull(retrievedElement);
    }
    
    @Test
    public void testGetElementByIndex() {
        XmlElement parent = new XmlElement("parent");
        XmlElement child = new XmlElement("child");
        parent.addElement(child);
        XmlElement retrievedElement = parent.getElement(0);
        Assert.assertEquals(child, retrievedElement);
    }
    
    @Test
    public void testAddSubElementWithPath() {
        XmlElement parent = new XmlElement("parent");
        XmlElement child = parent.addSubElement("child1.child2.child3");
        Assert.assertEquals("child3", child.getName());
        Assert.assertEquals(parent, child.getParent());
    }
    
    @Test
    public void testAddSubElementByObject() {
        XmlElement parent = new XmlElement("parent");
        XmlElement child = new XmlElement("child");
        XmlElement addedElement = parent.addSubElement(child);
        Assert.assertEquals(child, addedElement);
        Assert.assertEquals(parent, child.getParent());
    }
    
    @Test
    public void testAddSubElementByNameAndData() {
        XmlElement parent = new XmlElement("parent");
        String name = "child";
        String data = "data";
        XmlElement child = parent.addSubElement(name, data);
        Assert.assertEquals(name, child.getName());
        Assert.assertEquals(data, child.getData());
        Assert.assertEquals(parent, child.getParent());
    }
    
    @Test
    public void testSetParent() {
        XmlElement parent1 = new XmlElement("parent1");
        XmlElement parent2 = new XmlElement("parent2");
        XmlElement child = new XmlElement("child");
        child.setParent(parent1);
        child.setParent(parent2);
        Assert.assertEquals(parent2, child.getParent());
    }
    
    @Test
    public void testGetData() {
        XmlElement element = new XmlElement("element", "data");
        String data = element.getData();
        Assert.assertEquals("data", data);
    }
    
    @Test
    public void testGetName() {
        XmlElement element = new XmlElement("element");
        String name = element.getName();
        Assert.assertEquals("element", name);
    }
    
    @Test
    public void testPrintNode() {
        XmlElement root = new XmlElement("root");
        XmlElement child = new XmlElement("child");
        root.addElement(child);
        String indent = "";
        XmlElement.printNode(root, indent);
    }
    
    @Test
    public void testClone() {
        XmlElement element1 = new XmlElement("element");
        XmlElement element2 = (XmlElement) element1.clone();
        Assert.assertEquals(element1, element2);
    }
    
    @Test
    public void testNotifyObservers() {
        XmlElement element = new XmlElement();
        element.notifyObservers();
        Assert.assertTrue(element.hasChanged());
    }
    
    @Test
    public void testEquals() {
        XmlElement element1 = new XmlElement("element");
        XmlElement element2 = new XmlElement("element");
        Assert.assertTrue(element1.equals(element2));
    }
    
    @Test
    public void testHashCode() {
        XmlElement element1 = new XmlElement("element");
        XmlElement element2 = new XmlElement("element");
        int hashCode1 = element1.hashCode();
        int hashCode2 = element2.hashCode();
        Assert.assertEquals(hashCode1, hashCode2);
    }
}

