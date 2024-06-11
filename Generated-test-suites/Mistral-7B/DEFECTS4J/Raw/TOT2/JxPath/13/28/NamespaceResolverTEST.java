import org.apache.commons.jxpath.ri.NamespaceResolver;
import org.apache.commons.jxpath.ri.NodePointer;
import org.junit.Test;
import static org.junit.Assert.*;

public class NamespaceResolverTest {

    @Test
    public void testConstructor() {
        NamespaceResolver resolver = new NamespaceResolver();
        assertNull(resolver.parent);
        assertNotNull(resolver.namespaceMap);
        assertNull(resolver.reverseMap);
        assertNull(resolver.pointer);
        assertFalse(resolver.isSealed());
    }

    @Test
    public void testRegisterNamespace() {
        NamespaceResolver resolver = new NamespaceResolver();
        resolver.registerNamespace("xsi", "http://www.w3.org/2001/XMLSchema-instance");
        assertEquals("http://www.w3.org/2001/XMLSchema-instance", resolver.getNamespaceURI("xsi"));
        resolver.registerNamespace("xmlns", "http://www.w3.org/2000/xmlns/");
        assertNull(resolver.getNamespaceURI("xsi"));
        assertEquals("http://www.w3.org/2000/xmlns/", resolver.getNamespaceURI("xmlns"));
    }

    @Test
    public void testSetNamespaceContextPointer() {
        NamespaceResolver resolver = new NamespaceResolver();
        NodePointer pointer = new NodePointer("test");
        resolver.setNamespaceContextPointer(pointer);
        assertEquals(pointer, resolver.getNamespaceContextPointer());
    }

    @Test(expected = IllegalStateException.class)
    public void testRegisterNamespaceAfterSeal() {
        NamespaceResolver resolver = new NamespaceResolver();
        resolver.registerNamespace("xsi", "http://www.w3.org/2001/XMLSchema-instance");
        resolver.seal();
        resolver.registerNamespace("xmlns", "http://www.w3.org/2000/xmlns/");
    }

    @Test
    public void testGetNamespaceURI() {
        NamespaceResolver resolver = new NamespaceResolver();
        resolver.registerNamespace("xsi", "http://www.w3.org/2001/XMLSchema-instance");
        assertNull(resolver.getNamespaceURI(""));
        assertNull(resolver.getNamespaceURI("xmlns"));
        assertEquals("http://www.w3.org/2001/XMLSchema-instance", resolver.getNamespaceURI("xsi"));
    }

    @Test
    public void testGetPrefix() {
        NamespaceResolver resolver = new NamespaceResolver();
        resolver.registerNamespace("xsi", "http://www.w3.org/2001/XMLSchema-instance");
        assertEquals("xsi", resolver.getPrefix("http://www.w3.org/2001/XMLSchema-instance"));
        resolver.registerNamespace("xmlns", "http://www.w3.org/2000/xmlns/");
        assertEquals("xmlns", resolver.getPrefix("http://www.w3.org/2000/xmlns/"));
        assertNull(resolver.getPrefix(""));
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
        NamespaceResolver original = new NamespaceResolver();
        original.registerNamespace("xsi", "http://www.w3.org/2001/XMLSchema-instance");
        NamespaceResolver cloned = (NamespaceResolver) original.clone();
        assertEquals("http://www.w3.org/2001/XMLSchema-instance", cloned.getNamespaceURI("xsi"));
    }
}