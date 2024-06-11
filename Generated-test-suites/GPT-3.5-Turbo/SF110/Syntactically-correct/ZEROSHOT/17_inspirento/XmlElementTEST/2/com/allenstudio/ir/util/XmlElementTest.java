import com.allenstudio.ir.util.XmlElement;
import org.junit.Before;
import org.junit.Test;

import java.util.Hashtable;
import java.util.List;

import static org.junit.Assert.*;

public class XmlElementTest {

    private XmlElement xmlElement;

    @Before
    public void setup() {
        xmlElement = new XmlElement();
    }

    @Test
    public void testAddAttribute() {
        Object returnValue = xmlElement.addAttribute("name", "value");
        assertNull(returnValue);
    }

    @Test
    public void testGetAttribute() {
        xmlElement.addAttribute("name", "value");
        String attribute = xmlElement.getAttribute("name");
        assertEquals("value", attribute);
    }

    @Test
    public void testGetAttributeWithDefaultValue() {
        xmlElement.addAttribute("name", "value");
        String attribute = xmlElement.getAttribute("non-existing", "default");
        assertEquals("default", attribute);
    }

    @Test
    public void testGetAttributes() {
        Hashtable<String, String> attributes = xmlElement.getAttributes();
        assertNotNull(attributes);
    }

    @Test
    public void testSetAttributes() {
        Hashtable<String, String> attributes = new Hashtable<>();
        xmlElement.setAttributes(attributes);
        assertEquals(attributes, xmlElement.getAttributes());
    }

    @Test
    public void testGetAttributeNames() {
        xmlElement.addAttribute("name", "value");
        xmlElement.addAttribute("another", "attribute");
        assertEquals(2, xmlElement.getAttributeNames().size());
    }

    @Test
    public void testAddElement() {
        XmlElement childElement = new XmlElement("child");
        boolean added = xmlElement.addElement(childElement);
        assertTrue(added);
        assertEquals(1, xmlElement.count());
        assertEquals(xmlElement, childElement.getParent());
    }

    @Test
    public void testRemoveElementByObject() {
        XmlElement child = new XmlElement("child");
        xmlElement.addElement(child);
        XmlElement removed = xmlElement.removeElement(child);
        assertEquals(child, removed);
        assertEquals(0, xmlElement.count());
    }

    @Test
    public void testRemoveElementByIndex() {
        XmlElement child = new XmlElement("child");
        xmlElement.addElement(child);
        XmlElement removed = xmlElement.removeElement(0);
        assertEquals(child, removed);
        assertEquals(0, xmlElement.count());
    }

    @Test
    public void testRemoveAllElements() {
        XmlElement child1 = new XmlElement("child1");
        XmlElement child2 = new XmlElement("child2");
        xmlElement.addElement(child1);
        xmlElement.addElement(child2);
        xmlElement.removeAllElements();
        assertEquals(0, xmlElement.count());
    }

    @Test
    public void testRemoveFromParent() {
        XmlElement child = new XmlElement("child");
        
        XmlElement parent = new XmlElement("parent");
        parent.addElement(child);
        child.removeFromParent();

        assertNull(child.getParent());
        assertEquals(0, parent.count());
    }

    @Test
    public void testAppend() {
        XmlElement child = new XmlElement("child");
        
        XmlElement parent = new XmlElement("parent");
        parent.addElement(child);
        
        XmlElement newParent = new XmlElement("newParent");
        newParent.append(child);
        
        assertNull(parent.getParent());
        assertEquals(newParent, child.getParent());
    }

    @Test
    public void testInsertElement() {
        XmlElement child = new XmlElement("child");
        
        XmlElement parent = new XmlElement("parent");
        parent.insertElement(child, 0);
        
        assertEquals(child, parent.getElement(0));
        assertEquals(parent, child.getParent());
    }
    
    @Test
    public void testGetElements() {
        XmlElement child1 = new XmlElement("child1");
        XmlElement child2 = new XmlElement("child2");
        xmlElement.addElement(child1);
        xmlElement.addElement(child2);
        List<XmlElement> elements = xmlElement.getElements();
        assertEquals(2, elements.size());
        assertTrue(elements.contains(child1));
        assertTrue(elements.contains(child2));
    }

    @Test
    public void testCount() {
        XmlElement child1 = new XmlElement("child1");
        xmlElement.addElement(child1);
        assertEquals(1, xmlElement.count());
    }

    @Test
    public void testGetElementByPath() {
        XmlElement child1 = new XmlElement("child1");
        XmlElement child2 = new XmlElement();
        XmlElement child3 = new XmlElement("child3");
        xmlElement.addElement(child1);
        child1.addElement(child2);
        child2.addElement(child3);

        XmlElement element = xmlElement.getElement("child1.child3");
        assertEquals(child3, element);
    }

    @Test
    public void testGetElementByIndex() {
        XmlElement child1 = new XmlElement("child1");
        xmlElement.addElement(child1);
        XmlElement element = xmlElement.getElement(0);
        assertEquals(child1, element);
    }

    @Test
    public void testAddSubElementByString() {
        XmlElement child = xmlElement.addSubElement("child.child.child");
        assertEquals("child", child.getName());
        XmlElement grandChild = (XmlElement) child.getElements().get(0);
        assertEquals("child", grandChild.getName());
        XmlElement greatGrandChild = (XmlElement) grandChild.getElements().get(0);
        assertEquals("child", greatGrandChild.getName());
    }

    @Test
    public void testAddSubElementByElement() {
        XmlElement child = new XmlElement("child");
        xmlElement.addSubElement(child);
        assertEquals(child, xmlElement.getElement(0));
    }

    @Test
    public void testAddSubElementByStringAndData() {
        XmlElement child = xmlElement.addSubElement("child", "data");
        assertEquals("child", child.getName());
        assertEquals("data", child.getData());
        assertEquals(child, xmlElement.getElement(0));
    }

    @Test
    public void testSetParent() {
        XmlElement parent = new XmlElement("parent");
        xmlElement.setParent(parent);
        assertEquals(parent, xmlElement.getParent());
    }

    @Test
    public void testGetData() {
        xmlElement.setData("data");
        assertEquals("data", xmlElement.getData());
    }

    @Test
    public void testGetName() {
        xmlElement.setName("name");
        assertEquals("name", xmlElement.getName());
    }
    
    @Test
    public void testPrintNode() {
        //Cannot be tested as it prints to standard output
    }

    @Test
    public void testClone() {
        XmlElement clone = (XmlElement) xmlElement.clone();
        assertEquals(xmlElement, clone);
    }

    @Test
    public void testEquals() {
        XmlElement other = new XmlElement();
        assertTrue(xmlElement.equals(xmlElement));
        assertFalse(xmlElement.equals(null));
        assertFalse(xmlElement.equals(new Object()));
        assertFalse(xmlElement.equals(other));

        other.addAttribute("name", "value");
        assertFalse(xmlElement.equals(other));

        xmlElement.addAttribute("name", "value");
        assertTrue(xmlElement.equals(other));

        other.setData("data");
        assertFalse(xmlElement.equals(other));

        xmlElement.setData("data");
        assertTrue(xmlElement.equals(other));

        other.setName("name");
        assertFalse(xmlElement.equals(other));

        xmlElement.setName("name");
        assertTrue(xmlElement.equals(other));

        XmlElement child = new XmlElement("child");
        xmlElement.addElement(child);
        assertFalse(xmlElement.equals(other));

        other.addElement(child);
        assertTrue(xmlElement.equals(other));
    }

    @Test
    public void testHashCode() {
        int hashCode = 23;

        Hashtable<String, String> attributes = new Hashtable<>();
        attributes.put("name", "value");
        xmlElement.setAttributes(attributes);
        hashCode += attributes.hashCode() * 13;

        xmlElement.setData("data");
        hashCode += "data".hashCode() * 17;

        xmlElement.setName("name");
        hashCode += "name".hashCode() * 29;

        List<XmlElement> subElements = xmlElement.getElements();
        hashCode += subElements.hashCode() * 57;

        assertEquals(hashCode, xmlElement.hashCode());
    }
}