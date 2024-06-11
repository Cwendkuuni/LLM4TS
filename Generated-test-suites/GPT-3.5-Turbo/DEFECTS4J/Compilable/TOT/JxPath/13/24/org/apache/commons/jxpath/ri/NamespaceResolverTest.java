package org.apache.commons.jxpath.ri;
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
        resolver.registerNamespace("ns1", "http://namespace1.com");
        resolver.registerNamespace("ns2", "http://namespace2.com");

        Assert.assertEquals("http://namespace1.com", resolver.getNamespaceURI("ns1"));
        Assert.assertEquals("http://namespace2.com", resolver.getNamespaceURI("ns2"));
        Assert.assertNull(resolver.getNamespaceURI("ns3"));
    }

    @Test(expected = IllegalStateException.class)
    public void testRegisterNamespaceSealed() {
        resolver.seal();
        resolver.registerNamespace("ns1", "http://namespace1.com");
    }

    @Test
    public void testGetNamespaceURI() {
        resolver.registerNamespace("ns1", "http://namespace1.com");
        resolver.registerNamespace("ns2", "http://namespace2.com");

        Assert.assertEquals("http://namespace1.com", resolver.getNamespaceURI("ns1"));
        Assert.assertEquals("http://namespace2.com", resolver.getNamespaceURI("ns2"));
        Assert.assertNull(resolver.getNamespaceURI("ns3"));
    }

    @Test
    public void testGetNamespaceURIExternal() {
        resolver.setNamespaceContextPointer(null);
        resolver.registerNamespace("ns1", "http://namespace1.com");
        resolver.registerNamespace("ns2", "http://namespace2.com");

        Assert.assertEquals("http://namespace1.com", resolver.getNamespaceURI("ns1"));
        Assert.assertEquals("http://namespace2.com", resolver.getNamespaceURI("ns2"));
        Assert.assertNull(resolver.getNamespaceURI("ns3"));
    }

    @Test
    public void testGetNamespaceURIExternalWithParent() {
        NamespaceResolver parent = new NamespaceResolver();
        parent.registerNamespace("ns1", "http://namespace1.com");
        parent.registerNamespace("ns2", "http://namespace2.com");
        resolver = new NamespaceResolver(parent);

        Assert.assertEquals("http://namespace1.com", resolver.getNamespaceURI("ns1"));
        Assert.assertEquals("http://namespace2.com", resolver.getNamespaceURI("ns2"));
        Assert.assertNull(resolver.getNamespaceURI("ns3"));
    }

    @Test
    public void testGetPrefix() {
        resolver.registerNamespace("ns1", "http://namespace1.com");
        resolver.registerNamespace("ns2", "http://namespace2.com");

        Assert.assertEquals("ns1", resolver.getPrefix("http://namespace1.com"));
        Assert.assertEquals("ns2", resolver.getPrefix("http://namespace2.com"));
        Assert.assertNull(resolver.getPrefix("http://namespace3.com"));
    }

    @Test
    public void testGetPrefixWithParent() {
        NamespaceResolver parent = new NamespaceResolver();
        parent.registerNamespace("ns1", "http://namespace1.com");
        parent.registerNamespace("ns2", "http://namespace2.com");
        resolver = new NamespaceResolver(parent);

        Assert.assertEquals("ns1", resolver.getPrefix("http://namespace1.com"));
        Assert.assertEquals("ns2", resolver.getPrefix("http://namespace2.com"));
        Assert.assertNull(resolver.getPrefix("http://namespace3.com"));
    }
    
    @Test
    public void testClone() {
        resolver.registerNamespace("ns1", "http://namespace1.com");
        resolver.registerNamespace("ns2", "http://namespace2.com");

        NamespaceResolver cloneResolver = (NamespaceResolver) resolver.clone();

        Assert.assertEquals(resolver.getNamespaceURI("ns1"), cloneResolver.getNamespaceURI("ns1"));
        Assert.assertEquals(resolver.getNamespaceURI("ns2"), cloneResolver.getNamespaceURI("ns2"));
        Assert.assertNull(cloneResolver.getNamespaceURI("ns3"));

        resolver.registerNamespace("ns3", "http://namespace3.com");
        Assert.assertNull(cloneResolver.getNamespaceURI("ns3"));
    }
}