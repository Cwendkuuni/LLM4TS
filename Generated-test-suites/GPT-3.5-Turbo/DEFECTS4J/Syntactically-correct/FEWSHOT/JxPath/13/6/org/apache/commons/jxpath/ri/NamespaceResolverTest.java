import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertTrue;

import org.apache.commons.jxpath.ri.model.NodeIterator;
import org.apache.commons.jxpath.ri.model.NodePointer;
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
        String prefix = namespaceResolver.getPrefix("http://test.com");
        assertEquals("test", prefix);
    }

    @Test
    public void testSetNamespaceContextPointer() {
        NodePointer pointer = new NodePointer();
        namespaceResolver.setNamespaceContextPointer(pointer);
        assertEquals(pointer, namespaceResolver.getNamespaceContextPointer());
    }

    @Test
    public void testGetNamespaceURI() {
        namespaceResolver.registerNamespace("test", "http://test.com");
        String uri = namespaceResolver.getNamespaceURI("test");
        assertEquals("http://test.com", uri);
        uri = namespaceResolver.getNamespaceURI("unknown");
        assertNull(uri);
    }

    @Test
    public void testGetPrefix() {
        namespaceResolver.registerNamespace("test", "http://test.com");
        String prefix = namespaceResolver.getPrefix("http://test.com");
        assertEquals("test", prefix);
        prefix = namespaceResolver.getPrefix("unknown");
        assertNull(prefix);
    }

    @Test
    public void testSeal() {
        namespaceResolver.seal();
        assertTrue(namespaceResolver.isSealed());
    }

    @Test
    public void testClone() {
        NamespaceResolver clonedResolver = (NamespaceResolver) namespaceResolver.clone();
        assertEquals(namespaceResolver.isSealed(), clonedResolver.isSealed());
        assertEquals(namespaceResolver.getNamespaceContextPointer(), clonedResolver.getNamespaceContextPointer());
    }
}