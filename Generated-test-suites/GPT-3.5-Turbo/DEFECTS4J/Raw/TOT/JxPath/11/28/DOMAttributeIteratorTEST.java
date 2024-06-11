
import static org.junit.Assert.*;

import org.apache.commons.jxpath.ri.QName;
import org.apache.commons.jxpath.ri.model.NodePointer;
import org.apache.commons.jxpath.ri.model.dom.DOMAttributeIterator;
import org.junit.Before;
import org.junit.Test;
import org.w3c.dom.Attr;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.NamedNodeMap;

public class DOMAttributeIteratorTest {
    
    private NodePointer parent;
    
    @Before
    public void setUp() {
        Document document = TestUtils.createDocument();
        Element element = document.createElement("testElement");
        element.setAttribute("attr1", "value1");
        element.setAttribute("attr2", "value2");
        element.setAttribute("attr3", "value3");
        
        parent = TestUtils.createNodePointer(element);
    }
    
    @Test
    public void testConstructor_ElementWithAttributes() {
        QName name = new QName(null, "*", "");
        DOMAttributeIterator iterator = new DOMAttributeIterator(parent, name);
        
        NamedNodeMap attributes = parent.getNode().getAttributes();
        
        assertNotNull(iterator);
        assertEquals(attributes.getLength(), iterator.attributes.size());
    }
    
    @Test
    public void testConstructor_ElementWithoutAttributes() {
        Element element = parent.getDocument().createElement("noAttributesElement");
        parent = TestUtils.createNodePointer(element);
        
        QName name = new QName(null, "*", "");
        DOMAttributeIterator iterator = new DOMAttributeIterator(parent, name);
        
        assertEquals(0, iterator.attributes.size());
    }
    
    @Test
    public void testConstructor_NamespaceMatchingAttributes() {
        Element element = parent.getDocument().createElement("testElement");
        element.setAttributeNS("http://example.com", "prefix:attr1", "value1");
        element.setAttributeNS("http://example.com", "prefix:attr2", "value2");
        element.setAttribute("attr3", "value3");
        parent = TestUtils.createNodePointer(element);
        
        QName name = new QName("http://example.com", "*", "prefix");
        DOMAttributeIterator iterator = new DOMAttributeIterator(parent, name);
        
        assertEquals(2, iterator.attributes.size());
    }
    
    @Test
    public void testConstructor_NamespaceNotMatchingAttributes() {
        Element element = parent.getDocument().createElement("testElement");
        element.setAttributeNS("http://example.com", "prefix:attr1", "value1");
        element.setAttributeNS("http://example.com", "prefix:attr2", "value2");
        parent = TestUtils.createNodePointer(element);
        
        QName name = new QName("http://anotherexample.com", "*", "prefix");
        DOMAttributeIterator iterator = new DOMAttributeIterator(parent, name);
        
        assertEquals(0, iterator.attributes.size());
    }
    
    @Test
    public void testGetPosition() {
        QName name = new QName(null, "*", "");
        DOMAttributeIterator iterator = new DOMAttributeIterator(parent, name);
        
        assertEquals(0, iterator.getPosition());
        
        iterator.setPosition(2);
        assertEquals(2, iterator.getPosition());
    }
    
    @Test
    public void testSetPosition_OutOfBounds() {
        QName name = new QName(null, "*", "");
        DOMAttributeIterator iterator = new DOMAttributeIterator(parent, name);
        
        assertFalse(iterator.setPosition(-1));
        assertFalse(iterator.setPosition(4));
    }
    
    @Test
    public void testGetNodePointer() {
        QName name = new QName(null, "*", "");
        DOMAttributeIterator iterator = new DOMAttributeIterator(parent, name);
        
        NodePointer nodePointer1 = iterator.getNodePointer();
        assertNotNull(nodePointer1);
        assertEquals(nodePointer1, iterator.getNodePointer());
        
        iterator.setPosition(2);
        NodePointer nodePointer2 = iterator.getNodePointer();
        assertNotNull(nodePointer2);
        assertEquals(nodePointer2, iterator.getNodePointer());
        assertNotEquals(nodePointer1, nodePointer2);
    }

}
