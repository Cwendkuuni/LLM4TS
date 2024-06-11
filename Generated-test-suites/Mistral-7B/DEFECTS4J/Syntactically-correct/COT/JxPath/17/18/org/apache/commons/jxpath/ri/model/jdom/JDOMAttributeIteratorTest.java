package org.apache.commons.jxpath.ri.model.jdom.test;

import org.apache.commons.jxpath.ri.model.NodeIterator;
import org.apache.commons.jxpath.ri.model.NodePointer;
import org.apache.commons.jxpath.ri.QName;
import org.jdom.Attribute;
import org.jdom.Element;
import org.jdom.Namespace;
import org.junit.Assert;
import org.junit.Test;

import static org.junit.Assert.*;

public class JDOMAttributeIteratorTest {

    @Test
    public void testJDOMAttributeIteratorConstructorWithEmptyAttributeList() {
        NodePointer parent = new JDOMNodePointer(new Element("root"));
        JDOMAttributeIterator iterator = new JDOMAttributeIterator(parent, new QName("", "*"));
        assertEquals(0, iterator.getPosition());
        assertNull(iterator.getNodePointer());
    }

    @Test
    public void testJDOMAttributeIteratorConstructorWithSingleAttribute() {
        Element root = new Element("root");
        root.setAttribute("attr1", "value1");
        NodePointer parent = new JDOMNodePointer(root);
        QName name = new QName("", "attr1");
        JDOMAttributeIterator iterator = new JDOMAttributeIterator(parent, name);
        assertEquals(1, iterator.getPosition());
        NodePointer nodePointer = iterator.getNodePointer();
        assertEquals("value1", nodePointer.getNode().getValue());
    }

    @Test
    public void testJDOMAttributeIteratorConstructorWithMultipleAttributes() {
        Element root = new Element("root");
        root.setAttribute("attr1", "value1");
        root.setAttribute("attr2", "value2");
        NodePointer parent = new JDOMNodePointer(root);
        JDOMAttributeIterator iterator = new JDOMAttributeIterator(parent, new QName("", "*"));
        assertEquals(2, iterator.getPosition());
        NodePointer nodePointer = iterator.getNodePointer();
        assertEquals("value2", nodePointer.getNode().getValue());
    }

    @Test
    public void testJDOMAttributeIteratorGetPosition() {
        Element root = new Element("root");
        root.setAttribute("attr1", "value1");
        root.setAttribute("attr2", "value2");
        NodePointer parent = new JDOMNodePointer(root);
        JDOMAttributeIterator iterator = new JDOMAttributeIterator(parent, new QName("", "*"));
        assertEquals(2, iterator.getPosition());
        iterator.next();
        assertEquals(1, iterator.getPosition());
    }

    @Test(expected = NoSuchElementException.class)
    public void testJDOMAttributeIteratorNextAfterLastElement() {
        Element root = new Element("root");
        root.setAttribute("attr1", "value1");
        NodePointer parent = new JDOMNodePointer(root);
        JDOMAttributeIterator iterator = new JDOMAttributeIterator(parent, new QName("", "*"));
        iterator.next();
        iterator.next(); // move to last position
        iterator.next(); // throw NoSuchElementException
    }

    @Test
    public void testJDOMAttributeIteratorSetPositionWithInvalidIndex() {
        Element root = new Element("root");
        root.setAttribute("attr1", "value1");
        root.setAttribute("attr2", "value2");
        NodePointer parent = new JDOMNodePointer(root);
        JDOMAttributeIterator iterator = new JDOMAttributeIterator(parent, new QName("", "*"));
        assertFalse(iterator.setPosition(-1));
        assertFalse(iterator.setPosition(3));
    }

    @Test
    public void testJDOMAttributeIteratorSetPositionWithValidIndex() {
        Element root = new Element("root");
        root.setAttribute("attr1", "value1");
        root.setAttribute("attr2", "value2");
        NodePointer parent = new JDOMNodePointer(root);
        JDOMAttributeIterator iterator = new JDOMAttributeIterator(parent, new QName("", "*"));
        assertTrue(iterator.setPosition(1));
        assertEquals(1, iterator.getPosition());
    }

    @Test
    public void testJDOMAttributeIteratorWithNullParent() {
        JDOMAttributeIterator iterator = new JDOMAttributeIterator(null, new QName("", "*"));
        assertNull(iterator);
    }

    @Test
    public void testJDOMAttributeIteratorWithNullName() {
        NodePointer parent = new JDOMNodePointer(new Element("root"));
        JDOMAttributeIterator iterator = new JDOMAttributeIterator(parent, null);
        assertEquals(0, iterator.getPosition());
        assertNull(iterator.getNodePointer());
    }
}