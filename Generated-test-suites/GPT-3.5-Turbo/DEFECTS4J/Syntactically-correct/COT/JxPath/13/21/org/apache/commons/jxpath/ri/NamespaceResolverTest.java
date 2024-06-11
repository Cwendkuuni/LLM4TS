import org.junit.Test;
import static org.junit.Assert.*;

public class NamespaceResolverTest {

    @Test
    public void testRegisterNamespace() {
        // Create a new NamespaceResolver
        NamespaceResolver namespaceResolver = new NamespaceResolver();

        // Register a namespace
        namespaceResolver.registerNamespace("prefix", "namespaceURI");

        // Verify that the namespace was registered correctly
        assertEquals("namespaceURI", namespaceResolver.getNamespaceURI("prefix"));
    }

    @Test
    public void testRegisterNamespace_Sealed() {
        // Create a new sealed NamespaceResolver
        NamespaceResolver namespaceResolver = new NamespaceResolver();
        namespaceResolver.seal();

        // Try to register a namespace (should throw an exception)
        try {
            namespaceResolver.registerNamespace("prefix", "namespaceURI");
            fail("Expected IllegalStateException");
        } catch (IllegalStateException e) {
            // Exception expected
        }
    }

    @Test
    public void testGetNamespaceURI() {
        // Create a new NamespaceResolver
        NamespaceResolver namespaceResolver = new NamespaceResolver();

        // Register a namespace
        namespaceResolver.registerNamespace("prefix", "namespaceURI");

        // Get the namespace URI for the prefix
        String uri = namespaceResolver.getNamespaceURI("prefix");

        // Verify that the correct namespace URI is returned
        assertEquals("namespaceURI", uri);
    }

    @Test
    public void testGetNamespaceURI_Parent() {
        // Create a parent NamespaceResolver
        NamespaceResolver parentNamespaceResolver = new NamespaceResolver();
        parentNamespaceResolver.registerNamespace("prefix", "namespaceURI");

        // Create a child NamespaceResolver with the parent set
        NamespaceResolver namespaceResolver = new NamespaceResolver(parentNamespaceResolver);

        // Get the namespace URI for the prefix (should be inherited from the parent)
        String uri = namespaceResolver.getNamespaceURI("prefix");

        // Verify that the correct namespace URI is returned
        assertEquals("namespaceURI", uri);
    }

    @Test
    public void testGetPrefix() {
        // Create a new NamespaceResolver
        NamespaceResolver namespaceResolver = new NamespaceResolver();

        // Register a namespace
        namespaceResolver.registerNamespace("prefix", "namespaceURI");

        // Get the prefix for the namespace URI
        String prefix = namespaceResolver.getPrefix("namespaceURI");

        // Verify that the correct prefix is returned
        assertEquals("prefix", prefix);
    }

    @Test
    public void testGetPrefix_Parent() {
        // Create a parent NamespaceResolver
        NamespaceResolver parentNamespaceResolver = new NamespaceResolver();
        parentNamespaceResolver.registerNamespace("prefix", "namespaceURI");

        // Create a child NamespaceResolver with the parent set
        NamespaceResolver namespaceResolver = new NamespaceResolver(parentNamespaceResolver);

        // Get the prefix for the namespace URI (should be inherited from the parent)
        String prefix = namespaceResolver.getPrefix("namespaceURI");

        // Verify that the correct prefix is returned
        assertEquals("prefix", prefix);
    }

    @Test
    public void testSeal() {
        // Create a new NamespaceResolver
        NamespaceResolver namespaceResolver = new NamespaceResolver();

        // Seal the NamespaceResolver
        namespaceResolver.seal();

        // Verify that the NamespaceResolver is sealed
        assertTrue(namespaceResolver.isSealed());
    }

    @Test
    public void testClone() {
        // Create a new NamespaceResolver
        NamespaceResolver namespaceResolver = new NamespaceResolver();

        // Clone the NamespaceResolver
        NamespaceResolver clone = (NamespaceResolver) namespaceResolver.clone();

        // Verify that the cloned NamespaceResolver is not sealed
        assertFalse(clone.isSealed());
    }
}