import static org.junit.Assert.assertEquals;
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
        resolver.registerNamespace("prefix", "namespaceURI");
        assertEquals("namespaceURI", resolver.getNamespaceURI("prefix"));
        resolver.setNamespaceContextPointer(new NodePointer());
        assertEquals("namespaceURI", resolver.getNamespaceURI("prefix"));
    }

    @Test
    public void testGetPrefix() {
        resolver.registerNamespace("prefix", "namespaceURI");
        assertEquals("prefix", resolver.getPrefix("namespaceURI"));
        resolver.setNamespaceContextPointer(new NodePointer());
        assertEquals("prefix", resolver.getPrefix("namespaceURI"));
    }

    @Test
    public void testIsSealed() {
        assertEquals(false, resolver.isSealed());
    }

    @Test
    public void testSeal() {
        resolver.seal();
        assertEquals(true, resolver.isSealed());
    }

    @Test
    public void testClone() {
        NamespaceResolver clonedResolver = (NamespaceResolver) resolver.clone();
        assertEquals(false, clonedResolver.isSealed());
    }
}