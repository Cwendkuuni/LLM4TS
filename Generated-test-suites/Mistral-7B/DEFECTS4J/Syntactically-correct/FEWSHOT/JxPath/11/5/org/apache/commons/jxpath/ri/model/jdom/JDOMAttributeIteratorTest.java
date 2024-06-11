package org.apache.commons.cli;

import static org.junit.Assert.*;
import org.jdom.Attribute;
import org.jdom.Element;
import org.jdom.Namespace;
import org.junit.Test;

import org.apache.commons.jxpath.ri.QName;
import org.apache.commons.jxpath.ri.model.NodeIterator;
import org.apache.commons.jxpath.ri.model.NodePointer;

public class JDOMAttributeIteratorTest {

    @Test
    public void testJDOMAttributeIterator_WithNamespace() {
        Element root = new Element("root");
        Element e1 = new Element("e1");
        e1.setAttribute("attr1", new Attribute("attr1", "value1", "ns1:prefix1"));
        root.addContent(e1);

        QName attrName = new QName("ns1:prefix1:attr1");
        JDOMAttributeIterator iterator = new JDOMAttributeIterator(new JDOMPointer(root), attrName);
        NodePointer nodePointer = iterator.getNodePointer();
        assertEquals(nodePointer.getNode(), e1.getAttribute("attr1"));
        assertTrue(iterator.hasNext());
        nodePointer = iterator.getNodePointer();
        assertNull(nodePointer.getNode());
    }

    @Test
    public void testJDOMAttributeIterator_WithoutNamespace() {
        Element root = new Element("root");
        Element e1 = new Element("e1");
        e1.setAttribute("attr1", "value1");
        root.addContent(e1);

        JDOMAttributeIterator iterator = new JDOMAttributeIterator(new JDOMPointer(root), new QName("attr1"));
        NodePointer nodePointer = iterator.getNodePointer();
        assertEquals(nodePointer.getNode(), e1.getAttribute("attr1"));
        assertTrue(iterator.hasNext());
        nodePointer = iterator.getNodePointer();
        assertNull(nodePointer.getNode());
    }

    @Test
    public void testJDOMAttributeIterator_WithWildcard() {
        Element root = new Element("root");
        Element e1 = new Element("e1");
        e1.setAttribute("attr1", "value1");
        e1.setAttribute("attr2", "value2");
        root.addContent(e1);

        JDOMAttributeIterator iterator = new JDOMAttributeIterator(new JDOMPointer(root), new QName("*"));
        NodePointer nodePointer1 = iterator.getNodePointer();
        assertEquals(nodePointer1.getNode(), e1.getAttribute("attr1"));
        assertTrue(iterator.hasNext());
        NodePointer nodePointer2 = iterator.getNodePointer();
        assertEquals(nodePointer2.getNode(), e1.getAttribute("attr2"));
        assertFalse(iterator.hasNext());
    }

    @Test
    public void testJDOMAttributeIterator_Empty() {
        Element root = new Element("root");
        JDOMAttributeIterator iterator = new JDOMAttributeIterator(new JDOMPointer(root), new QName("*"));
        assertFalse(iterator.hasNext());
    }
}