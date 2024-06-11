import static org.junit.Assert.*;
import org.junit.Before;
import org.junit.Test;
import org.jdom2.Namespace;

public class NamespaceStackTest {
    private NamespaceStack namespaceStack;

    @Before
    public void setUp() {
        namespaceStack = new NamespaceStack();
    }

    @Test
    public void testPushElement() {
        Element element = new Element("test", Namespace.getNamespace("ns1", "http://www.example.com/ns1"));
        namespaceStack.push(element);

        // Test addedForward
        Iterable<Namespace> addedForward = namespaceStack.addedForward();

        boolean found = false;
        for (Namespace ns : addedForward) {
            if (ns.getURI().equals("http://www.example.com/ns1") && ns.getPrefix().equals("ns1")) {
                found = true;
                break;
            }
        }

        assertTrue(found);

        // Test addedReverse
        Iterable<Namespace> addedReverse = namespaceStack.addedReverse();

        found = false;
        for (Namespace ns : addedReverse) {
            if (ns.getURI().equals("http://www.example.com/ns1") && ns.getPrefix().equals("ns1")) {
                found = true;
                break;
            }
        }

        assertTrue(found);

        // Test iterator
        Iterable<Namespace> iterator = namespaceStack;

        found = false;
        for (Namespace ns : iterator) {
            if (ns.getURI().equals("http://www.example.com/ns1") && ns.getPrefix().equals("ns1")) {
                found = true;
                break;
            }
        }

        assertTrue(found);
    }

    @Test
    public void testPushAttribute() {
        Attribute attribute = new Attribute("test", "value", Namespace.getNamespace("ns2", "http://www.example.com/ns2"));
        namespaceStack.push(attribute);

        // Test addedForward
        Iterable<Namespace> addedForward = namespaceStack.addedForward();

        boolean found = false;
        for (Namespace ns : addedForward) {
            if (ns.getURI().equals("http://www.example.com/ns2") && ns.getPrefix().equals("ns2")) {
                found = true;
                break;
            }
        }

        assertTrue(found);

        // Test addedReverse
        Iterable<Namespace> addedReverse = namespaceStack.addedReverse();

        found = false;
        for (Namespace ns : addedReverse) {
            if (ns.getURI().equals("http://www.example.com/ns2") && ns.getPrefix().equals("ns2")) {
                found = true;
                break;
            }
        }

        assertTrue(found);

        // Test iterator
        Iterable<Namespace> iterator = namespaceStack;

        found = false;
        for (Namespace ns : iterator) {
            if (ns.getURI().equals("http://www.example.com/ns2") && ns.getPrefix().equals("ns2")) {
                found = true;
                break;
            }
        }

        assertTrue(found);
    }

    @Test
    public void testPop() {
        Element element = new Element("test", Namespace.getNamespace("ns3", "http://www.example.com/ns3"));
        namespaceStack.push(element);

        namespaceStack.pop();

        Iterable<Namespace> iterator = namespaceStack;

        boolean found = false;
        for (Namespace ns : iterator) {
            if (ns.getURI().equals("http://www.example.com/ns3") && ns.getPrefix().equals("ns3")) {
                found = true;
                break;
            }
        }

        assertFalse(found);
    }

    @Test
    public void testGetScope() {
        Namespace[] scope = namespaceStack.getScope();

        assertEquals(2, scope.length);
        assertEquals("", scope[0].getPrefix());
        assertEquals("", scope[0].getURI());
    }

    @Test
    public void testIsInScope() {
        Element element = new Element("test", Namespace.getNamespace("ns4", "http://www.example.com/ns4"));
        namespaceStack.push(element);

        assertTrue(namespaceStack.isInScope(Namespace.getNamespace("ns4", "http://www.example.com/ns4")));
        assertFalse(namespaceStack.isInScope(Namespace.getNamespace("ns5", "http://www.example.com/ns5")));
    }
}