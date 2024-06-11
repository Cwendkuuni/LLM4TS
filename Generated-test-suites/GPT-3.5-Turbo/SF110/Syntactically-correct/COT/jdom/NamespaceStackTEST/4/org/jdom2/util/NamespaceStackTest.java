
import org.junit.Test;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.NoSuchElementException;

import static org.junit.Assert.*;

public class NamespaceStackTest {

    // Test push method with Element containing no additional namespaces and attributes
    @Test
    public void testPushWithNoAdditionalNamespacesAndAttributes() {
        NamespaceStack stack = new NamespaceStack();
        Element element = new Element("test");
        stack.push(element);
        
        Namespace[] expected = {Namespace.NO_NAMESPACE, Namespace.XML_NAMESPACE};
        Namespace[] actual = stack.getScope();
        
        assertArrayEquals(expected, actual);
    }

    // Test push method with Element containing additional namespaces
    @Test
    public void testPushWithAdditionalNamespaces() {
        NamespaceStack stack = new NamespaceStack();
        Element element = new Element("test");
        Namespace ns1 = Namespace.getNamespace("ns1", "www.example.com/ns1");
        Namespace ns2 = Namespace.getNamespace("ns2", "www.example.com/ns2");
        element.addNamespaceDeclaration(ns1);
        element.addNamespaceDeclaration(ns2);
        stack.push(element);
        
        Namespace[] expected = {ns1, ns2, Namespace.NO_NAMESPACE, Namespace.XML_NAMESPACE};
        Namespace[] actual = stack.getScope();
        
        assertArrayEquals(expected, actual);
    }

    // Test push method with Element containing attributes
    @Test
    public void testPushWithAttributes() {
        NamespaceStack stack = new NamespaceStack();
        Element element = new Element("test");
        Namespace ns = Namespace.getNamespace("ns", "www.example.com/ns");
        Attribute att1 = new Attribute("att1", "value1", ns);
        Attribute att2 = new Attribute("att2", "value2");
        element.setAttribute(att1);
        element.setAttribute(att2);
        stack.push(element);
        
        Namespace[] expected = {ns, Namespace.NO_NAMESPACE, Namespace.XML_NAMESPACE};
        Namespace[] actual = stack.getScope();
        
        assertArrayEquals(expected, actual);
    }

    // Test push method with Attribute
    @Test
    public void testPushWithAttribute() {
        NamespaceStack stack = new NamespaceStack();
        Attribute attribute = new Attribute("test", "value");
        stack.push(attribute);
        
        Namespace[] expected = {Namespace.NO_NAMESPACE, Namespace.XML_NAMESPACE};
        Namespace[] actual = stack.getScope();
        
        assertArrayEquals(expected, actual);
    }

    // Test pop method when stack is not empty
    @Test
    public void testPop() {
        NamespaceStack stack = new NamespaceStack();
        stack.push(new Element("test"));
        stack.pop();
        
        Namespace[] expected = {Namespace.NO_NAMESPACE, Namespace.XML_NAMESPACE};
        Namespace[] actual = stack.getScope();
        
        assertArrayEquals(expected, actual);
    }

    // Test pop method when stack is empty
    @Test(expected = IllegalStateException.class)
    public void testPopWhenStackIsEmpty() {
        NamespaceStack stack = new NamespaceStack();
        stack.pop();
    }

    // Test addedForward method when added namespaces are empty
    @Test
    public void testAddedForwardWithEmptyNamespaces() {
        NamespaceStack stack = new NamespaceStack();
        Iterable<Namespace> iterable = stack.addedForward();

        Iterator<Namespace> iterator = iterable.iterator();
        
        assertFalse(iterator.hasNext());
    }

    // Test addedForward method when added namespaces are not empty
    @Test
    public void testAddedForwardWithNonEmptyNamespaces() {
        NamespaceStack stack = new NamespaceStack();
        Element element = new Element("test");
        Namespace ns1 = Namespace.getNamespace("ns1", "www.example.com/ns1");
        Namespace ns2 = Namespace.getNamespace("ns2", "www.example.com/ns2");
        element.addNamespaceDeclaration(ns1);
        element.addNamespaceDeclaration(ns2);
        stack.push(element);

        Iterable<Namespace> iterable = stack.addedForward();

        Iterator<Namespace> iterator = iterable.iterator();
        
        assertTrue(iterator.hasNext());
        assertEquals(ns1, iterator.next());
        assertTrue(iterator.hasNext());
        assertEquals(ns2, iterator.next());
        assertFalse(iterator.hasNext());
    }

    // Test addedReverse method when added namespaces are empty
    @Test
    public void testAddedReverseWithEmptyNamespaces() {
        NamespaceStack stack = new NamespaceStack();
        Iterable<Namespace> iterable = stack.addedReverse();

        Iterator<Namespace> iterator = iterable.iterator();
        
        assertFalse(iterator.hasNext());
    }

    // Test addedReverse method when added namespaces are not empty
    @Test
    public void testAddedReverseWithNonEmptyNamespaces() {
        NamespaceStack stack = new NamespaceStack();
        Element element = new Element("test");
        Namespace ns1 = Namespace.getNamespace("ns1", "www.example.com/ns1");
        Namespace ns2 = Namespace.getNamespace("ns2", "www.example.com/ns2");
        element.addNamespaceDeclaration(ns1);
        element.addNamespaceDeclaration(ns2);
        stack.push(element);

        Iterable<Namespace> iterable = stack.addedReverse();

        Iterator<Namespace> iterator = iterable.iterator();
        
        assertTrue(iterator.hasNext());
        assertEquals(ns2, iterator.next());
        assertTrue(iterator.hasNext());
        assertEquals(ns1, iterator.next());
        assertFalse(iterator.hasNext());
    }

    // Test iterator method
    @Test
    public void testIterator() {
        NamespaceStack stack = new NamespaceStack();
        Element element = new Element("test");
        Namespace ns1 = Namespace.getNamespace("ns1", "www.example.com/ns1");
        Namespace ns2 = Namespace.getNamespace("ns2", "www.example.com/ns2");
        element.addNamespaceDeclaration(ns1);
        element.addNamespaceDeclaration(ns2);
        stack.push(element);

        Iterator<Namespace> iterator = stack.iterator();
        
        assertTrue(iterator.hasNext());
        assertEquals(ns2, iterator.next());
        assertTrue(iterator.hasNext());
        assertEquals(ns1, iterator.next());
        assertEquals(Namespace.NO_NAMESPACE, iterator.next());
        assertEquals(Namespace.XML_NAMESPACE, iterator.next());
        assertFalse(iterator.hasNext());
    }

    // Test getScope method
    @Test
    public void testGetScope() {
        NamespaceStack stack = new NamespaceStack();
        Element element = new Element("test");
        Namespace ns1 = Namespace.getNamespace("ns1", "www.example.com/ns1");
        Namespace ns2 = Namespace.getNamespace("ns2", "www.example.com/ns2");
        element.addNamespaceDeclaration(ns1);
        element.addNamespaceDeclaration(ns2);
        stack.push(element);

        Namespace[] expected = {ns1, ns2, Namespace.NO_NAMESPACE, Namespace.XML_NAMESPACE};
        Namespace[] actual = stack.getScope();
        
        assertArrayEquals(expected, actual);
    }

    // Test isInScope method with namespace that is in the scope
    @Test
    public void testIsInScopeWithNamespaceInScope() {
        NamespaceStack stack = new NamespaceStack();
        Element element = new Element("test");
        Namespace ns = Namespace.getNamespace("ns", "www.example.com/ns");
        element.addNamespaceDeclaration(ns);
        stack.push(element);

        boolean result = stack.isInScope(ns);
        
        assertTrue(result);
    }

    // Test isInScope method with namespace that is not in the scope
    @Test
    public void testIsInScopeWithNamespaceNotInScope() {
        NamespaceStack stack = new NamespaceStack();
        Element element = new Element("test");
        Namespace ns = Namespace.getNamespace("ns", "www.example.com/ns");
        stack.push(element);

        boolean result = stack.isInScope(ns);
        
        assertFalse(result);
    }

}
