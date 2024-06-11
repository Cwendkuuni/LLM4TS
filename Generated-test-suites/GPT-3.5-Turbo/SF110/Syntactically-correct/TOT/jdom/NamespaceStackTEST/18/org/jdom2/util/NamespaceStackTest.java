import org.junit.Test;
import static org.junit.Assert.*;

public class NamespaceStackTest {

    @Test
    public void testPush() {
        NamespaceStack ns = new NamespaceStack();
        Element element = new Element("test", "http://www.example.com");
        ns.push(element);
        Namespace[] expected = new Namespace[]{ Namespace.NO_NAMESPACE, Namespace.XML_NAMESPACE, new Namespace("http://www.example.com", "test") };
        assertArrayEquals(expected, ns.getScope());
    }

    @Test
    public void testPush_WithAdditionalNamespaces() {
        NamespaceStack ns = new NamespaceStack();
        Element element = new Element("test", "http://www.example.com");
        element.addNamespaceDeclaration("prefix1", "http://www.example1.com");
        element.addNamespaceDeclaration("prefix2", "http://www.example2.com");
        ns.push(element);
        Namespace[] expected = new Namespace[]{ Namespace.NO_NAMESPACE, Namespace.XML_NAMESPACE, new Namespace("http://www.example.com", "test"), new Namespace("http://www.example1.com", "prefix1"), new Namespace("http://www.example2.com", "prefix2") };
        assertArrayEquals(expected, ns.getScope());
    }

    @Test
    public void testPush_WithAttributes() {
        NamespaceStack ns = new NamespaceStack();
        Element element = new Element("test", "http://www.example.com");
        element.setAttribute(new Attribute("attr1", "value1", Namespace.XML_NAMESPACE));
        element.setAttribute(new Attribute("attr2", "value2", new Namespace("http://www.example1.com", "prefix1")));
        ns.push(element);
        Namespace[] expected = new Namespace[]{ Namespace.NO_NAMESPACE, new Namespace("http://www.example1.com", "prefix1"), new Namespace("http://www.example.com", "test") };
        assertArrayEquals(expected, ns.getScope());
    }

    @Test
    public void testPush_PrefixAlreadyExists() {
        NamespaceStack ns = new NamespaceStack();
        Element element1 = new Element("test1", "http://www.example.com");
        ns.push(element1);
        Element element2 = new Element("test2", "http://www.example.com");
        ns.push(element2);
        Namespace[] expected = new Namespace[]{ Namespace.NO_NAMESPACE, Namespace.XML_NAMESPACE, new Namespace("http://www.example.com", "test2") };
        assertArrayEquals(expected, ns.getScope());
    }

    @Test
    public void testPush_EmptyStack() {
        NamespaceStack ns = new NamespaceStack();
        ns.pop();
        Element element = new Element("test", "http://www.example.com");
        ns.push(element);
        Namespace[] expected = new Namespace[]{ Namespace.NO_NAMESPACE, Namespace.XML_NAMESPACE, new Namespace("http://www.example.com", "test") };
        assertArrayEquals(expected, ns.getScope());
    }

    @Test(expected = IllegalStateException.class)
    public void testPop_EmptyStack() {
        NamespaceStack ns = new NamespaceStack();
        ns.pop();
    }

    @Test
    public void testAddedForward_EmptyStack() {
        NamespaceStack ns = new NamespaceStack();
        Iterable<Namespace> addedNamespaces = ns.addedForward();
        assertFalse(addedNamespaces.iterator().hasNext());
    }

    @Test
    public void testAddedForward() {
        NamespaceStack ns = new NamespaceStack();
        Element element1 = new Element("test1", "http://www.example.com");
        ns.push(element1);
        Element element2 = new Element("test2", "http://www.example.com");
        ns.push(element2);
        Iterable<Namespace> addedNamespaces = ns.addedForward();
        assertTrue(addedNamespaces.iterator().hasNext());
    }

    @Test
    public void testAddedReverse_EmptyStack() {
        NamespaceStack ns = new NamespaceStack();
        Iterable<Namespace> addedNamespaces = ns.addedReverse();
        assertFalse(addedNamespaces.iterator().hasNext());
    }

    @Test
    public void testAddedReverse() {
        NamespaceStack ns = new NamespaceStack();
        Element element1 = new Element("test1", "http://www.example.com");
        ns.push(element1);
        Element element2 = new Element("test2", "http://www.example.com");
        ns.push(element2);
        Iterable<Namespace> addedNamespaces = ns.addedReverse();
        assertTrue(addedNamespaces.iterator().hasNext());
    }

    @Test
    public void testGetScope() {
        NamespaceStack ns = new NamespaceStack();
        Element element1 = new Element("test1", "http://www.example.com");
        ns.push(element1);
        Element element2 = new Element("test2", "http://www.example.com");
        ns.push(element2);
        Namespace[] expected = new Namespace[]{ Namespace.NO_NAMESPACE, Namespace.XML_NAMESPACE, new Namespace("http://www.example.com", "test2") };
        assertArrayEquals(expected, ns.getScope());
    }

    @Test
    public void testIsInScope() {
        NamespaceStack ns = new NamespaceStack();
        Element element = new Element("test", "http://www.example.com");
        ns.push(element);
        assertTrue(ns.isInScope(new Namespace("http://www.example.com", "test")));
        assertFalse(ns.isInScope(new Namespace("http://www.example.com", "test1")));
    }
}