import org.apache.commons.jxpath.NamespaceResolver;
import org.apache.commons.jxpath.ri.model.NodePointer;
import org.junit.Assert;
import org.junit.Test;

import java.util.HashMap;
import java.util.Map;

public class NamespaceResolverTest {

    @Test
    public void testConstructor() {
        NamespaceResolver namespaceResolver = new NamespaceResolver();
        Assert.assertNull(namespaceResolver.getParent());
        Assert.assertNotNull(namespaceResolver.getNamespaceMap());
        Assert.assertNull(namespaceResolver.getReverseMap());
        Assert.assertNull(namespaceResolver.getPointer());
        Assert.assertFalse(namespaceResolver.isSealed());
    }

    @Test
    public void testConstructorWithParent() {
        NamespaceResolver parentNamespaceResolver = new NamespaceResolver();
        NamespaceResolver namespaceResolver = new NamespaceResolver(parentNamespaceResolver);
        Assert.assertSame(parentNamespaceResolver, namespaceResolver.getParent());
        Assert.assertNotNull(namespaceResolver.getNamespaceMap());
        Assert.assertNull(namespaceResolver.getReverseMap());
        Assert.assertNull(namespaceResolver.getPointer());
        Assert.assertFalse(namespaceResolver.isSealed());
    }

    @Test
    public void testRegisterNamespace() {
        NamespaceResolver namespaceResolver = new NamespaceResolver();
        namespaceResolver.registerNamespace("xsi", "http://www.w3.org/2001/XMLSchema-instance");
        Map namespaceMap = namespaceResolver.getNamespaceMap();
        Assert.assertEquals("http://www.w3.org/2001/XMLSchema-instance", namespaceMap.get("xsi"));
        Assert.assertNull(namespaceMap.get("xsd"));

        namespaceResolver.registerNamespace("xsd", "http://www.w3.org/2001/XMLSchema");
        Assert.assertEquals("http://www.w3.org/2001/XMLSchema-instance", namespaceMap.get("xsi"));
        Assert.assertEquals("http://www.w3.org/2001/XMLSchema", namespaceMap.get("xsd"));
    }

    @Test(expected = IllegalStateException.class)
    public void testRegisterNamespaceWhenSealed() {
        NamespaceResolver namespaceResolver = new NamespaceResolver();
        namespaceResolver.registerNamespace("xsi", "http://www.w3.org/2001/XMLSchema-instance");
        namespaceResolver.seal();
        namespaceResolver.registerNamespace("xsd", "http://www.w3.org/2001/XMLSchema");
    }

    @Test
    public void testGetNamespaceURI() {
        NamespaceResolver namespaceResolver = new NamespaceResolver();
        namespaceResolver.registerNamespace("xsi", "http://www.w3.org/2001/XMLSchema-instance");
        Assert.assertEquals("http://www.w3.org/2001/XMLSchema-instance", namespaceResolver.getNamespaceURI("xsi"));
        Assert.assertNull(namespaceResolver.getNamespaceURI("xsd"));

        NodePointer nodePointer = new NodePointer("", new HashMap());
        namespaceResolver.setNamespaceContextPointer(nodePointer);
        Assert.assertNull(namespaceResolver.getNamespaceURI("xsi"));
        Assert.assertNull(namespaceResolver.getNamespaceURI("xsd"));

        nodePointer.setNamespaceURI("xsi", "http://test.com/xsi");
        Assert.assertEquals("http://test.com/xsi", namespaceResolver.getNamespaceURI("xsi"));
        Assert.assertNull(namespaceResolver.getNamespaceURI("xsd"));

        nodePointer.setNamespaceURI("xsd", "http://test.com/xsd");
        Assert.assertNull(namespaceResolver.getNamespaceURI("xsi"));
        Assert.assertEquals("http://test.com/xsd", namespaceResolver.getNamespaceURI("xsd"));
    }

    @Test
    public void testGetPrefix() {
        NamespaceResolver namespaceResolver = new NamespaceResolver();
        namespaceResolver.registerNamespace("xsi", "http://www.w3.org/2001/XMLSchema-instance");
        Assert.assertNull(namespaceResolver.getPrefix("xsi"));
        Assert.assertNull(namespaceResolver.getPrefix("xsd"));

        namespaceResolver.registerNamespace("xsd", "http://www.w3.org/2001/XMLSchema");
        Assert.assertNull(namespaceResolver.getPrefix("xsi"));
        Assert.assertEquals("xsd", namespaceResolver.getPrefix("http://www.w3.org/2001/XMLSchema"));

        NodePointer nodePointer = new NodePointer("", new HashMap());
        namespaceResolver.setNamespaceContextPointer(nodePointer);
        nodePointer.setNamespaceURI("xsi", "http://test.com/xsi");
        Assert.assertNull(namespaceResolver.getPrefix("xsi"));
        Assert.assertNull(namespaceResolver.getPrefix("xsd"));

        nodePointer.setNamespaceURI("xsi", "http://test.com/xsi");
        nodePointer.setNamespaceURI("xsd", "http://test.com/xsd");
        Assert.assertNull(namespaceResolver.getPrefix("xsi"));
        Assert.assertEquals("xsd", namespaceResolver.getPrefix("http://test.com/xsd"));
    }

    @Test
    public void testIsSealed() {
        NamespaceResolver namespaceResolver = new NamespaceResolver();
        Assert.assertFalse(namespaceResolver.isSealed());

        namespaceResolver.seal();
        Assert.assertTrue(namespaceResolver.isSealed());
    }

    @Test(expected = IllegalStateException.class)
    public void testSealWhenParentNotSealed() {
        NamespaceResolver namespaceResolver = new NamespaceResolver();
        namespaceResolver.registerNamespace("xsi", "http://www.w3.org/2001/XMLSchema-instance");
        namespaceResolver.seal();
        NamespaceResolver parentNamespaceResolver = new NamespaceResolver(namespaceResolver);
        parentNamespaceResolver.seal();
    }

    @Test
    public void testClone() {
        NamespaceResolver namespaceResolver = new NamespaceResolver();
        namespaceResolver.registerNamespace("xsi", "http://www.w3.org/2001/XMLSchema-instance");
        Map namespaceMap = namespaceResolver.getNamespaceMap();
        Assert.assertEquals(namespaceMap, namespaceResolver.clone().getNamespaceMap());
    }

}