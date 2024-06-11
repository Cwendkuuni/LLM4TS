package org.apache.commons.jxpath.ri;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertSame;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

import org.apache.commons.jxpath.NodeIterator;
import org.apache.commons.jxpath.Pointer;
import org.apache.commons.jxpath.ri.model.NodeIteratorImpl;
import org.apache.commons.jxpath.ri.model.NodePointer;
import org.apache.commons.jxpath.ri.model.NodePointerImpl;

import java.util.HashMap;
import java.util.Map;

public class NamespaceResolverTest {

    @Test
    public void testNewInstance() {
        NamespaceResolver resolver = new NamespaceResolver();
        assertNull(resolver.getParent());
        assertEquals(0, resolver.size());
    }

    @Test
    public void testInstanceWithParent() {
        NamespaceResolver parentResolver = mock(NamespaceResolver.class);
        NamespaceResolver resolver = new NamespaceResolver(parentResolver);
        assertSame(parentResolver, resolver.getParent());
    }

    @Test
    public void testRegisterNamespace() {
        NamespaceResolver resolver = new NamespaceResolver();
        String prefix = "prefix";
        String namespaceURI = "http://test.com";

        resolver.registerNamespace(prefix, namespaceURI);
        assertEquals(prefix, resolver.getPrefix(namespaceURI));
        assertEquals(1, resolver.size());
    }

    @Test(expected = IllegalStateException.class)
    public void testRegisterNamespaceWhenSealed() {
        NamespaceResolver resolver = new NamespaceResolver();
        resolver.registerNamespace("prefix", "http://test.com");
        resolver.seal();
        resolver.registerNamespace("prefix2", "http://test.com2");
    }

    @Test
    public void testGetNamespaceURI() {
        NamespaceResolver resolver = new NamespaceResolver();
        String prefix = "prefix";
        String namespaceURI = "http://test.com";

        resolver.registerNamespace(prefix, namespaceURI);
        assertEquals(namespaceURI, resolver.getNamespaceURI(prefix));

        String anotherNamespaceURI = "http://test.com2";
        resolver.setNamespaceContextPointer(mock(Pointer.class));
        assertEquals(anotherNamespaceURI, resolver.getNamespaceURI(prefix));

        NamespaceResolver parentResolver = mock(NamespaceResolver.class);
        resolver.setParent(parentResolver);
        assertEquals(parentResolver.getNamespaceURI(prefix), resolver.getNamespaceURI(prefix));
    }

    @Test
    public void testGetPrefix() {
        NamespaceResolver resolver = new NamespaceResolver();
        String prefix = "prefix";
        String namespaceURI = "http://test.com";

        resolver.registerNamespace(prefix, namespaceURI);
        assertEquals(prefix, resolver.getPrefix(namespaceURI));

        String anotherPrefix = "anotherPrefix";
        resolver.setNamespaceContextPointer(mock(Pointer.class));
        assertEquals(anotherPrefix, resolver.getPrefix(namespaceURI));

        NamespaceResolver parentResolver = mock(NamespaceResolver.class);
        resolver.setParent(parentResolver);
        assertEquals(parentResolver.getPrefix(namespaceURI), resolver.getPrefix(namespaceURI));
    }

    @Test
    public void testSize() {
        NamespaceResolver resolver = new NamespaceResolver();
        String prefix = "prefix";
        String namespaceURI = "http://test.com";

        resolver.registerNamespace(prefix, namespaceURI);
        assertEquals(1, resolver.size());

        resolver.registerNamespace("prefix2", "http://test.com2");
        assertEquals(2, resolver.size());
    }

    @Test
    public void testSetNamespaceContextPointer() {
        NamespaceResolver resolver = new NamespaceResolver();
        Pointer pointer = mock(Pointer.class);

        resolver.setNamespaceContextPointer(pointer);
        assertEquals(pointer, resolver.getNamespaceContextPointer());
    }

    @Test
    public void testGetNamespaceContextPointer() {
        NamespaceResolver resolver = new NamespaceResolver();
        NamespaceResolver parentResolver = mock(NamespaceResolver.class);
        Pointer pointer = mock(Pointer.class);

        when(parentResolver.getNamespaceContextPointer()).thenReturn(pointer);
        resolver.setParent(parentResolver);

        assertEquals(pointer, resolver.getNamespaceContextPointer());
    }

    @Test
    public void testSeal() {
        NamespaceResolver resolver = new NamespaceResolver();
        boolean isSealedBeforeCall = resolver.isSealed();
        resolver.seal();
        assertTrue(resolver.isSealed());
        assertFalse(resolver.isSealedBeforeCall);
    }

    @Test(expected = IllegalStateException.class)
    public void testSealAlreadySealed() {
        NamespaceResolver resolver = new NamespaceResolver();
        resolver.seal();
        resolver.seal();
    }

    @Test
    public void testClone() {
        NamespaceResolver original = new NamespaceResolver();
        String prefix = "prefix";
        String namespaceURI = "http://test.com";
        original.registerNamespace(prefix, namespaceURI);

        NamespaceResolver cloned = (NamespaceResolver) original.clone();
        assertEquals(original.getPrefix(namespaceURI), cloned.getPrefix(namespaceURI));
    }

    @Test
    public void testNamespaceIterator() {
        NamespaceResolver resolver = new NamespaceResolver();

        String prefix1 = "prefix1";
        String namespaceURI1 = "http://test.com1";
        resolver.registerNamespace(prefix1, namespaceURI1);

        String prefix2 = "prefix2";
        String namespaceURI2 = "http://test.com2";
        resolver.registerNamespace(prefix2, namespaceURI2);

        NodePointer pointer = mock(NodePointer.class);
        when(pointer.getNamespaceURI()).thenReturn(namespaceURI1);
        when(pointer.getName()).thenReturn(new NodePointerImpl("nodeName"));

        NodeIterator iterator = mock(NodeIteratorImpl.class);
        when(iterator.getNextNodePointer()).thenReturn(pointer);
        when(iterator.hasNext()).thenReturn(true).thenReturn(false);

        when(resolver.getNamespaceContextPointer()).thenReturn(pointer);
        when(resolver.getParent().getNamespaceContextPointer()).thenReturn(pointer);

        Map<String, String> namespaceMap = resolver.namespaceMap;
        assertEquals(2, namespaceMap.size());

        NamespaceResolverTest namespaceResolverTest = new NamespaceResolverTest();
        namespaceResolverTest.runNamespaceIteratorTest(resolver, iterator);
    }

    private void runNamespaceIteratorTest(NamespaceResolver resolver, NodeIterator iterator) {
        Iterator<Map.Entry<String, String>> entryIterator = resolver.namespaceMap.entrySet().iterator();

        assertEquals(entryIterator.next().getKey(), iterator.next().getNodeName().getName());
        assertEquals(entryIterator.next().getValue(), iterator.next().getNodeName().getNamespaceURI());

        entryIterator.remove();
        assertEquals(entryIterator.next().getKey(), iterator.next().getNodeName().getName());
        assertEquals(entryIterator.next().getValue(), iterator.next().getNodeName().getNamespaceURI());

        assertNull(iterator.next().getNodeName());
        assertNull(iterator.next().getNodeName());
    }

}