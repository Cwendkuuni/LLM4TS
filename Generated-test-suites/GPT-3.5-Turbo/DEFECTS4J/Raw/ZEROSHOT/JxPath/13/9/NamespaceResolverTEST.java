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
        namespaceResolver.registerNamespace("prefix", "uri");
        String actual = namespaceResolver.getNamespaceURI("prefix");
        String expected = "uri";
        Assert.assertEquals(expected, actual);
    }

    @Test(expected = IllegalStateException.class)
    public void testRegisterNamespace_sealed() {
        namespaceResolver.seal();
        namespaceResolver.registerNamespace("prefix", "uri");
    }

    @Test
    public void testGetNamespaceURI() {
        namespaceResolver.registerNamespace("prefix", "uri");
        String actual = namespaceResolver.getNamespaceURI("prefix");
        String expected = "uri";
        Assert.assertEquals(expected, actual);
    }

    @Test
    public void testGetNamespaceURI_contextNode() {
        NodePointer pointer = new NodePointer();
        namespaceResolver.setNamespaceContextPointer(pointer);
        pointer.setNamespaceURI("prefix", "uri");
        String actual = namespaceResolver.getNamespaceURI("prefix");
        String expected = "uri";
        Assert.assertEquals(expected, actual);
    }

    @Test
    public void testGetNamespaceURI_parent() {
        NamespaceResolver parentResolver = new NamespaceResolver();
        parentResolver.registerNamespace("prefix", "uri");
        namespaceResolver = new NamespaceResolver(parentResolver);
        String actual = namespaceResolver.getNamespaceURI("prefix");
        String expected = "uri";
        Assert.assertEquals(expected, actual);
    }

    @Test
    public void testGetPrefix() {
        namespaceResolver.registerNamespace("prefix", "uri");
        String actual = namespaceResolver.getPrefix("uri");
        String expected = "prefix";
        Assert.assertEquals(expected, actual);
    }

    @Test
    public void testGetPrefix_parent() {
        NamespaceResolver parentResolver = new NamespaceResolver();
        parentResolver.registerNamespace("prefix", "uri");
        namespaceResolver = new NamespaceResolver(parentResolver);
        String actual = namespaceResolver.getPrefix("uri");
        String expected = "prefix";
        Assert.assertEquals(expected, actual);
    }

    @Test
    public void testClone() {
        NamespaceResolver clonedResolver = (NamespaceResolver) namespaceResolver.clone();
        Assert.assertNotSame(namespaceResolver, clonedResolver);
        Assert.assertFalse(clonedResolver.isSealed());
    }
}