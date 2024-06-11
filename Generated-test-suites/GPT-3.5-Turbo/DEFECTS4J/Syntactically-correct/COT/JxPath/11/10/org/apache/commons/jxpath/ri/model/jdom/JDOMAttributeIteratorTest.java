
import static org.junit.Assert.*;
import org.junit.Before;
import org.junit.Test;
import org.jdom.Attribute;
import org.jdom.Element;
import org.jdom.Namespace;
import org.apache.commons.jxpath.ri.QName;
import org.apache.commons.jxpath.ri.model.NodePointer;

public class JDOMAttributeIteratorTest {
    
    private Element testElement;
    private NodePointer testPointer;
    
    @Before
    public void setup() {
        testElement = new Element("testElement");
        testElement.setAttribute(new Attribute("attribute1", "value1"));
        testElement.setAttribute(new Attribute("attribute2", "value2"));
        testPointer = new JDOMAttributePointer(new JDOMDocumentPointer(testElement.getDocument()), 
                                               testElement.getAttribute("attribute1"));
    }
    
    @Test
    public void testConstructor() {
        JDOMAttributeIterator iterator = new JDOMAttributeIterator(testPointer, new QName("attribute1"));
        assertNotNull(iterator);
    }
    
    @Test
    public void testGetNodePointer() {
        JDOMAttributeIterator iterator = new JDOMAttributeIterator(testPointer, new QName("attribute1"));
        NodePointer nodePointer = iterator.getNodePointer();
        assertNotNull(nodePointer);
        assertEquals(testPointer.getNamespaceURI(), nodePointer.getNamespaceURI());
        assertEquals(testPointer.getName(), nodePointer.getName());
        assertEquals(testPointer.getValue(), nodePointer.getValue());
    }
    
    @Test
    public void testGetPosition() {
        JDOMAttributeIterator iterator = new JDOMAttributeIterator(testPointer, new QName("attribute1"));
        assertEquals(0, iterator.getPosition());
        iterator.setPosition(1);
        assertEquals(1, iterator.getPosition());
    }
    
    @Test
    public void testSetPosition() {
        JDOMAttributeIterator iterator = new JDOMAttributeIterator(testPointer, new QName("attribute1"));
        assertTrue(iterator.setPosition(1));
        assertFalse(iterator.setPosition(0));
        assertFalse(iterator.setPosition(3));
    }
}
