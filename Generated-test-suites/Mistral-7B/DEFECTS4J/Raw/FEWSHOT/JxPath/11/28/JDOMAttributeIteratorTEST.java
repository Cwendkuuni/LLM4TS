package org.apache.commons.jxpath.ri.model.jdom;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertTrue;

import org.apache.commons.jxpath.ri.QName;
import org.jdom.Attribute;
import org.jdom.Element;
import org.jdom.Namespace;

import org.junit.Test;

public class JDOMAttributeIteratorTest {

    @Test
    public void testJDOMAttributeIterator_Empty() {
        Element root = new Element("root");
        NodePointer parent = new NodePointer(root);
        JDOMAttributeIterator iterator = new JDOMAttributeIterator(parent, null);
        assertNull(iterator.getNodePointer());
        assertFalse(iterator.hasNext());
    }

    @Test
    public void testJDOMAttributeIterator_SingleAttribute() {
        Element root = new Element("root");
        Attribute attribute = new Attribute("name", "value", "ns");
        root.addContent(attribute);
        NodePointer parent = new NodePointer(root);
        QName name = new QName("ns:name");
        JDOMAttributeIterator iterator = new JDOMAttributeIterator(parent, name);
        JDOMAttributePointer actual = (JDOMAttributePointer) iterator.getNodePointer();
        assertEquals(attribute, actual.getAttribute());
        assertEquals("value", actual.getValue());
        assertEquals("ns", actual.getNamespaceURI());
        assertTrue(iterator.hasNext());
        assertFalse(iterator.hasPrevious());
    }

    @Test
    public void testJDOMAttributeIterator_MultipleAttributes() {
        Element root = new Element("root");
        Attribute attr1 = new Attribute("name1", "value1", "ns1");
        Attribute attr2 = new Attribute("name2", "value2", "ns2");
        Attribute attr3 = new Attribute("name1", "value3", "ns1");
        root.addContent(attr1);
        root.addContent(attr2);
        root.addContent(attr3);
        NodePointer parent = new NodePointer(root);
        JDOMAttributeIterator iterator = new JDOMAttributeIterator(parent, null);
        JDOMAttributePointer actual1 = (JDOMAttributePointer) iterator.next();
        assertEquals(attr1, actual1.getAttribute());
        assertEquals("value1", actual1.getValue());
        assertEquals("ns1", actual1.getNamespaceURI());
        JDOMAttributePointer actual2 = (JDOMAttributePointer) iterator.next();
        assertEquals(attr2, actual2.getAttribute());
        assertEquals("value2", actual2.getValue());
        assertEquals("ns2", actual2.getNamespaceURI());
        JDOMAttributePointer actual3 = (JDOMAttributePointer) iterator.next();
        assertEquals(attr3, actual3.getAttribute());
        assertEquals("value3", actual3.getValue());
        assertEquals("ns1", actual3.getNamespaceURI());
        assertFalse(iterator.hasNext());
        assertFalse(iterator.hasPrevious());
    }

    @Test
    public void testJDOMAttributeIterator_Namespace() {
        Element root = new Element("root");
        Attribute attr1 = new Attribute("prefix:name1", "value1", new Namespace("prefix", "prefixURI"));
        Attribute attr2 = new Attribute(null, "name2", new Namespace("ns2", "ns2URI"));
        Attribute attr3 = new Attribute("prefix:name1", "value3", new Namespace("prefix", "prefixURI"));
        root.addContent(attr1);
        root.addContent(attr2);
        root.addContent(attr3);
        NodePointer parent = new NodePointer(root);
        QName name1 = new QName("prefix:name1");
        JDOMAttributeIterator iterator1 = new JDOMAttributeIterator(parent, name1);
        assertEquals(attr1, iterator1.getNodePointer().getAttribute());
        assertEquals("value1", iterator1.getValue());
        assertEquals("prefixURI", iterator1.getNamespaceURI());
        JDOMAttributeIterator iterator2 = new JDOMAttributeIterator(parent, new QName("ns2", "name2"));
        assertFalse(iterator2.hasNext());
        assertFalse(iterator2.hasPrevious());
    }

    @Test
    public void testJDOMAttributeIterator_EmptyNamespace() {
        Element root = new Element("root");
        Attribute attr1 = new Attribute("name1", "value1");
        Attribute attr2 = new Attribute("name2", "value2");
        root.addContent(attr1);
        root.addContent(attr2);
        NodePointer parent = new NodePointer(root);
        JDOMAttributeIterator iterator = new JDOMAttributeIterator(parent, null);
        assertFalse(iterator.hasNext());
        assertFalse(iterator.hasPrevious());
    }

    @Test(expected = IllegalArgumentException.class)
    public void testJDOMAttributeIterator_NullParent() {
        QName name = new QName("name");
        new JDOMAttributeIterator(null, name);
    }

    @Test
    public void testJDOMAttributeIterator_NullName() {
        NodePointer parent = new NodePointer(new Element("root"));
        new JDOMAttributeIterator(parent, null);
    }
}