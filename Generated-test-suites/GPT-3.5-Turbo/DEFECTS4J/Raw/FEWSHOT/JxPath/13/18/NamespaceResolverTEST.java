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
    private NodePointer pointer;

    @Before
    public void setUp() {
        resolver = new NamespaceResolver();
        pointer = new NodePointer();
    }

    @Test
    public void testRegisterNamespace() {
        String prefix = "prefix";
        String namespaceURI = "http://www.example.com";
        resolver.registerNamespace(prefix, namespaceURI);

        String result = resolver.getNamespaceURI(prefix);
        assertEquals(namespaceURI, result);
    }

    @Test
    public void testRegisterNamespace_SealedResolver() {
        resolver.seal();
        String prefix = "prefix";
        String namespaceURI = "http://www.example.com";

        try {
            resolver.registerNamespace(prefix, namespaceURI);
        } catch (IllegalStateException e) {
            assertEquals("Cannot register namespaces on a sealed NamespaceResolver",
                    e.getMessage());
        }
    }

    @Test
    public void testGetNamespaceURI() {
        String prefix = "prefix";
        String namespaceURI = "http://www.example.com";
        resolver.registerNamespace(prefix, namespaceURI);

        String result = resolver.getNamespaceURI(prefix);
        assertEquals(namespaceURI, result);
    }

    @Test
    public void testGetNamespaceURI_PrefixUndefined() {
        String prefix = "prefix";

        String result = resolver.getNamespaceURI(prefix);
        assertNull(result);
    }

    @Test
    public void testGetNamespaceURI_NullParent() {
        NamespaceResolver parent = new NamespaceResolver();
        resolver = new NamespaceResolver(parent);

        String prefix = "prefix";
        String namespaceURI = "http://www.example.com";
        parent.registerNamespace(prefix, namespaceURI);

        String result = resolver.getNamespaceURI(prefix);
        assertEquals(namespaceURI, result);
    }

    @Test
    public void testGetNamespaceURI_PrefixDefinedOnPointer() {
        pointer.setNamespaceURI("prefix", "http://www.example.com");
        resolver.setNamespaceContextPointer(pointer);

        String prefix = "prefix";

        String result = resolver.getNamespaceURI(prefix);
        assertEquals("http://www.example.com", result);
    }

    @Test
    public void testGetNamespaceURI_PrefixDefinedOnParentPointer() {
        pointer.setNamespaceURI("prefix", "http://www.example.com");
        resolver.setNamespaceContextPointer(pointer);

        NamespaceResolver parent = new NamespaceResolver();
        resolver = new NamespaceResolver(parent);

        String prefix = "prefix";

        String result = resolver.getNamespaceURI(prefix);
        assertEquals("http://www.example.com", result);
    }

    @Test
    public void testGetPrefix() {
        String namespaceURI = "http://www.example.com";
        resolver.registerNamespace("prefix", namespaceURI);

        String result = resolver.getPrefix(namespaceURI);
        assertEquals("prefix", result);
    }

    @Test
    public void testGetPrefix_ExternalPrefixNotFound() {
        String result = resolver.getPrefix("http://www.example.com");
        assertNull(result);
    }

    @Test
    public void testGetPrefix_NamespacePrefixOnPointer() {
        pointer.setName(new NodePointer.Name("prefix"));
        pointer.setNamespaceURI("http://www.example.com");
        resolver.setNamespaceContextPointer(pointer);

        String result = resolver.getPrefix("http://www.example.com");
        assertEquals("prefix", result);
    }

    @Test
    public void testGetPrefix_ParentResolver() {
        pointer.setName(new NodePointer.Name("prefix"));
        pointer.setNamespaceURI("http://www.example.com");
        resolver.setNamespaceContextPointer(pointer);

        NamespaceResolver parent = new NamespaceResolver();
        resolver = new NamespaceResolver(parent);

        String result = resolver.getPrefix("http://www.example.com");
        assertEquals("prefix", result);
    }

}