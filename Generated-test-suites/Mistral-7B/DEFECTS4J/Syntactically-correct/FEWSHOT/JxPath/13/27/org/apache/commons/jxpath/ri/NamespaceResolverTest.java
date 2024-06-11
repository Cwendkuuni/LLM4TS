package org.apache.commons.cli;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertSame;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

import java.util.HashMap;
import java.util.Map;

import org.apache.commons.cli.Namespaces.Namespace;
import org.apache.commons.jxpath.Pointer;
import org.apache.commons.jxpath.ri.model.NodeIterator;
import org.apache.commons.jxpath.ri.model.NodePointer;
import org.junit.Before;
import org.junit.Test;

public class NamespaceResolverTest {

    private NamespaceResolver resolver;
    private NamespaceResolver parentResolver;
    private NodeIterator mockNodeIterator;
    private NodePointer mockNodePointer;

    @Before
    public void setUp() {
        parentResolver = mock(NamespaceResolver.class);
        mockNodeIterator = mock(NodeIterator.class);
        mockNodePointer = mock(NodePointer.class);
        resolver = new NamespaceResolver(parentResolver);

        when(parentResolver.getNamespaceContextPointer()).thenReturn(mockNodeIterator);
        when(mockNodeIterator.getNodePointer(0)).thenReturn(mockNodePointer);
        when(mockNodePointer.getNamespaceURI("xmlns")).thenReturn("http://www.w3.org/2000/xmlns/");
        when(mockNodePointer.getNamespaceURI("xsi")).thenReturn("http://www.w3.org/2001/XMLSchema-instance");
        when(mockNodePointer.getNamespaceURI("foo")).thenReturn("http://example.com/foo");
    }

    @Test
    public void testGetNamespaceURI() {
        assertEquals("http://www.w3.org/2000/xmlns/", resolver.getNamespaceURI("xmlns"));
        assertEquals("http://www.w3.org/2001/XMLSchema-instance", resolver.getNamespaceURI("xsi"));
        assertEquals("http://example.com/foo", resolver.getNamespaceURI("foo"));
        assertNull(resolver.getNamespaceURI(""));
        assertNull(resolver.getNamespaceURI("bar"));
    }

    @Test
    public void testGetPrefix() {
        assertEquals("xmlns", resolver.getPrefix("http://www.w3.org/2000/xmlns/"));
        assertEquals("xsi", resolver.getPrefix("http://www.w3.org/2001/XMLSchema-instance"));
        assertNull(resolver.getPrefix(""));
        assertNull(resolver.getPrefix("bar"));
    }

    @Test
    public void testRegisterNamespace() {
        resolver.registerNamespace("xml", "http://www.w3.org/XML/1998/namespace");
        assertEquals("xml", resolver.getPrefix("http://www.w3.org/XML/1998/namespace"));
        assertEquals("http://www.w3.org/XML/1998/namespace", resolver.getNamespaceURI("xml"));

        resolver.registerNamespace("xsd", "http://www.w3.org/2001/XMLSchema");
        assertEquals("xsd", resolver.getPrefix("http://www.w3.org/2001/XMLSchema"));
        assertEquals("http://www.w3.org/2001/XMLSchema", resolver.getNamespaceURI("xsd"));
    }

    @Test(expected = IllegalStateException.class)
    public void testRegisterNamespaceAfterSeal() {
        resolver.seal();
        resolver.registerNamespace("xml", "http://www.w3.org/XML/1998/namespace");
    }

    @Test
    public void testSeal() {
        resolver.registerNamespace("xml", "http://www.w3.org/XML/1998/namespace");
        resolver.registerNamespace("xsd", "http://www.w3.org/2001/XMLSchema");
        resolver.seal();

        resolver.registerNamespace("xml", "http://www.w3.org/XML/1998/namespace"); // should not throw an exception
        assertSame(resolver, resolver.clone());

        assertEquals(2, resolver.namespaceMap.size());
        assertNull(resolver.reverseMap);
        assertSame(parentResolver, resolver.parent);
    }
}