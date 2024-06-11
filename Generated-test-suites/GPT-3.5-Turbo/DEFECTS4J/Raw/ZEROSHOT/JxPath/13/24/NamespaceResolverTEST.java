import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

public class NamespaceResolverTest {
    private NamespaceResolver namespaceResolver;
    
    @Before
    public void setUp() {
        namespaceResolver = new NamespaceResolver();
    }
    
    @Test
    public void testRegisterNamespace() {
        String prefix = "testPrefix";
        String namespaceURI = "testNamespaceURI";
        
        namespaceResolver.registerNamespace(prefix, namespaceURI);
        
        String actualURI = namespaceResolver.getNamespaceURI(prefix);
        Assert.assertEquals(namespaceURI, actualURI);
    }
    
    @Test
    public void testSetNamespaceContextPointer() {
        NodePointer pointer = new NodePointer();
        
        namespaceResolver.setNamespaceContextPointer(pointer);
        
        Pointer actualPointer = namespaceResolver.getNamespaceContextPointer();
        Assert.assertEquals(pointer, actualPointer);
    }
    
    @Test
    public void testGetNamespaceURI() {
        String prefix = "testPrefix";
        String namespaceURI = "testNamespaceURI";
        namespaceResolver.registerNamespace(prefix, namespaceURI);
        
        String actualURI = namespaceResolver.getNamespaceURI(prefix);
        Assert.assertEquals(namespaceURI, actualURI);
    }
    
    @Test
    public void testGetPrefix() {
        String prefix = "testPrefix";
        String namespaceURI = "testNamespaceURI";
        namespaceResolver.registerNamespace(prefix, namespaceURI);
        
        String actualPrefix = namespaceResolver.getPrefix(namespaceURI);
        Assert.assertEquals(prefix, actualPrefix);
    }
    
    @Test
    public void testIsSealed() {
        namespaceResolver.seal();
        
        boolean actualSealed = namespaceResolver.isSealed();
        Assert.assertTrue(actualSealed);
    }
    
    @Test
    public void testClone() {
        NamespaceResolver clonedResolver = (NamespaceResolver) namespaceResolver.clone();
        
        Assert.assertNotSame(namespaceResolver, clonedResolver);
        Assert.assertFalse(clonedResolver.isSealed());
    }
}