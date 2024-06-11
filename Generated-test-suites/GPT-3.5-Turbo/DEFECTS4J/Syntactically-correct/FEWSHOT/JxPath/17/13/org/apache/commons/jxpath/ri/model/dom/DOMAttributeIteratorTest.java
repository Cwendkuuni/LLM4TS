package org.apache.commons.jxpath.ri.model.dom;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

import org.apache.commons.jxpath.ri.QName;
import org.apache.commons.jxpath.ri.model.NodePointer;
import org.junit.Before;
import org.junit.Test;
import org.w3c.dom.Attr;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.NamedNodeMap;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;

public class DOMAttributeIteratorTest {
    private NodePointer parent;
    private QName name;
    private DOMAttributeIterator iterator;

    @Before
    public void setUp() {
        Document doc = createDocument();
        Element element = doc.createElement("testElement");
        element.setAttribute("attribute1", "value1");
        element.setAttribute("attribute2", "value2");
        element.setAttribute("attribute3", "value3");
        parent = new DOMNodePointer(element);
        name = new QName("attribute1");
        iterator = new DOMAttributeIterator(parent, name);
    }

    private Document createDocument() {
        return null; // implement your own code to create a document
    }

    @Test
    public void testConstructor() {
        assertEquals(3, iterator.attributes.size());
        assertTrue(iterator.testAttr((Attr) iterator.attributes.get(0)));
        assertFalse(iterator.testAttr((Attr) iterator.attributes.get(1)));
    }

    @Test
    public void testGetAttribute() {
        Element element = (Element) parent.getNode();
        Attr attr = iterator.getAttribute(element, name);
        assertEquals("value1", attr.getValue());

        name = new QName("attribute2");
        attr = iterator.getAttribute(element, name);
        assertEquals("value2", attr.getValue());

        name = new QName("attribute3");
        attr = iterator.getAttribute(element, name);
        assertEquals("value3", attr.getValue());
        
        name = new QName("attribute4");
        attr = iterator.getAttribute(element, name);
        assertNull(attr);
    }

    @Test
    public void testGetNodePointer() {
        NodePointer nodePointer = iterator.getNodePointer();
        assertNotNull(nodePointer);
        assertEquals("value1", ((Attr) nodePointer.getNode()).getValue());

        iterator.setPosition(2);
        nodePointer = iterator.getNodePointer();
        assertNotNull(nodePointer);
        assertEquals("value2", ((Attr) nodePointer.getNode()).getValue());

        iterator.setPosition(4);
        nodePointer = iterator.getNodePointer();
        assertNull(nodePointer);
    }

    @Test
    public void testGetPosition() {
        assertEquals(0, iterator.getPosition());

        iterator.setPosition(2);
        assertEquals(2, iterator.getPosition());

        iterator.setPosition(4);
        assertEquals(4, iterator.getPosition());
    }

    @Test
    public void testSetPosition() {
        assertTrue(iterator.setPosition(1));
        assertEquals(1, iterator.getPosition());

        assertFalse(iterator.setPosition(0));
        assertEquals(0, iterator.getPosition());

        assertTrue(iterator.setPosition(4));
        assertEquals(4, iterator.getPosition());

        assertFalse(iterator.setPosition(5));
        assertEquals(0, iterator.getPosition());
    }
}