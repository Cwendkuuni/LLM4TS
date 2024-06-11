package org.apache.commons.jxpath.ri;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertTrue;

import java.util.HashMap;
import java.util.Map;

import org.apache.commons.jxpath.NodePointer;
import org.apache.commons.jxpath.Pointer;
import org.apache.commons.jxpath.ri.model.NodeIterator;

import org.junit.Before;
import org.junit.Test;

/**
 * JUnit 4 test file for NamespaceResolver class.
 */
public class NamespaceResolverTest {

    private NamespaceResolver namespaceResolver;
    private NamespaceResolver parentNamespaceResolver;
    private NodePointer nodePointer;

    @Before
    public void setUp() {
        namespaceResolver = new NamespaceResolver();
        parentNamespaceResolver = new NamespaceResolver(null);
        nodePointer = new NodePointer("", -1, -1, null);
    }

    /**
     * Test creating a new NamespaceResolver.
     */
    @Test
    public void testCreateNewNamespaceResolver() {
        NamespaceResolver namespaceResolver = new NamespaceResolver();
        assertTrue(namespaceResolver instanceof NamespaceResolver);
    }

    /**
     * Test creating a new NamespaceResolver with parent.
     */
    @Test
    public void testCreateNewNamespaceResolverWithParent() {
        NamespaceResolver namespaceResolver = new NamespaceResolver(parentNamespaceResolver);
        assertTrue(namespaceResolver instanceof NamespaceResolver);
        assertTrue(namespaceResolver.getParent() == parentNamespaceResolver);
    }

    /**
     * Test registering a namespace.
     */
    @Test
    public void testRegisterNamespace() {
        namespaceResolver.registerNamespace("xsi", "http://www.w3.org/2001/XMLSchema-instance");
        assertEquals("xsi", namespaceResolver.getPrefix("http://www.w3.org/2001/XMLSchema-instance"));
        assertEquals("http://www.w3.org/2001/XMLSchema-instance", namespaceResolver.getNamespaceURI("xsi"));
        assertTrue(namespaceResolver.namespaceMap.containsKey("xsi"));
        assertNull(namespaceResolver.getPrefix("http://www.w3.org/1999/xhtml"));
    }

    /**
     * Test getting namespace URI for undefined prefix.
     */
    @Test
    public void testGetNamespaceURIForUndefinedPrefix() {
        assertNull(namespaceResolver.getNamespaceURI("xsi"));
    }

    /**
     * Test getting namespace prefix for undefined namespace URI.
     */
    @Test
    public void testGetPrefixForUndefinedNamespaceURI() {
        assertNull(namespaceResolver.getPrefix("http://www.w3.org/2001/XMLSchema-instance"));
    }

    /**
     * Test getting namespace URI and prefix from parent.
     */
    @Test
    public void testGetNamespaceURIAndPrefixFromParent() {
        parentNamespaceResolver.registerNamespace("xsi", "http://www.w3.org/2001/XMLSchema-instance");
        namespaceResolver = new NamespaceResolver(parentNamespaceResolver);
        assertEquals("xsi", namespaceResolver.getPrefix("http://www.w3.org/2001/XMLSchema-instance"));
        assertEquals("http://www.w3.org/2001/XMLSchema-instance", namespaceResolver.getNamespaceURI("xsi"));
    }

    /**
     * Test setting namespace context pointer.
     */
    @Test
    public void testSetNamespaceContextPointer() {
        NodePointer nodePointer = new NodePointer("", 0, 0, null);
        namespaceResolver.setNamespaceContextPointer(nodePointer);
        assertEquals(nodePointer, namespaceResolver.getNamespaceContextPointer());
    }

    /**
     * Test getting namespace context pointer.
     */
    @Test
    public void testGetNamespaceContextPointer() {
        NodePointer nodePointer = new NodePointer("", 0, 0, null);
        namespaceResolver.setNamespaceContextPointer(nodePointer);
        assertEquals(nodePointer, namespaceResolver.getNamespaceContextPointer());
        assertNull(parentNamespaceResolver.getNamespaceContextPointer());
    }

    /**
     * Test sealing a NamespaceResolver.
     */
    @Test
    public void testSeal() {
        namespaceResolver.registerNamespace("xsi", "http://www.w3.org/2001/XMLSchema-instance");
        namespaceResolver.seal();
        try {
            namespaceResolver.registerNamespace("xsi", "http://www.w3.org/2001/XMLSchema-instance");
            fail("Expected an IllegalStateException");
        } catch (IllegalStateException e) {
            // Expected
        }
    }

    /**
     * Test cloning a NamespaceResolver.
     */
    @Test
    public void testClone() {
        namespaceResolver.registerNamespace("xsi", "http://www.w3.org/2001/XMLSchema-instance");
        NamespaceResolver clonedNamespaceResolver = (NamespaceResolver) namespaceResolver.clone();
        assertEquals("xsi", clonedNamespaceResolver.getPrefix("http://www.w3.org/2001/XMLSchema-instance"));
        assertEquals("http://www.w3.org/2001/XMLSchema-instance", clonedNamespaceResolver.getNamespaceURI("xsi"));
    }

    /**
     * Test iterating through namespace nodes in a NodeIterator.
     */
    @Test
    public void testNamespaceIterator() {
        namespaceResolver.registerNamespace("xsi", "http://www.w3.org/2001/XMLSchema-instance");
        NodeIterator nodeIterator = nodePointer.namespaceIterator();
        assertEquals(1, nodeIterator.getLength());
        assertTrue(nodeIterator.nextNode() instanceof NodePointer);
        NodePointer nodePointer = (NodePointer) nodeIterator.nextNode();
        assertEquals("http://www.w3.org/2001/XMLSchema-instance", nodePointer.getNamespaceURI());
        assertEquals("xsi", nodePointer.getName().getName());
    }
}