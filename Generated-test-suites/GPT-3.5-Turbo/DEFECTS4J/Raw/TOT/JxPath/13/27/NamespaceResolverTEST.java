import org.junit.Before;
import org.junit.Test;

public class NamespaceResolverTest {

    private NamespaceResolver namespaceResolver;

    @Before
    public void setUp() {
        namespaceResolver = new NamespaceResolver();
    }

    @Test
    public void testRegisterNamespace_ValidPrefixAndURI_Success() {
        // Arrange
        String prefix = "prefix";
        String namespaceURI = "http://www.example.com";

        // Act
        namespaceResolver.registerNamespace(prefix, namespaceURI);

        // Assert
        String result = namespaceResolver.getNamespaceURI(prefix);
        assertEquals(namespaceURI, result);
    }

    @Test(expected = IllegalStateException.class)
    public void testRegisterNamespace_SealedResolver_ThrowsException() {
        // Arrange
        namespaceResolver.seal();
        String prefix = "prefix";
        String namespaceURI = "http://www.example.com";

        // Act
        namespaceResolver.registerNamespace(prefix, namespaceURI);
    }

    @Test
    public void testGetNamespaceURI_PrefixRegistered_Success() {
        // Arrange
        String prefix = "prefix";
        String namespaceURI = "http://www.example.com";
        namespaceResolver.registerNamespace(prefix, namespaceURI);

        // Act
        String result = namespaceResolver.getNamespaceURI(prefix);

        // Assert
        assertEquals(namespaceURI, result);
    }

    @Test
    public void testGetNamespaceURI_PrefixNotRegistered_Success() {
        // Arrange
        String prefix = "prefix";

        // Act
        String result = namespaceResolver.getNamespaceURI(prefix);

        // Assert
        assertNull(result);
    }

    @Test
    public void testGetNamespaceURI_ExternalNamespaceURI_Success() {
        // Arrange
        String prefix = "prefix";
        String namespaceURI = "http://www.example.com";
        NodePointer pointer = createMockNodePointer(namespaceURI);
        namespaceResolver.setNamespaceContextPointer(pointer);

        // Act
        String result = namespaceResolver.getNamespaceURI(prefix);

        // Assert
        assertEquals(namespaceURI, result);
    }

    @Test
    public void testGetPrefix_NamespaceURISet_Success() {
        // Arrange
        String prefix = "prefix";
        String namespaceURI = "http://www.example.com";
        namespaceResolver.registerNamespace(prefix, namespaceURI);

        // Act
        String result = namespaceResolver.getPrefix(namespaceURI);

        // Assert
        assertEquals(prefix, result);
    }

    @Test
    public void testGetPrefix_NamespaceURINotSet_Success() {
        // Arrange
        String namespaceURI = "http://www.example.com";

        // Act
        String result = namespaceResolver.getPrefix(namespaceURI);

        // Assert
        assertNull(result);
    }

    @Test
    public void testGetPrefix_ExternalNamespaceURISet_Success() {
        // Arrange
        String prefix = "prefix";
        String namespaceURI = "http://www.example.com";
        NodePointer pointer = createMockNodePointer(namespaceURI);
        namespaceResolver.setNamespaceContextPointer(pointer);

        // Act
        String result = namespaceResolver.getPrefix(namespaceURI);

        // Assert
        assertEquals(prefix, result);
    }

    private NodePointer createMockNodePointer(String namespaceURI) {
        // Implement mock NodePointer with namespaceIterator() method
        // that returns a NodeIterator with a single NodePointer
        // having the given namespaceURI

        return null;
    }
}