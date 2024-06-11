import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertTrue;

import java.util.ArrayList;
import java.util.List;

import org.apache.commons.jxpath.ri.QName;
import org.apache.commons.jxpath.ri.model.jdom.JDOMAttributeIterator;
import org.apache.commons.jxpath.ri.model.jdom.JDOMAttributePointer;
import org.apache.commons.jxpath.ri.model.NodeIterator;
import org.apache.commons.jxpath.ri.model.NodePointer;
import org.jdom.Attribute;
import org.jdom.Element;
import org.jdom.Namespace;
import org.junit.Before;
import org.junit.Test;

public class JDOMAttributeIteratorTest {
    
    private Element element;
    private NodePointer parent;
    private QName name;
    private JDOMAttributeIterator iterator;
    
    @Before
    public void setUp() {
        element = new Element("testElement");
        element.setAttribute("attribute1", "value1");
        element.setAttribute("attribute2", "value2");
        element.setAttribute("attribute3", "value3");
        element.setNamespace(Namespace.NO_NAMESPACE);
        
        parent = new JDOMNodePointer(element, null, null);
        name = new QName(null, "*", null);
        iterator = new JDOMAttributeIterator(parent, name);
    }
    
    @Test
    public void testConstructor() {
        assertEquals(parent, iterator.getParent());
        assertEquals(name, iterator.getName());
    }
    
    @Test
    public void testSetPosition() {
        assertTrue(iterator.setPosition(1));
        assertTrue(iterator.setPosition(2));
        assertTrue(iterator.setPosition(3));
        assertTrue(!iterator.setPosition(4));
    }
    
    @Test
    public void testGetPosition() {
        iterator.setPosition(1);
        assertEquals(1, iterator.getPosition());
    }
    
    @Test
    public void testGetNodePointer() {
        iterator.setPosition(1);
        NodePointer nodePointer = iterator.getNodePointer();
        assertTrue(nodePointer instanceof JDOMAttributePointer);
        assertEquals("attribute1", ((Attribute)nodePointer.getNode()).getName());
    }
    
    @Test
    public void testGetNodePointerWithInvalidPosition() {
        iterator.setPosition(0);
        NodePointer nodePointer = iterator.getNodePointer();
        assertNull(nodePointer);
    }
}