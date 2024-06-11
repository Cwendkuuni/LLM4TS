import static org.junit.Assert.*;
import org.junit.Test;
import org.apache.commons.jxpath.ri.NamespaceResolver;
import org.apache.commons.jxpath.Pointer;
import org.apache.commons.jxpath.ri.model.NodePointer;
import org.apache.commons.jxpath.ri.model.NodeIterator;

public class NamespaceResolverTest {

    @Test
    public void testRegisterNamespace() {
        NamespaceResolver resolver = new NamespaceResolver();
        resolver.registerNamespace("test", "http://test.com");
        assertEquals("http://test.com", resolver.getNamespaceURI("test"));
    }

    @Test
    public void testSetNamespaceContextPointer() {
        NodePointer nodePointer = new NodePointer();
        NamespaceResolver resolver = new NamespaceResolver();
        resolver.setNamespaceContextPointer(nodePointer);
        assertEquals(nodePointer, resolver.getNamespaceContextPointer());
    }

    @Test
    public void testGetNamespaceURI() {
        NamespaceResolver resolver = new NamespaceResolver();
        resolver.registerNamespace("test", "http://test.com");
        assertEquals("http://test.com", resolver.getNamespaceURI("test"));
    }

    @Test
    public void testGetPrefix() {
        NamespaceResolver resolver = new NamespaceResolver();
        resolver.registerNamespace("test", "http://test.com");
        assertEquals("test", resolver.getPrefix("http://test.com"));
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
        NamespaceResolver parent = new NamespaceResolver();
        NamespaceResolver resolver = new NamespaceResolver(parent);
        NamespaceResolver clonedResolver = (NamespaceResolver) resolver.clone();
        assertFalse(clonedResolver.isSealed());
    }

}