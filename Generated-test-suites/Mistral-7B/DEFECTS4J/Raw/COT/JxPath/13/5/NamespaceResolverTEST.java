import org.apache.commons.jxpath. NamespaceContext;
import org.apache.commons.jxpath.ri.Namespaces;
import org.apache.commons.jxpath.ri.model.NodeIterator;
import org.apache.commons.jxpath.ri.model.NodePointer;
import org.apache.commons.jxpath.ri.NamespaceResolver;
import org.junit.Assert;
import org.junit.Test;

import java.util.HashMap;
import java.util.Map;

/**
 * JUnit 4 test cases for NamespaceResolver class.
 */
public class NamespaceResolverTest {

    @Test
    public void testConstructor() {
        NamespaceResolver namespaceResolver = new NamespaceResolver();
        Assert.assertNull(namespaceResolver.getParent());
        Assert.assertNull(namespaceResolver.getNamespaceMap());
        Assert.assertNull(namespaceResolver.getReverseMap());
        Assert.assertNull(namespaceResolver.getPointer());
        Assert.assertFalse(namespaceResolver.isSealed());

        NamespaceResolver parentNamespaceResolver = new NamespaceResolver(null);
        namespaceResolver = new NamespaceResolver(parentNamespaceResolver);
        Assert.assertSame(parentNamespaceResolver, namespaceResolver.getParent());
    }

    @Test
    public void testRegisterNamespace() {
        NamespaceResolver namespaceResolver = new NamespaceResolver();

        namespaceResolver.registerNamespace("xsd", "http://www.w3.org/2001/XMLSchema");
        Map<String, String> namespaceMap = namespaceResolver.getNamespaceMap();
        Assert.assertNotNull(namespaceMap);
        Assert.assertEquals("http://www.w3.org/2001/XMLSchema", namespaceMap.get("xsd"));

        Assert.assertNull(namespaceResolver.getNamespaceURI("test"));

        namespaceResolver.setNamespaceContextPointer(new NodePointer(new NamespaceContext(), new HashMap(), Namespaces.emptyNamespaces));
        Assert.assertEquals("http://www.w3.org/2001/XMLSchema", namespaceResolver.getNamespaceURI("xsd"));

        namespaceResolver.registerNamespace("xmlns", "http://www.w3.org/2000/xmlns/");
        Assert.assertEquals("http://www.w3.org/2000/xmlns/", namespaceResolver.getNamespaceURI("xmlns"));

        try {
            namespaceResolver.registerNamespace("xsd", "http://www.w3.org/2001/XMLSchema"); // already registered
            Assert.fail();
        } catch (IllegalStateException e) {
            // Expected
        }
    }

    @Test
    public void testGetPrefix() {
        NamespaceResolver namespaceResolver = new NamespaceResolver();

        namespaceResolver.registerNamespace("xsd", "http://www.w3.org/2001/XMLSchema");
        Assert.assertNull(namespaceResolver.getPrefix("http://www.w3.org/2001/XMLSchema"));

        namespaceResolver.setNamespaceContextPointer(new NodePointer(new NamespaceContext(), new HashMap(), Namespaces.emptyNamespaces));
        namespaceResolver.getNamespaceContextPointer().addNamespace("xsd", "http://www.w3.org/2001/XMLSchema");
        Assert.assertEquals("xsd", namespaceResolver.getPrefix("http://www.w3.org/2001/XMLSchema"));

        namespaceResolver.registerNamespace("xmlns", "http://www.w3.org/2000/xmlns/");
        Assert.assertEquals("xmlns", namespaceResolver.getPrefix("http://www.w3.org/2000/xmlns/"));

        Assert.assertNull(namespaceResolver.getPrefix("http://example.com"));
    }

    @Test
    public void testIsSealed() {
        NamespaceResolver namespaceResolver = new NamespaceResolver();
        Assert.assertFalse(namespaceResolver.isSealed());

        namespaceResolver.seal();
        Assert.assertTrue(namespaceResolver.isSealed());

        try {
            namespaceResolver.registerNamespace("xsd", "http://www.w3.org/2001/XMLSchema");
            Assert.fail();
        } catch (IllegalStateException e) {
            // Expected
        }
    }

    @Test
    public void testClone() {
        NamespaceResolver namespaceResolver = new NamespaceResolver();

        namespaceResolver.registerNamespace("xsd", "http://www.w3.org/2001/XMLSchema");
        Map<String, String> originalNamespaceMap = namespaceResolver.getNamespaceMap();

        NamespaceResolver clonedNamespaceResolver = (NamespaceResolver) namespaceResolver.clone();
        Map<String, String> clonedNamespaceMap = clonedNamespaceResolver.getNamespaceMap();
        Assert.assertNotNull(clonedNamespaceMap);
        Assert.assertEquals(originalNamespaceMap, clonedNamespaceMap);
    }

    @Test(expected = IllegalStateException.class)
    public void testSetNamespaceContextPointerWithNullPointer() {
        NamespaceResolver namespaceResolver = new NamespaceResolver();
        namespaceResolver.setNamespaceContextPointer(null);
    }

    @Test
    public void testGetNamespaceURIWithNullPrefix() {
        NamespaceResolver namespaceResolver = new NamespaceResolver();
        Assert.assertNull(namespaceResolver.getNamespaceURI(null));
    }

    @Test(expected = IllegalArgumentException.class)
    public void testRegisterNamespaceWithNullPrefix() {
        NamespaceResolver namespaceResolver = new NamespaceResolver();
        namespaceResolver.registerNamespace(null, "http://www.w3.org/2001/XMLSchema");
    }

    @Test(expected = IllegalArgumentException.class)
    public void testRegisterNamespaceWithEmptyPrefix() {
        NamespaceResolver namespaceResolver = new NamespaceResolver();
        namespaceResolver.registerNamespace("", "http://www.w3.org/2001/XMLSchema");
    }

    @Test(expected = IllegalArgumentException.class)
    public void testRegisterNamespaceWithEmptyURI() {
        NamespaceResolver namespaceResolver = new NamespaceResolver();
        namespaceResolver.registerNamespace("xsd", "");
    }

    @Test
    public void testRegisterNamespaceWithExistingPrefix() {
        NamespaceResolver namespaceResolver = new NamespaceResolver();
        namespaceResolver.registerNamespace("xsd", "http://www.w3.org/2001/XMLSchema");

        try {
            namespaceResolver.registerNamespace("xsd", "http://www.w3.org/2001/XMLSchema");
            Assert.fail();
        } catch (IllegalStateException e) {
            // Expected
        }
    }

    @Test
    public void testGetPrefixWithNullURI() {
        NamespaceResolver namespaceResolver = new NamespaceResolver();
        Assert.assertNull(namespaceResolver.getPrefix(null));
    }

    @Test
    public void testGetPrefixWithEmptyURI() {
        NamespaceResolver namespaceResolver = new NamespaceResolver();
        Assert.assertNull(namespaceResolver.getPrefix(""));
    }

    @Test
    public void testGetPrefixWithExistingPrefix() {
        NamespaceResolver namespaceResolver = new NamespaceResolver();
        namespaceResolver.registerNamespace("xsd", "http://www.w3.org/2001/XMLSchema");
        Assert.assertEquals("xsd", namespaceResolver.getPrefix("http://www.w3.org/2001/XMLSchema"));
    }

    @Test
    public void testGetPrefixWithNonExistingPrefix() {
        NamespaceResolver namespaceResolver = new NamespaceResolver();
        Assert.assertNull(namespaceResolver.getPrefix("test"));
    }
}