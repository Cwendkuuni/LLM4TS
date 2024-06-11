import static org.junit.Assert.*;
import org.junit.Test;

public class NamespaceResolverTest {

    @Test
    public void testRegisterNamespace() {
        NamespaceResolver resolver = new NamespaceResolver();
        resolver.registerNamespace("prefix1", "http://namespace1.com");
        assertEquals("http://namespace1.com", resolver.getNamespaceURI("prefix1"));
    }

    @Test
    public void testGetNamespaceURI() {
        NamespaceResolver resolver = new NamespaceResolver();
        resolver.registerNamespace("prefix2", "http://namespace2.com");
        assertEquals("http://namespace2.com", resolver.getNamespaceURI("prefix2"));
    }

    @Test
    public void testGetPrefix() {
        NamespaceResolver resolver = new NamespaceResolver();
        resolver.registerNamespace("prefix3", "http://namespace3.com");
        assertEquals("prefix3", resolver.getPrefix("http://namespace3.com"));
    }

    @Test
    public void testSeal() {
        NamespaceResolver resolver = new NamespaceResolver();
        resolver.seal();
        assertTrue(resolver.isSealed());
    }

    @Test
    public void testClone() {
        NamespaceResolver parentResolver = new NamespaceResolver();
        NamespaceResolver resolver = new NamespaceResolver(parentResolver);
        NamespaceResolver clonedResolver = (NamespaceResolver) resolver.clone();
        assertNotSame(resolver, clonedResolver);
    }

}