package org.apache.commons.jxpath.ri.model.jdom;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

import java.util.ArrayList;
import java.util.List;

import org.apache.commons.jxpath.JXPathContext;
import org.apache.commons.jxpath.Pointer;
import org.apache.commons.jxpath.ri.model.NodePointer;
import org.jdom.Attribute;
import org.jdom.Document;
import org.jdom.Element;
import org.jdom.Namespace;
import org.junit.Before;
import org.junit.Test;

public class JDOMAttributeIteratorTest {
    
    private Element rootElement;
    private NodePointer parentNode;
    
    @Before
    public void setUp() {
        rootElement = new Element("root");
        Document document = new Document(rootElement);
        parentNode = new JDOMElementPointer(null, document.getRootElement());
    }
    
    @Test
    public void testEmptyAttributeIterator() {
        JDOMAttributeIterator iterator = new JDOMAttributeIterator(parentNode, new QName("*"));
        
        assertFalse(iterator.setPosition(1));
        assertEquals(null, iterator.getNodePointer());
        assertEquals(0, iterator.getPosition());
    }
    
    @Test
    public void testSingleAttributeIterator() {
        Attribute attribute = new Attribute("name", "value");
        rootElement.setAttribute(attribute);
        
        JDOMAttributeIterator iterator = new JDOMAttributeIterator(parentNode, new QName("name"));
        
        assertTrue(iterator.setPosition(1));
        assertEquals("value", iterator.getNodePointer().getValue());
        assertTrue(iterator.setPosition(2));
        assertEquals(null, iterator.getNodePointer());
        assertEquals(2, iterator.getPosition());
    }

    @Test
    public void testNamespaceAttributeIterator() {
        Namespace namespace = Namespace.getNamespace("ns", "http://example.com");
        Attribute attribute1 = new Attribute("name1", "value1", namespace);
        Attribute attribute2 = new Attribute("name2", "value2");
        rootElement.setAttribute(attribute1);
        rootElement.setAttribute(attribute2);

        JDOMAttributeIterator iterator = new JDOMAttributeIterator(parentNode, new QName("name", "ns"));

        assertTrue(iterator.setPosition(1));
        assertEquals("value1", iterator.getNodePointer().getValue());
        assertTrue(iterator.setPosition(2));
        assertEquals(null, iterator.getNodePointer());
        assertEquals(2, iterator.getPosition());
    }

    @Test
    public void testWildcardAttributeIterator() {
        rootElement.setAttribute(new Attribute("name1", "value1"));
        rootElement.setAttribute(new Attribute("name2", "value2"));
        rootElement.setAttribute(new Attribute("other", "value3"));

        JDOMAttributeIterator iterator = new JDOMAttributeIterator(parentNode, new QName("*"));

        assertTrue(iterator.setPosition(1));
        assertEquals("value1", iterator.getNodePointer().getValue());
        assertTrue(iterator.setPosition(2));
        assertEquals("value2", iterator.getNodePointer().getValue());
        assertFalse(iterator.setPosition(3));
        assertEquals(null, iterator.getNodePointer());
        assertEquals(3, iterator.getPosition());
    }
}