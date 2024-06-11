import org.apache.commons.jxpath.ri.NamespacesResolver;
import org.junit.Test;

public class NamespaceResolverTest {
    // Tests for method getNamespaceURI(String prefix)

    // Test for typical use case
    @Test
    public void testGetNamespaceURI_TypicalUseCase() {
        NamespaceResolver resolver = new NamespaceResolver();
        resolver.registerNamespace("xsd", "http://www.w3.org/2001/XMLSchema");
        String uri = resolver.getNamespaceURI("xsd");
        assertNotNull(uri);
        assertEquals("http://www.w3.org/2001/XMLSchema", uri);
    }

    // Test for edge case: null prefix
    @Test(expected = NullPointerException.class)
    public void testGetNamespaceURI_NullPrefix() {
        NamespaceResolver resolver = new NamespaceResolver();
        String uri = resolver.getNamespaceURI(null);
    }

    // Test for edge case: empty prefix
    @Test(expected = IllegalArgumentException.class)
    public void testGetNamespaceURI_EmptyPrefix() {
        NamespaceResolver resolver = new NamespaceResolver();
        String uri = resolver.getNamespaceURI("");
    }

    // Test for error case: unregistered prefix
    @Test(expected = NullPointerException.class)
    public void testGetNamespaceURI_UnregisteredPrefix() {
        NamespaceResolver resolver = new NamespaceResolver();
        String uri = resolver.getNamespaceURI("foo");
    }

    // Tests for method getPrefix(String namespaceURI)

    // Test for typical use case
    @Test
    public void testGetPrefix_TypicalUseCase() {
        NamespaceResolver resolver = new NamespaceResolver();
        resolver.registerNamespace("xsd", "http://www.w3.org/2001/XMLSchema");
        String prefix = resolver.getPrefix("http://www.w3.org/2001/XMLSchema");
        assertNotNull(prefix);
        assertEquals("xsd", prefix);
    }

    // Test for edge case: null namespaceURI
    @Test(expected = NullPointerException.class)
    public void testGetPrefix_NullNamespaceURI() {
        NamespaceResolver resolver = new NamespaceResolver();
        String prefix = resolver.getPrefix(null);
    }

    // Test for edge case: empty namespaceURI
    @Test(expected = IllegalArgumentException.class)
    public void testGetPrefix_EmptyNamespaceURI() {
        NamespaceResolver resolver = new NamespaceResolver();
        String prefix = resolver.getPrefix("");
    }

    // Test for error case: unregistered namespaceURI
    @Test
    public void testGetPrefix_UnregisteredNamespaceURI() {
        NamespaceResolver resolver = new NamespaceResolver();
        String prefix = resolver.getPrefix("http://example.com");
        assertNull(prefix);
    }

    // Tests for method seal()

    // Test for typical use case
    @Test
    public void testSeal_TypicalUseCase() {
        NamespaceResolver resolver = new NamespaceResolver();
        resolver.registerNamespace("xsd", "http://www.w3.org/2001/XMLSchema");
        resolver.seal();
        NamespaceResolver clonedResolver = (NamespaceResolver) resolver.clone();
        try {
            String uri = clonedResolver.getNamespaceURI("xsd");
            fail("Should not be able to add or change namespaces in a sealed NamespaceResolver.");
        } catch (IllegalStateException e) {
            // Expected
        }
    }

    // Test for edge case: already sealed NamespaceResolver
    @Test(expected = IllegalStateException.class)
    public void testSeal_AlreadySealed() {
        NamespaceResolver resolver = new NamespaceResolver();
        resolver.seal();
        resolver.seal();
    }
}