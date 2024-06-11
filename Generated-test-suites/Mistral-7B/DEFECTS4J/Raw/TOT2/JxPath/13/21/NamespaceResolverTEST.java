import org.apache.commons.jxpath.ri.NamespacesResolver;
import org.junit.Test;
import static org.junit.Assert.*;

public class NamespaceResolverTest {

    @Test
    public void testConstructor() {
        // ... (same as Expert 1, 2)
    }

    @Test
    public void testRegisterNamespace() {
        // ... (same as Expert 1, 2)
    }

    @Test
    public void testGetNamespaceURI() {
        // ... (same as Expert 1, 2)
    }

    @Test
    public void testGetPrefix() {
        // ... (same as Expert 1, 2)
    }

    @Test
    public void testIsSealed() {
        // ... (same as Expert 1, 2)
    }

    @Test
    public void testClone() {
        // ... (same as Expert 1, 2)
    }

    @Test
    public void testRegisterNamespaceWithNullNamespaceURI() {
        NamespaceResolver namespaceResolver = new NamespaceResolver();
        try {
            namespaceResolver.registerNamespace("prefix", null);
            fail("expected IllegalArgumentException");
        } catch (IllegalArgumentException e) {
            // expected
        }
    }

    @Test
    public void testRegisterNamespaceWithEmptyNamespaceURI() {
        NamespaceResolver namespaceResolver = new NamespaceResolver();
        try {
            namespaceResolver.registerNamespace("prefix", "");
            fail("expected IllegalArgumentException");
        } catch (IllegalArgumentException e) {
            // expected
        }
    }

    @Test
    public void testGetNamespaceURIWithNullPrefix() {
        NamespaceResolver namespaceResolver = new NamespaceResolver();
        try {
            namespaceResolver.getNamespaceURI(null);
            fail("expected NullPointerException");
        } catch (NullPointerException e) {
            // expected
        }
    }

    @Test
    public void testGetNamespaceURIWithEmptyPrefix() {
        NamespaceResolver namespaceResolver = new NamespaceResolver();
        try {
            namespaceResolver.getNamespaceURI("");
            fail("expected NullPointerException");
        } catch (NullPointerException e) {
            // expected
        }
    }

    @Test
    public void testGetNamespaceURIWithNonExistentPrefix() {
        NamespaceResolver namespaceResolver = new NamespaceResolver();
        namespaceResolver.registerNamespace("prefix1", "http://example.com/prefix1");
        String namespaceURI = namespaceResolver.getNamespaceURI("prefix2");
        assertNull(namespaceURI);
    }

    @Test(expected = IllegalStateException.class)
    public void testRegisterNamespaceOnSealedResolver() {
        NamespaceResolver namespaceResolver = new NamespaceResolver();
        namespaceResolver.registerNamespace("prefix1", "http://example.com/prefix1");
        namespaceResolver.seal();
        namespaceResolver.registerNamespace("prefix2", "http://example.com/prefix2");
    }

    @Test
    public void testGetPrefixWithNullNamespaceURI() {
        NamespaceResolver namespaceResolver = new NamespaceResolver();
        try {
            namespaceResolver.getPrefix(null);
            fail("expected NullPointerException");
        } catch (NullPointerException e) {
            // expected
        }
    }

    @Test
    public void testGetPrefixWithEmptyNamespaceURI() {
        NamespaceResolver namespaceResolver = new NamespaceResolver();
        try {
            namespaceResolver.getPrefix("");
            fail("expected NullPointerException");
        } catch (NullPointerException e) {
            // expected
        }
    }

    @Test
    public void testGetPrefixWithNonExistentNamespaceURI() {
        NamespaceResolver namespaceResolver = new NamespaceResolver();
        namespaceResolver.registerNamespace("prefix1", "http://example.com/prefix1");
        String prefix = namespaceResolver.getPrefix("http://example.com/prefix2");
        assertNull(prefix);
    }

    @Test
    public void testGetPrefixWithExistentNamespaceURI() {
        NamespaceResolver namespaceResolver = new NamespaceResolver();
        namespaceResolver.registerNamespace("prefix1", "http://example.com/prefix1");
        String prefix = namespaceResolver.getPrefix("http://example.com/prefix1");
        assertEquals("prefix1", prefix);
    }

    @Test
    public void testGetPrefixWithExistentNamespaceURIUnderParent() {
        NamespaceResolver parentResolver = new NamespaceResolver();
        parentResolver.registerNamespace("prefix1", "http://example.com/prefix1");
        NamespaceResolver namespaceResolver = new NamespaceResolver(parentResolver);
        String prefix = namespaceResolver.getPrefix("http://example.com/prefix1");
        assertEquals("prefix1", prefix);
    }

    @Test
    public void testIsSealed() {
        NamespaceResolver namespaceResolver = new NamespaceResolver();
        assertFalse(namespaceResolver.isSealed());

        namespaceResolver.seal();
        assertTrue(namespaceResolver.isSealed());
    }

    @Test
    public void testClone() {
        NamespaceResolver original = new NamespaceResolver();
        original.registerNamespace("x", "http://example.com/x");
        original.seal();

        NamespaceResolver clone = (NamespaceResolver) original.clone();
        assertNotSame(original, clone);
        assertFalse(clone.isSealed());
        assertEquals("http://example.com/x", clone.getNamespaceURI("x"));
    }

    @Test
    public void testCloneWithSealedParent() {
        NamespaceResolver parentResolver = new NamespaceResolver();
        parentResolver.registerNamespace("x", "http://example.com/x");
        parentResolver.seal();

        NamespaceResolver namespaceResolver = new NamespaceResolver(parentResolver);
        NamespaceResolver clone = (NamespaceResolver) namespaceResolver.clone();
        assertNotSame(namespaceResolver, clone);
        assertFalse(clone.isSealed());
        assertEquals("http://example.com/x", clone.getNamespaceURI("x"));
    }
}