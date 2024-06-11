import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertTrue;

import org.apache.commons.jxpath.Pointer;
import org.apache.commons.jxpath.ri.model.NodeIterator;
import org.apache.commons.jxpath.ri.model.NodePointer;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;

public class NamespaceResolverTest {

    private NamespaceResolver resolver;

    @Before
    public void setUp() {
        resolver = new NamespaceResolver();
    }

    @After
    public void tearDown() {
        resolver = null;
    }

    @Test
    public void testRegisterNamespace() {
        resolver.registerNamespace("prefix1", "uri1");
        resolver.registerNamespace("prefix2", "uri2");
        assertEquals("uri1", resolver.getNamespaceURI("prefix1"));
        assertEquals("uri2", resolver.getNamespaceURI("prefix2"));
    }

    @Test(expected = IllegalStateException.class)
    public void testRegisterNamespaceSealed() {
        resolver.seal();
        resolver.registerNamespace("prefix1", "uri1");
    }

    @Test
    public void testGetNamespaceURI() {
        resolver.registerNamespace("prefix1", "uri1");
        resolver.registerNamespace("prefix2", "uri2");
        assertEquals("uri1", resolver.getNamespaceURI("prefix1"));
        assertEquals("uri2", resolver.getNamespaceURI("prefix2"));
        assertNull(resolver.getNamespaceURI("prefix3"));
    }

    @Test
    public void testGetNamespaceURIInParent() {
        NamespaceResolver parent = new NamespaceResolver();
        parent.registerNamespace("prefix1", "uri1");
        resolver = new NamespaceResolver(parent);
        assertEquals("uri1", resolver.getNamespaceURI("prefix1"));
    }

    @Test
    public void testGetPrefix() {
        resolver.registerNamespace("prefix1", "uri1");
        resolver.registerNamespace("prefix2", "uri2");
        assertEquals("prefix1", resolver.getPrefix("uri1"));
        assertEquals("prefix2", resolver.getPrefix("uri2"));
        assertNull(resolver.getPrefix("uri3"));
    }

    @Test
    public void testGetPrefixInParent() {
        NamespaceResolver parent = new NamespaceResolver();
        parent.registerNamespace("prefix1", "uri1");
        resolver = new NamespaceResolver(parent);
        assertEquals("prefix1", resolver.getPrefix("uri1"));
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
        assertEquals(resolver.isSealed(), clonedResolver.isSealed());
    }
}