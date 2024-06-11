package org.apache.commons.jxpath.ri.model.jdom;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

import org.apache.commons.jxpath.ri.model.NodePointer;
import org.jdom.Attribute;
import org.jdom.Element;
import org.jdom.Namespace;
import org.jdom.JDOMException;

import org.junit.Test;

public class JDOMAttributeIteratorTest {

    @Test
    public void testIteratorEmpty() {
        NodePointer parent = new JDOMNodePointer(new Element("root"));
        JDOMAttributeIterator iter = new JDOMAttributeIterator(parent, null);
        assertNull(iter.getNodePointer());
        assertEquals(0, iter.getPosition());
    }

    @Test
    public void testIteratorSingleAttribute() throws JDOMException {
        String xmlns = "xml";
        String prefix = "xml";
        String localName = "foo";
        String value = "bar";

        Element element = new Element("root");
        Attribute attribute = new Attribute(xmlns, localName, value);
        element.addContent(attribute);

        NodePointer parent = new JDOMNodePointer(element);
        JDOMAttributeIterator iter = new JDOMAttributeIterator(parent, new QName(xmlns, localName));
        NodePointer attrPointer = iter.getNodePointer();
        assertEquals(xmlns, attrPointer.getNamespaceURI());
        assertEquals(localName, attrPointer.getLocalName());
        assertEquals(value, attrPointer.getNodeValue());
        assertEquals(1, iter.getPosition());

        attrPointer = iter.getNodePointer(); // should return the same node pointer
        assertEquals(xmlns, attrPointer.getNamespaceURI());
        assertEquals(localName, attrPointer.getLocalName());
        assertEquals(value, attrPointer.getNodeValue());
        assertEquals(1, iter.getPosition());

        assertNull(iter.getNodePointer()); // should be end of iteration
        assertEquals(1, iter.getPosition());
    }

    @Test
    public void testIteratorMultipleAttributes() throws JDOMException {
        String xmlns = "xml";
        String prefix = "xml";
        String localName1 = "foo";
        String value1 = "bar";
        String localName2 = "baz";
        String value2 = "qux";

        Element element = new Element("root");
        Attribute attribute1 = new Attribute(xmlns, localName1, value1);
        Attribute attribute2 = new Attribute(xmlns, localName2, value2);
        element.addContent(attribute1);
        element.addContent(attribute2);

        NodePointer parent = new JDOMNodePointer(element);
        JDOMAttributeIterator iter = new JDOMAttributeIterator(parent, new QName(xmlns, "*"));

        NodePointer attr1Pointer = iter.getNodePointer();
        assertEquals(xmlns, attr1Pointer.getNamespaceURI());
        assertEquals(localName1, attr1Pointer.getLocalName());
        assertEquals(value1, attr1Pointer.getNodeValue());
        assertEquals(1, iter.getPosition());

        NodePointer attr2Pointer = iter.getNodePointer();
        assertEquals(xmlns, attr2Pointer.getNamespaceURI());
        assertEquals(localName2, attr2Pointer.getLocalName());
        assertEquals(value2, attr2Pointer.getNodeValue());
        assertEquals(2, iter.getPosition());

        attr1Pointer = iter.getNodePointer(); // should return the same node pointer
        assertEquals(xmlns, attr1Pointer.getNamespaceURI());
        assertEquals(localName1, attr1Pointer.getLocalName());
        assertEquals(value1, attr1Pointer.getNodeValue());
        assertEquals(2, iter.getPosition());

        attr2Pointer = iter.getNodePointer(); // should return the same node pointer
        assertEquals(xmlns, attr2Pointer.getNamespaceURI());
        assertEquals(localName2, attr2Pointer.getLocalName());
        assertEquals(value2, attr2Pointer.getNodeValue());
        assertEquals(2, iter.getPosition());

        assertNull(iter.getNodePointer()); // should be end of iteration
        assertEquals(2, iter.getPosition());
    }

    @Test
    public void testIteratorNamespace() throws JDOMException {
        String ns1 = "ns1";
        String prefix1 = "p1";
        String localName = "foo";
        String value = "bar";

        Element element = new Element("root");
        element.setNamespace(new Namespace(ns1, prefix1));
        Attribute attribute = new Attribute(ns1, localName, value);
        element.addContent(attribute);

        NodePointer parent = new JDOMNodePointer(element);
        JDOMAttributeIterator iter = new JDOMAttributeIterator(parent, new QName(ns1, localName));
        NodePointer attrPointer = iter.getNodePointer();
        assertEquals(ns1, attrPointer.getNamespaceURI());
        assertEquals(localName, attrPointer.getLocalName());
        assertEquals(value, attrPointer.getNodeValue());
        assertEquals(1, iter.getPosition());

        attrPointer = iter.getNodePointer(); // should return the same node pointer
        assertEquals(ns1, attrPointer.getNamespaceURI());
        assertEquals(localName, attrPointer.getLocalName());
        assertEquals(value, attrPointer.getNodeValue());
        assertEquals(1, iter.getPosition());

        assertNull(iter.getNodePointer()); // should be end of iteration
        assertEquals(1, iter.getPosition());
    }

    @Test
    public void testIteratorNamespaceWithEmptyPrefix() throws JDOMException {
        String ns1 = "ns1";
        String localName = "foo";
        String value = "bar";

        Element element = new Element("root");
        element.setNamespace(new Namespace(ns1, ""));
        Attribute attribute = new Attribute(ns1, localName, value);
        element.addContent(attribute);

        NodePointer parent = new JDOMNodePointer(element);
        JDOMAttributeIterator iter = new JDOMAttributeIterator(parent, new QName(localName));
        NodePointer attrPointer = iter.getNodePointer();
        assertEquals(ns1, attrPointer.getNamespaceURI());
        assertEquals(localName, attrPointer.getLocalName());
        assertEquals(value, attrPointer.getNodeValue());
        assertEquals(1, iter.getPosition());

        attrPointer = iter.getNodePointer(); // should return the same node pointer
        assertEquals(ns1, attrPointer.getNamespaceURI());
        assertEquals(localName, attrPointer.getLocalName());
        assertEquals(value, attrPointer.getNodeValue());
        assertEquals(1, iter.getPosition());

        assertNull(iter.getNodePointer()); // should be end of iteration
        assertEquals(1, iter.getPosition());
    }

    @Test
    public void testIteratorNamespaceWithDefaultPrefix() throws JDOMException {
        String ns1 = "ns1";
        String localName = "foo";
        String value = "bar";

        Element element = new Element("root");
        Attribute attribute = new Attribute(ns1, localName, value);
        element.addContent(attribute);

        NodePointer parent = new JDOMNodePointer(element);
        JDOMAttributeIterator iter = new JDOMAttributeIterator(parent, new QName(localName));
        NodePointer attrPointer = iter.getNodePointer();
        assertEquals(ns1, attrPointer.getNamespaceURI());
        assertEquals(localName, attrPointer.getLocalName());
        assertEquals(value, attrPointer.getNodeValue());
        assertEquals(1, iter.getPosition());

        attrPointer = iter.getNodePointer(); // should return the same node pointer
        assertEquals(ns1, attrPointer.getNamespaceURI());
        assertEquals(localName, attrPointer.getLocalName());
        assertEquals(value, attrPointer.getNodeValue());
        assertEquals(1, iter.getPosition());

        assertNull(iter.getNodePointer()); // should be end of iteration
        assertEquals(1, iter.getPosition());
    }

    @Test
    public void testIteratorNamespaceWithDifferentPrefix() throws JDOMException {
        String ns1 = "ns1";
        String prefix1 = "p1";
        String localName = "foo";
        String value = "bar";

        Element element = new Element("root");
        element.setNamespace(new Namespace(ns1, prefix1));
        Attribute attribute = new Attribute(ns1, localName, value);
        element.addContent(attribute);

        NodePointer parent = new JDOMNodePointer(element);
        JDOMAttributeIterator iter = new JDOMAttributeIterator(parent, new QName("", localName));
        assertNull(iter.getNodePointer()); // should not match attribute with prefix
        assertEquals(0, iter.getPosition());

        iter = new JDOMAttributeIterator(parent, new QName(prefix1, localName));
        NodePointer attrPointer = iter.getNodePointer();
        assertEquals(ns1, attrPointer.getNamespaceURI());
        assertEquals(localName, attrPointer.getLocalName());
        assertEquals(value, attrPointer.getNodeValue());
        assertEquals(1, iter.getPosition());

        attrPointer = iter.getNodePointer(); // should return the same node pointer
        assertEquals(ns1, attrPointer.getNamespaceURI());
        assertEquals(localName, attrPointer.getLocalName());
        assertEquals(value, attrPointer.getNodeValue());
        assertEquals(1, iter.getPosition());

        assertNull(iter.getNodePointer()); // should be end of iteration
        assertEquals(1, iter.getPosition());
    }
}