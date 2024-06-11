package org.apache.commons.cli;

import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertEquals;
import java.util.HashMap;
import java.util.Map;

import org.apache.commons.jxpath.Pointer;
import org.apache.commons.jxpath.ri.model.NodeIterator;
import org.apache.commons.jxpath.ri.model.NodePointer;
import org.junit.Test;

/**
 * Test cases for NamespaceResolver class
 */
public class NamespaceResolverTest {

    /**
     * Test constructor
     */
    @Test
    public void testConstructor() {
        NamespaceResolver resolver = new NamespaceResolver();
        assertNull(resolver.getParent());
        NamespaceResolver parentResolver = new NamespaceResolver(null);
        NamespaceResolver resolverWithParent = new NamespaceResolver(parentResolver);
        assertEquals(parentResolver, resolverWithParent.getParent());
    }

    /**
     * Test registerNamespace method
     */
    @Test
    public void testRegisterNamespace() {
        NamespaceResolver resolver = new NamespaceResolver();
        resolver.registerNamespace("prefix1", "http://test1.com");
        assertEquals("http://test1.com", resolver.getNamespaceURI("prefix1"));
        resolver.registerNamespace("prefix2", "http://test2.com");
        assertEquals("http://test2.com", resolver.getNamespaceURI("prefix2"));
        assertEquals(2, resolver.namespaceMap.size());

        // Test registering the same prefix twice
        resolver.registerNamespace("prefix1", "http://test1.com");
        assertEquals("http://test1.com", resolver.getNamespaceURI("prefix1"));

        // Test registering null prefix
        try {
            resolver.registerNamespace(null, "http://test3.com");
            assert false;
        } catch (IllegalArgumentException e) {
            // expected
        }

        // Test registering null uri
        try {
            resolver.registerNamespace("prefix3", null);
            assert false;
        } catch (IllegalArgumentException e) {
            // expected
        }

        // Test registering namespace with sealed resolver
        resolver.seal();
        try {
            resolver.registerNamespace("prefix4", "http://test4.com");
            assert false;
        } catch (IllegalStateException e) {
            // expected
        }

    }

    /**
     * Test getNamespaceURI method with registered namespaces
     */
    @Test
    public void testGetNamespaceURI() {
        NamespaceResolver resolver = new NamespaceResolver();
        resolver.registerNamespace("prefix1", "http://test1.com");
        resolver.registerNamespace("prefix2", "http://test2.com");
        assertEquals("http://test1.com", resolver.getNamespaceURI("prefix1"));
        assertEquals("http://test2.com", resolver.getNamespaceURI("prefix2"));
        assertNull(resolver.getNamespaceURI("unknownPrefix"));

        NamespaceResolver parentResolver = new NamespaceResolver();
        parentResolver.registerNamespace("prefix3", "http://parentTest.com");
        NamespaceResolver resolverWithParent = new NamespaceResolver(parentResolver);
        assertEquals("http://parentTest.com", resolverWithParent.getNamespaceURI("prefix3"));
        assertEquals("http://test1.com", resolverWithParent.getNamespaceURI("prefix1"));
        assertEquals("http://test2.com", resolverWithParent.getNamespaceURI("prefix2"));
        assertNull(resolverWithParent.getNamespaceURI("unknownPrefix"));
    }

    /**
     * Test getNamespaceURI method with namespace context pointer
     */
    @Test
    public void testGetNamespaceURIWithContextPointer() {
        NamespaceResolver resolver = new NamespaceResolver();
        Pointer pointer = new NodePointer(new Object(), new String[0], new String[0], null);
        resolver.setNamespaceContextPointer(pointer);
        resolver.registerNamespace("prefix1", "http://test1.com");
        assertEquals("http://test1.com", resolver.getNamespaceURI("prefix1"));

        NodePointer nsPointer = new NodePointer(new Object(), new String[]{"xmlns:prefix2"}, new String[]{"http://test2.com"}, null);
        resolver.setNamespaceContextPointer(nsPointer);
        assertEquals("http://test2.com", resolver.getNamespaceURI("prefix2"));
        assertEquals("http://test1.com", resolver.getNamespaceURI("prefix1"));
        assertNull(resolver.getNamespaceURI("unknownPrefix"));
    }

    /**
     * Test getPrefix method with registered namespaces
     */
    @Test
    public void testGetPrefix() {
        NamespaceResolver resolver = new NamespaceResolver();
        resolver.registerNamespace("prefix1", "http://test1.com");
        resolver.registerNamespace("prefix2", "http://test2.com");
        assertEquals("prefix1", resolver.getPrefix("http://test1.com"));
        assertEquals("prefix2", resolver.getPrefix("http://test2.com"));
        assertNull(resolver.getPrefix("unknownURI"));

        NamespaceResolver parentResolver = new NamespaceResolver();
        parentResolver.registerNamespace("prefix3", "http://parentTest.com");
        NamespaceResolver resolverWithParent = new NamespaceResolver(parentResolver);
        assertEquals("prefix3", resolverWithParent.getPrefix("http://parentTest.com"));
        assertEquals("prefix1", resolverWithParent.getPrefix("http://test1.com"));
        assertEquals("prefix2", resolverWithParent.getPrefix("http://test2.com"));
        assertNull(resolverWithParent.getPrefix("unknownURI"));
    }

    /**
     * Test getPrefix method with reverse map
     */
    @Test
    public void testGetPrefixWithReverseMap() {
        NamespaceResolver resolver = new NamespaceResolver();
        resolver.registerNamespace("prefix1", "http://test1.com");
        resolver.registerNamespace("prefix2", "http://test2.com");
        resolver.seal();

        NodePointer pointer = new NodePointer(new Object(), new String[0], new String[0], null);
        resolver.setNamespaceContextPointer(pointer);
        resolver.registerNamespace("prefix3", "http://test3.com");
        resolver.registerNamespace("prefix4", "http://test4.com");

        assertEquals("prefix1", resolver.getPrefix("http://test1.com"));
        assertEquals("prefix2", resolver.getPrefix("http://test2.com"));
        assertNull(resolver.getPrefix("unknownURI"));

        assertEquals("prefix3", resolver.getPrefix("http://test3.com"));
        assertEquals("prefix4", resolver.getPrefix("http://test4.com"));
        assertNull(resolver.getPrefix("http://test1.com"));
        assertNull(resolver.getPrefix("http://test2.com"));
    }

    /**
     * Test isSealed method
     */
    @Test
    public void testIsSealed() {
        NamespaceResolver resolver = new NamespaceResolver();
        assertFalse(resolver.isSealed());

        resolver.seal();
        assertTrue(resolver.isSealed());

        NamespaceResolver parentResolver = new NamespaceResolver();
        parentResolver.seal();
        NamespaceResolver resolverWithParent = new NamespaceResolver(parentResolver);
        assertFalse(resolverWithParent.isSealed());
    }

    /**
     * Test clone method
     */
    @Test
    public void testClone() {
        NamespaceResolver resolver = new NamespaceResolver();
        resolver.registerNamespace("prefix1", "http://test1.com");
        resolver.registerNamespace("prefix2", "http://test2.com");
        NamespaceResolver clonedResolver = (NamespaceResolver) resolver.clone();
        assertEquals("http://test1.com", clonedResolver.getNamespaceURI("prefix1"));
        assertEquals("http://test2.com", clonedResolver.getNamespaceURI("prefix2"));
    }
}