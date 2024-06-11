import org.junit.Assert;
import org.junit.Test;

public class NamespaceResolverTest {

    @Test
    public void testRegisterNamespace() {
        NamespaceResolver resolver = new NamespaceResolver();
        resolver.registerNamespace("test", "http://www.test.com");
        String namespaceURI = resolver.getNamespaceURI("test");
        Assert.assertEquals("http://www.test.com", namespaceURI);
    }
    
    @Test
    public void testSetNamespaceContextPointer() {
        NamespaceResolver resolver = new NamespaceResolver();
        NodePointer pointer = new NodePointer();
        resolver.setNamespaceContextPointer(pointer);
        Pointer contextPointer = resolver.getNamespaceContextPointer();
        Assert.assertEquals(pointer, contextPointer);
    }
    
    @Test
    public void testGetNamespaceURI() {
        NamespaceResolver resolver = new NamespaceResolver();
        resolver.registerNamespace("test", "http://www.test.com");
        String namespaceURI = resolver.getNamespaceURI("test");
        Assert.assertEquals("http://www.test.com", namespaceURI);
    }
    
    @Test
    public void testGetPrefix() {
        NamespaceResolver resolver = new NamespaceResolver();
        resolver.registerNamespace("test", "http://www.test.com");
        String prefix = resolver.getPrefix("http://www.test.com");
        Assert.assertEquals("test", prefix);
    }
    
    @Test
    public void testIsSealed() {
        NamespaceResolver resolver = new NamespaceResolver();
        resolver.seal();
        boolean sealed = resolver.isSealed();
        Assert.assertTrue(sealed);
    }
    
    @Test
    public void testClone() {
        NamespaceResolver resolver = new NamespaceResolver();
        NamespaceResolver clonedResolver = (NamespaceResolver) resolver.clone();
        Assert.assertNotSame(resolver, clonedResolver);
        Assert.assertFalse(clonedResolver.isSealed());
    }
}