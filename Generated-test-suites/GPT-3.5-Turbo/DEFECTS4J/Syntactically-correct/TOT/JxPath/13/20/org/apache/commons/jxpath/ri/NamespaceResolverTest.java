import org.junit.*;
import static org.junit.Assert.*;

public class NamespaceResolverTest {

    @Test
    public void testRegisterNamespace() {
        NamespaceResolver resolver = new NamespaceResolver();

        // Typical Use Case
        resolver.registerNamespace("prefix1", "http://namespace1.com");
        assertEquals("http://namespace1.com", resolver.getNamespaceURI("prefix1"));

        // Edge Case: Empty prefix
        resolver.registerNamespace("", "http://empty.com");
        assertEquals("http://empty.com", resolver.getNamespaceURI(""));

        // Edge Case: Empty namespace URI
        resolver.registerNamespace("prefix2", "");
        assertEquals("", resolver.getNamespaceURI("prefix2"));
    }

    @Test
    public void testSetNamespaceContextPointer() {
        NamespaceResolver resolver = new NamespaceResolver();

        // Typical Use Case
        NodePointer pointer = new NodePointer();
        resolver.setNamespaceContextPointer(pointer);
        assertEquals(pointer, resolver.getNamespaceContextPointer());
    }

    @Test
    public void testGetNamespaceURI() {
        NamespaceResolver resolver = new NamespaceResolver();

        // Typical Use Case: Existing prefix
        resolver.registerNamespace("prefix1", "http://namespace1.com");
        assertEquals("http://namespace1.com", resolver.getNamespaceURI("prefix1"));

        // Typical Use Case: Absent prefix
        assertNull(resolver.getNamespaceURI("nonexistent"));

        // Edge Case: Empty prefix
        resolver.registerNamespace("", "http://empty.com");
        assertEquals("http://empty.com", resolver.getNamespaceURI(""));

        // Edge Case: Empty prefix, no URI registered
        assertNull(resolver.getNamespaceURI(""));

        // Edge Case: Parent resolver
        NamespaceResolver parentResolver = new NamespaceResolver();
        parentResolver.registerNamespace("prefix2", "http://parent.com");
        resolver = new NamespaceResolver(parentResolver);
        assertEquals("http://parent.com", resolver.getNamespaceURI("prefix2"));
    }

    @Test
    public void testGetPrefix() {
        NamespaceResolver resolver = new NamespaceResolver();

        // Typical Use Case: Existing URI
        resolver.registerNamespace("prefix1", "http://namespace1.com");
        assertEquals("prefix1", resolver.getPrefix("http://namespace1.com"));

        // Typical Use Case: Absent URI
        assertNull(resolver.getPrefix("http://nonexistent.com"));

        // Edge Case: Empty prefix
        resolver.registerNamespace("", "http://empty.com");
        assertEquals("", resolver.getPrefix("http://empty.com"));

        // Edge Case: Empty prefix, no URI registered
        assertNull(resolver.getPrefix(""));

        // Edge Case: Parent resolver
        NamespaceResolver parentResolver = new NamespaceResolver();
        parentResolver.registerNamespace("prefix2", "http://parent.com");
        resolver = new NamespaceResolver(parentResolver);
        assertEquals("prefix2", resolver.getPrefix("http://parent.com"));
    }

    @Test
    public void testIsSealed() {
        NamespaceResolver resolver = new NamespaceResolver();

        // Typical Use Case: Unsealed resolver
        assertFalse(resolver.isSealed());

        // Typical Use Case: Sealed resolver
        resolver.seal();
        assertTrue(resolver.isSealed());
    }

    @Test
    public void testClone() {
        NamespaceResolver resolver = new NamespaceResolver();

        // Typical Use Case: Cloning an unsealed resolver
        NamespaceResolver clonedResolver = (NamespaceResolver) resolver.clone();
        assertEquals(resolver.isSealed(), clonedResolver.isSealed());

        // Typical Use Case: Cloning a sealed resolver
        resolver.seal();
        clonedResolver = (NamespaceResolver) resolver.clone();
        assertEquals(resolver.isSealed(), clonedResolver.isSealed());
    }
}