package org.apache.commons.cli;

import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertSame;
import static org.junit.Assert.assertTrue;
import static org.junit.Assert.assertEquals;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

import java.util.ArrayList;
import java.util.List;

import org.apache.commons.jxpath.ri.QName;
import org.apache.commons.jxpath.ri.model.NodeIterator;
import org.apache.commons.jxpath.ri.model.NodePointer;
import org.apache.commons.jxpath.ri.model.dom.DOMAttributeIterator;
import org.apache.commons.jxpath.ri.model.dom.DOMAttributePointer;
import org.apache.commons.jxpath.ri.model.dom.DOMNodePointer;
import org.w3c.dom.Attr;
import org.w3c.dom.Element;
import org.w3c.dom.NamedNodeMap;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;

public class DOMAttributeIteratorTest {

    @Test
    public void testDOMAttributeIterator() {
        Element root = mock(Element.class);
        when(root.getAttributes()).thenReturn(createMockAttrs());
        NodePointer parent = new DOMNodePointer(root);
        QName name = new QName("testName");
        DOMAttributeIterator iterator = new DOMAttributeIterator(parent, name);
        assertEquals(0, iterator.getPosition());
        assertNull(iterator.getNodePointer());
        assertTrue(iterator.hasNext());
    }

    @Test
    public void testDOMAttributeIterator_next() {
        Element root = mock(Element.class);
        when(root.getAttributes()).thenReturn(createMockAttrs());
        NodePointer parent = new DOMNodePointer(root);
        QName name = new QName("testName");
        DOMAttributeIterator iterator = new DOMAttributeIterator(parent, name);
        Attr nextAttr = iterator.next();
        assertEquals(1, iterator.getPosition());
        assertSame(nextAttr, iterator.getNodePointer().getNode());
        assertTrue(iterator.hasNext());
    }

    @Test
    public void testDOMAttributeIterator_next_noMore() {
        Element root = mock(Element.class);
        when(root.getAttributes()).thenReturn(createMockAttrs());
        NodePointer parent = new DOMNodePointer(root);
        QName name = new QName("testName");
        DOMAttributeIterator iterator = new DOMAttributeIterator(parent, name);
        iterator.next();
        iterator.next();
        assertFalse(iterator.hasNext());
    }

    @Test
    public void testDOMAttributeIterator_getPosition_setPosition() {
        Element root = mock(Element.class);
        when(root.getAttributes()).thenReturn(createMockAttrs());
        NodePointer parent = new DOMNodePointer(root);
        QName name = new QName("testName");
        DOMAttributeIterator iterator = new DOMAttributeIterator(parent, name);
        assertEquals(0, iterator.getPosition());
        iterator.setPosition(3);
        assertEquals(3, iterator.getPosition());
    }

    @Test(expected = IndexOutOfBoundsException.class)
    public void testDOMAttributeIterator_getPosition_outOfBounds() {
        Element root = mock(Element.class);
        when(root.getAttributes()).thenReturn(createMockAttrs());
        NodePointer parent = new DOMNodePointer(root);
        QName name = new QName("testName");
        DOMAttributeIterator iterator = new DOMAttributeIterator(parent, name);
        iterator.setPosition(5);
    }

    @Test
    public void testDOMAttributeIterator_getPosition_reset() {
        Element root = mock(Element.class);
        when(root.getAttributes()).thenReturn(createMockAttrs());
        NodePointer parent = new DOMNodePointer(root);
        QName name = new QName("testName");
        DOMAttributeIterator iterator = new DOMAttributeIterator(parent, name);
        iterator.next();
        iterator.next();
        iterator.setPosition(0);
        assertSame(iterator.getNodePointer().getNode(), iterator.next());
    }

    @Test
    public void testDOMAttributeIterator_getPointer() {
        Element root = mock(Element.class);
        when(root.getAttributes()).thenReturn(createMockAttrs());
        NodePointer parent = new DOMNodePointer(root);
        QName name = new QName("testName");
        DOMAttributeIterator iterator = new DOMAttributeIterator(parent, name);
        iterator.next();
        DOMAttributePointer attributePointer = iterator.getNodePointer();
        assertSame(attributePointer.getNode(), iterator.next().getNode());
    }

    @Test
    public void testDOMAttributeIterator_testAttr() {
        String testPrefix = "testPrefix";
        String testLocalName = "testLocalName";
        Attr testAttr = mock(Attr.class);
        when(testAttr.getPrefix()).thenReturn(testPrefix);
        when(testAttr.getLocalName()).thenReturn(testLocalName);
        String testNamespaceURI = "testNamespaceURI";
        when(testAttr.getNamespaceURI()).thenReturn(testNamespaceURI);

        String parentNamespaceURI = "parentNamespaceURI";
        String parentPrefix = "parentPrefix";
        String parentLocalName = "parentLocalName";
        Attr parentAttr = mock(Attr.class);
        when(parentAttr.getPrefix()).thenReturn(parentPrefix);
        when(parentAttr.getLocalName()).thenReturn(parentLocalName);
        when(parentAttr.getNamespaceURI()).thenReturn(parentNamespaceURI);

        assertTrue(DOMAttributeIterator.testAttr(testAttr));
        assertFalse(DOMAttributeIterator.testAttr(parentAttr));
        assertFalse(DOMAttributeIterator.testAttr(null));

        assertTrue(DOMAttributeIterator.equalStrings(testPrefix, testPrefix));
        assertTrue(DOMAttributeIterator.equalStrings(testPrefix, testPrefix));
        assertFalse(DOMAttributeIterator.equalStrings("testPrefix", null));
        assertFalse(DOMAttributeIterator.equalStrings(null, "testPrefix"));
        assertFalse(DOMAttributeIterator.equalStrings("testPrefix1", "testPrefix2"));
        assertFalse(DOMAttributeIterator.equalStrings("testPrefix", "testPrefix1"));

        assertFalse(DOMAttributeIterator.equalStrings(testNamespaceURI, null));
        assertFalse(DOMAttributeIterator.equalStrings(null, testNamespaceURI));
        assertTrue(DOMAttributeIterator.equalStrings(testNamespaceURI, testNamespaceURI));
    }

    @Test
    public void testDOMAttributeIterator_getAttribute() {
        Element root = mock(Element.class);
        when(root.getAttributes()).thenReturn(createMockAttrs());
        NodePointer parent = new DOMNodePointer(root);
        QName name = new QName("testName");
        QName attributeName = new QName("attributeName");
        Attr attribute = mock(Attr.class);
        when(attribute.getName()).thenReturn(attributeName);
        when(root.getAttributeNodeNS(attributeName.getNamespaceURI(), attributeName.getName())).thenReturn(attribute);

        DOMAttributeIterator iterator = new DOMAttributeIterator(parent, name);
        assertSame(attribute, iterator.getAttribute(attributeName));
    }

    @Test
    public void testDOMAttributeIterator_getAttribute_noAttribute() {
        Element root = mock(Element.class);
        when(root.getAttributes()).thenReturn(createMockAttrs());
        NodePointer parent = new DOMNodePointer(root);
        QName name = new QName("testName");
        QName attributeName = new QName("attributeName");

        DOMAttributeIterator iterator = new DOMAttributeIterator(parent, name);
        assertNull(iterator.getAttribute(attributeName));
    }

    @Test
    public void testDOMAttributeIterator_getAttributes() {
        Element root = mock(Element.class);
        NodeList attributes = mock(NodeList.class);
        when(root.getAttributes()).thenReturn(attributes);

        NodePointer parent = new DOMNodePointer(root);
        QName name = new QName("testName");
        DOMAttributeIterator iterator = new DOMAttributeIterator(parent, name);
        assertEquals(attributes.getLength(), iterator.size());
    }

    @Test
    public void testDOMAttributeIterator_size() {
        Element root = mock(Element.class);
        NodeList attributes = mock(NodeList.class);
        when(root.getAttributes()).thenReturn(attributes);

        NodePointer parent = new DOMNodePointer(root);
        QName name = new QName("testName");
        DOMAttributeIterator iterator = new DOMAttributeIterator(parent, name);
        assertEquals(attributes.getLength(), iterator.size());
    }

    @Test
    public void testDOMAttributeIterator_hasNext() {
        Element root = mock(Element.class);
        NodeList attributes = mock(NodeList.class);
        when(root.getAttributes()).thenReturn(attributes);

        NodePointer parent = new DOMNodePointer(root);
        QName name = new QName("testName");
        DOMAttributeIterator iterator = new DOMAttributeIterator(parent, name);
        assertTrue(iterator.hasNext());
        iterator.next();
        assertFalse(iterator.hasNext());
    }

    @Test
    public void testDOMAttributeIterator_hasNext_noAttributes() {
        Element root = mock(Element.class);
        NodePointer parent = new DOMNodePointer(root);
        QName name = new QName("testName");
        DOMAttributeIterator iterator = new DOMAttributeIterator(parent, name);
        assertFalse(iterator.hasNext());
    }

    private NamedNodeMap createMockAttrs() {
        NodeList attributes = mock(NodeList.class);
        Attr attr1 = mock(Attr.class);
        Attr attr2 = mock(Attr.class);
        when(attr1.getNodeName()).thenReturn(new QName("attr1"));
        when(attr2.getNodeName()).thenReturn(new QName("attr2"));
        when(attributes.getLength()).thenReturn(2);
        when(attributes.item(0)).thenReturn(attr1);
        when(attributes.item(1)).thenReturn(attr2);
        return (NamedNodeMap) mock(NamedNodeMap.class).withAnswers(m -> attributes);
    }
}