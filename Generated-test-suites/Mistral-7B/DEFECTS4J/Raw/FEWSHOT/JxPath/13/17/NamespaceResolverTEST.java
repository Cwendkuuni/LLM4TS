package org.apache.commons.jxpath.ri;

import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertEquals;

import org.apache.commons.jxpath.Pointer;
import org.apache.commons.jxpath.ri.model.NodeIterator;
import org.apache.commons.jxpath.ri.model.NodePointer;

import org.junit.Test;

public class NamespaceResolverTest {

    @Test
    public void testNewNamespaceResolver() {
        NamespaceResolver resolver = new NamespaceResolver();
        assertNull(resolver.getParent());
        assertNull(resolver.getNamespaceMap());
        assertNull(resolver.getReverseMap());
        assertNull(resolver.getPointer());
        assertFalse(resolver.isSealed());
    }

    @Test
    public void testNamespaceResolverWithParent() {
        NamespaceResolver parent = new NamespaceResolver();
        NamespaceResolver resolver = new NamespaceResolver(parent);
        assertEquals(parent, resolver.getParent());
        assertNull(resolver.getNamespaceMap());
        assertNull(resolver.getReverseMap());
        assertNull(resolver.getPointer());
        assertFalse(resolver.isSealed());
    }

    @Test
    public void testRegisterNamespace() {
        NamespaceResolver resolver = new NamespaceResolver();
        resolver.registerNamespace("prefix1", "http://example.com/prefix1");
        Map namespaceMap = resolver.getNamespaceMap();
        assertTrue(namespaceMap.containsKey("prefix1"));
        assertEquals("http://example.com/prefix1", namespaceMap.get("prefix1"));
        assertNull(resolver.getReverseMap());
        assertNull(resolver.getPointer());
        assertFalse(resolver.isSealed());
    }

    @Test
    public void testRegisterNamespaceWithExistingPrefix() {
        NamespaceResolver resolver = new NamespaceResolver();
        resolver.registerNamespace("prefix1", "http://example.com/prefix1");
        try {
            resolver.registerNamespace("prefix1", "http://example.com/prefix2");
        } catch (IllegalStateException e) {
            assertEquals("Cannot register namespaces on a sealed NamespaceResolver", e.getMessage());
            assertTrue(resolver.isSealed());
        }
    }

    @Test
    public void testGetNamespaceURI() {
        NamespaceResolver resolver = new NamespaceResolver();
        resolver.registerNamespace("prefix1", "http://example.com/prefix1");
        assertEquals("http://example.com/prefix1", resolver.getNamespaceURI("prefix1"));
        assertEquals(null, resolver.getNamespaceURI("prefix2"));
        assertNull(resolver.getPointer());
        assertFalse(resolver.isSealed());
    }

    @Test
    public void testGetNamespaceURIWithParent() {
        NamespaceResolver parent = new NamespaceResolver();
        parent.registerNamespace("prefix1", "http://example.com/prefix1");
        NamespaceResolver resolver = new NamespaceResolver(parent);
        assertEquals("http://example.com/prefix1", resolver.getNamespaceURI("prefix1"));
        assertEquals(null, resolver.getNamespaceURI("prefix2"));
        assertEquals("http://example.com/prefix1", parent.getNamespaceURI("prefix1"));
        assertNull(resolver.getPointer());
        assertFalse(resolver.isSealed());
    }

    @Test
    public void testGetPrefix() {
        NamespaceResolver resolver = new NamespaceResolver();
        resolver.registerNamespace("prefix1", "http://example.com/prefix1");
        assertEquals("prefix1", resolver.getPrefix("http://example.com/prefix1"));
        assertEquals(null, resolver.getPrefix("http://example.com/prefix2"));
        assertNull(resolver.getPointer());
        assertFalse(resolver.isSealed());
    }

    @Test
    public void testGetPrefixWithParent() {
        NamespaceResolver parent = new NamespaceResolver();
        parent.registerNamespace("prefix1", "http://example.com/prefix1");
        NamespaceResolver resolver = new NamespaceResolver(parent);
        assertEquals("prefix1", resolver.getPrefix("http://example.com/prefix1"));
        assertEquals(null, resolver.getPrefix("http://example.com/prefix2"));
        assertEquals("prefix1", parent.getPrefix("http://example.com/prefix1"));
        assertNull(resolver.getPointer());
        assertFalse(resolver.isSealed());
    }

    @Test
    public void testSetNamespaceContextPointer() {
        NamespaceResolver resolver = new NamespaceResolver();
        Pointer pointer = new NodePointer("", null, null, null, null);
        resolver.setNamespaceContextPointer(pointer);
        assertEquals(pointer, resolver.getNamespaceContextPointer());
        assertNull(resolver.getPointer());
        assertFalse(resolver.isSealed());
    }

    @Test
    public void testGetNamespaceContextPointer() {
        NamespaceResolver resolver = new NamespaceResolver();
        Pointer pointer = new NodePointer("", null, null, null, null);
        resolver.setNamespaceContextPointer(pointer);
        assertEquals(pointer, resolver.getNamespaceContextPointer());
        assertNull(resolver.getPointer());
        assertFalse(resolver.isSealed());
    }

    @Test
    public void testGetNamespaceContextPointerWithParent() {
        NamespaceResolver parent = new NamespaceResolver();
        parent.setNamespaceContextPointer(new NodePointer("", null, null, null, null));
        NamespaceResolver resolver = new NamespaceResolver(parent);
        assertEquals(parent.getNamespaceContextPointer(), resolver.getNamespaceContextPointer());
        assertNull(resolver.getPointer());
        assertFalse(resolver.isSealed());
    }

    @Test
    public void testSeal() {
        NamespaceResolver resolver = new NamespaceResolver();
        assertFalse(resolver.isSealed());
        resolver.seal();
        assertTrue(resolver.isSealed());
        assertFalse(resolver.canRegisterNamespace());
        assertFalse(resolver.canSetNamespaceContextPointer());
    }

    @Test
    public void testClone() {
        NamespaceResolver resolver = new NamespaceResolver();
        resolver.registerNamespace("prefix1", "http://example.com/prefix1");
        NamespaceResolver cloned = (NamespaceResolver) resolver.clone();
        assertEquals("prefix1", cloned.getPrefix("http://example.com/prefix1"));
        assertFalse(cloned.isSealed());
        assertFalse(cloned.canRegisterNamespace());
        assertFalse(cloned.canSetNamespaceContextPointer());
    }
}