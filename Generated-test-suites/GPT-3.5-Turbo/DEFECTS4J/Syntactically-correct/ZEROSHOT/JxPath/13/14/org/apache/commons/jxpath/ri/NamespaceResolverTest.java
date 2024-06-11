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
        resolver.registerNamespace("prefix1", "http://www.example.com/ns1");
        resolver.registerNamespace("prefix2", "http://www.example.com/ns2");

        String ns1 = resolver.getNamespaceURI("prefix1");
        String ns2 = resolver.getNamespaceURI("prefix2");

        Assert.assertEquals("http://www.example.com/ns1", ns1);
        Assert.assertEquals("http://www.example.com/ns2", ns2);
    }

    @Test
    public void testGetPrefix() {
        resolver.registerNamespace("prefix1", "http://www.example.com/ns1");
        resolver.registerNamespace("prefix2", "http://www.example.com/ns2");

        String prefix1 = resolver.getPrefix("http://www.example.com/ns1");
        String prefix2 = resolver.getPrefix("http://www.example.com/ns2");

        Assert.assertEquals("prefix1", prefix1);
        Assert.assertEquals("prefix2", prefix2);
    }

    @Test
    public void testGetNamespaceURI() {
        resolver.registerNamespace("prefix1", "http://www.example.com/ns1");
        resolver.registerNamespace("prefix2", "http://www.example.com/ns2");

        String ns1 = resolver.getNamespaceURI("prefix1");
        String ns2 = resolver.getNamespaceURI("prefix2");
        String ns3 = resolver.getNamespaceURI("prefix3");

        Assert.assertEquals("http://www.example.com/ns1", ns1);
        Assert.assertEquals("http://www.example.com/ns2", ns2);
        Assert.assertNull(ns3);
    }

    @Test
    public void testGetNamespaceURIExternal() {
        resolver.registerNamespace("prefix1", "http://www.example.com/ns1");
        resolver.registerNamespace("prefix2", "http://www.example.com/ns2");

        resolver.setNamespaceContextPointer(/*Set your test NodePointer here*/);

        String ns1 = resolver.getNamespaceURI("prefix1");
        String ns2 = resolver.getNamespaceURI("prefix2");

        Assert.assertEquals("http://www.example.com/ns1", ns1);
        Assert.assertEquals("http://www.example.com/ns2", ns2);
    }

    @Test
    public void testGetPrefixExternal() {
        resolver.registerNamespace("prefix1", "http://www.example.com/ns1");
        resolver.registerNamespace("prefix2", "http://www.example.com/ns2");

        resolver.setNamespaceContextPointer(/*Set your test NodePointer here*/);

        String prefix1 = resolver.getPrefix("http://www.example.com/ns1");
        String prefix2 = resolver.getPrefix("http://www.example.com/ns2");

        Assert.assertEquals("prefix1", prefix1);
        Assert.assertEquals("prefix2", prefix2);
    }
}