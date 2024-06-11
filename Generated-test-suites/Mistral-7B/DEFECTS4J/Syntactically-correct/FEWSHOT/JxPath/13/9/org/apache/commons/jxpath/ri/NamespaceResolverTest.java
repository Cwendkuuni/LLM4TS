package org.apache.commons.jxpath.ri;

import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertEquals;

import org.apache.commons.jxpath.Pointer;
import org.apache.commons.jxpath.ri.model.NodeIterator;
import org.apache.commons.jxpath.ri.model.NodePointer;

import org.junit.Test;

public class NamespaceResolverTest {

    @Test
    public void testNewInstance() {
        NamespaceResolver resolver = new NamespaceResolver();
        assertNull(resolver.getNamespaceURI("foo"));
    }

    @Test
    public void testNewInstanceWithParent() {
        NamespaceResolver parentResolver = new NamespaceResolver();
        NamespaceResolver resolver = new NamespaceResolver(parentResolver);
        assertNull(resolver.getNamespaceURI("foo"));
        assertNull(resolver.getPrefix("http://example.com/"));
        parentResolver.registerNamespace("x", "http://example.com/");
        assertEquals("x", resolver.getPrefix("http://example.com/"));
    }

    @Test
    public void testRegisterNamespace() {
        NamespaceResolver resolver = new NamespaceResolver();
        resolver.registerNamespace("x", "http://example.com/");
        assertEquals("x", resolver.getPrefix("http://example.com/"));
        assertEquals("http://example.com/", resolver.getNamespaceURI("x"));
    }

    @Test(expected = IllegalStateException.class)
    public void testRegisterNamespaceOnSealedResolver() {
        NamespaceResolver resolver = new NamespaceResolver();
        resolver.registerNamespace("x", "http://example.com/");
        resolver.seal();
        resolver.registerNamespace("y", "http://example.org/");
    }

    @Test
    public void testSetNamespaceContextPointer() {
        NamespaceResolver resolver = new NamespaceResolver();
        NodePointer pointer = new NodePointer(new Object(), null, null);
        resolver.setNamespaceContextPointer(pointer);
        assertEquals(pointer, resolver.getNamespaceContextPointer());
        assertEquals("http://example.com/", resolver.getNamespaceURI("x"));
    }

    @Test
    public void testGetNamespaceURI() {
        NamespaceResolver resolver = new NamespaceResolver();
        resolver.registerNamespace("x", "http://example.com/");
        assertEquals("http://example.com/", resolver.getNamespaceURI("x"));
        assertEquals(null, resolver.getNamespaceURI("y"));
        NodePointer pointer = new NodePointer(new Object(), null, null);
        resolver.setNamespaceContextPointer(pointer);
        assertEquals("http://example.com/", resolver.getNamespaceURI("x"));
        assertEquals("http://example.org/", pointer.getNamespaceURI("y"));
    }

    @Test
    public void testGetPrefix() {
        NamespaceResolver resolver = new NamespaceResolver();
        resolver.registerNamespace("x", "http://example.com/");
        assertEquals("x", resolver.getPrefix("http://example.com/"));
        assertEquals(null, resolver.getPrefix("http://example.org/"));
        NodePointer pointer = new NodePointer(new Object(), null, null);
        resolver.setNamespaceContextPointer(pointer);
        assertEquals("x", resolver.getPrefix("http://example.com/"));
        assertEquals("y", pointer.getPrefix("http://example.org/"));
    }

    @Test
    public void testSeal() {
        NamespaceResolver resolver = new NamespaceResolver();
        resolver.registerNamespace("x", "http://example.com/");
        resolver.seal();
        try {
            resolver.registerNamespace("y", "http://example.org/");
        } catch (IllegalStateException e) {
            assertEquals("Cannot register namespaces on a sealed NamespaceResolver", e.getMessage());
        }
    }

    @Test
    public void testClone() {
        NamespaceResolver resolver = new NamespaceResolver();
        resolver.registerNamespace("x", "http://example.com/");
        NamespaceResolver cloned = (NamespaceResolver) resolver.clone();
        assertEquals("x", cloned.getPrefix("http://example.com/"));
        assertEquals("http://example.com/", cloned.getNamespaceURI("x"));
    }

    @Test
    public void testNamespaceIterator() {
        NamespaceResolver resolver = new NamespaceResolver();
        NodePointer node = new NodePointer(new Object(), null, null);
        resolver.setNamespaceContextPointer(node);
        resolver.registerNamespace("x", "http://example.com/");
        NodeIterator iterator = node.namespaceIterator();
        assertEquals("x", iterator.getNodePointer(0).getName().getName());
        assertEquals("http://example.com/", iterator.getNodePointer(0).getNamespaceURI());
    }
}