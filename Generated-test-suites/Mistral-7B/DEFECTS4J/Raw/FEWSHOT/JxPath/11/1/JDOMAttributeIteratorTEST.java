package org.apache.commons.cli.jxpath.ri.model.jdom; // modify the package name accordingly

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertTrue;

import org.apache.commons.jxpath.ri.QName;
import org.apache.commons.jxpath.ri.model.NodeIterator;
import org.apache.commons.jxpath.ri.model.NodePointer;
import org.jdom.Attribute;
import org.jdom.Element;
import org.jdom.Namespace;

public class JDOMAttributeIteratorTest {

    @Test
    public void testCreateIteratorWithNullParent() {
        assertNull(new JDOMAttributeIterator(null, new QName("", "")));
    }

    @Test
    public void testCreateIteratorWithEmptyElement() {
        Element emptyElement = new Element("empty");
        JDOMAttributeIterator iterator = new JDOMAttributeIterator(new NodePointer(emptyElement), new QName("", ""));
        assertNotNull(iterator.getNodePointer());
        assertNull(iterator.getNodePointer().getNode());
        assertNull(iterator.getNodePointer().getParent());
        assertTrue(iterator.hasNext());
        assertNull(iterator.next());
    }

    @Test
    public void testCreateIteratorWithEmptyAttributeList() {
        Element element = new Element("element");
        JDOMAttributeIterator iterator = new JDOMAttributeIterator(new NodePointer(element), new QName("", ""));
        assertTrue(iterator.hasNext());
        assertNull(iterator.next());
    }

    @Test
    public void testCreateIteratorWithSingleAttribute() {
        Element element = new Element("element");
        Attribute attribute = new Attribute("name", "value");
        element.addContent(attribute);
        JDOMAttributeIterator iterator = new JDOMAttributeIterator(new NodePointer(element), new QName("", "name"));
        NodePointer nodePointer = iterator.next();
        assertEquals("value", nodePointer.getNode().getValue());
        assertNull(iterator.next());
    }

    @Test
    public void testCreateIteratorWithSingleNamespaceAttribute() {
        String prefix = "xml";
        Element element = new Element("element");
        Attribute attribute = new Attribute("name", "value", new Namespace(prefix, ""));
        element.addContent(attribute);
        JDOMAttributeIterator iterator = new JDOMAttributeIterator(new NodePointer(element), new QName(prefix, "name"));
        NodePointer nodePointer = iterator.next();
        assertEquals("value", nodePointer.getNode().getValue());
        assertNull(iterator.next());
    }

    @Test
    public void testCreateIteratorWithMultipleAttributes() {
        Element element = new Element("element");
        Attribute attribute1 = new Attribute("name1", "value1");
        Attribute attribute2 = new Attribute("name2", "value2");
        element.addContent(attribute1);
        element.addContent(attribute2);
        JDOMAttributeIterator iterator = new JDOMAttributeIterator(new NodePointer(element), new QName("", ""));
        NodePointer nodePointer = iterator.next();
        assertEquals("value1", nodePointer.getNode().getValue());
        nodePointer = iterator.next();
        assertEquals("value2", nodePointer.getNode().getValue());
        assertNull(iterator.next());
    }

    @Test
    public void testCreateIteratorWithMultipleNamespacesAttributes() {
        String prefix1 = "prefix1";
        String prefix2 = "prefix2";
        Element element = new Element("element");
        Attribute attribute1 = new Attribute("name1", "value1", new Namespace(prefix1, ""));
        Attribute attribute2 = new Attribute("name2", "value2", new Namespace(prefix2, ""));
        element.addContent(attribute1);
        element.addContent(attribute2);
        JDOMAttributeIterator iterator = new JDOMAttributeIterator(new NodePointer(element), new QName(prefix1, "name1"));
        NodePointer nodePointer = iterator.next();
        assertEquals("value1", nodePointer.getNode().getValue());
        assertNull(iterator.next());

        iterator = new JDOMAttributeIterator(new NodePointer(element), new QName(prefix2, "name2"));
        nodePointer = iterator.next();
        assertEquals("value2", nodePointer.getNode().getValue());
        assertNull(iterator.next());
    }

    @Test
    public void testCreateIteratorWithInvalidName() {
        Element element = new Element("element");
        JDOMAttributeIterator iterator = new JDOMAttributeIterator(new NodePointer(element), new QName("", "invalid"));
        assertNull(iterator.next());
    }

    @Test
    public void testCreateIteratorWithInvalidNamespace() {
        String invalidNamespaceURI = "invalid:namespace";
        Element element = new Element("element");
        JDOMAttributeIterator iterator = new JDOMAttributeIterator(new NodePointer(element), new QName(invalidNamespaceURI, "name"));
        assertNull(iterator.next());
    }
}