package org.apache.commons.jxpath.ri.model.jdom;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertTrue;

import org.apache.commons.jxpath.JXPathContext;
import org.apache.commons.jxpath.ri.model.NodePointer;
import org.jdom.Attribute;
import org.jdom.Document;
import org.jdom.Element;
import org.junit.Before;
import org.junit.Test;

public class JDOMAttributeIteratorTest {
    private Document document;
    private JXPathContext context;
    private NodePointer pointer;

    @Before
    public void setUp() {
        Element root = new Element("root");
        root.setAttribute(new Attribute("attr1", "value1"));
        root.setAttribute(new Attribute("attr2", "value2"));
        root.setAttribute(new Attribute("attr3", "value3"));
        document = new Document(root);
        context = JXPathContext.newContext(document);
        pointer = context.getPointer("/root");
    }

    @Test
    public void testConstructor() {
        JDOMAttributeIterator iterator = new JDOMAttributeIterator(pointer, new QName("attr1"));
        assertFalse(iterator.setPosition(0));
        assertEquals(1, iterator.getPosition());

        iterator = new JDOMAttributeIterator(pointer, new QName("*"));
        assertEquals("attr1", ((Attribute) iterator.getNodePointer().getNode()).getName());
        iterator.setPosition(2);
        assertEquals("attr2", ((Attribute) iterator.getNodePointer().getNode()).getName());
        iterator.setPosition(3);
        assertEquals("attr3", ((Attribute) iterator.getNodePointer().getNode()).getName());
        assertNull(iterator.getNodePointer());
    }

    @Test
    public void testGetPosition() {
        JDOMAttributeIterator iterator = new JDOMAttributeIterator(pointer, new QName("*"));
        assertEquals(0, iterator.getPosition());
        iterator.setPosition(2);
        assertEquals(2, iterator.getPosition());
        iterator.setPosition(5);
        assertEquals(0, iterator.getPosition());
    }

    @Test
    public void testSetPosition() {
        JDOMAttributeIterator iterator = new JDOMAttributeIterator(pointer, new QName("*"));
        assertFalse(iterator.setPosition(0));
        assertTrue(iterator.setPosition(1));
        assertTrue(iterator.setPosition(2));
        assertTrue(iterator.setPosition(3));
        assertFalse(iterator.setPosition(4));
    }
}