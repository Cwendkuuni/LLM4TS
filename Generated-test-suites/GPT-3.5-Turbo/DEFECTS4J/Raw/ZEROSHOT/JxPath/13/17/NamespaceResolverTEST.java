import org.junit.Test;
import static org.junit.Assert.*;

public class NamespaceResolverTest {

    @Test
    public void testRegisterNamespace() {
        NamespaceResolver resolver = new NamespaceResolver();
        resolver.registerNamespace("prefix1", "uri1");
        resolver.registerNamespace("prefix2", "uri2");

        assertEquals("uri1", resolver.getNamespaceURI("prefix1"));
        assertEquals("uri2", resolver.getNamespaceURI("prefix2"));
    }

    @Test
    public void testRegisterNamespace_InvalidPrefix() {
        NamespaceResolver resolver = new NamespaceResolver();
        resolver.registerNamespace("", "uri1");

        assertNull(resolver.getNamespaceURI(""));
    }

    @Test(expected = IllegalStateException.class)
    public void testRegisterNamespace_SealedResolver() {
        NamespaceResolver parent = new NamespaceResolver();
        parent.seal();

        parent.registerNamespace("prefix1", "uri1");
    }

    @Test
    public void testGetNamespaceURI() {
        NamespaceResolver parent = new NamespaceResolver();
        parent.registerNamespace("prefix1", "uri1");

        NamespaceResolver resolver = new NamespaceResolver(parent);
        resolver.registerNamespace("prefix2", "uri2");

        assertEquals("uri1", resolver.getNamespaceURI("prefix1"));
        assertEquals("uri2", resolver.getNamespaceURI("prefix2"));
    }

    @Test
    public void testGetNamespaceURI_UndefinedPrefix() {
        NamespaceResolver parent = new NamespaceResolver();
        parent.registerNamespace("prefix1", "uri1");

        NamespaceResolver resolver = new NamespaceResolver(parent);

        assertNull(resolver.getNamespaceURI("prefix2"));
    }

    @Test
    public void testGetNamespaceURI_ContextPointerDefined() {
        NodePointerMock pointer = new NodePointerMock();
        NamespaceResolver resolver = new NamespaceResolver();
        resolver.setNamespaceContextPointer(pointer);

        pointer.setNamespaceURI("prefix1", "uri1");

        assertEquals("uri1", resolver.getNamespaceURI("prefix1"));
    }

    @Test
    public void testGetPrefix() {
        NamespaceResolver parent = new NamespaceResolver();
        parent.registerNamespace("prefix1", "uri1");

        NamespaceResolver resolver = new NamespaceResolver(parent);
        resolver.registerNamespace("prefix2", "uri2");

        assertEquals("prefix1", resolver.getPrefix("uri1"));
        assertEquals("prefix2", resolver.getPrefix("uri2"));
    }

    @Test
    public void testGetPrefix_UndefinedNamespaceURI() {
        NamespaceResolver parent = new NamespaceResolver();
        parent.registerNamespace("prefix1", "uri1");

        NamespaceResolver resolver = new NamespaceResolver(parent);

        assertNull(resolver.getPrefix("uri2"));
    }

    @Test
    public void testGetPrefix_ExternalNamespace() {
        NodePointerMock pointer = new NodePointerMock();
        NamespaceResolver resolver = new NamespaceResolver();
        resolver.setNamespaceContextPointer(pointer);

        pointer.setNamespaceURI("prefix1", "uri1");

        assertEquals("prefix1", resolver.getPrefix("uri1"));
    }

    private class NodePointerMock extends NodePointer {

        private Map<String, String> namespaceURIByPrefix;

        public NodePointerMock() {
            super(null);
            namespaceURIByPrefix = new HashMap<>();
        }

        public void setNamespaceURI(String prefix, String uri) {
            namespaceURIByPrefix.put(prefix, uri);
        }

        @Override
        public String getNamespaceURI(String prefix) {
            return namespaceURIByPrefix.get(prefix);
        }

        // Other methods are not implemented for testing purposes
    }

}