import org.junit.Test;
import static org.junit.Assert.*;

public class NamespaceResolverTest {

    @Test
    public void testRegisterNamespace() {
        NamespaceResolver resolver = new NamespaceResolver();
        resolver.registerNamespace("prefix1", "http://www.example.com/ns1");
        resolver.registerNamespace("prefix2", "http://www.example.com/ns2");

        assertEquals("http://www.example.com/ns1", resolver.getNamespaceURI("prefix1"));
        assertEquals("http://www.example.com/ns2", resolver.getNamespaceURI("prefix2"));
        assertNull(resolver.getNamespaceURI("prefix3"));
    }
    
    @Test
    public void testGetPrefix() {
        NamespaceResolver resolver = new NamespaceResolver();
        resolver.registerNamespace("prefix1", "http://www.example.com/ns1");
        resolver.registerNamespace("prefix2", "http://www.example.com/ns2");

        assertEquals("prefix1", resolver.getPrefix("http://www.example.com/ns1"));
        assertEquals("prefix2", resolver.getPrefix("http://www.example.com/ns2"));
        assertNull(resolver.getPrefix("http://www.example.com/ns3"));
    }
    
    @Test
    public void testGetNamespaceURI() {
        NamespaceResolver resolver = new NamespaceResolver();
        resolver.registerNamespace("prefix1", "http://www.example.com/ns1");
        resolver.registerNamespace("prefix2", "http://www.example.com/ns2");

        assertEquals("http://www.example.com/ns1", resolver.getNamespaceURI("prefix1"));
        assertEquals("http://www.example.com/ns2", resolver.getNamespaceURI("prefix2"));
        assertNull(resolver.getNamespaceURI("prefix3"));
    }
    
    @Test
    public void testSetNamespaceContextPointer() {
        NamespaceResolver resolver = new NamespaceResolver();
        NodePointer pointer = new NodePointer();
        resolver.setNamespaceContextPointer(pointer);

        assertEquals(pointer, resolver.getNamespaceContextPointer());
    }
    
    // Test edge cases and exception handling scenarios
    @Test(expected = IllegalStateException.class)
    public void testRegisterNamespaceSealed() {
        NamespaceResolver resolver = new NamespaceResolver();
        resolver.seal();
        resolver.registerNamespace("prefix", "http://www.example.com/ns");
    }

    @Test
    public void testGetNamespaceURIEmptyPrefix() {
        NamespaceResolver resolver = new NamespaceResolver();
        resolver.registerNamespace("", "http://www.example.com/ns");

        assertEquals("http://www.example.com/ns", resolver.getNamespaceURI(""));
    }
    
    @Test
    public void testGetPrefixEmptyNamespaceURI() {
        NamespaceResolver resolver = new NamespaceResolver();
        resolver.registerNamespace("prefix", "");

        assertEquals("prefix", resolver.getPrefix(""));
    }

}