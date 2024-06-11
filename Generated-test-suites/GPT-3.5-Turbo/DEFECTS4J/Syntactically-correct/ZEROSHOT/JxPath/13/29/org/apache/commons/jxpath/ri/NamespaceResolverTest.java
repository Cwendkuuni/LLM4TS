import static org.junit.Assert.*;
import org.junit.Before;
import org.junit.Test;
import org.apache.commons.jxpath.Pointer;
import org.apache.commons.jxpath.ri.model.NodeIterator;
import org.apache.commons.jxpath.ri.model.NodePointer;

public class NamespaceResolverTest {

    private NamespaceResolver resolver;

    @Before
    public void setup() {
        resolver = new NamespaceResolver();
        resolver.registerNamespace("prefix1", "http://www.example.com/ns1");
        resolver.registerNamespace("prefix2", "http://www.example.com/ns2");
        resolver.registerNamespace("prefix3", "http://www.example.com/ns3");
    }

    @Test
    public void testRegisterNamespace() {
        NamespaceResolver childResolver = new NamespaceResolver(resolver);
        childResolver.registerNamespace("prefix4", "http://www.example.com/ns4");
        String prefix4Uri = childResolver.getNamespaceURI("prefix4");
        assertEquals("http://www.example.com/ns4", prefix4Uri);
    }

    @Test
    public void testGetNamespaceURI() {
        String ns1Uri = resolver.getNamespaceURI("prefix1");
        assertEquals("http://www.example.com/ns1", ns1Uri);
        
        String ns3Uri = resolver.getNamespaceURI("prefix3");
        assertEquals("http://www.example.com/ns3", ns3Uri);
        
        String ns2Uri = resolver.getNamespaceURI("prefix2");
        assertEquals("http://www.example.com/ns2", ns2Uri);

        String ns4Uri = resolver.getNamespaceURI("prefix4");
        assertNull(ns4Uri);
    }

    @Test
    public void testGetPrefix() {
        String prefix1 = resolver.getPrefix("http://www.example.com/ns1");
        assertEquals("prefix1", prefix1);

        String prefix2 = resolver.getPrefix("http://www.example.com/ns2");
        assertEquals("prefix2", prefix2);

        String prefix3 = resolver.getPrefix("http://www.example.com/ns3");
        assertEquals("prefix3", prefix3);

        String prefix4 = resolver.getPrefix("http://www.example.com/ns4");
        assertNull(prefix4);
    }

    @Test
    public void testSeal() {
        resolver.seal();
        
        try {
            resolver.registerNamespace("prefix5", "http://www.example.com/ns5");
            fail("Expected IllegalStateException was not thrown");
        } catch (IllegalStateException e) {
            assertTrue(true);
        }
    }

    @Test
    public void testClone() {
        NamespaceResolver clonedResolver = (NamespaceResolver) resolver.clone();
        assertFalse(clonedResolver.isSealed());

        clonedResolver.seal();
        assertTrue(clonedResolver.isSealed());
        assertFalse(resolver.isSealed());
    }

}