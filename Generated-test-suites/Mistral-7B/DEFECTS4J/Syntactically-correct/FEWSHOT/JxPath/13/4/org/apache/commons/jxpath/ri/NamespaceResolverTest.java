package org.apache.commons.jxpath.ri;

import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertEquals;

import org.apache.commons.jxpath.Pointer;
import org.apache.commons.jxpath.ri.model.NodeIterator;
import org.apache.commons.jxpath.ri.model.NodePointer;

import org.junit.Test;

public class NamespaceResolverTest {

    @Test
    public void testNewInstance() {
        NamespaceResolver resolver = new NamespaceResolver();
        assertNull(resolver.getParent());
        assertEquals(0, resolver.size());
    }

    @Test
    public void testNewInstanceWithParent() {
        NamespaceResolver parentResolver = new NamespaceResolver();
        NamespaceResolver childResolver = new NamespaceResolver(parentResolver);
        assertEquals(parentResolver, childResolver.getParent());
        assertEquals(0, childResolver.size());
    }

    @Test
    public void testRegisterNamespace() {
        NamespaceResolver resolver = new NamespaceResolver();
        resolver.registerNamespace("xsd", "http://www.w3.org/2001/XMLSchema");
        String uri = resolver.getNamespaceURI("xsd");
        assertEquals("http://www.w3.org/2001/XMLSchema", uri);
    }

    @Test(expected = IllegalStateException.class)
    public void testRegisterNamespaceSealed() {
        NamespaceResolver resolver = new NamespaceResolver();
        resolver.seal();
        resolver.registerNamespace("xsd", "http://www.w3.org/2001/XMLSchema");
    }

    @Test
    public void testGetNamespaceURI() {
        NamespaceResolver resolver = new NamespaceResolver();
        resolver.registerNamespace("xsd", "http://www.w3.org/2001/XMLSchema");
        String uri = resolver.getNamespaceURI("xsd");
        assertEquals("http://www.w3.org/2001/XMLSchema", uri);

        NodePointer pointer = new NodePointer(null, null, null,null, null, null, null);
        resolver.setNamespaceContextPointer(pointer);
        uri = resolver.getNamespaceURI("xsd");
        assertEquals("http://www.w3.org/2001/XMLSchema", uri);

        NamespaceResolver parent = new NamespaceResolver();
        resolver = new NamespaceResolver(parent);
        uri = resolver.getNamespaceURI("xsd");
        assertEquals("http://www.w3.org/2001/XMLSchema", uri);
        uri = parent.getNamespaceURI("xsd");
        assertEquals("http://www.w3.org/2001/XMLSchema", uri);
    }

    @Test
    public void testGetNamespaceURIUnknown() {
        NamespaceResolver resolver = new NamespaceResolver();
        String uri = resolver.getNamespaceURI("xsd");
        assertNull(uri);
    }

    @Test
    public void testGetPrefix() {
        NamespaceResolver resolver = new NamespaceResolver();
        resolver.registerNamespace("xsd", "http://www.w3.org/2001/XMLSchema");
        String prefix = resolver.getPrefix("http://www.w3.org/2001/XMLSchema");
        assertEquals("xsd", prefix);

        NodePointer pointer = new NodePointer(null, null, null,null, null, null, null);
        resolver.setNamespaceContextPointer(pointer);
        prefix = resolver.getPrefix("http://www.w3.org/2001/XMLSchema");
        assertEquals("xsd", prefix);

        NamespaceResolver parent = new NamespaceResolver();
        resolver = new NamespaceResolver(parent);
        prefix = resolver.getPrefix("http://www.w3.org/2001/XMLSchema");
        assertEquals("xsd", prefix);
        prefix = parent.getPrefix("http://www.w3.org/2001/XMLSchema");
        assertEquals("xsd", prefix);
    }

    @Test
    public void testGetPrefixUnknown() {
        NamespaceResolver resolver = new NamespaceResolver();
        String prefix = resolver.getPrefix("http://www.w3.org/2001/XMLSchema");
        assertNull(prefix);
    }

    @Test
    public void testSeal() {
        NamespaceResolver resolver = new NamespaceResolver();
        resolver.registerNamespace("xsd", "http://www.w3.org/2001/XMLSchema");
        resolver.seal();
        assertFalse(resolver.isSealed());

        resolver.registerNamespace("xsd2", "http://www.w3.org/2001/XMLSchema");
        assertTrue(resolver.isSealed());
    }

    @Test
    public void testClone() {
        NamespaceResolver resolver = new NamespaceResolver();
        resolver.registerNamespace("xsd", "http://www.w3.org/2001/XMLSchema");
        NamespaceResolver cloned = (NamespaceResolver) resolver.clone();
        assertEquals(resolver, cloned);
        assertFalse(cloned.isSealed());
        String uri = cloned.getNamespaceURI("xsd");
        assertEquals("http://www.w3.org/2001/XMLSchema", uri);
    }
}