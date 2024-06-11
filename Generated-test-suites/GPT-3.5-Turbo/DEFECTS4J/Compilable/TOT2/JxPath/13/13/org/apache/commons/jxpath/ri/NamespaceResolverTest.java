package org.apache.commons.jxpath.ri;
import static org.junit.Assert.*;
import org.junit.Test;

public class NamespaceResolverTest {

    @Test
    public void testRegisterNamespace() {
        NamespaceResolver resolver = new NamespaceResolver();
        resolver.registerNamespace("prefix1", "http://example1.com");
        assertEquals("http://example1.com", resolver.getNamespaceURI("prefix1"));
    }

    @Test
    public void testGetNamespaceURI_External() {
        NamespaceResolver resolver = new NamespaceResolver();
        resolver.registerNamespace("prefix2", "http://example2.com");
        assertEquals("http://example2.com", resolver.getNamespaceURI("prefix2"));
    }

    @Test
    public void testGetPrefix() {
        NamespaceResolver resolver = new NamespaceResolver();
        resolver.registerNamespace("prefix3", "http://example3.com");
        assertEquals("prefix3", resolver.getPrefix("http://example3.com"));
    }

    @Test
    public void testSeal() {
        NamespaceResolver resolver = new NamespaceResolver();
        assertFalse(resolver.isSealed());
        resolver.seal();
        assertTrue(resolver.isSealed());
    }

    @Test
    public void testClone() {
        NamespaceResolver parentResolver = new NamespaceResolver();
        parentResolver.registerNamespace("prefix4", "http://example4.com");
        NamespaceResolver childResolver = new NamespaceResolver(parentResolver);
        NamespaceResolver clonedResolver = (NamespaceResolver) childResolver.clone();
        assertEquals("http://example4.com", clonedResolver.getNamespaceURI("prefix4"));
    }
}