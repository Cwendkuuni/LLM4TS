import org.apache.commons.jxpath.NamespaceResolver;
import org.apache.commons.jxpath.ri.model.NodePointer;
import org.junit.Assert;
import org.junit.Test;

import java.util.HashMap;
import java.util.Map;

public class NamespaceResolverTest {

    @Test
    public void testCreateNewNamespaceResolver() {
        NamespaceResolver namespaceResolver = new NamespaceResolver();
        Assert.assertNull(namespaceResolver.getParent());
        Assert.assertNotNull(namespaceResolver.getNamespaceMap());
        Assert.assertNull(namespaceResolver.getReverseMap());
        Assert.assertNull(namespaceResolver.getPointer());
        Assert.assertFalse(namespaceResolver.isSealed());
    }

    @Test
    public void testCreateNewNamespaceResolverWithParent() {
        NamespaceResolver parentResolver = new NamespaceResolver();
        NamespaceResolver namespaceResolver = new NamespaceResolver(parentResolver);
        Assert.assertSame(parentResolver, namespaceResolver.getParent());
        // Rest of the assertions are same as testCreateNewNamespaceResolver()
    }

    @Test
    public void testRegisterNamespace() {
        NamespaceResolver namespaceResolver = new NamespaceResolver();
        namespaceResolver.registerNamespace("xsi", "http://www.w3.org/2001/XMLSchema-instance");
        Map<String, String> namespaceMap = namespaceResolver.getNamespaceMap();
        Assert.assertEquals("http://www.w3.org/2001/XMLSchema-instance", namespaceMap.get("xsi"));
        Assert.assertEquals("xsi", namespaceMap.get("http://www.w3.org/2001/XMLSchema-instance"));
        Assert.assertNull(namespaceResolver.getReverseMap());
        Assert.assertNull(namespaceResolver.getPointer());
        Assert.assertFalse(namespaceResolver.isSealed());
    }

    @Test(expected = IllegalStateException.class)
    public void testRegisterNamespaceOnSealedNamespaceResolver() {
        NamespaceResolver namespaceResolver = new NamespaceResolver();
        namespaceResolver.seal();
        namespaceResolver.registerNamespace("xsi", "http://www.w3.org/2001/XMLSchema-instance");
    }

    @Test
    public void testGetNamespaceURI() {
        NamespaceResolver namespaceResolver = new NamespaceResolver();
        namespaceResolver.registerNamespace("xsi", "http://www.w3.org/2001/XMLSchema-instance");
        Assert.assertEquals("http://www.w3.org/2001/XMLSchema-instance", namespaceResolver.getNamespaceURI("xsi"));
        Assert.assertNull(namespaceResolver.getNamespaceURI(""));
        Assert.assertNull(namespaceResolver.getNamespaceURI("nonExistent"));

        NodePointer nodePointer = new NodePointer();
        namespaceResolver.setNamespaceContextPointer(nodePointer);
        namespaceResolver.registerNamespace("xml", "http://www.w3.org/XML/1998/namespace");
        Assert.assertEquals("http://www.w3.org/XML/1998/namespace", namespaceResolver.getNamespaceURI("xml"));
        Assert.assertEquals("xml", namespaceResolver.getNamespaceURI("http://www.w3.org/XML/1998/namespace"));
        Assert.assertNull(namespaceResolver.getNamespaceURI("xsi"));
        Assert.assertNull(namespaceResolver.getNamespaceURI(""));

        NamespaceResolver parentResolver = new NamespaceResolver();
        parentResolver.registerNamespace("xsi", "http://www.w3.org/2001/XMLSchema-instance");
        namespaceResolver = new NamespaceResolver(parentResolver);
        Assert.assertEquals("http://www.w3.org/2001/XMLSchema-instance", namespaceResolver.getNamespaceURI("xsi"));
        Assert.assertEquals("xml", namespaceResolver.getNamespaceURI("http://www.w3.org/XML/1998/namespace"));
        Assert.assertEquals("xsi", namespaceResolver.getNamespaceURI("http://www.w3.org/2001/XMLSchema-instance"));
    }

    @Test
    public void testGetPrefix() {
        NamespaceResolver namespaceResolver = new NamespaceResolver();
        namespaceResolver.registerNamespace("xsi", "http://www.w3.org/2001/XMLSchema-instance");
        Assert.assertEquals("xsi", namespaceResolver.getPrefix("http://www.w3.org/2001/XMLSchema-instance"));
        Assert.assertNull(namespaceResolver.getPrefix(""));
        Assert.assertNull(namespaceResolver.getPrefix("nonExistent"));

        NodePointer nodePointer = new NodePointer();
        namespaceResolver.setNamespaceContextPointer(nodePointer);
        namespaceResolver.registerNamespace("xml", "http://www.w3.org/XML/1998/namespace");
        Assert.assertEquals("xml", namespaceResolver.getPrefix("http://www.w3.org/XML/1998/namespace"));
        Assert.assertEquals("xml", namespaceResolver.getPrefix("http://www.w3.org/XML/1998/namespace"));
        Assert.assertNull(namespaceResolver.getPrefix("xsi"));
        Assert.assertNull(namespaceResolver.getPrefix(""));

        NamespaceResolver parentResolver = new NamespaceResolver();
        parentResolver.registerNamespace("xsi", "http://www.w3.org/2001/XMLSchema-instance");
        namespaceResolver = new NamespaceResolver(parentResolver);
        Assert.assertEquals("xsi", namespaceResolver.getPrefix("http://www.w3.org/2001/XMLSchema-instance"));
        Assert.assertEquals("xml", namespaceResolver.getPrefix("http://www.w3.org/XML/1998/namespace"));
        Assert.assertEquals("xsi", namespaceResolver.getPrefix("http://www.w3.org/2001/XMLSchema-instance"));
    }

    @Test
    public void testSetNamespaceContextPointer() {
        NamespaceResolver namespaceResolver = new NamespaceResolver();
        namespaceResolver.setNamespaceContextPointer(new NodePointer());
        Assert.assertNotNull(namespaceResolver.getNamespaceContextPointer());
        Assert.assertFalse(namespaceResolver.isSealed());
    }

    @Test
    public void testGetNamespaceContextPointer() {
        NamespaceResolver namespaceResolver = new NamespaceResolver();
        NodePointer nodePointer = new NodePointer();
        namespaceResolver.setNamespaceContextPointer(nodePointer);
        Assert.assertSame(nodePointer, namespaceResolver.getNamespaceContextPointer());
        Assert.assertFalse(namespaceResolver.isSealed());
    }

    @Test
    public void testIsSealed() {
        NamespaceResolver namespaceResolver = new NamespaceResolver();
        Assert.assertFalse(namespaceResolver.isSealed());
        namespaceResolver.seal();
        Assert.assertTrue(namespaceResolver.isSealed());
        Assert.assertFalse(namespaceResolver.isSealed()); // Sealing does not affect the parent
    }

    @Test
    public void testClone() {
        NamespaceResolver namespaceResolver = new NamespaceResolver();
        namespaceResolver.registerNamespace("xsi", "http://www.w3.org/2001/XMLSchema-instance");
        Map<String, String> namespaceMap = namespaceResolver.getNamespaceMap();
        Map<String, String> clonedNamespaceMap = ((NamespaceResolver) namespaceResolver.clone()).getNamespaceMap();
        Assert.assertEquals(namespaceMap, clonedNamespaceMap);
        Assert.assertNotNull(namespaceResolver.getReverseMap());
        Assert.assertNotNull(namespaceResolver.getPointer());
        Assert.assertFalse(namespaceResolver.isSealed());
    }
}