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
        String namespaceURI = resolver.getNamespaceURI("prefix");
        assertEquals("http://www.example.com", namespaceURI);
    }
    
    @Test
    public void testRegisterNamespace_SealedResolver() {
        // Seal the resolver
        resolver.seal();
        
        // Attempt to register a namespace should throw an exception
        try {
            resolver.registerNamespace("prefix", "http://www.example.com");
            fail("Should have thrown an IllegalStateException");
        } catch (IllegalStateException e) {
            // Exception expected
        }
    }
    
    @Test
    public void testGetNamespaceURI_RegisteredNamespace() {
        // Register a namespace
        resolver.registerNamespace("prefix", "http://www.example.com");
        
        String namespaceURI = resolver.getNamespaceURI("prefix");
        assertEquals("http://www.example.com", namespaceURI);
    }
    
    @Test
    public void testGetNamespaceURI_ContextPointerDefinedNamespace() {
        NodePointer pointer = new NodePointer();
        pointer.setNamespaceURI("http://www.example.com");
        resolver.setNamespaceContextPointer(pointer);
        
        String namespaceURI = resolver.getNamespaceURI("prefix");
        assertEquals("http://www.example.com", namespaceURI);
    }
    
    @Test
    public void testGetNamespaceURI_ParentDefinedNamespace() {
        // Create a parent resolver
        NamespaceResolver parentResolver = new NamespaceResolver();
        parentResolver.registerNamespace("prefix", "http://www.example.com");
        
        // Set the parent resolver for the current resolver
        resolver = new NamespaceResolver(parentResolver);
        
        String namespaceURI = resolver.getNamespaceURI("prefix");
        assertEquals("http://www.example.com", namespaceURI);
    }
    
    @Test
    public void testGetNamespaceURI_UndefinedPrefix() {
        String namespaceURI = resolver.getNamespaceURI("prefix");
        assertNull(namespaceURI);
    }
    
    @Test
    public void testGetPrefix_RegisteredPrefix() {
        // Register a namespace
        resolver.registerNamespace("prefix", "http://www.example.com");
        
        String prefix = resolver.getPrefix("http://www.example.com");
        assertEquals("prefix", prefix);
    }
    
    @Test
    public void testGetPrefix_ContextPointerDefinedPrefix() {
        NodePointer pointer = new NodePointer();
        pointer.setName(new Name("prefix"));
        pointer.setNamespaceURI("http://www.example.com");
        resolver.setNamespaceContextPointer(pointer);
        
        String prefix = resolver.getPrefix("http://www.example.com");
        assertEquals("prefix", prefix);
    }
    
    @Test
    public void testGetPrefix_ParentDefinedPrefix() {
        // Create a parent resolver
        NamespaceResolver parentResolver = new NamespaceResolver();
        parentResolver.registerNamespace("prefix", "http://www.example.com");
        
        // Set the parent resolver for the current resolver
        resolver = new NamespaceResolver(parentResolver);
        
        String prefix = resolver.getPrefix("http://www.example.com");
        assertEquals("prefix", prefix);
    }
    
    @Test
    public void testGetPrefix_UndefinedNamespaceURI() {
        String prefix = resolver.getPrefix("http://www.example.com");
        assertNull(prefix);
    }
}