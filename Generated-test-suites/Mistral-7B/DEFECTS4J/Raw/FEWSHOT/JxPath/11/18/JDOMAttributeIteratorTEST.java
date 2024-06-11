package org.apache.commons.jxpath.ri.model.jdom;

import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertEquals;

import org.apache.commons.jxpath.ri.QName;
import org.apache.commons.jxpath.ri.model.NodeIterator;
import org.apache.commons.jxpath.ri.model.NodePointer;
import org.jdom.Attribute;
import org.jdom.Element;
import org.jdom.Namespace;

public class JDOMAttributeIteratorTest {

    @Test
    public void testJDOMAttributeIterator_SingleAttribute() {
        Element rootElement = new Element("root");
        rootElement.addAttribute("attr1", "value1");

        NodePointer parent = new JDOMNodePointer(new JDOMNode(rootElement));
        QName name = new QName("*", null);

        JDOMAttributeIterator iterator = new JDOMAttributeIterator(parent, name);

        NodePointer nodePointer = iterator.getNodePointer();
        assertNotNull(nodePointer);
        assertEquals("attr1", nodePointer.getName().getLocalPart());
        assertEquals("value1", nodePointer.getValue());

        assertNull(iterator.getNodePointer());
        assertNull(iterator.getNodePointer());
    }

    @Test
    public void testJDOMAttributeIterator_MultipleAttributes() {
        Element rootElement = new Element("root");
        rootElement.addAttribute("attr1", "value1");
        rootElement.addAttribute("attr2", "value2");

        NodePointer parent = new JDOMNodePointer(new JDOMNode(rootElement));
        QName name = new QName("*", null);

        JDOMAttributeIterator iterator = new JDOMAttributeIterator(parent, name);

        NodePointer nodePointer1 = iterator.getNodePointer();
        assertNotNull(nodePointer1);
        assertEquals("attr1", nodePointer1.getName().getLocalPart());
        assertEquals("value1", nodePointer1.getValue());

        NodePointer nodePointer2 = iterator.getNodePointer();
        assertNotNull(nodePointer2);
        assertEquals("attr2", nodePointer2.getName().getLocalPart());
        assertEquals("value2", nodePointer2.getValue());

        assertNull(iterator.getNodePointer());
        assertNull(iterator.getNodePointer());
    }

    @Test
    public void testJDOMAttributeIterator_Namespace() {
        Element rootElement = new Element("root");
        rootElement.addNamespaceDeclaration("ns1", "http://example.com/ns1");
        rootElement.addAttribute("attr1", "value1", "ns1:prefix1");
        rootElement.addAttribute("attr2", "value2", "ns1:prefix2");

        NodePointer parent = new JDOMNodePointer(new JDOMNode(rootElement));
        QName name = new QName("ns1:prefix1", "http://example.com/ns1", "*");

        JDOMAttributeIterator iterator = new JDOMAttributeIterator(parent, name);

        NodePointer nodePointer = iterator.getNodePointer();
        assertNotNull(nodePointer);
        assertEquals("attr1", nodePointer.getName().getLocalPart());
        assertEquals("value1", nodePointer.getValue());

        assertNull(iterator.getNodePointer());
        assertNull(iterator.getNodePointer());
    }

    @Test
    public void testJDOMAttributeIterator_EmptyList() {
        Element rootElement = new Element("root");

        NodePointer parent = new JDOMNodePointer(new JDOMNode(rootElement));
        QName name = new QName("*", null);

        JDOMAttributeIterator iterator = new JDOMAttributeIterator(parent, name);

        assertNull(iterator.getNodePointer());
        assertNull(iterator.getNodePointer());
    }

    @Test(expected = IllegalArgumentException.class)
    public void testJDOMAttributeIterator_NullParent() {
        NodePointer parent = null;
        QName name = new QName("attr", null);

        new JDOMAttributeIterator(parent, name);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testJDOMAttributeIterator_NullName() {
        NodePointer parent = new JDOMNodePointer(new JDOMNode(new Element("root")));
        QName name = null;

        new JDOMAttributeIterator(parent, name);
    }
}