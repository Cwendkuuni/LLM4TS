import org.junit.Test;
import static org.junit.Assert.*;

public class NamespaceResolverTest {

    @Test
    public void testRegisterNamespace() {
        NamespaceResolver resolver = new NamespaceResolver();
        resolver.registerNamespace("test", "http://test.com");
        assertEquals("http://test.com", resolver.getNamespaceURI("test"));
    }

    @Test
    public void testGetNamespaceURI() {
        NamespaceResolver resolver = new NamespaceResolver();
        resolver.registerNamespace("test", "http://test.com");
        assertEquals("http://test.com", resolver.getNamespaceURI("test"));
        assertNull(resolver.getNamespaceURI("unknown"));
    }

    @Test
    public void testGetPrefix() {
        NamespaceResolver resolver = new NamespaceResolver();
        resolver.registerNamespace("test", "http://test.com");
        assertEquals("test", resolver.getPrefix("http://test.com"));
        assertNull(resolver.getPrefix("http://unknown.com"));
    }

    @Test
    public void testSetNamespaceContextPointer() {
        NamespaceResolver resolver = new NamespaceResolver();
        // Create a mock NodePointer for testing
        NodePointer mockPointer = new NodePointerMock("testPointer");
        resolver.setNamespaceContextPointer(mockPointer);
        assertEquals(mockPointer, resolver.getNamespaceContextPointer());
    }

    @Test
    public void testSeal() {
        NamespaceResolver resolver = new NamespaceResolver();
        resolver.seal();
        assertTrue(resolver.isSealed());
    }

    @Test
    public void testClone() {
        NamespaceResolver parentResolver = new NamespaceResolver();
        NamespaceResolver childResolver = new NamespaceResolver(parentResolver);
        NamespaceResolver clonedResolver = (NamespaceResolver) childResolver.clone();
        assertNotSame(childResolver, clonedResolver);
    }
}