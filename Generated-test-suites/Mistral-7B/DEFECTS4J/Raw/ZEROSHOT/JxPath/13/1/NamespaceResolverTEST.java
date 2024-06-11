package org.apache.commons.jxpath.ri;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertSame;

import java.util.HashMap;
import java.util.Map;

import org.apache.commons.jxpath.NodePointer;
import org.apache.commons.jxpath.Pointer;
import org.apache.commons.jxpath.ri.model.NodeIterator;

import org.junit.Test;

public class NamespaceResolverTest {

    @Test
    public void testNewInstance() {
        NamespaceResolver resolver = new NamespaceResolver();
        assertNull(resolver.getParent());
        assertNull(resolver.getNamespaceMap());
        assertSame(resolver, resolver.clone());
    }

    @Test
    public void testNewInstanceWithParent() {
        NamespaceResolver parentResolver = new NamespaceResolver();
        NamespaceResolver resolver = new NamespaceResolver(parentResolver);
        assertSame(parentResolver, resolver.getParent());
        assertNull(resolver.getNamespaceMap());
        assertSame(resolver, resolver.clone());
    }

    @Test
    public void testRegisterNamespaceWithEmptyPrefix() {
        NamespaceResolver resolver = new NamespaceResolver();
        resolver.registerNamespace("", "http://test.com");
        Map<String, String> namespaceMap = (Map<String, String>) resolver.getNamespaceMap().clone();
        assertEquals("http://test.com", namespaceMap.get(""));
        assertNull(resolver.getPrefix("http://test.com"));
    }

    @Test
    public void testRegisterNamespace() {
        NamespaceResolver resolver = new NamespaceResolver();
        resolver.registerNamespace("prefix", "http://test.com");
        Map<String, String> namespaceMap = (Map<String, String>) resolver.getNamespaceMap().clone();
        assertEquals("http://test.com", namespaceMap.get("prefix"));
        assertEquals("prefix", resolver.getPrefix("http://test.com"));
    }

    @Test
    public void testRegisterNamespaceWithExistingPrefix() {
        NamespaceResolver resolver = new NamespaceResolver();
        resolver.registerNamespace("prefix1", "http://test1.com");
        resolver.registerNamespace("prefix1", "http://test2.com");
        Map<String, String> namespaceMap = (Map<String, String>) resolver.getNamespaceMap().clone();
        assertEquals("http://test2.com", namespaceMap.get("prefix1"));
        assertEquals("prefix1", resolver.getPrefix("http://test2.com"));
    }

    @Test
    public void testGetNamespaceURI() {
        NamespaceResolver resolver = new NamespaceResolver();
        resolver.registerNamespace("prefix1", "http://test1.com");
        resolver.registerNamespace("prefix2", "http://test2.com");
        assertEquals("http://test1.com", resolver.getNamespaceURI("prefix1"));
        assertEquals("http://test2.com", resolver.getNamespaceURI("prefix2"));
        assertNull(resolver.getNamespaceURI("prefix3"));

        NodePointer nodePointer = new NodePointer();
        resolver.setNamespaceContextPointer(nodePointer);
        assertEquals("http://test1.com", resolver.getNamespaceURI("prefix1"));
        assertEquals("http://test2.com", resolver.getNamespaceURI("prefix2"));
        assertNull(resolver.getNamespaceURI("prefix3"));
    }

    @Test
    public void testGetPrefix() {
        NamespaceResolver resolver = new NamespaceResolver();
        resolver.registerNamespace("prefix1", "http://test1.com");
        resolver.registerNamespace("prefix2", "http://test2.com");
        assertEquals("prefix1", resolver.getPrefix("http://test1.com"));
        assertEquals("prefix2", resolver.getPrefix("http://test2.com"));
        assertNull(resolver.getPrefix("http://test3.com"));

        NodePointer nodePointer = new NodePointer();
        resolver.setNamespaceContextPointer(nodePointer);
        assertEquals("prefix1", resolver.getPrefix("http://test1.com"));
        assertEquals("prefix2", resolver.getPrefix("http://test2.com"));
        assertNull(resolver.getPrefix("http://test3.com"));
    }

    @Test
    public void testSeal() {
        NamespaceResolver resolver = new NamespaceResolver();
        resolver.registerNamespace("prefix1", "http://test1.com");
        resolver.registerNamespace("prefix2", "http://test2.com");
        resolver.seal();

        try {
            resolver.registerNamespace("prefix3", "http://test3.com");
            assert false;
        } catch (IllegalStateException e) {
            assertEquals("Cannot register namespaces on a sealed NamespaceResolver", e.getMessage());
        }
    }

    @Test
    public void testClone() throws CloneNotSupportedException {
        NamespaceResolver resolver = new NamespaceResolver();
        resolver.registerNamespace("prefix1", "http://test1.com");
        resolver.registerNamespace("prefix2", "http://test2.com");
        resolver.seal();

        NamespaceResolver clonedResolver = (NamespaceResolver) resolver.clone();
        Map<String, String> clonedNamespaceMap = (Map<String, String>) clonedResolver.getNamespaceMap().clone();
        assertEquals(resolver.getNamespaceMap(), clonedNamespaceMap);
        assertEquals(resolver.isSealed(), clonedResolver.isSealed());
    }

    @Test
    public void testNamespaceIterator() {
        NamespaceResolver resolver = new NamespaceResolver();
        resolver.registerNamespace("prefix1", "http://test1.com");
        resolver.registerNamespace("prefix2", "http://test2.com");
        resolver.seal();

        NodePointer nodePointer = new NodePointer();
        resolver.setNamespaceContextPointer(nodePointer);

        NodeIterator nodeIterator = nodePointer.namespaceIterator();
        Map<String, String> namespaceMap = new HashMap<>();
        while (nodeIterator.hasNext()) {
            NodePointer nodePointer = nodeIterator.getNodePointer();
            String prefix = nodePointer.getName().getName();
            String namespaceURI = nodePointer.getNamespaceURI();
            namespaceMap.put(prefix, namespaceURI);
        }

        assertEquals(2, namespaceMap.size());
        assertEquals("prefix1", namespaceMap.get("prefix1"));
        assertEquals("http://test1.com", namespaceMap.get("prefix1"));
        assertEquals("prefix2", namespaceMap.get("prefix2"));
        assertEquals("http://test2.com", namespaceMap.get("prefix2"));
    }
}