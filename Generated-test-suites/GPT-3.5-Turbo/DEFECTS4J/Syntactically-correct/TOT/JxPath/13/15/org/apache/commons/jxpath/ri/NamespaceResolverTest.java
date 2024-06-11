import org.junit.Assert;
import org.junit.Test;
import org.apache.commons.jxpath.ri.NamespaceResolver;
import org.apache.commons.jxpath.ri.model.NodePointer;
import org.apache.commons.jxpath.Pointer;

public class NamespaceResolverTest {

    @Test
    public void testRegisterNamespace() {
        NamespaceResolver resolver = new NamespaceResolver();

        resolver.registerNamespace("prefix1", "http://namespace1");
        resolver.registerNamespace("prefix2", "http://namespace2");
        resolver.registerNamespace("prefix3", "http://namespace3");

        Assert.assertEquals("http://namespace1", resolver.getNamespaceURI("prefix1"));
        Assert.assertEquals("http://namespace2", resolver.getNamespaceURI("prefix2"));
        Assert.assertEquals("http://namespace3", resolver.getNamespaceURI("prefix3"));
    }

    @Test(expected = IllegalStateException.class)
    public void testSealedRegisterNamespace() {
        NamespaceResolver parentResolver = new NamespaceResolver();
        parentResolver.registerNamespace("prefix", "http://namespace");

        NamespaceResolver resolver = new NamespaceResolver(parentResolver);
        resolver.seal();

        resolver.registerNamespace("newPrefix", "http://newNamespace");
    }

    @Test
    public void testGetNamespaceURI() {
        NamespaceResolver parentResolver = new NamespaceResolver();
        parentResolver.registerNamespace("prefix", "http://namespace");

        NamespaceResolver resolver = new NamespaceResolver(parentResolver);

        Assert.assertEquals("http://namespace", resolver.getNamespaceURI("prefix"));
        Assert.assertNull(resolver.getNamespaceURI("newPrefix"));
    }

    @Test
    public void testGetPrefix() {
        NamespaceResolver parentResolver = new NamespaceResolver();
        parentResolver.registerNamespace("prefix", "http://namespace");

        NamespaceResolver resolver = new NamespaceResolver(parentResolver);

        Assert.assertEquals("prefix", resolver.getPrefix("http://namespace"));
        Assert.assertNull(resolver.getPrefix("http://newNamespace"));
    }

    @Test
    public void testCloning() {
        NamespaceResolver parentResolver = new NamespaceResolver();
        parentResolver.registerNamespace("prefix1", "http://namespace1");
        parentResolver.registerNamespace("prefix2", "http://namespace2");

        NamespaceResolver resolver = new NamespaceResolver(parentResolver);
        NamespaceResolver clonedResolver = (NamespaceResolver) resolver.clone();

        Assert.assertNotSame(resolver, clonedResolver);
        Assert.assertEquals(resolver.getNamespaceURI("prefix1"), clonedResolver.getNamespaceURI("prefix1"));
        Assert.assertEquals(resolver.getNamespaceURI("prefix2"), clonedResolver.getNamespaceURI("prefix2"));
        Assert.assertEquals(resolver.getNamespaceURI("newPrefix"), clonedResolver.getNamespaceURI("newPrefix"));

        resolver.registerNamespace("prefix3", "http://namespace3");

        Assert.assertEquals("http://namespace3", resolver.getNamespaceURI("prefix3"));
        Assert.assertNull(clonedResolver.getNamespaceURI("prefix3"));
    }
    
    @Test
    public void testSetAndGetNamespaceContextPointer() {
        NodePointer pointer = new NodePointer();
        NamespaceResolver resolver = new NamespaceResolver();

        resolver.setNamespaceContextPointer(pointer);

        Assert.assertEquals(pointer, resolver.getNamespaceContextPointer());
    }
    
    @Test
    public void testGetNamespaceContextPointerWithParent() {
        NodePointer pointer = new NodePointer();
        NamespaceResolver parentResolver = new NamespaceResolver();
        parentResolver.setNamespaceContextPointer(pointer);

        NamespaceResolver resolver = new NamespaceResolver(parentResolver);

        Assert.assertEquals(pointer, resolver.getNamespaceContextPointer());
    }
    
    @Test
    public void testGetNamespaceURIWithParentPointer() {
        NodePointer pointer = new NodePointer();
        pointer.setNamespaceURI("http://namespace");
        NamespaceResolver parentResolver = new NamespaceResolver();
        parentResolver.setNamespaceContextPointer(pointer);

        NamespaceResolver resolver = new NamespaceResolver(parentResolver);

        Assert.assertEquals("http://namespace", resolver.getNamespaceURI("prefix"));
    }
}