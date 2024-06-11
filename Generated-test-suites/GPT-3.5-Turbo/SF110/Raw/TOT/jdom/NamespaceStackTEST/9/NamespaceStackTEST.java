import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.jdom2.Namespace;
import org.jdom2.Element;
import org.jdom2.Attribute;

import java.util.Iterator;

public class NamespaceStackTest {

    @Test
    @DisplayName("Test push method with Element")
    public void testPushWithElement() {
        NamespaceStack stack = new NamespaceStack();

        Element element = new Element("testElement", Namespace.getNamespace("test", "http://www.example.com"));
        element.addNamespaceDeclaration(Namespace.getNamespace("test2", "http://www.example2.com"));

        stack.push(element);

        Namespace[] expectedScope = {Namespace.NO_NAMESPACE, Namespace.XML_NAMESPACE, element.getNamespace()};
        Assertions.assertArrayEquals(expectedScope, stack.getScope().toArray());

        Iterator<Namespace> addedForwardIterator = stack.addedForward().iterator();
        Assertions.assertEquals(Namespace.getNamespace("test2", "http://www.example2.com"), addedForwardIterator.next());
        Assertions.assertFalse(addedForwardIterator.hasNext());

        Iterator<Namespace> addedReverseIterator = stack.addedReverse().iterator();
        Assertions.assertEquals(Namespace.getNamespace("test2", "http://www.example2.com"), addedReverseIterator.next());
        Assertions.assertFalse(addedReverseIterator.hasNext());

        Assertions.assertTrue(stack.isInScope(Namespace.getNamespace("test", "http://www.example.com")));
        Assertions.assertTrue(stack.isInScope(Namespace.getNamespace("test2", "http://www.example2.com")));
    }

    @Test
    @DisplayName("Test push method with Attribute")
    public void testPushWithAttribute() {
        NamespaceStack stack = new NamespaceStack();

        Attribute attribute = new Attribute("testAttribute", "testValue", Namespace.getNamespace("test", "http://www.example.com"));

        stack.push(attribute);

        Namespace[] expectedScope = {Namespace.NO_NAMESPACE, Namespace.XML_NAMESPACE, attribute.getNamespace()};
        Assertions.assertArrayEquals(expectedScope, stack.getScope().toArray());

        Iterator<Namespace> addedForwardIterator = stack.addedForward().iterator();
        Assertions.assertFalse(addedForwardIterator.hasNext());

        Iterator<Namespace> addedReverseIterator = stack.addedReverse().iterator();
        Assertions.assertFalse(addedReverseIterator.hasNext());

        Assertions.assertTrue(stack.isInScope(Namespace.getNamespace("test", "http://www.example.com")));
    }

    @Test
    @DisplayName("Test pop method")
    public void testPop() {
        NamespaceStack stack = new NamespaceStack();

        Element element = new Element("testElement", Namespace.getNamespace("test", "http://www.example.com"));
        stack.push(element);

        stack.pop();

        Namespace[] expectedScope = {Namespace.NO_NAMESPACE, Namespace.XML_NAMESPACE};
        Assertions.assertArrayEquals(expectedScope, stack.getScope().toArray());

        Iterator<Namespace> addedForwardIterator = stack.addedForward().iterator();
        Assertions.assertFalse(addedForwardIterator.hasNext());

        Iterator<Namespace> addedReverseIterator = stack.addedReverse().iterator();
        Assertions.assertFalse(addedReverseIterator.hasNext());

        Assertions.assertFalse(stack.isInScope(Namespace.getNamespace("test", "http://www.example.com")));
    }

    @Test
    @DisplayName("Test hasNext method of ForwardWalker")
    public void testForwardWalkerHasNext() {
        Namespace[] namespaces = {Namespace.getNamespace("test", "http://www.example.com"), Namespace.getNamespace("test2", "http://www.example2.com")};
        NamespaceStack.ForwardWalker forwardWalker = new NamespaceStack.ForwardWalker(namespaces);

        Assertions.assertTrue(forwardWalker.hasNext());
        forwardWalker.next();
        Assertions.assertTrue(forwardWalker.hasNext());
        forwardWalker.next();
        Assertions.assertFalse(forwardWalker.hasNext());
    }

    @Test
    @DisplayName("Test next method of ForwardWalker")
    public void testForwardWalkerNext() {
        Namespace[] namespaces = {Namespace.getNamespace("test", "http://www.example.com"), Namespace.getNamespace("test2", "http://www.example2.com")};
        NamespaceStack.ForwardWalker forwardWalker = new NamespaceStack.ForwardWalker(namespaces);

        Assertions.assertEquals(Namespace.getNamespace("test", "http://www.example.com"), forwardWalker.next());
        Assertions.assertEquals(Namespace.getNamespace("test2", "http://www.example2.com"), forwardWalker.next());
    }

    @Test
    @DisplayName("Test hasNext method of BackwardWalker")
    public void testBackwardWalkerHasNext() {
        Namespace[] namespaces = {Namespace.getNamespace("test", "http://www.example.com"), Namespace.getNamespace("test2", "http://www.example2.com")};
        NamespaceStack.BackwardWalker backwardWalker = new NamespaceStack.BackwardWalker(namespaces);

        Assertions.assertTrue(backwardWalker.hasNext());
        backwardWalker.next();
        Assertions.assertTrue(backwardWalker.hasNext());
        backwardWalker.next();
        Assertions.assertFalse(backwardWalker.hasNext());
    }

    @Test
    @DisplayName("Test next method of BackwardWalker")
    public void testBackwardWalkerNext() {
        Namespace[] namespaces = {Namespace.getNamespace("test", "http://www.example.com"), Namespace.getNamespace("test2", "http://www.example2.com")};
        NamespaceStack.BackwardWalker backwardWalker = new NamespaceStack.BackwardWalker(namespaces);

        Assertions.assertEquals(Namespace.getNamespace("test2", "http://www.example2.com"), backwardWalker.next());
        Assertions.assertEquals(Namespace.getNamespace("test", "http://www.example.com"), backwardWalker.next());
    }

    @Test
    @DisplayName("Test hasNext method of EmptyIterable")
    public void testEmptyIterableHasNext() {
        NamespaceStack.EmptyIterable emptyIterable = new NamespaceStack.EmptyIterable();

        Assertions.assertFalse(emptyIterable.hasNext());
    }

    @Test
    @DisplayName("Test next method of EmptyIterable")
    public void testEmptyIterableNext() {
        NamespaceStack.EmptyIterable emptyIterable = new NamespaceStack.EmptyIterable();

        Assertions.assertThrows(NoSuchElementException.class, emptyIterable::next);
    }

    @Test
    @DisplayName("Test isInScope method with present namespace")
    public void testIsInScopeWithPresentNamespace() {
        NamespaceStack stack = new NamespaceStack();

        Element element = new Element("testElement", Namespace.getNamespace("test", "http://www.example.com"));
        stack.push(element);

        Assertions.assertTrue(stack.isInScope(Namespace.getNamespace("test", "http://www.example.com")));
    }

    @Test
    @DisplayName("Test isInScope method with not present namespace")
    public void testIsInScopeWithNotPresentNamespace() {
        NamespaceStack stack = new NamespaceStack();

        Element element = new Element("testElement", Namespace.getNamespace("test", "http://www.example.com"));
        stack.push(element);

        Assertions.assertFalse(stack.isInScope(Namespace.getNamespace("test2", "http://www.example2.com")));
    }

}