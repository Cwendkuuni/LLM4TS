
import org.junit.Test;
import org.apache.commons.jxpath.Pointer;
import org.apache.commons.jxpath.ri.model.NodeIterator;
import org.apache.commons.jxpath.ri.model.NodePointer;

public class NamespaceResolverTest {

    @Test
    public void testRegisterNamespace() {
        NamespaceResolver resolver = new NamespaceResolver();
        
        resolver.registerNamespace("prefix1", "http://www.example1.com");
        String namespaceURI = resolver.getNamespaceURI("prefix1");
        assertEquals("http://www.example1.com", namespaceURI);
        
        resolver.registerNamespace("prefix2", "http://www.example2.com");
        namespaceURI = resolver.getNamespaceURI("prefix2");
        assertEquals("http://www.example2.com", namespaceURI);
    }
    
    @Test(expected = IllegalStateException.class)
    public void testRegisterNamespaceOnSealedResolver() {
        NamespaceResolver parentResolver = new NamespaceResolver();
        parentResolver.registerNamespace("prefix", "http://www.example.com");
        parentResolver.seal();
        
        NamespaceResolver childResolver = new NamespaceResolver(parentResolver);
        childResolver.registerNamespace("prefix", "http://www.example.com");
    }
    
    @Test
    public void testGetNamespaceURI() {
        NamespaceResolver resolver = new NamespaceResolver();
        resolver.registerNamespace("prefix", "http://www.example.com");
        
        String namespaceURI = resolver.getNamespaceURI("prefix");
        assertEquals("http://www.example.com", namespaceURI);
        
        namespaceURI = resolver.getNamespaceURI("unknownPrefix");
        assertNull(namespaceURI);
    }
    
    @Test
    public void testGetPrefix() {
        NamespaceResolver resolver = new NamespaceResolver();
        resolver.registerNamespace("prefix", "http://www.example.com");
        
        String prefix = resolver.getPrefix("http://www.example.com");
        assertEquals("prefix", prefix);
        
        prefix = resolver.getPrefix("http://www.unknown.com");
        assertNull(prefix);
    }
    
    @Test
    public void testClone() {
        NamespaceResolver parentResolver = new NamespaceResolver();
        parentResolver.registerNamespace("prefix", "http://www.example.com");
        
        NamespaceResolver childResolver = (NamespaceResolver) parentResolver.clone();
        
        String namespaceURI = childResolver.getNamespaceURI("prefix");
        assertEquals("http://www.example.com", namespaceURI);
    }
}
