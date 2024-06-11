import org.apache.commons.jxpath.NamespaceContext;
import org.apache.commons.jxpath.ri.model.NodePointer;
import org.junit.Assert;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mockito;
import org.powermock.api.mockito.PowerMockito;
import org.powermock.core.classloader.annotations.PrepareForTest;
import org.powermock.modules.junit4.PowerMockRunner;

import java.util.HashMap;
import java.util.Map;

@RunWith(PowerMockRunner.class)
@PrepareForTest({NamespacesResolver.class})
public class NamespaceResolverTest {

    @Test
    public void testConstructor() {
        NamespaceResolver namespaceResolver = new NamespaceResolver();
        Assert.assertNull(namespaceResolver.getParent());
        Assert.assertNull(namespaceResolver.getNamespaceMap());
        Assert.assertNull(namespaceResolver.getReverseMap());
        Assert.assertNull(namespaceResolver.getPointer());
        Assert.assertFalse(namespaceResolver.isSealed());
    }

    @Test
    public void testConstructorWithParent() {
        NamespaceResolver parentResolver = Mockito.spy(new NamespaceResolver());
        NamespaceResolver namespaceResolver = new NamespaceResolver(parentResolver);
        Assert.assertEquals(parentResolver, namespaceResolver.getParent());
        Assert.assertNull(namespaceResolver.getNamespaceMap());
        Assert.assertNull(namespaceResolver.getReverseMap());
        Assert.assertNull(namespaceResolver.getPointer());
        Assert.assertFalse(namespaceResolver.isSealed());
    }

    @Test
    public void testRegisterNamespace() {
        NamespaceResolver namespaceResolver = new NamespaceResolver();
        String prefix = "testPrefix";
        String namespaceURI = "http://test.com";
        namespaceResolver.registerNamespace(prefix, namespaceURI);
        Map<String, String> namespaceMap = namespaceResolver.getNamespaceMap();
        Assert.assertEquals(1, namespaceMap.size());
        Assert.assertEquals(namespaceURI, namespaceMap.get(prefix));
        Assert.assertNull(namespaceResolver.getReverseMap());
        Assert.assertNull(namespaceResolver.getPointer());
        Assert.assertFalse(namespaceResolver.isSealed());
    }

    @Test(expected = IllegalStateException.class)
    public void testRegisterNamespaceWhenSealed() {
        NamespaceResolver namespaceResolver = new NamespaceResolver();
        namespaceResolver.seal();
        String prefix = "testPrefix";
        String namespaceURI = "http://test.com";
        namespaceResolver.registerNamespace(prefix, namespaceURI);
    }

    @Test
    public void testGetNamespaceURI() {
        NamespaceResolver namespaceResolver = new NamespaceResolver();
        String prefix = "testPrefix";
        String namespaceURI = "http://test.com";
        namespaceResolver.registerNamespace(prefix, namespaceURI);
        String result = namespaceResolver.getNamespaceURI(prefix);
        Assert.assertEquals(namespaceURI, result);
        Assert.assertNull(namespaceResolver.getNamespaceURI("nonExistentPrefix"));
        NamespaceResolver parentResolver = Mockito.spy(new NamespaceResolver());
        NamespaceResolver namespaceResolverWithParent = new NamespaceResolver(parentResolver);
        namespaceResolverWithParent.registerNamespace(prefix, namespaceURI);
        Assert.assertEquals(namespaceURI, namespaceResolverWithParent.getNamespaceURI(prefix));
        Assert.assertEquals(namespaceURI, parentResolver.getNamespaceURI(prefix));
    }

    @Test
    public void testGetPrefix() {
        NamespaceResolver namespaceResolver = new NamespaceResolver();
        String namespaceURI = "http://test.com";
        String prefix = "testPrefix";
        namespaceResolver.registerNamespace(prefix, namespaceURI);
        String result = namespaceResolver.getPrefix(namespaceURI);
        Assert.assertEquals(prefix, result);
        Assert.assertNull(namespaceResolver.getPrefix("nonExistentURI"));
        NamespaceResolver parentResolver = Mockito.spy(new NamespaceResolver());
        NamespaceResolver namespaceResolverWithParent = new NamespaceResolver(parentResolver);
        namespaceResolverWithParent.registerNamespace(prefix, namespaceURI);
        Assert.assertEquals(prefix, namespaceResolverWithParent.getPrefix(namespaceURI));
        Assert.assertEquals(prefix, parentResolver.getPrefix(namespaceURI));
    }

    @Test
    public void testIsSealed() {
        NamespaceResolver namespaceResolver = new NamespaceResolver();
        Assert.assertFalse(namespaceResolver.isSealed());
        namespaceResolver.seal();
        Assert.assertTrue(namespaceResolver.isSealed());
    }

    @Test
    public void testClone() {
        NamespaceResolver namespaceResolver = new NamespaceResolver();
        NamespaceResolver clonedNamespaceResolver = (NamespaceResolver) namespaceResolver.clone();
        Assert.assertNotSame(namespaceResolver, clonedNamespaceResolver);
        Assert.assertNull(clonedNamespaceResolver.getParent());
        Assert.assertNull(clonedNamespaceResolver.getNamespaceMap());
        Assert.assertNull(clonedNamespaceResolver.getReverseMap());
        Assert.assertNull(clonedNamespaceResolver.getPointer());
        Assert.assertFalse(clonedNamespaceResolver.isSealed());
    }

    @Test
    public void testSetNamespaceContextPointer() {
        NamespaceResolver namespaceResolver = new NamespaceResolver();
        NodePointer nodePointer = PowerMockito.mock(NodePointer.class);
        namespaceResolver.setNamespaceContextPointer(nodePointer);
        Assert.assertEquals(nodePointer, namespaceResolver.getNamespaceContextPointer());
        Assert.assertNull(namespaceResolver.getPointer());
    }

    @Test
    public void testGetNamespaceContextPointer() {
        NamespaceResolver namespaceResolver = new NamespaceResolver();
        Pointer pointer = Mockito.mock(Pointer.class);
        namespaceResolver.setNamespaceContextPointer(pointer);
        Assert.assertEquals(pointer, namespaceResolver.getNamespaceContextPointer());
        Assert.assertNull(namespaceResolver.getPointer());
        Assert.assertNull(namespaceResolver.getPointer());
    }
}