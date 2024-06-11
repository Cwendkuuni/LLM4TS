package org.apache.commons.jxpath.ri;
import org.junit.Test;
import static org.junit.Assert.*;
import org.apache.commons.jxpath.ri.NamespaceResolver;

public class NamespaceResolverTest {

    @Test
    public void testRegisterNamespace() {
        NamespaceResolver resolver = new NamespaceResolver();
        resolver.registerNamespace("ns1", "http://namespace1.com");
        assertEquals("http://namespace1.com", resolver.getNamespaceURI("ns1"));
    }

    @Test
    public void testGetNamespaceURI() {
        NamespaceResolver resolver = new NamespaceResolver();
        resolver.registerNamespace("ns1", "http://namespace1.com");
        resolver.registerNamespace("ns2", "http://namespace2.com");
        assertEquals("http://namespace1.com", resolver.getNamespaceURI("ns1"));
        assertEquals("http://namespace2.com", resolver.getNamespaceURI("ns2"));
    }

    @Test
    public void testGetPrefix() {
        NamespaceResolver resolver = new NamespaceResolver();
        resolver.registerNamespace("ns1", "http://namespace1.com");
        resolver.registerNamespace("ns2", "http://namespace2.com");
        assertEquals("ns1", resolver.getPrefix("http://namespace1.com"));
        assertEquals("ns2", resolver.getPrefix("http://namespace2.com"));
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
        parentResolver.registerNamespace("ns1", "http://namespace1.com");

        NamespaceResolver childResolver = new NamespaceResolver(parentResolver);
        NamespaceResolver clonedResolver = (NamespaceResolver) childResolver.clone();

        assertEquals(parentResolver.getNamespaceURI("ns1"), clonedResolver.getNamespaceURI("ns1"));
    }

}