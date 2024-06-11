package org.apache.commons.jxpath.ri;

import static org.junit.Assert.*;

import org.apache.commons.jxpath.Pointer;
import org.apache.commons.jxpath.ri.model.NodeIterator;
import org.apache.commons.jxpath.ri.model.NodePointer;
import org.apache.commons.jxpath.ri.NamespaceResolver;
import org.apache.commons.jxpath.ri.NamespaceResolverclone;

import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;

/**
 * Test class for NamespaceResolver.
 */
public class NamespaceResolverTest {

    @SuppressWarnings("unchecked")
    private NamespaceResolver createResolver() {
        return new NamespaceResolver();
    }

    @SuppressWarnings("unchecked")
    private NamespaceResolver createResolverWithParent(NamespaceResolver parent) {
        return new NamespaceResolver(parent);
    }

    /**
     * Test constructor.
     */
    @org.junit.Test
    public void testNamespaceResolverConstructor() {
        NamespaceResolver resolver = createResolver();
        assertNotNull(resolver);
        assertEquals(resolver.getClass(), NamespaceResolver.class);
        assertTrue(resolver.isSealed());
    }

    /**
     * Test constructor with parent.
     */
    @org.junit.Test
    public void testNamespaceResolverConstructorWithParent() {
        NamespaceResolver parentResolver = createResolver();
        NamespaceResolver resolver = createResolverWithParent(parentResolver);
        assertNotNull(resolver);
        assertEquals(resolver.getClass(), NamespaceResolver.class);
        assertSame(resolver.parent, parentResolver);
        assertFalse(resolver.isSealed());
    }

    /**
     * Test registerNamespace method.
     */
    @org.junit.Test
    public void testRegisterNamespace() {
        NamespaceResolver resolver = createResolver();
        String prefix = "testPrefix";
        String namespaceURI = "http://test.com/test";
        resolver.registerNamespace(prefix, namespaceURI);
        String resolvedNamespaceURI = resolver.getNamespaceURI(prefix);
        assertEquals(namespaceURI, resolvedNamespaceURI);
    }

    /**
     * Test registerNamespace method with existing prefix.
     */
    @org.junit.Test(expected = IllegalStateException.class)
    public void testRegisterNamespaceWithExistingPrefix() {
        NamespaceResolver resolver = createResolver();
        String prefix = "testPrefix";
        String namespaceURI = "http://test.com/test";
        resolver.registerNamespace(prefix, namespaceURI);
        resolver.registerNamespace(prefix, "http://another.com/test");
    }

    /**
     * Test registerNamespace method with null prefix or namespaceURI.
     */
    @org.junit.Test(expected = IllegalArgumentException.class)
    public void testRegisterNamespaceWithNullArguments() {
        NamespaceResolver resolver = createResolver();
        resolver.registerNamespace(null, "http://test.com/test");
        resolver.registerNamespace("testPrefix", null);
    }

    /**
     * Test getNamespaceURI method with registered prefix.
     */
    @org.junit.Test
    public void testGetNamespaceURI() {
        NamespaceResolver resolver = createResolver();
        String prefix = "testPrefix";
        String namespaceURI = "http://test.com/test";
        resolver.registerNamespace(prefix, namespaceURI);
        String resolvedNamespaceURI = resolver.getNamespaceURI(prefix);
        assertEquals(namespaceURI, resolvedNamespaceURI);
    }

    /**
     * Test getNamespaceURI method with unregistered prefix.
     */
    @org.junit.Test
    public void testGetNamespaceURIWithUnregisteredPrefix() {
        NamespaceResolver resolver = createResolver();
        String resolvedNamespaceURI = resolver.getNamespaceURI("testPrefix");
        assertNull(resolvedNamespaceURI);
    }

    /**
     * Test getPrefix method with registered namespaceURI.
     */
    @org.junit.Test
    public void testGetPrefix() {
        NamespaceResolver resolver = createResolver();
        String prefix = "testPrefix";
        String namespaceURI = "http://test.com/test";
        resolver.registerNamespace(prefix, namespaceURI);
        String resolvedPrefix = resolver.getPrefix(namespaceURI);
        assertEquals(prefix, resolvedPrefix);
    }

    /**
     * Test getPrefix method with unregistered namespaceURI.
     */
    @org.junit.Test
    public void testGetPrefixWithUnregisteredNamespaceURI() {
        NamespaceResolver resolver = createResolver();
        String resolvedPrefix = resolver.getPrefix("http://test.com/test");
        assertNull(resolvedPrefix);
    }

    /**
     * Test registerNamespace and getPrefix methods with same namespaceURI and different prefixes.
     */
    @org.junit.Test
    public void testRegisterNamespaceAndGetPrefixWithSameNamespaceURIAndDifferentPrefixes() {
        NamespaceResolver resolver = createResolver();
        String prefix1 = "testPrefix1";
        String prefix2 = "testPrefix2";
        String namespaceURI = "http://test.com/test";
        resolver.registerNamespace(prefix1, namespaceURI);
        resolver.registerNamespace(prefix2, namespaceURI);
        String resolvedPrefix1 = resolver.getPrefix(namespaceURI);
        String resolvedPrefix2 = resolver.getPrefix(namespaceURI);
        assertNotEquals(prefix1, resolvedPrefix2);
        assertEquals(prefix1, resolvedPrefix1);
    }

    /**
     * Test setNamespaceContextPointer method.
     */
    @org.junit.Test
    public void testSetNamespaceContextPointer() {
        NamespaceResolver resolver = createResolver();
        NodePointer pointer = new NodePointer("", null);
        resolver.setNamespaceContextPointer(pointer);
        Pointer resolvedPointer = resolver.getNamespaceContextPointer();
        assertEquals(pointer, resolvedPointer);
    }

    /**
     * Test getNamespaceContextPointer method.
     */
    @org.junit.Test
    public void testGetNamespaceContextPointer() {
        NamespaceResolver resolver = createResolver();
        NodePointer pointer = new NodePointer("", null);
        resolver.setNamespaceContextPointer(pointer);
        Pointer resolvedPointer = resolver.getNamespaceContextPointer();
        assertEquals(pointer, resolvedPointer);
        Pointer parentPointer = new NodePointer("", null);
        resolver.setNamespaceContextPointer(parentPointer);
        Pointer parentResolvedPointer = resolver.getNamespaceContextPointer();
        assertEquals(parentPointer, parentResolvedPointer);
        assertNull(resolver.getNamespaceContextPointer());
    }

    /**
     * Test isSealed method.
     */
    @org.junit.Test
    public void testIsSealed() {
        NamespaceResolver resolver = createResolver();
        assertFalse(resolver.isSealed());
        resolver.seal();
        assertTrue(resolver.isSealed());
    }

    /**
     * Test seal method.
     */
    @org.junit.Test
    public void testSeal() {
        NamespaceResolver resolver = createResolver();
        resolver.seal();
        assertThrows(IllegalStateException.class, () -> resolver.registerNamespace("testPrefix", "http://test.com/test"));
    }

    /**
     * Test clone method.
     */
    @org.junit.Test
    public void testClone() {
        NamespaceResolver resolver = createResolver();
        String prefix = "testPrefix";
        String namespaceURI = "http://test.com/test";
        resolver.registerNamespace(prefix, namespaceURI);
        NamespaceResolver clonedResolver = (NamespaceResolver) resolver.clone();
        String clonedPrefix = clonedResolver.getPrefix(namespaceURI);
        String clonedNamespaceURI = clonedResolver.getNamespaceURI(prefix);
        assertEquals(prefix, clonedPrefix);
        assertEquals(namespaceURI, clonedNamespaceURI);
    }
}