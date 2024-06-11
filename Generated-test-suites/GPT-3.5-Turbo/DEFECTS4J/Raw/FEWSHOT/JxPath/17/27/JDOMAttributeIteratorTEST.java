package org.apache.commons.jxpath.ri.model.jdom;

import static org.junit.Assert.*;
import org.apache.commons.jxpath.ri.QName;
import org.apache.commons.jxpath.ri.model.NodeIterator;
import org.apache.commons.jxpath.Pointer;
import org.jdom.Document;
import org.jdom.Element;
import org.jdom.Namespace;
import org.junit.Before;
import org.junit.Test;

public class JDOMAttributeIteratorTest {

    private Document document;
    private Element root;
    private JDOMAttributeIterator iterator;

    @Before
    public void setUp() throws Exception {
        document = new Document();
        root = new Element("root");
        document.setRootElement(root);
        iterator = new JDOMAttributeIterator(Pointer.newPointer(root), new QName("*"));
    }

    @Test
    public void testConstructor() {
        assertNotNull(iterator);
        assertNotNull(iterator.getNodePointer());
        assertEquals(1, iterator.getPosition());
    }

    @Test
    public void testGetPosition() {
        assertEquals(1, iterator.getPosition());
        iterator.setPosition(3);
        assertEquals(3, iterator.getPosition());
        iterator.setPosition(0);
        assertEquals(0, iterator.getPosition());
        iterator.setPosition(-5);
        assertEquals(0, iterator.getPosition());
        iterator.setPosition(10);
        assertEquals(0, iterator.getPosition());
    }

    @Test
    public void testSetPosition() {
        assertTrue(iterator.setPosition(1));
        assertTrue(iterator.setPosition(3));
        assertTrue(iterator.setPosition(0));
        assertFalse(iterator.setPosition(-5));
        assertFalse(iterator.setPosition(10));
    }

    @Test
    public void testGetNodePointer() {
        assertNotNull(iterator.getNodePointer());
    }
}