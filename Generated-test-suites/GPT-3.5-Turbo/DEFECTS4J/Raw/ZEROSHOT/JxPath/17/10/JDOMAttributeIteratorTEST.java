import static org.junit.Assert.*;
import org.junit.Before;
import org.junit.Test;
import org.apache.commons.jxpath.ri.model.jdom.JDOMAttributeIterator;
import org.apache.commons.jxpath.ri.model.jdom.JDOMAttributePointer;
import org.apache.commons.jxpath.ri.model.NodePointer;
import org.jdom.Attribute;
import org.jdom.Element;
import org.jdom.Namespace;

public class JDOMAttributeIteratorTest {

    private JDOMAttributeIterator iterator;
    private NodePointer parent;
    
    @Before
    public void setUp() {
        parent = new NodePointer();
    }

    @Test
    public void testGetNodePointer() {
        Element element = new Element("testElement");
        Attribute attribute = new Attribute("testAttribute", "testValue");
        element.setAttribute(attribute);
        parent.setNode(element);
        
        iterator = new JDOMAttributeIterator(parent, new QName("testAttribute"));
        
        JDOMAttributePointer expected = new JDOMAttributePointer(parent, attribute);
        assertEquals(expected, iterator.getNodePointer());
    }

    @Test
    public void testGetPosition() {
        Element element = new Element("testElement");
        element.setAttribute(new Attribute("attribute1", "value1"));
        element.setAttribute(new Attribute("attribute2", "value2"));
        element.setAttribute(new Attribute("attribute3", "value3"));
        parent.setNode(element);
        
        iterator = new JDOMAttributeIterator(parent, new QName("*"));
        
        assertEquals(0, iterator.getPosition());
        iterator.setPosition(1);
        assertEquals(1, iterator.getPosition());
        iterator.setPosition(2);
        assertEquals(2, iterator.getPosition());
        iterator.setPosition(3);
        assertEquals(3, iterator.getPosition());
    }

    @Test
    public void testSetPosition() {
        Element element = new Element("testElement");
        element.setAttribute(new Attribute("attribute1", "value1"));
        element.setAttribute(new Attribute("attribute2", "value2"));
        element.setAttribute(new Attribute("attribute3", "value3"));
        parent.setNode(element);
        
        iterator = new JDOMAttributeIterator(parent, new QName("*"));
        
        assertTrue(iterator.setPosition(1));
        assertTrue(iterator.setPosition(2));
        assertTrue(iterator.setPosition(3));
        assertFalse(iterator.setPosition(4));
        assertFalse(iterator.setPosition(0));
    }
}