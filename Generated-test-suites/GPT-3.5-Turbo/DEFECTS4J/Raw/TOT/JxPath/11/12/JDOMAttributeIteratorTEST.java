import static org.junit.Assert.*;
import org.junit.Before;
import org.junit.Test;
import org.apache.commons.jxpath.ri.model.jdom.JDOMAttributeIterator;
import org.apache.commons.jxpath.ri.QName;
import org.apache.commons.jxpath.ri.model.NodePointer;
import org.jdom.Element;
import org.jdom.Namespace;
import java.util.ArrayList;
import java.util.List;

public class JDOMAttributeIteratorTest {
    
    private JDOMAttributeIterator iterator;
    private NodePointer nodePointer;
    private Element parent;
    
    @Before
    public void setup() {
        parent = new Element("Parent");
        Namespace namespace = Namespace.getNamespace("test", "http://www.example.com/test");
        parent.addNamespaceDeclaration(namespace);
        parent.setAttribute("attribute1", "value1", namespace);
        parent.setAttribute("attribute2", "value2");
        parent.setAttribute(new org.jdom.Attribute("attribute3", "value3"));
        iterator = new JDOMAttributeIterator(parent, new QName("test:attribute1"));
        nodePointer = iterator.getNodePointer();
    }
    
    @Test
    public void testGetNodePointerForValidAttribute() {
        assertNotNull(nodePointer);
        assertEquals("attribute1", ((org.jdom.Attribute) nodePointer.getNode()).getName());
        assertEquals("value1", ((org.jdom.Attribute) nodePointer.getNode()).getValue());
        assertEquals("test:attribute1", ((QName) nodePointer.getName()).toString());
        assertEquals(1, iterator.getPosition());
        assertTrue(iterator.setPosition(2));
        nodePointer = iterator.getNodePointer();
        assertNotNull(nodePointer);
        assertEquals("attribute2", ((org.jdom.Attribute) nodePointer.getNode()).getName());
        assertEquals("value2", ((org.jdom.Attribute) nodePointer.getNode()).getValue());
        assertEquals("attribute2", ((QName) nodePointer.getName()).toString());
    }
    
    @Test
    public void testGetNodePointerForInvalidAttribute() {
        assertFalse(iterator.setPosition(4));
        assertNull(iterator.getNodePointer());
    }
    
    @Test
    public void testGetPosition() {
        assertEquals(1, iterator.getPosition());
        iterator.setPosition(3);
        assertEquals(3, iterator.getPosition());
        iterator.setPosition(5);
        assertEquals(0, iterator.getPosition());
    }
    
    @Test
    public void testSetPosition() {
        assertTrue(iterator.setPosition(1));
        assertEquals(1, iterator.getPosition());
        assertTrue(iterator.setPosition(3));
        assertEquals(3, iterator.getPosition());
        assertFalse(iterator.setPosition(5));
        assertEquals(0, iterator.getPosition());
    }
}