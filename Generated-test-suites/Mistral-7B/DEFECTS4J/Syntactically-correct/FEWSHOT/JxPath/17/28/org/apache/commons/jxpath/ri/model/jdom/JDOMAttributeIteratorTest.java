package org.apache.commons.cli;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertTrue;

import org.apache.commons.jxpath.ri.model.NodePointer;
import org.jdom.Attribute;
import org.jdom.Element;
import org.jdom.Namespace;

import java.util.ArrayList;
import java.util.List;

public class JDOMAttributeIteratorTest {
    @Test
    public void testJDOMAttributeIterator() {
        Element root = new Element("root");
        root.addAttribute("attr1", "value1");
        root.addAttribute("attr2", "value2");

        Namespace ns = Namespace.getNamespace("ns", "namespace-uri");
        root.addNamespaceDeclaration(ns.getURI(), ns.getPrefix());
        root.addAttribute("attr3", "value3", ns);

        QName name1 = new QName("attr1");
        QName name2 = new QName("attr2");
        QName name3 = new QName("attr3", "ns");

        NodePointer parent = new NodePointer(root);

        List<Attribute> attributes1 = new ArrayList<>();
        List<Attribute> attributes2 = new ArrayList<>();
        List<Attribute> attributes3 = new ArrayList<>();

        List<Attribute> expectedAttributes1 = Arrays.asList(root.getAttribute("attr1"), root.getAttribute("attr2"));
        List<Attribute> expectedAttributes2 = Arrays.asList(root.getAttribute("attr3"));

        // Test with empty namespace
        JDOMAttributeIterator iterator1 = new JDOMAttributeIterator(parent, name1);
        List<NodePointer> result1 = new ArrayList<>();
        for (int i = 0; i < iterator1.getSize(); i++) {
            result1.add(iterator1.getNodePointer());
            iterator1.next();
        }
        assertEquals(expectedAttributes1.size(), result1.size());
        for (int i = 0; i < result1.size(); i++) {
            Attribute attribute = (Attribute) result1.get(i).getNode();
            Attribute expectedAttribute = expectedAttributes1.get(i);
            assertEquals(expectedAttribute.getName(), attribute.getName());
            assertEquals(expectedAttribute.getValue(), attribute.getValue());
            assertEquals(expectedAttribute.getNamespace(), attribute.getNamespace());
        }

        // Test with specific namespace
        JDOMAttributeIterator iterator2 = new JDOMAttributeIterator(parent, name3);
        List<NodePointer> result2 = new ArrayList<>();
        for (int i = 0; i < iterator2.getSize(); i++) {
            result2.add(iterator2.getNodePointer());
            iterator2.next();
        }
        assertEquals(expectedAttributes2.size(), result2.size());
        for (int i = 0; i < result2.size(); i++) {
            Attribute attribute = (Attribute) result2.get(i).getNode();
            Attribute expectedAttribute = expectedAttributes2.get(i);
            assertEquals(expectedAttribute.getName(), attribute.getName());
            assertEquals(expectedAttribute.getValue(), attribute.getValue());
            assertEquals(expectedAttribute.getNamespaceURI(), attribute.getNamespaceURI());
            assertEquals(expectedAttribute.getNamespacePrefix(), attribute.getNamespacePrefix());
        }

        // Test with wildcard namespace
        JDOMAttributeIterator iterator3 = new JDOMAttributeIterator(parent, new QName("*"));
        List<NodePointer> result3 = new ArrayList<>();
        for (int i = 0; i < iterator3.getSize(); i++) {
            result3.add(iterator3.getNodePointer());
            iterator3.next();
        }
        assertEquals(expectedAttributes1.size() + expectedAttributes2.size(), result3.size());
        for (int i = 0; i < result3.size(); i++) {
            Attribute attribute = (Attribute) result3.get(i).getNode();
            Attribute expectedAttribute;
            if (i < expectedAttributes1.size()) {
                expectedAttribute = expectedAttributes1.get(i);
            } else {
                expectedAttribute = expectedAttributes2.get(i - expectedAttributes1.size());
            }
            assertEquals(expectedAttribute.getName(), attribute.getName());
            assertEquals(expectedAttribute.getValue(), attribute.getValue());
            assertEquals(expectedAttribute.getNamespace(), attribute.getNamespace());
        }
    }

    @Test
    public void testGetNodePointer() {
        Element root = new Element("root");
        root.addAttribute("attr1", "value1");
        root.addAttribute("attr2", "value2");

        Namespace ns = Namespace.getNamespace("ns", "namespace-uri");
        root.addNamespaceDeclaration(ns.getURI(), ns.getPrefix());
        root.addAttribute("attr3", "value3", ns);

        QName name1 = new QName("attr1");
        QName name2 = new QName("attr2");
        QName name3 = new QName("attr3", "ns");

        NodePointer parent = new NodePointer(root);

        List<Attribute> attributes1 = new ArrayList<>();
        List<Attribute> attributes2 = new ArrayList<>();
        List<Attribute> attributes3 = new ArrayList<>();

        List<Attribute> expectedAttributes1 = Arrays.asList(root.getAttribute("attr1"), root.getAttribute("attr2"));
        List<Attribute> expectedAttributes2 = Arrays.asList(root.getAttribute("attr3"));

        JDOMAttributeIterator iterator1 = new JDOMAttributeIterator(parent, name1);
        NodePointer nodePointer1 = iterator1.getNodePointer();
        Attribute expectedAttribute1 = expectedAttributes1.get(0);
        assertEquals(nodePointer1.getNode(), expectedAttribute1);
        assertEquals(nodePointer1.getPath(), "/root/@attr1");

        iterator1.next();
        Attribute expectedAttribute2 = expectedAttributes1.get(1);
        NodePointer nodePointer2 = iterator1.getNodePointer();
        assertEquals(nodePointer2.getNode(), expectedAttribute2);
        assertEquals(nodePointer2.getPath(), "/root/@attr2");

        JDOMAttributeIterator iterator2 = new JDOMAttributeIterator(parent, name3);
        NodePointer nodePointer3 = iterator2.getNodePointer();
        Attribute expectedAttribute3 = expectedAttributes2.get(0);
        assertEquals(nodePointer3.getNode(), expectedAttribute3);
        assertEquals(nodePointer3.getPath(), "/root/@attr3");

        iterator2.next();
        assertNull(iterator2.getNodePointer());
    }

    @Test
    public void testGetPosition() {
        Element root = new Element("root");
        root.addAttribute("attr1", "value1");
        root.addAttribute("attr2", "value2");

        Namespace ns = Namespace.getNamespace("ns", "namespace-uri");
        root.addNamespaceDeclaration(ns.getURI(), ns.getPrefix());
        root.addAttribute("attr3", "value3", ns);

        QName name1 = new QName("attr1");
        QName name2 = new QName("attr2");
        QName name3 = new QName("attr3", "ns");

        NodePointer parent = new NodePointer(root);

        List<Attribute> attributes1 = new ArrayList<>();
        List<Attribute> attributes2 = new ArrayList<>();
        List<Attribute> attributes3 = new ArrayList<>();

        List<Attribute> expectedAttributes1 = Arrays.asList(root.getAttribute("attr1"), root.getAttribute("attr2"));
        List<Attribute> expectedAttributes2 = Arrays.asList(root.getAttribute("attr3"));

        JDOMAttributeIterator iterator1 = new JDOMAttributeIterator(parent, name1);
        assertEquals(0, iterator1.getPosition());

        iterator1.next();
        assertEquals(1, iterator1.getPosition());

        iterator1.next();
        assertEquals(2, iterator1.getPosition());

        iterator1.next();
        assertNull(iterator1.getNodePointer());
        assertEquals(-1, iterator1.getPosition());

        JDOMAttribeIterator iterator2 = new JDOMAttributeIterator(parent, name3);
        assertEquals(0, iterator2.getPosition());

        iterator2.next();
        assertEquals(1, iterator2.getPosition());

        iterator2.next();
        assertNull(iterator2.getNodePointer());
        assertEquals(-1, iterator2.getPosition());
    }

    @Test
    public void testSetPosition() {
        Element root = new Element("root");
        root.addAttribute("attr1", "value1");
        root.addAttribute("attr2", "value2");

        Namespace ns = Namespace.getNamespace("ns", "namespace-uri");
        root.addNamespaceDeclaration(ns.getURI(), ns.getPrefix());
        root.addAttribute("attr3", "value3", ns);

        QName name1 = new QName("attr1");
        QName name2 = new QName("attr2");
        QName name3 = new QName("attr3", "ns");

        NodePointer parent = new NodePointer(root);

        List<Attribute> attributes1 = new ArrayList<>();
        List<Attribute> attributes2 = new ArrayList<>();
        List<Attribute> attributes3 = new ArrayList<>();

        List<Attribute> expectedAttributes1 = Arrays.asList(root.getAttribute("attr1"), root.getAttribute("attr2"));
        List<Attribute> expectedAttributes2 = Arrays.asList(root.getAttribute("attr3"));

        JDOMAttributeIterator iterator1 = new JDOMAttributeIterator(parent, name1);

        // Test setting position to valid index
        iterator1.setPosition(1);
        assertEquals(1, iterator1.getPosition());

        // Test setting position to invalid index
        iterator1.setPosition(3);
        assertEquals(1, iterator1.getPosition());

        // Test setting position to position 0 with an iterator that already on position 0
        iterator1.next();
        assertEquals(1, iterator1.getPosition());
        iterator1.setPosition(0);
        assertEquals(0, iterator1.getPosition());

        // Test setting position to position greater than size with an iterator that already on last position
        iterator1.next();
        assertEquals(2, iterator1.getPosition());
        iterator1.setPosition(3);
        assertEquals(2, iterator1.getPosition());

        // Test setting position to position less than 0 with an iterator that already on position 0
        iterator1.next();
        assertEquals(2, iterator1.getPosition());
        iterator1.setPosition(-1);
        assertEquals(2, iterator1.getPosition());

        JDOMAttributeIterator iterator2 = new JDOMAttributeIterator(parent, name3);

        // Test setting position to valid index
        iterator2.setPosition(1);
        assertEquals(1, iterator2.getPosition());

        // Test setting position to invalid index
        iterator2.setPosition(3);
        assertEquals(1, iterator2.getPosition());

        // Test setting position to position 0 with an iterator that already on position 0
        iterator2.next();
        assertEquals(1, iterator2.getPosition());
        iterator2.setPosition(0);
        assertEquals(0, iterator2.getPosition());

        // Test setting position to position greater than size with an iterator that already on last position
        iterator2.next();
        assertEquals(2, iterator2.getPosition());
        iterator2.setPosition(3);
        assertEquals(2, iterator2.getPosition());

        // Test setting position to position less than 0 with an iterator that already on position 0
        iterator2.next();
        assertEquals(2, iterator2.getPosition());
        iterator2.setPosition(-1);
        assertEquals(2, iterator2.getPosition());
    }
}