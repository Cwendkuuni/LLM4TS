import static org.junit.Assert.*;
import org.junit.Test;
import org.apache.commons.jxpath.ri.QName;
import org.apache.commons.jxpath.ri.model.NodePointer;
import org.jdom.Attribute;
import org.jdom.Element;
import org.jdom.Namespace;

public class JDOMAttributeIteratorTest {

    @Test
    public void testGetNodePointer() {
        Element element = new Element("element");
        Attribute attribute = new Attribute("attribute", "value");
        element.setAttribute(attribute);
        
        NodePointer parent = new JDOMNodePointer(element);
        JDOMAttributeIterator iterator = new JDOMAttributeIterator(parent, new QName("attribute"));
        NodePointer nodePointer = iterator.getNodePointer();
        
        assertTrue(nodePointer instanceof JDOMAttributePointer);
        assertEquals(parent, nodePointer.getParent());
        assertEquals(attribute, ((JDOMAttributePointer) nodePointer).getAttribute());
    }

    @Test
    public void testGetPosition() {
        Element element = new Element("element");
        element.setAttribute("attribute1", "value1");
        element.setAttribute("attribute2", "value2");
        
        NodePointer parent = new JDOMNodePointer(element);
        JDOMAttributeIterator iterator = new JDOMAttributeIterator(parent, new QName("*"));
        
        assertEquals(0, iterator.getPosition());
        iterator.setPosition(1);
        assertEquals(1, iterator.getPosition());
        iterator.setPosition(2);
        assertEquals(2, iterator.getPosition());
    }

    @Test
    public void testSetPosition() {
        Element element = new Element("element");
        element.setAttribute("attribute1", "value1");
        element.setAttribute("attribute2", "value2");
        
        NodePointer parent = new JDOMNodePointer(element);
        JDOMAttributeIterator iterator = new JDOMAttributeIterator(parent, new QName("*"));
        
        assertTrue(iterator.setPosition(1));
        assertEquals(1, iterator.getPosition());
        assertTrue(iterator.setPosition(2));
        assertEquals(2, iterator.getPosition());
        assertFalse(iterator.setPosition(3));
        assertEquals(2, iterator.getPosition());
        assertFalse(iterator.setPosition(0));
        assertEquals(2, iterator.getPosition());
    }

}