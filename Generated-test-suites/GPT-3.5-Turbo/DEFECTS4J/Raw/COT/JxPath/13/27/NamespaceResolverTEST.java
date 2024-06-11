import org.junit.Test;
import static org.junit.Assert.*;

public class NamespaceResolverTest {

    @Test
    public void testRegisterNamespace() {
        NamespaceResolver resolver = new NamespaceResolver();
        resolver.registerNamespace("prefix1", "namespace1");
        assertEquals("namespace1", resolver.getNamespaceURI("prefix1"));
    }

    @Test
    public void testSetNamespaceContextPointer() {
        NodePointer pointer = new NodePointer();
        NamespaceResolver resolver = new NamespaceResolver();
        resolver.setNamespaceContextPointer(pointer);
        assertEquals(pointer, resolver.getNamespaceContextPointer());
    }

    @Test
    public void testGetNamespaceURI() {
        NamespaceResolver resolver = new NamespaceResolver();
        resolver.registerNamespace("prefix1", "namespace1");
        resolver.registerNamespace("prefix2", "namespace2");
        assertEquals("namespace1", resolver.getNamespaceURI("prefix1"));
        assertEquals("namespace2", resolver.getNamespaceURI("prefix2"));
    }

    @Test
    public void testGetPrefix() {
        NamespaceResolver resolver = new NamespaceResolver();
        resolver.registerNamespace("prefix1", "namespace1");
        resolver.registerNamespace("prefix2", "namespace2");
        assertEquals("prefix1", resolver.getPrefix("namespace1"));
        assertEquals("prefix2", resolver.getPrefix("namespace2"));
    }

    @Test
    public void testSeal() {
        NamespaceResolver resolver = new NamespaceResolver();
        resolver.seal();
        assertTrue(resolver.isSealed());
    }
}