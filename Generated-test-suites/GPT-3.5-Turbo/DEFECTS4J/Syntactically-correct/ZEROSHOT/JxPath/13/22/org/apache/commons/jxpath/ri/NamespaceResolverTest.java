import org.junit.Before;
import org.junit.Test;
import org.apache.commons.jxpath.Pointer;
import org.apache.commons.jxpath.ri.model.NodeIterator;
import org.apache.commons.jxpath.ri.model.NodePointer;

import static org.junit.Assert.*;

public class NamespaceResolverTest {

    private NamespaceResolver namespaceResolver;

    @Before
    public void setUp() {
        namespaceResolver = new NamespaceResolver();
    }

    @Test
    public void testRegisterNamespace() {
        namespaceResolver.registerNamespace("prefix1", "uri1");
        assertEquals("uri1", namespaceResolver.getNamespaceURI("prefix1"));
    }

    @Test
    public void testSetNamespaceContextPointer() {
        NodePointer pointer = new NodePointer();
        namespaceResolver.setNamespaceContextPointer(pointer);
        assertEquals(pointer, namespaceResolver.getNamespaceContextPointer());
    }

    @Test
    public void testGetNamespaceURI() {
        namespaceResolver.registerNamespace("prefix1", "uri1");
        assertEquals("uri1", namespaceResolver.getNamespaceURI("prefix1"));
    }

    @Test
    public void testGetNamespaceURIWithInvalidPrefix() {
        assertNull(namespaceResolver.getNamespaceURI("prefix1"));
    }

    @Test
    public void testGetPrefix() {
        namespaceResolver.registerNamespace("prefix1", "uri1");
        assertEquals("prefix1", namespaceResolver.getPrefix("uri1"));
    }

    @Test
    public void testGetPrefixWithInvalidNamespaceURI() {
        assertNull(namespaceResolver.getPrefix("uri1"));
    }

    @Test
    public void testIsSealed() {
        assertFalse(namespaceResolver.isSealed());
    }

    @Test
    public void testSeal() {
        namespaceResolver.seal();
        assertTrue(namespaceResolver.isSealed());
    }

    @Test
    public void testClone() {
        NamespaceResolver clone = (NamespaceResolver) namespaceResolver.clone();
        assertNotNull(clone);
        assertFalse(clone.isSealed());
    }
}