package org.apache.commons.jxpath.ri;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertSame;

import java.util.HashMap;
import java.util.Map;

import org.apache.commons.jxpath.NodePointer;
import org.apache.commons.jxpath.Pointer;
import org.apache.commons.jxpath.ri.model.NodeIterator;

import org.junit.Before;
import org.junit.Test;

/**
 * Test class for NamespaceResolver.
 */
public class NamespaceResolverTest {

    private NamespaceResolver namespaceResolver;
    private NamespaceResolver parentNamespaceResolver;
    private NodePointer pointer;

    @Before
    public void setUp() {
        namespaceResolver = new NamespaceResolver();
        parentNamespaceResolver = new NamespaceResolver();
        pointer = new NodePointer();
    }

    @Test
    public void testCreateNewNamespaceResolver() {
        // Test creating a new NamespaceResolver with no parent.
        NamespaceResolver namespaceResolver = new NamespaceResolver();
        assertNull(namespaceResolver.getParent());
    }

    @Test
    public void testCreateNewNamespaceResolverWithParent() {
        // Test creating a new NamespaceResolver with a parent.
        NamespaceResolver parentNamespaceResolver = new NamespaceResolver();
        NamespaceResolver namespaceResolver = new NamespaceResolver(parentNamespaceResolver);
        assertSame(parentNamespaceResolver, namespaceResolver.getParent());
    }

    @Test
    public void testRegisterNamespace() {
        // Test registering a namespace.
        namespaceResolver.registerNamespace("test", "http://example.com");
        assertEquals("test", namespaceResolver.getNamespaceURI("test"));
    }

    @Test(expected = IllegalStateException.class)
    public void testRegisterNamespaceOnSealedNamespaceResolver() {
        // Test registering a namespace on a sealed NamespaceResolver.
        namespaceResolver.seal();
        namespaceResolver.registerNamespace("test", "http://example.com");
    }

    @Test
    public void testGetNamespaceURI() {
        // Test getting a namespace URI with an explicit prefix.
        namespaceResolver.registerNamespace("test", "http://example.com");
        assertEquals("http://example.com", namespaceResolver.getNamespaceURI("test"));
        
        // Test getting a namespace URI with a prefix that is not registered.
        assertNull(namespaceResolver.getNamespaceURI("unknown"));
        
        // Test getting a namespace URI with a prefix from the parent.
        parentNamespaceResolver.registerNamespace("test", "http://parent.example.com");
        namespaceResolver.setNamespaceContextPointer(pointer);
        parentNamespaceResolver.setNamespaceContextPointer(pointer);
        assertEquals("http://parent.example.com", namespaceResolver.getNamespaceURI("test"));
    }

    @Test
    public void testGetPrefix() {
        // Test getting a prefix for a registered namespace.
        namespaceResolver.registerNamespace("test", "http://example.com");
        assertEquals("test", namespaceResolver.getPrefix("http://example.com"));
        
        // Test getting a prefix for a namespace URI that is not registered.
        assertNull(namespaceResolver.getPrefix("http://unknown.com"));
        
        // Test getting a prefix for a namespace URI from the parent.
        parentNamespaceResolver.registerNamespace("test", "http://parent.example.com");
        parentNamespaceResolver.registerNamespace("other", "http://other.example.com");
        namespaceResolver.setNamespaceContextPointer(pointer);
        parentNamespaceResolver.setNamespaceContextPointer(pointer);
        assertEquals("test", namespaceResolver.getPrefix("http://parent.example.com"));
        assertEquals("other", namespaceResolver.getPrefix("http://other.example.com"));
    }

    @Test
    public void testIsSealed() {
        // Test creating a new, unsealed NamespaceResolver.
        assertFalse(namespaceResolver.isSealed());
        
        // Test sealing a NamespaceResolver.
        namespaceResolver.seal();
        assertTrue(namespaceResolver.isSealed());
        
        // Test creating a new, unsealed NamespaceResolver with a sealed parent.
        namespaceResolver = new NamespaceResolver(namespaceResolver);
        assertFalse(namespaceResolver.isSealed());
        
        // Test creating a new, sealed NamespaceResolver with a sealed parent.
        namespaceResolver = new NamespaceResolver(namespaceResolver);
        namespaceResolver.seal();
        assertTrue(namespaceResolver.isSealed());
    }

    @Test
    public void testClone() {
        // Test cloning a NamespaceResolver.
        NamespaceResolver namespaceResolver = new NamespaceResolver();
        namespaceResolver.registerNamespace("test", "http://example.com");
        NamespaceResolver clonedNamespaceResolver = (NamespaceResolver) namespaceResolver.clone();
        assertEquals("test", clonedNamespaceResolver.getNamespaceURI("test"));
    }

    @Test
    public void testNamespaceIterator() {
        // Test getting the namespace iterator with no registered namespaces.
        NamespaceResolver namespaceResolver = new NamespaceResolver();
        NodeIterator nodeIterator = namespaceResolver.getNamespaceContextPointer().namespaceIterator();
        assertFalse(nodeIterator.hasNext());

        // Test getting the namespace iterator with registered namespaces.
        namespaceResolver.registerNamespace("test", "http://example.com");
        nodeIterator = namespaceResolver.getNamespaceContextPointer().namespaceIterator();
        assertTrue(nodeIterator.hasNext());
        NodePointer nodePointer = nodeIterator.nextNode();
        assertEquals("test", nodePointer.getName().getName());
        assertEquals("http://example.com", nodePointer.getNamespaceURI());
        assertFalse(nodeIterator.hasNext());
    }
}