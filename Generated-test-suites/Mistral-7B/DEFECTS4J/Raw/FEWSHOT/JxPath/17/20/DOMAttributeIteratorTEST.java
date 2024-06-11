package org.apache.commons.cli;

import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

import java.util.ArrayList;
import java.util.List;

import org.apache.commons.jxpath.ri.model.NodeIterator;
import org.apache.commons.jxpath.ri.model.NodePointer;
import org.w3c.dom.Attr;
import org.w3c.dom.Element;
import org.w3c.dom.NamedNodeMap;
import org.w3c.dom.Node;

public class DOMAttributeIteratorTest {

    @Test
    public void testDOMAttributeIterator() {
        Element element = createTestElement();
        DOMAttributeIterator iterator = new DOMAttributeIterator(new DOMPointer(element), new QName("*"));
        assertNotNull(iterator);
        assertEquals(element, iterator.parent.getNode());
        assertNotNull(iterator.attributes);
        assertTrue(iterator.hasNext());
        assertEquals(1, iterator.getPosition());
    }

    @Test
    public void testDOMAttributeIteratorWithName() {
        Element element = createTestElement();
        Attr attr = createTestAttr("test", "value");
        element.setAttributeNode(attr);
        DOMAttributeIterator iterator = new DOMAttributeIterator(new DOMPointer(element), new QName("test"));
        assertNotNull(iterator);
        assertEquals(element, iterator.parent.getNode());
        assertNotNull(iterator.attributes);
        assertEquals(attr, iterator.getNodePointer().getNode());
        assertTrue(iterator.hasNext());
        assertEquals(1, iterator.getPosition());
    }

    @Test
    public void testDOMAttributeIteratorEmpty() {
        Element element = createTestElement();
        DOMAttributeIterator iterator = new DOMAttributeIterator(new DOMPointer(element), new QName("*"));
        assertNotNull(iterator);
        assertEquals(element, iterator.parent.getNode());
        assertNotNull(iterator.attributes);
        assertFalse(iterator.hasNext());
        assertEquals(0, iterator.getPosition());
    }

    @Test
    public void testDOMAttributeIteratorMixedNamespaces() {
        Element element = createTestElement();
        Attr attr1 = createTestAttr("test:prefix1:localName1", "value1");
        Attr attr2 = createTestAttr("test:prefix2:localName2", "value2");
        Attr attr3 = createTestAttr("localName3", "value3");
        element.setAttributeNode(attr1);
        element.setAttributeNode(attr2);
        element.setAttributeNode(attr3);
        DOMAttributeIterator iterator = new DOMAttributeIterator(new DOMPointer(element), new QName("test:prefix1:localName1"));
        assertNotNull(iterator);
        assertEquals(element, iterator.parent.getNode());
        assertNotNull(iterator.attributes);
        assertEquals(attr1, iterator.getNodePointer().getNode());
        assertTrue(iterator.hasNext());
        assertEquals(1, iterator.getPosition());

        iterator.next();
        assertEquals(attr2, iterator.getNodePointer().getNode());
        assertTrue(iterator.hasNext());
        assertEquals(2, iterator.getPosition());

        iterator.next();
        assertEquals(attr3, iterator.getNodePointer().getNode());
        assertFalse(iterator.hasNext());
        assertEquals(3, iterator.getPosition());
    }

    @Test
    public void testDOMAttributeIteratorSameNamespace() {
        Element element = createTestElement();
        Attr attr1 = createTestAttr("localName1", "value1");
        Attr attr2 = createTestAttr("localName1", "value2");
        element.setAttributeNode(attr1);
        element.setAttributeNode(attr2);
        DOMAttributeIterator iterator = new DOMAttributeIterator(new DOMPointer(element), new QName("localName1"));
        assertNotNull(iterator);
        assertEquals(element, iterator.parent.getNode());
        assertNotNull(iterator.attributes);
        assertEquals(attr1, iterator.getNodePointer().getNode());
        assertTrue(iterator.hasNext());
        assertEquals(1, iterator.getPosition());

        iterator.next();
        assertEquals(attr2, iterator.getNodePointer().getNode());
        assertTrue(iterator.hasNext());
        assertEquals(2, iterator.getPosition());

        assertFalse(iterator.hasNext());
        assertEquals(2, iterator.getPosition());
    }

    @Test
    public void testDOMAttributeIteratorNullParent() {
        DOMAttributeIterator iterator = new DOMAttributeIterator(null, new QName("*"));
        assertNull(iterator);
    }

    @Test
    public void testDOMAttributeIteratorNullName() {
        NodePointer parent = new DOMPointer(createTestElement());
        DOMAttributeIterator iterator = new DOMAttributeIterator(parent, null);
        assertNull(iterator);
    }

    @Test
    public void testDOMAttributeIteratorEmptyName() {
        NodePointer parent = new DOMPointer(createTestElement());
        DOMAttributeIterator iterator = new DOMAttributeIterator(parent, new QName(""));
        assertNotNull(iterator);
        assertFalse(iterator.hasNext());
        assertEquals(0, iterator.getPosition());
    }

    private Element createTestElement() {
        Element element = new ElementImpl();
        element.setTagName("test");
        return element;
    }

    private Attr createTestAttr(String prefix, String localName) {
        Attr attr = new AttrImpl();
        attr.setPrefix(prefix);
        attr.setLocalName(localName);
        attr.setNodeValue("value");
        return attr;
    }
}