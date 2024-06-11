import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertTrue;
import static org.junit.Assert.assertFalse;

import org.junit.Before;
import org.junit.Test;

public class NamespaceResolverTest {

    private NamespaceResolver namespaceResolver;

    @Before
    public void setUp() {
        namespaceResolver = new NamespaceResolver();
    }

    @Test
    public void testRegisterNamespace() {
        namespaceResolver.registerNamespace("prefix1", "http://www.example.com/namespace1");
        namespaceResolver.registerNamespace("prefix2", "http://www.example.com/namespace2");

        assertEquals("http://www.example.com/namespace1", namespaceResolver.getNamespaceURI("prefix1"));
        assertEquals("http://www.example.com/namespace2", namespaceResolver.getNamespaceURI("prefix2"));
        assertNull(namespaceResolver.getNamespaceURI("prefix3"));
    }

    @Test
    public void testGetNamespaceURI() {
        namespaceResolver.registerNamespace("prefix1", "http://www.example.com/namespace1");
        namespaceResolver.registerNamespace("prefix2", "http://www.example.com/namespace2");

        assertEquals("http://www.example.com/namespace1", namespaceResolver.getNamespaceURI("prefix1"));
        assertEquals("http://www.example.com/namespace2", namespaceResolver.getNamespaceURI("prefix2"));
        assertNull(namespaceResolver.getNamespaceURI("prefix3"));
    }

    @Test
    public void testGetPrefix() {
        namespaceResolver.registerNamespace("prefix1", "http://www.example.com/namespace1");
        namespaceResolver.registerNamespace("prefix2", "http://www.example.com/namespace2");

        assertEquals("prefix1", namespaceResolver.getPrefix("http://www.example.com/namespace1"));
        assertEquals("prefix2", namespaceResolver.getPrefix("http://www.example.com/namespace2"));
        assertNull(namespaceResolver.getPrefix("http://www.example.com/namespace3"));
    }

    @Test
    public void testGetPrefixWithExternalRegistrations() {
        namespaceResolver.registerNamespace("prefix1", "http://www.example.com/namespace1");
        
        NodePointer externalPointer1 = new NodePointer();
        externalPointer1.setNamespaceURI("http://www.example.com/namespace2");
        externalPointer1.getName().setName("prefix2");
        namespaceResolver.setNamespaceContextPointer(externalPointer1);

        assertEquals("prefix2", namespaceResolver.getPrefix("http://www.example.com/namespace2"));
        assertEquals("prefix1", namespaceResolver.getPrefix("http://www.example.com/namespace1"));
        assertNull(namespaceResolver.getPrefix("http://www.example.com/namespace3"));
    }

    @Test
    public void testGetNamespaceContextPointer() {
        NodePointer externalPointer1 = new NodePointer();
        namespaceResolver.setNamespaceContextPointer(externalPointer1);
        assertEquals(externalPointer1, namespaceResolver.getNamespaceContextPointer());
    }

    @Test
    public void testSeal() {
        namespaceResolver.registerNamespace("prefix1", "http://www.example.com/namespace1");
        namespaceResolver.seal();

        assertTrue(namespaceResolver.isSealed());
    }

    @Test
    public void testClone() {
        namespaceResolver.registerNamespace("prefix1", "http://www.example.com/namespace1");
        NamespaceResolver clone = (NamespaceResolver) namespaceResolver.clone();

        assertEquals("http://www.example.com/namespace1", clone.getNamespaceURI("prefix1"));
        assertFalse(clone.isSealed());
    }
}