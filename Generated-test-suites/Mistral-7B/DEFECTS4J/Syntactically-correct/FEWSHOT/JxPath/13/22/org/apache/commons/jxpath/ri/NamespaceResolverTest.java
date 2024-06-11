package org.apache.commons.jxpath.ri;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertSame;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

import java.util.HashMap;
import java.util.Map;

import org.apache.commons.jxpath.Pointer;
import org.apache.commons.jxpath.ri.model.NodeIterator;
import org.apache.commons.jxpath.ri.model.NodePointer;
import org.junit.Before;
import org.junit.Test;

public class NamespaceResolverTest {

    private NamespaceResolver namespaceResolver;
    private NodePointer nodePointer;
    private Pointer pointer;

    @Before
    public void setUp() {
        namespaceResolver = new NamespaceResolver();
        nodePointer = mock(NodePointer.class);
        pointer = mock(Pointer.class);
        when(nodePointer.getNamespaceURI("prefix")).thenReturn("namespaceURI");
        when(nodePointer.getNamespaceURI("prefix2")).thenReturn("namespaceURI2");
        when(pointer.getNamespaceContextPointer()).thenReturn(nodePointer);
        namespaceResolver.setNamespaceContextPointer(pointer);
    }

    @Test
    public void testNewNamespaceResolver() {
        assertNull(namespaceResolver.getNamespaceURI("prefix"));
        assertNull(namespaceResolver.getPrefix("namespaceURI"));
    }

    @Test
    public void testRegisterNamespace() {
        namespaceResolver.registerNamespace("prefix", "namespaceURI");
        assertEquals("namespaceURI", namespaceResolver.getNamespaceURI("prefix"));
        assertEquals("prefix", namespaceResolver.getPrefix("namespaceURI"));

        namespaceResolver.registerNamespace("prefix2", "namespaceURI2");
        assertEquals("namespaceURI2", namespaceResolver.getNamespaceURI("prefix2"));
        assertEquals("prefix2", namespaceResolver.getPrefix("namespaceURI2"));
    }

    @Test(expected = IllegalStateException.class)
    public void testRegisterNamespaceOnSealedNamespaceResolver() {
        namespaceResolver.seal();
        namespaceResolver.registerNamespace("prefix", "namespaceURI");
    }

    @Test
    public void testGetNamespaceURI() {
        assertEquals("namespaceURI", namespaceResolver.getNamespaceURI("prefix"));
        assertEquals("namespaceURI2", namespaceResolver.getNamespaceURI("prefix2"));
        assertNull(namespaceResolver.getNamespaceURI("prefix3"));

        NamespaceResolver parent = new NamespaceResolver();
        parent.registerNamespace("prefix3", "namespaceURI3");
        namespaceResolver.setParent(parent);
        assertEquals("namespaceURI3", namespaceResolver.getNamespaceURI("prefix3"));
    }

    @Test
    public void testGetPrefix() {
        assertEquals("prefix", namespaceResolver.getPrefix("namespaceURI"));
        assertEquals("prefix2", namespaceResolver.getPrefix("namespaceURI2"));
        assertNull(namespaceResolver.getPrefix("namespaceURI3"));

        NamespaceResolver parent = new NamespaceResolver();
        parent.registerNamespace("prefix3", "namespaceURI3");
        namespaceResolver.setParent(parent);
        assertEquals("prefix3", namespaceResolver.getPrefix("namespaceURI3"));
    }

    @Test
    public void testSetNamespaceContextPointer() {
        NamespaceResolver parent = new NamespaceResolver();
        Pointer parentPointer = mock(Pointer.class);
        NodePointer parentNodePointer = mock(NodePointer.class);
        when(parentPointer.getNamespaceContextPointer()).thenReturn(parentNodePointer);
        parent.setNamespaceContextPointer(parentPointer);
        parent.registerNamespace("prefix4", "namespaceURI4");

        NamespaceResolver child = new NamespaceResolver(parent);
        Pointer childPointer = mock(Pointer.class);
        NodePointer childNodePointer = mock(NodePointer.class);
        when(childPointer.getNamespaceContextPointer()).thenReturn(childNodePointer);
        child.setNamespaceContextPointer(childPointer);

        assertEquals("namespaceURI", namespaceResolver.getNamespaceURI("prefix"));
        assertEquals("namespaceURI2", namespaceResolver.getNamespaceURI("prefix2"));
        assertEquals("namespaceURI4", child.getNamespaceURI("prefix"));
        assertNull(child.getNamespaceURI("prefix2"));

        assertEquals("prefix", namespaceResolver.getPrefix("namespaceURI"));
        assertEquals("prefix2", namespaceResolver.getPrefix("namespaceURI2"));
        assertEquals("prefix", child.getPrefix("namespaceURI"));
        assertNull(child.getPrefix("namespaceURI2"));
    }

    @Test
    public void testSeal() {
        namespaceResolver.registerNamespace("prefix", "namespaceURI");
        namespaceResolver.seal();
        assertSame(namespaceResolver, namespaceResolver.getParent());
        assertNull(namespaceResolver.getNamespaceURI("prefix"));
        assertNull(namespaceResolver.getPrefix("namespaceURI"));
    }
}