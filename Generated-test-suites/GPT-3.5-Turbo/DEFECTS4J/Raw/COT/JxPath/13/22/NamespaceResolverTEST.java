import org.junit.Assert;
import org.junit.Test;
import org.apache.commons.jxpath.ri.model.NodeIterator;
import org.apache.commons.jxpath.ri.model.NodePointer;
import org.apache.commons.jxpath.Pointer;

public class NamespaceResolverTest {

    @Test
    public void testRegisterNamespace() {
        NamespaceResolver resolver = new NamespaceResolver();
        resolver.registerNamespace("prefix", "namespaceURI");
        
        String result = resolver.getNamespaceURI("prefix");
        Assert.assertEquals("namespaceURI", result);
    }
    
    @Test(expected = IllegalStateException.class)
    public void testRegisterNamespaceSealed() {
        NamespaceResolver resolver = new NamespaceResolver();
        resolver.seal();
        
        resolver.registerNamespace("prefix", "namespaceURI");
    }
    
    @Test
    public void testSetNamespaceContextPointer() {
        NamespaceResolver resolver = new NamespaceResolver();
        NodePointer pointer = new NodePointer();
        
        resolver.setNamespaceContextPointer(pointer);
        
        Pointer result = resolver.getNamespaceContextPointer();
        Assert.assertEquals(pointer, result);
    }
    
    @Test
    public void testGetNamespaceURI() {
        NamespaceResolver resolver = new NamespaceResolver();
        resolver.registerNamespace("prefix", "namespaceURI");
        
        String result = resolver.getNamespaceURI("prefix");
        Assert.assertEquals("namespaceURI", result);
    }
    
    @Test
    public void testGetNamespaceURINull() {
        NamespaceResolver resolver = new NamespaceResolver();
        
        String result = resolver.getNamespaceURI("prefix");
        Assert.assertNull(result);
    }
    
    @Test
    public void testGetNamespaceURIParent() {
        NamespaceResolver parent = new NamespaceResolver();
        parent.registerNamespace("prefix", "namespaceURI");
        NamespaceResolver resolver = new NamespaceResolver(parent);
        
        String result = resolver.getNamespaceURI("prefix");
        Assert.assertEquals("namespaceURI", result);
    }
    
    @Test
    public void testGetPrefix() {
        NamespaceResolver resolver = new NamespaceResolver();
        resolver.registerNamespace("prefix", "namespaceURI");
        
        String result = resolver.getPrefix("namespaceURI");
        Assert.assertEquals("prefix", result);
    }
    
    @Test
    public void testGetPrefixNull() {
        NamespaceResolver resolver = new NamespaceResolver();
        
        String result = resolver.getPrefix("namespaceURI");
        Assert.assertNull(result);
    }
    
    @Test
    public void testGetPrefixParent() {
        NamespaceResolver parent = new NamespaceResolver();
        parent.registerNamespace("prefix", "namespaceURI");
        NamespaceResolver resolver = new NamespaceResolver(parent);
        
        String result = resolver.getPrefix("namespaceURI");
        Assert.assertEquals("prefix", result);
    }
    
    @Test
    public void testIsSealedFalse() {
        NamespaceResolver resolver = new NamespaceResolver();
        
        boolean result = resolver.isSealed();
        Assert.assertFalse(result);
    }
    
    @Test
    public void testSeal() {
        NamespaceResolver resolver = new NamespaceResolver();
        
        resolver.seal();
        
        boolean result = resolver.isSealed();
        Assert.assertTrue(result);
    }

    @Test
    public void testClone() {
        NamespaceResolver resolver = new NamespaceResolver();
        
        NamespaceResolver clonedResolver = (NamespaceResolver) resolver.clone();
        
        boolean result = clonedResolver instanceof NamespaceResolver;
        Assert.assertTrue(result);
        Assert.assertFalse(clonedResolver.isSealed());
    }
}