package org.apache.commons.cli;

import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertEquals;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

import java.util.ArrayList;
import java.util.List;

import org.apache.commons.jxpath.ri.QName;
import org.apache.commons.jxpath.ri.model.NodeIterator;
import org.apache.commons.jxpath.ri.model.NodePointer;
import org.w3c.dom.Attr;
import org.w3c.dom.Element;
import org.w3c.dom.NamedNodeMap;
import org.w3c.dom.Node;
import org.mockito.invocation.InvocationOnMock;
import org.mockito.stubbing.Answer;

/**
 * Test cases for DOMAttributeIterator class
 */
public class DOMAttributeIteratorTest {

    @Test
    public void testDOMAttributeIterator() {
        // Test empty case
        DOMAttributeIterator iterator = new DOMAttributeIterator(mock(NodePointer.class), null);
        assertNull(iterator.getNodePointer());
        assertEquals(0, iterator.getPosition());
        assertNull(iterator.next());

        // Test single attribute case
        Element element = mock(Element.class);
        Attr attr = mock(Attr.class);
        when(element.getAttributeNode(eq("testAttr"))).thenReturn(attr);
        NodePointer parentPointer = mock(NodePointer.class);
        when(parentPointer.getNode()).then(new Answer<Node>() {
            @Override
            public Node answer(InvocationOnMock invocation) throws Throwable {
                return element;
            }
        });
        DOMAttributeIterator iterator1 = new DOMAttributeIterator(parentPointer, new QName("testPrefix", "testLocalName"));
        assertNotNull(iterator1.getNodePointer());
        assertEquals(attr, iterator1.getNodePointer().getNode());
        assertEquals(1, iterator1.getPosition());
        assertNull(iterator1.next());

        // Test multiple attributes case
        Element element2 = mock(Element.class);
        Attr attr1 = mock(Attr.class);
        Attr attr2 = mock(Attr.class);
        when(element2.getAttributeNodeNS(eq("testNS"), eq("testLocalName1"))).thenReturn(attr1);
        when(element2.getAttributeNodeNS(eq("testNS"), eq("testLocalName2"))).thenReturn(attr2);
        NodePointer parentPointer2 = mock(NodePointer.class);
        when(parentPointer2.getNode()).then(new Answer<Node>() {
            @Override
            public Node answer(InvocationOnMock invocation) throws Throwable {
                return element2;
            }
        });
        DOMAttributeIterator iterator2 = new DOMAttributeIterator(parentPointer2, new QName("testPrefix", "*"));
        assertNotNull(iterator2.getNodePointer());
        assertEquals(attr1, iterator2.next().getNode());
        assertEquals(attr2, iterator2.next().getNode());
        assertNull(iterator2.next());
    }

    @Test
    public void testDOMAttributeIteratorWithNamespace() {
        // Test empty case
        DOMAttributeIterator iterator = new DOMAttributeIterator(mock(NodePointer.class), null);
        assertNull(iterator.getNodePointer());
        assertEquals(0, iterator.getPosition());
        assertNull(iterator.next());

        // Test single attribute case with namespace
        Element element = mock(Element.class);
        Attr attr = mock(Attr.class);
        when(element.getAttributeNodeNS(eq("testNS"), eq("testLocalName"))).thenReturn(attr);
        NodePointer parentPointer = mock(NodePointer.class);
        when(parentPointer.getNode()).then(new Answer<Node>() {
            @Override
            public Node answer(InvocationOnMock invocation) throws Throwable {
                return element;
            }
        });
        DOMAttributeIterator iterator1 = new DOMAttributeIterator(parentPointer, new QName("testPrefix", "testLocalName"));
        assertNotNull(iterator1.getNodePointer());
        assertEquals(attr, iterator1.getNodePointer().getNode());
        assertEquals(1, iterator1.getPosition());
        assertNull(iterator1.next());

        // Test multiple attributes case with same namespace
        Element element2 = mock(Element.class);
        Attr attr1 = mock(Attr.class);
        Attr attr2 = mock(Attr.class);
        when(element2.getAttributeNodeNS(eq("testNS"), eq("testLocalName1"))).thenReturn(attr1);
        when(element2.getAttributeNodeNS(eq("testNS"), eq("testLocalName2"))).thenReturn(attr2);
        NodePointer parentPointer2 = mock(NodePointer.class);
        when(parentPointer2.getNode()).then(new Answer<Node>() {
            @Override
            public Node answer(InvocationOnMock invocation) throws Throwable {
                return element2;
            }
        });
        DOMAttributeIterator iterator2 = new DOMAttributeIterator(parentPointer2, new QName("testPrefix", "*"));
        assertNotNull(iterator2.getNodePointer());
        assertEquals(attr1, iterator2.next().getNode());
        assertEquals(attr2, iterator2.next().getNode());
        assertNull(iterator2.next());

        // Test multiple attributes case with different namespaces
        Element element3 = mock(Element.class);
        Attr attr3 = mock(Attr.class);
        Attr attr4 = mock(Attr.class);
        when(element3.getAttributeNodeNS(eq("testNS1"), eq("testLocalName1"))).thenReturn(attr3);
        when(element3.getAttributeNodeNS(eq("testNS2"), eq("testLocalName2"))).thenReturn(attr4);
        NodePointer parentPointer3 = mock(NodePointer.class);
        when(parentPointer3.getNode()).then(new Answer<Node>() {
            @Override
            public Node answer(InvocationOnMock invocation) throws Throwable {
                return element3;
            }
        });
        DOMAttributeIterator iterator3 = new DOMAttributeIterator(parentPointer3, new QName("testPrefix1", "*"));
        assertNotNull(iterator3.getNodePointer());
        assertEquals(attr3, iterator3.next().getNode());
        assertNull(iterator3.next());

        DOMAttributeIterator iterator4 = new DOMAttributeIterator(parentPointer3, new QName("testPrefix2", "*"));
        assertNotNull(iterator4.getNodePointer());
        assertEquals(attr4, iterator4.next().getNode());
        assertNull(iterator4.next());
    }
}