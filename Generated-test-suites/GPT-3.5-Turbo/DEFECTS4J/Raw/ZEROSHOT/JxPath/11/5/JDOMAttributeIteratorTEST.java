import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertTrue;

import java.util.Collections;
import java.util.Iterator;
import java.util.List;

import org.apache.commons.jxpath.ri.QName;
import org.apache.commons.jxpath.ri.model.NodeIterator;
import org.apache.commons.jxpath.ri.model.NodePointer;
import org.jdom.Attribute;
import org.jdom.Element;
import org.jdom.Namespace;
import org.junit.Before;
import org.junit.Test;

public class JDOMAttributeIteratorTest {
    
    private NodePointer parent;
    
    @Before
    public void setUp() {
        Element element = new Element("test");
        Attribute attribute = new Attribute("attr", "value");
        element.setAttribute(attribute);
        parent = new JDOMNodePointer(element);
    }
    
    @Test
    public void testConstructor() {
        QName name = new QName("attr");
        JDOMAttributeIterator iterator = new JDOMAttributeIterator(parent, name);
        
        assertTrue(iterator.attributes.contains(parent.getNode()));
        assertEquals(parent, iterator.parent);
        assertEquals(0, iterator.position);
    }
    
    @Test
    public void testConstructorWithNamespace() {
        Element element = new Element("test", Namespace.getNamespace("ns", "http://example.com"));
        Attribute attribute = new Attribute("attr", "value", Namespace.getNamespace("ns", "http://example.com"));
        element.setAttribute(attribute);
        parent = new JDOMNodePointer(element);
        
        QName name = new QName("ns", "attr");
        JDOMAttributeIterator iterator = new JDOMAttributeIterator(parent, name);
        
        assertTrue(iterator.attributes.contains(attribute));
        assertEquals(parent, iterator.parent);
        assertEquals(0, iterator.position);
    }
    
    @Test
    public void testConstructorWithWildcardName() {
        QName name = new QName("*");
        JDOMAttributeIterator iterator = new JDOMAttributeIterator(parent, name);
        
        assertTrue(iterator.attributes.contains(parent.getNode()));
        assertEquals(parent, iterator.parent);
        assertEquals(0, iterator.position);
    }
    
    @Test
    public void testConstructorWithWildcardNameAndNamespace() {
        Element element = new Element("test", Namespace.getNamespace("ns", "http://example.com"));
        Attribute attribute = new Attribute("attr", "value", Namespace.getNamespace("ns", "http://example.com"));
        element.setAttribute(attribute);
        parent = new JDOMNodePointer(element);
        
        QName name = new QName("ns", "*");
        JDOMAttributeIterator iterator = new JDOMAttributeIterator(parent, name);
        
        assertTrue(iterator.attributes.contains(attribute));
        assertEquals(parent, iterator.parent);
        assertEquals(0, iterator.position);
    }
    
    @Test
    public void testConstructorWithInvalidNamespace() {
        QName name = new QName("invalid", "attr");
        JDOMAttributeIterator iterator = new JDOMAttributeIterator(parent, name);
        
        assertEquals(Collections.EMPTY_LIST, iterator.attributes);
        assertEquals(parent, iterator.parent);
        assertEquals(0, iterator.position);
    }
    
    @Test
    public void testConstructorWithInvalidWildcardNameAndNamespace() {
        QName name = new QName("invalid", "*");
        JDOMAttributeIterator iterator = new JDOMAttributeIterator(parent, name);
        
        assertEquals(Collections.EMPTY_LIST, iterator.attributes);
        assertEquals(parent, iterator.parent);
        assertEquals(0, iterator.position);
    }
    
    @Test
    public void testGetNodePointer() {
        QName name = new QName("attr");
        JDOMAttributeIterator iterator = new JDOMAttributeIterator(parent, name);
        
        NodePointer nodePointer = iterator.getNodePointer();
        
        assertTrue(nodePointer instanceof JDOMAttributePointer);
        assertEquals(parent, nodePointer.getParent());
    }
    
    @Test
    public void testGetNodePointerWithWildcardName() {
        QName name = new QName("*");
        JDOMAttributeIterator iterator = new JDOMAttributeIterator(parent, name);
        
        NodePointer nodePointer = iterator.getNodePointer();
        
        assertTrue(nodePointer instanceof JDOMAttributePointer);
        assertEquals(parent, nodePointer.getParent());
    }
    
    @Test
    public void testGetNodePointerWithInvalidPosition() {
        QName name = new QName("attr");
        JDOMAttributeIterator iterator = new JDOMAttributeIterator(parent, name);
        
        assertNull(iterator.getNodePointer());
        iterator.setPosition(2);
        assertNull(iterator.getNodePointer());
    }
    
    @Test
    public void testGetPosition() {
        QName name = new QName("attr");
        JDOMAttributeIterator iterator = new JDOMAttributeIterator(parent, name);
        
        assertEquals(0, iterator.getPosition());
        iterator.setPosition(1);
        assertEquals(1, iterator.getPosition());
    }
    
    @Test
    public void testSetPosition() {
        QName name = new QName("attr");
        JDOMAttributeIterator iterator = new JDOMAttributeIterator(parent, name);
        
        assertTrue(iterator.setPosition(1));
        assertEquals(1, iterator.getPosition());
        
        assertFalse(iterator.setPosition(0));
        assertFalse(iterator.setPosition(2));
    }
}