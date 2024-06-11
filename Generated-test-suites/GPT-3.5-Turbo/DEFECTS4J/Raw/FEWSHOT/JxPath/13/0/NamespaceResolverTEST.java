package org.apache.commons.jxpath.ri;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertTrue;

import org.apache.commons.jxpath.Pointer;
import org.apache.commons.jxpath.ri.model.NodeIterator;
import org.apache.commons.jxpath.ri.model.NodePointer;
import org.junit.Before;
import org.junit.Test;

public class NamespaceResolverTest {
    private NamespaceResolver resolver;

    @Before
    public void setup() {
        resolver = new NamespaceResolver();
    }

    @Test
    public void testRegisterNamespace() {
        resolver.registerNamespace("prefix1", "http://www.example1.com");
        resolver.registerNamespace("prefix2", "http://www.example2.com");

        assertEquals("http://www.example1.com", resolver.getNamespaceURI("prefix1"));
        assertEquals("http://www.example2.com", resolver.getNamespaceURI("prefix2"));
    }

    @Test(expected = IllegalStateException.class)
    public void testRegisterNamespaceSealed() {
        resolver.seal();
        resolver.registerNamespace("prefix1", "http://www.example1.com");
    }

    @Test
    public void testSetNamespaceContextPointer() {
        NodePointer pointer = new NodePointer();
        resolver.setNamespaceContextPointer(pointer);

        assertEquals(pointer, resolver.getNamespaceContextPointer());
    }

    @Test
    public void testSetNamespaceContextPointerWithParent() {
        NamespaceResolver parent = new NamespaceResolver();
        NodePointer pointer = new NodePointer();
        parent.setNamespaceContextPointer(pointer);
        resolver = new NamespaceResolver(parent);

        assertEquals(pointer, resolver.getNamespaceContextPointer());
    }

    @Test
    public void testGetNamespaceURI() {
        resolver.registerNamespace("prefix1", "http://www.example1.com");
        resolver.registerNamespace("prefix2", "http://www.example2.com");

        assertEquals("http://www.example1.com", resolver.getNamespaceURI("prefix1"));
        assertEquals("http://www.example2.com", resolver.getNamespaceURI("prefix2"));
        assertNull(resolver.getNamespaceURI("prefix3"));
    }


    @Test
    public void testGetNamespaceURIWithParent() {
        NamespaceResolver parent = new NamespaceResolver();
        parent.registerNamespace("prefix1", "http://www.example1.com");
        parent.registerNamespace("prefix2", "http://www.example2.com");

        resolver = new NamespaceResolver(parent);

        assertEquals("http://www.example1.com", resolver.getNamespaceURI("prefix1"));
        assertEquals("http://www.example2.com", resolver.getNamespaceURI("prefix2"));
        assertNull(resolver.getNamespaceURI("prefix3"));
    }

    @Test
    public void testGetPrefix() {
        resolver.registerNamespace("prefix1", "http://www.example1.com");
        resolver.registerNamespace("prefix2", "http://www.example2.com");

        assertEquals("prefix1", resolver.getPrefix("http://www.example1.com"));
        assertEquals("prefix2", resolver.getPrefix("http://www.example2.com"));
        assertNull(resolver.getPrefix("http://www.example3.com"));
    }

    @Test
    public void testGetPrefixWithParent() {
        NamespaceResolver parent = new NamespaceResolver();
        parent.registerNamespace("prefix1", "http://www.example1.com");
        parent.registerNamespace("prefix2", "http://www.example2.com");

        resolver = new NamespaceResolver(parent);

        assertEquals("prefix1", resolver.getPrefix("http://www.example1.com"));
        assertEquals("prefix2", resolver.getPrefix("http://www.example2.com"));
        assertNull(resolver.getPrefix("http://www.example3.com"));
    }

    @Test
    public void testIsSealed() {
        assertFalse(resolver.isSealed());
    }

    @Test
    public void testSeal() {
        resolver.seal();

        assertTrue(resolver.isSealed());
    }

    @Test
    public void testClone() {
        NamespaceResolver clonedResolver = (NamespaceResolver) resolver.clone();

        assertEquals(resolver.isSealed(), clonedResolver.isSealed());
    }
}