package org.apache.commons.jxpath.ri;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertSame;

import java.util.HashMap;
import java.util.Map;

import org.apache.commons.jxpath.NodeIterator;
import org.apache.commons.jxpath.Pointer;
import org.apache.commons.jxpath.ri.model.NodeIterator;
import org.apache.commons.jxpath.ri.model.NodePointer;

public class NamespaceResolverTest {

    @Test
    public void testNewInstance() {
        NamespaceResolver resolver1 = new NamespaceResolver();
        NamespaceResolver resolver2 = new NamespaceResolver();
        assertSame(resolver1, resolver2);
    }

    @Test
    public void testNewInstanceWithParent() {
        NamespaceResolver parent = new NamespaceResolver();
        NamespaceResolver resolver = new NamespaceResolver(parent);
        assertNotNull(resolver.getParent());
        assertSame(parent, resolver.getParent());
    }

    @Test(expected = IllegalStateException.class)
    public void testRegisterNamespaceWhenSealed() {
        NamespaceResolver resolver = new NamespaceResolver();
        resolver.seal();
        resolver.registerNamespace("foo", "bar");
    }

    @Test
    public void testRegisterNamespace() {
        NamespaceResolver resolver = new NamespaceResolver();
        resolver.registerNamespace("prefix", "uri");
        assertEquals("uri", resolver.getNamespaceURI("prefix"));
        assertEquals("prefix", resolver.getPrefix("uri"));
    }

    @Test
    public void testRegisterNamespaceWithParent() {
        NamespaceResolver parent = new NamespaceResolver();
        parent.registerNamespace("prefix", "uri");
        NamespaceResolver resolver = new NamespaceResolver(parent);
        assertEquals("uri", resolver.getNamespaceURI("prefix"));
        assertEquals("prefix", resolver.getPrefix("uri"));
    }

    @Test
    public void testGetNamespaceURI() {
        NamespaceResolver resolver = new NamespaceResolver();
        resolver.registerNamespace("prefix", "uri");
        assertEquals("uri", resolver.getNamespaceURI("prefix"));
        assertEquals(null, resolver.getNamespaceURI("unknown"));

        NamespaceResolver parent = new NamespaceResolver();
        parent.registerNamespace("prefix", "uri");
        resolver = new NamespaceResolver(parent);
        assertEquals("uri", resolver.getNamespaceURI("prefix"));
        assertEquals(null, resolver.getNamespaceURI("unknown"));
        assertEquals("uri", parent.getNamespaceURI("prefix"));

        resolver.setNamespaceContextPointer(new NodePointer(new Object(), new HashMap()));
        assertEquals("uri", resolver.getNamespaceURI("prefix"));
        assertEquals(null, resolver.getNamespaceURI("unknown"));
    }

    @Test
    public void testGetPrefix() {
        NamespaceResolver resolver = new NamespaceResolver();
        resolver.registerNamespace("prefix", "uri");
        assertEquals("prefix", resolver.getPrefix("uri"));
        assertEquals(null, resolver.getPrefix("unknown"));

        NamespaceResolver parent = new NamespaceResolver();
        parent.registerNamespace("prefix", "uri");
        resolver = new NamespaceResolver(parent);
        assertEquals("prefix", resolver.getPrefix("uri"));
        assertEquals(null, resolver.getPrefix("unknown"));
        assertEquals("prefix", parent.getPrefix("uri"));

        resolver.setNamespaceContextPointer(new NodePointer(new Object(), new HashMap()));
        assertEquals("prefix", resolver.getPrefix("uri"));
        assertEquals(null, resolver.getPrefix("unknown"));
    }

    @Test
    public void testSetNamespaceContextPointer() {
        NamespaceResolver resolver = new NamespaceResolver();
        NodeIterator ni = new NodeIterator(new Object(), new HashMap()) {
            @Override
            public NodePointer getNodePointer(int position) {
                return new NodePointer(new Object(), new HashMap()) {
                    @Override
                    public String getNamespaceURI() {
                        return "namespace";
                    }

                    @Override
                    public String getName() {
                        return "prefix";
                    }
                };
            }
        };
        resolver.setNamespaceContextPointer(new NodePointer(new Object(), new HashMap()));
        resolver.setNamespaceContextPointer(ni);

        assertEquals("namespace", resolver.getNamespaceURI("prefix"));
        assertEquals("prefix", resolver.getPrefix("namespace"));
    }

    @Test
    public void testIsSealed() {
        NamespaceResolver resolver = new NamespaceResolver();
        assertFalse(resolver.isSealed());

        resolver.seal();
        assertTrue(resolver.isSealed());
    }

    @Test
    public void testClone() {
        NamespaceResolver resolver = new NamespaceResolver();
        NamespaceResolver cloned = (NamespaceResolver) resolver.clone();
        assertNotSame(resolver, cloned);
        assertTrue(cloned.isSealed());
    }

    @Test
    public void testGetNamespaceContextPointer() {
        NamespaceResolver resolver = new NamespaceResolver();
        Pointer pointer = new Pointer(new Object(), new HashMap());
        resolver.setnamespaceContextPointer(pointer);
        assertEquals(pointer, resolver.getNamespaceContextPointer());
    }
}