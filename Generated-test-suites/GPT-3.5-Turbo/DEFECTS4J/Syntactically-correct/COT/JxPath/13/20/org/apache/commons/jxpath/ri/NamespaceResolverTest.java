import static org.junit.Assert.*;
import org.junit.Test;

public class NamespaceResolverTest {

    @Test
    public void testRegisterNamespace() {
        NamespaceResolver namespaceResolver = new NamespaceResolver();
        namespaceResolver.registerNamespace("prefix", "namespaceURI");
        String result = namespaceResolver.getNamespaceURI("prefix");
        assertEquals("namespaceURI", result);
    }
    
    @Test
    public void testSetNamespaceContextPointer() {
        NamespaceResolver namespaceResolver = new NamespaceResolver();
        NodePointer pointer = new NodePointer();
        namespaceResolver.setNamespaceContextPointer(pointer);
        Pointer result = namespaceResolver.getNamespaceContextPointer();
        assertEquals(pointer, result);
    }
    
    @Test
    public void testGetNamespaceURI() {
        NamespaceResolver namespaceResolver = new NamespaceResolver();
        namespaceResolver.registerNamespace("prefix", "namespaceURI");
        String result = namespaceResolver.getNamespaceURI("prefix");
        assertEquals("namespaceURI", result);
    }
    
    @Test
    public void testGetPrefix() {
        NamespaceResolver namespaceResolver = new NamespaceResolver();
        namespaceResolver.registerNamespace("prefix", "namespaceURI");
        String result = namespaceResolver.getPrefix("namespaceURI");
        assertEquals("prefix", result);
    }
    
    @Test
    public void testIsSealed() {
        NamespaceResolver namespaceResolver = new NamespaceResolver();
        boolean result = namespaceResolver.isSealed();
        assertFalse(result);
    }
    
    @Test
    public void testSeal() {
        NamespaceResolver namespaceResolver = new NamespaceResolver();
        namespaceResolver.seal();
        boolean result = namespaceResolver.isSealed();
        assertTrue(result);
    }
    
    @Test
    public void testClone() {
        NamespaceResolver namespaceResolver = new NamespaceResolver();
        Object result = namespaceResolver.clone();
        assertNotNull(result);
    }
    
}