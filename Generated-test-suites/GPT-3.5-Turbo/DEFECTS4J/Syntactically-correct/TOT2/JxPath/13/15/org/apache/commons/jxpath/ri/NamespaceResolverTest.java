import static org.junit.Assert.*;

import org.junit.Before;
import org.junit.Test;

public class NamespaceResolverTest {

    private NamespaceResolver namespaceResolver;
    
    @Before
    public void setUp() {
        namespaceResolver = new NamespaceResolver();
    }
    
    @Test
    public void testRegisterNamespace() {
        namespaceResolver.registerNamespace("test", "http://test.com");
        assertEquals("http://test.com", namespaceResolver.getNamespaceURI("test"));
    }
    
    @Test
    public void testGetNamespaceURI_whenPrefixNotDefined() {
        assertNull(namespaceResolver.getNamespaceURI("undefined"));
    }

    @Test
    public void testGetPrefix() {
        namespaceResolver.registerNamespace("test", "http://test.com");
        assertEquals("test", namespaceResolver.getPrefix("http://test.com"));
    }
    
    @Test
    public void testGetPrefix_whenNamespaceURINotFound() {
        assertNull(namespaceResolver.getPrefix("http://undefined.com"));
    }
    
    @Test
    public void testSeal() {
        namespaceResolver.seal();
        assertTrue(namespaceResolver.isSealed());
    }
    
    @Test
    public void testClone() {
        NamespaceResolver clonedResolver = (NamespaceResolver) namespaceResolver.clone();
        assertNotSame(namespaceResolver, clonedResolver);
    }

}