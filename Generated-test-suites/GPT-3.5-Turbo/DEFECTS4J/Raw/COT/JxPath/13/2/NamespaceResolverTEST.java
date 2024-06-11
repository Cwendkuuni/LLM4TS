
import static org.junit.Assert.*;

import org.apache.commons.jxpath.JXPathContext;
import org.junit.Before;
import org.junit.Test;

public class NamespaceResolverTest {
    
    private NamespaceResolver nsResolver;
    
    @Before
    public void setUp() {
        nsResolver = new NamespaceResolver();
    }
    
    @Test
    public void testRegisterNamespace() {
        nsResolver.registerNamespace("prefix", "http://example.com/namespace");
        String uri = nsResolver.getNamespaceURI("prefix");
        assertEquals("http://example.com/namespace", uri);
    }
    
    @Test
    public void testSetNamespaceContextPointer() {
        // create a sample pointer
        NodePointer pointer = new NodePointer();
        nsResolver.setNamespaceContextPointer(pointer);
        Pointer contextPointer = nsResolver.getNamespaceContextPointer();
        assertEquals(pointer, contextPointer);
    }
    
    @Test
    public void testGetNamespaceURI() {
        nsResolver.registerNamespace("prefix", "http://example.com/namespace");
        String uri = nsResolver.getNamespaceURI("prefix");
        assertEquals("http://example.com/namespace", uri);
        
        uri = nsResolver.getNamespaceURI("nonexistent");
        assertNull(uri);
    }
    
    @Test
    public void testGetPrefix() {
        nsResolver.registerNamespace("prefix", "http://example.com/namespace");
        String prefix = nsResolver.getPrefix("http://example.com/namespace");
        assertEquals("prefix", prefix);
        
        prefix = nsResolver.getPrefix("http://example.com/nonexistent");
        assertNull(prefix);
    }
    
    @Test
    public void testIsSealed() {
        nsResolver.seal();
        assertTrue(nsResolver.isSealed());
    }
    
    @Test
    public void testClone() {
        NamespaceResolver clone = (NamespaceResolver) nsResolver.clone();
        assertNotNull(clone);
    }
}
