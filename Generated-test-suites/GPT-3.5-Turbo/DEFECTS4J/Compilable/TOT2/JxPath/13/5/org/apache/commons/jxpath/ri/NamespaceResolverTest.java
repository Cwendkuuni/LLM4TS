package org.apache.commons.jxpath.ri;
import static org.junit.Assert.*;
import org.junit.Test;

public class NamespaceResolverTest {

    @Test
    public void testRegisterNamespace() {
        NamespaceResolver resolver = new NamespaceResolver();
        resolver.registerNamespace("test", "http://test.com");
        assertEquals("http://test.com", resolver.getNamespaceURI("test"));
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
    public void testClone() {
        NamespaceResolver parentResolver = new NamespaceResolver();
        parentResolver.registerNamespace("test", "http://test.com");

        NamespaceResolver childResolver = new NamespaceResolver(parentResolver);
        childResolver.registerNamespace("child", "http://child.com");

        NamespaceResolver clonedResolver = (NamespaceResolver) childResolver.clone();

        assertEquals("http://test.com", clonedResolver.getNamespaceURI("test"));
        assertEquals("http://child.com", clonedResolver.getNamespaceURI("child"));
    }

    @Test
    public void testSeal() {
        NamespaceResolver resolver = new NamespaceResolver();
        resolver.registerNamespace("test", "http://test.com");
        resolver.seal();

        try {
            resolver.registerNamespace("new", "http://new.com");
            fail("Expected IllegalStateException");
        } catch (IllegalStateException e) {
            // Expected
        }
    }

}