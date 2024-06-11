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
import org.apache.commons.jxpath.ri.model.NodeIteratorStub;

import org.junit.Test;

public class NamespaceResolverTest {

    @Test
    public void testNewNamespaceResolver() {
        NamespaceResolver resolver = new NamespaceResolver();
        assertNull(resolver.parent);
        assertEquals(new HashMap(), resolver.namespaceMap);
        assertNull(resolver.reverseMap);
        assertNull(resolver.pointer);
        assertFalse(resolver.isSealed());
    }

    @Test
    public void testNewNamespaceResolverWithParent() {
        NamespaceResolver parentResolver = mock(NamespaceResolver.class);
        when(parentResolver.getNamespaceURI("xsi")).thenReturn("http://www.w3.org/2001/XMLSchema-instance");
        NamespaceResolver resolver = new NamespaceResolver(parentResolver);
        assertEquals(parentResolver, resolver.parent);
        assertEquals(new HashMap(), resolver.namespaceMap);
        assertNull(resolver.reverseMap);
        assertNull(resolver.pointer);
        assertFalse(resolver.isSealed());
    }

    @Test
    public void testRegisterNamespace() {
        NamespaceResolver resolver = new NamespaceResolver();
        resolver.registerNamespace("xsi", "http://www.w3.org/2001/XMLSchema-instance");
        assertEquals("http://www.w3.org/2001/XMLSchema-instance", resolver.getNamespaceURI("xsi"));
        assertEquals("xsi", resolver.getPrefix("http://www.w3.org/2001/XMLSchema-instance"));

        Map namespaceMap = resolver.namespaceMap;
        assertEquals("xsi", namespaceMap.keySet().iterator().next());
        assertEquals("http://www.w3.org/2001/XMLSchema-instance", namespaceMap.get("xsi"));
    }

    @Test(expected = IllegalStateException.class)
    public void testRegisterNamespaceOnSealedResolver() {
        NamespaceResolver resolver = new NamespaceResolver();
        resolver.seal();
        resolver.registerNamespace("xsi", "http://www.w3.org/2001/XMLSchema-instance");
    }

    @Test
    public void testGetNamespaceURI() {
        NamespaceResolver resolver = new NamespaceResolver();
        resolver.registerNamespace("xsi", "http://www.w3.org/2001/XMLSchema-instance");
        assertEquals("http://www.w3.org/2001/XMLSchema-instance", resolver.getNamespaceURI("xsi"));
        assertEquals(null, resolver.getNamespaceURI("foo"));

        NamespaceResolver parentResolver = mock(NamespaceResolver.class);
        when(parentResolver.getNamespaceURI("xsi")).thenReturn("http://example.com/xsi");
        NamespaceResolver resolverWithParent = new NamespaceResolver(parentResolver);
        assertEquals("http://example.com/xsi", resolverWithParent.getNamespaceURI("xsi"));
    }

    @Test
    public void testGetPrefix() {
        NamespaceResolver resolver = new NamespaceResolver();
        resolver.registerNamespace("xsi", "http://www.w3.org/2001/XMLSchema-instance");
        assertEquals("xsi", resolver.getPrefix("http://www.w3.org/2001/XMLSchema-instance"));
        assertEquals(null, resolver.getPrefix("http://example.com/foo"));

        NamespaceResolver parentResolver = mock(NamespaceResolver.class);
        when(parentResolver.getPrefix("xsi")).thenReturn("xsi-parent");
        NamespaceResolver resolverWithParent = new NamespaceResolver(parentResolver);
        assertEquals("xsi-parent", resolverWithParent.getPrefix("http://www.w3.org/2001/XMLSchema-instance"));
    }

    @Test
    public void testGetPrefixWithRegisteredPrefix() {
        NamespaceResolver resolver = new NamespaceResolver();
        resolver.registerNamespace("xsi", "http://www.w3.org/2001/XMLSchema-instance");
        resolver.registerNamespace("xsd", "http://www.w3.org/2001/XMLSchema");
        assertEquals("xsi", resolver.getPrefix("http://www.w3.org/2001/XMLSchema-instance"));
        assertEquals("xsd", resolver.getPrefix("http://www.w3.org/2001/XMLSchema"));
    }

    @Test
    public void testSetNamespaceContextPointer() {
        NamespaceResolver resolver = new NamespaceResolver();
        NodePointer nodePointer = mock(NodePointer.class);
        resolver.setNamespaceContextPointe(nodePointer);
        assertEquals(nodePointer, resolver.getNamespaceContextPointer());
    }

    @Test
    public void testGetNamespaceContextPointer() {
        NamespaceResolver resolver = new NamespaceResolver();
        NodePointer nodePointer = mock(NodePointer.class);
        when(nodePointer.getNamespaceURI("xsi")).thenReturn("http://www.w3.org/2001/XMLSchema-instance");
        resolver.setNamespaceContextPointer(nodePointer);
        assertEquals("http://www.w3.org/2001/XMLSchema-instance", resolver.getNamespaceURI("xsi"));

        NamespaceResolver parentResolver = mock(NamespaceResolver.class);
        NodePointer nodePointerParent = mock(NodePointer.class);
        when(nodePointerParent.getNamespaceURI("xsi")).thenReturn("http://example.com/xsi");
        when(parentResolver.getNamespaceContextPointer()).thenReturn(nodePointerParent);
        resolver = new NamespaceResolver(parentResolver);
        assertEquals("http://example.com/xsi", resolver.getNamespaceURI("xsi"));
    }

    @Test
    public void testSeal() {
        NamespaceResolver resolver = new NamespaceResolver();
        assertFalse(resolver.isSealed());
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
    public void testClone() {
        NamespaceResolver parentResolver = mock(NamespaceResolver.class);
        when(parentResolver.getNamespaceURI("xsi")).thenReturn("http://www.w3.org/2001/XMLSchema-instance");
        NamespaceResolver resolver = new NamespaceResolver(parentResolver);
        resolver.registerNamespace("xsd", "http://www.w3.org/2001/XMLSchema");

        NamespaceResolver clonedResolver = (NamespaceResolver) resolver.clone();
        assertEquals(resolver, clonedResolver);
        assertEquals(new HashMap(), clonedResolver.namespaceMap);
        assertEquals("xsi", clonedResolver.getPrefix("http://www.w3.org/2001/XMLSchema-instance"));
        assertEquals("http://www.w3.org/2001/XMLSchema-instance", clonedResolver.getNamespaceURI("xsi"));
        assertEquals("xsd", clonedResolver.getPrefix("http://www.w3.org/2001/XMLSchema"));
        assertSame(parentResolver, clonedResolver.parent);
        assertFalse(clonedResolver.isSealed());
    }

    @Test(expected = CloneNotSupportedException.class)
    public void testCloneUnsupported() {
        throw new CloneNotSupportedException();
    }

    @Test
    public void testNamespaceIterator() {
        NamespaceResolver resolver = new NamespaceResolver();
        resolver.registerNamespace("xsi", "http://www.w3.org/2001/XMLSchema-instance");
        resolver.registerNamespace("xsd", "http://www.w3.org/2001/XMLSchema");

        Pointer pointer = mock(Pointer.class);
        NodeIterator nodeIterator = new NodeIteratorStub(pointer);
        when(pointer.getNamespaceURI("xsi")).thenReturn("http://www.w3.org/2001/XMLSchema-instance");
        when(pointer.getNamespaceURI("xsd")).thenReturn("http://www.w3.org/2001/XMLSchema");

        resolver.setNamespaceContextPointer(pointer);

        Iterator<Map.Entry<String, String>> namespaceIterator = resolver.namespaceIterator();
        assertEquals(2, namespaceIterator.nextSet().size());

        Map.Entry<String, String> namespaceEntry = namespaceIterator.next();
        assertEquals("xsi", namespaceEntry.getKey());
        assertEquals("http://www.w3.org/2001/XMLSchema-instance", namespaceEntry.getValue());

        namespaceEntry = namespaceIterator.next();
        assertEquals("xsd", namespaceEntry.getKey());
        assertEquals("http://www.w3.org/2001/XMLSchema", namespaceEntry.getValue());

        assertFalse(namespaceIterator.hasNext());
    }

}