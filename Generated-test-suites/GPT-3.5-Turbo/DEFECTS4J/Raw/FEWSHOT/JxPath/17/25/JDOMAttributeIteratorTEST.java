package org.apache.commons.jxpath.ri.model.jdom;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertTrue;

import org.apache.commons.jxpath.JXPathContext;
import org.apache.commons.jxpath.ri.QName;
import org.apache.commons.jxpath.ri.model.NodeIterator;
import org.apache.commons.jxpath.ri.model.NodePointer;
import org.jdom.Attribute;
import org.jdom.Document;
import org.jdom.Element;
import org.jdom.Namespace;
import org.junit.Before;
import org.junit.Test;

public class JDOMAttributeIteratorTest {
    private JXPathContext context;
    private Element rootElement;
    
    @Before
    public void setUp() {
        rootElement = new Element("root");
        Document document = new Document(rootElement);
        context = JXPathContext.newContext(document);
    }
    
    @Test
    public void testGetNodePointer() {
        // Test with no attributes
        NodePointer pointer = new JDOMAttributeIterator(context.getPointer("/root"), new QName("*")).getNodePointer();
        assertNull(pointer);
        
        // Test with attributes
        Attribute attribute1 = new Attribute("attr1", "value1");
        Attribute attribute2 = new Attribute("attr2", "value2");
        rootElement.setAttribute(attribute1);
        rootElement.setAttribute(attribute2);
        
        // Test with specific attribute name
        pointer = new JDOMAttributeIterator(context.getPointer("/root"), new QName("attr1")).getNodePointer();
        assertTrue(pointer instanceof JDOMAttributePointer);
        assertEquals("attr1", ((JDOMAttributePointer) pointer).getAttribute().getName());
        
        // Test with wildcard attribute name
        pointer = new JDOMAttributeIterator(context.getPointer("/root"), new QName("*")).getNodePointer();
        assertTrue(pointer instanceof JDOMAttributePointer);
        assertEquals("attr1", ((JDOMAttributePointer) pointer).getAttribute().getName());
    }
    
    @Test
    public void testGetPosition() {
        NodeIterator iterator = new JDOMAttributeIterator(context.getPointer("/root"), new QName("*"));
        assertEquals(0, iterator.getPosition());
        
        // Move to first attribute
        assertTrue(iterator.setPosition(1));
        assertEquals(1, iterator.getPosition());
        
        // Move to second attribute
        assertTrue(iterator.setPosition(2));
        assertEquals(2, iterator.getPosition());
        
        // Move to non-existent attribute
        assertFalse(iterator.setPosition(3));
        assertEquals(0, iterator.getPosition());
    }
    
    @Test
    public void testSetPosition() {
        NodeIterator iterator = new JDOMAttributeIterator(context.getPointer("/root"), new QName("*"));
        
        // Try to move to invalid position
        assertFalse(iterator.setPosition(0));
        assertEquals(0, iterator.getPosition());
        
        // Move to first attribute
        assertTrue(iterator.setPosition(1));
        assertEquals(1, iterator.getPosition());
        
        // Move to second attribute
        assertTrue(iterator.setPosition(2));
        assertEquals(2, iterator.getPosition());
        
        // Move to non-existent attribute
        assertFalse(iterator.setPosition(3));
        assertEquals(0, iterator.getPosition());
    }
}