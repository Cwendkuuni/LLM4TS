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
        String result = resolver.getNamespaceURI("prefix");
        assertEquals("namespaceURI", result);
    }

    @Test(expected = IllegalStateException.class)
    public void testRegisterNamespaceSealed() {
        resolver.seal();
        resolver.registerNamespace("prefix", "namespaceURI");
    }

    @Test
    public void testGetNamespaceURI() {
        resolver.registerNamespace("prefix", "namespaceURI");
        String result = resolver.getNamespaceURI("prefix");
        assertEquals("namespaceURI", result);
    }

    @Test
    public void testGetNamespaceURINotFound() {
        String result = resolver.getNamespaceURI("nonExistentPrefix");
        assertNull(result);
    }

    @Test
    public void testGetPrefix() {
        resolver.registerNamespace("prefix", "namespaceURI");
        String result = resolver.getPrefix("namespaceURI");
        assertEquals("prefix", result);
    }

    @Test
    public void testGetPrefixNotFound() {
        String result = resolver.getPrefix("nonExistentNamespaceURI");
        assertNull(result);
    }

    @Test
    public void testSeal() {
        resolver.seal();
        assertTrue(resolver.isSealed());
    }

    @Test
    public void testClone() {
        NamespaceResolver clone = (NamespaceResolver) resolver.clone();
        assertFalse(clone.isSealed());
    }
}