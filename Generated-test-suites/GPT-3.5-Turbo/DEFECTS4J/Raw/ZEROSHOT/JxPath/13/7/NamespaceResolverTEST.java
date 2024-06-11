import static org.junit.Assert.*;
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
        resolver.registerNamespace("prefix1", "namespace1");
        resolver.registerNamespace("prefix2", "namespace2");
        assertEquals("namespace1", resolver.getNamespaceURI("prefix1"));
        assertEquals("namespace2", resolver.getNamespaceURI("prefix2"));
    }

    @Test(expected = IllegalStateException.class)
    public void testRegisterNamespaceSealed() {
        resolver.seal();
        resolver.registerNamespace("prefix", "namespace");
    }

    @Test
    public void testSetNamespaceContextPointer() {
        NodePointer pointer = new NodePointer();
        resolver.setNamespaceContextPointer(pointer);
        assertEquals(pointer, resolver.getNamespaceContextPointer());
    }

    @Test
    public void testGetNamespaceURI() {
        resolver.registerNamespace("prefix1", "namespace1");
        assertEquals("namespace1", resolver.getNamespaceURI("prefix1"));
    }

    @Test
    public void testGetNamespaceURIExternal() {
        NodePointer pointer = new NodePointer();
        pointer.setNamespaceURI("prefix2", "namespace2");
        resolver.setNamespaceContextPointer(pointer);
        assertEquals("namespace2", resolver.getNamespaceURI("prefix2"));
    }

    @Test
    public void testGetNamespaceURIInherited() {
        NodePointer pointer = new NodePointer();
        pointer.setNamespaceURI("prefix1", "namespace1");
        resolver.setNamespaceContextPointer(pointer);
        assertEquals("namespace1", resolver.getNamespaceURI("prefix1"));
    }

    @Test
    public void testGetNamespaceURIUndefined() {
        assertNull(resolver.getNamespaceURI("undefined"));
    }

    @Test
    public void testGetPrefix() {
        resolver.registerNamespace("prefix1", "namespace1");
        assertEquals("prefix1", resolver.getPrefix("namespace1"));
    }

    @Test
    public void testGetPrefixExternal() {
        NodePointer pointer = new NodePointer();
        pointer.setNamespaceURI("prefix2", "namespace2");
        resolver.setNamespaceContextPointer(pointer);
        assertEquals("prefix2", resolver.getPrefix("namespace2"));
    }

    @Test
    public void testGetPrefixInherited() {
        NodePointer pointer = new NodePointer();
        pointer.setNamespaceURI("prefix1", "namespace1");
        resolver.setNamespaceContextPointer(pointer);
        assertEquals("prefix1", resolver.getPrefix("namespace1"));
    }

    @Test
    public void testGetPrefixUndefined() {
        assertNull(resolver.getPrefix("undefined"));
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
        assertEquals(resolver.isSealed(), clonedResolver.isSealed());
    }
}