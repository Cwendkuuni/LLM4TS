import static org.junit.Assert.*;

import org.junit.Test;

public class NamespaceResolverTest {

    @Test
    public void testRegisterNamespace() {
        NamespaceResolver resolver = new NamespaceResolver();
        resolver.registerNamespace("prefix1", "namespace1");
        assertEquals("namespace1", resolver.getNamespaceURI("prefix1"));
    }
    
    @Test(expected = IllegalStateException.class)
    public void testRegisterNamespaceSealed() {
        NamespaceResolver resolver = new NamespaceResolver();
        resolver.seal();
        resolver.registerNamespace("prefix1", "namespace1");
    }
    
    @Test
    public void testGetNamespaceURI() {
        NamespaceResolver resolver = new NamespaceResolver();
        resolver.registerNamespace("prefix1", "namespace1");
        resolver.registerNamespace("prefix2", "namespace2");
        
        assertEquals("namespace1", resolver.getNamespaceURI("prefix1"));
        assertEquals("namespace2", resolver.getNamespaceURI("prefix2"));
        assertNull(resolver.getNamespaceURI("prefix3"));
    }

    @Test
    public void testGetPrefix() {
        NamespaceResolver resolver = new NamespaceResolver();
        resolver.registerNamespace("prefix1", "namespace1");
        resolver.registerNamespace("prefix2", "namespace2");
        
        assertEquals("prefix1", resolver.getPrefix("namespace1"));
        assertEquals("prefix2", resolver.getPrefix("namespace2"));
        assertNull(resolver.getPrefix("namespace3"));
    }
    
    @Test
    public void testGetPrefixWithParent() {
        NamespaceResolver parentResolver = new NamespaceResolver();
        parentResolver.registerNamespace("prefix1", "namespace1");
        parentResolver.registerNamespace("prefix2", "namespace2");
        
        NamespaceResolver resolver = new NamespaceResolver(parentResolver);
        assertNull(resolver.getPrefix("namespace1"));
        assertNull(resolver.getPrefix("namespace2"));
        assertNull(resolver.getPrefix("namespace3"));
        
        resolver.registerNamespace("prefix3", "namespace3");
        assertEquals("prefix3", resolver.getPrefix("namespace3"));
    }
    
    @Test
    public void testClone() {
        NamespaceResolver resolver = new NamespaceResolver();
        resolver.registerNamespace("prefix1", "namespace1");
        
        NamespaceResolver clonedResolver = (NamespaceResolver) resolver.clone();
        assertEquals("namespace1", clonedResolver.getNamespaceURI("prefix1"));
    }

}