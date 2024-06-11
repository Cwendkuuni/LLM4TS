package org.apache.commons.jxpath.ri.model.jdom;

import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertSame;

import org.apache.commons.jxpath.ri.NodePointer;
import org.apache.commons.jxpath.ri.model.JDOMAttributePointer;
import org.apache.commons.jxpath.ri.QName;
import org.jdom2.Attribute;
import org.jdom2.Element;
import org.jdom2.Namespace;
import org.junit.Test;

public class JDOMAttributeIteratorTest {

    @Test
    public void testCreateIteratorWithEmptyElement() {
        NodePointer parent = new JDOMNodePointer(new Element("empty"));
        JDOMAttributeIterator iterator = new JDOMAttributeIterator(parent, null);
        assertNull(iterator.getNodePointer());
        assertEquals(0, iterator.getPosition());
    }

    @Test
    public void testCreateIteratorWithSingleAttribute() {
        String namespaceURI = "http://example.com/ns";
        String attributeName = "attribute1";

        Element element = new Element("element");
        element.addAttribute(new Attribute("attribute1", "value1", new Namespace(namespaceURI)));

        NodePointer parent = new JDOMNodePointer(element);
        JDOMAttributeIterator iterator = new JDOMAttributeIterator(parent, new QName(namespaceURI, attributeName));

        NodePointer attributePointer = iterator.getNodePointer();
        assertSame(attributePointer, new JDOMAttributePointer(parent, (Attribute) element.getAttribute("attribute1")));
        assertEquals(1, iterator.getPosition());
    }

    @Test
    public void testCreateIteratorWithMultipleAttributes() {
        String namespaceURI = "http://example.com/ns";
        String attributeName1 = "attribute1";
        String attributeName2 = "attribute2";

        Element element = new Element("element");
        element.addAttribute(new Attribute("attribute1", "value1", new Namespace(namespaceURI)));
        element.addAttribute(new Attribute("attribute2", "value2", new Namespace(namespaceURI)));

        NodePointer parent = new JDOMNodePointer(element);
        JDOMAttributeIterator iterator = new JDOMAttributeIterator(parent, new QName(namespaceURI, "*"));

        List<NodePointer> attributePointers = new ArrayList<>();
        for (int i = 0; i < iterator.getSize(); i++) {
            NodePointer attributePointer = iterator.getNodePointer();
            assertSame(attributePointer, new JDOMAttributePointer(parent, (Attribute) element.getAttribute(attributePointer.getName().getName())));
            attributePointers.add(attributePointer);
            iterator.next();
        }

        assertEquals(2, iterator.getPosition());
        assertEquals(attributePointers.size(), iterator.getSize());
        assertSame(attributePointers.get(0), attributePointers.get(1));
    }

    @Test
    public void testCreateIteratorWithEmptyNamespace() {
        String attributeName = "attribute1";

        Element element = new Element("element");
        element.addAttribute(new Attribute("attribute1", "value1"));

        NodePointer parent = new JDOMNodePointer(element);
        JDOMAttributeIterator iterator = new JDOMAttributeIterator(parent, new QName("", attributeName));

        NodePointer attributePointer = iterator.getNodePointer();
        assertSame(attributePointer, new JDOMAttributePointer(parent, (Attribute) element.getAttribute("attribute1")));
        assertEquals(1, iterator.getPosition());
    }

    @Test
    public void testCreateIteratorWithNamespace() {
        String namespaceURI = "http://example.com/ns";
        String attributeName = "attribute1";

        Element element = new Element("element");
        element.addNamespaceDeclaration(new Namespace("xml", "http://www.w3.org/XML/1998/namespace"));
        element.addNamespaceDeclaration(new Namespace(namespaceURI));
        element.addAttribute(new Attribute("xml:attribute1", "value1", Namespace.XML_NAMESPACE));
        element.addAttribute(new Attribute("ns:attribute1", "value2", new Namespace(namespaceURI)));

        NodePointer parent = new JDOMNodePointer(element);
        JDOMAttributeIterator iterator = new JDOMAttributeIterator(parent, new QName(namespaceURI, attributeName));

        NodePointer attributePointer = iterator.getNodePointer();
        assertSame(attributePointer, new JDOMAttributePointer(parent, (Attribute) element.getAttribute("ns:attribute1")));
        assertEquals(1, iterator.getPosition());
    }

    @Test
    public void testCreateIteratorWithNoMatchingAttributes() {
        String namespaceURI = "http://example.com/ns";
        String attributeName = "attribute1";

        Element element = new Element("element");
        element.addNamespaceDeclaration(new Namespace("xml", "http://www.w3.org/XML/1998/namespace"));
        element.addNamespaceDeclaration(new Namespace(namespaceURI));

        NodePointer parent = new JDOMNodePointer(element);
        JDOMAttributeIterator iterator = new JDOMAttributeIterator(parent, new QName(namespaceURI, attributeName));

        assertNull(iterator.getNodePointer());
        assertEquals(0, iterator.getPosition());
    }
}