import org.apache.commons.jxpath.NamespaceResolver;
import org.apache.commons.jxpath.Pointer;
import org.apache.commons.jxpath.ri.model.NodeIterator;
import org.apache.commons.jxpath.ri.model.NodePointer;
import org.junit.Assert;
import org.junit.Test;

import java.util.HashMap;
import java.util.Map;

/**
 * JUnit test cases for NamespaceResolver class.
 */
public class NamespaceResolverTest {

    @Test
    public void testNewInstance() {
        NamespaceResolver resolver = new NamespaceResolver();
        Assert.assertNull(resolver.getParent());
        Assert.assertNull(resolver.getNamespaceMap());
        Assert.assertNull(resolver.getReverseMap());
        Assert.assertNull(resolver.getPointer());
        Assert.assertFalse(resolver.isSealed());
    }

    @Test
    public void testNewInstanceWithParent() {
        NamespaceResolver parentResolver = new NamespaceResolver();
        NamespaceResolver resolver = new NamespaceResolver(parentResolver);
        Assert.assertSame(parentResolver, resolver.getParent());
        Assert.assertNull(resolver.getNamespaceMap());
        Assert.assertNull(resolver.getReverseMap());
        Assert.assertNull(resolver.getPointer());
        Assert.assertFalse(resolver.isSealed());
    }

    @Test
    public void testRegisterNamespace() {
        NamespaceResolver resolver = new NamespaceResolver();
        resolver.registerNamespace("test", "http://test.com");
        Map<String, String> namespaceMap = resolver.getNamespaceMap();
        Assert.assertNotNull(namespaceMap);
        Assert.assertEquals("http://test.com", namespaceMap.get("test"));
        Assert.assertEquals(1, namespaceMap.size());
        Assert.assertNull(resolver.getReverseMap());
        Assert.assertNull(resolver.getPointer());
        Assert.assertFalse(resolver.isSealed());
    }

    @Test(expected = IllegalStateException.class)
    public void testRegisterNamespaceOnSealedResolver() {
        NamespaceResolver resolver = new NamespaceResolver();
        resolver.seal();
        resolver.registerNamespace("test", "http://test.com");
    }

    @Test
    public void testGetNamespaceURI() {
        NamespaceResolver resolver = new NamespaceResolver();
        resolver.registerNamespace("test", "http://test.com");
        Assert.assertEquals("http://test.com", resolver.getNamespaceURI("test"));
        Assert.assertEquals(null, resolver.getNamespaceURI("unknown"));
        Pointer pointer = new NodePointer("test:element");
        resolver.setNamespaceContextPointer(pointer);
        Assert.assertEquals("http://test.com", resolver.getNamespaceURI("test"));
        Assert.assertEquals(null, resolver.getNamespaceURI("unknown"));
        NamespaceResolver parentResolver = new NamespaceResolver();
        parentResolver.registerNamespace("parent", "http://parent.com");
        resolver = new NamespaceResolver(parentResolver);
        Assert.assertEquals("http://parent.com", resolver.getNamespaceURI("parent"));
        Assert.assertEquals("http://test.com", resolver.getNamespaceURI("test"));
        Assert.assertEquals(null, resolver.getNamespaceURI("unknown"));
    }

    @Test
    public void testGetPrefix() {
        NamespaceResolver resolver = new NamespaceResolver();
        resolver.registerNamespace("test", "http://test.com");
        Assert.assertEquals(null, resolver.getPrefix("http://test.com"));
        Assert.assertEquals("test", resolver.getPrefix("test"));
        Assert.assertEquals(null, resolver.getPrefix("unknown"));
        Pointer pointer = new NodePointer("test:element");
        resolver.setNamespaceContextPointer(pointer);
        Assert.assertEquals("test", resolver.getPrefix("test"));
        Assert.assertEquals(null, resolver.getPrefix("http://test.com"));
        Assert.assertEquals(null, resolver.getPrefix("unknown"));
        NamespaceResolver parentResolver = new NamespaceResolver();
        parentResolver.registerNamespace("parent", "http://parent.com");
        parentResolver.registerNamespace("parentTest", "http://parent.com/test");
        resolver = new NamespaceResolver(parentResolver);
        Assert.assertEquals("parentTest", resolver.getPrefix("http://parent.com/test"));
        Assert.assertEquals("test", resolver.getPrefix("test"));
        Assert.assertEquals(null, resolver.getPrefix("http://test.com"));
        Assert.assertEquals(null, resolver.getPrefix("unknown"));
    }

    @Test
    public void testIsSealed() {
        NamespaceResolver resolver = new NamespaceResolver();
        Assert.assertFalse(resolver.isSealed());
        resolver.seal();
        Assert.assertTrue(resolver.isSealed());
    }

    @Test
    public void testClone() {
        NamespaceResolver resolver = new NamespaceResolver();
        resolver.registerNamespace("test", "http://test.com");
        NamespaceResolver clonedResolver = (NamespaceResolver) resolver.clone();
        Assert.assertNotSame(resolver, clonedResolver);
        Assert.assertEquals("http://test.com", clonedResolver.getNamespaceURI("test"));
        Assert.assertEquals(null, clonedResolver.getNamespaceURI("unknown"));
        Assert.assertEquals(null, clonedResolver.getPointer());
        Assert.assertFalse(clonedResolver.isSealed());
    }

    @Test
    public void testNamespaceIterator() {
        NamespaceResolver resolver = new NamespaceResolver();
        resolver.registerNamespace("test1", "http://test1.com");
        resolver.registerNamespace("test2", "http://test2.com");
        Pointer pointer = new NodePointer("test1:element");
        resolver.setNamespaceContextPointer(pointer);
        NodeIterator nsIterator = pointer.namespaceIterator();
        Map<String, String> namespaceMap = new HashMap<>();
        for (int position = 1; nsIterator.setPosition(position); position++) {
            NodePointer nsPointer = nsIterator.getNodePointer();
            String uri = nsPointer.getNamespaceURI();
            String prefix = nsPointer.getName().getName();
            namespaceMap.put(prefix, uri);
        }
        Assert.assertEquals(2, nsIterator.getLength());
        Assert.assertEquals("http://test1.com", namespaceMap.get("test1"));
        Assert.assertEquals("http://test2.com", namespaceMap.get("test2"));
    }

}