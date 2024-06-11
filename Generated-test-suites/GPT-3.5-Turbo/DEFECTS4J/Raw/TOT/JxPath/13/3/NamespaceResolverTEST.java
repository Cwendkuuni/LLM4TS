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
    public void testRegisterNamespace_validArgs_success() {
        resolver.registerNamespace("prefix", "http://www.example.com");

        String actualUri = resolver.getNamespaceURI("prefix");

        assertEquals("http://www.example.com", actualUri);
    }

    @Test(expected = IllegalStateException.class)
    public void testRegisterNamespace_sealedResolver_exceptionThrown() {
        resolver.seal();
        resolver.registerNamespace("prefix", "http://www.example.com");
    }

    @Test
    public void testGetNamespaceURI_definedPrefixInResolver_success() {
        resolver.registerNamespace("prefix", "http://www.example.com");

        String actualUri = resolver.getNamespaceURI("prefix");

        assertEquals("http://www.example.com", actualUri);
    }

    @Test
    public void testGetNamespaceURI_definedPrefixInParentResolver_success() {
        NamespaceResolver parentResolver = new NamespaceResolver();
        parentResolver.registerNamespace("prefix", "http://www.example.com");
        resolver = new NamespaceResolver(parentResolver);

        String actualUri = resolver.getNamespaceURI("prefix");

        assertEquals("http://www.example.com", actualUri);
    }

    @Test
    public void testGetNamespaceURI_undefinedPrefix_success() {
        resolver.registerNamespace("prefix", "http://www.example.com");

        String actualUri = resolver.getNamespaceURI("undefined");

        assertNull(actualUri);
    }

    @Test
    public void testGetPrefix_definedUriInResolver_success() {
        resolver.registerNamespace("prefix", "http://www.example.com");

        String actualPrefix = resolver.getPrefix("http://www.example.com");

        assertEquals("prefix", actualPrefix);
    }

    @Test
    public void testGetPrefix_definedUriInParentResolver_success() {
        NamespaceResolver parentResolver = new NamespaceResolver();
        parentResolver.registerNamespace("prefix", "http://www.example.com");
        resolver = new NamespaceResolver(parentResolver);

        String actualPrefix = resolver.getPrefix("http://www.example.com");

        assertEquals("prefix", actualPrefix);
    }

    @Test
    public void testGetPrefix_undefinedUri_success() {
        resolver.registerNamespace("prefix", "http://www.example.com");

        String actualPrefix = resolver.getPrefix("http://www.undefined.com");

        assertNull(actualPrefix);
    }

    @Test
    public void testSetAndGetNamespaceContextPointer_success() {
        NodePointer pointer = new NodePointer();
        resolver.setNamespaceContextPointer(pointer);

        Pointer actualPointer = resolver.getNamespaceContextPointer();

        assertEquals(pointer, actualPointer);
    }

    @Test
    public void testSealResolver_success() {
        resolver.seal();

        assertTrue(resolver.isSealed());
    }

    @Test
    public void testClone_success() {
        resolver.seal();

        NamespaceResolver clonedResolver = (NamespaceResolver) resolver.clone();

        assertEquals(resolver.isSealed(), clonedResolver.isSealed());
    }

    @Test
    public void testClone_failure() throws CloneNotSupportedException {
        resolver.seal();

        NamespaceResolver clonedResolver = (NamespaceResolver) resolver.clone();

        resolver.unseal();

        assertFalse(clonedResolver.isSealed());
    }

}