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
    
    @Test(expected = IllegalStateException.class)
    public void testRegisterNamespaceSealed() {
        resolver.seal();
        resolver.registerNamespace("prefix", "uri");
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
    public void testGetPrefixNotFound() {
        assertNull(resolver.getPrefix("uri"));
    }

    @Test
    public void testGetPrefixParent() {
        NamespaceResolver parent = new NamespaceResolver();
        parent.registerNamespace("prefix", "uri");
        resolver = new NamespaceResolver(parent);
        assertEquals("prefix", resolver.getPrefix("uri"));
    }

    @Test
    public void testClone() {
        resolver.registerNamespace("prefix", "uri");
        NamespaceResolver clone = (NamespaceResolver) resolver.clone();
        assertEquals("uri", clone.getNamespaceURI("prefix"));
    }
}