import org.junit.Test;
import static org.junit.Assert.*;

public class NamespaceResolverTest {

    @Test
    public void testRegisterNamespace() {
        NamespaceResolver resolver = new NamespaceResolver();
        resolver.registerNamespace("prefix", "http://www.example.com");
        
        assertEquals("http://www.example.com", resolver.getNamespaceURI("prefix"));
    }
    
    @Test
    public void testGetNamespaceURI_External() {
        NamespaceResolver resolver = new NamespaceResolver();
        resolver.registerNamespace("prefix", "http://www.example.com");
        
        assertEquals("http://www.example.com", resolver.getNamespaceURI("prefix"));
    }
    
    @Test
    public void testGetNamespaceURI_NotFound() {
        NamespaceResolver resolver = new NamespaceResolver();
        
        assertNull(resolver.getNamespaceURI("nonExistent"));
    }
    
    @Test
    public void testGetPrefix() {
        NamespaceResolver resolver = new NamespaceResolver();
        resolver.registerNamespace("prefix", "http://www.example.com");
        
        assertEquals("prefix", resolver.getPrefix("http://www.example.com"));
    }
    
    @Test
    public void testGetPrefix_External() {
        NamespaceResolver resolver = new NamespaceResolver();
        resolver.registerNamespace("prefix", "http://www.example.com");
        
        assertEquals("prefix", resolver.getPrefix("http://www.example.com"));
    }
    
    @Test
    public void testGetPrefix_NotFound() {
        NamespaceResolver resolver = new NamespaceResolver();
        
        assertNull(resolver.getPrefix("http://www.nonexistent.com"));
    }
    
    @Test
    public void testSeal() {
        NamespaceResolver resolver = new NamespaceResolver();
        resolver.seal();
        
        assertTrue(resolver.isSealed());
    }

}