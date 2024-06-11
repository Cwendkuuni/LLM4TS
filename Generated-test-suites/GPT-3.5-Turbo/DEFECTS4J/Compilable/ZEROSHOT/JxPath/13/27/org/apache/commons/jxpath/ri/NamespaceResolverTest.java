package org.apache.commons.jxpath.ri;
import static org.junit.Assert.*;
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
        namespaceResolver.registerNamespace("prefix", "uri");
        String uri = namespaceResolver.getNamespaceURI("prefix");
        assertEquals("uri", uri);
    }

    @Test(expected = IllegalStateException.class)
    public void testRegisterNamespaceSealed() {
        namespaceResolver.seal();
        namespaceResolver.registerNamespace("prefix", "uri");
    }

    @Test
    public void testGetNamespaceURI() {
        String uri = namespaceResolver.getNamespaceURI("prefix");
        assertNull(uri);
    }

    @Test
    public void testGetNamespaceURINotNull() {
        namespaceResolver.registerNamespace("prefix", "uri");
        String uri = namespaceResolver.getNamespaceURI("prefix");
        assertEquals("uri", uri);
    }

    @Test
    public void testGetNamespaceURIParent() {
        NamespaceResolver parent = new NamespaceResolver();
        parent.registerNamespace("prefix", "uri");
        namespaceResolver = new NamespaceResolver(parent);
        String uri = namespaceResolver.getNamespaceURI("prefix");
        assertEquals("uri", uri);
    }

    @Test
    public void testGetPrefix() {
        String prefix = namespaceResolver.getPrefix("uri");
        assertNull(prefix);
    }

    @Test
    public void testGetPrefixNotNull() {
        namespaceResolver.registerNamespace("prefix", "uri");
        String prefix = namespaceResolver.getPrefix("uri");
        assertEquals("prefix", prefix);
    }

    @Test
    public void testGetPrefixParent() {
        NamespaceResolver parent = new NamespaceResolver();
        parent.registerNamespace("prefix", "uri");
        namespaceResolver = new NamespaceResolver(parent);
        String prefix = namespaceResolver.getPrefix("uri");
        assertEquals("prefix", prefix);
    }

    @Test
    public void testSeal() {
        assertFalse(namespaceResolver.isSealed());
        namespaceResolver.seal();
        assertTrue(namespaceResolver.isSealed());
    }

    @Test
    public void testClone() {
        NamespaceResolver clone = (NamespaceResolver) namespaceResolver.clone();
        assertNotSame(clone, namespaceResolver);
        assertEquals(clone.isSealed(), namespaceResolver.isSealed());
    }
}