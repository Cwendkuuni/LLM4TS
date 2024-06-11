import org.apache.commons.jxpath.Pointer;
import org.apache.commons.jxpath.ri.model.NodePointer;
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
        namespaceResolver.registerNamespace("prefix", "namespaceURI");
        assertEquals("namespaceURI", namespaceResolver.getNamespaceURI("prefix"));
    }

    @Test(expected = IllegalStateException.class)
    public void testRegisterNamespace_sealedResolver() {
        namespaceResolver.seal();
        namespaceResolver.registerNamespace("prefix", "namespaceURI");
    }

    @Test
    public void testGetNamespaceURI() {
        namespaceResolver.registerNamespace("prefix", "namespaceURI");
        assertEquals("namespaceURI", namespaceResolver.getNamespaceURI("prefix"));
    }

    @Test
    public void testGetNamespaceURI_notFound() {
        assertNull(namespaceResolver.getNamespaceURI("prefix"));
    }

    @Test
    public void testGetNamespaceURI_external() {
        // Simulate externally registered namespace
        NodePointer pointer = new NodePointer();
        pointer.addNamespace("prefix", "namespaceURI");

        namespaceResolver.setNamespaceContextPointer(pointer);

        assertEquals("namespaceURI", namespaceResolver.getNamespaceURI("prefix"));
    }

    @Test
    public void testGetNamespaceURI_parentResolver() {
        NamespaceResolver parentResolver = new NamespaceResolver();
        parentResolver.registerNamespace("prefix", "namespaceURI");
        namespaceResolver = new NamespaceResolver(parentResolver);

        assertEquals("namespaceURI", namespaceResolver.getNamespaceURI("prefix"));
    }

    @Test
    public void testGetPrefix() {
        namespaceResolver.registerNamespace("prefix", "namespaceURI");
        assertEquals("prefix", namespaceResolver.getPrefix("namespaceURI"));
    }

    @Test
    public void testGetPrefix_notFound() {
        assertNull(namespaceResolver.getPrefix("namespaceURI"));
    }

    @Test
    public void testGetPrefix_external() {
        // Simulate externally registered namespace
        NodePointer pointer = new NodePointer();
        pointer.addNamespace("prefix", "namespaceURI");

        namespaceResolver.setNamespaceContextPointer(pointer);

        assertEquals("prefix", namespaceResolver.getPrefix("namespaceURI"));
    }

    @Test
    public void testGetPrefix_parentResolver() {
        NamespaceResolver parentResolver = new NamespaceResolver();
        parentResolver.registerNamespace("prefix", "namespaceURI");
        namespaceResolver = new NamespaceResolver(parentResolver);

        assertEquals("prefix", namespaceResolver.getPrefix("namespaceURI"));
    }

    @Test
    public void testIsSealed() {
        assertFalse(namespaceResolver.isSealed());
        namespaceResolver.seal();
        assertTrue(namespaceResolver.isSealed());
    }

    @Test
    public void testClone() {
        NamespaceResolver clone = (NamespaceResolver) namespaceResolver.clone();
        assertNotSame(namespaceResolver, clone);
        assertSame(namespaceResolver.getNamespaceMap(), clone.getNamespaceMap());
        assertFalse(clone.isSealed());
    }
}