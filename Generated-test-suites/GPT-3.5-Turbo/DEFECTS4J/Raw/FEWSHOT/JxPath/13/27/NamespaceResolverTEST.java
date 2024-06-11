package org.apache.commons.jxpath.ri;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNull;

import org.apache.commons.jxpath.Pointer;
import org.apache.commons.jxpath.ri.model.NodeIterator;
import org.apache.commons.jxpath.ri.model.NodePointer;
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
        resolver.registerNamespace("ns1", "http://namespace1.com");
        resolver.registerNamespace("ns2", "http://namespace2.com");
        
        assertEquals("http://namespace1.com", resolver.getNamespaceURI("ns1"));
        assertEquals("http://namespace2.com", resolver.getNamespaceURI("ns2"));
        assertNull(resolver.getNamespaceURI("ns3"));
    }
    
    @Test(expected = IllegalStateException.class)
    public void testRegisterNamespaceSealed() {
        resolver.seal();
        resolver.registerNamespace("ns1", "http://namespace1.com");
    }
    
    @Test
    public void testSetNamespaceContextPointer() {
        NodePointer pointer = new NodePointer();
        resolver.setNamespaceContextPointer(pointer);
        
        assertEquals(pointer, resolver.getNamespaceContextPointer());
    }
    
    @Test
    public void testGetNamespaceURI() {
        resolver.registerNamespace("ns1", "http://namespace1.com");
        resolver.registerNamespace("ns2", "http://namespace2.com");
        
        String uri1 = resolver.getNamespaceURI("ns1");
        String uri2 = resolver.getNamespaceURI("ns2");
        
        assertEquals("http://namespace1.com", uri1);
        assertEquals("http://namespace2.com", uri2);
    }
    
    @Test
    public void testGetNamespaceURIExternal() {
        resolver.setNamespaceContextPointer(new NodePointer());
        
        String uri = resolver.getNamespaceURI("ns1");
        
        assertNull(uri);
    }
    
    @Test
    public void testGetNamespaceURIWithParent() {
        NamespaceResolver parentResolver = new NamespaceResolver();
        parentResolver.registerNamespace("ns1", "http://namespace1.com");
        parentResolver.registerNamespace("ns2", "http://namespace2.com");
        
        resolver = new NamespaceResolver(parentResolver);
        
        String uri1 = resolver.getNamespaceURI("ns1");
        String uri2 = resolver.getNamespaceURI("ns2");
        String uri3 = resolver.getNamespaceURI("ns3");
        
        assertEquals("http://namespace1.com", uri1);
        assertEquals("http://namespace2.com", uri2);
        assertNull(uri3);
    }
    
    @Test
    public void testGetPrefix() {
        resolver.registerNamespace("ns1", "http://namespace1.com");
        resolver.registerNamespace("ns2", "http://namespace2.com");
        
        String prefix1 = resolver.getPrefix("http://namespace1.com");
        String prefix2 = resolver.getPrefix("http://namespace2.com");
        
        assertEquals("ns1", prefix1);
        assertEquals("ns2", prefix2);
    }
    
    @Test
    public void testGetPrefixExternal() {
        resolver.setNamespaceContextPointer(new NodePointer());
        
        String prefix = resolver.getPrefix("http://namespace1.com");
        
        assertNull(prefix);
    }
    
    @Test
    public void testGetPrefixWithParent() {
        NamespaceResolver parentResolver = new NamespaceResolver();
        parentResolver.registerNamespace("ns1", "http://namespace1.com");
        parentResolver.registerNamespace("ns2", "http://namespace2.com");
        
        resolver = new NamespaceResolver(parentResolver);
        
        String prefix1 = resolver.getPrefix("http://namespace1.com");
        String prefix2 = resolver.getPrefix("http://namespace2.com");
        String prefix3 = resolver.getPrefix("http://namespace3.com");
        
        assertEquals("ns1", prefix1);
        assertEquals("ns2", prefix2);
        assertNull(prefix3);
    }
    
    @Test
    public void testIsSealed() {
        resolver.seal();
        
        assertEquals(true, resolver.isSealed());
    }
    
    @Test
    public void testClone() {
        NamespaceResolver cloned = (NamespaceResolver) resolver.clone();
        
        assertEquals(false, cloned.isSealed());
        assertEquals(null, cloned.getNamespaceContextPointer());
    }
}