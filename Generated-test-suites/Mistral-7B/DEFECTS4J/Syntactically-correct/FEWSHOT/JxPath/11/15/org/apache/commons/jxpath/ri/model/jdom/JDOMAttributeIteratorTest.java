package org.apache.commons.cli;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertTrue;

import org.apache.commons.jxpath.ri.model.NodePointer;
import org.jdom.Attribute;
import org.jdom.Element;
import org.jdom.Namespace;
import org.jdom.JDOMException;

import java.util.ArrayList;
import java.util.List;

public class JDOMAttributeIteratorTest {

    @Test
    public void testEmptyIterator() {
        NodePointer parent = new JDOMPointer(new Element("root"));
        JDOMAttributeIterator iterator = new JDOMAttributeIterator(parent, new QName(""));
        assertFalse(iterator.hasNext());
        assertNull(iterator.next());
    }

    @Test
    public void testSingleAttributeIterator() throws JDOMException {
        String xmlns = "xmlns";
        String prefix = "x";
        String localName = "attr";

        Element root = new Element("root");
        root.addNamespaceDeclaration(xmlns, "http://example.com/xmlns");
        root.addAttribute(new Attribute(xmlns, ":prefix", prefix));
        root.addAttribute(new Attribute("localName", "value"));

        NodePointer parent = new JDOMPointer(root);
        JDOMAttributeIterator iterator = new JDOMAttributeIterator(parent, new QName(prefix, localName));

        assertTrue(iterator.hasNext());
        NodePointer next = iterator.next();
        assertEquals(next.getNode(), root.getAttribute("localName"));
        assertEquals(next.getParent(), parent);
        assertEquals(next.getDepth(), 1);
        assertEquals(next.getValue(), "value");
        assertEquals(next.getQName().getLocalPart(), localName);
        assertEquals(next.getQName().getPrefix(), prefix);

        assertTrue(iterator.hasNext());
        next = iterator.next();
        assertEquals(next.getNode(), root.getAttribute("xmlns:" + xmlns));
        assertEquals(next.getParent(), parent);
        assertEquals(next.getDepth(), 1);
        assertEquals(next.getValue(), prefix);
        assertEquals(next.getQName().getLocalPart(), ":prefix");
        assertEquals(next.getQName().getPrefix(), null);

        assertFalse(iterator.hasNext());
    }

    @Test
    public void testMultipleAttributesIterator() throws JDOMException {
        String xmlns = "xmlns";
        String prefix = "x";
        String localName1 = "attr1";
        String localName2 = "attr2";

        Element root = new Element("root");
        root.addNamespaceDeclaration(xmlns, "http://example.com/xmlns");
        root.addAttribute(new Attribute(xmlns, ":prefix", prefix));
        Attribute attr1 = new Attribute("localName1", "value1");
        Attribute attr2 = new Attribute("localName2", "value2");
        root.addContent(new Element("child").addContent(attr1));
        root.addContent(new Element("child").addContent(attr2));

        NodePointer parent = new JDOMPointer(root);
        JDOMAttributeIterator iterator = new JDOMAttributeIterator(parent, new QName(prefix, "*"));

        assertTrue(iterator.hasNext());
        NodePointer next = iterator.next();
        assertEquals(next.getNode(), attr1);
        assertEquals(next.getParent(), parent.getNode().getContent().get(0));
        assertEquals(next.getDepth(), 2);
        assertEquals(next.getValue(), "value1");
        assertEquals(next.getQName().getLocalPart(), localName1);
        assertEquals(next.getQName().getPrefix(), prefix);

        assertTrue(iterator.hasNext());
        next = iterator.next();
        assertEquals(next.getNode(), attr2);
        assertEquals(next.getParent(), parent.getNode().getContent().get(1));
        assertEquals(next.getDepth(), 2);
        assertEquals(next.getValue(), "value2");
        assertEquals(next.getQName().getLocalPart(), localName2);
        assertEquals(next.getQName().getPrefix(), null);

        assertFalse(iterator.hasNext());
    }

    @Test
    public void testIteratorWithNamespacePrefix() throws JDOMException {
        String xmlns = "xmlns";
        String prefix = "x";
        String localName = "attr";

        Element root = new Element("root");
        root.addNamespaceDeclaration(xmlns, "http://example.com/xmlns");
        root.addAttribute(new Attribute("localName", "value"));
        root.addAttribute(new Attribute("xmlns:" + xmlns, prefix));

        NodePointer parent = new JDOMPointer(root);
        JDOMAttributeIterator iterator = new JDOMAttributeIterator(parent, new QName(prefix, localName));

        assertTrue(iterator.hasNext());
        NodePointer next = iterator.next();
        assertEquals(next.getNode(), root.getAttribute("localName"));
        assertEquals(next.getParent(), parent);
        assertEquals(next.getDepth(), 1);
        assertEquals(next.getValue(), "value");
        assertEquals(next.getQName().getLocalPart(), localName);
        assertEquals(next.getQName().getPrefix(), null);

        assertFalse(iterator.hasNext());
    }
}