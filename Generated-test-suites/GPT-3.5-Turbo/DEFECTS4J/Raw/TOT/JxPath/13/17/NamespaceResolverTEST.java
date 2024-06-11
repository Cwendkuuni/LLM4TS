package org.apache.commons.jxpath.ri;

import org.apache.commons.jxpath.JXPathContext;
import org.apache.commons.jxpath.JXPathException;
import org.apache.commons.jxpath.JXPathNotFoundException;
import org.apache.commons.jxpath.Pointer;
import org.apache.commons.jxpath.ri.model.NodeIterator;
import org.apache.commons.jxpath.ri.model.NodePointer;
import org.junit.Before;
import org.junit.Test;

import static org.junit.Assert.*;

public class NamespaceResolverTest {

    private NamespaceResolver resolver;
    private NodePointer pointer;
    private NodePointer namespacePointer;

    @Before
    public void setUp() {
        resolver = new NamespaceResolver();

        pointer = new NodePointer("/root", null, resolver);
        namespacePointer = new NodePointer("/root/namespace", pointer, resolver);

        resolver.setNamespaceContextPointer(namespacePointer);
    }

    @Test
    public void testRegisterNamespace() {
        resolver.registerNamespace("ns1", "http://example.com/ns1");
        resolver.registerNamespace("ns2", "http://example.com/ns2");

        assertEquals("http://example.com/ns1", resolver.getNamespaceURI("ns1"));
        assertEquals("http://example.com/ns2", resolver.getNamespaceURI("ns2"));
    }

    @Test(expected = IllegalStateException.class)
    public void testRegisterNamespaceSealed() {
        resolver.seal();
        resolver.registerNamespace("ns1", "http://example.com/ns1");
    }

    @Test
    public void testGetNamespaceURI() {
        resolver.registerNamespace("ns1", "http://example.com/ns1");

        assertEquals("http://example.com/ns1", resolver.getNamespaceURI("ns1"));
        assertNull(resolver.getNamespaceURI("ns2"));
    }

    @Test
    public void testGetPrefix() {
        resolver.registerNamespace("ns1", "http://example.com/ns1");

        assertEquals("ns1", resolver.getPrefix("http://example.com/ns1"));
        assertNull(resolver.getPrefix("http://example.com/ns2"));
    }

    @Test
    public void testGetPrefixWithExternalNamespace() {
        Pointer namespaceContext = new Pointer() {

            @Override
            public Object getValue() {
                return null;
            }

            @Override
            public void setValue(Object value) {
            }

            @Override
            public Pointer createPath(JXPathContext context) {
                return null;
            }

            @Override
            public Object getBaseValue() {
                return null;
            }

            @Override
            public Object getImmediateNode() {
                return null;
            }

            @Override
            public Pointer getValuePointer() {
                return null;
            }

            @Override
            public boolean isActual() {
                return false;
            }

            @Override
            public boolean isCollection() {
                return false;
            }

            @Override
            public boolean isLeaf() {
                return false;
            }

            @Override
            public Iterator childIterator() {
                return null;
            }

            @Override
            public NodePointer getParent() {
                return null;
            }
        };

        resolver.setNamespaceContextPointer(namespaceContext);

        assertEquals("ns1", resolver.getPrefix("http://example.com/ns1"));
        assertNull(resolver.getPrefix("http://example.com/ns2"));
    }

    @Test(expected = JXPathNotFoundException.class)
    public void testGetNamespaceURINotFound() {
        resolver.getNamespaceURI("ns1");
    }

    @Test(expected = JXPathException.class)
    public void testGetPrefixNotFound() {
        resolver.getPrefix("http://example.com/ns1");
    }

    @Test
    public void testClone() {
        resolver.registerNamespace("ns1", "http://example.com/ns1");

        NamespaceResolver clonedResolver = (NamespaceResolver) resolver.clone();

        assertEquals(resolver.getNamespaceURI("ns1"), clonedResolver.getNamespaceURI("ns1"));

        clonedResolver.registerNamespace("ns2", "http://example.com/ns2");

        assertNull(resolver.getNamespaceURI("ns2"));
        assertEquals("http://example.com/ns2", clonedResolver.getNamespaceURI("ns2"));
    }

    @Test
    public void testIsSealed() {
        assertFalse(resolver.isSealed());

        resolver.seal();

        assertTrue(resolver.isSealed());
    }

    @Test
    public void testSealParent() {
        NamespaceResolver parentResolver = new NamespaceResolver();

        resolver = new NamespaceResolver(parentResolver);

        assertFalse(parentResolver.isSealed());
        assertFalse(resolver.isSealed());

        resolver.seal();

        assertTrue(parentResolver.isSealed());
        assertTrue(resolver.isSealed());
    }

}