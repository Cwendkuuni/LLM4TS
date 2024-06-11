package org.apache.commons.jxpath.ri;

import org.apache.commons.jxpath.Pointer;
import org.apache.commons.jxpath.ri.model.NodeIterator;
import org.apache.commons.jxpath.ri.model.NodePointer;
import org.junit.Before;
import org.junit.Test;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNull;

public class NamespaceResolverTest {


    @Test
    public void testRegisterNamespace() {
        NamespaceResolver nsr = new NamespaceResolver();

        nsr.registerNamespace("prefix1", "uri1");
        assertEquals("uri1", nsr.getNamespaceURI("prefix1"));

        nsr.registerNamespace("prefix2", "uri2");
        assertEquals("uri2", nsr.getNamespaceURI("prefix2"));
    }

    @Test(expected = IllegalStateException.class)
    public void testRegisterNamespace_SealedResolver() {
        NamespaceResolver nsr = new NamespaceResolver();
        nsr.seal();
        nsr.registerNamespace("prefix1", "uri1");
    }

    @Test
    public void testSetNamespaceContextPointer() {
        NamespaceResolver nsr = new NamespaceResolver();
        NodePointer pointer = new NodePointer();
        nsr.setNamespaceContextPointer(pointer);
        assertEquals(pointer, nsr.getNamespaceContextPointer());
    }


    @Test
    public void testGetNamespaceURI() {
        NamespaceResolver nsr = new NamespaceResolver();
        nsr.registerNamespace("prefix1", "uri1");
        nsr.registerNamespace("prefix2", "uri2");

        assertEquals("uri1", nsr.getNamespaceURI("prefix1"));
        assertEquals("uri2", nsr.getNamespaceURI("prefix2"));
        assertNull(nsr.getNamespaceURI("prefix3"));
    }


    @Test
    public void testGetPrefix() {
        NamespaceResolver nsr = new NamespaceResolver();
        nsr.registerNamespace("prefix1", "uri1");
        nsr.registerNamespace("prefix2", "uri2");

        assertEquals("prefix1", nsr.getPrefix("uri1"));
        assertEquals("prefix2", nsr.getPrefix("uri2"));
        assertNull(nsr.getPrefix("uri3"));
    }

    @Test
    public void testGetPrefix_ReverseMapPopulated() {
        NamespaceResolver nsr = new NamespaceResolver();
        NodePointer pointer = new NodePointer();
        NodeIterator iterator = new NodeIterator(pointer);
        pointer.setNamespaceIterator(iterator);

        nsr.setNamespaceContextPointer(pointer);
        nsr.registerNamespace("prefix1", "uri1");
        nsr.registerNamespace("prefix2", "uri2");

        String prefix = nsr.getPrefix("uri1");
        assertEquals("prefix1", prefix);
    }

    @Test
    public void testGetPrefix_ParentResolver() {
        NamespaceResolver parent = new NamespaceResolver();
        parent.registerNamespace("prefix1", "uri1");

        NamespaceResolver nsr = new NamespaceResolver(parent);
        nsr.registerNamespace("prefix2", "uri2");

        assertEquals("prefix1", nsr.getPrefix("uri1"));
        assertEquals("prefix2", nsr.getPrefix("uri2"));
    }
    
    @Test
    public void testIsSealed() {
        NamespaceResolver nsr = new NamespaceResolver();
        assertFalse(nsr.isSealed());
    }

    @Test
    public void testSeal() {
        NamespaceResolver nsr = new NamespaceResolver();
        nsr.seal();
        assertTrue(nsr.isSealed());
    }

    @Test
    public void testClone() {
        NamespaceResolver nsr = new NamespaceResolver();
        NamespaceResolver clonedNsr = (NamespaceResolver) nsr.clone();
        assertFalse(nsr.isSealed());
        assertFalse(clonedNsr.isSealed());
    }
}