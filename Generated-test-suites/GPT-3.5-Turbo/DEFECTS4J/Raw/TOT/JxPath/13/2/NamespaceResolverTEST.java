import org.junit.Test;
import static org.junit.Assert.*;

public class NamespaceResolverTest {

    @Test
    public void testRegisterNamespace() {
        NamespaceResolver resolver = new NamespaceResolver();
        resolver.registerNamespace("test", "http://www.example.com/test");
        assertEquals("http://www.example.com/test", resolver.getNamespaceURI("test"));
    }

    @Test(expected = IllegalStateException.class)
    public void testRegisterNamespaceSealed() {
        NamespaceResolver resolver = new NamespaceResolver();
        resolver.seal();
        resolver.registerNamespace("test", "http://www.example.com/test");
    }

    @Test
    public void testGetNamespaceURI() {
        NamespaceResolver resolver = new NamespaceResolver();
        resolver.registerNamespace("test", "http://www.example.com/test");
        assertEquals("http://www.example.com/test", resolver.getNamespaceURI("test"));
    }

    @Test
    public void testGetNamespaceURINotFound() {
        NamespaceResolver resolver = new NamespaceResolver();
        assertNull(resolver.getNamespaceURI("test"));
    }

    @Test
    public void testGetPrefix() {
        NamespaceResolver resolver = new NamespaceResolver();
        resolver.registerNamespace("test", "http://www.example.com/test");
        assertEquals("test", resolver.getPrefix("http://www.example.com/test"));
    }

    @Test
    public void testGetPrefixNotFound() {
        NamespaceResolver resolver = new NamespaceResolver();
        assertNull(resolver.getPrefix("http://www.example.com/test"));
    }

    @Test
    public void testGetPrefixFromParent() {
        NamespaceResolver parent = new NamespaceResolver();
        parent.registerNamespace("test", "http://www.example.com/test");
        NamespaceResolver resolver = new NamespaceResolver(parent);
        assertEquals("test", resolver.getPrefix("http://www.example.com/test"));
    }

    @Test
    public void testClone() {
        NamespaceResolver resolver = new NamespaceResolver();
        resolver.registerNamespace("test", "http://www.example.com/test");
        NamespaceResolver clonedResolver = (NamespaceResolver) resolver.clone();
        assertEquals("http://www.example.com/test", clonedResolver.getNamespaceURI("test"));
    }

    @Test
    public void testSeal() {
        NamespaceResolver resolver = new NamespaceResolver();
        resolver.registerNamespace("test", "http://www.example.com/test");
        resolver.seal();
        assertTrue(resolver.isSealed());
    }

    @Test
    public void testSealParent() {
        NamespaceResolver parent = new NamespaceResolver();
        NamespaceResolver resolver = new NamespaceResolver(parent);
        resolver.registerNamespace("test", "http://www.example.com/test");
        resolver.seal();
        assertTrue(parent.isSealed());
    }

    @Test
    public void testSetNamespaceContextPointer() {
        NamespaceResolver resolver = new NamespaceResolver();
        NodePointer pointer = new NodePointer();
        resolver.setNamespaceContextPointer(pointer);
        assertEquals(pointer, resolver.getNamespaceContextPointer());
    }

    @Test
    public void testGetNamespaceContextPointerFromParent() {
        NamespaceResolver parent = new NamespaceResolver();
        NodePointer pointer = new NodePointer();
        parent.setNamespaceContextPointer(pointer);
        NamespaceResolver resolver = new NamespaceResolver(parent);
        assertEquals(pointer, resolver.getNamespaceContextPointer());
    }

}