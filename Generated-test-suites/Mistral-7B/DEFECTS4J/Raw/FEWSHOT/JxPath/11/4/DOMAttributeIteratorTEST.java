package org.apache.commons.cli;

import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertEquals;

import org.apache.commons.jxpath.ri.model.NodePointer;
import org.apache.commons.jxpath.ri.QName;
import org.w3c.dom.Attr;
import org.w3c.dom.Element;
import org.w3c.dom.Node;

public class DOMAttributeIteratorTest {

    @Test
    public void testNewInstanceWithEmptyNode() {
        DOMAttributeIterator iterator = new DOMAttributeIterator(new DOMPointer(null), null);
        assertNull(iterator.getNodePointer());
        assertNull(iterator.getPosition());
        assertNull(iterator.setPosition(1));
    }

    @Test
    public void testNewInstanceWithEmptyElement() {
        DOMAttributeIterator iterator = new DOMAttributeIterator(new DOMPointer(new Element("")), null);
        assertNull(iterator.getNodePointer());
        assertNull(iterator.getPosition());
        assertNull(iterator.setPosition(1));
    }

    @Test
    public void testNewInstanceWithSingleAttribute() {
        Element element = new Element("root");
        element.setAttributeNode(new Attr("id", "foo"));

        DOMAttributeIterator iterator = new DOMAttributeIterator(new DOMPointer(element), null);
        Attr attribute = iterator.getNodePointer();
        assertEquals("foo", attribute.getNodeName());
        assertEquals("id", attribute.getNodeValue());
        assertNull(iterator.getPosition());
        assertFalse(iterator.setPosition(1));
    }

    @Test
    public void testNewInstanceWithMultipleAttributes() {
        Element element = new Element("root");
        element.setAttributeNode(new Attr("id", "foo1"));
        element.setAttributeNode(new Attr("class", "bar"));

        DOMAttributeIterator iterator = new DOMAttributeIterator(new DOMPointer(element), null);
        Attr firstAttribute = iterator.getNodePointer();
        assertEquals("foo1", firstAttribute.getNodeName());
        assertEquals("id", firstAttribute.getNodeValue());

        Attr secondAttribute = iterator.getNodePointer();
        assertEquals("bar", secondAttribute.getNodeName());
        assertEquals("class", secondAttribute.getNodeValue());

        assertFalse(iterator.setPosition(1));
        assertEquals("bar", iterator.getNodePointer().getNodeName());

        assertTrue(iterator.setPosition(2));
        assertNull(iterator.getNodePointer());
    }

    @Test
    public void testNewInstanceWithWildcardName() {
        Element element = new Element("root");
        element.setAttributeNode(new Attr("id", "foo1"));
        element.setAttributeNode(new Attr("class", "bar"));

        DOMAttributeIterator iterator = new DOMAttributeIterator(new DOMPointer(element), new QName("*"));
        Attr firstAttribute = iterator.getNodePointer();
        assertEquals("foo1", firstAttribute.getNodeName());
        assertEquals("id", firstAttribute.getNodeValue());

        Attr secondAttribute = iterator.getNodePointer();
        assertEquals("bar", secondAttribute.getNodeName());
        assertEquals("class", secondAttribute.getNodeValue());

        assertFalse(iterator.setPosition(1));
        assertEquals("bar", iterator.getNodePointer().getNodeName());

        assertTrue(iterator.setPosition(3));
        assertNull(iterator.getNodePointer());
    }

    @Test
    public void testNewInstanceWithNamespaceAwareAttributes() {
        Element element = new Element("root");
        element.setAttributeNodeNS(new Attr("xml:id", "foo1"));
        element.setAttributeNodeNS(new Attr("xml:class", "bar"));

        DOMAttributeIterator iterator = new DOMAttributeIterator(new DOMPointer(element), null);
        iterator.setNamespaceContext(new NamespaceContext() {
            @Override
            public String getNamespaceURI(String prefix) {
                return "xml";
            }
        });

        Attr firstAttribute = iterator.getNodePointer();
        assertEquals("xml:id", firstAttribute.getNodeName().getNamespaceURI() + firstAttribute.getNodeName().getLocalPart());
        assertEquals("foo1", firstAttribute.getNodeValue());

        Attr secondAttribute = iterator.getNodePointer();
        assertEquals("xml:class", secondAttribute.getNodeName().getNamespaceURI() + secondAttribute.getNodeName().getLocalPart());
        assertEquals("bar", secondAttribute.getNodeValue());

        assertFalse(iterator.setPosition(1));
        assertEquals("xml:class", iterator.getNodePointer().getNodeName().getNamespaceURI() + iterator.getNodePointer().getNodeName().getLocalPart());

        assertTrue(iterator.setPosition(2));
        assertNull(iterator.getNodePointer());
    }
}