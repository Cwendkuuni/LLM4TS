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
        resolver.registerNamespace("xml", "http://www.w3.org/XML/1998/namespace");
        assertEquals("http://www.w3.org/XML/1998/namespace", resolver.getNamespaceURI("xml"));
    }

    @Test(expected = IllegalStateException.class)
    public void testRegisterNamespaceSealed() {
        resolver.seal();
        resolver.registerNamespace("xml", "http://www.w3.org/XML/1998/namespace");
    }

    @Test
    public void testSetNamespaceContextPointer() {
        NodePointer pointer = new NodePointer();
        resolver.setNamespaceContextPointer(pointer);
        assertSame(pointer, resolver.getNamespaceContextPointer());
    }

    @Test
    public void testGetNamespaceURI() {
        resolver.registerNamespace("xml", "http://www.w3.org/XML/1998/namespace");
        assertEquals("http://www.w3.org/XML/1998/namespace", resolver.getNamespaceURI("xml"));
    }

    @Test
    public void testGetPrefix() {
        resolver.registerNamespace("xml", "http://www.w3.org/XML/1998/namespace");
        assertEquals("xml", resolver.getPrefix("http://www.w3.org/XML/1998/namespace"));
    }

    @Test
    public void testGetPrefixNotFound() {
        assertNull(resolver.getPrefix("http://www.w3.org/XML/1998/namespace"));
    }

    @Test
    public void testGetPrefixWithParentResolver() {
        NamespaceResolver parentResolver = new NamespaceResolver();
        parentResolver.registerNamespace("xml", "http://www.w3.org/XML/1998/namespace");
        resolver.setParent(parentResolver);
        assertEquals("xml", resolver.getPrefix("http://www.w3.org/XML/1998/namespace"));
    }

    @Test
    public void testClone() {
        resolver.registerNamespace("xml", "http://www.w3.org/XML/1998/namespace");
        NamespaceResolver clonedResolver = (NamespaceResolver) resolver.clone();
        assertEquals("http://www.w3.org/XML/1998/namespace", clonedResolver.getNamespaceURI("xml"));
    }

    @Test
    public void testCloneSealed() {
        resolver.seal();
        NamespaceResolver clonedResolver = (NamespaceResolver) resolver.clone();
        assertFalse(clonedResolver.isSealed());
    }

    @Test
    public void testIsSealed() {
        assertFalse(resolver.isSealed());
        resolver.seal();
        assertTrue(resolver.isSealed());
    }

}