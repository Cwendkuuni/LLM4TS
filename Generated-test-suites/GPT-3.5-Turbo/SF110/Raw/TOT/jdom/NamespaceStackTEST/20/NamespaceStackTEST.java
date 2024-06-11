import org.junit.Test;
import org.jdom2.Namespace;
import org.jdom2.util.NamespaceStack;

import static org.junit.Assert.*;

public class NamespaceStackTest {

    @Test
    public void testPush_ElementWithNullNamespace_AddsNamespaceToScope() {
        NamespaceStack stack = new NamespaceStack();
        Namespace namespace = Namespace.getNamespace("test", "http://www.example.com");
        stack.push(new org.jdom2.Element("testElement", namespace));
        assertTrue(stack.isInScope(namespace));
    }

    @Test
    public void testPush_ElementWithAdditionalNamespaces_AddsNamespacesToScope() {
        NamespaceStack stack = new NamespaceStack();
        Namespace defaultNamespace = Namespace.getNamespace("default", "http://www.example.com/default");
        Namespace additionalNamespace1 = Namespace.getNamespace("additional1", "http://www.example.com/additional1");
        Namespace additionalNamespace2 = Namespace.getNamespace("additional2", "http://www.example.com/additional2");
        org.jdom2.Element element = new org.jdom2.Element("testElement", defaultNamespace);
        element.addNamespaceDeclaration(additionalNamespace1);
        element.addNamespaceDeclaration(additionalNamespace2);
        stack.push(element);

        assertTrue(stack.isInScope(defaultNamespace));
        assertTrue(stack.isInScope(additionalNamespace1));
        assertTrue(stack.isInScope(additionalNamespace2));
    }

    @Test
    public void testPush_ElementWithAttributes_AddsNamespacesToScope() {
        NamespaceStack stack = new NamespaceStack();
        Namespace defaultNamespace = Namespace.getNamespace("default", "http://www.example.com/default");
        Namespace attributeNamespace = Namespace.getNamespace("attribute", "http://www.example.com/attribute");
        org.jdom2.Element element = new org.jdom2.Element("testElement", defaultNamespace);
        org.jdom2.Attribute attribute = new org.jdom2.Attribute("testAttribute", "value", attributeNamespace);
        element.setAttribute(attribute);
        stack.push(element);

        assertTrue(stack.isInScope(defaultNamespace));
        assertTrue(stack.isInScope(attributeNamespace));
    }

    @Test
    public void testPush_AttributeWithNullNamespace_AddsNamespaceToScope() {
        NamespaceStack stack = new NamespaceStack();
        Namespace namespace = Namespace.getNamespace("attribute", "http://www.example.com/attribute");
        stack.push(new org.jdom2.Attribute("testAttribute", "value", namespace));
        assertTrue(stack.isInScope(namespace));
    }

    @Test
    public void testPop_PushCalledOnce_RemovesNamespaceFromScope() {
        NamespaceStack stack = new NamespaceStack();
        Namespace namespace = Namespace.getNamespace("test", "http://www.example.com");
        stack.push(new org.jdom2.Element("testElement", namespace));
        stack.pop();
        assertFalse(stack.isInScope(namespace));
    }

    @Test(expected = IllegalStateException.class)
    public void testPop_PushCalledZeroTimes_ThrowsIllegalStateException() {
        NamespaceStack stack = new NamespaceStack();
        stack.pop();
    }

    @Test
    public void testAddedForward_AddedNamespacesExists_ReturnsIterableInForwardOrder() {
        NamespaceStack stack = new NamespaceStack();
        Namespace namespace1 = Namespace.getNamespace("test1", "http://www.example.com/test1");
        Namespace namespace2 = Namespace.getNamespace("test2", "http://www.example.com/test2");
        stack.push(new org.jdom2.Element("testElement1", namespace1));
        stack.push(new org.jdom2.Element("testElement2", namespace2));

        int count = 0;
        for (Namespace ns : stack.addedForward()) {
            if (count == 0) {
                assertEquals(namespace1, ns);
            } else if (count == 1) {
                assertEquals(namespace2, ns);
            }
            count++;
        }
        assertEquals(2, count);
    }

    @Test
    public void testAddedForward_AddedNamespacesDoesNotExist_ReturnsEmptyIterable() {
        NamespaceStack stack = new NamespaceStack();
        int count = 0;
        for (Namespace ns : stack.addedForward()) {
            count++;
        }
        assertEquals(0, count);
    }

    @Test
    public void testAddedReverse_AddedNamespacesExists_ReturnsIterableInReverseOrder() {
        NamespaceStack stack = new NamespaceStack();
        Namespace namespace1 = Namespace.getNamespace("test1", "http://www.example.com/test1");
        Namespace namespace2 = Namespace.getNamespace("test2", "http://www.example.com/test2");
        stack.push(new org.jdom2.Element("testElement1", namespace1));
        stack.push(new org.jdom2.Element("testElement2", namespace2));

        int count = 0;
        for (Namespace ns : stack.addedReverse()) {
            if (count == 0) {
                assertEquals(namespace2, ns);
            } else if (count == 1) {
                assertEquals(namespace1, ns);
            }
            count++;
        }
        assertEquals(2, count);
    }

    @Test
    public void testAddedReverse_AddedNamespacesDoesNotExist_ReturnsEmptyIterable() {
        NamespaceStack stack = new NamespaceStack();
        int count = 0;
        for (Namespace ns : stack.addedReverse()) {
            count++;
        }
        assertEquals(0, count);
    }

    @Test
    public void testIterator_ScopeNamespacesExists_ReturnsIteratorInForwardOrder() {
        NamespaceStack stack = new NamespaceStack();
        Namespace namespace1 = Namespace.getNamespace("test1", "http://www.example.com/test1");
        Namespace namespace2 = Namespace.getNamespace("test2", "http://www.example.com/test2");
        stack.push(new org.jdom2.Element("testElement1", namespace1));
        stack.push(new org.jdom2.Element("testElement2", namespace2));

        int count = 0;
        for (Namespace ns : stack) {
            if (count == 0) {
                assertEquals(namespace1, ns);
            } else if (count == 1) {
                assertEquals(namespace2, ns);
            }
            count++;
        }
        assertEquals(2, count);
    }

    @Test
    public void testIterator_ScopeNamespacesDoesNotExist_ReturnsEmptyIterator() {
        NamespaceStack stack = new NamespaceStack();
        int count = 0;
        for (Namespace ns : stack) {
            count++;
        }
        assertEquals(0, count);
    }

    @Test
    public void testGetScope_AddedNamespacesExists_ReturnsArrayWithAddedNamespaces() {
        NamespaceStack stack = new NamespaceStack();
        Namespace namespace1 = Namespace.getNamespace("test1", "http://www.example.com/test1");
        Namespace namespace2 = Namespace.getNamespace("test2", "http://www.example.com/test2");
        stack.push(new org.jdom2.Element("testElement1", namespace1));
        stack.push(new org.jdom2.Element("testElement2", namespace2));

        Namespace[] scope = stack.getScope();
        assertEquals(2, scope.length);
        assertEquals(namespace1, scope[0]);
        assertEquals(namespace2, scope[1]);
    }

    @Test
    public void testGetScope_AddedNamespacesDoesNotExist_ReturnsEmptyArray() {
        NamespaceStack stack = new NamespaceStack();
        Namespace[] scope = stack.getScope();
        assertEquals(0, scope.length);
    }

    @Test
    public void testIsInScope_NamespaceExistsInScope_ReturnTrue() {
        NamespaceStack stack = new NamespaceStack();
        Namespace namespace = Namespace.getNamespace("test", "http://www.example.com");
        stack.push(new org.jdom2.Element("testElement", namespace));
        assertTrue(stack.isInScope(namespace));
    }

    @Test
    public void testIsInScope_NamespaceDoesNotExistInScope_ReturnFalse() {
        NamespaceStack stack = new NamespaceStack();
        Namespace namespace = Namespace.getNamespace("test", "http://www.example.com");
        assertFalse(stack.isInScope(namespace));
    }

}