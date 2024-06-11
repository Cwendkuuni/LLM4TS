import org.junit.Before;
import org.junit.Test;

public class NamespaceResolverTest {

    private NamespaceResolver namespaceResolver;

    @Before
    public void setUp() {
        namespaceResolver = new NamespaceResolver();
    }

    @Test(expected = IllegalStateException.class)
    public void testRegisterNamespaceOnSealedResolver() {
        namespaceResolver.seal();
        namespaceResolver.registerNamespace("prefix", "uri");
    }

    @Test
    public void testRegisterNamespace() {
        String prefix = "prefix";
        String uri = "uri";

        namespaceResolver.registerNamespace(prefix, uri);

        String retrievedUri = namespaceResolver.getNamespaceURI(prefix);
        assertEquals(uri, retrievedUri);
    }

    @Test
    public void testGetNamespaceURI() {
        String prefix = "prefix";
        String uri = "uri";

        namespaceResolver.registerNamespace(prefix, uri);

        String retrievedUri = namespaceResolver.getNamespaceURI(prefix);
        assertEquals(uri, retrievedUri);
    }

    @Test
    public void testGetPrefix() {
        String prefix = "prefix";
        String uri = "uri";

        namespaceResolver.registerNamespace(prefix, uri);

        String retrievedPrefix = namespaceResolver.getPrefix(uri);
        assertEquals(prefix, retrievedPrefix);
    }

    @Test
    public void testGetPrefixWithExternallyRegisteredNamespace() {
        String prefix = "prefix";
        String uri = "uri";

        namespaceResolver.registerNamespace(prefix, uri);

        NodePointer pointer = new NodePointer();
        pointer.registerNamespace(prefix, uri);
        namespaceResolver.setNamespaceContextPointer(pointer);

        String retrievedPrefix = namespaceResolver.getPrefix(uri);
        assertEquals(prefix, retrievedPrefix);
    }

    @Test
    public void testGetPrefixWithParentResolver() {
        NamespaceResolver parentResolver = new NamespaceResolver();
        String prefix = "prefix";
        String uri = "uri";

        parentResolver.registerNamespace(prefix, uri);
        namespaceResolver = new NamespaceResolver(parentResolver);

        String retrievedPrefix = namespaceResolver.getPrefix(uri);
        assertEquals(prefix, retrievedPrefix);
    }
}