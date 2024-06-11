import static org.junit.Assert.*;

import org.apache.commons.jxpath.ri.QName;
import org.apache.commons.jxpath.ri.model.NodePointer;
import org.jdom.Attribute;
import org.jdom.Element;
import org.jdom.Namespace;
import org.junit.Before;
import org.junit.Test;

public class JDOMAttributeIteratorTest {
    
    private JDOMAttributeIterator attributeIterator;
    private NodePointer parent;
    private Element element;

    @Before
    public void setUp() {
        parent = new NodePointer();
        element = new Element("test");
        element.setAttribute("attr1", "value1");
        element.setAttribute("attr2", "value2");
        element.setAttribute("attr3", "value3", Namespace.XML_NAMESPACE);
        parent.setNode(element);
    }

    @Test
    public void testGetNodePointer() {
        attributeIterator = new JDOMAttributeIterator(parent, new QName("attr1"));
        NodePointer nodePointer = attributeIterator.getNodePointer();
        
        assertTrue(nodePointer instanceof JDOMAttributePointer);
        Attribute attribute = ((JDOMAttributePointer) nodePointer).getAttribute();
        assertEquals("attr1", attribute.getName());
        assertEquals("value1", attribute.getValue());
    }

    @Test
    public void testGetPosition() {
        attributeIterator = new JDOMAttributeIterator(parent, new QName("attr1"));
        attributeIterator.setPosition(2);
        
        assertEquals(2, attributeIterator.getPosition());
    }

    @Test
    public void testSetPosition() {
        attributeIterator = new JDOMAttributeIterator(parent, new QName("attr1"));
        
        assertTrue(attributeIterator.setPosition(1));
        assertEquals(1, attributeIterator.getPosition());
        
        assertFalse(attributeIterator.setPosition(0));
        assertEquals(1, attributeIterator.getPosition());
        
        assertTrue(attributeIterator.setPosition(3));
        assertEquals(3, attributeIterator.getPosition());
        
        assertFalse(attributeIterator.setPosition(4));
        assertEquals(3, attributeIterator.getPosition());
    }
}