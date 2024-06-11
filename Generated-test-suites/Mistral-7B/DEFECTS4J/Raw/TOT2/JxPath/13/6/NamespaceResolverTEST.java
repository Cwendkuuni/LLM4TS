package org.junit.tests.NamespaceResolverTest;

import org.apache.commons.jxpath.ri.NamespaceResolver;
import org.junit.Test;
import static org.junit.Assert.*;

public class NamespaceResolverTest {
    @Test
    public void testGetNamespaceURI() {
        NamespaceResolver resolver = new NamespaceResolver();
        resolver.registerNamespace("x", "http://example.com/x");
        String prefix = "x";
        String expectedNamespaceURI = "http://example.com/x";
        String actualNamespaceURI = resolver.getNamespaceURI(prefix);
        assertEquals(expectedNamespaceURI, actualNamespaceURI);
    }

    @Test
    public void testGetNamespaceURI_PrefixNull() {
        NamespaceResolver resolver = new NamespaceResolver();
        assertNull(resolver.getNamespaceURI(null));
    }

    @Test
    public void testGetNamespaceURI_PrefixEmpty() {
        NamespaceResolver resolver = new NamespaceResolver();
        assertNull(resolver.getNamespaceURI(""));
    }

    @Test
    public void testGetNamespaceURI_RegisteredPrefix() {
        NamespaceResolver resolver = new NamespaceResolver();
        resolver.registerNamespace("x", "http://example.com/x");
        String prefix = "x";
        String expectedNamespaceURI = "http://example.com/x";
        String actualNamespaceURI = resolver.getNamespaceURI(prefix);
        assertEquals(expectedNamespaceURI, actualNamespaceURI);
    }

    @Test
    public void testGetNamespaceURI_UnregisteredPrefix() {
        NamespaceResolver resolver = new NamespaceResolver();
        String prefix = "y";
        assertNull(resolver.getNamespaceURI(prefix));
    }

    @Test
    public void testGetNamespaceURI_NullNamespaceContextPointer() {
        NamespaceResolver resolver = new NamespaceResolver();
        assertNull(resolver.getNamespaceURI("x"));
    }

    @Test
    public void testGetNamespaceURI_UnregisteredPrefix_WithParent() {
        NamespaceResolver parentResolver = new NamespaceResolver();
        parentResolver.registerNamespace("x", "http://example.com/x");
        NamespaceResolver resolver = new NamespaceResolver(parentResolver);
        String prefix = "x";
        String expectedNamespaceURI = "http://example.com/x";
        String actualNamespaceURI = resolver.getNamespaceURI(prefix);
        assertEquals(expectedNamespaceURI, actualNamespaceURI);
    }

    @Test
    public void testGetNamespaceURI_SealedResolver() {
        NamespaceResolver resolver = new NamespaceResolver();
        resolver.registerNamespace("x", "http://example.com/x");
        resolver.seal();
        String prefix = "x";
        assertNull(resolver.getNamespaceURI(prefix));
    }

    @Test(expected = IllegalStateException.class)
    public void testGetNamespaceURI_NullPointer() {
        NamespaceResolver resolver = new NamespaceResolver();
        resolver.getNamespaceURI(null);
    }

    @Test
    public void testRegisterNamespace() {
        NamespaceResolver resolver = new NamespaceResolver();
        resolver.registerNamespace("x", "http://example.com/x");
        String prefix = "x";
        String expectedNamespaceURI = "http://example.com/x";
        String actualNamespaceURI = resolver.getNamespaceURI(prefix);
        assertEquals(expectedNamespaceURI, actualNamespaceURI);
    }

    @Test(expected = IllegalStateException.class)
    public void testRegisterNamespace_SealedResolver() {
        NamespaceResolver resolver = new NamespaceResolver();
        resolver.registerNamespace("x", "http://example.com/x");
        resolver.seal();
        String prefix = "x";
        try {
            resolver.registerNamespace(prefix, "http://example.com/x");
        } catch (IllegalStateException e) {
            assertEquals("Cannot register namespaces on a sealed NamespaceResolver", e.getMessage());
        }
    }

    @Test
    public void testSetNamespaceContextPointer() {
        NamespaceResolver resolver = new NamespaceResolver();
        NodePointer pointer = new NodePointer("http://example.com");
        resolver.setNamespaceContextPointer(pointer);
        String prefix = "x";
        String expectedNamespaceURI = null;
        String actualNamespaceURI = resolver.getNamespaceURI(prefix);
        assertEquals(expectedNamespaceURI, actualNamespaceURI);

        NodePointer expectedPointer = pointer;
        NodePointer actualPointer = resolver.getNamespaceContextPointer();
        assertEquals(expectedPointer, actualPointer);
    }

    @Test
    public void testGetPrefix() {
        NamespaceResolver resolver = new NamespaceResolver();
        resolver.registerNamespace("x", "http://example.com/x");
        String namespaceURI = "http://example.com/x";
        String expectedPrefix = "x";
        String actualPrefix = resolver.getPrefix(namespaceURI);
        assertEquals(expectedPrefix, actualPrefix);
    }

    @Test
    public void testGetPrefix_NullNamespaceURI() {
        NamespaceResolver resolver = new NamespaceResolver();
        assertNull(resolver.getPrefix(null));
    }

    @Test
    public void testGetPrefix_NoPrefixRegistered() {
        NamespaceResolver resolver = new NamespaceResolver();
        String namespaceURI = "http://example.com/x";
        assertNull(resolver.getPrefix(namespaceURI));
    }

    @Test
    public void testGetPrefix_ParentPrefixRegistered() {
        NamespaceResolver parentResolver = new NamespaceResolver();
        parentResolver.registerNamespace("x", "http://example.com/x");
        NamespaceResolver resolver = new NamespaceResolver(parentResolver);
        String namespaceURI = "http://example.com/x";
        String expectedPrefix = "x";
        String actualPrefix = resolver.getPrefix(namespaceURI);
        assertEquals(expectedPrefix, actualPrefix);
    }

    @Test
    public void testIsSealed() {
        NamespaceResolver resolver = new NamespaceResolver();
        assertFalse(resolver.isSealed());

        resolver.seal();
        assertTrue(resolver.isSealed());
    }

    @Test
    public void testSeal() {
        NamespaceResolver resolver = new NamespaceResolver();
        resolver.registerNamespace("x", "http://example.com/x");
        resolver.seal();
        String prefix = "x";
        assertNull(resolver.getNamespaceURI(prefix));
    }

    @Test
    public void testClone() {
        NamespaceResolver resolver = new NamespaceResolver();
        resolver.registerNamespace("x", "http://example.com/x");
        NamespaceResolver clone = (NamespaceResolver) resolver.clone();
        String prefix = "x";
        String expectedNamespaceURI = "http://example.com/x";
        String actualNamespaceURI = clone.getNamespaceURI(prefix);
        assertEquals(expectedNamespaceURI, actualNamespaceURI);
    }
}