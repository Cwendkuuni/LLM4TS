package org.apache.commons.jxpath.ri.model.jdom;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertTrue;

import org.apache.commons.jxpath.ri.QName;
import org.apache.commons.jxpath.ri.model.NodePointer;
import org.jdom.Attribute;
import org.jdom.Element;
import org.jdom.Namespace;
import org.junit.Before;
import org.junit.Test;

public class JDOMAttributeIteratorTest {
    private Element element;
    private NodePointer parent;
    private JDOMAttributeIterator iterator;
    
    @Before
    public void setup() {
        element = new Element("test");
        parent = new JDOMNodePointer(null, element);
    }
    
    @Test
    public void testConstructorWithNullParent() {
        iterator = new JDOMAttributeIterator(null, new QName("test"));
        assertNull(iterator.getNodePointer());
    }
    
    @Test
    public void testConstructorWithNameWithPrefixAndNamespace() {
        Namespace ns = Namespace.getNamespace("prefix", "http://www.example.com");
        element.setAttribute(new Attribute("test", "value", ns));
        iterator = new JDOMAttributeIterator(parent, new QName("prefix:test"));
        assertEquals("value", ((Attribute) iterator.getNodePointer().getNode()).getValue());
        assertFalse(iterator.setPosition(2));
    }
    
    @Test
    public void testConstructorWithWildcardName() {
        Namespace ns = Namespace.getNamespace("prefix", "http://www.example.com");
        element.setAttribute(new Attribute("test", "value", ns));
        iterator = new JDOMAttributeIterator(parent, new QName("*"));
        assertEquals("value", ((Attribute) iterator.getNodePointer().getNode()).getValue());
        assertFalse(iterator.setPosition(2));
    }
    
    @Test
    public void testGetPosition() {
        iterator = new JDOMAttributeIterator(parent, new QName("test"));
        assertEquals(0, iterator.getPosition());
        iterator.setPosition(2);
        assertEquals(2, iterator.getPosition());
    }
    
    @Test
    public void testSetPosition() {
        iterator = new JDOMAttributeIterator(parent, new QName("test"));
        assertTrue(iterator.setPosition(1));
        assertTrue(iterator.setPosition(2));
        assertFalse(iterator.setPosition(3));
        assertFalse(iterator.setPosition(0));
    }
}