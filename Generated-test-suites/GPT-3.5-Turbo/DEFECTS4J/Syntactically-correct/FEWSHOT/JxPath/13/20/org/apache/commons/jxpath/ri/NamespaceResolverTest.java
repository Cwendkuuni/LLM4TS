package org.apache.commons.jxpath.ri;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertSame;

import org.apache.commons.jxpath.Pointer;
import org.apache.commons.jxpath.ri.model.NodeIterator;
import org.apache.commons.jxpath.ri.model.NodePointer;
import org.junit.Before;
import org.junit.Test;

public class NamespaceResolverTest {
    private NamespaceResolver resolver;

    @Before
    public void setUp() {
        resolver = new NamespaceResolver();
    }

    @Test
    public void testRegisterNamespace() {
        assertNull(resolver.getNamespaceURI("prefix"));
        resolver.registerNamespace("prefix", "http://www.example.com");
        assertEquals("http://www.example.com", resolver.getNamespaceURI("prefix"));
        resolver.registerNamespace("prefix", "http://www.example.org");
        assertEquals("http://www.example.org", resolver.getNamespaceURI("prefix"));
    }

    @Test(expected = IllegalStateException.class)
    public void testRegisterNamespaceSealedResolver() {
        resolver.seal();
        resolver.registerNamespace("prefix", "http://www.example.com");
    }
    
    @Test
    public void testSetNamespaceContextPointer() {
        assertNull(resolver.getNamespaceContextPointer());
        NodePointer pointer = new NodePointer();
        resolver.setNamespaceContextPointer(pointer);
        assertSame(pointer, resolver.getNamespaceContextPointer());
    }

    @Test
    public void testGetNamespaceURI() {
        assertNull(resolver.getNamespaceURI("prefix"));
        resolver.registerNamespace("prefix", "http://www.example.com");
        assertEquals("http://www.example.com", resolver.getNamespaceURI("prefix"));
        resolver.registerNamespace("prefix", "http://www.example.org");
        assertEquals("http://www.example.org", resolver.getNamespaceURI("prefix"));
        resolver.setNamespaceContextPointer(new NodePointer());
        assertEquals(null, resolver.getNamespaceURI("prefix"));
    }

    @Test
    public void testGetPrefix() {
        assertNull(resolver.getPrefix("http://www.example.com"));
        resolver.registerNamespace("prefix", "http://www.example.com");
        assertEquals("prefix", resolver.getPrefix("http://www.example.com"));
        resolver.registerNamespace("prefix", "http://www.example.org");
        assertNull(resolver.getPrefix("http://www.example.com"));
    }

    @Test
    public void testIsSealed() {
        assertEquals(false, resolver.isSealed());
        resolver.seal();
        assertEquals(true, resolver.isSealed());
    }

    @Test
    public void testSeal() {
        assertEquals(false, resolver.isSealed());
        resolver.seal();
        assertEquals(true, resolver.isSealed());
    }

    @Test
    public void testClone() {
        NamespaceResolver cloned = (NamespaceResolver) resolver.clone();
        assertEquals(false, cloned.isSealed());
    }
}