package org.apache.commons.jxpath.ri;

import static org.junit.Assert.*;

import org.apache.commons.jxpath.Node;
import org.apache.commons.jxpath.Pointer;
import org.apache.commons.jxpath.ri.model.NodeIterator;
import org.apache.commons.jxpath.ri.model.NodePointer;
import org.junit.Test;

public class NamespaceResolverTest {
    @Test
    public void testNewInstance() {
        NamespaceResolver resolver = new NamespaceResolver();
        assertNull(resolver.getParent());
        assertEquals(0, resolver.size());
    }

    @Test
    public void testNewInstanceWithParent() {
        NamespaceResolver parentResolver = new NamespaceResolver();
        NamespaceResolver resolver = new NamespaceResolver(parentResolver);
        assertEquals(parentResolver, resolver.getParent());
        assertEquals(0, resolver.size());
    }

    @Test
    public void testRegisterNamespace() {
        NamespaceResolver resolver = new NamespaceResolver();
        resolver.registerNamespace("x", "http://example.com/x");
        assertEquals("http://example.com/x", resolver.getNamespaceURI("x"));
        assertEquals("x", resolver.getPrefix("http://example.com/x"));
        assertEquals(1, resolver.size());

        try {
            resolver.registerNamespace("x", "http://example.com/x");
            fail();
        } catch (IllegalStateException e) {
            // expected
        }

        NamespaceResolver parentResolver = new NamespaceResolver();
        resolver = new NamespaceResolver(parentResolver);
        parentResolver.registerNamespace("x", "http://example.com/x");
        assertEquals("http://example.com/x", resolver.getNamespaceURI("x"));
        assertEquals("x", resolver.getPrefix("http://example.com/x"));
        assertEquals(1, resolver.size());
        assertEquals(1, parentResolver.size());
    }

    @Test
    public void testSetNamespaceContextPointer() {
        NamespaceResolver resolver = new NamespaceResolver();
        NodePointer pointer = new NodePointer(new NodeStub(), "");
        resolver.setNamespaceContextPointer(pointer);
        assertEquals(pointer, resolver.getNamespaceContextPointer());
    }

    @Test
    public void testGetNamespaceURI() {
        NamespaceResolver resolver = new NamespaceResolver();
        assertNull(resolver.getNamespaceURI("x"));
        assertNull(resolver.getNamespaceURI(""));
        assertNull(resolver.getNamespaceURI(null));

        resolver.registerNamespace("x", "http://example.com/x");
        assertEquals("http://example.com/x", resolver.getNamespaceURI("x"));
        assertEquals("http://example.com/x", resolver.getNamespaceURI("x:foo"));
        assertEquals(null, resolver.getNamespaceURI(""));
        assertEquals(null, resolver.getNamespaceURI(null));

        NodePointer pointer = new NodePointer(new NodeStub(), "");
        resolver.setNamespaceContextPointer(pointer);
        assertEquals("http://example.com/x", resolver.getNamespaceURI("x"));
        assertEquals("http://example.com/x", resolver.getNamespaceURI("x:foo"));
        assertEquals("http://example.com", pointer.getNamespaceURI(""));
        assertEquals("http://example.com", pointer.getNamespaceURI(null));

        NamespaceResolver parentResolver = new NamespaceResolver();
        resolver = new NamespaceResolver(parentResolver);
        parentResolver.registerNamespace("x", "http://example.com/x");
        assertEquals("http://example.com/x", resolver.getNamespaceURI("x"));
        assertEquals("http://example.com/x", resolver.getNamespaceURI("x:foo"));
        assertEquals("http://example.com", parentResolver.getNamespaceURI(""));
        assertEquals("http://example.com", parentResolver.getNamespaceURI(null));
    }

    @Test
    public void testGetPrefix() {
        NamespaceResolver resolver = new NamespaceResolver();
        assertNull(resolver.getPrefix("x"));
        assertNull(resolver.getPrefix(""));
        assertNull(resolver.getPrefix(null));

        resolver.registerNamespace("x", "http://example.com/x");
        assertEquals("x", resolver.getPrefix("http://example.com/x"));
        assertEquals("x", resolver.getPrefix("x:foo"));
        assertEquals(null, resolver.getPrefix(""));
        assertEquals(null, resolver.getPrefix(null));

        NodePointer pointer = new NodePointer(new NodeStub(), "");
        resolver.setNamespaceContextPointer(pointer);
        assertEquals("x", resolver.getPrefix("http://example.com/x"));
        assertEquals("x", resolver.getPrefix("x:foo"));
        assertEquals("", pointer.getPrefix(""));
        assertEquals("", pointer.getPrefix(null));

        NamespaceResolver parentResolver = new NamespaceResolver();
        resolver = new NamespaceResolver(parentResolver);
        parentResolver.registerNamespace("x", "http://example.com/x");
        assertEquals("x", resolver.getPrefix("http://example.com/x"));
        assertEquals("x", resolver.getPrefix("x:foo"));
        assertEquals("", parentResolver.getPrefix(""));
        assertEquals("", parentResolver.getPrefix(null));
    }

    @Test
    public void testSize() {
        NamespaceResolver resolver = new NamespaceResolver();
        assertEquals(0, resolver.size());

        resolver.registerNamespace("x", "http://example.com/x");
        assertEquals(1, resolver.size());

        resolver.registerNamespace("y", "http://example.com/y");
        assertEquals(2, resolver.size());

        NamespaceResolver parentResolver = new NamespaceResolver();
        resolver = new NamespaceResolver(parentResolver);
        parentResolver.registerNamespace("x", "http://example.com/x");
        parentResolver.registerNamespace("y", "http://example.com/y");
        assertEquals(2, resolver.size());
        assertEquals(2, parentResolver.size());
    }

    @Test
    public void testSeal() {
        NamespaceResolver resolver = new NamespaceResolver();
        assertEquals(false, resolver.isSealed());

        resolver.registerNamespace("x", "http://example.com/x");
        assertEquals(false, resolver.isSealed());

        resolver.seal();
        assertEquals(true, resolver.isSealed());

        try {
            resolver.registerNamespace("y", "http://example.com/y");
            fail();
        } catch (IllegalStateException e) {
            // expected
        }

        NamespaceResolver parentResolver = new NamespaceResolver();
        resolver = new NamespaceResolver(parentResolver);
        parentResolver.registerNamespace("x", "http://example.com/x");
        parentResolver.seal();

        assertEquals(false, resolver.isSealed());
        resolver.registerNamespace("y", "http://example.com/y");
        assertEquals(true, resolver.isSealed());
    }

    @Test
    public void testClone() {
        NamespaceResolver resolver = new NamespaceResolver();
        assertEquals(resolver, resolver.clone());

        resolver.registerNamespace("x", "http://example.com/x");
        NamespaceResolver cloned = (NamespaceResolver) resolver.clone();
        assertEquals(resolver, cloned);
        assertEquals(1, cloned.size());
        assertEquals("http://example.com/x", cloned.getNamespaceURI("x"));
        assertEquals("x", cloned.getPrefix("http://example.com/x"));

        resolver.seal();
        cloned = (NamespaceResolver) resolver.clone();
        assertEquals(resolver, cloned);
        assertEquals(1, cloned.size());
        assertEquals("http://example.com/x", cloned.getNamespaceURI("x"));
        assertEquals("x", cloned.getPrefix("http://example.com/x"));
        assertEquals(true, cloned.isSealed());
    }
}