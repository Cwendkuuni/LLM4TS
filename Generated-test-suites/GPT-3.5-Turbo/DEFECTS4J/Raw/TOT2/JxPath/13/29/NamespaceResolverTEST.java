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
        resolver.registerNamespace("prefix1", "http://namespace1");
        assertEquals("http://namespace1", resolver.getNamespaceURI("prefix1"));
    }

    @Test
    public void testGetNamespaceURI() {
        resolver.registerNamespace("prefix2", "http://namespace2");
        assertEquals("prefix2", resolver.getPrefix("http://namespace2"));
    }

    @Test
    public void testSetNamespaceContextPointer() {
        resolver.setNamespaceContextPointer(new NodePointer());
        assertNotNull(resolver.getNamespaceContextPointer());
    }

    @Test
    public void testGetPrefix() {
        resolver.registerNamespace("prefix3", "http://namespace3");
        assertEquals("prefix3", resolver.getPrefix("http://namespace3"));
    }

    @Test
    public void testIsSealed() {
        assertFalse(resolver.isSealed());
    }

    @Test
    public void testClone() {
        NamespaceResolver clonedResolver = (NamespaceResolver) resolver.clone();
        assertEquals(resolver.getNamespaceURI("prefix"), clonedResolver.getNamespaceURI("prefix"));
    }

    @Test(expected = IllegalStateException.class)
    public void testSeal() {
        resolver.seal();
        resolver.registerNamespace("prefix4", "http://namespace4");
    }

}