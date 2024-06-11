package org.apache.commons.jxpath.ri;

import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertEquals;

import org.apache.commons.cli. utils.Util;
import org.apache.commons.jxpath.Pointer;
import org.apache.commons.jxpath.ri.model.NodeIterator;
import org.apache.commons.jxpath.ri.model.NodePointer;
import org.apache.commons.jxpath.ri.NamespaceResolver;
import org.junit.Test;

public class NamespaceResolverTest {

    @Test
    public void testNewNamespaceResolver() {
        NamespaceResolver resolver = new NamespaceResolver();
        assertNull(resolver.parent);
        assertNull(resolver.namespaceMap);
        assertNull(resolver.reverseMap);
        assertNull(resolver.pointer);
        assertFalse(resolver.isSealed());
    }

    @Test
    public void testNewNamespaceResolverWithParent() {
        NamespaceResolver parentResolver = new NamespaceResolver();
        NamespaceResolver childResolver = new NamespaceResolver(parentResolver);
        assertEquals(parentResolver, childResolver.parent);
        assertNull(childResolver.namespaceMap);
        assertNull(childResolver.reverseMap);
        assertNull(childResolver.pointer);
        assertFalse(childResolver.isSealed());
    }

    @Test
    public void testRegisterNamespace() {
        NamespaceResolver resolver = new NamespaceResolver();
        resolver.registerNamespace("x", "http://example.com/x");
        assertEquals("x", resolver.namespaceMap.keySet().iterator().next());
        assertEquals("http://example.com/x", resolver.namespaceMap.get("x"));
        assertNull(resolver.reverseMap);
        assertNull(resolver.pointer);
        assertFalse(resolver.isSealed());
    }

    @Test(expected = IllegalStateException.class)
    public void testRegisterNamespaceOnSealedResolver() {
        NamespaceResolver resolver = new NamespaceResolver();
        resolver.seal();
        resolver.registerNamespace("x", "http://example.com/x");
    }

    @Test
    public void testSetNamespaceContextPointer() {
        NamespaceResolver resolver = new NamespaceResolver();
        NodePointer pointer = new NodePointer();
        resolver.setNamespaceContextPointer(pointer);
        assertEquals(pointer, resolver.getNamespaceContextPointer());
        assertNull(resolver.namespaceMap);
        assertNull(resolver.reverseMap);
        assertNull(resolver.pointer);
        assertFalse(resolver.isSealed());
    }

    @Test
    public void testGetNamespaceURI() {
        NamespaceResolver resolver = new NamespaceResolver();
        NodePointer pointer = new NodePointer();
        resolver.setNamespaceContextPointer(pointer);
        String prefix = "x";
        String namespaceURI = "http://example.com/x";
        pointer.registerNamespace(prefix, namespaceURI);
        assertEquals(namespaceURI, resolver.getNamespaceURI(prefix));
        assertEquals(namespaceURI, resolver.getNamespaceURI(prefix));
        assertEquals(prefix, resolver.getPrefix(namespaceURI));
        assertNull(resolver.namespaceMap);
        assertNull(resolver.reverseMap);
        assertNull(resolver.pointer);
        assertFalse(resolver.isSealed());
    }

    @Test
    public void testGetNamespaceURIWithParent() {
        NamespaceResolver parentResolver = new NamespaceResolver();
        parentResolver.registerNamespace("x", "http://example.com/x");
        NamespaceResolver childResolver = new NamespaceResolver(parentResolver);
        NodePointer pointer = new NodePointer();
        childResolver.setNamespaceContextPointer(pointer);
        String prefix = "x";
        String namespaceURI = "http://example.com/x";
        pointer.registerNamespace(prefix, namespaceURI);
        assertEquals(namespaceURI, childResolver.getNamespaceURI(prefix));
        assertEquals(namespaceURI, parentResolver.getNamespaceURI(prefix));
        assertEquals(prefix, childResolver.getPrefix(namespaceURI));
        assertEquals(prefix, parentResolver.getPrefix(namespaceURI));
        assertNull(childResolver.namespaceMap);
        assertNull(childResolver.reverseMap);
        assertNull(childResolver.pointer);
        assertNull(parentResolver.namespaceMap);
        assertNull(parentResolver.reverseMap);
        assertNull(parentResolver.pointer);
        assertFalse(childResolver.isSealed());
        assertFalse(parentResolver.isSealed());
    }

    @Test
    public void testGetPrefix() {
        NamespaceResolver resolver = new NamespaceResolver();
        NodePointer pointer = new NodePointer();
        resolver.setNamespaceContextPointer(pointer);
        String prefix = "x";
        String namespaceURI = "http://example.com/x";
        pointer.registerNamespace(prefix, namespaceURI);
        String prefixFromResolver = resolver.getPrefix(namespaceURI);
        String prefixFromPointer = pointer.getPrefix(namespaceURI);
        assertEquals(prefix, prefixFromResolver);
        assertEquals(prefix, prefixFromPointer);
        assertNull(resolver.namespaceMap);
        assertNull(resolver.reverseMap);
        assertNull(resolver.pointer);
        assertFalse(resolver.isSealed());
    }

    @Test
    public void testGetPrefixWithParent() {
        NamespaceResolver parentResolver = new NamespaceResolver();
        parentResolver.registerNamespace("x", "http://example.com/x");
        NamespaceResolver childResolver = new NamespaceResolver(parentResolver);
        NodePointer pointer = new NodePointer();
        childResolver.setNamespaceContextPointer(pointer);
        String prefix = "x";
        String namespaceURI = "http://example.com/x";
        pointer.registerNamespace(prefix, namespaceURI);
        String prefixFromResolver = childResolver.getPrefix(namespaceURI);
        String prefixFromParentResolver = parentResolver.getPrefix(namespaceURI);
        assertEquals(prefix, prefixFromResolver);
        assertEquals(prefix, prefixFromParentResolver);
        assertNull(childResolver.namespaceMap);
        assertNull(childResolver.reverseMap);
        assertNull(childResolver.pointer);
        assertNull(parentResolver.namespaceMap);
        assertNull(parentResolver.reverseMap);
        assertNull(parentResolver.pointer);
        assertFalse(childResolver.isSealed());
        assertFalse(parentResolver.isSealed());
    }

    @Test
    public void testIsSealed() {
        NamespaceResolver resolver = new NamespaceResolver();
        assertFalse(resolver.isSealed());
        resolver.seal();
        assertTrue(resolver.isSealed());
        assertNull(resolver.namespaceMap);
        assertNull(resolver.reverseMap);
        assertNull(resolver.pointer);
    }

    @Test
    public void testClone() {
        NamespaceResolver resolver = new NamespaceResolver();
        resolver.registerNamespace("x", "http://example.com/x");
        NodePointer pointer = new NodePointer();
        resolver.setNamespaceContextPointer(pointer);
        String prefix = "x";
        String namespaceURI = "http://example.com/x";
        pointer.registerNamespace(prefix, namespaceURI);
        NamespaceResolver clonedResolver = (NamespaceResolver) resolver.clone();
        assertEquals(resolver.getNamespaceURI(prefix), clonedResolver.getNamespaceURI(prefix));
        assertEquals(resolver.getPrefix(namespaceURI), clonedResolver.getPrefix(namespaceURI));
        assertEquals(resolver.isSealed(), clonedResolver.isSealed());
        assertNull(clonedResolver.namespaceMap);
        assertNull(clonedResolver.reverseMap);
        assertNull(clonedResolver.pointer);
    }
}