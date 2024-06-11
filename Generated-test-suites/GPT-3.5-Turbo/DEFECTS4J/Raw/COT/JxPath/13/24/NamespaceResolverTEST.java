import org.junit.Before;
import org.junit.Test;
import org.apache.commons.jxpath.Pointer;
import org.apache.commons.jxpath.ri.model.NodeIterator;
import org.apache.commons.jxpath.ri.model.NodePointer;

public class NamespaceResolverTest {

    private NamespaceResolver namespaceResolver;

    @Before
    public void setup() {
        namespaceResolver = new NamespaceResolver();
    }

    @Test
    public void registerNamespaceTest() {
        namespaceResolver.registerNamespace("prefix", "namespaceURI");
        String result = namespaceResolver.getNamespaceURI("prefix");
        assertEquals("namespaceURI", result);
    }

    @Test(expected = IllegalStateException.class)
    public void registerNamespaceThrowsExceptionWhenSealedTest() {
        namespaceResolver.seal();
        namespaceResolver.registerNamespace("prefix", "namespaceURI");
    }

    @Test
    public void setNamespaceContextPointerTest() {
        NodePointer pointer = new NodePointer();
        namespaceResolver.setNamespaceContextPointer(pointer);
        Pointer result = namespaceResolver.getNamespaceContextPointer();
        assertEquals(pointer, result);
    }

    @Test
    public void getNamespaceURITest() {
        String result = namespaceResolver.getNamespaceURI("prefix");
        assertNull(result);
    }

    @Test
    public void getNamespaceURIFromParentTest() {
        namespaceResolver.registerNamespace("prefix", "namespaceURI");
        NamespaceResolver parent = new NamespaceResolver();
        parent.registerNamespace("prefix", "namespaceURI");
        namespaceResolver = new NamespaceResolver(parent);
        String result = namespaceResolver.getNamespaceURI("prefix");
        assertEquals("namespaceURI", result);
    }

    @Test
    public void getPrefixTest() {
        String result = namespaceResolver.getPrefix("namespaceURI");
        assertNull(result);
    }

    @Test
    public void getPrefixFromParentTest() {
        namespaceResolver.registerNamespace("prefix", "namespaceURI");
        NamespaceResolver parent = new NamespaceResolver();
        parent.registerNamespace("prefix", "namespaceURI");
        namespaceResolver = new NamespaceResolver(parent);
        String result = namespaceResolver.getPrefix("namespaceURI");
        assertEquals("prefix", result);
    }

    @Test
    public void isSealedTest() {
        boolean result = namespaceResolver.isSealed();
        assertFalse(result);
    }

    @Test
    public void sealTest() {
        namespaceResolver.seal();
        boolean result = namespaceResolver.isSealed();
        assertTrue(result);
    }

}