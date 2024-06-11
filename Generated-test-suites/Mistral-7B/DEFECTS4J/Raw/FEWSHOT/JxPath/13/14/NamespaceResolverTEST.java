package org.apache.commons.cli;

import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertEquals;
import java.util.HashMap;
import java.util.Map;

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
    public void testNamespaceResolverWithParent() {
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
        Map namespaceMap = resolver.namespaceMap;
        assertEquals("http://example.com/x", namespaceMap.get("x"));
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
    public void testGetNamespaceURI() {
        NamespaceResolver resolver = new NamespaceResolver();
        resolver.registerNamespace("x", "http://example.com/x");
        assertEquals("http://example.com/x", resolver.getNamespaceURI("x"));
        assertEquals(null, resolver.getNamespaceURI("y"));
        NodePointer pointer = new NodePointer(new HashMap(), null, null, null, null, null, null, null, null, null, null);
        resolver.setNamespaceContextPointer(pointer);
        assertEquals("http://example.com/x", resolver.getNamespaceURI("x"));
        assertEquals("http://example.com/y", pointer.getNamespaceURI("y"));
        assertEquals(null, resolver.getNamespaceURI("z"));
    }

    @Test
    public void testGetPrefix() {
        NamespaceResolver resolver = new NamespaceResolver();
        resolver.registerNamespace("x", "http://example.com/x");
        assertEquals("x", resolver.getPrefix("http://example.com/x"));
        assertEquals(null, resolver.getPrefix("http://example.com/y"));
        NodePointer pointer = new NodePointer(new HashMap(), null, null, null, null, null, null, null, null, null, null);
        resolver.setNamespaceContextPointer(pointer);
        assertEquals("x", resolver.getPrefix("http://example.com/x"));
        assertEquals("y", pointer.getPrefix("http://example.com/y"));
        assertEquals(null, resolver.getPrefix("http://example.com/z"));
    }

    @Test
    public void testIsSealed() {
        NamespaceResolver resolver = new NamespaceResolver();
        assertFalse(resolver.isSealed());
        resolver.seal();
        assertTrue(resolver.isSealed());
    }

    @Test
    public void testClone() {
        NamespaceResolver resolver = new NamespaceResolver();
        NamespaceResolver clonedResolver = (NamespaceResolver) resolver.clone();
        assertNotNull(clonedResolver);
        assertFalse(clonedResolver.isSealed());
        assertNull(clonedResolver.namespaceMap);
        assertNull(clonedResolver.reverseMap);
        assertNull(clonedResolver.pointer);
        assertEquals(resolver, clonedResolver.parent);
    }
}