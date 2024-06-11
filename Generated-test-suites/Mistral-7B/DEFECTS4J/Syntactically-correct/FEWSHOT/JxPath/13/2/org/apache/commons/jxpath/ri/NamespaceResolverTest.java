package org.apache.commons.cli;

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
import org.apache.commons.jxpath.ri.model.NodeIteratorMock;
import org.apache.commons.jxpath.ri.model.NodePointerMock;
import org.junit.Test;

public class NamespaceResolverTest {

    @Test
    public void testRegisterNamespace() {
        NamespaceResolver resolver = new NamespaceResolver();
        String prefix = "x";
        String namespaceURI = "http://example.com/x";

        resolver.registerNamespace(prefix, namespaceURI);

        assertEquals(namespaceURI, resolver.getNamespaceURI(prefix));
        assertEquals(prefix, resolver.getPrefix(namespaceURI));
    }

    @Test
    public void testGetNamespaceURI() {
        NamespaceResolver resolver = new NamespaceResolver();

        String prefix = "x";
        String namespaceURI = "http://example.com/x";
        resolver.registerNamespace(prefix, namespaceURI);

        assertEquals(namespaceURI, resolver.getNamespaceURI(prefix));
        assertNull(resolver.getNamespaceURI("y"));

        NodePointerMock pointer = mock(NodePointerMock.class);
        when(pointer.getNamespaceURI(prefix)).thenReturn("http://example.com/x");
        resolver.setNamespaceContextPointer(pointer);

        assertEquals(namespaceURI, resolver.getNamespaceURI(prefix));
    }

    @Test
    public void testGetPrefix() {
        NamespaceResolver resolver = new NamespaceResolver();

        String prefix = "x";
        String namespaceURI = "http://example.com/x";
        resolver.registerNamespace(prefix, namespaceURI);

        assertEquals(prefix, resolver.getPrefix(namespaceURI));
        assertNull(resolver.getPrefix("http://example.com/y"));

        HashMap namespaceMap = new HashMap();
        namespaceMap.put("x", "http://example.com/x");
        NamespaceResolver parent = mock(NamespaceResolver.class);
        when(parent.getNamespaceMap()).thenReturn(namespaceMap);

        NamespaceResolver resolverUnderTest = new NamespaceResolver(parent);

        assertEquals(prefix, resolverUnderTest.getPrefix(namespaceURI));
    }

    @Test
    public void testIsSealed() {
        NamespaceResolver resolver = new NamespaceResolver();

        assertFalse(resolver.isSealed());

        resolver.seal();

        assertTrue(resolver.isSealed());
    }

    @Test(expected = IllegalStateException.class)
    public void testRegisterNamespaceOnSealedResolver() {
        NamespaceResolver resolver = new NamespaceResolver();
        resolver.seal();

        String prefix = "x";
        String namespaceURI = "http://example.com/x";

        resolver.registerNamespace(prefix, namespaceURI);
    }

    @Test
    public void testEquals() {
        NamespaceResolver resolver1 = new NamespaceResolver();
        NamespaceResolver resolver2 = new NamespaceResolver();

        assertEquals(resolver1, resolver2);

        HashMap namespaceMap1 = new HashMap();
        namespaceMap1.put("x", "http://example.com/x");
        NamespaceResolver resolverUnderTest1 = new NamespaceResolver();
        resolverUnderTest1.registerNamespace("x", "http://example.com/x");
        resolverUnderTest1.setNamespaceMap(namespaceMap1);

        HashMap namespaceMap2 = new HashMap();
        namespaceMap2.put("x", "http://example.com/x");
        NamespaceResolver resolverUnderTest2 = new NamespaceResolver();
        resolverUnderTest2.setNamespaceMap(namespaceMap2);

        assertEquals(resolverUnderTest1, resolverUnderTest2);

        NamespaceResolver resolver3 = new NamespaceResolver();
        HashMap namespaceMap3 = new HashMap();
        namespaceMap3.put("x", "http://example.com/x");
        namespaceMap3.put("y", "http://example.com/y");
        resolver3.setNamespaceMap(namespaceMap3);

        assertNotEquals(resolverUnderTest1, resolver3);
    }

    @Test
    public void testClone() {
        NamespaceResolver resolver = new NamespaceResolver();
        HashMap namespaceMap = new HashMap();
        namespaceMap.put("x", "http://example.com/x");
        NamespaceResolver parent = mock(NamespaceResolver.class);
        when(parent.getNamespaceMap()).thenReturn(namespaceMap);
        NamespaceResolver resolverUnderTest = new NamespaceResolver(parent);
        resolverUnderTest.registerNamespace("x", "http://example.com/x");

        NamespaceResolver cloned = (NamespaceResolver) resolverUnderTest.clone();

        assertEquals(resolverUnderTest.getNamespaceMap(), cloned.getNamespaceMap());
        assertNotEquals(resolverUnderTest, cloned);
    }

    @Test
    public void testRegisterNamespaceOnClonedResolver() {
        NamespaceResolver resolver = new NamespaceResolver();
        HashMap namespaceMap = new HashMap();
        namespaceMap.put("x", "http://example.com/x");
        NamespaceResolver parent = mock(NamespaceResolver.class);
        when(parent.getNamespaceMap()).thenReturn(namespaceMap);
        NamespaceResolver resolverUnderTest = new NamespaceResolver(parent);
        resolverUnderTest.registerNamespace("x", "http://example.com/x");
        NamespaceResolver cloned = (NamespaceResolver) resolverUnderTest.clone();

        String prefix = "y";
        String namespaceURI = "http://example.com/y";

        cloned.registerNamespace(prefix, namespaceURI);

        assertFalse(resolverUnderTest.getNamespaceMap().containsKey(prefix));
        assertTrue(cloned.getNamespaceMap().containsKey(prefix));
    }

    @Test
    public void testSetNamespaceContextPointer() {
        NamespaceResolver resolver = new NamespaceResolver();

        NodePointerMock pointer = mock(NodePointerMock.class);
        when(pointer.getNamespaceURI("x")).thenReturn("http://example.com/x");

        resolver.setNamespaceContextPointer(pointer);

        assertEquals("http://example.com/x", resolver.getNamespaceURI("x"));
    }

    @Test
    public void testGetNamespaceContextPointer() {
        NamespaceResolver resolver1 = new NamespaceResolver();
        NamespaceResolver resolver2 = new NamespaceResolver();

        NodePointerMock pointer1 = mock(NodePointerMock.class);
        NodePointerMock pointer2 = mock(NodePointerMock.class);

        when(pointer1.getNodeName()).thenReturn("pointer1");
        when(pointer2.getNodeName()).thenReturn("pointer2");

        resolver1.setNamespaceContextPointer(pointer1);
        resolver2.setNamespaceContextPointer(pointer2);

        assertNotEquals(pointer1, pointer2);
        assertNotEquals(resolver1.getNamespaceContextPointer(), resolver2.getNamespaceContextPointer());

        NodePointerMock pointer3 = mock(NodePointerMock.class);
        when(pointer3.getNodeName()).thenReturn("pointer1");

        resolver1.setNamespaceContextPointer(pointer3);

        assertEquals(pointer3, resolver1.getNamespaceContextPointer());
    }

    @Test
    public void testNamespaceIterator() {
        NamespaceResolver resolver = new NamespaceResolver();

        HashMap namespaceMap = new HashMap();
        namespaceMap.put("x", "http://example.com/x");
        namespaceMap.put("y", "http://example.com/y");
        resolver.setNamespaceMap(namespaceMap);

        NodeIteratorMock namespaceIterator = mock(NodeIteratorMock.class);
        when(namespaceIterator.getNodePointer(0)).thenReturn(new NodePointerMock("prefixX", "http://example.com/x"));
        when(namespaceIterator.getNodePointer(1)).thenReturn(new NodePointerMock("prefixY", "http://example.com/y"));

        resolver.setNamespaceContextPointer(new NodePointerMock("context"));
        resolver.setNamespaceIterator(namespaceIterator);

        Iterator<NodePointer> namespaceIteratorIt = resolver.namespaceIterator();

        assertEquals(namespaceIterator.getNodePointer(0), namespaceIteratorIt.next());
        assertEquals(namespaceIterator.getNodePointer(1), namespaceIteratorIt.next());
        assertNull(namespaceIteratorIt.next());
    }
}