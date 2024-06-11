import static org.junit.Assert.*;

import org.apache.commons.jxpath.JXPathContext;
import org.apache.commons.jxpath.NamespaceResolver;
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
        assertNull(resolver.getNamespaceURI("prefix1"));

        resolver.registerNamespace("prefix1", "http://namespace1");

        assertEquals("http://namespace1", resolver.getNamespaceURI("prefix1"));
    }

    @Test(expected = IllegalStateException.class)
    public void testRegisterNamespaceOnSealedResolver() {
        resolver.seal();

        resolver.registerNamespace("prefix", "http://namespace");
    }

    @Test
    public void testSetNamespaceContextPointer() {
        NodePointer pointer = new NodePointer();
        resolver.setNamespaceContextPointer(pointer);

        assertEquals(pointer, resolver.getNamespaceContextPointer());
    }

    @Test
    public void testGetNamespaceURI() {
        assertNull(resolver.getNamespaceURI("prefix2"));

        resolver.registerNamespace("prefix2", "http://namespace2");

        assertEquals("http://namespace2", resolver.getNamespaceURI("prefix2"));
    }

    @Test
    public void testGetNamespaceURIWithParentResolver() {
        NamespaceResolver parentResolver = new NamespaceResolver();
        parentResolver.registerNamespace("prefix", "http://parent");

        resolver = new NamespaceResolver(parentResolver);
        resolver.registerNamespace("prefix", "http://child");

        assertEquals("http://child", resolver.getNamespaceURI("prefix"));
    }

    @Test
    public void testGetPrefix() {
        assertNull(resolver.getPrefix("http://www.namespace.org"));

        resolver.registerNamespace("prefix3", "http://namespace3");

        assertEquals("prefix3", resolver.getPrefix("http://namespace3"));
    }

    @Test
    public void testGetPrefixWithParentResolver() {
        NamespaceResolver parentResolver = new NamespaceResolver();
        parentResolver.registerNamespace("prefix", "http://parent");

        resolver = new NamespaceResolver(parentResolver);
        resolver.registerNamespace("prefix", "http://child");

        assertEquals("prefix", resolver.getPrefix("http://parent"));
    }

    @Test
    public void testIsSealed() {
        assertFalse(resolver.isSealed());

        resolver.seal();

        assertTrue(resolver.isSealed());
    }

    @Test
    public void testClone() {
        NamespaceResolver clonedResolver = (NamespaceResolver) resolver.clone();

        assertEquals(clonedResolver.isSealed(), resolver.isSealed());
        assertNotSame(clonedResolver, resolver);
    }

    @Test
    public void testCompleteNamespaceResolver() {
        NamespaceResolver parentResolver = new NamespaceResolver();
        parentResolver.registerNamespace("prefix1", "http://parent1");
        parentResolver.registerNamespace("prefix2", "http://parent2");

        resolver = new NamespaceResolver(parentResolver);
        resolver.registerNamespace("prefix3", "http://child1");
        resolver.registerNamespace("prefix4", "http://child2");

        assertEquals("http://child1", resolver.getNamespaceURI("prefix3"));
        assertEquals("http://child2", resolver.getNamespaceURI("prefix4"));
        assertEquals("prefix3", resolver.getPrefix("http://child1"));
        assertEquals("prefix4", resolver.getPrefix("http://child2"));
        assertEquals("prefix1", resolver.getPrefix("http://parent1"));
        assertEquals("prefix2", resolver.getPrefix("http://parent2"));
        assertNull(resolver.getPrefix("http://www.namespace.org"));
    }

}