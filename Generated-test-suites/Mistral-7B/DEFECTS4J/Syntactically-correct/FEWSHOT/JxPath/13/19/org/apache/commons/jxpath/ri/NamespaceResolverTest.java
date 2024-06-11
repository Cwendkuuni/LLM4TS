package org.apache.commons.jxpath.ri;

import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertEquals;

import org.apache.commons.jxpath.Pointer;
import org.apache.commons.jxpath.ri.model.NodeIterator;
import org.apache.commons.jxpath.ri.model.NodePointer;

public class NamespaceResolverTest {

    @Test
    public void testNewInstance() {
        NamespaceResolver resolver = new NamespaceResolver();
        assertNull(resolver.parent);
        assertNull(resolver.namespaceMap);
        assertNull(resolver.reverseMap);
        assertNull(resolver.pointer);
        assertFalse(resolver.isSealed());
    }

    @Test
    public void testNewInstanceWithParent() {
        NamespaceResolver parentResolver = new NamespaceResolver();
        NamespaceResolver resolver = new NamespaceResolver(parentResolver);
        assertEquals(parentResolver, resolver.parent);
        assertNull(resolver.namespaceMap);
        assertNull(resolver.reverseMap);
        assertNull(resolver.pointer);
        assertFalse(resolver.isSealed());
    }

    @Test
    public void testRegisterNamespace() {
        NamespaceResolver resolver = new NamespaceResolver();
        resolver.registerNamespace("x", "http://example.com/x");
        assertEquals("http://example.com/x", resolver.getNamespaceURI("x"));
        assertEquals("x", resolver.getPrefix("http://example.com/x"));
        assertEquals(1, resolver.namespaceMap.size());
        assertNull(resolver.reverseMap);

        resolver.registerNamespace("x", "http://example.org/x");
        assertEquals("http://example.org/x", resolver.getNamespaceURI("x"));
        assertEquals("x", resolver.getPrefix("http://example.org/x"));
        assertEquals(1, resolver.namespaceMap.size());
    }

    @Test(expected = IllegalStateException.class)
    public void testRegisterNamespaceSealed() {
        NamespaceResolver resolver = new NamespaceResolver();
        resolver.seal();
        resolver.registerNamespace("x", "http://example.com/x");
    }

    @Test
    public void testSetNamespaceContextPointer() {
        NamespaceResolver resolver = new NamespaceResolver();
        NodePointer pointer = new NodePointerImpl();
        resolver.setNamespaceContextPointer(pointer);
        assertEquals(pointer, resolver.getNamespaceContextPointer());
    }

    @Test
    public void testGetNamespaceContextPointer() {
        NamespaceResolver resolver = new NamespaceResolver();
        NodePointer pointer = new NodePointerImpl();
        resolver.setNamespaceContextPointer(pointer);
        assertEquals(pointer, resolver.getNamespaceContextPointer());
        NodePointer nullPointer = null;
        assertNull(resolver.getNamespaceContextPointer());
        resolver.setNamespaceContextPointer(nullPointer);
        assertNull(resolver.getNamespaceContextPointer());
    }

    @Test
    public void testGetNamespaceURI() {
        NamespaceResolver resolver = new NamespaceResolver();
        assertEquals(null, resolver.getNamespaceURI("x"));
        NodePointer pointer = new NodePointerImpl();
        resolver.setNamespaceContextPointer(pointer);
        assertEquals(pointer.getNamespaceURI("x"), resolver.getNamespaceURI("x"));
        assertEquals(null, resolver.getNamespaceURI("y"));
        assertEquals(null, resolver.getNamespaceURI(null));
    }

    @Test
    public void testGetPrefix() {
        NamespaceResolver resolver = new NamespaceResolver();
        assertEquals(null, resolver.getPrefix("x"));
        NodePointer pointer = new NodePointerImpl();
        resolver.setNamespaceContextPointer(pointer);
        assertEquals(pointer.getPrefix("x"), resolver.getPrefix("x"));
        assertEquals(null, resolver.getPrefix("y"));
        assertEquals(null, resolver.getPrefix(null));
    }

    @Test
    public void testGetPrefixFromReverseMap() {
        NamespaceResolver resolver = new NamespaceResolver();
        String uri = "http://example.com/x";
        resolver.registerNamespace("x", uri);
        String prefix = resolver.getPrefix(uri);
        assertEquals("x", prefix);
        NodeIterator ni = null;
        resolver.reverseMap = new HashMap() {{
            put(uri, "x");
            put("http://example.org/x", "y");
            put("http://example.org/y", "z");
        }};
        prefix = resolver.getPrefix(uri);
        assertEquals("x", prefix);
        prefix = resolver.getPrefix("http://example.org/x");
        assertEquals("y", prefix);
        prefix = resolver.getPrefix("http://example.org/y");
        assertEquals("z", prefix);
    }

    @Test
    public void testIsSealed() {
        NamespaceResolver resolver = new NamespaceResolver();
        assertFalse(resolver.isSealed());
        resolver.seal();
        assertTrue(resolver.isSealed());
    }

    @Test
    public void testClone() {
        NamespaceResolver original = new NamespaceResolver();
        original.registerNamespace("x", "http://example.com/x");
        NamespaceResolver clone = (NamespaceResolver) original.clone();
        assertEquals(original.namespaceMap, clone.namespaceMap);
        assertEquals(original.reverseMap, clone.reverseMap);
        assertEquals(original.pointer, clone.pointer);
        assertEquals(original.isSealed(), clone.isSealed());
    }
}