package org.apache.commons.jxpath.ri;

import static org.junit.Assert.assertEquals;

import org.junit.Test;

public class NamespaceResolverTest {
    
    @Test
    public void testRegisterNamespace() {
        NamespaceResolver resolver = new NamespaceResolver();
        resolver.registerNamespace("prefix1", "uri1");
        
        // Test if the registered namespace is returned correctly
        assertEquals("uri1", resolver.getNamespaceURI("prefix1"));
    }
    
    @Test
    public void testGetNamespaceURI() {
        NamespaceResolver resolver = new NamespaceResolver();
        resolver.registerNamespace("prefix1", "uri1");
        
        // Test if the registered namespace is returned correctly
        assertEquals("uri1", resolver.getNamespaceURI("prefix1"));
        
        // Test if null is returned for undefined prefix
        assertEquals(null, resolver.getNamespaceURI("prefix2"));
    }
    
    @Test
    public void testGetPrefix() {
        NamespaceResolver resolver = new NamespaceResolver();
        resolver.registerNamespace("prefix1", "uri1");
        
        // Test if the registered prefix is returned correctly
        assertEquals("prefix1", resolver.getPrefix("uri1"));
        
        // Test if null is returned for undefined URI
        assertEquals(null, resolver.getPrefix("uri2"));
    }
    
    @Test
    public void testSeal() {
        NamespaceResolver resolver = new NamespaceResolver();
        resolver.seal();
        
        // Test if the resolver is sealed
        assertEquals(true, resolver.isSealed());
    }
    
    @Test
    public void testClone() {
        NamespaceResolver resolver = new NamespaceResolver();
        NamespaceResolver clonedResolver = (NamespaceResolver) resolver.clone();
        
        // Test if the cloned resolver is not sealed
        assertEquals(false, clonedResolver.isSealed());
    }

}