import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

public class NamespaceResolverTest {

    private NamespaceResolver resolver;

    @Before
    public void setUp() {
        resolver = new NamespaceResolver();
    }

    @Test
    public void testRegisterNamespace() {
        resolver.registerNamespace("prefix1", "namespaceURI1");
        String uri = resolver.getNamespaceURI("prefix1");
        Assert.assertEquals("namespaceURI1", uri);
    }

    @Test(expected = IllegalStateException.class)
    public void testRegisterNamespaceSealed() {
        resolver.seal();
        resolver.registerNamespace("prefix2", "namespaceURI2");
    }

    @Test
    public void testSetNamespaceContextPointer() {
        NodePointer pointer = new NodePointer();
        resolver.setNamespaceContextPointer(pointer);
        Pointer result = resolver.getNamespaceContextPointer();
        Assert.assertEquals(pointer, result);
    }

    @Test
    public void testGetNamespaceURI() {
        resolver.registerNamespace("prefix3", "namespaceURI3");
        String uri = resolver.getNamespaceURI("prefix3");
        Assert.assertEquals("namespaceURI3", uri);
    }

    @Test
    public void testGetNamespaceURIUndefinedPrefix() {
        String uri = resolver.getNamespaceURI("undefinedPrefix");
        Assert.assertNull(uri);
    }

    @Test
    public void testGetNamespaceURIExternal() {
        resolver.registerNamespace("prefix4", "namespaceURI4");
        NodePointer pointer = new NodePointer();
        pointer.setNamespaceURI("prefix4", "externalNamespaceURI4");
        resolver.setNamespaceContextPointer(pointer);
        String uri = resolver.getNamespaceURI("prefix4");
        Assert.assertEquals("externalNamespaceURI4", uri);
    }

    @Test
    public void testGetPrefix() {
        resolver.registerNamespace("prefix5", "namespaceURI5");
        String prefix = resolver.getPrefix("namespaceURI5");
        Assert.assertEquals("prefix5", prefix);
    }

    @Test
    public void testGetPrefixUndefinedNamespaceURI() {
        String prefix = resolver.getPrefix("undefinedNamespaceURI");
        Assert.assertNull(prefix);
    }

    @Test
    public void testGetPrefixExternal() {
        resolver.registerNamespace("prefix6", "namespaceURI6");
        NodePointer pointer = new NodePointer();
        pointer.setNamespaceURI("prefix6", "externalNamespaceURI6");
        resolver.setNamespaceContextPointer(pointer);
        String prefix = resolver.getPrefix("externalNamespaceURI6");
        Assert.assertEquals("prefix6", prefix);
    }

    @Test
    public void testIsSealed() {
        boolean sealed = resolver.isSealed();
        Assert.assertFalse(sealed);
    }

    @Test
    public void testSeal() {
        resolver.seal();
        boolean sealed = resolver.isSealed();
        Assert.assertTrue(sealed);
    }

    @Test
    public void testClone() {
        NamespaceResolver clone = (NamespaceResolver) resolver.clone();
        Assert.assertNotSame(resolver, clone);
        Assert.assertEquals(resolver.isSealed(), clone.isSealed());
    }
}