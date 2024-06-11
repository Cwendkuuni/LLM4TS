import org.apache.commons.jxpath.NamespaceResolver;
import org.apache.commons.jxpath.ri.model.NodePointer;
import org.junit.Assert;
import org.junit.Test;

import java.util.HashMap;
import java.util.Map;

public class NamespaceResolverTest {

    @Test
    public void testConstructor() {
        NamespaceResolver resolver = new NamespaceResolver();
        Assert.assertNull(resolver.getParent());
        Assert.assertEquals(0, resolver.size());
    }

    @Test
    public void testConstructorWithParent() {
        NamespaceResolver parent = new NamespaceResolver();
        NamespaceResolver resolver = new NamespaceResolver(parent);
        Assert.assertNotNull(resolver.getParent());
        Assert.assertEquals(parent, resolver.getParent());
    }

    @Test
    public void testRegisterNamespace() {
        NamespaceResolver resolver = new NamespaceResolver();
        resolver.registerNamespace("prefix", "uri");
        Assert.assertEquals("uri", resolver.getNamespaceURI("prefix"));
        Assert.assertEquals(1, resolver.size());
    }

    @Test(expected = IllegalStateException.class)
    public void testRegisterNamespaceOnSealedResolver() {
        NamespaceResolver resolver = new NamespaceResolver();
        resolver.seal();
        resolver.registerNamespace("prefix", "uri");
    }

    @Test
    public void testGetNamespaceURI() {
        NamespaceResolver resolver = new NamespaceResolver();
        resolver.registerNamespace("prefix", "uri");
        Assert.assertEquals("uri", resolver.getNamespaceURI("prefix"));
        Assert.assertNull(resolver.getNamespaceURI("unknown-prefix"));
        NodePointer pointer = new NodePointer();
        resolver.setNamespaceContextPointer(pointer);
        pointer.setNamespaceURI("prefix", "other-uri");
        Assert.assertEquals("other-uri", resolver.getNamespaceURI("prefix"));
    }

    @Test
    public void testGetPrefix() {
        NamespaceResolver resolver = new NamespaceResolver();
        resolver.registerNamespace("prefix", "uri");
        Assert.assertEquals("prefix", resolver.getPrefix("uri"));
        Assert.assertNull(resolver.getPrefix("unknown-uri"));
        NodePointer pointer = new NodePointer();
        resolver.setNamespaceContextPointer(pointer);
        pointer.setNamespaceURI("uri", "prefix");
        Assert.assertEquals("prefix", resolver.getPrefix("uri"));
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
        NamespaceResolver origin = new NamespaceResolver();
        origin.registerNamespace("prefix", "uri");
        NamespaceResolver clone = (NamespaceResolver) origin.clone();
        Assert.assertNotSame(origin, clone);
        Assert.assertEquals(origin, clone);
        Assert.assertEquals("uri", clone.getNamespaceURI("prefix"));
    }

    @Test
    public void testSize() {
        NamespaceResolver resolver = new NamespaceResolver();
        resolver.registerNamespace("prefix1", "uri1");
        resolver.registerNamespace("prefix2", "uri2");
        Assert.assertEquals(2, resolver.size());

        resolver.seal();
        Assert.assertEquals(2, resolver.size());
    }

    @Test
    public void testRegisterNamespaceWithEmptyPrefix() {
        NamespaceResolver resolver = new NamespaceResolver();
        resolver.registerNamespace("", "uri");
        Assert.assertEquals("uri", resolver.getNamespaceURI(""));
        Assert.assertEquals(1, resolver.size());
    }

    @Test(expected = IllegalArgumentException.class)
    public void testRegisterNamespaceWithEmptyNamespaceURI() {
        NamespaceResolver resolver = new NamespaceResolver();
        resolver.registerNamespace("prefix", "");
    }

    @Test
    public void testGetNamespaceURIWithEmptyPrefix() {
        NamespaceResolver resolver = new NamespaceResolver();
        resolver.registerNamespace("", "uri");
        Assert.assertEquals("uri", resolver.getNamespaceURI(""));
        Assert.assertNull(resolver.getNamespaceURI("prefix"));
    }

    @Test
    public void testGetPrefixWithEmptyNamespaceURI() {
        NamespaceResolver resolver = new NamespaceResolver();
        resolver.registerNamespace("", "uri");
        Assert.assertEquals("", resolver.getPrefix("uri"));
        Assert.assertNull(resolver.getPrefix("prefix"));
    }

    @Test
    public void testGetNamespaceURIWithNullPrefix() {
        NamespaceResolver resolver = new NamespaceResolver();
        Assert.assertNull(resolver.getNamespaceURI(null));
    }

    @Test
    public void testGetPrefixWithNullNamespaceURI() {
        NamespaceResolver resolver = new NamespaceResolver();
        Assert.assertNull(resolver.getPrefix(null));
    }

    @Test
    public void testGetNamespaceURIWithEmptyContextPointer() {
        NamespaceResolver resolver = new NamespaceResolver();
        NodePointer pointer = new NodePointer();
        resolver.setNamespaceContextPointer(pointer);
        Assert.assertNull(resolver.getNamespaceURI(null));
    }

    @Test
    public void testGetPrefixWithNullContextPointer() {
        NamespaceResolver resolver = new NamespaceResolver();
        NodePointer pointer = new NodePointer();
        resolver.setNamespaceContextPointer(pointer);
        Assert.assertNull(resolver.getPrefix(null));
    }
}