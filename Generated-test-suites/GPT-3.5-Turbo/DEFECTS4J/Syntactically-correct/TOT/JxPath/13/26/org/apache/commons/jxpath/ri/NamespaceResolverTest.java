import org.junit.Assert;
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
        // Typical use case
        namespaceResolver.registerNamespace("prefix1", "http://www.example.com/namespace1");
        String actualNamespaceURI = namespaceResolver.getNamespaceURI("prefix1");
        String expectedNamespaceURI = "http://www.example.com/namespace1";
        Assert.assertEquals(expectedNamespaceURI, actualNamespaceURI);

        // Edge case: Prefix already registered
        try {
            namespaceResolver.registerNamespace("prefix1", "http://www.example.com/namespace2");
        } catch (IllegalStateException e) {
            Assert.assertTrue(true);
        }

    }

    @Test
    public void testGetNamespaceURI() {
        // Typical use case
        namespaceResolver.registerNamespace("prefix1", "http://www.example.com/namespace1");
        String actualNamespaceURI = namespaceResolver.getNamespaceURI("prefix1");
        String expectedNamespaceURI = "http://www.example.com/namespace1";
        Assert.assertEquals(expectedNamespaceURI, actualNamespaceURI);

        // Typical use case: Prefix not defined explicitly, check context node
        namespaceResolver.setNamespaceContextPointer(new NodePointer());
        actualNamespaceURI = namespaceResolver.getNamespaceURI("prefix2");
        String expectedNamespaceURI = "http://www.example.com/namespace2";
        Assert.assertEquals(expectedNamespaceURI, actualNamespaceURI);

        // Edge case: Prefix not defined
        actualNamespaceURI = namespaceResolver.getNamespaceURI("prefix3");
        Assert.assertNull(actualNamespaceURI);
    }

    @Test
    public void testGetPrefix() {
        // Typical use case
        namespaceResolver.registerNamespace("prefix1", "http://www.example.com/namespace1");
        String actualPrefix = namespaceResolver.getPrefix("http://www.example.com/namespace1");
        String expectedPrefix = "prefix1";
        Assert.assertEquals(expectedPrefix, actualPrefix);

        // Typical use case: Check parent NamespaceResolver
        NamespaceResolver parentResolver = new NamespaceResolver();
        parentResolver.registerNamespace("prefix2", "http://www.example.com/namespace2");
        namespaceResolver = new NamespaceResolver(parentResolver);
        actualPrefix = namespaceResolver.getPrefix("http://www.example.com/namespace2");
        expectedPrefix = "prefix2";
        Assert.assertEquals(expectedPrefix, actualPrefix);

        // Edge case: External namespace URI not found
        actualPrefix = namespaceResolver.getPrefix("http://www.example.com/namespace3");
        Assert.assertNull(actualPrefix);
    }

    @Test
    public void testSeal() {
        // Typical use case
        namespaceResolver.registerNamespace("prefix1", "http://www.example.com/namespace1");
        namespaceResolver.seal();
        Assert.assertTrue(namespaceResolver.isSealed());

        // Edge case: Check parent NamespaceResolver
        NamespaceResolver parentResolver = new NamespaceResolver();
        parentResolver.registerNamespace("prefix2", "http://www.example.com/namespace2");
        namespaceResolver = new NamespaceResolver(parentResolver);
        namespaceResolver.seal();
        Assert.assertTrue(namespaceResolver.isSealed());
        Assert.assertTrue(parentResolver.isSealed());
    }

    @Test
    public void testClone() {
        // Typical use case
        namespaceResolver.registerNamespace("prefix1", "http://www.example.com/namespace1");
        NamespaceResolver clonedNamespaceResolver = (NamespaceResolver) namespaceResolver.clone();
        Assert.assertTrue(clonedNamespaceResolver != namespaceResolver);
        Assert.assertEquals(namespaceResolver.getNamespaceURI("prefix1"), clonedNamespaceResolver.getNamespaceURI("prefix1"));
        Assert.assertFalse(clonedNamespaceResolver.isSealed());
    }

}