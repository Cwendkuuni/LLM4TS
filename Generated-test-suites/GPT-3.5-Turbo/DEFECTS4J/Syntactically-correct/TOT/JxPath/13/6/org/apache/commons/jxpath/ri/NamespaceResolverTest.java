import org.apache.commons.jxpath.Pointer;
import org.apache.commons.jxpath.ri.model.NodePointer;
import org.junit.Assert;
import org.junit.Test;

public class NamespaceResolverTest {

    @Test
    public void testRegisterNamespace() {
        NamespaceResolver resolver = new NamespaceResolver();
        resolver.registerNamespace("prefix", "http://example.com");
        
        String uri = resolver.getNamespaceURI("prefix");
        Assert.assertEquals("http://example.com", uri);
        
        String prefix = resolver.getPrefix("http://example.com");
        Assert.assertEquals("prefix", prefix);
    }
    
    @Test
    public void testGetNamespaceURI_NullPrefix() {
        NamespaceResolver resolver = new NamespaceResolver();
        resolver.registerNamespace("prefix", "http://example.com");
        
        String uri = resolver.getNamespaceURI(null);
        Assert.assertNull(uri);
    }
    
    @Test
    public void testGetNamespaceURI_UndefinedPrefix() {
        NamespaceResolver resolver = new NamespaceResolver();
        resolver.registerNamespace("prefix", "http://example.com");
        
        String uri = resolver.getNamespaceURI("undef");
        Assert.assertNull(uri);
    }
    
    @Test
    public void testGetNamespaceURI_ContextNodePrefix() {
        NamespaceResolver resolver = new NamespaceResolver();
        resolver.setNamespaceContextPointer(new NodePointer());
        
        String uri = resolver.getNamespaceURI("prefix");
        Assert.assertNull(uri);
    }
    
    @Test
    public void testGetNamespaceURI_ParentResolverPrefix() {
        NamespaceResolver parentResolver = new NamespaceResolver();
        parentResolver.registerNamespace("prefix", "http://example.com");
        
        NamespaceResolver resolver = new NamespaceResolver(parentResolver);
        
        String uri = resolver.getNamespaceURI("prefix");
        Assert.assertEquals("http://example.com", uri);
    }
    
    @Test
    public void testGetPrefix() {
        NamespaceResolver resolver = new NamespaceResolver();
        resolver.registerNamespace("prefix", "http://example.com");
        
        String prefix = resolver.getPrefix("http://example.com");
        Assert.assertEquals("prefix", prefix);
    }
    
    @Test
    public void testGetPrefix_NullValue() {
        NamespaceResolver resolver = new NamespaceResolver();
        resolver.registerNamespace("prefix", "http://example.com");
        
        String prefix = resolver.getPrefix(null);
        Assert.assertNull(prefix);
    }
    
    @Test
    public void testGetPrefix_UndefinedValue() {
        NamespaceResolver resolver = new NamespaceResolver();
        resolver.registerNamespace("prefix", "http://example.com");
        
        String prefix = resolver.getPrefix("http://undefined.com");
        Assert.assertNull(prefix);
    }
    
    @Test
    public void testGetPrefix_ParentResolverValue() {
        NamespaceResolver parentResolver = new NamespaceResolver();
        parentResolver.registerNamespace("prefix", "http://example.com");
        
        NamespaceResolver resolver = new NamespaceResolver(parentResolver);
        
        String prefix = resolver.getPrefix("http://example.com");
        Assert.assertEquals("prefix", prefix);
    }
    
    @Test
    public void testIsSealed_False() {
        NamespaceResolver resolver = new NamespaceResolver();
        
        Assert.assertFalse(resolver.isSealed());
    }
    
    @Test
    public void testIsSealed_True() {
        NamespaceResolver resolver = new NamespaceResolver();
        resolver.seal();
        
        Assert.assertTrue(resolver.isSealed());
    }
    
    @Test
    public void testClone() {
        NamespaceResolver resolver = new NamespaceResolver();
        resolver.registerNamespace("prefix", "http://example.com");
        
        NamespaceResolver clonedResolver = (NamespaceResolver) resolver.clone();
        
        String uri = clonedResolver.getNamespaceURI("prefix");
        Assert.assertEquals("http://example.com", uri);
        
        String prefix = clonedResolver.getPrefix("http://example.com");
        Assert.assertEquals("prefix", prefix);
        
        Assert.assertFalse(clonedResolver.isSealed());
    }
    
    @Test
    public void testClone_Sealed() {
        NamespaceResolver resolver = new NamespaceResolver();
        resolver.registerNamespace("prefix", "http://example.com");
        resolver.seal();
        
        NamespaceResolver clonedResolver = (NamespaceResolver) resolver.clone();
        
        String uri = clonedResolver.getNamespaceURI("prefix");
        Assert.assertEquals("http://example.com", uri);
        
        String prefix = clonedResolver.getPrefix("http://example.com");
        Assert.assertEquals("prefix", prefix);
        
        Assert.assertFalse(clonedResolver.isSealed());
    }
}