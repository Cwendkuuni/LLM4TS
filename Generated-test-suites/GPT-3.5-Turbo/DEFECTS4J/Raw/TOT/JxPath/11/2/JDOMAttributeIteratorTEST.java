import org.junit.Before;
import org.junit.Test;
import static org.junit.Assert.*;

import org.jdom.Attribute;
import org.jdom.Element;
import org.jdom.Namespace;
import org.apache.commons.jxpath.ri.QName;
import org.apache.commons.jxpath.ri.model.NodePointer;

public class JDOMAttributeIteratorTest {
    
    private Element element;
    private JDOMAttributeIterator attributeIterator;
    
    @Before
    public void setUp() {
        element = new Element("testElement");
        element.setAttribute("attr1", "value1");
        element.setAttribute("attr2", "value2");
        element.setAttribute("attr3", "value3");
        
        NodePointer parent = new NodePointer(element, null, new QName("testElement"));
        QName name = new QName("attr1");
        attributeIterator = new JDOMAttributeIterator(parent, name);
    }
    
    @Test
    public void testGetNodePointer() {
        NodePointer nodePtr = attributeIterator.getNodePointer();
        assertTrue(nodePtr instanceof JDOMAttributePointer);
        assertTrue(((JDOMAttributePointer) nodePtr).getNode() instanceof Attribute);
    }
    
    @Test
    public void testGetPosition() {
        assertEquals(0, attributeIterator.getPosition());
    }
    
    @Test
    public void testSetPosition_Valid() {
        assertTrue(attributeIterator.setPosition(1));
        assertEquals(1, attributeIterator.getPosition());
    }
    
    @Test
    public void testSetPosition_Invalid() {
        assertFalse(attributeIterator.setPosition(0));
        assertEquals(0, attributeIterator.getPosition());
        
        assertFalse(attributeIterator.setPosition(4));
        assertEquals(0, attributeIterator.getPosition());
    }
    
    @Test
    public void testSetPosition_AttributesNull() {
        attributeIterator.setPosition(1);
        attributeIterator.attributes = null;
        
        assertFalse(attributeIterator.setPosition(1));
        assertEquals(0, attributeIterator.getPosition());
    }
    
    @Test
    public void testJDOMAttributeIterator_ElementWithPrefix() {
        Element elementWithPrefix = new Element("testElement");
        Namespace prefixNamespace = Namespace.getNamespace("prefix", "http://example.com");
        elementWithPrefix.addNamespaceDeclaration(prefixNamespace);
        
        elementWithPrefix.setAttribute("attr1", "value1", prefixNamespace);
        elementWithPrefix.setAttribute("attr2", "value2");
        elementWithPrefix.setAttribute("attr3", "value3");
        
        NodePointer parent = new NodePointer(elementWithPrefix, null, new QName("testElement"));
        QName name = new QName("prefix:attr1");
        attributeIterator = new JDOMAttributeIterator(parent, name);
        
        assertEquals(1, attributeIterator.attributes.size());
        assertTrue(attributeIterator.attributes.get(0) instanceof Attribute);
    }
    
    @Test
    public void testJDOMAttributeIterator_NoAttributes() {
        Element elementWithoutAttributes = new Element("testElement");
        
        NodePointer parent = new NodePointer(elementWithoutAttributes, null, new QName("testElement"));
        QName name = new QName("attr1");
        attributeIterator = new JDOMAttributeIterator(parent, name);
        
        assertTrue(attributeIterator.attributes.isEmpty());
    }
}