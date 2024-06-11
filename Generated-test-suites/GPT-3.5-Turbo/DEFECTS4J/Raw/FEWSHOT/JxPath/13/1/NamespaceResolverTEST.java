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
        pointer = new NodePointer();
        resolver = new NamespaceResolver();
        resolver.setNamespaceContextPointer(pointer);
    }

    @Test
    public void testRegisterNamespace() {
        resolver.registerNamespace("prefix1", "uri1");
        assertEquals("uri1", resolver.getNamespaceURI("prefix1"));
    }

    @Test(expected = IllegalStateException.class)
    public void testRegisterNamespaceSealed() {
        resolver.seal();
        resolver.registerNamespace("prefix", "uri");
    }

    @Test
    public void testGetNamespaceURI() {
        assertEquals("uri", resolver.getNamespaceURI("prefix"));
        assertNull(resolver.getNamespaceURI("nonexistentPrefix"));
    }

    @Test
    public void testGetPrefix() {
        assertEquals("prefix", resolver.getPrefix("uri"));
        assertNull(resolver.getPrefix("nonexistentURI"));
    }

    @Test
    public void testSeal() {
        resolver.seal();
        assertEquals(true, resolver.isSealed());
    }

    @Test
    public void testClone() {
        NamespaceResolver clone = (NamespaceResolver) resolver.clone();
        assertEquals(false, clone.isSealed());
    }
}