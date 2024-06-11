package org.apache.commons.jxpath.ri;
import org.junit.Assert;
import org.junit.Test;

public class NamespaceResolverTest {

    @Test
    public void testRegisterNamespace() {
        NamespaceResolver resolver = new NamespaceResolver();
        resolver.registerNamespace("prefix1", "uri1");
        resolver.registerNamespace("prefix2", "uri2");
        String prefix1URI = resolver.getNamespaceURI("prefix1");
        String prefix2URI = resolver.getNamespaceURI("prefix2");
        String prefix3URI = resolver.getNamespaceURI("prefix3");
        String uri1Prefix = resolver.getPrefix("uri1");
        String uri2Prefix = resolver.getPrefix("uri2");
        
        Assert.assertEquals("uri1", prefix1URI);
        Assert.assertEquals("uri2", prefix2URI);
        Assert.assertNull(prefix3URI);
        Assert.assertEquals("prefix1", uri1Prefix);
        Assert.assertEquals("prefix2", uri2Prefix);
    }

    @Test
    public void testGetNamespaceURI() {
        NamespaceResolver resolver = new NamespaceResolver();
        resolver.registerNamespace("prefix1", "uri1");
        resolver.registerNamespace("prefix2", "uri2");
        resolver.setNamespaceContextPointer(null);
        
        String prefix1URI = resolver.getNamespaceURI("prefix1");
        String prefix2URI = resolver.getNamespaceURI("prefix2");
        String prefix3URI = resolver.getNamespaceURI("prefix3");
        
        Assert.assertEquals("uri1", prefix1URI);
        Assert.assertEquals("uri2", prefix2URI);
        Assert.assertNull(prefix3URI);
    }

    @Test
    public void testGetPrefix() {
        NamespaceResolver resolver = new NamespaceResolver();
        resolver.registerNamespace("prefix1", "uri1");
        resolver.registerNamespace("prefix2", "uri2");
        resolver.setNamespaceContextPointer(null);
        
        String uri1Prefix = resolver.getPrefix("uri1");
        String uri2Prefix = resolver.getPrefix("uri2");
        String uri3Prefix = resolver.getPrefix("uri3");
        
        Assert.assertEquals("prefix1", uri1Prefix);
        Assert.assertEquals("prefix2", uri2Prefix);
        Assert.assertNull(uri3Prefix);
    }

    @Test
    public void testSealed() {
        NamespaceResolver resolver = new NamespaceResolver();
        resolver.seal();
        Assert.assertTrue(resolver.isSealed());
    }

    @Test
    public void testClone() {
        NamespaceResolver resolver = new NamespaceResolver();
        NamespaceResolver clonedResolver = (NamespaceResolver) resolver.clone();
        Assert.assertNotSame(resolver, clonedResolver);
        Assert.assertFalse(clonedResolver.isSealed());
    }
}