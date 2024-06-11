package org.apache.commons.jxpath.ri;
import static org.junit.Assert.*;
import org.junit.Test;

public class NamespaceResolverTest {

    @Test
    public void testRegisterNamespace() {
        NamespaceResolver resolver = new NamespaceResolver();
        
        resolver.registerNamespace("test", "http://example.com");
        assertEquals("http://example.com", resolver.getNamespaceURI("test"));
    }
    
    @Test
    public void testGetNamespaceURI() {
        NamespaceResolver resolver = new NamespaceResolver();
        
        resolver.registerNamespace("test", "http://example.com");
        assertEquals("http://example.com", resolver.getNamespaceURI("test"));
        assertNull(resolver.getNamespaceURI("unknown"));
    }
    
    @Test
    public void testGetPrefix() {
        NamespaceResolver resolver = new NamespaceResolver();
        
        resolver.registerNamespace("test", "http://example.com");
        assertEquals("test", resolver.getPrefix("http://example.com"));
        assertNull(resolver.getPrefix("http://unknown.com"));
    }
    
    @Test
    public void testSeal() {
        NamespaceResolver resolver = new NamespaceResolver();
        
        assertFalse(resolver.isSealed());
        resolver.seal();
        assertTrue(resolver.isSealed());
    }

    // Add more test cases for other methods as needed
    
}