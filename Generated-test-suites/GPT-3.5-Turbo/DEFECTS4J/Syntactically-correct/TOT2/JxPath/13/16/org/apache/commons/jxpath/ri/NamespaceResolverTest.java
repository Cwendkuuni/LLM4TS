import static org.junit.Assert.*;

import org.junit.Before;
import org.junit.Test;

public class NamespaceResolverTest {

    private NamespaceResolver namespaceResolver;

    @Before
    public void setUp() {
        namespaceResolver = new NamespaceResolver();
    }

    @Test
    public void testRegisterNamespace() {
        namespaceResolver.registerNamespace("test", "http://test.com");
        assertEquals("http://test.com", namespaceResolver.getNamespaceURI("test"));
    }

    @Test
    public void testSetNamespaceContextPointer() {
        // Test setting namespace context pointer
        NodePointer pointer = new NodePointer();
        namespaceResolver.setNamespaceContextPointer(pointer);
        assertEquals(pointer, namespaceResolver.getNamespaceContextPointer());
    }

    @Test
    public void testGetNamespaceURI() {
        // Test getting namespace URI
        namespaceResolver.registerNamespace("test", "http://test.com");
        assertEquals("http://test.com", namespaceResolver.getNamespaceURI("test"));
    }

    @Test
    public void testGetPrefix() {
        // Test getting prefix
        namespaceResolver.registerNamespace("test", "http://test.com");
        assertEquals("test", namespaceResolver.getPrefix("http://test.com"));
    }

    @Test
    public void testIsSealed() {
        // Test if namespace resolver is sealed
        assertFalse(namespaceResolver.isSealed());
        namespaceResolver.seal();
        assertTrue(namespaceResolver.isSealed());
    }

    @Test
    public void testClone() {
        // Test cloning namespace resolver
        NamespaceResolver clonedResolver = (NamespaceResolver) namespaceResolver.clone();
        assertNotSame(namespaceResolver, clonedResolver);
    }
}