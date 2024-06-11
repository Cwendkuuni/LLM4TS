import org.junit.Before;
import org.junit.Test;
import static org.junit.Assert.*;

public class NamespaceResolverTest {
    
    private NamespaceResolver resolver;

    @Before
    public void setup() {
        resolver = new NamespaceResolver();
    }

    @Test
    public void testRegisterNamespace() {
        String prefix = "prefix";
        String namespaceURI = "http://example.com";
        resolver.registerNamespace(prefix, namespaceURI);
        assertEquals(namespaceURI, resolver.getNamespaceURI(prefix));
    }
    
    @Test
    public void testSetNamespaceContextPointer() {
        NodePointer pointer = new NodePointer();
        resolver.setNamespaceContextPointer(pointer);
        assertEquals(pointer, resolver.getNamespaceContextPointer());
    }
    
    @Test
    public void testGetNamespaceURI() {
        String prefix = "prefix";
        String namespaceURI = "http://example.com";
        resolver.registerNamespace(prefix, namespaceURI);
        assertEquals(namespaceURI, resolver.getNamespaceURI(prefix));
    }
    
    @Test
    public void testGetPrefix() {
        String prefix = "prefix";
        String namespaceURI = "http://example.com";
        resolver.registerNamespace(prefix, namespaceURI);
        assertEquals(prefix, resolver.getPrefix(namespaceURI));
    }
    
    @Test
    public void testIsSealed() {
        assertFalse(resolver.isSealed());
        resolver.seal();
        assertTrue(resolver.isSealed());
    }
    
    @Test
    public void testClone() {
        NamespaceResolver clonedResolver = (NamespaceResolver) resolver.clone();
        assertNotSame(resolver, clonedResolver);
        assertEquals(resolver.isSealed(), clonedResolver.isSealed());
    }

}