package org.apache.commons.jxpath.ri;

import org.apache.commons.jxpath.NodePointer;
import org.apache.commons.jxpath.Pointer;
import org.apache.commons.jxpath.ri.model.NamespaceResolver;
import org.apache.commons.jxpath.ri.model.NamespaceResolver.NamespaceURI;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

import java.util.HashMap;
import java.util.Map;

/**
 * Tests for {@link NamespaceResolver}.
 */
public class NamespaceResolverTest {

    private NamespaceResolver namespaceResolver;
    private NamespaceResolver parentNamespaceResolver;

    @Before
    public void setUp() {
        namespaceResolver = new NamespaceResolver();
        parentNamespaceResolver = new NamespaceResolver(null);
    }

    @Test
    public void testNewInstance() {
        NamespaceResolver resolver = new NamespaceResolver();
        Assert.assertNull(resolver.getNamespaceURI("prefix"));
        Assert.assertNull(resolver.getPrefix("namespaceURI"));
    }

    @Test
    public void testRegisterNamespace() {
        namespaceResolver.registerNamespace("prefix", "namespaceURI");
        Assert.assertEquals("namespaceURI", namespaceResolver.getNamespaceURI("prefix"));
        Assert.assertEquals("prefix", namespaceResolver.getPrefix("namespaceURI"));
    }

    @Test
    public void testRegisterNamespaceWithSamePrefix() {
        namespaceResolver.registerNamespace("prefix", "namespaceURI1");
        try {
            namespaceResolver.registerNamespace("prefix", "namespaceURI2");
            Assert.fail();
        } catch (IllegalStateException e) {
            Assert.assertEquals("Cannot register namespaces on a sealed NamespaceResolver", e.getMessage());
        }
    }

    @Test
    public void testSealedNamespaceResolver() {
        namespaceResolver.registerNamespace("prefix", "namespaceURI");
        namespaceResolver.seal();
        try {
            namespaceResolver.registerNamespace("prefix2", "namespaceURI2");
            Assert.fail();
        } catch (IllegalStateException e) {
            Assert.assertEquals("Cannot register namespaces on a sealed NamespaceResolver", e.getMessage());
        }
    }

    @Test
    public void testSetNamespaceContextPointer() {
        NodePointer pointer = new NodePointer("root", new HashMap<>());
        namespaceResolver.setNamespaceContextPointer(pointer);
        Assert.assertEquals(pointer, namespaceResolver.getNamespaceContextPointer());
    }

    @Test
    public void testGetNamespaceURIForRegisteredPrefix() {
        namespaceResolver.registerNamespace("prefix", "namespaceURI");
        Assert.assertEquals("namespaceURI", namespaceResolver.getNamespaceURI("prefix"));
    }

    @Test
    public void testGetNamespaceURIForNonRegisteredPrefix() {
        Assert.assertNull(namespaceResolver.getNamespaceURI("prefix"));
    }

    @Test
    public void testGetNamespaceURIForRegisteredPrefixWithContext() {
        String uri = "http://example.com";
        NodePointer pointer = new NodePointer("root", new HashMap<>());
        pointer.setNamespaceURI(uri, "prefix");
        namespaceResolver.setNamespaceContextPointer(pointer);
        Assert.assertEquals(uri, namespaceResolver.getNamespaceURI("prefix"));
    }

    @Test
    public void testGetNamespaceURIForNonRegisteredPrefixWithContext() {
        NodePointer pointer = new NodePointer("root", new HashMap<>());
        namespaceResolver.setNamespaceContextPointer(pointer);
        Assert.assertNull(namespaceResolver.getNamespaceURI("prefix"));
    }

    @Test
    public void testGetNamespaceURIForRegisteredPrefixWithParent() {
        namespaceResolver.registerNamespace("prefix", "namespaceURI");
        parentNamespaceResolver.registerNamespace("prefix2", "namespaceURI2");
        namespaceResolver = new NamespaceResolver(parentNamespaceResolver);
        Assert.assertEquals("namespaceURI", namespaceResolver.getNamespaceURI("prefix"));
        Assert.assertEquals("namespaceURI2", namespaceResolver.getNamespaceURI("prefix2"));
    }

    @Test
    public void testGetPrefixForRegisteredNamespaceURI() {
        namespaceResolver.registerNamespace("prefix", "namespaceURI");
        Assert.assertEquals("prefix", namespaceResolver.getPrefix("namespaceURI"));
    }

    @Test
    public void testGetPrefixForNonRegisteredNamespaceURI() {
        Assert.assertNull(namespaceResolver.getPrefix("namespaceURI"));
    }

    @Test
    public void testGetPrefixForRegisteredNamespaceURIWithParent() {
        namespaceResolver.registerNamespace("prefix", "namespaceURI");
        parentNamespaceResolver.registerNamespace("prefix2", "namespaceURI2");
        namespaceResolver = new NamespaceResolver(parentNamespaceResolver);
        Assert.assertEquals("prefix", namespaceResolver.getPrefix("namespaceURI"));
        Assert.assertEquals("prefix2", namespaceResolver.getPrefix("namespaceURI2"));
    }

    @Test
    public void testGetPrefixForRegisteredNamespaceURIWithContext() {
        String uri = "http://example.com";
        NodePointer pointer = new NodePointer("root", new HashMap<>());
        pointer.setNamespaceURI(uri, "prefix");
        namespaceResolver.setNamespaceContextPointer(pointer);
        Assert.assertEquals("prefix", namespaceResolver.getPrefix("namespaceURI"));
    }

    @Test(expected = IllegalStateException.class)
    public void testGetPrefixForNonRegisteredNamespaceURIWithContext() {
        NodePointer pointer = new NodePointer("root", new HashMap<>());
        namespaceResolver.setNamespaceContextPointer(pointer);
        namespaceResolver.getPrefix("namespaceURI");
    }

    @Test
    public void testGetPrefixForRegisteredNamespaceURIWithParentAndContext() {
        namespaceResolver.registerNamespace("prefix", "namespaceURI");
        parentNamespaceResolver.registerNamespace("prefix2", "namespaceURI2");
        NodePointer pointer = new NodePointer("root", new HashMap<>());
        pointer.setNamespaceURI("http://example.com", "prefix3");
        namespaceResolver = new NamespaceResolver(parentNamespaceResolver);
        namespaceResolver.setNamespaceContextPointer(pointer);
        Assert.assertEquals("prefix", namespaceResolver.getPrefix("namespaceURI"));
        Assert.assertEquals("prefix2", namespaceResolver.getPrefix("namespaceURI2"));
        Assert.assertEquals("prefix3", namespaceResolver.getPrefix("namespaceURI3"));
    }

    @Test
    public void testClone() {
        namespaceResolver.registerNamespace("prefix", "namespaceURI");
        NamespaceResolver cloned = (NamespaceResolver) namespaceResolver.clone();
        Assert.assertEquals("namespaceURI", cloned.getNamespaceURI("prefix"));
    }

    @Test
    public void testCloneWithParent() {
        namespaceResolver.registerNamespace("prefix", "namespaceURI");
        parentNamespaceResolver.registerNamespace("prefix2", "namespaceURI2");
        NamespaceResolver cloned = new NamespaceResolver(parentNamespaceResolver);
        NamespaceResolver clonedWithParent = (NamespaceResolver) namespaceResolver.clone();
        clonedWithParent.registerNamespace("prefix3", "namespaceURI3");
        Assert.assertEquals("namespaceURI", cloned.getNamespaceURI("prefix"));
        Assert.assertEquals("namespaceURI2", cloned.getNamespaceURI("prefix2"));
        Assert.assertEquals("namespaceURI3", clonedWithParent.getNamespaceURI("prefix3"));
    }
}