import org.apache.commons.jxpath.ri.model.NodePointer;
import org.jdom.Attribute;
import org.jdom.Element;
import org.junit.Before;
import org.junit.Test;

import static org.junit.Assert.*;

public class JDOMAttributeIteratorTest {
    
    private JDOMAttributeIterator attributeIterator;
    private NodePointer parent;

    @Before
    public void setUp() {
        parent = new NodePointer();
        attributeIterator = new JDOMAttributeIterator(parent, new QName());
    }

    @Test
    public void testGetNodePointer() {
        assertNull(attributeIterator.getNodePointer());
        
        Element element = new Element("test");
        parent.setNode(element);
        
        attributeIterator = new JDOMAttributeIterator(parent, new QName());
        assertNull(attributeIterator.getNodePointer());
        
        element.setAttribute(new Attribute("attr1", "value1"));
        attributeIterator = new JDOMAttributeIterator(parent, new QName());
        assertNotNull(attributeIterator.getNodePointer());
        assertTrue(attributeIterator.getNodePointer() instanceof JDOMAttributePointer);

        attributeIterator.setPosition(1);
        assertSame(attributeIterator.getNodePointer(), new JDOMAttributePointer(parent, parent.getAttribute("attr1")));        
    }

    @Test
    public void testGetPosition() {
        assertEquals(0, attributeIterator.getPosition());
        
        Element element = new Element("test");
        parent.setNode(element);
        
        attributeIterator = new JDOMAttributeIterator(parent, new QName());
        assertEquals(0, attributeIterator.getPosition());
        
        element.setAttribute(new Attribute("attr1", "value1"));
        attributeIterator = new JDOMAttributeIterator(parent, new QName());
        assertEquals(0, attributeIterator.getPosition());
        
        attributeIterator.setPosition(1);
        assertEquals(1, attributeIterator.getPosition());
    }

    @Test
    public void testSetPosition() {
        assertFalse(attributeIterator.setPosition(1));
        
        Element element = new Element("test");
        parent.setNode(element);
        
        attributeIterator = new JDOMAttributeIterator(parent, new QName());
        assertFalse(attributeIterator.setPosition(1));
        
        element.setAttribute(new Attribute("attr1", "value1"));
        attributeIterator = new JDOMAttributeIterator(parent, new QName());
        assertFalse(attributeIterator.setPosition(0));
        assertTrue(attributeIterator.setPosition(1));
        assertFalse(attributeIterator.setPosition(2));
        
        element.setAttribute(new Attribute("attr2", "value2"));
        attributeIterator = new JDOMAttributeIterator(parent, new QName());
        assertFalse(attributeIterator.setPosition(-1));
        assertTrue(attributeIterator.setPosition(1));
        assertTrue(attributeIterator.setPosition(2));
        assertFalse(attributeIterator.setPosition(3));
    }
}