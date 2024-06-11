package org.apache.commons.cli;

import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

import org.apache.commons.jxpath.ri.model.NodePointer;
import org.junit.Test;
import org.w3c.dom.Attr;
import org.w3c.dom.Element;

public class DOMAttributeIteratorTest {

    @Test
    public void testDOMAttributeIteratorWithEmptyNode() {
        DOMAttributeIterator iterator = new DOMAttributeIterator(new DOMPointer(null), new QName("*"));
        assertNull(iterator.getNodePointer());
        assertEquals(0, iterator.getPosition());
        assertFalse(iterator.hasNext());
    }

    @Test
    public void testDOMAttributeIteratorWithSingleAttribute() {
        Element testElement = DocumentBuilderFactory.newInstance().newDocumentBuilder().newDocument().createElement("test");
        testElement.setAttribute("test-attribute", "test-value");
        DOMAttributeIterator iterator = new DOMAttributeIterator(new DOMPointer(testElement), new QName("test:test-attribute"));
        NodePointer nodePointer = iterator.getNodePointer();
        assertNotNull(nodePointer);
        assertEquals("test-value", nodePointer.getNodeValue());
        assertEquals(1, iterator.getPosition());
        assertTrue(iterator.hasNext());

        iterator.setPosition(1);
        nodePointer = iterator.getNodePointer();
        assertNull(nodePointer);
        assertEquals(2, iterator.getPosition());
        assertFalse(iterator.hasNext());
    }

    @Test
    public void testDOMAttributeIteratorWithMultipleAttributes() {
        Element testElement = DocumentBuilderFactory.newInstance().newDocumentBuilder().newDocument().createElement("test");
        testElement.setAttribute("test1-attribute", "test1-value");
        testElement.setAttribute("test2-attribute", "test2-value");
        DOMAttributeIterator iterator = new DOMAttributeIterator(new DOMPointer(testElement), new QName("*"));
        int i = 0;
        while (iterator.hasNext()) {
            NodePointer nodePointer = iterator.getNodePointer();
            assertEquals("test" + (i + 1) + ":test" + (i + 1) + "-attribute", nodePointer.getNode().getNodeName());
            assertEquals("test" + (i + 1) + "-value", nodePointer.getNodeValue());
            i++;
            iterator.next();
        }
        assertEquals(3, iterator.getPosition());
        assertFalse(iterator.hasNext());
    }

    @Test
    public void testDOMAttributeIteratorWithNamespace() {
        Element testElement = DocumentBuilderFactory.newInstance().newDocumentBuilder().newDocument().createElement("test");
        testElement.setAttributeNS("http://test.com", "test-namespace-attribute", "test-namespace-value");
        DOMAttributeIterator iterator = new DOMAttributeIterator(new DOMPointer(testElement), new QName("http://test.com:test-namespace-attribute"));
        NodePointer nodePointer = iterator.getNodePointer();
        assertNotNull(nodePointer);
        assertEquals("test-namespace-value", nodePointer.getNodeValue());
        assertEquals(1, iterator.getPosition());
        assertTrue(iterator.hasNext());

        iterator.setPosition(1);
        nodePointer = iterator.getNodePointer();
        assertNull(nodePointer);
        assertEquals(2, iterator.getPosition());
        assertFalse(iterator.hasNext());
    }

    @Test
    public void testDOMAttributeIteratorWithNoMatchingAttribute() {
        Element testElement = DocumentBuilderFactory.newInstance().newDocumentBuilder().newDocument().createElement("test");
        testElement.setAttribute("test1-attribute", "test1-value");
        DOMAttributeIterator iterator = new DOMAttributeIterator(new DOMPointer(testElement), new QName("test2:test-attribute"));
        assertNull(iterator.getNodePointer());
        assertEquals(0, iterator.getPosition());
        assertFalse(iterator.hasNext());
    }
}