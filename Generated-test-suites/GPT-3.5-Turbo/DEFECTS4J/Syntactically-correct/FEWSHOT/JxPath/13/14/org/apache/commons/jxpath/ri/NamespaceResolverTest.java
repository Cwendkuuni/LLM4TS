package org.apache.commons.jxpath.ri;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNull;
import org.junit.Before;
import org.junit.Test;

public class NamespaceResolverTest {
    private NamespaceResolver namespaceResolver;

    @Before
    public void setUp() {
        namespaceResolver = new NamespaceResolver();
    }

    @Test
    public void testRegisterNamespace() {
        namespaceResolver.registerNamespace("prefix1", "uri1");
        assertEquals("uri1", namespaceResolver.getNamespaceURI("prefix1"));
        namespaceResolver.registerNamespace("prefix2", "uri2");
        assertEquals("uri2", namespaceResolver.getNamespaceURI("prefix2"));
    }

    @Test(expected = IllegalStateException.class)
    public void testRegisterNamespace_whenSealed() {
        namespaceResolver.seal();
        namespaceResolver.registerNamespace("prefix1", "uri1");
    }

    @Test
    public void testGetNamespaceURI() {
        namespaceResolver.registerNamespace("prefix1", "uri1");
        assertEquals("uri1", namespaceResolver.getNamespaceURI("prefix1"));
        assertEquals(null, namespaceResolver.getNamespaceURI("prefix2"));

        NamespaceResolver parentResolver = new NamespaceResolver(namespaceResolver);
        assertEquals("uri1", parentResolver.getNamespaceURI("prefix1"));
        assertEquals(null, parentResolver.getNamespaceURI("prefix2"));

        parentResolver.registerNamespace("prefix2", "uri2");
        assertEquals("uri1", parentResolver.getNamespaceURI("prefix1"));
        assertEquals("uri2", parentResolver.getNamespaceURI("prefix2"));

        namespaceResolver.registerNamespace("prefix3", "uri3");
        assertEquals("uri1", parentResolver.getNamespaceURI("prefix1"));
        assertEquals("uri2", parentResolver.getNamespaceURI("prefix2"));
        assertEquals("uri3", parentResolver.getNamespaceURI("prefix3"));
    }

    @Test
    public void testGetNamespaceURI_whenPointerNotNull() {
        namespaceResolver.setNamespaceContextPointer(null);
        assertNull(namespaceResolver.getNamespaceURI("prefix1"));

        NodePointer pointer = new NodePointer();
        pointer.setNamespaceURI("uri1");
        namespaceResolver.setNamespaceContextPointer(pointer);
        assertEquals("uri1", namespaceResolver.getNamespaceURI("prefix1"));
    }

    @Test
    public void testGetPrefix() {
        namespaceResolver.registerNamespace("prefix1", "uri1");
        assertEquals("prefix1", namespaceResolver.getPrefix("uri1"));
        assertEquals(null, namespaceResolver.getPrefix("uri2"));

        NamespaceResolver parentResolver = new NamespaceResolver(namespaceResolver);
        assertEquals("prefix1", parentResolver.getPrefix("uri1"));
        assertEquals(null, parentResolver.getPrefix("uri2"));

        parentResolver.registerNamespace("prefix2", "uri2");
        assertEquals("prefix1", parentResolver.getPrefix("uri1"));
        assertEquals("prefix2", parentResolver.getPrefix("uri2"));

        namespaceResolver.registerNamespace("prefix3", "uri3");
        assertEquals("prefix1", parentResolver.getPrefix("uri1"));
        assertEquals("prefix2", parentResolver.getPrefix("uri2"));
        assertEquals("prefix3", parentResolver.getPrefix("uri3"));
    }

    @Test
    public void testGetPrefix_whenPointerNotNull() {
        namespaceResolver.setNamespaceContextPointer(null);
        assertNull(namespaceResolver.getPrefix("uri1"));

        NodePointer pointer = new NodePointer();
        pointer.getName().setName("prefix1");
        namespaceResolver.setNamespaceContextPointer(pointer);
        assertEquals("prefix1", namespaceResolver.getPrefix("uri1"));
    }
}