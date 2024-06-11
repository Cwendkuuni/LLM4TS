package org.apache.commons.jxpath.ri.test;

import static org.junit.Assert.*;

import org.apache.commons.jxpath.Pointer;
import org.apache.commons.jxpath.ri.NamespaceResolver;
import org.apache.commons.jxpath.ri.model.NodeIterator;
import org.apache.commons.jxpath.ri.model.NodePointer;

/**
 * Test case for {@link NamespaceResolver}.
 */
public class NamespaceResolverTest {

    @org.junit.Before
    public void setUp() {
        // Set up a NamespaceResolver instance
        NamespaceResolver resolver = new NamespaceResolver();
        resolver.registerNamespace("xs", "http://www.w3.org/2001/XMLSchema");
        resolver.registerNamespace("xpath", "http://www.w3.org/TR/xpath");
        resolver.setNamespaceContextPointer(new NodePointer(new Object(), new String[]{"root"}));
    }

    /**
     * Test getNamespaceURI method with registered prefix.
     */
    @org.junit.Test
    public void testGetNamespaceURIWithRegisteredPrefix() {
        String namespaceURI = resolver.getNamespaceURI("xs");
        assertNotNull(namespaceURI);
        assertEquals("http://www.w3.org/2001/XMLSchema", namespaceURI);
    }

    /**
     * Test getNamespaceURI method with unregistered prefix.
     */
    @org.junit.Test
    public void testGetNamespaceURIWithUnregisteredPrefix() {
        String namespaceURI = resolver.getNamespaceURI("foo");
        assertNull(namespaceURI);
    }

    /**
     * Test getNamespaceURI method with pointer.
     */
    @org.junit.Test
    public void testGetNamespaceURIWithPointer() {
        NodePointer pointer = new NodePointer(new Object(), new String[]{"root", "elem1"});
        resolver.setNamespaceContextPointer(pointer);
        String namespaceURI = resolver.getNamespaceURI("xpath");
        assertNotNull(namespaceURI);
        assertEquals("http://www.w3.org/TR/xpath", namespaceURI);
    }

    /**
     * Test getNamespaceURI method with pointer and unregistered prefix.
     */
    @org.junit.Test
    public void testGetNamespaceURIWithPointerAndUnregisteredPrefix() {
        NodePointer pointer = new NodePointer(new Object(), new String[]{"root", "elem1"});
        resolver.setNamespaceContextPointer(pointer);
        String namespaceURI = resolver.getNamespaceURI("foo");
        assertNull(namespaceURI);
    }

    /**
     * Test getNamespaceURI method with parent pointer.
     */
    @org.junit.Test
    public void testGetNamespaceURIWithParentPointer() {
        NamespaceResolver parentResolver = new NamespaceResolver(resolver);
        parentResolver.registerNamespace("foo", "http://example.com/foo");
        resolver.setNamespaceContextPointer(new NodePointer(new Object(), new String[]{"root"}));
        String namespaceURI = resolver.getNamespaceURI("foo");
        assertNull(namespaceURI);

        String parentNamespaceURI = parentResolver.getNamespaceURI("foo");
        assertNotNull(parentNamespaceURI);
        assertEquals("http://example.com/foo", parentNamespaceURI);
    }

    /**
     * Test getPrefix method with registered namespace URI.
     */
    @org.junit.Test
    public void testGetPrefixWithRegisteredNamespaceURI() {
        String prefix = resolver.getPrefix("http://www.w3.org/2001/XMLSchema");
        assertNotNull(prefix);
        assertEquals("xs", prefix);
    }

    /**
     * Test getPrefix method with unregistered namespace URI.
     */
    @org.junit.Test
    public void testGetPrefixWithUnregisteredNamespaceURI() {
        String prefix = resolver.getPrefix("http://example.com/foo");
        assertNull(prefix);
    }

    /**
     * Test getPrefix method with parent namespace resolver.
     */
    @org.junit.Test
    public void testGetPrefixWithParentNamespaceURI() {
        NamespaceResolver parentResolver = new NamespaceResolver(resolver);
        parentResolver.registerNamespace("foo", "http://example.com/foo");
        String prefix = resolver.getPrefix("http://example.com/foo");
        assertNull(prefix);

        String parentPrefix = parentResolver.getPrefix("http://example.com/foo");
        assertNotNull(parentPrefix);
        assertEquals("foo", parentPrefix);
    }

    /**
     * Test isSealed method.
     */
    @org.junit.Test
    public void testIsSealed() {
        NamespaceResolver resolver = new NamespaceResolver();
        assertFalse(resolver.isSealed());

        resolver.seal();
        assertTrue(resolver.isSealed());
    }

    /**
     * Test clone method.
     */
    @org.junit.Test
    public void testClone() {
        NamespaceResolver resolver = new NamespaceResolver();
        resolver.registerNamespace("xs", "http://www.w3.org/2001/XMLSchema");
        resolver.registerNamespace("xpath", "http://www.w3.org/TR/xpath");
        resolver.setNamespaceContextPointer(new NodePointer(new Object(), new String[]{"root"}));

        NamespaceResolver clonedResolver = (NamespaceResolver) resolver.clone();
        assertEquals(resolver.getNamespaceMap(), clonedResolver.getNamespaceMap());
        assertEquals(resolver.isSealed(), clonedResolver.isSealed());
    }
}