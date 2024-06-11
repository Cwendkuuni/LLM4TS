package org.apache.commons.jxpath.ri;

import org.junit.Before;
import org.junit.Test;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNull;

public class NamespaceResolverTest {
    private NamespaceResolver resolver;

    @Before
    public void setup() {
        resolver = new NamespaceResolver();
    }

    @Test
    public void testRegisterNamespace() {
        resolver.registerNamespace("p1", "http://ns1");
        resolver.registerNamespace("p2", "http://ns2");
        resolver.registerNamespace("p3", "http://ns3");

        String uri1 = resolver.getNamespaceURI("p1");
        assertEquals("http://ns1", uri1);

        String uri2 = resolver.getNamespaceURI("p2");
        assertEquals("http://ns2", uri2);

        String uri3 = resolver.getNamespaceURI("p3");
        assertEquals("http://ns3", uri3);
    }

    @Test(expected = IllegalStateException.class)
    public void testRegisterNamespaceWhenSealed() {
        resolver.seal();
        resolver.registerNamespace("p1", "http://ns1");
    }

    @Test
    public void testGetNamespaceURI() {
        resolver.registerNamespace("p1", "http://ns1");
        resolver.registerNamespace("p2", "http://ns2");
        resolver.registerNamespace("p3", "http://ns3");

        String uri1 = resolver.getNamespaceURI("p1");
        assertEquals("http://ns1", uri1);

        String uri2 = resolver.getNamespaceURI("p2");
        assertEquals("http://ns2", uri2);

        String uri3 = resolver.getNamespaceURI("p3");
        assertEquals("http://ns3", uri3);

        String uri4 = resolver.getNamespaceURI("p4");
        assertNull(uri4);
    }

    @Test
    public void testGetNamespaceURIWithParent() {
        NamespaceResolver parent = new NamespaceResolver();
        parent.registerNamespace("p1", "http://ns1");
        parent.registerNamespace("p2", "http://ns2");

        resolver = new NamespaceResolver(parent);
        resolver.registerNamespace("p3", "http://ns3");
        resolver.registerNamespace("p4", "http://ns4");

        String uri1 = resolver.getNamespaceURI("p1");
        assertEquals("http://ns1", uri1);

        String uri2 = resolver.getNamespaceURI("p2");
        assertEquals("http://ns2", uri2);

        String uri3 = resolver.getNamespaceURI("p3");
        assertEquals("http://ns3", uri3);

        String uri4 = resolver.getNamespaceURI("p4");
        assertEquals("http://ns4", uri4);

        String uri5 = resolver.getNamespaceURI("p5");
        assertNull(uri5);
    }

    @Test
    public void testGetPrefix() {
        resolver.registerNamespace("p1", "http://ns1");
        resolver.registerNamespace("p2", "http://ns2");
        resolver.registerNamespace("p3", "http://ns3");

        String prefix1 = resolver.getPrefix("http://ns1");
        assertEquals("p1", prefix1);

        String prefix2 = resolver.getPrefix("http://ns2");
        assertEquals("p2", prefix2);

        String prefix3 = resolver.getPrefix("http://ns3");
        assertEquals("p3", prefix3);

        String prefix4 = resolver.getPrefix("http://ns4");
        assertNull(prefix4);
    }

    @Test
    public void testGetPrefixWithParent() {
        NamespaceResolver parent = new NamespaceResolver();
        parent.registerNamespace("p1", "http://ns1");
        parent.registerNamespace("p2", "http://ns2");

        resolver = new NamespaceResolver(parent);
        resolver.registerNamespace("p3", "http://ns3");
        resolver.registerNamespace("p4", "http://ns4");

        String prefix1 = resolver.getPrefix("http://ns1");
        assertEquals("p1", prefix1);

        String prefix2 = resolver.getPrefix("http://ns2");
        assertEquals("p2", prefix2);

        String prefix3 = resolver.getPrefix("http://ns3");
        assertEquals("p3", prefix3);

        String prefix4 = resolver.getPrefix("http://ns4");
        assertEquals("p4", prefix4);

        String prefix5 = resolver.getPrefix("http://ns5");
        assertNull(prefix5);
    }
}