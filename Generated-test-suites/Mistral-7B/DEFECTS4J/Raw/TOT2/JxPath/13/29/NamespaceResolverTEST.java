import org.apache.commons.jxpath.ri.NamespaceResolver;
import org.apache.commons.jxpath.ri.model.NodePointer;
import org.junit.Test;
import static org.junit.Assert.*;

public class NamespaceResolverTest {

    @Test
    public void testNewInstance() {
        NamespaceResolver resolver = new NamespaceResolver();
        assertNull(resolver.getNamespaceURI("prefix"));
        assertNull(resolver.getPrefix("http://example.com/ns"));
        assertFalse(resolver.isSealed());
    }

    @Test
    public void testRegisterNamespace() {
        NamespaceResolver resolver = new NamespaceResolver();
        resolver.registerNamespace("prefix", "http://example.com/ns");
        assertEquals("http://example.com/ns", resolver.getNamespaceURI("prefix"));
        assertEquals("prefix", resolver.getPrefix("http://example.com/ns"));
        assertFalse(resolver.isSealed());
    }

    @Test
    public void testSealedResolver() {
        NamespaceResolver resolver = new NamespaceResolver();
        resolver.registerNamespace("prefix", "http://example.com/ns");
        resolver.seal();
        try {
            resolver.registerNamespace("prefix2", "http://example.com/ns2");
            fail("should not be able to register namespaces on a sealed NamespaceResolver");
        } catch (IllegalStateException e) {
            // expected
        }
    }

    @Test
    public void testGetNamespaceURI() {
        NamespaceResolver resolver = new NamespaceResolver();
        resolver.registerNamespace("prefix", "http://example.com/ns");
        NodePointer pointer = new NodePointer("element", null);
        resolver.setNamespaceContextPointer(pointer);
        assertEquals("http://example.com/ns", resolver.getNamespaceURI("prefix"));
        assertEquals(null, resolver.getNamespaceURI("otherPrefix"));
    }

    @Test
    public void testGetPrefix() {
        NamespaceResolver resolver = new NamespaceResolver();
        resolver.registerNamespace("prefix", "http://example.com/ns");
        assertEquals("prefix", resolver.getPrefix("http://example.com/ns"));
        assertEquals(null, resolver.getPrefix("http://example.com/other"));
        NodePointer pointer = new NodePointer("element", null);
        resolver.setNamespaceContextPointer(pointer);
        assertEquals("prefix", resolver.getPrefix("prefix"));
        assertEquals("ns", resolver.getNamespaceURI("prefix:elementName"));
    }

    @Test(expected = IllegalStateException.class)
    public void testGetNamespaceURIAfterSeal() {
        NamespaceResolver resolver = new NamespaceResolver();
        resolver.registerNamespace("prefix", "http://example.com/ns");
        resolver.seal();
        assertEquals("http://example.com/ns", resolver.getNamespaceURI("prefix"));
        resolver.getNamespaceURI("prefix2"); // should not be able to do this
    }

    @Test(expected = IllegalStateException.class)
    public void testGetPrefixAfterSeal() {
        NamespaceResolver resolver = new NamespaceResolver();
        resolver.registerNamespace("prefix", "http://example.com/ns");
        resolver.seal();
        assertEquals("prefix", resolver.getPrefix("prefix"));
        resolver.getPrefix("prefix2"); // should not be able to do this
    }
}