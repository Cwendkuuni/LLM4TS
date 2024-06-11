package org.apache.commons.jxpath.ri;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertTrue;

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
    }

    @Test
    public void testRegisterNamespace() {
        resolver.registerNamespace("a", "http://www.example.com/ns1");
        resolver.registerNamespace("b", "http://www.example.com/ns2");

        assertEquals("http://www.example.com/ns1", resolver.getNamespaceURI("a"));
        assertEquals("http://www.example.com/ns2", resolver.getNamespaceURI("b"));

        assertNull(resolver.getNamespaceURI("c"));
    }

    @Test(expected = IllegalStateException.class)
    public void testRegisterNamespaceSealed() {
        resolver.seal();
        resolver.registerNamespace("a", "http://www.example.com/ns1");
    }

    @Test
    public void testSetNamespaceContextPointer() {
        resolver.setNamespaceContextPointer(pointer);
        assertEquals(pointer, resolver.getNamespaceContextPointer());
    }

    @Test
    public void testGetNamespaceURI() {
        resolver.registerNamespace("a", "http://www.example.com/ns1");
        resolver.registerNamespace("b", "http://www.example.com/ns2");
        pointer.setNamespaceURI("http://www.example.com/ns3");

        assertEquals("http://www.example.com/ns1", resolver.getNamespaceURI("a"));
        assertEquals("http://www.example.com/ns2", resolver.getNamespaceURI("b"));
        assertEquals("http://www.example.com/ns3", resolver.getNamespaceURI("c"));
        assertNull(resolver.getNamespaceURI("d"));
    }

    @Test
    public void testGetPrefix() {
        resolver.registerNamespace("a", "http://www.example.com/ns1");
        resolver.registerNamespace("b", "http://www.example.com/ns2");
        pointer.setNamespaceURI("http://www.example.com/ns3");

        assertEquals("a", resolver.getPrefix("http://www.example.com/ns1"));
        assertEquals("b", resolver.getPrefix("http://www.example.com/ns2"));
        assertNull(resolver.getPrefix("http://www.example.com/ns3"));
    }

    @Test
    public void testGetPrefixWithParent() {
        NamespaceResolver parent = new NamespaceResolver();
        parent.registerNamespace("a", "http://www.example.com/ns1");
        parent.registerNamespace("b", "http://www.example.com/ns2");
        resolver = new NamespaceResolver(parent);
        pointer.setNamespaceURI("http://www.example.com/ns3");

        assertEquals("a", resolver.getPrefix("http://www.example.com/ns1"));
        assertEquals("b", resolver.getPrefix("http://www.example.com/ns2"));
        assertNull(resolver.getPrefix("http://www.example.com/ns3"));
    }

    @Test
    public void testSeal() {
        assertFalse(resolver.isSealed());
        resolver.seal();
        assertTrue(resolver.isSealed());
    }

    @Test
    public void testClone() {
        resolver.setNamespaceContextPointer(pointer);
        NamespaceResolver clonedResolver = (NamespaceResolver) resolver.clone();
        assertEquals(pointer, clonedResolver.getNamespaceContextPointer());
        assertFalse(clonedResolver.isSealed());
    }
}