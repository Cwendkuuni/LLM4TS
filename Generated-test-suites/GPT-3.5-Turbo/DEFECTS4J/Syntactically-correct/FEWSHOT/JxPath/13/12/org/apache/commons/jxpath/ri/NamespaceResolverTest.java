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
        resolver.registerNamespace("prefix1", "http://www.example.com/namespace1");
        resolver.registerNamespace("prefix2", "http://www.example.com/namespace2");
        resolver.registerNamespace("prefix3", "http://www.example.com/namespace3");

        assertEquals("http://www.example.com/namespace1", resolver.getNamespaceURI("prefix1"));
        assertEquals("http://www.example.com/namespace2", resolver.getNamespaceURI("prefix2"));
        assertEquals("http://www.example.com/namespace3", resolver.getNamespaceURI("prefix3"));

        assertNull(resolver.getNamespaceURI("prefix4"));
        assertNull(resolver.getNamespaceURI(null));
    }

    @Test
    public void testSetNamespaceContextPointer() {
        NamespaceResolver resolver = new NamespaceResolver();
        NodePointer pointer = new NodePointer();
        pointer.setNamespaceURI("http://www.example.com/namespace1");

        resolver.setNamespaceContextPointer(pointer);
        assertEquals(pointer, resolver.getNamespaceContextPointer());

        NodePointer childPointer = new NodePointer();
        childPointer.setNamespaceURI("http://www.example.com/namespace2");
        pointer.addChild(childPointer);

        resolver.setNamespaceContextPointer(childPointer);
        assertEquals(childPointer, resolver.getNamespaceContextPointer());
        assertEquals("http://www.example.com/namespace2", resolver.getNamespaceURI(null));
    }

    @Test
    public void testGetNamespaceURI() {
        NamespaceResolver resolver = new NamespaceResolver();
        resolver.registerNamespace("prefix1", "http://www.example.com/namespace1");
        resolver.registerNamespace("prefix2", "http://www.example.com/namespace2");
        resolver.registerNamespace("prefix3", "http://www.example.com/namespace3");

        assertEquals("http://www.example.com/namespace1", resolver.getNamespaceURI("prefix1"));
        assertEquals("http://www.example.com/namespace2", resolver.getNamespaceURI("prefix2"));
        assertEquals("http://www.example.com/namespace3", resolver.getNamespaceURI("prefix3"));

        assertNull(resolver.getNamespaceURI("prefix4"));
        assertNull(resolver.getNamespaceURI(null));
    }

    @Test
    public void testGetPrefix() {
        NamespaceResolver resolver = new NamespaceResolver();
        resolver.registerNamespace("prefix1", "http://www.example.com/namespace1");
        resolver.registerNamespace("prefix2", "http://www.example.com/namespace2");
        resolver.registerNamespace("prefix3", "http://www.example.com/namespace3");

        assertEquals("prefix1", resolver.getPrefix("http://www.example.com/namespace1"));
        assertEquals("prefix2", resolver.getPrefix("http://www.example.com/namespace2"));
        assertEquals("prefix3", resolver.getPrefix("http://www.example.com/namespace3"));

        assertNull(resolver.getPrefix("http://www.example.com/namespace4"));
        assertNull(resolver.getPrefix(null));
    }

    @Test
    public void testSeal() {
        NamespaceResolver resolver = new NamespaceResolver();
        resolver.seal();
        assertEquals(true, resolver.isSealed());
    }

    @Test
    public void testClone() {
        NamespaceResolver resolver = new NamespaceResolver();
        NamespaceResolver clonedResolver = (NamespaceResolver) resolver.clone();

        assertEquals(false, resolver.isSealed());
        assertEquals(false, clonedResolver.isSealed());
    }
}