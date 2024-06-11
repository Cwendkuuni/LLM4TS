
import org.jdom2.Namespace;
import org.junit.Before;
import org.junit.Test;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.NoSuchElementException;

import static org.junit.Assert.*;

public class NamespaceStackTest {

    private NamespaceStack namespaceStack;

    @Before
    public void setUp() {
        namespaceStack = new NamespaceStack();
    }

    @Test
    public void testPush_Element() {
        Element element = new Element("testElement");
        Namespace namespace = Namespace.getNamespace("testPrefix", "testUri");
        List<Namespace> additionalNamespaces = new ArrayList<>();
        Namespace additionalNamespace = Namespace.getNamespace("additionalPrefix", "additionalUri");
        additionalNamespaces.add(additionalNamespace);
        element.setNamespace(namespace);
        element.setAdditionalNamespaces(additionalNamespaces);

        namespaceStack.push(element);

        assertTrue(namespaceStack.isInScope(namespace));
        assertTrue(namespaceStack.isInScope(additionalNamespace));
    }

    @Test
    public void testPush_Attribute() {
        Attribute attribute = new Attribute("testAttribute", "testValue");
        Namespace namespace = Namespace.getNamespace("testPrefix", "testUri");
        attribute.setNamespace(namespace);

        namespaceStack.push(attribute);

        assertTrue(namespaceStack.isInScope(namespace));
    }

    @Test(expected = IllegalStateException.class)
    public void testPop_EmptyStack() {
        namespaceStack.pop();
    }

    @Test
    public void testPop() {
        Element element = new Element("testElement");
        Namespace namespace = Namespace.getNamespace("testPrefix", "testUri");
        element.setNamespace(namespace);
        namespaceStack.push(element);

        namespaceStack.pop();

        assertFalse(namespaceStack.isInScope(namespace));
    }

    @Test
    public void testAddedForward_EmptyStack() {
        Iterable<Namespace> addedNamespaces = namespaceStack.addedForward();

        Iteratort<Namespace> iterator = addedNamespaces.iterator();
        
        assertFalse(iterator.hasNext());
        assertThrows(NoSuchElementException.class, iterator::next);
    }

    @Test
    public void testAddedForward() {
        Element element = new Element("testElement");
        Namespace namespace = Namespace.getNamespace("testPrefix", "testUri");
        List<Namespace> additionalNamespaces = new ArrayList<>();
        Namespace additionalNamespace = Namespace.getNamespace("additionalPrefix", "additionalUri");
        additionalNamespaces.add(additionalNamespace);
        element.setNamespace(namespace);
        element.setAdditionalNamespaces(additionalNamespaces);
        namespaceStack.push(element);

        Iterable<Namespace> addedNamespaces = namespaceStack.addedForward();

        Iterator<Namespace> iterator = addedNamespaces.iterator();

        assertTrue(iterator.hasNext());
        assertEquals(additionalNamespace, iterator.next());
        assertFalse(iterator.hasNext());
        assertThrows(NoSuchElementException.class, iterator::next);
    }

    @Test
    public void testAddedReverse_EmptyStack() {
        Iterable<Namespace> addedNamespaces = namespaceStack.addedReverse();

        Iteratort<Namespace> iterator = addedNamespaces.iterator();
        
        assertFalse(iterator.hasNext());
        assertThrows(NoSuchElementException.class, iterator::next);
    }

    @Test
    public void testAddedReverse() {
        Element element = new Element("testElement");
        Namespace namespace = Namespace.getNamespace("testPrefix", "testUri");
        List<Namespace> additionalNamespaces = new ArrayList<>();
        Namespace additionalNamespace = Namespace.getNamespace("additionalPrefix", "additionalUri");
        additionalNamespaces.add(additionalNamespace);
        element.setNamespace(namespace);
        element.setAdditionalNamespaces(additionalNamespaces);
        namespaceStack.push(element);

        Iterable<Namespace> addedNamespaces = namespaceStack.addedReverse();

        Iterator<Namespace> iterator = addedNamespaces.iterator();

        assertTrue(iterator.hasNext());
        assertEquals(additionalNamespace, iterator.next());
        assertFalse(iterator.hasNext());
        assertThrows(NoSuchElementException.class, iterator::next);
    }

    @Test
    public void testIterator_EmptyStack() {
        Iterator<Namespace> iterator = namespaceStack.iterator();
        
        assertFalse(iterator.hasNext());
        assertThrows(NoSuchElementException.class, iterator::next);
    }

    @Test
    public void testIterator() {
        Element element = new Element("testElement");
        Namespace namespace = Namespace.getNamespace("testPrefix", "testUri");
        element.setNamespace(namespace);
        namespaceStack.push(element);

        Iterator<Namespace> iterator = namespaceStack.iterator();

        assertTrue(iterator.hasNext());
        assertEquals(namespace, iterator.next());
        assertFalse(iterator.hasNext());
        assertThrows(NoSuchElementException.class, iterator::next);
    }

    @Test
    public void testGetScope() {
        Element element = new Element("testElement");
        Namespace namespace = Namespace.getNamespace("testPrefix", "testUri");
        element.setNamespace(namespace);
        namespaceStack.push(element);

        Namespace[] scope = namespaceStack.getScope();

        assertEquals(2, scope.length);
        assertEquals(Namespace.NO_NAMESPACE, scope[0]);
        assertEquals(namespace, scope[1]);
    }

    @Test
    public void testIsInScope_NotInScope() {
        Namespace namespace = Namespace.getNamespace("testPrefix", "testUri");

        boolean isInScope = namespaceStack.isInScope(namespace);

        assertFalse(isInScope);
    }

    @Test
    public void testIsInScope() {
        Element element = new Element("testElement");
        Namespace namespace = Namespace.getNamespace("testPrefix", "testUri");
        element.setNamespace(namespace);
        namespaceStack.push(element);

        boolean isInScope = namespaceStack.isInScope(namespace);

        assertTrue(isInScope);
    }
}
