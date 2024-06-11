package org.apache.commons.jxpath.ri;
import org.junit.Before;
import org.junit.Test;
import static org.junit.Assert.*;

public class NamespaceResolverTest {

    private NamespaceResolver resolver;

    @Before
    public void setUp() {
        resolver = new NamespaceResolver();
        resolver.registerNamespace("ns1", "http://www.example.com/ns1");
        resolver.registerNamespace("ns2", "http://www.example.com/ns2");
    }

    @Test
    public void testRegisterNamespace() {
        assertEquals("http://www.example.com/ns1", resolver.getNamespaceURI("ns1"));
        assertEquals("http://www.example.com/ns2", resolver.getNamespaceURI("ns2"));
    }

    @Test
    public void testGetNamespaceURI() {
        assertEquals("http://www.example.com/ns1", resolver.getNamespaceURI("ns1"));
        assertEquals("http://www.example.com/ns1", resolver.getNamespaceURI("ns1"));
    }

    @Test
    public void testGetPrefix() {
        assertEquals("ns1", resolver.getPrefix("http://www.example.com/ns1"));
        assertEquals("ns2", resolver.getPrefix("http://www.example.com/ns2"));
    }

    @Test
    public void testSeal() {
        assertFalse(resolver.isSealed());
        resolver.seal();
        assertTrue(resolver.isSealed());
    }
}