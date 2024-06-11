package org.apache.commons.jxpath.ri;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNull;

import org.junit.Test;

public class NamespaceResolverTest {

    @Test
    public void testRegisterNamespace() {
        NamespaceResolver resolver = new NamespaceResolver();
        resolver.registerNamespace("prefix1", "http://www.example1.com");
        resolver.registerNamespace("prefix2", "http://www.example2.com");
        
        assertEquals("http://www.example1.com", resolver.getNamespaceURI("prefix1"));
        assertEquals("http://www.example2.com", resolver.getNamespaceURI("prefix2"));
        assertNull(resolver.getNamespaceURI("prefix3"));
    }

    @Test
    public void testSetNamespaceContextPointer() {
        NamespaceResolver parentResolver = new NamespaceResolver();
        NamespaceResolver resolver = new NamespaceResolver(parentResolver);
        
        resolver.setNamespaceContextPointer(null);
        
        assertNull(resolver.getNamespaceContextPointer());
    }
    
    @Test
    public void testGetNamespaceURI() {
        NamespaceResolver resolver = new NamespaceResolver();
        resolver.registerNamespace("prefix1", "http://www.example1.com");
        resolver.registerNamespace("prefix2", "http://www.example2.com");
        
        assertEquals("http://www.example1.com", resolver.getNamespaceURI("prefix1"));
        assertEquals("http://www.example2.com", resolver.getNamespaceURI("prefix2"));
        assertNull(resolver.getNamespaceURI("prefix3"));
    }

    @Test
    public void testGetPrefix() {
        NamespaceResolver resolver = new NamespaceResolver();
        resolver.registerNamespace("prefix1", "http://www.example1.com");
        resolver.registerNamespace("prefix2", "http://www.example2.com");
        
        assertEquals("prefix1", resolver.getPrefix("http://www.example1.com"));
        assertEquals("prefix2", resolver.getPrefix("http://www.example2.com"));
        assertNull(resolver.getPrefix("http://www.example3.com"));
    }

    @Test
    public void testIsSealed() {
        NamespaceResolver parentResolver = new NamespaceResolver();
        NamespaceResolver resolver = new NamespaceResolver(parentResolver);
        
        assertFalse(resolver.isSealed());
        assertFalse(parentResolver.isSealed());
        
        resolver.seal();
        
        assertTrue(resolver.isSealed());
        assertTrue(parentResolver.isSealed());
    }

    @Test
    public void testClone() {
        NamespaceResolver parentResolver = new NamespaceResolver();
        parentResolver.registerNamespace("prefix1", "http://www.example1.com");
        parentResolver.registerNamespace("prefix2", "http://www.example2.com");
        
        NamespaceResolver resolver = new NamespaceResolver(parentResolver);
        resolver.registerNamespace("prefix3", "http://www.example3.com");
        
        NamespaceResolver clonedResolver = (NamespaceResolver)resolver.clone();
        
        assertEquals(resolver.getNamespaceURI("prefix1"), clonedResolver.getNamespaceURI("prefix1"));
        assertEquals(resolver.getNamespaceURI("prefix2"), clonedResolver.getNamespaceURI("prefix2"));
        assertEquals(resolver.getNamespaceURI("prefix3"), clonedResolver.getNamespaceURI("prefix3"));
    }
}