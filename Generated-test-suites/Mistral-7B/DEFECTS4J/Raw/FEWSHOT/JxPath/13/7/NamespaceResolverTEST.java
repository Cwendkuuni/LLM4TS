package org.apache.commons.cli;

import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertEquals;

import org.junit.Test;

public class NamespaceResolverTest {

    @Test
    public void testNewNamespaceResolver() {
        NamespaceResolver resolver = new NamespaceResolver();
        assertNull(resolver.getNamespaceURI("prefix"));
        assertNull(resolver.getPrefix("uri"));
        assertNull(resolver.getNamespaceContextPointer());
        assertFalse(resolver.isSealed());
    }

    @Test
    public void testNewNamespaceResolverWithParent() {
        NamespaceResolver parentResolver = new NamespaceResolver();
        NamespaceResolver resolver = new NamespaceResolver(parentResolver);
        assertNull(resolver.getNamespaceURI("prefix"));
        assertNull(resolver.getPrefix("uri"));
        assertSame(parentResolver.getNamespaceContextPointer(), resolver.getNamespaceContextPointer());
        assertFalse(resolver.isSealed());
    }

    @Test
    public void testRegisterNamespace() {
        NamespaceResolver resolver = new NamespaceResolver();
        resolver.registerNamespace("prefix", "uri");
        assertEquals("uri", resolver.getNamespaceURI("prefix"));
        assertEquals("prefix", resolver.getPrefix("uri"));
        assertNull(resolver.getNamespaceURI("otherPrefix"));
        assertNull(resolver.getPrefix("otherUri"));
        assertNull(resolver.getNamespaceContextPointer());
        assertFalse(resolver.isSealed());
    }

    @Test(expected = IllegalStateException.class)
    public void testRegisterNamespaceOnSealedResolver() {
        NamespaceResolver resolver = new NamespaceResolver();
        resolver.seal();
        resolver.registerNamespace("prefix", "uri");
    }

    @Test
    public void testSetNamespaceContextPointer() {
        NamespaceResolver resolver = new NamespaceResolver();
        NodePointer pointer = new NodePointer(new Object(), "");
        resolver.setNamespaceContextPointer(pointer);
        assertEquals(pointer, resolver.getNamespaceContextPointer());
        assertNull(resolver.getNamespaceURI("prefix"));
        assertNull(resolver.getPrefix("uri"));
        assertFalse(resolver.isSealed());
    }

    @Test
    public void testGetNamespaceURI() {
        NamespaceResolver resolver = new NamespaceResolver();
        String uri = "uri";
        String prefix = "prefix";
        resolver.registerNamespace(prefix, uri);
        resolver.setNamespaceContextPointer(new NodePointer(new Object(), ""));
        assertEquals(uri, resolver.getNamespaceURI(prefix));
        assertEquals(prefix, resolver.getPrefix(uri));
        assertEquals(null, resolver.getNamespaceURI("otherPrefix"));
        assertEquals(null, resolver.getPrefix("otherUri"));
        assertEquals(null, resolver.getNamespaceURI(null));
        assertEquals(null, resolver.getPrefix(null));
        assertFalse(resolver.isSealed());
    }

    @Test
    public void testGetPrefix() {
        NamespaceResolver resolver = new NamespaceResolver();
        String uri = "uri";
        String prefix = "prefix";
        resolver.registerNamespace(prefix, uri);
        resolver.setNamespaceContextPointer(new NodePointer(new Object(), ""));
        assertEquals(prefix, resolver.getPrefix(uri));
        assertEquals(null, resolver.getPrefix("otherUri"));
        assertEquals(null, resolver.getPrefix(null));
        assertFalse(resolver.isSealed());
    }

    @Test
    public void testSealedResolver() {
        NamespaceResolver resolver = new NamespaceResolver();
        resolver.seal();
        assertNull(resolver.getNamespaceURI("prefix"));
        assertNull(resolver.getPrefix("uri"));
        assertNull(resolver.getNamespaceContextPointer());
        assertTrue(resolver.isSealed());
    }

    @Test
    public void testCloneResolver() {
        NamespaceResolver resolver = new NamespaceResolver();
        resolver.registerNamespace("prefix", "uri");
        resolver.setNamespaceContextPointer(new NodePointer(new Object(), ""));
        NamespaceResolver clonedResolver = (NamespaceResolver) resolver.clone();
        assertEquals("uri", clonedResolver.getNamespaceURI("prefix"));
        assertEquals("prefix", clonedResolver.getPrefix("uri"));
        assertFalse(clonedResolver.isSealed());
    }

}