package org.apache.commons.cli;

import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertEquals;

import org.apache.commons.jxpath.ri.model.NodePointer;
import org.apache.commons.jxpath.ri.NamespaceResolver;
import org.apache.commons.jxpath.ri.model.NodeIterator;

public class NamespaceResolverTest {

    @Test
    public void testNewNamespaceResolver() {
        NamespaceResolver resolver = new NamespaceResolver();
        assertNull(resolver.getNamespaceURI("foo"));
        assertNull(resolver.getPrefix("bar"));
        assertNull(resolver.getNamespaceContextPointer());
    }

    @Test
    public void testNewNamespaceResolverWithParent() {
        NamespaceResolver parentResolver = new NamespaceResolver();
        parentResolver.registerNamespace("x", "http://example.com/x");
        NamespaceResolver childResolver = new NamespaceResolver(parentResolver);
        assertEquals("http://example.com/x", childResolver.getNamespaceURI("x"));
        assertEquals("x", childResolver.getPrefix("http://example.com/x"));
        assertNull(childResolver.getNamespaceURI("y"));
        assertNull(childResolver.getPrefix("z"));
        assertNull(childResolver.getNamespaceContextPointer());
    }

    @Test
    public void testRegisterNamespace() {
        NamespaceResolver resolver = new NamespaceResolver();
        resolver.registerNamespace("x", "http://example.com/x");
        assertEquals("http://example.com/x", resolver.getNamespaceURI("x"));
        assertEquals("x", resolver.getPrefix("http://example.com/x"));

        resolver.registerNamespace("y", "http://example.com/y");
        assertEquals("http://example.com/y", resolver.getNamespaceURI("y"));
        assertEquals("y", resolver.getPrefix("http://example.com/y"));

        resolver.registerNamespace("x", "http://example.com/x2");
        assertEquals("http://example.com/x2", resolver.getNamespaceURI("x"));

        NodePointer pointer = new NodePointer("test");
        resolver.setNamespaceContextPointer(pointer);
        assertEquals(pointer, resolver.getNamespaceContextPointer());
    }

    @Test(expected = IllegalStateException.class)
    public void testSealedNamespaceResolver() {
        NamespaceResolver resolver = new NamespaceResolver();
        resolver.seal();
        resolver.registerNamespace("x", "http://example.com/x");
    }

    @Test
    public void testGetNamespaceURI() {
        NamespaceResolver resolver = new NamespaceResolver();
        assertEquals(null, resolver.getNamespaceURI("foo"));
        NodePointer pointer = new NodePointer("test", "http://example.com");
        resolver.setNamespaceContextPointer(pointer);
        assertEquals("http://example.com", resolver.getNamespaceURI(""));
        assertEquals("http://example.com", resolver.getNamespaceURI(""));
        assertEquals("http://example.com", resolver.getNamespaceURI(""));
        assertEquals("http://example.com", resolver.getNamespaceURI(""));
        assertEquals("http://example.com/test", resolver.getNamespaceURI("test"));
        assertEquals("http://example.com/test", resolver.getNamespaceURI("test:"));
        assertEquals("http://example.com/test", resolver.getNamespaceURI("test"));
        assertEquals("http://example.com/test", resolver.getNamespaceURI("test:"));
        assertEquals("http://example.com", resolver.getNamespaceURI(""));
        assertEquals(null, resolver.getNamespaceURI("foo"));

        NamespaceResolver parentResolver = new NamespaceResolver();
        parentResolver.registerNamespace("x", "http://example.com/x");
        parentResolver.registerNamespace("y", "http://example.com/y");
        NamespaceResolver childResolver = new NamespaceResolver(parentResolver);
        assertEquals("http://example.com/x", childResolver.getNamespaceURI("x"));
        assertEquals("http://example.com/y", childResolver.getNamespaceURI("y"));
        assertEquals("http://example.com/test", childResolver.getNamespaceURI("test:test"));
        assertEquals("http://example.com/test", childResolver.getNamespaceURI("test:test"));
        assertEquals("http://example.com/test", childResolver.getNamespaceURI("test:test"));
        assertEquals("http://example.com/test", childResolver.getNamespaceURI("test:test"));
        assertEquals("http://example.com/test", childResolver.getNamespaceURI("test:test"));
        assertEquals("http://example.com/x", childResolver.getNamespaceURI("x"));
        assertEquals("http://example.com/y", childResolver.getNamespaceURI("y"));
        assertEquals("http://example.com/test", childResolver.getNamespaceURI("test:test"));
        assertEquals("http://example.com/test", childResolver.getNamespaceURI("test:test"));
        assertEquals("http://example.com", childResolver.getNamespaceURI(""));
        assertEquals(null, childResolver.getNamespaceURI("foo"));
    }

    @Test
    public void testGetPrefix() {
        NamespaceResolver resolver = new NamespaceResolver();
        assertEquals(null, resolver.getPrefix("foo"));
        NodePointer pointer = new NodePointer("test", "http://example.com");
        resolver.setNamespaceContextPointer(pointer);
        assertEquals("", resolver.getPrefix(""));
        assertEquals("test", resolver.getPrefix("test"));
        assertEquals("test", resolver.getPrefix("test"));
        assertEquals("test", resolver.getPrefix("test"));
        assertEquals("test", resolver.getPrefix("test"));
        assertEquals("test", resolver.getPrefix("test"));
        assertEquals("", resolver.getPrefix(""));
        assertEquals(null, resolver.getPrefix("foo"));

        NamespaceResolver parentResolver = new NamespaceResolver();
        parentResolver.registerNamespace("x", "http://example.com/x");
        parentResolver.registerNamespace("y", "http://example.com/y");
        NamespaceResolver childResolver = new NamespaceResolver(parentResolver);
        assertEquals("x", childResolver.getPrefix("x"));
        assertEquals("x", childResolver.getPrefix("x"));
        assertEquals("x", childResolver.getPrefix("x"));
        assertEquals("x", childResolver.getPrefix("x"));
        assertEquals("x", childResolver.getPrefix("x"));
        assertEquals("x", childResolver.getPrefix("x"));
        assertEquals("x", childResolver.getPrefix("x"));
        assertEquals("x", childResolver.getPrefix("x"));
        assertEquals("y", childResolver.getPrefix("y"));
        assertEquals("y", childResolver.getPrefix("y"));
        assertEquals("y", childResolver.getPrefix("y"));
        assertEquals("y", childResolver.getPrefix("y"));
        assertEquals("y", childResolver.getPrefix("y"));
        assertEquals("y", childResolver.getPrefix("y"));
        assertEquals("", childResolver.getPrefix(""));
        assertEquals(null, childResolver.getPrefix("foo"));
        assertEquals("test", childResolver.getPrefix("test:test"));
        assertEquals("test", childResolver.getPrefix("test:test"));
        assertEquals("test", childResolver.getPrefix("test:test"));
        assertEquals("test", childResolver.getPrefix("test:test"));
        assertEquals("test", childResolver.getPrefix("test:test"));
        assertEquals("test", childResolver.getPrefix("test:test"));
        assertEquals("test", childResolver.getPrefix("test:test"));
        assertEquals("test", childResolver.getPrefix("test:test"));
        assertEquals("", childResolver.getPrefix(""));
        assertEquals("x", childResolver.getPrefix("x"));
        assertEquals("y", childResolver.getPrefix("y"));
        assertEquals("test:test", childResolver.getPrefix("test:test"));
        assertEquals("test:test", childResolver.getPrefix("test:test"));
        assertEquals("test:test", childResolver.getPrefix("test:test"));
        assertEquals("test:test", childResolver.getPrefix("test:test"));
        assertEquals("test:test", childResolver.getPrefix("test:test"));
        assertEquals("test:test", childResolver.getPrefix("test:test"));
        assertEquals("test:test", childResolver.getPrefix("test:test"));
        assertEquals("", childResolver.getPrefix(""));
        assertEquals(null, childResolver.getPrefix("foo"));
    }

    @Test
    public void testGetNamespaceContextPointer() {
        NamespaceResolver resolver = new NamespaceResolver();
        assertNull(resolver.getNamespaceContextPointer());
        NodePointer pointer = new NodePointer("test", "http://example.com");
        resolver.setNamespaceContextPointer(pointer);
        assertEquals(pointer, resolver.getNamespaceContextPointer());
    }

    @Test
    public void testClone() {
        NamespaceResolver parentResolver = new NamespaceResolver();
        parentResolver.registerNamespace("x", "http://example.com/x");
        parentResolver.registerNamespace("y", "http://example.com/y");
        NamespaceResolver childResolver = new NamespaceResolver(parentResolver);
        NamespaceResolver clonedChildResolver = (NamespaceResolver) childResolver.clone();

        assertEquals("http://example.com/x", clonedChildResolver.getNamespaceURI("x"));
        assertEquals("x", clonedChildResolver.getPrefix("http://example.com/x"));
        assertEquals("http://example.com/y", clonedChildResolver.getNamespaceURI("y"));
        assertEquals("y", clonedChildResolver.getPrefix("http://example.com/y"));
        assertEquals("http://example.com", clonedChildResolver.getNamespaceURI(""));
        assertEquals(null, clonedChildResolver.getNamespaceURI("foo"));

        assertEquals("http://example.com/test", clonedChildResolver.getNamespaceURI("test:test"));
        assertEquals("test:test", clonedChildResolver.getPrefix("http://example.com/test"));
        assertEquals("http://example.com/test", clonedChildResolver.getNamespaceURI("test:test"));
        assertEquals("test:test", clonedChildResolver.getPrefix("http://example.com/test"));
        assertEquals("http://example.com/test", clonedChildResolver.getNamespaceURI("test:test"));
        assertEquals("test:test", clonedChildResolver.getPrefix("http://example.com/test"));
        assertEquals("http://example.com/test", clonedChildResolver.getNamespaceURI("test:test"));
        assertEquals("test:test", clonedChildResolver.getPrefix("http://example.com/test"));
        assertEquals("http://example.com", clonedChildResolver.getNamespaceURI(""));
        assertEquals(null, clonedChildResolver.getNamespaceURI("foo"));

        assertEquals(parentResolver, childResolver.getParent());
        assertEquals(clonedChildResolver, clonedChildResolver.getParent());
        assertEquals(null, clonedChildResolver.getParent().getParent());
    }
}