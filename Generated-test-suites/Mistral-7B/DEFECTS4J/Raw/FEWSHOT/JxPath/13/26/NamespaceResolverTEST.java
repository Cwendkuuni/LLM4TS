package org.apache.commons.jxpath.ri;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertSame;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;

import org.apache.commons.jxpath.NodeIterator;
import org.apache.commons.jxpath.NodePointer;
import org.apache.commons.jxpath.Pointer;
import org.apache.commons.jxpath.ri.model.NodeIteratorMock;

/**
 * Tests for {@link NamespaceResolver}.
 */
public class NamespaceResolverTest {

    @Test
    public void testNewInstance() {
        NamespaceResolver resolver = new NamespaceResolver();
        assertNull(resolver.getParent());
        assertEquals(0, resolver.size());
        assertNull(resolver.getNamespaceURI("prefix"));
        assertNull(resolver.getPrefix("uri"));
    }

    @Test
    public void testNewInstanceWithParent() {
        NamespaceResolver parentResolver = mock(NamespaceResolver.class);
        when(parentResolver.getNamespaceURI("prefix")).thenReturn("prefixURI");

        NamespaceResolver resolver = new NamespaceResolver(parentResolver);
        assertEquals(parentResolver, resolver.getParent());
        assertEquals(1, resolver.size());
        assertEquals("prefixURI", resolver.getNamespaceURI("prefix"));
        assertNull(resolver.getPrefix("uri"));
    }

    @Test
    public void testRegisterNamespace() {
        NamespaceResolver resolver = new NamespaceResolver();
        resolver.registerNamespace("prefix", "uri");
        assertEquals(1, resolver.size());
        assertEquals("uri", resolver.getNamespaceURI("prefix"));
        assertNull(resolver.getPrefix("otherPrefix"));
    }

    @Test(expected = IllegalStateException.class)
    public void testRegisterNamespaceOnSealedResolver() {
        NamespaceResolver resolver = new NamespaceResolver();
        resolver.seal();
        resolver.registerNamespace("prefix", "uri");
    }

    @Test
    public void testGetNamespaceURI() {
        NamespaceResolver resolver = new NamespaceResolver();
        resolver.registerNamespace("prefix", "uri");
        assertEquals("uri", resolver.getNamespaceURI("prefix"));
        assertEquals(null, resolver.getNamespaceURI("otherPrefix"));

        NamespaceResolver parentResolver = mock(NamespaceResolver.class);
        when(parentResolver.getNamespaceURI("prefix")).thenReturn("prefixURI");
        NamespaceResolver resolverWithParent = new NamespaceResolver(parentResolver);
        resolverWithParent.registerNamespace("otherPrefix", "otherURI");
        assertEquals("prefixURI", resolverWithParent.getNamespaceURI("prefix"));
        assertEquals("otherURI", resolverWithParent.getNamespaceURI("otherPrefix"));
    }

    @Test
    public void testGetPrefix() {
        NamespaceResolver resolver = new NamespaceResolver();
        resolver.registerNamespace("prefix", "uri");
        assertEquals("prefix", resolver.getPrefix("uri"));
        assertEquals(null, resolver.getPrefix("otherUri"));

        NamespaceResolver parentResolver = mock(NamespaceResolver.class);
        when(parentResolver.getPrefix("prefix")).thenReturn("parentPrefix");
        NamespaceResolver resolverWithParent = new NamespaceResolver(parentResolver);
        resolverWithParent.registerNamespace("otherPrefix", "otherURI");
        assertEquals("parentPrefix", resolverWithParent.getPrefix("prefix"));
        assertEquals("otherPrefix", resolverWithParent.getPrefix("otherURI"));
    }

    @Test
    public void testSetNamespaceContextPointer() {
        NamespaceResolver resolver = new NamespaceResolver();
        NodePointer pointer = mock(NodePointer.class);
        resolver.setNamespaceContextPointer(pointer);
        assertEquals(pointer, resolver.getNamespaceContextPointer());
    }

    @Test
    public void testGetNamespaceContextPointer() {
        NamespaceResolver parentResolver = mock(NamespaceResolver.class);
        NodePointer parentPointer = mock(NodePointer.class);
        when(parentResolver.getNamespaceContextPointer()).thenReturn(parentPointer);
        NamespaceResolver resolver = new NamespaceResolver(parentResolver);
        NodePointer resolverPointer = mock(NodePointer.class);
        when(resolver.getNamespaceContextPointer()).thenReturn(resolverPointer);
        assertSame(parentPointer, resolver.getNamespaceContextPointer());
    }

    @Test
    public void testSeal() {
        NamespaceResolver resolver = new NamespaceResolver();
        resolver.seal();
        assertTrue(resolver.isSealed());
    }

    @Test(expected = IllegalStateException.class)
    public void testSealAlreadySealed() {
        NamespaceResolver resolver = new NamespaceResolver();
        resolver.seal();
        resolver.seal();
    }

    @Test
    public void testClone() throws CloneNotSupportedException {
        NamespaceResolver resolver = new NamespaceResolver();
        resolver.registerNamespace("prefix", "uri");
        NamespaceResolver clonedResolver = (NamespaceResolver) resolver.clone();
        assertEquals(1, clonedResolver.size());
        assertEquals("uri", clonedResolver.getNamespaceURI("prefix"));

        Map namespaceMap = (Map) resolver.namespaceMap.clone();
        Map clonedNamespaceMap = (Map) clonedResolver.namespaceMap.clone();
        assertNotSame(namespaceMap, clonedNamespaceMap);
    }

    @Test
    public void testNamespaceIterator() {
        NamespaceResolver resolver = new NamespaceResolver();
        resolver.registerNamespace("prefix1", "uri1");
        resolver.registerNamespace("prefix2", "uri2");

        NodeIterator nodeIteratorMock = new NodeIteratorMock();
        NodePointer nodePointer1 = mock(NodePointer.class);
        NodePointer nodePointer2 = mock(NodePointer.class);
        when(nodeIteratorMock.getNodePointer()).thenReturn(nodePointer1).thenReturn(nodePointer2);
        when(nodePointer1.getNamespaceURI()).thenReturn("uri1");
        when(nodePointer1.getName().getName()).thenReturn("prefix1");
        when(nodePointer2.getNamespaceURI()).thenReturn("uri2");
        when(nodePointer2.getName().getName()).thenReturn("prefix2");

        resolver.setNamespaceContextPointer(nodeIteratorMock);

        Iterator iterator = resolver.namespaceIterator();
        assertEquals(2, iterator.size());
        Map.Entry entry1 = (Map.Entry) iterator.next();
        assertEquals("prefix1", entry1.getKey());
        assertEquals("uri1", entry1.getValue());

        Map.Entry entry2 = (Map.Entry) iterator.next();
        assertEquals("prefix2", entry2.getKey());
        assertEquals("uri2", entry2.getValue());
    }
}