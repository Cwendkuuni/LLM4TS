package org.apache.commons.jxpath.ri;
import org.junit.Before;
import org.junit.Test;
import static org.junit.Assert.*;

public class NamespaceResolverTest {
    
    private NamespaceResolver namespaceResolver;
    
    @Before
    public void setup() {
        namespaceResolver = new NamespaceResolver();
    }
    
    @Test
    public void testRegisterNamespace() {
        namespaceResolver.registerNamespace("prefix1", "http://example1.com");
        assertEquals("http://example1.com", namespaceResolver.getNamespaceURI("prefix1"));
    }
    
    @Test(expected = IllegalStateException.class)
    public void testSealedRegisterNamespace() {
        namespaceResolver.seal();
        namespaceResolver.registerNamespace("prefix2", "http://example2.com");
    }
    
    @Test
    public void testGetNamespaceURIWithNullPrefix() {
        namespaceResolver.registerNamespace("prefix3", "http://example3.com");
        assertNull(namespaceResolver.getNamespaceURI(null));
    }
    
    @Test
    public void testGetNamespaceURIWithUndefinedPrefix() {
        assertNull(namespaceResolver.getNamespaceURI("prefix4"));
    }
    
    @Test
    public void testGetNamespaceURIWithParentResolver() {
        NamespaceResolver parentResolver = new NamespaceResolver();
        parentResolver.registerNamespace("parent", "http://example-parent.com");
        namespaceResolver = new NamespaceResolver(parentResolver);
        assertEquals("http://example-parent.com", namespaceResolver.getNamespaceURI("parent"));
    }
    
    @Test
    public void testGetPrefix() {
        namespaceResolver.registerNamespace("prefix5", "http://example5.com");
        assertEquals("prefix5", namespaceResolver.getPrefix("http://example5.com"));
    }
    
    @Test
    public void testGetPrefixWithUndefinedNamespace() {
        assertNull(namespaceResolver.getPrefix("http://example6.com"));
    }
    
    @Test
    public void testGetPrefixWithParentResolver() {
        NamespaceResolver parentResolver = new NamespaceResolver();
        parentResolver.registerNamespace("parent", "http://example-parent.com");
        namespaceResolver = new NamespaceResolver(parentResolver);
        assertEquals("parent", namespaceResolver.getPrefix("http://example-parent.com"));
    }
    
    @Test
    public void testIsSealed() {
        assertFalse(namespaceResolver.isSealed());
        namespaceResolver.seal();
        assertTrue(namespaceResolver.isSealed());
    }
    
    @Test
    public void testClone() {
        namespaceResolver.registerNamespace("prefix7", "http://example7.com");
        NamespaceResolver cloneResolver = (NamespaceResolver) namespaceResolver.clone();
        
        assertEquals("http://example7.com", cloneResolver.getNamespaceURI("prefix7"));
        assertFalse(cloneResolver.isSealed());
    }
}