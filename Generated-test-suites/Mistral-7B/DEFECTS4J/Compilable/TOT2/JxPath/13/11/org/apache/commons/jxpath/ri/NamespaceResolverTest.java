package org.apache.commons.jxpath.ri;

import org.junit.Test;
import static org.junit.Assert.*;
import org.apache.commons.jxpath.ri.NamespaceResolver;

public class NamespaceResolverTest {
    // Test cases for getNamespaceURI method

    // ...

    // Test cases for registerNamespace method

    @Test
    public void testRegisterNamespace_NullPrefix() {
        NamespaceResolver resolver = new NamespaceResolver();
        assertFalse(resolver.isSealed());
        try {
            resolver.registerNamespace(null, "http://example.com");
            fail("Expected IllegalArgumentException");
        } catch (IllegalArgumentException e) {
            // Expected
        }
        assertFalse(resolver.isSealed());
    }

    @Test
    public void testRegisterNamespace_EmptyPrefix() {
        NamespaceResolver resolver = new NamespaceResolver();
        assertFalse(resolver.isSealed());
        try {
            resolver.registerNamespace("", "http://example.com");
            fail("Expected IllegalArgumentException");
        } catch (IllegalArgumentException e) {
            // Expected
        }
        assertFalse(resolver.isSealed());
    }

    @Test
    public void testRegisterNamespace_SealedResolver() {
        NamespaceResolver resolver = new NamespaceResolver();
        resolver.registerNamespace("xsd", "http://www.w3.org/2001/XMLSchema");
        resolver.seal();
        String prefix = "xsd";
        try {
            resolver.registerNamespace(prefix, "http://example.com");
            fail("Expected IllegalStateException");
        } catch (IllegalStateException e) {
            // Expected
        }
        assertTrue(resolver.isSealed());
    }

    @Test
    public void testRegisterNamespace_SamePrefix() {
        NamespaceResolver resolver = new NamespaceResolver();
        resolver.registerNamespace("xsd", "http://example.com");
        try {
            resolver.registerNamespace("xsd", "http://example.com");
            fail("Expected IllegalArgumentException");
        } catch (IllegalArgumentException e) {
            // Expected
        }
        assertEquals("http://example.com", resolver.getNamespaceURI("xsd"));
    }

    // Test cases for setNamespaceContextPointer method

    // ...

    // Test cases for getPrefix method

    // ...

    // Test cases for isSealed method

    @Test
    public void testIsSealed_NewResolver() {
        NamespaceResolver resolver = new NamespaceResolver();
        assertFalse(resolver.isSealed());
    }

    @Test
    public void testIsSealed_SealedResolver() {
        NamespaceResolver resolver = new NamespaceResolver();
        resolver.seal();
        assertTrue(resolver.isSealed());
    }

    // Test cases for seal method

    @Test
    public void testSeal_NewResolver() {
        NamespaceResolver resolver = new NamespaceResolver();
        assertFalse(resolver.isSealed());
        resolver.seal();
        assertTrue(resolver.isSealed());
    }

    @Test
    public void testSeal_SealedResolver() {
        NamespaceResolver resolver = new NamespaceResolver();
        resolver.seal();
        assertTrue(resolver.isSealed());
        resolver.seal();
        assertTrue(resolver.isSealed());
    }

    // Test cases for clone method

    @Test
    public void testClone() {
        NamespaceResolver resolver = new NamespaceResolver();
        NamespaceResolver clone = (NamespaceResolver) resolver.clone();
        assertFalse(resolver.isSealed());
        assertFalse(clone.isSealed());
        resolver.registerNamespace("xsd", "http://example.com");
        assertFalse(resolver.isSealed());
        assertFalse(clone.isSealed());
        resolver.seal();
        assertTrue(resolver.isSealed());
        assertFalse(clone.isSealed());
    }
}