
package org.apache.commons.jxpath.ri;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNull;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

import org.apache.commons.jxpath.Pointer;
import org.apache.commons.jxpath.ri.model.NodeIterator;
import org.apache.commons.jxpath.ri.model.NodePointer;
import org.junit.Test;

public class NamespaceResolverTest {

    @Test
    public void testRegisterNamespace() {
        NamespaceResolver resolver = new NamespaceResolver();
        resolver.registerNamespace("prefix1", "namespaceURI1");
        
        assertEquals("namespaceURI1", resolver.getNamespaceURI("prefix1"));
    }
    
    @Test(expected = IllegalStateException.class)
    public void testSealedRegisterNamespace() {
        NamespaceResolver parent = new NamespaceResolver();
        parent.seal();
        
        NamespaceResolver resolver = new NamespaceResolver(parent);
        resolver.registerNamespace("prefix", "namespaceURI");
    }
    
    @Test
    public void testSetNamespaceContextPointer() {
        NodePointer pointer = mock(NodePointer.class);
        
        NamespaceResolver resolver = new NamespaceResolver();
        resolver.setNamespaceContextPointer(pointer);
        
        assertEquals(pointer, resolver.getNamespaceContextPointer());
    }
    
    @Test
    public void testGetNamespaceURI() {
        NamespaceResolver resolver = new NamespaceResolver();
        resolver.registerNamespace("prefix1", "namespaceURI1");
        NodePointer pointer = mock(NodePointer.class);
        resolver.setNamespaceContextPointer(pointer);
        
        when(pointer.getNamespaceURI("prefix2")).thenReturn("namespaceURI2");
        
        assertEquals("namespaceURI1", resolver.getNamespaceURI("prefix1"));
        assertEquals("namespaceURI2", resolver.getNamespaceURI("prefix2"));
        assertNull(resolver.getNamespaceURI("prefix3"));
    }
    
    @Test
    public void testGetPrefix() {
        NamespaceResolver parent = new NamespaceResolver();
        parent.registerNamespace("prefix1", "namespaceURI1");
        
        NodePointer pointer = mock(NodePointer.class);
        resolver.setNamespaceContextPointer(pointer);
        
        when(pointer.namespaceIterator()).thenReturn(mock(NodeIterator.class));
        when(pointer.namespaceIterator().setPosition(any(int.class))).thenReturn(true);
        when(pointer.namespaceIterator().getNodePointer().getNamespaceURI()).thenReturn("namespaceURI2");
        when(pointer.namespaceIterator().getNodePointer().getName().getName()).thenReturn("prefix2");
        
        NamespaceResolver resolver = new NamespaceResolver(parent);
        resolver.registerNamespace("prefix3", "namespaceURI3");
        
        assertEquals("prefix1", resolver.getPrefix("namespaceURI1"));
        assertEquals("prefix2", resolver.getPrefix("namespaceURI2"));
        assertEquals("prefix3", resolver.getPrefix("namespaceURI3"));
    }

    @Test
    public void testIsSealed() {
        NamespaceResolver resolver = new NamespaceResolver();
        resolver.registerNamespace("prefix", "namespaceURI");
        
        assertFalse(resolver.isSealed());
        
        resolver.seal();
        
        assertTrue(resolver.isSealed());
    }

    @Test
    public void testClone() {
        NamespaceResolver resolver = new NamespaceResolver();
        resolver.registerNamespace("prefix", "namespaceURI");
        
        NamespaceResolver clone = resolver.clone();
        
        assertEquals("namespaceURI", clone.getNamespaceURI("prefix"));
    }
}
