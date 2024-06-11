package org.apache.commons.jxpath.ri.test;

import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertEquals;

import java.util.HashMap;
import java.util.Map;

import org.apache.commons.jxpath.NamespaceContext;
import org.apache.commons.jxpath.Pointer;
import org.apache.commons.jxpath.ri.model.NodeIterator;
import org.apache.commons.jxpath.ri.model.NodePointer;
import org.apache.commons.jxpath.ri.Namespaces;
import org.apache.commons.jxpath.ri.NamespaceResolver;
import org.junit.Before;
import org.junit.Test;

/**
 * Tests for NamespaceResolver class.
 *
 * @author Dmitri Plotnikov
 */
public class NamespaceResolverTest {

    private NamespaceResolver resolver;
    private NamespaceResolver parentResolver;
    private NodePointer pointer;
    private NodeIterator nodeIterator;

    @Before
    public void setUp() {
        parentResolver = new NamespaceResolver();
        resolver = new NamespaceResolver(parentResolver);
        pointer = new NodePointer(new String[] { "/root" }, Namespaces.DEFAULT_NS_URI);
        nodeIterator = new NodeIterator(new Object[] { pointer }, null);

        parentResolver.registerNamespace("x", "http://test.com/x");
        parentResolver.registerNamespace("y", "http://test.com/y");
    }

    /**
     * Test method for {@link NamespaceResolver#NamespaceResolver()}.
     */
    @Test
    public void testConstructor() {
        NamespaceResolver resolver = new NamespaceResolver();
        assertNull(resolver.getNamespaceURI("prefix"));
    }

    /**
     * Test method for {@link NamespaceResolver#NamespaceResolver(NamespaceResolver)}.
     */
    @Test
    public void testConstructorWithParent() {
        NamespaceResolver resolver = new NamespaceResolver(parentResolver);
        assertNull(resolver.getNamespaceURI("prefix"));

        parentResolver.registerNamespace("x", "http://test.com/x");
        parentResolver.registerNamespace("y", "http://test.com/y");

        resolver.registerNamespace("z", "http://test.com/z");
        assertEquals("z", resolver.getPrefix("http://test.com/z"));
        assertEquals("x", resolver.getPrefix("http://test.com/x"));
        assertEquals("y", resolver.getPrefix("http://test.com/y"));
    }

    /**
     * Test method for {@link NamespaceResolver#registerNamespace(String, String)}.
     */
    @Test
    public void testRegisterNamespace() {
        resolver.registerNamespace("z", "http://test.com/z");
        assertEquals("z", resolver.getPrefix("http://test.com/z"));

        resolver.registerNamespace("a", "http://test.com/a");
        assertEquals("a", resolver.getPrefix("http://test.com/a"));

        resolver.registerNamespace("prefix", "http://test.com/prefix");
        assertEquals("prefix", resolver.getPrefix("http://test.com/prefix"));
    }

    /**
     * Test method for {@link NamespaceResolver#getNamespaceURI(String)}.
     */
    @Test
    public void testGetNamespaceURI() {
        parentResolver.registerNamespace("x", "http://test.com/x");
        parentResolver.registerNamespace("y", "http://test.com/y");

        resolver.registerNamespace("z", "http://test.com/z");
        assertNull(resolver.getNamespaceURI("prefix"));

        assertNull(resolver.getNamespaceURI("prefix1"));

        assertEquals("http://test.com/x", resolver.getNamespaceURI("x"));
        assertEquals("http://test.com/y", resolver.getNamespaceURI("y"));
        assertEquals("http://test.com/z", resolver.getNamespaceURI("z"));
    }

    /**
     * Test method for {@link NamespaceResolver#getPrefix(String)}.
     */
    @Test
    public void testGetPrefix() {
        parentResolver.registerNamespace("x", "http://test.com/x");
        parentResolver.registerNamespace("y", "http://test.com/y");

        resolver.registerNamespace("z", "http://test.com/z");
        assertNull(resolver.getPrefix("prefix"));

        assertNull(resolver.getPrefix("prefix1"));

        assertEquals("x", resolver.getPrefix("http://test.com/x"));
        assertEquals("y", resolver.getPrefix("http://test.com/y"));
        assertEquals("z", resolver.getPrefix("http://test.com/z"));
    }

    /**
     * Test method for {@link NamespaceResolver#setNamespaceContextPointer(NodePointer)}.
     */
    @Test
    public void testSetNamespaceContextPointer() {
        parentResolver.registerNamespace("x", "http://test.com/x");
        parentResolver.registerNamespace("y", "http://test.com/y");

        resolver.registerNamespace("z", "http://test.com/z");

        pointer.setNamespaceContext(resolver);
        resolver.setNamespaceContextPointer(pointer);

        NodePointer node = new NodePointer(new String[] { "/root/child" },
                "http://test.com/namespace");
        node.setNamespaceContext(resolver);
        nodeIterator.setPosition(node);

        String prefix = resolver.getPrefix("http://test.com/namespace");
        assertEquals("namespace", prefix);

        String uri = resolver.getNamespaceURI("namespace");
        assertEquals("http://test.com/namespace", uri);
    }

    /**
     * Test method for {@link NamespaceResolver#getNamespaceContextPointer()}.
     */
    @Test
    public void testGetNamespaceContextPointer() {
        parentResolver.registerNamespace("x", "http://test.com/x");
        parentResolver.registerNamespace("y", "http://test.com/y");

        resolver.registerNamespace("z", "http://test.com/z");

        pointer.setNamespaceContext(resolver);
        resolver.setNamespaceContextPointer(pointer);

        assertNotNull(resolver.getNamespaceContextPointer());

        NodePointer node = new NodePointer(new String[] { "/root/child" },
                "http://test.com/namespace");
        node.setNamespaceContext(resolver);
        nodeIterator.setPosition(node);

        Pointer namespaceContext = resolver.getNamespaceContextPointer();
        assertEquals(namespaceContext, resolver.getNamespaceContextPointer());
    }

    /**
     * Test method for {@link NamespaceResolver#seal()}.
     */
    @Test
    public void testSeal() {
        NamespaceResolver resolver = new NamespaceResolver();
        resolver.registerNamespace("x", "http://test.com/x");

        NamespaceResolver sealedResolver = new NamespaceResolver(resolver);
        sealedResolver.seal();

        try {
            sealedResolver.registerNamespace("y", "http://test.com/y");
            fail("Expected exception");
        } catch (IllegalStateException e) {
            // expected
        }
    }

    /**
     * Test method for {@link NamespaceResolver#clone()}.
     */
    @Test
    public void testClone() {
        parentResolver.registerNamespace("x", "http://test.com/x");
        parentResolver.registerNamespace("y", "http://test.com/y");

        resolver.registerNamespace("z", "http://test.com/z");

        NamespaceResolver clonedResolver = (NamespaceResolver) resolver.clone();

        assertEquals(namespaceMapSize(resolver), namespaceMapSize(clonedResolver));

        assertEquals(resolver.getNamespaceURI("x"), clonedResolver.getNamespaceURI("x"));
        assertEquals(resolver.getNamespaceURI("y"), clonedResolver.getNamespaceURI("y"));
        assertEquals(resolver.getNamespaceURI("z"), clonedResolver.getNamespaceURI("z"));

        assertEquals(resolver.getPrefix("x"), clonedResolver.getPrefix("x"));
        assertEquals(resolver.getPrefix("y"), clonedResolver.getPrefix("y"));
        assertEquals(resolver.getPrefix("z"), clonedResolver.getPrefix("z"));

        clonedResolver.registerNamespace("w", "http://test.com/w");
        assertNotNull(clonedResolver.getNamespaceURI("w"));
        assertEquals("w", clonedResolver.getPrefix("http://test.com/w"));

        assertEquals(resolver.isSealed(), clonedResolver.isSealed());
    }

    private int namespaceMapSize(NamespaceResolver resolver) {
        Map<String, String> namespaceMap = resolver.namespaceMap;
        return namespaceMap != null ? namespaceMap.size() : 0;
    }
}