package org.apache.commons.cli;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertSame;

import org.apache.commons.jxpath.NodePointer;
import org.apache.commons.jxpath.ri.model.NodeIterator;
import org.apache.commons.jxpath.ri.model.Namespaces;
import org.apache.commons.jxpath.ri.model.NamespaceContext;

import java.util.HashMap;
import java.util.Map;

public class NamespaceResolverTest {

    @Test
    public void testNewInstance() {
        NamespaceResolver resolver = new NamespaceResolver();
        assertNull(resolver.getParent());
        assertEquals(0, resolver.size());
    }

    @Test
    public void testNewInstanceWithParent() {
        NamespaceResolver parent = new NamespaceResolver();
        NamespaceResolver resolver = new NamespaceResolver(parent);
        assertEquals(parent, resolver.getParent());
        assertEquals(0, resolver.size());
    }

    @Test
    public void testRegisterNamespace() {
        NamespaceResolver resolver = new NamespaceResolver();
        resolver.registerNamespace("x", "http://example.com/x");
        assertEquals("http://example.com/x", resolver.getNamespaceURI("x"));
        assertEquals("x", resolver.getPrefix("http://example.com/x"));
        assertEquals("x", resolver.getPrefix("http://example.com/x"));

        resolver.registerNamespace("y", "http://example.com/y");
        assertEquals("http://example.com/y", resolver.getNamespaceURI("y"));
        assertEquals("y", resolver.getPrefix("http://example.com/y"));
        assertEquals("y", resolver.getPrefix("http://example.com/y"));

        // Test prefix collision
        resolver.registerNamespace("x", "http://example.com/x2");
        assertEquals("http://example.com/x2", resolver.getNamespaceURI("x"));
        assertEquals("x", resolver.getPrefix("http://example.com/x2"));
        assertEquals("x", resolver.getPrefix("http://example.com/x2"));

        // Test null prefix
        try {
            resolver.registerNamespace(null, "http://example.com/x3");
            fail();
        } catch (NullPointerException expected) {
        }

        // Test null uri
        try {
            resolver.registerNamespace("z", null);
            fail();
        } catch (NullPointerException expected) {
        }
    }

    @Test
    public void testGetNamespaceURI() {
        NamespaceResolver resolver = new NamespaceResolver();

        Namespaces namespaces = new Namespaces();
        namespaces.bind("xml", "http://www.w3.org/XML/1998/namespace");
        NodePointer xmlNamespaceNode = namespaces.getNamespaceNode("xml");

        resolver.setNamespaceContextPointer(xmlNamespaceNode);

        assertEquals("http://www.w3.org/XML/1998/namespace", resolver.getNamespaceURI("xml"));
        assertEquals("xml", resolver.getPrefix("http://www.w3.org/XML/1998/namespace"));

        // Test null namespace context
        assertNull(resolver.getNamespaceURI(null));

        // Test unregistered prefix
        assertNull(resolver.getNamespaceURI("foo"));

        // Test parent namespace
        NamespaceResolver parent = new NamespaceResolver();
        parent.registerNamespace("x", "http://example.com/x");
        resolver.setParent(parent);
        assertEquals("http://example.com/x", resolver.getNamespaceURI("x"));
    }

    @Test
    public void testGetPrefix() {
        NamespaceResolver resolver = new NamespaceResolver();

        Namespaces namespaces = new Namespaces();
        namespaces.bind("xml", "http://www.w3.org/XML/1998/namespace");
        NodePointer xmlNamespaceNode = namespaces.getNamespaceNode("xml");

        resolver.setNamespaceContextPointer(xmlNamespaceNode);

        assertEquals("xml", resolver.getPrefix("http://www.w3.org/XML/1998/namespace"));
        assertNull(resolver.getPrefix("http://example.com/"));

        // Test null namespace context
        assertNull(resolver.getPrefix(null));

        // Test unregistered prefix
        assertNull(resolver.getPrefix("foo"));

        // Test parent prefix
        NamespaceResolver parent = new NamespaceResolver();
        parent.registerNamespace("x", "http://example.com/x");
        resolver.setParent(parent);
        assertEquals("x", resolver.getPrefix("http://example.com/x"));
    }

    @Test
    public void testSeal() {
        NamespaceResolver resolver = new NamespaceResolver();
        resolver.registerNamespace("x", "http://example.com/x");
        resolver.registerNamespace("y", "http://example.com/y");
        assertEquals(2, resolver.size());

        resolver.seal();
        assertEquals(2, resolver.size());

        try {
            resolver.registerNamespace("z", "http://example.com/z");
            fail();
        } catch (IllegalStateException expected) {
        }

        assertEquals("x", resolver.getPrefix("http://example.com/x"));
        assertEquals("y", resolver.getPrefix("http://example.com/y"));
    }

    @Test
    public void testClone() {
        NamespaceResolver resolver = new NamespaceResolver();
        resolver.registerNamespace("x", "http://example.com/x");
        resolver.registerNamespace("y", "http://example.com/y");
        assertEquals(2, resolver.size());

        NamespaceResolver clonedResolver = (NamespaceResolver) resolver.clone();
        assertEquals(2, clonedResolver.size());
        assertSame(resolver, clonedResolver.getParent());

        // Test cloning with parent
        NamespaceResolver parent = new NamespaceResolver();
        parent.registerNamespace("z", "http://example.com/z");
        resolver.setParent(parent);
        NamespaceResolver clonedResolverWithParent = (NamespaceResolver) resolver.clone();
        assertSame(parent, clonedResolverWithParent.getParent());
        assertEquals(2, clonedResolverWithParent.size());

        // Test cloning a sealed NamespaceResolver
        resolver.seal();
        NamespaceResolver clonedSealedResolver = (NamespaceResolver) resolver.clone();
        assertEquals(2, clonedSealedResolver.size());
        assertTrue(clonedSealedResolver.isSealed());
    }
}