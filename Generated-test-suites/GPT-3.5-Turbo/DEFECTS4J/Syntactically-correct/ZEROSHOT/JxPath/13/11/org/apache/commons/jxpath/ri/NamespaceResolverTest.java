import static org.junit.Assert.*;
import org.junit.Before;
import org.junit.Test;

public class NamespaceResolverTest {
    private NamespaceResolver resolver;

    @Before
    public void setUp() {
        resolver = new NamespaceResolver();
    }

    @Test
    public void testRegisterNamespace() {
        resolver.registerNamespace("prefix", "namespaceURI");
        assertEquals("namespaceURI", resolver.getNamespaceURI("prefix"));
    }

    @Test(expected = IllegalStateException.class)
    public void testRegisterNamespaceSealed() {
        resolver.seal();
        resolver.registerNamespace("prefix", "namespaceURI");
    }

    @Test
    public void testGetNamespaceURI() {
        resolver.registerNamespace("prefix", "namespaceURI");
        assertEquals("namespaceURI", resolver.getNamespaceURI("prefix"));
        assertNull(resolver.getNamespaceURI("nonexistentPrefix"));
    }

    @Test
    public void testGetNamespaceURIInParent() {
        NamespaceResolver parent = new NamespaceResolver();
        parent.registerNamespace("prefix", "namespaceURI");
        resolver = new NamespaceResolver(parent);
        assertEquals("namespaceURI", resolver.getNamespaceURI("prefix"));
    }

    @Test
    public void testGetPrefix() {
        resolver.registerNamespace("prefix", "namespaceURI");
        assertEquals("prefix", resolver.getPrefix("namespaceURI"));
        assertNull(resolver.getPrefix("nonexistentNamespaceURI"));
    }

    @Test
    public void testGetPrefixInParent() {
        NamespaceResolver parent = new NamespaceResolver();
        parent.registerNamespace("prefix", "namespaceURI");
        resolver = new NamespaceResolver(parent);
        assertEquals("prefix", resolver.getPrefix("namespaceURI"));
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