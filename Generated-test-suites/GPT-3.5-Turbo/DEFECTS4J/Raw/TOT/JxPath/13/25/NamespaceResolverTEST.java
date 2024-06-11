import org.junit.Test;
import static org.junit.Assert.*;

public class NamespaceResolverTest {

    // Test case for registerNamespace method
    @Test
    public void testRegisterNamespace() {
        NamespaceResolver resolver = new NamespaceResolver();
        
        // Typical use case
        resolver.registerNamespace("prefix1", "http://www.example.com/ns1");
        assertEquals("http://www.example.com/ns1", resolver.getNamespaceURI("prefix1"));
        
        // Edge case: Registering same prefix with different URI
        resolver.registerNamespace("prefix1", "http://www.example.com/ns2");
        assertEquals("http://www.example.com/ns2", resolver.getNamespaceURI("prefix1"));
        
        // Error scenario: Registering null prefix and URI
        try {
            resolver.registerNamespace(null, null);
            fail("Expected IllegalArgumentException");
        } catch (IllegalArgumentException e) {
            // Test passed
        }
        
        // Error scenario: Registering empty prefix and null URI
        try {
            resolver.registerNamespace("", null);
            fail("Expected IllegalArgumentException");
        } catch (IllegalArgumentException e) {
            // Test passed
        }
    }

    // Test case for setNamespaceContextPointer method
    @Test
    public void testSetNamespaceContextPointer() {
        NamespaceResolver resolver = new NamespaceResolver();
        NodePointer pointer = new NodePointer();
        
        // Typical use case
        resolver.setNamespaceContextPointer(pointer);
        assertEquals(pointer, resolver.getNamespaceContextPointer());
        
        // Error scenario: Setting null pointer
        resolver.setNamespaceContextPointer(null);
        assertNull(resolver.getNamespaceContextPointer());
    }

    // Test case for getNamespaceURI method
    @Test
    public void testGetNamespaceURI() {
        NamespaceResolver resolver = new NamespaceResolver();
        
        // Typical use case: Namespace registered in this resolver
        resolver.registerNamespace("prefix1", "http://www.example.com/ns1");
        assertEquals("http://www.example.com/ns1", resolver.getNamespaceURI("prefix1"));
        
        // Typical use case: Namespace registered in parent resolver
        NamespaceResolver parent = new NamespaceResolver();
        parent.registerNamespace("prefix2", "http://www.example.com/ns2");
        resolver = new NamespaceResolver(parent);
        assertEquals("http://www.example.com/ns2", resolver.getNamespaceURI("prefix2"));
        
        // Error scenario: Namespace not found in any resolver
        assertNull(resolver.getNamespaceURI("prefix3"));
        
        // Error scenario: Trying to get empty prefix URI
        assertNull(resolver.getNamespaceURI(""));
    }

    // Test case for getPrefix method
    @Test
    public void testGetPrefix() {
        NamespaceResolver resolver = new NamespaceResolver();
        
        // Typical use case: Prefix registered in this resolver
        resolver.registerNamespace("prefix1", "http://www.example.com/ns1");
        assertEquals("prefix1", resolver.getPrefix("http://www.example.com/ns1"));
        
        // Typical use case: Prefix registered in parent resolver
        NamespaceResolver parent = new NamespaceResolver();
        parent.registerNamespace("prefix2", "http://www.example.com/ns2");
        resolver = new NamespaceResolver(parent);
        assertEquals("prefix2", resolver.getPrefix("http://www.example.com/ns2"));
        
        // Error scenario: Prefix not found in any resolver
        assertNull(resolver.getPrefix("http://www.example.com/ns3"));
    }

    // Test case for isSealed method
    @Test
    public void testIsSealed() {
        NamespaceResolver resolver = new NamespaceResolver();
        
        // Typical use case: Resolver is not sealed initially
        assertFalse(resolver.isSealed());
        
        // Typical use case: Resolver becomes sealed
        resolver.seal();
        assertTrue(resolver.isSealed());
    }
    
    // Test case for clone method
    @Test
    public void testClone() {
        NamespaceResolver resolver = new NamespaceResolver();
        
        // Typical use case: Cloning an unsealed resolver
        NamespaceResolver clonedResolver = (NamespaceResolver) resolver.clone();
        assertFalse(clonedResolver.isSealed());
        
        // Typical use case: Cloning a sealed resolver
        resolver.seal();
        clonedResolver = (NamespaceResolver) resolver.clone();
        assertTrue(clonedResolver.isSealed());
    }
}