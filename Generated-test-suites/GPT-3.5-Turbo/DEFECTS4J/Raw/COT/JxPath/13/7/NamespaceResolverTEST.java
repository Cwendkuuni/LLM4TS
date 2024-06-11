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
        resolver.registerNamespace("prefix", "uri");
        assertEquals("uri", resolver.getNamespaceURI("prefix"));
    }

    @Test
    public void testSetNamespaceContextPointer() {
        NodePointer pointer = new NodePointer();
        resolver.setNamespaceContextPointer(pointer);
        assertEquals(pointer, resolver.getNamespaceContextPointer());
    }

    @Test
    public void testGetNamespaceURI() {
        resolver.registerNamespace("prefix", "uri");
        assertEquals("uri", resolver.getNamespaceURI("prefix"));
    }

    @Test
    public void testGetPrefix() {
        resolver.registerNamespace("prefix", "uri");
        assertEquals("prefix", resolver.getPrefix("uri"));
    }

    @Test
    public void testIsSealed() {
        assertFalse(resolver.isSealed());
    }

    @Test
    public void testSeal() {
        resolver.seal();
        assertTrue(resolver.isSealed());
    }

    @Test
    public void testClone() {
        NamespaceResolver clonedResolver = (NamespaceResolver) resolver.clone();
        assertNotSame(resolver, clonedResolver);
    }
}