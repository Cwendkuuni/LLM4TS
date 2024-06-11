package org.apache.commons.jxpath.ri.model.jdom;

import org.apache.commons.jxpath.ri.QName;
import org.apache.commons.jxpath.ri.model.NodeIterator;
import org.apache.commons.jxpath.ri.model.NodePointer;
import org.jdom.Attribute;
import org.jdom.Element;
import org.jdom.Namespace;
import org.junit.Assert;
import org.junit.Test;

import java.util.ArrayList;
import java.util.List;

/**
 * JDOMAttributeIteratorTest class to test JDOMAttributeIterator methods.
 */
public class JDOMAttributeIteratorTest {

    @Test
    public void testEmptyIterator() {
        NodePointer parent = new JDOMNodePointer(new Element("root"));
        JDOMAttributeIterator iterator = new JDOMAttributeIterator(parent, null);
        Assert.assertFalse(iterator.hasNext());
    }

    @Test
    public void testSingleAttributeIterator() {
        String xmlns = "xml";
        String ns = "namespace";
        String attributeName = "testAttribute";
        Namespace xmlNamespace = Namespace.getNamespace(xmlns);
        Namespace namespace = new Namespace(ns, "namespaceUri");
        Element rootElement = new Element("root", "rootName");
        rootElement.setNamespace(xmlNamespace);
        rootElement.setNamespaceBinding(ns, namespace);
        rootElement.addAttribute(new Attribute("testAttribute", "testValue", xmlNamespace));
        NodePointer parent = new JDOMNodePointer(rootElement);
        JDOMAttributeIterator iterator = new JDOMAttributeIterator(parent, new QName(ns, attributeName));
        Assert.assertTrue(iterator.hasNext());
        NodePointer nodePointer = iterator.next();
        Assert.assertEquals(new JDOMAttributePointer(parent, new Attribute("testAttribute", "testValue", xmlNamespace)), nodePointer);
        Assert.assertFalse(iterator.hasNext());
    }

    @Test
    public void testMultipleAttributesIterator() {
        String xmlns = "xml";
        String ns = "namespace";
        String attributeName1 = "testAttribute1";
        String attributeName2 = "testAttribute2";
        Namespace xmlNamespace = Namespace.getNamespace(xmlns);
        Namespace namespace = new Namespace(ns, "namespaceUri");
        Element rootElement = new Element("root", "rootName");
        rootElement.setNamespace(xmlNamespace);
        rootElement.setNamespaceBinding(ns, namespace);
        rootElement.addAttribute(new Attribute("testAttribute1", "testValue1", xmlNamespace));
        rootElement.addAttribute(new Attribute("testAttribute2", "testValue2", xmlNamespace));
        NodePointer parent = new JDOMNodePointer(rootElement);
        JDOMAttributeIterator iterator = new JDOMAttributeIterator(parent, new QName(ns, "*"));
        List<NodePointer> nodePointers = new ArrayList<>();
        for (int i = 0; i < iterator.size(); i++) {
            nodePointers.add(iterator.next());
        }
        Assert.assertEquals(2, nodePointers.size());
        Assert.assertTrue(iterator.hasNext());
        Assert.assertFalse(iterator.hasPrevious());
        for (NodePointer nodePointer : nodePointers) {
            Assert.assertTrue(nodePointer instanceof JDOMAttributePointer);
        }
    }

    @Test
    public void testIteratorPosition() {
        String xmlns = "xml";
        String ns = "namespace";
        String attributeName = "testAttribute";
        Namespace xmlNamespace = Namespace.getNamespace(xmlns);
        Namespace namespace = new Namespace(ns, "namespaceUri");
        Element rootElement = new Element("root", "rootName");
        rootElement.setNamespace(xmlNamespace);
        rootElement.setNamespaceBinding(ns, namespace);
        rootElement.addAttribute(new Attribute("testAttribute1", "testValue1", xmlNamespace));
        rootElement.addAttribute(new Attribute("testAttribute", "testValue", xmlNamespace));
        rootElement.addAttribute(new Attribute("testAttribute2", "testValue2", xmlNamespace));
        NodePointer parent = new JDOMNodePointer(rootElement);
        JDOMAttributeIterator iterator = new JDOMAttributeIterator(parent, new QName(ns, attributeName));
        List<NodePointer> nodePointers = new ArrayList<>();
        for (int i = 0; i < 3; i++) {
            nodePointers.add(iterator.next());
            iterator.previous();
        }
        Assert.assertEquals(2, nodePointers.size());
        Assert.assertEquals(new JDOMAttributePointer(parent, new Attribute("testAttribute1", "testValue1", xmlNamespace)), nodePointers.get(0));
        Assert.assertEquals(new JDOMAttributePointer(parent, new Attribute("testAttribute", "testValue", xmlNamespace)), nodePointers.get(1));
    }

    @Test(expected = IllegalStateException.class)
    public void testIteratorWithoutParent() {
        String ns = "namespace";
        String attributeName = "testAttribute";
        NodePointer parent = null;
        JDOMAttributeIterator iterator = new JDOMAttributeIterator(parent, new QName(ns, attributeName));
    }

    @Test(expected = IllegalArgumentException.class)
    public void testIteratorWithInvalidName() {
        String ns = "namespace";
        String invalidName = null;
        NodePointer parent = new JDOMNodePointer(new Element("root"));
        JDOMAttributeIterator iterator = new JDOMAttributeIterator(parent, new QName(ns, invalidName));
    }
}