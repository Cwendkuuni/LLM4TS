
package org.jdom2.util;
import org.jdom2.Namespace;
import org.junit.*;
import static org.junit.Assert.*;

public class NamespaceStackTest {

    // Test case for default constructor
    @Test
    public void testDefaultConstructor() {
        NamespaceStack ns = new NamespaceStack();
        assertNotNull(ns);
        Namespace[] expected = { Namespace.NO_NAMESPACE, Namespace.XML_NAMESPACE };
        assertArrayEquals(expected, ns.getScope());
    }

    // Test case for constructor with seed
    @Test
    public void testConstructorWithSeed() {
        Namespace[] seed = { Namespace.NO_NAMESPACE };
        NamespaceStack ns = new NamespaceStack(seed);
        assertNotNull(ns);
        Namespace[] expected = { Namespace.NO_NAMESPACE };
        assertArrayEquals(expected, ns.getScope());
    }

    // Test case for push(Element) method
    @Test
    public void testPushElement() {
        NamespaceStack ns = new NamespaceStack();
        Element element = new Element("element");
        element.setNamespace(Namespace.getNamespace("ns", "http://www.example.com"));
        ns.push(element);
        assertTrue(ns.isInScope(Namespace.getNamespace("ns", "http://www.example.com")));
    }

    // Test case for push(Attribute) method
    @Test
    public void testPushAttribute() {
        NamespaceStack ns = new NamespaceStack();
        Attribute attribute = new Attribute("attribute", "value", Namespace.getNamespace("ns", "http://www.example.com"));
        ns.push(attribute);
        assertTrue(ns.isInScope(Namespace.getNamespace("ns", "http://www.example.com")));
    }

    // Test case for pop() method
    @Test
    public void testPop() {
        NamespaceStack ns = new NamespaceStack();
        Element element = new Element("element");
        element.setNamespace(Namespace.getNamespace("ns", "http://www.example.com"));
        ns.push(element);
        ns.pop();
        assertFalse(ns.isInScope(Namespace.getNamespace("ns", "http://www.example.com")));
    }

    // Test case for addedForward() method with non-empty added namespace
    @Test
    public void testAddedForwardNonEmpty() {
        NamespaceStack ns = new NamespaceStack();
        Element element = new Element("element");
        element.setNamespace(Namespace.getNamespace("ns", "http://www.example.com"));
        ns.push(element);
        Iterable<Namespace> added = ns.addedForward();
        assertTrue(added.iterator().hasNext());
        assertEquals(Namespace.getNamespace("ns", "http://www.example.com"), added.iterator().next());
    }

    // Test case for addedForward() method with empty added namespace
    @Test
    public void testAddedForwardEmpty() {
        NamespaceStack ns = new NamespaceStack();
        Iterable<Namespace> added = ns.addedForward();
        assertFalse(added.iterator().hasNext());
    }

    // Test case for addedReverse() method with non-empty added namespace
    @Test
    public void testAddedReverseNonEmpty() {
        NamespaceStack ns = new NamespaceStack();
        Element element = new Element("element");
        element.setNamespace(Namespace.getNamespace("ns", "http://www.example.com"));
        ns.push(element);
        Iterable<Namespace> added = ns.addedReverse();
        assertTrue(added.iterator().hasNext());
        assertEquals(Namespace.getNamespace("ns", "http://www.example.com"), added.iterator().next());
    }

    // Test case for addedReverse() method with empty added namespace
    @Test
    public void testAddedReverseEmpty() {
        NamespaceStack ns = new NamespaceStack();
        Iterable<Namespace> added = ns.addedReverse();
        assertFalse(added.iterator().hasNext());
    }

    // Test case for iterator() method
    @Test
    public void testIterator() {
        NamespaceStack ns = new NamespaceStack();
        Element element = new Element("element");
        element.setNamespace(Namespace.getNamespace("ns", "http://www.example.com"));
        ns.push(element);
        assertTrue(ns.iterator().hasNext());
        assertEquals(Namespace.getNamespace("ns", "http://www.example.com"), ns.iterator().next());
    }

    // Test case for getScope() method
    @Test
    public void testGetScope() {
        NamespaceStack ns = new NamespaceStack();
        Namespace[] scope = ns.getScope();
        assertNotNull(scope);
        assertArrayEquals(NamespaceStack.DEFAULTSEED, scope);
    }

    // Test case for isInScope() method when namespace is in scope
    @Test
    public void testIsInScopeTrue() {
        NamespaceStack ns = new NamespaceStack();
        Element element = new Element("element");
        element.setNamespace(Namespace.getNamespace("ns", "http://www.example.com"));
        ns.push(element);
        assertTrue(ns.isInScope(Namespace.getNamespace("ns", "http://www.example.com")));
    }

    // Test case for isInScope() method when namespace is not in scope
    @Test
    public void testIsInScopeFalse() {
        NamespaceStack ns = new NamespaceStack();
        Element element = new Element("element");
        element.setNamespace(Namespace.getNamespace("ns", "http://www.example.com"));
        ns.push(element);
        assertFalse(ns.isInScope(Namespace.getNamespace("other", "http://www.example.com")));
    }
}

