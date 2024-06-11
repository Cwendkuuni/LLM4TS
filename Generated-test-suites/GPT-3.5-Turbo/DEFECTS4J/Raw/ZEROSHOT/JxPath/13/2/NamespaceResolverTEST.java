import org.junit.Before;
import org.junit.Test;
import static org.junit.Assert.*;

public class NamespaceResolverTest {
    
    private NamespaceResolver resolver;
    
    @Before
    public void setUp() {
        resolver = new NamespaceResolver();
    }
    
    @Test
    public void testRegisterNamespace() {
        resolver.registerNamespace("prefix1", "namespace1");
        resolver.registerNamespace("prefix2", "namespace2");
        
        assertEquals("namespace1", resolver.getNamespaceURI("prefix1"));
        assertEquals("namespace2", resolver.getNamespaceURI("prefix2"));
    }
    
    @Test(expected = IllegalStateException.class)
    public void testRegisterNamespaceSealed() {
        resolver.seal();
        resolver.registerNamespace("prefix", "namespace");
    }
    
    @Test
    public void testGetNamespaceURI() {
        assertNull(resolver.getNamespaceURI("prefix"));
        
        resolver.registerNamespace("prefix", "namespace");
        
        assertEquals("namespace", resolver.getNamespaceURI("prefix"));
    }
    
    @Test
    public void testGetNamespaceURI_External() {
        resolver.setNamespaceContextPointer(new NodePointer());
        resolver.getNamespaceURI("prefix");
        
        // Test for invocation of pointer.getNamespaceURI()
    }
    
    @Test
    public void testGetPrefix() {
        assertNull(resolver.getPrefix("namespace"));
        
        resolver.registerNamespace("prefix", "namespace");
        
        assertEquals("prefix", resolver.getPrefix("namespace"));
    }
    
    @Test
    public void testGetPrefix_External() {
        resolver.setNamespaceContextPointer(new NodePointer());
        resolver.getPrefix("namespace");
        
        // Test for invocation of pointer.namespaceIterator()
    }
    
    @Test
    public void testIsSealed() {
        assertFalse(resolver.isSealed());
        
        resolver.seal();
        
        assertTrue(resolver.isSealed());
    }
    
    @Test
    public void testClone() {
        NamespaceResolver clone = (NamespaceResolver) resolver.clone();
        
        assertNotSame(resolver, clone);
        assertEquals(resolver.isSealed(), clone.isSealed());
    }
}