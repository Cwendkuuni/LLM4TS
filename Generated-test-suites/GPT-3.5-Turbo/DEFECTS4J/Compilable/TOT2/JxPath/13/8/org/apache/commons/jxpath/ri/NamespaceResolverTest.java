package org.apache.commons.jxpath.ri;
import static org.junit.Assert.*;
import org.junit.Test;

public class NamespaceResolverTest {

    @Test
    public void testConstructor() {
        NamespaceResolver resolver = new NamespaceResolver();
        assertNotNull(resolver);
    }

    @Test
    public void testRegisterNamespace() {
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
    public void testSeal() {
        NamespaceResolver parentResolver = new NamespaceResolver();
        NamespaceResolver childResolver = new NamespaceResolver(parentResolver);
        childResolver.seal();
        assertTrue(childResolver.isSealed());
        assertTrue(parentResolver.isSealed());
    }

    @Test
    public void testClone() {
        NamespaceResolver parentResolver = new NamespaceResolver();
        NamespaceResolver clonedResolver = (NamespaceResolver) parentResolver.clone();
        assertNotSame(parentResolver, clonedResolver);
    }

    // Add more test cases covering edge cases and exception scenarios if needed

}