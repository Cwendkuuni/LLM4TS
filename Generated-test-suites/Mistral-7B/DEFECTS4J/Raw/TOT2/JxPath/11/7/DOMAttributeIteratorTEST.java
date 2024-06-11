package org.apache.commons.jxpath.ri.model.dom;

import org.junit.Test;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertTrue;
import org.junit.Assert;
import org.junit.experiment.categories.Category;
import org.junit.runner.RunWith;
import org.junit.runners.Parameterized;
import org.junit.runners.Parameterized.Parameters;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;
import org.w3c.dom.NodeType;
import org.w3c.dom.Attr;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.Collections;
import java.util.List;

@RunWith(Parameterized.class)
public class DOMAttributeIteratorTest {
    private NodePointer nodePointer;
    private DOMAttributeIterator iterator;
    private QName name;

    public DOMAttributeIteratorTest(NodePointer nodePointer, QName name) {
        this.nodePointer = nodePointer;
        this.name = name;
    }

    @Parameters
    public static Collection<Object[]> data() {
        Document doc = DocumentBuilderFactory.newDocumentBuilder().newDocument();
        Element root = doc.createElement("root");
        root.setAttribute("test1", "test1");
        root.setAttribute("test2", "test2");
        doc.appendChild(root);

        return Arrays.asList(new Object[][]{
                {new DOMPoint(root), new QName("test", "*", "")},
                {new DOMPoint(null), new QName("test", "*", "")},
                {new DOMPoint(root), new QName("test3", "*", "")},
                {new DOMPoint(root), new QName("test1", "*", "test")},
                {new DOMPoint(root), new QName("xmlns:test", "*", "")}
        });
    }

    @Test
    public void testDOMAttributeIteratorConstructor() {
        DOMAttributeIterator iterator = new DOMAttributeIterator(nodePointer, name);
        assertNotNull(iterator);
    }

    @Test
    public void testTestAttr() {
        iterator = new DOMAttributeIterator(nodePointer, name);
        assertTrue(iterator.testAttr(iterator.getAttribute(iterator.getNodePointer().getNode(), name), name));
        assertTrue(iterator.testAttr(iterator.getAttribute(iterator.getNodePointer().getNode(), new QName("test2", "*", "")), new QName("test2", "*", "")));
        assertTrue(iterator.testAttr(iterator.getAttribute(iterator.getNodePointer().getNode(), new QName("test2", "*", "test")), new QName("test2", "*", "test")));
        assertTrue(iterator.testAttr(iterator.getAttribute(iterator.getNodePointer().getNode(), new QName("test3", "*", "")), new QName("test3", "*", "")));
        assertFalse(iterator.testAttr(iterator.getNodePointer().getNode().getAttributeNode("xmlns:test"), new QName("xmlns:test", "*", "")));
        assertFalse(iterator.testAttr(iterator.getNodePointer().getNode().getAttributeNode("xmlns"), new QName("xmlns", "*", "")));
    }

    @Test
    public void testGetNodePointer() {
        iterator = new DOMAttributeIterator(nodePointer, name);
        DOMAttributePointer attributePointer = iterator.getNodePointer();
        assertNotNull(attributePointer);
        assertEquals(nodePointer.getNode(), attributePointer.getNode());
    }

    @Test
    public void testNext() {
        iterator = new DOMAttributeIterator(nodePointer, name);
        iterator.next();
        DOMAttributePointer attributePointer = iterator.getNodePointer();
        assertNotNull(attributePointer);
        assertEquals(nodePointer.getNode(), attributePointer.getNode().getParentNode());
        assertEquals("test1", attributePointer.getNode().getNodeName());
    }

    @Test(expected = IllegalStateException.class)
    public void testSetPositionOutOfBounds() {
        iterator = new DOMAttributeIterator(nodePointer, name);
        iterator.next();
        iterator.setPosition(0);
        iterator.next();
        iterator.setPosition(10);
    }

    @Test
    public void testSetPositionValid() {
        iterator = new DOMAttributeIterator(nodePointer, name);
        iterator.next();
        iterator.next();
        iterator.setPosition(1);
        assertNotNull(iterator.getNodePointer());
    }

    @Test
    public void testHasNext() {
        iterator = new DOMAttributeIterator(nodePointer, name);
        iterator.next();
        assertTrue(iterator.hasNext());
        iterator.next();
        assertFalse(iterator.hasNext());
    }

    @Test
    public void testIsEmpty() {
        DOMAttributeIterator iterator = new DOMAttributeIterator(new DOMPoint(null), name);
        assertTrue(iterator.isEmpty());
    }
}