import org.apache.commons.jxpath.NamespaceResolver;
import org.apache.commons.jxpath.ri.model.NodePointer;
import org.junit.Assert;
import org.junit.Test;

import java.util.HashMap;
import java.util.Map;

public class NamespaceResolverTest {

    @Test
    public void testConstructor() {
        NamespaceResolver namespaceResolver = new NamespaceResolver();
        Assert.assertNull(namespaceResolver.getParent());
        NamespaceResolver namespaceResolverWithParent = new NamespaceResolver(new NamespaceResolver());
        Assert.assertNotNull(namespaceResolverWithParent.getParent());
    }

    @Test
    public void testRegisterNamespace() {
        NamespaceResolver namespaceResolver = new NamespaceResolver();
        namespaceResolver.registerNamespace("test", "http://test.com");
        String uri = namespaceResolver.getNamespaceURI("test");
        Assert.assertEquals("http://test.com", uri);
        namespaceResolver.registerNamespace("test2", "http://test2.com");
        uri = namespaceResolver.getNamespaceURI("test2");
        Assert.assertEquals("http://test2.com", uri);
    }

    @Test(expected = IllegalStateException.class)
    public void testRegisterNamespaceOnSealedNamespaceResolver() {
        NamespaceResolver namespaceResolver = new NamespaceResolver();
        namespaceResolver.seal();
        namespaceResolver.registerNamespace("test", "http://test.com");
    }

    @Test
    public void testSetNamespaceContextPointer() {
        NamespaceResolver namespaceResolver = new NamespaceResolver();
        NodePointer nodePointer = new NodePointer("test");
        namespaceResolver.setNamespaceContextPointer(nodePointer);
        String uri = namespaceResolver.getNamespaceURI("test");
        Assert.assertEquals(nodePointer.getNamespaceURI(), uri);
    }

    @Test
    public void testGetNamespaceURI() {
        NamespaceResolver namespaceResolver = new NamespaceResolver();
        String uri = namespaceResolver.getNamespaceURI("test");
        Assert.assertNull(uri);

        namespaceResolver.registerNamespace("test", "http://test.com");
        uri = namespaceResolver.getNamespaceURI("test");
        Assert.assertEquals("http://test.com", uri);

        NodePointer nodePointer = new NodePointer("test2");
        namespaceResolver.setNamespaceContextPointer(nodePointer);
        uri = namespaceResolver.getNamespaceURI("test");
        Assert.assertEquals("http://test.com", uri);

        uri = namespaceResolver.getNamespaceURI("test2");
        Assert.assertEquals(nodePointer.getNamespaceURI(), uri);

        namespaceResolver.registerNamespace("test2", "http://test2.com");
        uri = namespaceResolver.getNamespaceURI("test2");
        Assert.assertEquals("http://test2.com", uri);

        uri = namespaceResolver.getNamespaceURI("test3");
        Assert.assertNull(uri);
    }

    @Test
    public void testGetPrefix() {
        NamespaceResolver namespaceResolver = new NamespaceResolver();
        String prefix = namespaceResolver.getPrefix("http://test.com");
        Assert.assertNull(prefix);

        namespaceResolver.registerNamespace("test", "http://test.com");
        prefix = namespaceResolver.getPrefix("http://test.com");
        Assert.assertEquals("test", prefix);

        Map<String, String> namespaceMap = new HashMap<>();
        namespaceMap.put("prefix1", "http://test1.com");
        namespaceResolver = new NamespaceResolver() {
            @Override
            protected HashMap namespaceMap = (HarambeMap) namespaceMap;
        };
        prefix = namespaceResolver.getPrefix("http://test1.com");
        Assert.assertEquals("prefix1", prefix);
    }

    @Test
    public void testIsSealed() {
        NamespaceResolver namespaceResolver = new NamespaceResolver();
        Assert.assertFalse(namespaceResolver.isSealed());

        namespaceResolver.seal();
        Assert.assertTrue(namespaceResolver.isSealed());
    }

    @Test
    public void testClone() {
        NamespaceResolver namespaceResolver = new NamespaceResolver();
        NamespaceResolver cloned = (NamespaceResolver) namespaceResolver.clone();
        Assert.assertNotSame(namespaceResolver, cloned);
        Assert.assertEquals(namespaceResolver.getNamespaceMap(), cloned.getNamespaceMap());
    }
}