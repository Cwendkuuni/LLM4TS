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
        Assert.assertNull(namespaceResolver.getNamespaceMap());
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
        namespaceResolver.registerNamespace("xsd", "http://www.w3.org/2001/XMLSchema");
        Map<String, String> namespaceMap = namespaceResolver.getNamespaceMap();
        Assert.assertEquals("http://www.w3.org/2001/XMLSchema", namespaceMap.get("xsd"));
        Assert.assertNull(namespaceMap.get("x:xsi"));
        Assert.assertFalse(namespaceResolver.isSealed());
    }

    @Test(expected = IllegalStateException.class)
    public void testRegisterNamespaceOnSealedResolver() {
        NamespaceResolver namespaceResolver = new NamespaceResolver();
        namespaceResolver.seal();
        namespaceResolver.registerNamespace("xsd", "http://www.w3.org/2001/XMLSchema");
    }

    @Test
    public void testGetNamespaceURI() {
        NamespaceResolver namespaceResolver = new NamespaceResolver();
        namespaceResolver.registerNamespace("xsd", "http://www.w3.org/2001/XMLSchema");
        Assert.assertEquals("http://www.w3.org/2001/XMLSchema", namespaceResolver.getNamespaceURI("xsd"));
        Assert.assertNull(namespaceResolver.getNamespaceURI("x"));
        Pointer pointer = new NodePointer("", new HashMap<>());
        namespaceResolver.setNamespaceContextPointer(pointer);
        Assert.assertNull(namespaceResolver.getNamespaceURI("xsd"));
        namespaceResolver.registerNamespace("x", "http://example.com");
        Assert.assertEquals("http://example.com", namespaceResolver.getNamespaceURI("x"));
        Assert.assertEquals("http://www.w3.org/2001/XMLSchema", namespaceResolver.getNamespaceURI("xsd"));
    }

    @Test
    public void testGetPrefix() {
        NamespaceResolver namespaceResolver = new NamespaceResolver();
        namespaceResolver.registerNamespace("xsd", "http://www.w3.org/2001/XMLSchema");
        Assert.assertEquals("xsd", namespaceResolver.getPrefix("http://www.w3.org/2001/XMLSchema"));
        Assert.assertNull(namespaceResolver.getPrefix("http://example.com"));
        Pointer pointer = new NodePointer("", new HashMap<>());
        namespaceResolver.setNamespaceContextPointer(pointer);
        Assert.assertNull(namespaceResolver.getPrefix("http://www.w3.org/2001/XMLSchema"));
        namespaceResolver.registerNamespace("x", "http://example.com");
        Assert.assertEquals("x", namespaceResolver.getPrefix("http://example.com"));
        Assert.assertEquals("xsd", namespaceResolver.getPrefix("http://www.w3.org/2001/XMLSchema"));

        namespaceResolver = new NamespaceResolver();
        NamespaceResolver parentResolver = new NamespaceResolver();
        namespaceResolver.setParent(parentResolver);
        namespaceResolver.registerNamespace("xsd", "http://www.w3.org/2001/XMLSchema");
        Assert.assertEquals("xsd", namespaceResolver.getPrefix("http://www.w3.org/2001/XMLSchema"));
        Assert.assertNull(namespaceResolver.getPrefix("http://example.com"));
        parentResolver.registerNamespace("x", "http://example.com");
        Assert.assertEquals("x", namespaceResolver.getPrefix("http://example.com"));
        Assert.assertEquals("xsd", namespaceResolver.getPrefix("http://www.w3.org/2001/XMLSchema"));
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
        namespaceResolver.registerNamespace("xsd", "http://www.w3.org/2001/XMLSchema");
        NamespaceResolver clonedResolver = (NamespaceResolver) namespaceResolver.clone();
        Assert.assertNotSame(namespaceResolver, clonedResolver);
        Assert.assertEquals("http://www.w3.org/2001/XMLSchema", clonedResolver.getNamespaceURI("xsd"));
        Assert.assertNull(clonedResolver.getNamespaceURI("x"));
        Assert.assertFalse(clonedResolver.isSealed());
    }

    @Test
    public void testGetNamespaceMap() {
        NamespaceResolver namespaceResolver = new NamespaceResolver();
        namespaceResolver.registerNamespace("xsd", "http://www.w3.org/2001/XMLSchema");
        Map<String, String> namespaceMap = namespaceResolver.getNamespaceMap();
        Assert.assertEquals("http://www.w3.org/2001/XMLSchema", namespaceMap.get("xsd"));
        Assert.assertNull(namespaceMap.get("x:xsi"));
        Assert.assertFalse(namespaceResolver.isSealed());
    }

    @Test
    public void testGetReverseMap() {
        NamespaceResolver namespaceResolver = new NamespaceResolver();
        namespaceResolver.registerNamespace("xsd", "http://www.w3.org/2001/XMLSchema");
        namespaceResolver.registerNamespace("x", "http://example.com");
        Map<String, String> reverseMap = namespaceResolver.getReverseMap();
        Assert.assertEquals("xsd", reverseMap.get("http://www.w3.org/2001/XMLSchema"));
        Assert.assertEquals("x", reverseMap.get("http://example.com"));
        Assert.assertNull(reverseMap.get(""));
        Assert.assertFalse(namespaceResolver.isSealed());
    }
}