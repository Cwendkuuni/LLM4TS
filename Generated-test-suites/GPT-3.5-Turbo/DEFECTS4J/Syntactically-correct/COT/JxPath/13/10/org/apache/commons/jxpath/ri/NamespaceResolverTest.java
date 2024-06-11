
import org.junit.Test;

public class NamespaceResolverTest {

    @Test
    public void testRegisterNamespace() {
        NamespaceResolver resolver = new NamespaceResolver();
        resolver.registerNamespace("prefix", "uri");
        
        String actualUri = resolver.getNamespaceURI("prefix");
        assertEquals("uri", actualUri);
    }
    
    @Test
    public void testSetNamespaceContextPointer() {
        NamespaceResolver resolver = new NamespaceResolver();
        NodePointer pointer = new NodePointer();
        resolver.setNamespaceContextPointer(pointer);
        
        Pointer actualPointer = resolver.getNamespaceContextPointer();
        assertEquals(pointer, actualPointer);
    }
    
    @Test
    public void testGetNamespaceURI() {
        NamespaceResolver resolver = new NamespaceResolver();
        resolver.registerNamespace("prefix", "uri");
        
        String actualUri = resolver.getNamespaceURI("prefix");
        assertEquals("uri", actualUri);
    }

    @Test
    public void testGetPrefix() {
        NamespaceResolver resolver = new NamespaceResolver();
        resolver.registerNamespace("prefix", "uri");
        
        String actualPrefix = resolver.getPrefix("uri");
        assertEquals("prefix", actualPrefix);
    }
    
    @Test
    public void testIsSealed() {
        NamespaceResolver resolver = new NamespaceResolver();
        assertFalse(resolver.isSealed());
        
        resolver.seal();
        
        assertTrue(resolver.isSealed());
    }
    
    @Test
    public void testClone() {
        NamespaceResolver resolver = new NamespaceResolver();
        resolver.registerNamespace("prefix", "uri");
        
        NamespaceResolver clonedResolver = (NamespaceResolver) resolver.clone();
        
        assertEquals(resolver.getNamespaceURI("prefix"), clonedResolver.getNamespaceURI("prefix"));
        assertFalse(clonedResolver.isSealed());
    }
    
}
