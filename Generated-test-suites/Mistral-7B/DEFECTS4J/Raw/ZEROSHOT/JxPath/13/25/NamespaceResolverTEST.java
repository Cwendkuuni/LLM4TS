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
    private NodePointer nodePointer;

    /**
     * Initialize test environment.
     */
    @Before
    public void setUp() {
        namespaceResolver = new NamespaceResolver();
        parentNamespaceResolver = new NamespaceResolver();
        nodePointer = new NodePointer(new Object(), "test", new Object[0]);
    }

    /**
     * Test constructor.
     */
    @Test
    public void testConstructor() {
        NamespaceResolver namespaceResolver = new NamespaceResolver();
        assertNull(namespaceResolver.getParent());
        assertEquals(0, namespaceResolver.getNamespaceMap().size());

        NamespaceResolver namespaceResolverWithParent = new NamespaceResolver(parentNamespaceResolver);
        assertSame(parentNamespaceResolver, namespaceResolverWithParent.getParent());
    }

    /**
     * Test registerNamespace method.
     */
    @Test
    public void testRegisterNamespace() {
        namespaceResolver.registerNamespace("test", "http://test.com");
        assertEquals("http://test.com", namespaceResolver.getNamespaceURI("test"));
        assertEquals("test", namespaceResolver.getPrefix("http://test.com"));
        assertEquals(1, namespaceResolver.getNamespaceMap().size());

        namespaceResolver.registerNamespace("test2", "http://test2.com");
        assertEquals("http://test2.com", namespaceResolver.getNamespaceURI("test2"));
        assertEquals("test2", namespaceResolver.getPrefix("http://test2.com"));
        assertEquals(2, namespaceResolver.getNamespaceMap().size());
    }

    /**
     * Test registerNamespace method with parent.
     */
    @Test
    public void testRegisterNamespaceWithParent() {
        parentNamespaceResolver.registerNamespace("parentTest", "http://parent.com");
        namespaceResolver.registerNamespace("test", "http://test.com");

        assertEquals("http://parent.com", namespaceResolver.getNamespaceURI("parentTest"));
        assertEquals("parentTest", namespaceResolver.getPrefix("http://parent.com"));
        assertEquals("http://test.com", namespaceResolver.getNamespaceURI("test"));
        assertEquals("test", namespaceResolver.getPrefix("http://test.com"));
        assertEquals(2, namespaceResolver.getNamespaceMap().size());

        assertEquals("http://parent.com", parentNamespaceResolver.getNamespaceURI("parentTest"));
        assertEquals("parentTest", parentNamespaceResolver.getPrefix("http://parent.com"));
        assertNull(parentNamespaceResolver.getNamespaceURI("test"));
        assertNull(parentNamespaceResolver.getPrefix("http://test.com"));
    }

    /**
     * Test getNamespaceURI method.
     */
    @Test
    public void testGetNamespaceURI() {
        namespaceResolver.registerNamespace("test", "http://test.com");
        assertEquals("http://test.com", namespaceResolver.getNamespaceURI("test"));
        assertNull(namespaceResolver.getNamespaceURI(""));
        assertNull(namespaceResolver.getNamespaceURI("notRegistered"));

        NamespaceResolver namespaceResolverWithParent = new NamespaceResolver(parentNamespaceResolver);
        namespaceResolverWithParent.registerNamespace("test", "http://test2.com");
        assertEquals("http://test.com", namespaceResolver.getNamespaceURI("test"));
        assertEquals("http://test2.com", namespaceResolverWithParent.getNamespaceURI("test"));
        assertNull(namespaceResolver.getNamespaceURI("notRegistered"));
        assertNull(namespaceResolverWithParent.getNamespaceURI("notRegistered"));
    }

    /**
     * Test getPrefix method.
     */
    @Test
    public void testGetPrefix() {
        namespaceResolver.registerNamespace("test", "http://test.com");
        assertEquals("test", namespaceResolver.getPrefix("http://test.com"));
        assertNull(namespaceResolver.getPrefix(""));
        assertNull(namespaceResolver.getPrefix("notRegistered"));

        NamespaceResolver namespaceResolverWithParent = new NamespaceResolver(parentNamespaceResolver);
        namespaceResolverWithParent.registerNamespace("test", "http://test2.com");
        assertEquals("test", namespaceResolver.getPrefix("http://test.com"));
        assertEquals("test2", namespaceResolverWithParent.getPrefix("http://test.com"));
        assertNull(namespaceResolver.getPrefix("notRegistered"));
        assertNull(namespaceResolverWithParent.getPrefix("notRegistered"));
    }

    /**
     * Test setNamespaceContextPointer method.
     */
    @Test
    public void testSetNamespaceContextPointer() {
        NodePointer nodePointer = new NodePointer(new Object(), "test", new Object[0]);
        namespaceResolver.setNamespaceContextPointer(nodePointer);
        assertEquals(nodePointer, namespaceResolver.getNamespaceContextPointer());
        assertNull(namespaceResolver.getNamespaceContextPointerBeforeSet());
    }

    /**
     * Test getNamespaceContextPointer method.
     */
    @Test
    public void testGetNamespaceContextPointer() {
        NodePointer nodePointer = new NodePointer(new Object(), "test", new Object[0]);
        namespaceResolver.setNamespaceContextPointer(nodePointer);
        assertEquals(nodePointer, namespaceResolver.getNamespaceContextPointer());
        assertNull(namespaceResolver.getNamespaceContextPointerBeforeSet());

        NamespaceResolver namespaceResolverWithParent = new NamespaceResolver(namespaceResolver);
        namespaceResolverWithParent.setNamespaceContextPointer(nodePointer);
        assertEquals(nodePointer, namespaceResolverWithParent.getNamespaceContextPointer());
        assertEquals(nodePointer, namespaceResolver.getNamespaceContextPointer());
    }

    /**
     * Test getNamespaceContextPointer method with null.
     */
    @Test(expected = NullPointerException.class)
    public void testGetNamespaceContextPointerNull() {
        namespaceResolver.getNamespaceContextPointer();
    }

    /**
     * Test getNamespaceContextPointerBeforeSet method.
     */
    @Test
    public void testGetNamespaceContextPointerBeforeSet() {
        NodePointer nodePointer = new NodePointer(new Object(), "test", new Object[0]);
        namespaceResolver.setNamespaceContextPointer(nodePointer);
        assertNull(namespaceResolver.getNamespaceContextPointerBeforeSet());

        namespaceResolver.setNamespaceContextPointer(new NodePointer(new Object(), "test2", new Object[0]));
        assertSame(nodePointer, namespaceResolver.getNamespaceContextPointerBeforeSet());
    }

    /**
     * Test seal method.
     */
    @Test
    public void testSeal() {
        namespaceResolver.registerNamespace("test", "http://test.com");
        namespaceResolver.seal();
        try {
            namespaceResolver.registerNamespace("test2", "http://test2.com");
            assert false;
        } catch (IllegalStateException e) {
            // Expected
        }
    }

    /**
     * Test clone method.
     */
    @Test
    public void testClone() {
        namespaceResolver.registerNamespace("test", "http://test.com");
        NamespaceResolver cloned = (NamespaceResolver) namespaceResolver.clone();
        assertEquals("http://test.com", cloned.getNamespaceURI("test"));
    }
}