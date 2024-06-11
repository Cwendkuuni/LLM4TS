
package org.apache.commons.jxpath.ri;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNull;

import org.apache.commons.jxpath.Pointer;
import org.apache.commons.jxpath.ri.model.NodeIterator;
import org.apache.commons.jxpath.ri.model.NodePointer;
import org.junit.Test;

public class NamespaceResolverTest {

    @Test
    public void testRegisterNamespace() {
        NamespaceResolver resolver = new NamespaceResolver();
        resolver.registerNamespace("", "uri");
        assertEquals("uri", resolver.getNamespaceURI(""));
        
        resolver.registerNamespace("prefix", "uri2");
        assertEquals("uri2", resolver.getNamespaceURI("prefix"));
        
        resolver.seal();
        try {
            resolver.registerNamespace("prefix2", "uri3");
        } catch (IllegalStateException e) {
            assertNull(resolver.getNamespaceURI("prefix2"));
        }
    }

    @Test
    public void testSetNamespaceContextPointer() {
        NamespaceResolver resolver = new NamespaceResolver();
        NodePointer pointer = new NodePointer();
        resolver.setNamespaceContextPointer(pointer);
        assertEquals(pointer, resolver.getNamespaceContextPointer());
    }

    @Test
    public void testGetNamespaceURI() {
        NamespaceResolver resolver = new NamespaceResolver();
        resolver.registerNamespace("", "uri");
        assertEquals("uri", resolver.getNamespaceURI(""));
        
        resolver.registerNamespace("prefix", "uri2");
        assertEquals("uri2", resolver.getNamespaceURI("prefix"));
        
        resolver.seal();
        assertEquals("uri2", resolver.getNamespaceURI("prefix"));
        assertNull(resolver.getNamespaceURI("prefix2"));
        
        NodePointer pointer = new NodePointer();
        pointer.setNamespaceURI("prefix3", "uri3");
        resolver.setNamespaceContextPointer(pointer);
        assertEquals("uri3", resolver.getNamespaceURI("prefix3"));
    }

    @Test
    public void testGetPrefix() {
        NamespaceResolver resolver = new NamespaceResolver();
        resolver.registerNamespace("", "uri");
        assertEquals("", resolver.getPrefix("uri"));
        
        resolver.registerNamespace("prefix", "uri2");
        assertEquals("prefix", resolver.getPrefix("uri2"));
        
        resolver.seal();
        assertEquals("prefix", resolver.getPrefix("uri2"));
        assertNull(resolver.getPrefix("uri3"));
        
        NodePointer pointer = new NodePointer();
        pointer.setNamespaceURI("prefix3", "uri3");
        resolver.setNamespaceContextPointer(pointer);
        assertEquals("prefix3", resolver.getPrefix("uri3"));
    }

}
