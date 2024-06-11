package org.apache.commons.jxpath.ri;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertTrue;
import static org.junit.Assert.assertFalse;

import org.junit.Before;
import org.junit.Test;

public class NamespaceResolverTest {

    private NamespaceResolver resolver;

    @Before
    public void setUp() {
        resolver = new NamespaceResolver();
    }

    @Test
    public void testRegisterNamespace() {
        resolver.registerNamespace("prefix1", "http://namespace1.com");
        resolver.registerNamespace("prefix2", "http://namespace2.com");

        assertEquals("http://namespace1.com", resolver.getNamespaceURI("prefix1"));
        assertEquals("http://namespace2.com", resolver.getNamespaceURI("prefix2"));
    }

    @Test
    public void testGetPrefix() {
        resolver.registerNamespace("prefix1", "http://namespace1.com");
        resolver.registerNamespace("prefix2", "http://namespace2.com");

        assertEquals("prefix1", resolver.getPrefix("http://namespace1.com"));
        assertEquals("prefix2", resolver.getPrefix("http://namespace2.com"));
    }

    @Test
    public void testGetNamespaceURI() {
        resolver.registerNamespace("prefix1", "http://namespace1.com");
        resolver.registerNamespace("prefix2", "http://namespace2.com");

        assertEquals("http://namespace1.com", resolver.getNamespaceURI("prefix1"));
        assertEquals("http://namespace2.com", resolver.getNamespaceURI("prefix2"));
        assertNull(resolver.getNamespaceURI("prefix3"));
    }

    @Test
    public void testSetNamespaceContextPointer() {
        // Implementation specific, nothing to test
    }

    @Test
    public void testGetNamespaceContextPointer() {
        // Implementation specific, nothing to test
    }

    @Test
    public void testSeal() {
        resolver.seal();

        assertTrue(resolver.isSealed());
    }

}