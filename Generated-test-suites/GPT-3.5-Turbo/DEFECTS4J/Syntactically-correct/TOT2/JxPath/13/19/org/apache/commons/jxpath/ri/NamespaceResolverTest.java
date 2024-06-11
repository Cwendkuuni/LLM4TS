import static org.junit.Assert.*;
import org.junit.Before;
import org.junit.Test;

public class NamespaceResolverTest {
    
    private NamespaceResolver namespaceResolver;
    
    @Before
    public void setup() {
        namespaceResolver = new NamespaceResolver();
    }
    
    @Test
    public void testRegisterNamespace() {
        namespaceResolver.registerNamespace("ns1", "http://namespace1.com");
        assertEquals("http://namespace1.com", namespaceResolver.getNamespaceURI("ns1"));
    }
    
    @Test
    public void testGetNamespaceURIWithNullPrefix() {
        assertNull(namespaceResolver.getNamespaceURI(null));
    }
    
    @Test
    public void testGetNamespaceURIWithUndefinedPrefix() {
        assertNull(namespaceResolver.getNamespaceURI("undefined"));
    }
    
    @Test
    public void testGetPrefix() {
        namespaceResolver.registerNamespace("ns2", "http://namespace2.com");
        assertEquals("ns2", namespaceResolver.getPrefix("http://namespace2.com"));
    }
    
    @Test
    public void testGetPrefixWithUndefinedNamespaceURI() {
        assertNull(namespaceResolver.getPrefix("undefined"));
    }
    
    @Test
    public void testIsSealed() {
        assertFalse(namespaceResolver.isSealed());
        namespaceResolver.seal();
        assertTrue(namespaceResolver.isSealed());
    }
    
    @Test
    public void testClone() {
        NamespaceResolver clonedResolver = (NamespaceResolver) namespaceResolver.clone();
        assertNotSame(namespaceResolver, clonedResolver);
    }
}