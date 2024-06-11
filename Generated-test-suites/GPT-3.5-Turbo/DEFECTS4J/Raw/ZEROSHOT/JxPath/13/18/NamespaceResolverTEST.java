import static org.junit.Assert.*;
import org.junit.Before;
import org.junit.Test;
import org.apache.commons.jxpath.Pointer;
import org.apache.commons.jxpath.ri.model.NodeIterator;
import org.apache.commons.jxpath.ri.model.NodePointer;

public class NamespaceResolverTest {

    private NamespaceResolver resolver;

    @Before
    public void setUp() {
        resolver = new NamespaceResolver();
    }

    @Test
    public void testRegisterNamespace() {
        resolver.registerNamespace("prefix1", "uri1");
        resolver.registerNamespace("prefix2", "uri2");
        resolver.registerNamespace("prefix3", "uri3");

        assertEquals("uri1", resolver.getNamespaceURI("prefix1"));
        assertEquals("uri2", resolver.getNamespaceURI("prefix2"));
        assertEquals("uri3", resolver.getNamespaceURI("prefix3"));
        assertNull(resolver.getNamespaceURI("prefix4"));
    }

    @Test
    public void testGetNamespaceURI() {
        assertNull(resolver.getNamespaceURI("prefix1"));

        resolver.registerNamespace("prefix1", "uri1");

        assertEquals("uri1", resolver.getNamespaceURI("prefix1"));
        assertNull(resolver.getNamespaceURI("prefix2"));
    }

    @Test
    public void testGetPrefix() {
        assertNull(resolver.getPrefix("uri1"));

        resolver.registerNamespace("prefix1", "uri1");

        assertEquals("prefix1", resolver.getPrefix("uri1"));
        assertNull(resolver.getPrefix("uri2"));
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

        assertNotNull(clone);
        assertNotSame(resolver, clone);
        assertEquals(resolver.isSealed(), clone.isSealed());
    }
}