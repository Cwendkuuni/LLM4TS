import org.apache.commons.jxpath.ri.model.NodeIterator;
import org.apache.commons.jxpath.Pointer;
import org.apache.commons.jxpath.ri.model.NodePointer;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;

public class NamespaceResolverTest {
  
    private NamespaceResolver namespaceResolver;

    @Before
    public void setUp() {
        namespaceResolver = new NamespaceResolver();
    }

    @Test
    public void testRegisterNamespace() {
        String prefix = "prefix";
        String uri = "uri";
        namespaceResolver.registerNamespace(prefix, uri);
        String result = namespaceResolver.getNamespaceURI(prefix);
        Assert.assertEquals(uri, result);
    }
    
    @Test
    public void testSetNamespaceContextPointer() {
        NodePointer pointer = new NodePointer();
        namespaceResolver.setNamespaceContextPointer(pointer);
        Pointer result = namespaceResolver.getNamespaceContextPointer();
        Assert.assertEquals(pointer, result);
    }
    
    @Test
    public void testGetNamespaceURI() {
        String prefix = "prefix";
        String uri = "uri";
        namespaceResolver.registerNamespace(prefix, uri);
        String result = namespaceResolver.getNamespaceURI(prefix);
        Assert.assertEquals(uri, result);
    }

    @Test
    public void testGetNamespaceURI_ContextPointer() {
        String prefix = "prefix";
        String uri = "uri";
        namespaceResolver.registerNamespace(prefix, uri);
        NodePointer pointer = new NodePointer();
        pointer.setNamespaceURI(prefix, uri);
        namespaceResolver.setNamespaceContextPointer(pointer);
        String result = namespaceResolver.getNamespaceURI(prefix);
        Assert.assertEquals(uri, result);
    }

    @Test
    public void testGetNamespaceURI_ParentResolver() {
        String prefix = "prefix";
        String uri = "uri";
        namespaceResolver.registerNamespace(prefix, uri);
        NamespaceResolver parentResolver = new NamespaceResolver();
        parentResolver.registerNamespace("parentPrefix", "parentUri");
        namespaceResolver = new NamespaceResolver(parentResolver);
        String result = namespaceResolver.getNamespaceURI("parentPrefix");
        Assert.assertEquals("parentUri", result);
    }

    @Test
    public void testGetPrefix() {
        String prefix = "prefix";
        String uri = "uri";
        namespaceResolver.registerNamespace(prefix, uri);
        String result = namespaceResolver.getPrefix(uri);
        Assert.assertEquals(prefix, result);
    }

    @Test
    public void testGetPrefix_ContextPointer() {
        String prefix = "prefix";
        String uri = "uri";
        namespaceResolver.registerNamespace(prefix, uri);
        NodePointer pointer = new NodePointer();
        pointer.setNamespaceURI(prefix, uri);
        namespaceResolver.setNamespaceContextPointer(pointer);
        String result = namespaceResolver.getPrefix(uri);
        Assert.assertEquals(prefix, result);
    }

    @Test
    public void testGetPrefix_NoPrefixFound_ParentResolver() {
        String prefix = "prefix";
        String uri = "uri";
        namespaceResolver.registerNamespace(prefix, uri);
        NamespaceResolver parentResolver = new NamespaceResolver();
        parentResolver.registerNamespace("parentPrefix", "parentUri");
        namespaceResolver = new NamespaceResolver(parentResolver);
        String result = namespaceResolver.getPrefix("parentUri");
        Assert.assertEquals("parentPrefix", result);
    }

    @Test
    public void testIsSealed() {
        boolean result = namespaceResolver.isSealed();
        Assert.assertFalse(result);
    }

    @Test
    public void testSeal() {
        namespaceResolver.seal();
        boolean result = namespaceResolver.isSealed();
        Assert.assertTrue(result);
    }

    @Test
    public void testClone() {
        NamespaceResolver clonedResolver = (NamespaceResolver) namespaceResolver.clone();
        Assert.assertNotSame(namespaceResolver, clonedResolver);
        Assert.assertFalse(clonedResolver.isSealed());
    }

}