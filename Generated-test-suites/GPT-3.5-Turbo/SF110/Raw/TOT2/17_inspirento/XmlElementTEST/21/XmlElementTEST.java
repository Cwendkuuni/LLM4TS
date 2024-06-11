import org.junit.*;
import static org.junit.Assert.*;
import java.util.*;

public class XmlElementTest {
    
    private XmlElement testElement;
    
    @Before
    public void setUp() {
        testElement = new XmlElement("Test");
    }
    
    @Test
    public void testAddAttribute() {
        assertNull(testElement.addAttribute("key", "value"));
    }
    
    @Test
    public void testGetAttribute() {
        testElement.addAttribute("key", "value");
        assertEquals("value", testElement.getAttribute("key"));
    }
    
    @Test
    public void testGetAttributeWithDefault() {
        assertEquals("default", testElement.getAttribute("nonExistent", "default"));
    }
    
    @Test
    public void testAddElement() {
        XmlElement element = new XmlElement("Child");
        assertTrue(testElement.addElement(element));
    }
    
    @Test
    public void testRemoveElement() {
        XmlElement element = new XmlElement("Child");
        testElement.addElement(element);
        assertEquals(element, testElement.removeElement(element));
    }
    
    @Test
    public void testRemoveElementByIndex() {
        XmlElement element = new XmlElement("Child");
        testElement.addElement(element);
        assertEquals(element, testElement.removeElement(0));
    }
    
    @Test
    public void testGetElementByPath() {
        XmlElement child = new XmlElement("Child");
        testElement.addElement(child);
        assertEquals(child, testElement.getElement("Child"));
    }
    
    @Test
    public void testAddSubElementByPath() {
        XmlElement subElement = testElement.addSubElement("Child.Element");
        assertNotNull(subElement);
    }
    
    @Test
    public void testAddSubElementWithData() {
        XmlElement subElement = testElement.addSubElement("Child", "Data");
        assertEquals("Data", subElement.getData());
    }
    
    // Add more test cases for all methods
    
    @After
    public void tearDown() {
        testElement = null;
    }
}