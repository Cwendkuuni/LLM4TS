import org.junit.*;
import static org.junit.Assert.*;
import java.util.*;

public class NamespaceStackTest {

    @Test
    public void testConstructor1() {
        NamespaceStack ns = new NamespaceStack();
        Namespace[] expected = NamespaceStack.DEFAULTSEED;
        assertArrayEquals(expected, ns.getScope());
    }
    
    @Test
    public void testConstructor2() {
        Namespace[] seed = new Namespace[]{Namespace.getNamespace("foo", "http://www.foo.com")};
        NamespaceStack ns = new NamespaceStack(seed);
        Namespace[] expected = seed;
        assertArrayEquals(expected, ns.getScope());
    }
    
    @Test
    public void testPushWithElement() {
        Element element = new Element("test", Namespace.NO_NAMESPACE);
        NamespaceStack ns = new NamespaceStack();
        ns.push(element);
        Namespace[] expectedScope = new Namespace[]{Namespace.NO_NAMESPACE, Namespace.XML_NAMESPACE};
        assertArrayEquals(expectedScope, ns.getScope());
    }
    
    @Test
    public void testPushWithAttribute() {
        Attribute attribute = new Attribute("test", "value", Namespace.NO_NAMESPACE);
        NamespaceStack ns = new NamespaceStack();
        ns.push(attribute);
        Namespace[] expectedScope = new Namespace[]{Namespace.NO_NAMESPACE, Namespace.XML_NAMESPACE};
        assertArrayEquals(expectedScope, ns.getScope());
    }
    
    @Test(expected = IllegalStateException.class)
    public void testPop() {
        NamespaceStack ns = new NamespaceStack();
        ns.pop();
    }
    
    @Test
    public void testAddedForward() {
        Element element = new Element("test", Namespace.NO_NAMESPACE);
        element.addNamespaceDeclaration(Namespace.getNamespace("foo", "http://www.foo.com"));
        NamespaceStack ns = new NamespaceStack();
        ns.push(element);
        Iterable<Namespace> added = ns.addedForward();
        List<Namespace> expected = new ArrayList<>();
        expected.add(Namespace.getNamespace("foo", "http://www.foo.com"));
        List<Namespace> actual = new ArrayList<>();
        for (Namespace ns : added) {
            actual.add(ns);
        }
        assertEquals(expected, actual);
    }
    
    @Test
    public void testAddedReverse() {
        Element element = new Element("test", Namespace.NO_NAMESPACE);
        element.addNamespaceDeclaration(Namespace.getNamespace("foo", "http://www.foo.com"));
        NamespaceStack ns = new NamespaceStack();
        ns.push(element);
        Iterable<Namespace> added = ns.addedReverse();
        List<Namespace> expected = new ArrayList<>();
        expected.add(Namespace.getNamespace("foo", "http://www.foo.com"));
        Collections.reverse(expected);
        List<Namespace> actual = new ArrayList<>();
        for (Namespace ns : added) {
            actual.add(ns);
        }
        assertEquals(expected, actual);
    }
    
    @Test
    public void testIterator() {
        Element element = new Element("test", Namespace.NO_NAMESPACE);
        element.addNamespaceDeclaration(Namespace.getNamespace("foo", "http://www.foo.com"));
        NamespaceStack ns = new NamespaceStack();
        ns.push(element);
        Iterator<Namespace> iterator = ns.iterator();
        List<Namespace> expected = new ArrayList<>();
        expected.add(Namespace.NO_NAMESPACE);
        expected.add(Namespace.getNamespace("foo", "http://www.foo.com"));
        List<Namespace> actual = new ArrayList<>();
        while (iterator.hasNext()) {
            actual.add(iterator.next());
        }
        assertEquals(expected, actual);
    }
    
    @Test
    public void testIsInScope() {
        Element element = new Element("test", Namespace.NO_NAMESPACE);
        element.addNamespaceDeclaration(Namespace.getNamespace("foo", "http://www.foo.com"));
        NamespaceStack ns = new NamespaceStack();
        ns.push(element);
        boolean isInScope = ns.isInScope(Namespace.getNamespace("foo", "http://www.foo.com"));
        assertTrue(isInScope);
    }
}