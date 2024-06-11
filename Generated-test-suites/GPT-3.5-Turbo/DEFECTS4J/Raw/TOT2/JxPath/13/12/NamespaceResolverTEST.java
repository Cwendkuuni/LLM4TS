import static org.junit.Assert.*;
import org.junit.Test;

public class NamespaceResolverTest {

    private NamespaceResolver resolver;

    @Before
    public void setUp() {
        resolver = new NamespaceResolver();
    }

    @Test
    public void testRegisterNamespace() {
        resolver.registerNamespace("test", "http://test.com");
        assertEquals("http://test.com", resolver.getNamespaceURI("test"));
    }

    @Test
    public void testGetNamespaceURI_EmptyPrefix() {
        resolver.registerNamespace("", "http://default.com");
        assertEquals("http://default.com", resolver.getNamespaceURI(""));
    }

    @Test
    public void testGetPrefix() {
        resolver.registerNamespace("test", "http://test.com");
        assertEquals("test", resolver.getPrefix("http://test.com"));
    }

    @Test
    public void testGetNamespaceURI_ExternalNamespace() {
        resolver.registerNamespace("test", "http://test.com");
        NodePointer pointer = new NodePointer();
        pointer.setNamespaceURI("http://test.com");
        resolver.setNamespaceContextPointer(pointer);
        assertEquals("http://test.com", resolver.getNamespaceURI("test"));
    }

    @Test
    public void testGetPrefix_ExternalNamespace() {
        resolver.registerNamespace("test", "http://test.com");
        NodePointer pointer = new NodePointer();
        pointer.setNamespaceURI("http://test.com");
        pointer.setName(new QName("test"));
        resolver.setNamespaceContextPointer(pointer);
        assertEquals("test", resolver.getPrefix("http://test.com"));
    }

    @Test(expected = IllegalStateException.class)
    public void testSealedNamespaceResolver_RegisterNamespace() {
        resolver.seal();
        resolver.registerNamespace("test", "http://test.com");
    }

    @Test
    public void testCloneNamespaceResolver() {
        NamespaceResolver clone = (NamespaceResolver) resolver.clone();
        assertNotSame(resolver, clone);
        assertEquals(resolver.getNamespaceURI("test"), clone.getNamespaceURI("test"));
    }
}