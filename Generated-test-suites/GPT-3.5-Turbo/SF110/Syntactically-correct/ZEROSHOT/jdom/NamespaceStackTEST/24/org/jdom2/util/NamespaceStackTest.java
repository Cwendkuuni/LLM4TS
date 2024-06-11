import org.jdom2.Namespace;
import org.junit.Before;
import org.junit.Test;
import java.util.Arrays;
import static org.junit.Assert.*;

public class NamespaceStackTest {

    private NamespaceStack namespaceStack;

    @Before
    public void setUp() {
        Namespace[] seed = new Namespace[]{Namespace.getNamespace("test")};
        namespaceStack = new NamespaceStack(seed);
    }

    @Test
    public void testPush() {
        Element element = new Element("testElement");
        namespaceStack.push(element);
        Namespace[] expectedScope = new Namespace[]{Namespace.NO_NAMESPACE, Namespace.XML_NAMESPACE, Namespace.getNamespace("test")};
        assertArrayEquals(expectedScope, namespaceStack.getScope());
    }

    @Test
    public void testPushAttribute() {
        Attribute attribute = new Attribute("testAttribute", "value", Namespace.getNamespace("test"));
        namespaceStack.push(attribute);
        Namespace[] expectedScope = new Namespace[]{Namespace.NO_NAMESPACE, Namespace.XML_NAMESPACE, Namespace.getNamespace("test")};
        assertArrayEquals(expectedScope, namespaceStack.getScope());
    }

    @Test
    public void testPop() {
        namespaceStack.pop();
        Namespace[] expectedScope = new Namespace[]{Namespace.NO_NAMESPACE, Namespace.XML_NAMESPACE};
        assertArrayEquals(expectedScope, namespaceStack.getScope());
    }

    @Test
    public void testIsInScope() {
        Namespace ns = namespaceStack.getScope()[0];
        assertTrue(namespaceStack.isInScope(ns));
    }

    @Test
    public void testAddedForward() {
        Iterable<Namespace> added = namespaceStack.addedForward();
        Namespace[] expectedAdded = new Namespace[]{};
        for (Namespace ns : added) {
            fail("No namespaces should be added");
        }
        assertArrayEquals(expectedAdded, ((NamespaceStack.EmptyIterable) added).namespaces);
    }

    @Test
    public void testAddedReverse() {
        Iterable<Namespace> added = namespaceStack.addedReverse();
        Namespace[] expectedAdded = new Namespace[]{};
        for (Namespace ns : added) {
            fail("No namespaces should be added");
        }
        assertArrayEquals(expectedAdded, ((NamespaceStack.EmptyIterable) added).namespaces);
    }

    @Test
    public void testIterator() {
        Iterable<Namespace> iterable = namespaceStack;
        Namespace[] expectedScope = new Namespace[]{Namespace.NO_NAMESPACE, Namespace.XML_NAMESPACE, Namespace.getNamespace("test")};
        int i = 0;
        for (Namespace ns : iterable) {
            assertEquals(expectedScope[i++], ns);
        }
    }
}