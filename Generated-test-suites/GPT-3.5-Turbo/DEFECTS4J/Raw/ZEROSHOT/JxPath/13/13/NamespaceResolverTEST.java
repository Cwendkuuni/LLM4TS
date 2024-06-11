import org.junit.Test;
import org.junit.Before;
import static org.junit.Assert.*;

public class NamespaceResolverTest {

    private NamespaceResolver resolver;

    @Before
    public void setUp() {
        resolver = new NamespaceResolver();
    }

    @Test
    public void testRegisterNamespace() {
        resolver.registerNamespace("prefix1", "http://example1.com");
        resolver.registerNamespace("prefix2", "http://example2.com");

        String uri1 = resolver.getNamespaceURI("prefix1");
        assertEquals("http://example1.com", uri1);

        String uri2 = resolver.getNamespaceURI("prefix2");
        assertEquals("http://example2.com", uri2);
    }

    @Test(expected = IllegalStateException.class)
    public void testRegisterNamespaceWithSealedResolver() {
        resolver.seal();
        resolver.registerNamespace("prefix", "http://example.com");
    }

    @Test
    public void testGetNamespaceURI() {
        resolver.registerNamespace("prefix", "http://example.com");
        String uri = resolver.getNamespaceURI("prefix");
        assertEquals("http://example.com", uri);
    }

    @Test
    public void testGetNamespaceURIWithUndefinedPrefix() {
        String uri = resolver.getNamespaceURI("prefix");
        assertNull(uri);

        resolver.registerNamespace("prefix", "http://example.com");
        uri = resolver.getNamespaceURI("undefinedPrefix");
        assertNull(uri);
    }

    @Test
    public void testGetNamespaceURIWithParentResolver() {
        NamespaceResolver parentResolver = new NamespaceResolver();
        parentResolver.registerNamespace("parentPrefix", "http://parent.com");
        resolver = new NamespaceResolver(parentResolver);

        String uri = resolver.getNamespaceURI("parentPrefix");
        assertEquals("http://parent.com", uri);
    }

    @Test
    public void testGetPrefix() {
        resolver.registerNamespace("prefix", "http://example.com");
        String prefix = resolver.getPrefix("http://example.com");
        assertEquals("prefix", prefix);
    }

    @Test
    public void testGetPrefixWithUndefinedURI() {
        String prefix = resolver.getPrefix("http://example.com");
        assertNull(prefix);

        resolver.registerNamespace("prefix", "http://example.com");
        prefix = resolver.getPrefix("http://undefined.com");
        assertNull(prefix);
    }

    @Test
    public void testGetPrefixWithParentResolver() {
        NamespaceResolver parentResolver = new NamespaceResolver();
        parentResolver.registerNamespace("parentPrefix", "http://parent.com");
        resolver = new NamespaceResolver(parentResolver);

        String prefix = resolver.getPrefix("http://parent.com");
        assertEquals("parentPrefix", prefix);
    }

    @Test
    public void testClone() {
        resolver.registerNamespace("prefix", "http://example.com");
        resolver.seal();

        NamespaceResolver clone = (NamespaceResolver) resolver.clone();
        assertNotNull(clone);
        assertFalse(clone.isSealed());

        String uri = clone.getNamespaceURI("prefix");
        assertEquals("http://example.com", uri);
    }
}