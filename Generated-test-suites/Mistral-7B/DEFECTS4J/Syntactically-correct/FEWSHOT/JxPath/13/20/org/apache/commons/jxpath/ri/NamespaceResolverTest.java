package org.apache.commons.jxpath.ri;

import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertEquals;

import org.apache.commons.cli.util.NamespaceResolver;
import org.apache.commons.jxpath.Pointer;
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
        NamespaceResolver childResolver = new NamespaceResolver(parentResolver);
        assertEquals(parentResolver, childResolver.getParent());
        assertNull(childResolver.getNamespaceMap());
        assertNull(childResolver.getReverseMap());
        assertNull(childResolver.getPointer());
        assertFalse(childResolver.isSealed());
    }

    @Test
    public void testRegisterNamespace() {
        NamespaceResolver resolver = new NamespaceResolver();
        resolver.registerNamespace("prefix1", "http://example.com/namespace1");
        assertEquals("http://example.com/namespace1", resolver.getNamespaceURI("prefix1"));
        assertEquals("prefix1", resolver.getPrefix("http://example.com/namespace1"));
        assertNull(resolver.getNamespaceURI("prefix2"));
        assertNull(resolver.getPrefix("http://example.com/namespace2"));

        NamespaceResolver parentResolver = new NamespaceResolver();
        NamespaceResolver resolverWithParent = new NamespaceResolver(parentResolver);
        resolverWithParent.registerNamespace("prefix1", "http://example.com/namespace1");
        assertEquals("http://example.com/namespace1", resolverWithParent.getNamespaceURI("prefix1"));
        assertEquals("prefix1", resolverWithParent.getPrefix("http://example.com/namespace1"));
        assertEquals("prefix1", parentResolver.getPrefix("http://example.com/namespace1"));
        assertEquals("http://example.com/namespace1", parentResolver.getNamespaceURI("prefix1"));
        assertNull(resolverWithParent.getNamespaceURI("prefix2"));
        assertNull(resolverWithParent.getPrefix("http://example.com/namespace2"));
        assertNull(parentResolver.getNamespaceURI("prefix2"));
        assertNull(parentResolver.getPrefix("http://example.com/namespace2"));
    }

    @Test(expected = IllegalStateException.class)
    public void testRegisterNamespaceOnSealedResolver() {
        NamespaceResolver resolver = new NamespaceResolver();
        resolver.registerNamespace("prefix1", "http://example.com/namespace1");
        resolver.seal();
        resolver.registerNamespace("prefix2", "http://example.com/namespace2");
    }

    @Test
    public void testSetNamespaceContextPointer() {
        NamespaceResolver resolver = new NamespaceResolver();
        NodePointer pointer = new NodePointer("node");
        resolver.setNamespaceContextPointer(pointer);
        assertEquals(pointer, resolver.getNamespaceContextPointer());
        assertNull(resolver.getParent().getNamespaceContextPointer());
    }

    @Test
    public void testGetNamespaceContextPointer() {
        NamespaceResolver resolver = new NamespaceResolver();
        NodePointer pointer1 = new NodePointer("node1");
        NodePointer pointer2 = new NodePointer("node2");
        resolver.setNamespaceContextPointer(pointer1);
        assertEquals(pointer1, resolver.getNamespaceContextPointer());
        assertEquals(pointer1, resolver.getParent().getNamespaceContextPointer());
        resolver.setNamespaceContextPointer(pointer2);
        assertEquals(pointer2, resolver.getNamespaceContextPointer());
        assertNull(pointer1);
    }

    @Test
    public void testGetNamespaceURI() {
        NamespaceResolver resolver = new NamespaceResolver();
        assertEquals(null, resolver.getNamespaceURI("prefix1"));
        assertEquals(null, resolver.getPrefix("http://example.com/namespace1"));

        resolver.registerNamespace("prefix1", "http://example.com/namespace1");
        assertEquals("http://example.com/namespace1", resolver.getNamespaceURI("prefix1"));
        assertEquals("prefix1", resolver.getPrefix("http://example.com/namespace1"));
        assertEquals(null, resolver.getNamespaceURI("prefix2"));
        assertEquals(null, resolver.getPrefix("http://example.com/namespace2"));

        NodePointer pointer = new NodePointer("node");
        resolver.setNamespaceContextPointer(pointer);
        assertEquals(pointer.getNamespaceURI("prefix1"), resolver.getNamespaceURI("prefix1"));
        assertEquals(pointer.getNamespaceURI("prefix2"), resolver.getNamespaceURI("prefix2"));

        NamespaceResolver parentResolver = new NamespaceResolver();
        NamespaceResolver resolverWithParent = new NamespaceResolver(parentResolver);
        resolverWithParent.registerNamespace("prefix1", "http://example.com/namespace1");
        assertEquals(parentResolver.getNamespaceURI("prefix1"), resolverWithParent.getNamespaceURI("prefix1"));
        assertEquals("prefix1", resolverWithParent.getPrefix("http://example.com/namespace1"));
        assertEquals("prefix1", parentResolver.getPrefix("http://example.com/namespace1"));
    }

    @Test
    public void testGetPrefix() {
        NamespaceResolver resolver = new NamespaceResolver();
        assertEquals(null, resolver.getNamespaceURI("prefix1"));
        assertEquals(null, resolver.getPrefix("http://example.com/namespace1"));

        resolver.registerNamespace("prefix1", "http://example.com/namespace1");
        assertEquals("prefix1", resolver.getPrefix("http://example.com/namespace1"));
        assertEquals("http://example.com/namespace1", resolver.getNamespaceURI("prefix1"));
        assertEquals(null, resolver.getNamespaceURI("prefix2"));
        assertEquals(null, resolver.getPrefix("http://example.com/namespace2"));

        NodePointer pointer = new NodePointer("node");
        resolver.setNamespaceContextPointer(pointer);
        assertEquals(pointer.getNamespaceURI("prefix1"), resolver.getNamespaceURI("prefix1"));
        assertEquals(pointer.getNamespaceURI("prefix2"), resolver.getNamespaceURI("prefix2"));

        NamespaceResolver parentResolver = new NamespaceResolver();
        NamespaceResolver resolverWithParent = new NamespaceResolver(parentResolver);
        resolverWithParent.registerNamespace("prefix1", "http://example.com/namespace1");
        assertEquals(parentResolver.getPrefix("prefix1"), resolverWithParent.getPrefix("prefix1"));
        assertEquals("prefix1", resolverWithParent.getPrefix("http://example.com/namespace1"));
        assertEquals("prefix1", parentResolver.getPrefix("http://example.com/namespace1"));
    }

    @Test
    public void testIsSealed() {
        NamespaceResolver resolver = new NamespaceResolver();
        assertFalse(resolver.isSealed());

        resolver.registerNamespace("prefix1", "http://example.com/namespace1");
        assertFalse(resolver.isSealed());

        resolver.seal();
        assertTrue(resolver.isSealed());

        NamespaceResolver parentResolver = new NamespaceResolver();
        NamespaceResolver resolverWithParent = new NamespaceResolver(parentResolver);
        assertFalse(resolverWithParent.isSealed());

        resolverWithParent.registerNamespace("prefix1", "http://example.com/namespace1");
        assertFalse(resolverWithParent.isSealed());

        resolverWithParent.seal();
        assertTrue(resolverWithParent.isSealed());

        assertTrue(parentResolver.isSealed());
    }

    @Test
    public void testClone() {
        NamespaceResolver resolver = new NamespaceResolver();
        resolver.registerNamespace("prefix1", "http://example.com/namespace1");
        assertEquals(resolver.getNamespaceURI("prefix1"), resolver.clone().getNamespaceURI("prefix1"));
        assertFalse(resolver.clone().isSealed());

        resolver.seal();
        assertFalse(resolver.clone().isSealed());

        NamespaceResolver parentResolver = new NamespaceResolver();
        NamespaceResolver resolverWithParent = new NamespaceResolver(parentResolver);
        resolverWithParent.registerNamespace("prefix1", "http://example.com/namespace1");
        assertEquals(resolverWithParent.getNamespaceURI("prefix1"), resolverWithParent.clone().getNamespaceURI("prefix1"));
        assertFalse(resolverWithParent.clone().isSealed());

        resolverWithParent.seal();
        assertTrue(resolverWithParent.clone().isSealed());

        assertTrue(parentResolver.isSealed());
    }

}