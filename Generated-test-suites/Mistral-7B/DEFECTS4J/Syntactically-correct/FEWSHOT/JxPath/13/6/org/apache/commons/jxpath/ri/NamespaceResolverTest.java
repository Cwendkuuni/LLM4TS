package org.apache.commons.jxpath.ri;

import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertEquals;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mockito;
import org.mockito.junit.MockitoJUnitRunner;

import org.apache.commons.jxpath.Pointer;
import org.apache.commons.jxpath.ri.model.NodeIterator;
import org.apache.commons.jxpath.ri.model.NodePointer;
import org.apache.commons.jxpath.ri.NamespaceResolver;

@RunWith(MockitoJUnitRunner.class)
public class NamespaceResolverTest {

    @Test
    public void testNewInstance() {
        NamespaceResolver nsResolver = new NamespaceResolver();
        assertNull(nsResolver.getParent());
        assertNull(nsResolver.getNamespaceMap());
        assertNull(nsResolver.getReverseMap());
        assertNull(nsResolver.getPointer());
        assertFalse(nsResolver.isSealed());
    }

    @Test
    public void testNewInstanceWithParent() {
        NamespaceResolver parentNsResolver = Mockito.mock(NamespaceResolver.class);
        NamespaceResolver nsResolver = new NamespaceResolver(parentNsResolver);
        assertEquals(parentNsResolver, nsResolver.getParent());
        assertNull(nsResolver.getNamespaceMap());
        assertNull(nsResolver.getReverseMap());
        assertNull(nsResolver.getPointer());
        assertFalse(nsResolver.isSealed());
    }

    @Test
    public void testRegisterNamespace() {
        NamespaceResolver nsResolver = new NamespaceResolver();
        nsResolver.registerNamespace("x", "http://example.com/x");
        assertEquals("http://example.com/x", nsResolver.getNamespaceURI("x"));
        assertEquals("x", nsResolver.getPrefix("http://example.com/x"));
        assertNull(nsResolver.getNamespaceURI("y"));
        assertNull(nsResolver.getPrefix("http://example.org/y"));
    }

    @Test
    public void testRegisterNamespaceWithParent() {
        NamespaceResolver parentNsResolver = Mockito.mock(NamespaceResolver.class);
        NamespaceResolver nsResolver = new NamespaceResolver(parentNsResolver);
        nsResolver.registerNamespace("x", "http://example.com/x");
        assertEquals("http://example.com/x", nsResolver.getNamespaceURI("x"));
        assertEquals("x", nsResolver.getPrefix("http://example.com/x"));
        assertEquals("x", parentNsResolver.getPrefix("http://example.com/x"));
        assertEquals("http://example.com/x", parentNsResolver.getNamespaceURI("x"));
    }

    @Test
    public void testSetNamespaceContextPointer() {
        NamespaceResolver nsResolver = new NamespaceResolver();
        NodePointer nodePointer = Mockito.mock(NodePointer.class);
        nsResolver.setNamespaceContextPointer(nodePointer);
        assertEquals(nodePointer, nsResolver.getNamespaceContextPointer());
        assertNull(nsResolver.getPointer());
    }

    @Test
    public void testGetNamespaceContextPointer() {
        NamespaceResolver nsResolver = new NamespaceResolver();
        NodePointer nodePointer = Mockito.mock(NodePointer.class);
        nsResolver.setNamespaceContextPointer(nodePointer);
        assertEquals(nodePointer, nsResolver.getNamespaceContextPointer());
        NodePointer parentPointer = Mockito.mock(NodePointer.class);
        nsResolver.setNamespaceContextPointer(null);
        nsResolver.setNamespaceContextPointer(parentPointer);
        assertEquals(parentPointer, nsResolver.getNamespaceContextPointer());
    }

    @Test(expected = IllegalStateException.class)
    public void testRegisterNamespaceAfterSealed() {
        NamespaceResolver nsResolver = new NamespaceResolver();
        nsResolver.seal();
        nsResolver.registerNamespace("x", "http://example.com/x");
    }

    @Test
    public void testGetNamespaceURI() {
        NamespaceResolver nsResolver = new NamespaceResolver();
        nsResolver.registerNamespace("x", "http://example.com/x");
        assertEquals("http://example.com/x", nsResolver.getNamespaceURI("x"));
        assertEquals("http://example.com/x", nsResolver.getNamespaceURI("x"));
        NodePointer nodePointer = Mockito.mock(NodePointer.class);
        nsResolver.setNamespaceContextPointer(nodePointer);
        assertEquals("http://example.com/x", nsResolver.getNamespaceURI("x"));
        assertEquals("http://example.org/y", nsResolver.getNamespaceURI("y"));
    }

    @Test
    public void testGetPrefix() {
        NamespaceResolver nsResolver = new NamespaceResolver();
        nsResolver.registerNamespace("x", "http://example.com/x");
        assertEquals("x", nsResolver.getPrefix("http://example.com/x"));
        assertEquals("x", nsResolver.getPrefix("http://example.com/x"));
        NodePointer nodePointer = Mockito.mock(NodePointer.class);
        nsResolver.setNamespaceContextPointer(nodePointer);
        assertEquals("x", nsResolver.getPrefix("http://example.com/x"));
        assertEquals(null, nsResolver.getPrefix("http://example.org/y"));
    }

    @Test
    public void testGetPrefixWithParent() {
        NamespaceResolver parentNsResolver = Mockito.mock(NamespaceResolver.class);
        NamespaceResolver nsResolver = new NamespaceResolver(parentNsResolver);
        nsResolver.registerNamespace("x", "http://example.com/x");
        assertEquals("x", nsResolver.getPrefix("http://example.com/x"));
        assertEquals("x", nsResolver.getPrefix("http://example.com/x"));
        nsResolver.setNamespaceContextPointer(Mockito.mock(NodePointer.class));
        assertEquals("x", nsResolver.getPrefix("http://example.com/x"));
        assertEquals("x", parentNsResolver.getPrefix("http://example.com/x"));
        assertEquals("x", parentNsResolver.getPrefix("http://example.com/x"));
        assertEquals(null, nsResolver.getPrefix("http://example.org/y"));
        assertEquals(null, parentNsResolver.getPrefix("http://example.org/y"));
    }

    @Test
    public void testSeal() {
        NamespaceResolver nsResolver = new NamespaceResolver();
        nsResolver.seal();
        assertTrue(nsResolver.isSealed());
        nsResolver.registerNamespace("x", "http://example.com/x");
        assertTrue(nsResolver.isSealed());
    }

    @Test
    public void testClone() {
        NamespaceResolver originalNsResolver = new NamespaceResolver();
        originalNsResolver.registerNamespace("x", "http://example.com/x");
        NamespaceResolver clonedNsResolver = (NamespaceResolver) originalNsResolver.clone();
        assertEquals("http://example.com/x", clonedNsResolver.getNamespaceURI("x"));
        assertEquals("x", clonedNsResolver.getPrefix("http://example.com/x"));
        assertFalse(clonedNsResolver.isSealed());
    }
}