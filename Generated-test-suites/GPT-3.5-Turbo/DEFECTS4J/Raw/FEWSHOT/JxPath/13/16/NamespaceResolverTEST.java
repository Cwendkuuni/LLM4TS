import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertTrue;

import org.apache.commons.jxpath.Pointer;
import org.apache.commons.jxpath.ri.model.NodeIterator;
import org.apache.commons.jxpath.ri.model.NodePointer;
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
        resolver.registerNamespace("prefix", "namespaceURI");
        assertEquals("namespaceURI", resolver.getNamespaceURI("prefix"));
    }

    @Test(expected = IllegalStateException.class)
    public void testRegisterNamespaceSealed() {
        resolver.seal();
        resolver.registerNamespace("prefix", "namespaceURI");
    }

    @Test
    public void testSetNamespaceContextPointer() {
        NodePointer pointer = new NodePointer();
        resolver.setNamespaceContextPointer(pointer);
        assertEquals(pointer, resolver.getNamespaceContextPointer());
    }

    @Test
    public void testGetNamespaceURI() {
        assertNull(resolver.getNamespaceURI("prefix"));
        NodePointer pointer = new NodePointer();
        pointer.setNamespaceURI("prefix", "namespaceURI");
        resolver.setNamespaceContextPointer(pointer);
        assertEquals("namespaceURI", resolver.getNamespaceURI("prefix"));
        resolver.registerNamespace("prefix", "namespaceURI2");
        assertEquals("namespaceURI2", resolver.getNamespaceURI("prefix"));
    }

    @Test
    public void testGetPrefix() {
        assertNull(resolver.getPrefix("namespaceURI"));
        NodePointer pointer = new NodePointer();
        pointer.setNamespaceURI("prefix", "namespaceURI");
        resolver.setNamespaceContextPointer(pointer);
        assertEquals("prefix", resolver.getPrefix("namespaceURI"));
        resolver.registerNamespace("prefix2", "namespaceURI");
        assertEquals("prefix2", resolver.getPrefix("namespaceURI"));
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
        assertEquals(resolver.isSealed(), clone.isSealed());
    }
}