import org.apache.commons.jxpath.NamespaceResolver;
import org.apache.commons.jxpath.NodePointer;
import org.apache.commons.jxpath.ri.model.NodeIterator;
import org.junit.Assert;
import org.junit.Test;

import java.util.HashMap;
import java.util.Map;

public class NamespaceResolverTest {

    @Test
    public void testConstructor() {
        NamespaceResolver namespaceResolver = new NamespaceResolver();
        Assert.assertNull(namespaceResolver.getParent());
        Assert.assertNotNull(namespaceResolver.getNamespaceMap());
        Assert.assertNull(namespaceResolver.getReverseMap());
        Assert.assertNull(namespaceResolver.getPointer());
        Assert.assertFalse(namespaceResolver.isSealed());
    }

    @Test
    public void testConstructorWithParent() {
        NamespaceResolver parentResolver = new NamespaceResolver();
        NamespaceResolver namespaceResolver = new NamespaceResolver(parentResolver);
        Assert.assertSame(parentResolver, namespaceResolver.getParent());
        Assert.assertNull(namespaceResolver.getNamespaceMap());
        Assert.assertNull(namespaceResolver.getReverseMap());
        Assert.assertNull(namespaceResolver.getPointer());
        Assert.assertFalse(namespaceResolver.isSealed());
    }

    @Test
    public void testRegisterNamespace() {
        NamespaceResolver namespaceResolver = new NamespaceResolver();
        namespaceResolver.registerNamespace("prefix1", "http://example.com/ns1");
        Map<String, String> namespaceMap = namespaceResolver.getNamespaceMap();
        Assert.assertEquals("http://example.com/ns1", namespaceMap.get("prefix1"));
        Assert.assertNull(namespaceMap.get("prefix2"));
        Assert.assertNull(namespaceResolver.getReverseMap());
        Assert.assertNull(namespaceResolver.getPointer());
        Assert.assertFalse(namespaceResolver.isSealed());

        namespaceResolver.registerNamespace("prefix2", "http://example.com/ns2");
        namespaceMap = namespaceResolver.getNamespaceMap();
        Assert.assertEquals("http://example.com/ns1", namespaceMap.get("prefix1"));
        Assert.assertEquals("http://example.com/ns2", namespaceMap.get("prefix2"));
        Assert.assertNotNull(namespaceResolver.getReverseMap());
        Assert.assertEquals("prefix1", namespaceResolver.getReverseMap().get("http://example.com/ns1"));
        Assert.assertEquals("prefix2", namespaceResolver.getReverseMap().get("http://example.com/ns2"));
        Assert.assertNull(namespaceResolver.getPointer());
        Assert.assertFalse(namespaceResolver.isSealed());
    }

    @Test
    public void testSetNamespaceContextPointer() {
        NamespaceResolver namespaceResolver = new NamespaceResolver();
        NodePointer pointer = new NodePointer();
        namespaceResolver.setNamespaceContextPointer(pointer);
        Assert.assertSame(pointer, namespaceResolver.getNamespaceContextPointer());
        Assert.assertNull(namespaceResolver.getNamespaceMap());
        Assert.assertNull(namespaceResolver.getReverseMap());
        Assert.assertNull(namespaceResolver.getPointer());
        Assert.assertFalse(namespaceResolver.isSealed());
    }

    @Test
    public void testGetNamespaceURI() {
        NamespaceResolver namespaceResolver = new NamespaceResolver();
        namespaceResolver.registerNamespace("prefix1", "http://example.com/ns1");
        Assert.assertEquals("http://example.com/ns1", namespaceResolver.getNamespaceURI("prefix1"));
        Assert.assertNull(namespaceResolver.getNamespaceURI("prefix2"));
        NodePointer pointer = new NodePointer();
        namespaceResolver.setNamespaceContextPointer(pointer);
        namespaceResolver.registerNamespace("prefix2", "http://example.com/ns2");
        Assert.assertEquals("http://example.com/ns2", namespaceResolver.getNamespaceURI("prefix2"));
        Assert.assertEquals("http://example.com/ns1", namespaceResolver.getNamespaceURI("prefix1"));
        Assert.assertNull(namespaceResolver.getNamespaceURI("prefix3"));
    }

    @Test
    public void testGetPrefix() {
        NamespaceResolver namespaceResolver = new NamespaceResolver();
        namespaceResolver.registerNamespace("prefix1", "http://example.com/ns1");
        Assert.assertEquals("prefix1", namespaceResolver.getPrefix("http://example.com/ns1"));
        Assert.assertNull(namespaceResolver.getPrefix("http://example.com/ns2"));
        NodePointer pointer = new NodePointer();
        namespaceResolver.setNamespaceContextPointer(pointer);
        namespaceResolver.registerNamespace("prefix2", "http://example.com/ns2");
        Assert.assertEquals("prefix2", namespaceResolver.getPrefix("http://example.com/ns2"));
        Assert.assertEquals("prefix1", namespaceResolver.getPrefix("http://example.com/ns1"));
        Assert.assertNull(namespaceResolver.getPrefix("http://example.com/ns3"));

        namespaceResolver.seal();
        Assert.assertEquals("prefix1", namespaceResolver.getPrefix("http://example.com/ns1"));
        Assert.assertEquals("prefix2", namespaceResolver.getPrefix("http://example.com/ns2"));
        Assert.assertNull(namespaceResolver.getPrefix("http://example.com/ns3"));
    }

    @Test
    public void testIsSealed() {
        NamespaceResolver namespaceResolver = new NamespaceResolver();
        Assert.assertFalse(namespaceResolver.isSealed());
        namespaceResolver.seal();
        Assert.assertTrue(namespaceResolver.isSealed());
    }

    @Test(expected = IllegalStateException.class)
    public void testRegisterNamespaceOnSealedNamespaceResolver() {
        NamespaceResolver namespaceResolver = new NamespaceResolver();
        namespaceResolver.registerNamespace("prefix1", "http://example.com/ns1");
        namespaceResolver.seal();
        namespaceResolver.registerNamespace("prefix2", "http://example.com/ns2");
    }

    @Test
    public void testClone() {
        NamespaceResolver namespaceResolver = new NamespaceResolver();
        namespaceResolver.registerNamespace("prefix1", "http://example.com/ns1");
        NamespaceResolver clonedResolver = (NamespaceResolver) namespaceResolver.clone();
        Assert.assertNotSame(namespaceResolver, clonedResolver);
        Assert.assertEquals("http://example.com/ns1", clonedResolver.getNamespaceURI("prefix1"));
        Assert.assertNull(clonedResolver.getNamespaceURI("prefix2"));
        Assert.assertNull(namespaceResolver.getReverseMap());
        Assert.assertNull(namespaceResolver.getPointer());
        Assert.assertFalse(namespaceResolver.isSealed());
    }
}