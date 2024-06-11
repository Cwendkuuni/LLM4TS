
import org.junit.Before;
import org.junit.Test;
import org.apache.commons.jxpath.Pointer;
import org.apache.commons.jxpath.ri.model.NodeIterator;
import org.apache.commons.jxpath.ri.model.NodePointer;

public class NamespaceResolverTest {
    private NamespaceResolver resolver;

    @Before
    public void setUp() {
        resolver = new NamespaceResolver();
    }

    @Test
    public void testRegisterNamespace() {
        resolver.registerNamespace("prefix", "http://www.example.com");
        
        String namespaceURI = resolver.getNamespaceURI("prefix");
        assert(namespaceURI.equals("http://www.example.com"));
        
        String prefix = resolver.getPrefix("http://www.example.com");
        assert(prefix.equals("prefix"));
    }
    
    @Test(expected = IllegalStateException.class)
    public void testSealedResolver() {
        resolver.seal();
        resolver.registerNamespace("prefix", "http://www.example.com");
    }
    
    @Test
    public void testSetNamespaceContextPointer() {
        NodePointer pointer = new NodePointer();
        resolver.setNamespaceContextPointer(pointer);
        
        Pointer contextPointer = resolver.getNamespaceContextPointer();
        assert(contextPointer == pointer);
    }
    
    @Test
    public void testGetNamespaceURI() {
        resolver.registerNamespace("prefix", "http://www.example.com");
        resolver.registerNamespace("anotherPrefix", "http://www.example2.com");
        
        String namespaceURI1 = resolver.getNamespaceURI("prefix");
        assert(namespaceURI1.equals("http://www.example.com"));
        
        String namespaceURI2 = resolver.getNamespaceURI("anotherPrefix");
        assert(namespaceURI2.equals("http://www.example2.com"));
        
        String namespaceURI3 = resolver.getNamespaceURI("nonexistentPrefix");
        assert(namespaceURI3 == null);
    }
    
    @Test
    public void testGetPrefix() {
        resolver.registerNamespace("prefix", "http://www.example.com");
        resolver.registerNamespace("anotherPrefix", "http://www.example2.com");
        
        String prefix1 = resolver.getPrefix("http://www.example.com");
        assert(prefix1.equals("prefix"));
        
        String prefix2 = resolver.getPrefix("http://www.example2.com");
        assert(prefix2.equals("anotherPrefix"));
        
        String prefix3 = resolver.getPrefix("http://www.example3.com");
        assert(prefix3 == null);
    }
    
    @Test
    public void testClone() {
        NamespaceResolver clonedResolver = (NamespaceResolver) resolver.clone();
        assert(clonedResolver != resolver);
        assert(clonedResolver.getNamespaceURI("prefix") == null);
        assert(clonedResolver.getPrefix("http://www.example.com") == null);
        assert(clonedResolver.isSealed() == false);
    }
}
