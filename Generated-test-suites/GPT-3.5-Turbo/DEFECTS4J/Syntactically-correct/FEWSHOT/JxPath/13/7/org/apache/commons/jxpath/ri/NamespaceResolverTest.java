package org.apache.commons.jxpath.ri;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNull;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

import org.apache.commons.jxpath.Pointer;
import org.apache.commons.jxpath.ri.model.NodeIterator;
import org.apache.commons.jxpath.ri.model.NodePointer;
import org.junit.Before;
import org.junit.Test;

public class NamespaceResolverTest {
    private NamespaceResolver namespaceResolver;
    private NodePointer pointer;

    @Before
    public void setUp() {
        namespaceResolver = new NamespaceResolver();
        pointer = mock(NodePointer.class);
    }

    @Test
    public void testRegisterNamespace() {
        namespaceResolver.registerNamespace("prefix", "uri");
        assertEquals("uri", namespaceResolver.getNamespaceURI("prefix"));
    }
    
    @Test(expected = IllegalStateException.class)
    public void testRegisterNamespace_SealedNamespaceResolver() {
        namespaceResolver.seal();
        namespaceResolver.registerNamespace("prefix", "uri");
    }
    
    @Test
    public void testSetNamespaceContextPointer() {
        namespaceResolver.setNamespaceContextPointer(pointer);
        assertEquals(pointer, namespaceResolver.getNamespaceContextPointer());
    }
    
    @Test
    public void testGetNamespaceURI() {
        namespaceResolver.registerNamespace("prefix", "uri");
        assertEquals("uri", namespaceResolver.getNamespaceURI("prefix"));
    }
    
    @Test
    public void testGetNamespaceURI_UndefinedPrefix() {
        assertEquals(null, namespaceResolver.getNamespaceURI("prefix"));
    }
    
    @Test
    public void testGetNamespaceURI_NullPointer() {
        namespaceResolver.setNamespaceContextPointer(null);
        assertEquals(null, namespaceResolver.getNamespaceURI("prefix"));
    }
    
    @Test
    public void testGetNamespaceURI_ParentNamespace() {
        NamespaceResolver parentResolver = mock(NamespaceResolver.class);
        when(parentResolver.getNamespaceURI("prefix")).thenReturn("uri");
        namespaceResolver = new NamespaceResolver(parentResolver);
        assertEquals("uri", namespaceResolver.getNamespaceURI("prefix"));
    }
    
    @Test
    public void testGetPrefix() {
        namespaceResolver.registerNamespace("prefix", "uri");
        assertEquals("prefix", namespaceResolver.getPrefix("uri"));
    }
    
    @Test
    public void testGetPrefix_UndefinedNamespaceURI() {
        assertEquals(null, namespaceResolver.getPrefix("uri"));
    }
    
    @Test
    public void testGetPrefix_NullPointer() {
        namespaceResolver.setNamespaceContextPointer(null);
        assertEquals(null, namespaceResolver.getPrefix("uri"));
    }
    
    @Test
    public void testGetPrefix_ParentNamespace() {
        NamespaceResolver parentResolver = mock(NamespaceResolver.class);
        when(parentResolver.getPrefix("uri")).thenReturn("prefix");
        namespaceResolver = new NamespaceResolver(parentResolver);
        assertEquals("prefix", namespaceResolver.getPrefix("uri"));
    }
    
    @Test
    public void testSeal() {
        namespaceResolver.seal();
        assertEquals(true, namespaceResolver.isSealed());
    }
    
    @Test
    public void testClone() {
        NamespaceResolver clonedResolver = (NamespaceResolver) namespaceResolver.clone();
        assertEquals(false, clonedResolver.isSealed());
    }
}