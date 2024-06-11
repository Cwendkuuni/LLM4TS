package org.apache.commons.jxpath.ri;

import org.junit.Test;

import static org.junit.Assert.*;

public class NamespaceResolverTest {

    @Test
    public void testRegisterNamespace() {
        NamespaceResolver resolver = new NamespaceResolver();
        resolver.registerNamespace("prefix", "uri");
        assertEquals("uri", resolver.getNamespaceURI("prefix"));
    }
    
    @Test
    public void testSetNamespaceContextPointer() {
        NamespaceResolver resolver = new NamespaceResolver();
        assertNull(resolver.getNamespaceContextPointer());
        resolver.setNamespaceContextPointer(null);
        assertNull(resolver.getNamespaceContextPointer());
    }
    
    @Test
    public void testGetNamespaceURI() {
        NamespaceResolver resolver = new NamespaceResolver();
        assertNull(resolver.getNamespaceURI("prefix"));
    }
    
    @Test
    public void testGetPrefix() {
        NamespaceResolver resolver = new NamespaceResolver();
        assertNull(resolver.getPrefix("uri"));
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
        Object clone = resolver.clone();
        assertNotNull(clone);
        assertFalse(resolver == clone);
    }
}