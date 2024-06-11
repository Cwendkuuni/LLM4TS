package org.apache.commons.jxpath.ri.model.jdom.test;

import org.apache.commons.jxpath.ri.model.NodeIterator;
import org.apache.commons.jxpath.ri.model.NodePointer;
import org.jdom.Attribute;
import org.jdom.Element;
import org.jdom.Namespace;
import org.junit.Assert;
import org.junit.Test;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertSame;
import static org.junit.Assert.assertTrue;

/**
 * Test class for JDOMAttributeIterator.
 */
public class JDOMAttributeIteratorTest {

    @Test
    public void testJDOMAttributeIterator_empty() {
        NodePointer parent = new JDOMPointer(new Element("root"));
        JDOMAttributeIterator iterator = new JDOMAttributeIterator(parent, null);
        Assert.assertSame(Collections.EMPTY_LIST, iterator.attributes);
        Assert.assertFalse(iterator.hasNext());
    }

    @Test
    public void testJDOMAttributeIterator_singleAttribute() {
        String namespace = "test";
        String prefix = "t";
        String localName = "attribute";

        Element element = new Element("root");
        element.addAttribute("xml:namespace:" + namespace, "http://test.com/" + namespace);
        element.addAttribute(new QName(namespace, localName), "value");

        NodePointer parent = new JDOMPointer(element);
        JDOMAttributeIterator iterator = new JDOMAttributeIterator(parent, new QName(namespace, localName));
        Assert.assertSame(Collections.singletonList(element.getAttribute(localName, Namespace.getNamespace(prefix, namespace))), iterator.attributes);
        Assert.assertTrue(iterator.hasNext());
        Assert.assertSame(element.getAttribute(localName, Namespace.getNamespace(prefix, namespace)), iterator.next());
        Assert.assertFalse(iterator.hasNext());
    }

    @Test
    public void testJDOMAttributeIterator_multipleAttributes() {
        String namespace = "test";
        String prefix = "t";
        String localName = "attribute";

        Element element = new Element("root");
        element.addAttribute("xml:namespace:" + namespace, "http://test.com/" + namespace);
        element.addAttribute(new QName(namespace, localName), "value1");
        element.addAttribute(new QName(namespace, localName), "value2");

        NodePointer parent = new JDOMPointer(element);
        JDOMAttributeIterator iterator = new JDOMAttributeIterator(parent, new QName(namespace, localName));
        Assert.assertSame(new ArrayList<Attribute>() {{
            add(element.getAttribute(localName, Namespace.getNamespace(prefix, namespace)));
            add(element.getAttribute(localName, Namespace.getNamespace(prefix, namespace)));
        }}, iterator.attributes);
        Assert.assertTrue(iterator.hasNext());
        Assert.assertSame(element.getAttribute(localName, Namespace.getNamespace(prefix, namespace)), iterator.next());
        Assert.assertTrue(iterator.hasNext());
        Assert.assertSame(element.getAttribute(localName, Namespace.getNamespace(prefix, namespace)), iterator.next());
        Assert.assertFalse(iterator.hasNext());
    }

    @Test
    public void testJDOMAttributeIterator_noMatchingAttributes() {
        String namespace = "test";
        String prefix = "t";
        String localName = "attribute";

        Element element = new Element("root");
        element.addAttribute("xml:namespace:" + namespace, "http://test.com/" + namespace);

        NodePointer parent = new JDOMPointer(element);
        JDOMAttributeIterator iterator = new JDOMAttributeIterator(parent, new QName(namespace, localName));
        Assert.assertSame(Collections.EMPTY_LIST, iterator.attributes);
        Assert.assertFalse(iterator.hasNext());
    }

    @Test(expected = IllegalArgumentException.class)
    public void testJDOMAttributeIterator_nullParent() {
        new JDOMAttributeIterator(null, new QName("test", "attribute"));
    }

    @Test(expected = IllegalArgumentException.class)
    public void testJDOMAttributeIterator_nullName() {
        NodePointer parent = new JDOMPointer(new Element("root"));
        new JDOMAttributeIterator(parent, null);
    }
}

class JDOMPointer {
    private NodePointer nodePointer;
    private Attribute attribute;

    public JDOMPointer(NodePointer nodePointer) {
        this.nodePointer = nodePointer;
    }

    public JDOMPointer(NodePointer nodePointer, Attribute attribute) {
        this.nodePointer = nodePointer;
        this.attribute = attribute;
    }

    public NodePointer getNodePointer() {
        return nodePointer;
    }

    public Attribute getAttribute() {
        return attribute;
    }
}