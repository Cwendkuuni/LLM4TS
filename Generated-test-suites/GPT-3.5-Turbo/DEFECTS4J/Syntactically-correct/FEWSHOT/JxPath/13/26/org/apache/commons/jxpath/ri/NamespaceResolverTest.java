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

    @Before
    public void setUp() {
        resolver = new NamespaceResolver();
    }

    @Test
    public void testGetNamespaceURI() {
        resolver.registerNamespace("prefix", "http://www.example.com");
        assertEquals("http://www.example.com", resolver.getNamespaceURI("prefix"));
        assertNull(resolver.getNamespaceURI("anotherPrefix"));
    }

    @Test
    public void testGetPrefix() {
        resolver.registerNamespace("prefix", "http://www.example.com");
        assertEquals("prefix", resolver.getPrefix("http://www.example.com"));
        assertNull(resolver.getPrefix("http://www.another-example.com"));
    }

    @Test
    public void testSetNamespaceContextPointer() {
        NodePointer pointer = new NodePointer();
        resolver.setNamespaceContextPointer(pointer);
        assertEquals(pointer, resolver.getNamespaceContextPointer());
    }

    @Test
    public void testRegisterNamespace() {
        resolver.registerNamespace("prefix", "http://www.example.com");
        assertEquals("http://www.example.com", resolver.getNamespaceURI("prefix"));
        assertEquals("prefix", resolver.getPrefix("http://www.example.com"));
    }

    @Test(expected = IllegalStateException.class)
    public void testRegisterNamespaceSealed() {
        resolver.seal();
        resolver.registerNamespace("prefix", "http://www.example.com");
    }

    @Test
    public void testSeal() {
        resolver.seal();
        assertEquals(true, resolver.isSealed());
    }

    @Test
    public void testClone() {
        NamespaceResolver clonedResolver = (NamespaceResolver) resolver.clone();
        assertEquals(false, clonedResolver.isSealed());
    }
}