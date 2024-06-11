import org.junit.Before;
import org.junit.Test;
import static org.junit.Assert.*;

public class NamespaceResolverTest {

    private NamespaceResolver resolver;

    @Before
    public void setUp() {
        resolver = new NamespaceResolver();
    }

    @Test
    public void testRegisterNamespace() {
        resolver.registerNamespace("prefix1", "http://www.example.com/ns1");
        resolver.registerNamespace("prefix2", "http://www.example.com/ns2");

        String uri1 = resolver.getNamespaceURI("prefix1");
        String uri2 = resolver.getNamespaceURI("prefix2");
        String uri3 = resolver.getNamespaceURI("prefix3");

        assertEquals("http://www.example.com/ns1", uri1);
        assertEquals("http://www.example.com/ns2", uri2);
        assertNull(uri3);
    }

    @Test
    public void testGetPrefix() {
        resolver.registerNamespace("prefix1", "http://www.example.com/ns1");
        resolver.registerNamespace("prefix2", "http://www.example.com/ns2");

        String prefix1 = resolver.getPrefix("http://www.example.com/ns1");
        String prefix2 = resolver.getPrefix("http://www.example.com/ns2");
        String prefix3 = resolver.getPrefix("http://www.example.com/ns3");

        assertEquals("prefix1", prefix1);
        assertEquals("prefix2", prefix2);
        assertNull(prefix3);
    }

    @Test
    public void testGetNamespaceURI() {
        resolver.registerNamespace("prefix1", "http://www.example.com/ns1");
        resolver.registerNamespace("prefix2", "http://www.example.com/ns2");

        String uri1 = resolver.getNamespaceURI("prefix1");
        String uri2 = resolver.getNamespaceURI("prefix2");
        String uri3 = resolver.getNamespaceURI("prefix3");

        assertEquals("http://www.example.com/ns1", uri1);
        assertEquals("http://www.example.com/ns2", uri2);
        assertNull(uri3);
    }

    @Test
    public void testSetAndGetNamespaceContextPointer() {
        NodePointer pointer = new NodePointer();
        resolver.setNamespaceContextPointer(pointer);

        NodePointer retrievedPointer = resolver.getNamespaceContextPointer();

        assertEquals(pointer, retrievedPointer);
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

        assertFalse(clonedResolver.isSealed());
    }
}