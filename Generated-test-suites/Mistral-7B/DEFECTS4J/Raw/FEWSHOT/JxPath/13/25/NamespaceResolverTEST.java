package org.apache.commons.jxpath.ri;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertSame;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

import java.util.HashMap;
import java.util.Map;

import org.apache.commons.jxpath.NodeIterator;
import org.apache.commons.jxpath.Pointer;
import org.apache.commons.jxpath.ri.model.NodeIteratorImpl;
import org.apache.commons.jxpath.ri.model.NodePointerImpl;

public class NamespaceResolverTest {

    @Test
    public void testNewNamespaceResolver() {
        NamespaceResolver resolver = new NamespaceResolver();
        assertNull(resolver.getParent());
        assertEquals(0, resolver.getNamespaces().size());
    }

    @Test
    public void testNewNamespaceResolverWithParent() {
        NamespaceResolver parentResolver = mock(NamespaceResolver.class);
        NamespaceResolver resolver = new NamespaceResolver(parentResolver);
        assertSame(parentResolver, resolver.getParent());
        assertEquals(0, resolver.getNamespaces().size());
    }

    @Test
    public void testRegisterNamespace() {
        NamespaceResolver resolver = new NamespaceResolver();
        resolver.registerNamespace("prefix", "http://example.com");
        assertEquals("http://example.com", resolver.getNamespaceURI("prefix"));
        assertEquals("prefix", resolver.getPrefix("http://example.com"));
    }

    @Test(expected = IllegalStateException.class)
    public void testRegisterNamespaceOnSealedResolver() {
        NamespaceResolver resolver = new NamespaceResolver();
        resolver.seal();
        resolver.registerNamespace("prefix", "http://example.com");
    }

    @Test
    public void testSetNamespaceContextPointer() {
        NamespaceResolver resolver = new NamespaceResolver();
        Pointer pointer = mock(Pointer.class);
        resolver.setNamespaceContextPointer(pointer);
        assertSame(pointer, resolver.getNamespaceContextPointer());
    }

    @Test
    public void testGetNamespaceContextPointer() {
        NamespaceResolver resolver = new NamespaceResolver();
        Pointer pointer = mock(Pointer.class);
        resolver.setNamespaceContextPointer(pointer);
        assertSame(pointer, resolver.getNamespaceContextPointer());
        assertNull(resolver.getNamespaceContextPointer());
    }

    @Test
    public void testGetNamespaceURI() {
        NamespaceResolver resolver = new NamespaceResolver();
        String prefix = "prefix";
        String uri = "http://example.com";
        resolver.registerNamespace(prefix, uri);
        assertEquals(uri, resolver.getNamespaceURI(prefix));
        assertEquals(null, resolver.getNamespaceURI("prefix2"));
    }

    @Test
    public void testGetPrefix() {
        NamespaceResolver resolver = new NamespaceResolver();
        String prefix = "prefix";
        String uri = "http://example.com";
        resolver.registerNamespace(prefix, uri);
        assertEquals(prefix, resolver.getPrefix(uri));
        assertEquals(null, resolver.getPrefix("http://example2.com"));
    }

    @Test
    public void testGetPrefixWithParent() {
        String parentPrefix = "parent";
        String parentUri = "http://parent.com";
        String prefix = "prefix";
        String uri = "http://example.com";

        NamespaceResolver parentResolver = new NamespaceResolver();
        parentResolver.registerNamespace(parentPrefix, parentUri);

        NamespaceResolver resolver = new NamespaceResolver(parentResolver);
        resolver.registerNamespace(prefix, uri);

        assertEquals(prefix, resolver.getPrefix(uri));
        assertEquals(parentPrefix, resolver.getPrefix(parentUri));
    }

    @Test
    public void testSeal() {
        NamespaceResolver resolver = new NamespaceResolver();
        resolver.registerNamespace("prefix", "http://example.com");
        resolver.seal();
        try {
            resolver.registerNamespace("prefix2", "http://example2.com");
        } catch (IllegalStateException e) {
            // Expected
        }
    }

    @Test
    public void testClone() {
        NamespaceResolver resolver = new NamespaceResolver();
        resolver.registerNamespace("prefix", "http://example.com");
        NamespaceResolver cloned = (NamespaceResolver) resolver.clone();
        assertEquals(resolver.getNamespaceURI("prefix"), cloned.getNamespaceURI("prefix"));
        assertEquals(resolver.getParent(), cloned.getParent());
    }

    @Test
    public void testNamespaceIterator() {
        NamespaceResolver resolver = new NamespaceResolver();
        Pointer pointer = mock(Pointer.class);
        NodeIterator nodeIterator = mock(NodeIterator.class);
        when(pointer.namespaceIterator()).thenReturn(nodeIterator);
        resolver.setNamespaceContextPointer(pointer);

        Map<String, String> namespaces = new HashMap<>();
        namespaces.put("prefix1", "http://example1.com");
        namespaces.put("prefix2", "http://example2.com");

        NodeIteratorImpl nodeIteratorImpl = new NodeIteratorImpl(namespaces.entrySet().iterator());
        when(nodeIterator.getNextNodePointer()).thenReturn(new NodePointerImpl("node", "uri", null));
        when(nodeIterator.hasNext()).thenReturn(true);
        when(nodeIterator.getNodeIterator()).thenReturn(nodeIteratorImpl);

        Iterator<Map.Entry<String, String>> iterator = resolver.namespaceIterator();
        assertEquals("prefix1", iterator.next().getKey());
        assertEquals("http://example1.com", iterator.next().getValue());
        assertEquals("prefix2", iterator.next().getKey());
        assertEquals("http://example2.com", iterator.next().getValue());
        assertFalse(iterator.hasNext());
    }

}