package org.apache.commons.jxpath.ri;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertSame;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

import java.util.HashMap;
import java.util.Map;

import org.apache.commons.jxpath.NodeIterator;
import org.apache.commons.jxpath.Pointer;
import org.apache.commons.jxpath.ri.model.NodeIteratorMock;
import org.apache.commons.jxpath.ri.model.NodePointer;

import org.junit.Test;

public class NamespaceResolverTest {

    @Test
    public void testNewInstance() {
        NamespaceResolver resolver = new NamespaceResolver();
        assertNull(resolver.getParent());
        assertNull(resolver.getNamespaceMap());
        assertNull(resolver.getReverseMap());
        assertNull(resolver.getPointer());
        assertFalse(resolver.isSealed());
    }

    @Test
    public void testNewInstanceWithParent() {
        NamespaceResolver parentResolver = mock(NamespaceResolver.class);
        NamespaceResolver resolver = new NamespaceResolver(parentResolver);
        assertSame(parentResolver, resolver.getParent());
        assertNull(resolver.getNamespaceMap());
        assertNull(resolver.getReverseMap());
        assertNull(resolver.getPointer());
        assertFalse(resolver.isSealed());
    }

    @Test
    public void testRegisterNamespace() {
        NamespaceResolver resolver = new NamespaceResolver();
        resolver.registerNamespace("x", "http://example.com/x");
        Map<String, String> namespaceMap = (Map<String, String>) resolver.getNamespaceMap();
        assertEquals("http://example.com/x", namespaceMap.get("x"));
        assertEquals(1, namespaceMap.size());
    }

    @Test(expected = IllegalStateException.class)
    public void testRegisterNamespaceAfterSealed() {
        NamespaceResolver resolver = new NamespaceResolver();
        resolver.seal();
        resolver.registerNamespace("x", "http://example.com/x");
    }

    @Test
    public void testGetNamespaceURI() {
        NamespaceResolver resolver = new NamespaceResolver();
        assertEquals(null, resolver.getNamespaceURI("x"));

        resolver.registerNamespace("x", "http://example.com/x");
        assertEquals("http://example.com/x", resolver.getNamespaceURI("x"));

        NodePointer nodePointer = mock(NodePointer.class);
        when(nodePointer.getNamespaceURI("x")).thenReturn("http://example.org/x");
        resolver.setNamespaceContextPointer(nodePointer);
        assertEquals("http://example.org/x", resolver.getNamespaceURI("x"));

        NamespaceResolver parentResolver = mock(NamespaceResolver.class);
        when(parentResolver.getNamespaceURI("x")).thenReturn("http://example.com/x-parent");
        resolver = new NamespaceResolver(parentResolver);
        assertEquals("http://example.com/x-parent", resolver.getNamespaceURI("x"));
    }

    @Test
    public void testGetPrefix() {
        NamespaceResolver resolver = new NamespaceResolver();
        assertEquals(null, resolver.getPrefix("http://example.com/x"));

        resolver.registerNamespace("x", "http://example.com/x");
        assertEquals("x", resolver.getPrefix("http://example.com/x"));

        Map<String, String> reverseMap = (Map<String, String>) resolver.getReverseMap();
        reverseMap.put("http://example.com/x", "x");
        assertEquals("x", resolver.getPrefix("http://example.com/x"));

        NodePointer nodePointer = mock(NodePointer.class);
        when(nodePointer.getNamespaceURI("y")).thenReturn("http://example.org/y");
        when(nodePointer.getName().getName()).thenReturn("y");
        resolver.setNamespaceContextPointer(nodePointer);
        assertEquals(null, resolver.getPrefix("http://example.org/y"));

        NamespaceResolver parentResolver = mock(NamespaceResolver.class);
        when(parentResolver.getPrefix("http://example.com/x")).thenReturn("x-parent");
        resolver = new NamespaceResolver(parentResolver);
        assertEquals("x-parent", resolver.getPrefix("http://example.com/x"));
    }

    @Test
    public void testIsSealed() {
        NamespaceResolver resolver = new NamespaceResolver();
        assertFalse(resolver.isSealed());

        resolver.seal();
        assertTrue(resolver.isSealed());

        NamespaceResolver parentResolver = new NamespaceResolver();
        parentResolver.seal();
        resolver = new NamespaceResolver(parentResolver);
        assertTrue(resolver.isSealed());
    }

    @Test
    public void testClone() {
        NamespaceResolver resolver = new NamespaceResolver();
        Map<String, String> namespaceMap = new HashMap<>();
        namespaceMap.put("x", "http://example.com/x");
        resolver.setNamespaceMap(namespaceMap);
        NamespaceResolver cloned = (NamespaceResolver) resolver.clone();
        assertEquals(namespaceMap, cloned.getNamespaceMap());
    }

    @Test
    public void testNamespaceIterator() {
        NamespaceResolver resolver = new NamespaceResolver();
        NodeIterator nodeIterator = mock(NodeIterator.class);
        when(nodeIterator.getNodePointer(0)).thenReturn(mock(NodePointer.class));
        when(nodeIterator.getNodePointer(0).getNamespaceURI()).thenReturn("http://example.com/x");
        when(nodeIterator.getNodePointer(0).getName().getName()).thenReturn("x");
        resolver.setNamespaceContextPointer(mock(Pointer.class));
        resolver.setNamespaceIterator(nodeIterator);

        assertEquals("x", resolver.getPrefix("http://example.com/x"));
        assertEquals(1, resolver.namespaceCount());
    }
}