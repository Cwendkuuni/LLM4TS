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

    /**
     * Test constructor.
     */
    @Test
    public void testConstructor() {
        NamespaceResolver resolver = new NamespaceResolver();
        Assert.assertNull(resolver.getParent());
        Assert.assertNotNull(resolver.getNamespaceMap());
        Assert.assertNull(resolver.getReverseMap());
        Assert.assertNull(resolver.getPointer());
        Assert.assertFalse(resolver.isSealed());
    }

    /**
     * Test constructor with parent.
     */
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

    /**
     * Test registerNamespace method.
     */
    @Test
    public void testRegisterNamespace() {
        NamespaceResolver resolver = new NamespaceResolver();
        resolver.registerNamespace("xsd", "http://www.w3.org/2001/XMLSchema");
        Assert.assertEquals("xsd", resolver.getPrefix("http://www.w3.org/2001/XMLSchema"));
        Assert.assertEquals("http://www.w3.org/2001/XMLSchema", resolver.getNamespaceURI("xsd"));
        Assert.assertNotNull(resolver.getNamespaceMap());
        Assert.assertNull(resolver.getReverseMap());
        Assert.assertNull(resolver.getPointer());
        Assert.assertFalse(resolver.isSealed());
    }

    /**
     * Test registerNamespace method with sealed resolver.
     */
    @Test(expected = IllegalStateException.class)
    public void testRegisterNamespaceWithSealedResolver() {
        NamespaceResolver resolver = new NamespaceResolver();
        resolver.seal();
        resolver.registerNamespace("xsd", "http://www.w3.org/2001/XMLSchema");
    }

    /**
     * Test getPrefix method.
     */
    @Test
    public void testGetPrefix() {
        NamespaceResolver resolver = new NamespaceResolver();
        resolver.registerNamespace("xsd", "http://www.w3.org/2001/XMLSchema");
        Assert.assertEquals("xsd", resolver.getPrefix("http://www.w3.org/2001/XMLSchema"));
        Assert.assertEquals("http://www.w3.org/2001/XMLSchema", resolver.getNamespaceURI("xsd"));
        Assert.assertNull(resolver.getPointer());
        Assert.assertFalse(resolver.isSealed());

        NamespaceResolver parentResolver = new NamespaceResolver();
        parentResolver.registerNamespace("xmlns", "http://www.w3.org/2000/xmlns/");
        resolver = new NamespaceResolver(parentResolver);
        Assert.assertEquals("xmlns", resolver.getPrefix("http://www.w3.org/2000/xmlns/"));
        Assert.assertEquals("http://www.w3.org/2000/xmlns/", resolver.getNamespaceURI("xmlns"));
        Assert.assertNull(resolver.getPointer());
        Assert.assertFalse(resolver.isSealed());
    }

    /**
     * Test getNamespaceURI method.
     */
    @Test
    public void testGetNamespaceURI() {
        NamespaceResolver resolver = new NamespaceResolver();
        resolver.registerNamespace("xsd", "http://www.w3.org/2001/XMLSchema");
        Assert.assertEquals("http://www.w3.org/2001/XMLSchema", resolver.getNamespaceURI("xsd"));
        Assert.assertEquals("xsd", resolver.getPrefix("http://www.w3.org/2001/XMLSchema"));
        Assert.assertNull(resolver.getPointer());
        Assert.assertFalse(resolver.isSealed());

        NamespaceResolver parentResolver = new NamespaceResolver();
        parentResolver.registerNamespace("xmlns", "http://www.w3.org/2000/xmlns/");
        resolver = new NamespaceResolver(parentResolver);
        Assert.assertEquals("http://www.w3.org/2000/xmlns/", resolver.getNamespaceURI("xmlns"));
        Assert.assertEquals("xmlns", resolver.getPrefix("http://www.w3.org/2000/xmlns/"));
        Assert.assertNull(resolver.getPointer());
        Assert.assertFalse(resolver.isSealed());
    }

    /**
     * Test getNamespaceContextPointer method.
     */
    @Test
    public void testGetNamespaceContextPointer() {
        NamespaceResolver resolver = new NamespaceResolver();
        Pointer pointer = new NodePointer("", new HashMap());
        resolver.setNamespaceContextPointer(pointer);
        Assert.assertSame(pointer, resolver.getNamespaceContextPointer());
        Assert.assertNull(resolver.getNamespaceMap());
        Assert.assertNull(resolver.getReverseMap());
        Assert.assertFalse(resolver.isSealed());

        NamespaceResolver parentResolver = new NamespaceResolver();
        parentResolver.setNamespaceContextPointer(new NodePointer("", new HashMap()));
        resolver = new NamespaceResolver(parentResolver);
        Assert.assertSame(parentResolver.getNamespaceContextPointer(), resolver.getNamespaceContextPointer());
        Assert.assertNull(resolver.getNamespaceMap());
        Assert.assertNull(resolver.getReverseMap());
        Assert.assertFalse(resolver.isSealed());
    }

    /**
     * Test setNamespaceContextPointer method.
     */
    @Test
    public void testSetNamespaceContextPointer() {
        NamespaceResolver resolver = new NamespaceResolver();
        Pointer pointer = new NodePointer("", new HashMap());
        resolver.setNamespaceContextPointer(pointer);
        Assert.assertSame(pointer, resolver.getNamespaceContextPointer());
        Assert.assertNull(resolver.getNamespaceMap());
        Assert.assertNull(resolver.getReverseMap());
        Assert.assertFalse(resolver.isSealed());

        NamespaceResolver parentResolver = new NamespaceResolver();
        parentResolver.setNamespaceContextPointer(new NodePointer("", new HashMap()));
        resolver = new NamespaceResolver(parentResolver);
        Assert.assertSame(parentResolver.getNamespaceContextPointer(), resolver.getNamespaceContextPointer());
        Assert.assertNull(resolver.getNamespaceMap());
        Assert.assertNull(resolver.getReverseMap());
        Assert.assertFalse(resolver.isSealed());
    }

    /**
     * Test isSealed method.
     */
    @Test
    public void testIsSealed() {
        NamespaceResolver resolver = new NamespaceResolver();
        Assert.assertFalse(resolver.isSealed());

        resolver.seal();
        Assert.assertTrue(resolver.isSealed());

        NamespaceResolver parentResolver = new NamespaceResolver();
        parentResolver.seal();
        resolver = new NamespaceResolver(parentResolver);
        Assert.assertTrue(resolver.isSealed());
    }

    /**
     * Test clone method.
     */
    @Test
    public void testClone() {
        NamespaceResolver resolver = new NamespaceResolver();
        resolver.registerNamespace("xsd", "http://www.w3.org/2001/XMLSchema");
        Assert.assertNotNull(resolver.getNamespaceMap());
        Assert.assertNull(resolver.getReverseMap());
        Assert.assertNull(resolver.getPointer());
        Assert.assertFalse(resolver.isSealed());

        NamespaceResolver clonedResolver = (NamespaceResolver) resolver.clone();
        Assert.assertNotSame(resolver, clonedResolver);
        Assert.assertEquals(resolver.getNamespaceMap(), clonedResolver.getNamespaceMap());
        Assert.assertNull(clonedResolver.getReverseMap());
        Assert.assertNull(clonedResolver.getPointer());
        Assert.assertFalse(clonedResolver.isSealed());
    }
}