package org.apache.commons.jxpath.ri.test;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertSame;

import java.util.HashMap;
import java.util.Map;

import org.apache.commons.jxpath.NamespaceResolver;
import org.apache.commons.jxpath.NodePointer;
import org.apache.commons.jxpath.Pointer;
import org.apache.commons.jxpath.ri.model.NodeIterator;
import org.apache.commons.jxpath.ri.model.NodePointer;

import org.junit.Test;

/**
 * Test case for {@link NamespaceResolver}.
 */
public class NamespaceResolverTest {

    @Test
    public void testNewNamespaceResolver() {
        NamespaceResolver resolver = new NamespaceResolver();
        assertNull(resolver.getNamespaceURI("prefix:foo"));
    }

    @Test
    public void testRegisterNamespace() {
        NamespaceResolver resolver = new NamespaceResolver();
        resolver.registerNamespace("prefix", "http://example.com/ns");
        assertEquals("http://example.com/ns", resolver.getNamespaceURI("prefix"));
    }

    @Test(expected = IllegalStateException.class)
    public void testRegisterNamespaceAfterSealing() {
        NamespaceResolver resolver = new NamespaceResolver();
        resolver.registerNamespace("prefix", "http://example.com/ns");
        resolver.seal();
        resolver.registerNamespace("prefix2", "http://example.com/ns2");
    }

    @Test
    public void testGetNamespaceURIWithParent() {
        NamespaceResolver parent = new NamespaceResolver();
        parent.registerNamespace("prefix", "http://example.com/ns");
        NamespaceResolver child = new NamespaceResolver(parent);
        child.registerNamespace("prefix2", "http://example.com/ns2");
        assertEquals("http://example.com/ns", child.getNamespaceURI("prefix"));
        assertEquals("http://example.com/ns2", child.getNamespaceURI("prefix2"));
        assertEquals("http://example.com/ns", parent.getNamespaceURI("prefix"));
        assertNull(parent.getNamespaceURI("prefix2"));
    }

    @Test
    public void testGetNamespaceURIWithContext() {
        NamespaceResolver resolver = new NamespaceResolver();
        resolver.registerNamespace("prefix", "http://example.com/ns");
        NodePointer context = new NodePointer("root");
        resolver.setNamespaceContextPointer(context);
        resolver.registerNamespace("prefix2", "http://example.com/ns2");
        assertEquals("http://example.com/ns", resolver.getNamespaceURI("prefix"));
        assertEquals("http://example.com/ns2", resolver.getNamespaceURI("prefix2"));
        assertEquals("http://example.com/ns", context.getNamespaceURI("prefix"));
        assertNull(context.getNamespaceURI("prefix2"));
    }

    @Test
    public void testGetPrefix() {
        NamespaceResolver resolver = new NamespaceResolver();
        resolver.registerNamespace("prefix", "http://example.com/ns");
        assertEquals("prefix", resolver.getPrefix("http://example.com/ns"));
        assertNull(resolver.getPrefix("http://example.com/other"));
    }

    @Test
    public void testSeal() {
        NamespaceResolver resolver = new NamespaceResolver();
        resolver.registerNamespace("prefix", "http://example.com/ns");
        resolver.registerNamespace("prefix2", "http://example.com/ns2");
        resolver.seal();
        assertFalse(resolver.isSealed());
        try {
            resolver.registerNamespace("prefix3", "http://example.com/ns3");
        } catch (IllegalStateException e) {
            assertTrue(resolver.isSealed());
        }
    }

    @Test
    public void testClone() {
        NamespaceResolver original = new NamespaceResolver();
        original.registerNamespace("prefix", "http://example.com/ns");
        NamespaceResolver clone = (NamespaceResolver) original.clone();
        assertEquals("http://example.com/ns", clone.getNamespaceURI("prefix"));
    }

    @Test
    public void testNamespaceIterator() {
        NamespaceResolver resolver = new NamespaceResolver();
        resolver.registerNamespace("prefix", "http://example.com/ns");
        NodePointer context = new NodePointer("root");
        resolver.setNamespaceContextPointer(context);
        resolver.registerNamespace("prefix2", "http://example.com/ns2");

        NodeIterator ni = context.namespaceIterator();
        NodePointer nodePointer = null;
        while (ni.hasNext()) {
            nodePointer = ni.getNodePointer();
            assertEquals("prefix", nodePointer.getName().getLocalPart());
            assertEquals("http://example.com/ns", nodePointer.getNamespaceURI());
            ni.next();
            assertEquals("prefix2", nodePointer.getName().getLocalPart());
            assertEquals("http://example.com/ns2", nodePointer.getNamespaceURI());
        }
        assertFalse(ni.hasNext());
    }
}