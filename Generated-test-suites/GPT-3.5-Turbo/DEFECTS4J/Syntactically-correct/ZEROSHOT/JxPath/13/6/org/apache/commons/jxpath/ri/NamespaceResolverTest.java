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
        resolver.registerNamespace("prefix1", "http://www.namespace1.com");
        resolver.registerNamespace("prefix2", "http://www.namespace2.com");

        Assert.assertEquals("http://www.namespace1.com", resolver.getNamespaceURI("prefix1"));
        Assert.assertEquals("http://www.namespace2.com", resolver.getNamespaceURI("prefix2"));
    }

    @Test(expected = IllegalStateException.class)
    public void testRegisterNamespaceOnSealedResolver() {
        resolver.seal();
        resolver.registerNamespace("prefix", "http://www.namespace.com");
    }

    @Test
    public void testSetNamespaceContextPointer() {
        NodePointer pointer = new NodePointer();
        resolver.setNamespaceContextPointer(pointer);

        Assert.assertEquals(pointer, resolver.getNamespaceContextPointer());
    }

    @Test
    public void testGetNamespaceURI() {
        resolver.registerNamespace("prefix", "http://www.namespace.com");
        resolver.registerNamespace("prefix1", "http://www.namespace1.com");
        resolver.setNamespaceContextPointer(new NodePointer());

        Assert.assertEquals("http://www.namespace.com", resolver.getNamespaceURI("prefix"));
        Assert.assertEquals("http://www.namespace1.com", resolver.getNamespaceURI("prefix1"));
    }

    @Test
    public void testGetPrefix() {
        resolver.registerNamespace("prefix", "http://www.namespace.com");
        resolver.registerNamespace("prefix1", "http://www.namespace1.com");
        resolver.setNamespaceContextPointer(new NodePointer());

        Assert.assertEquals("prefix", resolver.getPrefix("http://www.namespace.com"));
        Assert.assertEquals("prefix1", resolver.getPrefix("http://www.namespace1.com"));
    }

    @Test
    public void testClone() {
        NamespaceResolver clonedResolver = (NamespaceResolver) resolver.clone();

        Assert.assertNotSame(resolver, clonedResolver);
        Assert.assertEquals(resolver.getClass(), clonedResolver.getClass());
        Assert.assertEquals(resolver.isSealed(), clonedResolver.isSealed());
    }

}