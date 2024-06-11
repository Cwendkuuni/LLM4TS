
import org.junit.Test;

import java.util.Enumeration;
import java.util.Hashtable;
import java.util.List;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNull;

public class XmlElementTest {

    @Test
    public void testAddAttribute() {
        XmlElement element = new XmlElement();
        assertEquals(null, element.addAttribute(null, "value"));
        assertEquals("value", element.addAttribute("name", "value"));
    }

    @Test
    public void testGetAttribute() {
        XmlElement element = new XmlElement();
        element.addAttribute("name", "value");
        assertEquals("value", element.getAttribute("name"));
        assertNull(element.getAttribute("nonexistent"));
    }

    @Test
    public void testGetAttributeWithDefaultValue(){
        XmlElement element = new XmlElement();
        assertEquals("default", element.getAttribute("name", "default"));
        element.addAttribute("name", "value");
        assertEquals("value", element.getAttribute("name", "default"));
    }

    @Test
    public void testGetAttributes(){
        XmlElement element = new XmlElement();
        Hashtable<String, String> attributes = new Hashtable<>();
        attributes.put("name1", "value1");
        attributes.put("name2", "value2");
        element.setAttributes(attributes);
        assertEquals(attributes, element.getAttributes());
    }

    @Test
    public void testGetAttributeNames(){
        XmlElement element = new XmlElement();
        element.addAttribute("name1", "value1");
        element.addAttribute("name2", "value2");
        Enumeration attributeNames = element.getAttributeNames();
        assertEquals("name1", attributeNames.nextElement());
        assertEquals("name2", attributeNames.nextElement());
    }

    @Test
    public void testAddElement(){
        XmlElement parent = new XmlElement();
        XmlElement child = new XmlElement("child");
        assertEquals(true, parent.addElement(child));
    }

    @Test
    public void testRemoveElement(){
        XmlElement parent = new XmlElement();
        XmlElement child = new XmlElement("child");
        parent.addElement(child);
        assertEquals(child, parent.removeElement(child));
    }

    @Test
    public void testRemoveElementByIndex(){
        XmlElement parent = new XmlElement();
        XmlElement child = new XmlElement("child");
        parent.addElement(child);
        assertEquals(child, parent.removeElement(0));
    }

    @Test
    public void testRemoveAllElements(){
        XmlElement parent = new XmlElement();
        XmlElement child1 = new XmlElement("child1");
        XmlElement child2 = new XmlElement("child2");
        parent.addElement(child1);
        parent.addElement(child2);
        parent.removeAllElements();
        assertEquals(0, parent.count());
    }

    @Test
    public void testRemoveFromParent(){
        XmlElement parent = new XmlElement("parent");
        XmlElement child = new XmlElement("child");
        parent.addElement(child);
        child.removeFromParent();
        assertEquals(0, parent.count());
        assertNull(child.getParent());
    }

    @Test
    public void testAppend(){
        XmlElement parent = new XmlElement("parent");
        XmlElement child = new XmlElement("child");
        child.removeFromParent();
        parent.append(child);
        assertEquals(child, parent.getElement(0));
    }

    @Test
    public void testInsertElement(){
        XmlElement parent = new XmlElement("parent");
        XmlElement child1 = new XmlElement("child1");
        XmlElement child2 = new XmlElement("child2");
        parent.addElement(child1);
        parent.insertElement(child2, 0);
        assertEquals(child2, parent.getElement(0));
    }

    @Test
    public void testGetElements(){
        XmlElement parent = new XmlElement("parent");
        XmlElement child1 = new XmlElement("child1");
        XmlElement child2 = new XmlElement("child2");
        parent.addElement(child1);
        parent.addElement(child2);
        List<XmlElement> elements = parent.getElements();
        assertEquals(2, elements.size());
        assertEquals(child1, elements.get(0));
        assertEquals(child2, elements.get(1));
    }

    @Test
    public void testCount(){
        XmlElement parent = new XmlElement("parent");
        XmlElement child1 = new XmlElement("child1");
        XmlElement child2 = new XmlElement("child2");
        parent.addElement(child1);
        parent.addElement(child2);
        assertEquals(2, parent.count());
    }

    @Test
    public void testGetElementByName(){
        XmlElement parent = new XmlElement("parent");
        XmlElement child1 = new XmlElement("child1");
        XmlElement child2 = new XmlElement("child2");
        parent.addElement(child1);
        parent.addElement(child2);
        assertEquals(child2, parent.getElement("child2"));
        assertNull(parent.getElement("nonexistent"));
    }

    @Test
    public void testGetElementByIndex(){
        XmlElement parent = new XmlElement("parent");
        XmlElement child1 = new XmlElement("child1");
        XmlElement child2 = new XmlElement("child2");
        parent.addElement(child1);
        parent.addElement(child2);
        assertEquals(child2, parent.getElement(1));
    }

    @Test
    public void testAddSubElementWithPath(){
        XmlElement parent = new XmlElement("parent");
        XmlElement child1 = parent.addSubElement("child1");
        XmlElement child2 = parent.addSubElement("child1.child2");
        assertEquals(child1, parent.getElement(0));
        assertEquals(child2, parent.getElement("child1.child2"));
    }

    @Test
    public void testAddSubElement(){
        XmlElement parent = new XmlElement("parent");
        XmlElement child = new XmlElement("child");
        assertEquals(child, parent.addSubElement(child));
    }

    @Test
    public void testAddSubElementWithNameAndData(){
        XmlElement parent = new XmlElement("parent");
        XmlElement child = parent.addSubElement("child", "data");
        assertEquals(child, parent.getElement(0));
        assertEquals("data", child.getData());
    }

    @Test
    public void testSetParent(){
        XmlElement parent = new XmlElement("parent");
        XmlElement child = new XmlElement("child");
        child.setParent(parent);
        assertEquals(parent, child.getParent());
    }

    @Test
    public void testSetData(){
        XmlElement element = new XmlElement("element");
        element.setData("data");
        assertEquals("data", element.getData());
    }

    @Test
    public void testGetName(){
        XmlElement element = new XmlElement("element");
        assertEquals("element", element.getName());
    }

    @Test
    public void testPrintNode(){
        XmlElement element = new XmlElement("element");
        element.addAttribute("name1", "value1");
        element.addAttribute("name2", "value2");
        XmlElement child = new XmlElement("child");
        element.addElement(child);
        XmlElement grandchild = new XmlElement("grandchild");
        child.addElement(grandchild);

        // No assertion as this is just for checking the log printout
        XmlElement.printNode(element, "");
    }

    @Test
    public void testClone(){
        XmlElement element = new XmlElement("element");
        XmlElement child = new XmlElement("child");
        element.addElement(child);

        XmlElement clone = (XmlElement) element.clone();
        assertEquals(element, clone);
    }

    @Test
    public void testNotifyObservers(){
        XmlElement element = new XmlElement("element");

        // No assertion as this is just to check if notifyObservers() throws any exceptions
        element.notifyObservers();
    }

    @Test
    public void testEquals(){
        XmlElement element1 = new XmlElement("element");
        XmlElement element2 = new XmlElement("element");
        assertEquals(true, element1.equals(element2));
    }

    @Test
    public void testHashCode(){
        XmlElement element = new XmlElement("element");
        assertEquals(-1767792316, element.hashCode());
    }
}

