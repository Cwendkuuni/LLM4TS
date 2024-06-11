import org.apache.commons.jxpath.Pointer;
import org.apache.commons.jxpath.ri.model.NodeIterator;
import org.apache.commons.jxpath.ri.model.NodePointer;
import org.junit.Before;
import org.junit.Test;

import static org.junit.Assert.*;

public class NamespaceResolverTest {

    private NamespaceResolver resolver;
    private NodePointer pointer;

    @Before
    public void setUp() {
        resolver = new NamespaceResolver();
        pointer = new NodePointer();
    }

    @Test
    public void testRegisterNamespace() {
        resolver.registerNamespace("prefix", "namespaceURI");
        String uri = resolver.getNamespaceURI("prefix");
        assertEquals("namespaceURI", uri);
    }

    @Test(expected = IllegalStateException.class)
    public void testRegisterNamespaceSealed() {
        resolver.seal();
        resolver.registerNamespace("prefix", "namespaceURI");
    }

    @Test
    public void testSetNamespaceContextPointer() {
        resolver.setNamespaceContextPointer(pointer);
        Pointer contextPointer = resolver.getNamespaceContextPointer();
        assertEquals(pointer, contextPointer);
    }

    @Test
    public void testGetNamespaceURI() {
        resolver.registerNamespace("prefix", "namespaceURI");
        String uri = resolver.getNamespaceURI("prefix");
        assertEquals("namespaceURI", uri);
    }

    @Test
    public void testGetNamespaceURINotFound() {
        String uri = resolver.getNamespaceURI("prefix");
        assertNull(uri);
    }

    @Test
    public void testGetNamespaceURIFromParent() {
        NamespaceResolver parentResolver = new NamespaceResolver();
        resolver = new NamespaceResolver(parentResolver);
        parentResolver.registerNamespace("prefix", "namespaceURI");
        String uri = resolver.getNamespaceURI("prefix");
        assertEquals("namespaceURI", uri);
    }

    @Test
    public void testGetPrefix() {
        resolver.registerNamespace("prefix", "namespaceURI");
        String prefix = resolver.getPrefix("namespaceURI");
        assertEquals("prefix", prefix);
    }

    @Test
    public void testGetPrefixNotFound() {
        String prefix = resolver.getPrefix("namespaceURI");
        assertNull(prefix);
    }

    @Test
    public void testGetPrefixFromParent() {
        NamespaceResolver parentResolver = new NamespaceResolver();
        resolver = new NamespaceResolver(parentResolver);
        parentResolver.registerNamespace("prefix", "namespaceURI");
        String prefix = resolver.getPrefix("namespaceURI");
        assertEquals("prefix", prefix);
    }

    @Test
    public void testIsSealed() {
        assertFalse(resolver.isSealed());
        resolver.seal();
        assertTrue(resolver.isSealed());
    }

    @Test
    public void testClone() {
        NamespaceResolver clonedResolver = (NamespaceResolver) resolver.clone();
        assertFalse(clonedResolver.isSealed());
    }
}