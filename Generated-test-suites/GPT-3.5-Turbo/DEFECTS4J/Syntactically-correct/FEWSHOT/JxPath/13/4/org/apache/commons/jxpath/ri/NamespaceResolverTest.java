import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNull;

import org.junit.Test;

public class NamespaceResolverTest {
    @Test
    public void testRegisterNamespace() {
        NamespaceResolver nsr = new NamespaceResolver();
        
        // Test registering a namespace
        nsr.registerNamespace("prefix1", "uri1");
        assertEquals("uri1", nsr.getNamespaceURI("prefix1"));
        
        // Test registering a namespace with empty prefix
        nsr.registerNamespace("", "uri2");
        assertEquals("uri2", nsr.getNamespaceURI(""));
        
        // Test registering a namespace with null prefix
        nsr.registerNamespace(null, "uri3");
        assertEquals("uri3", nsr.getNamespaceURI(null));
        
        // Test registering a duplicate namespace
        nsr.registerNamespace("prefix1", "uri4");
        assertEquals("uri4", nsr.getNamespaceURI("prefix1"));
        
        // Test registering namespaces on a sealed NamespaceResolver
        nsr.seal();
        try {
            nsr.registerNamespace("prefix2", "uri5");
        } catch (IllegalStateException e) {
            assertEquals("Cannot register namespaces on a sealed NamespaceResolver", e.getMessage());
        }
    }
    
    @Test
    public void testSetNamespaceContextPointer() {
        NamespaceResolver nsr = new NamespaceResolver();
        NodePointer pointer = new NodePointer();
        
        // Test setting the NamespaceContextPointer
        nsr.setNamespaceContextPointer(pointer);
        assertEquals(pointer, nsr.getNamespaceContextPointer());
    }
    
    @Test
    public void testGetNamespaceURI() {
        NamespaceResolver nsr = new NamespaceResolver();
        nsr.registerNamespace("prefix1", "uri1");
        nsr.registerNamespace("prefix2", "uri2");
        
        // Test getting a registered namespace URI
        assertEquals("uri1", nsr.getNamespaceURI("prefix1"));
        assertEquals("uri2", nsr.getNamespaceURI("prefix2"));
        
        // Test getting a namespace URI for an undefined prefix
        assertNull(nsr.getNamespaceURI("prefix3"));
        
        // Test getting a namespace URI with null prefix
        assertNull(nsr.getNamespaceURI(null));
        
        // Test getting a namespace URI from parent NamespaceResolver
        nsr = new NamespaceResolver(nsr);
        assertEquals("uri1", nsr.getNamespaceURI("prefix1"));
        assertEquals("uri2", nsr.getNamespaceURI("prefix2"));
    }
    
    @Test
    public void testGetPrefix() {
        NamespaceResolver nsr = new NamespaceResolver();
        nsr.registerNamespace("prefix1", "uri1");
        nsr.registerNamespace("prefix2", "uri2");
        
        // Test getting a prefix for a registered namespace URI
        assertEquals("prefix1", nsr.getPrefix("uri1"));
        assertEquals("prefix2", nsr.getPrefix("uri2"));
        
        // Test getting a prefix for an undefined namespace URI
        assertNull(nsr.getPrefix("uri3"));
        
        // Test getting a prefix from parent NamespaceResolver
        nsr = new NamespaceResolver(nsr);
        assertEquals("prefix1", nsr.getPrefix("uri1"));
        assertEquals("prefix2", nsr.getPrefix("uri2"));
    }
    
    @Test
    public void testIsSealed() {
        NamespaceResolver nsr = new NamespaceResolver();
        assertFalse(nsr.isSealed());
        
        // Test sealing the NamespaceResolver
        nsr.seal();
        assertTrue(nsr.isSealed());
    }
    
    @Test
    public void testClone() {
        NamespaceResolver nsr1 = new NamespaceResolver();
        nsr1.registerNamespace("prefix1", "uri1");
        nsr1.registerNamespace("prefix2", "uri2");
        
        // Test cloning the NamespaceResolver
        NamespaceResolver nsr2 = (NamespaceResolver) nsr1.clone();
        assertEquals("uri1", nsr2.getNamespaceURI("prefix1"));
        assertEquals("uri2", nsr2.getNamespaceURI("prefix2"));
    }
}