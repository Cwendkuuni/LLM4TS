import static org.junit.Assert.*;
import org.junit.Before;
import org.junit.Test;
import org.apache.commons.jxpath.ri.model.jdom.JDOMAttributeIterator;
import org.jdom.Attribute;
import org.jdom.Element;
import org.jdom.Namespace;
import org.jdom.Document;
import org.jdom.input.SAXBuilder;
import org.apache.commons.jxpath.ri.model.NodePointer;
import org.apache.commons.jxpath.ri.QName;

public class JDOMAttributeIteratorTest {

    private Element element;
    private NodePointer nodePointer;

    @Before
    public void setUp() throws Exception {
        // Prepare test element
        element = new Element("testElement");
        element.setNamespace(Namespace.getNamespace("http://example.com/ns"));
        element.setAttribute(new Attribute("attribute1", "value1"));
        element.setAttribute(new Attribute("attribute2", "value2"));

        // Setup parent node pointer
        Document document = new Document(element);
        SAXBuilder builder = new SAXBuilder();
        builder.build(document);
        nodePointer = new JDOMAttributeIterator(builder, new QName("attribute")).getNodePointer();
    }
    
    @Test
    public void testGetPosition() {
        assertEquals(0, nodePointer.getPosition());
    }
    
    @Test
    public void testSetPosition() {
        assertTrue(nodePointer.setPosition(1));
        assertFalse(nodePointer.setPosition(3));
        assertTrue(nodePointer.setPosition(2));
        assertFalse(nodePointer.setPosition(-1));
    }
    
    @Test
    public void testGetNodePointer() {
        // First attribute
        assertTrue(nodePointer.setPosition(1));
        NodePointer attribute1 = nodePointer.getNodePointer();
        assertNotNull(attribute1);
        assertEquals("attribute1", ((Attribute)attribute1.getNode()).getName());

        // Second attribute
        assertTrue(nodePointer.setPosition(2));
        NodePointer attribute2 = nodePointer.getNodePointer();
        assertNotNull(attribute2);
        assertEquals("attribute2", ((Attribute)attribute2.getNode()).getName());
        
        // Out of bounds
        assertNull(nodePointer.setPosition(3));
        assertNull(nodePointer.setPosition(-1));
    }
}