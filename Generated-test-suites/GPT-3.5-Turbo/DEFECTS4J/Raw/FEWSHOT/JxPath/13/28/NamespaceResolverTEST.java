package org.apache.commons.jxpath.ri;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNull;

import org.apache.commons.jxpath.Pointer;
import org.apache.commons.jxpath.ri.model.NodeIterator;
import org.apache.commons.jxpath.ri.model.NodePointer;
import org.junit.Test;

public class NamespaceResolverTest {
    @Test
    public void testRegisterNamespace() {
        NamespaceResolver resolver = new NamespaceResolver();
        resolver.registerNamespace("html", "http://www.w3.org/1999/xhtml");
        assertEquals("http://www.w3.org/1999/xhtml", resolver.getNamespaceURI("html"));
    }
    
    @Test(expected = IllegalStateException.class)
    public void testSealedNamespaceResolverRegisterNamespace() {
        NamespaceResolver resolver = new NamespaceResolver();
        resolver.seal();
        resolver.registerNamespace("html", "http://www.w3.org/1999/xhtml");
    }
    
    @Test
    public void testSetNamespaceContextPointer() {
        NamespaceResolver resolver = new NamespaceResolver();
        NodePointer pointer = new NodePointer();
        resolver.setNamespaceContextPointer(pointer);
        assertEquals(pointer, resolver.getNamespaceContextPointer());
    }
    
    @Test
    public void testGetNamespaceURI() {
        NamespaceResolver resolver = new NamespaceResolver();
        resolver.registerNamespace("html", "http://www.w3.org/1999/xhtml");
        assertEquals("http://www.w3.org/1999/xhtml", resolver.getNamespaceURI("html"));
        assertNull(resolver.getNamespaceURI("svg"));
    }
    
    @Test
    public void testGetPrefix() {
        NamespaceResolver resolver = new NamespaceResolver();
        resolver.registerNamespace("html", "http://www.w3.org/1999/xhtml");
        assertEquals("html", resolver.getPrefix("http://www.w3.org/1999/xhtml"));
        assertNull(resolver.getPrefix("http://www.w3.org/2000/svg"));
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
        NamespaceResolver resolver = new NamespaceResolver();
        NamespaceResolver clone = (NamespaceResolver) resolver.clone();
        assertFalse(clone.isSealed());
    }
}