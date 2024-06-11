package org.apache.commons.jxpath.ri.model.jdom;

import static org.junit.Assert.*;

import org.apache.commons.jxpath.ri.NodePointer;
import org.apache.commons.jxpath.ri.QName;
import org.apache.commons.jxpath.ri.model.NodeIterator;
import org.apache.commons.jxpath.ri.model.NodePointer;
import org.jdom.Attribute;
import org.jdom.Element;
import org.jdom.Namespace;

import java.util.ArrayList;
import java.util.List;

public class JDOMAttributeIteratorTest {
    @Test
    public void testJDOMAttributeIterator_with_no_attributes() {
        NodePointer parent = new JDOMNodePointer(new Element("root"));
        JDOMAttributeIterator iterator = new JDOMAttributeIterator(parent, new QName("", "*"));
        assertNull(iterator.getNodePointer());
        assertEquals(0, iterator.getPosition());
    }

    @Test
    public void testJDOMAttributeIterator_with_one_attribute() {
        String xmlns = "http://www.w3.org/2000/xmlns/";
        String prefix = "xml";
        QName name = new QName(xmlns + prefix, "foo");

        Element root = new Element("root");
        root.addAttribute(new Attribute("xmlns:" + prefix, xmlns));
        root.addAttribute(new Attribute("foo", "bar"));

        NodePointer parent = new JDOMNodePointer(root);
        JDOMAttributeIterator iterator = new JDOMAttributeIterator(parent, name);
        NodePointer attributePointer = iterator.getNodePointer();
        assertEquals(1, iterator.getPosition());
        assertNotNull(attributePointer);
        assertEquals("bar", attributePointer.getValue());
        assertEquals("foo", attributePointer.getName().getLocalPart());
        assertEquals(xmlns + prefix, attributePointer.getName().getNamespaceURI());
    }

    @Test
    public void testJDOMAttributeIterator_with_multiple_attributes() {
        String xmlns = "http://www.w3.org/2000/xmlns/";
        String prefix = "xml";

        Element root = new Element("root");
        root.addAttribute(new Attribute("xmlns:" + prefix, xmlns));
        root.addAttribute(new Attribute("foo1", "bar1"));
        root.addAttribute(new Attribute("foo2", "bar2"));

        NodePointer parent = new JDOMNodePointer(root);
        JDOMAttributeIterator iterator = new JDOMAttributeIterator(parent, new QName(xmlns + prefix, "*"));
        List<NodePointer> attributePointers = new ArrayList<>();

        for (int i = 0; i < 3; i++) {
            NodePointer attributePointer = iterator.getNodePointer();
            if (attributePointer != null) {
                attributePointers.add(attributePointer);
            }
            iterator.next();
        }

        assertEquals(3, iterator.getPosition());
        assertEquals(3, attributePointers.size());

        NodePointer firstAttributePointer = attributePointers.get(0);
        assertEquals("bar1", firstAttributePointer.getValue());
        assertEquals("foo1", firstAttributePointer.getName().getLocalPart());
        assertEquals(xmlns + prefix, firstAttributePointer.getName().getNamespaceURI());

        NodePointer secondAttributePointer = attributePointers.get(1);
        assertEquals("bar2", secondAttributePointer.getValue());
        assertEquals("foo2", secondAttributePointer.getName().getLocalPart());
        assertEquals(xmlns + prefix, secondAttributePointer.getName().getNamespaceURI());
    }

    @Test
    public void testJDOMAttributeIterator_with_namespaces() {
        String prefix = "ns";
        String uri = "http://example.com/";

        Element root = new Element("root");
        root.addNamespaceDeclaration(new Namespace(prefix, uri));
        root.addAttribute(new Attribute("{http://example.com/}foo", "bar"));

        NodePointer parent = new JDOMNodePointer(root);
        JDOMAttributeIterator iterator = new JDOMAttributeIterator(parent, new QName(prefix, "foo"));
        NodePointer attributePointer = iterator.getNodePointer();
        assertEquals(1, iterator.getPosition());
        assertNotNull(attributePointer);
        assertEquals("bar", attributePointer.getValue());
        assertEquals("foo", attributePointer.getName().getLocalPart());
        assertEquals(uri, attributePointer.getName().getNamespaceURI());
    }

    @Test
    public void testJDOMAttributeIterator_with_wildcard_namespace() {
        String prefix = "xml";
        String uri = "http://www.w3.org/2000/xmlns/";

        Element root = new Element("root");
        root.addNamespaceDeclaration(new Namespace(prefix, uri));
        root.addAttribute(new Attribute("xml:foo", "bar1"));
        root.addAttribute(new Attribute("xml:foo2", "bar2"));

        NodePointer parent = new JDOMNodePointer(root);
        JDOMAttributeIterator iterator = new JDOMAttributeIterator(parent, new QName(prefix, "*"));
        List<NodePointer> attributePointers = new ArrayList<>();

        for (int i = 0; i < 2; i++) {
            NodePointer attributePointer = iterator.getNodePointer();
            if (attributePointer != null) {
                attributePointers.add(attributePointer);
            }
            iterator.next();
        }

        assertEquals(2, iterator.getPosition());
        assertEquals(2, attributePointers.size());

        NodePointer firstAttributePointer = attributePointers.get(0);
        assertEquals("bar1", firstAttributePointer.getValue());
        assertEquals("foo", firstAttributePointer.getName().getLocalPart());
        assertEquals(uri + prefix, firstAttributePointer.getName().getNamespaceURI());

        NodePointer secondAttributePointer = attributePointers.get(1);
        assertEquals("bar2", secondAttributePointer.getValue());
        assertEquals("foo2", secondAttributePointer.getName().getLocalPart());
        assertEquals(uri + prefix, secondAttributePointer.getName().getNamespaceURI());
    }

    @Test
    public void testJDOMAttributeIterator_with_empty_parent_node() {
        NodePointer parent = new JDOMNodePointer(new Element(""));
        JDOMAttributeIterator iterator = new JDOMAttributeIterator(parent, new QName("", "*"));
        assertNull(iterator.getNodePointer());
        assertEquals(0, iterator.getPosition());
    }
}