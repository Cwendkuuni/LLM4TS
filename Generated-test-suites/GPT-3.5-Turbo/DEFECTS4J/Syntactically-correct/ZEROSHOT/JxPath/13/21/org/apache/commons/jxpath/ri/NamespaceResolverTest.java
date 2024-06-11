import org.junit.Assert;
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
        resolver.registerNamespace("prefix", "http://www.example.com");
        String uri = resolver.getNamespaceURI("prefix");
        Assert.assertEquals("http://www.example.com", uri);
    }
    
    @Test(expected = IllegalStateException.class)
    public void testRegisterNamespaceSealed() {
        resolver.seal();
        resolver.registerNamespace("prefix", "http://www.example.com");
    }
    
    @Test
    public void testGetNamespaceURI() {
        resolver.registerNamespace("prefix", "http://www.example.com");
        String uri = resolver.getNamespaceURI("prefix");
        Assert.assertEquals("http://www.example.com", uri);
        
        String uri2 = resolver.getNamespaceContextPointer().getNamespaceURI("prefix");
        Assert.assertEquals("http://www.example.com", uri2);
    }
    
    @Test
    public void testGetNamespaceURINotFound() {
        String uri = resolver.getNamespaceURI("prefix");
        Assert.assertNull(uri);
        
        String uri2 = resolver.getNamespaceContextPointer().getNamespaceURI("prefix");
        Assert.assertNull(uri2);
    }
    
    @Test
    public void testGetPrefix() {
        resolver.registerNamespace("prefix", "http://www.example.com");
        String prefix = resolver.getPrefix("http://www.example.com");
        Assert.assertEquals("prefix", prefix);
    }
    
    @Test
    public void testGetPrefixNotFound() {
        String prefix = resolver.getPrefix("http://www.example.com");
        Assert.assertNull(prefix);
    }
    
    @Test
    public void testSeal() {
        resolver.seal();
        Assert.assertTrue(resolver.isSealed());
    }
    
    @Test
    public void testClone() {
        NamespaceResolver clone = (NamespaceResolver) resolver.clone();
        Assert.assertEquals(resolver.isSealed(), clone.isSealed());
    }
}