import org.junit.Before;
import org.junit.Test;

import static org.junit.Assert.*;

public class NamespaceResolverTest {

    private NamespaceResolver namespaceResolver;

    @Before
    public void setUp() {
        namespaceResolver = new NamespaceResolver();
    }

    @Test
    public void testRegisterNamespace() {
        assertEquals(null, namespaceResolver.getNamespaceURI("test"));
        namespaceResolver.registerNamespace("test", "http://test.com");
        assertEquals("http://test.com", namespaceResolver.getNamespaceURI("test"));
    }

    @Test(expected = IllegalStateException.class)
    public void testRegisterNamespaceOnSealedResolver() {
        namespaceResolver.seal();
        namespaceResolver.registerNamespace("test", "http://test.com");
    }

    @Test
    public void testGetNamespaceURI() {
        assertEquals(null, namespaceResolver.getNamespaceURI("test"));
        namespaceResolver.registerNamespace("test", "http://test.com");
        assertEquals("http://test.com", namespaceResolver.getNamespaceURI("test"));
        assertEquals(null, namespaceResolver.getNamespaceURI("hello"));
    }

    @Test
    public void testGetPrefix() {
        assertEquals(null, namespaceResolver.getPrefix("http://test.com"));
        namespaceResolver.registerNamespace("test", "http://test.com");
        assertEquals("test", namespaceResolver.getPrefix("http://test.com"));
        assertEquals(null, namespaceResolver.getPrefix("http://hello.com"));
    }

    @Test
    public void testSeal() {
        assertFalse(namespaceResolver.isSealed());
        namespaceResolver.seal();
        assertTrue(namespaceResolver.isSealed());
    }

    @Test
    public void testClone() {
        NamespaceResolver clonedResolver = (NamespaceResolver) namespaceResolver.clone();
        assertNotSame(namespaceResolver, clonedResolver);
        assertEquals(namespaceResolver.isSealed(), clonedResolver.isSealed());
    }
}