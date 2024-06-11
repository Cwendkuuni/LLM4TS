package org.apache.commons.jxpath.ri.model.jdom;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertSame;
import static org.junit.Assert.assertTrue;

import org.apache.commons.jxpath.ri.QName;
import org.apache.commons.jxpath.ri.model.NodeIterator;
import org.apache.commons.jxpath.ri.model.NodePointer;
import org.jdom.Attribute;
import org.jdom.Element;
import org.jdom.Namespace;

public class JDOMAttributeIteratorTest {

    @Test
    public void testJDOMAttributeIterator_singleAttribute() {
        Element element = new Element("root");
        Attribute attribute = new Attribute("name", "value", "xmlns:xsi", "http://www.w3.org/2001/XMLSchema-instance");
        element.addAttribute(attribute);

        NodePointer parent = new JDOMElementPointer(new Element("parent"), null);
        QName name = new QName("xmlns:xsi", "name");
        JDOMAttributeIterator iterator = new JDOMAttributeIterator(parent, name);

        NodePointer nodePointer = iterator.getNodePointer();
        Attribute attributeFromIterator = (Attribute) nodePointer.getNode();
        assertEquals(attribute, attributeFromIterator);
        assertSame(attributeFromIterator, iterator.getNodePointer().getNode());
        assertTrue(iterator.hasNext());
        assertFalse(iterator.hasPrevious());

        nodePointer = iterator.next();
        assertNull(nodePointer.getNode());
    }

    @Test
    public void testJDOMAttributeIterator_multipleAttributes() {
        Element element = new Element("root");
        Attribute attribute1 = new Attribute("name1", "value1", "xmlns:xsi", "http://www.w3.org/2001/XMLSchema-instance");
        Attribute attribute2 = new Attribute("name2", "value2", null, null);
        Attribute attribute3 = new Attribute("name3", "value3", "xmlns:xsi", "http://www.w3.org/2001/XMLSchema-instance");
        element.addAttribute(attribute1);
        element.addAttribute(attribute2);
        element.addAttribute(attribute3);

        NodePointer parent = new JDOMElementPointer(new Element("parent"), null);
        QName name1 = new QName("xmlns:xsi", "name1");
        JDOMAttributeIterator iterator1 = new JDOMAttributeIterator(parent, name1);
        assertEquals(attribute1, iterator1.getNodePointer().getNode());
        assertTrue(iterator1.hasNext());
        assertSame(attribute2, iterator1.next().getNode());
        assertSame(attribute3, iterator1.next().getNode());
        assertNull(iterator1.next().getNode());

        QName name2 = new QName(null, "name2");
        JDOMAttributeIterator iterator2 = new JDOMAttributeIterator(parent, name2);
        assertSame(attribute2, iterator2.getNodePointer().getNode());
        assertTrue(iterator2.hasNext());
        assertSame(attribute3, iterator2.next().getNode());
        assertNull(iterator2.next().getNode());
    }

    @Test
    public void testJDOMAttributeIterator_emptyList() {
        Element element = new Element("root");

        NodePointer parent = new JDOMElementPointer(new Element("parent"), null);
        QName name = new QName("", "name");
        JDOMAttributeIterator iterator = new JDOMAttributeIterator(parent, name);
        assertNull(iterator.getNodePointer().getNode());
        assertFalse(iterator.hasNext());
        assertFalse(iterator.hasPrevious());
    }

    @Test
    public void testJDOMAttributeIterator_nullParent() {
        Element element = new Element("root");

        QName name = new QName("xmlns:xsi", "name");
        JDOMAttributeIterator iterator = new JDOMAttributeIterator(null, name);
        assertNull(iterator.getNodePointer().getNode());
        assertFalse(iterator.hasNext());
        assertFalse(iterator.hasPrevious());
    }

    @Test(expected = IllegalArgumentException.class)
    public void testJDOMAttributeIterator_nullName() {
        Element element = new Element("root");

        NodePointer parent = new JDOMElementPointer(new Element("parent"), null);
        JDOMAttributeIterator iterator = new JDOMAttributeIterator(parent, null);
    }

    @Test
    public void testJDOMAttributeIterator_namespaceResolver() {
        Element element = new Element("root");
        Attribute attribute = new Attribute("name", "value", "xmlns:xsi", "http://www.w3.org/2001/XMLSchema-instance");
        element.addAttribute(attribute);

        NodePointer parent = new JDOMElementPointer(new Element("parent"), new NamespaceResolver() {
            @Override
            public String getNamespaceURI(String prefix) {
                return "xmlns:xsi".equals(prefix) ? "http://www.w3.org/2001/XMLSchema-instance" : null;
            }
        });
        QName name = new QName("xmlns:xsi", "name");
        JDOMAttributeIterator iterator = new JDOMAttributeIterator(parent, name);

        NodePointer nodePointer = iterator.getNodePointer();
        Attribute attributeFromIterator = (Attribute) nodePointer.getNode();
        assertEquals(attribute, attributeFromIterator);
        assertSame(attributeFromIterator, iterator.getNodePointer().getNode());
        assertTrue(iterator.hasNext());
        assertFalse(iterator.hasPrevious());

        nodePointer = iterator.next();
        assertNull(nodePointer.getNode());
    }
}