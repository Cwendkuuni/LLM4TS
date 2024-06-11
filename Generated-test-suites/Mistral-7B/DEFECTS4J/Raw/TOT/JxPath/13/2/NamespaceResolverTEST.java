package org.apache.commons.jxpath.ri.test;

import org.apache.commons.jxpath.Pointer;
import org.apache.commons.jxpath.ri.model.NodeIterator;
import org.apache.commons.jxpath.ri.NamespaceResolver;
import org.apache.commons.jxpath.ri.model.NodePointer;
import org.junit.Before;
import org.junit.Test;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertSame;

/**
 * Tests for {@link NamespaceResolver}.
 */
public class NamespaceResolverTest {

    private NamespaceResolver resolver;
    private NodePointer pointer;

    @Before
    public void setUp() {
        resolver = new NamespaceResolver();
        pointer = new NodePointer(new Object(), "/test");
        resolver.setNamespaceContextPointer(pointer);
    }

    @Test
    public void testGetNamespaceURI_ExplicitlyRegistered() {
        resolver.registerNamespace("x", "http://example.com/x");
        assertEquals("http://example.com/x", resolver.getNamespaceURI("x"));
    }

    @Test
    public void testGetNamespaceURI_ExternallyRegistered() {
        pointer.registerNamespace("x", "http://example.com/x");
        assertEquals("http://example.com/x", resolver.getNamespaceURI("x"));
    }

    @Test
    public void testGetNamespaceURI_Null() {
        assertNull(resolver.getNamespaceURI(null));
    }

    @Test
    public void testGetNamespaceURI_Unregistered() {
        assertNull(resolver.getNamespaceURI("y"));
    }

    @Test
    public void testGetPrefix_ExplicitlyRegistered() {
        resolver.registerNamespace("x", "http://example.com/x");
        assertEquals("x", resolver.getPrefix("http://example.com/x"));
    }

    @Test
    public void testGetPrefix_ExternallyRegistered() {
        pointer.registerNamespace("x", "http://example.com/x");
        assertEquals("x", resolver.getPrefix("http://example.com/x"));
    }

    @Test
    public void testGetPrefix_Null() {
        assertNull(resolver.getPrefix(null));
    }

    @Test
    public void testGetPrefix_Unregistered() {
        assertNull(resolver.getPrefix("y"));
    }

    @Test
    public void testRegisterNamespace() {
        resolver.registerNamespace("x", "http://example.com/x");
        assertEquals("x", resolver.getPrefix("http://example.com/x"));
        assertEquals("http://example.com/x", resolver.getNamespaceURI("x"));
        assertSame(resolver, resolver.clone());
    }

    @Test(expected = IllegalStateException.class)
    public void testRegisterNamespace_Sealed() {
        resolver.seal();
        resolver.registerNamespace("x", "http://example.com/x");
    }

    @Test
    public void testSetNamespaceContextPointer() {
        NodePointer pointer1 = new NodePointer(new Object(), "/test1");
        NodePointer pointer2 = new NodePointer(new Object(), "/test2");
        resolver.setNamespaceContextPointer(pointer1);
        assertEquals(pointer1, resolver.getNamespaceContextPointer());
        resolver.setNamespaceContextPointer(pointer2);
        assertEquals(pointer2, resolver.getNamespaceContextPointer());
    }

    @Test
    public void testGetNamespaceContextPointer() {
        NodePointer pointer1 = new NodePointer(new Object(), "/test1");
        NodePointer pointer2 = new NodePointer(new Object(), "/test2");
        resolver.setNamespaceContextPointer(pointer1);
        assertEquals(pointer1, resolver.getNamespaceContextPointer());
        resolver.setNamespaceContextPointer(pointer2);
        assertEquals(pointer2, resolver.getNamespaceContextPointer());
        assertNull(resolver.getNamespaceContextPointer());
    }

    @Test
    public void testNamespaceIterator() {
        NodePointer nodePointer1 = new NodePointer(new Object(), "/test1/namespace:a");
        nodePointer1.registerNamespace("namespace", "http://example.com/namespace");

        NodePointer nodePointer2 = new NodePointer(new Object(), "/test1/namespace:b");
        nodePointer2.registerNamespace("namespace", "http://example.com/namespace2");

        NodePointer nodePointer3 = new NodePointer(new Object(), "/test1/namespace:a");

        List<NodePointer> nodePointers = new ArrayList<>();
        nodePointers.add(nodePointer1);
        nodePointers.add(nodePointer2);
        nodePointers.add(nodePointer3);

        NodeIterator nodeIterator = new NodeIterator(nodePointers.iterator());
        NamespaceResolver namespaceResolver = new NamespaceResolver();
        namespaceResolver.setNamespaceContextPointer(nodePointer3);

        for (int i = 0; i < 3; i++) {
            assertSame(nodePointer3, namespaceResolver.getNamespaceContextPointer());
            Map<String, String> namespaceMap = namespaceResolver.getNamespaces();
            assertEquals(nodePointer3.getNamespaceURI("namespace"), namespaceMap.get("namespace"));
            assertEquals("namespace", namespaceMap.get("http://example.com/namespace"));
            assertEquals("namespace", namespaceMap.get("http://example.com/namespace2"));
            namespaceResolver.setNamespaceContextPointer(nodeIterator.next());
        }

        assertNull(namespaceResolver.getNamespaceContextPointer());
    }

    @Test
    public void testSeal() {
        NamespaceResolver resolver1 = new NamespaceResolver();
        resolver1.registerNamespace("x", "http://example.com/x");
        assertFalse(resolver1.isSealed());

        NamespaceResolver resolver2 = resolver1.clone();
        assertFalse(resolver2.isSealed());

        resolver1.seal();
        assertFalse(resolver1.isSealed());
        assertFalse(resolver2.isSealed());

        resolver1.registerNamespace("y", "http://example.com/y");
        assertTrue(resolver1.isSealed());
        assertFalse(resolver2.isSealed());

        resolver2.seal();
        assertTrue(resolver1.isSealed());
        assertTrue(resolver2.isSealed());
    }

    @Test(expected = IllegalStateException.class)
    public void testSeal_Recursive() {
        NamespaceResolver resolver = new NamespaceResolver();
        resolver.seal();
        resolver.seal();
    }
}