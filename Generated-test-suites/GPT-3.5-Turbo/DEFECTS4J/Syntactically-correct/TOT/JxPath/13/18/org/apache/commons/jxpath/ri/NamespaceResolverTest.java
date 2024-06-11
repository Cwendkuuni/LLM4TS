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
        resolver.registerNamespace("ns1", "http://www.example.com/ns1");
        String result = resolver.getNamespaceURI("ns1");
        Assert.assertEquals("http://www.example.com/ns1", result);
    }

    @Test
    public void testSetNamespaceContextPointer() {
        NodePointer pointer = new NodePointer();
        resolver.setNamespaceContextPointer(pointer);
        Pointer result = resolver.getNamespaceContextPointer();
        Assert.assertEquals(pointer, result);
    }

    @Test
    public void testGetNamespaceURI() {
        resolver.registerNamespace("ns1", "http://www.example.com/ns1");
        String result = resolver.getNamespaceURI("ns1");
        Assert.assertEquals("http://www.example.com/ns1", result);
    }

    @Test
    public void testGetNamespaceURI_External() {
        resolver.setNamespaceContextPointer(new NodePointer());
        String result = resolver.getNamespaceURI("ns1");
        Assert.assertNull(result);
    }

    @Test
    public void testGetPrefix() {
        resolver.registerNamespace("ns1", "http://www.example.com/ns1");
        String result = resolver.getPrefix("http://www.example.com/ns1");
        Assert.assertEquals("ns1", result);
    }

    @Test
    public void testGetPrefix_NoMatch() {
        resolver.registerNamespace("ns1", "http://www.example.com/ns1");
        String result = resolver.getPrefix("http://www.example.com/ns2");
        Assert.assertNull(result);
    }

    @Test
    public void testGetPrefix_External() {
        resolver.setNamespaceContextPointer(new NodePointer());
        String result = resolver.getPrefix("http://www.example.com/ns1");
        Assert.assertNull(result);
    }

    @Test
    public void testClone() {
        NamespaceResolver clone = (NamespaceResolver) resolver.clone();
        Assert.assertNotSame(resolver, clone);
        Assert.assertEquals(resolver.isSealed(), clone.isSealed());
    }

}