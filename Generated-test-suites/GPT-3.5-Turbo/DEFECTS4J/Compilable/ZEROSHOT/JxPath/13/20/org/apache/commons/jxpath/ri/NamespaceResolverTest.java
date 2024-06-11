package org.apache.commons.jxpath.ri;
import static org.junit.Assert.*;
import org.junit.Test;

public class NamespaceResolverTest {

    @Test
    public void testRegisterNamespace() {
        NamespaceResolver resolver = new NamespaceResolver();
        resolver.registerNamespace("prefix", "namespaceURI");
        assertEquals("namespaceURI", resolver.getNamespaceURI("prefix"));
    }
    
    @Test
    public void testGetNamespaceURI() {
        NamespaceResolver resolver = new NamespaceResolver();
        resolver.registerNamespace("prefix", "namespaceURI");
        assertEquals("namespaceURI", resolver.getNamespaceURI("prefix"));
    }
    
    @Test
    public void testGetPrefix() {
        NamespaceResolver resolver = new NamespaceResolver();
        resolver.registerNamespace("prefix", "namespaceURI");
        assertEquals("prefix", resolver.getPrefix("namespaceURI"));
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
        assertEquals(resolver.isSealed(), clone.isSealed());
    }
    
}