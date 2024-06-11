import org.junit.Assert;
import org.junit.Test;

public class NamespaceResolverTest {

    @Test
    public void testRegisterNamespace() {
        // Test case for registerNamespace method
        NamespaceResolver resolver = new NamespaceResolver();
        resolver.registerNamespace("prefix1", "namespace1");

        String actualURI = resolver.getNamespaceURI("prefix1");
        String expectedURI = "namespace1";
        Assert.assertEquals(expectedURI, actualURI);
    }

    @Test
    public void testSetAndGetNamespaceContextPointer() {
        // Test case for setNamespaceContextPointer and getNamespaceContextPointer methods
        NamespaceResolver resolver = new NamespaceResolver();
        NodePointer pointer = new NodePointer();
        resolver.setNamespaceContextPointer(pointer);

        NodePointer actualPointer = resolver.getNamespaceContextPointer();
        Assert.assertEquals(pointer, actualPointer);
    }

    @Test
    public void testGetNamespaceURI() {
        // Test case for getNamespaceURI method with a defined prefix
        NamespaceResolver resolver = new NamespaceResolver();
        resolver.registerNamespace("prefix1", "namespace1");

        String actualURI = resolver.getNamespaceURI("prefix1");
        String expectedURI = "namespace1";
        Assert.assertEquals(expectedURI, actualURI);
        
        // Test case for getNamespaceURI method with an undefined prefix
        actualURI = resolver.getNamespaceURI("undefinedPrefix");
        Assert.assertNull(actualURI);
    }
    
    @Test
    public void testGetPrefix() {
        // Test case for getPrefix method with a registered namespaceURI
        NamespaceResolver resolver = new NamespaceResolver();
        resolver.registerNamespace("prefix1", "namespace1");
        
        String actualPrefix = resolver.getPrefix("namespace1");
        String expectedPrefix = "prefix1";
        Assert.assertEquals(expectedPrefix, actualPrefix);
        
        // Test case for getPrefix method with an unregistered namespaceURI
        actualPrefix = resolver.getPrefix("unregisteredURI");
        Assert.assertNull(actualPrefix);
    }

    @Test
    public void testClone() {
        // Test case for clone method
        NamespaceResolver resolver = new NamespaceResolver();
        resolver.registerNamespace("prefix1", "namespace1");
        
        NamespaceResolver clonedResolver = (NamespaceResolver) resolver.clone();
        
        Assert.assertEquals(resolver.getNamespaceURI("prefix1"), clonedResolver.getNamespaceURI("prefix1"));
    }

}