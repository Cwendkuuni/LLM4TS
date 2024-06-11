import org.junit.Before;
import org.junit.Test;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Iterator;
import java.util.List;

import static org.junit.Assert.*;

public class NamespaceStackTest {

    private NamespaceStack namespaceStack;

    @Before
    public void setUp() {
        namespaceStack = new NamespaceStack();
    }

    @Test
    public void testPush_ElementWithNoAdditionalNamespacesAndNoAttributes_ReturnsCorrectAddedForward() {
        // Arrange
        Element element = new Element("Element");
        
        // Act
        namespaceStack.push(element);
        Iterable<Namespace> addedForward = namespaceStack.addedForward();
        
        // Assert
        List<Namespace> expectedAddedForward = new ArrayList<>();
        assertEquals(expectedAddedForward, addedForward);
    }

    @Test
    public void testPush_ElementWithAdditionalNamespaces_ReturnsCorrectAddedForward() {
        // Arrange
        Namespace ns1 = Namespace.getNamespace("prefix1", "uri1");
        Namespace ns2 = Namespace.getNamespace("prefix2", "uri2");
        Namespace ns3 = Namespace.getNamespace("prefix3", "uri3");
        Element element = new Element("Element");
        element.addNamespaceDeclaration(ns1);
        element.addNamespaceDeclaration(ns2);
        element.addNamespaceDeclaration(ns3);

        // Act
        namespaceStack.push(element);
        Iterable<Namespace> addedForward = namespaceStack.addedForward();

        // Assert
        List<Namespace> expectedAddedForward = Arrays.asList(ns1, ns2, ns3);
        assertEquals(expectedAddedForward, addedForward);
    }

    @Test
    public void testPush_ElementWithAttributes_ReturnsCorrectAddedForward() {
        // Arrange
        Namespace ns1 = Namespace.getNamespace("prefix1", "uri1");
        Namespace ns2 = Namespace.getNamespace("prefix2", "uri2");
        Namespace ns3 = Namespace.getNamespace("prefix3", "uri3");
        Element element = new Element("Element");
        element.setAttribute("attribute1", "value1", ns1);
        element.setAttribute("attribute2", "value2", ns2);
        element.setAttribute("attribute3", "value3", ns3);

        // Act
        namespaceStack.push(element);
        Iterable<Namespace> addedForward = namespaceStack.addedForward();

        // Assert
        List<Namespace> expectedAddedForward = Arrays.asList(ns1, ns2, ns3);
        assertEquals(expectedAddedForward, addedForward);
    }

    @Test
    public void testPush_AttributeWithNamespace_ReturnsCorrectAddedForward() {
        // Arrange
        Namespace ns1 = Namespace.getNamespace("prefix1", "uri1");
        Attribute attribute = new Attribute("attribute", "value", ns1);

        // Act
        namespaceStack.push(attribute);
        Iterable<Namespace> addedForward = namespaceStack.addedForward();

        // Assert
        List<Namespace> expectedAddedForward = Arrays.asList(ns1);
        assertEquals(expectedAddedForward, addedForward);
    }

    @Test(expected = IllegalStateException.class)
    public void testPop_EmptyStack_ThrowsIllegalStateException() {
        // Act
        namespaceStack.pop();
    }

    @Test
    public void testPop_NonEmptyStack_RemovesTopElement() {
        // Arrange
        Element element1 = new Element("Element1");
        Element element2 = new Element("Element2");
        namespaceStack.push(element1);
        namespaceStack.push(element2);

        // Act
        namespaceStack.pop();

        // Assert
        Iterable<Namespace> addedForward = namespaceStack.addedForward();
        List<Namespace> expectedAddedForward = new ArrayList<>();
        assertEquals(expectedAddedForward, addedForward);
    }

    @Test
    public void testIterator_EmptyStack_ReturnsEmptyIterator() {
        // Arrange

        // Act
        Iterator<Namespace> iterator = namespaceStack.iterator();

        // Assert
        assertFalse(iterator.hasNext());
    }

    @Test
    public void testIterator_NonEmptyStack_ReturnsIteratorWithCorrectNamespaces() {
        // Arrange
        Namespace ns1 = Namespace.getNamespace("prefix1", "uri1");
        Namespace ns2 = Namespace.getNamespace("prefix2", "uri2");
        Element element = new Element("Element");
        element.addNamespaceDeclaration(ns1);
        element.addNamespaceDeclaration(ns2);
        namespaceStack.push(element);

        // Act
        Iterator<Namespace> iterator = namespaceStack.iterator();

        // Assert
        assertTrue(iterator.hasNext());
        assertEquals(ns1, iterator.next());
        assertTrue(iterator.hasNext());
        assertEquals(ns2, iterator.next());
        assertFalse(iterator.hasNext());
    }

    @Test
    public void testGetScope_EmptyStack_ReturnsEmptyArray() {
        // Arrange

        // Act
        Namespace[] scope = namespaceStack.getScope();

        // Assert
        Namespace[] expectedScope = new Namespace[0];
        assertArrayEquals(expectedScope, scope);
    }

    @Test
    public void testGetScope_NonEmptyStack_ReturnsArrayContainingNamespaces() {
        // Arrange
        Namespace ns1 = Namespace.getNamespace("prefix1", "uri1");
        Namespace ns2 = Namespace.getNamespace("prefix2", "uri2");
        Element element = new Element("Element");
        element.addNamespaceDeclaration(ns1);
        element.addNamespaceDeclaration(ns2);
        namespaceStack.push(element);

        // Act
        Namespace[] scope = namespaceStack.getScope();

        // Assert
        Namespace[] expectedScope = new Namespace[]{ns1, ns2};
        assertArrayEquals(expectedScope, scope);
    }

    @Test
    public void testIsInScope_ElementWithNamespaceInScope_ReturnsTrue() {
        // Arrange
        Namespace ns = Namespace.getNamespace("prefix", "uri");
        Element element = new Element("Element");
        element.addNamespaceDeclaration(ns);
        namespaceStack.push(element);

        // Act
        boolean isInScope = namespaceStack.isInScope(ns);

        // Assert
        assertTrue(isInScope);
    }

    @Test
    public void testIsInScope_ElementWithNamespaceNotInScope_ReturnsFalse() {
        // Arrange
        Namespace ns = Namespace.getNamespace("prefix", "uri");
        Element element = new Element("Element");
        element.addNamespaceDeclaration(ns);
        namespaceStack.push(element);

        // Act
        boolean isInScope = namespaceStack.isInScope(Namespace.getNamespace("prefix2", "uri2"));

        // Assert
        assertFalse(isInScope);
    }

    @Test
    public void testIsInScope_NamespaceNotInStack_ReturnsFalse() {
        // Arrange

        // Act
        boolean isInScope = namespaceStack.isInScope(Namespace.getNamespace("prefix", "uri"));

        // Assert
        assertFalse(isInScope);
    }
}