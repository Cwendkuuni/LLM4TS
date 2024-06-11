
import org.junit.Test;
import org.junit.Assert;
import java.util.*;
import org.jdom2.*;

public class NamespaceStackTest {

    @Test
    public void push_Element_AddsNamespaceToStack() {
        NamespaceStack nsStack = new NamespaceStack();
        Element element = new Element("test");
        element.setNamespace(Namespace.getNamespace("test", "http://example.com/test"));
        nsStack.push(element);
        Iterator<Namespace> iterator = nsStack.iterator();
        List<Namespace> namespaces = new ArrayList<>();
        while (iterator.hasNext()) {
            namespaces.add(iterator.next());
        }
        Assert.assertTrue(namespaces.contains(Namespace.getNamespace("test", "http://example.com/test")));
    }

    @Test
    public void push_Attribute_AddsNamespaceToStack() {
        NamespaceStack nsStack = new NamespaceStack();
        Attribute attribute = new Attribute("test", "value", Namespace.getNamespace("test", "http://example.com/test"));
        nsStack.push(attribute);
        Iterator<Namespace> iterator = nsStack.iterator();
        List<Namespace> namespaces = new ArrayList<>();
        while (iterator.hasNext()) {
            namespaces.add(iterator.next());
        }
        Assert.assertTrue(namespaces.contains(Namespace.getNamespace("test", "http://example.com/test")));
    }

    @Test(expected = IllegalStateException.class)
    public void pop_EmptyStack_ThrowsIllegalStateException() {
        NamespaceStack nsStack = new NamespaceStack();
        nsStack.pop();
    }

    @Test
    public void addedForward_EmptyStack_ReturnsEmptyIterable() {
        NamespaceStack nsStack = new NamespaceStack();
        Iterator<Namespace> iterator = nsStack.addedForward().iterator();
        Assert.assertFalse(iterator.hasNext());
    }

    @Test
    public void addedReverse_EmptyStack_ReturnsEmptyIterable() {
        NamespaceStack nsStack = new NamespaceStack();
        Iterator<Namespace> iterator = nsStack.addedReverse().iterator();
        Assert.assertFalse(iterator.hasNext());
    }

    @Test
    public void iterator_EmptyStack_ReturnsEmptyIterator() {
        NamespaceStack nsStack = new NamespaceStack();
        Iterator<Namespace> iterator = nsStack.iterator();
        Assert.assertFalse(iterator.hasNext());
    }

    @Test
    public void getScope_EmptyStack_ReturnsEmptyArray() {
        NamespaceStack nsStack = new NamespaceStack();
        Namespace[] scope = nsStack.getScope();
        Assert.assertArrayEquals(new Namespace[0], scope);
    }

    @Test
    public void isInScope_NamespaceInScope_ReturnsTrue() {
        NamespaceStack nsStack = new NamespaceStack();
        nsStack.push(new Element("test"));
        Assert.assertTrue(nsStack.isInScope(Namespace.NO_NAMESPACE));
    }

    @Test
    public void isInScope_NamespaceNotInScope_ReturnsFalse() {
        NamespaceStack nsStack = new NamespaceStack();
        nsStack.push(new Element("test"));
        Assert.assertFalse(nsStack.isInScope(Namespace.getNamespace("test", "http://example.com/test")));
    }

}
