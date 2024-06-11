package org.apache.commons.jxpath.ri.model.jdom.test;

import org.apache.commons.jxpath.ri.model.NodeIterator;
import org.apache.commons.jxpath.ri.model.NodePointer;
import org.junit.Assert;
import org.junit.Test;
import org.jdom2.Attribute;
import org.jdom2.Element;
import org.jdom2.Namespace;

import java.util.ArrayList;
import java.util.List;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertSame;
import static org.junit.Assert.assertTrue;

public class JDOMAttributeIteratorTest {

    @Test
    public void testJDOMAttributeIterator_noAttributes() {
        NodePointer parent = new JDOMNodePointer(null, new Element("root"));
        JDOMAttributeIterator iterator = new JDOMAttributeIterator(parent, new QName(""));
        assertFalse(iterator.hasNext());
    }

    @Test
    public void testJDOMAttributeIterator_singleAttribute() {
        String xmlns = "http://www.w3.org/2000/xmlns/";
        String prefix = "xml";
        String localName = "attr";
        Element element = new Element("root", "root", new Namespace(xmlns, prefix));
        element.addAttribute(new Attribute(localName, "value", new Namespace(xmlns, prefix)));
        NodePointer parent = new JDOMNodePointer(null, element);
        JDOMAttributeIterator iterator = new JDOMAttributeIterator(parent, new QName(prefix, localName));
        NodePointer attributePointer = iterator.next();
        Assert.assertNotNull(attributePointer);
        Assert.assertSame(localName, attributePointer.getNode().getName());
        Assert.assertSame(parent, attributePointer.getParent());
        Assert.assertEquals("value", attributePointer.getNode().getValue());
        Assert.assertFalse(iterator.hasNext());
    }

    @Test
    public void testJDOMAttributeIterator_multipleAttributes() {
        String xmlns = "http://www.w3.org/2000/xmlns/";
        String prefix = "xml";
        String localName1 = "attr1";
        String localName2 = "attr2";
        Element element = new Element("root", "root", new Namespace(xmlns, prefix));
        element.addAttribute(new Attribute(localName1, "value1", new Namespace(xmlns, prefix)));
        element.addAttribute(new Attribute(localName2, "value2", new Namespace(xmlns, prefix)));
        NodePointer parent = new JDOMNodePointer(null, element);
        JDOMAttributeIterator iterator = new JDOMAttributeIterator(parent, new QName(prefix, "*"));
        List<NodePointer> attributePointers = new ArrayList<>();
        for (int i = 0; i < 2; i++) {
            NodePointer attributePointer = iterator.next();
            attributePointers.add(attributePointer);
            Assert.assertNotNull(attributePointer);
            Assert.assertSame(localName1.equals(i == 0) ? localName1 : localName2, attributePointer.getNode().getName());
            Assert.assertSame(parent, attributePointer.getParent());
            Assert.assertEquals(i == 0 ? "value1" : "value2", attributePointer.getNode().getValue());
        }
        Assert.assertFalse(iterator.hasNext());
        for (NodePointer attributePointer : attributePointers) {
            iterator.remove();
            Assert.assertFalse(iterator.hasPrevious());
            Assert.assertNull(iterator.next());
        }
    }

    @Test(expected = IllegalArgumentException.class)
    public void testJDOMAttributeIterator_nullParent() {
        new JDOMAttributeIterator(null, new QName(""));
    }

    @Test(expected = IllegalArgumentException.class)
    public void testJDOMAttributeIterator_nullName() {
        NodePointer parent = new JDOMNodePointer(null, new Element("root"));
        new JDOMAttributeIterator(parent, null);
    }

    @Test(expected = IndexOutOfBoundsException.class)
    public void testJDOMAttributeIterator_indexOutOfBounds() {
        String xmlns = "http://www.w3.org/2000/xmlns/";
        String prefix = "xml";
        String localName = "attr";
        Element element = new Element("root", "root", new Namespace(xmlns, prefix));
        element.addAttribute(new Attribute(localName, "value1", new Namespace(xmlns, prefix)));
        NodePointer parent = new JDOMNodePointer(null, element);
        JDOMAttributeIterator iterator = new JDOMAttributeIterator(parent, new QName(prefix, localName));
        iterator.remove();
        iterator.next();
        iterator.next();
        NodePointer attributePointer = iterator.next();
        Assert.assertNull(attributePointer);
    }

    @Test
    public void testJDOMAttributeIterator_removeAttribute() {
        String xmlns = "http://www.w3.org/2000/xmlns/";
        String prefix = "xml";
        String localName1 = "attr1";
        String localName2 = "attr2";
        Element element = new Element("root", "root", new Namespace(xmlns, prefix));
        element.addAttribute(new Attribute(localName1, "value1", new Namespace(xmlns, prefix)));
        element.addAttribute(new Attribute(localName2, "value2", new Namespace(xmlns, prefix)));
        NodePointer parent = new JDOMNodePointer(null, element);
        JDOMAttributeIterator iterator = new JDOMAttributeIterator(parent, new QName(prefix, "*"));
        List<NodePointer> attributePointers = new ArrayList<>();
        for (int i = 0; i < 2; i++) {
            NodePointer attributePointer = iterator.next();
            attributePointers.add(attributePointer);
        }
        iterator.remove();
        Assert.assertEquals(1, attributePointers.size());
        assertSame(localName2, attributePointers.get(0).getNode().getName());
        Assert.assertFalse(iterator.hasPrevious());
        Assert.assertNull(iterator.next());
        iterator.remove();
        Assert.assertFalse(iterator.hasNext());
        Assert.assertFalse(iterator.hasPrevious());
    }
}