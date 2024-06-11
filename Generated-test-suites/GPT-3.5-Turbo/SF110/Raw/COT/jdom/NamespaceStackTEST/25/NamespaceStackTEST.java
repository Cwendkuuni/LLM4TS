
package org.jdom2.util;

import static org.junit.Assert.*;
import org.junit.Test;
import java.util.NoSuchElementException;
import org.jdom2.Namespace;
import org.jdom2.Element;
import org.jdom2.Attribute;

public class NamespaceStackTest {

    // Test case for push(Element element) method
    @Test
    public void testPushElement() {
        NamespaceStack stack = new NamespaceStack();
        Element element = new Element("test");
        element.setNamespace(Namespace.getNamespace("ns", "http://www.example.com"));

        stack.push(element);

        assertTrue(stack.isInScope(Namespace.getNamespace("ns", "http://www.example.com")));
    }

    // Test case for push(Attribute attribute) method
    @Test
    public void testPushAttribute() {
        NamespaceStack stack = new NamespaceStack();
        Attribute attribute = new Attribute("test", "value", Namespace.getNamespace("ns", "http://www.example.com"));

        stack.push(attribute);

        assertTrue(stack.isInScope(Namespace.getNamespace("ns", "http://www.example.com")));
    }

    // Test case for pop() method
    @Test(expected = IllegalStateException.class)
    public void testPop() {
        NamespaceStack stack = new NamespaceStack();
        stack.pop();
    }

    // Test case for addedForward() method
    @Test
    public void testAddedForward() {
        NamespaceStack stack = new NamespaceStack();
        Element element = new Element("test");
        element.setNamespace(Namespace.getNamespace("ns", "http://www.example.com"));
        stack.push(element);

        Iterable<Namespace> namespaces = stack.addedForward();
        assertNotNull(namespaces);

        Namespace[] expected = {Namespace.getNamespace("ns", "http://www.example.com")};
        int i = 0;
        for (Namespace ns : namespaces) {
            assertEquals(expected[i++], ns);
        }
    }

    // Test case for addedReverse() method
    @Test
    public void testAddedReverse() {
        NamespaceStack stack = new NamespaceStack();
        Element element = new Element("test");
        element.setNamespace(Namespace.getNamespace("ns", "http://www.example.com"));
        stack.push(element);

        Iterable<Namespace> namespaces = stack.addedReverse();
        assertNotNull(namespaces);

        Namespace[] expected = {Namespace.getNamespace("ns", "http://www.example.com")};
        int i = expected.length - 1;
        for (Namespace ns : namespaces) {
            assertEquals(expected[i--], ns);
        }
    }

    // Test case for iterator() method
    @Test
    public void testIterator() {
        NamespaceStack stack = new NamespaceStack();
        Element element = new Element("test");
        element.setNamespace(Namespace.getNamespace("ns", "http://www.example.com"));
        stack.push(element);

        Iterator<Namespace> iterator = stack.iterator();
        assertNotNull(iterator);

        int count = 0;
        while (iterator.hasNext()) {
            iterator.next();
            count++;
        }
        assertEquals(1, count);
    }

    // Test case for getScope() method
    @Test
    public void testGetScope() {
        NamespaceStack stack = new NamespaceStack();
        Element element = new Element("test");
        element.setNamespace(Namespace.getNamespace("ns", "http://www.example.com"));
        stack.push(element);

        Namespace[] scope = stack.getScope();
        assertNotNull(scope);
        assertEquals(1, scope.length);
        assertEquals(Namespace.getNamespace("ns", "http://www.example.com"), scope[0]);
    }

    // Test case for isInScope(Namespace ns) method
    @Test
    public void testIsInScope() {
        NamespaceStack stack = new NamespaceStack();
        Element element = new Element("test");
        element.setNamespace(Namespace.getNamespace("ns", "http://www.example.com"));
        stack.push(element);

        assertTrue(stack.isInScope(Namespace.getNamespace("ns", "http://www.example.com")));
        assertFalse(stack.isInScope(Namespace.getNamespace("ns", "http://www.example.org")));
    }
}
