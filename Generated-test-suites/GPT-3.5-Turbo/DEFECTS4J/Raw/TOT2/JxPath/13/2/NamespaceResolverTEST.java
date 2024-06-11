import static org.junit.Assert.*;

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
        resolver.registerNamespace("prefix", "http://example.com");
        assertEquals("http://example.com", resolver.getNamespaceURI("prefix"));
    }

    @Test
    public void testSetNamespaceContextPointer() {
        NodePointer pointer = new NodePointer();
        resolver.setNamespaceContextPointer(pointer);
        assertEquals(pointer, resolver.getNamespaceContextPointer());
    }

    @Test
    public void testGetNamespaceURI() {
        resolver.registerNamespace("prefix", "http://example.com");
        assertEquals("http://example.com", resolver.getNamespaceURI("prefix"));
        assertNull(resolver.getNamespaceURI("unknown"));
    }

    @Test
    public void testGetPrefix() {
        resolver.registerNamespace("prefix", "http://example.com");
        assertEquals("prefix", resolver.getPrefix("http://example.com"));
        assertNull(resolver.getPrefix("http://unknown.com"));
    }

    @Test
    public void testSeal() {
        assertFalse(resolver.isSealed());
        resolver.seal();
        assertTrue(resolver.isSealed());
    }

    @Test
    public void testClone() {
        NamespaceResolver clone = (NamespaceResolver) resolver.clone();
        assertNotSame(resolver, clone);
    }

}