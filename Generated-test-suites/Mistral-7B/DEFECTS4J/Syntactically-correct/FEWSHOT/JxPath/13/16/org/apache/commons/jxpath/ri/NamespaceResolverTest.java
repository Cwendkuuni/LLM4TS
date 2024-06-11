package org.apache.commons.jxpath.ri;

import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertEquals;

import java.util.HashMap;
import java.util.Map;

import org.apache.commons.jxpath.Pointer;
import org.apache.commons.jxpath.ri.model.NodeIterator;
import org.apache.commons.jxpath.ri.model.NodePointer;

import org.junit.Test;

public class NamespaceResolverTest {

    @Test
    public void testRegisterNamespace() {
        NamespaceResolver resolver = new NamespaceResolver();
        resolver.registerNamespace("x", "http://example.com/x");
        assertEquals("http://example.com/x", resolver.getNamespaceURI("x"));
        assertEquals("x", resolver.getPrefix("http://example.com/x"));

        assertNull(resolver.getNamespaceURI("y"));
        assertNull(resolver.getPrefix("http://other.com/y"));
    }

    @Test
    public void testSetNamespaceContextPointer() {
        NamespaceResolver resolver = new NamespaceResolver();
        NodePointer pointer = new NodePointer(new Object(), "element");
        resolver.setNamespaceContextPointer(pointer);

        String namespaceURI = "http://example.com/example";
        String prefix = "ex";
        pointer.registerNamespace(prefix, namespaceURI);

        assertEquals(namespaceURI, resolver.getNamespaceURI(prefix));
        assertEquals(prefix, resolver.getPrefix(namespaceURI));

        assertNull(resolver.getNamespaceURI("other"));
        assertNull(resolver.getPrefix("http://other.com/other"));
    }

    @Test
    public void testGetNamespaceURI() {
        NamespaceResolver resolver = new NamespaceResolver();
        NodePointer pointer = new NodePointer(new Object(), "element");
        resolver.setNamespaceContextPointer(pointer);

        String namespaceURI = "http://example.com/example";
        String prefix = "ex";
        pointer.registerNamespace(prefix, namespaceURI);

        assertEquals(namespaceURI, resolver.getNamespaceURI(prefix));
        assertEquals(null, resolver.getNamespaceURI("other"));

        NamespaceResolver parent = new NamespaceResolver(resolver);
        parent.registerNamespace("other", "http://other.com/other");
        assertEquals(namespaceURI, resolver.getNamespaceURI(prefix));
        assertEquals("other", resolver.getNamespaceURI("other"));
    }

    @Test
    public void testGetPrefix() {
        NamespaceResolver resolver = new NamespaceResolver();
        NodePointer pointer = new NodePointer(new Object(), "element");
        resolver.setNamespaceContextPointer(pointer);

        String namespaceURI = "http://example.com/example";
        String prefix = "ex";
        pointer.registerNamespace(prefix, namespaceURI);

        assertEquals("ex", resolver.getPrefix(namespaceURI));
        assertEquals(null, resolver.getPrefix("http://other.com/other"));

        NamespaceResolver parent = new NamespaceResolver(resolver);
        parent.registerNamespace("other", "http://other.com/other");
        assertEquals("ex", resolver.getPrefix(namespaceURI));
        assertEquals("other", resolver.getPrefix("http://other.com/other"));
    }

    @Test
    public void testIsSealed() {
        NamespaceResolver resolver = new NamespaceResolver();
        assertFalse(resolver.isSealed());

        resolver.seal();
        assertTrue(resolver.isSealed());

        NamespaceResolver parent = new NamespaceResolver(resolver);
        parent.seal();
        assertTrue(parent.isSealed());
        assertTrue(resolver.isSealed());
    }

    @Test
    public void testClone() {
        NamespaceResolver resolver = new NamespaceResolver();
        NamespaceResolver clone = (NamespaceResolver) resolver.clone();

        assertNotSame(resolver, clone);
        assertEquals(resolver.namespaceMap, clone.namespaceMap);
        assertEquals(resolver.reverseMap, clone.reverseMap);
        assertEquals(resolver.parent, clone.parent);
        assertEquals(resolver.sealed, clone.sealed);
    }

}