package org.apache.commons.jxpath.ri;
import static org.junit.Assert.*;
import org.junit.Test;

public class NamespaceResolverTest {

    @Test
    public void testRegisterNamespace() {
        NamespaceResolver resolver = new NamespaceResolver();
        resolver.registerNamespace("test", "http://www.test.com");
        assertEquals("http://www.test.com", resolver.getNamespaceURI("test"));
    }

    @Test
    public void testGetNamespaceURI() {
        NamespaceResolver resolver = new NamespaceResolver();
        resolver.registerNamespace("test", "http://www.test.com");
        assertEquals("http://www.test.com", resolver.getNamespaceURI("test"));
    }

    @Test
    public void testGetPrefix() {
        NamespaceResolver resolver = new NamespaceResolver();
        resolver.registerNamespace("test", "http://www.test.com");
        assertEquals("test", resolver.getPrefix("http://www.test.com"));
    }

}