import org.junit.Test;

import static org.junit.Assert.*;

public class NamespaceStackTest {

    @Test
    public void testPush_Element_NoError() {
        Element element = new Element("testElement");
        NamespaceStack namespaceStack = new NamespaceStack();
        namespaceStack.push(element);

        // Check if the element is added to the stack
        assertTrue(namespaceStack.isInScope(element.getNamespace()));
    }

    @Test
    public void testPush_Element_AdditionalNamespace() {
        Element element = new Element("testElement", Namespace.getNamespace("test", "http://www.testnamespace.com"));
        Namespace additionalNamespace = Namespace.getNamespace("additional", "http://www.additionalnamespace.com");
        element.addNamespaceDeclaration(additionalNamespace);

        NamespaceStack namespaceStack = new NamespaceStack();
        namespaceStack.push(element);

        // Check if the additional namespace is added to the stack
        assertTrue(namespaceStack.isInScope(additionalNamespace));
    }

    @Test
    public void testPush_Element_AttributeNamespace() {
        Element element = new Element("testElement");
        element.setAttribute(new Attribute("testAttr", "testValue", Namespace.getNamespace("attr", "http://www.attrnamespace.com")));

        NamespaceStack namespaceStack = new NamespaceStack();
        namespaceStack.push(element);

        // Check if the attribute namespace is added to the stack
        assertTrue(namespaceStack.isInScope(element.getAttribute("testAttr").getNamespace()));
    }

    @Test
    public void testPush_Attribute_NoError() {
        Attribute attribute = new Attribute("testAttr", "testValue", Namespace.getNamespace("attr", "http://www.attrnamespace.com"));

        NamespaceStack namespaceStack = new NamespaceStack();
        namespaceStack.push(attribute);

        // Check if the attribute namespace is added to the stack
        assertTrue(namespaceStack.isInScope(attribute.getNamespace()));
    }

    @Test(expected = IllegalStateException.class)
    public void testPop_EmptyStack_ThrowsException() {
        NamespaceStack namespaceStack = new NamespaceStack();
        namespaceStack.pop();

        // The stack is empty, so exception should be thrown
    }

    @Test
    public void testPop_ValidPop_NoError() {
        Element element = new Element("testElement");
        NamespaceStack namespaceStack = new NamespaceStack();
        namespaceStack.push(element);

        // Check if the element is added to the stack
        assertTrue(namespaceStack.isInScope(element.getNamespace()));

        namespaceStack.pop();

        // Check if the element is removed from the stack
        assertFalse(namespaceStack.isInScope(element.getNamespace()));
    }

    @Test
    public void testAddedForward_EmptyStack_ReturnsEmptyIterator() {
        NamespaceStack namespaceStack = new NamespaceStack();

        // Check if an empty iterator is returned
        assertFalse(namespaceStack.addedForward().iterator().hasNext());
    }

    @Test
    public void testAddedForward_NonEmptyStack_ReturnsForwardIterator() {
        Element element = new Element("testElement");
        NamespaceStack namespaceStack = new NamespaceStack();
        namespaceStack.push(element);

        // Check if the iterator returns the stack elements in the forward order
        assertTrue(namespaceStack.addedForward().iterator().hasNext());
        assertEquals(element.getNamespace(), namespaceStack.addedForward().iterator().next());
    }

    @Test
    public void testAddedReverse_EmptyStack_ReturnsEmptyIterator() {
        NamespaceStack namespaceStack = new NamespaceStack();

        // Check if an empty iterator is returned
        assertFalse(namespaceStack.addedReverse().iterator().hasNext());
    }

    @Test
    public void testAddedReverse_NonEmptyStack_ReturnsReverseIterator() {
        Element element = new Element("testElement");
        NamespaceStack namespaceStack = new NamespaceStack();
        namespaceStack.push(element);

        // Check if the iterator returns the stack elements in the reverse order
        assertTrue(namespaceStack.addedReverse().iterator().hasNext());
        assertEquals(element.getNamespace(), namespaceStack.addedReverse().iterator().next());
    }

    @Test
    public void testGetScope_EmptyStack_ReturnsEmptyArray() {
        NamespaceStack namespaceStack = new NamespaceStack();

        // Check if an empty array is returned
        assertEquals(0, namespaceStack.getScope().length);
    }

    @Test
    public void testGetScope_NonEmptyStack_ReturnsCorrectArray() {
        Element element = new Element("testElement");
        NamespaceStack namespaceStack = new NamespaceStack();
        namespaceStack.push(element);

        // Check if the correct scope array is returned
        assertEquals(2, namespaceStack.getScope().length);
        assertEquals(Namespace.NO_NAMESPACE, namespaceStack.getScope()[0]);
        assertEquals(Namespace.XML_NAMESPACE, namespaceStack.getScope()[1]);
    }

    @Test
    public void testIsInScope_ElementNamespace_ReturnsTrue() {
        Element element = new Element("testElement");
        NamespaceStack namespaceStack = new NamespaceStack();
        namespaceStack.push(element);

        // Check if the element namespace is in scope
        assertTrue(namespaceStack.isInScope(element.getNamespace()));
    }

    @Test
    public void testIsInScope_NotInScope_ReturnsFalse() {
        Element element = new Element("testElement");
        NamespaceStack namespaceStack = new NamespaceStack();

        // Check if the namespace is not in scope
        assertFalse(namespaceStack.isInScope(element.getNamespace()));
    }

    @Test
    public void testIsInScope_NonElementNamespace_ReturnsFalse() {
        Element element = new Element("testElement");
        Namespace nonElementNamespace = Namespace.getNamespace("test", "http://www.testnamespace.com");
        NamespaceStack namespaceStack = new NamespaceStack();
        namespaceStack.push(element);

        // Check if the non-element namespace is not in scope
        assertFalse(namespaceStack.isInScope(nonElementNamespace));
    }

}