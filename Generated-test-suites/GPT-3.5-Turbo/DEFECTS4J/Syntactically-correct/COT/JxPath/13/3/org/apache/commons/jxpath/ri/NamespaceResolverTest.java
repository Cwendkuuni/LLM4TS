
import org.apache.commons.jxpath.Pointer;
import org.apache.commons.jxpath.ri.model.NodeIterator;
import org.apache.commons.jxpath.ri.model.NodePointer;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

public class NamespaceResolverTest {

    private NamespaceResolver namespaceResolver;

    @Before
    public void setUp() {
        namespaceResolver = new NamespaceResolver();
    }

    @Test
    public void testRegisterNamespace() {
        String prefix = "testPrefix";
        String namespaceURI = "http://test.com";
        namespaceResolver.registerNamespace(prefix, namespaceURI);
        Assert.assertEquals(namespaceURI, namespaceResolver.getNamespaceURI(prefix));
    }

    @Test(expected = IllegalStateException.class)
    public void testRegisterNamespaceSealed() {
        NamespaceResolver sealedResolver = new NamespaceResolver();
        sealedResolver.seal();
        sealedResolver.registerNamespace("testPrefix", "http://test.com");
    }

    @Test
    public void testSetNamespaceContextPointer() {
        NodePointer pointer = new NodePointer();
        namespaceResolver.setNamespaceContextPointer(pointer);
        Assert.assertEquals(pointer, namespaceResolver.getNamespaceContextPointer());
    }

    @Test
    public void testGetNamespaceURI() {
        String prefix = "testPrefix";
        String namespaceURI = "http://test.com";
        namespaceResolver.registerNamespace(prefix, namespaceURI);
        Assert.assertEquals(namespaceURI, namespaceResolver.getNamespaceURI(prefix));
    }

    @Test
    public void testGetPrefix() {
        String prefix = "testPrefix";
        String namespaceURI = "http://test.com";
        namespaceResolver.registerNamespace(prefix, namespaceURI);
        Assert.assertEquals(prefix, namespaceResolver.getPrefix(namespaceURI));
    }
    
    @Test
    public void testGetPrefixNoMatch() {
        String prefix = namespaceResolver.getPrefix("http://test.com");
        Assert.assertNull(prefix);
    }
    
    @Test
    public void testGetPrefixWithParentResolver() {
        NamespaceResolver parentResolver = new NamespaceResolver();
        parentResolver.registerNamespace("testPrefix", "http://test.com");
        namespaceResolver = new NamespaceResolver(parentResolver);
        Assert.assertEquals("testPrefix", namespaceResolver.getPrefix("http://test.com"));
    }

    @Test
    public void testIsSealed() {
        Assert.assertFalse(namespaceResolver.isSealed());
    }

    @Test
    public void testSeal() {
        namespaceResolver.seal();
        Assert.assertTrue(namespaceResolver.isSealed());
    }

    @Test
    public void testClone() {
        NamespaceResolver clonedResolver = (NamespaceResolver) namespaceResolver.clone();
        Assert.assertNotSame(namespaceResolver, clonedResolver);
    }

}