import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNull;

import org.apache.commons.jxpath.Pointer;
import org.apache.commons.jxpath.ri.model.NodeIterator;
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
        resolver.registerNamespace("prefix1", "namespace1");
        resolver.registerNamespace("prefix2", "namespace2");

        assertEquals("namespace1", resolver.getNamespaceURI("prefix1"));
        assertEquals("namespace2", resolver.getNamespaceURI("prefix2"));
        assertNull(resolver.getNamespaceURI("prefix3"));
    }

    @Test(expected = IllegalStateException.class)
    public void testRegisterNamespaceSealed() {
        resolver.seal();
        resolver.registerNamespace("prefix1", "namespace1");
    }

    @Test
    public void testSetNamespaceContextPointer() {
        NodePointer pointer = new NodePointer();
        resolver.setNamespaceContextPointer(pointer);

        assertEquals(pointer, resolver.getNamespaceContextPointer());
    }

    @Test
    public void testGetNamespaceURI() {
        resolver.registerNamespace("prefix1", "namespace1");
        resolver.registerNamespace("prefix2", "namespace2");

        assertEquals("namespace1", resolver.getNamespaceURI("prefix1"));
        assertEquals("namespace2", resolver.getNamespaceURI("prefix2"));
        assertNull(resolver.getNamespaceURI("prefix3"));
    }

    @Test
    public void testGetNamespaceURINullPointer() {
        resolver.registerNamespace("prefix1", "namespace1");
        resolver.setNamespaceContextPointer(null);

        assertEquals("namespace1", resolver.getNamespaceURI("prefix1"));
    }

    @Test
    public void testGetNamespaceURIWithParent() {
        NamespaceResolver parent = new NamespaceResolver();
        parent.registerNamespace("prefix1", "namespace1");
        parent.registerNamespace("prefix2", "namespace2");
        resolver = new NamespaceResolver(parent);
        resolver.registerNamespace("prefix3", "namespace3");

        assertEquals("namespace1", resolver.getNamespaceURI("prefix1"));
        assertEquals("namespace2", resolver.getNamespaceURI("prefix2"));
        assertEquals("namespace3", resolver.getNamespaceURI("prefix3"));
    }

    @Test
    public void testGetPrefix() {
        resolver.registerNamespace("prefix1", "namespace1");
        resolver.registerNamespace("prefix2", "namespace2");

        assertEquals("prefix1", resolver.getPrefix("namespace1"));
        assertEquals("prefix2", resolver.getPrefix("namespace2"));
        assertNull(resolver.getPrefix("namespace3"));
    }

    @Test
    public void testGetPrefixNullPointer() {
        resolver.registerNamespace("prefix1", "namespace1");
        resolver.setNamespaceContextPointer(null);

        assertEquals("prefix1", resolver.getPrefix("namespace1"));
    }

    @Test
    public void testGetPrefixWithParent() {
        NamespaceResolver parent = new NamespaceResolver();
        parent.registerNamespace("prefix1", "namespace1");
        parent.registerNamespace("prefix2", "namespace2");
        resolver = new NamespaceResolver(parent);
        resolver.registerNamespace("prefix3", "namespace3");

        assertEquals("prefix1", resolver.getPrefix("namespace1"));
        assertEquals("prefix2", resolver.getPrefix("namespace2"));
        assertEquals("prefix3", resolver.getPrefix("namespace3"));
    }

    @Test
    public void testIsSealed() {
        assertFalse(resolver.isSealed());
        resolver.seal();
        assertTrue(resolver.isSealed());
    }

    @Test
    public void testClone() {
        resolver.registerNamespace("prefix1", "namespace1");
        resolver.registerNamespace("prefix2", "namespace2");

        NamespaceResolver clone = (NamespaceResolver) resolver.clone();

        assertEquals(resolver.getNamespaceURI("prefix1"), clone.getNamespaceURI("prefix1"));
        assertEquals(resolver.getNamespaceURI("prefix2"), clone.getNamespaceURI("prefix2"));
    }
}