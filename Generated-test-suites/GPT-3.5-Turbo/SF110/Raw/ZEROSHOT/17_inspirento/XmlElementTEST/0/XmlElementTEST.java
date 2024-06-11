import static org.junit.Assert.*;
import org.junit.*;

public class XmlElementTest {

    @Before
    public void setUp() {
        // setup code goes here
    }
    
    @After
    public void tearDown() {
        // teardown code goes here
    }
    
    @Test
    public void testAddAttribute() {
        XmlElement element = new XmlElement();
        element.addAttribute("name", "value");
        assertEquals("value", element.getAttribute("name"));
    }
    
    @Test
    public void testGetAttribute() {
        XmlElement element = new XmlElement();
        element.addAttribute("name", "value");
        assertEquals("value", element.getAttribute("name"));
    }
    
    @Test
    public void testGetAttributeWithDefaultValue() {
        XmlElement element = new XmlElement();
        assertEquals("default", element.getAttribute("name", "default"));
    }
    
    @Test
    public void testGetAttributes() {
        XmlElement element = new XmlElement();
        element.addAttribute("name1", "value1");
        element.addAttribute("name2", "value2");
        assertEquals(2, element.getAttributes().size());
    }
    
    @Test
    public void testSetAttributes() {
        XmlElement element = new XmlElement();
        Hashtable<String, String> attrs = new Hashtable<String, String>();
        attrs.put("name1", "value1");
        attrs.put("name2", "value2");
        element.setAttributes(attrs);
        assertEquals(2, element.getAttributes().size());
    }
    
    @Test
    public void testGetAttributeNames() {
        XmlElement element = new XmlElement();
        element.addAttribute("name1", "value1");
        element.addAttribute("name2", "value2");
        assertEquals(2, element.getAttributeNames().size());
    }
    
    @Test
    public void testAddElement() {
        XmlElement parent = new XmlElement();
        XmlElement child = new XmlElement("Child");
        assertTrue(parent.addElement(child));
        assertEquals(1, parent.getElements().size());
    }
    
    @Test
    public void testRemoveElementByElement() {
        XmlElement parent = new XmlElement();
        XmlElement child = new XmlElement("Child");
        parent.addElement(child);
        assertNotNull(parent.removeElement(child));
        assertEquals(0, parent.getElements().size());
    }
    
    @Test
    public void testRemoveElementByIndex() {
        XmlElement parent = new XmlElement();
        XmlElement child1 = new XmlElement("Child1");
        XmlElement child2 = new XmlElement("Child2");
        parent.addElement(child1);
        parent.addElement(child2);
        assertNotNull(parent.removeElement(1));
        assertEquals(1, parent.getElements().size());
    }
    
    @Test
    public void testRemoveAllElements() {
        XmlElement parent = new XmlElement();
        parent.addElement(new XmlElement("Child1"));
        parent.addElement(new XmlElement("Child2"));
        parent.removeAllElements();
        assertEquals(0, parent.getElements().size());
    }
    
    @Test
    public void testRemoveFromParent() {
        XmlElement parent = new XmlElement();
        XmlElement child = new XmlElement("Child");
        parent.addElement(child);
        child.removeFromParent();
        assertEquals(0, parent.getElements().size());
        assertNull(child.getParent());
    }
    
    @Test
    public void testAppend() {
        XmlElement parent = new XmlElement();
        XmlElement child = new XmlElement("Child");
        child.setData("data");
        parent.append(child);
        assertEquals(child, parent.getElements().get(0));
    }
    
    @Test
    public void testInsertElement() {
        XmlElement parent = new XmlElement();
        XmlElement child1 = new XmlElement("Child1");
        XmlElement child2 = new XmlElement("Child2");
        parent.addElement(child1);
        parent.insertElement(child2, 0);
        assertEquals(child2, parent.getElements().get(0));
    }
    
    @Test
    public void testGetElements() {
        XmlElement parent = new XmlElement();
        XmlElement child1 = new XmlElement("Child1");
        XmlElement child2 = new XmlElement("Child2");
        parent.addElement(child1);
        parent.addElement(child2);
        assertEquals(2, parent.getElements().size());
    }
    
    @Test
    public void testCount() {
        XmlElement parent = new XmlElement();
        parent.addElement(new XmlElement("Child1"));
        parent.addElement(new XmlElement("Child2"));
        assertEquals(2, parent.count());
    }
    
    @Test
    public void testGetElement() {
        XmlElement parent = new XmlElement();
        XmlElement child = new XmlElement("Child");
        parent.addElement(child);
        assertEquals(child, parent.getElement("Child"));
    }
    
    @Test
    public void testGetElementByIndex() {
        XmlElement parent = new XmlElement();
        XmlElement child = new XmlElement("Child");
        parent.addElement(child);
        assertEquals(child, parent.getElement(0));
    }
    
    @Test
    public void testAddSubElementByPath() {
        XmlElement parent = new XmlElement();
        XmlElement child = parent.addSubElement("Child1.Child2.Child3");
        assertNotNull(child);
        assertEquals(3, parent.getElements().size());
    }
    
    @Test
    public void testAddSubElementByElement() {
        XmlElement parent = new XmlElement();
        XmlElement child1 = new XmlElement("Child1");
        XmlElement child2 = parent.addSubElement(child1);
        assertNotNull(child2);
        assertEquals(1, parent.getElements().size());
        assertEquals(child1, parent.getElements().get(0));
    }
    
    @Test
    public void testAddSubElementByNameAndData() {
        XmlElement parent = new XmlElement();
        XmlElement child = parent.addSubElement("Child", "data");
        assertNotNull(child);
        assertEquals(1, parent.getElements().size());
        assertEquals("data", child.getData());
    }
    
    @Test
    public void testSetParent() {
        XmlElement parent = new XmlElement("Parent");
        XmlElement child = new XmlElement("Child");
        child.setParent(parent);
        assertEquals(parent, child.getParent());
    }
    
    @Test
    public void testGetData() {
        XmlElement element = new XmlElement("Element");
        element.setData("data");
        assertEquals("data", element.getData());
    }
    
    @Test
    public void testGetName() {
        XmlElement element = new XmlElement("Element");
        assertEquals("Element", element.getName());
    }
    
    @Test
    public void testPrintNode() {
        XmlElement root = new XmlElement("Root");
        XmlElement child1 = root.addSubElement("Child1");
        XmlElement child2 = root.addSubElement("Child2");
        child1.addAttribute("attr1", "value1");
        child2.setData("data");
        // assert the output in console
    }
    
    @Test
    public void testClone() throws CloneNotSupportedException {
        XmlElement element = new XmlElement("Element");
        XmlElement clone = (XmlElement) element.clone();
        assertEquals(element, clone);
        assertNotSame(element, clone);
    }
    
    @Test
    public void testSetName() {
        XmlElement element = new XmlElement("Element");
        element.setName("NewName");
        assertEquals("NewName", element.getName());
    }
    
    @Test
    public void testNotifyObservers() {
        XmlElement element = new XmlElement();
        // assert the behavior of notifyObservers()
    }
    
    @Test
    public void testEquals() {
        XmlElement element1 = new XmlElement("Element");
        XmlElement element2 = new XmlElement("Element");
        assertEquals(element1, element2);
    }
    
    @Test
    public void testHashCode() {
        XmlElement element = new XmlElement("Element");
        assertEquals(23, element.hashCode());
    }
}