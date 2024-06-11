import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertTrue;

import org.apache.commons.jxpath.Pointer;
import org.apache.commons.jxpath.VariableContext;
import org.apache.commons.jxpath.ri.model.NodePointer;
import org.junit.Before;
import org.junit.Test;

public class NamespaceResolverTest {

    private NamespaceResolver resolver;

    @Before
    public void setup() {
        resolver = new NamespaceResolver();
    }

    @Test
    public void testRegisterNamespace() {
        resolver.registerNamespace("prefix1", "http://example.com");
        assertEquals("http://example.com", resolver.getNamespaceURI("prefix1"));
    }

    @Test(expected = IllegalStateException.class)
    public void testRegisterNamespaceSealedResolver() {
        resolver.seal();
        resolver.registerNamespace("prefix1", "http://example.com");
    }

    @Test
    public void testGetNamespaceURI() {
        resolver.registerNamespace("prefix1", "http://example.com");
        assertEquals("http://example.com", resolver.getNamespaceURI("prefix1"));
    }

    @Test
    public void testGetNamespaceURINotRegistered() {
        assertNull(resolver.getNamespaceURI("prefix1"));
    }

    @Test
    public void testGetNamespaceURIWithParentResolver() {
        NamespaceResolver parentResolver = new NamespaceResolver();
        parentResolver.registerNamespace("prefix1", "http://parent.com");
        resolver = new NamespaceResolver(parentResolver);
        assertEquals("http://parent.com", resolver.getNamespaceURI("prefix1"));
    }

    @Test
    public void testGetPrefix() {
        resolver.registerNamespace("prefix1", "http://example.com");
        assertEquals("prefix1", resolver.getPrefix("http://example.com"));
    }

    @Test
    public void testGetPrefixNotRegistered() {
        assertNull(resolver.getPrefix("http://example.com"));
    }

    @Test
    public void testGetPrefixWithParentResolver() {
        NamespaceResolver parentResolver = new NamespaceResolver();
        parentResolver.registerNamespace("prefix1", "http://parent.com");
        resolver = new NamespaceResolver(parentResolver);
        assertEquals("prefix1", resolver.getPrefix("http://parent.com"));
    }

    @Test
    public void testIsSealed() {
        resolver.seal();
        assertTrue(resolver.isSealed());
    }

    @Test
    public void testClone() {
        resolver.registerNamespace("prefix1", "http://example.com");
        NamespaceResolver clonedResolver = (NamespaceResolver) resolver.clone();
        assertEquals("http://example.com", clonedResolver.getNamespaceURI("prefix1"));
        assertEquals(resolver.isSealed(), clonedResolver.isSealed());
    }
    
    @Test
    public void testSetAndGetNamespaceContextPointer() {
        NodePointer pointer = new NodePointer();
        resolver.setNamespaceContextPointer(pointer);
        assertEquals(pointer, resolver.getNamespaceContextPointer());
    }
    
    @Test
    public void testGetNamespaceContextPointerWithParentResolver() {
        NamespaceResolver parentResolver = new NamespaceResolver();
        NodePointer pointer = new NodePointer();
        parentResolver.setNamespaceContextPointer(pointer);
        resolver = new NamespaceResolver(parentResolver);
        assertEquals(pointer, resolver.getNamespaceContextPointer());
    }

    @Test
    public void testGetNamespaceURIWithPointer() {
        NodePointer pointer = new NodePointer();
        resolver.setNamespaceContextPointer(pointer);
        pointer.registerNamespace("prefix1", "http://example.com");
        assertEquals("http://example.com", resolver.getNamespaceURI("prefix1"));
    }
    
    @Test
    public void testGetNamespaceURIWithPointerAndParentResolver() {
        NamespaceResolver parentResolver = new NamespaceResolver();
        NodePointer pointer = new NodePointer();
        parentResolver.setNamespaceContextPointer(pointer);
        pointer.registerNamespace("prefix1", "http://example.com");
        resolver = new NamespaceResolver(parentResolver);
        assertEquals("http://example.com", resolver.getNamespaceURI("prefix1"));
    }
}