package org.apache.commons.cli;

import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertEquals;

import org.apache.commons.jxpath.ri.NamespaceResolver;
import org.apache.commons.jxpath.ri.model.NodeIterator;
import org.apache.commons.jxpath.ri.model.NodePointer;

public class NamespaceResolverTest {

    @Test
    public void testNewInstance() {
        NamespaceResolver resolver = new NamespaceResolver();
        assertNull(resolver.getParent());
        assertNull(resolver.getNamespaceMap());
        assertNull(resolver.getReverseMap());
        assertNull(resolver.getPointer());
        assertFalse(resolver.isSealed());
    }

    @Test
    public void testNewInstanceWithParent() {
        NamespaceResolver parentResolver = new NamespaceResolver();
        NamespaceResolver resolver = new NamespaceResolver(parentResolver);
        assertEquals(parentResolver, resolver.getParent());
        assertNull(resolver.getNamespaceMap());
        assertNull(resolver.getReverseMap());
        assertNull(resolver.getPointer());
        assertFalse(resolver.isSealed());
    }

    @Test
    public void testRegisterNamespace() {
        NamespaceResolver resolver = new NamespaceResolver();
        resolver.registerNamespace("foo", "http://example.com/foo");
        assertEquals("http://example.com/foo", resolver.getNamespaceURI("foo"));
        assertEquals("foo", resolver.getPrefix("http://example.com/foo"));
        assertEquals(1, resolver.getNamespaceMap().size());
        assertEquals("http://example.com/foo", resolver.getReverseMap().get("foo"));

        try {
            resolver.registerNamespace("foo", "http://example.com/foo");
            fail("Exception expected");
        } catch (IllegalStateException e) {
            assertEquals("Cannot register namespaces on a sealed NamespaceResolver", e.getMessage());
        }

        resolver.seal();
        try {
            resolver.registerNamespace("foo", "http://example.com/foo");
            fail("Exception expected");
        } catch (IllegalStateException e) {
            assertEquals("Cannot register namespaces on a sealed NamespaceResolver", e.getMessage());
        }
    }

    @Test
    public void testSetNamespaceContextPointer() {
        NamespaceResolver resolver = new NamespaceResolver();
        NodePointer pointer = new NodePointer(null, null, null, 0);
        resolver.setNamespaceContextPointer(pointer);
        assertEquals(pointer, resolver.getNamespaceContextPointer());
    }

    @Test
    public void testGetNamespaceContextPointer() {
        NamespaceResolver resolver = new NamespaceResolver();
        NodePointer pointer1 = new NodePointer(null, null, null, 0);
        resolver.setNamespaceContextPointer(pointer1);
        assertEquals(pointer1, resolver.getNamespaceContextPointer());

        NamespaceResolver parentResolver = new NamespaceResolver();
        NodePointer pointer2 = new NodePointer(null, null, null, 0);
        parentResolver.setNamespaceContextPointer(pointer2);
        resolver = new NamespaceResolver(parentResolver);
        assertEquals(pointer2, resolver.getNamespaceContextPointer());

        assertNull(resolver.getNamespaceContextPointer());
    }

    @Test
    public void testGetNamespaceURI() {
        NamespaceResolver resolver = new NamespaceResolver();
        String uri1 = resolver.getNamespaceURI("foo");
        assertNull(uri1);

        resolver.registerNamespace("foo", "http://example.com/foo");
        String uri2 = resolver.getNamespaceURI("foo");
        assertEquals("http://example.com/foo", uri2);

        String uri3 = resolver.getNamespaceURI("bar");
        assertNull(uri3);

        NodePointer pointer = new NodePointer(null, null, null, 0);
        resolver.setNamespaceContextPointer(pointer);
        String uri4 = resolver.getNamespaceURI("foo");
        assertEquals("http://example.com/foo", uri4);

        NodeIterator ni = new NodeIterator(null, null, null, new Object[0]);
        NodePointer nsPointer = new NodePointer(null, null, null, 0);
        ni.setNamespaceIterator(new NamespaceIterator() {
            @Override
            public NodePointer getNamespaceNode(int position) {
                return nsPointer;
            }

            @Override
            public String getNamespaceURI() {
                return "http://example.com/bar";
            }

            @Override
            public String getNamespacePrefix() {
                return "bar";
            }
        });
        resolver.setNamespaceContextPointer(ni);
        String uri5 = resolver.getNamespaceURI("bar");
        assertEquals("http://example.com/bar", uri5);

        resolver = new NamespaceResolver(resolver);
        String uri6 = resolver.getNamespaceURI("foo");
        assertEquals("http://example.com/foo", uri6);

        resolver.seal();
        String uri7 = resolver.getNamespaceURI("foo");
        assertEquals("http://example.com/foo", uri7);
    }

    @Test
    public void testGetPrefix() {
        NamespaceResolver resolver = new NamespaceResolver();
        String prefix1 = resolver.getPrefix("http://example.com/foo");
        assertNull(prefix1);

        resolver.registerNamespace("foo", "http://example.com/foo");
        String prefix2 = resolver.getPrefix("http://example.com/foo");
        assertEquals("foo", prefix2);

        String prefix3 = resolver.getPrefix("http://example.com/bar");
        assertNull(prefix3);

        NodePointer pointer = new NodePointer(null, null, null, 0);
        resolver.setNamespaceContextPointer(pointer);
        String prefix4 = resolver.getPrefix("http://example.com/foo");
        assertEquals("foo", prefix4);

        NodeIterator ni = new NodeIterator(null, null, null, new Object[0]);
        NodePointer nsPointer = new NodePointer(null, null, null, 0);
        ni.setNamespaceIterator(new NamespaceIterator() {
            @Override
            public NodePointer getNamespaceNode(int position) {
                return nsPointer;
            }

            @Override
            public String getNamespaceURI() {
                return "http://example.com/bar";
            }

            @Override
            public String getNamespacePrefix() {
                return "bar";
            }
        });
        resolver.setNamespaceContextPointer(ni);
        String prefix5 = resolver.getPrefix("http://example.com/bar");
        assertEquals("bar", prefix5);

        resolver = new NamespaceResolver(resolver);
        String prefix6 = resolver.getPrefix("http://example.com/foo");
        assertEquals("foo", prefix6);

        resolver.seal();
        String prefix7 = resolver.getPrefix("http://example.com/foo");
        assertEquals("foo", prefix7);
    }

    @Test
    public void testLearnWhetherSealed() {
        NamespaceResolver resolver = new NamespaceResolver();
        assertFalse(resolver.isSealed());

        resolver.seal();
        assertTrue(resolver.isSealed());

        resolver = new NamespaceResolver(resolver);
        assertTrue(resolver.isSealed());
    }

    @Test
    public void testClone() {
        NamespaceResolver resolver = new NamespaceResolver();
        assertEquals(resolver, resolver.clone());

        resolver.registerNamespace("foo", "http://example.com/foo");
        NamespaceResolver cloned = (NamespaceResolver) resolver.clone();
        assertEquals("http://example.com/foo", cloned.getNamespaceURI("foo"));

        resolver.seal();
        NamespaceResolver clonedSealed = (NamespaceResolver) resolver.clone();
        assertTrue(clonedSealed.isSealed());
    }
}