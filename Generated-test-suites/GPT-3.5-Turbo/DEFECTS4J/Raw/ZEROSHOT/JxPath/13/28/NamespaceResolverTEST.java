import org.junit.Before;
import org.junit.Test;

import static org.junit.Assert.*;

public class NamespaceResolverTest {

    private NamespaceResolver resolver;

    @Before
    public void setUp() {
        resolver = new NamespaceResolver();
    }

    @Test
    public void testRegisterNamespace() {
        resolver.registerNamespace("prefix", "uri");
        assertEquals("uri", resolver.getNamespaceURI("prefix"));
    }
    
    @Test(expected = IllegalStateException.class)
    public void testRegisterNamespaceSealed() {
        resolver.seal();
        resolver.registerNamespace("prefix", "uri");
    }

    @Test
    public void testSetNamespaceContextPointer() {
        NodePointer pointer = new NodePointer();
        resolver.setNamespaceContextPointer(pointer);
        assertEquals(pointer, resolver.getNamespaceContextPointer());
    }

    @Test
    public void testGetNamespaceURI() {
        resolver.registerNamespace("prefix", "uri");
        assertEquals("uri", resolver.getNamespaceURI("prefix"));
        assertNull(resolver.getNamespaceURI("otherPrefix"));
    }

    @Test
    public void testGetNamespaceURIWithParent() {
        NamespaceResolver parentResolver = new NamespaceResolver();
        parentResolver.registerNamespace("prefix", "uri");
        resolver = new NamespaceResolver(parentResolver);
        assertEquals("uri", resolver.getNamespaceURI("prefix"));
    }

    @Test
    public void testGetPrefix() {
        resolver.registerNamespace("prefix", "uri");
        assertEquals("prefix", resolver.getPrefix("uri"));
        assertNull(resolver.getPrefix("otherUri"));
    }

    @Test
    public void testGetPrefixWithParent() {
        NamespaceResolver parentResolver = new NamespaceResolver();
        parentResolver.registerNamespace("prefix", "uri");
        resolver = new NamespaceResolver(parentResolver);
        assertEquals("prefix", resolver.getPrefix("uri"));
    }

    @Test
    public void testIsSealed() {
        assertFalse(resolver.isSealed());
        resolver.seal();
        assertTrue(resolver.isSealed());
    }

    @Test
    public void testClone() {
        NamespaceResolver clone = (NamespaceResolver) resolver.clone();
        assertNotSame(resolver, clone);
        assertEquals(resolver.isSealed(), clone.isSealed());
    }
}