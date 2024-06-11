import org.junit.Before;
import org.junit.Test;
import static org.junit.Assert.*;

public class NamespaceResolverTest {

    private NamespaceResolver namespaceResolver;

    @Before
    public void setUp() {
        namespaceResolver = new NamespaceResolver();
    }

    @Test
    public void testRegisterNamespace() {
        namespaceResolver.registerNamespace("prefix", "uri");
        String namespaceURI = namespaceResolver.getNamespaceURI("prefix");
        assertEquals("uri", namespaceURI);
    }

    @Test
    public void testSetNamespaceContextPointer() {
        NodePointer pointer = new NodePointer();
        namespaceResolver.setNamespaceContextPointer(pointer);
        Pointer contextPointer = namespaceResolver.getNamespaceContextPointer();
        assertEquals(pointer, contextPointer);
    }

    @Test
    public void testGetNamespaceURI() {
        namespaceResolver.registerNamespace("prefix", "uri");
        String namespaceURI = namespaceResolver.getNamespaceURI("prefix");
        assertEquals("uri", namespaceURI);
    }

    @Test
    public void testGetPrefix() {
        namespaceResolver.registerNamespace("prefix", "uri");
        String prefix = namespaceResolver.getPrefix("uri");
        assertEquals("prefix", prefix);
    }

    @Test
    public void testIsSealed() {
        namespaceResolver.seal();
        boolean isSealed = namespaceResolver.isSealed();
        assertTrue(isSealed);
    }

    @Test
    public void testClone() {
        NamespaceResolver clone = (NamespaceResolver) namespaceResolver.clone();
        assertNotNull(clone);
        assertNotSame(namespaceResolver, clone);
    }

}