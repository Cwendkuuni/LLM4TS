import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNull;

import org.apache.commons.jxpath.Pointer;
import org.apache.commons.jxpath.ri.NamespaceResolver;
import org.apache.commons.jxpath.ri.model.NodeIterator;
import org.apache.commons.jxpath.ri.model.NodePointer;
import org.junit.Before;
import org.junit.Test;

public class NamespaceResolverTest {
    private NamespaceResolver resolver;
    private NodePointer pointer;
    
    @Before
    public void setUp() {
        resolver = new NamespaceResolver();
        pointer = new NodePointer();
        resolver.setNamespaceContextPointer(pointer);
    }
    
    @Test
    public void testRegisterNamespace() {
        resolver.registerNamespace("x", "http://example.com/x");
        String result = resolver.getNamespaceURI("x");
        assertEquals("http://example.com/x", result);
    }
    
    @Test(expected = IllegalStateException.class)
    public void testRegisterNamespaceSealed() {
        resolver.seal();
        resolver.registerNamespace("x", "http://example.com/x");
    }
    
    @Test
    public void testGetNamespaceURI() {
        pointer.setNamespaceURI("x", "http://example.com/x");
        String result = resolver.getNamespaceURI("x");
        assertEquals("http://example.com/x", result);
    }
    
    @Test
    public void testGetNamespaceURIParent() {
        NamespaceResolver parent = new NamespaceResolver();
        parent.registerNamespace("x", "http://example.com/x");
        resolver = new NamespaceResolver(parent);
        String result = resolver.getNamespaceURI("x");
        assertEquals("http://example.com/x", result);
    }
    
    @Test
    public void testGetNamespaceURINotFound() {
        pointer.setNamespaceURI("y", "http://example.com/y");
        String result = resolver.getNamespaceURI("x");
        assertNull(result);
    }
    
    @Test
    public void testGetPrefix() {
        pointer.setNamespaceURI("x", "http://example.com/x");
        String result = resolver.getPrefix("http://example.com/x");
        assertEquals("x", result);
    }
    
    @Test
    public void testGetPrefixParent() {
        NamespaceResolver parent = new NamespaceResolver();
        parent.registerNamespace("x", "http://example.com/x");
        resolver = new NamespaceResolver(parent);
        String result = resolver.getPrefix("http://example.com/x");
        assertEquals("x", result);
    }
    
    @Test
    public void testGetPrefixNotFound() {
        pointer.setNamespaceURI("y", "http://example.com/y");
        String result = resolver.getPrefix("http://example.com/x");
        assertNull(result);
    }
    
    @Test
    public void testIsSealed() {
        resolver.seal();
        boolean result = resolver.isSealed();
        assertEquals(true, result);
    }
    
    @Test
    public void testClone() {
        NamespaceResolver clone = (NamespaceResolver) resolver.clone();
        clone.seal();
        boolean result = clone.isSealed();
        assertEquals(true, result);
    }
}