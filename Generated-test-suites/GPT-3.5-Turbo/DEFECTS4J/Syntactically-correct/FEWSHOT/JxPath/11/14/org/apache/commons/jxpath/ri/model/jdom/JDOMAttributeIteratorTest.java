import org.junit.Test;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertTrue;
import static org.junit.Assert.assertNull;

import org.apache.commons.jxpath.ri.QName;
import org.apache.commons.jxpath.ri.model.NodeIterator;
import org.apache.commons.jxpath.ri.model.NodePointer;
import org.jdom.Attribute;
import org.jdom.Element;
import org.jdom.Namespace;

public class JDOMAttributeIteratorTest {
    
    @Test
    public void testConstructor() {
        NodePointer nodePointer = new JDOMNodePointer(new Element("testElement", Namespace.NO_NAMESPACE));
        QName attributeName = new QName("attributeName");
        JDOMAttributeIterator iterator = new JDOMAttributeIterator(nodePointer, attributeName);
        
        assertNotNull(iterator);
    }
    
    @Test
    public void testGetPosition() {
        NodePointer nodePointer = new JDOMNodePointer(new Element("testElement", Namespace.NO_NAMESPACE));
        QName attributeName = new QName("attributeName");
        JDOMAttributeIterator iterator = new JDOMAttributeIterator(nodePointer, attributeName);
        
        assertEquals(0, iterator.getPosition());
    }
    
    @Test
    public void testSetPosition() {
        NodePointer nodePointer = new JDOMNodePointer(new Element("testElement", Namespace.NO_NAMESPACE));
        QName attributeName = new QName("attributeName");
        JDOMAttributeIterator iterator = new JDOMAttributeIterator(nodePointer, attributeName);
        
        assertTrue(iterator.setPosition(1));
        assertEquals(1, iterator.getPosition());
        
        assertFalse(iterator.setPosition(0));
        assertFalse(iterator.setPosition(2));
    }
    
    @Test
    public void testGetNodePointer() {
        NodePointer nodePointer = new JDOMNodePointer(new Element("testElement", Namespace.NO_NAMESPACE));
        QName attributeName = new QName("attributeName");
        JDOMAttributeIterator iterator = new JDOMAttributeIterator(nodePointer, attributeName);
        
        assertNotNull(iterator.getNodePointer());
    }
    
    @Test
    public void testGetNodePointerMultipleAttributes() {
        Element element = new Element("testElement", Namespace.NO_NAMESPACE);
        element.setAttribute(new Attribute("attribute1", "value1"));
        element.setAttribute(new Attribute("attribute2", "value2"));
        NodePointer nodePointer = new JDOMNodePointer(element);
        QName attributeName = new QName("attributeName");
        JDOMAttributeIterator iterator = new JDOMAttributeIterator(nodePointer, attributeName);
        
        assertNotNull(iterator.getNodePointer());
        assertNotNull(iterator.getNodePointer().getNode());
        assertEquals("value1", ((Attribute) iterator.getNodePointer().getNode()).getValue());
        assertTrue(iterator.setPosition(2));
        assertNotNull(iterator.getNodePointer());
        assertNotNull(iterator.getNodePointer().getNode());
        assertEquals("value2", ((Attribute) iterator.getNodePointer().getNode()).getValue());
        assertFalse(iterator.setPosition(3));
        assertNull(iterator.getNodePointer());
    }
}