import static org.junit.Assert.*;
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
        resolver.registerNamespace("prefix", "uri");
        assertEquals("uri", resolver.getNamespaceURI("prefix"));
    }
    
    @Test(expected = IllegalStateException.class)
    public void testRegisterNamespaceSealed() {
        resolver.seal();
        resolver.registerNamespace("prefix", "uri");
    }
    
    @Test
    public void testSetNamespaceContextPointer() {
        NodePointer pointer = new NodePointer();
        resolver.setNamespaceContextPointer(pointer);
        assertEquals(pointer, resolver.getNamespaceContextPointer());
    }
    
    @Test
    public void testGetNamespaceURI() {
        resolver.registerNamespace("prefix", "uri");
        assertEquals("uri", resolver.getNamespaceURI("prefix"));
    }
    
    @Test
    public void testGetNamespaceURIExternal() {
        NodePointer pointer = new NodePointer();
        pointer.setNamespaceURI("prefix", "uri");
        resolver.setNamespaceContextPointer(pointer);
        assertEquals("uri", resolver.getNamespaceURI("prefix"));
    }
    
    @Test
    public void testGetNamespaceURIParent() {
        NamespaceResolver parent = new NamespaceResolver();
        parent.registerNamespace("prefix", "uri");
        resolver = new NamespaceResolver(parent);
        assertEquals("uri", resolver.getNamespaceURI("prefix"));
    }
    
    @Test
    public void testGetPrefix() {
        resolver.registerNamespace("prefix", "uri");
        assertEquals("prefix", resolver.getPrefix("uri"));
    }
    
    @Test
    public void testGetPrefixExternal() {
        NodePointer pointer = new NodePointer();
        pointer.setNamespaceURI("prefix", "uri");
        resolver.setNamespaceContextPointer(pointer);
        assertEquals("prefix", resolver.getPrefix("uri"));
    }
    
    @Test
    public void testGetPrefixParent() {
        NamespaceResolver parent = new NamespaceResolver();
        parent.registerNamespace("prefix", "uri");
        resolver = new NamespaceResolver(parent);
        assertEquals("prefix", resolver.getPrefix("uri"));
    }
    
    @Test
    public void testIsSealed() {
        assertFalse(resolver.isSealed());
    }
    
    @Test
    public void testSeal() {
        resolver.seal();
        assertTrue(resolver.isSealed());
    }
    
    @Test
    public void testClone() {
        NamespaceResolver clone = (NamespaceResolver) resolver.clone();
        assertNotSame(resolver, clone);
        assertFalse(clone.isSealed());
    }
}