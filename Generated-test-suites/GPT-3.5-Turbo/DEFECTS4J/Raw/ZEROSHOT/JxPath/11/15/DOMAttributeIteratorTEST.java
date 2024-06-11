import static org.junit.Assert.*;
import org.junit.Before;
import org.junit.Test;
import org.apache.commons.jxpath.ri.model.dom.DOMAttributeIterator;
import org.apache.commons.jxpath.ri.model.*; 
import org.w3c.dom.Node;
import org.w3c.dom.Attr;
import org.w3c.dom.Element;
import org.w3c.dom.NamedNodeMap;
import java.util.List;
import java.util.ArrayList;
import org.apache.commons.jxpath.ri.QName;
import org.apache.commons.jxpath.ri.model.NodePointer;
import org.apache.commons.jxpath.ri.model.NodeIterator;

public class DOMAttributeIteratorTest {
    private NodePointer parent;
    private QName name;
    private List<Attr> attributes;
    private DOMAttributeIterator iterator;
    
    @Before
    public void setUp() {
        // Create a sample node
        Node node = new Element();
        
        // Mock parent NodePointer
        parent = new DOMNodePointer(node);
        
        // Set the name
        name = new QName("attribute");
        
        // Create a list of attributes
        attributes = new ArrayList<Attr>();
        Attr attr1 = new Attr();
        attr1.setNodeName("attribute1");
        Attr attr2 = new Attr();
        attr2.setNodeName("attribute2");
        Attr attr3 = new Attr();
        attr3.setNodeName("attribute3");
        attributes.add(attr1);
        attributes.add(attr2);
        attributes.add(attr3);
        
        iterator = new DOMAttributeIterator(parent, name);
    }
    
    @Test
    public void testConstructor() {
        assertNotNull(iterator);
    }
    
    @Test
    public void testGetNodePointer() {
        NodePointer pointer = iterator.getNodePointer();
        assertNull(pointer);
        
        // Set position to 1
        iterator.setPosition(1);
        pointer = iterator.getNodePointer();
        assertNotNull(pointer);
        assertEquals(DOMAttributePointer.class, pointer.getClass());
    }
    
    @Test
    public void testGetPosition() {
        assertEquals(0, iterator.getPosition());
    }
    
    @Test
    public void testSetPosition() {
        assertFalse(iterator.setPosition(0));
        assertTrue(iterator.setPosition(1));
        assertTrue(iterator.setPosition(2));
        assertTrue(iterator.setPosition(3));
        assertFalse(iterator.setPosition(4));
    }
    
    @Test
    public void testTestAttr() {
        Attr attr = attributes.get(0);
        QName testName = new QName(attr.getNodeName());
        assertTrue(iterator.testAttr(attr, testName));
        
        testName = new QName("invalid");
        assertFalse(iterator.testAttr(attr, testName));
    }
    
    @Test
    public void testGetAttribute() {
        Element element = new Element();
        element.setAttribute("attribute1", "value1");
        
        QName attributeQName = new QName("attribute1");
        Attr attr = iterator.getAttribute(element, attributeQName);
        assertNotNull(attr);
        assertEquals("attribute1", attr.getName());
        
        attributeQName = new QName("attribute2");
        attr = iterator.getAttribute(element, attributeQName);
        assertNull(attr);
    }
}