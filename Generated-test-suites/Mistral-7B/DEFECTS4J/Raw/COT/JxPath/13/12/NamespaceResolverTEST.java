import org.apache.commons.jxpath.NamespaceResolver;
import org.apache.commons.jxpath.Pointer;
import org.apache.commons.jxpath.ri.model.NodeIterator;
import org.apache.commons.jxpath.ri.model.NodePointer;
import org.junit.Assert;
import org.junit.Test;

import java.util.HashMap;
import java.util.Map;

/**
 * Test class for NamespaceResolver.
 */
public class NamespaceResolverTest {

    @Test
    public void testConstructor() {
        NamespaceResolver resolver = new NamespaceResolver();
        Assert.assertNull(resolver.getParent());
        Assert.assertNull(resolver.getNamespaceMap());
        Assert.assertNull(resolver.getReverseMap());
        Assert.assertNull(resolver.getPointer());
        Assert.assertFalse(resolver.isSealed());
    }

    @Test
    public void testConstructorWithParent() {
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
        resolver.registerNamespace("xsd", "http://www.w3.org/2001/XMLSchema");
        Map namespaceMap = resolver.getNamespaceMap();
        Assert.assertEquals("http://www.w3.org/2001/XMLSchema", namespaceMap.get("xsd"));
        Assert.assertEquals(1, namespaceMap.size());
    }

    @Test(expected = IllegalStateException.class)
    public void testRegisterNamespaceOnSealedResolver() {
        NamespaceResolver resolver = new NamespaceResolver();
        resolver.registerNamespace("xsd", "http://www.w3.org/2001/XMLSchema");
        resolver.seal();
        resolver.registerNamespace("foo", "bar");
    }

    @Test
    public void testSetNamespaceContextPointer() {
        NamespaceResolver resolver = new NamespaceResolver();
        NodePointer pointer = new NodePointer("dummy");
        resolver.setNamespaceContextPointer(pointer);
        Assert.assertSame(pointer, resolver.getNamespaceContextPointer());
    }

    @Test
    public void testGetNamespaceURI() {
        NamespaceResolver resolver = new NamespaceResolver();
        String prefix = "xsd";
        String namespaceURI = "http://www.w3.org/2001/XMLSchema";
        resolver.registerNamespace(prefix, namespaceURI);
        String uri = resolver.getNamespaceURI(prefix);
        Assert.assertEquals(namespaceURI, uri);
        Pointer pointer = new NodePointer("dummy");
        resolver.setNamespaceContextPointer(pointer);
        uri = resolver.getNamespaceURI(prefix);
        Assert.assertEquals(namespaceURI, uri);
        if (resolver.getParent() != null) {
            String parentUri = resolver.getParent().getNamespaceURI(prefix);
            Assert.assertEquals(namespaceURI, uri);
            Assert.assertEquals(prefix, resolver.getParent().getPrefix(namespaceURI));
        }
    }

    @Test
    public void testGetPrefix() {
        NamespaceResolver resolver = new NamespaceResolver();
        String namespaceURI = "http://www.w3.org/2001/XMLSchema";
        resolver.registerNamespace("xsd", namespaceURI);
        String prefix = resolver.getPrefix(namespaceURI);
        Assert.assertEquals(prefix, "xsd");
        Pointer pointer = new NodePointer("dummy");
        resolver.setNamespaceContextPointer(pointer);
        prefix = resolver.getPrefix(namespaceURI);
        Assert.assertEquals(prefix, "xsd");
        if (resolver.getParent() != null) {
            String parentPrefix = resolver.getParent().getPrefix(namespaceURI);
            Assert.assertEquals(prefix, parentPrefix);
            Assert.assertEquals(namespaceURI, resolver.getParent().getNamespaceURI(prefix));
        }
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
        NamespaceResolver original = new NamespaceResolver();
        NamespaceResolver cloned = (NamespaceResolver) original.clone();
        Assert.assertNotSame(original, cloned);
        Assert.assertEquals(original.getNamespaceMap(), cloned.getNamespaceMap());
        Assert.assertFalse(original.isSealed());
        Assert.assertFalse(cloned.isSealed());
    }

    @Test
    public void testRegisterNamespacesMultipleTimes() {
        NamespaceResolver resolver = new NamespaceResolver();
        String prefix = "xsd";
        String namespaceURI = "http://www.w3.org/2001/XMLSchema";
        resolver.registerNamespace(prefix, namespaceURI);
        resolver.registerNamespace(prefix, namespaceURI);
        Map namespaceMap = resolver.getNamespaceMap();
        Assert.assertEquals(1, namespaceMap.size());
    }

    @Test
    public void testGetNamespaceURIWithEmptyPrefix() {
        NamespaceResolver resolver = new NamespaceResolver();
        String namespaceURI = "http://www.w3.org/2001/XMLSchema";
        resolver.registerNamespace("", namespaceURI);
        String uri = resolver.getNamespaceURI("");
        Assert.assertEquals(namespaceURI, uri);
        Pointer pointer = new NodePointer("dummy");
        resolver.setNamespaceContextPointer(pointer);
        uri = resolver.getNamespaceURI("");
        Assert.assertEquals(namespaceURI, uri);
        if (resolver.getParent() != null) {
            String parentUri = resolver.getParent().getNamespaceURI("");
            Assert.assertEquals(namespaceURI, uri);
            Assert.assertEquals("", resolver.getParent().getPrefix(namespaceURI));
        }
    }

    @Test
    public void testGetNamespaceURIWithNullPrefix() {
        NamespaceResolver resolver = new NamespaceResolver();
        String namespaceURI = "http://www.w3.org/2001/XMLSchema";
        resolver.registerNamespace(null, namespaceURI);
        String uri = resolver.getNamespaceURI(null);
        Assert.assertEquals(namespaceURI, uri);
        Pointer pointer = new NodePointer("dummy");
        resolver.setNamespaceContextPointer(pointer);
        uri = resolver.getNamespaceURI(null);
        Assert.assertEquals(namespaceURI, uri);
        if (resolver.getParent() != null) {
            String parentUri = resolver.getParent().getNamespaceURI(null);
            Assert.assertEquals(namespaceURI, uri);
            Assert.assertNull(resolver.getParent().getPrefix(namespaceURI));
        }
    }

    @Test
    public void testGetNamespaceURIWithEmptyNamespaceURI() {
        NamespaceResolver resolver = new NamespaceResolver();
        String prefix = "xsd";
        String namespaceURI = "";
        resolver.registerNamespace(prefix, namespaceURI);
        String uri = resolver.getNamespaceURI(prefix);
        Assert.assertEquals(namespaceURI, uri);
        Pointer pointer = new NodePointer("dummy");
        resolver.setNamespaceContextPointer(pointer);
        uri = resolver.getNamespaceURI(prefix);
        Assert.assertEquals(namespaceURI, uri);
        if (resolver.getParent() != null) {
            String parentUri = resolver.getParent().getNamespaceURI(prefix);
            Assert.assertEquals(namespaceURI, uri);
            Assert.assertEquals(prefix, resolver.getParent().getPrefix(namespaceURI));
        }
    }

    @Test(expected = NullPointerException.class)
    public void testGetNamespaceURIWithNullNamespaceURI() {
        NamespaceResolver resolver = new NamespaceResolver();
        String prefix = "xsd";
        String namespaceURI = null;
        resolver.registerNamespace(prefix, namespaceURI);
        resolver.getNamespaceURI(prefix);
    }

    @Test(expected = NullPointerException.class)
    public void testGetNamespaceURIWithNullPrefix() {
        NamespaceResolver resolver = new NamespaceResolver();
        String namespaceURI = "http://www.w3.org/2001/XMLSchema";
        resolver.getNamespaceURI(null);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testGetNamespaceURIWithEmptyStringAsPrefix() {
        NamespaceResolver resolver = new NamespaceResolver();
        String namespaceURI = "http://www.w3.org/2001/XMLSchema";
        resolver.getNamespaceURI("");
    }

    @Test
    public void testGetPrefixWithEmptyNamespaceURI() {
        NamespaceResolver resolver = new NamespaceResolver();
        String namespaceURI = "";
        resolver.registerNamespace("xsd", namespaceURI);
        String prefix = resolver.getPrefix(namespaceURI);
        Assert.assertNull(prefix);
        Pointer pointer = new NodePointer("dummy");
        resolver.setNamespaceContextPointer(pointer);
        prefix = resolver.getPrefix(namespaceURI);
        Assert.assertNull(prefix);
        if (resolver.getParent() != null) {
            String parentPrefix = resolver.getParent().getPrefix(namespaceURI);
            Assert.assertNull(parentPrefix);
            Assert.assertEquals(namespaceURI, resolver.getParent().getNamespaceURI(prefix));
        }
    }

    @Test
    public void testGetPrefixWithNullNamespaceURI() {
        NamespaceResolver resolver = new NamespaceResolver();
        String prefix = resolver.getPrefix(null);
        Assert.assertNull(prefix);
        Pointer pointer = new NodePointer("dummy");
        resolver.setNamespaceContextPointer(pointer);
        prefix = resolver.getPrefix(null);
        Assert.assertNull(prefix);
        if (resolver.getParent() != null) {
            String parentPrefix = resolver.getParent().getPrefix(null);
            Assert.assertNull(parentPrefix);
            Assert.assertNull(resolver.getParent().getNamespaceURI(prefix));
        }
    }

    @Test(expected = NullPointerException.class)
    public void testGetPrefixWithNullPrefix() {
        NamespaceResolver resolver = new NamespaceResolver();
        String namespaceURI = "http://www.w3.org/2001/XMLSchema";
        resolver.getPrefix(null);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testGetPrefixWithEmptyStringAsPrefix() {
        NamespaceResolver resolver = new NamespaceResolver();
        String namespaceURI = "http://www.w3.org/2001/XMLSchema";
        resolver.getPrefix("");
    }

    @Test
    public void testIsSealedWithNullParent() {
        NamespaceResolver resolver = new NamespaceResolver();
        Assert.assertFalse(resolver.isSealed());
        resolver.seal();
        Assert.assertTrue(resolver.isSealed());
        NamespaceResolver parentResolver = null;
        Assert.assertFalse(resolver.isSealedWithParent(parentResolver));
    }

    @Test
    public void testIsSealedWithSealedParent() {
        NamespaceResolver parentResolver = new NamespaceResolver();
        parentResolver.seal();
        NamespaceResolver resolver = new NamespaceResolver(parentResolver);
        Assert.assertTrue(resolver.isSealed());
    }

    @Test
    public void testIsSealedWithUnsealedParent() {
        NamespaceResolver parentResolver = new NamespaceResolver();
        NamespaceResolver resolver = new NamespaceResolver(parentResolver);
        Assert.assertFalse(resolver.isSealed());
    }

    @Test(expected = IllegalStateException.class)
    public void testRegisterNamespaceAfterSeal() {
        NamespaceResolver resolver = new NamespaceResolver();
        resolver.registerNamespace("xsd", "http://www.w3.org/2001/XMLSchema");
        resolver.seal();
        resolver.registerNamespace("foo", "bar");
    }
}