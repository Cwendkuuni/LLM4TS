import static org.junit.Assert.*;
import org.junit.Before;
import org.junit.Test;
import org.jdom2.*;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

public class NamespaceStackTest {

    private NamespaceStack stack;
    private Element element;
    private Attribute attribute;

    @Before
    public void setUp() {
        stack = new NamespaceStack();
        element = new Element("element", Namespace.NO_NAMESPACE);
        attribute = new Attribute("attribute", "value", Namespace.NO_NAMESPACE);
    }

    @Test
    public void testPush_Element_AddsNamespaceToStack() {
        stack.push(element);

        Namespace[] expectedScope = new Namespace[] { Namespace.NO_NAMESPACE, Namespace.XML_NAMESPACE, element.getNamespace() };
        assertArrayEquals(expectedScope, stack.getScope());
    }

    @Test
    public void testPush_Element_AddsAdditionalNamespacesToStack() {
        Namespace namespace1 = Namespace.getNamespace("prefix1", "uri1");
        Namespace namespace2 = Namespace.getNamespace("prefix2", "uri2");

        element.addNamespaceDeclaration(namespace1);
        element.addNamespaceDeclaration(namespace2);

        stack.push(element);

        Namespace[] expectedScope = new Namespace[] { Namespace.NO_NAMESPACE, Namespace.XML_NAMESPACE, element.getNamespace(), namespace1, namespace2 };
        assertArrayEquals(expectedScope, stack.getScope());
    }

    @Test
    public void testPush_Element_AddsNamespaceAttributesToStack() {
        Namespace namespace1 = Namespace.getNamespace("prefix1", "uri1");

        Attribute attribute1 = new Attribute("attr1", "value1", namespace1);
        Attribute attribute2 = new Attribute("attr2", "value2", Namespace.NO_NAMESPACE);

        element.setAttribute(attribute1);
        element.setAttribute(attribute2);

        stack.push(element);

        Namespace[] expectedScope = new Namespace[] { Namespace.NO_NAMESPACE, Namespace.XML_NAMESPACE, element.getNamespace(), namespace1 };
        assertArrayEquals(expectedScope, stack.getScope());
    }

    @Test(expected = IllegalStateException.class)
    public void testPop_EmptyStack() {
        stack.pop();
    }

    @Test
    public void testPop_RemovesTopNamespaceFromStack() {
        stack.push(element);
        stack.push(attribute);

        stack.pop();

        Namespace[] expectedScope = new Namespace[] { Namespace.NO_NAMESPACE, Namespace.XML_NAMESPACE, element.getNamespace() };
        assertArrayEquals(expectedScope, stack.getScope());
    }

    @Test
    public void testIterator_ReturnsIteratorOverNamespacesInStack() {
        Namespace namespace1 = Namespace.getNamespace("prefix1", "uri1");
        Namespace namespace2 = Namespace.getNamespace("prefix2", "uri2");

        element.addNamespaceDeclaration(namespace1);
        element.addNamespaceDeclaration(namespace2);

        stack.push(element);

        List<Namespace> expectedNamespaces = new ArrayList<>();
        expectedNamespaces.add(Namespace.NO_NAMESPACE);
        expectedNamespaces.add(Namespace.XML_NAMESPACE);
        expectedNamespaces.add(element.getNamespace());
        expectedNamespaces.add(namespace1);
        expectedNamespaces.add(namespace2);

        Iterator<Namespace> iterator = stack.iterator();

        int i = 0;
        while (iterator.hasNext()) {
            assertEquals(expectedNamespaces.get(i++), iterator.next());
        }

        assertEquals(expectedNamespaces.size(), i);
    }

    @Test
    public void testIsInScope_ReturnsTrueIfNamespaceInScope() {
        Namespace namespace1 = Namespace.getNamespace("prefix1", "uri1");
        Namespace namespace2 = Namespace.getNamespace("prefix2", "uri2");

        element.addNamespaceDeclaration(namespace1);
        element.addNamespaceDeclaration(namespace2);

        stack.push(element);

        assertTrue(stack.isInScope(Namespace.NO_NAMESPACE));
        assertTrue(stack.isInScope(Namespace.XML_NAMESPACE));
        assertTrue(stack.isInScope(element.getNamespace()));
        assertTrue(stack.isInScope(namespace1));
        assertTrue(stack.isInScope(namespace2));
    }

    @Test
    public void testIsInScope_ReturnsFalseIfNamespaceNotInScope() {
        Namespace namespace1 = Namespace.getNamespace("prefix1", "uri1");
        Namespace namespace2 = Namespace.getNamespace("prefix2", "uri2");

        element.addNamespaceDeclaration(namespace1);
        element.addNamespaceDeclaration(namespace2);

        stack.push(element);

        assertFalse(stack.isInScope(Namespace.NO_NAMESPACE, "prefixA"));
        assertFalse(stack.isInScope(element.getNamespace(), "prefixA"));
        assertFalse(stack.isInScope(namespace1, "prefixA"));
        assertFalse(stack.isInScope(namespace2, "prefixA"));
    }
}