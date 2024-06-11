package org.apache.commons.jxpath.ri;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNull;

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
        resolver.registerNamespace("xml", "http://www.w3.org/XML/1998/namespace");
        assertEquals("http://www.w3.org/XML/1998/namespace", resolver.getNamespaceURI("xml"));
    }

    @Test(expected = IllegalStateException.class)
    public void testRegisterNamespaceWhenSealed() {
        resolver.seal();
        resolver.registerNamespace("xml", "http://www.w3.org/XML/1998/namespace");
    }

    @Test
    public void testGetNamespaceURI() {
        resolver.registerNamespace("xml", "http://www.w3.org/XML/1998/namespace");
        assertEquals("http://www.w3.org/XML/1998/namespace", resolver.getNamespaceURI("xml"));
    }

    @Test
    public void testGetNamespaceURIWhenUndefinedPrefix() {
        assertNull(resolver.getNamespaceURI("xml"));
    }

    @Test
    public void testGetNamespaceURIWhenDefinedInPointer() {
        pointer.registerNamespace("xml", "http://www.w3.org/XML/1998/namespace");
        assertEquals("http://www.w3.org/XML/1998/namespace", resolver.getNamespaceURI("xml"));
    }

    @Test
    public void testGetNamespaceURIWhenParentHasNamespace() {
        NamespaceResolver parent = new NamespaceResolver();
        parent.registerNamespace("xml", "http://www.w3.org/XML/1998/namespace");
        NamespaceResolver child = new NamespaceResolver(parent);
        assertEquals("http://www.w3.org/XML/1998/namespace", child.getNamespaceURI("xml"));
    }

    @Test
    public void testGetPrefix() {
        resolver.registerNamespace("xml", "http://www.w3.org/XML/1998/namespace");
        assertEquals("xml", resolver.getPrefix("http://www.w3.org/XML/1998/namespace"));
    }

    @Test
    public void testGetPrefixWhenUndefinedNamespaceURI() {
        assertNull(resolver.getPrefix("http://www.w3.org/XML/1998/namespace"));
    }

    @Test
    public void testGetPrefixWhenDefinedInPointer() {
        pointer.registerNamespace("xml", "http://www.w3.org/XML/1998/namespace");
        assertEquals("xml", resolver.getPrefix("http://www.w3.org/XML/1998/namespace"));
    }

    @Test
    public void testGetPrefixWhenParentHasNamespace() {
        NamespaceResolver parent = new NamespaceResolver();
        parent.registerNamespace("xml", "http://www.w3.org/XML/1998/namespace");
        NamespaceResolver child = new NamespaceResolver(parent);
        assertEquals("xml", child.getPrefix("http://www.w3.org/XML/1998/namespace"));
    }

    @Test
    public void testIsSealed() {
        assertEquals(false, resolver.isSealed());
        resolver.seal();
        assertEquals(true, resolver.isSealed());
    }

    @Test
    public void testClone() {
        resolver.registerNamespace("xml", "http://www.w3.org/XML/1998/namespace");
        resolver.seal();

        NamespaceResolver clone = (NamespaceResolver) resolver.clone();
        assertEquals(true, clone.isSealed());
        assertEquals("http://www.w3.org/XML/1998/namespace", clone.getNamespaceURI("xml"));
    }
}