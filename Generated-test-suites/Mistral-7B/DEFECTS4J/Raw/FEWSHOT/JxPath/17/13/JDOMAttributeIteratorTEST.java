package org.apache.commons.jxpath.ri.model.jdom;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertTrue;
import static org.junit.Assert.fail;

import org.apache.commons.jxpath.ri.QName;
import org.jdom.Attribute;
import org.jdom.Element;
import org.jdom.Namespace;

import org.junit.Test;

public class JDOMAttributeIteratorTest {

    @Test
    public void testJDOMAttributeIterator_EmptyElement() {
        NodePointer parent = new JDOMNodePointer(new Element("root"));
        JDOMAttributeIterator iterator = new JDOMAttributeIterator(parent, new QName(""));
        assertNull(iterator.getNodePointer());
    }

    @Test
    public void testJDOMAttributeIterator_EmptyAttributeList() {
        Element root = new Element("root");
        NodePointer parent = new JDOMNodePointer(root);
        QName name = new QName("", "");
        JDOMAttributeIterator iterator = new JDOMAttributeIterator(parent, name);
        assertNull(iterator.getNodePointer());
    }

    @Test
    public void testJDOMAttributeIterator_SingleAttribute() {
        String xmlns = "xmlns";
        String prefix = "x";
        String nsUri = "http://www.w3.org/2000/xmlns/";
        String name = "attr";

        Element root = new Element("root");
        root.setAttribute(new QName(xmlns, prefix, nsUri), "val1");
        NodePointer parent = new JDOMNodePointer(root);
        QName nameQName = new QName(xmlns, prefix, nsUri, name);
        JDOMAttributeIterator iterator = new JDOMAttributeIterator(parent, nameQName);

        Attribute attribute = iterator.getNodePointer().getAttribute();
        assertEquals("val1", attribute.getValue());
        assertEquals(nameQName.getLocalPart(), attribute.getName().getLocalPart());
        assertEquals(nameQName.getNamespaceURI(), attribute.getNamespace().getURI());
        assertEquals(nameQName.getNamespacePrefix(), attribute.getNamespace().getPrefix());
    }

    @Test
    public void testJDOMAttributeIterator_MultipleAttributes() {
        String xmlns = "xmlns";
        String prefix = "x";
        String nsUri = "http://www.w3.org/2000/xmlns/";
        String name1 = "attr1";
        String name2 = "attr2";

        Element root = new Element("root");
        root.setAttribute(new QName(xmlns, prefix, nsUri), "val1");
        root.setAttribute(new QName("", name1), "val2");
        root.setAttribute(new QName("", name2), "val3");
        NodePointer parent = new JDOMNodePointer(root);
        JDOMAttributeIterator iterator = new JDOMAttributeIterator(parent, new QName(""));

        int totalAttributes = 3;
        for (int i = 0; i < totalAttributes; i++) {
            Attribute attribute = iterator.getNodePointer().getAttribute();
            if (attribute == null) {
                fail("Unexpected null attribute");
            }
            if (i == 0) {
                assertEquals(xmlns, attribute.getNamespace().getURI());
                assertEquals(prefix, attribute.getNamespace().getPrefix());
                assertEquals(name1, attribute.getName().getLocalPart());
                assertEquals("val1", attribute.getValue());
            } else if (i == 1) {
                assertEquals("", attribute.getNamespace().getURI());
                assertEquals(name1, attribute.getName().getLocalPart());
                assertEquals("val2", attribute.getValue());
            } else {
                assertEquals("", attribute.getNamespace().getURI());
                assertEquals(name2, attribute.getName().getLocalPart());
                assertEquals("val3", attribute.getValue());
            }
            iterator.next();
        }

        assertFalse(iterator.hasNext());
    }

    @Test
    public void testJDOMAttributeIterator_NamespacePrefix() {
        String xmlns = "xmlns";
        String prefix = "x";
        String nsUri = "http://www.w3.org/2000/xmlns/";
        String name = "attr";

        Element root = new Element("root");
        root.setAttribute(new QName(xmlns, prefix, nsUri), "val1");
        NodePointer parent = new JDOMNodePointer(root);
        QName nameQName = new QName(xmlns, prefix, nsUri, name);
        JDOMAttributeIterator iterator = new JDOMAttributeIterator(parent, nameQName);

        Attribute attribute = iterator.getNodePointer().getAttribute();
        assertEquals("val1", attribute.getValue());
        assertEquals(nameQName.getPrefix(), attribute.getNamespace().getPrefix());
    }

    @Test
    public void testJDOMAttributeIterator_NamespaceURI() {
        String xmlns = "xmlns";
        String prefix = "x";
        String nsUri = "http://www.w3.org/2000/xmlns/";
        String name = "attr";

        Element root = new Element("root");
        root.setAttribute(new QName(xmlns, prefix, nsUri), "val1");
        NodePointer parent = new JDOMNodePointer(root);
        QName nameQName = new QName(xmlns, prefix, nsUri, name);
        JDOMAttributeIterator iterator = new JDOMAttributeIterator(parent, nameQName);

        Attribute attribute = iterator.getNodePointer().getAttribute();
        assertEquals(nsUri, attribute.getNamespace().getURI());
    }

    @Test
    public void testJDOMAttributeIterator_MoveToNext() {
        String xmlns = "xmlns";
        String prefix = "x";
        String nsUri = "http://www.w3.org/2000/xmlns/";
        String name1 = "attr1";
        String name2 = "attr2";

        Element root = new Element("root");
        root.setAttribute(new QName(xmlns, prefix, nsUri), "val1");
        root.setAttribute(new QName("", name1), "val2");
        root.setAttribute(new QName("", name2), "val3");
        NodePointer parent = new JDOMNodePointer(root);
        JDOMAttributeIterator iterator = new JDOMAttributeIterator(parent, new QName(""));

        int totalAttributes = 3;
        for (int i = 0; i < totalAttributes; i++) {
            Attribute attribute = iterator.getNodePointer().getAttribute();
            if (attribute == null) {
                fail("Unexpected null attribute");
            }
            iterator.next();
        }

        assertFalse(iterator.hasNext());
    }

    @Test
    public void testJDOMAttributeIterator_MoveToPrevious() {
        String xmlns = "xmlns";
        String prefix = "x";
        String nsUri = "http://www.w3.org/2000/xmlns/";
        String name1 = "attr1";
        String name2 = "attr2";

        Element root = new Element("root");
        root.setAttribute(new QName(xmlns, prefix, nsUri), "val1");
        root.setAttribute(new QName("", name1), "val2");
        root.setAttribute(new QName("", name2), "val3");
        NodePointer parent = new JDOMNodePointer(root);
        JDOMAttributeIterator iterator = new JDOMAttributeIterator(parent, new QName(""));

        int totalAttributes = 3;
        for (int i = 0; i < totalAttributes; i++) {
            iterator.next();
        }

        iterator.prev();
        Attribute attribute = iterator.getNodePointer().getAttribute();
        assertEquals("val2", attribute.getValue());
    }

    @Test
    public void testJDOMAttributeIterator_MoveToFirst() {
        String xmlns = "xmlns";
        String prefix = "x";
        String nsUri = "http://www.w3.org/2000/xmlns/";
        String name1 = "attr1";
        String name2 = "attr2";

        Element root = new Element("root");
        root.setAttribute(new QName(xmlns, prefix, nsUri), "val1");
        root.setAttribute(new QName("", name1), "val2");
        root.setAttribute(new QName("", name2), "val3");
        NodePointer parent = new JDOMNodePointer(root);
        JDOMAttributeIterator iterator = new JDOMAttributeIterator(parent, new QName(""));

        iterator.next(); // Move to first attribute
        Attribute attribute = iterator.getNodePointer().getAttribute();
        assertEquals("val1", attribute.getValue());
    }

    @Test
    public void testJDOMAttributeIterator_MoveToLast() {
        String xmlns = "xmlns";
        String prefix = "x";
        String nsUri = "http://www.w3.org/2000/xmlns/";
        String name1 = "attr1";
        String name2 = "attr2";

        Element root = new Element("root");
        root.setAttribute(new QName(xmlns, prefix, nsUri), "val1");
        root.setAttribute(new QName("", name1), "val2");
        root.setAttribute(new QName("", name2), "val3");
        NodePointer parent = new JDOMNodePointer(root);
        JDOMAttributeIterator iterator = new JDOMAttributeIterator(parent, new QName(""));

        int totalAttributes = 3;
        for (int i = 0; i < totalAttributes; i++) {
            iterator.next();
        }

        iterator.previous(); // Move to last attribute
        Attribute attribute = iterator.getNodePointer().getAttribute();
        assertEquals("val3", attribute.getValue());
    }
}