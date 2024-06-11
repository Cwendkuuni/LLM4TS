package org.apache.commons.jxpath.ri;

import static org.junit.Assert.*;

import org.apache.commons.jxpath.JXPathContext;
import org.apache.commons.jxpath.NamespaceResolver;
import org.apache.commons.jxpath.Pointer;
import org.apache.commons.jxpath.ri.model.NodeIterator;
import org.apache.commons.jxpath.ri.model.NodePointer;
import org.junit.Before;
import org.junit.Test;

public class NamespaceResolverTest {

    private NamespaceResolver resolver;
    private NodePointer pointer;

    @Before
    public void setUp() {
        resolver = new NamespaceResolver();
        pointer = new NodePointer();
        resolver.setNamespaceContextPointer(pointer);
    }

    @Test
    public void testRegisterNamespace() {
        resolver.registerNamespace("p", "uri");
        assertEquals("uri", resolver.getNamespaceURI("p"));
    }

    @Test(expected = IllegalStateException.class)
    public void testRegisterNamespaceSealed() {
        resolver.seal();
        resolver.registerNamespace("p", "uri");
    }

    @Test
    public void testSetNamespaceContextPointer() {
        assertNull(resolver.getNamespaceContextPointer());
        resolver.setNamespaceContextPointer(pointer);
        assertSame(pointer, resolver.getNamespaceContextPointer());
    }

    @Test
    public void testGetNamespaceURI() {
        assertEquals("uri", resolver.getNamespaceURI("p"));
    }

    @Test
    public void testGetNamespaceURIUndefinedPrefix() {
        assertNull(resolver.getNamespaceURI("undefined"));
    }

    @Test
    public void testGetNamespaceURINullPointer() {
        resolver.setNamespaceContextPointer(null);
        assertNull(resolver.getNamespaceURI("p"));
    }

    @Test
    public void testGetNamespaceURIWithParentResolver() {
        NamespaceResolver parent = new NamespaceResolver();
        parent.registerNamespace("p", "parentUri");
        resolver = new NamespaceResolver(parent);
        assertEquals("parentUri", resolver.getNamespaceURI("p"));
    }

    @Test
    public void testGetPrefix() {
        assertEquals("p", resolver.getPrefix("uri"));
    }

    @Test
    public void testGetPrefixUndefinedNamespaceURI() {
        assertNull(resolver.getPrefix("undefined"));
    }

    @Test
    public void testGetPrefixWithParentResolver() {
        NamespaceResolver parent = new NamespaceResolver();
        parent.registerNamespace("p", "parentUri");
        resolver = new NamespaceResolver(parent);
        assertEquals("p", resolver.getPrefix("parentUri"));
    }

    @Test
    public void testIsSealed() {
        assertFalse(resolver.isSealed());
        resolver.seal();
        assertTrue(resolver.isSealed());
    }

    @Test
    public void testClone() {
        resolver.registerNamespace("p", "uri");
        NamespaceResolver clone = (NamespaceResolver) resolver.clone();
        assertEquals("uri", clone.getNamespaceURI("p"));
        assertFalse(clone.isSealed());
    }
}