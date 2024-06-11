package org.apache.commons.jxpath.ri;

import org.junit.Before;
import org.junit.Test;
import static org.junit.Assert.*;

public class NamespaceResolverTest {
    
    private NamespaceResolver resolver;
    
    @Before
    public void setUp() {
        resolver = new NamespaceResolver();
    }
    
    @Test
    public void testRegisterNamespace() {
        resolver.registerNamespace("prefix", "http://www.example.com");
        
        String actualUri = resolver.getNamespaceURI("prefix");
        assertEquals("http://www.example.com", actualUri);
    }
    
    @Test(expected = IllegalStateException.class)
    public void testRegisterNamespaceOnSealedResolver() {
        resolver.seal();
        resolver.registerNamespace("prefix", "http://www.example.com");
    }
    
    @Test
    public void testGetNamespaceURI() {
        resolver.registerNamespace("prefix", "http://www.example.com");
        
        String actualUri = resolver.getNamespaceURI("prefix");
        assertEquals("http://www.example.com", actualUri);
    }
    
    @Test
    public void testGetNamespaceURIWithNullPointer() {
        resolver.setNamespaceContextPointer(null);
        resolver.registerNamespace("prefix", "http://www.example.com");
        
        String actualUri = resolver.getNamespaceURI("prefix");
        assertEquals("http://www.example.com", actualUri);
    }
    
    @Test
    public void testGetNamespaceURINotFound() {
        String actualUri = resolver.getNamespaceURI("prefix");
        assertNull(actualUri);
    }
    
    @Test
    public void testGetNamespaceURINotFoundWithNullPointer() {
        resolver.setNamespaceContextPointer(null);
        
        String actualUri = resolver.getNamespaceURI("prefix");
        assertNull(actualUri);
    }
    
    @Test
    public void testGetPrefix() {
        resolver.registerNamespace("prefix", "http://www.example.com");
        
        String actualPrefix = resolver.getPrefix("http://www.example.com");
        assertEquals("prefix", actualPrefix);
    }
    
    @Test
    public void testGetPrefixNotFound() {
        String actualPrefix = resolver.getPrefix("http://www.example.com");
        assertNull(actualPrefix);
    }
    
    @Test
    public void testGetPrefixWithNullPointer() {
        resolver.setNamespaceContextPointer(null);
        resolver.registerNamespace("prefix", "http://www.example.com");
        
        String actualPrefix = resolver.getPrefix("http://www.example.com");
        assertEquals("prefix", actualPrefix);
    }
    
    @Test
    public void testGetPrefixWithNullPointerAndNotFound() {
        resolver.setNamespaceContextPointer(null);
        
        String actualPrefix = resolver.getPrefix("http://www.example.com");
        assertNull(actualPrefix);
    }
    
    @Test
    public void testIsSealed() {
        boolean actualSealed = resolver.isSealed();
        assertFalse(actualSealed);
    }
    
    @Test
    public void testSeal() {
        resolver.seal();
        
        boolean actualSealed = resolver.isSealed();
        assertTrue(actualSealed);
    }
    
    @Test
    public void testClone() {
        NamespaceResolver cloned = (NamespaceResolver) resolver.clone();
        
        boolean actualSealed = cloned.isSealed();
        assertFalse(actualSealed);
    }
}
