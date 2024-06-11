import static org.junit.Assert.*;
import org.junit.Before;
import org.junit.Test;

import org.apache.commons.jxpath.Pointer;
import org.apache.commons.jxpath.ri.model.NodeIterator;
import org.apache.commons.jxpath.ri.model.NodePointer;

public class NamespaceResolverTest {

    @Test
    public void testRegisterNamespace() {
        NamespaceResolver resolver = new NamespaceResolver();
        resolver.registerNamespace("prefix", "uri");
        assertEquals("uri", resolver.getNamespaceURI("prefix"));
    }

    @Test(expected = IllegalStateException.class)
    public void testRegisterNamespaceOnSealedResolver() {
        NamespaceResolver parent = new NamespaceResolver();
        parent.seal();
        NamespaceResolver resolver = new NamespaceResolver(parent);
        resolver.registerNamespace("prefix", "uri");
    }

    @Test
    public void testSetAndGetNamespaceContextPointer() {
        Pointer pointer = new NodePointer();
        NamespaceResolver resolver = new NamespaceResolver();
        resolver.setNamespaceContextPointer(pointer);
        assertEquals(pointer, resolver.getNamespaceContextPointer());
    }

    @Test
    public void testGetNamespaceURI() {
        NamespaceResolver resolver = new NamespaceResolver();
        resolver.registerNamespace("prefix", "uri");
        assertEquals("uri", resolver.getNamespaceURI("prefix"));
        assertEquals(null, resolver.getNamespaceURI("invalidPrefix"));
    }

    @Test
    public void testGetNamespaceURIWithParentResolver() {
        NamespaceResolver parent = new NamespaceResolver();
        parent.registerNamespace("prefix", "uri");
        NamespaceResolver resolver = new NamespaceResolver(parent);
        assertEquals("uri", resolver.getNamespaceURI("prefix"));
        assertEquals(null, resolver.getNamespaceURI("invalidPrefix"));
    }

    @Test
    public void testGetPrefix() {
        NamespaceResolver resolver = new NamespaceResolver();
        resolver.registerNamespace("prefix", "uri");
        assertEquals("prefix", resolver.getPrefix("uri"));
        assertEquals(null, resolver.getPrefix("invalidUri"));
    }

    @Test
    public void testGetPrefixWithParentResolver() {
        NamespaceResolver parent = new NamespaceResolver();
        parent.registerNamespace("prefix", "uri");
        NamespaceResolver resolver = new NamespaceResolver(parent);
        assertEquals("prefix", resolver.getPrefix("uri"));
        assertEquals(null, resolver.getPrefix("invalidUri"));
    }

    @Test
    public void testSeal() {
        NamespaceResolver resolver = new NamespaceResolver();
        resolver.seal();
        assertTrue(resolver.isSealed());
    }

    @Test
    public void testClone() {
        NamespaceResolver parent = new NamespaceResolver();
        NamespaceResolver resolver = new NamespaceResolver(parent);
        NamespaceResolver clonedResolver = (NamespaceResolver)resolver.clone();
        assertNotSame(resolver, clonedResolver);
        assertEquals(resolver.isSealed(), clonedResolver.isSealed());
    }
}