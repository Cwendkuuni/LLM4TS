import static org.junit.Assert.*;
import org.junit.*;

import java.util.*;

public class XmlElementTest {

    @Test
    public void testAddAttribute() {
        XmlElement element = new XmlElement();
        Object returnValue = element.addAttribute("name", "value");
        assertNull(returnValue);
        assertEquals("value", element.getAttribute("name"));
    }

    @Test
    public void testGetAttribute() {
        XmlElement element = new XmlElement();
        element.addAttribute("name", "value");
        assertEquals("value", element.getAttribute("name"));
    }

    @Test
    public void testGetAttributeWithDefault() {
        XmlElement element = new XmlElement();
        assertEquals("defaultValue", element.getAttribute("name", "defaultValue"));
    }

    @Test
    public void testGetAttributes() {
        XmlElement element = new XmlElement();
        assertNotNull(element.getAttributes());
        assertEquals(0, element.getAttributes().size());
    }

    @Test
    public void testSetAttributes() {
        XmlElement element = new XmlElement();
        Hashtable<String, String> attributes = new Hashtable<String, String>();
        attributes.put("name", "value");
        element.setAttributes(attributes);
        assertEquals(attributes, element.getAttributes());
    }

    @Test
    public void testGetAttributeNames() {
        XmlElement element = new XmlElement();
        element.addAttribute("name1", "value1");
        element.addAttribute("name2", "value2");
        Enumeration attributeNames = element.getAttributeNames();
        List<String> expectedNames = new ArrayList<>();
        expectedNames.add("name1");
        expectedNames.add("name2");
        while (attributeNames.hasMoreElements()) {
            String name = (String) attributeNames.nextElement();
            assertTrue(expectedNames.contains(name));
        }
    }

    @Test
    public void testAddElement() {
        XmlElement parent = new XmlElement();
        XmlElement child = new XmlElement("child");
        assertTrue(parent.addElement(child));
        assertEquals(child, parent.getElement(0));
    }

    @Test
    public void testRemoveElementByObject() {
        XmlElement parent = new XmlElement();
        XmlElement child = new XmlElement("child");
        parent.addElement(child);
        assertEquals(child, parent.removeElement(child));
        assertEquals(0, parent.count());
    }

    @Test
    public void testRemoveElementByIndex() {
        XmlElement parent = new XmlElement();
        XmlElement child = new XmlElement("child");
        parent.addElement(child);
        assertEquals(child, parent.removeElement(0));
        assertEquals(0, parent.count());
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
        assertNull(child.getParent());
    }

    @Test
    public void testAppend() {
        XmlElement parent = new XmlElement();
        XmlElement child = new XmlElement("child");
        child.appendToParent(parent);
        assertEquals(parent, child.getParent());
    }

    @Test
    public void testInsertElement() {
        XmlElement parent = new XmlElement();
        XmlElement child1 = new XmlElement("child1");
        XmlElement child2 = new XmlElement("child2");
        parent.addElement(child1);
        parent.insertElement(child2, 1);
        assertEquals(child2, parent.getElement(1));
    }

    @Test
    public void testGetElements() {
        XmlElement parent = new XmlElement();
        List<XmlElement> elements = new ArrayList<>();
        elements.add(new XmlElement("child1"));
        elements.add(new XmlElement("child2"));
        parent.addElement(elements.get(0));
        parent.addElement(elements.get(1));
        assertEquals(elements, parent.getElements());
    }

    @Test
    public void testCount() {
        XmlElement parent = new XmlElement();
        parent.addElement(new XmlElement("child1"));
        parent.addElement(new XmlElement("child2"));
        assertEquals(2, parent.count());
    }

    @Test
    public void testGetElement() {
        XmlElement parent = new XmlElement();
        XmlElement child = new XmlElement("child");
        parent.addElement(child);
        assertEquals(child, parent.getElement("child"));
    }

    @Test
    public void testGetElementWithSubElement() {
        XmlElement parent = new XmlElement();
        XmlElement child1 = new XmlElement("child1");
        XmlElement child2 = new XmlElement("child2");
        child1.addElement(child2);
        parent.addElement(child1);
        assertEquals(child2, parent.getElement("child1.child2"));
    }

    @Test
    public void testGetElementWithNonExistingSubElement() {
        XmlElement parent = new XmlElement();
        XmlElement child = new XmlElement("child");
        parent.addElement(child);
        assertNull(parent.getElement("child.nonExistingChild"));
    }

    @Test
    public void testGetElementByIndex() {
        XmlElement parent = new XmlElement();
        XmlElement child = new XmlElement("child");
        parent.addElement(child);
        assertEquals(child, parent.getElement(0));
    }

    @Test
    public void testAddSubElementWithPath() {
        XmlElement parent = new XmlElement();
        XmlElement subElement = parent.addSubElement("child1.child2.child3");
        assertNotNull(subElement);
        assertEquals("child3", subElement.getName());
        assertEquals(3, parent.count());
    }

    @Test
    public void testAddSubElementByObject() {
        XmlElement parent = new XmlElement();
        XmlElement child = new XmlElement("child");
        parent.addSubElement(child);
        assertEquals(child, parent.getElement(0));
    }

    @Test
    public void testAddSubElementByNameAndData() {
        XmlElement parent = new XmlElement();
        XmlElement child = parent.addSubElement("child", "data");
        assertEquals(child, parent.getElement(0));
        assertEquals("data", child.getData());
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
        assertEquals("data", element.getData());
    }
   
    @Test
    public void testGetName() {
        XmlElement element = new XmlElement("name");
        assertEquals("name", element.getName());
    }

    @Test
    public void testPrintNode() {
        XmlElement element = new XmlElement("name");
        element.addAttribute("attribute1", "value1");
        element.addAttribute("attribute2", "value2");
        XmlElement child = new XmlElement("child");
        element.addElement(child);
        
        String expectedOutput = "name\n" +
                                "    attribute1:value1\n" +
                                "    attribute2:value2\n" +
                                "    child";
        ByteArrayOutputStream output = new ByteArrayOutputStream();
        System.setOut(new PrintStream(output));
        
        XmlElement.printNode(element, "");
        
        assertEquals(expectedOutput, output.toString().trim());
    }

    @Test
    public void testClone() {
        XmlElement element = new XmlElement("name1");
        element.addAttribute("attribute1", "value1");
        element.addAttribute("attribute2", "value2");
        
        XmlElement child1 = new XmlElement("child1");
        child1.addData("data1");
        
        XmlElement child2 = new XmlElement("child2");
        child2.addData("data2");
        
        element.addElement(child1);
        element.addElement(child2);
        
        XmlElement clonedElement = (XmlElement) element.clone();
        
        assertEquals(element, clonedElement);
        assertNotSame(element, clonedElement);
    }

    @Test
    public void testNotifyObservers() {
        XmlElement element = new XmlElement();
        MockObserver observer = new MockObserver();
        element.addObserver(observer);
        
        element.notifyObservers();
        
        assertTrue(observer.isNotified());
    }

    @Test
    public void testEquals() {
        XmlElement element1 = new XmlElement("name");
        element1.addAttribute("attribute1", "value1");
        element1.addAttribute("attribute2", "value2");
        
        XmlElement element2 = new XmlElement("name");
        element2.addAttribute("attribute1", "value1");
        element2.addAttribute("attribute2", "value2");
        
        assertEquals(element1, element2);
    }

    @Test
    public void testHashCode() {
        XmlElement element = new XmlElement("name");
        element.addAttribute("attribute1", "value1");
        element.addAttribute("attribute2", "value2");
        
        int expectedHashCode = 23 + element.getAttributes().hashCode() * 13 +
                               element.getName().hashCode() * 29;
        
        assertEquals(expectedHashCode, element.hashCode());
    }
}