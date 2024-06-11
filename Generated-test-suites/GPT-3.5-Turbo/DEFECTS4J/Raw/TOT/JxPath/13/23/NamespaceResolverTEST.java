import static org.junit.Assert.*;

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
        resolver.registerNamespace("prefix1", "http://www.namespace1.com");
        String namespaceURI = resolver.getNamespaceURI("prefix1");
        assertEquals("http://www.namespace1.com", namespaceURI);
    }

    @Test
    public void testRegisterMultipleNamespaces() {
        resolver.registerNamespace("prefix1", "http://www.namespace1.com");
        resolver.registerNamespace("prefix2", "http://www.namespace2.com");
        resolver.registerNamespace("prefix3", "http://www.namespace3.com");

        String namespaceURI1 = resolver.getNamespaceURI("prefix1");
        assertEquals("http://www.namespace1.com", namespaceURI1);

        String namespaceURI2 = resolver.getNamespaceURI("prefix2");
        assertEquals("http://www.namespace2.com", namespaceURI2);

        String namespaceURI3 = resolver.getNamespaceURI("prefix3");
        assertEquals("http://www.namespace3.com", namespaceURI3);
    }

    @Test(expected = IllegalStateException.class)
    public void testRegisterNamespaceSealed() {
        resolver.seal(); // Seal the resolver to simulate an already sealed state
        resolver.registerNamespace("prefix1", "http://www.namespace1.com");
    }

    @Test
    public void testSetNamespaceContextPointer() {
        NodePointer pointer = new NodePointer();
        resolver.setNamespaceContextPointer(pointer);

        Pointer contextPointer = resolver.getNamespaceContextPointer();
        assertEquals(pointer, contextPointer);
    }

    @Test
    public void testGetNamespaceURI() {
        resolver.registerNamespace("prefix1", "http://www.namespace1.com");
        resolver.registerNamespace("prefix2", "http://www.namespace2.com");

        String namespaceURI1 = resolver.getNamespaceURI("prefix1");
        assertEquals("http://www.namespace1.com", namespaceURI1);

        String namespaceURI2 = resolver.getNamespaceURI("prefix2");
        assertEquals("http://www.namespace2.com", namespaceURI2);

        String namespaceURI3 = resolver.getNamespaceURI("prefix3");
        assertNull(namespaceURI3);
    }

    @Test
    public void testGetPrefix() {
        resolver.registerNamespace("prefix1", "http://www.namespace1.com");
        resolver.registerNamespace("prefix2", "http://www.namespace2.com");
        resolver.registerNamespace("prefix3", "http://www.namespace3.com");

        String prefix1 = resolver.getPrefix("http://www.namespace1.com");
        assertEquals("prefix1", prefix1);

        String prefix2 = resolver.getPrefix("http://www.namespace2.com");
        assertEquals("prefix2", prefix2);

        String prefix3 = resolver.getPrefix("http://www.namespace3.com");
        assertEquals("prefix3", prefix3);

        String prefix4 = resolver.getPrefix("http://www.namespace4.com");
        assertNull(prefix4);
    }

    @Test
    public void testGetPrefixWithParentResolver() {
        NamespaceResolver parentResolver = new NamespaceResolver();
        parentResolver.registerNamespace("prefix1", "http://www.namespace1.com");
        resolver = new NamespaceResolver(parentResolver);
        resolver.registerNamespace("prefix2", "http://www.namespace2.com");

        String prefix1 = resolver.getPrefix("http://www.namespace1.com");
        assertEquals("prefix1", prefix1);

        String prefix2 = resolver.getPrefix("http://www.namespace2.com");
        assertEquals("prefix2", prefix2);
    }

    @Test
    public void testClone() {
        resolver.registerNamespace("prefix1", "http://www.namespace1.com");
        resolver.registerNamespace("prefix2", "http://www.namespace2.com");
        resolver.seal();

        NamespaceResolver cloneResolver = (NamespaceResolver) resolver.clone();
        assertNotSame(resolver, cloneResolver);
        
        String namespaceURI1 = cloneResolver.getNamespaceURI("prefix1");
        assertEquals("http://www.namespace1.com", namespaceURI1);

        String namespaceURI2 = cloneResolver.getNamespaceURI("prefix2");
        assertEquals("http://www.namespace2.com", namespaceURI2);
    }

}